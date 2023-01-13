package com.nothing.systemui.p024qs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.p012qs.PageIndicator;
import com.android.systemui.p012qs.QSEvent;
import com.android.systemui.p012qs.QSEvents;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.QSPanelControllerBase;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.nothing.systemui.qs.CirclePagedTileLayout */
public class CirclePagedTileLayout extends ViewPager implements QSPanel.QSTileLayout {
    private static final long BOUNCE_ANIMATION_DURATION = 450;
    private static final float BOUNCE_ANIMATION_TENSION = 1.3f;
    private static final String CURRENT_PAGE = "current_page_circle";
    private static final boolean DEBUG = true;
    private static final int REVEAL_SCROLL_DURATION_MILLIS = 750;
    private static final Interpolator SCROLL_CUBIC = new CirclePagedTileLayout$$ExternalSyntheticLambda1();
    private static final String TAG = "CirclePagedTileLayout";
    private static final int TILE_ANIMATION_STAGGER_DELAY = 85;
    private final PagerAdapter mAdapter;
    private AnimatorSet mBounceAnimatorSet;
    private boolean mDistributeTiles;
    private QSFragmentEx mEx;
    private int mExcessHeight;
    private boolean mHasUnselected;
    private boolean mIsNightMode;
    private boolean mIsSignal;
    private int mLastExcessHeight;
    private float mLastExpansion;
    private int mLayoutDirection;
    private int mLayoutOrientation;
    private boolean mListening;
    private int mMaxColumns;
    private int mMinRows;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener;
    /* access modifiers changed from: private */
    public PageIndicator mPageIndicator;
    /* access modifiers changed from: private */
    public float mPageIndicatorPosition;
    private PageListener mPageListener;
    private int mPageToRestore;
    /* access modifiers changed from: private */
    public final ArrayList<QSTileView> mPages;
    private Path mPath;
    private NTQSAnimator mQSAnimator;
    private Scroller mScroller;
    private final ArrayList<QSPanelControllerBase.TileRecord> mTiles;
    private final UiEventLogger mUiEventLogger;

    /* renamed from: com.nothing.systemui.qs.CirclePagedTileLayout$PageListener */
    public interface PageListener {
        void onPageChanged(boolean z);
    }

