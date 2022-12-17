package com.android.settings.display;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.text.TextUtils;
import android.view.Display;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$array;
import com.android.settings.R$drawable;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.FooterPreference;
import com.android.settingslib.widget.IllustrationPreference;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScreenResolutionFragment extends RadioButtonPickerFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.screen_resolution_settings) {
        boolean mIsFHDSupport = false;
        boolean mIsQHDSupport = false;

        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            return new ScreenResolutionController(context, "fragment").checkSupportedResolutions();
        }
    };
    private Display mDefaultDisplay;
    private IllustrationPreference mImagePreference;
    private Set<Point> mResolutions;
    private Resources mResources;
    private String[] mScreenResolutionOptions;
    private String[] mScreenResolutionSummaries;

    public int getMetricsCategory() {
        return 1920;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mDefaultDisplay = ((DisplayManager) context.getSystemService(DisplayManager.class)).getDisplay(0);
        Resources resources = context.getResources();
        this.mResources = resources;
        this.mScreenResolutionOptions = resources.getStringArray(R$array.config_screen_resolution_options_strings);
        this.mScreenResolutionSummaries = this.mResources.getStringArray(R$array.config_screen_resolution_summaries_strings);
        this.mResolutions = getAllSupportedResolution();
        this.mImagePreference = new IllustrationPreference(context);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.screen_resolution_settings;
    }

    /* access modifiers changed from: protected */
    public void addStaticPreferences(PreferenceScreen preferenceScreen) {
        updateIllustrationImage(this.mImagePreference);
        preferenceScreen.addPreference(this.mImagePreference);
        FooterPreference footerPreference = new FooterPreference(preferenceScreen.getContext());
        footerPreference.setTitle(R$string.screen_resolution_footer);
        footerPreference.setSelectable(false);
        footerPreference.setLayoutResource(R$layout.preference_footer);
        preferenceScreen.addPreference(footerPreference);
    }

    public void bindPreferenceExtra(SelectorWithWidgetPreference selectorWithWidgetPreference, String str, CandidateInfo candidateInfo, String str2, String str3) {
        CharSequence loadSummary = ((ScreenResolutionCandidateInfo) candidateInfo).loadSummary();
        if (loadSummary != null) {
            selectorWithWidgetPreference.setSummary(loadSummary);
        }
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            String[] strArr = this.mScreenResolutionOptions;
            if (i >= strArr.length) {
                return arrayList;
            }
            String str = strArr[i];
            arrayList.add(new ScreenResolutionCandidateInfo(str, this.mScreenResolutionSummaries[i], str, true));
            i++;
        }
    }

    private Set<Point> getAllSupportedResolution() {
        HashSet hashSet = new HashSet();
        for (Display.Mode mode : this.mDefaultDisplay.getSupportedModes()) {
            hashSet.add(new Point(mode.getPhysicalWidth(), mode.getPhysicalHeight()));
        }
        return hashSet;
    }

    private Display.Mode getPreferMode(int i) {
        for (Point next : this.mResolutions) {
            if (next.x == i) {
                return new Display.Mode(next.x, next.y, getDisplayMode().getRefreshRate());
            }
        }
        return getDisplayMode();
    }

    public Display.Mode getDisplayMode() {
        return this.mDefaultDisplay.getMode();
    }

    public void setDisplayMode(int i) {
        this.mDefaultDisplay.setUserPreferredDisplayMode(getPreferMode(i));
    }

    /* access modifiers changed from: package-private */
    public String getKeyForResolution(int i) {
        if (i == 1080) {
            return this.mScreenResolutionOptions[0];
        }
        if (i == 1440) {
            return this.mScreenResolutionOptions[1];
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        return getKeyForResolution(getDisplayMode().getPhysicalWidth());
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        if (this.mScreenResolutionOptions[0].equals(str)) {
            setDisplayMode(1080);
        } else if (this.mScreenResolutionOptions[1].equals(str)) {
            setDisplayMode(1440);
        }
        updateIllustrationImage(this.mImagePreference);
        return true;
    }

    private void updateIllustrationImage(IllustrationPreference illustrationPreference) {
        String defaultKey = getDefaultKey();
        if (TextUtils.equals(this.mScreenResolutionOptions[0], defaultKey)) {
            illustrationPreference.setLottieAnimationResId(R$drawable.screen_resolution_1080p);
        } else if (TextUtils.equals(this.mScreenResolutionOptions[1], defaultKey)) {
            illustrationPreference.setLottieAnimationResId(R$drawable.screen_resolution_1440p);
        }
    }

    public static class ScreenResolutionCandidateInfo extends CandidateInfo {
        private final String mKey;
        private final CharSequence mLabel;
        private final CharSequence mSummary;

        public Drawable loadIcon() {
            return null;
        }

        ScreenResolutionCandidateInfo(CharSequence charSequence, CharSequence charSequence2, String str, boolean z) {
            super(z);
            this.mLabel = charSequence;
            this.mSummary = charSequence2;
            this.mKey = str;
        }

        public CharSequence loadLabel() {
            return this.mLabel;
        }

        public CharSequence loadSummary() {
            return this.mSummary;
        }

        public String getKey() {
            return this.mKey;
        }
    }
}
