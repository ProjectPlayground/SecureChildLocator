import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
    private Socket socket;
    private String serverName;
    private int port;

    public Client()
    {
        socket = null;
        serverName = "10.0.2.2"; // because of Android Studio
        port = 9000;
    }

    public Client(String serverName, int port)
    {
        socket = null;
        this.serverName = serverName;
        this.port = port;
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

        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            printWriter.println("Hi!");
            printWriter.flush();

            System.out.println(bufferedReader.readLine());

            printWriter.println("stop");
            printWriter.flush();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
                socket.close();
            }
            catch (IOException e) {
                System.exit(1);
            }
        }
    }

    public static void main(String[] args)
    {
        Client client;

        client = new Client("localhost", 9000);
        client.startClient();
    }
}
