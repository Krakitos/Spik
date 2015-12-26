package com.polytech.spik.remotes;

import com.polytech.spik.context.Context;
import com.polytech.spik.domain.Contact;
import com.polytech.spik.domain.Conversation;
import com.polytech.spik.domain.FXConversation;
import com.polytech.spik.domain.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by momo- on 15/12/2015.
 */
public class FXContext implements Context {

    protected ObservableList<FXConversation> conversations;
    protected ObservableSet<Contact> contacts;

    public FXContext() {
        this.conversations = FXCollections.observableArrayList();
        this.contacts = FXCollections.observableSet(
            new TreeSet<>((a, b) -> Long.compareUnsigned(a.id(), b.id()))
        );
    }

    @Override
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    @Override
    public Iterable<Contact> contacts() {
        return contacts;
    }

    @Override
    public Contact findContactById(long id) {
        return contacts.stream().filter(c -> c.id() == id).findFirst().get();
    }

    @Override
    public void addConversation(Collection<Contact> participants) {
        if(conversations.parallelStream().noneMatch(c -> c.participantsProperty().containsAll(participants))){
            conversations.add(new FXConversation(System.currentTimeMillis(), participants));
        }
    }

    @Override
    public void addConversation(Conversation conversation) {
        if(conversation instanceof FXConversation && !conversations.contains(conversation)){
            conversations.add((FXConversation) conversation);
        }
    }

    @Override
    public FXConversation findConversationById(long threadId) {
        return conversations.stream().filter(c -> c.id() == threadId).findFirst().get();
    }

    @Override
    public Iterable<Conversation> conversations() {
        return conversations.stream().collect(Collectors.toList());
    }

    public ObservableList<FXConversation> conversationsProperty(){
        return FXCollections.unmodifiableObservableList(conversations);
    }

    @Override
    public void addMessage(Conversation c, Message msg) {
        if(c instanceof FXConversation && conversations.contains(c)){

        }
    }

    @Override
    public void clear() {
        conversations.clear();
        contacts.clear();
    }
}
