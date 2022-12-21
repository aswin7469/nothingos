package com.android.p019wm.shell.bubbles;

import com.android.p019wm.shell.draganddrop.DragAndDropController;

/* renamed from: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda8 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda8 implements DragAndDropController.DragAndDropListener {
    public final /* synthetic */ BubbleController f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda8(BubbleController bubbleController) {
        this.f$0 = bubbleController;
    }

    public final void onDragStarted() {
        this.f$0.collapseStack();
    }
}
