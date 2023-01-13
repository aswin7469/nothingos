package com.android.keyguard;

import android.R;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import java.p026io.PrintWriter;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\r\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0002MNB/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007¢\u0006\u0002\u0010\tJ\u0006\u0010%\u001a\u00020&J\u000e\u0010'\u001a\u00020&2\u0006\u0010(\u001a\u00020)J\u0016\u0010*\u001a\u00020&2\u0006\u0010+\u001a\u00020\u00142\u0006\u0010,\u001a\u00020\u0014J\u0006\u0010-\u001a\u00020&J\u000e\u0010.\u001a\u00020&2\u0006\u0010/\u001a\u000200J\b\u00101\u001a\u00020&H\u0014J\u0010\u00102\u001a\u00020&2\u0006\u00103\u001a\u000204H\u0014J\u0018\u00105\u001a\u00020&2\u0006\u00106\u001a\u00020\u00072\u0006\u00107\u001a\u00020\u0007H\u0015J\u0010\u00108\u001a\u00020&2\b\u00109\u001a\u0004\u0018\u00010:J\u0006\u0010;\u001a\u00020&J\u0006\u0010<\u001a\u00020&J\u0016\u0010=\u001a\u00020&2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u0007J\u000e\u0010>\u001a\u00020&2\u0006\u0010?\u001a\u00020\u0017JS\u0010@\u001a\u00020&2\u0006\u0010A\u001a\u00020\u00072\u0006\u0010B\u001a\u00020\u00172\b\u0010C\u001a\u0004\u0018\u00010\u00072\u0006\u0010,\u001a\u00020\u00142\b\u0010D\u001a\u0004\u0018\u00010E2\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020G2\b\u0010I\u001a\u0004\u0018\u00010\u001dH\u0002¢\u0006\u0002\u0010JJI\u0010@\u001a\u00020&2\u0006\u0010A\u001a\u00020\u00072\u0006\u0010B\u001a\u00020\u00172\b\u0010C\u001a\u0004\u0018\u00010\u00072\u0006\u0010,\u001a\u00020\u00142\u0006\u0010F\u001a\u00020G2\u0006\u0010H\u001a\u00020G2\b\u0010I\u001a\u0004\u0018\u00010\u001dH\u0002¢\u0006\u0002\u0010KJ\u0006\u0010L\u001a\u00020\u0014R\u000e\u0010\n\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000e\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0019\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u0010R\u000e\u0010\u001b\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fXD¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\"\u001a\n $*\u0004\u0018\u00010#0#X\u0004¢\u0006\u0002\n\u0000¨\u0006O"}, mo65043d2 = {"Lcom/android/keyguard/AnimatableClockView;", "Landroid/widget/TextView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "defStyleAttr", "", "defStyleRes", "(Landroid/content/Context;Landroid/util/AttributeSet;II)V", "chargeAnimationDelay", "descFormat", "", "dozingColor", "dozingWeight", "getDozingWeight", "()I", "dozingWeightInternal", "format", "isSingleLineInternal", "", "lastMeasureCall", "lineSpacingScale", "", "lockScreenColor", "lockScreenWeight", "getLockScreenWeight", "lockScreenWeightInternal", "onTextAnimatorInitialized", "Ljava/lang/Runnable;", "tag", "", "textAnimator", "Lcom/android/keyguard/TextAnimator;", "time", "Ljava/util/Calendar;", "kotlin.jvm.PlatformType", "animateAppearOnLockscreen", "", "animateCharge", "dozeStateGetter", "Lcom/android/keyguard/AnimatableClockView$DozeStateGetter;", "animateDoze", "isDozing", "animate", "animateFoldAppear", "dump", "pw", "Ljava/io/PrintWriter;", "onAttachedToWindow", "onDraw", "canvas", "Landroid/graphics/Canvas;", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "onTimeZoneChanged", "timeZone", "Ljava/util/TimeZone;", "refreshFormat", "refreshTime", "setColors", "setLineSpacingScale", "scale", "setTextStyle", "weight", "textSize", "color", "interpolator", "Landroid/animation/TimeInterpolator;", "duration", "", "delay", "onAnimationEnd", "(IFLjava/lang/Integer;ZLandroid/animation/TimeInterpolator;JJLjava/lang/Runnable;)V", "(IFLjava/lang/Integer;ZJJLjava/lang/Runnable;)V", "useBoldedVersion", "DozeStateGetter", "Patterns", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AnimatableClockView.kt */
public final class AnimatableClockView extends TextView {
    public Map<Integer, View> _$_findViewCache;
    private final int chargeAnimationDelay;
    private CharSequence descFormat;
    private int dozingColor;
    private final int dozingWeightInternal;
    private CharSequence format;
    private final boolean isSingleLineInternal;
    private CharSequence lastMeasureCall;
    private float lineSpacingScale;
    private int lockScreenColor;
    private final int lockScreenWeightInternal;
    private Runnable onTextAnimatorInitialized;
    private final String tag;
    private TextAnimator textAnimator;
    private final Calendar time;

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0005À\u0006\u0001"}, mo65043d2 = {"Lcom/android/keyguard/AnimatableClockView$DozeStateGetter;", "", "isDozing", "", "()Z", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AnimatableClockView.kt */
    public interface DozeStateGetter {
        boolean isDozing();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AnimatableClockView(Context context) {
        this(context, (AttributeSet) null, 0, 0, 14, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AnimatableClockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AnimatableClockView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: finally extract failed */
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AnimatableClockView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Intrinsics.checkNotNullParameter(context, "context");
        this._$_findViewCache = new LinkedHashMap();
        this.tag = "AnimatableClockView";
        this.lastMeasureCall = "";
        this.time = Calendar.getInstance();
        this.lineSpacingScale = 1.0f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.AnimatableClockView, i, i2);
        try {
            this.dozingWeightInternal = obtainStyledAttributes.getInt(1, 100);
            this.lockScreenWeightInternal = obtainStyledAttributes.getInt(2, 300);
            this.chargeAnimationDelay = obtainStyledAttributes.getInt(0, 200);
            obtainStyledAttributes.recycle();
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.TextView, i, i2);
            try {
                boolean z = obtainStyledAttributes2.getBoolean(32, false);
                obtainStyledAttributes2.recycle();
                this.isSingleLineInternal = z;
                refreshFormat();
            } catch (Throwable th) {
                obtainStyledAttributes2.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            obtainStyledAttributes.recycle();
            throw th2;
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ AnimatableClockView(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    public final int getDozingWeight() {
        boolean useBoldedVersion = useBoldedVersion();
        int i = this.dozingWeightInternal;
        return useBoldedVersion ? i + 100 : i;
    }

    public final int getLockScreenWeight() {
        boolean useBoldedVersion = useBoldedVersion();
        int i = this.lockScreenWeightInternal;
        return useBoldedVersion ? i + 100 : i;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFormat();
    }

    public final boolean useBoldedVersion() {
        return getResources().getConfiguration().fontWeightAdjustment > 100;
    }

    public final void refreshTime() {
        this.time.setTimeInMillis(System.currentTimeMillis());
        setContentDescription(DateFormat.format(this.descFormat, this.time));
        CharSequence format2 = DateFormat.format(this.format, this.time);
        if (!TextUtils.equals(getText(), format2)) {
            setText(format2);
        }
    }

    public final void onTimeZoneChanged(TimeZone timeZone) {
        this.time.setTimeZone(timeZone);
        refreshFormat();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        CharSequence format2 = DateFormat.format(this.descFormat, System.currentTimeMillis());
        Intrinsics.checkNotNullExpressionValue(format2, "format(descFormat, System.currentTimeMillis())");
        this.lastMeasureCall = format2;
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 == null) {
            Layout layout = getLayout();
            Intrinsics.checkNotNullExpressionValue(layout, "layout");
            this.textAnimator = new TextAnimator(layout, new AnimatableClockView$onMeasure$1(this));
            Runnable runnable = this.onTextAnimatorInitialized;
            if (runnable != null) {
                runnable.run();
            }
            this.onTextAnimatorInitialized = null;
            return;
        }
        Layout layout2 = getLayout();
        Intrinsics.checkNotNullExpressionValue(layout2, "layout");
        textAnimator2.updateLayout(layout2);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 != null) {
            textAnimator2.draw(canvas);
        }
    }

    public final void setLineSpacingScale(float f) {
        this.lineSpacingScale = f;
        setLineSpacing(0.0f, f);
    }

    public final void setColors(int i, int i2) {
        this.dozingColor = i;
        this.lockScreenColor = i2;
    }

    public final void animateAppearOnLockscreen() {
        if (this.textAnimator != null) {
            setTextStyle(getDozingWeight(), -1.0f, Integer.valueOf(this.lockScreenColor), false, 0, 0, (Runnable) null);
            setTextStyle(getLockScreenWeight(), -1.0f, Integer.valueOf(this.lockScreenColor), true, 350, 0, (Runnable) null);
        }
    }

    public final void animateFoldAppear() {
        if (this.textAnimator != null) {
            setTextStyle(this.lockScreenWeightInternal, -1.0f, Integer.valueOf(this.lockScreenColor), false, 0, 0, (Runnable) null);
            setTextStyle(this.dozingWeightInternal, -1.0f, Integer.valueOf(this.dozingColor), true, Interpolators.EMPHASIZED_DECELERATE, 600, 0, (Runnable) null);
        }
    }

    public final void animateCharge(DozeStateGetter dozeStateGetter) {
        Intrinsics.checkNotNullParameter(dozeStateGetter, "dozeStateGetter");
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 != null) {
            Intrinsics.checkNotNull(textAnimator2);
            if (!textAnimator2.isRunning()) {
                setTextStyle(dozeStateGetter.isDozing() ? getLockScreenWeight() : getDozingWeight(), -1.0f, (Integer) null, true, 500, (long) this.chargeAnimationDelay, new AnimatableClockView$$ExternalSyntheticLambda0(this, dozeStateGetter));
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: animateCharge$lambda-0  reason: not valid java name */
    public static final void m2283animateCharge$lambda0(AnimatableClockView animatableClockView, DozeStateGetter dozeStateGetter) {
        Intrinsics.checkNotNullParameter(animatableClockView, "this$0");
        Intrinsics.checkNotNullParameter(dozeStateGetter, "$dozeStateGetter");
        animatableClockView.setTextStyle(dozeStateGetter.isDozing() ? animatableClockView.getDozingWeight() : animatableClockView.getLockScreenWeight(), -1.0f, (Integer) null, true, 1000, 0, (Runnable) null);
    }

    public final void animateDoze(boolean z, boolean z2) {
        setTextStyle(z ? getDozingWeight() : getLockScreenWeight(), -1.0f, Integer.valueOf(z ? this.dozingColor : this.lockScreenColor), z2, 300, 0, (Runnable) null);
    }

    private final void setTextStyle(int i, float f, Integer num, boolean z, TimeInterpolator timeInterpolator, long j, long j2, Runnable runnable) {
        TextAnimator textAnimator2 = this.textAnimator;
        if (textAnimator2 == null) {
            this.onTextAnimatorInitialized = new AnimatableClockView$$ExternalSyntheticLambda1(this, i, f, num, j, timeInterpolator, j2, runnable);
        } else if (textAnimator2 != null) {
            textAnimator2.setTextStyle(i, f, num, z, j, timeInterpolator, j2, runnable);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: setTextStyle$lambda-1  reason: not valid java name */
    public static final void m2284setTextStyle$lambda1(AnimatableClockView animatableClockView, int i, float f, Integer num, long j, TimeInterpolator timeInterpolator, long j2, Runnable runnable) {
        Intrinsics.checkNotNullParameter(animatableClockView, "this$0");
        TextAnimator textAnimator2 = animatableClockView.textAnimator;
        if (textAnimator2 != null) {
            textAnimator2.setTextStyle(i, f, num, false, j, timeInterpolator, j2, runnable);
        }
    }

    private final void setTextStyle(int i, float f, Integer num, boolean z, long j, long j2, Runnable runnable) {
        setTextStyle(i, f, num, z, (TimeInterpolator) null, j, j2, runnable);
    }

    public final void refreshFormat() {
        String str;
        Patterns patterns = Patterns.INSTANCE;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        patterns.update(context);
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        boolean z = this.isSingleLineInternal;
        if (!z || !is24HourFormat) {
            str = (z || !is24HourFormat) ? (!z || is24HourFormat) ? "hh\nmm" : Patterns.INSTANCE.getSClockView12() : "HH\nmm";
        } else {
            str = Patterns.INSTANCE.getSClockView24();
        }
        this.format = str;
        this.descFormat = is24HourFormat ? Patterns.INSTANCE.getSClockView24() : Patterns.INSTANCE.getSClockView12();
        refreshTime();
    }

    public final void dump(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printWriter.println(String.valueOf((Object) this));
        printWriter.println("    measuredWidth=" + getMeasuredWidth());
        printWriter.println("    measuredHeight=" + getMeasuredHeight());
        printWriter.println("    singleLineInternal=" + this.isSingleLineInternal);
        printWriter.println("    lastMeasureCall=" + this.lastMeasureCall);
        printWriter.println("    currText=" + getText());
        printWriter.println("    currTimeContextDesc=" + getContentDescription());
        printWriter.println("    time=" + this.time);
    }

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001c\u0010\f\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u0006\"\u0004\b\u000e\u0010\b¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/keyguard/AnimatableClockView$Patterns;", "", "()V", "sCacheKey", "", "getSCacheKey", "()Ljava/lang/String;", "setSCacheKey", "(Ljava/lang/String;)V", "sClockView12", "getSClockView12", "setSClockView12", "sClockView24", "getSClockView24", "setSClockView24", "update", "", "context", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AnimatableClockView.kt */
    private static final class Patterns {
        public static final Patterns INSTANCE = new Patterns();
        private static String sCacheKey;
        private static String sClockView12;
        private static String sClockView24;

        private Patterns() {
        }

        public final String getSClockView12() {
            return sClockView12;
        }

        public final void setSClockView12(String str) {
            sClockView12 = str;
        }

        public final String getSClockView24() {
            return sClockView24;
        }

        public final void setSClockView24(String str) {
            sClockView24 = str;
        }

        public final String getSCacheKey() {
            return sCacheKey;
        }

        public final void setSCacheKey(String str) {
            sCacheKey = str;
        }

        public final void update(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Locale locale = Locale.getDefault();
            Resources resources = context.getResources();
            String string = resources.getString(C1894R.string.clock_12hr_format);
            String string2 = resources.getString(C1894R.string.clock_24hr_format);
            String str = locale.toString() + string + string2;
            if (!Intrinsics.areEqual((Object) str, (Object) sCacheKey)) {
                String bestDateTimePattern = DateFormat.getBestDateTimePattern(locale, string);
                sClockView12 = bestDateTimePattern;
                Intrinsics.checkNotNullExpressionValue(string, "clockView12Skel");
                if (!StringsKt.contains$default((CharSequence) string, (CharSequence) "a", false, 2, (Object) null)) {
                    Intrinsics.checkNotNullExpressionValue(bestDateTimePattern, "clockView12");
                    CharSequence replace = new Regex("a").replace((CharSequence) bestDateTimePattern, "");
                    int length = replace.length() - 1;
                    int i = 0;
                    boolean z = false;
                    while (i <= length) {
                        boolean z2 = Intrinsics.compare((int) replace.charAt(!z ? i : length), 32) <= 0;
                        if (!z) {
                            if (!z2) {
                                z = true;
                            } else {
                                i++;
                            }
                        } else if (!z2) {
                            break;
                        } else {
                            length--;
                        }
                    }
                    sClockView12 = replace.subSequence(i, length + 1).toString();
                }
                sClockView24 = DateFormat.getBestDateTimePattern(locale, string2);
                sCacheKey = str;
            }
        }
    }
}
