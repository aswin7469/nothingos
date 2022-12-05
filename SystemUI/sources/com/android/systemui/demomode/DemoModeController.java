package com.android.systemui.demomode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.settings.GlobalSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: DemoModeController.kt */
/* loaded from: classes.dex */
public final class DemoModeController implements CallbackController<DemoMode>, Dumpable {
    @NotNull
    private final DemoModeController$broadcastReceiver$1 broadcastReceiver;
    @NotNull
    private final Context context;
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final GlobalSettings globalSettings;
    private boolean initialized;
    private boolean isInDemoMode;
    @NotNull
    private final Map<String, List<DemoMode>> receiverMap;
    @NotNull
    private final List<DemoMode> receivers = new ArrayList();
    @NotNull
    private final DemoModeController$tracker$1 tracker;

    /* JADX WARN: Type inference failed for: r3v4, types: [com.android.systemui.demomode.DemoModeController$broadcastReceiver$1] */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.android.systemui.demomode.DemoModeController$tracker$1] */
    public DemoModeController(@NotNull Context context, @NotNull DumpManager dumpManager, @NotNull GlobalSettings globalSettings) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(globalSettings, "globalSettings");
        this.context = context;
        this.dumpManager = dumpManager;
        this.globalSettings = globalSettings;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        List<String> COMMANDS = DemoMode.COMMANDS;
        Intrinsics.checkNotNullExpressionValue(COMMANDS, "COMMANDS");
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(COMMANDS, 10));
        for (String command : COMMANDS) {
            Intrinsics.checkNotNullExpressionValue(command, "command");
            arrayList.add((List) linkedHashMap.put(command, new ArrayList()));
        }
        this.receiverMap = linkedHashMap;
        final Context context2 = this.context;
        this.tracker = new DemoModeAvailabilityTracker(context2) { // from class: com.android.systemui.demomode.DemoModeController$tracker$1
            @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
            public void onDemoModeAvailabilityChanged() {
                DemoModeController.this.setIsDemoModeAllowed(isDemoModeAvailable());
            }

            @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
            public void onDemoModeStarted() {
                if (DemoModeController.this.isInDemoMode() != isInDemoMode()) {
                    DemoModeController.this.enterDemoMode();
                }
            }

            @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
            public void onDemoModeFinished() {
                if (DemoModeController.this.isInDemoMode() != isInDemoMode()) {
                    DemoModeController.this.exitDemoMode();
                }
            }
        };
        this.broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.demomode.DemoModeController$broadcastReceiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(@NotNull Context context3, @NotNull Intent intent) {
                Bundle extras;
                CharSequence trim;
                Intrinsics.checkNotNullParameter(context3, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if ("com.android.systemui.demo".equals(intent.getAction()) && (extras = intent.getExtras()) != null) {
                    String string = extras.getString("command", "");
                    Intrinsics.checkNotNullExpressionValue(string, "bundle.getString(\"command\", \"\")");
                    trim = StringsKt__StringsKt.trim(string);
                    String obj = trim.toString();
                    Objects.requireNonNull(obj, "null cannot be cast to non-null type java.lang.String");
                    String lowerCase = obj.toLowerCase();
                    Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase()");
                    if (lowerCase.length() == 0) {
                        return;
                    }
                    try {
                        DemoModeController.this.dispatchDemoCommand(lowerCase, extras);
                    } catch (Throwable th) {
                        Log.w("DemoModeController", "Error running demo command, intent=" + intent + ' ' + th);
                    }
                }
            }
        };
    }

    public final boolean isInDemoMode() {
        return this.isInDemoMode;
    }

    public final boolean isAvailable() {
        return isDemoModeAvailable();
    }

    public final void initialize() {
        if (this.initialized) {
            throw new IllegalStateException("Already initialized");
        }
        this.initialized = true;
        this.dumpManager.registerDumpable("DemoModeController", this);
        startTracking();
        this.isInDemoMode = isInDemoMode();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.systemui.demo");
        this.context.registerReceiverAsUser(this.broadcastReceiver, UserHandle.ALL, intentFilter, "android.permission.DUMP", null);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(@NotNull DemoMode listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        List<String> commands = listener.demoCommands();
        Intrinsics.checkNotNullExpressionValue(commands, "commands");
        for (String str : commands) {
            if (!this.receiverMap.containsKey(str)) {
                throw new IllegalStateException("Command (" + ((Object) str) + ") not recognized. See DemoMode.java for valid commands");
            }
            List<DemoMode> list = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list);
            list.add(listener);
        }
        synchronized (this) {
            this.receivers.add(listener);
        }
        if (this.isInDemoMode) {
            listener.onDemoModeStarted();
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(@NotNull DemoMode listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        synchronized (this) {
            List<String> demoCommands = listener.demoCommands();
            Intrinsics.checkNotNullExpressionValue(demoCommands, "listener.demoCommands()");
            for (String str : demoCommands) {
                List<DemoMode> list = this.receiverMap.get(str);
                Intrinsics.checkNotNull(list);
                list.remove(listener);
            }
            this.receivers.remove(listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setIsDemoModeAllowed(boolean z) {
        if (!this.isInDemoMode || z) {
            return;
        }
        requestFinishDemoMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void enterDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = true;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            demoModeCommandReceiver.onDemoModeStarted();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void exitDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = false;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            demoModeCommandReceiver.onDemoModeFinished();
        }
    }

    public final void dispatchDemoCommand(@NotNull String command, @NotNull Bundle args) {
        Intrinsics.checkNotNullParameter(command, "command");
        Intrinsics.checkNotNullParameter(args, "args");
        Assert.isMainThread();
        if (!isAvailable()) {
            return;
        }
        if (Intrinsics.areEqual(command, "enter")) {
            enterDemoMode();
        } else if (Intrinsics.areEqual(command, "exit")) {
            exitDemoMode();
        } else if (!this.isInDemoMode) {
            enterDemoMode();
        }
        List<DemoMode> list = this.receiverMap.get(command);
        Intrinsics.checkNotNull(list);
        for (DemoMode demoMode : list) {
            demoMode.dispatchDemoCommand(command, args);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        List<DemoModeCommandReceiver> list;
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("DemoModeController state -");
        pw.println(Intrinsics.stringPlus("  isInDemoMode=", Boolean.valueOf(this.isInDemoMode)));
        pw.println(Intrinsics.stringPlus("  isDemoModeAllowed=", Boolean.valueOf(isAvailable())));
        pw.print("  receivers=[");
        synchronized (this) {
            list = CollectionsKt.toList(this.receivers);
            Unit unit = Unit.INSTANCE;
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            pw.print(Intrinsics.stringPlus(" ", demoModeCommandReceiver.getClass().getSimpleName()));
        }
        pw.println(" ]");
        pw.println("  receiverMap= [");
        for (String str : this.receiverMap.keySet()) {
            pw.print("    " + str + " : [");
            List<DemoMode> list2 = this.receiverMap.get(str);
            Intrinsics.checkNotNull(list2);
            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(list2, 10));
            for (DemoMode demoMode : list2) {
                arrayList.add(demoMode.getClass().getSimpleName());
            }
            pw.println(Intrinsics.stringPlus(CollectionsKt.joinToString$default(arrayList, ",", null, null, 0, null, null, 62, null), " ]"));
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
