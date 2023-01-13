package com.android.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.keyguard.dagger.KeyguardStatusViewScope;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.ClockPlugin;
import com.nothing.systemui.keyguard.NTKeyguardClockView;
import com.nothing.systemui.keyguard.NTKeyguardClockViewLarge;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.TimeZone;

@KeyguardStatusViewScope
public class KeyguardClockSwitch extends RelativeLayout {
    private static final long CLOCK_IN_MILLIS = 200;
    private static final long CLOCK_OUT_MILLIS = 150;
    public static final int LARGE = 0;
    public static final int SMALL = 1;
    private static final long STATUS_AREA_MOVE_MILLIS = 350;
    private static final String TAG = "KeyguardClockSwitch";
    boolean mChildrenAreLaidOut = false;
    private FrameLayout mClockFrame;
    AnimatorSet mClockInAnim = null;
    AnimatorSet mClockOutAnim = null;
    private ClockPlugin mClockPlugin;
    private int mClockSwitchYAmount;
    private AnimatableClockView mClockView;
    private int[] mColorPalette;
    private float mDarkAmount;
    private Integer mDisplayedClockSize = null;
    private int mKeyguardClockViewLargeMarginTop = 0;
    private FrameLayout mLargeClockFrame;
    private AnimatableClockView mLargeClockView;
    private NTKeyguardClockView mNothingClockView;
    private NTKeyguardClockViewLarge mNothingClockViewLarge;
    private int mSmartspaceTopOffset;
    private View mStatusArea;
    /* access modifiers changed from: private */
    public ObjectAnimator mStatusAreaAnim = null;
    private boolean mSupportsDarkText;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ClockSize {
    }

