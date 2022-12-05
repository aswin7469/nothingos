package androidx.recyclerview.widget;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public abstract class SnapHelper extends RecyclerView.OnFlingListener {
    private Scroller mGravityScroller;
    RecyclerView mRecyclerView;
    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() { // from class: androidx.recyclerview.widget.SnapHelper.1
        boolean mScrolled = false;

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState != 0 || !this.mScrolled) {
                return;
            }
            this.mScrolled = false;
            SnapHelper.this.snapToTargetExistingView();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx == 0 && dy == 0) {
                return;
            }
            this.mScrolled = true;
        }
    };

    public abstract int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View targetView);

    /* renamed from: createScroller */
    protected abstract RecyclerView.SmoothScroller mo1503createScroller(RecyclerView.LayoutManager layoutManager);

    public abstract View findSnapView(RecyclerView.LayoutManager layoutManager);

    public abstract int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY);

    @Override // androidx.recyclerview.widget.RecyclerView.OnFlingListener
    public boolean onFling(int velocityX, int velocityY) {
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager == null || this.mRecyclerView.getAdapter() == null) {
            return false;
        }
        int minFlingVelocity = this.mRecyclerView.getMinFlingVelocity();
        return (Math.abs(velocityY) > minFlingVelocity || Math.abs(velocityX) > minFlingVelocity) && snapFromFling(layoutManager, velocityX, velocityY);
    }

    public void attachToRecyclerView(RecyclerView recyclerView) throws IllegalStateException {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (recyclerView == null) {
            return;
        }
        setupCallbacks();
        this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
        snapToTargetExistingView();
    }

    private void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() != null) {
            throw new IllegalStateException("An instance of OnFlingListener already set.");
        }
        this.mRecyclerView.addOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(this);
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(null);
    }

    private boolean snapFromFling(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        RecyclerView.SmoothScroller mo1503createScroller;
        int findTargetSnapPosition;
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) || (mo1503createScroller = mo1503createScroller(layoutManager)) == null || (findTargetSnapPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY)) == -1) {
            return false;
        }
        mo1503createScroller.setTargetPosition(findTargetSnapPosition);
        layoutManager.startSmoothScroll(mo1503createScroller);
        return true;
    }

    void snapToTargetExistingView() {
        RecyclerView.LayoutManager layoutManager;
        View findSnapView;
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView == null || (layoutManager = recyclerView.getLayoutManager()) == null || (findSnapView = findSnapView(layoutManager)) == null) {
            return;
        }
        int[] calculateDistanceToFinalSnap = calculateDistanceToFinalSnap(layoutManager, findSnapView);
        if (calculateDistanceToFinalSnap[0] == 0 && calculateDistanceToFinalSnap[1] == 0) {
            return;
        }
        this.mRecyclerView.smoothScrollBy(calculateDistanceToFinalSnap[0], calculateDistanceToFinalSnap[1]);
    }
}
