package com.android.p019wm.shell.bubbles;

import android.app.NotificationChannel;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.util.ArraySet;
import android.util.Pair;
import android.util.SparseArray;
import com.android.p019wm.shell.common.annotations.ExternalThread;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.p026io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@ExternalThread
/* renamed from: com.android.wm.shell.bubbles.Bubbles */
public interface Bubbles {
    public static final int DISMISS_ACCESSIBILITY_ACTION = 6;
    public static final int DISMISS_AGED = 2;
    public static final int DISMISS_BLOCKED = 4;
    public static final int DISMISS_GROUP_CANCELLED = 9;
    public static final int DISMISS_INVALID_INTENT = 10;
    public static final int DISMISS_NOTIF_CANCEL = 5;
    public static final int DISMISS_NO_BUBBLE_UP = 14;
    public static final int DISMISS_NO_LONGER_BUBBLE = 7;
    public static final int DISMISS_OVERFLOW_MAX_REACHED = 11;
    public static final int DISMISS_PACKAGE_REMOVED = 13;
    public static final int DISMISS_RELOAD_FROM_DISK = 15;
    public static final int DISMISS_SHORTCUT_REMOVED = 12;
    public static final int DISMISS_TASK_FINISHED = 3;
    public static final int DISMISS_USER_CHANGED = 8;
    public static final int DISMISS_USER_GESTURE = 1;
    public static final int DISMISS_USER_REMOVED = 16;

    /* renamed from: com.android.wm.shell.bubbles.Bubbles$BubbleExpandListener */
    public interface BubbleExpandListener {
        void onBubbleExpandChanged(boolean z, String str);
    }

    /* renamed from: com.android.wm.shell.bubbles.Bubbles$BubbleMetadataFlagListener */
    public interface BubbleMetadataFlagListener {
        void onBubbleMetadataFlagChanged(Bubble bubble);
    }

    @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: com.android.wm.shell.bubbles.Bubbles$DismissReason */
    public @interface DismissReason {
    }

    /* renamed from: com.android.wm.shell.bubbles.Bubbles$PendingIntentCanceledListener */
    public interface PendingIntentCanceledListener {
        void onPendingIntentCanceled(Bubble bubble);
    }

    /* renamed from: com.android.wm.shell.bubbles.Bubbles$SysuiProxy */
    public interface SysuiProxy {
        void getPendingOrActiveEntry(String str, Consumer<BubbleEntry> consumer);

        void getShouldRestoredEntries(ArraySet<String> arraySet, Consumer<List<BubbleEntry>> consumer);

        void isNotificationShadeExpand(Consumer<Boolean> consumer);

        void notifyInvalidateNotifications(String str);

        void notifyMaybeCancelSummary(String str);

        void notifyRemoveNotification(String str, int i);

        void onManageMenuExpandChanged(boolean z);

        void onStackExpandChanged(boolean z);

        void onUnbubbleConversation(String str);

        void removeNotificationEntry(String str);

        void requestNotificationShadeTopUi(boolean z, String str);

        void setNotificationInterruption(String str);

        void updateNotificationBubbleButton(String str);

        void updateNotificationSuppression(String str);
    }

    void collapseStack();

    void dump(PrintWriter printWriter, String[] strArr);

    void expandStackAndSelectBubble(Bubble bubble);

    void expandStackAndSelectBubble(BubbleEntry bubbleEntry);

    Bubble getBubbleWithShortcutId(String str);

    boolean handleDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer, Executor executor);

    boolean isBubbleExpanded(String str);

    boolean isBubbleNotificationSuppressedFromShade(String str, String str2);

    boolean isStackExpanded();

    void onConfigChanged(Configuration configuration);

    void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray);

    void onEntryAdded(BubbleEntry bubbleEntry);

    void onEntryRemoved(BubbleEntry bubbleEntry);

    void onEntryUpdated(BubbleEntry bubbleEntry, boolean z);

    void onNotificationChannelModified(String str, UserHandle userHandle, NotificationChannel notificationChannel, int i);

    void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap);

    void onStatusBarStateChanged(boolean z);

    void onStatusBarVisibilityChanged(boolean z);

    void onTaskbarChanged(Bundle bundle);

    void onUserChanged(int i);

    void onUserRemoved(int i);

    void onZenStateChanged();

    void openBubbleOverflow();

    void removeSuppressedSummaryIfNecessary(String str, Consumer<String> consumer, Executor executor);

    void setExpandListener(BubbleExpandListener bubbleExpandListener);

    void setSysuiProxy(SysuiProxy sysuiProxy);

    void updateForThemeChanges();
}
