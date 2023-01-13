package com.android.systemui.demomode;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.settings.GlobalSettings;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0003*\u0002\f\u001c\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\u001d\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0002H\u0016J\u0016\u0010!\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u00182\u0006\u0010#\u001a\u00020$J%\u0010%\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020'2\u000e\u0010#\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00180(H\u0016¢\u0006\u0002\u0010)J\b\u0010*\u001a\u00020\u001fH\u0002J\b\u0010+\u001a\u00020\u001fH\u0002J\u0006\u0010,\u001a\u00020\u001fJ\u0010\u0010-\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0002H\u0016J\u0006\u0010.\u001a\u00020\u001fJ\u000e\u0010/\u001a\u00020\u001f2\u0006\u00100\u001a\u00020\u000fJ\u0006\u00101\u001a\u00020\u001fJ\u0018\u00102\u001a\u00020\u001f2\u0006\u00103\u001a\u00020\u00182\u0006\u00104\u001a\u000205H\u0002J\u0010\u00106\u001a\u00020\u001f2\u0006\u00107\u001a\u00020\u000fH\u0002R\u0010\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0010\u001a\u00020\u000f8FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\u00020\u000fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0011\"\u0004\b\u0015\u0010\u0013R \u0010\u0016\u001a\u0014\u0012\u0004\u0012\u00020\u0018\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u00190\u0017X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0004\n\u0002\u0010\u001d¨\u00068"}, mo65043d2 = {"Lcom/android/systemui/demomode/DemoModeController;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/demomode/DemoMode;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "globalSettings", "Lcom/android/systemui/util/settings/GlobalSettings;", "(Landroid/content/Context;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/util/settings/GlobalSettings;)V", "broadcastReceiver", "com/android/systemui/demomode/DemoModeController$broadcastReceiver$1", "Lcom/android/systemui/demomode/DemoModeController$broadcastReceiver$1;", "initialized", "", "isAvailable", "()Z", "setAvailable", "(Z)V", "isInDemoMode", "setInDemoMode", "receiverMap", "", "", "", "receivers", "tracker", "com/android/systemui/demomode/DemoModeController$tracker$1", "Lcom/android/systemui/demomode/DemoModeController$tracker$1;", "addCallback", "", "listener", "dispatchDemoCommand", "command", "args", "Landroid/os/Bundle;", "dump", "pw", "Ljava/io/PrintWriter;", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "enterDemoMode", "exitDemoMode", "initialize", "removeCallback", "requestFinishDemoMode", "requestSetDemoModeAllowed", "allowed", "requestStartDemoMode", "setGlobal", "key", "value", "", "setIsDemoModeAllowed", "enabled", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DemoModeController.kt */
public final class DemoModeController implements CallbackController<DemoMode>, Dumpable {
    private final DemoModeController$broadcastReceiver$1 broadcastReceiver;
    private final Context context;
    private final DumpManager dumpManager;
    private final GlobalSettings globalSettings;
    private boolean initialized;
    private boolean isAvailable;
    private boolean isInDemoMode;
    private final Map<String, List<DemoMode>> receiverMap;
    private final List<DemoMode> receivers = new ArrayList();
    private final DemoModeController$tracker$1 tracker;

    public DemoModeController(Context context2, DumpManager dumpManager2, GlobalSettings globalSettings2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(globalSettings2, "globalSettings");
        this.context = context2;
        this.dumpManager = dumpManager2;
        this.globalSettings = globalSettings2;
        Map<String, List<DemoMode>> linkedHashMap = new LinkedHashMap<>();
        List<String> list = DemoMode.COMMANDS;
        Intrinsics.checkNotNullExpressionValue(list, "COMMANDS");
        Iterable<String> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (String str : iterable) {
            Intrinsics.checkNotNullExpressionValue(str, DemoMode.EXTRA_COMMAND);
            arrayList.add(linkedHashMap.put(str, new ArrayList()));
        }
        List list2 = (List) arrayList;
        this.receiverMap = linkedHashMap;
        this.tracker = new DemoModeController$tracker$1(this, this.context);
        this.broadcastReceiver = new DemoModeController$broadcastReceiver$1(this);
    }

    public final boolean isInDemoMode() {
        return this.isInDemoMode;
    }

    public final void setInDemoMode(boolean z) {
        this.isInDemoMode = z;
    }

    public final void setAvailable(boolean z) {
        this.isAvailable = z;
    }

    public final boolean isAvailable() {
        return this.tracker.isDemoModeAvailable();
    }

