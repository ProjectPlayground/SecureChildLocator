package com.locator.chield.secure.application;


public class Result {
    private boolean result;
    private String msg;

    public Result(boolean result,String msg){
        this.result=result;
        this.msg=msg;
    }
    public boolean getResult(){
        return result;
    }
    public String getMessage(){
        return msg;
    }
}
