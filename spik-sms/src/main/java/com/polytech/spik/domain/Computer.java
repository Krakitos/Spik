package com.polytech.spik.domain;

/**
 * Created by momo- on 16/11/2015.
 */
public class Computer {
    private final String name;
    private final String os;
    private final String version;
    private final String ip;
    private int port;

    public Computer(String name, String os, String version, String ip, int port) {
        this.name = name;
        this.os = os;
        this.version = version;
        this.ip = ip;
        this.port = port;
    }

    public String name(){
        return name;
    }

    public String ip(){
        return ip;
    }

    public String os(){
        return os;
    }

    public String version(){ return version; }

    public int port(){
        return port;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "name='" + name + '\'' +
                ", ip=" + ip +
                ", os='" + os + '\'' +
                ", port=" + port +
                "} ";
    }
}
