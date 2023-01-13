package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.RemeasuringLinearLayout;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.PagedTileLayout;
import com.android.systemui.p012qs.QSPanelControllerBase;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import com.nothing.systemui.p024qs.QSPanelEx;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* renamed from: com.android.systemui.qs.QSPanel */
public class QSPanel extends LinearLayout implements TunerService.Tunable {
    public static final String QS_SHOW_BRIGHTNESS = "qs_show_brightness";
    public static final String QS_SHOW_HEADER = "qs_show_header";
    private static final String TAG = "QSPanel";
    protected View mBrightnessView;
    private final ArrayMap<View, Integer> mChildrenLayoutTop = new ArrayMap<>();
    private final Rect mClippingRect = new Rect();
    private Runnable mCollapseExpandAction;
    private int mContentMarginEnd;
    private int mContentMarginStart;
    protected final Context mContext;
    /* access modifiers changed from: private */
    public QSPanelEx mEx;
    protected boolean mExpanded;
    protected View mFooter;
    private PageIndicator mFooterPageIndicator;
    protected LinearLayout mHorizontalContentContainer;
    private LinearLayout mHorizontalLinearLayout;
    protected QSTileHost mHost;
    protected boolean mListening;
    private ViewGroup mMediaHostView;
    private final int mMediaTopMargin;
    private final int mMediaTotalBottomMargin;
    private int mMovableContentStartIndex;
    private final List<OnConfigurationChangedListener> mOnConfigurationChangedListeners = new ArrayList();
    private boolean mShouldMoveMediaOnExpansion = true;
    private float mSquishinessFraction = 1.0f;
    protected QSTileLayout mTileLayout;
    protected BrightnessSliderController mToggleSliderController;
    private boolean mUsingHorizontalLayout;
    protected boolean mUsingMediaPlayer;

    /* renamed from: com.android.systemui.qs.QSPanel$OnConfigurationChangedListener */
    interface OnConfigurationChangedListener {
        void onConfigurationChange(Configuration configuration);
    }

    /* renamed from: com.android.systemui.qs.QSPanel$QSTileLayout */
    public interface QSTileLayout {
        void addTile(QSPanelControllerBase.TileRecord tileRecord);

        int getHeight();

        int getNumVisibleTiles();

        int getOffsetTop(QSPanelControllerBase.TileRecord tileRecord);

        int getTilesHeight();

        void removeTile(QSPanelControllerBase.TileRecord tileRecord);

        void restoreInstanceState(Bundle bundle) {
        }

        void saveInstanceState(Bundle bundle) {
        }

        void setExpansion(float f, float f2) {
        }

        void setListening(boolean z, UiEventLogger uiEventLogger);

        boolean setMaxColumns(int i) {
            return false;
        }

        boolean setMinRows(int i) {
            return false;
        }

        void setSquishinessFraction(float f);

        boolean updateResources();
    }

    private boolean needsDynamicRowsAndColumns() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean displayMediaMarginsOnMedia() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String getDumpableTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public boolean mediaNeedsTopMargin() {
        return false;
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public QSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUsingMediaPlayer = Utils.useQsMediaPlayer(context);
        this.mMediaTotalBottomMargin = getResources().getDimensionPixelSize(C1894R.dimen.quick_settings_bottom_margin_media);
        this.mMediaTopMargin = getResources().getDimensionPixelSize(C1894R.dimen.qs_tile_margin_vertical);
        this.mContext = context;
        setOrientation(1);
        this.mMovableContentStartIndex = getChildCount();
    }

