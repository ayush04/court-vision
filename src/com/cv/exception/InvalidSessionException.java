package com.cv.exception;

public class InvalidSessionException extends Exception {
    private String message = "";

    public InvalidSessionException() {
        super();
    }

    public InvalidSessionException(String mesg) {
        super(mesg);
        this.message = mesg;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        if (!message.equals("")) {
            return message;
        } else {
            return "Session is not valid. Please login again";
        }
    }
}