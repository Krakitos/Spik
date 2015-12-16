package com.polytech.spik.exceptions;

/**
 * Created by momo- on 16/12/2015.
 */
public class UnboundServerException extends Exception {
    public UnboundServerException() {
        super("The server is not bound");
    }
}
