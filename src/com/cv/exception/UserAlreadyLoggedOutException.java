package com.cv.exception;

public class UserAlreadyLoggedOutException extends Exception {
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        //return super.getMessage();
        return "User is not logged in";
    }
}