package com.polytech.spik.domain;

import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by momo- on 15/12/2015.
 */
public class Conversation {
    private SimpleLongProperty id;
    private ObservableList<Contact> participants;
    private ObservableList<Message> messages;

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
}
