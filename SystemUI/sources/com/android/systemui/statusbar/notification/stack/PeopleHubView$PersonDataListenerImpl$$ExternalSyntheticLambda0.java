package com.android.systemui.statusbar.notification.stack;

import android.view.View;
import com.android.systemui.statusbar.notification.people.PersonViewModel;
import com.android.systemui.statusbar.notification.stack.PeopleHubView;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PeopleHubView$PersonDataListenerImpl$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ PersonViewModel f$0;

    public /* synthetic */ PeopleHubView$PersonDataListenerImpl$$ExternalSyntheticLambda0(PersonViewModel personViewModel) {
        this.f$0 = personViewModel;
    }

    public final void onClick(View view) {
        PeopleHubView.PersonDataListenerImpl.m3155onDataChanged$lambda1(this.f$0, view);
    }
}
