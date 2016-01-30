package com.polytech.spik.views.notifications;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * Created by mfuntowicz on 30/12/15.
 */
public class DefaultNotificationProvider implements NotificationProvider {

    public static final String DEFAULT_ICON = "images/ic_phonelink_ring_black_48dp_2x.png";

    @Override
    public void notify(String name, String title, String description) {
        notify(name, title, description, DEFAULT_ICON);
    }

    @Override
    public void notify(String name, String title, String description, String icon) {
        Notifications.create()
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(2))
                .title(title)
                .graphic(new ImageView(
                    new Image(DefaultNotificationProvider.class.getClassLoader().getResourceAsStream(icon), 48, 48, true, true)
                ))
                .text(description)
                .show();
    }

    @Override
    public void clean() {}
}
