package com.polytech.spik.domain;

import java.net.InetSocketAddress;

/**
 * Created by momo- on 16/11/2015.
 */
public class Phone {
    private final String manufacturer;
    private final String model;
    private final String os;
    private final int sdkVersion;
    private final String name;
    private final InetSocketAddress remoteAddress;

    public Phone(String name, String manufacturer, String model, String os, int sdkVersion, String ip, int port) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.model = model;
        this.os = os;
        this.sdkVersion = sdkVersion;
        remoteAddress = new InetSocketAddress(ip, port);
    }

    public String name(){
        return name;
    }

    public InetSocketAddress address(){
        return remoteAddress;
    }

    public String manufacturer() {
        return manufacturer;
    }

    public String model() {
        return model;
    }

    public String os() {
        return os;
    }

    public int sdkVersion() {
        return sdkVersion;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "name='" + name + '\'' +
                ", addr='" + remoteAddress + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", remotes='" + model + '\'' +
                ", os='" + os + '\'' +
                ", sdkVersion=" + sdkVersion +
                '}';
    }
}
