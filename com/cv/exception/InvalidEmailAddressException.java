package com.cv.exception;

public class InvalidEmailAddressException extends Exception {
    private String message = "";

    public InvalidEmailAddressException() {
        super();
    }

    public InvalidEmailAddressException(String mesg) {
        super(mesg);
        this.message = mesg;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        if (!message.equals("")) {
            return message;
        } else {
            return "Email Address not valid";
        }
    }
}