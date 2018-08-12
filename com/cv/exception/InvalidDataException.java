package com.cv.exception;

public class InvalidDataException extends Exception {
    private String message = "";
    
    public InvalidDataException() {
        super();
    }
    public InvalidDataException(String mesg) {
        super(mesg);
        this.message = mesg;
    }
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        if (!message.equals("")) {
            return message;
        } else {
            return "Input data is invalid";
        }
    }
}