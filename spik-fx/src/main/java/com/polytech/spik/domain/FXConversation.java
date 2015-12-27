package com.polytech.spik.domain;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Created by momo- on 15/12/2015.
 */
public class FXConversation implements Conversation{

    private static final int SNIPPET_LENGTH = 40;
    public static Comparator<FXMessage> DEFAULT_COMPARATOR = new MessageComparator();
    private SimpleLongProperty id;
    private ObservableList<Contact> participants;
    private ObservableList<FXMessage> messages;
    private SortedList<FXMessage> sortedMessages;
    private SimpleStringProperty snippet;

    public FXConversation(long id, Collection<Contact> participants) {
        this(id, participants, new ArrayList<>());
    }

    public FXConversation(long id, Collection<Contact> participants, Collection<FXMessage> messages) {
        this.id = new SimpleLongProperty(id);
        this.participants = FXCollections.observableArrayList(participants);
        this.messages = FXCollections.observableArrayList(messages);
        this.sortedMessages = this.messages.sorted(DEFAULT_COMPARATOR);

        this.snippet = new SimpleStringProperty();
        this.sortedMessages.addListener((InvalidationListener) c -> updateSnippet());

        updateSnippet();
    }

    public long id(){
         return id.get();
    }

    public ReadOnlyLongProperty idProperty(){
        return id;
    }

    public Iterable<Contact> participants(){
        return participants;
    }

    public ObservableList<Contact> participantsProperty(){
        return FXCollections.unmodifiableObservableList(participants);
    }

    public Collection<Message> messages(){
        return messages.stream().collect(Collectors.toList());
    }

    @Override
    public void addMessage(Message message) {
        if(message instanceof FXMessage)
            messages.add(((FXMessage) message));
    }

    public ObservableList<FXMessage> messagesProperty(){
        return messages;
    }

    public SortedList<FXMessage> sortedMessagesProperty(){ return sortedMessages; }

    public String snippet() {
        return snippet.get();
    }

    public SimpleStringProperty snippetProperty() {
        return snippet;
    }

    private void updateSnippet() {
        if(messages.size() > 0){
            String msg = sortedMessages.get(sortedMessages.size() - 1).text();
            snippet.set(msg.length() < SNIPPET_LENGTH ? msg : msg.substring(0, SNIPPET_LENGTH));
        }
    }

    public static class MessageComparator implements Comparator<FXMessage>{
        @Override
        public int compare(FXMessage a, FXMessage b) {
            return Long.compareUnsigned(a.date(), b.date());
        }
    }
}
