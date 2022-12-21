package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.icu.text.DateTimePatternGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.widget.TextView;
import androidx.slice.core.SliceHints;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeCommandReceiver;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Clock extends TextView implements DemoModeCommandReceiver, TunerService.Tunable, CommandQueue.Callbacks, DarkIconDispatcher.DarkReceiver, ConfigurationController.ConfigurationListener {
    private static final int AM_PM_STYLE_GONE = 2;
    private static final int AM_PM_STYLE_NORMAL = 0;
    private static final int AM_PM_STYLE_SMALL = 1;
    public static final String CLOCK_SECONDS = "clock_seconds";
    private static final String CLOCK_SUPER_PARCELABLE = "clock_super_parcelable";
    private static final String CURRENT_USER_ID = "current_user_id";
    private static final String SHOW_SECONDS = "show_seconds";
    private static final String VISIBILITY = "visibility";
    private static final String VISIBLE_BY_POLICY = "visible_by_policy";
    private static final String VISIBLE_BY_USER = "visible_by_user";
    private final int mAmPmStyle;
    private boolean mAttached;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private int mCachedWidth;
    /* access modifiers changed from: private */
    public Calendar mCalendar;
    private int mCharsAtCurrentWidth;
    /* access modifiers changed from: private */
    public SimpleDateFormat mClockFormat;
    private boolean mClockVisibleByPolicy;
    private boolean mClockVisibleByUser;
    private final CommandQueue mCommandQueue;
    private SimpleDateFormat mContentDescriptionFormat;
    /* access modifiers changed from: private */
    public String mContentDescriptionFormatString;
    /* access modifiers changed from: private */
    public int mCurrentUserId;
    private final CurrentUserTracker mCurrentUserTracker;
    /* access modifiers changed from: private */
    public DateTimePatternGenerator mDateTimePatternGenerator;
    private boolean mDemoMode;
    private final BroadcastReceiver mIntentReceiver;
    /* access modifiers changed from: private */
    public Locale mLocale;
    private int mNonAdaptedColor;
    private final BroadcastReceiver mScreenReceiver;
    private boolean mScreenReceiverRegistered;
    /* access modifiers changed from: private */
    public final Runnable mSecondTick;
    /* access modifiers changed from: private */
    public Handler mSecondsHandler;
    private boolean mShowSeconds;

    public Clock(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    /* JADX INFO: finally extract failed */
    public Clock(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mClockVisibleByPolicy = true;
        this.mClockVisibleByUser = true;
        this.mCharsAtCurrentWidth = -1;
        this.mCachedWidth = -1;
        this.mIntentReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Handler handler = Clock.this.getHandler();
                if (handler != null) {
                    String action = intent.getAction();
                    if (action.equals("android.intent.action.TIMEZONE_CHANGED")) {
                        handler.post(new Clock$2$$ExternalSyntheticLambda0(this, intent.getStringExtra("time-zone")));
                    } else if (action.equals("android.intent.action.CONFIGURATION_CHANGED")) {
                        handler.post(new Clock$2$$ExternalSyntheticLambda1(this, Clock.this.getResources().getConfiguration().locale));
                    }
                    handler.post(new Clock$2$$ExternalSyntheticLambda2(this));
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onReceive$0$com-android-systemui-statusbar-policy-Clock$2  reason: not valid java name */
            public /* synthetic */ void m3225lambda$onReceive$0$comandroidsystemuistatusbarpolicyClock$2(String str) {
                Calendar unused = Clock.this.mCalendar = Calendar.getInstance(TimeZone.getTimeZone(str));
                if (Clock.this.mClockFormat != null) {
                    Clock.this.mClockFormat.setTimeZone(Clock.this.mCalendar.getTimeZone());
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onReceive$1$com-android-systemui-statusbar-policy-Clock$2  reason: not valid java name */
            public /* synthetic */ void m3226lambda$onReceive$1$comandroidsystemuistatusbarpolicyClock$2(Locale locale) {
                if (!locale.equals(Clock.this.mLocale)) {
                    Locale unused = Clock.this.mLocale = locale;
                    String unused2 = Clock.this.mContentDescriptionFormatString = "";
                    DateTimePatternGenerator unused3 = Clock.this.mDateTimePatternGenerator = null;
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onReceive$2$com-android-systemui-statusbar-policy-Clock$2  reason: not valid java name */
            public /* synthetic */ void m3227lambda$onReceive$2$comandroidsystemuistatusbarpolicyClock$2() {
                Clock.this.updateClock();
            }
        };
        this.mScreenReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.intent.action.SCREEN_OFF".equals(action)) {
                    if (Clock.this.mSecondsHandler != null) {
                        Clock.this.mSecondsHandler.removeCallbacks(Clock.this.mSecondTick);
                    }
                } else if ("android.intent.action.SCREEN_ON".equals(action) && Clock.this.mSecondsHandler != null) {
                    Clock.this.mSecondsHandler.postAtTime(Clock.this.mSecondTick, ((SystemClock.uptimeMillis() / 1000) * 1000) + 1000);
                }
            }
        };
        this.mSecondTick = new Runnable() {
            public void run() {
                if (Clock.this.mCalendar != null) {
                    Clock.this.updateClock();
                }
                Clock.this.mSecondsHandler.postAtTime(this, ((SystemClock.uptimeMillis() / 1000) * 1000) + 1000);
            }
        };
        this.mCommandQueue = (CommandQueue) Dependency.get(CommandQueue.class);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, C1893R.styleable.Clock, 0, 0);
        try {
            this.mAmPmStyle = obtainStyledAttributes.getInt(0, 2);
            this.mNonAdaptedColor = getCurrentTextColor();
            obtainStyledAttributes.recycle();
            BroadcastDispatcher broadcastDispatcher = (BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class);
            this.mBroadcastDispatcher = broadcastDispatcher;
            this.mCurrentUserTracker = new CurrentUserTracker(broadcastDispatcher) {
                public void onUserSwitched(int i) {
                    int unused = Clock.this.mCurrentUserId = i;
                }
            };
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CLOCK_SUPER_PARCELABLE, super.onSaveInstanceState());
        bundle.putInt(CURRENT_USER_ID, this.mCurrentUserId);
        bundle.putBoolean(VISIBLE_BY_POLICY, this.mClockVisibleByPolicy);
        bundle.putBoolean(VISIBLE_BY_USER, this.mClockVisibleByUser);
        bundle.putBoolean(SHOW_SECONDS, this.mShowSeconds);
        bundle.putInt("visibility", getVisibility());
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !(parcelable instanceof Bundle)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable(CLOCK_SUPER_PARCELABLE));
        if (bundle.containsKey(CURRENT_USER_ID)) {
            this.mCurrentUserId = bundle.getInt(CURRENT_USER_ID);
        }
        this.mClockVisibleByPolicy = bundle.getBoolean(VISIBLE_BY_POLICY, true);
        this.mClockVisibleByUser = bundle.getBoolean(VISIBLE_BY_USER, true);
        this.mShowSeconds = bundle.getBoolean(SHOW_SECONDS, false);
        if (bundle.containsKey("visibility")) {
            super.setVisibility(bundle.getInt("visibility"));
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            this.mAttached = true;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_TICK");
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            this.mBroadcastDispatcher.registerReceiverWithHandler(this.mIntentReceiver, intentFilter, (Handler) Dependency.get(Dependency.TIME_TICK_HANDLER), UserHandle.ALL);
            ((TunerService) Dependency.get(TunerService.class)).addTunable(this, CLOCK_SECONDS, StatusBarIconController.ICON_HIDE_LIST);
            this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
            this.mCurrentUserTracker.startTracking();
            this.mCurrentUserId = this.mCurrentUserTracker.getCurrentUserId();
        }
        this.mCalendar = Calendar.getInstance(TimeZone.getDefault());
        this.mContentDescriptionFormatString = "";
        this.mDateTimePatternGenerator = null;
        updateClock();
        updateClockVisibility();
        updateShowSeconds();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mScreenReceiverRegistered) {
            this.mScreenReceiverRegistered = false;
            this.mBroadcastDispatcher.unregisterReceiver(this.mScreenReceiver);
            Handler handler = this.mSecondsHandler;
            if (handler != null) {
                handler.removeCallbacks(this.mSecondTick);
                this.mSecondsHandler = null;
            }
        }
        if (this.mAttached) {
            this.mBroadcastDispatcher.unregisterReceiver(this.mIntentReceiver);
            this.mAttached = false;
            ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
            this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
            this.mCurrentUserTracker.stopTracking();
        }
    }

    public void setVisibility(int i) {
        if (i != 0 || shouldBeVisible()) {
            super.setVisibility(i);
        }
    }

    public void setClockVisibleByUser(boolean z) {
        this.mClockVisibleByUser = z;
        updateClockVisibility();
    }

    public void setClockVisibilityByPolicy(boolean z) {
        this.mClockVisibleByPolicy = z;
        updateClockVisibility();
    }

    private boolean shouldBeVisible() {
        return this.mClockVisibleByPolicy && this.mClockVisibleByUser;
    }

    private void updateClockVisibility() {
        super.setVisibility(shouldBeVisible() ? 0 : 8);
    }

    /* access modifiers changed from: package-private */
    public final void updateClock() {
        if (!this.mDemoMode) {
            this.mCalendar.setTimeInMillis(System.currentTimeMillis());
            CharSequence smallTime = getSmallTime();
            if (!TextUtils.equals(smallTime, getText())) {
                setText(smallTime);
            }
            setContentDescription(this.mContentDescriptionFormat.format(this.mCalendar.getTime()));
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int length = getText().length();
        if (length != this.mCharsAtCurrentWidth) {
            this.mCharsAtCurrentWidth = length;
            this.mCachedWidth = getMeasuredWidth();
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int i3 = this.mCachedWidth;
        if (i3 > measuredWidth) {
            setMeasuredDimension(i3, getMeasuredHeight());
        } else {
            this.mCachedWidth = measuredWidth;
        }
    }

    public void onTuningChanged(String str, String str2) {
        if (CLOCK_SECONDS.equals(str)) {
            this.mShowSeconds = TunerService.parseIntegerSwitch(str2, false);
            updateShowSeconds();
        } else if (StatusBarIconController.ICON_HIDE_LIST.equals(str)) {
            setClockVisibleByUser(!StatusBarIconController.getIconHideList(getContext(), str2).contains(DemoMode.COMMAND_CLOCK));
            updateClockVisibility();
        }
    }

    public void disable(int i, int i2, int i3, boolean z) {
        if (i == getDisplay().getDisplayId()) {
            boolean z2 = (8388608 & i2) == 0;
            if (z2 != this.mClockVisibleByPolicy) {
                setClockVisibilityByPolicy(z2);
            }
        }
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        int tint = DarkIconDispatcher.getTint(arrayList, this, i);
        this.mNonAdaptedColor = tint;
        setTextColor(tint);
    }

    public void onColorsChanged(boolean z) {
        setTextColor(Utils.getColorAttrDefaultColor(new ContextThemeWrapper(this.mContext, z ? C1893R.style.Theme_SystemUI_LightWallpaper : C1893R.style.Theme_SystemUI), C1893R.attr.wallpaperTextColor));
    }

    public void onDensityOrFontScaleChanged() {
        FontSizeUtils.updateFontSize(this, C1893R.dimen.status_bar_clock_size);
        setPaddingRelative(this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.status_bar_clock_starting_padding), 0, this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.status_bar_clock_end_padding), 0);
    }

    private void updateShowSeconds() {
        if (this.mShowSeconds) {
            if (this.mSecondsHandler == null && getDisplay() != null) {
                this.mSecondsHandler = new Handler();
                if (getDisplay().getState() == 2) {
                    this.mSecondsHandler.postAtTime(this.mSecondTick, ((SystemClock.uptimeMillis() / 1000) * 1000) + 1000);
                }
                this.mScreenReceiverRegistered = true;
                IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
                intentFilter.addAction("android.intent.action.SCREEN_ON");
                this.mBroadcastDispatcher.registerReceiver(this.mScreenReceiver, intentFilter);
            }
        } else if (this.mSecondsHandler != null) {
            this.mScreenReceiverRegistered = false;
            this.mBroadcastDispatcher.unregisterReceiver(this.mScreenReceiver);
            this.mSecondsHandler.removeCallbacks(this.mSecondTick);
            this.mSecondsHandler = null;
            updateClock();
        }
    }

    private final CharSequence getSmallTime() {
        Context context = getContext();
        boolean is24HourFormat = DateFormat.is24HourFormat(context, this.mCurrentUserId);
        if (this.mDateTimePatternGenerator == null) {
            this.mDateTimePatternGenerator = DateTimePatternGenerator.getInstance(context.getResources().getConfiguration().locale);
        }
        String bestPattern = this.mDateTimePatternGenerator.getBestPattern(this.mShowSeconds ? is24HourFormat ? android.icu.text.DateFormat.HOUR24_MINUTE_SECOND : "hms" : is24HourFormat ? android.icu.text.DateFormat.HOUR24_MINUTE : "hm");
        if (!bestPattern.equals(this.mContentDescriptionFormatString)) {
            this.mContentDescriptionFormatString = bestPattern;
            this.mContentDescriptionFormat = new SimpleDateFormat(bestPattern);
            if (this.mAmPmStyle != 0) {
                int i = 0;
                boolean z = false;
                while (true) {
                    if (i >= bestPattern.length()) {
                        i = -1;
                        break;
                    }
                    char charAt = bestPattern.charAt(i);
                    if (charAt == '\'') {
                        z = !z;
                    }
                    if (!z && charAt == 'a') {
                        break;
                    }
                    i++;
                }
                if (i >= 0) {
                    int i2 = i;
                    while (i2 > 0 && Character.isWhitespace(bestPattern.charAt(i2 - 1))) {
                        i2--;
                    }
                    bestPattern = bestPattern.substring(0, i2) + 61184 + bestPattern.substring(i2, i) + "a" + bestPattern.substring(i + 1);
                }
            }
            this.mClockFormat = new SimpleDateFormat(bestPattern);
        }
        String format = this.mClockFormat.format(this.mCalendar.getTime());
        if (this.mAmPmStyle != 0) {
            int indexOf = format.indexOf(61184);
            int indexOf2 = format.indexOf(61185);
            if (indexOf >= 0 && indexOf2 > indexOf) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
                int i3 = this.mAmPmStyle;
                if (i3 == 2) {
                    spannableStringBuilder.delete(indexOf, indexOf2 + 1);
                } else {
                    if (i3 == 1) {
                        spannableStringBuilder.setSpan(new RelativeSizeSpan(0.7f), indexOf, indexOf2, 34);
                    }
                    spannableStringBuilder.delete(indexOf2, indexOf2 + 1);
                    spannableStringBuilder.delete(indexOf, indexOf + 1);
                }
                return spannableStringBuilder;
            }
        }
        return format;
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
        String string = bundle.getString(SliceHints.SUBTYPE_MILLIS);
        String string2 = bundle.getString("hhmm");
        if (string != null) {
            this.mCalendar.setTimeInMillis(Long.parseLong(string));
        } else if (string2 != null && string2.length() == 4) {
            int parseInt = Integer.parseInt(string2.substring(0, 2));
            int parseInt2 = Integer.parseInt(string2.substring(2));
            if (DateFormat.is24HourFormat(getContext(), this.mCurrentUserId)) {
                this.mCalendar.set(11, parseInt);
            } else {
                this.mCalendar.set(10, parseInt);
            }
            this.mCalendar.set(12, parseInt2);
        }
        setText(getSmallTime());
        setContentDescription(this.mContentDescriptionFormat.format(this.mCalendar.getTime()));
    }

    public void onDemoModeStarted() {
        this.mDemoMode = true;
    }

    public void onDemoModeFinished() {
        this.mDemoMode = false;
        updateClock();
    }
}
