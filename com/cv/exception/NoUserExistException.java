package com.cv.exception;

public class NoUserExistException extends Exception {
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        //return super.getMessage();
        return "User does not exist";
    }
}