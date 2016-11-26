package com.locator.chield.secure.application;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import sirs.communication.response.AddUserResponse;

public class Client
{
    private Socket socket;
    private String serverName;
    private int port;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Client()
    {
        socket = null;
        serverName = "10.0.2.2"; // because of Android Studio
        port = 9000;
        bufferedReader = null;
        printWriter = null;
    }

    public Client(String serverName, int port)
    {
        socket = null;
        this.serverName = serverName;
        this.port = port;
        bufferedReader = null;
        printWriter = null;
    }

    public void startClient()
    {
        try {
            socket = new Socket(serverName, port);
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host: " + serverName);
        }
        catch (IOException e) {
            System.out.println("Cannot connect to server at " + port + "port.");
        }

        if (socket == null) {
            System.exit(1);
        }

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void send(String message)
    {
        System.out.println("Sending: " + message);
        printWriter.println(message);
        printWriter.flush();

        System.out.println("Sent");

        receive();
    }

    private void receive()
    {
        try {
            String message = bufferedReader.readLine();

            System.out.println("Received: " + message);

            Gson gson = new Gson();
            AddUserResponse addUserResponse = gson.fromJson(message, AddUserResponse.class);

            System.out.println(addUserResponse);
        }
        catch (IOException e) {
            System.out.println("Error reading from socket.");
            System.exit(1);
        }
    }

    public void close()
    {
        printWriter.println("stop");
        printWriter.flush();

        try {
            bufferedReader.close();
            printWriter.close();
            socket.close();
        }
        catch (IOException e) {
            System.exit(1);
        }
    }

}
