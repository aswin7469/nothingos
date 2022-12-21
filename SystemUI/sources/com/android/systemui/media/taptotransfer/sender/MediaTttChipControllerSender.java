package com.android.systemui.media.taptotransfer.sender;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.media.taptotransfer.common.MediaTttRemovalReason;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0017\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001BS\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\b\b\u0001\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014¢\u0006\u0002\u0010\u0015J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0018\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0016J\"\u0010#\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010)H\u0002J\f\u0010*\u001a\u00020%*\u00020+H\u0002R\u0010\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0004\n\u0002\u0010\u0018R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000¨\u0006,"}, mo64987d2 = {"Lcom/android/systemui/media/taptotransfer/sender/MediaTttChipControllerSender;", "Lcom/android/systemui/media/taptotransfer/common/MediaTttChipControllerCommon;", "Lcom/android/systemui/media/taptotransfer/sender/ChipSenderInfo;", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "context", "Landroid/content/Context;", "logger", "Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;", "windowManager", "Landroid/view/WindowManager;", "viewUtil", "Lcom/android/systemui/util/view/ViewUtil;", "mainExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "tapGestureDetector", "Lcom/android/systemui/statusbar/gesture/TapGestureDetector;", "powerManager", "Landroid/os/PowerManager;", "uiEventLogger", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttSenderUiEventLogger;", "(Lcom/android/systemui/statusbar/CommandQueue;Landroid/content/Context;Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;Landroid/view/WindowManager;Lcom/android/systemui/util/view/ViewUtil;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/statusbar/gesture/TapGestureDetector;Landroid/os/PowerManager;Lcom/android/systemui/media/taptotransfer/sender/MediaTttSenderUiEventLogger;)V", "commandQueueCallbacks", "com/android/systemui/media/taptotransfer/sender/MediaTttChipControllerSender$commandQueueCallbacks$1", "Lcom/android/systemui/media/taptotransfer/sender/MediaTttChipControllerSender$commandQueueCallbacks$1;", "currentlyDisplayedChipState", "Lcom/android/systemui/media/taptotransfer/sender/ChipStateSender;", "removeChip", "", "removalReason", "", "updateChipView", "chipInfo", "currentChipView", "Landroid/view/ViewGroup;", "updateMediaTapToTransferSenderDisplay", "displayState", "", "routeInfo", "Landroid/media/MediaRoute2Info;", "undoCallback", "Lcom/android/internal/statusbar/IUndoMediaTransferCallback;", "visibleIfTrue", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTttChipControllerSender.kt */
public final class MediaTttChipControllerSender extends MediaTttChipControllerCommon<ChipSenderInfo> {
    private final MediaTttChipControllerSender$commandQueueCallbacks$1 commandQueueCallbacks;
    private ChipStateSender currentlyDisplayedChipState;
    private final MediaTttSenderUiEventLogger uiEventLogger;

    private final int visibleIfTrue(boolean z) {
        return z ? 0 : 8;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MediaTttChipControllerSender(com.android.systemui.statusbar.CommandQueue r13, android.content.Context r14, @com.android.systemui.media.taptotransfer.sender.MediaTttSenderLogger com.android.systemui.media.taptotransfer.common.MediaTttLogger r15, android.view.WindowManager r16, com.android.systemui.util.view.ViewUtil r17, @com.android.systemui.dagger.qualifiers.Main com.android.systemui.util.concurrency.DelayableExecutor r18, com.android.systemui.statusbar.gesture.TapGestureDetector r19, android.os.PowerManager r20, com.android.systemui.media.taptotransfer.sender.MediaTttSenderUiEventLogger r21) {
        /*
            r12 = this;
            r9 = r12
            r10 = r13
            r11 = r21
            java.lang.String r0 = "commandQueue"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            java.lang.String r0 = "context"
            r1 = r14
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            java.lang.String r0 = "logger"
            r2 = r15
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r15, r0)
            java.lang.String r0 = "windowManager"
            r3 = r16
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "viewUtil"
            r4 = r17
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            java.lang.String r0 = "mainExecutor"
            r5 = r18
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
            java.lang.String r0 = "tapGestureDetector"
            r6 = r19
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            java.lang.String r0 = "powerManager"
            r7 = r20
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            java.lang.String r0 = "uiEventLogger"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            r8 = 2131624241(0x7f0e0131, float:1.8875656E38)
            r0 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            r9.uiEventLogger = r11
            com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender$commandQueueCallbacks$1 r0 = new com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender$commandQueueCallbacks$1
            r0.<init>(r12)
            r9.commandQueueCallbacks = r0
            com.android.systemui.statusbar.CommandQueue$Callbacks r0 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r0
            r13.addCallback((com.android.systemui.statusbar.CommandQueue.Callbacks) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender.<init>(com.android.systemui.statusbar.CommandQueue, android.content.Context, com.android.systemui.media.taptotransfer.common.MediaTttLogger, android.view.WindowManager, com.android.systemui.util.view.ViewUtil, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.statusbar.gesture.TapGestureDetector, android.os.PowerManager, com.android.systemui.media.taptotransfer.sender.MediaTttSenderUiEventLogger):void");
    }

    /* access modifiers changed from: private */
    public final void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        String str;
        ChipStateSender senderStateFromId = ChipStateSender.Companion.getSenderStateFromId(i);
        if (senderStateFromId == null || (str = senderStateFromId.name()) == null) {
            str = "Invalid";
        }
        MediaTttLogger logger$SystemUI_nothingRelease = getLogger$SystemUI_nothingRelease();
        String id = mediaRoute2Info.getId();
        Intrinsics.checkNotNullExpressionValue(id, "routeInfo.id");
        logger$SystemUI_nothingRelease.logStateChange(str, id);
        if (senderStateFromId == null) {
            Log.e(MediaTttChipControllerSenderKt.SENDER_TAG, "Unhandled MediaTransferSenderState " + i);
            return;
        }
        this.uiEventLogger.logSenderStateChange(senderStateFromId);
        if (senderStateFromId == ChipStateSender.FAR_FROM_RECEIVER) {
            String simpleName = Reflection.getOrCreateKotlinClass(ChipStateSender.FAR_FROM_RECEIVER.getClass()).getSimpleName();
            Intrinsics.checkNotNull(simpleName);
            removeChip(simpleName);
            return;
        }
        displayChip(new ChipSenderInfo(senderStateFromId, mediaRoute2Info, iUndoMediaTransferCallback));
    }

    public void updateChipView(ChipSenderInfo chipSenderInfo, ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(chipSenderInfo, "chipInfo");
        Intrinsics.checkNotNullParameter(viewGroup, "currentChipView");
        ChipStateSender state = chipSenderInfo.getState();
        this.currentlyDisplayedChipState = state;
        MediaTttChipControllerCommon.setIcon$SystemUI_nothingRelease$default(this, viewGroup, chipSenderInfo.getRouteInfo().getPackageName(), (Drawable) null, (CharSequence) null, 12, (Object) null);
        String obj = chipSenderInfo.getRouteInfo().getName().toString();
        TextView textView = (TextView) viewGroup.requireViewById(C1893R.C1897id.text);
        Context context = textView.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        textView.setText(state.getChipTextString(context, obj));
        viewGroup.requireViewById(C1893R.C1897id.loading).setVisibility(visibleIfTrue(state.isMidTransfer()));
        View requireViewById = viewGroup.requireViewById(C1893R.C1897id.undo);
        View.OnClickListener undoClickListener = state.undoClickListener(this, chipSenderInfo.getRouteInfo(), chipSenderInfo.getUndoCallback(), this.uiEventLogger);
        requireViewById.setOnClickListener(undoClickListener);
        requireViewById.setVisibility(visibleIfTrue(undoClickListener != null));
        viewGroup.requireViewById(C1893R.C1897id.failure_icon).setVisibility(visibleIfTrue(state.isTransferFailure()));
    }

    public void removeChip(String str) {
        Intrinsics.checkNotNullParameter(str, "removalReason");
        ChipStateSender chipStateSender = this.currentlyDisplayedChipState;
        boolean z = false;
        if (chipStateSender != null && chipStateSender.isMidTransfer()) {
            z = true;
        }
        if (!z || Intrinsics.areEqual((Object) str, (Object) MediaTttRemovalReason.REASON_TIMEOUT)) {
            super.removeChip(str);
            this.currentlyDisplayedChipState = null;
        }
    }
}