    static /* synthetic */ float lambda$static$0(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2) + 1.0f;
    }

    public int getTilesHeight() {
        return 0;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public void setSquishinessFraction(float f) {
    }

    public boolean updateResources() {
        return false;
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public CirclePagedTileLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CirclePagedTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTiles = new ArrayList<>();
        this.mPages = new ArrayList<>();
        boolean z = false;
        this.mDistributeTiles = false;
        this.mPageToRestore = -1;
        this.mUiEventLogger = QSEvents.INSTANCE.getQsUiEventsLogger();
        this.mMinRows = 1;
        this.mMaxColumns = 100;
        this.mPath = new Path();
        this.mEx = (QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class);
        C41921 r1 = new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int i) {
                CirclePagedTileLayout.this.updateSelected();
                if (CirclePagedTileLayout.this.mPageIndicator != null) {
                    CirclePagedTileLayout circlePagedTileLayout = CirclePagedTileLayout.this;
                    if (circlePagedTileLayout.isLayoutRtl()) {
                        i = (CirclePagedTileLayout.this.mPages.size() - 1) - i;
                    }
                    circlePagedTileLayout.onPageChanged(i);
                }
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (CirclePagedTileLayout.this.mPageIndicator != null) {
                    float unused = CirclePagedTileLayout.this.mPageIndicatorPosition = ((float) i) + f;
                    CirclePagedTileLayout.this.mPageIndicator.setLocation(CirclePagedTileLayout.this.mPageIndicatorPosition);
                    CirclePagedTileLayout circlePagedTileLayout = CirclePagedTileLayout.this;
                    if (circlePagedTileLayout.isLayoutRtl()) {
                        i = (CirclePagedTileLayout.this.mPages.size() - 1) - i;
                    }
                    circlePagedTileLayout.onPageChanged(i);
                }
            }
        };
        this.mOnPageChangeListener = r1;
        C41932 r2 = new PagerAdapter() {
            public boolean isViewFromObject(View view, Object obj) {
                return view == obj;
            }

            public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
                NTLogUtil.m1686d(CirclePagedTileLayout.TAG, "Destantiating " + i);
                viewGroup.removeView((View) obj);
                CirclePagedTileLayout.this.updateListening();
            }

            public Object instantiateItem(ViewGroup viewGroup, int i) {
                NTLogUtil.m1686d(CirclePagedTileLayout.TAG, "Instantiating " + i);
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

            public int getCount() {
                return CirclePagedTileLayout.this.mPages.size();
            }
        };
        this.mAdapter = r2;
        this.mScroller = new Scroller(context, SCROLL_CUBIC);
        setAdapter(r2);
        setOnPageChangeListener(r1);
        setCurrentItem(0, false);
        this.mLayoutOrientation = getResources().getConfiguration().orientation;
        this.mLayoutDirection = getLayoutDirection();
        this.mIsNightMode = (getResources().getConfiguration().uiMode & 48) == 32 ? true : z;
        setOverScrollMode(2);
    }

    public void setPageMargin(int i) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int i2 = -i;
        marginLayoutParams.setMarginStart(i2);
        marginLayoutParams.setMarginEnd(i2);
        setLayoutParams(marginLayoutParams);
        int size = this.mPages.size();
        for (int i3 = 0; i3 < size; i3++) {
            View view = this.mPages.get(i3);
            view.setPadding(i, view.getPaddingTop(), i, view.getPaddingBottom());
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        float f = this.mLastExpansion;
        if (f == 0.0f || f == 1.0f) {
            this.mPath.reset();
            Rect clipBounds = canvas.getClipBounds();
            this.mPath.addCircle((float) ((clipBounds.left + clipBounds.right) / 2), (float) (getHeight() / 2), (float) Math.min(getWidth() / 2, getHeight() / 2), Path.Direction.CW);
            canvas.clipPath(this.mPath);
        }
        super.dispatchDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
    }

    public void saveInstanceState(Bundle bundle) {
        bundle.putInt(CURRENT_PAGE, getCurrentItem());
    }

    public void restoreInstanceState(Bundle bundle) {
        this.mPageToRestore = bundle.getInt(CURRENT_PAGE, -1);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean z = (configuration.uiMode & 48) == 32;
        if (!(this.mIsNightMode == z && this.mLayoutOrientation == configuration.orientation) && this.mIsSignal) {
            this.mLayoutOrientation = configuration.orientation;
            this.mIsNightMode = z;
            return;
        }
        setAdapter(this.mAdapter);
        setCurrentItem(0, false);
        this.mPageToRestore = 0;
    }

    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.mLayoutDirection != i) {
            this.mLayoutDirection = i;
        }
    }

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

    public void setListening(boolean z, UiEventLogger uiEventLogger) {
        if (this.mListening != z) {
            this.mListening = z;
            updateListening();
        }
    }

    /* access modifiers changed from: private */
    public void updateListening() {
        Iterator<QSPanelControllerBase.TileRecord> it = this.mTiles.iterator();
        while (it.hasNext()) {
            QSPanelControllerBase.TileRecord next = it.next();
            next.tile.setListening(this, next.tileView.getParent() != null && this.mListening);
        }
    }

    public void fakeDragBy(float f) {
        try {
            super.fakeDragBy(f);
            postInvalidateOnAnimation();
        } catch (NullPointerException e) {
            NTLogUtil.m1687e(TAG, "FakeDragBy called before begin: " + e);
            post(new CirclePagedTileLayout$$ExternalSyntheticLambda0(this, this.mPages.size() - 1));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fakeDragBy$1$com-nothing-systemui-qs-CirclePagedTileLayout */
    public /* synthetic */ void mo57613xeb001256(int i) {
        setCurrentItem(i, true);
        AnimatorSet animatorSet = this.mBounceAnimatorSet;
        if (animatorSet != null) {
            animatorSet.start();
        }
        setOffscreenPageLimit(1);
    }

    public void endFakeDrag() {
        try {
            super.endFakeDrag();
        } catch (NullPointerException e) {
            NTLogUtil.m1687e(TAG, "endFakeDrag called without velocityTracker: " + e);
        }
    }

    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            if (!isFakeDragging()) {
                beginFakeDrag();
            }
            fakeDragBy((float) (getScrollX() - this.mScroller.getCurrX()));
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

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setPageIndicator(PageIndicator pageIndicator) {
        this.mPageIndicator = pageIndicator;
        pageIndicator.setNumPages(this.mPages.size());
        this.mPageIndicator.setLocation(this.mPageIndicatorPosition);
    }

    public int getOffsetTop(QSPanelControllerBase.TileRecord tileRecord) {
        ViewGroup viewGroup = (ViewGroup) tileRecord.tileView.getParent();
        if (viewGroup == null) {
            return 0;
        }
        return viewGroup.getTop() + getTop();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addTile(com.android.systemui.p012qs.QSPanelControllerBase.TileRecord r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            java.util.ArrayList<com.android.systemui.qs.QSPanelControllerBase$TileRecord> r0 = r1.mTiles     // Catch:{ all -> 0x0030 }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x0030 }
            if (r0 == 0) goto L_0x000b
            monitor-exit(r1)     // Catch:{ all -> 0x0030 }
            return
        L_0x000b:
            java.util.ArrayList<com.android.systemui.qs.QSPanelControllerBase$TileRecord> r0 = r1.mTiles     // Catch:{ all -> 0x0030 }
            r0.add(r2)     // Catch:{ all -> 0x0030 }
            java.util.ArrayList<com.android.systemui.plugins.qs.QSTileView> r0 = r1.mPages     // Catch:{ all -> 0x0030 }
            com.android.systemui.plugins.qs.QSTileView r2 = r2.tileView     // Catch:{ all -> 0x0030 }
            r0.add(r2)     // Catch:{ all -> 0x0030 }
            androidx.viewpager.widget.PagerAdapter r2 = r1.mAdapter     // Catch:{ all -> 0x0030 }
            r1.setAdapter(r2)     // Catch:{ all -> 0x0030 }
            androidx.viewpager.widget.PagerAdapter r2 = r1.mAdapter     // Catch:{ all -> 0x0030 }
            r2.notifyDataSetChanged()     // Catch:{ all -> 0x0030 }
            com.android.systemui.qs.PageIndicator r2 = r1.mPageIndicator     // Catch:{ all -> 0x0030 }
            if (r2 == 0) goto L_0x002e
            java.util.ArrayList<com.android.systemui.plugins.qs.QSTileView> r0 = r1.mPages     // Catch:{ all -> 0x0030 }
            int r0 = r0.size()     // Catch:{ all -> 0x0030 }
            r2.setNumPages(r0)     // Catch:{ all -> 0x0030 }
        L_0x002e:
            monitor-exit(r1)     // Catch:{ all -> 0x0030 }
            return
        L_0x0030:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0030 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.p024qs.CirclePagedTileLayout.addTile(com.android.systemui.qs.QSPanelControllerBase$TileRecord):void");
    }

    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        synchronized (this) {
            if (this.mTiles.remove((Object) tileRecord) && this.mPages.remove((Object) tileRecord.tileView)) {
                setAdapter(this.mAdapter);
                this.mAdapter.notifyDataSetChanged();
            }
            PageIndicator pageIndicator = this.mPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(this.mPages.size());
            }
        }
    }

    public void setExpansion(float f, float f2) {
        this.mLastExpansion = f;
        updateSelected();
        for (int i = 0; i < this.mPages.size(); i++) {
            ((QSTileViewCircle) this.mPages.get(i)).setExpansion(f);
            if (f == 1.0f) {
                invalidate();
                NTLogUtil.m1686d(TAG, "setExpansion: mLastExpansion = " + this.mLastExpansion + " try again dispatchDraw");
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateSelected() {
        float f = this.mLastExpansion;
        boolean z = true;
        if (f <= 0.0f || f >= 1.0f) {
            if (f != 1.0f) {
                z = false;
            }
            this.mHasUnselected = false;
            setImportantForAccessibility(4);
            int currentPageNumber = getCurrentPageNumber();
            int i = 0;
            while (i < this.mPages.size()) {
                QSTileView qSTileView = this.mPages.get(i);
                qSTileView.setSelected(i == currentPageNumber ? z : false);
                if (qSTileView.isSelected()) {
                    logVisibleTiles(qSTileView);
                }
                i++;
            }
            setImportantForAccessibility(0);
        } else if (!this.mHasUnselected) {
            for (int i2 = 0; i2 < this.mPages.size(); i2++) {
                NTLogUtil.m1686d(TAG, "updateSelected: mHasUnselected");
                this.mPages.get(i2).setSelected(false);
            }
            setSelected(false);
            this.mHasUnselected = true;
        }
    }

    public void setPageListener(PageListener pageListener) {
        this.mPageListener = pageListener;
    }

    public void setExcessHeight(int i) {
        this.mExcessHeight = i;
    }

    public int getNumPages() {
        return Math.max(this.mTiles.size(), 1);
    }

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

    private static Animator setupBounceAnimator(View view, int i) {
        view.setAlpha(0.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{1.0f}), PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{1.0f}), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{1.0f})});
        ofPropertyValuesHolder.setDuration(BOUNCE_ANIMATION_DURATION);
        ofPropertyValuesHolder.setStartDelay((long) (i * 85));
        ofPropertyValuesHolder.setInterpolator(new OvershootInterpolator(BOUNCE_ANIMATION_TENSION));
        return ofPropertyValuesHolder;
    }

    public void onPageChanged(int i) {
        if (this.mIsSignal) {
            this.mEx.onSignalPageChanged(i);
        } else {
            this.mEx.onBtPageChanged(i);
        }
    }

    public PageIndicator getPageIndicator() {
        return this.mPageIndicator;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mPages.size() == 1) {
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }
}
