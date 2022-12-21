package com.android.systemui.people;

import android.app.Notification;
import android.app.Person;
import android.content.pm.PackageManager;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.android.internal.util.ArrayUtils;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class NotificationHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "PeopleNotifHelper";
    public static Comparator<NotificationEntry> notificationEntryComparator = new Comparator<NotificationEntry>() {
        public int compare(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
            Notification notification = notificationEntry.getSbn().getNotification();
            Notification notification2 = notificationEntry2.getSbn().getNotification();
            boolean isMissedCall = NotificationHelper.isMissedCall(notification);
            boolean isMissedCall2 = NotificationHelper.isMissedCall(notification2);
            if (isMissedCall && !isMissedCall2) {
                return -1;
            }
            if (!isMissedCall && isMissedCall2) {
                return 1;
            }
            List<Notification.MessagingStyle.Message> messagingStyleMessages = NotificationHelper.getMessagingStyleMessages(notification);
            List<Notification.MessagingStyle.Message> messagingStyleMessages2 = NotificationHelper.getMessagingStyleMessages(notification2);
            if (messagingStyleMessages != null && messagingStyleMessages2 != null) {
                return (int) (messagingStyleMessages2.get(0).getTimestamp() - messagingStyleMessages.get(0).getTimestamp());
            }
            if (messagingStyleMessages == null) {
                return 1;
            }
            if (messagingStyleMessages2 == null) {
                return -1;
            }
            return (int) (notification2.when - notification.when);
        }
    };

    public static NotificationEntry getHighestPriorityNotification(Set<NotificationEntry> set) {
        if (set == null || set.isEmpty()) {
            return null;
        }
        return set.stream().filter(new NotificationHelper$$ExternalSyntheticLambda0()).sorted(notificationEntryComparator).findFirst().orElse(null);
    }

    public static boolean isMissedCall(NotificationEntry notificationEntry) {
        return (notificationEntry == null || notificationEntry.getSbn().getNotification() == null || !isMissedCall(notificationEntry.getSbn().getNotification())) ? false : true;
    }

    public static boolean isMissedCall(Notification notification) {
        return notification != null && Objects.equals(notification.category, NotificationCompat.CATEGORY_MISSED_CALL);
    }

    private static boolean hasContent(NotificationEntry notificationEntry) {
        List<Notification.MessagingStyle.Message> messagingStyleMessages;
        if (notificationEntry == null || (messagingStyleMessages = getMessagingStyleMessages(notificationEntry.getSbn().getNotification())) == null || messagingStyleMessages.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isValid(NotificationEntry notificationEntry) {
        return (notificationEntry == null || notificationEntry.getRanking() == null || notificationEntry.getRanking().getConversationShortcutInfo() == null || notificationEntry.getSbn().getNotification() == null) ? false : true;
    }

    public static boolean isMissedCallOrHasContent(NotificationEntry notificationEntry) {
        return isMissedCall(notificationEntry) || hasContent(notificationEntry);
    }

    public static boolean hasReadContactsPermission(PackageManager packageManager, StatusBarNotification statusBarNotification) {
        return packageManager.checkPermission("android.permission.READ_CONTACTS", statusBarNotification.getPackageName()) == 0;
    }

    public static boolean shouldMatchNotificationByUri(StatusBarNotification statusBarNotification) {
        Notification notification = statusBarNotification.getNotification();
        if (notification == null) {
            return false;
        }
        return isMissedCall(notification);
    }

    public static String getContactUri(StatusBarNotification statusBarNotification) {
        Person senderPerson;
        String uri;
        ArrayList parcelableArrayList = statusBarNotification.getNotification().extras.getParcelableArrayList(NotificationCompat.EXTRA_PEOPLE_LIST);
        if (parcelableArrayList != null && parcelableArrayList.get(0) != null && (uri = ((Person) parcelableArrayList.get(0)).getUri()) != null && !uri.isEmpty()) {
            return uri;
        }
        List<Notification.MessagingStyle.Message> messagingStyleMessages = getMessagingStyleMessages(statusBarNotification.getNotification());
        if (messagingStyleMessages == null || messagingStyleMessages.isEmpty() || (senderPerson = messagingStyleMessages.get(0).getSenderPerson()) == null || senderPerson.getUri() == null || senderPerson.getUri().isEmpty()) {
            return null;
        }
        return senderPerson.getUri();
    }

    public static List<Notification.MessagingStyle.Message> getMessagingStyleMessages(Notification notification) {
        if (!(notification == null || !notification.isStyle(Notification.MessagingStyle.class) || notification.extras == null)) {
            Parcelable[] parcelableArray = notification.extras.getParcelableArray(NotificationCompat.EXTRA_MESSAGES);
            if (!ArrayUtils.isEmpty(parcelableArray)) {
                List<Notification.MessagingStyle.Message> messagesFromBundleArray = Notification.MessagingStyle.Message.getMessagesFromBundleArray(parcelableArray);
                messagesFromBundleArray.sort(Collections.reverseOrder(Comparator.comparing(new NotificationHelper$$ExternalSyntheticLambda1())));
                return messagesFromBundleArray;
            }
        }
        return null;
    }

    private static boolean isGroupConversation(Notification notification) {
        return notification.extras.getBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION, false);
    }

    public static CharSequence getSenderIfGroupConversation(Notification notification, Notification.MessagingStyle.Message message) {
        Person senderPerson;
        if (isGroupConversation(notification) && (senderPerson = message.getSenderPerson()) != null) {
            return senderPerson.getName();
        }
        return null;
    }

    public static boolean shouldFilterOut(Optional<Bubbles> optional, NotificationEntry notificationEntry) {
        try {
            if (!optional.isPresent() || !optional.get().isBubbleNotificationSuppressedFromShade(notificationEntry.getKey(), notificationEntry.getSbn().getGroupKey())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Exception checking if notification is suppressed: " + e);
            return false;
        }
    }
}
