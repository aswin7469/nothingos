package com.android.systemui.keyguard;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Icon;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Handler;
import android.os.Trace;
import android.service.notification.ZenModeConfig;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.C1894R;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothing.systemui.keyguard.calendar.CalendarManager;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherController;
import com.nothing.systemui.util.NTLogUtil;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class KeyguardSliceProvider extends SliceProvider implements NextAlarmController.NextAlarmChangeCallback, ZenModeController.Callback, NotificationMediaManager.MediaListener, StatusBarStateController.StateListener, SystemUIAppComponentFactory.ContextInitializer, KeyguardWeatherController.Callback, CalendarManager.Callback {
    static final int ALARM_VISIBILITY_HOURS = 12;
    private static final StyleSpan BOLD_STYLE = new StyleSpan(1);
    public static final String KEYGUARD_ACTION_URI = "content://com.android.systemui.keyguard/action";
    public static final String KEYGUARD_CALENDAR_URI = "content://com.android.systemui.keyguard/calendar";
    public static final String KEYGUARD_DATE_URI = "content://com.android.systemui.keyguard/date";
    public static final String KEYGUARD_DND_URI = "content://com.android.systemui.keyguard/dnd";
    private static final String KEYGUARD_HEADER_URI = "content://com.android.systemui.keyguard/header";
    public static final String KEYGUARD_MEDIA_URI = "content://com.android.systemui.keyguard/media";
    public static final String KEYGUARD_NEXT_ALARM_URI = "content://com.android.systemui.keyguard/alarm";
    public static final String KEYGUARD_SLICE_URI = "content://com.android.systemui.keyguard/main";
    public static final String KEYGUARD_WEATHER_URI = "content://com.android.systemui.keyguard/weather";
    private static final String TAG = "KgdSliceProvider";
    private static KeyguardSliceProvider sInstance;
    private static final Object sInstanceLock = new Object();
    @Inject
    public AlarmManager mAlarmManager;
    protected final Uri mAlarmUri = Uri.parse(KEYGUARD_NEXT_ALARM_URI);
    @Inject
    public CalendarManager mCalendarManager;
    protected final Uri mCalendarUri = Uri.parse(KEYGUARD_CALENDAR_URI);
    @Inject
    public ContentResolver mContentResolver;
    private SystemUIAppComponentFactory.ContextAvailableCallback mContextAvailableCallback;
    private final Date mCurrentTime = new Date();
    private DateFormat mDateFormat;
    private String mDatePattern;
    protected final Uri mDateUri = Uri.parse(KEYGUARD_DATE_URI);
    protected final Uri mDndUri = Uri.parse(KEYGUARD_DND_URI);
    @Inject
    public DozeParameters mDozeParameters;
    protected boolean mDozing;
    private final Handler mHandler = new Handler();
    protected final Uri mHeaderUri = Uri.parse(KEYGUARD_HEADER_URI);
    final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.DATE_CHANGED".equals(action)) {
                synchronized (this) {
                    KeyguardSliceProvider.this.updateClockLocked();
                }
            } else if ("android.intent.action.LOCALE_CHANGED".equals(action)) {
                synchronized (this) {
                    KeyguardSliceProvider.this.cleanDateFormatLocked();
                }
            }
        }
    };
    @Inject
    public KeyguardBypassController mKeyguardBypassController;
    @Inject
    public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onTimeChanged() {
            synchronized (this) {
                KeyguardSliceProvider.this.updateClockLocked();
            }
        }

        public void onTimeZoneChanged(TimeZone timeZone) {
            synchronized (this) {
                KeyguardSliceProvider.this.cleanDateFormatLocked();
            }
        }
    };
    @Inject
    public KeyguardWeatherController mKeyguardWeatherController;
    private String mLastText;
    private CharSequence mMediaArtist;
    private final Handler mMediaHandler = new Handler();
    private boolean mMediaIsVisible;
    @Inject
    public NotificationMediaManager mMediaManager;
    private CharSequence mMediaTitle;
    protected final Uri mMediaUri = Uri.parse(KEYGUARD_MEDIA_URI);
    protected SettableWakeLock mMediaWakeLock;
    private String mNextAlarm;
    @Inject
    public NextAlarmController mNextAlarmController;
    private AlarmManager.AlarmClockInfo mNextAlarmInfo;
    private PendingIntent mPendingIntent;
    private boolean mRegistered;
    protected final Uri mSliceUri = Uri.parse(KEYGUARD_SLICE_URI);
    private int mStatusBarState;
    @Inject
    public StatusBarStateController mStatusBarStateController;
    private final AlarmManager.OnAlarmListener mUpdateNextAlarm = new KeyguardSliceProvider$$ExternalSyntheticLambda1(this);
    protected final Uri mWeatherUri = Uri.parse(KEYGUARD_WEATHER_URI);
    @Inject
    public ZenModeController mZenModeController;

    public static KeyguardSliceProvider getAttachedInstance() {
        return sInstance;
    }

    public Slice onBindSlice(Uri uri) {
        Slice build;
        Trace.beginSection("KeyguardSliceProvider#onBindSlice");
        synchronized (this) {
            ListBuilder listBuilder = new ListBuilder(getContext(), this.mSliceUri, -1);
            listBuilder.addRow(new ListBuilder.RowBuilder(this.mDateUri).setTitle(this.mLastText));
            if (isCalendarOn()) {
                addCalendarLocked(listBuilder);
            } else {
                addWeatherLocked(listBuilder);
            }
            addNextAlarmLocked(listBuilder);
            addPrimaryActionLocked(listBuilder);
            build = listBuilder.build();
        }
        Trace.endSection();
        return build;
    }

    /* access modifiers changed from: protected */
    public boolean needsMediaLocked() {
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        boolean z = keyguardBypassController != null && keyguardBypassController.getBypassEnabled() && this.mDozeParameters.getAlwaysOn();
        boolean z2 = this.mStatusBarState == 0 && this.mMediaIsVisible;
        if (TextUtils.isEmpty(this.mMediaTitle) || !this.mMediaIsVisible || (!this.mDozing && !z && !z2)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void addMediaLocked(ListBuilder listBuilder) {
        if (!TextUtils.isEmpty(this.mMediaTitle)) {
            listBuilder.setHeader(new ListBuilder.HeaderBuilder(this.mHeaderUri).setTitle(this.mMediaTitle));
            if (!TextUtils.isEmpty(this.mMediaArtist)) {
                ListBuilder.RowBuilder rowBuilder = new ListBuilder.RowBuilder(this.mMediaUri);
                rowBuilder.setTitle(this.mMediaArtist);
                NotificationMediaManager notificationMediaManager = this.mMediaManager;
                IconCompat iconCompat = null;
                Icon mediaIcon = notificationMediaManager == null ? null : notificationMediaManager.getMediaIcon();
                if (mediaIcon != null) {
                    iconCompat = IconCompat.createFromIcon(getContext(), mediaIcon);
                }
                if (iconCompat != null) {
                    rowBuilder.addEndItem(iconCompat, 0);
                }
                listBuilder.addRow(rowBuilder);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addPrimaryActionLocked(ListBuilder listBuilder) {
        listBuilder.addRow(new ListBuilder.RowBuilder(Uri.parse(KEYGUARD_ACTION_URI)).setPrimaryAction(SliceAction.createDeeplink(this.mPendingIntent, IconCompat.createWithResource(getContext(), C1894R.C1896drawable.ic_access_alarms_big), 0, (CharSequence) this.mLastText)));
    }

    /* access modifiers changed from: protected */
    public void addNextAlarmLocked(ListBuilder listBuilder) {
        if (!TextUtils.isEmpty(this.mNextAlarm)) {
            listBuilder.addRow(new ListBuilder.RowBuilder(this.mAlarmUri).setTitle(this.mNextAlarm).addEndItem(IconCompat.createWithResource(getContext(), C1894R.C1896drawable.ic_access_alarms_big), 0));
        }
    }

    /* access modifiers changed from: protected */
    public void addZenModeLocked(ListBuilder listBuilder) {
        if (isDndOn()) {
            listBuilder.addRow(new ListBuilder.RowBuilder(this.mDndUri).setContentDescription(getContext().getResources().getString(C1894R.string.accessibility_quick_settings_dnd)).addEndItem(IconCompat.createWithResource(getContext(), C1894R.C1896drawable.stat_sys_dnd), 0));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isDndOn() {
        return this.mZenModeController.getZen() != 0;
    }

    public boolean onCreateSliceProvider() {
        this.mContextAvailableCallback.onContextAvailable(getContext());
        this.mMediaWakeLock = new SettableWakeLock(WakeLock.createPartial(getContext(), "media"), "media");
        synchronized (sInstanceLock) {
            KeyguardSliceProvider keyguardSliceProvider = sInstance;
            if (keyguardSliceProvider != null) {
                keyguardSliceProvider.onDestroy();
            }
            this.mDatePattern = getContext().getString(C1894R.string.system_ui_aod_date_pattern);
            KeyguardWeatherController keyguardWeatherController = this.mKeyguardWeatherController;
            if (keyguardWeatherController != null) {
                keyguardWeatherController.addCallback(this);
            }
            CalendarManager calendarManager = this.mCalendarManager;
            if (calendarManager != null) {
                calendarManager.addCallback(this);
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

    /* access modifiers changed from: protected */
    public void onDestroy() {
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
                this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
                getContext().unregisterReceiver(this.mIntentReceiver);
            }
            sInstance = null;
        }
    }

    public void onZenChanged(int i) {
        notifyChange();
    }

    public void onConfigChanged(ZenModeConfig zenModeConfig) {
        notifyChange();
    }

    /* access modifiers changed from: private */
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
        if (this.mNextAlarmInfo.getTriggerTime() <= System.currentTimeMillis() + TimeUnit.HOURS.toMillis((long) i)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void registerClockUpdate() {
        synchronized (this) {
            if (!this.mRegistered) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
                getContext().registerReceiver(this.mIntentReceiver, intentFilter, (String) null, (Handler) null);
                this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
                this.mRegistered = true;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isRegistered() {
        boolean z;
        synchronized (this) {
            z = this.mRegistered;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void updateClockLocked() {
        String formattedDateLocked = getFormattedDateLocked();
        if (!formattedDateLocked.equals(this.mLastText)) {
            this.mLastText = formattedDateLocked;
            notifyChange();
        }
    }

    /* access modifiers changed from: protected */
    public String getFormattedDateLocked() {
        String format = new SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "EEE, d MMM"), Locale.getDefault()).format(new Date());
        NTLogUtil.m1688i(TAG, "getFormattedDateLocked =" + format);
        return format;
    }

    /* access modifiers changed from: package-private */
    public void cleanDateFormatLocked() {
        this.mDateFormat = null;
    }

    public void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
        long triggerTime;
        synchronized (this) {
            this.mNextAlarmInfo = alarmClockInfo;
            this.mAlarmManager.cancel(this.mUpdateNextAlarm);
            AlarmManager.AlarmClockInfo alarmClockInfo2 = this.mNextAlarmInfo;
            if (alarmClockInfo2 == null) {
                triggerTime = -1;
            } else {
                triggerTime = alarmClockInfo2.getTriggerTime() - TimeUnit.HOURS.toMillis(12);
            }
            long j = triggerTime;
            if (j > 0) {
                this.mAlarmManager.setExact(1, j, "lock_screen_next_alarm", this.mUpdateNextAlarm, this.mHandler);
            }
        }
        updateNextAlarm();
    }

    public void onPrimaryMetadataOrStateChanged(MediaMetadata mediaMetadata, int i) {
        synchronized (this) {
            boolean isPlayingState = NotificationMediaManager.isPlayingState(i);
            this.mMediaHandler.removeCallbacksAndMessages((Object) null);
            if (!this.mMediaIsVisible || isPlayingState || this.mStatusBarState == 0) {
                this.mMediaWakeLock.setAcquired(false);
                updateMediaStateLocked(mediaMetadata, i);
            } else {
                this.mMediaWakeLock.setAcquired(true);
                this.mMediaHandler.postDelayed(new KeyguardSliceProvider$$ExternalSyntheticLambda0(this, mediaMetadata, i), 100);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPrimaryMetadataOrStateChanged$0$com-android-systemui-keyguard-KeyguardSliceProvider */
    public /* synthetic */ void mo33185x500fc039(MediaMetadata mediaMetadata, int i) {
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
                charSequence = getContext().getResources().getString(C1894R.string.music_controls_no_title);
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

    /* access modifiers changed from: protected */
    public void notifyChange() {
        this.mContentResolver.notifyChange(this.mSliceUri, (ContentObserver) null);
    }

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

    public void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mContextAvailableCallback = contextAvailableCallback;
    }

    /* access modifiers changed from: protected */
    public void addWeatherLocked(ListBuilder listBuilder) {
        NTLogUtil.m1688i(TAG, "addWeatherLocked isWeatherOn=" + isWeatherOn());
        if (isWeatherOn()) {
            listBuilder.addRow(new ListBuilder.RowBuilder(this.mWeatherUri));
        }
    }

    public boolean isWeatherOn() {
        KeyguardWeatherController keyguardWeatherController = this.mKeyguardWeatherController;
        return (keyguardWeatherController == null || !keyguardWeatherController.isKeyguardWeatherOn() || this.mKeyguardWeatherController.getWeatherData() == null) ? false : true;
    }

    public void onWeatherSwitchChanged(boolean z) {
        NTLogUtil.m1688i(TAG, "KeyguardWeather onWeatherSwitchChanged :" + z);
        notifyChange();
    }

    public void onWeatherDataChanged() {
        if (this.mKeyguardWeatherController != null) {
            NTLogUtil.m1688i(TAG, "KeyguardWeather onWeatherDataChanged getWeatherData:" + this.mKeyguardWeatherController.getWeatherData());
        }
        notifyChange();
    }

    /* access modifiers changed from: protected */
    public void addCalendarLocked(ListBuilder listBuilder) {
        if (isCalendarOn()) {
            NTLogUtil.m1688i(TAG, "addCalendarLocked");
            listBuilder.addRow(new ListBuilder.RowBuilder(this.mCalendarUri));
        }
    }

    public boolean isCalendarOn() {
        CalendarManager calendarManager = this.mCalendarManager;
        return calendarManager != null && calendarManager.shouldShowCalendar();
    }

    public void onCalendarSwitchChanged(boolean z) {
        NTLogUtil.m1688i(TAG, "KeyguardWeather onWeatherSwitchChanged :" + z);
        notifyChange();
    }

    public void onCalendarDataChanged() {
        if (this.mCalendarManager != null) {
            NTLogUtil.m1688i(TAG, "KeyguardWeather onWeatherDataChanged getWeatherData:" + this.mCalendarManager.getCalendarEventData());
        }
        notifyChange();
    }
}
