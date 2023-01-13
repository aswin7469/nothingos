package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.graphics.Rect;
import android.view.View;
import kotlin.Metadata;
import kotlin.Unit;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\n\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0016J\n\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0016¨\u0006\u0005"}, mo65043d2 = {"com/android/systemui/statusbar/events/PrivacyDotViewController$systemStatusAnimationCallback$1", "Lcom/android/systemui/statusbar/events/SystemStatusAnimationCallback;", "onHidePersistentDot", "Landroid/animation/Animator;", "onSystemStatusAnimationTransitionToPersistentDot", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewController$systemStatusAnimationCallback$1 implements SystemStatusAnimationCallback {
    final /* synthetic */ PrivacyDotViewController this$0;

    PrivacyDotViewController$systemStatusAnimationCallback$1(PrivacyDotViewController privacyDotViewController) {
        this.this$0 = privacyDotViewController;
    }

    public Animator onSystemStatusAnimationTransitionToPersistentDot() {
        Object access$getLock$p = this.this$0.lock;
        PrivacyDotViewController privacyDotViewController = this.this$0;
        synchronized (access$getLock$p) {
            privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, true, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8189, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
        return null;
    }

    public Animator onHidePersistentDot() {
        Object access$getLock$p = this.this$0.lock;
        PrivacyDotViewController privacyDotViewController = this.this$0;
        synchronized (access$getLock$p) {
            privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8189, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
        return null;
    }
}
