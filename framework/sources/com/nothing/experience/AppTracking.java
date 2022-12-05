package com.nothing.experience;

import android.content.Context;
import android.os.Bundle;
import com.nothing.experience.sdk.NothingExperience;
/* loaded from: classes4.dex */
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

    public boolean logProductEvent(String eventName, Bundle eventParams) {
        return this.mSdk.logEvent(eventName, eventParams);
    }

    public boolean logActivationEvent(String eventName, Bundle eventParams) {
        return this.mSdk.logActivationEvent(eventName, eventParams);
    }

    public boolean logQualityEvent(String eventName, Bundle eventParams) {
        return this.mSdk.logQualityEvent(eventName, eventParams);
    }
}
