package com.locator.child.secure.application;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import sirs.communication.request.AddLocationRequest;
import sirs.communication.request.AddUserRequest;
import sirs.communication.request.CreateSessionKeyRequest;
import sirs.communication.request.GetLocationsRequest;
import sirs.communication.request.LoginRequest;
import sirs.communication.request.VerifySessionKeyRequest;

public class Manager {

    public void login(Context context ,String mail,String pass){
        LocalMemory.getInstance().setLoggedUserMail(mail);
        LocalMemory.getInstance().setLoggedUserPass(pass);

        Client client = new Client(context);
        LoginRequest loginRequest = new LoginRequest(mail,pass);
        Gson gson = new Gson();
        String loginRequestString = gson.toJson(loginRequest);
        client.execute(loginRequestString);
    }

    public void register(Context context,String mail,String phone,String pass){
        Client client = new Client(context);
        AddUserRequest addUserRequest = new AddUserRequest(mail,phone,pass);
        Gson gson = new Gson();
        String addUserRequestString = gson.toJson(addUserRequest);
        client.execute(addUserRequestString);
    }

    public Result confirmRegistration(String mail,String pass,String code){
        LocalMemory.getInstance().setLoggedUserMail(mail);
        LocalMemory.getInstance().setLoggedUserPass(pass);

        return new Result(true,null);
    }

    public void addParent(Context context,String mail,String name,String pass,String sharedCode){
        LocalMemory m = LocalMemory.getInstance();

        Client client = new Client(context);
        VerifySessionKeyRequest verifySessionKeyRequest = new VerifySessionKeyRequest(mail,pass,sharedCode);
        Gson gson = new Gson();
        String verifySessionKeyRequestString = gson.toJson(verifySessionKeyRequest);
        client.execute(verifySessionKeyRequestString);

        m.addParent(new Parent(mail,name,pass,sharedCode,false));
    }

    public void addKid(Context context,String name,String pass){
        LocalMemory m = LocalMemory.getInstance();

        Client client = new Client(context);
        CreateSessionKeyRequest createSessionKeyRequest = new CreateSessionKeyRequest(m.getLoggedUserMail(),m.getLoggedUserPass());
        Gson gson = new Gson();
        String createSessionKeyRequestString = gson.toJson(createSessionKeyRequest);
        client.execute(createSessionKeyRequestString);

        m.addKid(new Kid(name,pass,"---"));
    }

    public List<Parent> loadParents(){
        //loads parents from a file
        return new ArrayList<Parent>();
    }

    public List<Kid> loadKids(){
        //loads kids from a file
        return new ArrayList<Kid>();
    }

    public void saveParents(){

    }

    public void saveKids(){

    }

    public void addLocation(Context context,double lat, double lon){
        Client client = new Client(context);

        for (Parent p : LocalMemory.getInstance().getParents()){
            AddLocationRequest addLocationRequest = new AddLocationRequest(p.getCode(), p.getMail(), p.getPass(), ""+lat+";"+lon+";xxx");
            Gson gson = new Gson();
            String addLocationRequestString = gson.toJson(addLocationRequest);

            client.execute(addLocationRequestString);
        }


    }

    public void getLocations(Context context,String code){
        LocalMemory m = LocalMemory.getInstance();

        Client client = new Client(context);
        GetLocationsRequest getLocationsRequest = new GetLocationsRequest(m.getLoggedUserMail(),m.getLoggedUserPass(),code);
        Gson gson = new Gson();
        String getLocationsRequestString = gson.toJson(getLocationsRequest);
        client.execute(getLocationsRequestString);

    }
}
