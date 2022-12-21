package com.android.systemui.statusbar.notification.people;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleHubDataSourceImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ PeopleHubDataSourceImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ PersonModel f$2;

    public /* synthetic */ PeopleHubDataSourceImpl$$ExternalSyntheticLambda3(PeopleHubDataSourceImpl peopleHubDataSourceImpl, int i, PersonModel personModel) {
        this.f$0 = peopleHubDataSourceImpl;
        this.f$1 = i;
        this.f$2 = personModel;
    }

    public final void run() {
        PeopleHubDataSourceImpl.m3125addVisibleEntry$lambda7$lambda6(this.f$0, this.f$1, this.f$2);
    }
}
