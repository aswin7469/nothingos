package com.android.settings.notification;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.RestrictedRadioButton;
import com.android.settings.SettingsActivity;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.SetupRedactionInterstitial;
import com.android.settings.SetupWizardUtils;
import com.android.settings.Utils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;

public class RedactionInterstitial extends SettingsActivity {
    /* access modifiers changed from: protected */
    public boolean isToolbarEnabled() {
        return false;
    }

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", RedactionInterstitialFragment.class.getName());
        return intent;
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return RedactionInterstitialFragment.class.getName().equals(str);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        super.onCreate(bundle);
        findViewById(R$id.content_parent).setFitsSystemWindows(false);
    }

    public static Intent createStartIntent(Context context, int i) {
        int i2;
        Intent intent = new Intent(context, RedactionInterstitial.class);
        if (UserManager.get(context).isManagedProfile(i)) {
            i2 = R$string.lock_screen_notifications_interstitial_title_profile;
        } else {
            i2 = R$string.lock_screen_notifications_interstitial_title;
        }
        return intent.putExtra(":settings:show_fragment_title_resid", i2).putExtra("android.intent.extra.USER_ID", i);
    }

    public static class RedactionInterstitialFragment extends SettingsPreferenceFragment implements RadioGroup.OnCheckedChangeListener {
        private RadioGroup mRadioGroup;
        private RestrictedRadioButton mRedactSensitiveButton;
        private RestrictedRadioButton mShowAllButton;
        private int mUserId;

        public int getMetricsCategory() {
            return 74;
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R$layout.redaction_interstitial, viewGroup, false);
        }

        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DevicePolicyManager.class);
            this.mRadioGroup = (RadioGroup) view.findViewById(R$id.radio_group);
            this.mShowAllButton = (RestrictedRadioButton) view.findViewById(R$id.show_all);
            this.mRedactSensitiveButton = (RestrictedRadioButton) view.findViewById(R$id.redact_sensitive);
            this.mRadioGroup.setOnCheckedChangeListener(this);
            this.mUserId = Utils.getUserIdFromBundle(getContext(), getActivity().getIntent().getExtras());
            if (UserManager.get(getContext()).isManagedProfile(this.mUserId)) {
                ((TextView) view.findViewById(R$id.sud_layout_description)).setText(R$string.lock_screen_notifications_interstitial_message_profile);
                this.mShowAllButton.setText(devicePolicyManager.getResources().getString("Settings.LOCK_SCREEN_SHOW_WORK_NOTIFICATION_CONTENT", new C1140x56ed0641(this)));
                this.mRedactSensitiveButton.setText(devicePolicyManager.getResources().getString("Settings.LOCK_SCREEN_HIDE_WORK_NOTIFICATION_CONTENT", new C1141x56ed0642(this)));
                ((RadioButton) view.findViewById(R$id.hide_all)).setVisibility(8);
            }
            GlifLayout glifLayout = (GlifLayout) view.findViewById(R$id.setup_wizard_layout);
            glifLayout.findViewById(R$id.sud_layout_header).setVisibility(8);
            ((FooterBarMixin) glifLayout.getMixin(FooterBarMixin.class)).setPrimaryButton(new FooterButton.Builder(getContext()).setText(R$string.app_notifications_dialog_done).setListener(new C1142x56ed0643(this)).setButtonType(5).setTheme(R$style.SudGlifButton_Primary).build());
            ((TextView) view.findViewById(R$id.interstitial_title)).setTypeface(Typeface.create("NDot57", 0));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$onViewCreated$0() {
            return getString(R$string.lock_screen_notifications_summary_show_profile);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$onViewCreated$1() {
            return getString(R$string.lock_screen_notifications_summary_hide_profile);
        }

        /* access modifiers changed from: private */
        public void onDoneButtonClicked(View view) {
            if (!WizardManagerHelper.isAnySetupWizard(getIntent())) {
                SetupRedactionInterstitial.setEnabled(getContext(), false);
            }
            RedactionInterstitial redactionInterstitial = (RedactionInterstitial) getActivity();
            if (redactionInterstitial != null) {
                if (getIntent().getBooleanExtra("nothingStartNextSuwScreen", false)) {
                    startActivity(WizardManagerHelper.getNextIntent(getIntent(), -1));
                    redactionInterstitial.setResult(-1, (Intent) null);
                } else {
                    redactionInterstitial.setResult(0, (Intent) null);
                }
                finish();
            }
        }

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
            int i = R$id.hide_all;
            if (z2) {
                if (z && !this.mShowAllButton.isDisabledByAdmin()) {
                    i = R$id.show_all;
                } else if (!this.mRedactSensitiveButton.isDisabledByAdmin()) {
                    i = R$id.redact_sensitive;
                }
            }
            this.mRadioGroup.check(i);
        }

        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int i2 = 1;
            int i3 = i == R$id.show_all ? 1 : 0;
            if (i == R$id.hide_all) {
                i2 = 0;
            }
            Settings.Secure.putIntForUser(getContentResolver(), "lock_screen_allow_private_notifications", i3, this.mUserId);
            Settings.Secure.putIntForUser(getContentResolver(), "lock_screen_show_notifications", i2, this.mUserId);
        }
    }
}
