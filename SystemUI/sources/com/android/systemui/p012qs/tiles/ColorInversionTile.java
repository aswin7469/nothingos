package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.SettingObserver;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.SecureSettings;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.biometrics.NTColorController;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.ColorInversionTile */
public class ColorInversionTile extends QSTileImpl<QSTile.BooleanState> {
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_invert_colors);
    private boolean mIsUnavailable = false;
    private final SettingObserver mSetting;

    public int getMetricsCategory() {
        return 116;
    }

    @Inject
    public ColorInversionTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, UserTracker userTracker, SecureSettings secureSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mSetting = new SettingObserver(secureSettings, this.mHandler, "accessibility_display_inversion_enabled", userTracker.getUserId()) {
            /* access modifiers changed from: protected */
            public void handleValueChanged(int i, boolean z) {
                ColorInversionTile.this.handleRefreshState(Integer.valueOf(i));
            }
        };
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        super.handleDestroy();
        this.mSetting.setListening(false);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mSetting.setListening(z);
    }

    /* access modifiers changed from: protected */
    public void handleUserSwitch(int i) {
        this.mSetting.setUserId(i);
        handleRefreshState(Integer.valueOf(this.mSetting.getValue()));
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.COLOR_INVERSION_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (!this.mIsUnavailable) {
            this.mSetting.setValue(((QSTile.BooleanState) this.mState).value ^ true ? 1 : 0);
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1894R.string.quick_settings_inversion_label);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i = 1;
        booleanState.value = (obj instanceof Integer ? ((Integer) obj).intValue() : this.mSetting.getValue()) != 0;
        if (booleanState.value) {
            i = 2;
        }
        booleanState.state = i;
        booleanState.label = this.mContext.getString(C1894R.string.quick_settings_inversion_label);
        booleanState.icon = this.mIcon;
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        boolean isColorControlled = ((NTColorController) NTDependencyEx.get(NTColorController.class)).isColorControlled();
        this.mIsUnavailable = isColorControlled;
        if (isColorControlled) {
            booleanState.state = 0;
        }
    }
}
