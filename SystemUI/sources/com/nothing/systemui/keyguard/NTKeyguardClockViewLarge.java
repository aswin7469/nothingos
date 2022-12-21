package com.nothing.systemui.keyguard;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.systemui.C1893R;
import dalvik.bytecode.Opcodes;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class NTKeyguardClockViewLarge extends RelativeLayout {
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

    /* access modifiers changed from: package-private */
    public void setColors(int i, int i2) {
    }

    /* access modifiers changed from: package-private */
    public void setLineSpacingScale(float f) {
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public NTKeyguardClockViewLarge(Context context) {
        this(context, (AttributeSet) null, 0, 0);
    }

    public NTKeyguardClockViewLarge(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public NTKeyguardClockViewLarge(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NTKeyguardClockViewLarge(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTime = Calendar.getInstance();
        this.mLineSpacingScale = 1.0f;
        this.mIsSingleLine = false;
        refreshFormat();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFormat();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mHour1 = (TextView) findViewById(C1893R.C1897id.nothing_clock_view_hour1);
        this.mHour2 = (TextView) findViewById(C1893R.C1897id.nothing_clock_view_hour2);
        this.mMinute1 = (TextView) findViewById(C1893R.C1897id.nothing_clock_view_minute1);
        this.mMinute2 = (TextView) findViewById(C1893R.C1897id.nothing_clock_view_minute2);
    }

    public void setTextSize(int i, float f) {
        TextView textView = this.mHour1;
        if (textView != null && this.mHour2 != null && this.mMinute1 != null && this.mMinute2 != null) {
            textView.setTextSize(i, f);
            this.mHour2.setTextSize(i, f);
            this.mMinute1.setTextSize(i, f);
            this.mMinute2.setTextSize(i, f);
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshTime() {
        String str;
        String str2;
        if (this.mHour1 != null && this.mHour2 != null && this.mMinute1 != null && this.mMinute2 != null) {
            this.mTime.setTimeInMillis(System.currentTimeMillis());
            String[] split = DateFormat.format(this.mFormat, this.mTime).toString().split(":");
            try {
                if (split.length > 0 && (str2 = split[0]) != null) {
                    Integer valueOf = Integer.valueOf(Integer.parseInt(str2));
                    int intValue = valueOf.intValue() / 10;
                    if (intValue != 0 || !this.mIsSingleLine) {
                        this.mHour1.setText(String.valueOf(intValue));
                    } else {
                        this.mHour1.setVisibility(8);
                    }
                    this.mHour2.setText(String.valueOf(valueOf.intValue() % 10));
                }
                if (split.length > 1 && (str = split[1]) != null) {
                    Integer valueOf2 = Integer.valueOf(Integer.parseInt(str));
                    this.mMinute1.setText(String.valueOf(valueOf2.intValue() / 10));
                    this.mMinute2.setText(String.valueOf(valueOf2.intValue() % 10));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onTimeZoneChanged(TimeZone timeZone) {
        this.mTime.setTimeZone(timeZone);
        refreshFormat();
    }

    public int getCurrtentTextColor() {
        TextView textView = this.mHour1;
        return textView != null ? textView.getCurrentTextColor() : Opcodes.OP_IPUT_OBJECT_JUMBO;
    }

    /* access modifiers changed from: package-private */
    public void refreshFormat() {
        Patterns.update(this.mContext);
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        boolean z = this.mIsSingleLine;
        if (z && is24HourFormat) {
            this.mFormat = Patterns.sClockView24;
        } else if (!z && is24HourFormat) {
            this.mFormat = DOUBLE_LINE_FORMAT_24_HOUR;
        } else if (!z || is24HourFormat) {
            this.mFormat = DOUBLE_LINE_FORMAT_12_HOUR;
        } else {
            this.mFormat = Patterns.sClockView12;
        }
        this.mDescFormat = is24HourFormat ? Patterns.sClockView24 : Patterns.sClockView12;
        refreshTime();
    }

    private static final class Patterns {
        static String sCacheKey;
        static String sClockView12;
        static String sClockView24;

        private Patterns() {
        }

        static void update(Context context) {
            Locale locale = Locale.getDefault();
            Resources resources = context.getResources();
            String string = resources.getString(C1893R.string.clock_12hr_format);
            String string2 = resources.getString(C1893R.string.clock_24hr_format);
            String str = locale.toString() + string + string2;
            if (!str.equals(sCacheKey)) {
                sClockView12 = DateFormat.getBestDateTimePattern(locale, string);
                if (!string.contains("a")) {
                    sClockView12 = sClockView12.replaceAll("a", "").trim();
                }
                sClockView24 = DateFormat.getBestDateTimePattern(locale, string2);
                sCacheKey = str;
            }
        }
    }
}
