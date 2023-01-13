package com.android.systemui.dump;

import android.util.ArrayMap;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.Dumpable;
import com.android.systemui.log.LogBuffer;
import java.p026io.PrintWriter;
import java.util.Map;
import javax.inject.Singleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Singleton
@Metadata(mo65042d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u000f\b\u0017\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u0001H\u0002J&\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0016\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J1\u0010\u0016\u001a\u00020\u000f2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\t0\u00062\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0019H\u0002¢\u0006\u0002\u0010\u001aJ!\u0010\u001b\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0019¢\u0006\u0002\u0010\u001cJ1\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u00192\u0006\u0010\u0013\u001a\u00020\u0014¢\u0006\u0002\u0010\u001fJ\u0006\u0010 \u001a\u00020\u000fJ\u000e\u0010!\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\"\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010#\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0007J\u000e\u0010$\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\tJ\u0016\u0010$\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\tJ\u0006\u0010&\u001a\u00020\u000fJ\u000e\u0010'\u001a\u00020\u000f2\u0006\u0010\f\u001a\u00020\u0005R \u0010\u0003\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0004X\u0004¢\u0006\u0002\n\u0000R \u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00060\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006("}, mo65043d2 = {"Lcom/android/systemui/dump/DumpManager;", "", "()V", "buffers", "", "", "Lcom/android/systemui/dump/RegisteredDumpable;", "Lcom/android/systemui/log/LogBuffer;", "dumpables", "Lcom/android/systemui/Dumpable;", "canAssignToNameLocked", "", "name", "newDumpable", "dumpBuffer", "", "buffer", "pw", "Ljava/io/PrintWriter;", "tailLength", "", "dumpBuffers", "dumpDumpable", "dumpable", "args", "", "(Lcom/android/systemui/dump/RegisteredDumpable;Ljava/io/PrintWriter;[Ljava/lang/String;)V", "dumpDumpables", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "dumpTarget", "target", "(Ljava/lang/String;Ljava/io/PrintWriter;[Ljava/lang/String;I)V", "freezeBuffers", "listBuffers", "listDumpables", "registerBuffer", "registerDumpable", "module", "unfreezeBuffers", "unregisterDumpable", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpManager.kt */
public class DumpManager {
    private final Map<String, RegisteredDumpable<LogBuffer>> buffers = new ArrayMap();
    private final Map<String, RegisteredDumpable<Dumpable>> dumpables = new ArrayMap();

    public final synchronized void registerDumpable(String str, Dumpable dumpable) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(dumpable, "module");
        if (canAssignToNameLocked(str, dumpable)) {
            this.dumpables.put(str, new RegisteredDumpable(str, dumpable));
        } else {
            throw new IllegalArgumentException("'" + str + "' is already registered");
        }
    }

    public final synchronized void registerDumpable(Dumpable dumpable) {
        Intrinsics.checkNotNullParameter(dumpable, "module");
        String simpleName = dumpable.getClass().getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "module::class.java.simpleName");
        registerDumpable(simpleName, dumpable);
    }

    public final synchronized void unregisterDumpable(String str) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        this.dumpables.remove(str);
    }

    public final synchronized void registerBuffer(String str, LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        if (canAssignToNameLocked(str, logBuffer)) {
            this.buffers.put(str, new RegisteredDumpable(str, logBuffer));
        } else {
            throw new IllegalArgumentException("'" + str + "' is already registered");
        }
    }

    public final synchronized void dumpTarget(String str, PrintWriter printWriter, String[] strArr, int i) {
        Intrinsics.checkNotNullParameter(str, "target");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        for (RegisteredDumpable next : this.dumpables.values()) {
            if (StringsKt.endsWith$default(next.getName(), str, false, 2, (Object) null)) {
                dumpDumpable(next, printWriter, strArr);
                return;
            }
        }
        for (RegisteredDumpable next2 : this.buffers.values()) {
            if (StringsKt.endsWith$default(next2.getName(), str, false, 2, (Object) null)) {
                dumpBuffer(next2, printWriter, i);
                return;
            }
        }
    }

    public final synchronized void dumpDumpables(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        for (RegisteredDumpable<Dumpable> dumpDumpable : this.dumpables.values()) {
            dumpDumpable(dumpDumpable, printWriter, strArr);
        }
    }

    public final synchronized void listDumpables(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        for (RegisteredDumpable<Dumpable> name : this.dumpables.values()) {
            printWriter.println(name.getName());
        }
    }

    public final synchronized void dumpBuffers(PrintWriter printWriter, int i) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        for (RegisteredDumpable<LogBuffer> dumpBuffer : this.buffers.values()) {
            dumpBuffer(dumpBuffer, printWriter, i);
        }
    }

    public final synchronized void listBuffers(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        for (RegisteredDumpable<LogBuffer> name : this.buffers.values()) {
            printWriter.println(name.getName());
        }
    }

    public final synchronized void freezeBuffers() {
        for (RegisteredDumpable<LogBuffer> dumpable : this.buffers.values()) {
            ((LogBuffer) dumpable.getDumpable()).freeze();
        }
    }

    public final synchronized void unfreezeBuffers() {
        for (RegisteredDumpable<LogBuffer> dumpable : this.buffers.values()) {
            ((LogBuffer) dumpable.getDumpable()).unfreeze();
        }
    }

    private final void dumpDumpable(RegisteredDumpable<Dumpable> registeredDumpable, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println(registeredDumpable.getName() + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        printWriter.println("----------------------------------------------------------------------------");
        registeredDumpable.getDumpable().dump(printWriter, strArr);
    }

    private final void dumpBuffer(RegisteredDumpable<LogBuffer> registeredDumpable, PrintWriter printWriter, int i) {
        printWriter.println();
        printWriter.println();
        printWriter.println("BUFFER " + registeredDumpable.getName() + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        printWriter.println("============================================================================");
        registeredDumpable.getDumpable().dump(printWriter, i);
    }

    private final boolean canAssignToNameLocked(String str, Object obj) {
        Object obj2;
        RegisteredDumpable registeredDumpable = this.dumpables.get(str);
        if (registeredDumpable == null || (obj2 = (Dumpable) registeredDumpable.getDumpable()) == null) {
            RegisteredDumpable registeredDumpable2 = this.buffers.get(str);
            obj2 = registeredDumpable2 != null ? registeredDumpable2.getDumpable() : null;
        }
        return obj2 == null || Intrinsics.areEqual(obj, obj2);
    }
}
