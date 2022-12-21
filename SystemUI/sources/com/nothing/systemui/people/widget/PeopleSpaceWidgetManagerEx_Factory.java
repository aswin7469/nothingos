package com.nothing.systemui.people.widget;

import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PeopleSpaceWidgetManagerEx_Factory implements Factory<PeopleSpaceWidgetManagerEx> {
    private final Provider<PeopleSpaceWidgetManager> managerProvider;

    public PeopleSpaceWidgetManagerEx_Factory(Provider<PeopleSpaceWidgetManager> provider) {
        this.managerProvider = provider;
    }

    public PeopleSpaceWidgetManagerEx get() {
        return newInstance(this.managerProvider.get());
    }

    public static PeopleSpaceWidgetManagerEx_Factory create(Provider<PeopleSpaceWidgetManager> provider) {
        return new PeopleSpaceWidgetManagerEx_Factory(provider);
    }

    public static PeopleSpaceWidgetManagerEx newInstance(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        return new PeopleSpaceWidgetManagerEx(peopleSpaceWidgetManager);
    }
}
