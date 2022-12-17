package com.android.settings.dashboard.profileselector;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.DevicePolicyResourcesManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.UserIcons;
import com.android.internal.widget.RecyclerView;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final ArrayList<UserDetails> mUserDetails;

    public interface OnClickListener {
        void onClick(int i);
    }

    public static class UserDetails {
        /* access modifiers changed from: private */
        public final Drawable mIcon;
        /* access modifiers changed from: private */
        public final String mTitle;
        /* access modifiers changed from: private */
        public final UserHandle mUserHandle;

        public UserDetails(UserHandle userHandle, UserManager userManager, Context context) {
            this.mUserHandle = userHandle;
            UserInfo userInfo = userManager.getUserInfo(userHandle.getIdentifier());
            int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(context, 17956901);
            if (userInfo.isManagedProfile()) {
                Drawable userBadgeForDensityNoBackground = context.getPackageManager().getUserBadgeForDensityNoBackground(userHandle, 0);
                this.mIcon = userBadgeForDensityNoBackground;
                userBadgeForDensityNoBackground.setTint(colorAttrDefaultColor);
            } else {
                this.mIcon = UserIcons.getDefaultUserIconInColor(context.getResources(), colorAttrDefaultColor);
            }
            this.mTitle = getTitle(context);
        }

        private String getTitle(Context context) {
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
            Objects.requireNonNull(devicePolicyManager);
            DevicePolicyManager devicePolicyManager2 = devicePolicyManager;
            DevicePolicyResourcesManager resources = devicePolicyManager.getResources();
            int identifier = this.mUserHandle.getIdentifier();
            if (identifier == -2 || identifier == ActivityManager.getCurrentUser()) {
                return resources.getString("Settings.PERSONAL_CATEGORY_HEADER", new UserAdapter$UserDetails$$ExternalSyntheticLambda0(context));
            }
            return resources.getString("Settings.WORK_CATEGORY_HEADER", new UserAdapter$UserDetails$$ExternalSyntheticLambda1(context));
        }
    }

    public UserAdapter(Context context, ArrayList<UserDetails> arrayList) {
        if (arrayList != null) {
            this.mUserDetails = arrayList;
            this.mInflater = (LayoutInflater) context.getSystemService(LayoutInflater.class);
            return;
        }
        throw new IllegalArgumentException("A list of user details must be provided");
    }

    public UserHandle getUserHandle(int i) {
        if (i < 0 || i >= this.mUserDetails.size()) {
            return null;
        }
        return this.mUserDetails.get(i).mUserHandle;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = this.mInflater.inflate(R$layout.user_preference, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        bindViewHolder(viewHolder, i);
        return view;
    }

    /* access modifiers changed from: private */
    public void bindViewHolder(ViewHolder viewHolder, int i) {
        UserDetails item = getItem(i);
        viewHolder.getIconView().setImageDrawable(item.mIcon);
        viewHolder.setTitle(item.mTitle);
    }

    public int getCount() {
        return this.mUserDetails.size();
    }

    public UserDetails getItem(int i) {
        return this.mUserDetails.get(i);
    }

    public long getItemId(int i) {
        return (long) this.mUserDetails.get(i).mUserHandle.getIdentifier();
    }

    private RecyclerView.Adapter<ViewHolder> createRecyclerViewAdapter(final OnClickListener onClickListener) {
        return new RecyclerView.Adapter<ViewHolder>() {
            public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.user_select_item, viewGroup, false), onClickListener);
            }

            public void onBindViewHolder(ViewHolder viewHolder, int i) {
                UserAdapter.this.bindViewHolder(viewHolder, i);
            }

            public int getItemCount() {
                return UserAdapter.this.getCount();
            }
        };
    }

    public static UserAdapter createUserSpinnerAdapter(UserManager userManager, Context context) {
        List<UserHandle> userProfiles = userManager.getUserProfiles();
        if (userProfiles.size() < 2) {
            return null;
        }
        UserHandle userHandle = new UserHandle(UserHandle.myUserId());
        userProfiles.remove(userHandle);
        userProfiles.add(0, userHandle);
        return createUserAdapter(userManager, context, userProfiles);
    }

    public static RecyclerView.Adapter<ViewHolder> createUserRecycleViewAdapter(Context context, List<UserHandle> list, OnClickListener onClickListener) {
        return createUserAdapter((UserManager) context.getSystemService(UserManager.class), context, list).createRecyclerViewAdapter(onClickListener);
    }

    private static UserAdapter createUserAdapter(UserManager userManager, Context context, List<UserHandle> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (UserHandle userDetails : list) {
            arrayList.add(new UserDetails(userDetails, userManager, context));
        }
        return new UserAdapter(context, arrayList);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mButtonView;
        private final ImageView mIconView;
        private final TextView mTitleView;

        private ViewHolder(View view) {
            super(view);
            this.mIconView = (ImageView) view.findViewById(16908294);
            this.mTitleView = (TextView) view.findViewById(16908310);
            this.mButtonView = view.findViewById(R$id.button);
        }

        private ViewHolder(View view, OnClickListener onClickListener) {
            this(view);
            View view2 = this.mButtonView;
            if (view2 != null) {
                view2.setOnClickListener(new UserAdapter$ViewHolder$$ExternalSyntheticLambda0(this, onClickListener));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(OnClickListener onClickListener, View view) {
            onClickListener.onClick(getAdapterPosition());
        }

        /* access modifiers changed from: private */
        public ImageView getIconView() {
            return this.mIconView;
        }

        /* access modifiers changed from: private */
        public void setTitle(CharSequence charSequence) {
            this.mTitleView.setText(charSequence);
            View view = this.mButtonView;
            if (view != null) {
                view.setContentDescription(charSequence);
            }
        }
    }
}
