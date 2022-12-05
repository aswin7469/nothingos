package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanelControllerBase;
/* loaded from: classes.dex */
public class QuickQSPanel extends QSPanel {
    private boolean mDisabledByPolicy;
    private int mMaxTiles = Math.min(6, getResources().getInteger(R$integer.quick_qs_panel_max_columns));

    @Override // com.android.systemui.qs.QSPanel
    public void createCirclePagedTileLayouts() {
    }

    @Override // com.android.systemui.qs.QSPanel
    protected boolean displayMediaMarginsOnMedia() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.QSPanel
    public String getDumpableTag() {
        return "QuickQSPanel";
    }

    @Override // com.android.systemui.qs.QSPanel
    protected boolean mediaNeedsTopMargin() {
        return true;
    }

    @Override // com.android.systemui.qs.QSPanel
    public void setBrightnessView(View view) {
    }

    public QuickQSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.systemui.qs.QSPanel
    public void initialize() {
        super.initialize();
        LinearLayout linearLayout = this.mHorizontalContentContainer;
        if (linearLayout != null) {
            linearLayout.setClipChildren(false);
        }
    }

    @Override // com.android.systemui.qs.QSPanel
    /* renamed from: getOrCreateTileLayout  reason: collision with other method in class */
    public TileLayout mo773getOrCreateTileLayout() {
        return new QQSSideLabelTileLayout(this.mContext);
    }

    @Override // com.android.systemui.qs.QSPanel
    protected void updatePadding() {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), this.mContext.getResources().getDimensionPixelSize(R$dimen.qqs_padding_bottom));
    }

    @Override // com.android.systemui.qs.QSPanel
    protected boolean shouldShowDetail() {
        return !this.mExpanded;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.QSPanel
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
        this.mMaxTiles = Math.min(i, 6);
    }

    @Override // com.android.systemui.qs.QSPanel, com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("qs_show_brightness".equals(str)) {
            super.onTuningChanged(str, "0");
        }
    }

    public int getNumQuickTiles() {
        return this.mMaxTiles;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDisabledByPolicy(boolean z) {
        if (z != this.mDisabledByPolicy) {
            this.mDisabledByPolicy = z;
            setVisibility(z ? 8 : 0);
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        if (this.mDisabledByPolicy) {
            if (getVisibility() == 8) {
                return;
            }
            i = 8;
        }
        super.setVisibility(i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.QSPanel
    public QSEvent openPanelEvent() {
        return QSEvent.QQS_PANEL_EXPANDED;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.QSPanel
    public QSEvent closePanelEvent() {
        return QSEvent.QQS_PANEL_COLLAPSED;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class QQSSideLabelTileLayout extends SideLabelTileLayout {
        private boolean mLastSelected;

        QQSSideLabelTileLayout(Context context) {
            super(context, null);
            setClipChildren(false);
            setClipToPadding(false);
            setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            setMaxColumns(2);
        }

        @Override // com.android.systemui.qs.SideLabelTileLayout, com.android.systemui.qs.TileLayout, com.android.systemui.qs.QSPanel.QSTileLayout
        public boolean updateResources() {
            this.mCellHeightResId = R$dimen.qs_quick_tile_size;
            boolean updateResources = super.updateResources();
            this.mMaxAllowedRows = getResources().getInteger(R$integer.quick_settings_max_rows);
            return updateResources;
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            updateResources();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.systemui.qs.TileLayout, android.view.View
        public void onMeasure(int i, int i2) {
            updateMaxRows(10000, this.mRecords.size());
            super.onMeasure(i, i2);
        }

        @Override // com.android.systemui.qs.TileLayout, com.android.systemui.qs.QSPanel.QSTileLayout
        public void setListening(boolean z, UiEventLogger uiEventLogger) {
            boolean z2 = !this.mListening && z;
            super.setListening(z, uiEventLogger);
            if (z2) {
                for (int i = 0; i < getNumVisibleTiles(); i++) {
                    QSTile qSTile = this.mRecords.get(i).tile;
                    uiEventLogger.logWithInstanceId(QSEvent.QQS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
                }
            }
        }

        @Override // com.android.systemui.qs.QSPanel.QSTileLayout
        public void setExpansion(float f, float f2) {
            if (f <= 0.0f || f >= 1.0f) {
                boolean z = f == 1.0f || f2 < 0.0f;
                if (this.mLastSelected == z) {
                    return;
                }
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
