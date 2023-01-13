package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.util.IndentingPrintWriter;
import android.util.SparseSetArray;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fJ%\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u000f2\u000e\u0010\u0010\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00120\u0011H\u0016¢\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\u0016\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068\u0002X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, mo65043d2 = {"Lcom/android/systemui/broadcast/PendingRemovalStore;", "Lcom/android/systemui/Dumpable;", "logger", "Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;", "(Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;)V", "pendingRemoval", "Landroid/util/SparseSetArray;", "Landroid/content/BroadcastReceiver;", "clearPendingRemoval", "", "broadcastReceiver", "userId", "", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isPendingRemoval", "", "tagForRemoval", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PendingRemovalStore.kt */
public final class PendingRemovalStore implements Dumpable {
    private final BroadcastDispatcherLogger logger;
    private final SparseSetArray<BroadcastReceiver> pendingRemoval = new SparseSetArray<>();

    @Inject
    public PendingRemovalStore(BroadcastDispatcherLogger broadcastDispatcherLogger) {
        Intrinsics.checkNotNullParameter(broadcastDispatcherLogger, "logger");
        this.logger = broadcastDispatcherLogger;
    }

    public final void tagForRemoval(BroadcastReceiver broadcastReceiver, int i) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "broadcastReceiver");
        this.logger.logTagForRemoval(i, broadcastReceiver);
        synchronized (this.pendingRemoval) {
            this.pendingRemoval.add(i, broadcastReceiver);
        }
    }

    public final boolean isPendingRemoval(BroadcastReceiver broadcastReceiver, int i) {
        boolean z;
        Intrinsics.checkNotNullParameter(broadcastReceiver, "broadcastReceiver");
        synchronized (this.pendingRemoval) {
            z = this.pendingRemoval.contains(i, broadcastReceiver) || this.pendingRemoval.contains(-1, broadcastReceiver);
        }
        return z;
    }

    public final void clearPendingRemoval(BroadcastReceiver broadcastReceiver, int i) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "broadcastReceiver");
        synchronized (this.pendingRemoval) {
            this.pendingRemoval.remove(i, broadcastReceiver);
        }
        this.logger.logClearedAfterRemoval(i, broadcastReceiver);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        synchronized (this.pendingRemoval) {
            if (printWriter instanceof IndentingPrintWriter) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            int size = this.pendingRemoval.size();
            for (int i = 0; i < size; i++) {
                int keyAt = this.pendingRemoval.keyAt(i);
                printWriter.print(keyAt);
                printWriter.print("->");
                printWriter.println((Object) this.pendingRemoval.get(keyAt));
            }
            if (printWriter instanceof IndentingPrintWriter) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            Unit unit = Unit.INSTANCE;
        }
    }
}
