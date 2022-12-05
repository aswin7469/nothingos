package com.android.systemui.statusbar.policy;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class SmartReplyStateInflaterImpl_Factory implements Factory<SmartReplyStateInflaterImpl> {
    private final Provider<ActivityManagerWrapper> activityManagerWrapperProvider;
    private final Provider<SmartReplyConstants> constantsProvider;
    private final Provider<DevicePolicyManagerWrapper> devicePolicyManagerWrapperProvider;
    private final Provider<PackageManagerWrapper> packageManagerWrapperProvider;
    private final Provider<SmartActionInflater> smartActionsInflaterProvider;
    private final Provider<SmartReplyInflater> smartRepliesInflaterProvider;

    public SmartReplyStateInflaterImpl_Factory(Provider<SmartReplyConstants> provider, Provider<ActivityManagerWrapper> provider2, Provider<PackageManagerWrapper> provider3, Provider<DevicePolicyManagerWrapper> provider4, Provider<SmartReplyInflater> provider5, Provider<SmartActionInflater> provider6) {
        this.constantsProvider = provider;
        this.activityManagerWrapperProvider = provider2;
        this.packageManagerWrapperProvider = provider3;
        this.devicePolicyManagerWrapperProvider = provider4;
        this.smartRepliesInflaterProvider = provider5;
        this.smartActionsInflaterProvider = provider6;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public SmartReplyStateInflaterImpl mo1933get() {
        return newInstance(this.constantsProvider.mo1933get(), this.activityManagerWrapperProvider.mo1933get(), this.packageManagerWrapperProvider.mo1933get(), this.devicePolicyManagerWrapperProvider.mo1933get(), this.smartRepliesInflaterProvider.mo1933get(), this.smartActionsInflaterProvider.mo1933get());
    }

    public static SmartReplyStateInflaterImpl_Factory create(Provider<SmartReplyConstants> provider, Provider<ActivityManagerWrapper> provider2, Provider<PackageManagerWrapper> provider3, Provider<DevicePolicyManagerWrapper> provider4, Provider<SmartReplyInflater> provider5, Provider<SmartActionInflater> provider6) {
        return new SmartReplyStateInflaterImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static SmartReplyStateInflaterImpl newInstance(SmartReplyConstants smartReplyConstants, ActivityManagerWrapper activityManagerWrapper, PackageManagerWrapper packageManagerWrapper, DevicePolicyManagerWrapper devicePolicyManagerWrapper, SmartReplyInflater smartReplyInflater, SmartActionInflater smartActionInflater) {
        return new SmartReplyStateInflaterImpl(smartReplyConstants, activityManagerWrapper, packageManagerWrapper, devicePolicyManagerWrapper, smartReplyInflater, smartActionInflater);
    }
}
