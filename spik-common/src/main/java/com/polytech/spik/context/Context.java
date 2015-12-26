package com.polytech.spik.context;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.Conversation;
import com.polytech.spik.domain.Message;

import java.util.Collection;

/**
 * Created by momo- on 19/12/2015.
 */
public interface Context {

    /**
     * Add a contact to this contact
     * @param contact
     */
    void addContact(Contact contact);


    /**
     * Contacts of this context
     * @return
     */
    Iterable<Contact> contacts();

    /**
     * Add a bew conversation (empty)
     * @param participants Contact's involved in the conversation
     */
    void addConversation(Collection<Contact> participants);

    /**
     * Add a conversation
     * @param conversation
     */
    void addConversation(Conversation conversation);

    /**
     * Attempt to find a conversation according to the specified id
     * @param id
     * @return
     */
    Conversation findConversationById(long id);

    /**
     * Conversations of this context
     * @return
     */
    Iterable<Conversation> conversations();

    /**
     * Add a message to the specified conversation
     * @param c Conversation to add the message
     * @param message Message to add
     */
    void addMessage(Conversation c, Message message);

    /**
     * Attempt to find a contact according to his id
     * @param id
     * @return
     */
    Contact findContactById(long id);

    /**
     * Clear this context
     */
    void clear();
}