    /* access modifiers changed from: package-private */
    public void setClockPlugin(ClockPlugin clockPlugin, int i) {
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public KeyguardClockSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onDensityOrFontScaleChanged() {
        this.mLargeClockView.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.large_clock_text_size));
        this.mClockView.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.clock_text_size));
        this.mNothingClockView.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.nt_clock_text_size));
        this.mNothingClockViewLarge.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.nt_clock_text_size));
        this.mClockSwitchYAmount = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.keyguard_clock_switch_y_shift);
        this.mKeyguardClockViewLargeMarginTop = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.nt_keyguard_clock_large_top_margin);
    }

    public boolean hasCustomClock() {
        return this.mClockPlugin != null;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mClockFrame = (FrameLayout) findViewById(C1894R.C1898id.nothing_lockscreen_clock_view);
        this.mClockView = (AnimatableClockView) findViewById(C1894R.C1898id.animatable_clock_view);
        this.mLargeClockFrame = (FrameLayout) findViewById(C1894R.C1898id.nothing_lockscreen_clock_view_large);
        this.mLargeClockView = (AnimatableClockView) findViewById(C1894R.C1898id.animatable_clock_view_large);
        this.mStatusArea = findViewById(C1894R.C1898id.keyguard_status_area);
        this.mNothingClockView = (NTKeyguardClockView) findViewById(C1894R.C1898id.nothing_keyguard_clock_view);
        this.mNothingClockViewLarge = (NTKeyguardClockViewLarge) findViewById(C1894R.C1898id.nothing_keyguard_clock_view_large);
        onDensityOrFontScaleChanged();
    }

    public void setStyle(Paint.Style style) {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setStyle(style);
        }
    }

    public void setTextColor(int i) {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setTextColor(i);
        }
    }

    private void updateClockViews(boolean z, boolean z2) {
        FrameLayout frameLayout;
        int i;
        float f;
        FrameLayout frameLayout2;
        AnimatorSet animatorSet = this.mClockInAnim;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        AnimatorSet animatorSet2 = this.mClockOutAnim;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
        }
        ObjectAnimator objectAnimator = this.mStatusAreaAnim;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        this.mClockInAnim = null;
        this.mClockOutAnim = null;
        this.mStatusAreaAnim = null;
        if (z) {
            frameLayout = this.mClockFrame;
            frameLayout2 = this.mLargeClockFrame;
            i = -1;
            if (indexOfChild(frameLayout2) == -1) {
                addView(frameLayout2);
            }
            f = (float) ((this.mClockFrame.getTop() - this.mStatusArea.getTop()) + this.mSmartspaceTopOffset);
            ((RelativeLayout.LayoutParams) this.mLargeClockFrame.getLayoutParams()).setMargins(0, this.mKeyguardClockViewLargeMarginTop + this.mSmartspaceTopOffset + this.mStatusArea.getHeight(), 0, 0);
        } else {
            frameLayout2 = this.mClockFrame;
            frameLayout = this.mLargeClockFrame;
            removeView(frameLayout);
            f = 0.0f;
            i = 1;
        }
        if (!z2) {
            frameLayout.setAlpha(0.0f);
            frameLayout2.setAlpha(1.0f);
            frameLayout2.setVisibility(0);
            this.mStatusArea.setTranslationY(f);
            return;
        }
        AnimatorSet animatorSet3 = new AnimatorSet();
        this.mClockOutAnim = animatorSet3;
        animatorSet3.setDuration(150);
        this.mClockOutAnim.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
        this.mClockOutAnim.playTogether(new Animator[]{ObjectAnimator.ofFloat(frameLayout, View.ALPHA, new float[]{0.0f}), ObjectAnimator.ofFloat(frameLayout, View.TRANSLATION_Y, new float[]{0.0f, (float) ((-this.mClockSwitchYAmount) * i)})});
        this.mClockOutAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                KeyguardClockSwitch.this.mClockOutAnim = null;
            }
        });
        frameLayout2.setAlpha(0.0f);
        frameLayout2.setVisibility(0);
        AnimatorSet animatorSet4 = new AnimatorSet();
        this.mClockInAnim = animatorSet4;
        animatorSet4.setDuration(200);
        this.mClockInAnim.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
        this.mClockInAnim.playTogether(new Animator[]{ObjectAnimator.ofFloat(frameLayout2, View.ALPHA, new float[]{1.0f}), ObjectAnimator.ofFloat(frameLayout2, View.TRANSLATION_Y, new float[]{(float) (i * this.mClockSwitchYAmount), 0.0f})});
        this.mClockInAnim.setStartDelay(75);
        this.mClockInAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                KeyguardClockSwitch.this.mClockInAnim = null;
            }
        });
        this.mClockInAnim.start();
        this.mClockOutAnim.start();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mStatusArea, View.TRANSLATION_Y, new float[]{f});
        this.mStatusAreaAnim = ofFloat;
        ofFloat.setDuration(350);
        this.mStatusAreaAnim.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        this.mStatusAreaAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ObjectAnimator unused = KeyguardClockSwitch.this.mStatusAreaAnim = null;
            }
        });
        this.mStatusAreaAnim.start();
    }

    public void setDarkAmount(float f) {
        this.mDarkAmount = f;
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setDarkAmount(f);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean switchToClock(int i, boolean z) {
        Integer num = this.mDisplayedClockSize;
        boolean z2 = false;
        if (num != null && i == num.intValue()) {
            return false;
        }
        if (this.mChildrenAreLaidOut) {
            if (i == 0) {
                z2 = true;
            }
            updateClockViews(z2, z);
        }
        this.mDisplayedClockSize = Integer.valueOf(i);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mDisplayedClockSize != null && !this.mChildrenAreLaidOut) {
            post(new KeyguardClockSwitch$$ExternalSyntheticLambda0(this));
        }
        this.mChildrenAreLaidOut = true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onLayout$0$com-android-keyguard-KeyguardClockSwitch  reason: not valid java name */
    public /* synthetic */ void m2288lambda$onLayout$0$comandroidkeyguardKeyguardClockSwitch() {
        updateClockViews(this.mDisplayedClockSize.intValue() == 0, true);
    }

    public Paint getPaint() {
        return this.mClockView.getPaint();
    }

    public int getCurrentTextColor() {
        return this.mNothingClockView.getCurrtentTextColor();
    }

    public float getTextSize() {
        return this.mClockView.getTextSize();
    }

    public void refresh() {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.onTimeTick();
        }
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.onTimeZoneChanged(timeZone);
        }
    }

    public void onTimeFormatChanged(String str) {
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.onTimeFormatChanged(str);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateColors(ColorExtractor.GradientColors gradientColors) {
        this.mSupportsDarkText = gradientColors.supportsDarkText();
        int[] colorPalette = gradientColors.getColorPalette();
        this.mColorPalette = colorPalette;
        ClockPlugin clockPlugin = this.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setColorPalette(this.mSupportsDarkText, colorPalette);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardClockSwitch:");
        printWriter.println("  mClockPlugin: " + this.mClockPlugin);
        printWriter.println("  mClockFrame: " + this.mClockFrame);
        printWriter.println("  mLargeClockFrame: " + this.mLargeClockFrame);
        printWriter.println("  mStatusArea: " + this.mStatusArea);
        printWriter.println("  mDarkAmount: " + this.mDarkAmount);
        printWriter.println("  mSupportsDarkText: " + this.mSupportsDarkText);
        printWriter.println("  mColorPalette: " + Arrays.toString(this.mColorPalette));
        printWriter.println("  mDisplayedClockSize: " + this.mDisplayedClockSize);
    }
}
