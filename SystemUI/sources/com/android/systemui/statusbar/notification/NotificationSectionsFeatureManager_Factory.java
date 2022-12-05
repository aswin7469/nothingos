package com.android.systemui.statusbar.notification;

import android.content.Context;
import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationSectionsFeatureManager_Factory implements Factory<NotificationSectionsFeatureManager> {
    private final Provider<Context> contextProvider;
    private final Provider<DeviceConfigProxy> proxyProvider;

    public NotificationSectionsFeatureManager_Factory(Provider<DeviceConfigProxy> provider, Provider<Context> provider2) {
        this.proxyProvider = provider;
        this.contextProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public NotificationSectionsFeatureManager mo1933get() {
        return newInstance(this.proxyProvider.mo1933get(), this.contextProvider.mo1933get());
    }

    public static NotificationSectionsFeatureManager_Factory create(Provider<DeviceConfigProxy> provider, Provider<Context> provider2) {
        return new NotificationSectionsFeatureManager_Factory(provider, provider2);
    }

    public static NotificationSectionsFeatureManager newInstance(DeviceConfigProxy deviceConfigProxy, Context context) {
        return new NotificationSectionsFeatureManager(deviceConfigProxy, context);
    }
}
