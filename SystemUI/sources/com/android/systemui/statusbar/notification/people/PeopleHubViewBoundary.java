package com.android.systemui.statusbar.notification.people;

import android.view.View;
import kotlin.Metadata;
import kotlin.sequences.Sequence;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R \u0010\u0006\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b0\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0010À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubViewBoundary;", "", "associatedViewForClickAnimation", "Landroid/view/View;", "getAssociatedViewForClickAnimation", "()Landroid/view/View;", "personViewAdapters", "Lkotlin/sequences/Sequence;", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "Lcom/android/systemui/statusbar/notification/people/PersonViewModel;", "getPersonViewAdapters", "()Lkotlin/sequences/Sequence;", "setVisible", "", "isVisible", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
public interface PeopleHubViewBoundary {
    View getAssociatedViewForClickAnimation();

    Sequence<DataListener<PersonViewModel>> getPersonViewAdapters();

    void setVisible(boolean z);
}
