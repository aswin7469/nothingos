package com.android.systemui.statusbar.notification.row;

import android.transition.Transition;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0007\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\b\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\t\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/statusbar/notification/row/ChannelEditorListView$updateRows$1", "Landroid/transition/Transition$TransitionListener;", "onTransitionCancel", "", "p0", "Landroid/transition/Transition;", "onTransitionEnd", "onTransitionPause", "onTransitionResume", "onTransitionStart", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChannelEditorListView.kt */
public final class ChannelEditorListView$updateRows$1 implements Transition.TransitionListener {
    final /* synthetic */ ChannelEditorListView this$0;

    public void onTransitionCancel(Transition transition) {
    }

    public void onTransitionPause(Transition transition) {
    }

    public void onTransitionResume(Transition transition) {
    }

    public void onTransitionStart(Transition transition) {
    }

    ChannelEditorListView$updateRows$1(ChannelEditorListView channelEditorListView) {
        this.this$0 = channelEditorListView;
    }

    public void onTransitionEnd(Transition transition) {
        this.this$0.notifySubtreeAccessibilityStateChangedIfNeeded();
    }
}
