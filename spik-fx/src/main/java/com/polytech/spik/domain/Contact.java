package com.polytech.spik.domain;

import javafx.beans.property.*;

import java.util.Optional;

/**
 * Created by momo- on 15/12/2015.
 */
public class Contact {

    private SimpleLongProperty id;
    private StringProperty name;
    private StringProperty address;
    private Optional<byte[]> picture;

    public Contact(long id, String name, String address) {
        this(id, name, address, null);
    }

    public Contact(long id, String name, String address, byte[] picture) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.picture = Optional.ofNullable(picture);
    }

    public long id(){
        return id.get();
    }

    public ReadOnlyLongProperty idProperty(){
        return id;
    }

    public String name(){
        return name.get();
    }

    public ReadOnlyStringProperty nameProperty(){
        return name;
    }

    public String address(){
        return address.get();
    }

    public ReadOnlyStringProperty addressProperty(){
        return address;
    }

    public Optional<byte[]> picture(){
        return picture;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id() +
                ", name=" + name() +
                ", address=" + address() +
                ", picture=" + picture.isPresent() +
                '}';
    }
}
