package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/statusbar/notification/people/PeopleHubSettingChangeDataSourceImpl$registerListener$1", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "unsubscribe", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
public final class PeopleHubSettingChangeDataSourceImpl$registerListener$1 implements Subscription {
    final /* synthetic */ PeopleHubSettingChangeDataSourceImpl$registerListener$observer$1 $observer;
    final /* synthetic */ PeopleHubSettingChangeDataSourceImpl this$0;

    PeopleHubSettingChangeDataSourceImpl$registerListener$1(PeopleHubSettingChangeDataSourceImpl peopleHubSettingChangeDataSourceImpl, PeopleHubSettingChangeDataSourceImpl$registerListener$observer$1 peopleHubSettingChangeDataSourceImpl$registerListener$observer$1) {
        this.this$0 = peopleHubSettingChangeDataSourceImpl;
        this.$observer = peopleHubSettingChangeDataSourceImpl$registerListener$observer$1;
    }

    public void unsubscribe() {
        this.this$0.contentResolver.unregisterContentObserver(this.$observer);
    }
}
