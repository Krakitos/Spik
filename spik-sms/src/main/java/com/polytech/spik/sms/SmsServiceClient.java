package com.polytech.spik.sms;

import com.google.protobuf.ByteString;
import com.polytech.spik.protocol.SpikMessages;
import org.slf4j.Logger;

import java.io.Closeable;

/**
 * Created by mfuntowicz on 12/12/15.
 */
public abstract class SmsServiceClient implements Closeable {

    /**
     * Ask to the device to send a message to the specified contact
     * @param phone Contact to send the message to
     * @param message Message to send. Must be non-empty
     */
    public void sendMessage(long id, String phone, String message){
        assert !message.trim().isEmpty() : "Empty message";

        getLogger().trace("Sending message to {} (length: {})", phone, message.length());

        SpikMessages.SendMessage.Builder msg = SpikMessages.SendMessage.newBuilder()
                .setMid(id)
                .setParticipants(0, phone)
                .setText(message);

        lowSend(SpikMessages.Wrapper.newBuilder().setSendMessage(msg));
    }

    public void sendContact(long id, String name, String phone, byte[] photo){
        getLogger().trace("Sending contact {}", id);

        SpikMessages.Contact.Builder msg = SpikMessages.Contact.newBuilder()
                .setId(id)
                .setName(name)
                .setPhone(phone);

        if(photo != null)
            msg.setPicture(ByteString.copyFrom(photo));

        lowSend(SpikMessages.Wrapper.newBuilder().setContact(msg));
    }

    public abstract void lowSend(SpikMessages.WrapperOrBuilder msg);

    protected abstract Logger getLogger();
}
