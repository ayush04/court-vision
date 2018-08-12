package com.cv.exception;

public class SessionAlreadyExistException extends Exception {
    public SessionAlreadyExistException() {
        super();
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        //return super.getMessage();
        return "Session already exist";
    }
}