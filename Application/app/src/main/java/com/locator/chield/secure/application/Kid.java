package com.locator.chield.secure.application;

/**
 * Created by Valentyn on 12-11-2016.
 */

public class Kid {
    String name;
    String pass;

    public Kid(String name, String pass){
        this.name=name;
        this.pass=pass;
    }

    public String getName(){
        return name;
    }

    public String getPass(){
        return pass;
    }
}
