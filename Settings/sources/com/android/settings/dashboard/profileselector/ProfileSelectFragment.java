package com.android.settings.dashboard.profileselector;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.google.android.material.tabs.TabLayout;
import java.util.Locale;
/* loaded from: classes.dex */
public abstract class ProfileSelectFragment extends DashboardFragment {
    private static final int[] LABEL = {R.string.category_personal, R.string.category_work};
    private ViewGroup mContentView;

    public abstract Fragment[] getFragments();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "ProfileSelectFragment";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = (ViewGroup) super.onCreateView(layoutInflater, viewGroup, bundle);
        FragmentActivity activity = getActivity();
        int convertPosition = convertPosition(getTabId(activity, getArguments()));
        View findViewById = this.mContentView.findViewById(R.id.tab_container);
        ViewPager viewPager = (ViewPager) findViewById.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout tabLayout = (TabLayout) findViewById.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabTextColor(tabLayout);
        findViewById.setVisibility(0);
        tabLayout.getTabAt(convertPosition).select();
        ((FrameLayout) this.mContentView.findViewById(16908351)).setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        RecyclerView listView = getListView();
        listView.setOverScrollMode(2);
        Utils.setActionBarShadowAnimation(activity, getSettingsLifecycle(), listView);
        return this.mContentView;
    }

    private void setupTabTextColor(TabLayout tabLayout) {
        tabLayout.setTabTextColors(new ColorStateList(new int[][]{new int[]{16842913}, new int[0]}, new int[]{tabLayout.getTabTextColors().getColorForState(new int[]{16842913}, com.android.settingslib.Utils.getColorAttrDefaultColor(getContext(), 17956901)), com.android.settingslib.Utils.getColorAttrDefaultColor(getContext(), 16842808)}));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.placeholder_preference_screen;
    }

    int getTabId(Activity activity, Bundle bundle) {
        if (bundle != null) {
            int i = bundle.getInt(":settings:show_fragment_tab", -1);
            if (i != -1) {
                return i;
            }
            if (UserManager.get(activity).isManagedProfile(bundle.getInt("android.intent.extra.USER_ID", UserHandle.SYSTEM.getIdentifier()))) {
                return 1;
            }
        }
        return UserManager.get(activity).isManagedProfile(activity.getIntent().getContentUserHint()) ? 1 : 0;
    }

    /* loaded from: classes.dex */
    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final Fragment[] mChildFragments;
        private final Context mContext;

        ViewPagerAdapter(ProfileSelectFragment profileSelectFragment) {
            super(profileSelectFragment.getChildFragmentManager());
            this.mContext = profileSelectFragment.getContext();
            this.mChildFragments = profileSelectFragment.getFragments();
        }

        @Override // androidx.fragment.app.FragmentStatePagerAdapter
        public Fragment getItem(int i) {
            return this.mChildFragments[ProfileSelectFragment.convertPosition(i)];
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.mChildFragments.length;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public CharSequence getPageTitle(int i) {
            return this.mContext.getString(ProfileSelectFragment.LABEL[ProfileSelectFragment.convertPosition(i)]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int convertPosition(int i) {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1 ? (LABEL.length - 1) - i : i;
    }
}
