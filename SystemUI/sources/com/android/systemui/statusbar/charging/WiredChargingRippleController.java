package com.android.systemui.statusbar.charging;

import android.content.Context;
import android.graphics.PointF;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.flags.ResourceBooleanFlag;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.leak.RotationUtils;
import com.android.systemui.util.time.SystemClock;
import java.p026io.PrintWriter;
import java.sql.Types;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001:\u000201BG\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\b\u0010)\u001a\u00020*H\u0002J\u0006\u0010+\u001a\u00020*J\u0006\u0010,\u001a\u00020*J\r\u0010-\u001a\u00020*H\u0000¢\u0006\u0002\b.J\b\u0010/\u001a\u00020*H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0004\n\u0002\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0019X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0019X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u000e¢\u0006\u0004\n\u0002\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R$\u0010\u001f\u001a\u00020 8\u0006@\u0006X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b!\u0010\"\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000¨\u00062"}, mo64987d2 = {"Lcom/android/systemui/statusbar/charging/WiredChargingRippleController;", "", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "batteryController", "Lcom/android/systemui/statusbar/policy/BatteryController;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "featureFlags", "Lcom/android/systemui/flags/FeatureFlags;", "context", "Landroid/content/Context;", "windowManager", "Landroid/view/WindowManager;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "(Lcom/android/systemui/statusbar/commandline/CommandRegistry;Lcom/android/systemui/statusbar/policy/BatteryController;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/flags/FeatureFlags;Landroid/content/Context;Landroid/view/WindowManager;Lcom/android/systemui/util/time/SystemClock;Lcom/android/internal/logging/UiEventLogger;)V", "debounceLevel", "", "lastTriggerTime", "", "Ljava/lang/Long;", "normalizedPortPosX", "", "normalizedPortPosY", "pluggedIn", "", "Ljava/lang/Boolean;", "rippleEnabled", "rippleView", "Lcom/android/systemui/statusbar/charging/ChargingRippleView;", "getRippleView$annotations", "()V", "getRippleView", "()Lcom/android/systemui/statusbar/charging/ChargingRippleView;", "setRippleView", "(Lcom/android/systemui/statusbar/charging/ChargingRippleView;)V", "windowLayoutParams", "Landroid/view/WindowManager$LayoutParams;", "layoutRipple", "", "registerCallbacks", "startRipple", "startRippleWithDebounce", "startRippleWithDebounce$SystemUI_nothingRelease", "updateRippleColor", "ChargingRippleCommand", "WiredChargingRippleEvent", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: WiredChargingRippleController.kt */
public final class WiredChargingRippleController {
    /* access modifiers changed from: private */
    public final BatteryController batteryController;
    private final ConfigurationController configurationController;
    /* access modifiers changed from: private */
    public final Context context;
    private int debounceLevel;
    private Long lastTriggerTime;
    /* access modifiers changed from: private */
    public float normalizedPortPosX;
    /* access modifiers changed from: private */
    public float normalizedPortPosY;
    /* access modifiers changed from: private */
    public Boolean pluggedIn;
    private final boolean rippleEnabled;
    private ChargingRippleView rippleView;
    private final SystemClock systemClock;
    private final UiEventLogger uiEventLogger;
    private final WindowManager.LayoutParams windowLayoutParams;
    /* access modifiers changed from: private */
    public final WindowManager windowManager;

    public static /* synthetic */ void getRippleView$annotations() {
    }

    @Inject
    public WiredChargingRippleController(CommandRegistry commandRegistry, BatteryController batteryController2, ConfigurationController configurationController2, FeatureFlags featureFlags, Context context2, WindowManager windowManager2, SystemClock systemClock2, UiEventLogger uiEventLogger2) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(batteryController2, "batteryController");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(windowManager2, "windowManager");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        this.batteryController = batteryController2;
        this.configurationController = configurationController2;
        this.context = context2;
        this.windowManager = windowManager2;
        this.systemClock = systemClock2;
        this.uiEventLogger = uiEventLogger2;
        ResourceBooleanFlag resourceBooleanFlag = Flags.CHARGING_RIPPLE;
        Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "CHARGING_RIPPLE");
        this.rippleEnabled = featureFlags.isEnabled(resourceBooleanFlag) && !SystemProperties.getBoolean("persist.debug.suppress-charging-ripple", false);
        this.normalizedPortPosX = context2.getResources().getFloat(C1893R.dimen.physical_charger_port_location_normalized_x);
        this.normalizedPortPosY = context2.getResources().getFloat(C1893R.dimen.physical_charger_port_location_normalized_y);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.format = -3;
        layoutParams.type = Types.REF;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("Wired Charging Animation");
        layoutParams.flags = 24;
        layoutParams.setTrustedOverlay();
        this.windowLayoutParams = layoutParams;
        this.rippleView = new ChargingRippleView(context2, (AttributeSet) null);
        this.pluggedIn = Boolean.valueOf(batteryController2.isPluggedIn());
        commandRegistry.registerCommand("charging-ripple", new Function0<Command>(this) {
            final /* synthetic */ WiredChargingRippleController this$0;

            {
                this.this$0 = r1;
            }

            public final Command invoke() {
                return new ChargingRippleCommand();
            }
        });
        updateRippleColor();
    }

    public final ChargingRippleView getRippleView() {
        return this.rippleView;
    }

    public final void setRippleView(ChargingRippleView chargingRippleView) {
        Intrinsics.checkNotNullParameter(chargingRippleView, "<set-?>");
        this.rippleView = chargingRippleView;
    }

    public final void registerCallbacks() {
        this.batteryController.addCallback(new C2604x82710e47(this));
        this.configurationController.addCallback(new C2605xe97593c0(this));
    }

    public final void startRippleWithDebounce$SystemUI_nothingRelease() {
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        Long l = this.lastTriggerTime;
        if (l != null) {
            Intrinsics.checkNotNull(l);
            if (((double) (elapsedRealtime - l.longValue())) <= ((double) 2000) * Math.pow(2.0d, (double) this.debounceLevel)) {
                this.debounceLevel = Math.min(3, this.debounceLevel + 1);
                this.lastTriggerTime = Long.valueOf(elapsedRealtime);
            }
        }
        startRipple();
        this.debounceLevel = 0;
        this.lastTriggerTime = Long.valueOf(elapsedRealtime);
    }

    public final void startRipple() {
        if (!this.rippleView.getRippleInProgress() && this.rippleView.getParent() == null) {
            this.windowLayoutParams.packageName = this.context.getOpPackageName();
            this.rippleView.addOnAttachStateChangeListener(new WiredChargingRippleController$startRipple$1(this));
            this.windowManager.addView(this.rippleView, this.windowLayoutParams);
            this.uiEventLogger.log(WiredChargingRippleEvent.CHARGING_RIPPLE_PLAYED);
        }
    }

    /* access modifiers changed from: private */
    public final void layoutRipple() {
        PointF pointF;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.context.getDisplay().getRealMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        this.rippleView.setRadius((float) Integer.max(i, i2));
        ChargingRippleView chargingRippleView = this.rippleView;
        int exactRotation = RotationUtils.getExactRotation(this.context);
        if (exactRotation == 1) {
            pointF = new PointF(((float) i) * this.normalizedPortPosY, ((float) i2) * (((float) 1) - this.normalizedPortPosX));
        } else if (exactRotation == 2) {
            float f = (float) 1;
            pointF = new PointF(((float) i) * (f - this.normalizedPortPosX), ((float) i2) * (f - this.normalizedPortPosY));
        } else if (exactRotation != 3) {
            pointF = new PointF(((float) i) * this.normalizedPortPosX, ((float) i2) * this.normalizedPortPosY);
        } else {
            pointF = new PointF(((float) i) * (((float) 1) - this.normalizedPortPosY), ((float) i2) * this.normalizedPortPosX);
        }
        chargingRippleView.setOrigin(pointF);
    }

    /* access modifiers changed from: private */
    public final void updateRippleColor() {
        this.rippleView.setColor(Utils.getColorAttr(this.context, 16843829).getDefaultColor());
    }

    @Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/charging/WiredChargingRippleController$ChargingRippleCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "(Lcom/android/systemui/statusbar/charging/WiredChargingRippleController;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: WiredChargingRippleController.kt */
    public final class ChargingRippleCommand implements Command {
        public ChargingRippleCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            WiredChargingRippleController.this.startRipple();
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar charging-ripple");
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/charging/WiredChargingRippleController$WiredChargingRippleEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "_id", "", "(Ljava/lang/String;II)V", "getId", "CHARGING_RIPPLE_PLAYED", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: WiredChargingRippleController.kt */
    public enum WiredChargingRippleEvent implements UiEventLogger.UiEventEnum {
        CHARGING_RIPPLE_PLAYED(829);
        
        private final int _id;

        private WiredChargingRippleEvent(int i) {
            this._id = i;
        }

        public int getId() {
            return this._id;
        }
    }
}
