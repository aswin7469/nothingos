package com.android.systemui.volume;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import com.android.keyguard.AlphaOptimizedImageButton;
import com.android.systemui.C1894R;

public class CaptionsToggleImageButton extends AlphaOptimizedImageButton {
    private boolean mCaptionsEnabled = false;
    private ConfirmedTapListener mConfirmedTapListener;
    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return CaptionsToggleImageButton.this.tryToSendTapConfirmedEvent();
        }
    };

    interface ConfirmedTapListener {
        void onConfirmedTap();
    }

    public CaptionsToggleImageButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setContentDescription(getContext().getString(C1894R.string.volume_odi_captions_content_description));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        GestureDetector gestureDetector = this.mGestureDetector;
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }

    public int[] onCreateDrawableState(int i) {
        return super.onCreateDrawableState(i + 1);
    }

    /* access modifiers changed from: package-private */
    public Runnable setCaptionsEnabled(boolean z) {
        String str;
        this.mCaptionsEnabled = z;
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK;
        if (this.mCaptionsEnabled) {
            str = getContext().getString(C1894R.string.volume_odi_captions_hint_disable);
        } else {
            str = getContext().getString(C1894R.string.volume_odi_captions_hint_enable);
        }
        ViewCompat.replaceAccessibilityAction(this, accessibilityActionCompat, str, new CaptionsToggleImageButton$$ExternalSyntheticLambda0(this));
        return setImageResourceAsync(this.mCaptionsEnabled ? C1894R.C1896drawable.ic_volume_odi_captions : C1894R.C1896drawable.ic_volume_odi_captions_disabled);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setCaptionsEnabled$0$com-android-systemui-volume-CaptionsToggleImageButton */
    public /* synthetic */ boolean mo47245x6e1d1350(View view, AccessibilityViewCommand.CommandArguments commandArguments) {
        return tryToSendTapConfirmedEvent();
    }

    /* access modifiers changed from: private */
    public boolean tryToSendTapConfirmedEvent() {
        ConfirmedTapListener confirmedTapListener = this.mConfirmedTapListener;
        if (confirmedTapListener == null) {
            return false;
        }
        confirmedTapListener.onConfirmedTap();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean getCaptionsEnabled() {
        return this.mCaptionsEnabled;
    }

    /* access modifiers changed from: package-private */
    public void setOnConfirmedTapListener(ConfirmedTapListener confirmedTapListener, Handler handler) {
        this.mConfirmedTapListener = confirmedTapListener;
        if (this.mGestureDetector == null) {
            this.mGestureDetector = new GestureDetector(getContext(), this.mGestureListener, handler);
        }
    }
}
