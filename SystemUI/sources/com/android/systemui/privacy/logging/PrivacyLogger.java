package com.android.systemui.privacy.logging;

import android.permission.PermissionGroupUsage;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.log.dagger.PrivacyLog;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyItem;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0002JE\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0017\u0010\u000e\u001a\u0013\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u000b0\u000f¢\u0006\u0002\b\u00112\u0019\b\b\u0010\u0012\u001a\u0013\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00060\u000f¢\u0006\u0002\b\u0011H\bJ\u000e\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u0015J\u0014\u0010\u0016\u001a\u00020\u000b2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\bJ\u0006\u0010\u0019\u001a\u00020\u000bJ\u0006\u0010\u001a\u001a\u00020\u000bJ\u0014\u0010\u001b\u001a\u00020\u000b2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u000e\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u001eJ\u0014\u0010\u001f\u001a\u00020\u000b2\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\u0014\u0010 \u001a\u00020\u000b2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\bJ\u0016\u0010#\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u0018J\u001e\u0010&\u001a\u00020\u000b2\u0006\u0010'\u001a\u00020\u00152\u0006\u0010(\u001a\u00020\u00152\u0006\u0010)\u001a\u00020\u0015J\u0014\u0010*\u001a\u00020\u000b2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020+0\bJ&\u0010,\u001a\u00020\u000b2\u0006\u0010-\u001a\u00020\u00182\u0006\u0010.\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u0015J\u001e\u00100\u001a\u00020\u000b2\u0006\u0010.\u001a\u00020\u00182\u0006\u0010$\u001a\u00020\u00062\u0006\u0010/\u001a\u00020\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u00061"}, mo65043d2 = {"Lcom/android/systemui/privacy/logging/PrivacyLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "listToString", "", "list", "", "Lcom/android/systemui/privacy/PrivacyItem;", "log", "", "logLevel", "Lcom/android/systemui/log/LogLevel;", "initializer", "Lkotlin/Function1;", "Lcom/android/systemui/log/LogMessage;", "Lkotlin/ExtensionFunctionType;", "printer", "logChipVisible", "visible", "", "logCurrentProfilesChanged", "profiles", "", "logEmptyDialog", "logPrivacyDialogDismissed", "logPrivacyItemsToHold", "logPrivacyItemsUpdateScheduled", "delay", "", "logRetrievedPrivacyItemsList", "logShowDialogContents", "contents", "Lcom/android/systemui/privacy/PrivacyDialog$PrivacyElement;", "logStartSettingsActivityFromDialog", "packageName", "userId", "logStatusBarIconsVisible", "showCamera", "showMicrophone", "showLocation", "logUnfilteredPermGroupUsage", "Landroid/permission/PermissionGroupUsage;", "logUpdatedItemFromAppOps", "code", "uid", "active", "logUpdatedItemFromMediaProjection", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyLogger.kt */
public final class PrivacyLogger {
    private final LogBuffer buffer;

    @Inject
    public PrivacyLogger(@PrivacyLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logUpdatedItemFromAppOps(int i, int i2, String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logUpdatedItemFromAppOps$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setInt2(i2);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logUpdatedItemFromMediaProjection(int i, String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logUpdatedItemFromMediaProjection$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logRetrievedPrivacyItemsList(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logRetrievedPrivacyItemsList$2.INSTANCE);
        obtain.setStr1(listToString(list));
        logBuffer.commit(obtain);
    }

    public final void logPrivacyItemsToHold(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "list");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.DEBUG, PrivacyLogger$logPrivacyItemsToHold$2.INSTANCE);
        obtain.setStr1(listToString(list));
        logBuffer.commit(obtain);
    }

    public final void logPrivacyItemsUpdateScheduled(long j) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logPrivacyItemsUpdateScheduled$2.INSTANCE);
        obtain.setStr1(PrivacyLoggerKt.DATE_FORMAT.format(Long.valueOf(System.currentTimeMillis() + j)));
        logBuffer.commit(obtain);
    }

    public final void logCurrentProfilesChanged(List<Integer> list) {
        Intrinsics.checkNotNullParameter(list, "profiles");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logCurrentProfilesChanged$2.INSTANCE);
        obtain.setStr1(list.toString());
        logBuffer.commit(obtain);
    }

    public final void logChipVisible(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logChipVisible$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logStatusBarIconsVisible(boolean z, boolean z2, boolean z3) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logStatusBarIconsVisible$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        obtain.setBool3(z3);
        logBuffer.commit(obtain);
    }

    public final void logUnfilteredPermGroupUsage(List<PermissionGroupUsage> list) {
        Intrinsics.checkNotNullParameter(list, "contents");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.DEBUG, PrivacyLogger$logUnfilteredPermGroupUsage$2.INSTANCE);
        obtain.setStr1(list.toString());
        logBuffer.commit(obtain);
    }

    public final void logShowDialogContents(List<PrivacyDialog.PrivacyElement> list) {
        Intrinsics.checkNotNullParameter(list, "contents");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logShowDialogContents$2.INSTANCE);
        obtain.setStr1(list.toString());
        logBuffer.commit(obtain);
    }

    public final void logEmptyDialog() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("PrivacyLog", LogLevel.WARNING, PrivacyLogger$logEmptyDialog$2.INSTANCE));
    }

    public final void logPrivacyDialogDismissed() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logPrivacyDialogDismissed$2.INSTANCE));
    }

    public final void logStartSettingsActivityFromDialog(String str, int i) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("PrivacyLog", LogLevel.INFO, PrivacyLogger$logStartSettingsActivityFromDialog$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    private final String listToString(List<PrivacyItem> list) {
        return CollectionsKt.joinToString$default(list, ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, PrivacyLogger$listToString$1.INSTANCE, 30, (Object) null);
    }

    private final void log(LogLevel logLevel, Function1<? super LogMessage, Unit> function1, Function1<? super LogMessage, String> function12) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, function12);
        function1.invoke(obtain);
        logBuffer.commit(obtain);
    }
}
