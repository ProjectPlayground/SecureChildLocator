package com.locator.child.secure.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.binary.Base64;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import sirs.communication.response.*;
import sirs.communication.request.*;

public class Client extends AsyncTask<String, Void, Result>
{
    private Socket socket;
    private String serverName;
    private int port;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Context context;
    private SecretKey secretKey;
    private Cryptography cryptography;
    private Gson gson;

    public Client(Context c)
    {
        context=c;
        socket = null;
        serverName = "10.0.2.2"; // because of Android Studio
        port = 9000;
        bufferedReader = null;
        printWriter = null;
        secretKey = null;
        cryptography = new Cryptography(context);
        gson = Converters.registerDateTime(new GsonBuilder().disableHtmlEscaping()).create();
    }

    public Client(String serverName, int port)
    {
        socket = null;
        this.serverName = serverName;
        this.port = port;
        bufferedReader = null;
        printWriter = null;
        secretKey = null;
        cryptography = new Cryptography(context);
        gson = Converters.registerDateTime(new GsonBuilder().disableHtmlEscaping()).create();
    }

    public boolean wellInicialized(){
        return socket!=null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Result doInBackground(String... params) {
        Result r1 = startClient();
        if(r1.getResult()){
            Result r2 = send(params[0]);
            close();
            return r2;
        }
        return r1;
    }

    @Override
    protected void onPostExecute(Result r) {
        super.onPostExecute(r);
        if(r.getResult()==false)
            Toast.makeText(context, r.getMessage(), Toast.LENGTH_SHORT).show();

        try {
            AddUserResponse addUserResponse = gson.fromJson(r.getMessage(), AddUserResponse.class);

            if (addUserResponse.getType().equals("LoginResponse")) {
                LoginResponse loginResponse = gson.fromJson(r.getMessage(), LoginResponse.class);

                if (loginResponse.isSuccessful()) {
                    Intent myIntent = new Intent(context, MainParentActivity.class);
                    context.startActivity(myIntent);
                    ((Activity) context).finish();
                } else
                    Toast.makeText(context, loginResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();

            } else if (addUserResponse.getType().equals("AddUserResponse")) {
                if (addUserResponse.isSuccessful()) {
                    Intent myIntent = new Intent(context, ConfirmRegisterActivity.class);
                    context.startActivity(myIntent);
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, addUserResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (addUserResponse.getType().equals("CreateSessionKeyResponse")) {
                CreateSessionKeyResponse createSessionKeyResponse = gson.fromJson(r.getMessage(), CreateSessionKeyResponse.class);
                LocalMemory m = LocalMemory.getInstance();
                List<Kid> kids = m.getKids();

                if (createSessionKeyResponse.isSuccessful()) {
                    Kid nKid = null;
                    for (int i = 0; i < kids.size(); i++) {
                        if (kids.get(i).getCode().equals("---")) {
                            nKid = new Kid(kids.get(i).getName(), kids.get(i).getPass(), createSessionKeyResponse.getSessionKey());
                            m.removeKid(kids.get(i).getName());
                            break;
                        }

                    }
                    m.addKid(nKid);

                    Intent myIntent = new Intent(context, MainParentActivity.class);
                    context.startActivity(myIntent);
                    ((Activity) context).finish();
                } else {
                    for (int i = 0; i < kids.size(); i++) {
                        if (kids.get(i).getCode().equals("---")) {
                            m.removeKid(kids.get(i).getName());
                            break;
                        }

                    }
                    Toast.makeText(context, createSessionKeyResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (addUserResponse.getType().equals("VerifySessionKeyResponse")) {
                VerifySessionKeyResponse verifySessionKeyResponse = gson.fromJson(r.getMessage(), VerifySessionKeyResponse.class);
                LocalMemory m = LocalMemory.getInstance();
                List<Parent> parents = m.getParents();

                if (verifySessionKeyResponse.isValid()) {
                    Parent nParent = null;
                    for (int i = 0; i < parents.size(); i++) {
                        if (!parents.get(i).isVerified()) {
                            nParent = new Parent(parents.get(i).getMail(), parents.get(i).getPass(), parents.get(i).getSharedPass(), parents.get(i).getCode(), true);
                            m.removeParent(parents.get(i).getMail());
                            break;
                        }

                    }
                    m.addParent(nParent);

                    Intent myIntent = new Intent(context, MainKidsActivity.class);
                    context.startActivity(myIntent);
                    ((Activity) context).finish();
                } else {
                    for (int i = 0; i < parents.size(); i++) {
                        if (!parents.get(i).isVerified()) {
                            m.removeParent(parents.get(i).getMail());
                            break;
                        }

                    }
                    Toast.makeText(context, verifySessionKeyResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (addUserResponse.getType().equals("GetLocationsResponse")) {
                Cryptography cript = new Cryptography(context);
                LocalMemory m = LocalMemory.getInstance();

                GetLocationsResponse getLocationsResponse = gson.fromJson(r.getMessage(), GetLocationsResponse.class);

                if (getLocationsResponse.isSuccessful()) {
                    List<String> locations = new ArrayList<>();
                    List<String> locationsEnc = getLocationsResponse.getLocation();
                    for (String s : locationsEnc)
                        locations.add(cript.decryptWithPassword(s, m.getKidRequestPass()));
                    MyLocationsAdapter adapter = new MyLocationsAdapter(locations, context);
                    ListView list = (ListView) ((Activity) context).findViewById(R.id.listViewLocations);
                    list.setAdapter(adapter);
                } else {
                    Toast.makeText(context, getLocationsResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
        catch (com.google.gson.JsonSyntaxException e) {
            System.out.println("Result contains an unsuccessful request - meaning, without json");
        }
    }

    private Result startClient()
    {
        try {
            socket = new Socket(serverName, port);
        }
        catch (UnknownHostException e) {
            socket=null;
            return new Result(false,"Unknown host: " + serverName);
        }
        catch (IOException e) {
            socket=null;
            return new Result(false,"Cannot connect to server at " + port + "port.");
        }
        catch(Exception e){
            return new Result(false,e.getMessage());
        }

        if (socket != null) {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            catch (IOException e) {
                socket=null;
                return new Result(false,e.getMessage());
            }
            catch (Exception e){
                return new Result(false,e.getMessage());
            }
        }
        return new Result(true,"");
    }

    private Result send(String message)
    {
        try {
            secretKey = cryptography.generateKey();
            byte[] utf8 = secretKey.getEncoded();
            String secretKeyString = new String(Base64.encodeBase64(utf8), "UTF-8");
            String encryptedSecretKey = cryptography.encryptRSA(secretKeyString);

            System.out.println("secretkeystring: " + secretKeyString);
            System.out.println("encrypted secret key: " + encryptedSecretKey);

            String messageHash = cryptography.hash(message);

            System.out.println("hashed the message");

            DateTime dateTime = new DateTime();
            String dateTimeHash = cryptography.hash(dateTime.toString());

            System.out.println("hashed the time");

            CipheredRequest cipheredRequest = new CipheredRequest(message, dateTime, messageHash, dateTimeHash);
            String cipheredRequestJson = gson.toJson(cipheredRequest);
            System.out.println("cipheredrequestjson: " + cipheredRequestJson);

            String encryptedRequest = cryptography.encryptAES(cipheredRequestJson, secretKey);
            System.out.println("encrypted request: " + encryptedRequest);

            FinalRequest finalRequest = new FinalRequest(encryptedRequest, encryptedSecretKey);
            String finalRequestJson = gson.toJson(finalRequest);

            System.out.println("final request json: " + finalRequestJson);

            printWriter.println(finalRequestJson);
            printWriter.flush();

            return receive();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Result receive()
    {
        System.out.println("Receive!!!");
        try {
            String request = bufferedReader.readLine();

            System.out.println("request: " + request);

            CipheredResponse cipheredResponse = gson.fromJson(request, CipheredResponse.class);

            System.out.println("ciphered response: " + cipheredResponse);

            String originalMessage = cipheredResponse.getMessage();
            String message = cryptography.decryptAES(originalMessage, secretKey);

            System.out.println("message: " + message);

            String messageHash = cipheredResponse.getMessageHash();
            String dateTimeHash = cipheredResponse.getTimestampHash();
            DateTime dateTime = cipheredResponse.getDateTime();
            DateTime now = new DateTime();

            System.out.println("messageHash: " + messageHash);
            System.out.println("timehash: " + dateTimeHash);

            if (!cryptography.hashIsValid(originalMessage, messageHash)) {
                System.out.println("Invalid message hash");
                return new Result(false, "Invalid message: it was tampered with.");
            }
            if (!cryptography.hashIsValid(dateTime.toString(), dateTimeHash)) {
                System.out.println("Invalid time hash");
                return new Result(false, "Invalid message: it was tampered with.");
            }

            dateTime = dateTime.plusSeconds(20); // if message is more than twenty seconds, discard
            if (now.isAfter(dateTime)) {
                System.out.println("Response expired");
                return new Result(false, "Request is no longer valid: it has expired.");
            }

            return new Result(true,message);
        }
        catch (IOException e) {
            return new Result(false,"Error reading from socket.");
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

}
