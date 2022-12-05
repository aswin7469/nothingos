package com.android.systemui.statusbar.phone;

import android.hardware.Sensor;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: KeyguardLiftController.kt */
/* loaded from: classes.dex */
public final class KeyguardLiftController extends KeyguardUpdateMonitorCallback implements StatusBarStateController.StateListener, Dumpable {
    @NotNull
    private final AsyncSensorManager asyncSensorManager;
    private boolean bouncerVisible;
    private boolean isListening;
    @NotNull
    private final KeyguardUpdateMonitor keyguardUpdateMonitor;
    @NotNull
    private final TriggerEventListener listener = new TriggerEventListener() { // from class: com.android.systemui.statusbar.phone.KeyguardLiftController$listener$1
        @Override // android.hardware.TriggerEventListener
        public void onTrigger(@Nullable TriggerEvent triggerEvent) {
            KeyguardUpdateMonitor keyguardUpdateMonitor;
            Assert.isMainThread();
            KeyguardLiftController.this.isListening = false;
            KeyguardLiftController.this.updateListeningState();
            keyguardUpdateMonitor = KeyguardLiftController.this.keyguardUpdateMonitor;
            keyguardUpdateMonitor.requestFaceAuth(true);
        }
    };
    private final Sensor pickupSensor;
    @NotNull
    private final StatusBarStateController statusBarStateController;

    public KeyguardLiftController(@NotNull StatusBarStateController statusBarStateController, @NotNull AsyncSensorManager asyncSensorManager, @NotNull KeyguardUpdateMonitor keyguardUpdateMonitor, @NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(asyncSensorManager, "asyncSensorManager");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.statusBarStateController = statusBarStateController;
        this.asyncSensorManager = asyncSensorManager;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.pickupSensor = asyncSensorManager.getDefaultSensor(25);
        String name = KeyguardLiftController.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
        statusBarStateController.addCallback(this);
        keyguardUpdateMonitor.registerCallback(this);
        updateListeningState();
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        updateListeningState();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onKeyguardBouncerChanged(boolean z) {
        this.bouncerVisible = z;
        updateListeningState();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
    public void onKeyguardVisibilityChanged(boolean z) {
        updateListeningState();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("KeyguardLiftController:");
        pw.println(Intrinsics.stringPlus("  pickupSensor: ", this.pickupSensor));
        pw.println(Intrinsics.stringPlus("  isListening: ", Boolean.valueOf(this.isListening)));
        pw.println(Intrinsics.stringPlus("  bouncerVisible: ", Boolean.valueOf(this.bouncerVisible)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateListeningState() {
        if (this.pickupSensor == null) {
            return;
        }
        boolean z = true;
        boolean z2 = this.keyguardUpdateMonitor.isKeyguardVisible() && !this.statusBarStateController.isDozing();
        boolean isFaceAuthEnabledForUser = this.keyguardUpdateMonitor.isFaceAuthEnabledForUser(KeyguardUpdateMonitor.getCurrentUser());
        if ((!z2 && !this.bouncerVisible) || !isFaceAuthEnabledForUser) {
            z = false;
        }
        if (z == this.isListening) {
            return;
        }
        this.isListening = z;
        if (z) {
            this.asyncSensorManager.requestTriggerSensor(this.listener, this.pickupSensor);
        } else {
            this.asyncSensorManager.cancelTriggerSensor(this.listener, this.pickupSensor);
        }
    }
}
