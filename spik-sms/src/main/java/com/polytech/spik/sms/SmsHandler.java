package com.polytech.spik.sms;

import com.polytech.spik.protocol.SpikMessages;

/**
 * Created by mfuntowicz on 13/12/15.
 */
public interface SmsHandler {

    void onContactReceived(SpikMessages.Contact contact);

    void onConversationReceive(SpikMessages.Conversation conversation);

    void onMessage(SpikMessages.Sms message);

    void onStatusChanged(SpikMessages.StatusChanged statusChanged);

    void onSendMessage(SpikMessages.SendMessage sendMessage);
}
