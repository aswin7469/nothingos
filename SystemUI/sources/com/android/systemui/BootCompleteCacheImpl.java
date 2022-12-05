package com.android.systemui;

import com.android.internal.annotations.GuardedBy;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: BootCompleteCacheImpl.kt */
/* loaded from: classes.dex */
public final class BootCompleteCacheImpl implements BootCompleteCache, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @GuardedBy({"listeners"})
    @NotNull
    private final List<WeakReference<BootCompleteCache.BootCompleteListener>> listeners = new ArrayList();
    @NotNull
    private final AtomicBoolean bootComplete = new AtomicBoolean(false);

    public BootCompleteCacheImpl(@NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        dumpManager.registerDumpable("BootCompleteCacheImpl", this);
    }

    /* compiled from: BootCompleteCacheImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.BootCompleteCache
    public boolean isBootComplete() {
        return this.bootComplete.get();
    }

    public final void setBootComplete() {
        if (this.bootComplete.compareAndSet(false, true)) {
            synchronized (this.listeners) {
                Iterator<T> it = this.listeners.iterator();
                while (it.hasNext()) {
                    BootCompleteCache.BootCompleteListener bootCompleteListener = (BootCompleteCache.BootCompleteListener) ((WeakReference) it.next()).get();
                    if (bootCompleteListener != null) {
                        bootCompleteListener.onBootComplete();
                    }
                }
                this.listeners.clear();
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    @Override // com.android.systemui.BootCompleteCache
    public boolean addListener(@NotNull BootCompleteCache.BootCompleteListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        if (this.bootComplete.get()) {
            return true;
        }
        synchronized (this.listeners) {
            if (this.bootComplete.get()) {
                return true;
            }
            this.listeners.add(new WeakReference<>(listener));
            return false;
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("BootCompleteCache state:");
        pw.println(Intrinsics.stringPlus("  boot complete: ", Boolean.valueOf(isBootComplete())));
        if (!isBootComplete()) {
            pw.println("  listeners:");
            synchronized (this.listeners) {
                Iterator<T> it = this.listeners.iterator();
                while (it.hasNext()) {
                    pw.println(Intrinsics.stringPlus("    ", (WeakReference) it.next()));
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
