package com.locator.chield.secure.application;

/**
 * Created by Valentyn on 12-11-2016.
 */

public class Kid {
    String name;
    String pass;
    String code;

    public Kid(String name, String pass,String code){
        this.name=name;
        this.pass=pass;
        this.code=code;
    }

    public String getName(){
        return name;
    }

    public String getPass(){ return pass; }

    public String getCode(){ return code; }

}
