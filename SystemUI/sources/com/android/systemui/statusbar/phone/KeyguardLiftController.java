package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.TriggerEventListener;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0005*\u0002\u0012\u001a\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B/\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ%\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u000e\u0010 \u001a\n\u0012\u0006\b\u0001\u0012\u00020\"0!H\u0016¢\u0006\u0002\u0010#J\b\u0010$\u001a\u00020\u001dH\u0002J\b\u0010%\u001a\u00020\u001dH\u0016J\b\u0010&\u001a\u00020\u001dH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0016\u001a\n \u0018*\u0004\u0018\u00010\u00170\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0004\n\u0002\u0010\u001b¨\u0006'"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/KeyguardLiftController;", "Lcom/android/systemui/Dumpable;", "Lcom/android/systemui/CoreStartable;", "context", "Landroid/content/Context;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "asyncSensorManager", "Lcom/android/systemui/util/sensors/AsyncSensorManager;", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/Context;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/util/sensors/AsyncSensorManager;Lcom/android/keyguard/KeyguardUpdateMonitor;Lcom/android/systemui/dump/DumpManager;)V", "bouncerVisible", "", "isListening", "keyguardUpdateMonitorCallback", "com/android/systemui/statusbar/phone/KeyguardLiftController$keyguardUpdateMonitorCallback$1", "Lcom/android/systemui/statusbar/phone/KeyguardLiftController$keyguardUpdateMonitorCallback$1;", "listener", "Landroid/hardware/TriggerEventListener;", "pickupSensor", "Landroid/hardware/Sensor;", "kotlin.jvm.PlatformType", "statusBarStateListener", "com/android/systemui/statusbar/phone/KeyguardLiftController$statusBarStateListener$1", "Lcom/android/systemui/statusbar/phone/KeyguardLiftController$statusBarStateListener$1;", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "init", "start", "updateListeningState", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: KeyguardLiftController.kt */
public final class KeyguardLiftController extends CoreStartable implements Dumpable {
    private final AsyncSensorManager asyncSensorManager;
    /* access modifiers changed from: private */
    public boolean bouncerVisible;
    private final Context context;
    private final DumpManager dumpManager;
    /* access modifiers changed from: private */
    public boolean isListening;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final KeyguardLiftController$keyguardUpdateMonitorCallback$1 keyguardUpdateMonitorCallback = new KeyguardLiftController$keyguardUpdateMonitorCallback$1(this);
    private final TriggerEventListener listener = new KeyguardLiftController$listener$1(this);
    private final Sensor pickupSensor;
    private final StatusBarStateController statusBarStateController;
    private final KeyguardLiftController$statusBarStateListener$1 statusBarStateListener = new KeyguardLiftController$statusBarStateListener$1(this);

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public KeyguardLiftController(Context context2, StatusBarStateController statusBarStateController2, AsyncSensorManager asyncSensorManager2, KeyguardUpdateMonitor keyguardUpdateMonitor2, DumpManager dumpManager2) {
        super(context2);
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(asyncSensorManager2, "asyncSensorManager");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor2, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        this.context = context2;
        this.statusBarStateController = statusBarStateController2;
        this.asyncSensorManager = asyncSensorManager2;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.dumpManager = dumpManager2;
        this.pickupSensor = asyncSensorManager2.getDefaultSensor(25);
    }

    public void start() {
        if (this.context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            init();
        }
    }

    private final void init() {
        DumpManager dumpManager2 = this.dumpManager;
        String name = getClass().getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager2.registerDumpable(name, this);
        this.statusBarStateController.addCallback(this.statusBarStateListener);
        this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateMonitorCallback);
        updateListeningState();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("KeyguardLiftController:");
        printWriter.println("  pickupSensor: " + this.pickupSensor);
        printWriter.println("  isListening: " + this.isListening);
        printWriter.println("  bouncerVisible: " + this.bouncerVisible);
    }

    /* access modifiers changed from: private */
    public final void updateListeningState() {
        if (this.pickupSensor != null) {
            boolean z = true;
            boolean z2 = this.keyguardUpdateMonitor.isKeyguardVisible() && !this.statusBarStateController.isDozing();
            boolean isFaceAuthEnabledForUser = this.keyguardUpdateMonitor.isFaceAuthEnabledForUser(KeyguardUpdateMonitor.getCurrentUser());
            if ((!z2 && !this.bouncerVisible) || !isFaceAuthEnabledForUser) {
                z = false;
            }
            if (z != this.isListening) {
                this.isListening = z;
                if (z) {
                    this.asyncSensorManager.requestTriggerSensor(this.listener, this.pickupSensor);
                } else {
                    this.asyncSensorManager.cancelTriggerSensor(this.listener, this.pickupSensor);
                }
            }
        }
    }
}
