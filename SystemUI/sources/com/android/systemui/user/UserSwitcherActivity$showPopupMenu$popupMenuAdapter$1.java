package com.android.systemui.user;

import com.android.systemui.statusbar.policy.UserSwitcherController;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "item", "Lcom/android/systemui/statusbar/policy/UserSwitcherController$UserRecord;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UserSwitcherActivity.kt */
final class UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1 extends Lambda implements Function1<UserSwitcherController.UserRecord, String> {
    final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1(UserSwitcherActivity userSwitcherActivity) {
        super(1);
        this.this$0 = userSwitcherActivity;
    }

    public final String invoke(UserSwitcherController.UserRecord userRecord) {
        Intrinsics.checkNotNullParameter(userRecord, "item");
        return this.this$0.adapter.getName(this.this$0, userRecord);
    }
}
