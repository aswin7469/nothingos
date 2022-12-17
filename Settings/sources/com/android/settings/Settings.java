package com.android.settings;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.FeatureFlagUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.enterprise.EnterprisePrivacySettings;
import com.android.settings.network.MobileNetworkIntentConverter;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.safetycenter.SafetyCenterManagerWrapper;
import com.android.settings.security.SecuritySettingsFeatureProvider;
import com.google.android.setupdesign.util.ThemeHelper;

public class Settings extends SettingsActivity {

    public static class AboutDeviceRegulatoryActivity extends SettingsActivity {
    }

    public static class AccessibilityDaltonizerSettingsActivity extends SettingsActivity {
    }

    public static class AccessibilityDetailsSettingsActivity extends SettingsActivity {
    }

    public static class AccessibilityInversionSettingsActivity extends SettingsActivity {
    }

    public static class AccessibilitySettingsActivity extends SettingsActivity {
    }

    public static class AccountDashboardActivity extends SettingsActivity {
    }

    public static class AccountSyncSettingsActivity extends SettingsActivity {
    }

    public static class AdaptiveBrightnessActivity extends SettingsActivity {
    }

    public static class AdvancedConnectedDeviceActivity extends SettingsActivity {
    }

    public static class AlarmsAndRemindersActivity extends SettingsActivity {
    }

    public static class AlarmsAndRemindersAppActivity extends SettingsActivity {
    }

    public static class AndroidBeamSettingsActivity extends SettingsActivity {
    }

    public static class AodDisplayModeSettingsActivity extends SettingsActivity {
    }

    public static class AodSettingsActivity extends SettingsActivity {
    }

    public static class ApnEditorActivity extends SettingsActivity {
    }

    public static class ApnSettingsActivity extends SettingsActivity {
    }

    public static class AppBubbleNotificationSettingsActivity extends SettingsActivity {
    }

    public static class AppDashboardActivity extends SettingsActivity {
    }

    public static class AppDrawOverlaySettingsActivity extends SettingsActivity {
    }

    public static class AppInteractAcrossProfilesSettingsActivity extends SettingsActivity {
    }

    public static class AppManageExternalStorageActivity extends SettingsActivity {
    }

    public static class AppMediaManagementAppsActivity extends SettingsActivity {
    }

    public static class AppMemoryUsageActivity extends SettingsActivity {
    }

    public static class AppNotificationSettingsActivity extends SettingsActivity {
    }

    public static class AppPictureInPictureSettingsActivity extends SettingsActivity {
    }

    public static class AppUsageAccessSettingsActivity extends SettingsActivity {
    }

    public static class AppWriteSettingsActivity extends SettingsActivity {
    }

    public static class AssistGestureSettingsActivity extends SettingsActivity {
    }

    public static class AutomaticStorageManagerSettingsActivity extends SettingsActivity {
    }

    public static class AvailableVirtualKeyboardActivity extends SettingsActivity {
    }

    public static class BatterySaverScheduleSettingsActivity extends SettingsActivity {
    }

    public static class BatterySaverSettingsActivity extends SettingsActivity {
    }

    public static class BatteryShareActivity extends SettingsActivity {
    }

    public static class BlueToothPairingActivity extends SettingsActivity {
    }

    public static class BluetoothBroadcastActivity extends SettingsActivity {
    }

    public static class BluetoothDeviceDetailActivity extends SettingsActivity {
    }

    public static class BluetoothFindBroadcastsActivity extends SettingsActivity {
    }

    public static class BluetoothSettingsActivity extends SettingsActivity {
    }

    public static class BugReportHandlerPickerActivity extends SettingsActivity {
    }

    public static class ButtonNavigationSettingsActivity extends SettingsActivity {
    }

    public static class CaptioningSettingsActivity extends SettingsActivity {
    }

    public static class ChangeWifiStateActivity extends SettingsActivity {
    }

    public static class ChooseAccountActivity extends SettingsActivity {
    }

    public static class CombinedBiometricProfileSettingsActivity extends SettingsActivity {
    }

    public static class CombinedBiometricSettingsActivity extends SettingsActivity {
    }

    public static class ConfigureNotificationSettingsActivity extends SettingsActivity {
    }

    public static class ConfigureWifiSettingsActivity extends SettingsActivity {
    }

    public static class ConnectedDeviceDashboardActivity extends SettingsActivity {
    }

    public static class ConversationListSettingsActivity extends SettingsActivity {
    }

    public static class CreateShortcutActivity extends SettingsActivity {
    }

    public static class DarkThemeSettingsActivity extends SettingsActivity {
    }

    public static class DataSaverSummaryActivity extends SettingsActivity {
    }

    public static class DataUsageSummaryActivity extends SettingsActivity {
    }

    public static class DateTimeSettingsActivity extends SettingsActivity {
    }

