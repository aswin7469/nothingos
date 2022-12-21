package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/statusbar/notification/people/PeopleHubViewModelFactoryDataSourceImpl$registerListener$1", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "unsubscribe", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubViewController.kt */
public final class PeopleHubViewModelFactoryDataSourceImpl$registerListener$1 implements Subscription {
    final /* synthetic */ Subscription $dataSub;

    PeopleHubViewModelFactoryDataSourceImpl$registerListener$1(Subscription subscription) {
        this.$dataSub = subscription;
    }

    public void unsubscribe() {
        this.$dataSub.unsubscribe();
    }
}
