package com.android.systemui.statusbar.notification.people;

import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactoryImpl;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactory;", "model", "Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "(Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;Lcom/android/systemui/plugins/ActivityStarter;)V", "createWithAssociatedClickView", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModel;", "view", "Landroid/view/View;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubViewController.kt */
final class PeopleHubViewModelFactoryImpl implements PeopleHubViewModelFactory {
    private final ActivityStarter activityStarter;
    private final PeopleHubModel model;

    public PeopleHubViewModelFactoryImpl(PeopleHubModel peopleHubModel, ActivityStarter activityStarter2) {
        Intrinsics.checkNotNullParameter(peopleHubModel, "model");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        this.model = peopleHubModel;
        this.activityStarter = activityStarter2;
    }

    public PeopleHubViewModel createWithAssociatedClickView(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        return new PeopleHubViewModel(SequencesKt.map(CollectionsKt.asSequence(this.model.getPeople()), C2733xa9b90728.INSTANCE), !this.model.getPeople().isEmpty());
    }
}
