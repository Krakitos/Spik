package com.polytech.spik.views.notifications;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Created by mfuntowicz on 30/12/15.
 */
public class LibNotifyProvider implements NotificationProvider {

    private final LibNotify nativeLib;
    private final String myIcon = null;
    private final Object myLock = new Object();
    private boolean myDisposed = false;

    public LibNotifyProvider() {
        nativeLib = (LibNotify) Native.loadLibrary("libnotify.so.4", LibNotify.class);

        String appName = "Spik";
        if (nativeLib.notify_init(appName) == 0) {
            throw new IllegalStateException("notify_init failed");
        }
    }

    @Override
    public void notify(String name, String title, String description) {
        synchronized (myLock) {
            if (!myDisposed) {
                Pointer notification = nativeLib.notify_notification_new(title, description, myIcon);
                nativeLib.notify_notification_show(notification, null);
            }
        }
    }

    @Override
    public void notify(String name, String title, String description, String icon) {

    }

    @Override
    public void clean() {
        synchronized (myLock) {
            myDisposed = true;
            nativeLib.notify_uninit();
        }
    }

    private interface LibNotify extends Library {
        int notify_init(String appName);
        void notify_uninit();
        Pointer notify_notification_new(String summary, String body, String icon);
        int notify_notification_show(Pointer notification, Pointer error);
    }
}
