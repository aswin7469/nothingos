package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.QSPanelControllerBase;
import com.android.systemui.plugins.p011qs.QSTile;

/* renamed from: com.android.systemui.qs.QuickQSPanel */
public class QuickQSPanel extends QSPanel {
    private static final String TAG = "QuickQSPanel";
    public static final int TUNER_MAX_TILES_FALLBACK = 6;
    private boolean mDisabledByPolicy;
    private int mMaxTiles = getResources().getInteger(C1893R.integer.quick_qs_panel_max_tiles);

    public void createCirclePagedTileLayouts() {
    }

    /* access modifiers changed from: protected */
    public boolean displayMediaMarginsOnMedia() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getDumpableTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public boolean mediaNeedsTopMargin() {
        return true;
    }

    public QuickQSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void setHorizontalContentContainerClipping() {
        this.mHorizontalContentContainer.setClipToPadding(false);
        this.mHorizontalContentContainer.setClipChildren(false);
    }

    public TileLayout getOrCreateTileLayout() {
        return new QQSSideLabelTileLayout(this.mContext);
    }

    /* access modifiers changed from: protected */
    public void updatePadding() {
        getResources().getDimensionPixelSize(C1893R.dimen.qqs_layout_padding_bottom);
        setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingEnd(), this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.qqs_padding_bottom));
    }

    /* access modifiers changed from: protected */
    public boolean shouldShowDetail() {
        return !this.mExpanded;
    }

    public void drawTile(QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        if (state instanceof QSTile.SignalState) {
            QSTile.SignalState signalState = new QSTile.SignalState();
            state.copyTo(signalState);
            signalState.activityIn = false;
            signalState.activityOut = false;
            state = signalState;
        }
        super.drawTile(tileRecord, state);
    }

    public void setMaxTiles(int i) {
        this.mMaxTiles = i;
    }

    public void onTuningChanged(String str, String str2) {
        if (QSPanel.QS_SHOW_BRIGHTNESS.equals(str)) {
            super.onTuningChanged(str, "0");
        }
    }

    public int getNumQuickTiles() {
        return this.mMaxTiles;
    }

    public static int parseNumTiles(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return 6;
        }
    }

    /* access modifiers changed from: package-private */
    public void setDisabledByPolicy(boolean z) {
        if (z != this.mDisabledByPolicy) {
            this.mDisabledByPolicy = z;
            setVisibility(z ? 8 : 0);
        }
    }

    public void setVisibility(int i) {
        if (this.mDisabledByPolicy) {
            if (getVisibility() != 8) {
                i = 8;
            } else {
                return;
            }
        }
        super.setVisibility(i);
    }

    /* access modifiers changed from: protected */
    public QSEvent openPanelEvent() {
        return QSEvent.QQS_PANEL_EXPANDED;
    }

    /* access modifiers changed from: protected */
    public QSEvent closePanelEvent() {
        return QSEvent.QQS_PANEL_COLLAPSED;
    }

    /* access modifiers changed from: protected */
    public QSEvent tileVisibleEvent() {
        return QSEvent.QQS_TILE_VISIBLE;
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.removeAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_COLLAPSE);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
    }

    /* renamed from: com.android.systemui.qs.QuickQSPanel$QQSSideLabelTileLayout */
    static class QQSSideLabelTileLayout extends SideLabelTileLayout {
        private boolean mLastSelected;

        QQSSideLabelTileLayout(Context context) {
            super(context, (AttributeSet) null);
            setClipChildren(false);
            setClipToPadding(false);
            setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            setMaxColumns(2);
        }

        public boolean updateResources() {
            this.mCellHeightResId = C1893R.dimen.qs_quick_tile_size;
            boolean updateResources = super.updateResources();
            this.mMaxAllowedRows = getResources().getInteger(C1893R.integer.quick_settings_max_rows);
            return updateResources;
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            updateResources();
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            updateMaxRows(10000, this.mRecords.size());
            super.onMeasure(i, i2);
        }

        public void setListening(boolean z, UiEventLogger uiEventLogger) {
            boolean z2 = !this.mListening && z;
            super.setListening(z, uiEventLogger);
            if (z2) {
                for (int i = 0; i < getNumVisibleTiles(); i++) {
                    QSTile qSTile = ((QSPanelControllerBase.TileRecord) this.mRecords.get(i)).tile;
                    uiEventLogger.logWithInstanceId(QSEvent.QQS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
                }
            }
        }

        public void setExpansion(float f, float f2) {
            if (f <= 0.0f || f >= 1.0f) {
                boolean z = f == 1.0f || f2 < 0.0f;
                if (this.mLastSelected != z) {
                    setImportantForAccessibility(4);
                    for (int i = 0; i < getChildCount(); i++) {
                        getChildAt(i).setSelected(z);
                    }
                    setImportantForAccessibility(0);
                    this.mLastSelected = z;
                }
            }
        }
    }
}
