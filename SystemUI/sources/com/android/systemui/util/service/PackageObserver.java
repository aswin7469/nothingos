package com.android.systemui.util.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.android.systemui.util.service.Observer;
import com.google.android.collect.Lists;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;

public class PackageObserver implements Observer {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "PackageObserver";
    /* access modifiers changed from: private */
    public final ArrayList<WeakReference<Observer.Callback>> mCallbacks = Lists.newArrayList();
    private final Context mContext;
    private final String mPackageName;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (PackageObserver.DEBUG) {
                Log.d(PackageObserver.TAG, "package added receiver - onReceive");
            }
            Iterator it = PackageObserver.this.mCallbacks.iterator();
            while (it.hasNext()) {
                Observer.Callback callback = (Observer.Callback) ((WeakReference) it.next()).get();
                if (callback != null) {
                    callback.onSourceChanged();
                } else {
                    it.remove();
                }
            }
        }
    };

    @Inject
    public PackageObserver(Context context, ComponentName componentName) {
        this.mContext = context;
        this.mPackageName = componentName.getPackageName();
    }

    public void addCallback(Observer.Callback callback) {
        if (DEBUG) {
            Log.d(TAG, "addCallback:" + callback);
        }
        this.mCallbacks.add(new WeakReference(callback));
        if (this.mCallbacks.size() <= 1) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
            intentFilter.addDataScheme("package");
            intentFilter.addDataSchemeSpecificPart(this.mPackageName, 0);
            this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        }
    }

    public void removeCallback(Observer.Callback callback) {
        if (DEBUG) {
            Log.d(TAG, "removeCallback:" + callback);
        }
        if (this.mCallbacks.removeIf(new PackageObserver$$ExternalSyntheticLambda0(callback)) && this.mCallbacks.isEmpty()) {
            this.mContext.unregisterReceiver(this.mReceiver);
        }
    }

    static /* synthetic */ boolean lambda$removeCallback$0(Observer.Callback callback, WeakReference weakReference) {
        return weakReference.get() == callback;
    }
}
