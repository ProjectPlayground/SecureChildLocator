package com.locator.chield.secure.application;

/**
 * Created by Valentyn on 12-11-2016.
 */

public class Parent {
    String mail;
    String name;
    String pass;
    String code;

    public Parent(String mail, String name, String pass,String code){
        this.mail=mail;
        this.name=name;
        this.pass=pass;
        this.code=code;
    }

    public String getMail(){
        return mail;
    }

    public String getName(){
        return name;
    }

    public String getPass(){
        return pass;
    }

    public String getCode(){
        return code;
    }

}
