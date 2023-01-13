package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ2\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\b2\b\u0010\u000f\u001a\u0004\u0018\u00010\bJ\u0016\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u001e\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewDifferLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logAttachingChild", "", "key", "", "parent", "logDetachingChild", "isTransfer", "", "isParentRemoved", "oldParent", "newParent", "logDuplicateNodeInTree", "node", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "ex", "Ljava/lang/RuntimeException;", "logMovingChild", "toIndex", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeViewDifferLogger.kt */
public final class ShadeViewDifferLogger {
    private final LogBuffer buffer;

    @Inject
    public ShadeViewDifferLogger(@NotificationLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logDetachingChild(String str, boolean z, boolean z2, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifViewManager", LogLevel.DEBUG, ShadeViewDifferLogger$logDetachingChild$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logAttachingChild(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "parent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifViewManager", LogLevel.DEBUG, ShadeViewDifferLogger$logAttachingChild$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logMovingChild(String str, String str2, int i) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(str2, "parent");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifViewManager", LogLevel.DEBUG, ShadeViewDifferLogger$logMovingChild$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logDuplicateNodeInTree(NodeSpec nodeSpec, RuntimeException runtimeException) {
        Intrinsics.checkNotNullParameter(nodeSpec, "node");
        Intrinsics.checkNotNullParameter(runtimeException, "ex");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("NotifViewManager", LogLevel.ERROR, ShadeViewDifferLogger$logDuplicateNodeInTree$2.INSTANCE);
        obtain.setStr1(runtimeException.toString());
        obtain.setStr2(NodeControllerKt.treeSpecToStr(nodeSpec));
        logBuffer.commit(obtain);
    }
}
