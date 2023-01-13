package com.android.systemui.media.taptotransfer.receiver;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaRoute2Info;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.media.taptotransfer.common.MediaTttChipControllerCommon;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000*\u0001\u0019\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B[\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0001\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016¢\u0006\u0002\u0010\u0017J\u0017\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016¢\u0006\u0002\u0010\u001fJ\u0018\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u00022\u0006\u0010#\u001a\u00020$H\u0016J,\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020\u001c2\u0006\u0010'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010*2\b\u0010+\u001a\u0004\u0018\u00010,H\u0002R\u0010\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0004\n\u0002\u0010\u001aR\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000¨\u0006-"}, mo65043d2 = {"Lcom/android/systemui/media/taptotransfer/receiver/MediaTttChipControllerReceiver;", "Lcom/android/systemui/media/taptotransfer/common/MediaTttChipControllerCommon;", "Lcom/android/systemui/media/taptotransfer/receiver/ChipReceiverInfo;", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "context", "Landroid/content/Context;", "logger", "Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;", "windowManager", "Landroid/view/WindowManager;", "viewUtil", "Lcom/android/systemui/util/view/ViewUtil;", "mainExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "tapGestureDetector", "Lcom/android/systemui/statusbar/gesture/TapGestureDetector;", "powerManager", "Landroid/os/PowerManager;", "mainHandler", "Landroid/os/Handler;", "uiEventLogger", "Lcom/android/systemui/media/taptotransfer/receiver/MediaTttReceiverUiEventLogger;", "(Lcom/android/systemui/statusbar/CommandQueue;Landroid/content/Context;Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;Landroid/view/WindowManager;Lcom/android/systemui/util/view/ViewUtil;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/statusbar/gesture/TapGestureDetector;Landroid/os/PowerManager;Landroid/os/Handler;Lcom/android/systemui/media/taptotransfer/receiver/MediaTttReceiverUiEventLogger;)V", "commandQueueCallbacks", "com/android/systemui/media/taptotransfer/receiver/MediaTttChipControllerReceiver$commandQueueCallbacks$1", "Lcom/android/systemui/media/taptotransfer/receiver/MediaTttChipControllerReceiver$commandQueueCallbacks$1;", "getIconSize", "", "isAppIcon", "", "(Z)Ljava/lang/Integer;", "updateChipView", "", "chipInfo", "currentChipView", "Landroid/view/ViewGroup;", "updateMediaTapToTransferReceiverDisplay", "displayState", "routeInfo", "Landroid/media/MediaRoute2Info;", "appIcon", "Landroid/graphics/drawable/Icon;", "appName", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTttChipControllerReceiver.kt */
public final class MediaTttChipControllerReceiver extends MediaTttChipControllerCommon<ChipReceiverInfo> {
    private final MediaTttChipControllerReceiver$commandQueueCallbacks$1 commandQueueCallbacks;
    private final Handler mainHandler;
    private final MediaTttReceiverUiEventLogger uiEventLogger;

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public MediaTttChipControllerReceiver(com.android.systemui.statusbar.CommandQueue r14, android.content.Context r15, @com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverLogger com.android.systemui.media.taptotransfer.common.MediaTttLogger r16, android.view.WindowManager r17, com.android.systemui.util.view.ViewUtil r18, com.android.systemui.util.concurrency.DelayableExecutor r19, com.android.systemui.statusbar.gesture.TapGestureDetector r20, android.os.PowerManager r21, @com.android.systemui.dagger.qualifiers.Main android.os.Handler r22, com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverUiEventLogger r23) {
        /*
            r13 = this;
            r9 = r13
            r10 = r14
            r11 = r22
            r12 = r23
            java.lang.String r0 = "commandQueue"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            java.lang.String r0 = "context"
            r1 = r15
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r15, r0)
            java.lang.String r0 = "logger"
            r2 = r16
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            java.lang.String r0 = "windowManager"
            r3 = r17
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "viewUtil"
            r4 = r18
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            java.lang.String r0 = "mainExecutor"
            r5 = r19
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
            java.lang.String r0 = "tapGestureDetector"
            r6 = r20
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            java.lang.String r0 = "powerManager"
            r7 = r21
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            java.lang.String r0 = "mainHandler"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "uiEventLogger"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            r8 = 2131624242(0x7f0e0132, float:1.8875658E38)
            r0 = r13
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            r9.mainHandler = r11
            r9.uiEventLogger = r12
            com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver$commandQueueCallbacks$1 r0 = new com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver$commandQueueCallbacks$1
            r0.<init>(r13)
            r9.commandQueueCallbacks = r0
            com.android.systemui.statusbar.CommandQueue$Callbacks r0 = (com.android.systemui.statusbar.CommandQueue.Callbacks) r0
            r14.addCallback((com.android.systemui.statusbar.CommandQueue.Callbacks) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver.<init>(com.android.systemui.statusbar.CommandQueue, android.content.Context, com.android.systemui.media.taptotransfer.common.MediaTttLogger, android.view.WindowManager, com.android.systemui.util.view.ViewUtil, com.android.systemui.util.concurrency.DelayableExecutor, com.android.systemui.statusbar.gesture.TapGestureDetector, android.os.PowerManager, android.os.Handler, com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverUiEventLogger):void");
    }

    /* access modifiers changed from: private */
    public final void updateMediaTapToTransferReceiverDisplay(int i, MediaRoute2Info mediaRoute2Info, Icon icon, CharSequence charSequence) {
        String str;
        ChipStateReceiver receiverStateFromId = ChipStateReceiver.Companion.getReceiverStateFromId(i);
        if (receiverStateFromId == null || (str = receiverStateFromId.name()) == null) {
            str = "Invalid";
        }
        MediaTttLogger logger$SystemUI_nothingRelease = getLogger$SystemUI_nothingRelease();
        String id = mediaRoute2Info.getId();
        Intrinsics.checkNotNullExpressionValue(id, "routeInfo.id");
        logger$SystemUI_nothingRelease.logStateChange(str, id);
        if (receiverStateFromId == null) {
            Log.e("MediaTapToTransferRcvr", "Unhandled MediaTransferReceiverState " + i);
            return;
        }
        this.uiEventLogger.logReceiverStateChange(receiverStateFromId);
        if (receiverStateFromId == ChipStateReceiver.FAR_FROM_SENDER) {
            String simpleName = Reflection.getOrCreateKotlinClass(ChipStateReceiver.FAR_FROM_SENDER.getClass()).getSimpleName();
            Intrinsics.checkNotNull(simpleName);
            removeChip(simpleName);
        } else if (icon == null) {
            displayChip(new ChipReceiverInfo(mediaRoute2Info, (Drawable) null, charSequence));
        } else {
            icon.loadDrawableAsync(getContext$SystemUI_nothingRelease(), new MediaTttChipControllerReceiver$$ExternalSyntheticLambda0(this, mediaRoute2Info, charSequence), this.mainHandler);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateMediaTapToTransferReceiverDisplay$lambda-0  reason: not valid java name */
    public static final void m2849updateMediaTapToTransferReceiverDisplay$lambda0(MediaTttChipControllerReceiver mediaTttChipControllerReceiver, MediaRoute2Info mediaRoute2Info, CharSequence charSequence, Drawable drawable) {
        Intrinsics.checkNotNullParameter(mediaTttChipControllerReceiver, "this$0");
        Intrinsics.checkNotNullParameter(mediaRoute2Info, "$routeInfo");
        mediaTttChipControllerReceiver.displayChip(new ChipReceiverInfo(mediaRoute2Info, drawable, charSequence));
    }

    public void updateChipView(ChipReceiverInfo chipReceiverInfo, ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(chipReceiverInfo, "chipInfo");
        Intrinsics.checkNotNullParameter(viewGroup, "currentChipView");
        setIcon$SystemUI_nothingRelease(viewGroup, chipReceiverInfo.getRouteInfo().getPackageName(), chipReceiverInfo.getAppIconDrawableOverride(), chipReceiverInfo.getAppNameOverride());
    }

    public Integer getIconSize(boolean z) {
        return Integer.valueOf(getContext$SystemUI_nothingRelease().getResources().getDimensionPixelSize(z ? C1894R.dimen.media_ttt_icon_size_receiver : C1894R.dimen.media_ttt_generic_icon_size_receiver));
    }
}
