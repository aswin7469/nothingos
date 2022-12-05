package com.android.systemui.telephony;

import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
class TelephonyCallback extends android.telephony.TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.CallStateListener, TelephonyCallback.ServiceStateListener {
    private final List<TelephonyCallback.ActiveDataSubscriptionIdListener> mActiveDataSubscriptionIdListeners = new ArrayList();
    private final List<TelephonyCallback.CallStateListener> mCallStateListeners = new ArrayList();
    private final List<TelephonyCallback.ServiceStateListener> mServiceStateListeners = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAnyListeners() {
        return !this.mActiveDataSubscriptionIdListeners.isEmpty() || !this.mCallStateListeners.isEmpty() || !this.mServiceStateListeners.isEmpty();
    }

    @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
    public void onActiveDataSubscriptionIdChanged(final int i) {
        ArrayList arrayList;
        synchronized (this.mActiveDataSubscriptionIdListeners) {
            arrayList = new ArrayList(this.mActiveDataSubscriptionIdListeners);
        }
        arrayList.forEach(new Consumer() { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TelephonyCallback.ActiveDataSubscriptionIdListener) obj).onActiveDataSubscriptionIdChanged(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mActiveDataSubscriptionIdListeners.add(activeDataSubscriptionIdListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeActiveDataSubscriptionIdListener(TelephonyCallback.ActiveDataSubscriptionIdListener activeDataSubscriptionIdListener) {
        this.mActiveDataSubscriptionIdListeners.remove(activeDataSubscriptionIdListener);
    }

    @Override // android.telephony.TelephonyCallback.CallStateListener
    public void onCallStateChanged(final int i) {
        ArrayList arrayList;
        synchronized (this.mCallStateListeners) {
            arrayList = new ArrayList(this.mCallStateListeners);
        }
        arrayList.forEach(new Consumer() { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TelephonyCallback.CallStateListener) obj).onCallStateChanged(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addCallStateListener(TelephonyCallback.CallStateListener callStateListener) {
        this.mCallStateListeners.add(callStateListener);
    }

    @Override // android.telephony.TelephonyCallback.ServiceStateListener
    public void onServiceStateChanged(final ServiceState serviceState) {
        ArrayList arrayList;
        synchronized (this.mServiceStateListeners) {
            arrayList = new ArrayList(this.mServiceStateListeners);
        }
        arrayList.forEach(new Consumer() { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TelephonyCallback.ServiceStateListener) obj).onServiceStateChanged(serviceState);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mServiceStateListeners.add(serviceStateListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeServiceStateListener(TelephonyCallback.ServiceStateListener serviceStateListener) {
        this.mServiceStateListeners.remove(serviceStateListener);
    }
}
