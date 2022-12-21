package androidx.leanback.widget;

import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import androidx.leanback.widget.Parallax;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewParallax extends Parallax<ChildPositionProperty> {
    boolean mIsVertical;
    View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            RecyclerViewParallax.this.updateValues();
        }
    };
    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            RecyclerViewParallax.this.updateValues();
        }
    };
    RecyclerView mRecylerView;

    public static final class ChildPositionProperty extends Parallax.IntProperty {
        int mAdapterPosition;
        float mFraction;
        int mOffset;
        int mViewId;

        ChildPositionProperty(String str, int i) {
            super(str, i);
        }

        public ChildPositionProperty adapterPosition(int i) {
            this.mAdapterPosition = i;
            return this;
        }

        public ChildPositionProperty viewId(int i) {
            this.mViewId = i;
            return this;
        }

        public ChildPositionProperty offset(int i) {
            this.mOffset = i;
            return this;
        }

        public ChildPositionProperty fraction(float f) {
            this.mFraction = f;
            return this;
        }

        public int getAdapterPosition() {
            return this.mAdapterPosition;
        }

        public int getViewId() {
            return this.mViewId;
        }

        public int getOffset() {
            return this.mOffset;
        }

        public float getFraction() {
            return this.mFraction;
        }

        /* access modifiers changed from: package-private */
        public void updateValue(RecyclerViewParallax recyclerViewParallax) {
            RecyclerView.ViewHolder viewHolder;
            RecyclerView recyclerView = recyclerViewParallax.mRecylerView;
            if (recyclerView == null) {
                viewHolder = null;
            } else {
                viewHolder = recyclerView.findViewHolderForAdapterPosition(this.mAdapterPosition);
            }
            if (viewHolder != null) {
                View findViewById = viewHolder.itemView.findViewById(this.mViewId);
                if (findViewById != null) {
                    Rect rect = new Rect(0, 0, findViewById.getWidth(), findViewById.getHeight());
                    recyclerView.offsetDescendantRectToMyCoords(findViewById, rect);
                    float f = 0.0f;
                    float f2 = 0.0f;
                    while (findViewById != recyclerView && findViewById != null) {
                        if (findViewById.getParent() != recyclerView || !recyclerView.isAnimating()) {
                            f += findViewById.getTranslationX();
                            f2 += findViewById.getTranslationY();
                        }
                        findViewById = (View) findViewById.getParent();
                    }
                    rect.offset((int) f, (int) f2);
                    if (recyclerViewParallax.mIsVertical) {
                        recyclerViewParallax.setIntPropertyValue(getIndex(), rect.top + this.mOffset + ((int) (this.mFraction * ((float) rect.height()))));
                    } else {
                        recyclerViewParallax.setIntPropertyValue(getIndex(), rect.left + this.mOffset + ((int) (this.mFraction * ((float) rect.width()))));
                    }
                }
            } else if (recyclerView == null || recyclerView.getLayoutManager().getChildCount() == 0) {
                recyclerViewParallax.setIntPropertyValue(getIndex(), Integer.MAX_VALUE);
            } else if (recyclerView.findContainingViewHolder(recyclerView.getLayoutManager().getChildAt(0)).getAbsoluteAdapterPosition() < this.mAdapterPosition) {
                recyclerViewParallax.setIntPropertyValue(getIndex(), Integer.MAX_VALUE);
            } else {
                recyclerViewParallax.setIntPropertyValue(getIndex(), Integer.MIN_VALUE);
            }
        }
    }

    public ChildPositionProperty createProperty(String str, int i) {
        return new ChildPositionProperty(str, i);
    }

    public float getMaxValue() {
        RecyclerView recyclerView = this.mRecylerView;
        if (recyclerView == null) {
            return 0.0f;
        }
        return (float) (this.mIsVertical ? recyclerView.getHeight() : recyclerView.getWidth());
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mRecylerView;
        if (recyclerView2 != recyclerView) {
            if (recyclerView2 != null) {
                recyclerView2.removeOnScrollListener(this.mOnScrollListener);
                this.mRecylerView.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
            }
            this.mRecylerView = recyclerView;
            if (recyclerView != null) {
                recyclerView.getLayoutManager();
                boolean z = false;
                if (RecyclerView.LayoutManager.getProperties(this.mRecylerView.getContext(), (AttributeSet) null, 0, 0).orientation == 1) {
                    z = true;
                }
                this.mIsVertical = z;
                this.mRecylerView.addOnScrollListener(this.mOnScrollListener);
                this.mRecylerView.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
            }
        }
    }

    public void updateValues() {
        for (Property property : getProperties()) {
            ((ChildPositionProperty) property).updateValue(this);
        }
        super.updateValues();
    }

    public RecyclerView getRecyclerView() {
        return this.mRecylerView;
    }
}
