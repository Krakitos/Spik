package com.polytech.util;

import com.intellij.openapi.util.SystemInfo;

import java.util.Map;

/**
 * Created by momo- on 22/10/2015.
 */
public class ComputerInfo extends SystemInfo {

    private static final Map<String, String> ENV = System.getenv();

    public static String hostname(){
        if (ENV.containsKey("COMPUTERNAME"))
            return ENV.get("COMPUTERNAME");
        else if (ENV.containsKey("HOSTNAME"))
            return ENV.get("HOSTNAME");
        else
            return "Unknown";
    }
}
