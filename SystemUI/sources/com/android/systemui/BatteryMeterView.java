package com.android.systemui;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.graph.ThemedBatteryDrawable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.SysuiLifecycle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class BatteryMeterView extends LinearLayout implements BatteryController.BatteryStateChangeCallbackExt, TunerService.Tunable, DarkIconDispatcher.DarkReceiver, ConfigurationController.ConfigurationListener {
    private BatteryController mBatteryController;
    private final ImageView mBatteryIconView;
    private TextView mBatteryPercentView;
    private final BatteryController.BatteryStateExt mBatteryStateExt;
    private boolean mBatteryStateUnknown;
    private boolean mCharging;
    private final ThemedBatteryDrawable mDrawable;
    private DualToneHandler mDualToneHandler;
    private boolean mIgnoreTunerUpdates;
    private boolean mIsSubscribedForTunerUpdates;
    private int mLevel;
    private int mNonAdaptedBackgroundColor;
    private int mNonAdaptedForegroundColor;
    private int mNonAdaptedSingleToneColor;
    private final int mPercentageStyleId;
    private SettingObserver mSettingObserver;
    private boolean mShowPercentAvailable;
    private int mShowPercentMode;
    private final String mSlotBattery;
    private int mTextColor;
    private Drawable mUnknownStateDrawable;
    private int mUser;
    private final CurrentUserTracker mUserTracker;

    private void scaleBatteryMeterViews() {
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BatteryMeterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowPercentMode = 0;
        this.mBatteryStateExt = new BatteryController.BatteryStateExt();
        setOrientation(0);
        setGravity(8388627);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.BatteryMeterView, i, 0);
        int color = obtainStyledAttributes.getColor(R$styleable.BatteryMeterView_frameColor, context.getColor(R$color.meter_background_color));
        this.mPercentageStyleId = obtainStyledAttributes.getResourceId(R$styleable.BatteryMeterView_textAppearance, 0);
        ThemedBatteryDrawable themedBatteryDrawable = new ThemedBatteryDrawable(context, color);
        this.mDrawable = themedBatteryDrawable;
        obtainStyledAttributes.recycle();
        this.mSettingObserver = new SettingObserver(new Handler(context.getMainLooper()));
        this.mShowPercentAvailable = context.getResources().getBoolean(17891381);
        setupLayoutTransition();
        this.mSlotBattery = context.getString(17041438);
        ImageView imageView = new ImageView(context);
        this.mBatteryIconView = imageView;
        imageView.setImageDrawable(themedBatteryDrawable);
        addBatteryImageView(context);
        updateShowPercent();
        this.mDualToneHandler = new DualToneHandler(context);
        onDarkChanged(new Rect(), 0.0f, -1);
        this.mUserTracker = new CurrentUserTracker((BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class)) { // from class: com.android.systemui.BatteryMeterView.1
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i2) {
                BatteryMeterView.this.mUser = i2;
                BatteryMeterView.this.getContext().getContentResolver().unregisterContentObserver(BatteryMeterView.this.mSettingObserver);
                BatteryMeterView.this.getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, BatteryMeterView.this.mSettingObserver, i2);
                BatteryMeterView.this.updateShowPercent();
            }
        };
        setClipChildren(false);
        setClipToPadding(false);
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).observe(SysuiLifecycle.viewAttachLifecycle(this), (LifecycleOwner) this);
    }

    private void setupLayoutTransition() {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(200L);
        layoutTransition.setAnimator(2, ObjectAnimator.ofFloat((Object) null, "alpha", 0.0f, 1.0f));
        layoutTransition.setInterpolator(2, Interpolators.ALPHA_IN);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, "alpha", 1.0f, 0.0f);
        layoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
        layoutTransition.setAnimator(3, ofFloat);
        setLayoutTransition(layoutTransition);
    }

    public void setForceShowPercent(boolean z) {
        setPercentShowMode(z ? 1 : 0);
    }

    public void setPercentShowMode(int i) {
        if (i == this.mShowPercentMode) {
            return;
        }
        this.mShowPercentMode = i;
        updateShowPercent();
    }

    public void setIgnoreTunerUpdates(boolean z) {
        this.mIgnoreTunerUpdates = z;
        updateTunerSubscription();
    }

    private void updateTunerSubscription() {
        if (this.mIgnoreTunerUpdates) {
            unsubscribeFromTunerUpdates();
        } else {
            subscribeForTunerUpdates();
        }
    }

    private void subscribeForTunerUpdates() {
        if (this.mIsSubscribedForTunerUpdates || this.mIgnoreTunerUpdates) {
            return;
        }
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "icon_blacklist");
        this.mIsSubscribedForTunerUpdates = true;
    }

    private void unsubscribeFromTunerUpdates() {
        if (!this.mIsSubscribedForTunerUpdates) {
            return;
        }
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        this.mIsSubscribedForTunerUpdates = false;
    }

    public void setColorsFromContext(Context context) {
        if (context == null) {
            return;
        }
        this.mDualToneHandler.setColorsFromContext(context);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("icon_blacklist".equals(str)) {
            setVisibility(StatusBarIconController.getIconHideList(getContext(), str2).contains(this.mSlotBattery) ? 8 : 0);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        BatteryController batteryController = (BatteryController) Dependency.get(BatteryController.class);
        this.mBatteryController = batteryController;
        batteryController.addCallback(this);
        this.mUser = ActivityManager.getCurrentUser();
        getContext().getContentResolver().registerContentObserver(Settings.System.getUriFor("status_bar_show_battery_percent"), false, this.mSettingObserver, this.mUser);
        getContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor("battery_estimates_last_update_time"), false, this.mSettingObserver);
        updateShowPercent();
        subscribeForTunerUpdates();
        this.mUserTracker.startTracking();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mUserTracker.stopTracking();
        this.mBatteryController.removeCallback(this);
        getContext().getContentResolver().unregisterContentObserver(this.mSettingObserver);
        unsubscribeFromTunerUpdates();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallbackExt
    public void onBatteryLevelChangedExt(int i, boolean z, boolean z2, BatteryController.BatteryStateExt batteryStateExt) {
        updateBatteryStateExt(batteryStateExt);
        refreshByBatteryStateExt();
        this.mDrawable.setCharging(z);
        this.mDrawable.setBatteryLevel(i);
        this.mCharging = z;
        this.mLevel = i;
        updatePercentText();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onPowerSaveChanged(boolean z) {
        this.mDrawable.setPowerSaveEnabled(z);
    }

    private TextView loadPercentView() {
        return (TextView) LayoutInflater.from(getContext()).inflate(R$layout.battery_percentage_view, (ViewGroup) null);
    }

    public void updatePercentView() {
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            removeView(textView);
            this.mBatteryPercentView = null;
        }
        updateShowPercent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePercentText() {
        if (this.mBatteryStateUnknown) {
            setContentDescription(getContext().getString(R$string.accessibility_battery_unknown));
            return;
        }
        BatteryController batteryController = this.mBatteryController;
        if (batteryController == null) {
            return;
        }
        if (this.mBatteryPercentView != null) {
            if (this.mShowPercentMode == 3 && !this.mCharging) {
                batteryController.getEstimatedTimeRemainingString(new BatteryController.EstimateFetchCompletion() { // from class: com.android.systemui.BatteryMeterView$$ExternalSyntheticLambda0
                    @Override // com.android.systemui.statusbar.policy.BatteryController.EstimateFetchCompletion
                    public final void onBatteryRemainingEstimateRetrieved(String str) {
                        BatteryMeterView.this.lambda$updatePercentText$0(str);
                    }
                });
                return;
            } else {
                setPercentTextAtCurrentLevel();
                return;
            }
        }
        setContentDescription(getContext().getString(this.mCharging ? R$string.accessibility_battery_level_charging : R$string.accessibility_battery_level, Integer.valueOf(this.mLevel)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePercentText$0(String str) {
        TextView textView = this.mBatteryPercentView;
        if (textView == null) {
            return;
        }
        if (str != null && this.mShowPercentMode == 3) {
            textView.setText(str);
            setContentDescription(getContext().getString(R$string.accessibility_battery_level_with_estimate, Integer.valueOf(this.mLevel), str));
            return;
        }
        setPercentTextAtCurrentLevel();
    }

    private void setPercentTextAtCurrentLevel() {
        TextView textView = this.mBatteryPercentView;
        if (textView == null) {
            return;
        }
        textView.setText(NumberFormat.getPercentInstance().format(this.mLevel / 100.0f));
        setContentDescription(getContext().getString(this.mCharging ? R$string.accessibility_battery_level_charging : R$string.accessibility_battery_level, Integer.valueOf(this.mLevel)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShowPercent() {
        int i;
        boolean z = false;
        boolean z2 = this.mBatteryPercentView != null;
        if (((this.mShowPercentAvailable && (((Integer) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.BatteryMeterView$$ExternalSyntheticLambda1
            @Override // java.util.function.Supplier
            public final Object get() {
                Integer lambda$updateShowPercent$1;
                lambda$updateShowPercent$1 = BatteryMeterView.this.lambda$updateShowPercent$1();
                return lambda$updateShowPercent$1;
            }
        })).intValue() != 0) && this.mShowPercentMode != 2) || (i = this.mShowPercentMode) == 1 || i == 3) && !this.mBatteryStateUnknown) {
            z = true;
        }
        if (!z) {
            if (!z2) {
                return;
            }
            removeView(this.mBatteryPercentView);
            this.mBatteryPercentView = null;
        } else if (z2) {
        } else {
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$updateShowPercent$1() {
        return Integer.valueOf(Settings.System.getIntForUser(getContext().getContentResolver(), "status_bar_show_battery_percent", 0, this.mUser));
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        scaleBatteryMeterViews();
    }

    private Drawable getUnknownStateDrawable() {
        if (this.mUnknownStateDrawable == null) {
            Drawable drawable = ((LinearLayout) this).mContext.getDrawable(R$drawable.ic_battery_unknown);
            this.mUnknownStateDrawable = drawable;
            drawable.setTint(this.mTextColor);
        }
        return this.mUnknownStateDrawable;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryUnknownStateChanged(boolean z) {
        if (this.mBatteryStateUnknown == z) {
            return;
        }
        this.mBatteryStateUnknown = z;
        if (z) {
            this.mBatteryIconView.setImageDrawable(getUnknownStateDrawable());
        } else {
            refreshByBatteryStateExt();
        }
        updateShowPercent();
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public void onDarkChanged(Rect rect, float f, int i) {
        if (!DarkIconDispatcher.isInArea(rect, this)) {
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

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        CharSequence charSequence = null;
        if (this.mDrawable == null) {
            str = null;
        } else {
            str = this.mDrawable.getPowerSaveEnabled() + "";
        }
        TextView textView = this.mBatteryPercentView;
        if (textView != null) {
            charSequence = textView.getText();
        }
        printWriter.println("  BatteryMeterView:");
        printWriter.println("    mDrawable.getPowerSave: " + str);
        printWriter.println("    mBatteryPercentView.getText(): " + ((Object) charSequence));
        printWriter.println("    mTextColor: #" + Integer.toHexString(this.mTextColor));
        printWriter.println("    mBatteryStateUnknown: " + this.mBatteryStateUnknown);
        printWriter.println("    mLevel: " + this.mLevel);
        printWriter.println("    mMode: " + this.mShowPercentMode);
        printWriter.println("    mBatterStateExt: " + this.mBatteryStateExt);
    }

    /* loaded from: classes.dex */
    private final class SettingObserver extends ContentObserver {
        public SettingObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            BatteryMeterView.this.updateShowPercent();
            if (TextUtils.equals(uri.getLastPathSegment(), "battery_estimates_last_update_time")) {
                BatteryMeterView.this.updatePercentText();
            }
        }
    }

    private void addBatteryImageView(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R$dimen.status_bar_battery_icon_width), getResources().getDimensionPixelSize(R$dimen.status_bar_battery_icon_height));
        layoutParams.gravity = 17;
        frameLayout.addView(this.mBatteryIconView, layoutParams);
        addView(frameLayout, new ViewGroup.MarginLayoutParams(getResources().getDimensionPixelSize(R$dimen.battery_icon_container_size), -1));
    }

    private void refreshByBatteryStateExt() {
        if (this.mBatteryStateUnknown) {
            Log.i("BatteryMeterView", "refreshByBatteryStateExt: batteryStateUnknown");
            return;
        }
        BatteryController.BatteryStateExt batteryStateExt = this.mBatteryStateExt;
        if (batteryStateExt.status == 4) {
            if (batteryStateExt.voltage > 4480) {
                this.mBatteryIconView.setImageDrawable(((LinearLayout) this).mContext.getDrawable(R$drawable.ic_battery_alert));
                Log.i("BatteryMeterView", "refreshByBatteryStateExt: volt > max");
                return;
            }
            int i = batteryStateExt.temperature;
            if (i >= 530) {
                this.mBatteryIconView.setImageDrawable(((LinearLayout) this).mContext.getDrawable(R$drawable.ic_battery_pause));
                Log.i("BatteryMeterView", "refreshByBatteryStateExt: temp >= max");
                return;
            } else if (i <= 0) {
                this.mBatteryIconView.setImageDrawable(((LinearLayout) this).mContext.getDrawable(R$drawable.ic_battery_pause));
                Log.i("BatteryMeterView", "refreshByBatteryStateExt: temp <= min");
                return;
            }
        }
        this.mBatteryIconView.setImageDrawable(this.mDrawable);
    }

    private void updateBatteryStateExt(BatteryController.BatteryStateExt batteryStateExt) {
        BatteryController.BatteryStateExt batteryStateExt2 = this.mBatteryStateExt;
        boolean z = true;
        boolean z2 = batteryStateExt2.status != batteryStateExt.status;
        boolean z3 = batteryStateExt2.temperature != batteryStateExt.temperature;
        if (batteryStateExt2.voltage == batteryStateExt.voltage) {
            z = false;
        }
        if (z2 || z3 || z) {
            Log.i("BatteryMeterView", "batteryStateExt changed from: " + batteryStateExt + " to " + this.mBatteryStateExt);
        }
        this.mBatteryStateExt.copy(batteryStateExt);
    }
}
