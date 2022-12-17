package com.android.settings.network.telephony;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.ArraySet;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TelephonyDisplayInfoListener {
    private static final TelephonyDisplayInfo mDefaultTelephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    private TelephonyManager mBaseTelephonyManager;
    /* access modifiers changed from: private */
    public Callback mCallback;
    /* access modifiers changed from: private */
    public Map<Integer, TelephonyDisplayInfo> mDisplayInfos = new HashMap();
    private Map<Integer, PhoneStateListener> mListeners = new HashMap();

    public interface Callback {
        void onTelephonyDisplayInfoChanged(int i, TelephonyDisplayInfo telephonyDisplayInfo);
    }

    public TelephonyDisplayInfoListener(Context context, Callback callback) {
        this.mBaseTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mCallback = callback;
    }

    public void resume() {
        for (Integer intValue : this.mListeners.keySet()) {
            startListening(intValue.intValue());
        }
    }

    public void pause() {
        for (Integer intValue : this.mListeners.keySet()) {
            stopListening(intValue.intValue());
        }
    }

    public void updateSubscriptionIds(Set<Integer> set) {
        ArraySet arraySet = new ArraySet(this.mListeners.keySet());
        UnmodifiableIterator<Integer> it = Sets.difference(arraySet, set).iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            stopListening(intValue);
            this.mListeners.remove(Integer.valueOf(intValue));
            this.mDisplayInfos.remove(Integer.valueOf(intValue));
        }
        UnmodifiableIterator<Integer> it2 = Sets.difference(set, arraySet).iterator();
        while (it2.hasNext()) {
            final int intValue2 = it2.next().intValue();
            C11181 r1 = new PhoneStateListener() {
                public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
                    TelephonyDisplayInfoListener.this.mDisplayInfos.put(Integer.valueOf(intValue2), telephonyDisplayInfo);
                    TelephonyDisplayInfoListener.this.mCallback.onTelephonyDisplayInfoChanged(intValue2, telephonyDisplayInfo);
                }
            };
            this.mDisplayInfos.put(Integer.valueOf(intValue2), mDefaultTelephonyDisplayInfo);
            this.mListeners.put(Integer.valueOf(intValue2), r1);
            startListening(intValue2);
        }
    }

    private void startListening(int i) {
        this.mBaseTelephonyManager.createForSubscriptionId(i).listen(this.mListeners.get(Integer.valueOf(i)), 1048576);
    }

    private void stopListening(int i) {
        this.mBaseTelephonyManager.createForSubscriptionId(i).listen(this.mListeners.get(Integer.valueOf(i)), 0);
    }
}
