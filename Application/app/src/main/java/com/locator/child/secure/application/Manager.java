package com.locator.child.secure.application;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        for(Parent p:m.getParents()){
            if(p.getMail().equals(mail)) {
                Toast.makeText(context, "The mail is already in use.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Client client = new Client(context);
        /*
         * SESSION KEY!!!!!!!!!!!!! FIX THIS
         */
        VerifySessionKeyRequest verifySessionKeyRequest = new VerifySessionKeyRequest(mail, "30pago8oace7lfg54fdu");
        Gson gson = new Gson();
        String verifySessionKeyRequestString = gson.toJson(verifySessionKeyRequest);
        client.execute(verifySessionKeyRequestString);

        m.addParent(new Parent(mail,name,pass,sharedCode,false));
    }

    public void addKid(Context context,String name,String pass){
        LocalMemory m = LocalMemory.getInstance();
        for(Kid k:m.getKids()){
            if(k.getName().equals(name)) {
                Toast.makeText(context, "The name is already in use.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Client client = new Client(context);
        CreateSessionKeyRequest createSessionKeyRequest = new CreateSessionKeyRequest(m.getLoggedUserMail());
        Gson gson = new Gson();
        String createSessionKeyRequestString = gson.toJson(createSessionKeyRequest);
        client.execute(createSessionKeyRequestString);

        m.addKid(new Kid(name,pass,"---"));
    }


    public void loadKids(Context context){
        Map<String,List<Kid>> kids = new HashMap<String, List<Kid>>();
        FileInputStream fin = null;
        ObjectInputStream ois = null;

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions((Activity)context, PERMISSIONS_STORAGE, 1);
        }

        try {

            fin = context.openFileInput("kids.txt");
            ois = new ObjectInputStream(fin);
            kids = (Map<String,List<Kid>>) ois.readObject();


        } catch (Exception ex) {
            ex.printStackTrace();
            kids = new HashMap<String, List<Kid>>();
        } finally {

            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        LocalMemory.getInstance().loadKids(kids);
    }

    public void loadParents(Context context){
        List<Parent> parents = new ArrayList<>();
        FileInputStream fin = null;
        ObjectInputStream ois = null;

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions((Activity)context, PERMISSIONS_STORAGE, 1);
        }

        try {

            fin = context.openFileInput("parents.txt");
            ois = new ObjectInputStream(fin);
            parents = (List<Parent>) ois.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
            parents = new ArrayList<>();
        } finally {

            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        LocalMemory.getInstance().loadParents(parents);
    }

    public void saveParents(Context context){
        List<Parent> parents = LocalMemory.getInstance().getParents();
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions((Activity)context, PERMISSIONS_STORAGE, 1);
        }

        try{
            fout = context.openFileOutput("parents.txt", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(parents);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void saveKids(Context context){
        Map<String,List<Kid>> kids = LocalMemory.getInstance().getAllKids();
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;

        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions((Activity)context, PERMISSIONS_STORAGE, 1);
        }

        try{
            fout = context.openFileOutput("kids.txt", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(kids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void addLocation(Context context,Parent p,double lat, double lon){
        Client client = new Client(context);
        Cryptography cript = new Cryptography(context);

        String time = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a zzz").format(Calendar.getInstance().getTime());
        String location = cript.encryptWithPassword(""+lat+";"+lon+";\n"+time,p.getSharedPass());
        AddLocationRequest addLocationRequest = new AddLocationRequest(p.getCode(), p.getMail(), p.getPass(), location);
        Gson gson = new Gson();
        String addLocationRequestString = gson.toJson(addLocationRequest);

        client.execute(addLocationRequestString);

    }

    public void getLocations(Context context,String code,String pass){
        LocalMemory m = LocalMemory.getInstance();
        m.setKidRequestPass(pass);

        Client client = new Client(context);
        /*
         * SESSION KEY!!!!!!!!!!!!! FIX THIS
         */
        GetLocationsRequest getLocationsRequest = new GetLocationsRequest(m.getLoggedUserMail(),m.getLoggedUserPass(), "hello");
        Gson gson = new Gson();
        String getLocationsRequestString = gson.toJson(getLocationsRequest);
        client.execute(getLocationsRequestString);

    }
}
