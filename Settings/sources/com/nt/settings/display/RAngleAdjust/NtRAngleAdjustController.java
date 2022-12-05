package com.nt.settings.display.RAngleAdjust;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.NtPackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
/* loaded from: classes2.dex */
public class NtRAngleAdjustController {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private static NtRAngleAdjustController mNtRAngleAdjustController;
    private ContentResolver mContentResolver;
    private Context mContext;
    private NtPackageManager mNtPackageManager;

    public static NtRAngleAdjustController getInstance(Context context) {
        if (mNtRAngleAdjustController == null) {
            mNtRAngleAdjustController = new NtRAngleAdjustController(context);
        }
        return mNtRAngleAdjustController;
    }

    public NtRAngleAdjustController(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mContentResolver = applicationContext.getContentResolver();
        this.mNtPackageManager = NtPackageManager.getInstance(this.mContext);
    }

    public boolean isRAngleUnadjustOpened(String str) {
        int i = Settings.Global.getInt(this.mContentResolver, "r_angle_adjust_" + str, -2);
        boolean z = false;
        boolean z2 = true;
        if (i != -2) {
            return i == 1 || i == 2;
        }
        if (!"com.tools.cit".equals(str) || (!Build.IS_USER && !Build.IS_USERDEBUG)) {
            z2 = false;
        } else {
            Log.d("NtRAngleAdjustController", "@_@ ----------------- default open for CIT");
            z = true;
        }
        setRAngleUnadustOpened(str, z, z2);
        return z;
    }

    private void setForceFull(String str, boolean z) {
        try {
            this.mNtPackageManager.setForceFull(str, z);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setRAngleUnadustOpened(String str, boolean z, boolean z2) {
        int i = z ? z2 ? 2 : 1 : z2 ? 3 : 0;
        if (DEBUG) {
            Log.d("NtRAngleAdjustController", "@_@ ----------------- setRAngleUnadustOpened " + str + ", value: " + i + ", opened: " + z + ", byUser: " + z2);
        }
        if (z2 || !z) {
            setForceFull(str, z);
        }
        setAppRAngleState(str, i);
    }

    private void setAppRAngleAdjust(String str) {
        setAppRAngleState(str, -1);
    }

    private void setAppRAngleState(String str, int i) {
        ContentResolver contentResolver = this.mContentResolver;
        Settings.Global.putInt(contentResolver, "r_angle_adjust_" + str, i);
    }

    public void initLocalRAngleAppList() {
        if (!(Settings.Global.getInt(this.mContentResolver, "r_angle_has_initialized", 0) == 1)) {
            for (String str : NtRAngleAdjustAppList.APP_ARRAY_ADJUST_R_ANGLE) {
                setAppRAngleAdjust(str);
            }
            Settings.Global.putInt(this.mContentResolver, "r_angle_has_initialized", 1);
        }
    }

    public boolean needShowTipsDialog() {
        return Settings.Global.getInt(this.mContentResolver, "r_angle_app_state_change_show_tips_dialog", 1) == 1;
    }

    public void setShowTipsDialog(boolean z) {
        Settings.Global.putInt(this.mContentResolver, "r_angle_app_state_change_show_tips_dialog", z ? 1 : 0);
    }
}
