package com.cv.exception;

public class InvalidGameException extends Exception {
    private String message = "";

    public InvalidGameException() {
        super();
    }

    public InvalidGameException(String mesg) {
        super(mesg);
        this.message = mesg;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        if (!message.equals("")) {
            return message;
        } else {
            return "Game ID not valid";
        }
    }
}