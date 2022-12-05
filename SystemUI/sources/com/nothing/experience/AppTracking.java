package com.nothing.experience;

import android.content.Context;
import android.os.Bundle;
import com.nothing.experience.sdk.NothingExperience;
/* loaded from: classes2.dex */
public class AppTracking {
    public static AppTracking instance;
    private NothingExperience mSdk;

    private AppTracking(Context context) {
        this.mSdk = NothingExperience.getInstance(context);
    }

    public static AppTracking getInstance(Context context) {
        if (instance == null) {
            synchronized (AppTracking.class) {
                if (instance == null) {
                    instance = new AppTracking(context);
                }
            }
        }
        return instance;
    }

    public boolean logProductEvent(String str, Bundle bundle) {
        return this.mSdk.logEvent(str, bundle);
    }
}