package org.rivelles.socialmedia.users;

public class EmailNotificationsObserver implements NotificationsObserver {
    @Override
    public void notify(User user) {
        System.out.println("Sending email...");
    }
}
