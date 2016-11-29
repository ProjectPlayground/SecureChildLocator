package com.locator.child.secure.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import sirs.communication.response.AddUserResponse;
import sirs.communication.response.CreateSessionKeyResponse;
import sirs.communication.response.GetLocationsResponse;
import sirs.communication.response.LoginResponse;
import sirs.communication.response.VerifySessionKeyResponse;

public class Client extends AsyncTask<String, Void, Result>
{
    private Socket socket;
    private String serverName;
    private int port;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Context context;

    public Client(Context c)
    {
        context=c;
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

        Gson gson = new Gson();
        AddUserResponse addUserResponse = gson.fromJson(r.getMessage(), AddUserResponse.class);

        if (addUserResponse.getType().equals("LoginResponse")  ) {
            LoginResponse loginResponse = gson.fromJson(r.getMessage(), LoginResponse.class);

            if (loginResponse.isSuccessful()){
                Intent myIntent = new Intent(context, MainParentActivity.class);
                context.startActivity(myIntent);
                ((Activity)context).finish();
            }
            else
                Toast.makeText(context, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

        }

        else if(addUserResponse.getType().equals("AddUserResponse")){
            if (addUserResponse.isSuccessful()){
                Intent myIntent = new Intent(context, ConfirmRegisterActivity.class);
                context.startActivity(myIntent);
                ((Activity)context).finish();
            }
            else{
                Toast.makeText(context,addUserResponse.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        else if(addUserResponse.getType().equals("CreateSessionKeyResponse")){
            CreateSessionKeyResponse createSessionKeyResponse = gson.fromJson(r.getMessage(), CreateSessionKeyResponse.class);
            LocalMemory m = LocalMemory.getInstance();
            List<Kid> kids = m.getKids();

            if (createSessionKeyResponse.isSuccessful()){
                Kid nKid=null;
                for(int i=0;i<kids.size();i++){
                    if(kids.get(i).getCode().equals("---")){
                        nKid = new Kid(kids.get(i).getName(),kids.get(i).getPass(),createSessionKeyResponse.getSessionKey());
                        m.removeKid(kids.get(i).getName());
                        break;
                    }

                }
                m.addKid(nKid);

                Intent myIntent = new Intent(context, MainParentActivity.class);
                context.startActivity(myIntent);
                ((Activity)context).finish();
            }
            else{
                for(int i=0;i<kids.size();i++){
                    if(kids.get(i).getCode().equals("---")){
                        m.removeKid(kids.get(i).getName());
                        break;
                    }

                }
                Toast.makeText(context,createSessionKeyResponse.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        else if(addUserResponse.getType().equals("VerifySessionKeyResponse")){
            VerifySessionKeyResponse verifySessionKeyResponse = gson.fromJson(r.getMessage(), VerifySessionKeyResponse.class);
            LocalMemory m = LocalMemory.getInstance();
            List<Parent> parents = m.getParents();

            if (verifySessionKeyResponse.isValid()){
                Parent nParent=null;
                for(int i=0;i<parents.size();i++){
                    if(!parents.get(i).isVerified()){
                        nParent = new Parent(parents.get(i).getMail(),parents.get(i).getPass(),parents.get(i).getSharedPass(),parents.get(i).getCode(),true);
                        m.removeParent(parents.get(i).getMail());
                        break;
                    }

                }
                m.addParent(nParent);

                Intent myIntent = new Intent(context, MainKidsActivity.class);
                context.startActivity(myIntent);
                ((Activity)context).finish();
            }
            else{
                for(int i=0;i<parents.size();i++){
                    if(!parents.get(i).isVerified()){
                        m.removeParent(parents.get(i).getMail());
                        break;
                    }

                }
                Toast.makeText(context,verifySessionKeyResponse.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        else if(addUserResponse.getType().equals("GetLocationsResponse")){
            Cryptography cript = new Cryptography();
            LocalMemory m = LocalMemory.getInstance();

            GetLocationsResponse getLocationsResponse = gson.fromJson(r.getMessage(), GetLocationsResponse.class);

            if (getLocationsResponse.isSuccessful()){
                List<String> locations = new ArrayList<>();
                List<String> locationsEnc = getLocationsResponse.getLocation();
                for(String s:locationsEnc)
                    locations.add(cript.desincriptSymetricMessage(s,m.getKidRequestPass()));
                MyLocationsAdapter adapter = new MyLocationsAdapter(locations,context);
                ListView list = (ListView) ((Activity)context).findViewById(R.id.listViewLocations);
                list.setAdapter(adapter);
            }
            else{
                Toast.makeText(context,getLocationsResponse.getMessage(),Toast.LENGTH_LONG).show();
            }

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
        printWriter.println(message);
        printWriter.flush();
        return receive();
    }

    private Result receive()
    {
        try {
            String message = bufferedReader.readLine();

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
