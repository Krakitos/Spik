package com.polytech.spik.views.notifications;

import com.polytech.spik.utils.mac.Foundation;
import com.polytech.spik.utils.mac.ID;

import static com.polytech.spik.utils.mac.Foundation.invoke;
import static com.polytech.spik.utils.mac.Foundation.nsString;

/**
 * Created by mfuntowicz on 30/12/15.
 */
public class MountainLionNotificationProvider implements NotificationProvider{

    private static MountainLionNotificationProvider ourInstance;

    public static synchronized MountainLionNotificationProvider getInstance() {
        if (ourInstance == null) {
            ourInstance = new MountainLionNotificationProvider();
        }
        return ourInstance;
    }

    private static void cleanupDeliveredNotifications() {
        final ID center = invoke(Foundation.getObjcClass("NSUserNotificationCenter"), "defaultUserNotificationCenter");
        invoke(center, "removeAllDeliveredNotifications");
    }

    public static String stripHtml(String html, boolean convertBreaks) {
        if (convertBreaks)
            html = html.replaceAll("<br/?>", "\n\n");

        return html.replaceAll("<(.|\n)*?>", "");
    }

    @Override
    public void notify(String name, String title, String description) {
        final ID notification = invoke(Foundation.getObjcClass("NSUserNotification"), "new");
        invoke(notification, "setTitle:", nsString(stripHtml(title, true).replace("%", "%%")));
        invoke(notification, "setInformativeText:", nsString(stripHtml(description, true).replace("%", "%%")));
        final ID center = invoke(Foundation.getObjcClass("NSUserNotificationCenter"), "defaultUserNotificationCenter");
        invoke(center, "deliverNotification:", notification);
    }

    @Override
    public void notify(String name, String title, String description, String icon) {

    }

    @Override
    public void clean() {
        cleanupDeliveredNotifications();
    }
}
