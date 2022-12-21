package com.android.systemui;

import com.android.systemui.dagger.ContextComponentHelper;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class SystemUIAppComponentFactory_MembersInjector implements MembersInjector<SystemUIAppComponentFactory> {
    private final Provider<ContextComponentHelper> mComponentHelperProvider;

    public SystemUIAppComponentFactory_MembersInjector(Provider<ContextComponentHelper> provider) {
        this.mComponentHelperProvider = provider;
    }

    public static MembersInjector<SystemUIAppComponentFactory> create(Provider<ContextComponentHelper> provider) {
        return new SystemUIAppComponentFactory_MembersInjector(provider);
    }

    public void injectMembers(SystemUIAppComponentFactory systemUIAppComponentFactory) {
        injectMComponentHelper(systemUIAppComponentFactory, this.mComponentHelperProvider.get());
    }

    public static void injectMComponentHelper(SystemUIAppComponentFactory systemUIAppComponentFactory, ContextComponentHelper contextComponentHelper) {
        systemUIAppComponentFactory.mComponentHelper = contextComponentHelper;
    }
}
