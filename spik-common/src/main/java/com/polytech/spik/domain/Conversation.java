package com.polytech.spik.domain;

/**
 * Created by momo- on 19/12/2015.
 */
public interface Conversation {

    /**
     * Conversation's id
     * @return
     */
    long id();

    /**
     * Participants of the conversation
     * @return
     */
    Iterable<Contact> participants();

    /**
     * Message of the conversation
     * @return
     */
    Iterable<Message> messages();

    /**
     * Add a message to the conversation
     * @param message
     */
    void addMessage(Message message);

    /**
     * Snippet of this conversation
     * @return
     */
    String snippet();

    /**
     * Date of the last message
     * @return
     */
    long lastMessageDate();
}
