package com.polytech.spik.domain;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by momo- on 15/12/2015.
 */
public class FXMessage implements Message{

    private SimpleLongProperty date;
    private SimpleObjectProperty<Status> status;
    private SimpleStringProperty text;
    private byte[] content;
    private String mimeContent;

    public FXMessage(long date, Status status, String text) {
       this(date, status, text, null, null);
    }

    public FXMessage(long date, Status status, String text, byte[] content, String mimeContent) {
        this.date = new SimpleLongProperty(date);
        this.status = new SimpleObjectProperty<>(status);
        this.text = new SimpleStringProperty(text);
        this.content = content;
        this.mimeContent = mimeContent;
    }

    @Override
    public long id() {
        return date();
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

    @Override
    public byte[] content() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return content != null;
    }

    @Override
    public String contentMime() {
        return null;
    }

    public SimpleStringProperty textProperty() {
        return text;
    }
}
