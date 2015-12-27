package com.polytech.spik.domain;

import javafx.beans.property.*;

/**
 * Created by momo- on 15/12/2015.
 */
public class FXContact implements Contact {

    private SimpleLongProperty id;
    private StringProperty name;
    private StringProperty address;
    private byte[] picture;

    public FXContact(long id, String name, String address) {
        this(id, name, address, null);
    }

    public FXContact(long id, String name, String address, byte[] picture) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.picture = picture;
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

    public byte[] picture(){
        return picture;
    }

    @Override
    public boolean hasPicture() {
        return picture != null;
    }

    @Override
    public String toString() {
        return "FXContact{" +
                "id=" + id() +
                ", name=" + name() +
                ", address=" + address() +
                ", picture=" + hasPicture() +
                '}';
    }
}
