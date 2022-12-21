package com.android.systemui.p012qs.tiles;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.app.MediaRouteDialogPresenter;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1893R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.CastTile */
public class CastTile extends QSTileImpl<QSTile.BooleanState> {
    private static final Intent CAST_SETTINGS = new Intent("android.settings.CAST_SETTINGS");
    private static final String WFD_ENABLE = "persist.debug.wfd.enable";
    private final Callback mCallback;
    private final CastController mController;
    private final DialogLaunchAnimator mDialogLaunchAnimator;
    private final HotspotController.Callback mHotspotCallback;
    /* access modifiers changed from: private */
    public boolean mHotspotConnected;
    private final KeyguardStateController mKeyguard;
    private final NetworkController mNetworkController;
    private final SignalCallback mSignalCallback;
    /* access modifiers changed from: private */
    public boolean mWifiConnected;

    public int getMetricsCategory() {
        return 114;
    }

    @Inject
    public CastTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, CastController castController, KeyguardStateController keyguardStateController, NetworkController networkController, HotspotController hotspotController, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Callback callback = new Callback();
        this.mCallback = callback;
        C23901 r2 = new SignalCallback() {
            public void setWifiIndicators(WifiIndicators wifiIndicators) {
                boolean z = false;
                if (!SystemProperties.getBoolean(CastTile.WFD_ENABLE, false)) {
                    if (wifiIndicators.enabled && wifiIndicators.qsIcon.visible) {
                        z = true;
                    }
                    if (z != CastTile.this.mWifiConnected) {
                        boolean unused = CastTile.this.mWifiConnected = z;
                        if (!CastTile.this.mHotspotConnected) {
                            CastTile.this.refreshState();
                        }
                    }
                } else if (wifiIndicators.enabled != CastTile.this.mWifiConnected) {
                    boolean unused2 = CastTile.this.mWifiConnected = wifiIndicators.enabled;
                    CastTile.this.refreshState();
                }
            }
        };
        this.mSignalCallback = r2;
        C23912 r3 = new HotspotController.Callback() {
            public void onHotspotChanged(boolean z, int i) {
                boolean z2 = z && i > 0;
                if (z2 != CastTile.this.mHotspotConnected) {
                    boolean unused = CastTile.this.mHotspotConnected = z2;
                    if (!CastTile.this.mWifiConnected) {
                        CastTile.this.refreshState();
                    }
                }
            }
        };
        this.mHotspotCallback = r3;
        this.mController = castController;
        this.mKeyguard = keyguardStateController;
        this.mNetworkController = networkController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        castController.observe((LifecycleOwner) this, callback);
        keyguardStateController.observe((LifecycleOwner) this, callback);
        networkController.observe((LifecycleOwner) this, r2);
        hotspotController.observe((LifecycleOwner) this, r3);
    }

    public QSTile.BooleanState newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.handlesLongClick = false;
        return booleanState;
    }

    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (DEBUG) {
            Log.d(this.TAG, "handleSetListening " + z);
        }
        if (!z) {
            this.mController.setDiscovering(false);
        }
    }

    /* access modifiers changed from: protected */
    public void handleUserSwitch(int i) {
        super.handleUserSwitch(i);
        this.mController.setCurrentUserId(i);
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.CAST_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleLongClick(View view) {
        handleClick(view);
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (((QSTile.BooleanState) getState()).state != 0) {
            List<CastController.CastDevice> activeDevices = getActiveDevices();
            if (!willPopDialog()) {
                this.mController.stopCasting(activeDevices.get(0));
            } else if (!this.mKeyguard.isShowing()) {
                showDialog(view);
            } else {
                this.mActivityStarter.postQSRunnableDismissingKeyguard(new CastTile$$ExternalSyntheticLambda1(this));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleClick$0$com-android-systemui-qs-tiles-CastTile  reason: not valid java name */
    public /* synthetic */ void m2968lambda$handleClick$0$comandroidsystemuiqstilesCastTile() {
        showDialog((View) null);
    }

    private boolean willPopDialog() {
        List<CastController.CastDevice> activeDevices = getActiveDevices();
        return activeDevices.isEmpty() || (activeDevices.get(0).tag instanceof MediaRouter.RouteInfo);
    }

    private List<CastController.CastDevice> getActiveDevices() {
        ArrayList arrayList = new ArrayList();
        for (CastController.CastDevice next : this.mController.getCastDevices()) {
            if (next.state == 2 || next.state == 1) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* renamed from: com.android.systemui.qs.tiles.CastTile$DialogHolder */
    private static class DialogHolder {
        /* access modifiers changed from: private */
        public Dialog mDialog;

        private DialogHolder() {
        }

        /* access modifiers changed from: private */
        public void init(Dialog dialog) {
            this.mDialog = dialog;
        }
    }

    private void showDialog(View view) {
        this.mUiHandler.post(new CastTile$$ExternalSyntheticLambda0(this, view));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showDialog$3$com-android-systemui-qs-tiles-CastTile  reason: not valid java name */
    public /* synthetic */ void m2971lambda$showDialog$3$comandroidsystemuiqstilesCastTile(View view) {
        DialogHolder dialogHolder = new DialogHolder();
        Dialog createDialog = MediaRouteDialogPresenter.createDialog(this.mContext, 4, new CastTile$$ExternalSyntheticLambda2(this, dialogHolder), C1893R.style.Theme_SystemUI_Dialog_Cast, false);
        dialogHolder.init(createDialog);
        SystemUIDialog.setShowForAllUsers(createDialog, true);
        SystemUIDialog.registerDismissListener(createDialog);
        SystemUIDialog.setWindowOnTop(createDialog, this.mKeyguard.isShowing());
        SystemUIDialog.setDialogSize(createDialog);
        this.mUiHandler.post(new CastTile$$ExternalSyntheticLambda3(this, view, createDialog));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showDialog$1$com-android-systemui-qs-tiles-CastTile  reason: not valid java name */
    public /* synthetic */ void m2969lambda$showDialog$1$comandroidsystemuiqstilesCastTile(DialogHolder dialogHolder, View view) {
        ActivityLaunchAnimator.Controller createActivityLaunchController = this.mDialogLaunchAnimator.createActivityLaunchController(view);
        if (createActivityLaunchController == null) {
            dialogHolder.mDialog.dismiss();
        }
        this.mActivityStarter.postStartActivityDismissingKeyguard(getLongClickIntent(), 0, createActivityLaunchController);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showDialog$2$com-android-systemui-qs-tiles-CastTile  reason: not valid java name */
    public /* synthetic */ void m2970lambda$showDialog$2$comandroidsystemuiqstilesCastTile(View view, Dialog dialog) {
        if (view != null) {
            this.mDialogLaunchAnimator.showFromView(dialog, view);
        } else {
            dialog.show();
        }
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1893R.string.quick_settings_cast_title);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        booleanState.label = this.mContext.getString(C1893R.string.quick_settings_cast_title);
        booleanState.contentDescription = booleanState.label;
        booleanState.stateDescription = "";
        booleanState.value = false;
        Iterator<CastController.CastDevice> it = this.mController.getCastDevices().iterator();
        boolean z = false;
        while (true) {
            i = 2;
            if (!it.hasNext()) {
                break;
            }
            CastController.CastDevice next = it.next();
            if (next.state == 2) {
                booleanState.value = true;
                booleanState.secondaryLabel = getDeviceName(next);
                booleanState.stateDescription += NavigationBarInflaterView.BUTTON_SEPARATOR + this.mContext.getString(C1893R.string.accessibility_cast_name, new Object[]{booleanState.label});
                z = false;
                break;
            } else if (next.state == 1) {
                z = true;
            }
        }
        if (z && !booleanState.value) {
            booleanState.secondaryLabel = this.mContext.getString(C1893R.string.quick_settings_connecting);
        }
        booleanState.icon = QSTileImpl.ResourceIcon.get(booleanState.value ? C1893R.C1895drawable.ic_cast_connected : C1893R.C1895drawable.ic_cast);
        if (canCastToWifi() || booleanState.value) {
            if (!booleanState.value) {
                i = 1;
            }
            booleanState.state = i;
            if (!booleanState.value) {
                booleanState.secondaryLabel = "";
            }
            booleanState.expandedAccessibilityClassName = Button.class.getName();
            booleanState.forceExpandIcon = willPopDialog();
        } else {
            booleanState.state = 0;
            booleanState.secondaryLabel = this.mContext.getString(C1893R.string.quick_settings_cast_no_wifi);
            booleanState.forceExpandIcon = false;
        }
        booleanState.stateDescription += ", " + booleanState.secondaryLabel;
    }

    private String getDeviceName(CastController.CastDevice castDevice) {
        if (castDevice.name != null) {
            return castDevice.name;
        }
        return this.mContext.getString(C1893R.string.quick_settings_cast_device_default_name);
    }

    private boolean canCastToWifi() {
        return this.mWifiConnected || this.mHotspotConnected;
    }

    /* renamed from: com.android.systemui.qs.tiles.CastTile$Callback */
    private final class Callback implements CastController.Callback, KeyguardStateController.Callback {
        private Callback() {
        }

        public void onCastDevicesChanged() {
            CastTile.this.refreshState();
        }

        public void onKeyguardShowingChanged() {
            CastTile.this.refreshState();
        }
    }
}
