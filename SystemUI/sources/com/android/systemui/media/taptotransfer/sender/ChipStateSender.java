package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.media.MediaRoute2Info;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.C1894R;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0001\u0018\u0000 /2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001/BA\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0001\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0018\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0018J,\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010$\u001a\u00020%H\u0016R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\rR\u0011\u0010\t\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016j\u0002\b&j\u0002\b'j\u0002\b(j\u0002\b)j\u0002\b*j\u0002\b+j\u0002\b,j\u0002\b-j\u0002\b.¨\u00060"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "", "stateInt", "", "uiEvent", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "stringResId", "isMidTransfer", "", "isTransferFailure", "timeout", "", "(Ljava/lang/String;IILcom/android/internal/logging/UiEventLogger$UiEventEnum;Ljava/lang/Integer;ZZJ)V", "()Z", "getStateInt", "()I", "getStringResId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getTimeout", "()J", "getUiEvent", "()Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "getChipTextString", "", "context", "Landroid/content/Context;", "otherDeviceName", "undoClickListener", "Landroid/view/View$OnClickListener;", "controllerSender", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttChipControllerSender;", "routeInfo", "Landroid/media/MediaRoute2Info;", "undoCallback", "Lcom/android/internal/statusbar/IUndoMediaTransferCallback;", "uiEventLogger", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttSenderUiEventLogger;", "ALMOST_CLOSE_TO_START_CAST", "ALMOST_CLOSE_TO_END_CAST", "TRANSFER_TO_RECEIVER_TRIGGERED", "TRANSFER_TO_THIS_DEVICE_TRIGGERED", "TRANSFER_TO_RECEIVER_SUCCEEDED", "TRANSFER_TO_THIS_DEVICE_SUCCEEDED", "TRANSFER_TO_RECEIVER_FAILED", "TRANSFER_TO_THIS_DEVICE_FAILED", "FAR_FROM_RECEIVER", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChipStateSender.kt */
public enum ChipStateSender {
    ALMOST_CLOSE_TO_START_CAST(0, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_ALMOST_CLOSE_TO_START_CAST, Integer.valueOf((int) C1894R.string.media_move_closer_to_start_cast), false, false, 0, 56, (long) null),
    ALMOST_CLOSE_TO_END_CAST(1, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_ALMOST_CLOSE_TO_END_CAST, Integer.valueOf((int) C1894R.string.media_move_closer_to_end_cast), false, false, 0, 56, (long) null),
    TRANSFER_TO_RECEIVER_TRIGGERED(2, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_RECEIVER_TRIGGERED, Integer.valueOf((int) C1894R.string.media_transfer_playing_different_device), true, false, 15000, 16, (long) null),
    TRANSFER_TO_THIS_DEVICE_TRIGGERED(3, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_THIS_DEVICE_TRIGGERED, Integer.valueOf((int) C1894R.string.media_transfer_playing_this_device), true, false, 15000, 16, (long) null),
    TRANSFER_TO_RECEIVER_FAILED(6, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_RECEIVER_FAILED, r1, false, true, 0, 40, (long) null),
    TRANSFER_TO_THIS_DEVICE_FAILED(7, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_THIS_DEVICE_FAILED, r1, false, true, 0, 40, (long) null),
    FAR_FROM_RECEIVER(8, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_FAR_FROM_RECEIVER, (int) null, false, false, 0, 56, (long) null);
    
    public static final Companion Companion = null;
    private final boolean isMidTransfer;
    private final boolean isTransferFailure;
    private final int stateInt;
    private final Integer stringResId;
    private final long timeout;
    private final UiEventLogger.UiEventEnum uiEvent;

    public View.OnClickListener undoClickListener(MediaTttChipControllerSender mediaTttChipControllerSender, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback, MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
        Intrinsics.checkNotNullParameter(mediaTttChipControllerSender, "controllerSender");
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
        Intrinsics.checkNotNullParameter(mediaTttSenderUiEventLogger, "uiEventLogger");
        return null;
    }

    private ChipStateSender(int i, UiEventLogger.UiEventEnum uiEventEnum, Integer num, boolean z, boolean z2, long j) {
        this.stateInt = i;
        this.uiEvent = uiEventEnum;
        this.stringResId = num;
        this.isMidTransfer = z;
        this.isTransferFailure = z2;
        this.timeout = j;
    }

    public final int getStateInt() {
        return this.stateInt;
    }

    public final UiEventLogger.UiEventEnum getUiEvent() {
        return this.uiEvent;
    }

    public final Integer getStringResId() {
        return this.stringResId;
    }

    public final boolean isMidTransfer() {
        return this.isMidTransfer;
    }

    public final boolean isTransferFailure() {
        return this.isTransferFailure;
    }

    public final long getTimeout() {
        return this.timeout;
    }

    static {
        Companion = new Companion((DefaultConstructorMarker) null);
    }

    @Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0001\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender$TRANSFER_TO_RECEIVER_SUCCEEDED;", "Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "undoClickListener", "Landroid/view/View$OnClickListener;", "controllerSender", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttChipControllerSender;", "routeInfo", "Landroid/media/MediaRoute2Info;", "undoCallback", "Lcom/android/internal/statusbar/IUndoMediaTransferCallback;", "uiEventLogger", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttSenderUiEventLogger;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ChipStateSender.kt */
    static final class TRANSFER_TO_RECEIVER_SUCCEEDED extends ChipStateSender {
        TRANSFER_TO_RECEIVER_SUCCEEDED(String str, int i) {
            super(str, i, 4, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_RECEIVER_SUCCEEDED, Integer.valueOf((int) C1894R.string.media_transfer_playing_different_device), false, false, 0, 56, (DefaultConstructorMarker) null);
        }

