package com.android.systemui;

import com.android.systemui.BootCompleteCache;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u0000 \u001a2\u00020\u00012\u00020\u0002:\u0001\u001aB\u000f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000bH\u0016J%\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u000e\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0014H\u0016¢\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\rH\u0016J\u0010\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\u000bH\u0016J\u0006\u0010\u0019\u001a\u00020\u0010R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\t8\u0002X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/BootCompleteCacheImpl;", "Lcom/android/systemui/BootCompleteCache;", "Lcom/android/systemui/Dumpable;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/dump/DumpManager;)V", "bootComplete", "Ljava/util/concurrent/atomic/AtomicBoolean;", "listeners", "", "Ljava/lang/ref/WeakReference;", "Lcom/android/systemui/BootCompleteCache$BootCompleteListener;", "addListener", "", "listener", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isBootComplete", "removeListener", "setBootComplete", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BootCompleteCacheImpl.kt */
public final class BootCompleteCacheImpl implements BootCompleteCache, Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEBUG = false;
    private static final String TAG = "BootCompleteCacheImpl";
    private final AtomicBoolean bootComplete = new AtomicBoolean(false);
    private final List<WeakReference<BootCompleteCache.BootCompleteListener>> listeners = new ArrayList();

    @Inject
    public BootCompleteCacheImpl(DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        dumpManager.registerDumpable(TAG, this);
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/BootCompleteCacheImpl$Companion;", "", "()V", "DEBUG", "", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: BootCompleteCacheImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public boolean isBootComplete() {
        return this.bootComplete.get();
    }

    public final void setBootComplete() {
        if (this.bootComplete.compareAndSet(false, true)) {
            synchronized (this.listeners) {
                for (WeakReference weakReference : this.listeners) {
                    BootCompleteCache.BootCompleteListener bootCompleteListener = (BootCompleteCache.BootCompleteListener) weakReference.get();
                    if (bootCompleteListener != null) {
                        bootCompleteListener.onBootComplete();
                    }
                }
                this.listeners.clear();
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    public boolean addListener(BootCompleteCache.BootCompleteListener bootCompleteListener) {
        Intrinsics.checkNotNullParameter(bootCompleteListener, "listener");
        if (this.bootComplete.get()) {
            return true;
        }
        synchronized (this.listeners) {
            if (this.bootComplete.get()) {
                return true;
            }
            this.listeners.add(new WeakReference(bootCompleteListener));
            return false;
        }
    }

    public void removeListener(BootCompleteCache.BootCompleteListener bootCompleteListener) {
        Intrinsics.checkNotNullParameter(bootCompleteListener, "listener");
        if (!this.bootComplete.get()) {
            synchronized (this.listeners) {
                this.listeners.removeIf(new BootCompleteCacheImpl$$ExternalSyntheticLambda0(bootCompleteListener));
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: removeListener$lambda-4$lambda-3  reason: not valid java name */
    public static final boolean m2513removeListener$lambda4$lambda3(BootCompleteCache.BootCompleteListener bootCompleteListener, WeakReference weakReference) {
        Intrinsics.checkNotNullParameter(bootCompleteListener, "$listener");
        Intrinsics.checkNotNullParameter(weakReference, "it");
        return weakReference.get() == null || weakReference.get() == bootCompleteListener;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("BootCompleteCache state:");
        printWriter.println("  boot complete: " + isBootComplete());
        if (!isBootComplete()) {
            printWriter.println("  listeners:");
            synchronized (this.listeners) {
                for (WeakReference weakReference : this.listeners) {
                    printWriter.println("    " + weakReference);
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
