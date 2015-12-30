package com.polytech.spik.views.notifications;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Created by mfuntowicz on 30/12/15.
 */
public class DefaultNotificationProvider implements NotificationProvider {

    @Override
    public void notify(String name, String title, String description) {
        Notifications.create()
            .position(Pos.BOTTOM_RIGHT)
            .hideAfter(Duration.seconds(2))
            .title(title)
            .text(description)
            .show();
    }

    @Override
    public void clean() {}
}
