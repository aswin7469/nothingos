package com.android.systemui.controls;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.controls.p010ui.ControlViewHolder;
import com.nothing.p023os.device.DeviceConstant;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\bf\u0018\u00002\u00020\u0001:\u0001\u0012J\b\u0010\u0002\u001a\u00020\u0003H&J\u0018\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J(\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH&J\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\u000f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0018\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0013À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/controls/ControlsMetricsLogger;", "", "assignInstanceId", "", "drag", "cvh", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "isLocked", "", "log", "eventId", "", "deviceType", "uid", "longPress", "refreshBegin", "refreshEnd", "touch", "ControlsEvents", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsMetricsLogger.kt */
public interface ControlsMetricsLogger {
    void assignInstanceId();

    void log(int i, int i2, int i3, boolean z);

    void touch(ControlViewHolder controlViewHolder, boolean z) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        log(ControlsEvents.CONTROL_TOUCH.getId(), controlViewHolder.getDeviceType(), controlViewHolder.getUid(), z);
    }

    void drag(ControlViewHolder controlViewHolder, boolean z) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        log(ControlsEvents.CONTROL_DRAG.getId(), controlViewHolder.getDeviceType(), controlViewHolder.getUid(), z);
    }

    void longPress(ControlViewHolder controlViewHolder, boolean z) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        log(ControlsEvents.CONTROL_LONG_PRESS.getId(), controlViewHolder.getDeviceType(), controlViewHolder.getUid(), z);
    }

    void refreshBegin(int i, boolean z) {
        assignInstanceId();
        log(ControlsEvents.CONTROL_REFRESH_BEGIN.getId(), 0, i, z);
    }

    void refreshEnd(ControlViewHolder controlViewHolder, boolean z) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        log(ControlsEvents.CONTROL_REFRESH_END.getId(), controlViewHolder.getDeviceType(), controlViewHolder.getUid(), z);
    }

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\u0004H\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007j\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/controls/ControlsMetricsLogger$ControlsEvents;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "metricId", "", "(Ljava/lang/String;II)V", "getMetricId", "()I", "getId", "CONTROL_TOUCH", "CONTROL_DRAG", "CONTROL_LONG_PRESS", "CONTROL_REFRESH_BEGIN", "CONTROL_REFRESH_END", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsMetricsLogger.kt */
    private enum ControlsEvents implements UiEventLogger.UiEventEnum {
        CONTROL_TOUCH(714),
        CONTROL_DRAG(713),
        CONTROL_LONG_PRESS(DeviceConstant.ORDER_LOW_LATENCY),
        CONTROL_REFRESH_BEGIN(716),
        CONTROL_REFRESH_END(717);
        
        private final int metricId;

        private ControlsEvents(int i) {
            this.metricId = i;
        }

        public final int getMetricId() {
            return this.metricId;
        }

        public int getId() {
            return this.metricId;
        }
    }
}
