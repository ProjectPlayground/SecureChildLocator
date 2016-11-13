package com.locator.chield.secure.application;

import java.util.ArrayList;
import java.util.List;

public class LocalMemory {

    private static LocalMemory instance;
    private List<Parent> parents = new ArrayList<>();
    private List<Kid> kids = new ArrayList<>();

    public LocalMemory(){
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void addParent(Parent p){
        for(int i=0;i<parents.size();i++){
            if(parents.get(i).equals(p.getName()))
                return;
        }
        parents.add(p);
    }

    public void removeParent(String mail){
        for(int i=0;i<parents.size();i++){
            if(parents.get(i).getMail().equals(mail))
                parents.remove(i);
        }
    }

    public List<Kid> getKids() {
        return kids;
    }

    public void addKid(Kid k){
        for(int i=0;i<kids.size();i++){
            if(kids.get(i).equals(k.getName()))
                return;
        }
        kids.add(k);
    }

    public void removeKid(String name){
        for(int i=0;i<kids.size();i++){
            if(kids.get(i).getName().equals(name))
                kids.remove(i);
        }
    }



    public static synchronized LocalMemory getInstance(){
        if(instance==null)
            instance = new LocalMemory();
        return instance;
    }
    }