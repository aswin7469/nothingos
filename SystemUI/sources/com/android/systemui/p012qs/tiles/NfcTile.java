package com.android.systemui.p012qs.tiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.nothing.systemui.statusbar.policy.NfcController;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.NfcTile */
public class NfcTile extends QSTileImpl<QSTile.BooleanState> {
    private static final String NFC = "nfc";
    private NfcAdapter mAdapter;
    private BroadcastDispatcher mBroadcastDispatcher;
    private final NfcController.CallBack mCallBack;
    private final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qs_nfc);
    private boolean mListening;
    /* access modifiers changed from: private */
    public final NfcController mNfcController;
    private BroadcastReceiver mNfcReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            NfcTile.this.refreshState();
        }
    };

    public int getMetricsCategory() {
        return 800;
    }

    /* access modifiers changed from: protected */
    public void handleUserSwitch(int i) {
    }

    @Inject
    public NfcTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BroadcastDispatcher broadcastDispatcher, NfcController nfcController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        C24072 r1 = new NfcController.CallBack() {
            public void onBatteryWirelessChange() {
                NTLogUtil.m1686d(NfcTile.this.TAG, "isWirelessCharging: " + NfcTile.this.mNfcController.isWirelessCharging());
                NfcTile.this.mHandler.postDelayed(new NfcTile$2$$ExternalSyntheticLambda0(this), 1000);
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onBatteryWirelessChange$0$com-android-systemui-qs-tiles-NfcTile$2 */
            public /* synthetic */ void mo36898xbc94029b() {
                NfcTile.this.refreshState();
            }
        };
        this.mCallBack = r1;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mNfcController = nfcController;
        nfcController.observe(getLifecycle(), r1);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mListening = z;
        if (z) {
            this.mBroadcastDispatcher.registerReceiver(this.mNfcReceiver, new IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED"));
        } else {
            this.mBroadcastDispatcher.unregisterReceiver(this.mNfcReceiver);
        }
    }

    public boolean isAvailable() {
        if (this.mContext.getString(C1894R.string.quick_settings_tiles_stock).contains(NFC)) {
            return this.mContext.getPackageManager().hasSystemFeature("android.hardware.nfc");
        }
        return false;
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.NFC_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (getAdapter() != null && !this.mNfcController.isWirelessCharging()) {
            if (!getAdapter().isEnabled()) {
                getAdapter().enable();
            } else {
                getAdapter().disable();
            }
            this.mHandler.postDelayed(new NfcTile$$ExternalSyntheticLambda0(this), 1000);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleClick$0$com-android-systemui-qs-tiles-NfcTile  reason: not valid java name */
    public /* synthetic */ void m2984lambda$handleClick$0$comandroidsystemuiqstilesNfcTile() {
        refreshState();
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1894R.string.quick_settings_nfc_label);
    }

    /* access modifiers changed from: protected */
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
        booleanState.label = this.mContext.getString(C1894R.string.quick_settings_nfc_label);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.contentDescription = booleanState.label;
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
