package com.polytech.spik.remotes;

import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.Conversation;
import com.polytech.spik.domain.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.util.Optional;
import java.util.TreeSet;

/**
 * Created by momo- on 15/12/2015.
 */
public class FXRemoteContextImpl implements FXRemoteContext {

    private ObservableList<Conversation> conversations;
    private ObservableSet<Contact> contacts;

    public FXRemoteContextImpl() {
        this.conversations = FXCollections.observableArrayList();
        this.contacts = FXCollections.observableSet(
            new TreeSet<>((a, b) -> Long.compareUnsigned(a.id(), b.id()))
        );
    }

    @Override
    public void addConversation(Conversation conversation) {
        this.conversations.add(conversation);
    }

    @Override
    public Optional<Conversation> findConversationById(long threadId) {
        return conversations.stream().filter(c -> c.id() == threadId).findFirst();
    }

    @Override
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    @Override
    public Optional<Contact> findContactById(long id) {
        return contacts.stream().filter(c -> c.id() == id).findFirst();
    }

    @Override
    public void addMessage(long threadId, Message msg) {
        conversations.stream().filter(c -> c.id() == threadId).findFirst().ifPresent(c -> c.messages().add(msg));
    }

    @Override
    public ObservableList<Conversation> conversationsProperty(){
        return FXCollections.unmodifiableObservableList(conversations);
    }

    @Override
    public void clear() {
        conversations.clear();
        contacts.clear();
    }
}
