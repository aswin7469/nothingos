package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.internal.logging.UiEventLogger;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: OngoingCallLogger.kt */
/* loaded from: classes.dex */
public final class OngoingCallLogger {
    private boolean chipIsVisible;
    @NotNull
    private final UiEventLogger logger;

    public OngoingCallLogger(@NotNull UiEventLogger logger) {
        Intrinsics.checkNotNullParameter(logger, "logger");
        this.logger = logger;
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

    /* compiled from: OngoingCallLogger.kt */
    /* loaded from: classes.dex */
    public enum OngoingCallEvents implements UiEventLogger.UiEventEnum {
        ONGOING_CALL_VISIBLE(813),
        ONGOING_CALL_CLICKED(814);
        
        private final int metricId;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static OngoingCallEvents[] valuesCustom() {
            OngoingCallEvents[] valuesCustom = values();
            OngoingCallEvents[] ongoingCallEventsArr = new OngoingCallEvents[valuesCustom.length];
            System.arraycopy(valuesCustom, 0, ongoingCallEventsArr, 0, valuesCustom.length);
            return ongoingCallEventsArr;
        }

        OngoingCallEvents(int i) {
            this.metricId = i;
        }

        public int getId() {
            return this.metricId;
        }
    }
}
