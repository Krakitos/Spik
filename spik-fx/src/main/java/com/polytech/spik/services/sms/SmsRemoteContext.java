package com.polytech.spik.services.sms;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.Conversation;
import com.polytech.spik.domain.Message;
import com.polytech.spik.protocol.SpikMessages;
import com.polytech.spik.remotes.FXRemoteContext;
import com.polytech.spik.sms.SmsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by momo- on 15/12/2015.
 */
public class SmsRemoteContext extends FXRemoteContext implements SmsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsRemoteContext.class);

    @Override
    public void onContactReceived(SpikMessages.Contact contact) {
        LOGGER.info("Received Contact {} / {}", contact.getId(), contact.getName());

        addContact(new Contact(
            contact.getId(),
            contact.getName(),
            contact.getPhone(),
            contact.hasPicture() ? contact.getPicture().toByteArray() : null
        ));
    }

    @Override
    public void onConversationReceive(SpikMessages.Conversation conversation) {
        LOGGER.info("Received Conversation {} / {} / {}",
                conversation.getId(), conversation.getParticipantsCount(), conversation.getMessagesCount());

        addConversation(new Conversation(
            conversation.getId(),
            conversation.getParticipantsList().stream().map(this::findContactById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()),
            conversation.getMessagesList().stream().map(m -> new Message(m.getId(), m.getDate(), convertStatus(m.getStatus()), m.getText())).collect(Collectors.toList())
        ));
    }

    @Override
    public void onMessage(SpikMessages.Sms message) {

    }

    @Override
    public void onStatusChanged(SpikMessages.StatusChanged statusChanged) {

    }

    @Override
    public void onSendMessage(SpikMessages.SendMessage sendMessage) {
        LOGGER.error("Not allowed to received this kind of message");
    }

    private Message.Status convertStatus(SpikMessages.Status status){
        switch (status){
            case NOT_READ:
                return Message.Status.NOT_READ;
            case READ:
                return Message.Status.READ;
            case SENDING:
                return Message.Status.SENDING;
            case SENT:
                return Message.Status.SENT;
            default:
                return Message.Status.READ;
        }
    }
}
