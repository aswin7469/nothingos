package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.people.PeopleHubViewModelFactoryImpl$createWithAssociatedClickView$personViewModels$1$onClick$1 */
/* compiled from: PeopleHubViewController.kt */
final class C2734xf85ca6ba extends Lambda implements Function0<Unit> {
    final /* synthetic */ PersonModel $personModel;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C2734xf85ca6ba(PersonModel personModel) {
        super(0);
        this.$personModel = personModel;
    }

    public final void invoke() {
        this.$personModel.getClickRunnable().run();
    }
}
