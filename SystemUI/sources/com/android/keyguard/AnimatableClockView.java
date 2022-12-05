package com.android.keyguard;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class AnimatableClockView extends TextView {
    private static final CharSequence DOUBLE_LINE_FORMAT_12_HOUR = "hh\nmm";
    private static final CharSequence DOUBLE_LINE_FORMAT_24_HOUR = "HH\nmm";
    private int mChargeAnimationDelay;
    private CharSequence mDescFormat;
    private int mDozingColor;
    private final int mDozingWeight;
    private CharSequence mFormat;
    private boolean mIsSingleLine;
    private float mLineSpacingScale;
    private int mLockScreenColor;
    private final int mLockScreenWeight;
    private Runnable mOnTextAnimatorInitialized;
    private TextAnimator mTextAnimator;
    private final Calendar mTime;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface DozeStateGetter {
        boolean isDozing();
    }

    public AnimatableClockView(Context context) {
        this(context, null, 0, 0);
    }

    public AnimatableClockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public AnimatableClockView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public AnimatableClockView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTime = Calendar.getInstance();
        this.mLineSpacingScale = 1.0f;
        this.mChargeAnimationDelay = 0;
        this.mTextAnimator = null;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.AnimatableClockView, i, i2);
        try {
            this.mDozingWeight = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_dozeWeight, 100);
            this.mLockScreenWeight = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_lockScreenWeight, 300);
            this.mChargeAnimationDelay = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_chargeAnimationDelay, 200);
            obtainStyledAttributes.recycle();
            obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TextView, i, i2);
            try {
                this.mIsSingleLine = obtainStyledAttributes.getBoolean(32, false);
                obtainStyledAttributes.recycle();
                refreshFormat();
            } finally {
            }
        } finally {
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFormat();
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshTime() {
        this.mTime.setTimeInMillis(System.currentTimeMillis());
        setText(DateFormat.format(this.mFormat, this.mTime));
        setContentDescription(DateFormat.format(this.mDescFormat, this.mTime));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTimeZoneChanged(TimeZone timeZone) {
        this.mTime.setTimeZone(timeZone);
        refreshFormat();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        TextAnimator textAnimator = this.mTextAnimator;
        if (textAnimator == null) {
            this.mTextAnimator = new TextAnimator(getLayout(), new Function0() { // from class: com.android.keyguard.AnimatableClockView$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function0
                /* renamed from: invoke */
                public final Object mo1951invoke() {
                    Unit lambda$onMeasure$0;
                    lambda$onMeasure$0 = AnimatableClockView.this.lambda$onMeasure$0();
                    return lambda$onMeasure$0;
                }
            });
            Runnable runnable = this.mOnTextAnimatorInitialized;
            if (runnable == null) {
                return;
            }
            runnable.run();
            this.mOnTextAnimatorInitialized = null;
            return;
        }
        textAnimator.updateLayout(getLayout());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Unit lambda$onMeasure$0() {
        invalidate();
        return Unit.INSTANCE;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        this.mTextAnimator.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLineSpacingScale(float f) {
        this.mLineSpacingScale = f;
        setLineSpacing(0.0f, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setColors(int i, int i2) {
        this.mDozingColor = i;
        this.mLockScreenColor = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void animateAppearOnLockscreen() {
        if (this.mTextAnimator == null) {
            return;
        }
        setTextStyle(this.mDozingWeight, -1.0f, Integer.valueOf(this.mLockScreenColor), false, 0L, 0L, null);
        setTextStyle(this.mLockScreenWeight, -1.0f, Integer.valueOf(this.mLockScreenColor), true, 350L, 0L, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void animateCharge(final DozeStateGetter dozeStateGetter) {
        TextAnimator textAnimator = this.mTextAnimator;
        if (textAnimator == null || textAnimator.isRunning()) {
            return;
        }
        setTextStyle(dozeStateGetter.isDozing() ? this.mLockScreenWeight : this.mDozingWeight, -1.0f, null, true, 500L, this.mChargeAnimationDelay, new Runnable() { // from class: com.android.keyguard.AnimatableClockView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                AnimatableClockView.this.lambda$animateCharge$1(dozeStateGetter);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateCharge$1(DozeStateGetter dozeStateGetter) {
        setTextStyle(dozeStateGetter.isDozing() ? this.mDozingWeight : this.mLockScreenWeight, -1.0f, null, true, 1000L, 0L, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void animateDoze(boolean z, boolean z2) {
        setTextStyle(z ? this.mDozingWeight : this.mLockScreenWeight, -1.0f, Integer.valueOf(z ? this.mDozingColor : this.mLockScreenColor), z2, 300L, 0L, null);
    }

    private void setTextStyle(final int i, final float f, final Integer num, boolean z, final long j, final long j2, final Runnable runnable) {
        TextAnimator textAnimator = this.mTextAnimator;
        if (textAnimator != null) {
            textAnimator.setTextStyle(i, f, num, z, j, null, j2, runnable);
        } else {
            this.mOnTextAnimatorInitialized = new Runnable() { // from class: com.android.keyguard.AnimatableClockView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatableClockView.this.lambda$setTextStyle$2(i, f, num, j, j2, runnable);
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setTextStyle$2(int i, float f, Integer num, long j, long j2, Runnable runnable) {
        this.mTextAnimator.setTextStyle(i, f, num, false, j, null, j2, runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshFormat() {
        Patterns.update(((TextView) this).mContext);
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
    /* loaded from: classes.dex */
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
