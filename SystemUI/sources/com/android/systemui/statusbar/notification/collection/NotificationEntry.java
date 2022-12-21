package com.android.systemui.statusbar.notification.collection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.Person;
import android.content.Context;
import android.content.pm.ShortcutInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import android.service.notification.SnoozeCriterion;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.view.ContentInfo;
import androidx.core.app.NotificationCompat;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.statusbar.InflationTask;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class NotificationEntry extends ListEntry {
    private static final int COLOR_INVALID = 1;
    private static final long INITIALIZATION_DELAY = 400;
    private static final long LAUNCH_COOLDOWN = 2000;
    private static final long NOT_LAUNCHED_YET = -2000;
    private static final long REMOTE_INPUT_COOLDOWN = 500;
    public EditedSuggestionInfo editedSuggestionInfo;
    private boolean hasSentReply;
    public CharSequence headsUpStatusBarText;
    public CharSequence headsUpStatusBarTextPublic;
    private long initializationTime = -1;
    private boolean interruption;
    private long lastFullScreenIntentLaunchTime = NOT_LAUNCHED_YET;
    public long lastRemoteInputSent = NOT_LAUNCHED_YET;
    public final ArraySet<Integer> mActiveAppOps = new ArraySet<>(3);
    private boolean mAutoHeadsUp;
    private Notification.BubbleMetadata mBubbleMetadata;
    private int mBucket = 5;
    private int mCachedContrastColor = 1;
    private int mCachedContrastColorIsFor = 1;
    int mCancellationReason = -1;
    private Throwable mDebugThrowable;
    final List<NotifDismissInterceptor> mDismissInterceptors = new ArrayList();
    private DismissState mDismissState = DismissState.NOT_DISMISSED;
    private boolean mExpandAnimationRunning;
    private IconPack mIcons = IconPack.buildEmptyPack((IconPack) null);
    private boolean mIsAlerting;
    private boolean mIsMarkedForUserTriggeredMovement;
    private final String mKey;
    final List<NotifLifetimeExtender> mLifetimeExtenders = new ArrayList();
    private List<OnSensitivityChangedListener> mOnSensitivityChangedListeners = new ArrayList();
    private Long mPendingAnimationDuration;
    private boolean mPulseSupressed;
    private NotificationListenerService.Ranking mRanking;
    public boolean mRemoteEditImeAnimatingAway;
    public boolean mRemoteEditImeVisible;
    private ExpandableNotificationRowController mRowController;
    private InflationTask mRunningTask = null;
    private StatusBarNotification mSbn;
    private boolean mSensitive = true;
    private ShortcutInfo mShortcutInfo;
    public ContentInfo remoteInputAttachment;
    public String remoteInputMimeType;
    public CharSequence remoteInputText;
    public CharSequence remoteInputTextWhenReset;
    public Uri remoteInputUri;
    private ExpandableNotificationRow row;
    public int targetSdk;

    public enum DismissState {
        NOT_DISMISSED,
        DISMISSED,
        PARENT_DISMISSED
    }

    public interface OnSensitivityChangedListener {
        void onSensitivityChanged(NotificationEntry notificationEntry);
    }

    public NotificationEntry getRepresentativeEntry() {
        return this;
    }

    public NotificationEntry(StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking, long j) {
        super((String) Objects.requireNonNull(((StatusBarNotification) Objects.requireNonNull(statusBarNotification)).getKey()), j);
        Objects.requireNonNull(ranking);
        this.mKey = statusBarNotification.getKey();
        setSbn(statusBarNotification);
        setRanking(ranking);
    }

    public String getKey() {
        return this.mKey;
    }

    public StatusBarNotification getSbn() {
        return this.mSbn;
    }

    public void setSbn(StatusBarNotification statusBarNotification) {
        Objects.requireNonNull(statusBarNotification);
        Objects.requireNonNull(statusBarNotification.getKey());
        if (statusBarNotification.getKey().equals(this.mKey)) {
            this.mSbn = statusBarNotification;
            this.mBubbleMetadata = statusBarNotification.getNotification().getBubbleMetadata();
            return;
        }
        throw new IllegalArgumentException("New key " + statusBarNotification.getKey() + " doesn't match existing key " + this.mKey);
    }

    public NotificationListenerService.Ranking getRanking() {
        return this.mRanking;
    }

    public void setRanking(NotificationListenerService.Ranking ranking) {
        Objects.requireNonNull(ranking);
        Objects.requireNonNull(ranking.getKey());
        if (ranking.getKey().equals(this.mKey)) {
            this.mRanking = ranking.withAudiblyAlertedInfo(this.mRanking);
            return;
        }
        throw new IllegalArgumentException("New key " + ranking.getKey() + " doesn't match existing key " + this.mKey);
    }

    public DismissState getDismissState() {
        return this.mDismissState;
    }

    /* access modifiers changed from: package-private */
    public void setDismissState(DismissState dismissState) {
        this.mDismissState = (DismissState) Objects.requireNonNull(dismissState);
    }

    public NotifFilter getExcludingFilter() {
        return getAttachState().getExcludingFilter();
    }

    public NotifPromoter getNotifPromoter() {
        return getAttachState().getPromoter();
    }

    public NotificationChannel getChannel() {
        return this.mRanking.getChannel();
    }

    public long getLastAudiblyAlertedMs() {
        return this.mRanking.getLastAudiblyAlertedMillis();
    }

    public boolean isAmbient() {
        return this.mRanking.isAmbient();
    }

    public int getImportance() {
        return this.mRanking.getImportance();
    }

    public List<SnoozeCriterion> getSnoozeCriteria() {
        return this.mRanking.getSnoozeCriteria();
    }

    public int getUserSentiment() {
        return this.mRanking.getUserSentiment();
    }

    public int getSuppressedVisualEffects() {
        return this.mRanking.getSuppressedVisualEffects();
    }

    public boolean canBubble() {
        return this.mRanking.canBubble();
    }

    public List<Notification.Action> getSmartActions() {
        return this.mRanking.getSmartActions();
    }

    public List<CharSequence> getSmartReplies() {
        return this.mRanking.getSmartReplies();
    }

    public IconPack getIcons() {
        return this.mIcons;
    }

    public void setIcons(IconPack iconPack) {
        this.mIcons = iconPack;
    }

    public void setInterruption() {
        this.interruption = true;
    }

    public boolean hasInterrupted() {
        return this.interruption;
    }

    public boolean isBubble() {
        return (this.mSbn.getNotification().flags & 4096) != 0;
    }

    public Notification.BubbleMetadata getBubbleMetadata() {
        return this.mBubbleMetadata;
    }

    public void setBubbleMetadata(Notification.BubbleMetadata bubbleMetadata) {
        this.mBubbleMetadata = bubbleMetadata;
    }

    public boolean setFlagBubble(boolean z) {
        boolean isBubble = isBubble();
        if (!z) {
            this.mSbn.getNotification().flags &= -4097;
        } else if (this.mBubbleMetadata != null && canBubble()) {
            this.mSbn.getNotification().flags |= 4096;
        }
        return isBubble != isBubble();
    }

    public int getBucket() {
        return this.mBucket;
    }

    public void setBucket(int i) {
        this.mBucket = i;
    }

    public ExpandableNotificationRow getRow() {
        return this.row;
    }

    public void setRow(ExpandableNotificationRow expandableNotificationRow) {
        this.row = expandableNotificationRow;
    }

    public ExpandableNotificationRowController getRowController() {
        return this.mRowController;
    }

    public void setRowController(ExpandableNotificationRowController expandableNotificationRowController) {
        this.mRowController = expandableNotificationRowController;
    }

    public List<NotificationEntry> getAttachedNotifChildren() {
        List<ExpandableNotificationRow> attachedChildren;
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow == null || (attachedChildren = expandableNotificationRow.getAttachedChildren()) == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ExpandableNotificationRow entry : attachedChildren) {
            arrayList.add(entry.getEntry());
        }
        return arrayList;
    }

    public void notifyFullScreenIntentLaunched() {
        setInterruption();
        this.lastFullScreenIntentLaunchTime = SystemClock.elapsedRealtime();
    }

    public boolean hasJustLaunchedFullScreenIntent() {
        return SystemClock.elapsedRealtime() < this.lastFullScreenIntentLaunchTime + 2000;
    }

    public boolean hasJustSentRemoteInput() {
        return SystemClock.elapsedRealtime() < this.lastRemoteInputSent + 500;
    }

    public boolean hasFinishedInitialization() {
        return this.initializationTime != -1 && SystemClock.elapsedRealtime() > this.initializationTime + INITIALIZATION_DELAY;
    }

    public int getContrastedColor(Context context, boolean z, int i) {
        int i2;
        int i3 = z ? 0 : this.mSbn.getNotification().color;
        if (this.mCachedContrastColorIsFor == i3 && (i2 = this.mCachedContrastColor) != 1) {
            return i2;
        }
        int resolveContrastColor = ContrastColorUtil.resolveContrastColor(context, i3, i);
        this.mCachedContrastColorIsFor = i3;
        this.mCachedContrastColor = resolveContrastColor;
        return resolveContrastColor;
    }

    public void abortTask() {
        InflationTask inflationTask = this.mRunningTask;
        if (inflationTask != null) {
            inflationTask.abort();
            this.mRunningTask = null;
        }
    }

    public void setInflationTask(InflationTask inflationTask) {
        abortTask();
        this.mRunningTask = inflationTask;
    }

    public void onInflationTaskFinished() {
        this.mRunningTask = null;
    }

    public InflationTask getRunningTask() {
        return this.mRunningTask;
    }

    public void setDebugThrowable(Throwable th) {
        this.mDebugThrowable = th;
    }

    public Throwable getDebugThrowable() {
        return this.mDebugThrowable;
    }

    public void onRemoteInputInserted() {
        this.lastRemoteInputSent = NOT_LAUNCHED_YET;
        this.remoteInputTextWhenReset = null;
    }

    public void setHasSentReply() {
        this.hasSentReply = true;
    }

    public boolean isLastMessageFromReply() {
        Notification.MessagingStyle.Message message;
        if (!this.hasSentReply) {
            return false;
        }
        Bundle bundle = this.mSbn.getNotification().extras;
        if (!ArrayUtils.isEmpty(bundle.getParcelableArray("android.remoteInputHistoryItems"))) {
            return true;
        }
        List messagesFromBundleArray = Notification.MessagingStyle.Message.getMessagesFromBundleArray(bundle.getParcelableArray(NotificationCompat.EXTRA_MESSAGES));
        if (messagesFromBundleArray == null || messagesFromBundleArray.isEmpty() || (message = (Notification.MessagingStyle.Message) messagesFromBundleArray.get(messagesFromBundleArray.size() - 1)) == null) {
            return false;
        }
        Person senderPerson = message.getSenderPerson();
        if (senderPerson == null) {
            return true;
        }
        return Objects.equals((Person) bundle.getParcelable("android.messagingUser", Person.class), senderPerson);
    }

    public void resetInitializationTime() {
        this.initializationTime = -1;
    }

    public void setInitializationTime(long j) {
        if (this.initializationTime == -1) {
            this.initializationTime = j;
        }
    }

    public void sendAccessibilityEvent(int i) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.sendAccessibilityEvent(i);
        }
    }

    public boolean isMediaNotification() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow == null) {
            return false;
        }
        return expandableNotificationRow.isMediaRow();
    }

    public boolean isTopLevelChild() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isTopLevelChild();
    }

    public void resetUserExpansion() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.resetUserExpansion();
        }
    }

    public boolean rowExists() {
        return this.row != null;
    }

    public boolean isRowDismissed() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isDismissed();
    }

    public boolean isRowRemoved() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isRemoved();
    }

    public boolean isRemoved() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow == null || expandableNotificationRow.isRemoved();
    }

    public boolean isRowPinned() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isPinned();
    }

    public boolean isPinnedAndExpanded() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isPinnedAndExpanded();
    }

    public void setRowPinned(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setPinned(z);
        }
    }

    public boolean isRowHeadsUp() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isHeadsUp();
    }

    public boolean showingPulsing() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.showingPulsing();
    }

    public void setHeadsUp(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setHeadsUp(z);
        }
    }

    public void setHeadsUpAnimatingAway(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setHeadsUpAnimatingAway(z);
        }
    }

    public boolean mustStayOnScreen() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.mustStayOnScreen();
    }

    public void setHeadsUpIsVisible() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setHeadsUpIsVisible();
        }
    }

    public ExpandableNotificationRow getHeadsUpAnimationView() {
        return this.row;
    }

    public void setUserLocked(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setUserLocked(z);
        }
    }

    public void setUserExpanded(boolean z, boolean z2) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setUserExpanded(z, z2);
        }
    }

    public void setGroupExpansionChanging(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setGroupExpansionChanging(z);
        }
    }

    public void notifyHeightChanged(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.notifyHeightChanged(z);
        }
    }

    public void closeRemoteInput() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.closeRemoteInput();
        }
    }

    public boolean areChildrenExpanded() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.areChildrenExpanded();
    }

    public boolean keepInParent() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.keepInParent();
    }

    public boolean isGroupNotFullyVisible() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow == null || expandableNotificationRow.isGroupNotFullyVisible();
    }

    public NotificationGuts getGuts() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            return expandableNotificationRow.getGuts();
        }
        return null;
    }

    public void removeRow() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setRemoved();
        }
    }

    public boolean isSummaryWithChildren() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isSummaryWithChildren();
    }

    public void setKeepInParent(boolean z) {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setKeepInParent(z);
        }
    }

    public void onDensityOrFontScaleChanged() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.onDensityOrFontScaleChanged();
        }
    }

    public boolean areGutsExposed() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return (expandableNotificationRow == null || expandableNotificationRow.getGuts() == null || !this.row.getGuts().isExposed()) ? false : true;
    }

    public boolean isChildInGroup() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        return expandableNotificationRow != null && expandableNotificationRow.isChildInGroup();
    }

    public boolean isClearable() {
        if (!this.mSbn.isClearable()) {
            return false;
        }
        List<NotificationEntry> attachedNotifChildren = getAttachedNotifChildren();
        if (attachedNotifChildren == null || attachedNotifChildren.size() <= 0) {
            return true;
        }
        for (int i = 0; i < attachedNotifChildren.size(); i++) {
            if (!attachedNotifChildren.get(i).getSbn().isClearable()) {
                return false;
            }
        }
        return true;
    }

    public boolean isDismissable() {
        if (this.mSbn.isOngoing()) {
            return false;
        }
        List<NotificationEntry> attachedNotifChildren = getAttachedNotifChildren();
        if (attachedNotifChildren == null || attachedNotifChildren.size() <= 0) {
            return true;
        }
        for (int i = 0; i < attachedNotifChildren.size(); i++) {
            if (attachedNotifChildren.get(i).getSbn().isOngoing()) {
                return false;
            }
        }
        return true;
    }

    public boolean canViewBeDismissed() {
        ExpandableNotificationRow expandableNotificationRow = this.row;
        if (expandableNotificationRow == null) {
            return true;
        }
        return expandableNotificationRow.canViewBeDismissed();
    }

    /* access modifiers changed from: package-private */
    public boolean isExemptFromDndVisualSuppression() {
        if (isNotificationBlockedByPolicy(this.mSbn.getNotification())) {
            return false;
        }
        if ((this.mSbn.getNotification().flags & 64) == 0 && !this.mSbn.getNotification().isMediaNotification() && isBlockable()) {
            return false;
        }
        return true;
    }

    public boolean isBlockable() {
        if (getChannel() == null) {
            return false;
        }
        if (!getChannel().isImportanceLockedByCriticalDeviceFunction() || getChannel().isBlockable()) {
            return true;
        }
        return false;
    }

    private boolean shouldSuppressVisualEffect(int i) {
        if (!isExemptFromDndVisualSuppression() && (getSuppressedVisualEffects() & i) != 0) {
            return true;
        }
        return false;
    }

    public boolean shouldSuppressFullScreenIntent() {
        return shouldSuppressVisualEffect(4);
    }

    public boolean shouldSuppressPeek() {
        return shouldSuppressVisualEffect(16);
    }

    public boolean shouldSuppressStatusBar() {
        return shouldSuppressVisualEffect(32);
    }

    public boolean shouldSuppressAmbient() {
        return shouldSuppressVisualEffect(128);
    }

    public boolean shouldSuppressNotificationList() {
        return shouldSuppressVisualEffect(256);
    }

    public boolean shouldSuppressNotificationDot() {
        return shouldSuppressVisualEffect(64);
    }

    private static boolean isNotificationBlockedByPolicy(Notification notification) {
        return isCategory(NotificationCompat.CATEGORY_CALL, notification) || isCategory(NotificationCompat.CATEGORY_MESSAGE, notification) || isCategory(NotificationCompat.CATEGORY_ALARM, notification) || isCategory(NotificationCompat.CATEGORY_EVENT, notification) || isCategory(NotificationCompat.CATEGORY_REMINDER, notification);
    }

    private static boolean isCategory(String str, Notification notification) {
        return Objects.equals(notification.category, str);
    }

    public void setSensitive(boolean z, boolean z2) {
        getRow().setSensitive(z, z2);
        if (z != this.mSensitive) {
            this.mSensitive = z;
            for (int i = 0; i < this.mOnSensitivityChangedListeners.size(); i++) {
                this.mOnSensitivityChangedListeners.get(i).onSensitivityChanged(this);
            }
        }
    }

    public boolean isSensitive() {
        return this.mSensitive;
    }

    public void addOnSensitivityChangedListener(OnSensitivityChangedListener onSensitivityChangedListener) {
        this.mOnSensitivityChangedListeners.add(onSensitivityChangedListener);
    }

    public void removeOnSensitivityChangedListener(OnSensitivityChangedListener onSensitivityChangedListener) {
        this.mOnSensitivityChangedListeners.remove((Object) onSensitivityChangedListener);
    }

    public boolean isPulseSuppressed() {
        return this.mPulseSupressed;
    }

    public void setPulseSuppressed(boolean z) {
        this.mPulseSupressed = z;
    }

    public boolean isMarkedForUserTriggeredMovement() {
        return this.mIsMarkedForUserTriggeredMovement;
    }

    public void markForUserTriggeredMovement(boolean z) {
        this.mIsMarkedForUserTriggeredMovement = z;
    }

    public void setIsAlerting(boolean z) {
        this.mIsAlerting = z;
    }

    public boolean isAlerting() {
        return this.mIsAlerting;
    }

    public void setExpandAnimationRunning(boolean z) {
        this.mExpandAnimationRunning = z;
    }

    public boolean isExpandAnimationRunning() {
        return this.mExpandAnimationRunning;
    }

    public static class EditedSuggestionInfo {
        public final int index;
        public final CharSequence originalText;

        public EditedSuggestionInfo(CharSequence charSequence, int i) {
            this.originalText = charSequence;
            this.index = i;
        }
    }
}
