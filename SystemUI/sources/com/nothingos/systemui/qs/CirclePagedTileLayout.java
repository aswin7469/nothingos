package com.nothingos.systemui.qs;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.PageIndicator;
import com.android.systemui.qs.QSAnimator;
import com.android.systemui.qs.QSEvent;
import com.android.systemui.qs.QSEvents;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class CirclePagedTileLayout extends ViewPager implements QSPanel.QSTileLayout {
    private static final Interpolator SCROLL_CUBIC = CirclePagedTileLayout$$ExternalSyntheticLambda0.INSTANCE;
    private final PagerAdapter mAdapter;
    private AnimatorSet mBounceAnimatorSet;
    private boolean mDistributeTiles;
    private boolean mHasUnselected;
    private boolean mIsSignal;
    private float mLastExpansion;
    private int mLayoutDirection;
    private int mLayoutOrientation;
    private boolean mListening;
    private int mMaxColumns;
    private int mMinRows;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener;
    private PageIndicator mPageIndicator;
    private float mPageIndicatorPosition;
    private int mPageToRestore;
    private final ArrayList<QSTileView> mPages;
    private Path mPath;
    private QSAnimator mQSAnimator;
    private Scroller mScroller;
    private final ArrayList<QSPanelControllerBase.TileRecord> mTiles;
    private final UiEventLogger mUiEventLogger;

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2) + 1.0f;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean updateResources() {
        return false;
    }

    public CirclePagedTileLayout(Context context) {
        this(context, null);
    }

    public CirclePagedTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTiles = new ArrayList<>();
        this.mPages = new ArrayList<>();
        this.mDistributeTiles = false;
        this.mPageToRestore = -1;
        this.mUiEventLogger = QSEvents.INSTANCE.getQsUiEventsLogger();
        this.mMinRows = 1;
        this.mMaxColumns = 100;
        this.mPath = new Path();
        ViewPager.SimpleOnPageChangeListener simpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() { // from class: com.nothingos.systemui.qs.CirclePagedTileLayout.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                CirclePagedTileLayout.this.updateSelected();
                if (CirclePagedTileLayout.this.mPageIndicator == null || CirclePagedTileLayout.this.mQSAnimator == null) {
                    return;
                }
                if (CirclePagedTileLayout.this.mIsSignal) {
                    QSAnimator qSAnimator = CirclePagedTileLayout.this.mQSAnimator;
                    if (CirclePagedTileLayout.this.isLayoutRtl()) {
                        i = (CirclePagedTileLayout.this.mPages.size() - 1) - i;
                    }
                    qSAnimator.onSignalPageChanged(i);
                    return;
                }
                QSAnimator qSAnimator2 = CirclePagedTileLayout.this.mQSAnimator;
                if (CirclePagedTileLayout.this.isLayoutRtl()) {
                    i = (CirclePagedTileLayout.this.mPages.size() - 1) - i;
                }
                qSAnimator2.onBtPageChanged(i);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                if (CirclePagedTileLayout.this.mPageIndicator == null) {
                    return;
                }
                CirclePagedTileLayout.this.mPageIndicatorPosition = i + f;
                CirclePagedTileLayout.this.mPageIndicator.setLocation(CirclePagedTileLayout.this.mPageIndicatorPosition);
                if (CirclePagedTileLayout.this.mQSAnimator == null) {
                    return;
                }
                if (CirclePagedTileLayout.this.mIsSignal) {
                    QSAnimator qSAnimator = CirclePagedTileLayout.this.mQSAnimator;
                    if (CirclePagedTileLayout.this.isLayoutRtl()) {
                        i = (CirclePagedTileLayout.this.mPages.size() - 1) - i;
                    }
                    qSAnimator.onSignalPageChanged(i);
                    return;
                }
                QSAnimator qSAnimator2 = CirclePagedTileLayout.this.mQSAnimator;
                if (CirclePagedTileLayout.this.isLayoutRtl()) {
                    i = (CirclePagedTileLayout.this.mPages.size() - 1) - i;
                }
                qSAnimator2.onBtPageChanged(i);
            }
        };
        this.mOnPageChangeListener = simpleOnPageChangeListener;
        PagerAdapter pagerAdapter = new PagerAdapter() { // from class: com.nothingos.systemui.qs.CirclePagedTileLayout.2
            @Override // androidx.viewpager.widget.PagerAdapter
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
                Log.d("CirclePagedTileLayout", "Destantiating " + i);
                viewGroup.removeView((View) obj);
                CirclePagedTileLayout.this.updateListening();
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public Object instantiateItem(ViewGroup viewGroup, int i) {
                Log.d("CirclePagedTileLayout", "Instantiating " + i);
                if (CirclePagedTileLayout.this.isLayoutRtl()) {
                    i = (CirclePagedTileLayout.this.mPages.size() - 1) - i;
                }
                ViewGroup viewGroup2 = (ViewGroup) CirclePagedTileLayout.this.mPages.get(i);
                if (viewGroup2.getParent() != null) {
                    viewGroup.removeView(viewGroup2);
                }
                viewGroup.addView(viewGroup2);
                CirclePagedTileLayout.this.updateListening();
                return viewGroup2;
            }

            @Override // androidx.viewpager.widget.PagerAdapter
            public int getCount() {
                return CirclePagedTileLayout.this.mPages.size();
            }
        };
        this.mAdapter = pagerAdapter;
        this.mScroller = new Scroller(context, SCROLL_CUBIC);
        setAdapter(pagerAdapter);
        setOnPageChangeListener(simpleOnPageChangeListener);
        setCurrentItem(0, false);
        this.mLayoutOrientation = getResources().getConfiguration().orientation;
        this.mLayoutDirection = getLayoutDirection();
        setOverScrollMode(2);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        float f = this.mLastExpansion;
        if (f == 0.0f || f == 1.0f) {
            this.mPath.reset();
            Rect clipBounds = canvas.getClipBounds();
            this.mPath.addCircle((clipBounds.left + clipBounds.right) / 2, getHeight() / 2, Math.min(getWidth() / 2, getHeight() / 2), Path.Direction.CW);
            canvas.clipPath(this.mPath);
        }
        super.dispatchDraw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void saveInstanceState(Bundle bundle) {
        bundle.putInt("current_page_circle", getCurrentItem());
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void restoreInstanceState(Bundle bundle) {
        this.mPageToRestore = bundle.getInt("current_page_circle", -1);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = this.mLayoutOrientation;
        int i2 = configuration.orientation;
        if (i != i2) {
            this.mLayoutOrientation = i2;
            setCurrentItem(0, false);
            this.mPageToRestore = 0;
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.mLayoutDirection != i) {
            this.mLayoutDirection = i;
            setAdapter(this.mAdapter);
            setCurrentItem(0, false);
            this.mPageToRestore = 0;
        }
    }

    @Override // androidx.viewpager.widget.ViewPager
    public void setCurrentItem(int i, boolean z) {
        if (isLayoutRtl()) {
            i = (this.mPages.size() - 1) - i;
        }
        super.setCurrentItem(i, z);
    }

    private int getCurrentPageNumber() {
        int currentItem = getCurrentItem();
        return this.mLayoutDirection == 1 ? (this.mPages.size() - 1) - currentItem : currentItem;
    }

    private void logVisibleTiles(QSTileView qSTileView) {
        Iterator<QSPanelControllerBase.TileRecord> it = this.mTiles.iterator();
        while (it.hasNext()) {
            QSPanelControllerBase.TileRecord next = it.next();
            if (next.tileView.equals(qSTileView)) {
                QSTile qSTile = next.tile;
                this.mUiEventLogger.logWithInstanceId(QSEvent.QS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
            }
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void setListening(boolean z, UiEventLogger uiEventLogger) {
        if (this.mListening == z) {
            return;
        }
        this.mListening = z;
        updateListening();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateListening() {
        Iterator<QSPanelControllerBase.TileRecord> it = this.mTiles.iterator();
        while (it.hasNext()) {
            QSPanelControllerBase.TileRecord next = it.next();
            next.tile.setListening(this, next.tileView.getParent() != null && this.mListening);
        }
    }

    @Override // androidx.viewpager.widget.ViewPager
    public void fakeDragBy(float f) {
        try {
            super.fakeDragBy(f);
            postInvalidateOnAnimation();
        } catch (NullPointerException e) {
            Log.e("CirclePagedTileLayout", "FakeDragBy called before begin", e);
            final int size = this.mPages.size() - 1;
            post(new Runnable() { // from class: com.nothingos.systemui.qs.CirclePagedTileLayout$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    CirclePagedTileLayout.this.lambda$fakeDragBy$1(size);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fakeDragBy$1(int i) {
        setCurrentItem(i, true);
        AnimatorSet animatorSet = this.mBounceAnimatorSet;
        if (animatorSet != null) {
            animatorSet.start();
        }
        setOffscreenPageLimit(1);
    }

    @Override // androidx.viewpager.widget.ViewPager
    public void endFakeDrag() {
        try {
            super.endFakeDrag();
        } catch (NullPointerException e) {
            Log.e("CirclePagedTileLayout", "endFakeDrag called without velocityTracker", e);
        }
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            if (!isFakeDragging()) {
                beginFakeDrag();
            }
            fakeDragBy(getScrollX() - this.mScroller.getCurrX());
        } else if (isFakeDragging()) {
            endFakeDrag();
            AnimatorSet animatorSet = this.mBounceAnimatorSet;
            if (animatorSet != null) {
                animatorSet.start();
            }
            setOffscreenPageLimit(1);
        }
        super.computeScroll();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setPageIndicator(PageIndicator pageIndicator) {
        this.mPageIndicator = pageIndicator;
        pageIndicator.setNumPages(this.mPages.size());
        this.mPageIndicator.setLocation(this.mPageIndicatorPosition);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public int getOffsetTop(QSPanelControllerBase.TileRecord tileRecord) {
        ViewGroup viewGroup = (ViewGroup) tileRecord.tileView.getParent();
        if (viewGroup == null) {
            return 0;
        }
        return viewGroup.getTop() + getTop();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void addTile(QSPanelControllerBase.TileRecord tileRecord) {
        synchronized (this) {
            if (this.mTiles.contains(tileRecord)) {
                return;
            }
            this.mTiles.add(tileRecord);
            this.mPages.add(tileRecord.tileView);
            setAdapter(this.mAdapter);
            this.mAdapter.notifyDataSetChanged();
            PageIndicator pageIndicator = this.mPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(this.mPages.size());
            }
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        synchronized (this) {
            if (this.mTiles.remove(tileRecord) && this.mPages.remove(tileRecord.tileView)) {
                setAdapter(this.mAdapter);
                this.mAdapter.notifyDataSetChanged();
            }
            PageIndicator pageIndicator = this.mPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(this.mPages.size());
            }
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void setExpansion(float f, float f2) {
        this.mLastExpansion = f;
        updateSelected();
        for (int i = 0; i < this.mPages.size(); i++) {
            ((QSTileViewCircle) this.mPages.get(i)).setExpansion(f);
            if (f == 1.0f) {
                invalidate();
                Log.d("CirclePagedTileLayout", "setExpansion: mLastExpansion = " + this.mLastExpansion + " try again dispatchDraw");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelected() {
        float f = this.mLastExpansion;
        boolean z = true;
        if (f > 0.0f && f < 1.0f) {
            if (this.mHasUnselected) {
                return;
            }
            for (int i = 0; i < this.mPages.size(); i++) {
                Log.d("CirclePagedTileLayout", "updateSelected: mHasUnselected");
                this.mPages.get(i).setSelected(false);
            }
            setSelected(false);
            this.mHasUnselected = true;
            return;
        }
        if (f != 1.0f) {
            z = false;
        }
        this.mHasUnselected = false;
        setImportantForAccessibility(4);
        int currentPageNumber = getCurrentPageNumber();
        int i2 = 0;
        while (i2 < this.mPages.size()) {
            QSTileView qSTileView = this.mPages.get(i2);
            qSTileView.setSelected(i2 == currentPageNumber ? z : false);
            if (qSTileView.isSelected()) {
                logVisibleTiles(qSTileView);
            }
            i2++;
        }
        setImportantForAccessibility(0);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public int getNumVisibleTiles() {
        return this.mPages.size();
    }

    public void markSignal() {
        this.mIsSignal = true;
    }

    public int getBtTileIndex(QSTileView qSTileView) {
        for (int i = 0; i < this.mPages.size(); i++) {
            if (this.mPages.get(i).equals(qSTileView)) {
                return i;
            }
        }
        return -1;
    }

    public QSTileView getBtTile(int i) {
        if (i < this.mPages.size()) {
            return this.mPages.get(i);
        }
        return null;
    }

    public void setQSAnimator(QSAnimator qSAnimator) {
        this.mQSAnimator = qSAnimator;
    }

    public PageIndicator getPageIndicator() {
        return this.mPageIndicator;
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mPages.size() == 1) {
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }
}
