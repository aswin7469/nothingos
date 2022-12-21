package com.android.systemui.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000E\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0005J\u0010\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0005H\u0002J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0006\u0010\r\u001a\u00020\u000eJ\"\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u000e2\b\u0010\u0012\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016¨\u0006\u0017"}, mo64987d2 = {"com/android/systemui/user/UserSwitcherActivity$adapter$1", "Lcom/android/systemui/statusbar/policy/UserSwitcherController$BaseUserAdapter;", "doNotRenderUserView", "", "item", "Lcom/android/systemui/statusbar/policy/UserSwitcherController$UserRecord;", "findUserIcon", "Landroid/graphics/drawable/Drawable;", "getDrawable", "getName", "", "context", "Landroid/content/Context;", "getTotalUserViews", "", "getView", "Landroid/view/View;", "position", "convertView", "parent", "Landroid/view/ViewGroup;", "notifyDataSetChanged", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$adapter$1 extends UserSwitcherController.BaseUserAdapter {
    final /* synthetic */ UserSwitcherActivity this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UserSwitcherActivity$adapter$1(UserSwitcherActivity userSwitcherActivity, UserSwitcherController userSwitcherController) {
        super(userSwitcherController);
        this.this$0 = userSwitcherActivity;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        UserSwitcherController.UserRecord item = getItem(i);
        ViewGroup viewGroup2 = (ViewGroup) view;
        if (viewGroup2 == null) {
            View inflate = this.this$0.layoutInflater.inflate(C1893R.layout.user_switcher_fullscreen_item, viewGroup, false);
            if (inflate != null) {
                viewGroup2 = (ViewGroup) inflate;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
            }
        }
        View childAt = viewGroup2.getChildAt(0);
        if (childAt != null) {
            Intrinsics.checkNotNullExpressionValue(item, "item");
            ((ImageView) childAt).setImageDrawable(getDrawable(item));
            View childAt2 = viewGroup2.getChildAt(1);
            if (childAt2 != null) {
                TextView textView = (TextView) childAt2;
                Context context = textView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "getContext()");
                textView.setText(getName(context, item));
                viewGroup2.setEnabled(item.isSwitchToEnabled);
                viewGroup2.setAlpha(viewGroup2.isEnabled() ? 1.0f : 0.38f);
                viewGroup2.setTag("user_view");
                return viewGroup2;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.ImageView");
    }

    public String getName(Context context, UserSwitcherController.UserRecord userRecord) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(userRecord, "item");
        if (Intrinsics.areEqual((Object) userRecord, (Object) this.this$0.manageUserRecord)) {
            String string = this.this$0.getString(C1893R.string.manage_users);
            Intrinsics.checkNotNullExpressionValue(string, "{\n                getStr…nage_users)\n            }");
            return string;
        }
        String name = super.getName(context, userRecord);
        Intrinsics.checkNotNullExpressionValue(name, "{\n                super.…text, item)\n            }");
        return name;
    }

    public final Drawable findUserIcon(UserSwitcherController.UserRecord userRecord) {
        Intrinsics.checkNotNullParameter(userRecord, "item");
        if (Intrinsics.areEqual((Object) userRecord, (Object) this.this$0.manageUserRecord)) {
            Drawable drawable = this.this$0.getDrawable(C1893R.C1895drawable.ic_manage_users);
            Intrinsics.checkNotNullExpressionValue(drawable, "getDrawable(R.drawable.ic_manage_users)");
            return drawable;
        } else if (userRecord.info == null) {
            Drawable iconDrawable = UserSwitcherController.BaseUserAdapter.getIconDrawable(this.this$0, userRecord);
            Intrinsics.checkNotNullExpressionValue(iconDrawable, "getIconDrawable(this@UserSwitcherActivity, item)");
            return iconDrawable;
        } else {
            Bitmap userIcon = this.this$0.userManager.getUserIcon(userRecord.info.id);
            if (userIcon != null) {
                return new BitmapDrawable(userIcon);
            }
            Drawable defaultUserIcon = UserIcons.getDefaultUserIcon(this.this$0.getResources(), userRecord.info.id, false);
            Intrinsics.checkNotNullExpressionValue(defaultUserIcon, "getDefaultUserIcon(resources, item.info.id, false)");
            return defaultUserIcon;
        }
    }

    public final int getTotalUserViews() {
        ArrayList<UserSwitcherController.UserRecord> users = getUsers();
        Intrinsics.checkNotNullExpressionValue(users, "users");
        Iterable<UserSwitcherController.UserRecord> iterable = users;
        int i = 0;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            for (UserSwitcherController.UserRecord userRecord : iterable) {
                Intrinsics.checkNotNullExpressionValue(userRecord, "item");
                if ((!doNotRenderUserView(userRecord)) && (i = i + 1) < 0) {
                    CollectionsKt.throwCountOverflow();
                }
            }
        }
        return i;
    }

    public final boolean doNotRenderUserView(UserSwitcherController.UserRecord userRecord) {
        Intrinsics.checkNotNullParameter(userRecord, "item");
        return userRecord.isAddUser || userRecord.isAddSupervisedUser || (userRecord.isGuest && userRecord.info == null);
    }

    private final Drawable getDrawable(UserSwitcherController.UserRecord userRecord) {
        Drawable drawable;
        if (userRecord.isGuest) {
            drawable = this.this$0.getDrawable(C1893R.C1895drawable.ic_account_circle);
        } else {
            drawable = findUserIcon(userRecord);
        }
        drawable.mutate();
        if (!userRecord.isCurrent && !userRecord.isSwitchToEnabled) {
            drawable.setTint(this.this$0.getResources().getColor(C1893R.C1894color.kg_user_switcher_restricted_avatar_icon_color, this.this$0.getTheme()));
        }
        Drawable mutate = this.this$0.getDrawable(C1893R.C1895drawable.user_switcher_icon_large).mutate();
        if (mutate != null) {
            LayerDrawable layerDrawable = (LayerDrawable) mutate;
            if (Intrinsics.areEqual((Object) userRecord, (Object) this.this$0.userSwitcherController.getCurrentUserRecord())) {
                Drawable findDrawableByLayerId = layerDrawable.findDrawableByLayerId(C1893R.C1897id.ring);
                if (findDrawableByLayerId != null) {
                    UserSwitcherActivity userSwitcherActivity = this.this$0;
                    ((GradientDrawable) findDrawableByLayerId).setStroke(userSwitcherActivity.getResources().getDimensionPixelSize(C1893R.dimen.user_switcher_icon_selected_width), Utils.getColorAttrDefaultColor(userSwitcherActivity, 17956900));
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.GradientDrawable");
                }
            }
            layerDrawable.setDrawableByLayerId(C1893R.C1897id.user_avatar, drawable);
            return layerDrawable;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.this$0.buildUserViews();
    }
}
