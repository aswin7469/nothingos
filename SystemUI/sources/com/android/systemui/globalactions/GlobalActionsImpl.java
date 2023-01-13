package com.android.systemui.globalactions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import javax.inject.Inject;

public class GlobalActionsImpl implements GlobalActions, CommandQueue.Callbacks {
    private final BlurUtils mBlurUtils;
    private final CommandQueue mCommandQueue;
    private final Context mContext;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private boolean mDisabled;
    private final GlobalActionsDialogLite mGlobalActionsDialog;
    private final KeyguardStateController mKeyguardStateController;

    @Inject
    public GlobalActionsImpl(Context context, CommandQueue commandQueue, GlobalActionsDialogLite globalActionsDialogLite, BlurUtils blurUtils, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController) {
        this.mContext = context;
        this.mGlobalActionsDialog = globalActionsDialogLite;
        this.mKeyguardStateController = keyguardStateController;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mCommandQueue = commandQueue;
        this.mBlurUtils = blurUtils;
        commandQueue.addCallback((CommandQueue.Callbacks) this);
    }

    public void destroy() {
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        this.mGlobalActionsDialog.destroy();
    }

    public void showGlobalActions(GlobalActions.GlobalActionsManager globalActionsManager) {
        if (!this.mDisabled) {
            this.mGlobalActionsDialog.showOrHideDialog(this.mKeyguardStateController.isShowing(), this.mDeviceProvisionedController.isDeviceProvisioned(), (View) null);
        }
    }

    public void showShutdownUi(boolean z, String str) {
        int i;
        ScrimDrawable scrimDrawable = new ScrimDrawable();
        Dialog dialog = new Dialog(this.mContext, C1894R.style.Theme_SystemUI_Dialog_GlobalActions);
        dialog.setOnShowListener(new GlobalActionsImpl$$ExternalSyntheticLambda0(this, scrimDrawable, dialog));
        Window window = dialog.getWindow();
        window.requestFeature(1);
        window.getAttributes().systemUiVisibility |= 1792;
        window.getDecorView();
        window.getAttributes().width = -1;
        window.getAttributes().height = -1;
        window.getAttributes().layoutInDisplayCutoutMode = 3;
        window.setType(2020);
        window.getAttributes().setFitInsetsTypes(0);
        window.clearFlags(2);
        window.addFlags(17629472);
        window.setBackgroundDrawable(scrimDrawable);
        window.setWindowAnimations(C1894R.style.Animation_ShutdownUi);
        dialog.setContentView(17367321);
        dialog.setCancelable(false);
        if (this.mBlurUtils.supportsBlursOnWindows()) {
            i = Utils.getColorAttrDefaultColor(this.mContext, C1894R.attr.wallpaperTextColor);
        } else {
            i = this.mContext.getResources().getColor(C1894R.C1895color.global_actions_shutdown_ui_text);
        }
        ((ProgressBar) dialog.findViewById(16908301)).getIndeterminateDrawable().setTint(i);
        TextView textView = (TextView) dialog.findViewById(16908308);
        TextView textView2 = (TextView) dialog.findViewById(16908309);
        textView.setTextColor(i);
        textView2.setTextColor(i);
        textView2.setText(getRebootMessage(z, str));
        String reasonMessage = getReasonMessage(str);
        if (reasonMessage != null) {
            textView.setVisibility(0);
            textView.setText(reasonMessage);
        }
        dialog.show();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showShutdownUi$0$com-android-systemui-globalactions-GlobalActionsImpl */
    public /* synthetic */ void mo33012x50e7553b(ScrimDrawable scrimDrawable, Dialog dialog, DialogInterface dialogInterface) {
        if (this.mBlurUtils.supportsBlursOnWindows()) {
            scrimDrawable.setAlpha(255);
            this.mBlurUtils.applyBlur(dialog.getWindow().getDecorView().getViewRootImpl(), (int) this.mBlurUtils.blurRadiusOfRatio(1.0f), true);
            return;
        }
        scrimDrawable.setAlpha((int) (this.mContext.getResources().getFloat(C1894R.dimen.shutdown_scrim_behind_alpha) * 255.0f));
    }

    private int getRebootMessage(boolean z, String str) {
        if (str != null && str.startsWith("recovery-update")) {
            return 17041363;
        }
        if ((str == null || !str.equals("recovery")) && !z) {
            return 17041512;
        }
        return 17041359;
    }

    private String getReasonMessage(String str) {
        if (str != null && str.startsWith("recovery-update")) {
            return this.mContext.getString(17041364);
        }
        if (str == null || !str.equals("recovery")) {
            return null;
        }
        return this.mContext.getString(17041360);
    }

    public void disable(int i, int i2, int i3, boolean z) {
        boolean z2 = (i3 & 8) != 0;
        if (i == this.mContext.getDisplayId() && z2 != this.mDisabled) {
            this.mDisabled = z2;
            if (z2) {
                this.mGlobalActionsDialog.dismissDialog();
            }
        }
    }
}