        public View.OnClickListener undoClickListener(MediaTttChipControllerSender mediaTttChipControllerSender, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback, MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
            Intrinsics.checkNotNullParameter(mediaTttChipControllerSender, "controllerSender");
            Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
            Intrinsics.checkNotNullParameter(mediaTttSenderUiEventLogger, "uiEventLogger");
            if (iUndoMediaTransferCallback == null) {
                return null;
            }
            return new C2242x18afdbba(mediaTttSenderUiEventLogger, iUndoMediaTransferCallback, mediaTttChipControllerSender, mediaRoute2Info);
        }

        /* access modifiers changed from: private */
        /* renamed from: undoClickListener$lambda-0  reason: not valid java name */
        public static final void m2850undoClickListener$lambda0(MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger, IUndoMediaTransferCallback iUndoMediaTransferCallback, MediaTttChipControllerSender mediaTttChipControllerSender, MediaRoute2Info mediaRoute2Info, View view) {
            Intrinsics.checkNotNullParameter(mediaTttSenderUiEventLogger, "$uiEventLogger");
            Intrinsics.checkNotNullParameter(mediaTttChipControllerSender, "$controllerSender");
            Intrinsics.checkNotNullParameter(mediaRoute2Info, "$routeInfo");
            mediaTttSenderUiEventLogger.logUndoClicked(MediaTttSenderUiEvents.MEDIA_TTT_SENDER_UNDO_TRANSFER_TO_RECEIVER_CLICKED);
            iUndoMediaTransferCallback.onUndoTriggered();
            mediaTttChipControllerSender.displayChip(new ChipSenderInfo(ChipStateSender.TRANSFER_TO_THIS_DEVICE_TRIGGERED, mediaRoute2Info, iUndoMediaTransferCallback));
        }
    }

    @Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0001\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender$TRANSFER_TO_THIS_DEVICE_SUCCEEDED;", "Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "undoClickListener", "Landroid/view/View$OnClickListener;", "controllerSender", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttChipControllerSender;", "routeInfo", "Landroid/media/MediaRoute2Info;", "undoCallback", "Lcom/android/internal/statusbar/IUndoMediaTransferCallback;", "uiEventLogger", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttSenderUiEventLogger;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ChipStateSender.kt */
    static final class TRANSFER_TO_THIS_DEVICE_SUCCEEDED extends ChipStateSender {
        TRANSFER_TO_THIS_DEVICE_SUCCEEDED(String str, int i) {
            super(str, i, 5, MediaTttSenderUiEvents.MEDIA_TTT_SENDER_TRANSFER_TO_THIS_DEVICE_SUCCEEDED, Integer.valueOf((int) C1894R.string.media_transfer_playing_this_device), false, false, 0, 56, (DefaultConstructorMarker) null);
        }

        public View.OnClickListener undoClickListener(MediaTttChipControllerSender mediaTttChipControllerSender, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback, MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger) {
            Intrinsics.checkNotNullParameter(mediaTttChipControllerSender, "controllerSender");
            Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
            Intrinsics.checkNotNullParameter(mediaTttSenderUiEventLogger, "uiEventLogger");
            if (iUndoMediaTransferCallback == null) {
                return null;
            }
            return new C2243x7e269a2e(mediaTttSenderUiEventLogger, iUndoMediaTransferCallback, mediaTttChipControllerSender, mediaRoute2Info);
        }

        /* access modifiers changed from: private */
        /* renamed from: undoClickListener$lambda-0  reason: not valid java name */
        public static final void m2851undoClickListener$lambda0(MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger, IUndoMediaTransferCallback iUndoMediaTransferCallback, MediaTttChipControllerSender mediaTttChipControllerSender, MediaRoute2Info mediaRoute2Info, View view) {
            Intrinsics.checkNotNullParameter(mediaTttSenderUiEventLogger, "$uiEventLogger");
            Intrinsics.checkNotNullParameter(mediaTttChipControllerSender, "$controllerSender");
            Intrinsics.checkNotNullParameter(mediaRoute2Info, "$routeInfo");
            mediaTttSenderUiEventLogger.logUndoClicked(MediaTttSenderUiEvents.MEDIA_TTT_SENDER_UNDO_TRANSFER_TO_THIS_DEVICE_CLICKED);
            iUndoMediaTransferCallback.onUndoTriggered();
            mediaTttChipControllerSender.displayChip(new ChipSenderInfo(ChipStateSender.TRANSFER_TO_RECEIVER_TRIGGERED, mediaRoute2Info, iUndoMediaTransferCallback));
        }
    }

    public final String getChipTextString(Context context, String str) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(str, "otherDeviceName");
        Integer num = this.stringResId;
        if (num == null) {
            return null;
        }
        return context.getString(num.intValue(), new Object[]{str});
    }

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender$Companion;", "", "()V", "getSenderStateFromId", "Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "displayState", "", "getSenderStateIdFromName", "name", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ChipStateSender.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final ChipStateSender getSenderStateFromId(int i) {
            try {
                for (ChipStateSender chipStateSender : ChipStateSender.values()) {
                    if (chipStateSender.getStateInt() == i) {
                        return chipStateSender;
                    }
                }
                throw new NoSuchElementException("Array contains no element matching the predicate.");
            } catch (NoSuchElementException e) {
                Log.e("ChipStateSender", "Could not find requested state " + i, e);
                ChipStateSender chipStateSender2 = null;
                return null;
            }
        }

        public final int getSenderStateIdFromName(String str) {
            Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
            return ChipStateSender.valueOf(str).getStateInt();
        }
    }
}