    public static class DevelopmentSettingsDashboardActivity extends SettingsActivity {
    }

    public static class DeviceAdminSettingsActivity extends SettingsActivity {
    }

    public static class DeviceSoftwareInfoActivity extends SettingsActivity {
    }

    public static class DisplaySettingsActivity extends SettingsActivity {
    }

    public static class DreamSettingsActivity extends SettingsActivity {
    }

    public static class ExperienceImprovementProgramsActivity extends SettingsActivity {
    }

    public static class FaceSettingsActivity extends SettingsActivity {
    }

    public static class FaceSettingsInternalActivity extends SettingsActivity {
    }

    public static class FingerprintSettingsActivity extends SettingsActivity {
    }

    public static class FlipToGlyphActivity extends SettingsActivity {
    }

    public static class GameModeActivity extends SettingsActivity {
    }

    public static class GamesStorageActivity extends SettingsActivity {
    }

    public static class GestureNavigationSettingsActivity extends SettingsActivity {
    }

    public static class GlyphsContactRingtoneSettingsActivity extends SettingsActivity {
    }

    public static class GlyphsContactRingtonesSettingsActivity extends SettingsActivity {
    }

    public static class GlyphsMySoundActivity extends SettingsActivity {
    }

    public static class GlyphsNotificationAppListActivity extends SettingsActivity {
    }

    public static class GlyphsNotificationConversationListActivity extends SettingsActivity {
    }

    public static class GlyphsNotificationRingtonesSettingsActivity extends SettingsActivity {
    }

    public static class GlyphsRingtoneSelectorActivity extends SettingsActivity {
    }

    public static class GlyphsSettingsActivity extends SettingsActivity {
    }

    public static class HighPowerApplicationsActivity extends SettingsActivity {
    }

    public static class IccLockSettingsActivity extends SettingsActivity {
    }

    public static class InteractAcrossProfilesSettingsActivity extends SettingsActivity {
    }

    public static class KeyboardLayoutPickerActivity extends SettingsActivity {
    }

    public static class LanguageAndInputSettingsActivity extends SettingsActivity {
    }

    public static class LocalePickerActivity extends SettingsActivity {
    }

    public static class LocationSettingsActivity extends SettingsActivity {
    }

    public static class LockScreenSettingsActivity extends SettingsActivity {
    }

    public static class ManageAppExternalSourcesActivity extends SettingsActivity {
    }

    public static class ManageApplicationsActivity extends SettingsActivity {
    }

    public static class ManageAssistActivity extends SettingsActivity {
    }

    public static class ManageDomainUrlsActivity extends SettingsActivity {
    }

    public static class ManageExternalSourcesActivity extends SettingsActivity {
    }

    public static class ManageExternalStorageActivity extends SettingsActivity {
    }

    public static class ManagedProfileSettingsActivity extends SettingsActivity {
    }

    public static class MediaControlsSettingsActivity extends SettingsActivity {
    }

    public static class MediaManagementAppsActivity extends SettingsActivity {
    }

    public static class MobileDataUsageListActivity extends SettingsActivity {
    }

    public static class MobileNetworkListActivity extends SettingsActivity {
    }

    public static class ModuleLicensesActivity extends SettingsActivity {
    }

    public static class MyDeviceInfoActivity extends SettingsActivity {
    }

    public static class NetworkDashboardActivity extends SettingsActivity {
    }

    public static class NetworkProviderSettingsActivity extends SettingsActivity {
    }

    public static class NetworkSelectActivity extends SettingsActivity {
    }

    public static class NightDisplaySettingsActivity extends SettingsActivity {
    }

    public static class NightDisplaySuggestionActivity extends NightDisplaySettingsActivity {
    }

    public static class NotificationAccessDetailsActivity extends SettingsActivity {
    }

    public static class NotificationAccessSettingsActivity extends SettingsActivity {
    }

    public static class NotificationAppListActivity extends SettingsActivity {
    }

    public static class NotificationAssistantSettingsActivity extends SettingsActivity {
    }

    public static class NotificationReviewPermissionsActivity extends SettingsActivity {
    }

    public static class NotificationStationActivity extends SettingsActivity {
    }

    public static class OneHandedSettingsActivity extends SettingsActivity {
    }

    public static class OverlaySettingsActivity extends SettingsActivity {
    }

    public static class PaymentSettingsActivity extends SettingsActivity {
    }

    public static class PhysicalKeyboardActivity extends SettingsActivity {
    }

    public static class PictureInPictureSettingsActivity extends SettingsActivity {
    }

    public static class PowerMenuSettingsActivity extends SettingsActivity {
    }

    public static class PowerUsageSummaryActivity extends SettingsActivity {
    }

    public static class PremiumSmsAccessActivity extends SettingsActivity {
    }

