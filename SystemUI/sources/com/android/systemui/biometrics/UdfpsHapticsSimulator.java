package com.android.systemui.biometrics;

import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.io.PrintWriter;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: UdfpsHapticsSimulator.kt */
/* loaded from: classes.dex */
public final class UdfpsHapticsSimulator implements Command {
    @NotNull
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final AudioAttributes sonificationEffects = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    @Nullable
    private UdfpsController udfpsController;
    @Nullable
    private final Vibrator vibrator;

    public UdfpsHapticsSimulator(@NotNull CommandRegistry commandRegistry, @Nullable Vibrator vibrator, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor) {
        Intrinsics.checkNotNullParameter(commandRegistry, "commandRegistry");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        this.vibrator = vibrator;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        commandRegistry.registerCommand("udfps-haptic", new AnonymousClass1());
    }

    public final void setUdfpsController(@Nullable UdfpsController udfpsController) {
        this.udfpsController = udfpsController;
    }

    /* compiled from: UdfpsHapticsSimulator.kt */
    /* renamed from: com.android.systemui.biometrics.UdfpsHapticsSimulator$1  reason: invalid class name */
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
            return UdfpsHapticsSimulator.this;
        }
    }

    @Override // com.android.systemui.statusbar.commandline.Command
    public void execute(@NotNull PrintWriter pw, @NotNull List<String> args) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        if (args.isEmpty()) {
            invalidCommand(pw);
            return;
        }
        String str = args.get(0);
        switch (str.hashCode()) {
            case -1867169789:
                if (str.equals("success")) {
                    Vibrator vibrator = this.vibrator;
                    if (vibrator == null) {
                        return;
                    }
                    vibrator.vibrate(VibrationEffect.get(0), this.sonificationEffects);
                    return;
                }
                break;
            case -1731151282:
                if (str.equals("acquired")) {
                    this.keyguardUpdateMonitor.playAcquiredHaptic();
                    return;
                }
                break;
            case 96784904:
                if (str.equals("error")) {
                    Vibrator vibrator2 = this.vibrator;
                    if (vibrator2 == null) {
                        return;
                    }
                    vibrator2.vibrate(VibrationEffect.get(1), this.sonificationEffects);
                    return;
                }
                break;
            case 109757538:
                if (str.equals("start")) {
                    UdfpsController udfpsController = this.udfpsController;
                    if (udfpsController == null) {
                        return;
                    }
                    udfpsController.playStartHaptic();
                    return;
                }
                break;
        }
        invalidCommand(pw);
    }

    public void help(@NotNull PrintWriter pw) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        pw.println("Usage: adb shell cmd statusbar udfps-haptic <haptic>");
        pw.println("Available commands:");
        pw.println("  start");
        pw.println("  acquired");
        pw.println("  success, always plays CLICK haptic");
        pw.println("  error, always plays DOUBLE_CLICK haptic");
    }

    public final void invalidCommand(@NotNull PrintWriter pw) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        pw.println("invalid command");
        help(pw);
    }
}
