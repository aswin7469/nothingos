package com.android.systemui.keyguard;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Handler;
import android.os.Trace;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothingos.keyguard.weather.KeyguardWeatherController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class KeyguardSliceProvider extends SliceProvider implements NextAlarmController.NextAlarmChangeCallback, ZenModeController.Callback, NotificationMediaManager.MediaListener, StatusBarStateController.StateListener, SystemUIAppComponentFactory.ContextInitializer, KeyguardWeatherController.Callback {
    @VisibleForTesting
    static final int ALARM_VISIBILITY_HOURS = 12;
    private static KeyguardSliceProvider sInstance;
    public AlarmManager mAlarmManager;
    public ContentResolver mContentResolver;
    private SystemUIAppComponentFactory.ContextAvailableCallback mContextAvailableCallback;
    private DateFormat mDateFormat;
    private String mDatePattern;
    public DozeParameters mDozeParameters;
    protected boolean mDozing;
    public KeyguardBypassController mKeyguardBypassController;
    public KeyguardWeatherController mKeyguardWeatherController;
    private String mLastText;
    private CharSequence mMediaArtist;
    private boolean mMediaIsVisible;
    public NotificationMediaManager mMediaManager;
    private CharSequence mMediaTitle;
    @VisibleForTesting
    protected SettableWakeLock mMediaWakeLock;
    private String mNextAlarm;
    public NextAlarmController mNextAlarmController;
    private AlarmManager.AlarmClockInfo mNextAlarmInfo;
    private PendingIntent mPendingIntent;
    private boolean mRegistered;
    private int mStatusBarState;
    public StatusBarStateController mStatusBarStateController;
    public ZenModeController mZenModeController;
    private static final StyleSpan BOLD_STYLE = new StyleSpan(1);
    private static final Object sInstanceLock = new Object();
    private final Date mCurrentTime = new Date();
    private final AlarmManager.OnAlarmListener mUpdateNextAlarm = new AlarmManager.OnAlarmListener() { // from class: com.android.systemui.keyguard.KeyguardSliceProvider$$ExternalSyntheticLambda0
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            KeyguardSliceProvider.this.updateNextAlarm();
        }
    };
    @VisibleForTesting
    final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.keyguard.KeyguardSliceProvider.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.DATE_CHANGED".equals(action)) {
                synchronized (this) {
                    KeyguardSliceProvider.this.updateClockLocked();
                }
            } else if (!"android.intent.action.LOCALE_CHANGED".equals(action)) {
            } else {
                synchronized (this) {
                    KeyguardSliceProvider.this.cleanDateFormatLocked();
                }
            }
        }
    };
    @VisibleForTesting
    final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.keyguard.KeyguardSliceProvider.2
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTimeChanged() {
            synchronized (this) {
                KeyguardSliceProvider.this.updateClockLocked();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTimeZoneChanged(TimeZone timeZone) {
            synchronized (this) {
                KeyguardSliceProvider.this.cleanDateFormatLocked();
            }
        }
    };
    private final Handler mHandler = new Handler();
    private final Handler mMediaHandler = new Handler();
    protected final Uri mSliceUri = Uri.parse("content://com.android.systemui.keyguard/main");
    protected final Uri mHeaderUri = Uri.parse("content://com.android.systemui.keyguard/header");
    protected final Uri mDateUri = Uri.parse("content://com.android.systemui.keyguard/date");
    protected final Uri mAlarmUri = Uri.parse("content://com.android.systemui.keyguard/alarm");
    protected final Uri mDndUri = Uri.parse("content://com.android.systemui.keyguard/dnd");
    protected final Uri mMediaUri = Uri.parse("content://com.android.systemui.keyguard/media");
    protected final Uri mWeatherUri = Uri.parse("content://com.android.systemui.keyguard/weather");

    public static KeyguardSliceProvider getAttachedInstance() {
        return sInstance;
    }

    @Override // androidx.slice.SliceProvider
    public Slice onBindSlice(Uri uri) {
        Slice build;
        Trace.beginSection("KeyguardSliceProvider#onBindSlice");
        synchronized (this) {
            ListBuilder listBuilder = new ListBuilder(getContext(), this.mSliceUri, -1L);
            listBuilder.addRow(new ListBuilder.RowBuilder(this.mDateUri).setTitle(this.mLastText));
            if (!addNextAlarmLocked(listBuilder)) {
                addWeatherLocked(listBuilder);
            }
            addPrimaryActionLocked(listBuilder);
            build = listBuilder.build();
        }
        Trace.endSection();
        return build;
    }

    protected boolean needsMediaLocked() {
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        return !TextUtils.isEmpty(this.mMediaTitle) && this.mMediaIsVisible && (this.mDozing || (keyguardBypassController != null && keyguardBypassController.getBypassEnabled() && this.mDozeParameters.getAlwaysOn()) || (this.mStatusBarState == 0 && this.mMediaIsVisible));
    }

    protected void addPrimaryActionLocked(ListBuilder listBuilder) {
        listBuilder.addRow(new ListBuilder.RowBuilder(Uri.parse("content://com.android.systemui.keyguard/action")).setPrimaryAction(SliceAction.createDeeplink(this.mPendingIntent, IconCompat.createWithResource(getContext(), R$drawable.ic_access_alarms_big), 0, this.mLastText)));
    }

    protected boolean addNextAlarmLocked(ListBuilder listBuilder) {
        if (TextUtils.isEmpty(this.mNextAlarm)) {
            return false;
        }
        listBuilder.addRow(new ListBuilder.RowBuilder(this.mAlarmUri).setTitle(this.mNextAlarm).addEndItem(IconCompat.createWithResource(getContext(), R$drawable.ic_access_alarms_big), 0));
        return true;
    }

    @Override // androidx.slice.SliceProvider
    public boolean onCreateSliceProvider() {
        this.mContextAvailableCallback.onContextAvailable(getContext());
        this.mMediaWakeLock = new SettableWakeLock(WakeLock.createPartial(getContext(), "media"), "media");
        synchronized (sInstanceLock) {
            KeyguardSliceProvider keyguardSliceProvider = sInstance;
            if (keyguardSliceProvider != null) {
                keyguardSliceProvider.onDestroy();
            }
            this.mDatePattern = getContext().getString(R$string.nothing_system_ui_aod_date_pattern);
            KeyguardWeatherController keyguardWeatherController = this.mKeyguardWeatherController;
            if (keyguardWeatherController != null) {
                keyguardWeatherController.addCallback(this);
            }
            this.mPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), KeyguardSliceProvider.class), 67108864);
            this.mMediaManager.addCallback(this);
            this.mStatusBarStateController.addCallback(this);
            this.mNextAlarmController.addCallback(this);
            this.mZenModeController.addCallback(this);
            sInstance = this;
            registerClockUpdate();
            updateClockLocked();
        }
        return true;
    }

    @VisibleForTesting
    protected void onDestroy() {
        synchronized (sInstanceLock) {
            KeyguardWeatherController keyguardWeatherController = this.mKeyguardWeatherController;
            if (keyguardWeatherController != null) {
                keyguardWeatherController.removeCallback(this);
            }
            this.mNextAlarmController.removeCallback(this);
            this.mZenModeController.removeCallback(this);
            this.mMediaWakeLock.setAcquired(false);
            this.mAlarmManager.cancel(this.mUpdateNextAlarm);
            if (this.mRegistered) {
                this.mRegistered = false;
                getKeyguardUpdateMonitor().removeCallback(this.mKeyguardUpdateMonitorCallback);
                getContext().unregisterReceiver(this.mIntentReceiver);
            }
            sInstance = null;
        }
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
    public void onZenChanged(int i) {
        notifyChange();
    }

    @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
    public void onConfigChanged(ZenModeConfig zenModeConfig) {
        notifyChange();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNextAlarm() {
        synchronized (this) {
            if (withinNHoursLocked(this.mNextAlarmInfo, 12)) {
                this.mNextAlarm = android.text.format.DateFormat.format(android.text.format.DateFormat.is24HourFormat(getContext(), ActivityManager.getCurrentUser()) ? "HH:mm" : "h:mm", this.mNextAlarmInfo.getTriggerTime()).toString();
            } else {
                this.mNextAlarm = "";
            }
        }
        notifyChange();
    }

    private boolean withinNHoursLocked(AlarmManager.AlarmClockInfo alarmClockInfo, int i) {
        if (alarmClockInfo == null) {
            return false;
        }
        return this.mNextAlarmInfo.getTriggerTime() <= System.currentTimeMillis() + TimeUnit.HOURS.toMillis((long) i);
    }

    @VisibleForTesting
    protected void registerClockUpdate() {
        synchronized (this) {
            if (this.mRegistered) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.DATE_CHANGED");
            intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
            getContext().registerReceiver(this.mIntentReceiver, intentFilter, null, null);
            getKeyguardUpdateMonitor().registerCallback(this.mKeyguardUpdateMonitorCallback);
            this.mRegistered = true;
        }
    }

    @VisibleForTesting
    boolean isRegistered() {
        boolean z;
        synchronized (this) {
            z = this.mRegistered;
        }
        return z;
    }

    protected void updateClockLocked() {
        String formattedDateLocked = getFormattedDateLocked();
        if (!formattedDateLocked.equals(this.mLastText)) {
            this.mLastText = formattedDateLocked;
            notifyChange();
        }
    }

    protected String getFormattedDateLocked() {
        String format = new SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE, d MMM"), Locale.getDefault()).format(new Date());
        Log.i("KgdSliceProvider", "getFormattedDateLocked =" + format);
        return format;
    }

    @VisibleForTesting
    void cleanDateFormatLocked() {
        this.mDateFormat = null;
    }

    @Override // com.android.systemui.statusbar.policy.NextAlarmController.NextAlarmChangeCallback
    public void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
        synchronized (this) {
            this.mNextAlarmInfo = alarmClockInfo;
            this.mAlarmManager.cancel(this.mUpdateNextAlarm);
            AlarmManager.AlarmClockInfo alarmClockInfo2 = this.mNextAlarmInfo;
            long triggerTime = alarmClockInfo2 == null ? -1L : alarmClockInfo2.getTriggerTime() - TimeUnit.HOURS.toMillis(12L);
            if (triggerTime > 0) {
                this.mAlarmManager.setExact(1, triggerTime, "lock_screen_next_alarm", this.mUpdateNextAlarm, this.mHandler);
            }
        }
        updateNextAlarm();
    }

    private KeyguardUpdateMonitor getKeyguardUpdateMonitor() {
        return (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
    }

    @Override // com.android.systemui.statusbar.NotificationMediaManager.MediaListener
    public void onPrimaryMetadataOrStateChanged(final MediaMetadata mediaMetadata, final int i) {
        synchronized (this) {
            boolean isPlayingState = NotificationMediaManager.isPlayingState(i);
            this.mMediaHandler.removeCallbacksAndMessages(null);
            if (this.mMediaIsVisible && !isPlayingState && this.mStatusBarState != 0) {
                this.mMediaWakeLock.setAcquired(true);
                this.mMediaHandler.postDelayed(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardSliceProvider$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        KeyguardSliceProvider.this.lambda$onPrimaryMetadataOrStateChanged$0(mediaMetadata, i);
                    }
                }, 100L);
            } else {
                this.mMediaWakeLock.setAcquired(false);
                updateMediaStateLocked(mediaMetadata, i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPrimaryMetadataOrStateChanged$0(MediaMetadata mediaMetadata, int i) {
        synchronized (this) {
            updateMediaStateLocked(mediaMetadata, i);
            this.mMediaWakeLock.setAcquired(false);
        }
    }

    private void updateMediaStateLocked(MediaMetadata mediaMetadata, int i) {
        CharSequence charSequence;
        boolean isPlayingState = NotificationMediaManager.isPlayingState(i);
        CharSequence charSequence2 = null;
        if (mediaMetadata != null) {
            charSequence = mediaMetadata.getText("android.media.metadata.TITLE");
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = getContext().getResources().getString(R$string.music_controls_no_title);
            }
        } else {
            charSequence = null;
        }
        if (mediaMetadata != null) {
            charSequence2 = mediaMetadata.getText("android.media.metadata.ARTIST");
        }
        if (isPlayingState != this.mMediaIsVisible || !TextUtils.equals(charSequence, this.mMediaTitle) || !TextUtils.equals(charSequence2, this.mMediaArtist)) {
            this.mMediaTitle = charSequence;
            this.mMediaArtist = charSequence2;
            this.mMediaIsVisible = isPlayingState;
            notifyChange();
        }
    }

    protected void notifyChange() {
        this.mContentResolver.notifyChange(this.mSliceUri, null);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        boolean z2;
        synchronized (this) {
            boolean needsMediaLocked = needsMediaLocked();
            this.mDozing = z;
            z2 = needsMediaLocked != needsMediaLocked();
        }
        if (z2) {
            notifyChange();
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        boolean z;
        synchronized (this) {
            boolean needsMediaLocked = needsMediaLocked();
            this.mStatusBarState = i;
            z = needsMediaLocked != needsMediaLocked();
        }
        if (z) {
            notifyChange();
        }
    }

    @Override // com.android.systemui.SystemUIAppComponentFactory.ContextInitializer
    public void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mContextAvailableCallback = contextAvailableCallback;
    }

    protected void addWeatherLocked(ListBuilder listBuilder) {
        Log.i("KgdSliceProvider", "addWeatherLocked isWeatherOn=" + isWeatherOn());
        if (!isWeatherOn()) {
            return;
        }
        listBuilder.addRow(new ListBuilder.RowBuilder(this.mWeatherUri));
    }

    public boolean isWeatherOn() {
        KeyguardWeatherController keyguardWeatherController = this.mKeyguardWeatherController;
        return (keyguardWeatherController == null || !keyguardWeatherController.isKeyguardWeatherOn() || this.mKeyguardWeatherController.getWeatherData() == null) ? false : true;
    }

    @Override // com.nothingos.keyguard.weather.KeyguardWeatherController.Callback
    public void onWeatherSwitchChanged(boolean z) {
        Log.i("KgdSliceProvider", "KeyguardWeather onWeatherSwitchChanged :" + z);
        notifyChange();
    }

    @Override // com.nothingos.keyguard.weather.KeyguardWeatherController.Callback
    public void onWeatherDataChanged() {
        if (this.mKeyguardWeatherController != null) {
            Log.i("KgdSliceProvider", "KeyguardWeather onWeatherDataChanged getWeatherData:" + this.mKeyguardWeatherController.getWeatherData());
        }
        notifyChange();
    }
}
