package com.android.systemui.p012qs.tiles;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.PseudoGridView;
import com.android.systemui.p012qs.QSUserSwitcherEvent;
import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.UserDetailView */
public class UserDetailView extends PseudoGridView {
    protected Adapter mAdapter;

    public UserDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public static UserDetailView inflate(Context context, ViewGroup viewGroup, boolean z) {
        return (UserDetailView) LayoutInflater.from(context).inflate(C1894R.layout.qs_user_detail, viewGroup, z);
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        PseudoGridView.ViewGroupAdapterBridge.link(this, adapter);
    }

    public void refreshAdapter() {
        this.mAdapter.refresh();
    }

    /* renamed from: com.android.systemui.qs.tiles.UserDetailView$Adapter */
    public static class Adapter extends UserSwitcherController.BaseUserAdapter implements View.OnClickListener {
        private final Context mContext;
        protected UserSwitcherController mController;
        private View mCurrentUserView;
        private UserSwitchDialogController.DialogShower mDialogShower;
        private final FalsingManager mFalsingManager;
        private final UiEventLogger mUiEventLogger;

        @Inject
        public Adapter(Context context, UserSwitcherController userSwitcherController, UiEventLogger uiEventLogger, FalsingManager falsingManager) {
            super(userSwitcherController);
            this.mContext = context;
            this.mController = userSwitcherController;
            this.mUiEventLogger = uiEventLogger;
            this.mFalsingManager = falsingManager;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            return createUserDetailItemView(view, viewGroup, getItem(i));
        }

        public void injectDialogShower(UserSwitchDialogController.DialogShower dialogShower) {
            this.mDialogShower = dialogShower;
        }

        public UserDetailItemView createUserDetailItemView(View view, ViewGroup viewGroup, UserSwitcherController.UserRecord userRecord) {
            UserDetailItemView convertOrInflate = UserDetailItemView.convertOrInflate(viewGroup.getContext(), view, viewGroup);
            ColorFilter colorFilter = null;
            if (!userRecord.isCurrent || userRecord.isGuest) {
                convertOrInflate.setOnClickListener(this);
            } else {
                convertOrInflate.setOnClickListener((View.OnClickListener) null);
                convertOrInflate.setClickable(false);
            }
            String name = getName(this.mContext, userRecord);
            if (userRecord.picture == null) {
                convertOrInflate.bind(name, getDrawable(this.mContext, userRecord).mutate(), userRecord.resolveId());
            } else {
                CircleFramedDrawable circleFramedDrawable = new CircleFramedDrawable(userRecord.picture, (int) this.mContext.getResources().getDimension(C1894R.dimen.qs_framed_avatar_size));
                if (!userRecord.isSwitchToEnabled) {
                    colorFilter = getDisabledUserAvatarColorFilter();
                }
                circleFramedDrawable.setColorFilter(colorFilter);
                convertOrInflate.bind(name, (Drawable) circleFramedDrawable, userRecord.info.id);
            }
            convertOrInflate.setActivated(userRecord.isCurrent);
            convertOrInflate.setDisabledByAdmin(userRecord.isDisabledByAdmin);
            convertOrInflate.setEnabled(userRecord.isSwitchToEnabled);
            convertOrInflate.setAlpha(convertOrInflate.isEnabled() ? 1.0f : 0.38f);
            if (userRecord.isCurrent) {
                this.mCurrentUserView = convertOrInflate;
            }
            convertOrInflate.setTag(userRecord);
            return convertOrInflate;
        }

        private static Drawable getDrawable(Context context, UserSwitcherController.UserRecord userRecord) {
            int i;
            Drawable iconDrawable = getIconDrawable(context, userRecord);
            if (userRecord.isCurrent) {
                i = C1894R.C1895color.qs_user_switcher_selected_avatar_icon_color;
            } else {
                i = !userRecord.isSwitchToEnabled ? C1894R.C1895color.GM2_grey_600 : C1894R.C1895color.qs_user_switcher_avatar_icon_color;
            }
            iconDrawable.setTint(context.getResources().getColor(i, context.getTheme()));
            return new LayerDrawable(new Drawable[]{context.getDrawable(userRecord.isCurrent ? C1894R.C1896drawable.bg_avatar_selected : C1894R.C1896drawable.qs_bg_avatar), iconDrawable});
        }

        public void onClick(View view) {
            if (!this.mFalsingManager.isFalseTap(1)) {
                Trace.beginSection("UserDetailView.Adapter#onClick");
                UserSwitcherController.UserRecord userRecord = (UserSwitcherController.UserRecord) view.getTag();
                if (userRecord.isDisabledByAdmin) {
                    this.mController.startActivity(RestrictedLockUtils.getShowAdminSupportDetailsIntent(this.mContext, userRecord.enforcedAdmin));
                } else if (userRecord.isSwitchToEnabled) {
                    MetricsLogger.action(this.mContext, 156);
                    this.mUiEventLogger.log(QSUserSwitcherEvent.QS_USER_SWITCH);
                    if (!userRecord.isAddUser && !userRecord.isRestricted && !userRecord.isDisabledByAdmin) {
                        View view2 = this.mCurrentUserView;
                        if (view2 != null) {
                            view2.setActivated(false);
                        }
                        view.setActivated(true);
                    }
                    onUserListItemClicked(userRecord, this.mDialogShower);
                }
                Trace.endSection();
            }
        }

        public void linkToViewGroup(ViewGroup viewGroup) {
            PseudoGridView.ViewGroupAdapterBridge.link(viewGroup, this);
        }
    }
}
