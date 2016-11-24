package sirs.server;

import java.io.*;
import java.net.Socket;

class ClientServiceThread extends Thread
{
    private Socket clientSocket;
    private boolean running = true;

    ClientServiceThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;

        System.out.println("Accepted Client Address - " + clientSocket.getInetAddress().getHostName());

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while (running) {
                String clientCommand = bufferedReader.readLine();
                System.out.println("Client says: " + clientCommand);

                if (clientCommand.equalsIgnoreCase("stop")) {
                    running = false;
                    System.out.print("Stopping client thread for client - ");
                }
                else {
                    printWriter.println("Server says: " + clientCommand);
                    printWriter.flush();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
                clientSocket.close();
                System.out.println("Stopped");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
