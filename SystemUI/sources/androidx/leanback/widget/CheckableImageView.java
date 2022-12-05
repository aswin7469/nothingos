package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
class CheckableImageView extends ImageView implements Checkable {
    private static final int[] CHECKED_STATE_SET = {16842912};
    private boolean mChecked;

    public CheckableImageView(Context context) {
        this(context, null);
    }

    public CheckableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override // android.widget.ImageView, android.view.View
    public int[] onCreateDrawableState(final int extraSpace) {
        int[] onCreateDrawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            ImageView.mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.mChecked);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.mChecked;
    }

    @Override // android.widget.Checkable
    public void setChecked(final boolean checked) {
        if (this.mChecked != checked) {
            this.mChecked = checked;
            refreshDrawableState();
        }
    }
}
