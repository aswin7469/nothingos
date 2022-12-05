package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.leanback.R$id;
/* loaded from: classes.dex */
public class PlaybackTransportRowView extends LinearLayout {
    private OnUnhandledKeyListener mOnUnhandledKeyListener;

    /* loaded from: classes.dex */
    public interface OnUnhandledKeyListener {
        boolean onUnhandledKey(KeyEvent event);
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public PlaybackTransportRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaybackTransportRowView(Context context, AttributeSet attrs, int defStyle) {
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
            View findViewById = findViewById(R$id.playback_progress);
            if (findViewById != null && findViewById.isFocusable() && findViewById.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        View childAt;
        if (focused != null) {
            if (direction == 33) {
                for (int indexOfChild = indexOfChild(getFocusedChild()) - 1; indexOfChild >= 0; indexOfChild--) {
                    View childAt2 = getChildAt(indexOfChild);
                    if (childAt2.hasFocusable()) {
                        return childAt2;
                    }
                }
            } else if (direction == 130) {
                int indexOfChild2 = indexOfChild(getFocusedChild());
                do {
                    indexOfChild2++;
                    if (indexOfChild2 < getChildCount()) {
                        childAt = getChildAt(indexOfChild2);
                    }
                } while (!childAt.hasFocusable());
                return childAt;
            } else if ((direction == 17 || direction == 66) && (getFocusedChild() instanceof ViewGroup)) {
                return FocusFinder.getInstance().findNextFocus((ViewGroup) getFocusedChild(), focused, direction);
            }
        }
        return super.focusSearch(focused, direction);
    }
}
