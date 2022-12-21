package com.android.systemui.volume;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.p012qs.tiles.DndTile;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public class VolumeUI extends CoreStartable {
    private static boolean LOGD = Log.isLoggable(TAG, 3);
    private static final String TAG = "VolumeUI";
    private boolean mEnabled;
    private final Handler mHandler = new Handler();
    private VolumeDialogComponent mVolumeComponent;

    @Inject
    public VolumeUI(Context context, VolumeDialogComponent volumeDialogComponent) {
        super(context);
        this.mVolumeComponent = volumeDialogComponent;
    }

    public void start() {
        boolean z = this.mContext.getResources().getBoolean(C1893R.bool.enable_volume_ui);
        boolean z2 = this.mContext.getResources().getBoolean(C1893R.bool.enable_safety_warning);
        boolean z3 = z || z2;
        this.mEnabled = z3;
        if (z3) {
            this.mVolumeComponent.setEnableDialogs(z, z2);
            setDefaultVolumeController();
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mEnabled) {
            this.mVolumeComponent.onConfigurationChanged(configuration);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("mEnabled=");
        printWriter.println(this.mEnabled);
        if (this.mEnabled) {
            this.mVolumeComponent.dump(printWriter, strArr);
        }
    }

    private void setDefaultVolumeController() {
        DndTile.setVisible(this.mContext, true);
        if (LOGD) {
            Log.d(TAG, "Registering default volume controller");
        }
        this.mVolumeComponent.register();
    }
}
