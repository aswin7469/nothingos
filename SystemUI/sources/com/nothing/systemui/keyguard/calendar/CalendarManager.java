package com.nothing.systemui.keyguard.calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.Utils;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.utils.CalendarPermissionActivity;
import java.util.ArrayList;
import javax.inject.Inject;

@SysUISingleton
public class CalendarManager {
    private static final String DIFF_YEAR_SKELETON = "d/MMM/yyyy ";
    private static final String KEYGUARD_CALENDAR_SWITCH = "nothing_keyguard_one_glance_calendar";
    private static final String SAME_DAY_SKELETON_12 = "hh:mm";
    private static final String SAME_DAY_SKELETON_24 = "HH:mm";
    private static final String SAME_YEAR_SKELETON = "d/MMM ";
    private static final String TAG = "CalendarManager";
    /* access modifiers changed from: private */
    public static final Uri mCalendarSwitchUri = Settings.Global.getUriFor(KEYGUARD_CALENDAR_SWITCH);
    private final ArrayList<Callback> mCallbacks = new ArrayList<>();
    private final Object mCallbacksLock = new Object();
    /* access modifiers changed from: private */
    public final Context mContext;
    private CalendarSimpleData mCurrentCalendarEvent = null;
    private KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    /* access modifiers changed from: private */
    public final LocalObserver mLocalObserver;
    /* access modifiers changed from: private */
    public boolean mSwitchOn = false;
    private HandlerThread mWorker;

    public interface Callback {
        void onCalendarDataChanged() {
        }

        void onCalendarSwitchChanged(boolean z) {
        }
    }

    @Inject
    public CalendarManager(Context context, @Main Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        C41851 r0 = new KeyguardUpdateMonitorCallback() {
            public void onKeyguardVisibilityChanged(boolean z) {
                if (CalendarManager.this.mLocalObserver != null && z && CalendarManager.this.mSwitchOn && !CalendarManager.this.mLocalObserver.isCalendarEventsRegister()) {
                    CalendarManager.this.mLocalObserver.registerCalendarEvents();
                }
            }
        };
        this.mKeyguardUpdateMonitorCallback = r0;
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mWorker = handlerThread;
        handlerThread.start();
        LocalObserver localObserver = new LocalObserver(handler);
        this.mLocalObserver = localObserver;
        localObserver.register();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(r0);
    }