    /* access modifiers changed from: package-private */
    public void initialize() {
        this.mTileLayout = getOrCreateTileLayout();
        if (this.mUsingMediaPlayer) {
            RemeasuringLinearLayout remeasuringLinearLayout = new RemeasuringLinearLayout(this.mContext);
            this.mHorizontalLinearLayout = remeasuringLinearLayout;
            remeasuringLinearLayout.setOrientation(0);
            this.mHorizontalLinearLayout.setClipChildren(false);
            this.mHorizontalLinearLayout.setClipToPadding(false);
            RemeasuringLinearLayout remeasuringLinearLayout2 = new RemeasuringLinearLayout(this.mContext);
            this.mHorizontalContentContainer = remeasuringLinearLayout2;
            remeasuringLinearLayout2.setOrientation(1);
            setHorizontalContentContainerClipping();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
            layoutParams.setMarginStart(0);
            layoutParams.setMarginEnd((int) this.mContext.getResources().getDimension(C1894R.dimen.qs_media_padding));
            layoutParams.gravity = 16;
            this.mHorizontalLinearLayout.addView(this.mHorizontalContentContainer, layoutParams);
            addView(this.mHorizontalLinearLayout, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        }
        createCirclePagedTileLayouts();
    }

    /* access modifiers changed from: protected */
    public void setHorizontalContentContainerClipping() {
        this.mHorizontalContentContainer.setClipChildren(true);
        this.mHorizontalContentContainer.setClipToPadding(false);
        this.mHorizontalContentContainer.addOnLayoutChangeListener(new QSPanel$$ExternalSyntheticLambda0(this));
        this.mClippingRect.left = 0;
        this.mClippingRect.top = -1000;
        this.mHorizontalContentContainer.setClipBounds(this.mClippingRect);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setHorizontalContentContainerClipping$0$com-android-systemui-qs-QSPanel */
    public /* synthetic */ void mo36148xbee49df2(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i3 - i;
        if (i9 != i7 - i5 || i4 - i2 != i8 - i6) {
            this.mClippingRect.right = i9;
            this.mClippingRect.bottom = i4 - i2;
            this.mHorizontalContentContainer.setClipBounds(this.mClippingRect);
        }
    }

    public void setBrightnessView(View view) {
        View view2 = this.mBrightnessView;
        if (view2 != null) {
            removeView(view2);
            this.mMovableContentStartIndex--;
        }
        addView(view, 1);
        this.mBrightnessView = view;
        setBrightnessViewMargin();
        this.mMovableContentStartIndex++;
    }

    private void setBrightnessViewMargin() {
        View view = this.mBrightnessView;
        if (view != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginLayoutParams.topMargin = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.qs_brightness_margin_top);
            marginLayoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.qs_brightness_margin_bottom);
            this.mBrightnessView.setLayoutParams(marginLayoutParams);
        }
    }

    public QSTileLayout getOrCreateTileLayout() {
        if (this.mTileLayout == null) {
            QSTileLayout qSTileLayout = (QSTileLayout) LayoutInflater.from(this.mContext).inflate(C1894R.layout.qs_paged_tile_layout, this, false);
            this.mTileLayout = qSTileLayout;
            qSTileLayout.setSquishinessFraction(this.mSquishinessFraction);
        }
        return this.mTileLayout;
    }

