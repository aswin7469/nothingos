package com.android.systemui.qs.tiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.nothingos.systemui.statusbar.policy.NfcController;
/* loaded from: classes.dex */
public class NfcTile extends QSTileImpl<QSTile.BooleanState> {
    private NfcAdapter mAdapter;
    private BroadcastDispatcher mBroadcastDispatcher;
    private final NfcController.CallBack mCallBack;
    private boolean mListening;
    private final NfcController mNfcController;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_nfc);
    private BroadcastReceiver mNfcReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.tiles.NfcTile.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            NfcTile.this.refreshState();
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 800;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleUserSwitch(int i) {
    }

    public NfcTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BroadcastDispatcher broadcastDispatcher, NfcController nfcController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        NfcController.CallBack callBack = new NfcController.CallBack() { // from class: com.android.systemui.qs.tiles.NfcTile.2
            @Override // com.nothingos.systemui.statusbar.policy.NfcController.CallBack
            public void onBatteryWirelessChange() {
                NfcTile.this.refreshState();
            }
        };
        this.mCallBack = callBack;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mNfcController = nfcController;
        nfcController.observe(mo1437getLifecycle(), (Lifecycle) callBack);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.BooleanState mo1926newTileState() {
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mListening = z;
        if (z) {
            this.mBroadcastDispatcher.registerReceiver(this.mNfcReceiver, new IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED"), null, UserHandle.ALL);
        } else {
            this.mBroadcastDispatcher.unregisterReceiver(this.mNfcReceiver);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        if (this.mContext.getString(R$string.quick_settings_tiles_stock).contains("nfc")) {
            return this.mContext.getPackageManager().hasSystemFeature("android.hardware.nfc");
        }
        return true;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        return new Intent("android.settings.NFC_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (getAdapter() == null || this.mNfcController.isWirelessCharging()) {
            return;
        }
        if (!getAdapter().isEnabled()) {
            getAdapter().enable();
        } else {
            getAdapter().disable();
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return this.mContext.getString(R$string.quick_settings_nfc_label);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i = 1;
        booleanState.value = getAdapter() != null && getAdapter().isEnabled();
        if (getAdapter() == null || this.mNfcController.isWirelessCharging()) {
            i = 0;
        } else if (booleanState.value) {
            i = 2;
        }
        booleanState.state = i;
        booleanState.icon = this.mIcon;
        booleanState.label = this.mContext.getString(R$string.quick_settings_nfc_label);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected String composeChangeAnnouncement() {
        if (((QSTile.BooleanState) this.mState).value) {
            return this.mContext.getString(R$string.quick_settings_nfc_on);
        }
        return this.mContext.getString(R$string.quick_settings_nfc_off);
    }

    private NfcAdapter getAdapter() {
        if (this.mAdapter == null) {
            try {
                this.mAdapter = NfcAdapter.getDefaultAdapter(this.mContext);
            } catch (UnsupportedOperationException unused) {
                this.mAdapter = null;
            }
        }
        return this.mAdapter;
    }
}
