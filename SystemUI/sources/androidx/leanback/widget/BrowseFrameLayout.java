package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class BrowseFrameLayout extends FrameLayout {
    private OnFocusSearchListener mListener;
    private OnChildFocusListener mOnChildFocusListener;
    private View.OnKeyListener mOnDispatchKeyListener;

    /* loaded from: classes.dex */
    public interface OnChildFocusListener {
        void onRequestChildFocus(View child, View focused);

        boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect);
    }

    /* loaded from: classes.dex */
    public interface OnFocusSearchListener {
        View onFocusSearch(View focused, int direction);
    }

    public BrowseFrameLayout(Context context) {
        this(context, null, 0);
    }

    public BrowseFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrowseFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        OnChildFocusListener onChildFocusListener = this.mOnChildFocusListener;
        if (onChildFocusListener == null || !onChildFocusListener.onRequestFocusInDescendants(direction, previouslyFocusedRect)) {
            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        View onFocusSearch;
        OnFocusSearchListener onFocusSearchListener = this.mListener;
        return (onFocusSearchListener == null || (onFocusSearch = onFocusSearchListener.onFocusSearch(focused, direction)) == null) ? super.focusSearch(focused, direction) : onFocusSearch;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        OnChildFocusListener onChildFocusListener = this.mOnChildFocusListener;
        if (onChildFocusListener != null) {
            onChildFocusListener.onRequestChildFocus(child, focused);
        }
        super.requestChildFocus(child, focused);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        View.OnKeyListener onKeyListener = this.mOnDispatchKeyListener;
        return (onKeyListener == null || dispatchKeyEvent) ? dispatchKeyEvent : onKeyListener.onKey(getRootView(), event.getKeyCode(), event);
    }
}
