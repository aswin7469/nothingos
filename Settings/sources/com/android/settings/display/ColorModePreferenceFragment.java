package com.android.settings.display;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.preference.PreferenceScreen;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.android.settings.R$array;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.RadioButtonPickerFragment;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.LayoutPreference;
import com.nothing.experience.AppTracking;
import com.nothing.settings.display.colors.ColorTemperaturePreference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ColorModePreferenceFragment extends RadioButtonPickerFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.color_mode_settings) {
        /* access modifiers changed from: protected */
        public boolean isPageSearchEnabled(Context context) {
            int[] intArray = context.getResources().getIntArray(17235996);
            return intArray != null && intArray.length > 0 && !ColorDisplayManager.areAccessibilityTransformsEnabled(context);
        }
    };
    private ColorDisplayManager mColorDisplayManager;
    private ContentObserver mContentObserver;
    private ImageView[] mDotIndicators;
    /* access modifiers changed from: private */
    public ArrayList<View> mPageList;
    private Resources mResources;
    private View mViewArrowNext;
    private View mViewArrowPrevious;
    private ViewPager mViewPager;
    /* access modifiers changed from: private */
    public View[] mViewPagerImages;

    private boolean isValidColorMode(int i) {
        if (i == 0 || i == 260 || i == 0 || i == 1 || i == 2 || i == 3) {
            return true;
        }
        return i >= 256 && i <= 511;
    }

    public int getMetricsCategory() {
        return 1143;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mColorDisplayManager = (ColorDisplayManager) context.getSystemService(ColorDisplayManager.class);
        this.mResources = context.getResources();
        ContentResolver contentResolver = context.getContentResolver();
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z, Uri uri) {
                super.onChange(z, uri);
                if (ColorDisplayManager.areAccessibilityTransformsEnabled(ColorModePreferenceFragment.this.getContext())) {
                    ColorModePreferenceFragment.this.getActivity().finish();
                }
            }
        };
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_inversion_enabled"), false, this.mContentObserver, this.mUserId);
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("accessibility_display_daltonizer_enabled"), false, this.mContentObserver, this.mUserId);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            int i = bundle.getInt("page_viewer_selection_index");
            this.mViewPager.setCurrentItem(i);
            updateIndicator(i);
        }
    }

    public void onDetach() {
        if (this.mContentObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
            this.mContentObserver = null;
        }
        super.onDetach();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("page_viewer_selection_index", this.mViewPager.getCurrentItem());
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.color_mode_settings;
    }

    /* access modifiers changed from: package-private */
    public void configureAndInstallPreview(LayoutPreference layoutPreference, PreferenceScreen preferenceScreen) {
        layoutPreference.setSelectable(false);
        preferenceScreen.addPreference(layoutPreference);
    }

    public ArrayList<Integer> getViewPagerResource() {
        return new ArrayList<>(Arrays.asList(new Integer[]{Integer.valueOf(R$layout.color_mode_view1), Integer.valueOf(R$layout.color_mode_view2), Integer.valueOf(R$layout.color_mode_view3)}));
    }

    /* access modifiers changed from: package-private */
    public void addViewPager(LayoutPreference layoutPreference) {
        ArrayList<Integer> viewPagerResource = getViewPagerResource();
        this.mViewPager = (ViewPager) layoutPreference.findViewById(R$id.viewpager);
        this.mViewPagerImages = new View[3];
        for (int i = 0; i < viewPagerResource.size(); i++) {
            this.mViewPagerImages[i] = getLayoutInflater().inflate(viewPagerResource.get(i).intValue(), (ViewGroup) null);
        }
        ArrayList<View> arrayList = new ArrayList<>();
        this.mPageList = arrayList;
        arrayList.add(this.mViewPagerImages[0]);
        this.mPageList.add(this.mViewPagerImages[1]);
        this.mPageList.add(this.mViewPagerImages[2]);
        this.mViewPager.setAdapter(new ColorPagerAdapter(this.mPageList));
        View findViewById = layoutPreference.findViewById(R$id.arrow_previous);
        this.mViewArrowPrevious = findViewById;
        findViewById.setOnClickListener(new ColorModePreferenceFragment$$ExternalSyntheticLambda0(this));
        View findViewById2 = layoutPreference.findViewById(R$id.arrow_next);
        this.mViewArrowNext = findViewById2;
        findViewById2.setOnClickListener(new ColorModePreferenceFragment$$ExternalSyntheticLambda1(this));
        this.mViewPager.addOnPageChangeListener(createPageListener());
        ViewGroup viewGroup = (ViewGroup) layoutPreference.findViewById(R$id.viewGroup);
        this.mDotIndicators = new ImageView[this.mPageList.size()];
        for (int i2 = 0; i2 < this.mPageList.size(); i2++) {
            ImageView imageView = new ImageView(getContext());
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(12, 12);
            marginLayoutParams.setMargins(6, 0, 6, 0);
            imageView.setLayoutParams(marginLayoutParams);
            this.mDotIndicators[i2] = imageView;
            viewGroup.addView(imageView);
        }
        updateIndicator(this.mViewPager.getCurrentItem());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addViewPager$0(View view) {
        this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() - 1, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addViewPager$1(View view) {
        this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1, true);
    }

    /* access modifiers changed from: protected */
    public void addStaticPreferences(PreferenceScreen preferenceScreen) {
        LayoutPreference layoutPreference = new LayoutPreference(preferenceScreen.getContext(), R$layout.color_mode_preview);
        configureAndInstallPreview(layoutPreference, preferenceScreen);
        addViewPager(layoutPreference);
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        Map<Integer, String> colorModeSummaryAndValueMapping = ColorModeUtils.getColorModeSummaryAndValueMapping(this.mResources);
        ArrayList arrayList = new ArrayList();
        for (Map.Entry next : colorModeSummaryAndValueMapping.entrySet()) {
            Log.d("ColorMode", "colorModesToSummaries key:" + next.getKey() + " colorModesToSummaries.value:" + ((String) next.getValue()));
        }
        for (int i : this.mResources.getIntArray(R$array.nt_config_color_mode_options_values)) {
            Log.d("ColorMode", "colorMode:" + i);
            ColorModeCandidateInfo colorModeCandidateInfo = new ColorModeCandidateInfo(colorModeSummaryAndValueMapping.get(Integer.valueOf(i)), getKeyForColorMode(i), true);
            Log.d("ColorMode", "prebuild color info:" + colorModeCandidateInfo.mLabel);
            arrayList.add(colorModeCandidateInfo);
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        int colorMode = getColorMode();
        if (isValidColorMode(colorMode)) {
            return getKeyForColorMode(colorMode);
        }
        return getKeyForColorMode(0);
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        int parseInt = Integer.parseInt(str.substring(str.lastIndexOf("_") + 1));
        if (isValidColorMode(parseInt)) {
            setColorMode(parseInt);
            logEvent(parseInt);
        }
        return true;
    }

    private void logEvent(int i) {
        boolean equals = ColorModeUtils.getColorModeSummaryAndValueMapping(this.mResources).get(Integer.valueOf(i)).equals(getString(R$string.color_mode_option_alive));
        Bundle bundle = new Bundle();
        bundle.putInt("color_mode", equals ^ true ? 1 : 0);
        Log.d("event", "log color mode" + (equals ^ true ? 1 : 0));
        AppTracking.getInstance(getActivity()).logProductEvent("Display_Event", bundle);
    }

    public int getColorMode() {
        return this.mColorDisplayManager.getColorMode();
    }

    public void setColorMode(int i) {
        this.mColorDisplayManager.setColorMode(i);
    }

    /* access modifiers changed from: package-private */
    public String getKeyForColorMode(int i) {
        return "color_mode_" + i;
    }

    static class ColorModeCandidateInfo extends CandidateInfo {
        private final String mKey;
        /* access modifiers changed from: private */
        public final CharSequence mLabel;

        public Drawable loadIcon() {
            return null;
        }

        ColorModeCandidateInfo(CharSequence charSequence, String str, boolean z) {
            super(z);
            this.mLabel = charSequence;
            this.mKey = str;
        }

        public CharSequence loadLabel() {
            return this.mLabel;
        }

        public String getKey() {
            return this.mKey;
        }
    }

    private ViewPager.OnPageChangeListener createPageListener() {
        return new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
                if (f != 0.0f) {
                    for (int i3 = 0; i3 < ColorModePreferenceFragment.this.mPageList.size(); i3++) {
                        ColorModePreferenceFragment.this.mViewPagerImages[i3].setVisibility(0);
                    }
                    return;
                }
                ColorModePreferenceFragment.this.mViewPagerImages[i].setContentDescription(ColorModePreferenceFragment.this.getContext().getString(R$string.colors_viewpager_content_description));
                ColorModePreferenceFragment.this.updateIndicator(i);
            }
        };
    }

    /* access modifiers changed from: private */
    public void updateIndicator(int i) {
        for (int i2 = 0; i2 < this.mPageList.size(); i2++) {
            if (i == i2) {
                this.mDotIndicators[i2].setBackgroundResource(R$drawable.ic_color_page_indicator_focused);
                this.mViewPagerImages[i2].setVisibility(0);
            } else {
                this.mDotIndicators[i2].setBackgroundResource(R$drawable.ic_color_page_indicator_unfocused);
                this.mViewPagerImages[i2].setVisibility(4);
            }
        }
        if (i == 0) {
            this.mViewArrowPrevious.setVisibility(4);
            this.mViewArrowNext.setVisibility(0);
        } else if (i == this.mPageList.size() - 1) {
            this.mViewArrowPrevious.setVisibility(0);
            this.mViewArrowNext.setVisibility(4);
        } else {
            this.mViewArrowPrevious.setVisibility(0);
            this.mViewArrowNext.setVisibility(0);
        }
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        super.onCreatePreferences(bundle, str);
        addColorTemperaturePreference();
    }

    private void addColorTemperaturePreference() {
        ColorTemperaturePreference colorTemperaturePreference = new ColorTemperaturePreference(getPrefContext());
        colorTemperaturePreference.setTitle(R$string.nt_color_temperature);
        getPreferenceScreen().addPreference(colorTemperaturePreference);
    }

    static class ColorPagerAdapter extends PagerAdapter {
        private final ArrayList<View> mPageViewList;

        public boolean isViewFromObject(View view, Object obj) {
            return obj == view;
        }

        ColorPagerAdapter(ArrayList<View> arrayList) {
            this.mPageViewList = arrayList;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            if (this.mPageViewList.get(i) != null) {
                viewGroup.removeView(this.mPageViewList.get(i));
            }
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            viewGroup.addView(this.mPageViewList.get(i));
            return this.mPageViewList.get(i);
        }

        public int getCount() {
            return this.mPageViewList.size();
        }
    }
}
