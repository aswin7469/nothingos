package com.android.systemui.statusbar.notification.collection.provider;

import android.os.Build;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.Assert;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.ListenerSetKt;
import java.p026io.PrintWriter;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u001a2\u00020\u0001:\u0002\u001a\u001bB\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J%\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u000e\u0010\u0011\u001a\n\u0012\u0006\b\u0001\u0012\u00020\t0\u0012H\u0016¢\u0006\u0002\u0010\u0013J\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\fJ\u000e\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider;", "Lcom/android/systemui/Dumpable;", "commandRegistry", "Lcom/android/systemui/statusbar/commandline/CommandRegistry;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/statusbar/commandline/CommandRegistry;Lcom/android/systemui/dump/DumpManager;)V", "allowedPackages", "", "", "listeners", "Lcom/android/systemui/util/ListenerSet;", "Ljava/lang/Runnable;", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "registerInvalidationListener", "listener", "shouldFilterOut", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "Companion", "NotifFilterCommand", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DebugModeFilterProvider.kt */
public final class DebugModeFilterProvider implements Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "DebugModeFilterProvider";
    /* access modifiers changed from: private */
    public List<String> allowedPackages = CollectionsKt.emptyList();
    private final CommandRegistry commandRegistry;
    /* access modifiers changed from: private */
    public final ListenerSet<Runnable> listeners = new ListenerSet<>();

    @Inject
    public DebugModeFilterProvider(CommandRegistry commandRegistry2, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(commandRegistry2, "commandRegistry");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.commandRegistry = commandRegistry2;
        dumpManager.registerDumpable(this);
    }

    public final void registerInvalidationListener(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "listener");
        Assert.isMainThread();
        if (Build.isDebuggable()) {
            boolean isEmpty = this.listeners.isEmpty();
            this.listeners.addIfAbsent(runnable);
            if (isEmpty) {
                this.commandRegistry.registerCommand("notif-filter", new DebugModeFilterProvider$registerInvalidationListener$1(this));
                Log.d(TAG, "Registered notif-filter command");
            }
        }
    }

    public final boolean shouldFilterOut(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        if (this.allowedPackages.isEmpty()) {
            return false;
        }
        return !this.allowedPackages.contains(notificationEntry.getSbn().getPackageName());
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("initialized: " + ListenerSetKt.isNotEmpty(this.listeners));
        printWriter.println("allowedPackages: " + this.allowedPackages.size());
        int i = 0;
        for (Object next : this.allowedPackages) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            printWriter.println("  [" + i + "]: " + ((String) next));
            i = i2;
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: DebugModeFilterProvider.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0018\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\tH\u0002¨\u0006\r"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider$NotifFilterCommand;", "Lcom/android/systemui/statusbar/commandline/Command;", "(Lcom/android/systemui/statusbar/notification/collection/provider/DebugModeFilterProvider;)V", "execute", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "help", "invalidCommand", "reason", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: DebugModeFilterProvider.kt */
    public final class NotifFilterCommand implements Command {
        public NotifFilterCommand() {
        }

        public void execute(PrintWriter printWriter, List<String> list) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            Intrinsics.checkNotNullParameter(list, "args");
            String str = (String) CollectionsKt.firstOrNull(list);
            if (Intrinsics.areEqual((Object) str, (Object) "reset")) {
                if (list.size() > 1) {
                    invalidCommand(printWriter, "Unexpected arguments for 'reset' command");
                    return;
                }
                DebugModeFilterProvider.this.allowedPackages = CollectionsKt.emptyList();
            } else if (Intrinsics.areEqual((Object) str, (Object) "allowed-pkgs")) {
                DebugModeFilterProvider.this.allowedPackages = CollectionsKt.drop(list, 1);
            } else if (str == null) {
                invalidCommand(printWriter, "Missing command");
                return;
            } else {
                invalidCommand(printWriter, "Unknown command: " + ((String) CollectionsKt.firstOrNull(list)));
                return;
            }
            Log.d(DebugModeFilterProvider.TAG, "Updated allowedPackages: " + DebugModeFilterProvider.this.allowedPackages);
            if (DebugModeFilterProvider.this.allowedPackages.isEmpty()) {
                printWriter.print("Resetting allowedPackages ... ");
            } else {
                printWriter.print("Updating allowedPackages: " + DebugModeFilterProvider.this.allowedPackages + " ... ");
            }
            for (Runnable run : DebugModeFilterProvider.this.listeners) {
                run.run();
            }
            printWriter.println("DONE");
        }

        private final void invalidCommand(PrintWriter printWriter, String str) {
            printWriter.println("Error: " + str);
            printWriter.println();
            help(printWriter);
        }

        public void help(PrintWriter printWriter) {
            Intrinsics.checkNotNullParameter(printWriter, "pw");
            printWriter.println("Usage: adb shell cmd statusbar notif-filter <command>");
            printWriter.println("Available commands:");
            printWriter.println("  reset");
            printWriter.println("     Restore the default system behavior.");
            printWriter.println("  allowed-pkgs <package> ...");
            printWriter.println("     Hide all notification except from packages listed here.");
            printWriter.println("     Providing no packages is treated as a reset.");
        }
    }
}
