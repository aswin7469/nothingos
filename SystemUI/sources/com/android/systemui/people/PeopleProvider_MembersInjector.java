package com.android.systemui.people;

import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PeopleProvider_MembersInjector implements MembersInjector<PeopleProvider> {
    private final Provider<PeopleSpaceWidgetManager> mPeopleSpaceWidgetManagerProvider;

    public PeopleProvider_MembersInjector(Provider<PeopleSpaceWidgetManager> provider) {
        this.mPeopleSpaceWidgetManagerProvider = provider;
    }

    public static MembersInjector<PeopleProvider> create(Provider<PeopleSpaceWidgetManager> provider) {
        return new PeopleProvider_MembersInjector(provider);
    }

    public void injectMembers(PeopleProvider peopleProvider) {
        injectMPeopleSpaceWidgetManager(peopleProvider, this.mPeopleSpaceWidgetManagerProvider.get());
    }

    public static void injectMPeopleSpaceWidgetManager(PeopleProvider peopleProvider, PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        peopleProvider.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }
}
