package com.android.systemui.statusbar.charging;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: WiredChargingRippleController.kt */
/* loaded from: classes.dex */
public final class WiredChargingRippleController {
    @NotNull
    private final Context context;
    private int debounceLevel;
    @Nullable
    private Long lastTriggerTime;
    private float normalizedPortPosX;
    private float normalizedPortPosY;
    @Nullable
    private Boolean pluggedIn;
    private final boolean rippleEnabled;
    @NotNull
    private ChargingRippleView rippleView;
    @NotNull
    private final SystemClock systemClock;
    @NotNull
    private final UiEventLogger uiEventLogger;
    @NotNull
    private final WindowManager.LayoutParams windowLayoutParams;
    @NotNull
    private final WindowManager windowManager;

    @VisibleForTesting
    public static /* synthetic */ void getRippleView$annotations() {
    }

    public WiredChargingRippleController(@NotNull CommandRegistry commandRegistry, @NotNull final BatteryController batteryController, @NotNull ConfigurationController configurationController, @NotNull FeatureFlags featureFlags, @NotNull Context context, @NotNull WindowManager windowManager, @NotNull SystemClock systemClock, @NotNull UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(batteryController, "batteryController");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(windowManager, "windowManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.context = context;
        this.windowManager = windowManager;
        this.systemClock = systemClock;
        this.uiEventLogger = uiEventLogger;
        this.rippleEnabled = featureFlags.isChargingRippleEnabled() && !SystemProperties.getBoolean("persist.debug.suppress-charging-ripple", false);
        this.normalizedPortPosX = context.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_x);
        this.normalizedPortPosY = context.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_y);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.format = -3;
        layoutParams.type = 2006;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("Wired Charging Animation");
        layoutParams.flags = 24;
        layoutParams.setTrustedOverlay();
        Unit unit = Unit.INSTANCE;
        this.windowLayoutParams = layoutParams;
        this.rippleView = new ChargingRippleView(context, null);
        this.pluggedIn = Boolean.valueOf(batteryController.isPluggedIn());
        batteryController.addCallback(new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$batteryStateChangeCallback$1
            @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
            public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
                boolean z3;
                Boolean bool;
                z3 = WiredChargingRippleController.this.rippleEnabled;
                if (!z3 || batteryController.isPluggedInWireless()) {
                    return;
                }
                bool = WiredChargingRippleController.this.pluggedIn;
                WiredChargingRippleController.this.pluggedIn = Boolean.valueOf(z);
                if ((bool != null && bool.booleanValue()) || !z) {
                    return;
                }
                WiredChargingRippleController.this.startRippleWithDebounce$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$configurationChangedListener$1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                WiredChargingRippleController.this.updateRippleColor();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onThemeChanged() {
                WiredChargingRippleController.this.updateRippleColor();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                WiredChargingRippleController.this.updateRippleColor();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(@Nullable Configuration configuration) {
                Context context2;
                Context context3;
                WiredChargingRippleController wiredChargingRippleController = WiredChargingRippleController.this;
                context2 = wiredChargingRippleController.context;
                wiredChargingRippleController.normalizedPortPosX = context2.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_x);
                WiredChargingRippleController wiredChargingRippleController2 = WiredChargingRippleController.this;
                context3 = wiredChargingRippleController2.context;
                wiredChargingRippleController2.normalizedPortPosY = context3.getResources().getFloat(R$dimen.physical_charger_port_location_normalized_y);
            }
        });
        commandRegistry.registerCommand("charging-ripple", new AnonymousClass1());
        updateRippleColor();
    }

    @NotNull
    public final ChargingRippleView getRippleView() {
        return this.rippleView;
    }

    /* compiled from: WiredChargingRippleController.kt */
    /* renamed from: com.android.systemui.statusbar.charging.WiredChargingRippleController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static final class AnonymousClass1 extends Lambda implements Function0<Command> {
        AnonymousClass1() {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @NotNull
        /* renamed from: invoke */
        public final Command mo1951invoke() {
            return new ChargingRippleCommand(WiredChargingRippleController.this);
        }
    }

    public final void startRippleWithDebounce$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        Long l = this.lastTriggerTime;
        if (l != null) {
            Intrinsics.checkNotNull(l);
            if (elapsedRealtime - l.longValue() <= 2000 * Math.pow(2.0d, this.debounceLevel)) {
                this.debounceLevel = Math.min(3, this.debounceLevel + 1);
                this.lastTriggerTime = Long.valueOf(elapsedRealtime);
            }
        }
        startRipple();
        this.debounceLevel = 0;
        this.lastTriggerTime = Long.valueOf(elapsedRealtime);
    }

    public final void startRipple() {
        if (!this.rippleEnabled || this.rippleView.getRippleInProgress() || this.rippleView.getParent() != null) {
            return;
        }
        this.windowLayoutParams.packageName = this.context.getOpPackageName();
        this.rippleView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(@Nullable View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(@Nullable View view) {
                WiredChargingRippleController.this.layoutRipple();
                ChargingRippleView rippleView = WiredChargingRippleController.this.getRippleView();
                final WiredChargingRippleController wiredChargingRippleController = WiredChargingRippleController.this;
                rippleView.startRipple(new Runnable() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        WindowManager windowManager;
                        windowManager = WiredChargingRippleController.this.windowManager;
                        windowManager.removeView(WiredChargingRippleController.this.getRippleView());
                    }
                });
                WiredChargingRippleController.this.getRippleView().removeOnAttachStateChangeListener(this);
            }
        });
        this.windowManager.addView(this.rippleView, this.windowLayoutParams);
        this.uiEventLogger.log(WiredChargingRippleEvent.CHARGING_RIPPLE_PLAYED);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void layoutRipple() {
        PointF pointF;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.context.getDisplay().getRealMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        this.rippleView.setRadius(Integer.max(i, i2));
        ChargingRippleView chargingRippleView = this.rippleView;
        int rotation = RotationUtils.getRotation(this.context);
        if (rotation == 1) {
            pointF = new PointF(i * this.normalizedPortPosY, i2 * (1 - this.normalizedPortPosX));
        } else if (rotation == 2) {
            float f = 1;
            pointF = new PointF(i * (f - this.normalizedPortPosX), i2 * (f - this.normalizedPortPosY));
        } else if (rotation == 3) {
            pointF = new PointF(i * (1 - this.normalizedPortPosY), i2 * this.normalizedPortPosX);
        } else {
            pointF = new PointF(i * this.normalizedPortPosX, i2 * this.normalizedPortPosY);
        }
        chargingRippleView.setOrigin(pointF);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateRippleColor() {
        this.rippleView.setColor(Utils.getColorAttr(this.context, 16843829).getDefaultColor());
    }

    /* compiled from: WiredChargingRippleController.kt */
    /* loaded from: classes.dex */
    public final class ChargingRippleCommand implements Command {
        final /* synthetic */ WiredChargingRippleController this$0;

        public ChargingRippleCommand(WiredChargingRippleController this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public void execute(@NotNull PrintWriter pw, @NotNull List<String> args) {
            Intrinsics.checkNotNullParameter(pw, "pw");
            Intrinsics.checkNotNullParameter(args, "args");
            this.this$0.startRipple();
        }
    }

    /* compiled from: WiredChargingRippleController.kt */
    /* loaded from: classes.dex */
    public enum WiredChargingRippleEvent implements UiEventLogger.UiEventEnum {
        CHARGING_RIPPLE_PLAYED(829);
        
        private final int _id;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static WiredChargingRippleEvent[] valuesCustom() {
            WiredChargingRippleEvent[] valuesCustom = values();
            WiredChargingRippleEvent[] wiredChargingRippleEventArr = new WiredChargingRippleEvent[valuesCustom.length];
            System.arraycopy(valuesCustom, 0, wiredChargingRippleEventArr, 0, valuesCustom.length);
            return wiredChargingRippleEventArr;
        }

        WiredChargingRippleEvent(int i) {
            this._id = i;
        }

        public int getId() {
            return this._id;
        }
    }
}
