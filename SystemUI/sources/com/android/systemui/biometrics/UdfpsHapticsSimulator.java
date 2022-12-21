package com.android.systemui.biometrics;

import android.media.AudioAttributes;
import android.os.VibrationEffect;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.p026io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u001e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0016J\u0010\u0010\u001f\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u000e\u0010 \u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0019\u0010\u000b\u001a\n \r*\u0004\u0018\u00010\f0\f¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017¨\u0006!"}, mo64987d2 = {"Lcom/android/systemui/biometrics/UdfpsHapticsSimulator;", "Lcom/android/systemui/statusbar/commandline/Command;", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "vibrator", "Lcom/android/systemui/statusbar/VibratorHelper;", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "(Lcom/android/systemui/statusbar/commandline/CommandRegistry;Lcom/android/systemui/statusbar/VibratorHelper;Lcom/android/keyguard/KeyguardUpdateMonitor;)V", "getKeyguardUpdateMonitor", "()Lcom/android/keyguard/KeyguardUpdateMonitor;", "sonificationEffects", "Landroid/media/AudioAttributes;", "kotlin.jvm.PlatformType", "getSonificationEffects", "()Landroid/media/AudioAttributes;", "udfpsController", "Lcom/android/systemui/biometrics/UdfpsController;", "getUdfpsController", "()Lcom/android/systemui/biometrics/UdfpsController;", "setUdfpsController", "(Lcom/android/systemui/biometrics/UdfpsController;)V", "getVibrator", "()Lcom/android/systemui/statusbar/VibratorHelper;", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "invalidCommand", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UdfpsHapticsSimulator.kt */
public final class UdfpsHapticsSimulator implements Command {
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final AudioAttributes sonificationEffects = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private UdfpsController udfpsController;
    private final VibratorHelper vibrator;

    @Inject
    public UdfpsHapticsSimulator(CommandRegistry commandRegistry, VibratorHelper vibratorHelper, KeyguardUpdateMonitor keyguardUpdateMonitor2) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(vibratorHelper, "vibrator");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor2, "keyguardUpdateMonitor");
        this.vibrator = vibratorHelper;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        commandRegistry.registerCommand("udfps-haptic", new Function0<Command>(this) {
            final /* synthetic */ UdfpsHapticsSimulator this$0;

            {
                this.this$0 = r1;
            }

            public final Command invoke() {
                return this.this$0;
            }
        });
    }

    public final VibratorHelper getVibrator() {
        return this.vibrator;
    }

    public final KeyguardUpdateMonitor getKeyguardUpdateMonitor() {
        return this.keyguardUpdateMonitor;
    }

    public final AudioAttributes getSonificationEffects() {
        return this.sonificationEffects;
    }

    public final UdfpsController getUdfpsController() {
        return this.udfpsController;
    }

    public final void setUdfpsController(UdfpsController udfpsController2) {
        this.udfpsController = udfpsController2;
    }

    public void execute(PrintWriter printWriter, List<String> list) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(list, "args");
        if (list.isEmpty()) {
            invalidCommand(printWriter);
            return;
        }
        String str = list.get(0);
        int hashCode = str.hashCode();
        if (hashCode != -1867169789) {
            if (hashCode != 96784904) {
                if (hashCode == 109757538 && str.equals("start")) {
                    UdfpsController udfpsController2 = this.udfpsController;
                    if (udfpsController2 != null) {
                        udfpsController2.playStartHaptic();
                        return;
                    }
                    return;
                }
            } else if (str.equals("error")) {
                this.vibrator.vibrate(VibrationEffect.get(1), this.sonificationEffects);
                return;
            }
        } else if (str.equals("success")) {
            this.vibrator.vibrate(VibrationEffect.get(0), this.sonificationEffects);
            return;
        }
        invalidCommand(printWriter);
    }

    public void help(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printWriter.println("Usage: adb shell cmd statusbar udfps-haptic <haptic>");
        printWriter.println("Available commands:");
        printWriter.println("  start");
        printWriter.println("  success, always plays CLICK haptic");
        printWriter.println("  error, always plays DOUBLE_CLICK haptic");
    }

    public final void invalidCommand(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        printWriter.println("invalid command");
        help(printWriter);
    }
}
