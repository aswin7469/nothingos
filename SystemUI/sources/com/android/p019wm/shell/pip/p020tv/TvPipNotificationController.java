package com.android.p019wm.shell.pip.p020tv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Handler;
import android.safetycenter.SafetyCenterStatus;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.android.internal.protolog.common.ProtoLog;
import com.android.internal.util.ImageUtils;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipUtils;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.android.wm.shell.pip.tv.TvPipNotificationController */
public class TvPipNotificationController {
    private static final String ACTION_CLOSE_PIP = "com.android.wm.shell.pip.tv.notification.action.CLOSE_PIP";
    private static final String ACTION_FULLSCREEN = "com.android.wm.shell.pip.tv.notification.action.FULLSCREEN";
    private static final String ACTION_MOVE_PIP = "com.android.wm.shell.pip.tv.notification.action.MOVE_PIP";
    private static final String ACTION_SHOW_PIP_MENU = "com.android.wm.shell.pip.tv.notification.action.SHOW_PIP_MENU";
    private static final String ACTION_TOGGLE_EXPANDED_PIP = "com.android.wm.shell.pip.tv.notification.action.TOGGLE_EXPANDED_PIP";
    public static final String NOTIFICATION_CHANNEL = "TVPIP";
    private static final String NOTIFICATION_TAG = "TvPip";
    private static final String SYSTEMUI_PERMISSION = "com.android.systemui.permission.SELF";
    private static final String TAG = "TvPipNotification";
    private final ActionBroadcastReceiver mActionBroadcastReceiver;
    private Bitmap mActivityIcon;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final List<RemoteAction> mCustomActions = new ArrayList();
    /* access modifiers changed from: private */
    public RemoteAction mCustomCloseAction;
    private String mDefaultTitle;
    /* access modifiers changed from: private */
    public Delegate mDelegate;
    private boolean mIsNotificationShown;
    /* access modifiers changed from: private */
    public final Handler mMainHandler;
    private final List<RemoteAction> mMediaActions = new ArrayList();
    private MediaSession.Token mMediaSessionToken;
    private final Notification.Builder mNotificationBuilder;
    private final NotificationManager mNotificationManager;
    private final PackageManager mPackageManager;
    private String mPackageName;
    /* access modifiers changed from: private */
    public String mPipSubtitle;
    /* access modifiers changed from: private */
    public String mPipTitle;
    private final TvPipBoundsState mTvPipBoundsState;

    /* renamed from: com.android.wm.shell.pip.tv.TvPipNotificationController$Delegate */
    interface Delegate {
        void closePip();

        void enterPipMovementMenu();

        void movePipToFullscreen();

        void showPictureInPictureMenu();

        void togglePipExpansion();
    }

