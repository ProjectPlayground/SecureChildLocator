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
}
