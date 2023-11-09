package server;

import controller.CommandReader;
import controller.MessageLogger;
import controller.StringHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {

    private final List<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService pool;
    private final CommandReader commandReader;

    public Server() {
        connections = new LinkedList<>();
        done = false;
        commandReader = new CommandReader();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(444);
            pool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                pool.execute(handler);
            }
        } catch (Exception e) {
            shutdown();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler connection : connections) {
            if (connection != null) {
                connection.sendMessage(message);
            }
        }
    }

    public void shutdown() {
        try {
            done = true;
            pool.shutdown();
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler connection : connections) {
                connection.shutdown();
            }
        } catch (IOException e) {
            // TODO: should be handle
        }
    }

    class ConnectionHandler implements Runnable {

        private final Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private int count;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(() -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    count++;
                    MessageLogger.logMessage("Counter " + count + ", Time " + dateFormat.format(new Date()));
                    sendMessage("Counter " + count + ", Time " + dateFormat.format(new Date()));
                }, 0, 10, TimeUnit.SECONDS);

                String receivedMessage;
                while ((receivedMessage = in.readLine()) != null) {
                    if ("quit".equals(receivedMessage)) {
                        shutdown();
                        return;
                    }
                    String sentMessage;
                    if (commandReader.isCommand(receivedMessage)) {
                        sentMessage = commandReader.getCommandText(receivedMessage);
                    } else {
                        sentMessage = StringHelper.modifyText(receivedMessage);
                    }

                    MessageLogger.logMessage(receivedMessage + " - " + sentMessage);

                    broadcast("Server answer: " + sentMessage);
                }
            } catch (IOException e) {
                shutdown();
            } finally {
                MessageLogger.saveLoggedMessage();
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
                connections.remove(this);
            } catch (IOException e) {
                //TODO: should be handle
            }
        }
    }
}
