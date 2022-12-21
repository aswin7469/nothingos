package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/notification/people/PeopleHubViewModelFactoryDataSourceImpl$registerListener$dataSub$1", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;", "onDataChanged", "", "data", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.people.PeopleHubViewModelFactoryDataSourceImpl$registerListener$dataSub$1 */
/* compiled from: PeopleHubViewController.kt */
public final class C2732x8c4359c9 implements DataListener<PeopleHubModel> {
    final /* synthetic */ DataListener<PeopleHubViewModelFactory> $listener;
    final /* synthetic */ Ref.ObjectRef<PeopleHubModel> $model;
    final /* synthetic */ PeopleHubViewModelFactoryDataSourceImpl this$0;

    C2732x8c4359c9(Ref.ObjectRef<PeopleHubModel> objectRef, PeopleHubViewModelFactoryDataSourceImpl peopleHubViewModelFactoryDataSourceImpl, DataListener<? super PeopleHubViewModelFactory> dataListener) {
        this.$model = objectRef;
        this.this$0 = peopleHubViewModelFactoryDataSourceImpl;
        this.$listener = dataListener;
    }

    public void onDataChanged(PeopleHubModel peopleHubModel) {
        Intrinsics.checkNotNullParameter(peopleHubModel, "data");
        this.$model.element = peopleHubModel;
        PeopleHubViewModelFactoryDataSourceImpl.registerListener$updateListener(this.$model, this.this$0, this.$listener);
    }
}
