package com.nothing.settings.display.rangle;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.NtPackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class RAngleController {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private static RAngleController mNtRAngleAdjustController;
    private ContentResolver mContentResolver;
    private Context mContext;
    private NtPackageManager mNtPackageManager = NtPackageManager.getInstance();

    public static RAngleController getInstance(Context context) {
        if (mNtRAngleAdjustController == null) {
            mNtRAngleAdjustController = new RAngleController(context);
        }
        return mNtRAngleAdjustController;
    }

    public RAngleController(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mContentResolver = applicationContext.getContentResolver();
    }

    public boolean isRAngleNotAdjustOpened(String str) {
        int i = Settings.Global.getInt(this.mContentResolver, "r_angle_adjust_" + str, -2);
        boolean z = false;
        boolean z2 = true;
        if (i != -2) {
            return i == 1 || i == 2;
        }
        if (!"com.tools.cit".equals(str) || (!Build.IS_USER && !Build.IS_USERDEBUG)) {
            z2 = false;
        } else {
            Log.d("RAngleController", "default open for CIT");
            z = true;
        }
        setRAngleNotAdjustOpened(str, z, z2);
        return z;
    }

    private void setForceFull(String str, boolean z) {
        try {
            Log.d("RAngle", "setForceFull: pkg:" + str + " forceFull:" + z);
            this.mNtPackageManager.setForceFull(str, z);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setRAngleNotAdjustOpened(String str, boolean z, boolean z2) {
        int i = z2 ? 2 : 1;
        int i2 = z2 ? 3 : 0;
        if (!z) {
            i = i2;
        }
        if (DEBUG) {
            Log.d("RAngle", "setRAngleNotAdjustOpened " + str + ", value: " + i + ", opened: " + z + ", byUser: " + z2);
        }
        if (z2 || !z) {
            setForceFull(str, z);
        }
        setRAngleStateIntoSettings(str, i);
    }

    private void setRAngleStateIntoSettings(String str, int i) {
        ContentResolver contentResolver = this.mContentResolver;
        Settings.Global.putInt(contentResolver, "r_angle_adjust_" + str, i);
    }

    public void initLocalRAngleAppList() {
        if (Settings.Global.getInt(this.mContentResolver, "r_angle_has_initialized", 0) != 1) {
            for (String rAngleStateIntoSettings : RAngleAppList.APP_ARRAY_ADJUST_R_ANGLE) {
                setRAngleStateIntoSettings(rAngleStateIntoSettings, -1);
            }
            Settings.Global.putInt(this.mContentResolver, "r_angle_has_initialized", 1);
        }
    }
}
