package com.android.systemui.media.taptotransfer.sender;

import android.media.MediaRoute2Info;
import android.view.View;
import com.android.internal.statusbar.IUndoMediaTransferCallback;
import com.android.systemui.media.taptotransfer.sender.ChipStateSender;

/* renamed from: com.android.systemui.media.taptotransfer.sender.ChipStateSender$TRANSFER_TO_RECEIVER_SUCCEEDED$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2242x18afdbba implements View.OnClickListener {
    public final /* synthetic */ MediaTttSenderUiEventLogger f$0;
    public final /* synthetic */ IUndoMediaTransferCallback f$1;
    public final /* synthetic */ MediaTttChipControllerSender f$2;
    public final /* synthetic */ MediaRoute2Info f$3;

    public /* synthetic */ C2242x18afdbba(MediaTttSenderUiEventLogger mediaTttSenderUiEventLogger, IUndoMediaTransferCallback iUndoMediaTransferCallback, MediaTttChipControllerSender mediaTttChipControllerSender, MediaRoute2Info mediaRoute2Info) {
        this.f$0 = mediaTttSenderUiEventLogger;
        this.f$1 = iUndoMediaTransferCallback;
        this.f$2 = mediaTttChipControllerSender;
        this.f$3 = mediaRoute2Info;
    }

    public final void onClick(View view) {
        ChipStateSender.TRANSFER_TO_RECEIVER_SUCCEEDED.m2850undoClickListener$lambda0(this.f$0, this.f$1, this.f$2, this.f$3, view);
    }
}
