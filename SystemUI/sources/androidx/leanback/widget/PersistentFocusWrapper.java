package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
class PersistentFocusWrapper extends FrameLayout {
    private int mSelectedPosition = -1;
    private boolean mPersistFocusVertical = true;

    public PersistentFocusWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int getGrandChildCount() {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null) {
            return 0;
        }
        return viewGroup.getChildCount();
    }

    private boolean shouldPersistFocusFromDirection(int direction) {
        boolean z = this.mPersistFocusVertical;
        return (z && (direction == 33 || direction == 130)) || (!z && (direction == 17 || direction == 66));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (hasFocus() || getGrandChildCount() == 0 || !shouldPersistFocusFromDirection(direction)) {
            super.addFocusables(views, direction, focusableMode);
        } else {
            views.add(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        while (focused != null && focused.getParent() != child) {
            focused = (View) focused.getParent();
        }
        this.mSelectedPosition = focused == null ? -1 : ((ViewGroup) child).indexOfChild(focused);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        int i;
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || (i = this.mSelectedPosition) < 0 || i >= getGrandChildCount() || !viewGroup.getChildAt(this.mSelectedPosition).requestFocus(direction, previouslyFocusedRect)) {
            return super.requestFocus(direction, previouslyFocusedRect);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.leanback.widget.PersistentFocusWrapper.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public SavedState mo118createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public SavedState[] mo119newArray(int size) {
                return new SavedState[size];
            }
        };
        int mSelectedPosition;

        SavedState(Parcel in) {
            super(in);
            this.mSelectedPosition = in.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.mSelectedPosition);
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mSelectedPosition = this.mSelectedPosition;
        return savedState;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        this.mSelectedPosition = savedState.mSelectedPosition;
        super.onRestoreInstanceState(savedState.getSuperState());
    }
}
