package com.android.keyguard.clock;

import dagger.MembersInjector;
import java.util.List;
import javax.inject.Provider;

public final class ClockOptionsProvider_MembersInjector implements MembersInjector<ClockOptionsProvider> {
    private final Provider<List<ClockInfo>> mClockInfosProvider;

    public ClockOptionsProvider_MembersInjector(Provider<List<ClockInfo>> provider) {
        this.mClockInfosProvider = provider;
    }

    public static MembersInjector<ClockOptionsProvider> create(Provider<List<ClockInfo>> provider) {
        return new ClockOptionsProvider_MembersInjector(provider);
    }

    public void injectMembers(ClockOptionsProvider clockOptionsProvider) {
        injectMClockInfosProvider(clockOptionsProvider, this.mClockInfosProvider);
    }

    public static void injectMClockInfosProvider(ClockOptionsProvider clockOptionsProvider, Provider<List<ClockInfo>> provider) {
        clockOptionsProvider.mClockInfosProvider = provider;
    }
}
