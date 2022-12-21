package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001\nB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallLogger;", "", "logger", "Lcom/android/internal/logging/UiEventLogger;", "(Lcom/android/internal/logging/UiEventLogger;)V", "chipIsVisible", "", "logChipClicked", "", "logChipVisibilityChanged", "OngoingCallEvents", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: OngoingCallLogger.kt */
public final class OngoingCallLogger {
    private boolean chipIsVisible;
    private final UiEventLogger logger;

    @Inject
    public OngoingCallLogger(UiEventLogger uiEventLogger) {
        Intrinsics.checkNotNullParameter(uiEventLogger, "logger");
        this.logger = uiEventLogger;
    }

    public final void logChipClicked() {
        this.logger.log(OngoingCallEvents.ONGOING_CALL_CLICKED);
    }

    public final void logChipVisibilityChanged(boolean z) {
        if (z && z != this.chipIsVisible) {
            this.logger.log(OngoingCallEvents.ONGOING_CALL_VISIBLE);
        }
        this.chipIsVisible = z;
    }

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\u0004H\u0016R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007j\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/ongoingcall/OngoingCallLogger$OngoingCallEvents;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "metricId", "", "(Ljava/lang/String;II)V", "getMetricId", "()I", "getId", "ONGOING_CALL_VISIBLE", "ONGOING_CALL_CLICKED", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: OngoingCallLogger.kt */
    public enum OngoingCallEvents implements UiEventLogger.UiEventEnum {
        ONGOING_CALL_VISIBLE(813),
        ONGOING_CALL_CLICKED(814);
        
        private final int metricId;

        private OngoingCallEvents(int i) {
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
