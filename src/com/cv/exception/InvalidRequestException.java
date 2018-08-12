package com.cv.exception;

public class InvalidRequestException extends Exception {
    private String message = "";

    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String mesg) {
        super(mesg);
        this.message = mesg;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        if (!message.equals("")) {
            return message;
        } else {
            return "This is not a valid request.";
        }
    }
}
