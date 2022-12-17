package com.android.settings.homepage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import androidx.window.embedding.SplitController;
import com.android.settings.R$bool;
import com.android.settings.R$dimen;
import com.android.settings.R$xml;
import com.android.settings.Utils;
import com.android.settings.activityembedding.ActivityEmbeddingRulesController;
import com.android.settings.activityembedding.ActivityEmbeddingUtils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.support.SupportPreferenceController;
import com.android.settings.widget.HomepagePreference;
import com.android.settings.widget.HomepagePreferenceLayoutHelper;
import com.android.settingslib.core.instrumentation.Instrumentable;
import com.android.settingslib.drawer.Tile;

public class TopLevelSettings extends DashboardFragment implements SplitLayoutListener, PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.top_level_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return false;
        }
    };
    private boolean mFirstStarted = true;
    private TopLevelHighlightMixin mHighlightMixin;
    private boolean mIsEmbeddingActivityEnabled;
    private int mPaddingHorizontal;
    private boolean mScrollNeeded = true;

    private interface PreferenceJob {
        void doForEach(Preference preference);

        void init() {
        }
    }

    public Fragment getCallbackFragment() {
        return this;
    }

    public int getHelpResource() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "TopLevelSettings";
    }

    public int getMetricsCategory() {
        return 35;
    }

    public TopLevelSettings() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("need_search_icon_in_action_bar", false);
        setArguments(bundle);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.top_level_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        HighlightableMenu.fromXml(context, getPreferenceScreenResId());
        ((SupportPreferenceController) use(SupportPreferenceController.class)).setActivity(getActivity());
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (isDuplicateClick(preference)) {
            return true;
        }
        ActivityEmbeddingRulesController.registerSubSettingsPairRule(getContext(), true);
        setHighlightPreferenceKey(preference.getKey());
        return super.onPreferenceTreeClick(preference);
    }

    public boolean onPreferenceStartFragment(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference) {
        new SubSettingLauncher(getActivity()).setDestination(preference.getFragment()).setArguments(preference.getExtras()).setSourceMetricsCategory(preferenceFragmentCompat instanceof Instrumentable ? ((Instrumentable) preferenceFragmentCompat).getMetricsCategory() : 0).setTitleRes(-1).setIsSecondLayerPage(true).launch();
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean isEmbeddingActivityEnabled = ActivityEmbeddingUtils.isEmbeddingActivityEnabled(getContext());
        this.mIsEmbeddingActivityEnabled = isEmbeddingActivityEnabled;
        if (isEmbeddingActivityEnabled) {
            boolean isActivityEmbedded = SplitController.getInstance().isActivityEmbedded(getActivity());
            if (bundle != null) {
                TopLevelHighlightMixin topLevelHighlightMixin = (TopLevelHighlightMixin) bundle.getParcelable("highlight_mixin");
                this.mHighlightMixin = topLevelHighlightMixin;
                this.mScrollNeeded = !topLevelHighlightMixin.isActivityEmbedded() && isActivityEmbedded;
                this.mHighlightMixin.setActivityEmbedded(isActivityEmbedded);
            }
            if (this.mHighlightMixin == null) {
                this.mHighlightMixin = new TopLevelHighlightMixin(isActivityEmbedded);
            }
        }
    }

    public void onStart() {
        if (this.mFirstStarted) {
            this.mFirstStarted = false;
        } else if (this.mIsEmbeddingActivityEnabled && isOnlyOneActivityInTask() && !SplitController.getInstance().isActivityEmbedded(getActivity())) {
            Log.i("TopLevelSettings", "Set default menu key");
            setHighlightMenuKey(getString(SettingsHomepageActivity.DEFAULT_HIGHLIGHT_MENU_KEY), false);
        }
        super.onStart();
    }

    private boolean isOnlyOneActivityInTask() {
        if (((ActivityManager) getSystemService(ActivityManager.class)).getRunningTasks(1).get(0).numActivities == 1) {
            return true;
        }
        return false;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            bundle.putParcelable("highlight_mixin", topLevelHighlightMixin);
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        iteratePreferences(new TopLevelSettings$$ExternalSyntheticLambda0(Utils.getHomepageIconColor(getContext())));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onCreatePreferences$0(int i, Preference preference) {
        Drawable icon = preference.getIcon();
        if (icon != null) {
            icon.setTint(i);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        highlightPreferenceIfNeeded();
    }

    public void onSplitLayoutChanged(boolean z) {
        iteratePreferences(new TopLevelSettings$$ExternalSyntheticLambda1(z));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onSplitLayoutChanged$1(boolean z, Preference preference) {
        if (preference instanceof HomepagePreferenceLayoutHelper.HomepagePreferenceLayout) {
            ((HomepagePreferenceLayoutHelper.HomepagePreferenceLayout) preference).getHelper().setIconVisible(z);
        }
    }

    public void highlightPreferenceIfNeeded() {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.highlightPreferenceIfNeeded();
        }
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RecyclerView onCreateRecyclerView = super.onCreateRecyclerView(layoutInflater, viewGroup, bundle);
        int i = this.mPaddingHorizontal;
        onCreateRecyclerView.setPadding(i, 0, i, 0);
        return onCreateRecyclerView;
    }

    public void setPaddingHorizontal(int i) {
        this.mPaddingHorizontal = i;
        RecyclerView listView = getListView();
        if (listView != null) {
            listView.setPadding(i, 0, i, 0);
        }
    }

    public void updatePreferencePadding(final boolean z) {
        iteratePreferences(new PreferenceJob() {
            private int mIconPaddingStart;
            private int mTextPaddingStart;

            public void init() {
                int i;
                int i2;
                Resources resources = TopLevelSettings.this.getResources();
                if (z) {
                    i = R$dimen.homepage_preference_icon_padding_start_two_pane;
                } else {
                    i = R$dimen.homepage_preference_icon_padding_start;
                }
                this.mIconPaddingStart = resources.getDimensionPixelSize(i);
                Resources resources2 = TopLevelSettings.this.getResources();
                if (z) {
                    i2 = R$dimen.homepage_preference_text_padding_start_two_pane;
                } else {
                    i2 = R$dimen.homepage_preference_text_padding_start;
                }
                this.mTextPaddingStart = resources2.getDimensionPixelSize(i2);
            }

            public void doForEach(Preference preference) {
                if (preference instanceof HomepagePreferenceLayoutHelper.HomepagePreferenceLayout) {
                    HomepagePreferenceLayoutHelper.HomepagePreferenceLayout homepagePreferenceLayout = (HomepagePreferenceLayoutHelper.HomepagePreferenceLayout) preference;
                    homepagePreferenceLayout.getHelper().setIconPaddingStart(this.mIconPaddingStart);
                    homepagePreferenceLayout.getHelper().setTextPaddingStart(this.mTextPaddingStart);
                }
            }
        });
    }

    public TopLevelHighlightMixin getHighlightMixin() {
        return this.mHighlightMixin;
    }

    public void setHighlightPreferenceKey(String str) {
        if (this.mHighlightMixin != null && !TextUtils.equals(str, "top_level_support")) {
            this.mHighlightMixin.setHighlightPreferenceKey(str);
        }
    }

    public boolean isDuplicateClick(Preference preference) {
        return this.mHighlightMixin != null && TextUtils.equals(preference.getKey(), this.mHighlightMixin.getHighlightPreferenceKey()) && SplitController.getInstance().isActivityEmbedded(getActivity());
    }

    public void setMenuHighlightShowed(boolean z) {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.setMenuHighlightShowed(z);
        }
    }

    public void setHighlightMenuKey(String str, boolean z) {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.setHighlightMenuKey(str, z);
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldForceRoundedIcon() {
        return getContext().getResources().getBoolean(R$bool.config_force_rounded_icon_TopLevelSettings);
    }

    /* access modifiers changed from: protected */
    public RecyclerView.Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        if (!this.mIsEmbeddingActivityEnabled || !(getActivity() instanceof SettingsHomepageActivity)) {
            return super.onCreateAdapter(preferenceScreen);
        }
        return this.mHighlightMixin.onCreateAdapter(this, preferenceScreen, this.mScrollNeeded);
    }

    /* access modifiers changed from: protected */
    public Preference createPreference(Tile tile) {
        return new HomepagePreference(getPrefContext());
    }

    /* access modifiers changed from: package-private */
    public void reloadHighlightMenuKey() {
        TopLevelHighlightMixin topLevelHighlightMixin = this.mHighlightMixin;
        if (topLevelHighlightMixin != null) {
            topLevelHighlightMixin.reloadHighlightMenuKey(getArguments());
        }
    }

    private void iteratePreferences(PreferenceJob preferenceJob) {
        PreferenceScreen preferenceScreen;
        if (preferenceJob != null && getPreferenceManager() != null && (preferenceScreen = getPreferenceScreen()) != null) {
            preferenceJob.init();
            int preferenceCount = preferenceScreen.getPreferenceCount();
            int i = 0;
            while (i < preferenceCount) {
                Preference preference = preferenceScreen.getPreference(i);
                if (preference != null) {
                    preferenceJob.doForEach(preference);
                    i++;
                } else {
                    return;
                }
            }
        }
    }
}