    public final void initialize() {
        if (!this.initialized) {
            this.initialized = true;
            this.dumpManager.registerDumpable("DemoModeController", this);
            this.tracker.startTracking();
            this.isInDemoMode = this.tracker.isInDemoMode();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DemoMode.ACTION_DEMO);
            this.context.registerReceiverAsUser(this.broadcastReceiver, UserHandle.ALL, intentFilter, "android.permission.DUMP", (Handler) null, 2);
            return;
        }
        throw new IllegalStateException("Already initialized");
    }

    public void addCallback(DemoMode demoMode) {
        Intrinsics.checkNotNullParameter(demoMode, "listener");
        List<String> demoCommands = demoMode.demoCommands();
        Intrinsics.checkNotNullExpressionValue(demoCommands, "commands");
        for (String str : demoCommands) {
            if (this.receiverMap.containsKey(str)) {
                List<DemoMode> list = this.receiverMap.get(str);
                Intrinsics.checkNotNull(list);
                list.add(demoMode);
            } else {
                throw new IllegalStateException("Command (" + str + ") not recognized. See DemoMode.java for valid commands");
            }
        }
        synchronized (this) {
            this.receivers.add(demoMode);
        }
        if (this.isInDemoMode) {
            demoMode.onDemoModeStarted();
        }
    }

    public void removeCallback(DemoMode demoMode) {
        Intrinsics.checkNotNullParameter(demoMode, "listener");
        synchronized (this) {
            List<String> demoCommands = demoMode.demoCommands();
            Intrinsics.checkNotNullExpressionValue(demoCommands, "listener.demoCommands()");
            for (String str : demoCommands) {
                List<DemoMode> list = this.receiverMap.get(str);
                Intrinsics.checkNotNull(list);
                list.remove((Object) demoMode);
            }
            this.receivers.remove((Object) demoMode);
        }
    }

    /* access modifiers changed from: private */
    public final void setIsDemoModeAllowed(boolean z) {
        if (this.isInDemoMode && !z) {
            requestFinishDemoMode();
        }
    }

    /* access modifiers changed from: private */
    public final void enterDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = true;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver onDemoModeStarted : list) {
            onDemoModeStarted.onDemoModeStarted();
        }
    }

    /* access modifiers changed from: private */
    public final void exitDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = false;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver onDemoModeFinished : list) {
            onDemoModeFinished.onDemoModeFinished();
        }
    }

    public final void dispatchDemoCommand(String str, Bundle bundle) {
        Intrinsics.checkNotNullParameter(str, DemoMode.EXTRA_COMMAND);
        Intrinsics.checkNotNullParameter(bundle, "args");
        Assert.isMainThread();
        if (isAvailable()) {
            if (Intrinsics.areEqual((Object) str, (Object) DemoMode.COMMAND_ENTER)) {
                enterDemoMode();
            } else if (Intrinsics.areEqual((Object) str, (Object) DemoMode.COMMAND_EXIT)) {
                exitDemoMode();
            } else if (!this.isInDemoMode) {
                enterDemoMode();
            }
            List<DemoMode> list = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list);
            for (DemoMode dispatchDemoCommand : list) {
                dispatchDemoCommand.dispatchDemoCommand(str, bundle);
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        List<DemoModeCommandReceiver> list;
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("DemoModeController state -");
        printWriter.println("  isInDemoMode=" + this.isInDemoMode);
        printWriter.println("  isDemoModeAllowed=" + isAvailable());
        printWriter.print("  receivers=[");
        synchronized (this) {
            list = CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + demoModeCommandReceiver.getClass().getSimpleName());
        }
        printWriter.println(" ]");
        printWriter.println("  receiverMap= [");
        for (String str : this.receiverMap.keySet()) {
            printWriter.print("    " + str + " : [");
            List<DemoMode> list2 = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list2);
            Iterable<DemoMode> iterable = list2;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (DemoMode demoMode : iterable) {
                arrayList.add(demoMode.getClass().getSimpleName());
            }
            printWriter.println(CollectionsKt.joinToString$default((List) arrayList, NavigationBarInflaterView.BUTTON_SEPARATOR, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null) + " ]");
        }
    }

    public final void requestSetDemoModeAllowed(boolean z) {
        setGlobal("sysui_demo_allowed", z ? 1 : 0);
    }

    public final void requestStartDemoMode() {
        setGlobal("sysui_tuner_demo_on", 1);
    }

    public final void requestFinishDemoMode() {
        setGlobal("sysui_tuner_demo_on", 0);
    }

    private final void setGlobal(String str, int i) {
        this.globalSettings.putInt(str, i);
    }
}
