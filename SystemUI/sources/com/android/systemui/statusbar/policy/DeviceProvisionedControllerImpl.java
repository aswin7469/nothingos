package com.android.systemui.statusbar.policy;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000¢\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\f*\u0002\"%\b\u0017\u0018\u0000 F2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001FB;\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u0002H\u0016J!\u0010-\u001a\u00020+2\u0017\u0010.\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020+0/¢\u0006\u0002\b0H\u0004J%\u00101\u001a\u00020+2\u0006\u00102\u001a\u0002032\u000e\u00104\u001a\n\u0012\u0006\b\u0001\u0012\u00020605H\u0016¢\u0006\u0002\u00107J\b\u00108\u001a\u00020\u0012H\u0016J\b\u00109\u001a\u00020+H\u0016J\b\u0010:\u001a\u00020;H\u0016J\b\u0010<\u001a\u00020;H\u0016J\u0010\u0010=\u001a\u00020;2\u0006\u0010>\u001a\u00020\u0012H\u0016J\b\u0010?\u001a\u00020+H\u0016J\b\u0010@\u001a\u00020+H\u0016J\b\u0010A\u001a\u00020+H\u0016J\u0010\u0010B\u001a\u00020+2\u0006\u0010,\u001a\u00020\u0002H\u0016J\u001c\u0010C\u001a\u00020+2\b\b\u0002\u0010D\u001a\u00020;2\b\b\u0002\u0010E\u001a\u00020\u0012H\u0003R\u0014\u0010\u0011\u001a\u00020\u00128BX\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0019\u001a\n \u001b*\u0004\u0018\u00010\u001a0\u001aX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00020\u001e8\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u00020\"X\u0004¢\u0006\u0004\n\u0002\u0010#R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u00020%X\u0004¢\u0006\u0004\n\u0002\u0010&R\u0010\u0010'\u001a\u00020(8\u0002X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010)\u001a\n \u001b*\u0004\u0018\u00010\u001a0\u001aX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006G"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/DeviceProvisionedControllerImpl;", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedController;", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedController$DeviceProvisionedListener;", "Lcom/android/systemui/Dumpable;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "globalSettings", "Lcom/android/systemui/util/settings/GlobalSettings;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "backgroundHandler", "Landroid/os/Handler;", "mainExecutor", "Ljava/util/concurrent/Executor;", "(Lcom/android/systemui/util/settings/SecureSettings;Lcom/android/systemui/util/settings/GlobalSettings;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/dump/DumpManager;Landroid/os/Handler;Ljava/util/concurrent/Executor;)V", "_currentUser", "", "get_currentUser", "()I", "backgroundExecutor", "Landroid/os/HandlerExecutor;", "deviceProvisioned", "Ljava/util/concurrent/atomic/AtomicBoolean;", "deviceProvisionedUri", "Landroid/net/Uri;", "kotlin.jvm.PlatformType", "initted", "listeners", "Landroid/util/ArraySet;", "lock", "", "observer", "com/android/systemui/statusbar/policy/DeviceProvisionedControllerImpl$observer$1", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedControllerImpl$observer$1;", "userChangedCallback", "com/android/systemui/statusbar/policy/DeviceProvisionedControllerImpl$userChangedCallback$1", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedControllerImpl$userChangedCallback$1;", "userSetupComplete", "Landroid/util/SparseBooleanArray;", "userSetupUri", "addCallback", "", "listener", "dispatchChange", "callback", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getCurrentUser", "init", "isCurrentUserSetup", "", "isDeviceProvisioned", "isUserSetup", "user", "onDeviceProvisionedChanged", "onUserSetupChanged", "onUserSwitched", "removeCallback", "updateValues", "updateDeviceProvisioned", "updateUser", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DeviceProvisionedControllerImpl.kt */
public class DeviceProvisionedControllerImpl implements DeviceProvisionedController, DeviceProvisionedController.DeviceProvisionedListener, Dumpable {
    private static final int ALL_USERS = -1;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int NO_USERS = -2;
    protected static final String TAG = "DeviceProvisionedControllerImpl";
    private final HandlerExecutor backgroundExecutor;
    private final Handler backgroundHandler;
    private final AtomicBoolean deviceProvisioned = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public final Uri deviceProvisionedUri;
    private final DumpManager dumpManager;
    private final GlobalSettings globalSettings;
    private final AtomicBoolean initted;
    private final ArraySet<DeviceProvisionedController.DeviceProvisionedListener> listeners;
    private final Object lock;
    private final Executor mainExecutor;
    private final DeviceProvisionedControllerImpl$observer$1 observer;
    private final SecureSettings secureSettings;
    private final DeviceProvisionedControllerImpl$userChangedCallback$1 userChangedCallback;
    private final SparseBooleanArray userSetupComplete;
    /* access modifiers changed from: private */
    public final Uri userSetupUri;
    private final UserTracker userTracker;

