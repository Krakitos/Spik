package com.polytech.spik.domain;

import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by momo- on 15/12/2015.
 */
public class Conversation {

    private static final int SNIPPET_LENGTH = 40;

    private SimpleLongProperty id;
    private ObservableList<Contact> participants;
    private ObservableList<Message> messages;
    private SimpleStringProperty snippet;

    public Conversation(long id, Collection<Contact> participants) {
        this(id, participants, new ArrayList<>());
    }

    public Conversation(long id, Collection<Contact> participants, Collection<Message> messages) {
        this.id = new SimpleLongProperty(id);
        this.participants = FXCollections.observableArrayList(participants);
        this.messages = new SortedList<>(
                FXCollections.observableArrayList(messages),
                (a, b) -> Long.compareUnsigned(a.date(), b.date())
        );

        this.snippet = new SimpleStringProperty();
        this.messages.addListener((ListChangeListener<Message>) c -> updateSnippet());

        updateSnippet();
    }

    public long id(){
         return id.get();
    }

    public ReadOnlyLongProperty idProperty(){
        return id;
    }

    public Collection<Contact> participants(){
        return participants;
    }

    public ObservableList<Contact> partipantsProperty(){
        return FXCollections.unmodifiableObservableList(participants);
    }

    public Collection<Message> messages(){
        return messages;
    }

    public ObservableList<Message> messagesProperty(){
        return FXCollections.unmodifiableObservableList(messages);
    }

    public String snippet() {
        return snippet.get();
    }

    public SimpleStringProperty snippetProperty() {
        return snippet;
    }

    private void updateSnippet() {
        if(messages.size() > 0){
            String msg = messages.get(0).text();
            snippet.set(msg.length() < SNIPPET_LENGTH ? msg : msg.substring(0, SNIPPET_LENGTH));
        }
    }
}
