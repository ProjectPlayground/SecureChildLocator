package com.locator.child.secure.application;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalMemory {

    private Manager manager = new Manager();
    private static LocalMemory instance;
    private List<Parent> parents;
    private Map<String, List<Kid>> kids = new HashMap<String, List<Kid>>();
    private Runnable runnableGPS = null;
    private Handler handlerGPS = null;

    private String loggedUserMail;
    private String loggedUserPassword;
    private String kidRequestPass;
    private String sessionKey;

    public LocalMemory(){

    }

    public void setLoggedUserMail(String m){
        loggedUserMail=m;
    }

    public String getLoggedUserMail(){
        return loggedUserMail;
    }

    public String getKidRequestPass(){
        return kidRequestPass;
    }

    public void setKidRequestPass(String r){
        kidRequestPass=r;
    }

    public void setLoggedUserPass(String p){
        loggedUserPassword=p;
    }

    public String getLoggedUserPass(){
        return loggedUserPassword;
    }

    public Runnable getRunnableGPS(){
        return runnableGPS;
    }

    public void setRunnableGPS(Runnable r){
        runnableGPS=r;
    }

    public Handler getHandlerGPS(){
        return handlerGPS;
    }

    public void setHandlerGPS(Handler h){
        handlerGPS=h;
    }

    public String getSessionKey()
    {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey)
    {
        this.sessionKey = sessionKey;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void addParent(Parent p){
        for(int i=0;i<parents.size();i++){
            if(parents.get(i).getMail().equals(p.getMail()))
                return;
        }
        parents.add(p);
    }

    public void loadParents(List<Parent> l){
        parents=l;
    }

    public void loadKids(Map<String,List<Kid>> dic){
        kids=dic;
    }

    public void removeParent(String mail){
        for(int i=0;i<parents.size();i++){
            if(parents.get(i).getMail().equals(mail))
                parents.remove(i);
        }
    }

    public List<Kid> getKids() {
        if(!kids.containsKey(loggedUserMail)){
            List<Kid> l = new ArrayList<>();
            kids.put(loggedUserMail,l);
        }
        return kids.get(loggedUserMail);
    }

    public Map<String,List<Kid>> getAllKids() {
        return kids;
    }

    public void addKid(Kid k){
        for(int i=0;i<kids.get(loggedUserMail).size();i++){
            if(kids.get(loggedUserMail).get(i).getName().equals(k.getName()))
                return;
        }
        List<Kid> l = kids.get(loggedUserMail);
        l.add(k);
    }

    public void removeKid(String name){
        for(int i=0;i<kids.get(loggedUserMail).size();i++){
            if(kids.get(loggedUserMail).get(i).getName().equals(name))
                kids.get(loggedUserMail).remove(i);
        }
    }

    public Manager getManager(){
        return manager;
    }



    public static synchronized LocalMemory getInstance(){
        if(instance==null)
            instance = new LocalMemory();
        return instance;
    }
    }