package com.android.systemui.statusbar;

import android.content.pm.UserInfo;
import android.util.SparseArray;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;

public interface NotificationLockscreenUserManager {
    public static final String NOTIFICATION_UNLOCKED_BY_WORK_CHALLENGE_ACTION = "com.android.systemui.statusbar.work_challenge_unlocked_notification_action";
    public static final String PERMISSION_SELF = "com.android.systemui.permission.SELF";

    public interface KeyguardNotificationSuppressor {
        boolean shouldSuppressOnKeyguard(NotificationEntry notificationEntry);
    }

    public interface NotificationStateChangedListener {
        void onNotificationStateChanged();
    }

    public interface UserChangedListener {
        void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
        }

        void onUserChanged(int i) {
        }

        void onUserRemoved(int i) {
        }
    }

    void addKeyguardNotificationSuppressor(KeyguardNotificationSuppressor keyguardNotificationSuppressor);

    void addNotificationStateChangedListener(NotificationStateChangedListener notificationStateChangedListener);

    void addUserChangedListener(UserChangedListener userChangedListener);

    SparseArray<UserInfo> getCurrentProfiles();

    int getCurrentUserId();

    boolean isAnyProfilePublicMode();

    boolean isCurrentProfile(int i);

    boolean isLockscreenPublicMode(int i);

    boolean needsRedaction(NotificationEntry notificationEntry);

    boolean needsSeparateWorkChallenge(int i) {
        return false;
    }

    void removeNotificationStateChangedListener(NotificationStateChangedListener notificationStateChangedListener);

    void removeUserChangedListener(UserChangedListener userChangedListener);

    void setLockscreenPublicMode(boolean z, int i);

    void setUpWithPresenter(NotificationPresenter notificationPresenter);

    boolean shouldAllowLockscreenRemoteInput();

    boolean shouldHideNotifications(int i);

    boolean shouldHideNotifications(String str);

    boolean shouldShowLockscreenNotifications();

    boolean shouldShowOnKeyguard(NotificationEntry notificationEntry);

    void updatePublicMode();

    boolean userAllowsNotificationsAndContents(int i);

    boolean userAllowsNotificationsInPublic(int i);

    boolean userAllowsPrivateNotificationsInPublic(int i);
}
