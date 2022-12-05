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
public class NothingKeyguardClockView extends RelativeLayout {
    private static final CharSequence DOUBLE_LINE_FORMAT_12_HOUR = "hh:mm";
    private static final CharSequence DOUBLE_LINE_FORMAT_24_HOUR = "HH:mm";
    private TextView mClockTv;
    private CharSequence mDescFormat;
    private CharSequence mFormat;
    boolean mIsSingleLine;
    private float mLineSpacingScale;
    private final Calendar mTime;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLineSpacingScale(float f) {
    }

    public NothingKeyguardClockView(Context context) {
        this(context, null, 0, 0);
    }

    public NothingKeyguardClockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public NothingKeyguardClockView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NothingKeyguardClockView(Context context, AttributeSet attributeSet, int i, int i2) {
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
        this.mClockTv = (TextView) findViewById(R$id.nothing_clock_view_clock);
    }

    public void setTextSize(int i, float f) {
        TextView textView = this.mClockTv;
        if (textView == null) {
            return;
        }
        textView.setTextSize(i, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshTime() {
        if (this.mClockTv == null) {
            return;
        }
        this.mTime.setTimeInMillis(System.currentTimeMillis());
        this.mClockTv.setText(DateFormat.format(this.mFormat, this.mTime).toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTimeZoneChanged(TimeZone timeZone) {
        this.mTime.setTimeZone(timeZone);
        refreshFormat();
    }

    public int getCurrtentTextColor() {
        TextView textView = this.mClockTv;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return 4095;
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
