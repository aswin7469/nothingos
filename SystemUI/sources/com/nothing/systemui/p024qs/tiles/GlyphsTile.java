package com.nothing.systemui.p024qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.media.MediaOutputConstants;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.nothing.systemui.statusbar.policy.GlyphsController;
import javax.inject.Inject;

/* renamed from: com.nothing.systemui.qs.tiles.GlyphsTile */
public class GlyphsTile extends QSTileImpl<QSTile.BooleanState> {
    private final GlyphsController.Callback mCallBack;
    private final GlyphsController mGlyphsController;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_glyphs);

    public int getMetricsCategory() {
        return 0;
    }

    @Inject
    public GlyphsTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, GlyphsController glyphsController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        C42051 r1 = new GlyphsController.Callback() {
            public void onGlyphsChange() {
                GlyphsTile.this.refreshState();
            }
        };
        this.mCallBack = r1;
        this.mGlyphsController = glyphsController;
        glyphsController.observe(getLifecycle(), r1);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        this.mGlyphsController.setGlyphsEnable();
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        booleanState.icon = this.mIcon;
        booleanState.label = this.mContext.getString(C1894R.string.quick_settings_glyphs_label);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        booleanState.value = this.mGlyphsController.getGlyphsEnabled();
        booleanState.state = booleanState.value ? 2 : 1;
    }

    public Intent getLongClickIntent() {
        Intent intent = new Intent();
        intent.setAction("android.settings.ACTION_GLYPHS_SETTINGS");
        intent.setPackage(MediaOutputConstants.SETTINGS_PACKAGE_NAME);
        return intent;
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1894R.string.quick_settings_glyphs_label);
    }
}
