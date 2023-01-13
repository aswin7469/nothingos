package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/statusbar/notification/people/PeopleHubDataSourceImpl$registerListener$3", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "unsubscribe", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
public final class PeopleHubDataSourceImpl$registerListener$3 implements Subscription {
    final /* synthetic */ DataListener<PeopleHubModel> $listener;
    final /* synthetic */ PeopleHubDataSourceImpl this$0;

    PeopleHubDataSourceImpl$registerListener$3(PeopleHubDataSourceImpl peopleHubDataSourceImpl, DataListener<? super PeopleHubModel> dataListener) {
        this.this$0 = peopleHubDataSourceImpl;
        this.$listener = dataListener;
    }

    public void unsubscribe() {
        this.this$0.dataListeners.remove((Object) this.$listener);
        if (this.this$0.dataListeners.isEmpty()) {
            Subscription access$getUserChangeSubscription$p = this.this$0.userChangeSubscription;
            if (access$getUserChangeSubscription$p != null) {
                access$getUserChangeSubscription$p.unsubscribe();
            }
            this.this$0.userChangeSubscription = null;
            this.this$0.notifCollection.removeCollectionListener(this.this$0.notifCollectionListener);
        }
    }
}
