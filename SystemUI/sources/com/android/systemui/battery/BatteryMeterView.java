package com.android.systemui.battery;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.Key;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.DualToneHandler;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.policy.BatteryController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.battery.BatteryMeterViewEx;
import com.nothing.systemui.statusbar.policy.BatteryControllerImplEx;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;

public class BatteryMeterView extends LinearLayout implements DarkIconDispatcher.DarkReceiver {
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_ESTIMATE = 3;
    public static final int MODE_OFF = 2;
    public static final int MODE_ON = 1;
    private BatteryEstimateFetcher mBatteryEstimateFetcher;
    private final ImageView mBatteryIconView;
    private TextView mBatteryPercentView;
    private boolean mBatteryStateUnknown;
    private boolean mCharging;
    private final ThemedBatteryDrawable mDrawable;
    private DualToneHandler mDualToneHandler;
    private int mLevel;
    private int mNonAdaptedBackgroundColor;
    private int mNonAdaptedForegroundColor;
    private int mNonAdaptedSingleToneColor;
    private final int mPercentageStyleId;
    private boolean mShowPercentAvailable;
    private int mShowPercentMode;
    private int mTextColor;
    private Drawable mUnknownStateDrawable;

    public interface BatteryEstimateFetcher {
        void fetchBatteryTimeRemainingEstimate(BatteryController.EstimateFetchCompletion estimateFetchCompletion);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface BatteryPercentMode {
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public void scaleBatteryMeterViews() {
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowPercentMode = 0;
        setOrientation(0);
        setGravity(8388627);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1893R.styleable.BatteryMeterView, i, 0);
        int color = obtainStyledAttributes.getColor(0, context.getColor(C1893R.C1894color.meter_background_color));
        this.mPercentageStyleId = obtainStyledAttributes.getResourceId(1, 0);
        ThemedBatteryDrawable themedBatteryDrawable = new ThemedBatteryDrawable(context, color);
        this.mDrawable = themedBatteryDrawable;
        obtainStyledAttributes.recycle();
        this.mShowPercentAvailable = context.getResources().getBoolean(17891384);
        setupLayoutTransition();
        ImageView imageView = new ImageView(context);
        this.mBatteryIconView = imageView;
        imageView.setImageDrawable(themedBatteryDrawable);
        BatteryMeterViewEx.addBatteryImageView(context, this, imageView);
        updateShowPercent();
        this.mDualToneHandler = new DualToneHandler(context);
        onDarkChanged(new ArrayList(), 0.0f, -1);
        setClipChildren(false);
        setClipToPadding(false);
    }

