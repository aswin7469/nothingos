package com.android.systemui.statusbar.phone.fragment;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.CollapsedSbFragmentLog;
import com.android.systemui.statusbar.DisableFlagsLogger;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/fragment/CollapsedStatusBarFragmentLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "disableFlagsLogger", "Lcom/android/systemui/statusbar/DisableFlagsLogger;", "(Lcom/android/systemui/log/LogBuffer;Lcom/android/systemui/statusbar/DisableFlagsLogger;)V", "logDisableFlagChange", "", "new", "Lcom/android/systemui/statusbar/DisableFlagsLogger$DisableState;", "newAfterLocalModification", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: CollapsedStatusBarFragmentLogger.kt */
public final class CollapsedStatusBarFragmentLogger {
    private final LogBuffer buffer;
    /* access modifiers changed from: private */
    public final DisableFlagsLogger disableFlagsLogger;

    @Inject
    public CollapsedStatusBarFragmentLogger(@CollapsedSbFragmentLog LogBuffer logBuffer, DisableFlagsLogger disableFlagsLogger2) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        Intrinsics.checkNotNullParameter(disableFlagsLogger2, "disableFlagsLogger");
        this.buffer = logBuffer;
        this.disableFlagsLogger = disableFlagsLogger2;
    }

    public final void logDisableFlagChange(DisableFlagsLogger.DisableState disableState, DisableFlagsLogger.DisableState disableState2) {
        Intrinsics.checkNotNullParameter(disableState, "new");
        Intrinsics.checkNotNullParameter(disableState2, "newAfterLocalModification");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("CollapsedSbFragment", LogLevel.INFO, new CollapsedStatusBarFragmentLogger$logDisableFlagChange$2(this));
        obtain.setInt1(disableState.getDisable1());
        obtain.setInt2(disableState.getDisable2());
        obtain.setLong1((long) disableState2.getDisable1());
        obtain.setLong2((long) disableState2.getDisable2());
        logBuffer.commit(obtain);
    }
}