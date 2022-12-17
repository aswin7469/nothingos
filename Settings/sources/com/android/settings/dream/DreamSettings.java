package com.android.settings.dream;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.dream.DreamBackend;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.util.ArrayList;
import java.util.List;

public class DreamSettings extends DashboardFragment implements OnMainSwitchChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.dream_fragment_overview);
    private MainSwitchPreference mMainSwitchPreference;
    private Button mPreviewButton;
    private RecyclerView mRecyclerView;

    static String getKeyFromSetting(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "never" : "either_charging_or_docked" : "while_docked_only" : "while_charging_only";
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DreamSettings";
    }

    public int getMetricsCategory() {
        return 47;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int getSettingFromPrefKey(java.lang.String r5) {
        /*
            int r0 = r5.hashCode()
            r1 = 3
            r2 = 0
            r3 = 2
            r4 = 1
            switch(r0) {
                case -1592701525: goto L_0x002b;
                case -294641318: goto L_0x0021;
                case 104712844: goto L_0x0017;
                case 1019349036: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0036
        L_0x000c:
            java.lang.String r0 = "while_charging_only"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0036
            r5 = r2
            goto L_0x0037
        L_0x0017:
            java.lang.String r0 = "never"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0036
            r5 = r1
            goto L_0x0037
        L_0x0021:
            java.lang.String r0 = "either_charging_or_docked"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0036
            r5 = r3
            goto L_0x0037
        L_0x002b:
            java.lang.String r0 = "while_docked_only"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0036
            r5 = r4
            goto L_0x0037
        L_0x0036:
            r5 = -1
        L_0x0037:
            if (r5 == 0) goto L_0x0040
            if (r5 == r4) goto L_0x003f
            if (r5 == r3) goto L_0x003e
            return r1
        L_0x003e:
            return r3
        L_0x003f:
            return r4
        L_0x0040:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.dream.DreamSettings.getSettingFromPrefKey(java.lang.String):int");
    }

    static int getDreamSettingDescriptionResId(int i) {
        if (i == 0) {
            return R$string.screensaver_settings_summary_sleep;
        }
        if (i == 1) {
            return R$string.screensaver_settings_summary_dock;
        }
        if (i != 2) {
            return R$string.screensaver_settings_summary_never;
        }
        return R$string.screensaver_settings_summary_either_long;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.dream_fragment_overview;
    }

    public int getHelpResource() {
        return R$string.help_url_screen_saver;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context);
    }

    public static CharSequence getSummaryTextWithDreamName(Context context) {
        return getSummaryTextFromBackend(DreamBackend.getInstance(context), context);
    }

    static CharSequence getSummaryTextFromBackend(DreamBackend dreamBackend, Context context) {
        if (!dreamBackend.isEnabled()) {
            return context.getString(R$string.screensaver_settings_summary_off);
        }
        return dreamBackend.getActiveDreamName();
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new WhenToDreamPreferenceController(context));
        return arrayList;
    }

    private void setAllPreferencesEnabled(boolean z) {
        getPreferenceControllers().forEach(new DreamSettings$$ExternalSyntheticLambda2(this, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAllPreferencesEnabled$1(boolean z, List list) {
        list.forEach(new DreamSettings$$ExternalSyntheticLambda3(this, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setAllPreferencesEnabled$0(boolean z, AbstractPreferenceController abstractPreferenceController) {
        Preference findPreference;
        String preferenceKey = abstractPreferenceController.getPreferenceKey();
        if (!preferenceKey.equals("dream_main_settings_switch") && (findPreference = findPreference(preferenceKey)) != null) {
            findPreference.setEnabled(z);
            abstractPreferenceController.updateState(findPreference);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        DreamBackend instance = DreamBackend.getInstance(getContext());
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) findPreference("dream_main_settings_switch");
        this.mMainSwitchPreference = mainSwitchPreference;
        if (mainSwitchPreference != null) {
            mainSwitchPreference.addOnSwitchChangeListener(this);
        }
        setAllPreferencesEnabled(instance.isEnabled());
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        DreamBackend instance = DreamBackend.getInstance(getContext());
        ViewGroup viewGroup2 = (ViewGroup) getActivity().findViewById(16908290);
        int i = 0;
        Button button = (Button) getActivity().getLayoutInflater().inflate(R$layout.dream_preview_button, viewGroup2, false);
        this.mPreviewButton = button;
        if (!instance.isEnabled()) {
            i = 8;
        }
        button.setVisibility(i);
        viewGroup2.addView(this.mPreviewButton);
        this.mPreviewButton.setOnClickListener(new DreamSettings$$ExternalSyntheticLambda0(instance));
        this.mRecyclerView = super.onCreateRecyclerView(layoutInflater, viewGroup, bundle);
        updatePaddingForPreviewButton();
        return this.mRecyclerView;
    }

    private void updatePaddingForPreviewButton() {
        this.mPreviewButton.post(new DreamSettings$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePaddingForPreviewButton$3() {
        this.mRecyclerView.setPadding(0, 0, 0, this.mPreviewButton.getMeasuredHeight());
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        setAllPreferencesEnabled(z);
        this.mPreviewButton.setVisibility(z ? 0 : 8);
        updatePaddingForPreviewButton();
    }
}
