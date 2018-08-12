package com.cv.exception;

public class InvalidPasswordException extends Exception {
    private String message = "";

    public InvalidPasswordException() {
        super();
    }

    public InvalidPasswordException(String mesg) {
        super(mesg);
        this.message = mesg;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        if (!message.equals("")) {
            return message;
        } else {
            return "Username and password do not match!!";
        }
    }
}