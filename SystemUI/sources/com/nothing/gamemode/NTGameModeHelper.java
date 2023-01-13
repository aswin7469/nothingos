package com.nothing.gamemode;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.Settings;
import com.android.systemui.C1894R;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.dagger.SysUISingleton;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.systemui.util.NotificationChannelsEx;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000M\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0014\b\u0007\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\tH\u0016J\b\u0010\u0019\u001a\u00020\tH\u0016J\b\u0010\u001a\u001a\u00020\u0017H\u0002J\b\u0010\u001b\u001a\u00020\u0017H\u0002J\b\u0010\u001c\u001a\u00020\u0017H\u0002J\n\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0002J\b\u0010\u001f\u001a\u00020\u0017H\u0002J\b\u0010 \u001a\u00020\u0017H\u0002J\b\u0010!\u001a\u00020\tH\u0016R\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \u0007*\u0004\u0018\u00010\u00100\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0004\n\u0002\u0010\u0015¨\u0006#"}, mo65043d2 = {"Lcom/nothing/gamemode/NTGameModeHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "contentResolver", "Landroid/content/ContentResolver;", "kotlin.jvm.PlatformType", "currentShowNotification", "", "gameModeEnabled", "handler", "Landroid/os/Handler;", "lightWeightHeadsupEnabled", "mistouchPreventEnabled", "notificationManager", "Landroid/app/NotificationManager;", "receiver", "Landroid/content/BroadcastReceiver;", "settingsObserver", "com/nothing/gamemode/NTGameModeHelper$settingsObserver$1", "Lcom/nothing/gamemode/NTGameModeHelper$settingsObserver$1;", "cancelGameModeOnNotification", "", "isGameModeOn", "isMistouchPreventEnabled", "loadGameModeEnabled", "loadMistouchPreventEnabled", "loadNotificationDisplayMode", "pendingBroadcast", "Landroid/app/PendingIntent;", "registerContentObserver", "sendGameModeOnNotification", "shouldShowLightweightHeadsup", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTGameModeHelper.kt */
public final class NTGameModeHelper {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String GAME_MODE_ENABLED = "nt_game_mode_gaming";
    public static final String MISTOUCH_PREVENTION = "nt_game_mode_mistouch_prevention";
    public static final int NOTE_GAME_MODE = 50000;
    public static final String NOTIFICATION_DISPLAY_MODE = "nt_game_mode_notification_display_mode";
    private static final String TAG = "NTGameModeHelper";
    private final ContentResolver contentResolver;
    private final Context context;
    /* access modifiers changed from: private */
    public boolean currentShowNotification = isGameModeOn();
    private boolean gameModeEnabled;
    private final Handler handler;
    private boolean lightWeightHeadsupEnabled;
    private boolean mistouchPreventEnabled;
    private final NotificationManager notificationManager;
    private final BroadcastReceiver receiver;
    private final NTGameModeHelper$settingsObserver$1 settingsObserver;

    @Inject
    public NTGameModeHelper(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.contentResolver = context2.getContentResolver();
        Handler handler2 = new Handler(Looper.getMainLooper());
        this.handler = handler2;
        this.notificationManager = (NotificationManager) context2.getSystemService(NotificationManager.class);
        this.settingsObserver = new NTGameModeHelper$settingsObserver$1(this, handler2);
        BroadcastReceiver nTGameModeHelper$receiver$1 = new NTGameModeHelper$receiver$1(this);
        this.receiver = nTGameModeHelper$receiver$1;
        this.context = context2;
        registerContentObserver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        context2.registerReceiver(nTGameModeHelper$receiver$1, intentFilter);
        cancelGameModeOnNotification();
        loadNotificationDisplayMode();
        loadGameModeEnabled();
        loadMistouchPreventEnabled();
    }

    /* access modifiers changed from: private */
    public final void cancelGameModeOnNotification() {
        NTLogUtil.m1686d(TAG, "cancel GameMode notification");
        this.notificationManager.cancelAsUser((String) null, NOTE_GAME_MODE, UserHandle.ALL);
    }

