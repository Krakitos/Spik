package com.polytech.spik.views.notifications;

import com.intellij.openapi.util.SystemInfo;

/**
 * Created by mfuntowicz on 30/12/15.
 */
public class NotificationManager {

    private static final NotificationManager INSTANCE = new NotificationManager();

    private final NotificationProvider provider;

    public NotificationManager() {
        if(SystemInfo.isLinux){
            provider = new LibNotifyProvider();
        }else if(SystemInfo.isMacOSMountainLion){
            provider = new MountainLionNotificationProvider();
        }else {
            provider = new DefaultNotificationProvider();
        }
    }

    public static NotificationManager getInstance(){
        return INSTANCE;
    }

    public NotificationProvider provider(){
        return provider;
    }
}
