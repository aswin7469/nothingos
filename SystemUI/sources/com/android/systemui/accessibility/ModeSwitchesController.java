package com.android.systemui.accessibility;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.view.Display;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;

@SysUISingleton
public class ModeSwitchesController implements MagnificationModeSwitch.SwitchListener {
    private MagnificationModeSwitch.SwitchListener mSwitchListenerDelegate;
    private final DisplayIdIndexSupplier<MagnificationModeSwitch> mSwitchSupplier;

    @Inject
    public ModeSwitchesController(Context context) {
        this.mSwitchSupplier = new SwitchSupplier(context, (DisplayManager) context.getSystemService(DisplayManager.class), new ModeSwitchesController$$ExternalSyntheticLambda0(this));
    }

    ModeSwitchesController(DisplayIdIndexSupplier<MagnificationModeSwitch> displayIdIndexSupplier) {
        this.mSwitchSupplier = displayIdIndexSupplier;
    }

    /* access modifiers changed from: package-private */
    public void showButton(int i, int i2) {
        MagnificationModeSwitch magnificationModeSwitch = this.mSwitchSupplier.get(i);
        if (magnificationModeSwitch != null) {
            magnificationModeSwitch.showButton(i2);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeButton(int i) {
        MagnificationModeSwitch magnificationModeSwitch = this.mSwitchSupplier.get(i);
        if (magnificationModeSwitch != null) {
            magnificationModeSwitch.mo29919xbe988fce();
        }
    }

    /* access modifiers changed from: package-private */
    public void onConfigurationChanged(int i) {
        this.mSwitchSupplier.forEach(new ModeSwitchesController$$ExternalSyntheticLambda1(i));
    }

    public void onSwitch(int i, int i2) {
        MagnificationModeSwitch.SwitchListener switchListener = this.mSwitchListenerDelegate;
        if (switchListener != null) {
            switchListener.onSwitch(i, i2);
        }
    }

    public void setSwitchListenerDelegate(MagnificationModeSwitch.SwitchListener switchListener) {
        this.mSwitchListenerDelegate = switchListener;
    }

    private static class SwitchSupplier extends DisplayIdIndexSupplier<MagnificationModeSwitch> {
        private final Context mContext;
        private final MagnificationModeSwitch.SwitchListener mSwitchListener;

        SwitchSupplier(Context context, DisplayManager displayManager, MagnificationModeSwitch.SwitchListener switchListener) {
            super(displayManager);
            this.mContext = context;
            this.mSwitchListener = switchListener;
        }

        /* access modifiers changed from: protected */
        public MagnificationModeSwitch createInstance(Display display) {
            return new MagnificationModeSwitch(this.mContext.createWindowContext(display, 2039, (Bundle) null), this.mSwitchListener);
        }
    }
}
