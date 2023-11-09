Ivashechenko Ivan

Server console application for communicating with the client via TCP/IP. Implement the following functions in the application:
1. Input client text is edited by some criteria. 
2. Commands can now be read from a file and shared with all clients.
3. Events can be scheduled and sent to all clients.

LogFile.txt and Commands.txt located at the ..\out\production folder.

Should be improved:
CommandReader should function like a database, allowing us to read data as if from a real database, this class should 
be improved and refactored. And the MessageLogger should be more general for better usability for any classes or better
using java.util.logging.
