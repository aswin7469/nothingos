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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NTKeyguardClockView extends RelativeLayout {
    private static final CharSequence DOUBLE_LINE_FORMAT_12_HOUR = "hh:mm";
    private static final CharSequence DOUBLE_LINE_FORMAT_24_HOUR = "HH:mm";
    private TextView mClockTv;
    private CharSequence mDescFormat;
    private CharSequence mFormat;
    boolean mIsSingleLine;
    private float mLineSpacingScale;
    private SimpleDateFormat mSimpleDateFormat;
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

    public NTKeyguardClockView(Context context) {
        this(context, (AttributeSet) null, 0, 0);
    }

    public NTKeyguardClockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public NTKeyguardClockView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NTKeyguardClockView(Context context, AttributeSet attributeSet, int i, int i2) {
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
        this.mClockTv = (TextView) findViewById(C1893R.C1897id.nothing_clock_view_clock);
    }

    public void setTextSize(int i, float f) {
        TextView textView = this.mClockTv;
        if (textView != null) {
            textView.setTextSize(i, f);
        }
    }

    /* access modifiers changed from: package-private */
    public void refreshTime() {
        TextView textView = this.mClockTv;
        if (textView != null) {
            textView.setText(this.mSimpleDateFormat.format(new Date()));
        }
    }

    /* access modifiers changed from: package-private */
    public void onTimeZoneChanged(TimeZone timeZone) {
        this.mTime.setTimeZone(timeZone);
        refreshFormat();
    }

    public int getCurrtentTextColor() {
        TextView textView = this.mClockTv;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.mFormat.toString(), Locale.ENGLISH);
        this.mSimpleDateFormat = simpleDateFormat;
        simpleDateFormat.setCalendar(this.mTime);
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
