package com.android.systemui.controls;

import com.android.internal.logging.InstanceIdSequence;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.shared.system.SysUiStatsLog;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0007\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016J(\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/controls/ControlsMetricsLoggerImpl;", "Lcom/android/systemui/controls/ControlsMetricsLogger;", "()V", "instanceId", "", "instanceIdSequence", "Lcom/android/internal/logging/InstanceIdSequence;", "assignInstanceId", "", "log", "eventId", "deviceType", "uid", "isLocked", "", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsMetricsLoggerImpl.kt */
public final class ControlsMetricsLoggerImpl implements ControlsMetricsLogger {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int INSTANCE_ID_MAX = 8192;
    private int instanceId;
    private final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(8192);

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/controls/ControlsMetricsLoggerImpl$Companion;", "", "()V", "INSTANCE_ID_MAX", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsMetricsLoggerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public void assignInstanceId() {
        this.instanceId = this.instanceIdSequence.newInstanceId().getId();
    }

    public void log(int i, int i2, int i3, boolean z) {
        SysUiStatsLog.write((int) SysUiStatsLog.DEVICE_CONTROL_CHANGED, i, this.instanceId, i2, i3, z);
    }
}
