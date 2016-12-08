package sirs.server;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import sirs.communication.request.*;
import sirs.communication.response.*;
import sirs.server.database.Database;
import sirs.server.database.UserAlreadyExistsException;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class ClientServiceThread extends Thread
{
    private boolean running;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Database database;
    private Cryptography cryptography;
    private Gson gson;
    private SecretKey secretKey;

    ClientServiceThread(Socket clientSocket, Database database, Cryptography cryptography)
    {
        this.running = true;
        this.clientSocket = clientSocket;
        this.bufferedReader = null;
        this.printWriter = null;
        this.database = database;
        this.cryptography = cryptography;
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
        this.secretKey = null;
    }

    @Override
    public void run()
    {
        System.out.println("Accepted Client Address - " + clientSocket.getInetAddress().getHostName());

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while (running) {
                receive();
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

    private void receive()
    {
        try {
            String message = bufferedReader.readLine();

            if (message.equalsIgnoreCase("stop")) {
                System.out.println("End of request.");
                running = false;
            }
            else {
                System.out.println("Received a request");
                doFinalRequest(message);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void send(String response)
    {
        String encryptedMessage = cryptography.encryptAES(response, secretKey);
        String messageHash = cryptography.hash(encryptedMessage);
        DateTime dateTime = new DateTime();
        String timeHash = cryptography.hash(dateTime.toString());

        Gson gson2 = Converters.registerDateTime(new GsonBuilder().disableHtmlEscaping()).create();

        CipheredResponse cipheredResponse = new CipheredResponse(encryptedMessage, dateTime, messageHash, timeHash);
        String cipheredResponseJson = gson2.toJson(cipheredResponse);

        printWriter.println(cipheredResponseJson);
        printWriter.flush();
    }

    private void doFinalRequest(String request)
    {
        FinalRequest finalRequest = gson.fromJson(request, FinalRequest.class);
        String key = finalRequest.getKey();
        String message = finalRequest.getMessage();

        doCipheredRequest(key, message);
    }

    private void doCipheredRequest(String key, String request)
    {
        try {
            // First we decrypt the key with our private key
            String decryptedKey = cryptography.decryptRSA(key);
            secretKey = cryptography.getKey(decryptedKey);

            // Now we use the decrypted key to decrypt the content of our request
            String cipheredRequestJson = cryptography.decryptAES(request, secretKey);

            Gson gson2 = Converters.registerDateTime(new GsonBuilder()).create();

            CipheredRequest cipheredRequest = gson2.fromJson(cipheredRequestJson, CipheredRequest.class);

            if (!cryptography.hashIsValid(cipheredRequest.getMessage(), cipheredRequest.getMessageHash())) {
                // do nothing, message is not valid
                System.out.println("Message hash is not valid");
                return;
            }
            if (!cryptography.hashIsValid(cipheredRequest.getDateTime().toString(), cipheredRequest.getDateTimeHash())) {
                // do nothing, message is not valid
                System.out.println("Date time hash is not valid");
                return;
            }

            // but what if the timestamp is old?
            DateTime now = new DateTime();
            DateTime requestTimestamp = cipheredRequest.getDateTime();
            requestTimestamp = requestTimestamp.plusSeconds(20); // if message is more than twenty seconds, discard as protection to replay attacks

            if (now.isAfter(requestTimestamp)) {
                // do nothing, expired request
                System.out.println("Message has expired");
                return;
            }

            // message is valid! let's continue
            doRequest(cipheredRequest.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doRequest(String request)
    {
        AddUserRequest addUserRequest = gson.fromJson(request, AddUserRequest.class);
        String type = addUserRequest.getType();

        if (type.equals("AddUserRequest")) {
            doAddUserRequest(addUserRequest);
        }
        else if (type.equals("AddLocationRequest")) {
            AddLocationRequest addLocationRequest = gson.fromJson(request, AddLocationRequest.class);
            doAddLocationRequest(addLocationRequest);
        }
        else if (type.equals("ConfirmUserRequest")) {
            ConfirmUserRequest confirmUserRequest = gson.fromJson(request, ConfirmUserRequest.class);
            doConfirmUserRequest(confirmUserRequest);
        }
        else if (type.equals("CreateSessionKeyRequest")) {
            CreateSessionKeyRequest createSessionKeyRequest = gson.fromJson(request, CreateSessionKeyRequest.class);
            doCreateSessionKeyRequest(createSessionKeyRequest);
        }
        else if (type.equals("GetLatestLocationRequest")) {
            GetLatestLocationRequest getLatestLocationRequest = gson.fromJson(request, GetLatestLocationRequest.class);
            doLatestLocationRequest(getLatestLocationRequest);
        }
        else if (type.equals("GetLocationsRequest")) {
            GetLocationsRequest getLocationsRequest = gson.fromJson(request, GetLocationsRequest.class);
            doGetLocations(getLocationsRequest);
        }
        else if (type.equals("LoginRequest")) {
            LoginRequest loginRequest = gson.fromJson(request, LoginRequest.class);
            doLoginRequest(loginRequest);
        }
        else if (type.equals("RemoveUserRequest")) {
            RemoveUserRequest removeUserRequest = gson.fromJson(request, RemoveUserRequest.class);
            doRemoveUserRequest(removeUserRequest);
        }
        else if (type.equals("VerifySessionKeyRequest")) {
            VerifySessionKeyRequest verifySessionKeyRequest = gson.fromJson(request, VerifySessionKeyRequest.class);
            doVerifySessionKeyRequest(verifySessionKeyRequest);
        }
        else {
            System.out.println("Should not happen...");
            System.out.println("Unknown type: " + type);
        }
    }

    private void doAddUserRequest(AddUserRequest addUserRequest)
    {
        synchronized (database) {
            try {
                database.addUser(addUserRequest.getEmail(),
                        addUserRequest.getPhoneNumber(),
                        addUserRequest.getPassword());
                AddUserResponse addUserResponse = new AddUserResponse(true);
                String response = gson.toJson(addUserResponse);
                send(response);
            }
            catch (UserAlreadyExistsException e) {
                AddUserResponse addUserResponse = new AddUserResponse(false);
                addUserResponse.setError(e.getClass().getSimpleName());
                addUserResponse.setErrorMessage(e.getMessage());
                String response = gson.toJson(addUserResponse);
                send(response);
            }
        }
    }

    private void doAddLocationRequest(AddLocationRequest addLocationRequest)
    {
        synchronized (database) {
            try {
                database.addLocation(addLocationRequest.getSessionKey(),
                        addLocationRequest.getEmail(),
                        /*addLocationRequest.getPassword(),*/
                        addLocationRequest.getLocation());
                AddLocationResponse addLocationResponse = new AddLocationResponse(true);
                String response = gson.toJson(addLocationResponse);
                send(response);
            }
            catch (Exception e) {
                AddLocationResponse addLocationResponse = new AddLocationResponse(false);
                addLocationResponse.setError(e.getClass().getSimpleName());
                addLocationResponse.setErrorMessage(e.getMessage());
                String response = gson.toJson(addLocationResponse);
                send(response);
            }
        }
    }

    private void doConfirmUserRequest(ConfirmUserRequest confirmUserRequest)
    {
        synchronized (database) {
            // not done yet
            ConfirmUserResponse confirmUserResponse = new ConfirmUserResponse(true);
            String response = gson.toJson(confirmUserResponse);
            send(response);
        }
    }

    private void doCreateSessionKeyRequest(CreateSessionKeyRequest createSessionKeyRequest)
    {
        synchronized (database) {
            try {
                String sessionKey = database.createSessionKey(createSessionKeyRequest.getEmail());
                CreateSessionKeyResponse createSessionKeyResponse = new CreateSessionKeyResponse(sessionKey);
                String response = gson.toJson(createSessionKeyResponse);
                send(response);
            }
            catch (Exception e) {
                CreateSessionKeyResponse createSessionKeyResponse = new CreateSessionKeyResponse(false);
                createSessionKeyResponse.setError(e.getClass().getSimpleName());
                createSessionKeyResponse.setErrorMessage(e.getMessage());
                String response = gson.toJson(createSessionKeyResponse);
                send(response);
            }
        }
    }

    private void doLatestLocationRequest(GetLatestLocationRequest getLatestLocationRequest)
    {
        synchronized (database) {
            try {
                Map.Entry<Date, String> locationMap = database.getLatestLocation(getLatestLocationRequest.getEmail(),
                        getLatestLocationRequest.getPassword(),
                        getLatestLocationRequest.getSessionKey());
                GetLatestLocationResponse getLatestLocationResponse = new GetLatestLocationResponse(locationMap.getValue());
                String response = gson.toJson(getLatestLocationResponse);
                send(response);
            }
            catch (Exception e) {
                GetLatestLocationResponse getLatestLocationResponse = new GetLatestLocationResponse(false);
                getLatestLocationResponse.setError(e.getClass().getSimpleName());
                getLatestLocationResponse.setErrorMessage(e.getMessage());
                String response = gson.toJson(getLatestLocationResponse);
                send(response);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void doGetLocations(GetLocationsRequest getLocationsRequest)
    {
        synchronized (database) {
            try {
                Map<Date, String> locationsMap = database.get30Locations(getLocationsRequest.getEmail(),
                        getLocationsRequest.getSessionKey());
                List<String> locations = (List<String>) new ArrayList(locationsMap.values());
                GetLocationsResponse getLocationsResponse = new GetLocationsResponse(locations);
                String response = gson.toJson(getLocationsResponse);
                send(response);
            }
            catch (Exception e) {
                GetLocationsResponse getLocationsResponse = new GetLocationsResponse(false);
                getLocationsResponse.setError(e.getClass().getSimpleName());
                getLocationsResponse.setErrorMessage(e.getMessage());
                String response = gson.toJson(getLocationsResponse);
                send(response);
            }
        }
    }

    private void doLoginRequest(LoginRequest loginRequest)
    {
        try {
            database.login(loginRequest.getEmail(), loginRequest.getPassword());
            LoginResponse loginResponse = new LoginResponse(true);
            String response = gson.toJson(loginResponse);
            send(response);
        }
        catch (Exception e) {
            LoginResponse loginResponse = new LoginResponse(false);
            loginResponse.setError(e.getClass().getSimpleName());
            loginResponse.setErrorMessage(e.getMessage());
            String response = gson.toJson(loginResponse);
            send(response);
        }
    }

    private void doRemoveUserRequest(RemoveUserRequest removeUserRequest)
    {
        synchronized (database) {
            try {
                database.removeUser(removeUserRequest.getEmail(), removeUserRequest.getPassword());
                RemoveUserResponse removeUserResponse = new RemoveUserResponse(true);
                String response = gson.toJson(removeUserResponse);
                send(response);
            }
            catch (Exception e) {
                RemoveUserResponse removeUserResponse = new RemoveUserResponse(false);
                removeUserResponse.setError(e.getClass().getSimpleName());
                removeUserResponse.setErrorMessage(e.getMessage());
                String response = gson.toJson(removeUserResponse);
                send(response);
            }
        }
    }

    private void doVerifySessionKeyRequest(VerifySessionKeyRequest verifySessionKeyRequest)
    {
        synchronized (database) {
            try {
                database.verifySessionKey(verifySessionKeyRequest.getEmail(),
                        verifySessionKeyRequest.getSessionKey());
                VerifySessionKeyResponse verifySessionKeyResponse = new VerifySessionKeyResponse(true);
                String response = gson.toJson(verifySessionKeyResponse);
                send(response);
            }
            catch (Exception e) {
                VerifySessionKeyResponse verifySessionKeyResponse = new VerifySessionKeyResponse(false);
                verifySessionKeyResponse.setError(e.getClass().getSimpleName());
                verifySessionKeyResponse.setErrorMessage(e.getMessage());
                String response = gson.toJson(verifySessionKeyResponse);
                send(response);
            }
        }
    }
}