    @Inject
    public DeviceProvisionedControllerImpl(SecureSettings secureSettings2, GlobalSettings globalSettings2, UserTracker userTracker2, DumpManager dumpManager2, @Background Handler handler, @Main Executor executor) {
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        Intrinsics.checkNotNullParameter(globalSettings2, "globalSettings");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(handler, "backgroundHandler");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.secureSettings = secureSettings2;
        this.globalSettings = globalSettings2;
        this.userTracker = userTracker2;
        this.dumpManager = dumpManager2;
        this.backgroundHandler = handler;
        this.mainExecutor = executor;
        this.deviceProvisionedUri = globalSettings2.getUriFor(WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED);
        this.userSetupUri = secureSettings2.getUriFor(WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE);
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        this.userSetupComplete = sparseBooleanArray;
        this.listeners = new ArraySet<>();
        this.lock = new Object();
        this.backgroundExecutor = new HandlerExecutor(handler);
        this.initted = new AtomicBoolean(false);
        this.observer = new DeviceProvisionedControllerImpl$observer$1(this, handler);
        this.userChangedCallback = new DeviceProvisionedControllerImpl$userChangedCallback$1(this);
        sparseBooleanArray.put(getCurrentUser(), false);
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/DeviceProvisionedControllerImpl$Companion;", "", "()V", "ALL_USERS", "", "NO_USERS", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: DeviceProvisionedControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final int get_currentUser() {
        return this.userTracker.getUserId();
    }

    public int getCurrentUser() {
        return get_currentUser();
    }

    public void init() {
        if (this.initted.compareAndSet(false, true)) {
            this.dumpManager.registerDumpable(this);
            updateValues$default(this, false, 0, 3, (Object) null);
            this.userTracker.addCallback(this.userChangedCallback, this.backgroundExecutor);
            this.globalSettings.registerContentObserver(this.deviceProvisionedUri, (ContentObserver) this.observer);
            this.secureSettings.registerContentObserverForUser(this.userSetupUri, (ContentObserver) this.observer, -1);
        }
    }

    static /* synthetic */ void updateValues$default(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl, boolean z, int i, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 1) != 0) {
                z = true;
            }
            if ((i2 & 2) != 0) {
                i = -1;
            }
            deviceProvisionedControllerImpl.updateValues(z, i);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: updateValues");
    }

    /* access modifiers changed from: private */
    public final void updateValues(boolean z, int i) {
        boolean z2 = true;
        if (z) {
            this.deviceProvisioned.set(this.globalSettings.getInt(WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED, 0) != 0);
        }
        synchronized (this.lock) {
            if (i == -1) {
                try {
                    int size = this.userSetupComplete.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        int keyAt = this.userSetupComplete.keyAt(i2);
                        this.userSetupComplete.put(keyAt, this.secureSettings.getIntForUser(WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE, 0, keyAt) != 0);
                    }
                } finally {
                }
            } else if (i != -2) {
                if (this.secureSettings.getIntForUser(WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE, 0, i) == 0) {
                    z2 = false;
                }
                this.userSetupComplete.put(i, z2);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public void addCallback(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        Intrinsics.checkNotNullParameter(deviceProvisionedListener, "listener");
        synchronized (this.lock) {
            this.listeners.add(deviceProvisionedListener);
        }
    }

    public void removeCallback(DeviceProvisionedController.DeviceProvisionedListener deviceProvisionedListener) {
        Intrinsics.checkNotNullParameter(deviceProvisionedListener, "listener");
        synchronized (this.lock) {
            this.listeners.remove(deviceProvisionedListener);
        }
    }

    public boolean isDeviceProvisioned() {
        return this.deviceProvisioned.get();
    }

    public boolean isUserSetup(int i) {
        int indexOfKey;
        synchronized (this.lock) {
            indexOfKey = this.userSetupComplete.indexOfKey(i);
        }
        boolean z = false;
        if (indexOfKey < 0) {
            if (this.secureSettings.getIntForUser(WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE, 0, i) != 0) {
                z = true;
            }
            synchronized (this.lock) {
                this.userSetupComplete.put(i, z);
                Unit unit = Unit.INSTANCE;
            }
        } else {
            synchronized (this.lock) {
                z = this.userSetupComplete.get(i, false);
            }
        }
        return z;
    }

    public boolean isCurrentUserSetup() {
        return isUserSetup(getCurrentUser());
    }

    public void onDeviceProvisionedChanged() {
        dispatchChange(DeviceProvisionedControllerImpl$onDeviceProvisionedChanged$1.INSTANCE);
    }

    public void onUserSetupChanged() {
        dispatchChange(DeviceProvisionedControllerImpl$onUserSetupChanged$1.INSTANCE);
    }

    public void onUserSwitched() {
        dispatchChange(DeviceProvisionedControllerImpl$onUserSwitched$1.INSTANCE);
    }

    /* access modifiers changed from: protected */
    public final void dispatchChange(Function1<? super DeviceProvisionedController.DeviceProvisionedListener, Unit> function1) {
        ArrayList arrayList;
        Intrinsics.checkNotNullParameter(function1, "callback");
        synchronized (this.lock) {
            arrayList = new ArrayList(this.listeners);
        }
        this.mainExecutor.execute(new DeviceProvisionedControllerImpl$$ExternalSyntheticLambda0(arrayList, function1));
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchChange$lambda-7  reason: not valid java name */
    public static final void m3237dispatchChange$lambda7(ArrayList arrayList, Function1 function1) {
        Intrinsics.checkNotNullParameter(arrayList, "$listenersCopy");
        Intrinsics.checkNotNullParameter(function1, "$callback");
        for (Object invoke : arrayList) {
            function1.invoke(invoke);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Device provisioned: " + this.deviceProvisioned.get());
        synchronized (this.lock) {
            printWriter.println("User setup complete: " + this.userSetupComplete);
            printWriter.println("Listeners: " + this.listeners);
            Unit unit = Unit.INSTANCE;
        }
    }
}
