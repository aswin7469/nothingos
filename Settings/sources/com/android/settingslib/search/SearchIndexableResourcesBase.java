package com.android.settingslib.search;

import com.android.settings.AllInOneTetherSettings;
import com.android.settings.LegalSettings;
import com.android.settings.TetherSettings;
import com.android.settings.accounts.AccountDashboardFragment;
import com.android.settings.accounts.ManagedProfileSettings;
import com.android.settings.applications.AppDashboardFragment;
import com.android.settings.applications.assist.ManageAssist;
import com.android.settings.applications.specialaccess.SpecialAccessSettings;
import com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminSettings;
import com.android.settings.applications.specialaccess.interactacrossprofiles.InteractAcrossProfilesSettings;
import com.android.settings.applications.specialaccess.pictureinpicture.PictureInPictureSettings;
import com.android.settings.applications.specialaccess.premiumsms.PremiumSmsAccess;
import com.android.settings.applications.specialaccess.turnscreenon.TurnScreenOnSettings;
import com.android.settings.applications.specialaccess.vrlistener.VrListenerSettings;
import com.android.settings.backup.BackupSettingsFragment;
import com.android.settings.backup.PrivacySettings;
import com.android.settings.backup.UserBackupSettingsActivity;
import com.android.settings.biometrics.combination.CombinedBiometricSettings;
import com.android.settings.biometrics.face.FaceSettings;
import com.android.settings.datausage.BillingCycleSettings;
import com.android.settings.datausage.DataSaverSummary;
import com.android.settings.datausage.UnrestrictedDataAccess;
import com.android.settings.datetime.DateTimeSettings;
import com.android.settings.datetime.timezone.TimeZoneSettings;
import com.android.settings.deletionhelper.AutomaticStorageManagerSettings;
import com.android.settings.development.WirelessDebuggingFragment;
import com.android.settings.development.featureflags.FeatureFlagsDashboard;
import com.android.settings.development.graphicsdriver.GraphicsDriverDashboard;
import com.android.settings.development.qstile.DevelopmentTileConfigFragment;
import com.android.settings.development.transcode.TranscodeSettingsFragment;
import com.android.settings.deviceinfo.StorageDashboardFragment;
import com.android.settings.deviceinfo.aboutphone.MyDeviceInfoFragment;
import com.android.settings.deviceinfo.firmwareversion.FirmwareVersionSettings;
import com.android.settings.deviceinfo.hardwareinfo.HardwareInfoFragment;
import com.android.settings.display.ColorModePreferenceFragment;
import com.android.settings.display.DeviceStateAutoRotateDetailsFragment;
import com.android.settings.display.ScreenResolutionFragment;
import com.android.settings.display.SmartAutoRotatePreferenceFragment;
import com.android.settings.display.ToggleFontSizePreferenceFragment;
import com.android.settings.dream.DreamSettings;
import com.android.settings.emergency.EmergencyDashboardFragment;
import com.android.settings.emergency.EmergencyGestureSettings;
import com.android.settings.enterprise.EnterprisePrivacySettings;
import com.android.settings.gestures.AssistGestureSettings;
import com.android.settings.gestures.DoubleTapPowerSettings;
import com.android.settings.gestures.DoubleTapScreenSettings;
import com.android.settings.gestures.DoubleTwistGestureSettings;
import com.android.settings.gestures.GestureSettings;
import com.android.settings.gestures.PickupGestureSettings;
import com.android.settings.gestures.PowerMenuSettings;
import com.android.settings.gestures.PreventRingingGestureSettings;
import com.android.settings.gestures.SwipeToNotificationSettings;
import com.android.settings.gestures.SystemNavigationGestureSettings;
import com.android.settings.gestures.TapScreenGestureSettings;
import com.android.settings.inputmethod.AvailableVirtualKeyboardFragment;
import com.android.settings.inputmethod.PhysicalKeyboardFragment;
import com.android.settings.inputmethod.UserDictionaryList;
import com.android.settings.language.LanguageAndInputSettings;
import com.android.settings.localepicker.LocaleListEditor;
import com.android.settings.location.LocationSettings;
import com.android.settings.location.RecentLocationAccessSeeAllFragment;
import com.android.settings.network.AdaptiveConnectivitySettings;
import com.android.settings.network.NetworkDashboardFragment;
import com.android.settings.network.NetworkProviderSettings;
import com.android.settings.nfc.PaymentSettings;
import com.android.settings.notification.BubbleNotificationSettings;
import com.android.settings.notification.ConfigureNotificationSettings;
import com.android.settings.notification.NotificationAccessSettings;
import com.android.settings.notification.SoundSettings;
import com.android.settings.notification.SoundWorkSettings;
import com.android.settings.notification.SpatialAudioSettings;
import com.android.settings.notification.app.AppBubbleNotificationSettings;
import com.android.settings.notification.zen.ZenAccessSettings;
import com.android.settings.notification.zen.ZenModeAutomationSettings;
import com.android.settings.notification.zen.ZenModeBlockedEffectsSettings;
import com.android.settings.notification.zen.ZenModeBypassingAppsSettings;
import com.android.settings.notification.zen.ZenModeCallsSettings;
import com.android.settings.notification.zen.ZenModeConversationsSettings;
import com.android.settings.notification.zen.ZenModeMessagesSettings;
import com.android.settings.notification.zen.ZenModePeopleSettings;
import com.android.settings.notification.zen.ZenModeSettings;
import com.android.settings.notification.zen.ZenModeSoundVibrationSettings;
import com.android.settings.print.PrintSettingsFragment;
import com.android.settings.privacy.PrivacyDashboardFragment;
import com.android.settings.security.CredentialManagementAppFragment;
import com.android.settings.security.EncryptionAndCredential;
import com.android.settings.security.InstallCertificateFromStorage;
import com.android.settings.security.LockscreenDashboardFragment;
import com.android.settings.security.ScreenPinningSettings;
import com.android.settings.security.SecurityAdvancedSettings;
import com.android.settings.security.SecuritySettings;
import com.android.settings.security.screenlock.ScreenLockSettings;
import com.android.settings.security.trustagent.TrustAgentSettings;
import com.android.settings.sound.MediaControlsSettings;
import com.android.settings.support.SupportDashboardActivity;
import com.android.settings.system.SystemDashboardFragment;
import com.android.settings.tts.TextToSpeechSettings;
import com.android.settings.tts.TtsEnginePreferenceFragment;
import com.android.settings.users.UserSettings;
import com.android.settings.wallpaper.WallpaperSuggestionActivity;
import com.android.settings.wifi.ConfigureWifiSettings;
import com.android.settings.wifi.WifiSettings;
import com.android.settings.wifi.tether.WifiTetherSettings;
import com.nothing.settings.game.GameNotificationDisplay;
import com.nothing.settings.game.GameSettings;
import com.nothing.settings.glyphs.notification.GlyphConversationListFragment;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SearchIndexableResourcesBase implements SearchIndexableResources {
    private final Set<SearchIndexableData> mProviders = new HashSet();

    public SearchIndexableResourcesBase() {
        addIndex(new SearchIndexableData(AllInOneTetherSettings.class, AllInOneTetherSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(LegalSettings.class, LegalSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TetherSettings.class, TetherSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(AccountDashboardFragment.class, AccountDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ManagedProfileSettings.class, ManagedProfileSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(AppDashboardFragment.class, AppDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ManageAssist.class, ManageAssist.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SpecialAccessSettings.class, SpecialAccessSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DeviceAdminSettings.class, DeviceAdminSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(InteractAcrossProfilesSettings.class, InteractAcrossProfilesSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PictureInPictureSettings.class, PictureInPictureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PremiumSmsAccess.class, PremiumSmsAccess.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TurnScreenOnSettings.class, TurnScreenOnSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(VrListenerSettings.class, VrListenerSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(BackupSettingsFragment.class, BackupSettingsFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PrivacySettings.class, PrivacySettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(UserBackupSettingsActivity.class, UserBackupSettingsActivity.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(CombinedBiometricSettings.class, CombinedBiometricSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(FaceSettings.class, FaceSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(BillingCycleSettings.class, BillingCycleSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DataSaverSummary.class, DataSaverSummary.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(UnrestrictedDataAccess.class, UnrestrictedDataAccess.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DateTimeSettings.class, DateTimeSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TimeZoneSettings.class, TimeZoneSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(AutomaticStorageManagerSettings.class, AutomaticStorageManagerSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(WirelessDebuggingFragment.class, WirelessDebuggingFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(FeatureFlagsDashboard.class, FeatureFlagsDashboard.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(GraphicsDriverDashboard.class, GraphicsDriverDashboard.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DevelopmentTileConfigFragment.class, DevelopmentTileConfigFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TranscodeSettingsFragment.class, TranscodeSettingsFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(StorageDashboardFragment.class, StorageDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(MyDeviceInfoFragment.class, MyDeviceInfoFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(FirmwareVersionSettings.class, FirmwareVersionSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(HardwareInfoFragment.class, HardwareInfoFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ColorModePreferenceFragment.class, ColorModePreferenceFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DeviceStateAutoRotateDetailsFragment.class, DeviceStateAutoRotateDetailsFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ScreenResolutionFragment.class, ScreenResolutionFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SmartAutoRotatePreferenceFragment.class, SmartAutoRotatePreferenceFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ToggleFontSizePreferenceFragment.class, ToggleFontSizePreferenceFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DreamSettings.class, DreamSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(EmergencyDashboardFragment.class, EmergencyDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(EmergencyGestureSettings.class, EmergencyGestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(EnterprisePrivacySettings.class, EnterprisePrivacySettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(AssistGestureSettings.class, AssistGestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DoubleTapPowerSettings.class, DoubleTapPowerSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DoubleTapScreenSettings.class, DoubleTapScreenSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(DoubleTwistGestureSettings.class, DoubleTwistGestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(GestureSettings.class, GestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PickupGestureSettings.class, PickupGestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PowerMenuSettings.class, PowerMenuSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PreventRingingGestureSettings.class, PreventRingingGestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SwipeToNotificationSettings.class, SwipeToNotificationSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SystemNavigationGestureSettings.class, SystemNavigationGestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TapScreenGestureSettings.class, TapScreenGestureSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(AvailableVirtualKeyboardFragment.class, AvailableVirtualKeyboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PhysicalKeyboardFragment.class, PhysicalKeyboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(UserDictionaryList.class, UserDictionaryList.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(LanguageAndInputSettings.class, LanguageAndInputSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(LocaleListEditor.class, LocaleListEditor.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(LocationSettings.class, LocationSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(RecentLocationAccessSeeAllFragment.class, RecentLocationAccessSeeAllFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(AdaptiveConnectivitySettings.class, AdaptiveConnectivitySettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(NetworkDashboardFragment.class, NetworkDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(NetworkProviderSettings.class, NetworkProviderSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PaymentSettings.class, PaymentSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(BubbleNotificationSettings.class, BubbleNotificationSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ConfigureNotificationSettings.class, ConfigureNotificationSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(NotificationAccessSettings.class, NotificationAccessSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SoundSettings.class, SoundSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SoundWorkSettings.class, SoundWorkSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SpatialAudioSettings.class, SpatialAudioSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(AppBubbleNotificationSettings.class, AppBubbleNotificationSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenAccessSettings.class, ZenAccessSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeAutomationSettings.class, ZenModeAutomationSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeBlockedEffectsSettings.class, ZenModeBlockedEffectsSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeBypassingAppsSettings.class, ZenModeBypassingAppsSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeCallsSettings.class, ZenModeCallsSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeConversationsSettings.class, ZenModeConversationsSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeMessagesSettings.class, ZenModeMessagesSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModePeopleSettings.class, ZenModePeopleSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeSettings.class, ZenModeSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ZenModeSoundVibrationSettings.class, ZenModeSoundVibrationSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PrintSettingsFragment.class, PrintSettingsFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(PrivacyDashboardFragment.class, PrivacyDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(CredentialManagementAppFragment.class, CredentialManagementAppFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(EncryptionAndCredential.class, EncryptionAndCredential.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(InstallCertificateFromStorage.class, InstallCertificateFromStorage.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(LockscreenDashboardFragment.class, LockscreenDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ScreenPinningSettings.class, ScreenPinningSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SecurityAdvancedSettings.class, SecurityAdvancedSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SecuritySettings.class, SecuritySettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ScreenLockSettings.class, ScreenLockSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TrustAgentSettings.class, TrustAgentSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(MediaControlsSettings.class, MediaControlsSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SupportDashboardActivity.class, SupportDashboardActivity.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(SystemDashboardFragment.class, SystemDashboardFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TextToSpeechSettings.class, TextToSpeechSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(TtsEnginePreferenceFragment.class, TtsEnginePreferenceFragment.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(UserSettings.class, UserSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(WallpaperSuggestionActivity.class, WallpaperSuggestionActivity.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(ConfigureWifiSettings.class, ConfigureWifiSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(WifiSettings.class, WifiSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(WifiTetherSettings.class, WifiTetherSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(GameNotificationDisplay.class, GameNotificationDisplay.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(GameSettings.class, GameSettings.SEARCH_INDEX_DATA_PROVIDER));
        addIndex(new SearchIndexableData(GlyphConversationListFragment.class, GlyphConversationListFragment.SEARCH_INDEX_DATA_PROVIDER));
    }

    public void addIndex(SearchIndexableData searchIndexableData) {
        this.mProviders.add(searchIndexableData);
    }

    public Collection<SearchIndexableData> getProviderValues() {
        return this.mProviders;
    }
}
