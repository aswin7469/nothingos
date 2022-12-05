package com.android.settings.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settings.RestrictedRadioButton;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.SetupRedactionInterstitial;
import com.android.settings.SetupWizardUtils;
import com.android.settings.Utils;
import com.android.settings.notification.RedactionInterstitial;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;
/* loaded from: classes.dex */
public class RedactionInterstitial extends SettingsActivity {
    @Override // com.android.settings.core.SettingsBaseActivity
    protected boolean isToolbarEnabled() {
        return false;
    }

    @Override // com.android.settings.SettingsActivity, android.app.Activity
    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", RedactionInterstitialFragment.class.getName());
        return intent;
    }

    @Override // com.android.settings.SettingsActivity
    protected boolean isValidFragment(String str) {
        return RedactionInterstitialFragment.class.getName().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.SettingsActivity, com.android.settings.core.SettingsBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        super.onCreate(bundle);
        findViewById(R.id.content_parent).setFitsSystemWindows(false);
    }

    public static Intent createStartIntent(Context context, int i) {
        int i2;
        Intent intent = new Intent(context, RedactionInterstitial.class);
        if (UserManager.get(context).isManagedProfile(i)) {
            i2 = R.string.lock_screen_notifications_interstitial_title_profile;
        } else {
            i2 = R.string.lock_screen_notifications_interstitial_title;
        }
        return intent.putExtra(":settings:show_fragment_title_resid", i2).putExtra("android.intent.extra.USER_ID", i);
    }

    /* loaded from: classes.dex */
    public static class RedactionInterstitialFragment extends SettingsPreferenceFragment implements RadioGroup.OnCheckedChangeListener {
        private RadioGroup mRadioGroup;
        private RestrictedRadioButton mRedactSensitiveButton;
        private RestrictedRadioButton mShowAllButton;
        private int mUserId;

        @Override // com.android.settingslib.core.instrumentation.Instrumentable
        public int getMetricsCategory() {
            return 74;
        }

        @Override // com.android.settings.SettingsPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.redaction_interstitial, viewGroup, false);
        }

        @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            this.mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
            this.mShowAllButton = (RestrictedRadioButton) view.findViewById(R.id.show_all);
            this.mRedactSensitiveButton = (RestrictedRadioButton) view.findViewById(R.id.redact_sensitive);
            this.mRadioGroup.setOnCheckedChangeListener(this);
            this.mUserId = Utils.getUserIdFromBundle(getContext(), getActivity().getIntent().getExtras());
            if (UserManager.get(getContext()).isManagedProfile(this.mUserId)) {
                ((TextView) view.findViewById(R.id.sud_layout_description)).setText(R.string.lock_screen_notifications_interstitial_message_profile);
                this.mShowAllButton.setText(R.string.lock_screen_notifications_summary_show_profile);
                this.mRedactSensitiveButton.setText(R.string.lock_screen_notifications_summary_hide_profile);
                ((RadioButton) view.findViewById(R.id.hide_all)).setVisibility(8);
            }
            GlifLayout glifLayout = (GlifLayout) view.findViewById(R.id.setup_wizard_layout);
            glifLayout.findViewById(R.id.sud_layout_header).setVisibility(8);
            ((FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class)).setPrimaryButton(new FooterButton.Builder(getContext()).setText(R.string.app_notifications_dialog_done).setListener(new View.OnClickListener() { // from class: com.android.settings.notification.RedactionInterstitial$RedactionInterstitialFragment$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    RedactionInterstitial.RedactionInterstitialFragment.this.onDoneButtonClickedNt(view2);
                }
            }).setButtonType(5).setTheme(R.style.SudGlifButton_Primary).build());
        }

        @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            checkNotificationFeaturesAndSetDisabled(this.mShowAllButton, 12);
            checkNotificationFeaturesAndSetDisabled(this.mRedactSensitiveButton, 4);
            loadFromSettings();
        }

        private void checkNotificationFeaturesAndSetDisabled(RestrictedRadioButton restrictedRadioButton, int i) {
            restrictedRadioButton.setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(getActivity(), i, this.mUserId));
        }

        private void loadFromSettings() {
            boolean z = false;
            boolean z2 = UserManager.get(getContext()).isManagedProfile(this.mUserId) || Settings.Secure.getIntForUser(getContentResolver(), "lock_screen_show_notifications", 0, this.mUserId) != 0;
            if (Settings.Secure.getIntForUser(getContentResolver(), "lock_screen_allow_private_notifications", 1, this.mUserId) != 0) {
                z = true;
            }
            int i = R.id.hide_all;
            if (z2) {
                if (z && !this.mShowAllButton.isDisabledByAdmin()) {
                    i = R.id.show_all;
                } else if (!this.mRedactSensitiveButton.isDisabledByAdmin()) {
                    i = R.id.redact_sensitive;
                }
            }
            this.mRadioGroup.check(i);
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int i2 = 1;
            int i3 = i == R.id.show_all ? 1 : 0;
            if (i == R.id.hide_all) {
                i2 = 0;
            }
            Settings.Secure.putIntForUser(getContentResolver(), "lock_screen_allow_private_notifications", i3, this.mUserId);
            Settings.Secure.putIntForUser(getContentResolver(), "lock_screen_show_notifications", i2, this.mUserId);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onDoneButtonClickedNt(View view) {
            if (!WizardManagerHelper.isAnySetupWizard(getIntent())) {
                SetupRedactionInterstitial.setEnabled(getContext(), false);
            }
            RedactionInterstitial redactionInterstitial = (RedactionInterstitial) getActivity();
            if (redactionInterstitial == null) {
                return;
            }
            if (getIntent().getBooleanExtra("nothingStartNextSuwScreen", false)) {
                startActivity(WizardManagerHelper.getNextIntent(getIntent(), -1));
                redactionInterstitial.setResult(-1, null);
            } else {
                redactionInterstitial.setResult(0, null);
            }
            finish();
        }
    }
}
