package com.locator.child.secure.application;

import java.io.Serializable;

/**
 * Created by Valentyn on 12-11-2016.
 */

public class Kid implements Serializable{
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
