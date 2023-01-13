package com.android.systemui.volume;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.VolumePolicy;
import android.os.Bundle;
import com.android.settingslib.applications.InterestingConfigChanges;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.p012qs.tiles.DndTile;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@SysUISingleton
public class VolumeDialogComponent implements VolumeComponent, TunerService.Tunable, VolumeDialogControllerImpl.UserActivityListener {
    public static final boolean DEFAULT_DO_NOT_DISTURB_WHEN_SILENT = false;
    public static final boolean DEFAULT_VOLUME_DOWN_TO_ENTER_SILENT = false;
    public static final boolean DEFAULT_VOLUME_UP_TO_EXIT_SILENT = false;
    public static final String VOLUME_DOWN_SILENT = "sysui_volume_down_silent";
    public static final String VOLUME_SILENT_DO_NOT_DISTURB = "sysui_do_not_disturb";
    public static final String VOLUME_UP_SILENT = "sysui_volume_up_silent";
    /* access modifiers changed from: private */
    public static final Intent ZEN_PRIORITY_SETTINGS = new Intent("android.settings.ZEN_MODE_PRIORITY_SETTINGS");
    /* access modifiers changed from: private */
    public static final Intent ZEN_SETTINGS = new Intent("android.settings.ZEN_MODE_SETTINGS");
    private final ActivityStarter mActivityStarter;
    private final InterestingConfigChanges mConfigChanges = new InterestingConfigChanges(-1073741308);
    protected final Context mContext;
    private final VolumeDialogControllerImpl mController;
    private VolumeDialog mDialog;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private final VolumeDialog.Callback mVolumeDialogCallback = new VolumeDialog.Callback() {
        public void onZenSettingsClicked() {
            VolumeDialogComponent.this.startSettings(VolumeDialogComponent.ZEN_SETTINGS);
        }

        public void onZenPrioritySettingsClicked() {
            VolumeDialogComponent.this.startSettings(VolumeDialogComponent.ZEN_PRIORITY_SETTINGS);
        }
    };
    private VolumePolicy mVolumePolicy = new VolumePolicy(false, false, false, 400);

    static /* synthetic */ VolumeDialog lambda$new$0(VolumeDialog volumeDialog) {
        return volumeDialog;
    }

    public void dispatchDemoCommand(String str, Bundle bundle) {
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
    }

    @Inject
    public VolumeDialogComponent(Context context, KeyguardViewMediator keyguardViewMediator, ActivityStarter activityStarter, VolumeDialogControllerImpl volumeDialogControllerImpl, DemoModeController demoModeController, PluginDependencyProvider pluginDependencyProvider, ExtensionController extensionController, TunerService tunerService, VolumeDialog volumeDialog) {
        this.mContext = context;
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mActivityStarter = activityStarter;
        this.mController = volumeDialogControllerImpl;
        volumeDialogControllerImpl.setUserActivityListener(this);
        pluginDependencyProvider.allowPluginDependency(VolumeDialogController.class);
        extensionController.newExtension(VolumeDialog.class).withPlugin(VolumeDialog.class).withDefault(new VolumeDialogComponent$$ExternalSyntheticLambda0(volumeDialog)).withCallback(new VolumeDialogComponent$$ExternalSyntheticLambda1(this)).build();
        applyConfiguration();
        tunerService.addTunable(this, VOLUME_DOWN_SILENT, VOLUME_UP_SILENT, VOLUME_SILENT_DO_NOT_DISTURB);
        demoModeController.addCallback((DemoMode) this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-volume-VolumeDialogComponent  reason: not valid java name */
    public /* synthetic */ void m3315lambda$new$1$comandroidsystemuivolumeVolumeDialogComponent(VolumeDialog volumeDialog) {
        VolumeDialog volumeDialog2 = this.mDialog;
        if (volumeDialog2 != null) {
            volumeDialog2.destroy();
        }
        this.mDialog = volumeDialog;
        volumeDialog.init(2020, this.mVolumeDialogCallback);
    }

    public void onTuningChanged(String str, String str2) {
        boolean z = this.mVolumePolicy.volumeDownToEnterSilent;
        boolean z2 = this.mVolumePolicy.volumeUpToExitSilent;
        boolean z3 = this.mVolumePolicy.doNotDisturbWhenSilent;
        if (VOLUME_DOWN_SILENT.equals(str)) {
            z = TunerService.parseIntegerSwitch(str2, false);
        } else if (VOLUME_UP_SILENT.equals(str)) {
            z2 = TunerService.parseIntegerSwitch(str2, false);
        } else if (VOLUME_SILENT_DO_NOT_DISTURB.equals(str)) {
            z3 = TunerService.parseIntegerSwitch(str2, false);
        }
        setVolumePolicy(z, z2, z3, this.mVolumePolicy.vibrateToSilentDebounce);
    }

    private void setVolumePolicy(boolean z, boolean z2, boolean z3, int i) {
        VolumePolicy volumePolicy = new VolumePolicy(z, z2, z3, i);
        this.mVolumePolicy = volumePolicy;
        this.mController.setVolumePolicy(volumePolicy);
    }

    /* access modifiers changed from: package-private */
    public void setEnableDialogs(boolean z, boolean z2) {
        this.mController.setEnableDialogs(z, z2);
    }

    public void onUserActivity() {
        this.mKeyguardViewMediator.userActivity();
    }

    private void applyConfiguration() {
        this.mController.setVolumePolicy(this.mVolumePolicy);
        this.mController.showDndTile();
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mConfigChanges.applyNewConfig(this.mContext.getResources())) {
            this.mController.mCallbacks.onConfigurationChanged();
        }
    }

    public void dismissNow() {
        this.mController.dismiss();
    }

    public List<String> demoCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("volume");
        return arrayList;
    }

    public void register() {
        this.mController.register();
        DndTile.setCombinedIcon(this.mContext, true);
    }

    /* access modifiers changed from: private */
    public void startSettings(Intent intent) {
        this.mActivityStarter.startActivity(intent, true, true);
    }
}
