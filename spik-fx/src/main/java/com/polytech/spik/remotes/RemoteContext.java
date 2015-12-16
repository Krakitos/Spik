package com.polytech.spik.remotes;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.Conversation;
import com.polytech.spik.domain.Message;

import java.util.Optional;

/**
 * Created by momo- on 15/12/2015.
 */
public interface RemoteContext {

    void addConversation(Conversation conversation);

    Optional<Conversation> findConversationById(long threadId);

    void addContact(Contact contact);

    Optional<Contact> findContactById(long id);

    void addMessage(long threadId, Message msg);

    void clear();
}
