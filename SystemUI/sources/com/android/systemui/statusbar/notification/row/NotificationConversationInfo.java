package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.notification.ConversationIconFactory;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.statusbar.notification.NotificationChannelHelper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.wmshell.BubblesManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

public class NotificationConversationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    static final int ACTION_DEFAULT = 0;
    static final int ACTION_FAVORITE = 2;
    static final int ACTION_HOME = 1;
    static final int ACTION_MUTE = 4;
    static final int ACTION_SETTINGS = 5;
    static final int ACTION_SNOOZE = 3;
    private static final String TAG = "ConversationGuts";
    private int mActualHeight;
    /* access modifiers changed from: private */
    public int mAppBubble;
    private String mAppName;
    private int mAppUid;
    private Handler mBgHandler;
    private Notification.BubbleMetadata mBubbleMetadata;
    /* access modifiers changed from: private */
    public Optional<BubblesManager> mBubblesManagerOptional;
    private TextView mDefaultDescriptionView;
    private String mDelegatePkg;
    /* access modifiers changed from: private */
    public NotificationEntry mEntry;
    private NotificationGuts mGutsContainer;
    private INotificationManager mINotificationManager;
    private ConversationIconFactory mIconFactory;
    private boolean mIsDeviceProvisioned;
    private Handler mMainHandler;
    private NotificationChannel mNotificationChannel;
    private OnConversationSettingsClickListener mOnConversationSettingsClickListener;
    private View.OnClickListener mOnDefaultClick = new NotificationConversationInfo$$ExternalSyntheticLambda5(this);
    private View.OnClickListener mOnDone = new NotificationConversationInfo$$ExternalSyntheticLambda7(this);
    private View.OnClickListener mOnFavoriteClick = new NotificationConversationInfo$$ExternalSyntheticLambda4(this);
    private View.OnClickListener mOnMuteClick = new NotificationConversationInfo$$ExternalSyntheticLambda6(this);
    private OnSettingsClickListener mOnSettingsClickListener;
    private OnUserInteractionCallback mOnUserInteractionCallback;
    private String mPackageName;
    private PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    private PackageManager mPm;
    private boolean mPressedApply;
    private TextView mPriorityDescriptionView;
    private StatusBarNotification mSbn;
    private int mSelectedAction = -1;
    private ShadeController mShadeController;
    private ShortcutInfo mShortcutInfo;
    private ShortcutManager mShortcutManager;
    private TextView mSilentDescriptionView;
    boolean mSkipPost = false;
    private Context mUserContext;

    @Retention(RetentionPolicy.SOURCE)
    private @interface Action {
    }

    public interface OnAppSettingsClickListener {
        void onClick(View view, Intent intent);
    }

    public interface OnConversationSettingsClickListener {
        void onClick();
    }

    public interface OnSettingsClickListener {
        void onClick(View view, NotificationChannel notificationChannel, int i);
    }

    public View getContentView() {
        return this;
    }

    public boolean isAnimating() {
        return false;
    }

    public boolean needsFalsingProtection() {
        return true;
    }

    public boolean willBeRemoved() {
        return false;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-notification-row-NotificationConversationInfo */
    public /* synthetic */ void mo41542xcc6fd7da(View view) {
        setSelectedAction(2);
        updateToggleActions(this.mSelectedAction, true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-notification-row-NotificationConversationInfo */
    public /* synthetic */ void mo41543xe5712979(View view) {
        setSelectedAction(0);
        updateToggleActions(this.mSelectedAction, true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-statusbar-notification-row-NotificationConversationInfo */
    public /* synthetic */ void mo41544xfe727b18(View view) {
        setSelectedAction(4);
        updateToggleActions(this.mSelectedAction, true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-statusbar-notification-row-NotificationConversationInfo */
    public /* synthetic */ void mo41545x1773ccb7(View view) {
        this.mPressedApply = true;
        if (this.mSelectedAction == 2 && getPriority() != this.mSelectedAction) {
            this.mShadeController.animateCollapsePanels();
            this.mPeopleSpaceWidgetManager.requestPinAppWidget(this.mShortcutInfo, new Bundle());
        }
        this.mGutsContainer.closeControls(view, true);
    }

    public NotificationConversationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public void setSelectedAction(int i) {
        if (this.mSelectedAction != i) {
            this.mSelectedAction = i;
        }
    }

    public void bindNotification(int i, ShortcutManager shortcutManager, PackageManager packageManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, INotificationManager iNotificationManager, OnUserInteractionCallback onUserInteractionCallback, String str, NotificationChannel notificationChannel, NotificationEntry notificationEntry, Notification.BubbleMetadata bubbleMetadata, OnSettingsClickListener onSettingsClickListener, ConversationIconFactory conversationIconFactory, Context context, boolean z, @Main Handler handler, @Background Handler handler2, OnConversationSettingsClickListener onConversationSettingsClickListener, Optional<BubblesManager> optional, ShadeController shadeController) {
        this.mPressedApply = false;
        this.mSelectedAction = i;
        this.mINotificationManager = iNotificationManager;
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mPackageName = str;
        this.mEntry = notificationEntry;
        StatusBarNotification sbn = notificationEntry.getSbn();
        this.mSbn = sbn;
        this.mPm = packageManager;
        this.mAppName = this.mPackageName;
        this.mOnSettingsClickListener = onSettingsClickListener;
        this.mNotificationChannel = notificationChannel;
        this.mAppUid = sbn.getUid();
        this.mDelegatePkg = this.mSbn.getOpPkg();
        this.mIsDeviceProvisioned = z;
        this.mOnConversationSettingsClickListener = onConversationSettingsClickListener;
        this.mIconFactory = conversationIconFactory;
        this.mUserContext = context;
        this.mBubbleMetadata = bubbleMetadata;
        this.mBubblesManagerOptional = optional;
        this.mShadeController = shadeController;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mShortcutManager = shortcutManager;
        ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
        this.mShortcutInfo = conversationShortcutInfo;
        if (conversationShortcutInfo != null) {
            this.mNotificationChannel = NotificationChannelHelper.createConversationChannelIfNeeded(getContext(), this.mINotificationManager, notificationEntry, this.mNotificationChannel);
            try {
                this.mAppBubble = this.mINotificationManager.getBubblePreferenceForPackage(this.mPackageName, this.mAppUid);
            } catch (RemoteException e) {
                Log.e(TAG, "can't reach OS", e);
                this.mAppBubble = 2;
            }
            bindHeader();
            bindActions();
            View findViewById = findViewById(C1894R.C1898id.done);
            findViewById.setOnClickListener(this.mOnDone);
            findViewById.setAccessibilityDelegate(this.mGutsContainer.getAccessibilityDelegate());
            return;
        }
        throw new IllegalArgumentException("Does not have required information");
    }

    private void bindActions() {
        if (this.mAppBubble == 1) {
            ((TextView) findViewById(C1894R.C1898id.default_summary)).setText(getResources().getString(C1894R.string.notification_channel_summary_default_with_bubbles, new Object[]{this.mAppName}));
        }
        findViewById(C1894R.C1898id.priority).setOnClickListener(this.mOnFavoriteClick);
        findViewById(C1894R.C1898id.default_behavior).setOnClickListener(this.mOnDefaultClick);
        findViewById(C1894R.C1898id.silence).setOnClickListener(this.mOnMuteClick);
        View findViewById = findViewById(C1894R.C1898id.info);
        findViewById.setOnClickListener(getSettingsOnClickListener());
        findViewById.setVisibility(findViewById.hasOnClickListeners() ? 0 : 8);
        int i = this.mSelectedAction;
        if (i == -1) {
            i = getPriority();
        }
        updateToggleActions(i, false);
    }

    private void bindHeader() {
        bindConversationDetails();
        bindDelegate();
    }

    private View.OnClickListener getSettingsOnClickListener() {
        int i = this.mAppUid;
        if (i < 0 || this.mOnSettingsClickListener == null || !this.mIsDeviceProvisioned) {
            return null;
        }
        return new NotificationConversationInfo$$ExternalSyntheticLambda0(this, i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getSettingsOnClickListener$4$com-android-systemui-statusbar-notification-row-NotificationConversationInfo */
    public /* synthetic */ void mo41541x24e356f8(int i, View view) {
        this.mOnSettingsClickListener.onClick(view, this.mNotificationChannel, i);
    }

    private void bindConversationDetails() {
        ((TextView) findViewById(C1894R.C1898id.parent_channel_name)).setText(this.mNotificationChannel.getName());
        bindGroup();
        bindPackage();
        bindIcon(this.mNotificationChannel.isImportantConversation());
        this.mPriorityDescriptionView = (TextView) findViewById(C1894R.C1898id.priority_summary);
        if (willShowAsBubble() && willBypassDnd()) {
            this.mPriorityDescriptionView.setText(C1894R.string.notification_channel_summary_priority_all);
        } else if (willShowAsBubble()) {
            this.mPriorityDescriptionView.setText(C1894R.string.notification_channel_summary_priority_bubble);
        } else if (willBypassDnd()) {
            this.mPriorityDescriptionView.setText(C1894R.string.notification_channel_summary_priority_dnd);
        } else {
            this.mPriorityDescriptionView.setText(C1894R.string.notification_channel_summary_priority_baseline);
        }
    }

    private void bindIcon(boolean z) {
        Drawable baseIconDrawable = this.mIconFactory.getBaseIconDrawable(this.mShortcutInfo);
        int i = 0;
        if (baseIconDrawable == null) {
            baseIconDrawable = this.mContext.getDrawable(C1894R.C1896drawable.ic_person).mutate();
            TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16843829});
            int color = obtainStyledAttributes.getColor(0, 0);
            obtainStyledAttributes.recycle();
            baseIconDrawable.setTint(color);
        }
        ((ImageView) findViewById(C1894R.C1898id.conversation_icon)).setImageDrawable(baseIconDrawable);
        ((ImageView) findViewById(C1894R.C1898id.conversation_icon_badge_icon)).setImageDrawable(this.mIconFactory.getAppBadge(this.mPackageName, UserHandle.getUserId(this.mSbn.getUid())));
        View findViewById = findViewById(C1894R.C1898id.conversation_icon_badge_ring);
        if (!z) {
            i = 8;
        }
        findViewById.setVisibility(i);
    }

    private void bindPackage() {
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(this.mPackageName, 795136);
            if (applicationInfo != null) {
                this.mAppName = String.valueOf((Object) this.mPm.getApplicationLabel(applicationInfo));
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        ((TextView) findViewById(C1894R.C1898id.pkg_name)).setText(this.mAppName);
    }

    private void bindDelegate() {
        TextView textView = (TextView) findViewById(C1894R.C1898id.delegate_name);
        if (!TextUtils.equals(this.mPackageName, this.mDelegatePkg)) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
    }

    private void bindGroup() {
        NotificationChannel notificationChannel = this.mNotificationChannel;
        CharSequence charSequence = null;
        if (!(notificationChannel == null || notificationChannel.getGroup() == null)) {
            try {
                NotificationChannelGroup notificationChannelGroupForPackage = this.mINotificationManager.getNotificationChannelGroupForPackage(this.mNotificationChannel.getGroup(), this.mPackageName, this.mAppUid);
                if (notificationChannelGroupForPackage != null) {
                    charSequence = notificationChannelGroupForPackage.getName();
                }
            } catch (RemoteException unused) {
            }
        }
        TextView textView = (TextView) findViewById(C1894R.C1898id.group_name);
        if (charSequence != null) {
            textView.setText(charSequence);
            textView.setVisibility(0);
            return;
        }
        textView.setVisibility(8);
    }

    public boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDefaultDescriptionView = (TextView) findViewById(C1894R.C1898id.default_summary);
        this.mSilentDescriptionView = (TextView) findViewById(C1894R.C1898id.silence_summary);
    }

    public void onFinishedClosing() {
        this.mSelectedAction = -1;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.mGutsContainer != null && accessibilityEvent.getEventType() == 32) {
            if (this.mGutsContainer.isExposed()) {
                accessibilityEvent.getText().add(this.mContext.getString(C1894R.string.notification_channel_controls_opened_accessibility, new Object[]{this.mAppName}));
                return;
            }
            accessibilityEvent.getText().add(this.mContext.getString(C1894R.string.notification_channel_controls_closed_accessibility, new Object[]{this.mAppName}));
        }
    }

    private void updateToggleActions(int i, boolean z) {
        boolean z2 = true;
        if (z) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(0);
            transitionSet.addTransition(new Fade(2)).addTransition(new ChangeBounds()).addTransition(new Fade(1).setStartDelay(150).setDuration(200).setInterpolator(Interpolators.FAST_OUT_SLOW_IN));
            transitionSet.setDuration(350);
            transitionSet.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        View findViewById = findViewById(C1894R.C1898id.priority);
        View findViewById2 = findViewById(C1894R.C1898id.default_behavior);
        View findViewById3 = findViewById(C1894R.C1898id.silence);
        if (i == 0) {
            this.mDefaultDescriptionView.setVisibility(0);
            this.mSilentDescriptionView.setVisibility(8);
            this.mPriorityDescriptionView.setVisibility(8);
            post(new NotificationConversationInfo$$ExternalSyntheticLambda3(findViewById, findViewById2, findViewById3));
        } else if (i == 2) {
            this.mPriorityDescriptionView.setVisibility(0);
            this.mDefaultDescriptionView.setVisibility(8);
            this.mSilentDescriptionView.setVisibility(8);
            post(new NotificationConversationInfo$$ExternalSyntheticLambda1(findViewById, findViewById2, findViewById3));
        } else if (i == 4) {
            this.mSilentDescriptionView.setVisibility(0);
            this.mDefaultDescriptionView.setVisibility(8);
            this.mPriorityDescriptionView.setVisibility(8);
            post(new NotificationConversationInfo$$ExternalSyntheticLambda2(findViewById, findViewById2, findViewById3));
        } else {
            throw new IllegalArgumentException("Unrecognized behavior: " + this.mSelectedAction);
        }
        ((TextView) findViewById(C1894R.C1898id.done)).setText(getPriority() != i ? C1894R.string.inline_ok_button : C1894R.string.inline_done_button);
        if (i != 2) {
            z2 = false;
        }
        bindIcon(z2);
    }

    static /* synthetic */ void lambda$updateToggleActions$5(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    static /* synthetic */ void lambda$updateToggleActions$6(View view, View view2, View view3) {
        view.setSelected(false);
        view2.setSelected(false);
        view3.setSelected(true);
    }

    static /* synthetic */ void lambda$updateToggleActions$7(View view, View view2, View view3) {
        view.setSelected(false);
        view2.setSelected(true);
        view3.setSelected(false);
    }

    /* access modifiers changed from: package-private */
    public int getSelectedAction() {
        return this.mSelectedAction;
    }

    private int getPriority() {
        if (this.mNotificationChannel.getImportance() <= 2 && this.mNotificationChannel.getImportance() > -1000) {
            return 4;
        }
        if (this.mNotificationChannel.isImportantConversation()) {
            return 2;
        }
        return 0;
    }

    private void updateChannel() {
        this.mBgHandler.post(new UpdateChannelRunnable(this.mINotificationManager, this.mPackageName, this.mAppUid, this.mSelectedAction, this.mNotificationChannel));
        this.mEntry.markForUserTriggeredMovement(true);
        this.mMainHandler.postDelayed(new NotificationConversationInfo$$ExternalSyntheticLambda8(this), 360);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateChannel$8$com-android-systemui-statusbar-notification-row-NotificationConversationInfo */
    public /* synthetic */ void mo41546x560aeef8() {
        this.mOnUserInteractionCallback.onImportanceChanged(this.mEntry);
    }

    private boolean willBypassDnd() {
        try {
            int i = this.mINotificationManager.getConsolidatedNotificationPolicy().priorityConversationSenders;
            if (i == 2 || i == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Could not check conversation senders", e);
            return false;
        }
    }

    private boolean willShowAsBubble() {
        return this.mBubbleMetadata != null && BubblesManager.areBubblesEnabled(this.mContext, this.mSbn.getUser());
    }

    public void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    public boolean shouldBeSaved() {
        return this.mPressedApply;
    }

    public boolean handleCloseControls(boolean z, boolean z2) {
        if (!z || this.mSelectedAction <= -1) {
            return false;
        }
        updateChannel();
        return false;
    }

    public int getActualHeight() {
        return this.mActualHeight;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mActualHeight = getHeight();
    }

    class UpdateChannelRunnable implements Runnable {
        private final int mAction;
        private final String mAppPkg;
        private final int mAppUid;
        private NotificationChannel mChannelToUpdate;
        private final INotificationManager mINotificationManager;

        public UpdateChannelRunnable(INotificationManager iNotificationManager, String str, int i, int i2, NotificationChannel notificationChannel) {
            this.mINotificationManager = iNotificationManager;
            this.mAppPkg = str;
            this.mAppUid = i;
            this.mChannelToUpdate = notificationChannel;
            this.mAction = i2;
        }

        public void run() {
            try {
                int i = this.mAction;
                if (i == 0) {
                    NotificationChannel notificationChannel = this.mChannelToUpdate;
                    notificationChannel.setImportance(Math.max(notificationChannel.getOriginalImportance(), 3));
                    if (this.mChannelToUpdate.isImportantConversation()) {
                        this.mChannelToUpdate.setImportantConversation(false);
                        this.mChannelToUpdate.setAllowBubbles(false);
                    }
                } else if (i == 2) {
                    this.mChannelToUpdate.setImportantConversation(true);
                    if (this.mChannelToUpdate.isImportantConversation()) {
                        this.mChannelToUpdate.setAllowBubbles(true);
                        if (NotificationConversationInfo.this.mAppBubble == 0) {
                            this.mINotificationManager.setBubblesAllowed(this.mAppPkg, this.mAppUid, 2);
                        }
                        if (NotificationConversationInfo.this.mBubblesManagerOptional.isPresent()) {
                            NotificationConversationInfo.this.post(new C2768xc525717e(this));
                        }
                    }
                    NotificationChannel notificationChannel2 = this.mChannelToUpdate;
                    notificationChannel2.setImportance(Math.max(notificationChannel2.getOriginalImportance(), 3));
                } else if (i == 4) {
                    if (this.mChannelToUpdate.getImportance() == -1000 || this.mChannelToUpdate.getImportance() >= 3) {
                        this.mChannelToUpdate.setImportance(2);
                    }
                    if (this.mChannelToUpdate.isImportantConversation()) {
                        this.mChannelToUpdate.setImportantConversation(false);
                        this.mChannelToUpdate.setAllowBubbles(false);
                    }
                }
                this.mINotificationManager.updateNotificationChannelForPackage(this.mAppPkg, this.mAppUid, this.mChannelToUpdate);
            } catch (RemoteException e) {
                Log.e(NotificationConversationInfo.TAG, "Unable to update notification channel", e);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-android-systemui-statusbar-notification-row-NotificationConversationInfo$UpdateChannelRunnable */
        public /* synthetic */ void mo41556xc57ee9c2() {
            ((BubblesManager) NotificationConversationInfo.this.mBubblesManagerOptional.get()).onUserSetImportantConversation(NotificationConversationInfo.this.mEntry);
        }
    }
}
