package com.cv.exception;

public class InvalidCredentialsException extends Exception {
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        //return super.getMessage();
        return "Username and password do not match!!";
    }
}