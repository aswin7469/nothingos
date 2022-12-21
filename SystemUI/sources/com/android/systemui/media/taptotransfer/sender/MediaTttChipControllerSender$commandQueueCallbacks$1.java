package com.android.systemui.media.taptotransfer.sender;

import android.media.MediaRoute2Info;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.statusbar.CommandQueue;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016¨\u0006\n"}, mo64987d2 = {"com/android/systemui/media/taptotransfer/sender/MediaTttChipControllerSender$commandQueueCallbacks$1", "Lcom/android/systemui/statusbar/CommandQueue$Callbacks;", "updateMediaTapToTransferSenderDisplay", "", "displayState", "", "routeInfo", "Landroid/media/MediaRoute2Info;", "undoCallback", "Lcom/android/internal/statusbar/IUndoMediaTransferCallback;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTttChipControllerSender.kt */
public final class MediaTttChipControllerSender$commandQueueCallbacks$1 implements CommandQueue.Callbacks {
    final /* synthetic */ MediaTttChipControllerSender this$0;

    MediaTttChipControllerSender$commandQueueCallbacks$1(MediaTttChipControllerSender mediaTttChipControllerSender) {
        this.this$0 = mediaTttChipControllerSender;
    }

    public void updateMediaTapToTransferSenderDisplay(int i, MediaRoute2Info mediaRoute2Info, IUndoMediaTransferCallback iUndoMediaTransferCallback) {
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
        this.this$0.updateMediaTapToTransferSenderDisplay(i, mediaRoute2Info, iUndoMediaTransferCallback);
    }
}
