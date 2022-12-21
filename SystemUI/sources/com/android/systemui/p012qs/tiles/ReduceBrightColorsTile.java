package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.ReduceBrightColorsController;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import javax.inject.Inject;
import javax.inject.Named;

/* renamed from: com.android.systemui.qs.tiles.ReduceBrightColorsTile */
public class ReduceBrightColorsTile extends QSTileImpl<QSTile.BooleanState> implements ReduceBrightColorsController.Listener {
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(C1893R.C1895drawable.ic_reduce_bright_colors);
    private final boolean mIsAvailable;
    private boolean mIsListening;
    private final ReduceBrightColorsController mReduceBrightColorsController;

    public int getMetricsCategory() {
        return 0;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public ReduceBrightColorsTile(@Named("rbc_available") boolean z, ReduceBrightColorsController reduceBrightColorsController, QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mReduceBrightColorsController = reduceBrightColorsController;
        reduceBrightColorsController.observe(getLifecycle(), this);
        this.mIsAvailable = z;
    }

    public boolean isAvailable() {
        return this.mIsAvailable;
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        super.handleDestroy();
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.REDUCE_BRIGHT_COLORS_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        this.mReduceBrightColorsController.setReduceBrightColorsActivated(!((QSTile.BooleanState) this.mState).value);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(17041367);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        booleanState.value = this.mReduceBrightColorsController.isReduceBrightColorsActivated();
        booleanState.state = booleanState.value ? 2 : 1;
        booleanState.label = this.mContext.getString(17041367);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        booleanState.icon = this.mIcon;
    }

    public void onActivated(boolean z) {
        refreshState();
    }
}
