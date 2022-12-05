package com.nothingos.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.nothingos.systemui.statusbar.policy.GlyphsController;
/* loaded from: classes2.dex */
public class GlyphsTile extends QSTileImpl<QSTile.BooleanState> {
    private final GlyphsController.Callback mCallBack;
    private final GlyphsController mGlyphsController;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_glyphs);

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 0;
    }

    public GlyphsTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, GlyphsController glyphsController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        GlyphsController.Callback callback = new GlyphsController.Callback() { // from class: com.nothingos.systemui.qs.tiles.GlyphsTile.1
            @Override // com.nothingos.systemui.statusbar.policy.GlyphsController.Callback
            public void onGlyphsChange() {
                GlyphsTile.this.refreshState();
            }
        };
        this.mCallBack = callback;
        this.mGlyphsController = glyphsController;
        glyphsController.observe(mo1437getLifecycle(), (Lifecycle) callback);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.BooleanState mo1926newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        this.mGlyphsController.setGlyphsEnable();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        booleanState.icon = this.mIcon;
        booleanState.label = this.mContext.getString(R$string.quick_settings_glyphs_label);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
        boolean glyphsEnabled = this.mGlyphsController.getGlyphsEnabled();
        booleanState.value = glyphsEnabled;
        booleanState.state = glyphsEnabled ? 2 : 1;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        Intent intent = new Intent();
        intent.setAction("android.settings.ACTION_GLYPHS_SETTINGS");
        intent.setPackage("com.android.settings");
        return intent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_glyphs_label);
    }
}