    public TvPipNotificationController(Context context, PipMediaController pipMediaController, PipParamsChangedForwarder pipParamsChangedForwarder, TvPipBoundsState tvPipBoundsState, Handler handler) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mMainHandler = handler;
        this.mTvPipBoundsState = tvPipBoundsState;
        this.mNotificationBuilder = new Notification.Builder(context, NOTIFICATION_CHANNEL).setLocalOnly(true).setOngoing(true).setCategory(NotificationCompat.CATEGORY_SYSTEM).setShowWhen(true).setSmallIcon(C3353R.C3355drawable.pip_icon).setAllowSystemGeneratedContextualActions(false).setContentIntent(createPendingIntent(context, ACTION_FULLSCREEN)).setDeleteIntent(getCloseAction().actionIntent).extend(new Notification.TvExtender().setContentIntent(createPendingIntent(context, ACTION_SHOW_PIP_MENU)).setDeleteIntent(createPendingIntent(context, ACTION_CLOSE_PIP)));
        this.mActionBroadcastReceiver = new ActionBroadcastReceiver();
        pipMediaController.addActionListener(new TvPipNotificationController$$ExternalSyntheticLambda0(this));
        pipMediaController.addTokenListener(new TvPipNotificationController$$ExternalSyntheticLambda1(this));
        pipParamsChangedForwarder.addListener(new PipParamsChangedForwarder.PipParamsChangedCallback() {
            public void onExpandedAspectRatioChanged(float f) {
                TvPipNotificationController.this.updateExpansionState();
            }

            public void onActionsChanged(List<RemoteAction> list, RemoteAction remoteAction) {
                TvPipNotificationController.this.mCustomActions.clear();
                TvPipNotificationController.this.mCustomActions.addAll(list);
                RemoteAction unused = TvPipNotificationController.this.mCustomCloseAction = remoteAction;
                TvPipNotificationController.this.updateNotificationContent();
            }

            public void onTitleChanged(String str) {
                String unused = TvPipNotificationController.this.mPipTitle = str;
                TvPipNotificationController.this.updateNotificationContent();
            }

            public void onSubtitleChanged(String str) {
                String unused = TvPipNotificationController.this.mPipSubtitle = str;
                TvPipNotificationController.this.updateNotificationContent();
            }
        });
        onConfigurationChanged(context);
    }

    /* access modifiers changed from: package-private */
    public void setDelegate(Delegate delegate) {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: setDelegate(), delegate=%s", new Object[]{TAG, delegate});
        if (this.mDelegate != null) {
            throw new IllegalStateException("The delegate has already been set and should not change.");
        } else if (delegate != null) {
            this.mDelegate = delegate;
        } else {
            throw new IllegalArgumentException("The delegate must not be null.");
        }
    }

    /* access modifiers changed from: package-private */
    public void show(String str) {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: show %s", new Object[]{TAG, str});
        if (this.mDelegate != null) {
            this.mIsNotificationShown = true;
            this.mPackageName = str;
            this.mActivityIcon = getActivityIcon();
            this.mActionBroadcastReceiver.register();
            updateNotificationContent();
            return;
        }
        throw new IllegalStateException("Delegate is not set.");
    }

    /* access modifiers changed from: package-private */
    public void dismiss() {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: dismiss()", new Object[]{TAG});
        this.mIsNotificationShown = false;
        this.mPackageName = null;
        this.mActionBroadcastReceiver.unregister();
        this.mNotificationManager.cancel(NOTIFICATION_TAG, SafetyCenterStatus.OVERALL_SEVERITY_LEVEL_OK);
    }

    private Notification.Action getToggleAction(boolean z) {
        if (z) {
            return createSystemAction(C3353R.C3355drawable.pip_ic_collapse, C3353R.string.pip_collapse, ACTION_TOGGLE_EXPANDED_PIP);
        }
        return createSystemAction(C3353R.C3355drawable.pip_ic_expand, C3353R.string.pip_expand, ACTION_TOGGLE_EXPANDED_PIP);
    }

    private Notification.Action createSystemAction(int i, int i2, String str) {
        Notification.Action.Builder builder = new Notification.Action.Builder(Icon.createWithResource(this.mContext, i), this.mContext.getString(i2), createPendingIntent(this.mContext, str));
        builder.setContextual(true);
        return builder.build();
    }

    /* access modifiers changed from: private */
    public void onMediaActionsChanged(List<RemoteAction> list) {
        this.mMediaActions.clear();
        this.mMediaActions.addAll(list);
        if (this.mCustomActions.isEmpty()) {
            updateNotificationContent();
        }
    }

    /* access modifiers changed from: private */
    public void onMediaSessionTokenChanged(MediaSession.Token token) {
        this.mMediaSessionToken = token;
        updateNotificationContent();
    }

    private Notification.Action remoteToNotificationAction(RemoteAction remoteAction) {
        return remoteToNotificationAction(remoteAction, 0);
    }

    private Notification.Action remoteToNotificationAction(RemoteAction remoteAction, int i) {
        Notification.Action.Builder builder = new Notification.Action.Builder(remoteAction.getIcon(), remoteAction.getTitle(), remoteAction.getActionIntent());
        if (remoteAction.getContentDescription() != null) {
            Bundle bundle = new Bundle();
            bundle.putCharSequence(NotificationCompat.EXTRA_PICTURE_CONTENT_DESCRIPTION, remoteAction.getContentDescription());
            builder.addExtras(bundle);
        }
        builder.setSemanticAction(i);
        builder.setContextual(true);
        return builder.build();
    }

    private Notification.Action[] getNotificationActions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(getFullscreenAction());
        arrayList.add(getCloseAction());
        for (RemoteAction next : this.mCustomActions.isEmpty() ? this.mMediaActions : this.mCustomActions) {
            if (!PipUtils.remoteActionsMatch(this.mCustomCloseAction, next) && next.isEnabled()) {
                arrayList.add(remoteToNotificationAction(next));
            }
        }
        arrayList.add(getMoveAction());
        if (this.mTvPipBoundsState.getDesiredTvExpandedAspectRatio() > 0.0f && this.mTvPipBoundsState.isTvExpandedPipSupported()) {
            arrayList.add(getToggleAction(this.mTvPipBoundsState.isTvPipExpanded()));
        }
        return (Notification.Action[]) arrayList.toArray(new Notification.Action[0]);
    }

    private Notification.Action getCloseAction() {
        RemoteAction remoteAction = this.mCustomCloseAction;
        if (remoteAction == null) {
            return createSystemAction(C3353R.C3355drawable.pip_ic_close_white, C3353R.string.pip_close, ACTION_CLOSE_PIP);
        }
        return remoteToNotificationAction(remoteAction, 4);
    }

    private Notification.Action getFullscreenAction() {
        return createSystemAction(C3353R.C3355drawable.pip_ic_fullscreen_white, C3353R.string.pip_fullscreen, ACTION_FULLSCREEN);
    }

    private Notification.Action getMoveAction() {
        return createSystemAction(C3353R.C3355drawable.pip_ic_move_white, C3353R.string.pip_move, ACTION_MOVE_PIP);
    }

    /* access modifiers changed from: package-private */
    public void onConfigurationChanged(Context context) {
        this.mDefaultTitle = context.getResources().getString(C3353R.string.pip_notification_unknown_title);
        updateNotificationContent();
    }

    /* access modifiers changed from: package-private */
    public void updateExpansionState() {
        updateNotificationContent();
    }

    /* access modifiers changed from: private */
    public void updateNotificationContent() {
        if (this.mPackageManager != null && this.mIsNotificationShown) {
            Notification.Action[] notificationActions = getNotificationActions();
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: update(), title: %s, subtitle: %s, mediaSessionToken: %s, #actions: %s", new Object[]{TAG, getNotificationTitle(), this.mPipSubtitle, this.mMediaSessionToken, Integer.valueOf(notificationActions.length)});
            for (Notification.Action obj : notificationActions) {
                ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: action: %s", new Object[]{TAG, obj.toString()});
            }
            this.mNotificationBuilder.setWhen(System.currentTimeMillis()).setContentTitle(getNotificationTitle()).setContentText(this.mPipSubtitle).setSubText(getApplicationLabel(this.mPackageName)).setActions(notificationActions);
            setPipIcon();
            Bundle bundle = new Bundle();
            bundle.putParcelable(NotificationCompat.EXTRA_MEDIA_SESSION, this.mMediaSessionToken);
            this.mNotificationBuilder.setExtras(bundle);
            this.mNotificationBuilder.extend(new Notification.TvExtender().setContentIntent(createPendingIntent(this.mContext, ACTION_SHOW_PIP_MENU)).setDeleteIntent(createPendingIntent(this.mContext, ACTION_CLOSE_PIP)));
            this.mNotificationManager.notify(NOTIFICATION_TAG, SafetyCenterStatus.OVERALL_SEVERITY_LEVEL_OK, this.mNotificationBuilder.build());
        }
    }

    private String getNotificationTitle() {
        if (!TextUtils.isEmpty(this.mPipTitle)) {
            return this.mPipTitle;
        }
        String applicationLabel = getApplicationLabel(this.mPackageName);
        if (!TextUtils.isEmpty(applicationLabel)) {
            return applicationLabel;
        }
        return this.mDefaultTitle;
    }

    private String getApplicationLabel(String str) {
        try {
            return this.mPackageManager.getApplicationLabel(this.mPackageManager.getApplicationInfo(str, 0)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    private void setPipIcon() {
        Bitmap bitmap = this.mActivityIcon;
        if (bitmap != null) {
            this.mNotificationBuilder.setLargeIcon(bitmap);
        } else {
            this.mNotificationBuilder.setLargeIcon(Icon.createWithResource(this.mContext, C3353R.C3355drawable.pip_icon));
        }
    }

    private Bitmap getActivityIcon() {
        ComponentName componentName;
        Context context = this.mContext;
        if (context == null || (componentName = (ComponentName) PipUtils.getTopPipActivity(context).first) == null) {
            return null;
        }
        try {
            return ImageUtils.buildScaledBitmap(this.mPackageManager.getActivityIcon(componentName), this.mContext.getResources().getDimensionPixelSize(17104901), this.mContext.getResources().getDimensionPixelSize(17104902), true);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    private static PendingIntent createPendingIntent(Context context, String str) {
        return PendingIntent.getBroadcast(context, 0, new Intent(str).setPackage(context.getPackageName()), 201326592);
    }

    /* renamed from: com.android.wm.shell.pip.tv.TvPipNotificationController$ActionBroadcastReceiver */
    private class ActionBroadcastReceiver extends BroadcastReceiver {
        final IntentFilter mIntentFilter;
        boolean mRegistered;

        private ActionBroadcastReceiver() {
            IntentFilter intentFilter = new IntentFilter();
            this.mIntentFilter = intentFilter;
            intentFilter.addAction(TvPipNotificationController.ACTION_CLOSE_PIP);
            intentFilter.addAction(TvPipNotificationController.ACTION_SHOW_PIP_MENU);
            intentFilter.addAction(TvPipNotificationController.ACTION_MOVE_PIP);
            intentFilter.addAction(TvPipNotificationController.ACTION_TOGGLE_EXPANDED_PIP);
            intentFilter.addAction(TvPipNotificationController.ACTION_FULLSCREEN);
            this.mRegistered = false;
        }

        /* access modifiers changed from: package-private */
        public void register() {
            if (!this.mRegistered) {
                TvPipNotificationController.this.mContext.registerReceiverForAllUsers(this, this.mIntentFilter, "com.android.systemui.permission.SELF", TvPipNotificationController.this.mMainHandler);
                this.mRegistered = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void unregister() {
            if (this.mRegistered) {
                TvPipNotificationController.this.mContext.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: on(Broadcast)Receive(), action=%s", new Object[]{TvPipNotificationController.TAG, action});
            if (TvPipNotificationController.ACTION_SHOW_PIP_MENU.equals(action)) {
                TvPipNotificationController.this.mDelegate.showPictureInPictureMenu();
            } else if (TvPipNotificationController.ACTION_CLOSE_PIP.equals(action)) {
                TvPipNotificationController.this.mDelegate.closePip();
            } else if (TvPipNotificationController.ACTION_MOVE_PIP.equals(action)) {
                TvPipNotificationController.this.mDelegate.enterPipMovementMenu();
            } else if (TvPipNotificationController.ACTION_TOGGLE_EXPANDED_PIP.equals(action)) {
                TvPipNotificationController.this.mDelegate.togglePipExpansion();
            } else if (TvPipNotificationController.ACTION_FULLSCREEN.equals(action)) {
                TvPipNotificationController.this.mDelegate.movePipToFullscreen();
            }
        }
    }
}