    public void setSquishinessFraction(float f) {
        if (Float.compare(f, this.mSquishinessFraction) != 0) {
            this.mSquishinessFraction = f;
            QSTileLayout qSTileLayout = this.mTileLayout;
            if (qSTileLayout != null) {
                qSTileLayout.setSquishinessFraction(f);
                if (getMeasuredWidth() != 0) {
                    updateViewPositions();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            PageIndicator pageIndicator = this.mFooterPageIndicator;
            if (pageIndicator != null) {
                pageIndicator.setNumPages(((PagedTileLayout) qSTileLayout).getNumPages());
            }
            if (((View) this.mTileLayout).getParent() == this) {
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(10000, 1073741824);
                ((PagedTileLayout) this.mTileLayout).setExcessHeight(10000 - View.MeasureSpec.getSize(i2));
                i2 = makeMeasureSpec;
            }
        }
        super.onMeasure(i, i2);
        int paddingBottom = getPaddingBottom() + getPaddingTop();
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            if (childAt.getVisibility() != 8) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                paddingBottom = paddingBottom + childAt.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), paddingBottom);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            this.mChildrenLayoutTop.put(childAt, Integer.valueOf(childAt.getTop()));
        }
        updateViewPositions();
    }

    private void updateViewPositions() {
        int tilesHeight = this.mTileLayout.getTilesHeight() - this.mTileLayout.getHeight();
        boolean z = false;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (z) {
                int intValue = ((Integer) Objects.requireNonNull(this.mChildrenLayoutTop.get(childAt))).intValue() + ((childAt != this.mMediaHostView || this.mShouldMoveMediaOnExpansion) ? tilesHeight : 0);
                childAt.setLeftTopRightBottom(childAt.getLeft(), intValue, childAt.getRight(), childAt.getHeight() + intValue);
            }
            if (childAt == this.mTileLayout) {
                z = true;
            }
        }
    }

    public void onTuningChanged(String str, String str2) {
        View view;
        if (QS_SHOW_BRIGHTNESS.equals(str) && (view = this.mBrightnessView) != null) {
            updateViewVisibilityForTuningValue(view, str2);
        }
    }

    private void updateViewVisibilityForTuningValue(View view, String str) {
        view.setVisibility(TunerService.parseIntegerSwitch(str, true) ? 0 : 8);
    }

    /* access modifiers changed from: package-private */
    public View getBrightnessView() {
        return this.mBrightnessView;
    }

    public void setFooterPageIndicator(PageIndicator pageIndicator) {
        if (this.mTileLayout instanceof PagedTileLayout) {
            this.mFooterPageIndicator = pageIndicator;
            updatePageIndicator();
        }
    }

    private void updatePageIndicator() {
        PageIndicator pageIndicator;
        if ((this.mTileLayout instanceof PagedTileLayout) && (pageIndicator = this.mFooterPageIndicator) != null) {
            pageIndicator.setVisibility(8);
            ((PagedTileLayout) this.mTileLayout).setPageIndicator(this.mFooterPageIndicator);
        }
    }

    public QSTileHost getHost() {
        return this.mHost;
    }

    public void updateResources() {
        updatePadding();
        updatePageIndicator();
        setBrightnessViewMargin();
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout != null) {
            qSTileLayout.updateResources();
        }
    }

    /* access modifiers changed from: protected */
    public void updatePadding() {
        Resources resources = this.mContext.getResources();
        setPaddingRelative(getPaddingStart(), resources.getDimensionPixelSize(C1894R.dimen.qs_panel_padding_top), getPaddingEnd(), resources.getDimensionPixelSize(C1894R.dimen.qs_panel_padding_bottom));
    }

    /* access modifiers changed from: package-private */
    public void addOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListeners.add(onConfigurationChangedListener);
    }

    /* access modifiers changed from: package-private */
    public void removeOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListeners.remove((Object) onConfigurationChangedListener);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mOnConfigurationChangedListeners.forEach(new QSPanel$$ExternalSyntheticLambda1(configuration));
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mFooter = findViewById(C1894R.C1898id.qs_footer);
    }

    private void updateHorizontalLinearLayoutMargins() {
        if (this.mHorizontalLinearLayout != null && !displayMediaMarginsOnMedia()) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mHorizontalLinearLayout.getLayoutParams();
            layoutParams.bottomMargin = Math.max(this.mMediaTotalBottomMargin - getPaddingBottom(), 0);
            this.mHorizontalLinearLayout.setLayoutParams(layoutParams);
        }
    }

    private void switchAllContentToParent(ViewGroup viewGroup, QSTileLayout qSTileLayout) {
        int i = viewGroup == this ? this.mMovableContentStartIndex : 0;
        switchToParent((View) qSTileLayout, viewGroup, i);
        int i2 = i + 1;
        View view = this.mFooter;
        if (view != null) {
            switchToParent(view, viewGroup, i2);
        }
    }

    private void switchToParent(View view, ViewGroup viewGroup, int i) {
        switchToParent(view, viewGroup, i, getDumpableTag());
    }

    private void reAttachMediaHost(ViewGroup viewGroup, boolean z) {
        int i;
        if (this.mUsingMediaPlayer) {
            this.mMediaHostView = viewGroup;
            LinearLayout linearLayout = z ? this.mHorizontalLinearLayout : this;
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
            if (viewGroup2 != linearLayout) {
                if (viewGroup2 != null) {
                    viewGroup2.removeView(viewGroup);
                }
                linearLayout.addView(viewGroup);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
                layoutParams.height = -2;
                int i2 = 0;
                layoutParams.width = z ? 0 : -1;
                layoutParams.weight = z ? 1.0f : 0.0f;
                if (!z || displayMediaMarginsOnMedia()) {
                    i = Math.max(this.mMediaTotalBottomMargin - getPaddingBottom(), 0);
                } else {
                    i = 0;
                }
                layoutParams.bottomMargin = i;
                if (mediaNeedsTopMargin() && !z) {
                    i2 = this.mMediaTopMargin;
                }
                layoutParams.topMargin = i2;
            }
        }
    }

    public void setExpanded(boolean z) {
        if (this.mExpanded != z) {
            this.mExpanded = z;
            if (!z) {
                QSTileLayout qSTileLayout = this.mTileLayout;
                if (qSTileLayout instanceof PagedTileLayout) {
                    ((PagedTileLayout) qSTileLayout).setCurrentItem(0, false);
                }
            }
            QSPanelEx qSPanelEx = this.mEx;
            if (qSPanelEx != null) {
                qSPanelEx.setCurrentItemIfNeeded(this.mExpanded);
            }
            this.mHost.setExpanded(z);
        }
    }

    public void setPageListener(PagedTileLayout.PageListener pageListener) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            ((PagedTileLayout) qSTileLayout).setPageListener(pageListener);
        }
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public void setListening(boolean z) {
        this.mListening = z;
        QSPanelEx qSPanelEx = this.mEx;
        if (qSPanelEx != null) {
            qSPanelEx.setListening(z);
        }
    }

    public void drawTile(QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        tileRecord.tileView.onStateChanged(state);
    }

    /* access modifiers changed from: protected */
    public QSEvent openPanelEvent() {
        return QSEvent.QS_PANEL_EXPANDED;
    }

    /* access modifiers changed from: protected */
    public QSEvent closePanelEvent() {
        return QSEvent.QS_PANEL_COLLAPSED;
    }

    /* access modifiers changed from: protected */
    public QSEvent tileVisibleEvent() {
        return QSEvent.QS_TILE_VISIBLE;
    }

    /* access modifiers changed from: protected */
    public boolean shouldShowDetail() {
        return this.mExpanded;
    }

    /* access modifiers changed from: package-private */
    public void addTile(final QSPanelControllerBase.TileRecord tileRecord) {
        C23401 r0 = new QSTile.Callback() {
            public void onStateChanged(QSTile.State state) {
                QSPanel.this.drawTile(tileRecord, state);
                if (QSPanel.this.mEx != null) {
                    QSPanel.this.mEx.drawTileForBtPage(QSPanel.this.mContext, tileRecord, state);
                }
            }
        };
        tileRecord.tile.addCallback(r0);
        tileRecord.callback = r0;
        tileRecord.tileView.init(tileRecord.tile);
        tileRecord.tile.refreshState();
        QSPanelEx qSPanelEx = this.mEx;
        if (qSPanelEx != null) {
            qSPanelEx.addTile(tileRecord, this.mTileLayout);
        } else if (!tileRecord.isSignalTile) {
            this.mTileLayout.addTile(tileRecord);
        } else if (tileRecord.isCollapsedSignalTile) {
            this.mTileLayout.addTile(tileRecord);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        QSPanelEx qSPanelEx = this.mEx;
        if (qSPanelEx != null) {
            qSPanelEx.removeTile(tileRecord, this.mTileLayout);
        } else {
            this.mTileLayout.removeTile(tileRecord);
        }
    }

    public int getGridHeight() {
        return getMeasuredHeight();
    }

    public QSTileLayout getTileLayout() {
        return this.mTileLayout;
    }

    public void setContentMargins(int i, int i2, ViewGroup viewGroup) {
        this.mContentMarginStart = i;
        this.mContentMarginEnd = i2;
        updateMediaHostContentMargins(viewGroup);
    }

    /* access modifiers changed from: protected */
    public void updateMediaHostContentMargins(ViewGroup viewGroup) {
        if (this.mUsingMediaPlayer) {
            updateMargins(viewGroup, 0, this.mUsingHorizontalLayout ? this.mContentMarginEnd : 0);
        }
    }

    /* access modifiers changed from: protected */
    public void updateMargins(View view, int i, int i2) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMarginStart(i);
            layoutParams.setMarginEnd(i2);
            view.setLayoutParams(layoutParams);
        }
    }

    public boolean isListening() {
        return this.mListening;
    }

    /* access modifiers changed from: protected */
    public void setPageMargin(int i) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            ((PagedTileLayout) qSTileLayout).setPageMargin(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void setUsingHorizontalLayout(boolean z, ViewGroup viewGroup, boolean z2) {
        if (z != this.mUsingHorizontalLayout || z2) {
            this.mUsingHorizontalLayout = z;
            switchAllContentToParent(z ? this.mHorizontalContentContainer : this, this.mTileLayout);
            reAttachMediaHost(viewGroup, z);
            if (needsDynamicRowsAndColumns()) {
                this.mTileLayout.setMinRows(z ? 2 : 1);
                this.mTileLayout.setMaxColumns(2);
            }
            updateMargins(viewGroup);
            this.mHorizontalLinearLayout.setVisibility(z ? 0 : 8);
        }
    }

    private void updateMargins(ViewGroup viewGroup) {
        updateMediaHostContentMargins(viewGroup);
        updateHorizontalLinearLayoutMargins();
        updatePadding();
    }

    public void setShouldMoveMediaOnExpansion(boolean z) {
        this.mShouldMoveMediaOnExpansion = z;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
    }

    public boolean performAccessibilityAction(int i, Bundle bundle) {
        Runnable runnable;
        if ((i != 262144 && i != 524288) || (runnable = this.mCollapseExpandAction) == null) {
            return super.performAccessibilityAction(i, bundle);
        }
        runnable.run();
        return true;
    }

    public void setCollapseExpandAction(Runnable runnable) {
        this.mCollapseExpandAction = runnable;
    }

    /* renamed from: com.android.systemui.qs.QSPanel$H */
    private class C2341H extends Handler {
        private static final int ANNOUNCE_FOR_ACCESSIBILITY = 1;

        private C2341H() {
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                QSPanel.this.announceForAccessibility((CharSequence) message.obj);
            }
        }
    }

    static void switchToParent(View view, ViewGroup viewGroup, int i, String str) {
        if (viewGroup == null) {
            Log.w(str, "Trying to move view to null parent", new IllegalStateException());
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) view.getParent();
        if (viewGroup2 != viewGroup) {
            if (viewGroup2 != null) {
                viewGroup2.removeView(view);
            }
            viewGroup.addView(view, i);
        } else if (viewGroup.indexOfChild(view) != i) {
            viewGroup.removeView(view);
            viewGroup.addView(view, i);
        }
    }

    /* access modifiers changed from: protected */
    public void createCirclePagedTileLayouts() {
        if (this.mEx == null) {
            this.mEx = new QSPanelEx();
        }
        this.mEx.createCirclePagedTileLayouts(this.mContext, this);
        this.mMovableContentStartIndex++;
    }

    public void removeAndRecreateCirclePagedTileLayouts() {
        QSPanelEx qSPanelEx = this.mEx;
        if (qSPanelEx != null) {
            this.mMovableContentStartIndex--;
            qSPanelEx.removeAndRecreateCirclePagedTileLayouts(this.mContext);
            this.mMovableContentStartIndex++;
        }
    }

    public void setExpansion(float f, float f2) {
        QSPanelEx qSPanelEx = this.mEx;
        if (qSPanelEx != null) {
            qSPanelEx.setExpansion(f, f2);
        }
    }

    public void setHost(QSTileHost qSTileHost) {
        this.mHost = qSTileHost;
        QSPanelEx qSPanelEx = this.mEx;
        if (qSPanelEx != null) {
            qSPanelEx.setHost(qSTileHost);
        }
    }

    public int getBtTileIndex(QSTileView qSTileView) {
        return this.mEx.getBtTileIndex(qSTileView);
    }

    public QSTileView getBtTile(int i) {
        return this.mEx.getBtTile(i);
    }
}
