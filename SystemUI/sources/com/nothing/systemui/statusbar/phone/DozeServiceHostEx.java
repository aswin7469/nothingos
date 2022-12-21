package com.nothing.systemui.statusbar.phone;

import com.android.systemui.doze.DozeHost;
import java.util.ArrayList;
import java.util.Iterator;

public class DozeServiceHostEx {
    private ArrayList<DozeHost.Callback> mCallbacks = null;

    public void init(ArrayList<DozeHost.Callback> arrayList) {
        this.mCallbacks = arrayList;
    }

    public void fireLiftWake() {
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onLiftWake();
        }
    }

    public void fireMotion() {
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onMotion();
        }
    }

    /* access modifiers changed from: package-private */
    public void fireFingerprintDown() {
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onDozeFingerprintDown();
        }
    }

    /* access modifiers changed from: package-private */
    public void fireFingerprintUp() {
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onDozeFingerprintUp();
        }
    }

    /* access modifiers changed from: package-private */
    public void fireDozeFingerprintRunningStateChanged() {
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onDozeFingerprintRunningStateChanged();
        }
    }

    public void fireTapWakeUp() {
        Iterator<DozeHost.Callback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onTapWakeUp();
        }
    }
}
