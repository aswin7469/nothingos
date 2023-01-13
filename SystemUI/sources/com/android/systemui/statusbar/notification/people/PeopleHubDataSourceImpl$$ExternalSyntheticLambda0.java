package com.android.systemui.statusbar.notification.people;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleHubDataSourceImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ PeopleHubDataSourceImpl f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ PeopleHubDataSourceImpl$$ExternalSyntheticLambda0(int i, PeopleHubDataSourceImpl peopleHubDataSourceImpl, int i2, String str) {
        this.f$0 = i;
        this.f$1 = peopleHubDataSourceImpl;
        this.f$2 = i2;
        this.f$3 = str;
    }

    public final void run() {
        PeopleHubDataSourceImpl.m3133removeVisibleEntry$lambda3$lambda2$lambda1(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
