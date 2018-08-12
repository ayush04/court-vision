package com.cv.exception;

public class UserAlreadyLoggedInException extends Exception {
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        //return super.getMessage();
        return "User already logged in";
    }
}