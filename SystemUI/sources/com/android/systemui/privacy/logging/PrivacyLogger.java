package com.android.systemui.privacy.logging;

import android.permission.PermGroupUsage;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyItem;
import java.text.SimpleDateFormat;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger {
    @NotNull
    private final LogBuffer buffer;

    public PrivacyLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logUpdatedItemFromAppOps(int i, int i2, @NotNull String packageName, boolean z) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logUpdatedItemFromAppOps$2 privacyLogger$logUpdatedItemFromAppOps$2 = PrivacyLogger$logUpdatedItemFromAppOps$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUpdatedItemFromAppOps$2);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            obtain.setStr1(packageName);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logRetrievedPrivacyItemsList(@NotNull List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logRetrievedPrivacyItemsList$2 privacyLogger$logRetrievedPrivacyItemsList$2 = PrivacyLogger$logRetrievedPrivacyItemsList$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logRetrievedPrivacyItemsList$2);
            obtain.setStr1(listToString(list));
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyItemsToHold(@NotNull List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logPrivacyItemsToHold$2 privacyLogger$logPrivacyItemsToHold$2 = PrivacyLogger$logPrivacyItemsToHold$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsToHold$2);
            obtain.setStr1(listToString(list));
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyItemsUpdateScheduled(long j) {
        SimpleDateFormat simpleDateFormat;
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyItemsUpdateScheduled$2 privacyLogger$logPrivacyItemsUpdateScheduled$2 = PrivacyLogger$logPrivacyItemsUpdateScheduled$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsUpdateScheduled$2);
            long currentTimeMillis = System.currentTimeMillis() + j;
            simpleDateFormat = PrivacyLoggerKt.DATE_FORMAT;
            obtain.setStr1(simpleDateFormat.format(Long.valueOf(currentTimeMillis)));
            logBuffer.push(obtain);
        }
    }

    public final void logCurrentProfilesChanged(@NotNull List<Integer> profiles) {
        Intrinsics.checkNotNullParameter(profiles, "profiles");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logCurrentProfilesChanged$2 privacyLogger$logCurrentProfilesChanged$2 = PrivacyLogger$logCurrentProfilesChanged$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logCurrentProfilesChanged$2);
            obtain.setStr1(profiles.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logChipVisible(boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logChipVisible$2 privacyLogger$logChipVisible$2 = PrivacyLogger$logChipVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logChipVisible$2);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }

    public final void logStatusBarIconsVisible(boolean z, boolean z2, boolean z3) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStatusBarIconsVisible$2 privacyLogger$logStatusBarIconsVisible$2 = PrivacyLogger$logStatusBarIconsVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStatusBarIconsVisible$2);
            obtain.setBool1(z);
            obtain.setBool2(z2);
            obtain.setBool3(z3);
            logBuffer.push(obtain);
        }
    }

    public final void logUnfilteredPermGroupUsage(@NotNull List<PermGroupUsage> contents) {
        Intrinsics.checkNotNullParameter(contents, "contents");
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logUnfilteredPermGroupUsage$2 privacyLogger$logUnfilteredPermGroupUsage$2 = PrivacyLogger$logUnfilteredPermGroupUsage$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUnfilteredPermGroupUsage$2);
            obtain.setStr1(contents.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logShowDialogContents(@NotNull List<PrivacyDialog.PrivacyElement> contents) {
        Intrinsics.checkNotNullParameter(contents, "contents");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logShowDialogContents$2 privacyLogger$logShowDialogContents$2 = PrivacyLogger$logShowDialogContents$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logShowDialogContents$2);
            obtain.setStr1(contents.toString());
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyDialogDismissed() {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyDialogDismissed$2 privacyLogger$logPrivacyDialogDismissed$2 = PrivacyLogger$logPrivacyDialogDismissed$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            logBuffer.push(logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyDialogDismissed$2));
        }
    }

    public final void logStartSettingsActivityFromDialog(@NotNull String packageName, int i) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStartSettingsActivityFromDialog$2 privacyLogger$logStartSettingsActivityFromDialog$2 = PrivacyLogger$logStartSettingsActivityFromDialog$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStartSettingsActivityFromDialog$2);
            obtain.setStr1(packageName);
            obtain.setInt1(i);
            logBuffer.push(obtain);
        }
    }

    private final String listToString(List<PrivacyItem> list) {
        return CollectionsKt.joinToString$default(list, ", ", null, null, 0, null, PrivacyLogger$listToString$1.INSTANCE, 30, null);
    }
}
