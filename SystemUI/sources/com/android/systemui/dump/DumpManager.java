package com.android.systemui.dump;

import android.util.ArrayMap;
import com.android.systemui.Dumpable;
import com.android.systemui.log.LogBuffer;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: DumpManager.kt */
/* loaded from: classes.dex */
public class DumpManager {
    @NotNull
    private final Map<String, RegisteredDumpable<Dumpable>> dumpables = new ArrayMap();
    @NotNull
    private final Map<String, RegisteredDumpable<LogBuffer>> buffers = new ArrayMap();

    public final synchronized void registerDumpable(@NotNull String name, @NotNull Dumpable module) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(module, "module");
        if (!canAssignToNameLocked(name, module)) {
            throw new IllegalArgumentException('\'' + name + "' is already registered");
        }
        this.dumpables.put(name, new RegisteredDumpable<>(name, module));
    }

    public final synchronized void unregisterDumpable(@NotNull String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.dumpables.remove(name);
    }

    public final synchronized void registerBuffer(@NotNull String name, @NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        if (!canAssignToNameLocked(name, buffer)) {
            throw new IllegalArgumentException('\'' + name + "' is already registered");
        }
        this.buffers.put(name, new RegisteredDumpable<>(name, buffer));
    }

    public final synchronized void dumpTarget(@NotNull String target, @NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args, int i) {
        Intrinsics.checkNotNullParameter(target, "target");
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        for (RegisteredDumpable<Dumpable> registeredDumpable : this.dumpables.values()) {
            if (StringsKt.endsWith$default(registeredDumpable.getName(), target, false, 2, null)) {
                dumpDumpable(registeredDumpable, fd, pw, args);
                return;
            }
        }
        for (RegisteredDumpable<LogBuffer> registeredDumpable2 : this.buffers.values()) {
            if (StringsKt.endsWith$default(registeredDumpable2.getName(), target, false, 2, null)) {
                dumpBuffer(registeredDumpable2, pw, i);
                return;
            }
        }
    }

    public final synchronized void dumpDumpables(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        for (RegisteredDumpable<Dumpable> registeredDumpable : this.dumpables.values()) {
            dumpDumpable(registeredDumpable, fd, pw, args);
        }
    }

    public final synchronized void listDumpables(@NotNull PrintWriter pw) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        for (RegisteredDumpable<Dumpable> registeredDumpable : this.dumpables.values()) {
            pw.println(registeredDumpable.getName());
        }
    }

    public final synchronized void dumpBuffers(@NotNull PrintWriter pw, int i) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            dumpBuffer(registeredDumpable, pw, i);
        }
    }

    public final synchronized void listBuffers(@NotNull PrintWriter pw) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            pw.println(registeredDumpable.getName());
        }
    }

    public final synchronized void freezeBuffers() {
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            registeredDumpable.getDumpable().freeze();
        }
    }

    public final synchronized void unfreezeBuffers() {
        for (RegisteredDumpable<LogBuffer> registeredDumpable : this.buffers.values()) {
            registeredDumpable.getDumpable().unfreeze();
        }
    }

    private final void dumpDumpable(RegisteredDumpable<Dumpable> registeredDumpable, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println(Intrinsics.stringPlus(registeredDumpable.getName(), ":"));
        printWriter.println("----------------------------------------------------------------------------");
        registeredDumpable.getDumpable().dump(fileDescriptor, printWriter, strArr);
    }

    private final void dumpBuffer(RegisteredDumpable<LogBuffer> registeredDumpable, PrintWriter printWriter, int i) {
        printWriter.println();
        printWriter.println();
        printWriter.println("BUFFER " + registeredDumpable.getName() + ':');
        printWriter.println("============================================================================");
        registeredDumpable.getDumpable().dump(printWriter, i);
    }

    private final boolean canAssignToNameLocked(String str, Object obj) {
        LogBuffer dumpable;
        RegisteredDumpable<Dumpable> registeredDumpable = this.dumpables.get(str);
        if (registeredDumpable == null) {
            RegisteredDumpable<LogBuffer> registeredDumpable2 = this.buffers.get(str);
            dumpable = registeredDumpable2 == null ? null : registeredDumpable2.getDumpable();
        } else {
            dumpable = registeredDumpable.getDumpable();
        }
        return dumpable == null || Intrinsics.areEqual(obj, dumpable);
    }
}
