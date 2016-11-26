package com.locator.chield.secure.application;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    public Result login(String mail,String pass){
        return new Result(true,null);
    }

    public Result register(String mail,String pass){
        return new Result(false,"Mensagem de Erro");
    }

    public Result confirmRegistration(String mail,String pass,String code){
        return new Result(true,null);
    }

    public Result addParent(String mail,String name,String pass){
        LocalMemory m = LocalMemory.getInstance();
        m.addParent(new Parent(mail,name,pass,"Xy383892"));
        saveParents();
        return new Result(true,null);
    }

    public Result addKid(String name,String pass){
        LocalMemory m = LocalMemory.getInstance();
        m.addKid(new Kid(name,pass,"Xy383892"));
        saveKids();
        return new Result(true,null);
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

    public void addLocation(double latitude, double longitude){

    }

    public List<String> getLocations(String personName){
        List<String> l = new ArrayList<>();
        l.add("38.703809;-9.182701;17-01-2015");
        l.add("0;0;17-02-2015");
        l.add("38.703809;-9.182701;17-03-2015");
        l.add("0;0;17-02-2015");
        l.add("0;0;17-02-2015");
        l.add("0;0;17-02-2015");
        l.add("0;0;17-02-2015");

        return l;
    }
}
