package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/notification/people/PersonViewModel;", "personModel", "Lcom/android/systemui/statusbar/notification/people/PersonModel;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.people.PeopleHubViewModelFactoryImpl$createWithAssociatedClickView$personViewModels$1 */
/* compiled from: PeopleHubViewController.kt */
final class C2739xa9b90728 extends Lambda implements Function1<PersonModel, PersonViewModel> {
    public static final C2739xa9b90728 INSTANCE = new C2739xa9b90728();

    C2739xa9b90728() {
        super(1);
    }

    public final PersonViewModel invoke(PersonModel personModel) {
        Intrinsics.checkNotNullParameter(personModel, "personModel");
        return new PersonViewModel(personModel.getName(), personModel.getAvatar(), new C2740xf85ca6ba(personModel));
    }
}
