package com.android.systemui.user;

import android.graphics.drawable.Drawable;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "Landroid/graphics/drawable/Drawable;", "item", "Lcom/android/systemui/statusbar/policy/UserSwitcherController$UserRecord;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UserSwitcherActivity.kt */
final class UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2 extends Lambda implements Function1<UserSwitcherController.UserRecord, Drawable> {
    final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2(UserSwitcherActivity userSwitcherActivity) {
        super(1);
        this.this$0 = userSwitcherActivity;
    }

    public final Drawable invoke(UserSwitcherController.UserRecord userRecord) {
        Intrinsics.checkNotNullParameter(userRecord, "item");
        Drawable mutate = this.this$0.adapter.findUserIcon(userRecord).mutate();
        UserSwitcherActivity userSwitcherActivity = this.this$0;
        mutate.setTint(userSwitcherActivity.getResources().getColor(C1893R.C1894color.user_switcher_fullscreen_popup_item_tint, userSwitcherActivity.getTheme()));
        Intrinsics.checkNotNullExpressionValue(mutate, "adapter.findUserIcon(ite…         ))\n            }");
        return mutate;
    }
}