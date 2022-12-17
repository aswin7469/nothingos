package com.android.settings.accessibility;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settingslib.widget.TopIntroPreference;
import com.google.android.setupdesign.GlifPreferenceLayout;

public class ToggleScreenReaderPreferenceFragmentForSetupWizard extends ToggleAccessibilityServicePreferenceFragment {
    private boolean mToggleSwitchWasInitiallyChecked;

    public int getMetricsCategory() {
        return 371;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        String string = getArguments().getString("title");
        String string2 = getContext().getString(R$string.talkback_summary);
        Drawable drawable = getContext().getDrawable(R$drawable.ic_accessibility_visibility);
        AccessibilitySetupWizardUtils.updateGlifPreferenceLayout(getContext(), (GlifPreferenceLayout) view, string, string2, drawable);
        this.mToggleSwitchWasInitiallyChecked = this.mToggleServiceSwitchPreference.isChecked();
        TopIntroPreference topIntroPreference = this.mTopIntroPreference;
        if (topIntroPreference != null) {
            topIntroPreference.setVisible(false);
        }
        this.mToggleServiceSwitchPreference.applyPartnerCustomizationPaddingStyle();
    }

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return ((GlifPreferenceLayout) viewGroup).onCreateRecyclerView(layoutInflater, viewGroup, bundle);
    }

    public void onStop() {
        if (this.mToggleServiceSwitchPreference.isChecked() != this.mToggleSwitchWasInitiallyChecked) {
            this.mMetricsFeatureProvider.action(getContext(), 371, this.mToggleServiceSwitchPreference.isChecked());
        }
        super.onStop();
    }
}
