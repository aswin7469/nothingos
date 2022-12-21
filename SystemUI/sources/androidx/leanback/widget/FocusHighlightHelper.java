package androidx.leanback.widget;

import android.animation.TimeAnimator;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.leanback.C0742R;
import androidx.leanback.graphics.ColorOverlayDimmer;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.recyclerview.widget.RecyclerView;

public class FocusHighlightHelper {
    static boolean isValidZoomIndex(int i) {
        return i == 0 || getResId(i) > 0;
    }

    static int getResId(int i) {
        if (i == 1) {
            return C0742R.fraction.lb_focus_zoom_factor_small;
        }
        if (i == 2) {
            return C0742R.fraction.lb_focus_zoom_factor_medium;
        }
        if (i == 3) {
            return C0742R.fraction.lb_focus_zoom_factor_large;
        }
        if (i != 4) {
            return 0;
        }
        return C0742R.fraction.lb_focus_zoom_factor_xsmall;
    }

    static class FocusAnimator implements TimeAnimator.TimeListener {
        private final TimeAnimator mAnimator;
        private final ColorOverlayDimmer mDimmer;
        private final int mDuration;
        private float mFocusLevel = 0.0f;
        private float mFocusLevelDelta;
        private float mFocusLevelStart;
        private final Interpolator mInterpolator;
        private final float mScaleDiff;
        private final View mView;
        private final ShadowOverlayContainer mWrapper;

        /* access modifiers changed from: package-private */
        public void animateFocus(boolean z, boolean z2) {
            endAnimation();
            float f = z ? 1.0f : 0.0f;
            if (z2) {
                setFocusLevel(f);
                return;
            }
            float f2 = this.mFocusLevel;
            if (f2 != f) {
                this.mFocusLevelStart = f2;
                this.mFocusLevelDelta = f - f2;
                this.mAnimator.start();
            }
        }

        FocusAnimator(View view, float f, boolean z, int i) {
            TimeAnimator timeAnimator = new TimeAnimator();
            this.mAnimator = timeAnimator;
            this.mInterpolator = new AccelerateDecelerateInterpolator();
            this.mView = view;
            this.mDuration = i;
            this.mScaleDiff = f - 1.0f;
            if (view instanceof ShadowOverlayContainer) {
                this.mWrapper = (ShadowOverlayContainer) view;
            } else {
                this.mWrapper = null;
            }
            timeAnimator.setTimeListener(this);
            if (z) {
                this.mDimmer = ColorOverlayDimmer.createDefault(view.getContext());
            } else {
                this.mDimmer = null;
            }
        }

