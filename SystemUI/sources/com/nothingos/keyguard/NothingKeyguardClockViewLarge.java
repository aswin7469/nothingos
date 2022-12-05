package com.nothingos.keyguard;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
/* loaded from: classes2.dex */
public class NothingKeyguardClockViewLarge extends RelativeLayout {
    private static final CharSequence DOUBLE_LINE_FORMAT_12_HOUR = "hh:mm";
    private static final CharSequence DOUBLE_LINE_FORMAT_24_HOUR = "HH:mm";
    private CharSequence mDescFormat;
    private CharSequence mFormat;
    private TextView mHour1;
    private TextView mHour2;
    boolean mIsSingleLine;
    private float mLineSpacingScale;
    private TextView mMinute1;
    private TextView mMinute2;
    private final Calendar mTime;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLineSpacingScale(float f) {
    }

    public NothingKeyguardClockViewLarge(Context context) {
        this(context, null, 0, 0);
    }

    public NothingKeyguardClockViewLarge(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public NothingKeyguardClockViewLarge(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NothingKeyguardClockViewLarge(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTime = Calendar.getInstance();
        this.mLineSpacingScale = 1.0f;
        this.mIsSingleLine = false;
        refreshFormat();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFormat();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHour1 = (TextView) findViewById(R$id.nothing_clock_view_hour1);
        this.mHour2 = (TextView) findViewById(R$id.nothing_clock_view_hour2);
        this.mMinute1 = (TextView) findViewById(R$id.nothing_clock_view_minute1);
        this.mMinute2 = (TextView) findViewById(R$id.nothing_clock_view_minute2);
    }

    public void setTextSize(int i, float f) {
        TextView textView = this.mHour1;
        if (textView == null || this.mHour2 == null || this.mMinute1 == null || this.mMinute2 == null) {
            return;
        }
        textView.setTextSize(i, f);
        this.mHour2.setTextSize(i, f);
        this.mMinute1.setTextSize(i, f);
        this.mMinute2.setTextSize(i, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshTime() {
        if (this.mHour1 == null || this.mHour2 == null || this.mMinute1 == null || this.mMinute2 == null) {
            return;
        }
        this.mTime.setTimeInMillis(System.currentTimeMillis());
        String[] split = DateFormat.format(this.mFormat, this.mTime).toString().split(":");
        try {
            if (split.length > 0 && split[0] != null) {
                Integer valueOf = Integer.valueOf(Integer.parseInt(split[0]));
                int intValue = valueOf.intValue() / 10;
                if (intValue == 0 && this.mIsSingleLine) {
                    this.mHour1.setVisibility(8);
                } else {
                    this.mHour1.setText(String.valueOf(intValue));
                }
                this.mHour2.setText(String.valueOf(valueOf.intValue() % 10));
            }
            if (split.length <= 1 || split[1] == null) {
                return;
            }
            Integer valueOf2 = Integer.valueOf(Integer.parseInt(split[1]));
            this.mMinute1.setText(String.valueOf(valueOf2.intValue() / 10));
            this.mMinute2.setText(String.valueOf(valueOf2.intValue() % 10));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTimeZoneChanged(TimeZone timeZone) {
        this.mTime.setTimeZone(timeZone);
        refreshFormat();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshFormat() {
        Patterns.update(((RelativeLayout) this).mContext);
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        boolean z = this.mIsSingleLine;
        if (z && is24HourFormat) {
            this.mFormat = Patterns.sClockView24;
        } else if (!z && is24HourFormat) {
            this.mFormat = DOUBLE_LINE_FORMAT_24_HOUR;
        } else if (z && !is24HourFormat) {
            this.mFormat = Patterns.sClockView12;
        } else {
            this.mFormat = DOUBLE_LINE_FORMAT_12_HOUR;
        }
        this.mDescFormat = is24HourFormat ? Patterns.sClockView24 : Patterns.sClockView12;
        refreshTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class Patterns {
        static String sCacheKey;
        static String sClockView12;
        static String sClockView24;

        static void update(Context context) {
            Locale locale = Locale.getDefault();
            Resources resources = context.getResources();
            String string = resources.getString(R$string.clock_12hr_format);
            String string2 = resources.getString(R$string.clock_24hr_format);
            String str = locale.toString() + string + string2;
            if (str.equals(sCacheKey)) {
                return;
            }
            sClockView12 = DateFormat.getBestDateTimePattern(locale, string);
            if (!string.contains("a")) {
                sClockView12 = sClockView12.replaceAll("a", "").trim();
            }
            sClockView24 = DateFormat.getBestDateTimePattern(locale, string2);
            sCacheKey = str;
        }
    }
}
