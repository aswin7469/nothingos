package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.RemeasuringLinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.QSDetail;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.nothingos.systemui.qs.CirclePagedTileLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class QSPanel extends LinearLayout implements TunerService.Tunable {
    protected View mBrightnessView;
    private PageIndicator mBtIndicator;
    private CirclePagedTileLayout mBtTilePage;
    private QSDetail.Callback mCallback;
    private View mCircleTileContainer;
    private int mContentMarginEnd;
    private int mContentMarginStart;
    protected final Context mContext;
    private Record mDetailRecord;
    protected boolean mExpanded;
    protected View mFooter;
    private PageIndicator mFooterPageIndicator;
    private ViewGroup mHeaderContainer;
    protected LinearLayout mHorizontalContentContainer;
    private LinearLayout mHorizontalLinearLayout;
    protected QSTileHost mHost;
    protected boolean mListening;
    private QSAnimator mQSAnimator;
    protected View mSecurityFooter;
    private PageIndicator mSignalIndicator;
    private CirclePagedTileLayout mSignalTilePage;
    protected QSTileLayout mTileLayout;
    private boolean mUsingHorizontalLayout;
    protected boolean mUsingMediaPlayer;
    private final H mHandler = new H();
    private final List<OnConfigurationChangedListener> mOnConfigurationChangedListeners = new ArrayList();
    private ArrayMap<CharSequence, QSPanelControllerBase.TileRecord> mCacheBtDevices = new ArrayMap<>();
    private final int mMediaTotalBottomMargin = getResources().getDimensionPixelSize(R$dimen.quick_settings_bottom_margin_media);
    private final int mMediaTopMargin = getResources().getDimensionPixelSize(R$dimen.qs_tile_margin_vertical);
    private int mMovableContentStartIndex = getChildCount();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnConfigurationChangedListener {
        void onConfigurationChange(Configuration configuration);
    }

    /* loaded from: classes.dex */
    public interface QSTileLayout {
        void addTile(QSPanelControllerBase.TileRecord tileRecord);

        int getNumVisibleTiles();

        int getOffsetTop(QSPanelControllerBase.TileRecord tileRecord);

        void removeTile(QSPanelControllerBase.TileRecord tileRecord);

        default void restoreInstanceState(Bundle bundle) {
        }

        default void saveInstanceState(Bundle bundle) {
        }

        default void setExpansion(float f, float f2) {
        }

        void setListening(boolean z, UiEventLogger uiEventLogger);

        default boolean setMaxColumns(int i) {
            return false;
        }

        default boolean setMinRows(int i) {
            return false;
        }

        boolean updateResources();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public static class Record {
        DetailAdapter detailAdapter;
        int x;
        int y;
    }

    private boolean needsDynamicRowsAndColumns() {
        return true;
    }

    protected boolean displayMediaMarginsOnMedia() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getDumpableTag() {
        return "QSPanel";
    }

    protected boolean mediaNeedsTopMargin() {
        return false;
    }

    public QSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUsingMediaPlayer = Utils.useQsMediaPlayer(context);
        this.mContext = context;
        setOrientation(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initialize() {
        this.mTileLayout = mo773getOrCreateTileLayout();
        if (this.mUsingMediaPlayer) {
            RemeasuringLinearLayout remeasuringLinearLayout = new RemeasuringLinearLayout(this.mContext);
            this.mHorizontalLinearLayout = remeasuringLinearLayout;
            remeasuringLinearLayout.setOrientation(0);
            this.mHorizontalLinearLayout.setClipChildren(false);
            this.mHorizontalLinearLayout.setClipToPadding(false);
            RemeasuringLinearLayout remeasuringLinearLayout2 = new RemeasuringLinearLayout(this.mContext);
            this.mHorizontalContentContainer = remeasuringLinearLayout2;
            remeasuringLinearLayout2.setOrientation(1);
            this.mHorizontalContentContainer.setClipChildren(true);
            this.mHorizontalContentContainer.setClipToPadding(false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
            layoutParams.setMarginStart(0);
            layoutParams.setMarginEnd((int) this.mContext.getResources().getDimension(R$dimen.qs_media_padding));
            layoutParams.gravity = 16;
            this.mHorizontalLinearLayout.addView(this.mHorizontalContentContainer, layoutParams);
            addView(this.mHorizontalLinearLayout, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        }
        createCirclePagedTileLayouts();
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
            marginLayoutParams.topMargin = this.mContext.getResources().getDimensionPixelSize(R$dimen.qs_brightness_margin_top);
            marginLayoutParams.bottomMargin = this.mContext.getResources().getDimensionPixelSize(R$dimen.qs_brightness_margin_bottom);
            this.mBrightnessView.setLayoutParams(marginLayoutParams);
        }
    }

    /* renamed from: getOrCreateTileLayout */
    public QSTileLayout mo773getOrCreateTileLayout() {
        if (this.mTileLayout == null) {
            this.mTileLayout = (QSTileLayout) LayoutInflater.from(this.mContext).inflate(R$layout.qs_paged_tile_layout, (ViewGroup) this, false);
        }
        return this.mTileLayout;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
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

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        View view;
        if (!"qs_show_brightness".equals(str) || (view = this.mBrightnessView) == null) {
            return;
        }
        updateViewVisibilityForTuningValue(view, str2);
    }

    private void updateViewVisibilityForTuningValue(View view, String str) {
        view.setVisibility(TunerService.parseIntegerSwitch(str, true) ? 0 : 8);
    }

    public void openDetails(QSTile qSTile) {
        if (qSTile != null) {
            showDetailAdapter(true, qSTile.getDetailAdapter(), new int[]{getWidth() / 2, 0});
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View getBrightnessView() {
        return this.mBrightnessView;
    }

    public void setCallback(QSDetail.Callback callback) {
        this.mCallback = callback;
    }

    public void setFooterPageIndicator(PageIndicator pageIndicator) {
        if (this.mTileLayout instanceof PagedTileLayout) {
            this.mFooterPageIndicator = pageIndicator;
            updatePageIndicator();
        }
    }

    private void updatePageIndicator() {
        PageIndicator pageIndicator;
        if (!(this.mTileLayout instanceof PagedTileLayout) || (pageIndicator = this.mFooterPageIndicator) == null) {
            return;
        }
        pageIndicator.setVisibility(8);
        ((PagedTileLayout) this.mTileLayout).setPageIndicator(this.mFooterPageIndicator);
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

    protected void updatePadding() {
        Resources resources = this.mContext.getResources();
        setPaddingRelative(getPaddingStart(), resources.getDimensionPixelSize(R$dimen.qs_panel_padding_top), getPaddingEnd(), resources.getDimensionPixelSize(R$dimen.qs_panel_padding_bottom));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListeners.add(onConfigurationChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListeners.remove(onConfigurationChangedListener);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mOnConfigurationChangedListeners.forEach(new Consumer() { // from class: com.android.systemui.qs.QSPanel$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((QSPanel.OnConfigurationChangedListener) obj).onConfigurationChange(configuration);
            }
        });
        switchSecurityFooter();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mFooter = findViewById(R$id.qs_footer);
    }

    private void updateHorizontalLinearLayoutMargins() {
        if (this.mHorizontalLinearLayout == null || displayMediaMarginsOnMedia()) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mHorizontalLinearLayout.getLayoutParams();
        layoutParams.bottomMargin = Math.max(this.mMediaTotalBottomMargin - getPaddingBottom(), 0);
        this.mHorizontalLinearLayout.setLayoutParams(layoutParams);
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

    private void switchSecurityFooter() {
        ViewGroup viewGroup;
        if (this.mSecurityFooter != null) {
            if (this.mContext.getResources().getConfiguration().orientation == 2 && (viewGroup = this.mHeaderContainer) != null) {
                switchToParent(this.mSecurityFooter, viewGroup, 0);
                return;
            }
            View findViewByPredicate = findViewByPredicate(QSPanel$$ExternalSyntheticLambda1.INSTANCE);
            int indexOfChild = findViewByPredicate != null ? indexOfChild(findViewByPredicate) : -1;
            if (this.mSecurityFooter.getParent() == this && indexOfChild(this.mSecurityFooter) < indexOfChild) {
                indexOfChild--;
            }
            switchToParent(this.mSecurityFooter, this, indexOfChild);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$switchSecurityFooter$1(View view) {
        return view instanceof UniqueObjectHostView;
    }

    private void switchToParent(View view, ViewGroup viewGroup, int i) {
        switchToParent(view, viewGroup, i, getDumpableTag());
    }

    private void reAttachMediaHost(ViewGroup viewGroup, boolean z) {
        if (!this.mUsingMediaPlayer) {
            return;
        }
        LinearLayout linearLayout = z ? this.mHorizontalLinearLayout : this;
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
        if (viewGroup2 == linearLayout) {
            return;
        }
        if (viewGroup2 != null) {
            viewGroup2.removeView(viewGroup);
        }
        linearLayout.addView(viewGroup);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
        layoutParams.height = -2;
        int i = 0;
        layoutParams.width = z ? 0 : -1;
        layoutParams.weight = z ? 1.0f : 0.0f;
        layoutParams.bottomMargin = (!z || displayMediaMarginsOnMedia()) ? Math.max(this.mMediaTotalBottomMargin - getPaddingBottom(), 0) : 0;
        if (mediaNeedsTopMargin() && !z) {
            i = this.mMediaTopMargin;
        }
        layoutParams.topMargin = i;
    }

    public void setExpanded(boolean z) {
        if (this.mExpanded == z) {
            return;
        }
        this.mExpanded = z;
        if (!z) {
            QSTileLayout qSTileLayout = this.mTileLayout;
            if (qSTileLayout instanceof PagedTileLayout) {
                ((PagedTileLayout) qSTileLayout).setCurrentItem(0, false);
            }
        }
        if (this.mExpanded) {
            return;
        }
        CirclePagedTileLayout circlePagedTileLayout = this.mSignalTilePage;
        if (circlePagedTileLayout != null) {
            circlePagedTileLayout.setCurrentItem(0, false);
        }
        CirclePagedTileLayout circlePagedTileLayout2 = this.mBtTilePage;
        if (circlePagedTileLayout2 == null) {
            return;
        }
        circlePagedTileLayout2.setCurrentItem(0, false);
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
        CirclePagedTileLayout circlePagedTileLayout = this.mSignalTilePage;
        if (circlePagedTileLayout != null) {
            circlePagedTileLayout.setListening(z, null);
        }
        CirclePagedTileLayout circlePagedTileLayout2 = this.mBtTilePage;
        if (circlePagedTileLayout2 != null) {
            circlePagedTileLayout2.setListening(z, null);
        }
    }

    public void showDetailAdapter(boolean z, DetailAdapter detailAdapter, int[] iArr) {
        int i = iArr[0];
        int i2 = iArr[1];
        ((View) getParent()).getLocationInWindow(iArr);
        Record record = new Record();
        record.detailAdapter = detailAdapter;
        record.x = i - iArr[0];
        record.y = i2 - iArr[1];
        iArr[0] = i;
        iArr[1] = i2;
        showDetail(z, record);
    }

    protected void showDetail(boolean z, Record record) {
        this.mHandler.obtainMessage(1, z ? 1 : 0, 0, record).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void drawTile(QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        tileRecord.tileView.onStateChanged(state);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public QSEvent openPanelEvent() {
        return QSEvent.QS_PANEL_EXPANDED;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public QSEvent closePanelEvent() {
        return QSEvent.QS_PANEL_COLLAPSED;
    }

    protected boolean shouldShowDetail() {
        return this.mExpanded;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addTile(final QSPanelControllerBase.TileRecord tileRecord) {
        CirclePagedTileLayout circlePagedTileLayout;
        CirclePagedTileLayout circlePagedTileLayout2;
        QSTile.Callback callback = new QSTile.Callback() { // from class: com.android.systemui.qs.QSPanel.1
            @Override // com.android.systemui.plugins.qs.QSTile.Callback
            public void onStateChanged(QSTile.State state) {
                QSPanel.this.drawTile(tileRecord, state);
                if (!tileRecord.isCircleBluetoothTile || QSPanel.this.mBtTilePage == null) {
                    return;
                }
                ArrayList<CharSequence> arrayList = state.addressList;
                if (arrayList == null || arrayList.isEmpty()) {
                    if (QSPanel.this.mCacheBtDevices.isEmpty()) {
                        return;
                    }
                    for (Map.Entry entry : QSPanel.this.mCacheBtDevices.entrySet()) {
                        QSPanel.this.mHandler.obtainMessage(5, entry.getValue()).sendToTarget();
                        Log.d("QSPanel", "onStateChanged: remove device " + entry.getKey());
                    }
                    QSPanel.this.mCacheBtDevices.clear();
                    QSPanel.this.mBtTilePage.post(new Runnable() { // from class: com.android.systemui.qs.QSPanel.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            QSPanel.this.mBtTilePage.setCurrentItem(0, false);
                        }
                    });
                    return;
                }
                Iterator it = QSPanel.this.mCacheBtDevices.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry2 = (Map.Entry) it.next();
                    if (!state.addressList.contains(entry2.getKey())) {
                        Log.d("QSPanel", "onStateChanged: remove pre device " + entry2.getKey());
                        QSPanel.this.mHandler.obtainMessage(5, entry2.getValue()).sendToTarget();
                        it.remove();
                    }
                }
                for (int i = 0; i < state.addressList.size(); i++) {
                    CharSequence charSequence = state.addressList.get(i);
                    QSPanelControllerBase.TileRecord tileRecord2 = (QSPanelControllerBase.TileRecord) QSPanel.this.mCacheBtDevices.get(charSequence);
                    if (tileRecord2 == null) {
                        tileRecord2 = new QSPanelControllerBase.TileRecord();
                        QSPanelControllerBase.TileRecord tileRecord3 = tileRecord;
                        tileRecord2.tile = tileRecord3.tile;
                        QSPanel qSPanel = QSPanel.this;
                        QSTileView createTileView = qSPanel.mHost.createTileView(qSPanel.mContext, tileRecord3.tile, false);
                        tileRecord2.tileView = createTileView;
                        createTileView.init(tileRecord.tile);
                        QSPanel.this.mCacheBtDevices.put(charSequence, tileRecord2);
                        Log.d("QSPanel", "onStateChanged: create bt device " + ((Object) charSequence));
                    }
                    QSTile.State copy = state.copy();
                    copy.icon = state.iconList.get(i);
                    copy.label = state.labelList.get(i);
                    copy.secondaryLabel = state.secondaryLabelList.get(i);
                    copy.state = state.stateList[i];
                    QSPanel.this.mHandler.obtainMessage(4, tileRecord2).sendToTarget();
                    QSPanel.this.drawTile(tileRecord2, copy);
                }
            }

            @Override // com.android.systemui.plugins.qs.QSTile.Callback
            public void onShowDetail(boolean z) {
                if (QSPanel.this.shouldShowDetail()) {
                    QSPanel.this.showDetail(z, tileRecord);
                }
            }

            @Override // com.android.systemui.plugins.qs.QSTile.Callback
            public void onToggleStateChanged(boolean z) {
                if (QSPanel.this.mDetailRecord == tileRecord) {
                    QSPanel.this.fireToggleStateChanged(z);
                }
            }

            @Override // com.android.systemui.plugins.qs.QSTile.Callback
            public void onScanStateChanged(boolean z) {
                tileRecord.scanState = z;
                Record record = QSPanel.this.mDetailRecord;
                QSPanelControllerBase.TileRecord tileRecord2 = tileRecord;
                if (record == tileRecord2) {
                    QSPanel.this.fireScanStateChanged(tileRecord2.scanState);
                }
            }

            @Override // com.android.systemui.plugins.qs.QSTile.Callback
            public void onAnnouncementRequested(CharSequence charSequence) {
                if (charSequence != null) {
                    QSPanel.this.mHandler.obtainMessage(3, charSequence).sendToTarget();
                }
            }
        };
        tileRecord.tile.addCallback(callback);
        tileRecord.callback = callback;
        tileRecord.tileView.init(tileRecord.tile);
        tileRecord.tile.refreshState();
        if (tileRecord.isSignalTile) {
            if (tileRecord.isCircleSignalTile && (circlePagedTileLayout2 = this.mSignalTilePage) != null) {
                circlePagedTileLayout2.addTile(tileRecord);
            } else if (!tileRecord.isCollapsedSignalTile) {
            } else {
                this.mTileLayout.addTile(tileRecord);
            }
        } else if (tileRecord.isBtTile) {
            if (tileRecord.isCircleBluetoothTile && (circlePagedTileLayout = this.mBtTilePage) != null) {
                circlePagedTileLayout.addTile(tileRecord);
            } else {
                this.mTileLayout.addTile(tileRecord);
            }
        } else {
            this.mTileLayout.addTile(tileRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        CirclePagedTileLayout circlePagedTileLayout;
        CirclePagedTileLayout circlePagedTileLayout2;
        if (tileRecord.isCircleSignalTile && (circlePagedTileLayout2 = this.mSignalTilePage) != null) {
            circlePagedTileLayout2.removeTile(tileRecord);
        } else if (tileRecord.isCircleBluetoothTile && (circlePagedTileLayout = this.mBtTilePage) != null) {
            circlePagedTileLayout.removeTile(tileRecord);
        } else {
            this.mTileLayout.removeTile(tileRecord);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeDetail() {
        showDetail(false, this.mDetailRecord);
    }

    protected void handleShowDetail(Record record, boolean z) {
        int i;
        if (record instanceof QSPanelControllerBase.TileRecord) {
            handleShowDetailTile((QSPanelControllerBase.TileRecord) record, z);
            return;
        }
        int i2 = 0;
        if (record != null) {
            i2 = record.x;
            i = record.y;
        } else {
            i = 0;
        }
        handleShowDetailImpl(record, z, i2, i);
    }

    private void handleShowDetailTile(QSPanelControllerBase.TileRecord tileRecord, boolean z) {
        Record record = this.mDetailRecord;
        if ((record != null) == z && record == tileRecord) {
            return;
        }
        if (z) {
            DetailAdapter detailAdapter = tileRecord.tile.getDetailAdapter();
            tileRecord.detailAdapter = detailAdapter;
            if (detailAdapter == null) {
                return;
            }
        }
        tileRecord.tile.setDetailListening(z);
        handleShowDetailImpl(tileRecord, z, tileRecord.tileView.getLeft() + (tileRecord.tileView.getWidth() / 2), tileRecord.tileView.getDetailY() + this.mTileLayout.getOffsetTop(tileRecord) + getTop());
    }

    private void handleShowDetailImpl(Record record, boolean z, int i, int i2) {
        DetailAdapter detailAdapter = null;
        setDetailRecord(z ? record : null);
        if (z) {
            detailAdapter = record.detailAdapter;
        }
        fireShowingDetail(detailAdapter, i, i2);
    }

    protected void setDetailRecord(Record record) {
        if (record == this.mDetailRecord) {
            return;
        }
        this.mDetailRecord = record;
        fireScanStateChanged((record instanceof QSPanelControllerBase.TileRecord) && ((QSPanelControllerBase.TileRecord) record).scanState);
    }

    private void fireShowingDetail(DetailAdapter detailAdapter, int i, int i2) {
        QSDetail.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onShowingDetail(detailAdapter, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireToggleStateChanged(boolean z) {
        QSDetail.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onToggleStateChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireScanStateChanged(boolean z) {
        QSDetail.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onScanStateChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public QSTileLayout getTileLayout() {
        return this.mTileLayout;
    }

    public void setContentMargins(int i, int i2, ViewGroup viewGroup) {
        this.mContentMarginStart = i;
        this.mContentMarginEnd = i2;
        updateMediaHostContentMargins(viewGroup);
    }

    protected void updateMediaHostContentMargins(ViewGroup viewGroup) {
        if (this.mUsingMediaPlayer) {
            updateMargins(viewGroup, 0, this.mUsingHorizontalLayout ? this.mContentMarginEnd : 0);
        }
    }

    protected void updateMargins(View view, int i, int i2) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.setMarginStart(i);
            layoutParams.setMarginEnd(i2);
            view.setLayoutParams(layoutParams);
        }
    }

    public void setHeaderContainer(ViewGroup viewGroup) {
        this.mHeaderContainer = viewGroup;
    }

    public boolean isListening() {
        return this.mListening;
    }

    public void setSecurityFooter(View view) {
        this.mSecurityFooter = view;
        switchSecurityFooter();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPageMargin(int i) {
        QSTileLayout qSTileLayout = this.mTileLayout;
        if (qSTileLayout instanceof PagedTileLayout) {
            ((PagedTileLayout) qSTileLayout).setPageMargin(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class H extends Handler {
        private H() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            boolean z = true;
            if (i == 1) {
                QSPanel qSPanel = QSPanel.this;
                Record record = (Record) message.obj;
                if (message.arg1 == 0) {
                    z = false;
                }
                qSPanel.handleShowDetail(record, z);
            } else if (i == 3) {
                QSPanel.this.announceForAccessibility((CharSequence) message.obj);
            } else if (i == 4) {
                QSPanel.this.mBtTilePage.addTile((QSPanelControllerBase.TileRecord) message.obj);
            } else if (i != 5) {
            } else {
                QSPanelControllerBase.TileRecord tileRecord = (QSPanelControllerBase.TileRecord) message.obj;
                QSPanel.this.mBtTilePage.removeTile(tileRecord);
                tileRecord.tileView = null;
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
        } else if (viewGroup.indexOfChild(view) == i) {
        } else {
            viewGroup.removeView(view);
            viewGroup.addView(view, i);
        }
    }

    protected void createCirclePagedTileLayouts() {
        Log.d("QSPanel", "createCirclePagedTileLayouts: ");
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.circle_qs_tile_layout, (ViewGroup) this, false);
        this.mCircleTileContainer = inflate;
        CirclePagedTileLayout circlePagedTileLayout = (CirclePagedTileLayout) inflate.findViewById(R$id.signal_tile_page);
        this.mSignalTilePage = circlePagedTileLayout;
        circlePagedTileLayout.markSignal();
        this.mBtTilePage = (CirclePagedTileLayout) this.mCircleTileContainer.findViewById(R$id.bt_tile_page);
        this.mSignalIndicator = (PageIndicator) this.mCircleTileContainer.findViewById(R$id.signal_tile_indicator);
        this.mBtIndicator = (PageIndicator) this.mCircleTileContainer.findViewById(R$id.bt_tile_indicator);
        this.mSignalTilePage.setPageIndicator(this.mSignalIndicator);
        this.mBtTilePage.setPageIndicator(this.mBtIndicator);
        addView(this.mCircleTileContainer, 0);
        this.mMovableContentStartIndex++;
        setupCirclePagedTileLayout();
    }

    public void removeAndRecreateCirclePagedTileLayouts() {
        View view = this.mCircleTileContainer;
        if (view != null) {
            this.mMovableContentStartIndex--;
            removeView(view);
            this.mSignalTilePage.removeAllViews();
            this.mBtTilePage.removeAllViews();
            Log.d("QSPanel", "removeAndRecreateCirclePagedTileLayouts:");
            createCirclePagedTileLayouts();
        }
    }

    public void setExpansion(float f, float f2) {
        CirclePagedTileLayout circlePagedTileLayout = this.mSignalTilePage;
        if (circlePagedTileLayout != null) {
            circlePagedTileLayout.setExpansion(f, f2);
        }
        CirclePagedTileLayout circlePagedTileLayout2 = this.mBtTilePage;
        if (circlePagedTileLayout2 != null) {
            circlePagedTileLayout2.setExpansion(f, f2);
        }
    }

    public void setHost(QSTileHost qSTileHost) {
        this.mHost = qSTileHost;
    }

    public void setQSAnimator(QSAnimator qSAnimator) {
        this.mQSAnimator = qSAnimator;
        setupCirclePagedTileLayout();
    }

    public void setupCirclePagedTileLayout() {
        CirclePagedTileLayout circlePagedTileLayout = this.mSignalTilePage;
        if (circlePagedTileLayout != null) {
            circlePagedTileLayout.setQSAnimator(this.mQSAnimator);
        }
        CirclePagedTileLayout circlePagedTileLayout2 = this.mBtTilePage;
        if (circlePagedTileLayout2 != null) {
            circlePagedTileLayout2.setQSAnimator(this.mQSAnimator);
        }
    }

    public int getBtTileIndex(QSTileView qSTileView) {
        CirclePagedTileLayout circlePagedTileLayout = this.mBtTilePage;
        if (circlePagedTileLayout != null) {
            return circlePagedTileLayout.getBtTileIndex(qSTileView);
        }
        return -1;
    }

    public QSTileView getBtTile(int i) {
        CirclePagedTileLayout circlePagedTileLayout = this.mBtTilePage;
        if (circlePagedTileLayout != null) {
            return circlePagedTileLayout.getBtTile(i);
        }
        return null;
    }
}
