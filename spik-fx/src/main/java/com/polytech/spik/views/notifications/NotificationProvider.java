package com.polytech.spik.views.notifications;


/**
 * Created by mfuntowicz on 30/12/15.
 */
public interface NotificationProvider {

    void notify(String name, String title, String description);

    void notify(String name, String title, String description, String icon);

    void clean();
}
