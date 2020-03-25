package com.cloud.filesystem.exception;

public class ConnectionNotEstablishedException extends Exception {
    private static final long serialVersionUID = 1L;

    public ConnectionNotEstablishedException(String message){
        super(message);
    }

    public ConnectionNotEstablishedException() {
        super("Can not establish a DivvyDrive connection. Try again by checking your parameters.");
    }
}
