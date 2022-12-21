package com.android.systemui.p012qs.external;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

/* renamed from: com.android.systemui.qs.external.PackageManagerAdapter_Factory */
public final class PackageManagerAdapter_Factory implements Factory<PackageManagerAdapter> {
    private final Provider<Context> contextProvider;

    public PackageManagerAdapter_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public PackageManagerAdapter get() {
        return newInstance(this.contextProvider.get());
    }

    public static PackageManagerAdapter_Factory create(Provider<Context> provider) {
        return new PackageManagerAdapter_Factory(provider);
    }

    public static PackageManagerAdapter newInstance(Context context) {
        return new PackageManagerAdapter(context);
    }
}
