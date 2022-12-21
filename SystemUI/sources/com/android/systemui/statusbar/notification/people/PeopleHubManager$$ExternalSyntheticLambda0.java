package com.android.systemui.statusbar.notification.people;

import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleHubManager$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ PersonModel f$0;

    public /* synthetic */ PeopleHubManager$$ExternalSyntheticLambda0(PersonModel personModel) {
        this.f$0 = personModel;
    }

    public final boolean test(Object obj) {
        return PeopleHubManager.m3130addActivePerson$lambda1(this.f$0, (PersonModel) obj);
    }
}
