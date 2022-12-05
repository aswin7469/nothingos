package com.android.systemui.volume;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import com.android.keyguard.AlphaOptimizedImageButton;
import com.android.systemui.R$attr;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
/* loaded from: classes2.dex */
public class CaptionsToggleImageButton extends AlphaOptimizedImageButton {
    private static final int[] OPTED_OUT_STATE = {R$attr.optedOut};
    private ConfirmedTapListener mConfirmedTapListener;
    private GestureDetector mGestureDetector;
    private boolean mCaptionsEnabled = false;
    private boolean mOptedOut = false;
    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.volume.CaptionsToggleImageButton.1
        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return CaptionsToggleImageButton.this.tryToSendTapConfirmedEvent();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface ConfirmedTapListener {
        void onConfirmedTap();
    }

    public CaptionsToggleImageButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setContentDescription(getContext().getString(R$string.volume_odi_captions_content_description));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.widget.ImageView, android.view.View
    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (this.mOptedOut) {
            ImageButton.mergeDrawableStates(onCreateDrawableState, OPTED_OUT_STATE);
        }
        return onCreateDrawableState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Runnable setCaptionsEnabled(boolean z) {
        String string;
        int i;
        this.mCaptionsEnabled = z;
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK;
        if (z) {
            string = getContext().getString(R$string.volume_odi_captions_hint_disable);
        } else {
            string = getContext().getString(R$string.volume_odi_captions_hint_enable);
        }
        ViewCompat.replaceAccessibilityAction(this, accessibilityActionCompat, string, new AccessibilityViewCommand() { // from class: com.android.systemui.volume.CaptionsToggleImageButton$$ExternalSyntheticLambda0
            @Override // androidx.core.view.accessibility.AccessibilityViewCommand
            public final boolean perform(View view, AccessibilityViewCommand.CommandArguments commandArguments) {
                boolean lambda$setCaptionsEnabled$0;
                lambda$setCaptionsEnabled$0 = CaptionsToggleImageButton.this.lambda$setCaptionsEnabled$0(view, commandArguments);
                return lambda$setCaptionsEnabled$0;
            }
        });
        if (this.mCaptionsEnabled) {
            i = R$drawable.ic_volume_odi_captions;
        } else {
            i = R$drawable.ic_volume_odi_captions_disabled;
        }
        return setImageResourceAsync(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setCaptionsEnabled$0(View view, AccessibilityViewCommand.CommandArguments commandArguments) {
        return tryToSendTapConfirmedEvent();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean tryToSendTapConfirmedEvent() {
        ConfirmedTapListener confirmedTapListener = this.mConfirmedTapListener;
        if (confirmedTapListener != null) {
            confirmedTapListener.onConfirmedTap();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getCaptionsEnabled() {
        return this.mCaptionsEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOptedOut(boolean z) {
        this.mOptedOut = z;
        refreshDrawableState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getOptedOut() {
        return this.mOptedOut;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnConfirmedTapListener(ConfirmedTapListener confirmedTapListener, Handler handler) {
        this.mConfirmedTapListener = confirmedTapListener;
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(getContext(), this.mGestureListener, handler);
        }
    }
}
