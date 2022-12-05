package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
class PlaybackControlsRowView extends LinearLayout {
    private OnUnhandledKeyListener mOnUnhandledKeyListener;

    /* loaded from: classes.dex */
    public interface OnUnhandledKeyListener {
        boolean onUnhandledKey(KeyEvent event);
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public PlaybackControlsRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaybackControlsRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        OnUnhandledKeyListener onUnhandledKeyListener = this.mOnUnhandledKeyListener;
        return onUnhandledKeyListener != null && onUnhandledKeyListener.onUnhandledKey(event);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View findFocus = findFocus();
        if (findFocus == null || !findFocus.requestFocus(direction, previouslyFocusedRect)) {
            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
        return true;
    }
}
