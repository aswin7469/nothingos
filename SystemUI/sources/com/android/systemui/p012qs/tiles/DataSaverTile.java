package com.android.systemui.p012qs.tiles;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.Prefs;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.DataSaverController;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.DataSaverTile */
public class DataSaverTile extends QSTileImpl<QSTile.BooleanState> implements DataSaverController.Listener {
    private final DataSaverController mDataSaverController;
    private final DialogLaunchAnimator mDialogLaunchAnimator;

    public int getMetricsCategory() {
        return UCharacter.UnicodeBlock.GUNJALA_GONDI_ID;
    }

    @Inject
    public DataSaverTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, DataSaverController dataSaverController, DialogLaunchAnimator dialogLaunchAnimator) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mDataSaverController = dataSaverController;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        dataSaverController.observe(getLifecycle(), this);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.DATA_SAVER_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (((QSTile.BooleanState) this.mState).value || Prefs.getBoolean(this.mContext, Prefs.Key.QS_DATA_SAVER_DIALOG_SHOWN, false)) {
            toggleDataSaver();
        } else {
            this.mUiHandler.post(new DataSaverTile$$ExternalSyntheticLambda0(this, view));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleClick$1$com-android-systemui-qs-tiles-DataSaverTile  reason: not valid java name */
    public /* synthetic */ void m2978lambda$handleClick$1$comandroidsystemuiqstilesDataSaverTile(View view) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
        systemUIDialog.setTitle(17040096);
        systemUIDialog.setMessage(17040094);
        systemUIDialog.setPositiveButton(17040095, new DataSaverTile$$ExternalSyntheticLambda1(this));
        systemUIDialog.setNeutralButton(17039360, (DialogInterface.OnClickListener) null);
        systemUIDialog.setShowForAllUsers(true);
        if (view != null) {
            this.mDialogLaunchAnimator.showFromView(systemUIDialog, view);
        } else {
            systemUIDialog.show();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleClick$0$com-android-systemui-qs-tiles-DataSaverTile  reason: not valid java name */
    public /* synthetic */ void m2977lambda$handleClick$0$comandroidsystemuiqstilesDataSaverTile(DialogInterface dialogInterface, int i) {
        toggleDataSaver();
        Prefs.putBoolean(this.mContext, Prefs.Key.QS_DATA_SAVER_DIALOG_SHOWN, true);
    }

    private void toggleDataSaver() {
        ((QSTile.BooleanState) this.mState).value = !this.mDataSaverController.isDataSaverEnabled();
        this.mDataSaverController.setDataSaverEnabled(((QSTile.BooleanState) this.mState).value);
        refreshState(Boolean.valueOf(((QSTile.BooleanState) this.mState).value));
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1894R.string.data_saver);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        if (obj instanceof Boolean) {
            z = ((Boolean) obj).booleanValue();
        } else {
            z = this.mDataSaverController.isDataSaverEnabled();
        }
        booleanState.value = z;
        booleanState.state = booleanState.value ? 2 : 1;
        booleanState.label = this.mContext.getString(C1894R.string.data_saver);
        booleanState.contentDescription = booleanState.label;
        booleanState.icon = QSTileImpl.ResourceIcon.get(booleanState.value ? C1894R.C1896drawable.ic_data_saver : C1894R.C1896drawable.ic_data_saver_off);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
    }

    public void onDataSaverChanged(boolean z) {
        refreshState(Boolean.valueOf(z));
    }
}
