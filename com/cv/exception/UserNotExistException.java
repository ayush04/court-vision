package com.cv.exception;

public class UserNotExistException extends Exception {

    private String message = "";
    public UserNotExistException(String string) {
        super(string);
        this.message = string;
    }
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        //return super.getMessage();
        if (!message.equals("")) {
            return message;
        } else {
            return "User does not exist";
        }
    }
    
    
}