    public void beginLoadEvent() {
        this.mWorker.getThreadHandler().post(new CalendarManager$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$beginLoadEvent$1$com-nothing-systemui-keyguard-calendar-CalendarManager */
    public /* synthetic */ void mo57466xa89c5f78() {
        CalendarSimpleData queryCalendarEvent;
        if (hasCalendarReadPermission() && this.mCurrentCalendarEvent != (queryCalendarEvent = CalendarDataLoader.getInstance().queryCalendarEvent(this.mContext))) {
            Log.i(TAG, "new event = " + queryCalendarEvent);
            this.mCurrentCalendarEvent = queryCalendarEvent;
            Utils.safeForeach(this.mCallbacks, new CalendarManager$$ExternalSyntheticLambda0());
        }
    }

    public CalendarSimpleData getCalendarEventData() {
        return this.mCurrentCalendarEvent;
    }

    /* access modifiers changed from: private */
    public boolean hasCalendarReadPermission() {
        return this.mContext.checkSelfPermission("android.permission.READ_CALENDAR") == 0;
    }

    public String getCalendarDescription(CalendarSimpleData calendarSimpleData) {
        int eventStatus = calendarSimpleData.getEventStatus();
        if (eventStatus != 1) {
            return eventStatus == 2 ? this.mContext.getResources().getString(C1894R.string.quick_look_widget_calendar_now) : "";
        }
        return this.mContext.getResources().getString(C1894R.string.quick_look_widget_calendar_in_time, new Object[]{Long.valueOf(calendarSimpleData.getToBeginTime())});
    }

    public void addCallback(Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.add(callback);
        }
    }

    public void removeCallback(Callback callback) {
        synchronized (this.mCallbacksLock) {
            this.mCallbacks.remove((Object) callback);
        }
    }

    private String formatTime(long j, long j2) {
        boolean isSameDay = CalendarUtils.isSameDay(j2, j);
        String str = DateFormat.is24HourFormat(this.mContext) ? SAME_DAY_SKELETON_24 : SAME_DAY_SKELETON_12;
        if (isSameDay) {
            return CalendarUtils.formatTime(j, str);
        }
        if (CalendarUtils.isNextDay(j2, j)) {
            return String.format("%s %s", this.mContext.getString(C1894R.string.quick_look_widget_calendar_tomorrow), CalendarUtils.formatTime(j, str));
        } else if (CalendarUtils.isPreDay(j2, j)) {
            return String.format("%s %s", this.mContext.getString(C1894R.string.quick_look_widget_calendar_yesterday), CalendarUtils.formatTime(j, str));
        } else if (CalendarUtils.isSameYear(j, j2)) {
            return CalendarUtils.formatTime(j, SAME_YEAR_SKELETON + str);
        } else {
            return CalendarUtils.formatTime(j, DIFF_YEAR_SKELETON + str);
        }
    }

    public String getCalenderWidgetTime(CalendarSimpleData calendarSimpleData) {
        long currentTimeMillis = System.currentTimeMillis();
        String formatTime = formatTime(calendarSimpleData.getStartTime(), currentTimeMillis);
        String formatTime2 = formatTime(calendarSimpleData.getEndTime(), currentTimeMillis);
        if (CalendarUtils.isNextDay(currentTimeMillis, calendarSimpleData.getStartTime()) && CalendarUtils.isNextDay(currentTimeMillis, calendarSimpleData.getEndTime())) {
            String format = String.format("%s ", this.mContext.getString(C1894R.string.quick_look_widget_calendar_tomorrow));
            if (formatTime2.startsWith(format)) {
                formatTime2 = formatTime2.replaceFirst(format, "");
            }
        }
        return formatTime + " - " + formatTime2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r1 = r1.mCurrentCalendarEvent;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldShowCalendar() {
        /*
            r1 = this;
            boolean r0 = r1.mSwitchOn
            if (r0 == 0) goto L_0x0010
            com.nothing.systemui.keyguard.calendar.CalendarSimpleData r1 = r1.mCurrentCalendarEvent
            if (r1 == 0) goto L_0x0010
            boolean r1 = r1.isEventVisible()
            if (r1 == 0) goto L_0x0010
            r1 = 1
            goto L_0x0011
        L_0x0010:
            r1 = 0
        L_0x0011:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.keyguard.calendar.CalendarManager.shouldShowCalendar():boolean");
    }

    public boolean isCalendarSwitchOn() {
        return this.mSwitchOn;
    }

    private boolean updateCalendarSwitchOn() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), KEYGUARD_CALENDAR_SWITCH, 0) != 0;
    }

    /* access modifiers changed from: private */
    public void fireCalendarSwitchChanged() {
        this.mSwitchOn = updateCalendarSwitchOn();
        NTLogUtil.m1688i(TAG, "fireCalendarSwitchChanged =" + this.mSwitchOn);
        if (!this.mSwitchOn || hasCalendarReadPermission()) {
            Utils.safeForeach(this.mCallbacks, new CalendarManager$$ExternalSyntheticLambda2(this));
        } else {
            this.mContext.startActivity(new Intent(this.mContext, CalendarPermissionActivity.class).setFlags(268435456));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fireCalendarSwitchChanged$2$com-nothing-systemui-keyguard-calendar-CalendarManager */
    public /* synthetic */ void mo57467x9aec6e50(Callback callback) {
        callback.onCalendarSwitchChanged(this.mSwitchOn);
    }

    /* access modifiers changed from: private */
    public void fireCalendarDataChanged() {
        NTLogUtil.m1688i(TAG, "fireCalendarDataChanged =" + this.mSwitchOn);
        if (this.mSwitchOn) {
            beginLoadEvent();
        }
    }

    private final class LocalObserver extends ContentObserver {
        private boolean mCalendarRegistered;
        private boolean mRegistered;
        private final ContentResolver mResolver;

        public LocalObserver(Handler handler) {
            super(handler);
            this.mResolver = CalendarManager.this.mContext.getContentResolver();
        }

        public void register() {
            if (this.mRegistered) {
                this.mResolver.unregisterContentObserver(this);
            }
            NTLogUtil.m1688i(CalendarManager.TAG, "register");
            this.mResolver.registerContentObserver(CalendarManager.mCalendarSwitchUri, true, this);
            this.mRegistered = true;
            CalendarManager.this.fireCalendarSwitchChanged();
            registerCalendarEvents();
        }

        public boolean isCalendarEventsRegister() {
            return this.mCalendarRegistered;
        }

        public void registerCalendarEvents() {
            if (!this.mCalendarRegistered && CalendarManager.this.hasCalendarReadPermission()) {
                NTLogUtil.m1688i(CalendarManager.TAG, "registerCalendarEvents");
                this.mCalendarRegistered = true;
                this.mResolver.registerContentObserver(CalendarDataLoader.CALENDAR_EVENTS_URL, true, this);
                CalendarManager.this.fireCalendarDataChanged();
            }
        }

        public void onChange(boolean z, Uri uri) {
            NTLogUtil.m1688i(CalendarManager.TAG, "onChange uri=" + uri);
            if (CalendarManager.mCalendarSwitchUri.equals(uri)) {
                CalendarManager.this.fireCalendarSwitchChanged();
            } else {
                CalendarManager.this.fireCalendarDataChanged();
            }
        }
    }
}
