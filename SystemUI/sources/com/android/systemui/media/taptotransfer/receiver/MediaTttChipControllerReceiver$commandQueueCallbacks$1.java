package com.android.systemui.media.taptotransfer.receiver;

import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;
import com.android.systemui.statusbar.CommandQueue;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016¨\u0006\f"}, mo64987d2 = {"com/android/systemui/media/taptotransfer/receiver/MediaTttChipControllerReceiver$commandQueueCallbacks$1", "Lcom/android/systemui/statusbar/CommandQueue$Callbacks;", "updateMediaTapToTransferReceiverDisplay", "", "displayState", "", "routeInfo", "Landroid/media/MediaRoute2Info;", "appIcon", "Landroid/graphics/drawable/Icon;", "appName", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTttChipControllerReceiver.kt */
public final class MediaTttChipControllerReceiver$commandQueueCallbacks$1 implements CommandQueue.Callbacks {
    final /* synthetic */ MediaTttChipControllerReceiver this$0;

    MediaTttChipControllerReceiver$commandQueueCallbacks$1(MediaTttChipControllerReceiver mediaTttChipControllerReceiver) {
        this.this$0 = mediaTttChipControllerReceiver;
    }

    public void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "routeInfo");
        this.this$0.updateMediaTapToTransferReceiverDisplay(i, mediaRoute2Info, icon, charSequence);
    }
}