    private void setupLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(200);
        layoutTransition.setAnimator(2, ObjectAnimator.ofFloat((Object) null, Key.ALPHA, new float[]{0.0f, 1.0f}));
        layoutTransition.setInterpolator(2, Interpolators.ALPHA_IN);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, Key.ALPHA, new float[]{1.0f, 0.0f});
        layoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
        layoutTransition.setAnimator(3, ofFloat);
        layoutTransition.setAnimator(0, (Animator) null);
        layoutTransition.setAnimator(1, (Animator) null);
        layoutTransition.setAnimator(4, (Animator) null);
        setLayoutTransition(layoutTransition);
    }

    public void setForceShowPercent(boolean z) {
        setPercentShowMode(z ? 1 : 0);
    }

    public void setPercentShowMode(int i) {
        if (i != this.mShowPercentMode) {
            this.mShowPercentMode = i;
            updateShowPercent();
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updatePercentView();
    }

    public void setColorsFromContext(Context context) {
        if (context != null) {
            this.mDualToneHandler.setColorsFromContext(context);
        }
    }

    /* access modifiers changed from: package-private */
    public void onBatteryLevelChanged(int i, boolean z) {
        BatteryMeterViewEx.updateBatteryStateEx(((BatteryControllerImplEx) NTDependencyEx.get(BatteryControllerImplEx.class)).getBatteryStateEx());
        BatteryMeterViewEx.refreshByBatteryStateEx(this.mContext, this.mBatteryIconView, this.mDrawable, this.mBatteryStateUnknown);
        this.mDrawable.setCharging(z);
        this.mDrawable.setBatteryLevel(i);
        this.mCharging = z;
        this.mLevel = i;
        updatePercentText();
    }

    /* access modifiers changed from: package-private */
    public void onPowerSaveChanged(boolean z) {
        this.mDrawable.setPowerSaveEnabled(z);
    }

    private TextView loadPercentView() {
        return (TextView) LayoutInflater.from(getContext()).inflate(C1893R.layout.battery_percentage_view, (ViewGroup) null);
    }

    public void updatePercentView() {
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            removeView(textView);
            this.mBatteryPercentView = null;
        }
        updateShowPercent();
    }

    /* access modifiers changed from: package-private */
    public void setBatteryEstimateFetcher(BatteryEstimateFetcher batteryEstimateFetcher) {
        this.mBatteryEstimateFetcher = batteryEstimateFetcher;
    }

    /* access modifiers changed from: package-private */
    public void updatePercentText() {
        if (this.mBatteryStateUnknown) {
            setContentDescription(getContext().getString(C1893R.string.accessibility_battery_unknown));
            return;
        }
        BatteryEstimateFetcher batteryEstimateFetcher = this.mBatteryEstimateFetcher;
        if (batteryEstimateFetcher != null) {
            if (this.mBatteryPercentView == null) {
                setContentDescription(getContext().getString(this.mCharging ? C1893R.string.accessibility_battery_level_charging : C1893R.string.accessibility_battery_level, new Object[]{Integer.valueOf(this.mLevel)}));
            } else if (this.mShowPercentMode != 3 || this.mCharging) {
                setPercentTextAtCurrentLevel();
            } else {
                batteryEstimateFetcher.fetchBatteryTimeRemainingEstimate(new BatteryMeterView$$ExternalSyntheticLambda0(this));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updatePercentText$0$com-android-systemui-battery-BatteryMeterView */
    public /* synthetic */ void mo30446x299cb55(String str) {
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            if (str == null || this.mShowPercentMode != 3) {
                setPercentTextAtCurrentLevel();
                return;
            }
            textView.setText(str);
            setContentDescription(getContext().getString(C1893R.string.accessibility_battery_level_with_estimate, new Object[]{Integer.valueOf(this.mLevel), str}));
        }
    }

    private void setPercentTextAtCurrentLevel() {
        if (this.mBatteryPercentView != null) {
            String format = NumberFormat.getPercentInstance().format((double) (((float) this.mLevel) / 100.0f));
            if (!TextUtils.equals(this.mBatteryPercentView.getText(), format)) {
                this.mBatteryPercentView.setText(format);
            }
            setContentDescription(getContext().getString(this.mCharging ? C1893R.string.accessibility_battery_level_charging : C1893R.string.accessibility_battery_level, new Object[]{Integer.valueOf(this.mLevel)}));
        }
    }

    /* access modifiers changed from: package-private */
    public void updateShowPercent() {
        int i;
        boolean z = false;
        boolean z2 = this.mBatteryPercentView != null;
        if (((this.mShowPercentAvailable && (((Integer) DejankUtils.whitelistIpcs(new BatteryMeterView$$ExternalSyntheticLambda1(this))).intValue() != 0) && this.mShowPercentMode != 2) || (i = this.mShowPercentMode) == 1 || i == 3) && !this.mBatteryStateUnknown) {
            z = true;
        }
        if (z) {
            if (!z2) {
                TextView loadPercentView = loadPercentView();
                this.mBatteryPercentView = loadPercentView;
                int i2 = this.mPercentageStyleId;
                if (i2 != 0) {
                    loadPercentView.setTextAppearance(i2);
                }
                int i3 = this.mTextColor;
                if (i3 != 0) {
                    this.mBatteryPercentView.setTextColor(i3);
                }
                updatePercentText();
                addView(this.mBatteryPercentView, new ViewGroup.LayoutParams(-2, -1));
            }
        } else if (z2) {
            removeView(this.mBatteryPercentView);
            this.mBatteryPercentView = null;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateShowPercent$1$com-android-systemui-battery-BatteryMeterView */
    public /* synthetic */ Integer mo30447xce37b50c() {
        return Integer.valueOf(Settings.System.getIntForUser(getContext().getContentResolver(), "status_bar_show_battery_percent", 0, -2));
    }

    private Drawable getUnknownStateDrawable() {
        if (this.mUnknownStateDrawable == null) {
            Drawable drawable = this.mContext.getDrawable(C1893R.C1895drawable.ic_battery_unknown);
            this.mUnknownStateDrawable = drawable;
            drawable.setTint(this.mTextColor);
        }
        return this.mUnknownStateDrawable;
    }

    /* access modifiers changed from: package-private */
    public void onBatteryUnknownStateChanged(boolean z) {
        if (this.mBatteryStateUnknown != z) {
            this.mBatteryStateUnknown = z;
            if (z) {
                this.mBatteryIconView.setImageDrawable(getUnknownStateDrawable());
            } else {
                this.mBatteryIconView.setImageDrawable(this.mDrawable);
            }
            updateShowPercent();
        }
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        if (!DarkIconDispatcher.isInAreas(arrayList, this)) {
            f = 0.0f;
        }
        this.mNonAdaptedSingleToneColor = this.mDualToneHandler.getSingleColor(f);
        this.mNonAdaptedForegroundColor = this.mDualToneHandler.getFillColor(f);
        int backgroundColor = this.mDualToneHandler.getBackgroundColor(f);
        this.mNonAdaptedBackgroundColor = backgroundColor;
        updateColors(this.mNonAdaptedForegroundColor, backgroundColor, this.mNonAdaptedSingleToneColor);
    }

    public void updateColors(int i, int i2, int i3) {
        this.mDrawable.setColors(i, i2, i3);
        this.mTextColor = i3;
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            textView.setTextColor(i3);
        }
        Drawable drawable = this.mUnknownStateDrawable;
        if (drawable != null) {
            drawable.setTint(i3);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        CharSequence charSequence = null;
        String str = this.mDrawable == null ? null : this.mDrawable.getPowerSaveEnabled() + "";
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            charSequence = textView.getText();
        }
        printWriter.println("  BatteryMeterView:");
        printWriter.println("    mDrawable.getPowerSave: " + str);
        printWriter.println("    mBatteryPercentView.getText(): " + charSequence);
        printWriter.println("    mTextColor: #" + Integer.toHexString(this.mTextColor));
        printWriter.println("    mBatteryStateUnknown: " + this.mBatteryStateUnknown);
        printWriter.println("    mLevel: " + this.mLevel);
        printWriter.println("    mMode: " + this.mShowPercentMode);
        printWriter.println("    BatteryState: " + BatteryMeterViewEx.getBatteryStateEx());
    }

    /* access modifiers changed from: package-private */
    public CharSequence getBatteryPercentViewText() {
        return this.mBatteryPercentView.getText();
    }
}
