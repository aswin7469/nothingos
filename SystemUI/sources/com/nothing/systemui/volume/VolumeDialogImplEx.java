package com.nothing.systemui.volume;

import android.content.Context;
import android.view.ViewGroup;
import com.android.systemui.C1893R;

public class VolumeDialogImplEx {
    public int getVolumeDialogPosition(Context context, boolean z) {
        if (z) {
            return context.getResources().getInteger(C1893R.integer.volume_dialog_gravity);
        }
        return context.getResources().getInteger(C1893R.integer.nt_volume_dialog_gravity);
    }

    public int getVolumeDialogOffset(Context context, boolean z) {
        if (z) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(C1893R.dimen.nt_volume_dialog_top_offset);
    }

    public float getTranslationX(ViewGroup viewGroup, boolean z) {
        if (!z) {
            return ((float) (-viewGroup.getWidth())) / 2.0f;
        }
        return 0.0f;
    }

    public int getRightMostVisibleRowIndex(boolean z, boolean z2, int i, int i2) {
        if (!z) {
            if (z2) {
                return Math.max(i, i2);
            }
            return Math.min(i, i2);
        } else if (!z2) {
            return Math.max(i, i2);
        } else {
            return Math.min(i, i2);
        }
    }
}
