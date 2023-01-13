package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0002H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubDataListenerImpl;", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactory;", "viewBoundary", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewBoundary;", "(Lcom/android/systemui/statusbar/notification/people/PeopleHubViewBoundary;)V", "onDataChanged", "", "data", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
final class PeopleHubDataListenerImpl implements DataListener<PeopleHubViewModelFactory> {
    private final PeopleHubViewBoundary viewBoundary;

    public PeopleHubDataListenerImpl(PeopleHubViewBoundary peopleHubViewBoundary) {
        Intrinsics.checkNotNullParameter(peopleHubViewBoundary, "viewBoundary");
        this.viewBoundary = peopleHubViewBoundary;
    }

    public void onDataChanged(PeopleHubViewModelFactory peopleHubViewModelFactory) {
        Intrinsics.checkNotNullParameter(peopleHubViewModelFactory, "data");
        PeopleHubViewModel createWithAssociatedClickView = peopleHubViewModelFactory.createWithAssociatedClickView(this.viewBoundary.getAssociatedViewForClickAnimation());
        this.viewBoundary.setVisible(createWithAssociatedClickView.isVisible());
        for (Pair next : SequencesKt.zip(this.viewBoundary.getPersonViewAdapters(), SequencesKt.plus(createWithAssociatedClickView.getPeople(), (Sequence<PersonViewModel>) PeopleHubViewControllerKt.repeated(null)))) {
            ((DataListener) next.component1()).onDataChanged((PersonViewModel) next.component2());
        }
    }
}