    /* access modifiers changed from: private */
    public final void sendGameModeOnNotification() {
        NTLogUtil.m1686d(TAG, "send GameMode notification");
        String string = this.context.getString(C1894R.string.gamemode_notification_title);
        Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.stri…emode_notification_title)");
        String string2 = this.context.getString(C1894R.string.gamemode_notification_content);
        Intrinsics.checkNotNullExpressionValue(string2, "context.getString(\n     …ode_notification_content)");
        CharSequence charSequence = string2;
        Notification.Builder visibility = new Notification.Builder(this.context, NotificationChannelsEx.GAME_MODE_CHANNEL_NAME).setSmallIcon(C1894R.C1896drawable.ic_game_mode).setContentText(charSequence).setContentTitle(string).setOnlyAlertOnce(false).setStyle(new Notification.BigTextStyle().bigText(charSequence)).setContentIntent(pendingBroadcast()).setVisibility(1);
        Intrinsics.checkNotNullExpressionValue(visibility, "Builder(context, Notific…cation.VISIBILITY_PUBLIC)");
        SystemUIApplication.overrideNotificationAppName(this.context, visibility, false);
        Notification build = visibility.build();
        build.flags = 2;
        PendingIntent pendingIntent = build.contentIntent;
        this.notificationManager.cancelAsUser((String) null, NOTE_GAME_MODE, UserHandle.ALL);
        this.notificationManager.notifyAsUser((String) null, NOTE_GAME_MODE, build, UserHandle.ALL);
    }

    private final PendingIntent pendingBroadcast() {
        return PendingIntent.getActivityAsUser(this.context, 0, new Intent().setAction("android.settings.ACTION_GAME_MODE_SETTINGS").setFlags(268435456), 67108864, (Bundle) null, UserHandle.CURRENT);
    }

    /* access modifiers changed from: private */
    public final void loadGameModeEnabled() {
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.contentResolver, GAME_MODE_ENABLED, 0, -2) != 0) {
            z = true;
        }
        this.gameModeEnabled = z;
        NTLogUtil.m1688i(TAG, "gameModeEnabled = " + this.gameModeEnabled);
    }

    /* access modifiers changed from: private */
    public final void loadNotificationDisplayMode() {
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.contentResolver, NOTIFICATION_DISPLAY_MODE, 0, -2) != 0) {
            z = true;
        }
        this.lightWeightHeadsupEnabled = z;
        NTLogUtil.m1688i(TAG, "lightWeightHeadsupEnabled = " + this.lightWeightHeadsupEnabled);
    }

    /* access modifiers changed from: private */
    public final void loadMistouchPreventEnabled() {
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.contentResolver, MISTOUCH_PREVENTION, 0, -2) != 0) {
            z = true;
        }
        this.mistouchPreventEnabled = z;
        NTLogUtil.m1688i(TAG, "mistouchPreventEnabled = " + this.mistouchPreventEnabled);
    }

    private final void registerContentObserver() {
        this.contentResolver.registerContentObserver(Settings.Secure.getUriFor(GAME_MODE_ENABLED), true, this.settingsObserver, -1);
        this.contentResolver.registerContentObserver(Settings.Secure.getUriFor(NOTIFICATION_DISPLAY_MODE), true, this.settingsObserver, -1);
        this.contentResolver.registerContentObserver(Settings.Secure.getUriFor(MISTOUCH_PREVENTION), true, this.settingsObserver, -1);
    }

    public boolean isGameModeOn() {
        return this.gameModeEnabled;
    }

    public boolean shouldShowLightweightHeadsup() {
        return this.gameModeEnabled && this.lightWeightHeadsupEnabled;
    }

    public boolean isMistouchPreventEnabled() {
        return this.gameModeEnabled && this.mistouchPreventEnabled;
    }

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\n"}, mo65043d2 = {"Lcom/nothing/gamemode/NTGameModeHelper$Companion;", "", "()V", "GAME_MODE_ENABLED", "", "MISTOUCH_PREVENTION", "NOTE_GAME_MODE", "", "NOTIFICATION_DISPLAY_MODE", "TAG", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NTGameModeHelper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
