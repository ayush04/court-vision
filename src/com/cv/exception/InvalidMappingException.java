package com.cv.exception;

public class InvalidMappingException extends Exception {
    String str = "";
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return "".equals(str)? "Mapping does not exist": str;
    }
    
    public InvalidMappingException(String str) {
        this.str = str;
    }
}