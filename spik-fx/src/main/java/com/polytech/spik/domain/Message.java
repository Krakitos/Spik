package com.polytech.spik.domain;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by momo- on 15/12/2015.
 */
public class Message {

    private SimpleLongProperty id;
    private SimpleLongProperty date;
    private SimpleObjectProperty<Status> status;
    private SimpleStringProperty text;


    public Message(long id, long date, Status status, String text) {
        this.id = new SimpleLongProperty(id);
        this.date = new SimpleLongProperty(date);
        this.status = new SimpleObjectProperty<>(status);
        this.text = new SimpleStringProperty(text);
    }

    public long id() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public long date() {
        return date.get();
    }

    public SimpleLongProperty dateProperty() {
        return date;
    }

    public Status status() {
        return status.get();
    }

    public SimpleObjectProperty<Status> statusProperty() {
        return status;
    }

    public String text() {
        return text.get();
    }

    public SimpleStringProperty textProperty() {
        return text;
    }

    public enum Status{
        NOT_READ,
        READ,
        SENDING,
        SENT
    }
}
