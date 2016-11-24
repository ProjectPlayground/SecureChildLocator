import com.google.gson.Gson;
import sirs.communication.request.AddLocationRequest;
import sirs.communication.response.AddUserResponse;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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

    private void startClient()
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

    private void send(String message)
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

    private void close()
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

    public static void main(String[] args)
    {
        Client client;

        client = new Client("localhost", 9000);
        client.startClient();

        AddLocationRequest addLocationRequest = new AddLocationRequest("session", "h@g.c", "qwerty", "location");
        Gson gson = new Gson();
        String addLocationRequestString = gson.toJson(addLocationRequest);

        client.send(addLocationRequestString);
        client.close();
    }
}
