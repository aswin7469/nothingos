package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.QRCodeScannerTile */
public class QRCodeScannerTile extends QSTileImpl<QSTile.State> {
    private static final String TAG = "QRCodeScanner";
    private final QRCodeScannerController.Callback mCallback;
    private final CharSequence mLabel = this.mContext.getString(C1894R.string.qr_code_scanner_title);
    private final QRCodeScannerController mQRCodeScannerController;

    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    @Inject
    public QRCodeScannerTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, QRCodeScannerController qRCodeScannerController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        C24091 r1 = new QRCodeScannerController.Callback() {
            public void onQRCodeScannerActivityChanged() {
                QRCodeScannerTile.this.refreshState();
            }
        };
        this.mCallback = r1;
        this.mQRCodeScannerController = qRCodeScannerController;
        qRCodeScannerController.observe(getLifecycle(), r1);
    }

    /* access modifiers changed from: protected */
    public void handleInitialize() {
        this.mQRCodeScannerController.registerQRCodeScannerChangeObservers(0);
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        super.handleDestroy();
        this.mQRCodeScannerController.unregisterQRCodeScannerChangeObservers(0);
    }

    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        ActivityLaunchAnimator.Controller controller;
        Intent intent = this.mQRCodeScannerController.getIntent();
        if (intent == null) {
            Log.e(TAG, "Expected a non-null intent");
            return;
        }
        if (view == null) {
            controller = null;
        } else {
            controller = ActivityLaunchAnimator.Controller.fromView(view, 32);
        }
        this.mActivityStarter.startActivity(intent, true, controller, true);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.State state, Object obj) {
        state.label = this.mContext.getString(C1894R.string.qr_code_scanner_title);
        state.contentDescription = state.label;
        state.icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_qr_code_scanner);
        state.state = this.mQRCodeScannerController.isEnabledForQuickSettings() ? 2 : 0;
    }

    public boolean isAvailable() {
        return this.mQRCodeScannerController.isCameraAvailable();
    }

    public CharSequence getTileLabel() {
        return this.mLabel;
    }
}
