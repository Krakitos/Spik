package com.polytech.spik.services.sms;

import com.polytech.spik.domain.*;
import com.polytech.spik.protocol.SpikMessages;
import com.polytech.spik.remotes.FXContext;
import com.polytech.spik.remotes.FXContextWrapper;
import com.polytech.spik.sms.service.LanSmsHandler;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by momo- on 19/12/2015.
 */
public abstract class SmsContext extends LanSmsHandler implements FXContextWrapper{

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsContext.class);
    private final FXContext context;

    public SmsContext(FXContext context) {
        this.context = context;
    }

    @Override
    protected void onContactReceived(SpikMessages.Contact contact) {
        LOGGER.trace("Received Contact {}", contact.getId());


        final FXContact c;
        if(contact.hasPicture())
            c = new FXContact(contact.getId(), contact.getName(), contact.getPhone(), contact.getPicture().toByteArray());
        else
            c = new FXContact(contact.getId(), contact.getName(), contact.getPhone());

        context.addContact(c);
    }

    @Override
    protected void onConversationReceive(SpikMessages.Conversation conversation) {
        List<Contact> contacts = conversation.getParticipantsList().parallelStream()
                                                .map(context::findContactById)
                                                .collect(Collectors.toList());

        List<FXMessage> messages = conversation.getMessagesList().parallelStream()
                                                .map(this::fromSms).collect(Collectors.toList());

        LOGGER.trace("Received Conversation {} --> Participants {}", conversation.getId(), contacts.size());

        Platform.runLater(() ->
            context.addConversation(new FXConversation(
                conversation.getId(),
                contacts,
                messages
            ))
        );

    }

    @Override
    protected void onMessage(SpikMessages.Sms sms) {
        LOGGER.info("Received Message for conversation {}", sms.getThreadId());
        Optional<FXConversation> conversation = Optional.ofNullable(context.findConversationById(sms.getThreadId()));

        if(conversation.isPresent()) {
            LOGGER.trace("Adding message {} to conversation {}", sms.getDate(), sms.getThreadId());
            Platform.runLater(() -> conversation.get().messagesProperty().add(fromSms(sms)));
        }else
            LOGGER.warn("Unable to find conversation {}", sms.getThreadId());

    }

    @Override
    protected void onStatusChanged(SpikMessages.StatusChanged statusChanged) {

    }

    @Override
    protected void onSendMessage(SpikMessages.SendMessage sendMessage) {
        LOGGER.warn("Received an unintended SendMessage");
    }

    @Override
    public FXContext fxContext() {
        return context;
    }

    private FXMessage fromSms(SpikMessages.Sms sms){
        return new FXMessage(
                sms.getDate(),
                convertStatus(sms.getStatus()),
                sms.getText()
        );
    }

    private Message.Status convertStatus(SpikMessages.Status status) {
        switch (status){
            case NOT_READ:
                return Message.Status.NOT_READ;
            case READ:
                return Message.Status.READ;
            case SENT:
                return Message.Status.SENT;
            case SENDING:
                return Message.Status.PENDING;
            default:
                return Message.Status.READ;
        }
    }
}