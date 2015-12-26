package com.polytech.spik.exceptions;

/**
 * Created by momo- on 19/12/2015.
 */
public class UnboundChannelException extends Exception {
    public UnboundChannelException(){
        super("Trying to write in an unbound channel");
    }
}
