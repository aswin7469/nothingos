package com.android.systemui.media.taptotransfer.receiver;

import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.datetime.ZoneGetter;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0001\u0018\u0000 \r2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\rB\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nj\u0002\b\u000bj\u0002\b\f¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/receiver/ChipStateReceiver;", "", "stateInt", "", "uiEvent", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "(Ljava/lang/String;IILcom/android/internal/logging/UiEventLogger$UiEventEnum;)V", "getStateInt", "()I", "getUiEvent", "()Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "CLOSE_TO_SENDER", "FAR_FROM_SENDER", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChipStateReceiver.kt */
public enum ChipStateReceiver {
    CLOSE_TO_SENDER(0, MediaTttReceiverUiEvents.MEDIA_TTT_RECEIVER_CLOSE_TO_SENDER),
    FAR_FROM_SENDER(1, MediaTttReceiverUiEvents.MEDIA_TTT_RECEIVER_FAR_FROM_SENDER);
    
    public static final Companion Companion = null;
    private final int stateInt;
    private final UiEventLogger.UiEventEnum uiEvent;

    private ChipStateReceiver(int i, UiEventLogger.UiEventEnum uiEventEnum) {
        this.stateInt = i;
        this.uiEvent = uiEventEnum;
    }

    public final int getStateInt() {
        return this.stateInt;
    }

    public final UiEventLogger.UiEventEnum getUiEvent() {
        return this.uiEvent;
    }

    static {
        Companion = new Companion((DefaultConstructorMarker) null);
    }

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/receiver/ChipStateReceiver$Companion;", "", "()V", "getReceiverStateFromId", "Lcom/android/systemui/media/taptotransfer/receiver/ChipStateReceiver;", "displayState", "", "getReceiverStateIdFromName", "name", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ChipStateReceiver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ChipStateReceiver getReceiverStateFromId(int i) {
            try {
                for (ChipStateReceiver chipStateReceiver : ChipStateReceiver.values()) {
                    if (chipStateReceiver.getStateInt() == i) {
                        return chipStateReceiver;
                    }
                }
                throw new NoSuchElementException("Array contains no element matching the predicate.");
            } catch (NoSuchElementException e) {
                Log.e("ChipStateReceiver", "Could not find requested state " + i, e);
                ChipStateReceiver chipStateReceiver2 = null;
                return null;
            }
        }

        public final int getReceiverStateIdFromName(String str) {
            Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
            return ChipStateReceiver.valueOf(str).getStateInt();
        }
    }
}
