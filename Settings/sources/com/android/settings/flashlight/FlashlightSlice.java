package com.android.settings.flashlight;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.CustomSliceable;
import com.android.settingslib.Utils;
/* loaded from: classes.dex */
public class FlashlightSlice implements CustomSliceable {
    private final Context mContext;

    @Override // com.android.settings.slices.CustomSliceable
    public Intent getIntent() {
        return null;
    }

    public FlashlightSlice(Context context) {
        this.mContext = context;
    }

    @Override // com.android.settings.slices.CustomSliceable
    public Slice getSlice() {
        if (!isFlashlightAvailable(this.mContext)) {
            return null;
        }
        PendingIntent broadcastIntent = getBroadcastIntent(this.mContext);
        return new ListBuilder(this.mContext, CustomSliceRegistry.FLASHLIGHT_SLICE_URI, -1L).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext)).addRow(new ListBuilder.RowBuilder().setTitle(this.mContext.getText(R.string.power_flashlight)).setTitleItem(IconCompat.createWithResource(this.mContext, R.drawable.ic_signal_flashlight), 0).setPrimaryAction(SliceAction.createToggle(broadcastIntent, null, isFlashlightEnabled(this.mContext)))).build();
    }

    @Override // com.android.settings.slices.CustomSliceable
    public Uri getUri() {
        return CustomSliceRegistry.FLASHLIGHT_SLICE_URI;
    }

    @Override // com.android.settings.slices.Sliceable
    public IntentFilter getIntentFilter() {
        return new IntentFilter("com.android.settings.flashlight.action.FLASHLIGHT_CHANGED");
    }

    @Override // com.android.settings.slices.CustomSliceable
    public void onNotifyChange(Intent intent) {
        try {
            String cameraId = getCameraId(this.mContext);
            if (cameraId != null) {
                ((CameraManager) this.mContext.getSystemService(CameraManager.class)).setTorchMode(cameraId, intent.getBooleanExtra("android.app.slice.extra.TOGGLE_STATE", isFlashlightEnabled(this.mContext)));
            }
        } catch (CameraAccessException e) {
            Log.e("FlashlightSlice", "Camera couldn't set torch mode.", e);
        }
        this.mContext.getContentResolver().notifyChange(CustomSliceRegistry.FLASHLIGHT_SLICE_URI, null);
    }

    private static String getCameraId(Context context) throws CameraAccessException {
        String[] cameraIdList;
        CameraManager cameraManager = (CameraManager) context.getSystemService(CameraManager.class);
        for (String str : cameraManager.getCameraIdList()) {
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(str);
            Boolean bool = (Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            Integer num = (Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (bool != null && bool.booleanValue() && num != null && num.intValue() == 1) {
                return str;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001f  */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static boolean isFlashlightAvailable(Context context) {
        int i;
        try {
        } catch (CameraAccessException e) {
            Log.e("FlashlightSlice", "Error getting camera id.", e);
        }
        if (getCameraId(context) != null) {
            i = 1;
            return Settings.Secure.getInt(context.getContentResolver(), "flashlight_available", i) != 1;
        }
        i = 0;
        if (Settings.Secure.getInt(context.getContentResolver(), "flashlight_available", i) != 1) {
        }
    }

    private static boolean isFlashlightEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "flashlight_enabled", 0) == 1;
    }
}
