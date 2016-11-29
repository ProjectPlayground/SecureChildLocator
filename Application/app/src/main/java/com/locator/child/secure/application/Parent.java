package com.locator.child.secure.application;

import java.io.Serializable;

/**
 * Created by Valentyn on 12-11-2016.
 */

public class Parent implements Serializable {
    String mail;
    String pass;
    String sharedPass;
    String code;
    boolean verified;

    public Parent(String mail, String pass,String sharedPass,String code,boolean verified){
        this.mail=mail;
        this.pass=pass;
        this.sharedPass=sharedPass;
        this.code=code;
        this.verified=verified;
    }

    public String getMail(){
        return mail;
    }

    public String getPass(){
        return pass;
    }

    public String getSharedPass(){
        return sharedPass;
    }

    public String getCode(){
        return code;
    }
    public boolean isVerified(){
        return verified;
    }

}