    public static class PrintJobSettingsActivity extends SettingsActivity {
    }

    public static class PrintSettingsActivity extends SettingsActivity {
    }

    public static class PrivacyControlsActivity extends SettingsActivity {
    }

    public static class PrivacySettingsActivity extends SettingsActivity {
    }

    public static class PrivateVolumeForgetActivity extends SettingsActivity {
    }

    public static class PublicVolumeSettingsActivity extends SettingsActivity {
    }

    public static class ReduceBrightColorsSettingsActivity extends SettingsActivity {
    }

    public static class RunningServicesActivity extends SettingsActivity {
    }

    public static class SMQQtiFeedbackActivity extends SettingsActivity {
    }

    public static class SavedAccessPointsSettingsActivity extends SettingsActivity {
    }

    public static class ScanningSettingsActivity extends SettingsActivity {
    }

    public static class SecurityAdvancedSettings extends SettingsActivity {
    }

    public static class SmartAutoRotateSettingsActivity extends SettingsActivity {
    }

    public static class SoundSettingsActivity extends SettingsActivity {
    }

    public static class SpellCheckersSettingsActivity extends SettingsActivity {
    }

    public static class StorageDashboardActivity extends SettingsActivity {
    }

    public static class StorageUseActivity extends SettingsActivity {
    }

    public static class SystemDashboardActivity extends SettingsActivity {
    }

    public static class TestingSettingsActivity extends SettingsActivity {
    }

    public static class TextToSpeechSettingsActivity extends SettingsActivity {
    }

    public static class TrustedCredentialsSettingsActivity extends SettingsActivity {
    }

    public static class TurnScreenOnSettingsActivity extends SettingsActivity {
    }

    public static class UsageAccessSettingsActivity extends SettingsActivity {
    }

    public static class UsbDetailsActivity extends SettingsActivity {
    }

    public static class UserDictionarySettingsActivity extends SettingsActivity {
    }

    public static class UserSettingsActivity extends SettingsActivity {
    }

    public static class VpnSettingsActivity extends SettingsActivity {
    }

    public static class VrListenersSettingsActivity extends SettingsActivity {
    }

    public static class WallpaperSettingsActivity extends SettingsActivity {
    }

    public static class WebViewAppPickerActivity extends SettingsActivity {
    }

    public static class WifiAPITestActivity extends SettingsActivity {
    }

    public static class WifiBtSarActivity extends SettingsActivity {
    }

    public static class WifiCallingDisclaimerActivity extends SettingsActivity {
    }

    public static class WifiCallingSettingsActivity extends SettingsActivity {
    }

    public static class WifiDetailsSettingsActivity extends SettingsActivity {
    }

    public static class WifiDisplaySettingsActivity extends SettingsActivity {
    }

    public static class WifiInfoActivity extends SettingsActivity {
    }

    public static class WifiP2pSettingsActivity extends SettingsActivity {
    }

    public static class WifiScanningSettingsActivity extends SettingsActivity {
    }

    public static class WifiSettingsActivity extends SettingsActivity {
    }

    public static class WriteSettingsActivity extends SettingsActivity {
    }

    public static class ZenAccessDetailSettingsActivity extends SettingsActivity {
    }

    public static class ZenAccessSettingsActivity extends SettingsActivity {
    }

    public static class ZenModeAutomationSettingsActivity extends SettingsActivity {
    }

    public static class ZenModeEventRuleSettingsActivity extends SettingsActivity {
    }

    public static class ZenModeScheduleRuleSettingsActivity extends SettingsActivity {
    }

    public static class ZenModeSettingsActivity extends SettingsActivity {
    }

    public static class TetherSettingsActivity extends SettingsActivity {
        public Intent getIntent() {
            return Settings.wrapIntentWithAllInOneTetherSettingsIfNeeded(getApplicationContext(), super.getIntent());
        }
    }

    public static class WifiTetherSettingsActivity extends SettingsActivity {
        public Intent getIntent() {
            return Settings.wrapIntentWithAllInOneTetherSettingsIfNeeded(getApplicationContext(), super.getIntent());
        }
    }

    /* access modifiers changed from: private */
    public static Intent wrapIntentWithAllInOneTetherSettingsIfNeeded(Context context, Intent intent) {
        Bundle bundle;
        if (!FeatureFlagUtils.isEnabled(context, "settings_tether_all_in_one")) {
            return intent;
        }
        Intent intent2 = new Intent(intent);
        intent2.putExtra(":settings:show_fragment", AllInOneTetherSettings.class.getCanonicalName());
        Bundle bundleExtra = intent.getBundleExtra(":settings:show_fragment_args");
        if (bundleExtra != null) {
            bundle = new Bundle(bundleExtra);
        } else {
            bundle = new Bundle();
        }
        bundle.putParcelable("intent", intent);
        intent2.putExtra(":settings:show_fragment_args", bundle);
        return intent2;
    }

