package com.android.systemui.assist;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\b\u0001\u0018\u0000 \u00122\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002:\u0001\u0012B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/assist/AssistantInvocationEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "id", "", "(Ljava/lang/String;II)V", "getId", "ASSISTANT_INVOCATION_UNKNOWN", "ASSISTANT_INVOCATION_TOUCH_GESTURE", "ASSISTANT_INVOCATION_TOUCH_GESTURE_ALT", "ASSISTANT_INVOCATION_HOTWORD", "ASSISTANT_INVOCATION_QUICK_SEARCH_BAR", "ASSISTANT_INVOCATION_HOME_LONG_PRESS", "ASSISTANT_INVOCATION_PHYSICAL_GESTURE", "ASSISTANT_INVOCATION_START_UNKNOWN", "ASSISTANT_INVOCATION_START_TOUCH_GESTURE", "ASSISTANT_INVOCATION_START_PHYSICAL_GESTURE", "ASSISTANT_INVOCATION_POWER_LONG_PRESS", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AssistantInvocationEvent.kt */
public enum AssistantInvocationEvent implements UiEventLogger.UiEventEnum {
    ASSISTANT_INVOCATION_UNKNOWN(442),
    ASSISTANT_INVOCATION_TOUCH_GESTURE(443),
    ASSISTANT_INVOCATION_TOUCH_GESTURE_ALT(444),
    ASSISTANT_INVOCATION_HOTWORD(445),
    ASSISTANT_INVOCATION_QUICK_SEARCH_BAR(446),
    ASSISTANT_INVOCATION_HOME_LONG_PRESS(447),
    ASSISTANT_INVOCATION_PHYSICAL_GESTURE(StackStateAnimator.ANIMATION_DURATION_GO_TO_FULL_SHADE),
    ASSISTANT_INVOCATION_START_UNKNOWN(530),
    ASSISTANT_INVOCATION_START_TOUCH_GESTURE(531),
    ASSISTANT_INVOCATION_START_PHYSICAL_GESTURE(532),
    ASSISTANT_INVOCATION_POWER_LONG_PRESS(758);
    
    public static final Companion Companion = null;

    /* renamed from: id */
    private final int f292id;

    private AssistantInvocationEvent(int i) {
        this.f292id = i;
    }

    static {
        Companion = new Companion((DefaultConstructorMarker) null);
    }

    public int getId() {
        return this.f292id;
    }

    @Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/assist/AssistantInvocationEvent$Companion;", "", "()V", "deviceStateFromLegacyDeviceState", "", "legacyDeviceState", "eventFromLegacyInvocationType", "Lcom/android/systemui/assist/AssistantInvocationEvent;", "legacyInvocationType", "isInvocationComplete", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: AssistantInvocationEvent.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int deviceStateFromLegacyDeviceState(int i) {
            switch (i) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 5:
                    return 5;
                case 6:
                    return 6;
                case 7:
                    return 7;
                case 8:
                    return 8;
                case 9:
                    return 9;
                case 10:
                    return 10;
                default:
                    return 0;
            }
        }

        private Companion() {
        }

        public final AssistantInvocationEvent eventFromLegacyInvocationType(int i, boolean z) {
            if (z) {
                switch (i) {
                    case 1:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_TOUCH_GESTURE;
                    case 2:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_PHYSICAL_GESTURE;
                    case 3:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_HOTWORD;
                    case 4:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_QUICK_SEARCH_BAR;
                    case 5:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_HOME_LONG_PRESS;
                    case 6:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_POWER_LONG_PRESS;
                    default:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_UNKNOWN;
                }
            } else if (i == 1) {
                return AssistantInvocationEvent.ASSISTANT_INVOCATION_START_TOUCH_GESTURE;
            } else {
                if (i != 2) {
                    return AssistantInvocationEvent.ASSISTANT_INVOCATION_START_UNKNOWN;
                }
                return AssistantInvocationEvent.ASSISTANT_INVOCATION_START_PHYSICAL_GESTURE;
            }
        }
    }
}
