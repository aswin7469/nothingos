package com.android.systemui.p012qs.logging;

import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.log.dagger.QSLog;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.statusbar.StatusBarState;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004JE\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0017\u0010\t\u001a\u0013\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00060\n¢\u0006\u0002\b\f2\u0019\b\b\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000e0\n¢\u0006\u0002\b\fH\bJ\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u000eJ\u0016\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000eJ\u000e\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u000eJ\u0016\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011J\u001e\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bJ\u0016\u0010\u001d\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u000eJ\u001e\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bJ\u001e\u0010 \u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bJ\u0016\u0010!\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\"J\u0010\u0010#\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u001bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, mo65043d2 = {"Lcom/android/systemui/qs/logging/QSLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "log", "", "logLevel", "Lcom/android/systemui/log/LogLevel;", "initializer", "Lkotlin/Function1;", "Lcom/android/systemui/log/LogMessage;", "Lkotlin/ExtensionFunctionType;", "printer", "", "logAllTilesChangeListening", "listening", "", "containerName", "allSpecs", "logPanelExpanded", "expanded", "logTileAdded", "tileSpec", "logTileChangeListening", "logTileClick", "statusBarState", "", "state", "logTileDestroyed", "reason", "logTileLongClick", "logTileSecondaryClick", "logTileUpdated", "Lcom/android/systemui/plugins/qs/QSTile$State;", "toStateString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.logging.QSLogger */
/* compiled from: QSLogger.kt */
public final class QSLogger {
    private final LogBuffer buffer;

    private final String toStateString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "wrong state" : "active" : "inactive" : "unavailable";
    }

    @Inject
    public QSLogger(@QSLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logTileAdded(String str) {
        Intrinsics.checkNotNullParameter(str, "tileSpec");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.DEBUG, QSLogger$logTileAdded$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logTileDestroyed(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "tileSpec");
        Intrinsics.checkNotNullParameter(str2, "reason");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.DEBUG, QSLogger$logTileDestroyed$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logTileChangeListening(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "tileSpec");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.VERBOSE, QSLogger$logTileChangeListening$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logAllTilesChangeListening(boolean z, String str, String str2) {
        Intrinsics.checkNotNullParameter(str, "containerName");
        Intrinsics.checkNotNullParameter(str2, "allSpecs");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.DEBUG, QSLogger$logAllTilesChangeListening$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logTileClick(String str, int i, int i2) {
        Intrinsics.checkNotNullParameter(str, "tileSpec");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.DEBUG, QSLogger$logTileClick$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setStr2(StatusBarState.toString(i));
        obtain.setStr3(toStateString(i2));
        logBuffer.commit(obtain);
    }

    public final void logTileSecondaryClick(String str, int i, int i2) {
        Intrinsics.checkNotNullParameter(str, "tileSpec");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.DEBUG, QSLogger$logTileSecondaryClick$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setStr2(StatusBarState.toString(i));
        obtain.setStr3(toStateString(i2));
        logBuffer.commit(obtain);
    }

    public final void logTileLongClick(String str, int i, int i2) {
        Intrinsics.checkNotNullParameter(str, "tileSpec");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.DEBUG, QSLogger$logTileLongClick$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setStr2(StatusBarState.toString(i));
        obtain.setStr3(toStateString(i2));
        logBuffer.commit(obtain);
    }

    public final void logTileUpdated(String str, QSTile.State state) {
        Intrinsics.checkNotNullParameter(str, "tileSpec");
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.VERBOSE, QSLogger$logTileUpdated$2.INSTANCE);
        obtain.setStr1(str);
        CharSequence charSequence = state.label;
        String str2 = null;
        obtain.setStr2(charSequence != null ? charSequence.toString() : null);
        QSTile.Icon icon = state.icon;
        if (icon != null) {
            str2 = icon.toString();
        }
        obtain.setStr3(str2);
        obtain.setInt1(state.state);
        if (state instanceof QSTile.SignalState) {
            obtain.setBool1(true);
            QSTile.SignalState signalState = (QSTile.SignalState) state;
            obtain.setBool2(signalState.activityIn);
            obtain.setBool3(signalState.activityOut);
        }
        logBuffer.commit(obtain);
    }

    public final void logPanelExpanded(boolean z, String str) {
        Intrinsics.checkNotNullParameter(str, "containerName");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("QSLog", LogLevel.DEBUG, QSLogger$logPanelExpanded$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    private final void log(LogLevel logLevel, Function1<? super LogMessage, Unit> function1, Function1<? super LogMessage, String> function12) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, function12);
        function1.invoke(obtain);
        logBuffer.commit(obtain);
    }
}