    public static class SecurityDashboardActivity extends SettingsActivity {
        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            handleSafetyCenterRedirection();
        }

        @VisibleForTesting
        public void handleSafetyCenterRedirection() {
            if (SafetyCenterManagerWrapper.get().isEnabled(this)) {
                try {
                    startActivity(new Intent("android.intent.action.SAFETY_CENTER"));
                    finish();
                } catch (ActivityNotFoundException e) {
                    Log.e("SecurityDashboardActivity", "Unable to open safety center", e);
                }
            }
        }

        @VisibleForTesting
        public boolean isValidFragment(String str) {
            return super.isValidFragment(str) || (str != null && TextUtils.equals(str, getAlternativeFragmentName()));
        }

        public String getInitialFragmentName(Intent intent) {
            String alternativeFragmentName = getAlternativeFragmentName();
            if (alternativeFragmentName != null) {
                return alternativeFragmentName;
            }
            return super.getInitialFragmentName(intent);
        }

        private String getAlternativeFragmentName() {
            SecuritySettingsFeatureProvider securitySettingsFeatureProvider = FeatureFactory.getFactory(this).getSecuritySettingsFeatureProvider();
            if (securitySettingsFeatureProvider.hasAlternativeSecuritySettingsFragment()) {
                return securitySettingsFeatureProvider.getAlternativeSecuritySettingsFragmentClassname();
            }
            return null;
        }
    }

    public static class PrivacyDashboardActivity extends SettingsActivity {
        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            handleSafetyCenterRedirection();
        }

        @VisibleForTesting
        public void handleSafetyCenterRedirection() {
            if ("android.settings.PRIVACY_SETTINGS".equals(getIntent().getAction()) && SafetyCenterManagerWrapper.get().isEnabled(this)) {
                try {
                    startActivity(new Intent("android.intent.action.SAFETY_CENTER"));
                    finish();
                } catch (ActivityNotFoundException e) {
                    Log.e("PrivacyDashboardActivity", "Unable to open safety center", e);
                }
            }
        }
    }

    public static class FactoryResetActivity extends SettingsActivity {
        /* access modifiers changed from: protected */
        public boolean isToolbarEnabled() {
            return false;
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            setTheme(SetupWizardUtils.getTheme(this, getIntent()));
            ThemeHelper.trySetDynamicColor(this);
            super.onCreate(bundle);
        }
    }

    public static class FactoryResetConfirmActivity extends SettingsActivity {
        /* access modifiers changed from: protected */
        public boolean isToolbarEnabled() {
            return false;
        }

        /* access modifiers changed from: protected */
        public void onCreate(Bundle bundle) {
            setTheme(SetupWizardUtils.getTheme(this, getIntent()));
            ThemeHelper.trySetDynamicColor(this);
            super.onCreate(bundle);
        }
    }

    public static class EnterprisePrivacySettingsActivity extends SettingsActivity {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            if (FeatureFactory.getFactory(this).getEnterprisePrivacyFeatureProvider(this).showParentalControls()) {
                finish();
            } else if (!EnterprisePrivacySettings.isPageEnabled(this)) {
                finish();
            }
        }
    }

    public static class MobileNetworkActivity extends SettingsActivity {
        private MobileNetworkIntentConverter mIntentConverter;

        /* access modifiers changed from: protected */
        public void onNewIntent(Intent intent) {
            super.onNewIntent(intent);
            Log.d("MobileNetworkActivity", "Starting onNewIntent");
            createUiFromIntent((Bundle) null, convertIntent(intent));
        }

        public Intent getIntent() {
            return convertIntent(super.getIntent());
        }

        private Intent convertIntent(Intent intent) {
            if (this.mIntentConverter == null) {
                this.mIntentConverter = new MobileNetworkIntentConverter(this);
            }
            Intent apply = this.mIntentConverter.apply(intent);
            return apply == null ? intent : apply;
        }

        public static boolean doesIntentContainOptInAction(Intent intent) {
            return TextUtils.equals(intent != null ? intent.getAction() : null, "android.telephony.ims.action.SHOW_CAPABILITY_DISCOVERY_OPT_IN");
        }
    }

    public static class GlyphsGuideSettingsActivity extends SettingsActivity {
        /* access modifiers changed from: protected */
        public boolean isToolbarEnabled() {
            return false;
        }

        public void onCreate(Bundle bundle) {
            setTheme(SetupWizardUtils.getTheme(this, getIntent()));
            ThemeHelper.trySetDynamicColor(this);
            super.onCreate(bundle);
        }

        public Intent getIntent() {
            return Settings.wrapIntentWithAllInOneTetherSettingsIfNeeded(getApplicationContext(), super.getIntent());
        }
    }
}