        /* access modifiers changed from: package-private */
        public void setFocusLevel(float f) {
            this.mFocusLevel = f;
            float f2 = (this.mScaleDiff * f) + 1.0f;
            this.mView.setScaleX(f2);
            this.mView.setScaleY(f2);
            ShadowOverlayContainer shadowOverlayContainer = this.mWrapper;
            if (shadowOverlayContainer != null) {
                shadowOverlayContainer.setShadowFocusLevel(f);
            } else {
                ShadowOverlayHelper.setNoneWrapperShadowFocusLevel(this.mView, f);
            }
            ColorOverlayDimmer colorOverlayDimmer = this.mDimmer;
            if (colorOverlayDimmer != null) {
                colorOverlayDimmer.setActiveLevel(f);
                int color = this.mDimmer.getPaint().getColor();
                ShadowOverlayContainer shadowOverlayContainer2 = this.mWrapper;
                if (shadowOverlayContainer2 != null) {
                    shadowOverlayContainer2.setOverlayColor(color);
                } else {
                    ShadowOverlayHelper.setNoneWrapperOverlayColor(this.mView, color);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public float getFocusLevel() {
            return this.mFocusLevel;
        }

        /* access modifiers changed from: package-private */
        public void endAnimation() {
            this.mAnimator.end();
        }

        public void onTimeUpdate(TimeAnimator timeAnimator, long j, long j2) {
            float f;
            int i = this.mDuration;
            if (j >= ((long) i)) {
                this.mAnimator.end();
                f = 1.0f;
            } else {
                f = (float) (((double) j) / ((double) i));
            }
            Interpolator interpolator = this.mInterpolator;
            if (interpolator != null) {
                f = interpolator.getInterpolation(f);
            }
            setFocusLevel(this.mFocusLevelStart + (f * this.mFocusLevelDelta));
        }
    }

    static class BrowseItemFocusHighlight implements FocusHighlightHandler {
        private static final int DURATION_MS = 150;
        private int mScaleIndex;
        private final boolean mUseDimmer;

        BrowseItemFocusHighlight(int i, boolean z) {
            if (FocusHighlightHelper.isValidZoomIndex(i)) {
                this.mScaleIndex = i;
                this.mUseDimmer = z;
                return;
            }
            throw new IllegalArgumentException("Unhandled zoom index");
        }

        private float getScale(Resources resources) {
            int i = this.mScaleIndex;
            if (i == 0) {
                return 1.0f;
            }
            return resources.getFraction(FocusHighlightHelper.getResId(i), 1, 1);
        }

        public void onItemFocused(View view, boolean z) {
            view.setSelected(z);
            getOrCreateAnimator(view).animateFocus(z, false);
        }

        public void onInitializeView(View view) {
            getOrCreateAnimator(view).animateFocus(false, true);
        }

        private FocusAnimator getOrCreateAnimator(View view) {
            FocusAnimator focusAnimator = (FocusAnimator) view.getTag(C0742R.C0745id.lb_focus_animator);
            if (focusAnimator != null) {
                return focusAnimator;
            }
            FocusAnimator focusAnimator2 = new FocusAnimator(view, getScale(view.getResources()), this.mUseDimmer, 150);
            view.setTag(C0742R.C0745id.lb_focus_animator, focusAnimator2);
            return focusAnimator2;
        }
    }

    public static void setupBrowseItemFocusHighlight(ItemBridgeAdapter itemBridgeAdapter, int i, boolean z) {
        if (i != 0 || z) {
            itemBridgeAdapter.setFocusHighlight(new BrowseItemFocusHighlight(i, z));
        } else {
            itemBridgeAdapter.setFocusHighlight((FocusHighlightHandler) null);
        }
    }

    @Deprecated
    public static void setupHeaderItemFocusHighlight(VerticalGridView verticalGridView) {
        setupHeaderItemFocusHighlight(verticalGridView, true);
    }

    @Deprecated
    public static void setupHeaderItemFocusHighlight(VerticalGridView verticalGridView, boolean z) {
        if (verticalGridView != null && (verticalGridView.getAdapter() instanceof ItemBridgeAdapter)) {
            ((ItemBridgeAdapter) verticalGridView.getAdapter()).setFocusHighlight(z ? new HeaderItemFocusHighlight() : null);
        }
    }

    public static void setupHeaderItemFocusHighlight(ItemBridgeAdapter itemBridgeAdapter) {
        setupHeaderItemFocusHighlight(itemBridgeAdapter, true);
    }

    public static void setupHeaderItemFocusHighlight(ItemBridgeAdapter itemBridgeAdapter, boolean z) {
        itemBridgeAdapter.setFocusHighlight(z ? new HeaderItemFocusHighlight() : null);
    }

    static class HeaderItemFocusHighlight implements FocusHighlightHandler {
        private int mDuration;
        private boolean mInitialized;
        private float mSelectScale;

        public void onInitializeView(View view) {
        }

        HeaderItemFocusHighlight() {
        }

        /* access modifiers changed from: package-private */
        public void lazyInit(View view) {
            if (!this.mInitialized) {
                Resources resources = view.getResources();
                TypedValue typedValue = new TypedValue();
                resources.getValue(C0742R.dimen.lb_browse_header_select_scale, typedValue, true);
                this.mSelectScale = typedValue.getFloat();
                resources.getValue(C0742R.dimen.lb_browse_header_select_duration, typedValue, true);
                this.mDuration = typedValue.data;
                this.mInitialized = true;
            }
        }

        static class HeaderFocusAnimator extends FocusAnimator {
            ItemBridgeAdapter.ViewHolder mViewHolder;

            HeaderFocusAnimator(View view, float f, int i) {
                super(view, f, false, i);
                ViewParent parent = view.getParent();
                while (parent != null && !(parent instanceof RecyclerView)) {
                    parent = parent.getParent();
                }
                if (parent != null) {
                    this.mViewHolder = (ItemBridgeAdapter.ViewHolder) ((RecyclerView) parent).getChildViewHolder(view);
                }
            }

            /* access modifiers changed from: package-private */
            public void setFocusLevel(float f) {
                Presenter presenter = this.mViewHolder.getPresenter();
                if (presenter instanceof RowHeaderPresenter) {
                    ((RowHeaderPresenter) presenter).setSelectLevel((RowHeaderPresenter.ViewHolder) this.mViewHolder.getViewHolder(), f);
                }
                super.setFocusLevel(f);
            }
        }

        private void viewFocused(View view, boolean z) {
            lazyInit(view);
            view.setSelected(z);
            FocusAnimator focusAnimator = (FocusAnimator) view.getTag(C0742R.C0745id.lb_focus_animator);
            if (focusAnimator == null) {
                focusAnimator = new HeaderFocusAnimator(view, this.mSelectScale, this.mDuration);
                view.setTag(C0742R.C0745id.lb_focus_animator, focusAnimator);
            }
            focusAnimator.animateFocus(z, false);
        }

        public void onItemFocused(View view, boolean z) {
            viewFocused(view, z);
        }
    }
}
