package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ShadeViewDifferLogger.kt */
/* loaded from: classes.dex */
public final class ShadeViewDifferLogger {
    @NotNull
    private final LogBuffer buffer;

    public ShadeViewDifferLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logDetachingChild(@NotNull String key, boolean z, @Nullable String str, @Nullable String str2) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeViewDifferLogger$logDetachingChild$2 shadeViewDifferLogger$logDetachingChild$2 = ShadeViewDifferLogger$logDetachingChild$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logDetachingChild$2);
            obtain.setStr1(key);
            obtain.setBool1(z);
            obtain.setStr2(str);
            obtain.setStr3(str2);
            logBuffer.push(obtain);
        }
    }

    public final void logSkippingDetach(@NotNull String key, @Nullable String str) {
        Intrinsics.checkNotNullParameter(key, "key");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeViewDifferLogger$logSkippingDetach$2 shadeViewDifferLogger$logSkippingDetach$2 = ShadeViewDifferLogger$logSkippingDetach$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logSkippingDetach$2);
            obtain.setStr1(key);
            obtain.setStr2(str);
            logBuffer.push(obtain);
        }
    }

    public final void logAttachingChild(@NotNull String key, @NotNull String parent) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(parent, "parent");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeViewDifferLogger$logAttachingChild$2 shadeViewDifferLogger$logAttachingChild$2 = ShadeViewDifferLogger$logAttachingChild$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logAttachingChild$2);
            obtain.setStr1(key);
            obtain.setStr2(parent);
            logBuffer.push(obtain);
        }
    }

    public final void logMovingChild(@NotNull String key, @NotNull String parent, int i) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(parent, "parent");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeViewDifferLogger$logMovingChild$2 shadeViewDifferLogger$logMovingChild$2 = ShadeViewDifferLogger$logMovingChild$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logMovingChild$2);
            obtain.setStr1(key);
            obtain.setStr2(parent);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    public final void logDuplicateNodeInTree(@NotNull NodeSpec node, @NotNull RuntimeException ex) {
        Intrinsics.checkNotNullParameter(node, "node");
        Intrinsics.checkNotNullParameter(ex, "ex");
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.ERROR;
        ShadeViewDifferLogger$logDuplicateNodeInTree$2 shadeViewDifferLogger$logDuplicateNodeInTree$2 = ShadeViewDifferLogger$logDuplicateNodeInTree$2.INSTANCE;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", logLevel, shadeViewDifferLogger$logDuplicateNodeInTree$2);
            obtain.setStr1(ex.toString());
            obtain.setStr2(NodeControllerKt.treeSpecToStr(node));
            logBuffer.push(obtain);
        }
    }
}
