package com.nothing.systemui.volume;

import android.content.Context;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;

public class VolumeDialogImplEx {
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onPhoneStateChanged(int i) {
            int unused = VolumeDialogImplEx.this.mPhoneState = i;
        }
    };
    /* access modifiers changed from: private */
    public int mPhoneState = 0;

    public int getVolumeDialogPosition(Context context, boolean z) {
        if (z) {
            return context.getResources().getInteger(C1894R.integer.volume_dialog_gravity);
        }
        return context.getResources().getInteger(C1894R.integer.nt_volume_dialog_gravity);
    }

    public int getVolumeDialogOffset(Context context, boolean z) {
        if (z) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(C1894R.dimen.nt_volume_dialog_top_offset);
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

    public void registerCallback() {
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    public void removeCallback() {
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).removeCallback(this.mKeyguardUpdateMonitorCallback);
    }

    public int getPhoneState() {
        return this.mPhoneState;
    }
}
