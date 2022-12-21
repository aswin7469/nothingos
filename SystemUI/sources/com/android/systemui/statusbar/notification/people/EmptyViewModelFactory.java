package com.android.systemui.statusbar.notification.people;

import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/people/EmptyViewModelFactory;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactory;", "()V", "createWithAssociatedClickView", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModel;", "view", "Landroid/view/View;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubViewController.kt */
final class EmptyViewModelFactory implements PeopleHubViewModelFactory {
    public static final EmptyViewModelFactory INSTANCE = new EmptyViewModelFactory();

    private EmptyViewModelFactory() {
    }

    public PeopleHubViewModel createWithAssociatedClickView(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        return new PeopleHubViewModel(SequencesKt.emptySequence(), false);
    }
}
