package com.polytech.spik.domain;

/**
 * Created by mfuntowicz on 29/12/15.
 */
public interface FXEventHandler {

    /**
     * Called when a message is received from a context
     * @param c
     * @param message
     */
    void onMessageReceived(FXConversation c, FXMessage message);
}
