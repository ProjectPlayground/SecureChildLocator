package sirs.server;

import sirs.server.database.Database;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Server
{
    private int port;
    private ServerSocket serverSocket;
    private Database database;
    private Cryptography cryptography;

    public Server()
    {
        this.port = 9000;
        this.database = new Database();
        this.cryptography = new Cryptography();

        startServer();
    }

    public Server(int port)
    {
        this.port = port;
        this.database = new Database();
        startServer();
    }

    private void startServer()
    {
        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("Error creating the server on port " + port + ". Exiting.");
            System.exit(1);
        }

        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        System.out.println("Server started at : " + formatter.format(now.getTime()));

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientServiceThread clientThread = new ClientServiceThread(clientSocket, database, cryptography);
                clientThread.start();
            }
            catch (IOException e) {
                System.out.println("Error accepting client.");
                e.printStackTrace();
            }
        }
    }
}