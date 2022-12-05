package android.app.admin;

import android.accounts.Account;
import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.app.admin.StartInstallingUpdateCallback;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.StringParceledListSlice;
import android.graphics.Bitmap;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keystore.ParcelableKeyGenParameterSpec;
import android.telephony.data.ApnSetting;
import android.text.TextUtils;
import java.util.List;
/* loaded from: classes.dex */
public interface IDevicePolicyManager extends IInterface {
    void acknowledgeDeviceCompliant() throws RemoteException;

    void addCrossProfileIntentFilter(ComponentName componentName, IntentFilter intentFilter, int i) throws RemoteException;

    boolean addCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    int addOverrideApn(ComponentName componentName, ApnSetting apnSetting) throws RemoteException;

    void addPersistentPreferredActivity(ComponentName componentName, IntentFilter intentFilter, ComponentName componentName2) throws RemoteException;

    boolean approveCaCert(String str, int i, boolean z) throws RemoteException;

    boolean bindDeviceAdminServiceAsUser(ComponentName componentName, IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, IServiceConnection iServiceConnection, int i, int i2) throws RemoteException;

    boolean canAdminGrantSensorsPermissionsForUser(int i) throws RemoteException;

    boolean canProfileOwnerResetPasswordWhenLocked(int i) throws RemoteException;

    boolean canUsbDataSignalingBeDisabled() throws RemoteException;

    boolean checkDeviceIdentifierAccess(String str, int i, int i2) throws RemoteException;

    int checkProvisioningPreCondition(String str, String str2) throws RemoteException;

    void choosePrivateKeyAlias(int i, Uri uri, String str, IBinder iBinder) throws RemoteException;

    void clearApplicationUserData(ComponentName componentName, String str, IPackageDataObserver iPackageDataObserver) throws RemoteException;

    void clearCrossProfileIntentFilters(ComponentName componentName) throws RemoteException;

    void clearDeviceOwner(String str) throws RemoteException;

    void clearPackagePersistentPreferredActivities(ComponentName componentName, String str) throws RemoteException;

    void clearProfileOwner(ComponentName componentName) throws RemoteException;

    boolean clearResetPasswordToken(ComponentName componentName) throws RemoteException;

    void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException;

    Intent createAdminSupportIntent(String str) throws RemoteException;

    UserHandle createAndManageUser(ComponentName componentName, String str, ComponentName componentName2, PersistableBundle persistableBundle, int i) throws RemoteException;

    UserHandle createAndProvisionManagedProfile(ManagedProfileProvisioningParams managedProfileProvisioningParams, String str) throws RemoteException;

    void enableSystemApp(ComponentName componentName, String str, String str2) throws RemoteException;

    int enableSystemAppWithIntent(ComponentName componentName, String str, Intent intent) throws RemoteException;

    void enforceCanManageCaCerts(ComponentName componentName, String str) throws RemoteException;

    void finalizeWorkProfileProvisioning(UserHandle userHandle, Account account) throws RemoteException;

    long forceNetworkLogs() throws RemoteException;

    void forceRemoveActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    long forceSecurityLogs() throws RemoteException;

    void forceUpdateUserSetupComplete(int i) throws RemoteException;

    boolean generateKeyPair(ComponentName componentName, String str, String str2, ParcelableKeyGenParameterSpec parcelableKeyGenParameterSpec, int i, KeymasterCertificateChain keymasterCertificateChain) throws RemoteException;

    String[] getAccountTypesWithManagementDisabled() throws RemoteException;

    String[] getAccountTypesWithManagementDisabledAsUser(int i, boolean z) throws RemoteException;

    List<ComponentName> getActiveAdmins(int i) throws RemoteException;

    List<String> getAffiliationIds(ComponentName componentName) throws RemoteException;

    int getAggregatedPasswordComplexityForUser(int i, boolean z) throws RemoteException;

    List<String> getAllCrossProfilePackages() throws RemoteException;

    List<String> getAlwaysOnVpnLockdownAllowlist(ComponentName componentName) throws RemoteException;

    String getAlwaysOnVpnPackage(ComponentName componentName) throws RemoteException;

    String getAlwaysOnVpnPackageForUser(int i) throws RemoteException;

    Bundle getApplicationRestrictions(ComponentName componentName, String str, String str2) throws RemoteException;

    String getApplicationRestrictionsManagingPackage(ComponentName componentName) throws RemoteException;

    boolean getAutoTimeEnabled(ComponentName componentName) throws RemoteException;

    boolean getAutoTimeRequired() throws RemoteException;

    boolean getAutoTimeZoneEnabled(ComponentName componentName) throws RemoteException;

    List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName componentName) throws RemoteException;

    boolean getBluetoothContactSharingDisabled(ComponentName componentName) throws RemoteException;

    boolean getBluetoothContactSharingDisabledForUser(int i) throws RemoteException;

    boolean getCameraDisabled(ComponentName componentName, int i, boolean z) throws RemoteException;

    String getCertInstallerPackage(ComponentName componentName) throws RemoteException;

    List<String> getCrossProfileCalendarPackages(ComponentName componentName) throws RemoteException;

    List<String> getCrossProfileCalendarPackagesForUser(int i) throws RemoteException;

    boolean getCrossProfileCallerIdDisabled(ComponentName componentName) throws RemoteException;

    boolean getCrossProfileCallerIdDisabledForUser(int i) throws RemoteException;

    boolean getCrossProfileContactsSearchDisabled(ComponentName componentName) throws RemoteException;

    boolean getCrossProfileContactsSearchDisabledForUser(int i) throws RemoteException;

    List<String> getCrossProfilePackages(ComponentName componentName) throws RemoteException;

    List<String> getCrossProfileWidgetProviders(ComponentName componentName) throws RemoteException;

    int getCurrentFailedPasswordAttempts(int i, boolean z) throws RemoteException;

    List<String> getDefaultCrossProfilePackages() throws RemoteException;

    List<String> getDelegatePackages(ComponentName componentName, String str) throws RemoteException;

    List<String> getDelegatedScopes(ComponentName componentName, String str) throws RemoteException;

    ComponentName getDeviceOwnerComponent(boolean z) throws RemoteException;

    CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException;

    String getDeviceOwnerName() throws RemoteException;

    CharSequence getDeviceOwnerOrganizationName() throws RemoteException;

    int getDeviceOwnerType(ComponentName componentName) throws RemoteException;

    int getDeviceOwnerUserId() throws RemoteException;

    List<String> getDisallowedSystemApps(ComponentName componentName, int i, String str) throws RemoteException;

    boolean getDoNotAskCredentialsOnBoot() throws RemoteException;

    CharSequence getEndUserSessionMessage(ComponentName componentName) throws RemoteException;

    Bundle getEnforcingAdminAndUserDetails(int i, String str) throws RemoteException;

    String getEnrollmentSpecificId(String str) throws RemoteException;

    FactoryResetProtectionPolicy getFactoryResetProtectionPolicy(ComponentName componentName) throws RemoteException;

    boolean getForceEphemeralUsers(ComponentName componentName) throws RemoteException;

    String getGlobalPrivateDnsHost(ComponentName componentName) throws RemoteException;

    int getGlobalPrivateDnsMode(ComponentName componentName) throws RemoteException;

    ComponentName getGlobalProxyAdmin(int i) throws RemoteException;

    List<String> getKeepUninstalledPackages(ComponentName componentName, String str) throws RemoteException;

    ParcelableGranteeMap getKeyPairGrants(String str, String str2) throws RemoteException;

    int getKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getLastBugReportRequestTime() throws RemoteException;

    long getLastNetworkLogRetrievalTime() throws RemoteException;

    long getLastSecurityLogRetrievalTime() throws RemoteException;

    int getLockTaskFeatures(ComponentName componentName) throws RemoteException;

    String[] getLockTaskPackages(ComponentName componentName) throws RemoteException;

    CharSequence getLongSupportMessage(ComponentName componentName) throws RemoteException;

    CharSequence getLongSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    long getManagedProfileMaximumTimeOff(ComponentName componentName) throws RemoteException;

    int getMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getMaximumTimeToLock(ComponentName componentName, int i, boolean z) throws RemoteException;

    List<String> getMeteredDataDisabledPackages(ComponentName componentName) throws RemoteException;

    int getNearbyAppStreamingPolicy(int i) throws RemoteException;

    int getNearbyNotificationStreamingPolicy(int i) throws RemoteException;

    int getOrganizationColor(ComponentName componentName) throws RemoteException;

    int getOrganizationColorForUser(int i) throws RemoteException;

    CharSequence getOrganizationName(ComponentName componentName) throws RemoteException;

    CharSequence getOrganizationNameForUser(int i) throws RemoteException;

    List<ApnSetting> getOverrideApns(ComponentName componentName) throws RemoteException;

    StringParceledListSlice getOwnerInstalledCaCerts(UserHandle userHandle) throws RemoteException;

    int getPasswordComplexity(boolean z) throws RemoteException;

    long getPasswordExpiration(ComponentName componentName, int i, boolean z) throws RemoteException;

    long getPasswordExpirationTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    PasswordMetrics getPasswordMinimumMetrics(int i, boolean z) throws RemoteException;

    int getPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    int getPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    SystemUpdateInfo getPendingSystemUpdate(ComponentName componentName) throws RemoteException;

    int getPermissionGrantState(ComponentName componentName, String str, String str2, String str3) throws RemoteException;

    int getPermissionPolicy(ComponentName componentName) throws RemoteException;

    List getPermittedAccessibilityServices(ComponentName componentName) throws RemoteException;

    List getPermittedAccessibilityServicesForUser(int i) throws RemoteException;

    List<String> getPermittedCrossProfileNotificationListeners(ComponentName componentName) throws RemoteException;

    List getPermittedInputMethods(ComponentName componentName, boolean z) throws RemoteException;

    List getPermittedInputMethodsForCurrentUser() throws RemoteException;

    int getPersonalAppsSuspendedReasons(ComponentName componentName) throws RemoteException;

    ComponentName getProfileOwnerAsUser(int i) throws RemoteException;

    String getProfileOwnerName(int i) throws RemoteException;

    ComponentName getProfileOwnerOrDeviceOwnerSupervisionComponent(UserHandle userHandle) throws RemoteException;

    int getProfileWithMinimumFailedPasswordsForWipe(int i, boolean z) throws RemoteException;

    void getRemoveWarning(ComponentName componentName, RemoteCallback remoteCallback, int i) throws RemoteException;

    int getRequiredPasswordComplexity(boolean z) throws RemoteException;

    long getRequiredStrongAuthTimeout(ComponentName componentName, int i, boolean z) throws RemoteException;

    ComponentName getRestrictionsProvider(int i) throws RemoteException;

    boolean getScreenCaptureDisabled(ComponentName componentName, int i, boolean z) throws RemoteException;

    List<UserHandle> getSecondaryUsers(ComponentName componentName) throws RemoteException;

    CharSequence getShortSupportMessage(ComponentName componentName) throws RemoteException;

    CharSequence getShortSupportMessageForUser(ComponentName componentName, int i) throws RemoteException;

    CharSequence getStartUserSessionMessage(ComponentName componentName) throws RemoteException;

    boolean getStorageEncryption(ComponentName componentName, int i) throws RemoteException;

    int getStorageEncryptionStatus(String str, int i) throws RemoteException;

    SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException;

    PersistableBundle getTransferOwnershipBundle() throws RemoteException;

    List<PersistableBundle> getTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, int i, boolean z) throws RemoteException;

    List<String> getUserControlDisabledPackages(ComponentName componentName) throws RemoteException;

    int getUserProvisioningState() throws RemoteException;

    Bundle getUserRestrictions(ComponentName componentName, boolean z) throws RemoteException;

    String getWifiMacAddress(ComponentName componentName) throws RemoteException;

    boolean hasDeviceOwner() throws RemoteException;

    boolean hasGrantedPolicy(ComponentName componentName, int i, int i2) throws RemoteException;

    boolean hasKeyPair(String str, String str2) throws RemoteException;

    boolean hasLockdownAdminConfiguredNetworks(ComponentName componentName) throws RemoteException;

    boolean hasUserSetupCompleted() throws RemoteException;

    boolean installCaCert(ComponentName componentName, String str, byte[] bArr) throws RemoteException;

    boolean installExistingPackage(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean installKeyPair(ComponentName componentName, String str, byte[] bArr, byte[] bArr2, byte[] bArr3, String str2, boolean z, boolean z2) throws RemoteException;

    void installUpdateFromFile(ComponentName componentName, ParcelFileDescriptor parcelFileDescriptor, StartInstallingUpdateCallback startInstallingUpdateCallback) throws RemoteException;

    boolean isAccessibilityServicePermittedByAdmin(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isActivePasswordSufficient(int i, boolean z) throws RemoteException;

    boolean isActivePasswordSufficientForDeviceRequirement() throws RemoteException;

    boolean isAdminActive(ComponentName componentName, int i) throws RemoteException;

    boolean isAffiliatedUser(int i) throws RemoteException;

    boolean isAlwaysOnVpnLockdownEnabled(ComponentName componentName) throws RemoteException;

    boolean isAlwaysOnVpnLockdownEnabledForUser(int i) throws RemoteException;

    boolean isApplicationHidden(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    boolean isBackupServiceEnabled(ComponentName componentName) throws RemoteException;

    boolean isCaCertApproved(String str, int i) throws RemoteException;

    boolean isCallerApplicationRestrictionsManagingPackage(String str) throws RemoteException;

    boolean isCallingUserAffiliated() throws RemoteException;

    boolean isCommonCriteriaModeEnabled(ComponentName componentName) throws RemoteException;

    boolean isComplianceAcknowledgementRequired() throws RemoteException;

    boolean isCurrentInputMethodSetByOwner() throws RemoteException;

    boolean isDeviceProvisioned() throws RemoteException;

    boolean isDeviceProvisioningConfigApplied() throws RemoteException;

    boolean isEphemeralUser(ComponentName componentName) throws RemoteException;

    boolean isFactoryResetProtectionPolicySupported() throws RemoteException;

    boolean isInputMethodPermittedByAdmin(ComponentName componentName, String str, int i, boolean z) throws RemoteException;

    boolean isKeyPairGrantedToWifiAuth(String str, String str2) throws RemoteException;

    boolean isLockTaskPermitted(String str) throws RemoteException;

    boolean isLogoutEnabled() throws RemoteException;

    boolean isManagedKiosk() throws RemoteException;

    boolean isManagedProfile(ComponentName componentName) throws RemoteException;

    boolean isMasterVolumeMuted(ComponentName componentName) throws RemoteException;

    boolean isMeteredDataDisabledPackageForUser(ComponentName componentName, String str, int i) throws RemoteException;

    boolean isNetworkLoggingEnabled(ComponentName componentName, String str) throws RemoteException;

    boolean isNotificationListenerServicePermitted(String str, int i) throws RemoteException;

    boolean isOrganizationOwnedDeviceWithManagedProfile() throws RemoteException;

    boolean isOverrideApnEnabled(ComponentName componentName) throws RemoteException;

    boolean isPackageAllowedToAccessCalendarForUser(String str, int i) throws RemoteException;

    boolean isPackageSuspended(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean isPasswordSufficientAfterProfileUnification(int i, int i2) throws RemoteException;

    boolean isPreferentialNetworkServiceEnabled(int i) throws RemoteException;

    boolean isProvisioningAllowed(String str, String str2) throws RemoteException;

    boolean isRemovingAdmin(ComponentName componentName, int i) throws RemoteException;

    boolean isResetPasswordTokenActive(ComponentName componentName) throws RemoteException;

    boolean isSafeOperation(int i) throws RemoteException;

    boolean isSecondaryLockscreenEnabled(UserHandle userHandle) throws RemoteException;

    boolean isSecurityLoggingEnabled(ComponentName componentName, String str) throws RemoteException;

    boolean isSeparateProfileChallengeAllowed(int i) throws RemoteException;

    boolean isUnattendedManagedKiosk() throws RemoteException;

    boolean isUninstallBlocked(ComponentName componentName, String str) throws RemoteException;

    boolean isUninstallInQueue(String str) throws RemoteException;

    boolean isUsbDataSignalingEnabled(String str) throws RemoteException;

    boolean isUsbDataSignalingEnabledForUser(int i) throws RemoteException;

    boolean isUsingUnifiedPassword(ComponentName componentName) throws RemoteException;

    List<UserHandle> listForegroundAffiliatedUsers() throws RemoteException;

    List<String> listPolicyExemptApps() throws RemoteException;

    void lockNow(int i, boolean z) throws RemoteException;

    int logoutUser(ComponentName componentName) throws RemoteException;

    void markProfileOwnerOnOrganizationOwnedDevice(ComponentName componentName, int i) throws RemoteException;

    void notifyLockTaskModeChanged(boolean z, String str, int i) throws RemoteException;

    void notifyPendingSystemUpdate(SystemUpdateInfo systemUpdateInfo) throws RemoteException;

    boolean packageHasActiveAdmins(String str, int i) throws RemoteException;

    void provisionFullyManagedDevice(FullyManagedDeviceProvisioningParams fullyManagedDeviceProvisioningParams, String str) throws RemoteException;

    void reboot(ComponentName componentName) throws RemoteException;

    void removeActiveAdmin(ComponentName componentName, int i) throws RemoteException;

    boolean removeCrossProfileWidgetProvider(ComponentName componentName, String str) throws RemoteException;

    boolean removeKeyPair(ComponentName componentName, String str, String str2) throws RemoteException;

    boolean removeOverrideApn(ComponentName componentName, int i) throws RemoteException;

    boolean removeUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    void reportFailedBiometricAttempt(int i) throws RemoteException;

    void reportFailedPasswordAttempt(int i) throws RemoteException;

    void reportKeyguardDismissed(int i) throws RemoteException;

    void reportKeyguardSecured(int i) throws RemoteException;

    void reportPasswordChanged(int i) throws RemoteException;

    void reportSuccessfulBiometricAttempt(int i) throws RemoteException;

    void reportSuccessfulPasswordAttempt(int i) throws RemoteException;

    boolean requestBugreport(ComponentName componentName) throws RemoteException;

    void resetDefaultCrossProfileIntentFilters(int i) throws RemoteException;

    void resetNewUserDisclaimer() throws RemoteException;

    boolean resetPassword(String str, int i) throws RemoteException;

    boolean resetPasswordWithToken(ComponentName componentName, String str, byte[] bArr, int i) throws RemoteException;

    List<NetworkEvent> retrieveNetworkLogs(ComponentName componentName, String str, long j) throws RemoteException;

    ParceledListSlice retrievePreRebootSecurityLogs(ComponentName componentName, String str) throws RemoteException;

    ParceledListSlice retrieveSecurityLogs(ComponentName componentName, String str) throws RemoteException;

    void setAccountManagementDisabled(ComponentName componentName, String str, boolean z, boolean z2) throws RemoteException;

    void setActiveAdmin(ComponentName componentName, boolean z, int i) throws RemoteException;

    void setAffiliationIds(ComponentName componentName, List<String> list) throws RemoteException;

    boolean setAlwaysOnVpnPackage(ComponentName componentName, String str, boolean z, List<String> list) throws RemoteException;

    boolean setApplicationHidden(ComponentName componentName, String str, String str2, boolean z, boolean z2) throws RemoteException;

    void setApplicationRestrictions(ComponentName componentName, String str, String str2, Bundle bundle) throws RemoteException;

    boolean setApplicationRestrictionsManagingPackage(ComponentName componentName, String str) throws RemoteException;

    void setAutoTimeEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setAutoTimeRequired(ComponentName componentName, boolean z) throws RemoteException;

    void setAutoTimeZoneEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setBackupServiceEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setBluetoothContactSharingDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCameraDisabled(ComponentName componentName, boolean z, boolean z2) throws RemoteException;

    void setCertInstallerPackage(ComponentName componentName, String str) throws RemoteException;

    void setCommonCriteriaModeEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setConfiguredNetworksLockdownState(ComponentName componentName, boolean z) throws RemoteException;

    void setCrossProfileCalendarPackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setCrossProfileCallerIdDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCrossProfileContactsSearchDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setCrossProfilePackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setDefaultSmsApplication(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setDelegatedScopes(ComponentName componentName, String str, List<String> list) throws RemoteException;

    boolean setDeviceOwner(ComponentName componentName, String str, int i) throws RemoteException;

    void setDeviceOwnerLockScreenInfo(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setDeviceOwnerType(ComponentName componentName, int i) throws RemoteException;

    void setDeviceProvisioningConfigApplied() throws RemoteException;

    void setEndUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setFactoryResetProtectionPolicy(ComponentName componentName, FactoryResetProtectionPolicy factoryResetProtectionPolicy) throws RemoteException;

    void setForceEphemeralUsers(ComponentName componentName, boolean z) throws RemoteException;

    int setGlobalPrivateDns(ComponentName componentName, int i, String str) throws RemoteException;

    ComponentName setGlobalProxy(ComponentName componentName, String str, String str2) throws RemoteException;

    void setGlobalSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setKeepUninstalledPackages(ComponentName componentName, String str, List<String> list) throws RemoteException;

    boolean setKeyGrantForApp(ComponentName componentName, String str, String str2, String str3, boolean z) throws RemoteException;

    boolean setKeyGrantToWifiAuth(String str, String str2, boolean z) throws RemoteException;

    boolean setKeyPairCertificate(ComponentName componentName, String str, String str2, byte[] bArr, byte[] bArr2, boolean z) throws RemoteException;

    boolean setKeyguardDisabled(ComponentName componentName, boolean z) throws RemoteException;

    void setKeyguardDisabledFeatures(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setLocationEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setLockTaskFeatures(ComponentName componentName, int i) throws RemoteException;

    void setLockTaskPackages(ComponentName componentName, String[] strArr) throws RemoteException;

    void setLogoutEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setLongSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setManagedProfileMaximumTimeOff(ComponentName componentName, long j) throws RemoteException;

    void setMasterVolumeMuted(ComponentName componentName, boolean z) throws RemoteException;

    void setMaximumFailedPasswordsForWipe(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setMaximumTimeToLock(ComponentName componentName, long j, boolean z) throws RemoteException;

    List<String> setMeteredDataDisabledPackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setNearbyAppStreamingPolicy(int i) throws RemoteException;

    void setNearbyNotificationStreamingPolicy(int i) throws RemoteException;

    void setNetworkLoggingEnabled(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setNextOperationSafety(int i, int i2) throws RemoteException;

    void setOrganizationColor(ComponentName componentName, int i) throws RemoteException;

    void setOrganizationColorForUser(int i, int i2) throws RemoteException;

    void setOrganizationIdForUser(String str, String str2, int i) throws RemoteException;

    void setOrganizationName(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setOverrideApnsEnabled(ComponentName componentName, boolean z) throws RemoteException;

    String[] setPackagesSuspended(ComponentName componentName, String str, String[] strArr, boolean z) throws RemoteException;

    void setPasswordExpirationTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    void setPasswordHistoryLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLength(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLetters(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumLowerCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumNonLetter(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumNumeric(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumSymbols(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordMinimumUpperCase(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPasswordQuality(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setPermissionGrantState(ComponentName componentName, String str, String str2, String str3, int i, RemoteCallback remoteCallback) throws RemoteException;

    void setPermissionPolicy(ComponentName componentName, String str, int i) throws RemoteException;

    boolean setPermittedAccessibilityServices(ComponentName componentName, List list) throws RemoteException;

    boolean setPermittedCrossProfileNotificationListeners(ComponentName componentName, List<String> list) throws RemoteException;

    boolean setPermittedInputMethods(ComponentName componentName, List list, boolean z) throws RemoteException;

    void setPersonalAppsSuspended(ComponentName componentName, boolean z) throws RemoteException;

    void setPreferentialNetworkServiceEnabled(boolean z) throws RemoteException;

    void setProfileEnabled(ComponentName componentName) throws RemoteException;

    void setProfileName(ComponentName componentName, String str) throws RemoteException;

    boolean setProfileOwner(ComponentName componentName, String str, int i) throws RemoteException;

    void setRecommendedGlobalProxy(ComponentName componentName, ProxyInfo proxyInfo) throws RemoteException;

    void setRequiredPasswordComplexity(int i, boolean z) throws RemoteException;

    void setRequiredStrongAuthTimeout(ComponentName componentName, long j, boolean z) throws RemoteException;

    boolean setResetPasswordToken(ComponentName componentName, byte[] bArr) throws RemoteException;

    void setRestrictionsProvider(ComponentName componentName, ComponentName componentName2) throws RemoteException;

    void setScreenCaptureDisabled(ComponentName componentName, boolean z, boolean z2) throws RemoteException;

    void setSecondaryLockscreenEnabled(ComponentName componentName, boolean z) throws RemoteException;

    void setSecureSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setSecurityLoggingEnabled(ComponentName componentName, String str, boolean z) throws RemoteException;

    void setShortSupportMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    void setStartUserSessionMessage(ComponentName componentName, CharSequence charSequence) throws RemoteException;

    boolean setStatusBarDisabled(ComponentName componentName, boolean z) throws RemoteException;

    int setStorageEncryption(ComponentName componentName, boolean z) throws RemoteException;

    void setSystemSetting(ComponentName componentName, String str, String str2) throws RemoteException;

    void setSystemUpdatePolicy(ComponentName componentName, SystemUpdatePolicy systemUpdatePolicy) throws RemoteException;

    boolean setTime(ComponentName componentName, long j) throws RemoteException;

    boolean setTimeZone(ComponentName componentName, String str) throws RemoteException;

    void setTrustAgentConfiguration(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle, boolean z) throws RemoteException;

    void setUninstallBlocked(ComponentName componentName, String str, String str2, boolean z) throws RemoteException;

    void setUsbDataSignalingEnabled(String str, boolean z) throws RemoteException;

    void setUserControlDisabledPackages(ComponentName componentName, List<String> list) throws RemoteException;

    void setUserIcon(ComponentName componentName, Bitmap bitmap) throws RemoteException;

    void setUserProvisioningState(int i, int i2) throws RemoteException;

    void setUserRestriction(ComponentName componentName, String str, boolean z, boolean z2) throws RemoteException;

    void startManagedQuickContact(String str, long j, boolean z, long j2, Intent intent) throws RemoteException;

    int startUserInBackground(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean startViewCalendarEventInManagedProfile(String str, long j, long j2, long j3, boolean z, int i) throws RemoteException;

    int stopUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean switchUser(ComponentName componentName, UserHandle userHandle) throws RemoteException;

    void transferOwnership(ComponentName componentName, ComponentName componentName2, PersistableBundle persistableBundle) throws RemoteException;

    void uninstallCaCerts(ComponentName componentName, String str, String[] strArr) throws RemoteException;

    void uninstallPackageWithActiveAdmins(String str) throws RemoteException;

    boolean updateOverrideApn(ComponentName componentName, int i, ApnSetting apnSetting) throws RemoteException;

    void wipeDataWithReason(int i, String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IDevicePolicyManager {
        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordQuality(ComponentName who, int quality, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordQuality(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumLength(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumUpperCase(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumUpperCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumLowerCase(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumLowerCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumLetters(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumLetters(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumNumeric(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumNumeric(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumSymbols(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumSymbols(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordMinimumNonLetter(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordMinimumNonLetter(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public PasswordMetrics getPasswordMinimumMetrics(int userHandle, boolean deviceWideOnly) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordHistoryLength(ComponentName who, int length, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordHistoryLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPasswordExpirationTimeout(ComponentName who, long expiration, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getPasswordExpirationTimeout(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getPasswordExpiration(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isActivePasswordSufficient(int userHandle, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isActivePasswordSufficientForDeviceRequirement() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isPasswordSufficientAfterProfileUnification(int userHandle, int profileUser) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPasswordComplexity(boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setRequiredPasswordComplexity(int passwordComplexity, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getRequiredPasswordComplexity(boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getAggregatedPasswordComplexityForUser(int userId, boolean deviceWideOnly) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUsingUnifiedPassword(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getCurrentFailedPasswordAttempts(int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getProfileWithMinimumFailedPasswordsForWipe(int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void finalizeWorkProfileProvisioning(UserHandle managedProfileUser, Account migratedAccount) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean resetPassword(String password, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setMaximumTimeToLock(ComponentName who, long timeMs, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getMaximumTimeToLock(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setRequiredStrongAuthTimeout(ComponentName who, long timeMs, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getRequiredStrongAuthTimeout(ComponentName who, int userId, boolean parent) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void lockNow(int flags, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void wipeDataWithReason(int flags, String wipeReasonForUser, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setFactoryResetProtectionPolicy(ComponentName who, FactoryResetProtectionPolicy policy) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public FactoryResetProtectionPolicy getFactoryResetProtectionPolicy(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isFactoryResetProtectionPolicySupported() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName setGlobalProxy(ComponentName admin, String proxySpec, String exclusionList) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getGlobalProxyAdmin(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int setStorageEncryption(ComponentName who, boolean encrypt) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getStorageEncryption(ComponentName who, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getStorageEncryptionStatus(String callerPackage, int userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean requestBugreport(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCameraDisabled(ComponentName who, boolean disabled, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCameraDisabled(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setScreenCaptureDisabled(ComponentName who, boolean disabled, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getScreenCaptureDisabled(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setNearbyNotificationStreamingPolicy(int policy) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getNearbyNotificationStreamingPolicy(int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setNearbyAppStreamingPolicy(int policy) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getNearbyAppStreamingPolicy(int userId) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setKeyguardDisabledFeatures(ComponentName who, int which, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getKeyguardDisabledFeatures(ComponentName who, int userHandle, boolean parent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAdminActive(ComponentName policyReceiver, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<ComponentName> getActiveAdmins(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean packageHasActiveAdmins(String packageName, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void getRemoveWarning(ComponentName policyReceiver, RemoteCallback result, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void removeActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasGrantedPolicy(ComponentName policyReceiver, int usesPolicy, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportPasswordChanged(int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportFailedPasswordAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportSuccessfulPasswordAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportFailedBiometricAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportSuccessfulBiometricAttempt(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportKeyguardDismissed(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reportKeyguardSecured(int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setDeviceOwner(ComponentName who, String ownerName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getDeviceOwnerComponent(boolean callingUserOnly) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasDeviceOwner() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getDeviceOwnerName() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearDeviceOwner(String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getDeviceOwnerUserId() throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setProfileOwner(ComponentName who, String ownerName, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getProfileOwnerAsUser(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getProfileOwnerOrDeviceOwnerSupervisionComponent(UserHandle userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getProfileOwnerName(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setProfileEnabled(ComponentName who) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setProfileName(ComponentName who, String profileName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearProfileOwner(ComponentName who) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasUserSetupCompleted() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isOrganizationOwnedDeviceWithManagedProfile() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean checkDeviceIdentifierAccess(String packageName, int pid, int uid) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDeviceOwnerLockScreenInfo(ComponentName who, CharSequence deviceOwnerInfo) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] setPackagesSuspended(ComponentName admin, String callerPackage, String[] packageNames, boolean suspended) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isPackageSuspended(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> listPolicyExemptApps() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean installCaCert(ComponentName admin, String callerPackage, byte[] certBuffer) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void uninstallCaCerts(ComponentName admin, String callerPackage, String[] aliases) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void enforceCanManageCaCerts(ComponentName admin, String callerPackage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean approveCaCert(String alias, int userHandle, boolean approval) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCaCertApproved(String alias, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean installKeyPair(ComponentName who, String callerPackage, byte[] privKeyBuffer, byte[] certBuffer, byte[] certChainBuffer, String alias, boolean requestAccess, boolean isUserSelectable) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeKeyPair(ComponentName who, String callerPackage, String alias) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasKeyPair(String callerPackage, String alias) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean generateKeyPair(ComponentName who, String callerPackage, String algorithm, ParcelableKeyGenParameterSpec keySpec, int idAttestationFlags, KeymasterCertificateChain attestationChain) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setKeyPairCertificate(ComponentName who, String callerPackage, String alias, byte[] certBuffer, byte[] certChainBuffer, boolean isUserSelectable) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void choosePrivateKeyAlias(int uid, Uri uri, String alias, IBinder aliasCallback) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDelegatedScopes(ComponentName who, String delegatePackage, List<String> scopes) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getDelegatedScopes(ComponentName who, String delegatePackage) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getDelegatePackages(ComponentName who, String scope) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCertInstallerPackage(ComponentName who, String installerPackage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getCertInstallerPackage(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setAlwaysOnVpnPackage(ComponentName who, String vpnPackage, boolean lockdown, List<String> lockdownAllowlist) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getAlwaysOnVpnPackage(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getAlwaysOnVpnPackageForUser(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAlwaysOnVpnLockdownEnabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAlwaysOnVpnLockdownEnabledForUser(int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getAlwaysOnVpnLockdownAllowlist(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDefaultSmsApplication(ComponentName admin, String packageName, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setApplicationRestrictions(ComponentName who, String callerPackage, String packageName, Bundle settings) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public Bundle getApplicationRestrictions(ComponentName who, String callerPackage, String packageName) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setApplicationRestrictionsManagingPackage(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getApplicationRestrictionsManagingPackage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCallerApplicationRestrictionsManagingPackage(String callerPackage) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setRestrictionsProvider(ComponentName who, ComponentName provider) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ComponentName getRestrictionsProvider(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUserRestriction(ComponentName who, String key, boolean enable, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public Bundle getUserRestrictions(ComponentName who, boolean parent) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearCrossProfileIntentFilters(ComponentName admin) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setPermittedAccessibilityServices(ComponentName admin, List packageList) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedAccessibilityServices(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedAccessibilityServicesForUser(int userId) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAccessibilityServicePermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setPermittedInputMethods(ComponentName admin, List packageList, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedInputMethods(ComponentName admin, boolean parent) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List getPermittedInputMethodsForCurrentUser() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isInputMethodPermittedByAdmin(ComponentName admin, String packageName, int userId, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setPermittedCrossProfileNotificationListeners(ComponentName admin, List<String> packageList) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getPermittedCrossProfileNotificationListeners(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isNotificationListenerServicePermitted(String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public Intent createAdminSupportIntent(String restriction) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public Bundle getEnforcingAdminAndUserDetails(int userId, String restriction) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean hidden, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean parent) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public UserHandle createAndManageUser(ComponentName who, String name, ComponentName profileOwner, PersistableBundle adminExtras, int flags) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean switchUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int startUserInBackground(ComponentName who, UserHandle userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int stopUser(ComponentName who, UserHandle userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int logoutUser(ComponentName who) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<UserHandle> getSecondaryUsers(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void resetNewUserDisclaimer() throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void enableSystemApp(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int enableSystemAppWithIntent(ComponentName admin, String callerPackage, Intent intent) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean installExistingPackage(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAccountManagementDisabled(ComponentName who, String accountType, boolean disabled, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] getAccountTypesWithManagementDisabled() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] getAccountTypesWithManagementDisabledAsUser(int userId, boolean parent) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSecondaryLockscreenEnabled(ComponentName who, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isSecondaryLockscreenEnabled(UserHandle userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPreferentialNetworkServiceEnabled(boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isPreferentialNetworkServiceEnabled(int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLockTaskPackages(ComponentName who, String[] packages) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String[] getLockTaskPackages(ComponentName who) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isLockTaskPermitted(String pkg) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLockTaskFeatures(ComponentName who, int flags) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getLockTaskFeatures(ComponentName who) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setGlobalSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSystemSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSecureSetting(ComponentName who, String setting, String value) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setConfiguredNetworksLockdownState(ComponentName who, boolean lockdown) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean hasLockdownAdminConfiguredNetworks(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLocationEnabled(ComponentName who, boolean locationEnabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setTime(ComponentName who, long millis) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setTimeZone(ComponentName who, String timeZone) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setMasterVolumeMuted(ComponentName admin, boolean on) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isMasterVolumeMuted(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void notifyLockTaskModeChanged(boolean isEnabled, String pkg, int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUninstallBlocked(ComponentName admin, String callerPackage, String packageName, boolean uninstallBlocked) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUninstallBlocked(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCrossProfileCallerIdDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileCallerIdDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileCallerIdDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCrossProfileContactsSearchDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileContactsSearchDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getCrossProfileContactsSearchDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void startManagedQuickContact(String lookupKey, long contactId, boolean isContactIdIgnored, long directoryId, Intent originalIntent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setBluetoothContactSharingDisabled(ComponentName who, boolean disabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getBluetoothContactSharingDisabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getBluetoothContactSharingDisabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setTrustAgentConfiguration(ComponentName admin, ComponentName agent, PersistableBundle args, boolean parent) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userId, boolean parent) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getCrossProfileWidgetProviders(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAutoTimeRequired(ComponentName who, boolean required) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getAutoTimeRequired() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAutoTimeEnabled(ComponentName who, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getAutoTimeEnabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAutoTimeZoneEnabled(ComponentName who, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getAutoTimeZoneEnabled(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setForceEphemeralUsers(ComponentName who, boolean forceEpehemeralUsers) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getForceEphemeralUsers(ComponentName who) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isRemovingAdmin(ComponentName adminReceiver, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUserIcon(ComponentName admin, Bitmap icon) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSystemUpdatePolicy(ComponentName who, SystemUpdatePolicy policy) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setKeyguardDisabled(ComponentName admin, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setStatusBarDisabled(ComponentName who, boolean disabled) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean getDoNotAskCredentialsOnBoot() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void notifyPendingSystemUpdate(SystemUpdateInfo info) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public SystemUpdateInfo getPendingSystemUpdate(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPermissionPolicy(ComponentName admin, String callerPackage, int policy) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPermissionPolicy(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission, int grantState, RemoteCallback resultReceiver) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isProvisioningAllowed(String action, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int checkProvisioningPreCondition(String action, String packageName) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setKeepUninstalledPackages(ComponentName admin, String callerPackage, List<String> packageList) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getKeepUninstalledPackages(ComponentName admin, String callerPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isManagedProfile(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getWifiMacAddress(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void reboot(ComponentName admin) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setShortSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getShortSupportMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLongSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getLongSupportMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getShortSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getLongSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isSeparateProfileChallengeAllowed(int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOrganizationColor(ComponentName admin, int color) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOrganizationColorForUser(int color, int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getOrganizationColor(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getOrganizationColorForUser(int userHandle) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOrganizationName(ComponentName admin, CharSequence title) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getOrganizationNameForUser(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getUserProvisioningState() throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUserProvisioningState(int state, int userHandle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setAffiliationIds(ComponentName admin, List<String> ids) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getAffiliationIds(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCallingUserAffiliated() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isAffiliatedUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setSecurityLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isSecurityLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ParceledListSlice retrieveSecurityLogs(ComponentName admin, String packageName) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ParceledListSlice retrievePreRebootSecurityLogs(ComponentName admin, String packageName) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long forceNetworkLogs() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long forceSecurityLogs() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUninstallInQueue(String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void uninstallPackageWithActiveAdmins(String packageName) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isDeviceProvisioned() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isDeviceProvisioningConfigApplied() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDeviceProvisioningConfigApplied() throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void forceUpdateUserSetupComplete(int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setBackupServiceEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isBackupServiceEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setNetworkLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isNetworkLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<NetworkEvent> retrieveNetworkLogs(ComponentName admin, String packageName, long batchToken) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean bindDeviceAdminServiceAsUser(ComponentName admin, IApplicationThread caller, IBinder token, Intent service, IServiceConnection connection, int flags, int targetUserId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isEphemeralUser(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getLastSecurityLogRetrievalTime() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getLastBugReportRequestTime() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getLastNetworkLogRetrievalTime() throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setResetPasswordToken(ComponentName admin, byte[] token) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean clearResetPasswordToken(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isResetPasswordTokenActive(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean resetPasswordWithToken(ComponentName admin, String password, byte[] token, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCurrentInputMethodSetByOwner() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public StringParceledListSlice getOwnerInstalledCaCerts(UserHandle user) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void clearApplicationUserData(ComponentName admin, String packageName, IPackageDataObserver callback) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setLogoutEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isLogoutEnabled() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getDisallowedSystemApps(ComponentName admin, int userId, String provisioningAction) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void transferOwnership(ComponentName admin, ComponentName target, PersistableBundle bundle) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public PersistableBundle getTransferOwnershipBundle() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setStartUserSessionMessage(ComponentName admin, CharSequence startUserSessionMessage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setEndUserSessionMessage(ComponentName admin, CharSequence endUserSessionMessage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getStartUserSessionMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public CharSequence getEndUserSessionMessage(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> setMeteredDataDisabledPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getMeteredDataDisabledPackages(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int addOverrideApn(ComponentName admin, ApnSetting apnSetting) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean updateOverrideApn(ComponentName admin, int apnId, ApnSetting apnSetting) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean removeOverrideApn(ComponentName admin, int apnId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<ApnSetting> getOverrideApns(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOverrideApnsEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isOverrideApnEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isMeteredDataDisabledPackageForUser(ComponentName admin, String packageName, int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int setGlobalPrivateDns(ComponentName admin, int mode, String privateDnsHost) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getGlobalPrivateDnsMode(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getGlobalPrivateDnsHost(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void markProfileOwnerOnOrganizationOwnedDevice(ComponentName who, int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void installUpdateFromFile(ComponentName admin, ParcelFileDescriptor updateFileDescriptor, StartInstallingUpdateCallback listener) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCrossProfileCalendarPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getCrossProfileCalendarPackages(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isPackageAllowedToAccessCalendarForUser(String packageName, int userHandle) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getCrossProfileCalendarPackagesForUser(int userHandle) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCrossProfilePackages(ComponentName admin, List<String> packageNames) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getCrossProfilePackages(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getAllCrossProfilePackages() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getDefaultCrossProfilePackages() throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isManagedKiosk() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUnattendedManagedKiosk() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean startViewCalendarEventInManagedProfile(String packageName, long eventId, long start, long end, boolean allDay, int flags) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setKeyGrantForApp(ComponentName admin, String callerPackage, String alias, String packageName, boolean hasGrant) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public ParcelableGranteeMap getKeyPairGrants(String callerPackage, String alias) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean setKeyGrantToWifiAuth(String callerPackage, String alias, boolean hasGrant) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isKeyPairGrantedToWifiAuth(String callerPackage, String alias) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUserControlDisabledPackages(ComponentName admin, List<String> packages) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<String> getUserControlDisabledPackages(ComponentName admin) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setCommonCriteriaModeEnabled(ComponentName admin, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isCommonCriteriaModeEnabled(ComponentName admin) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getPersonalAppsSuspendedReasons(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setPersonalAppsSuspended(ComponentName admin, boolean suspended) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public long getManagedProfileMaximumTimeOff(ComponentName admin) throws RemoteException {
            return 0L;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setManagedProfileMaximumTimeOff(ComponentName admin, long timeoutMs) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void acknowledgeDeviceCompliant() throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isComplianceAcknowledgementRequired() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean canProfileOwnerResetPasswordWhenLocked(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setNextOperationSafety(int operation, int reason) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isSafeOperation(int reason) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public String getEnrollmentSpecificId(String callerPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setOrganizationIdForUser(String callerPackage, String enterpriseId, int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public UserHandle createAndProvisionManagedProfile(ManagedProfileProvisioningParams provisioningParams, String callerPackage) throws RemoteException {
            return null;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void provisionFullyManagedDevice(FullyManagedDeviceProvisioningParams provisioningParams, String callerPackage) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setDeviceOwnerType(ComponentName admin, int deviceOwnerType) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public int getDeviceOwnerType(ComponentName admin) throws RemoteException {
            return 0;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void resetDefaultCrossProfileIntentFilters(int userId) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean canAdminGrantSensorsPermissionsForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public void setUsbDataSignalingEnabled(String callerPackage, boolean enabled) throws RemoteException {
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUsbDataSignalingEnabled(String callerPackage) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean isUsbDataSignalingEnabledForUser(int userId) throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public boolean canUsbDataSignalingBeDisabled() throws RemoteException {
            return false;
        }

        @Override // android.app.admin.IDevicePolicyManager
        public List<UserHandle> listForegroundAffiliatedUsers() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDevicePolicyManager {
        public static final String DESCRIPTOR = "android.app.admin.IDevicePolicyManager";
        static final int TRANSACTION_acknowledgeDeviceCompliant = 327;
        static final int TRANSACTION_addCrossProfileIntentFilter = 133;
        static final int TRANSACTION_addCrossProfileWidgetProvider = 198;
        static final int TRANSACTION_addOverrideApn = 292;
        static final int TRANSACTION_addPersistentPreferredActivity = 121;
        static final int TRANSACTION_approveCaCert = 102;
        static final int TRANSACTION_bindDeviceAdminServiceAsUser = 268;
        static final int TRANSACTION_canAdminGrantSensorsPermissionsForUser = 339;
        static final int TRANSACTION_canProfileOwnerResetPasswordWhenLocked = 329;
        static final int TRANSACTION_canUsbDataSignalingBeDisabled = 343;
        static final int TRANSACTION_checkDeviceIdentifierAccess = 93;
        static final int TRANSACTION_checkProvisioningPreCondition = 224;
        static final int TRANSACTION_choosePrivateKeyAlias = 109;
        static final int TRANSACTION_clearApplicationUserData = 280;
        static final int TRANSACTION_clearCrossProfileIntentFilters = 134;
        static final int TRANSACTION_clearDeviceOwner = 82;
        static final int TRANSACTION_clearPackagePersistentPreferredActivities = 122;
        static final int TRANSACTION_clearProfileOwner = 90;
        static final int TRANSACTION_clearResetPasswordToken = 275;
        static final int TRANSACTION_clearSystemUpdatePolicyFreezePeriodRecord = 213;
        static final int TRANSACTION_createAdminSupportIntent = 146;
        static final int TRANSACTION_createAndManageUser = 150;
        static final int TRANSACTION_createAndProvisionManagedProfile = 334;
        static final int TRANSACTION_enableSystemApp = 158;
        static final int TRANSACTION_enableSystemAppWithIntent = 159;
        static final int TRANSACTION_enforceCanManageCaCerts = 101;
        static final int TRANSACTION_finalizeWorkProfileProvisioning = 33;
        static final int TRANSACTION_forceNetworkLogs = 255;
        static final int TRANSACTION_forceRemoveActiveAdmin = 69;
        static final int TRANSACTION_forceSecurityLogs = 256;
        static final int TRANSACTION_forceUpdateUserSetupComplete = 262;
        static final int TRANSACTION_generateKeyPair = 107;
        static final int TRANSACTION_getAccountTypesWithManagementDisabled = 162;
        static final int TRANSACTION_getAccountTypesWithManagementDisabledAsUser = 163;
        static final int TRANSACTION_getActiveAdmins = 65;
        static final int TRANSACTION_getAffiliationIds = 248;
        static final int TRANSACTION_getAggregatedPasswordComplexityForUser = 29;
        static final int TRANSACTION_getAllCrossProfilePackages = 310;
        static final int TRANSACTION_getAlwaysOnVpnLockdownAllowlist = 120;
        static final int TRANSACTION_getAlwaysOnVpnPackage = 116;
        static final int TRANSACTION_getAlwaysOnVpnPackageForUser = 117;
        static final int TRANSACTION_getApplicationRestrictions = 125;
        static final int TRANSACTION_getApplicationRestrictionsManagingPackage = 127;
        static final int TRANSACTION_getAutoTimeEnabled = 204;
        static final int TRANSACTION_getAutoTimeRequired = 202;
        static final int TRANSACTION_getAutoTimeZoneEnabled = 206;
        static final int TRANSACTION_getBindDeviceAdminTargetUsers = 269;
        static final int TRANSACTION_getBluetoothContactSharingDisabled = 194;
        static final int TRANSACTION_getBluetoothContactSharingDisabledForUser = 195;
        static final int TRANSACTION_getCameraDisabled = 54;
        static final int TRANSACTION_getCertInstallerPackage = 114;
        static final int TRANSACTION_getCrossProfileCalendarPackages = 305;
        static final int TRANSACTION_getCrossProfileCalendarPackagesForUser = 307;
        static final int TRANSACTION_getCrossProfileCallerIdDisabled = 187;
        static final int TRANSACTION_getCrossProfileCallerIdDisabledForUser = 188;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabled = 190;
        static final int TRANSACTION_getCrossProfileContactsSearchDisabledForUser = 191;
        static final int TRANSACTION_getCrossProfilePackages = 309;
        static final int TRANSACTION_getCrossProfileWidgetProviders = 200;
        static final int TRANSACTION_getCurrentFailedPasswordAttempts = 31;
        static final int TRANSACTION_getDefaultCrossProfilePackages = 311;
        static final int TRANSACTION_getDelegatePackages = 112;
        static final int TRANSACTION_getDelegatedScopes = 111;
        static final int TRANSACTION_getDeviceOwnerComponent = 79;
        static final int TRANSACTION_getDeviceOwnerLockScreenInfo = 95;
        static final int TRANSACTION_getDeviceOwnerName = 81;
        static final int TRANSACTION_getDeviceOwnerOrganizationName = 243;
        static final int TRANSACTION_getDeviceOwnerType = 337;
        static final int TRANSACTION_getDeviceOwnerUserId = 83;
        static final int TRANSACTION_getDisallowedSystemApps = 283;
        static final int TRANSACTION_getDoNotAskCredentialsOnBoot = 216;
        static final int TRANSACTION_getEndUserSessionMessage = 289;
        static final int TRANSACTION_getEnforcingAdminAndUserDetails = 147;
        static final int TRANSACTION_getEnrollmentSpecificId = 332;
        static final int TRANSACTION_getFactoryResetProtectionPolicy = 44;
        static final int TRANSACTION_getForceEphemeralUsers = 208;
        static final int TRANSACTION_getGlobalPrivateDnsHost = 301;
        static final int TRANSACTION_getGlobalPrivateDnsMode = 300;
        static final int TRANSACTION_getGlobalProxyAdmin = 47;
        static final int TRANSACTION_getKeepUninstalledPackages = 226;
        static final int TRANSACTION_getKeyPairGrants = 316;
        static final int TRANSACTION_getKeyguardDisabledFeatures = 62;
        static final int TRANSACTION_getLastBugReportRequestTime = 272;
        static final int TRANSACTION_getLastNetworkLogRetrievalTime = 273;
        static final int TRANSACTION_getLastSecurityLogRetrievalTime = 271;
        static final int TRANSACTION_getLockTaskFeatures = 172;
        static final int TRANSACTION_getLockTaskPackages = 169;
        static final int TRANSACTION_getLongSupportMessage = 233;
        static final int TRANSACTION_getLongSupportMessageForUser = 235;
        static final int TRANSACTION_getManagedProfileMaximumTimeOff = 325;
        static final int TRANSACTION_getMaximumFailedPasswordsForWipe = 35;
        static final int TRANSACTION_getMaximumTimeToLock = 38;
        static final int TRANSACTION_getMeteredDataDisabledPackages = 291;
        static final int TRANSACTION_getNearbyAppStreamingPolicy = 60;
        static final int TRANSACTION_getNearbyNotificationStreamingPolicy = 58;
        static final int TRANSACTION_getOrganizationColor = 239;
        static final int TRANSACTION_getOrganizationColorForUser = 240;
        static final int TRANSACTION_getOrganizationName = 242;
        static final int TRANSACTION_getOrganizationNameForUser = 244;
        static final int TRANSACTION_getOverrideApns = 295;
        static final int TRANSACTION_getOwnerInstalledCaCerts = 279;
        static final int TRANSACTION_getPasswordComplexity = 26;
        static final int TRANSACTION_getPasswordExpiration = 22;
        static final int TRANSACTION_getPasswordExpirationTimeout = 21;
        static final int TRANSACTION_getPasswordHistoryLength = 19;
        static final int TRANSACTION_getPasswordMinimumLength = 4;
        static final int TRANSACTION_getPasswordMinimumLetters = 10;
        static final int TRANSACTION_getPasswordMinimumLowerCase = 8;
        static final int TRANSACTION_getPasswordMinimumMetrics = 17;
        static final int TRANSACTION_getPasswordMinimumNonLetter = 16;
        static final int TRANSACTION_getPasswordMinimumNumeric = 12;
        static final int TRANSACTION_getPasswordMinimumSymbols = 14;
        static final int TRANSACTION_getPasswordMinimumUpperCase = 6;
        static final int TRANSACTION_getPasswordQuality = 2;
        static final int TRANSACTION_getPendingSystemUpdate = 218;
        static final int TRANSACTION_getPermissionGrantState = 222;
        static final int TRANSACTION_getPermissionPolicy = 220;
        static final int TRANSACTION_getPermittedAccessibilityServices = 136;
        static final int TRANSACTION_getPermittedAccessibilityServicesForUser = 137;
        static final int TRANSACTION_getPermittedCrossProfileNotificationListeners = 144;
        static final int TRANSACTION_getPermittedInputMethods = 140;
        static final int TRANSACTION_getPermittedInputMethodsForCurrentUser = 141;
        static final int TRANSACTION_getPersonalAppsSuspendedReasons = 323;
        static final int TRANSACTION_getProfileOwnerAsUser = 85;
        static final int TRANSACTION_getProfileOwnerName = 87;
        static final int TRANSACTION_getProfileOwnerOrDeviceOwnerSupervisionComponent = 86;
        static final int TRANSACTION_getProfileWithMinimumFailedPasswordsForWipe = 32;
        static final int TRANSACTION_getRemoveWarning = 67;
        static final int TRANSACTION_getRequiredPasswordComplexity = 28;
        static final int TRANSACTION_getRequiredStrongAuthTimeout = 40;
        static final int TRANSACTION_getRestrictionsProvider = 130;
        static final int TRANSACTION_getScreenCaptureDisabled = 56;
        static final int TRANSACTION_getSecondaryUsers = 156;
        static final int TRANSACTION_getShortSupportMessage = 231;
        static final int TRANSACTION_getShortSupportMessageForUser = 234;
        static final int TRANSACTION_getStartUserSessionMessage = 288;
        static final int TRANSACTION_getStorageEncryption = 50;
        static final int TRANSACTION_getStorageEncryptionStatus = 51;
        static final int TRANSACTION_getSystemUpdatePolicy = 212;
        static final int TRANSACTION_getTransferOwnershipBundle = 285;
        static final int TRANSACTION_getTrustAgentConfiguration = 197;
        static final int TRANSACTION_getUserControlDisabledPackages = 320;
        static final int TRANSACTION_getUserProvisioningState = 245;
        static final int TRANSACTION_getUserRestrictions = 132;
        static final int TRANSACTION_getWifiMacAddress = 228;
        static final int TRANSACTION_hasDeviceOwner = 80;
        static final int TRANSACTION_hasGrantedPolicy = 70;
        static final int TRANSACTION_hasKeyPair = 106;
        static final int TRANSACTION_hasLockdownAdminConfiguredNetworks = 177;
        static final int TRANSACTION_hasUserSetupCompleted = 91;
        static final int TRANSACTION_installCaCert = 99;
        static final int TRANSACTION_installExistingPackage = 160;
        static final int TRANSACTION_installKeyPair = 104;
        static final int TRANSACTION_installUpdateFromFile = 303;
        static final int TRANSACTION_isAccessibilityServicePermittedByAdmin = 138;
        static final int TRANSACTION_isActivePasswordSufficient = 23;
        static final int TRANSACTION_isActivePasswordSufficientForDeviceRequirement = 24;
        static final int TRANSACTION_isAdminActive = 64;
        static final int TRANSACTION_isAffiliatedUser = 250;
        static final int TRANSACTION_isAlwaysOnVpnLockdownEnabled = 118;
        static final int TRANSACTION_isAlwaysOnVpnLockdownEnabledForUser = 119;
        static final int TRANSACTION_isApplicationHidden = 149;
        static final int TRANSACTION_isBackupServiceEnabled = 264;
        static final int TRANSACTION_isCaCertApproved = 103;
        static final int TRANSACTION_isCallerApplicationRestrictionsManagingPackage = 128;
        static final int TRANSACTION_isCallingUserAffiliated = 249;
        static final int TRANSACTION_isCommonCriteriaModeEnabled = 322;
        static final int TRANSACTION_isComplianceAcknowledgementRequired = 328;
        static final int TRANSACTION_isCurrentInputMethodSetByOwner = 278;
        static final int TRANSACTION_isDeviceProvisioned = 259;
        static final int TRANSACTION_isDeviceProvisioningConfigApplied = 260;
        static final int TRANSACTION_isEphemeralUser = 270;
        static final int TRANSACTION_isFactoryResetProtectionPolicySupported = 45;
        static final int TRANSACTION_isInputMethodPermittedByAdmin = 142;
        static final int TRANSACTION_isKeyPairGrantedToWifiAuth = 318;
        static final int TRANSACTION_isLockTaskPermitted = 170;
        static final int TRANSACTION_isLogoutEnabled = 282;
        static final int TRANSACTION_isManagedKiosk = 312;
        static final int TRANSACTION_isManagedProfile = 227;
        static final int TRANSACTION_isMasterVolumeMuted = 182;
        static final int TRANSACTION_isMeteredDataDisabledPackageForUser = 298;
        static final int TRANSACTION_isNetworkLoggingEnabled = 266;
        static final int TRANSACTION_isNotificationListenerServicePermitted = 145;
        static final int TRANSACTION_isOrganizationOwnedDeviceWithManagedProfile = 92;
        static final int TRANSACTION_isOverrideApnEnabled = 297;
        static final int TRANSACTION_isPackageAllowedToAccessCalendarForUser = 306;
        static final int TRANSACTION_isPackageSuspended = 97;
        static final int TRANSACTION_isPasswordSufficientAfterProfileUnification = 25;
        static final int TRANSACTION_isPreferentialNetworkServiceEnabled = 167;
        static final int TRANSACTION_isProvisioningAllowed = 223;
        static final int TRANSACTION_isRemovingAdmin = 209;
        static final int TRANSACTION_isResetPasswordTokenActive = 276;
        static final int TRANSACTION_isSafeOperation = 331;
        static final int TRANSACTION_isSecondaryLockscreenEnabled = 165;
        static final int TRANSACTION_isSecurityLoggingEnabled = 252;
        static final int TRANSACTION_isSeparateProfileChallengeAllowed = 236;
        static final int TRANSACTION_isUnattendedManagedKiosk = 313;
        static final int TRANSACTION_isUninstallBlocked = 185;
        static final int TRANSACTION_isUninstallInQueue = 257;
        static final int TRANSACTION_isUsbDataSignalingEnabled = 341;
        static final int TRANSACTION_isUsbDataSignalingEnabledForUser = 342;
        static final int TRANSACTION_isUsingUnifiedPassword = 30;
        static final int TRANSACTION_listForegroundAffiliatedUsers = 344;
        static final int TRANSACTION_listPolicyExemptApps = 98;
        static final int TRANSACTION_lockNow = 41;
        static final int TRANSACTION_logoutUser = 155;
        static final int TRANSACTION_markProfileOwnerOnOrganizationOwnedDevice = 302;
        static final int TRANSACTION_notifyLockTaskModeChanged = 183;
        static final int TRANSACTION_notifyPendingSystemUpdate = 217;
        static final int TRANSACTION_packageHasActiveAdmins = 66;
        static final int TRANSACTION_provisionFullyManagedDevice = 335;
        static final int TRANSACTION_reboot = 229;
        static final int TRANSACTION_removeActiveAdmin = 68;
        static final int TRANSACTION_removeCrossProfileWidgetProvider = 199;
        static final int TRANSACTION_removeKeyPair = 105;
        static final int TRANSACTION_removeOverrideApn = 294;
        static final int TRANSACTION_removeUser = 151;
        static final int TRANSACTION_reportFailedBiometricAttempt = 74;
        static final int TRANSACTION_reportFailedPasswordAttempt = 72;
        static final int TRANSACTION_reportKeyguardDismissed = 76;
        static final int TRANSACTION_reportKeyguardSecured = 77;
        static final int TRANSACTION_reportPasswordChanged = 71;
        static final int TRANSACTION_reportSuccessfulBiometricAttempt = 75;
        static final int TRANSACTION_reportSuccessfulPasswordAttempt = 73;
        static final int TRANSACTION_requestBugreport = 52;
        static final int TRANSACTION_resetDefaultCrossProfileIntentFilters = 338;
        static final int TRANSACTION_resetNewUserDisclaimer = 157;
        static final int TRANSACTION_resetPassword = 36;
        static final int TRANSACTION_resetPasswordWithToken = 277;
        static final int TRANSACTION_retrieveNetworkLogs = 267;
        static final int TRANSACTION_retrievePreRebootSecurityLogs = 254;
        static final int TRANSACTION_retrieveSecurityLogs = 253;
        static final int TRANSACTION_setAccountManagementDisabled = 161;
        static final int TRANSACTION_setActiveAdmin = 63;
        static final int TRANSACTION_setAffiliationIds = 247;
        static final int TRANSACTION_setAlwaysOnVpnPackage = 115;
        static final int TRANSACTION_setApplicationHidden = 148;
        static final int TRANSACTION_setApplicationRestrictions = 124;
        static final int TRANSACTION_setApplicationRestrictionsManagingPackage = 126;
        static final int TRANSACTION_setAutoTimeEnabled = 203;
        static final int TRANSACTION_setAutoTimeRequired = 201;
        static final int TRANSACTION_setAutoTimeZoneEnabled = 205;
        static final int TRANSACTION_setBackupServiceEnabled = 263;
        static final int TRANSACTION_setBluetoothContactSharingDisabled = 193;
        static final int TRANSACTION_setCameraDisabled = 53;
        static final int TRANSACTION_setCertInstallerPackage = 113;
        static final int TRANSACTION_setCommonCriteriaModeEnabled = 321;
        static final int TRANSACTION_setConfiguredNetworksLockdownState = 176;
        static final int TRANSACTION_setCrossProfileCalendarPackages = 304;
        static final int TRANSACTION_setCrossProfileCallerIdDisabled = 186;
        static final int TRANSACTION_setCrossProfileContactsSearchDisabled = 189;
        static final int TRANSACTION_setCrossProfilePackages = 308;
        static final int TRANSACTION_setDefaultSmsApplication = 123;
        static final int TRANSACTION_setDelegatedScopes = 110;
        static final int TRANSACTION_setDeviceOwner = 78;
        static final int TRANSACTION_setDeviceOwnerLockScreenInfo = 94;
        static final int TRANSACTION_setDeviceOwnerType = 336;
        static final int TRANSACTION_setDeviceProvisioningConfigApplied = 261;
        static final int TRANSACTION_setEndUserSessionMessage = 287;
        static final int TRANSACTION_setFactoryResetProtectionPolicy = 43;
        static final int TRANSACTION_setForceEphemeralUsers = 207;
        static final int TRANSACTION_setGlobalPrivateDns = 299;
        static final int TRANSACTION_setGlobalProxy = 46;
        static final int TRANSACTION_setGlobalSetting = 173;
        static final int TRANSACTION_setKeepUninstalledPackages = 225;
        static final int TRANSACTION_setKeyGrantForApp = 315;
        static final int TRANSACTION_setKeyGrantToWifiAuth = 317;
        static final int TRANSACTION_setKeyPairCertificate = 108;
        static final int TRANSACTION_setKeyguardDisabled = 214;
        static final int TRANSACTION_setKeyguardDisabledFeatures = 61;
        static final int TRANSACTION_setLocationEnabled = 178;
        static final int TRANSACTION_setLockTaskFeatures = 171;
        static final int TRANSACTION_setLockTaskPackages = 168;
        static final int TRANSACTION_setLogoutEnabled = 281;
        static final int TRANSACTION_setLongSupportMessage = 232;
        static final int TRANSACTION_setManagedProfileMaximumTimeOff = 326;
        static final int TRANSACTION_setMasterVolumeMuted = 181;
        static final int TRANSACTION_setMaximumFailedPasswordsForWipe = 34;
        static final int TRANSACTION_setMaximumTimeToLock = 37;
        static final int TRANSACTION_setMeteredDataDisabledPackages = 290;
        static final int TRANSACTION_setNearbyAppStreamingPolicy = 59;
        static final int TRANSACTION_setNearbyNotificationStreamingPolicy = 57;
        static final int TRANSACTION_setNetworkLoggingEnabled = 265;
        static final int TRANSACTION_setNextOperationSafety = 330;
        static final int TRANSACTION_setOrganizationColor = 237;
        static final int TRANSACTION_setOrganizationColorForUser = 238;
        static final int TRANSACTION_setOrganizationIdForUser = 333;
        static final int TRANSACTION_setOrganizationName = 241;
        static final int TRANSACTION_setOverrideApnsEnabled = 296;
        static final int TRANSACTION_setPackagesSuspended = 96;
        static final int TRANSACTION_setPasswordExpirationTimeout = 20;
        static final int TRANSACTION_setPasswordHistoryLength = 18;
        static final int TRANSACTION_setPasswordMinimumLength = 3;
        static final int TRANSACTION_setPasswordMinimumLetters = 9;
        static final int TRANSACTION_setPasswordMinimumLowerCase = 7;
        static final int TRANSACTION_setPasswordMinimumNonLetter = 15;
        static final int TRANSACTION_setPasswordMinimumNumeric = 11;
        static final int TRANSACTION_setPasswordMinimumSymbols = 13;
        static final int TRANSACTION_setPasswordMinimumUpperCase = 5;
        static final int TRANSACTION_setPasswordQuality = 1;
        static final int TRANSACTION_setPermissionGrantState = 221;
        static final int TRANSACTION_setPermissionPolicy = 219;
        static final int TRANSACTION_setPermittedAccessibilityServices = 135;
        static final int TRANSACTION_setPermittedCrossProfileNotificationListeners = 143;
        static final int TRANSACTION_setPermittedInputMethods = 139;
        static final int TRANSACTION_setPersonalAppsSuspended = 324;
        static final int TRANSACTION_setPreferentialNetworkServiceEnabled = 166;
        static final int TRANSACTION_setProfileEnabled = 88;
        static final int TRANSACTION_setProfileName = 89;
        static final int TRANSACTION_setProfileOwner = 84;
        static final int TRANSACTION_setRecommendedGlobalProxy = 48;
        static final int TRANSACTION_setRequiredPasswordComplexity = 27;
        static final int TRANSACTION_setRequiredStrongAuthTimeout = 39;
        static final int TRANSACTION_setResetPasswordToken = 274;
        static final int TRANSACTION_setRestrictionsProvider = 129;
        static final int TRANSACTION_setScreenCaptureDisabled = 55;
        static final int TRANSACTION_setSecondaryLockscreenEnabled = 164;
        static final int TRANSACTION_setSecureSetting = 175;
        static final int TRANSACTION_setSecurityLoggingEnabled = 251;
        static final int TRANSACTION_setShortSupportMessage = 230;
        static final int TRANSACTION_setStartUserSessionMessage = 286;
        static final int TRANSACTION_setStatusBarDisabled = 215;
        static final int TRANSACTION_setStorageEncryption = 49;
        static final int TRANSACTION_setSystemSetting = 174;
        static final int TRANSACTION_setSystemUpdatePolicy = 211;
        static final int TRANSACTION_setTime = 179;
        static final int TRANSACTION_setTimeZone = 180;
        static final int TRANSACTION_setTrustAgentConfiguration = 196;
        static final int TRANSACTION_setUninstallBlocked = 184;
        static final int TRANSACTION_setUsbDataSignalingEnabled = 340;
        static final int TRANSACTION_setUserControlDisabledPackages = 319;
        static final int TRANSACTION_setUserIcon = 210;
        static final int TRANSACTION_setUserProvisioningState = 246;
        static final int TRANSACTION_setUserRestriction = 131;
        static final int TRANSACTION_startManagedQuickContact = 192;
        static final int TRANSACTION_startUserInBackground = 153;
        static final int TRANSACTION_startViewCalendarEventInManagedProfile = 314;
        static final int TRANSACTION_stopUser = 154;
        static final int TRANSACTION_switchUser = 152;
        static final int TRANSACTION_transferOwnership = 284;
        static final int TRANSACTION_uninstallCaCerts = 100;
        static final int TRANSACTION_uninstallPackageWithActiveAdmins = 258;
        static final int TRANSACTION_updateOverrideApn = 293;
        static final int TRANSACTION_wipeDataWithReason = 42;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDevicePolicyManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDevicePolicyManager)) {
                return (IDevicePolicyManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setPasswordQuality";
                case 2:
                    return "getPasswordQuality";
                case 3:
                    return "setPasswordMinimumLength";
                case 4:
                    return "getPasswordMinimumLength";
                case 5:
                    return "setPasswordMinimumUpperCase";
                case 6:
                    return "getPasswordMinimumUpperCase";
                case 7:
                    return "setPasswordMinimumLowerCase";
                case 8:
                    return "getPasswordMinimumLowerCase";
                case 9:
                    return "setPasswordMinimumLetters";
                case 10:
                    return "getPasswordMinimumLetters";
                case 11:
                    return "setPasswordMinimumNumeric";
                case 12:
                    return "getPasswordMinimumNumeric";
                case 13:
                    return "setPasswordMinimumSymbols";
                case 14:
                    return "getPasswordMinimumSymbols";
                case 15:
                    return "setPasswordMinimumNonLetter";
                case 16:
                    return "getPasswordMinimumNonLetter";
                case 17:
                    return "getPasswordMinimumMetrics";
                case 18:
                    return "setPasswordHistoryLength";
                case 19:
                    return "getPasswordHistoryLength";
                case 20:
                    return "setPasswordExpirationTimeout";
                case 21:
                    return "getPasswordExpirationTimeout";
                case 22:
                    return "getPasswordExpiration";
                case 23:
                    return "isActivePasswordSufficient";
                case 24:
                    return "isActivePasswordSufficientForDeviceRequirement";
                case 25:
                    return "isPasswordSufficientAfterProfileUnification";
                case 26:
                    return "getPasswordComplexity";
                case 27:
                    return "setRequiredPasswordComplexity";
                case 28:
                    return "getRequiredPasswordComplexity";
                case 29:
                    return "getAggregatedPasswordComplexityForUser";
                case 30:
                    return "isUsingUnifiedPassword";
                case 31:
                    return "getCurrentFailedPasswordAttempts";
                case 32:
                    return "getProfileWithMinimumFailedPasswordsForWipe";
                case 33:
                    return "finalizeWorkProfileProvisioning";
                case 34:
                    return "setMaximumFailedPasswordsForWipe";
                case 35:
                    return "getMaximumFailedPasswordsForWipe";
                case 36:
                    return "resetPassword";
                case 37:
                    return "setMaximumTimeToLock";
                case 38:
                    return "getMaximumTimeToLock";
                case 39:
                    return "setRequiredStrongAuthTimeout";
                case 40:
                    return "getRequiredStrongAuthTimeout";
                case 41:
                    return "lockNow";
                case 42:
                    return "wipeDataWithReason";
                case 43:
                    return "setFactoryResetProtectionPolicy";
                case 44:
                    return "getFactoryResetProtectionPolicy";
                case 45:
                    return "isFactoryResetProtectionPolicySupported";
                case 46:
                    return "setGlobalProxy";
                case 47:
                    return "getGlobalProxyAdmin";
                case 48:
                    return "setRecommendedGlobalProxy";
                case 49:
                    return "setStorageEncryption";
                case 50:
                    return "getStorageEncryption";
                case 51:
                    return "getStorageEncryptionStatus";
                case 52:
                    return "requestBugreport";
                case 53:
                    return "setCameraDisabled";
                case 54:
                    return "getCameraDisabled";
                case 55:
                    return "setScreenCaptureDisabled";
                case 56:
                    return "getScreenCaptureDisabled";
                case 57:
                    return "setNearbyNotificationStreamingPolicy";
                case 58:
                    return "getNearbyNotificationStreamingPolicy";
                case 59:
                    return "setNearbyAppStreamingPolicy";
                case 60:
                    return "getNearbyAppStreamingPolicy";
                case 61:
                    return "setKeyguardDisabledFeatures";
                case 62:
                    return "getKeyguardDisabledFeatures";
                case 63:
                    return "setActiveAdmin";
                case 64:
                    return "isAdminActive";
                case 65:
                    return "getActiveAdmins";
                case 66:
                    return "packageHasActiveAdmins";
                case 67:
                    return "getRemoveWarning";
                case 68:
                    return "removeActiveAdmin";
                case 69:
                    return "forceRemoveActiveAdmin";
                case 70:
                    return "hasGrantedPolicy";
                case 71:
                    return "reportPasswordChanged";
                case 72:
                    return "reportFailedPasswordAttempt";
                case 73:
                    return "reportSuccessfulPasswordAttempt";
                case 74:
                    return "reportFailedBiometricAttempt";
                case 75:
                    return "reportSuccessfulBiometricAttempt";
                case 76:
                    return "reportKeyguardDismissed";
                case 77:
                    return "reportKeyguardSecured";
                case 78:
                    return "setDeviceOwner";
                case 79:
                    return "getDeviceOwnerComponent";
                case 80:
                    return "hasDeviceOwner";
                case 81:
                    return "getDeviceOwnerName";
                case 82:
                    return "clearDeviceOwner";
                case 83:
                    return "getDeviceOwnerUserId";
                case 84:
                    return "setProfileOwner";
                case 85:
                    return "getProfileOwnerAsUser";
                case 86:
                    return "getProfileOwnerOrDeviceOwnerSupervisionComponent";
                case 87:
                    return "getProfileOwnerName";
                case 88:
                    return "setProfileEnabled";
                case 89:
                    return "setProfileName";
                case 90:
                    return "clearProfileOwner";
                case 91:
                    return "hasUserSetupCompleted";
                case 92:
                    return "isOrganizationOwnedDeviceWithManagedProfile";
                case 93:
                    return "checkDeviceIdentifierAccess";
                case 94:
                    return "setDeviceOwnerLockScreenInfo";
                case 95:
                    return "getDeviceOwnerLockScreenInfo";
                case 96:
                    return "setPackagesSuspended";
                case 97:
                    return "isPackageSuspended";
                case 98:
                    return "listPolicyExemptApps";
                case 99:
                    return "installCaCert";
                case 100:
                    return "uninstallCaCerts";
                case 101:
                    return "enforceCanManageCaCerts";
                case 102:
                    return "approveCaCert";
                case 103:
                    return "isCaCertApproved";
                case 104:
                    return "installKeyPair";
                case 105:
                    return "removeKeyPair";
                case 106:
                    return "hasKeyPair";
                case 107:
                    return "generateKeyPair";
                case 108:
                    return "setKeyPairCertificate";
                case 109:
                    return "choosePrivateKeyAlias";
                case 110:
                    return "setDelegatedScopes";
                case 111:
                    return "getDelegatedScopes";
                case 112:
                    return "getDelegatePackages";
                case 113:
                    return "setCertInstallerPackage";
                case 114:
                    return "getCertInstallerPackage";
                case 115:
                    return "setAlwaysOnVpnPackage";
                case 116:
                    return "getAlwaysOnVpnPackage";
                case 117:
                    return "getAlwaysOnVpnPackageForUser";
                case 118:
                    return "isAlwaysOnVpnLockdownEnabled";
                case 119:
                    return "isAlwaysOnVpnLockdownEnabledForUser";
                case 120:
                    return "getAlwaysOnVpnLockdownAllowlist";
                case 121:
                    return "addPersistentPreferredActivity";
                case 122:
                    return "clearPackagePersistentPreferredActivities";
                case 123:
                    return "setDefaultSmsApplication";
                case 124:
                    return "setApplicationRestrictions";
                case 125:
                    return "getApplicationRestrictions";
                case 126:
                    return "setApplicationRestrictionsManagingPackage";
                case 127:
                    return "getApplicationRestrictionsManagingPackage";
                case 128:
                    return "isCallerApplicationRestrictionsManagingPackage";
                case 129:
                    return "setRestrictionsProvider";
                case 130:
                    return "getRestrictionsProvider";
                case 131:
                    return "setUserRestriction";
                case 132:
                    return "getUserRestrictions";
                case 133:
                    return "addCrossProfileIntentFilter";
                case 134:
                    return "clearCrossProfileIntentFilters";
                case 135:
                    return "setPermittedAccessibilityServices";
                case 136:
                    return "getPermittedAccessibilityServices";
                case 137:
                    return "getPermittedAccessibilityServicesForUser";
                case 138:
                    return "isAccessibilityServicePermittedByAdmin";
                case 139:
                    return "setPermittedInputMethods";
                case 140:
                    return "getPermittedInputMethods";
                case 141:
                    return "getPermittedInputMethodsForCurrentUser";
                case 142:
                    return "isInputMethodPermittedByAdmin";
                case 143:
                    return "setPermittedCrossProfileNotificationListeners";
                case 144:
                    return "getPermittedCrossProfileNotificationListeners";
                case 145:
                    return "isNotificationListenerServicePermitted";
                case 146:
                    return "createAdminSupportIntent";
                case 147:
                    return "getEnforcingAdminAndUserDetails";
                case 148:
                    return "setApplicationHidden";
                case 149:
                    return "isApplicationHidden";
                case 150:
                    return "createAndManageUser";
                case 151:
                    return "removeUser";
                case 152:
                    return "switchUser";
                case 153:
                    return "startUserInBackground";
                case 154:
                    return "stopUser";
                case 155:
                    return "logoutUser";
                case 156:
                    return "getSecondaryUsers";
                case 157:
                    return "resetNewUserDisclaimer";
                case 158:
                    return "enableSystemApp";
                case 159:
                    return "enableSystemAppWithIntent";
                case 160:
                    return "installExistingPackage";
                case 161:
                    return "setAccountManagementDisabled";
                case 162:
                    return "getAccountTypesWithManagementDisabled";
                case 163:
                    return "getAccountTypesWithManagementDisabledAsUser";
                case 164:
                    return "setSecondaryLockscreenEnabled";
                case 165:
                    return "isSecondaryLockscreenEnabled";
                case 166:
                    return "setPreferentialNetworkServiceEnabled";
                case 167:
                    return "isPreferentialNetworkServiceEnabled";
                case 168:
                    return "setLockTaskPackages";
                case 169:
                    return "getLockTaskPackages";
                case 170:
                    return "isLockTaskPermitted";
                case 171:
                    return "setLockTaskFeatures";
                case 172:
                    return "getLockTaskFeatures";
                case 173:
                    return "setGlobalSetting";
                case 174:
                    return "setSystemSetting";
                case 175:
                    return "setSecureSetting";
                case 176:
                    return "setConfiguredNetworksLockdownState";
                case 177:
                    return "hasLockdownAdminConfiguredNetworks";
                case 178:
                    return "setLocationEnabled";
                case 179:
                    return "setTime";
                case 180:
                    return "setTimeZone";
                case 181:
                    return "setMasterVolumeMuted";
                case 182:
                    return "isMasterVolumeMuted";
                case 183:
                    return "notifyLockTaskModeChanged";
                case 184:
                    return "setUninstallBlocked";
                case 185:
                    return "isUninstallBlocked";
                case 186:
                    return "setCrossProfileCallerIdDisabled";
                case 187:
                    return "getCrossProfileCallerIdDisabled";
                case 188:
                    return "getCrossProfileCallerIdDisabledForUser";
                case 189:
                    return "setCrossProfileContactsSearchDisabled";
                case 190:
                    return "getCrossProfileContactsSearchDisabled";
                case 191:
                    return "getCrossProfileContactsSearchDisabledForUser";
                case 192:
                    return "startManagedQuickContact";
                case 193:
                    return "setBluetoothContactSharingDisabled";
                case 194:
                    return "getBluetoothContactSharingDisabled";
                case 195:
                    return "getBluetoothContactSharingDisabledForUser";
                case 196:
                    return "setTrustAgentConfiguration";
                case 197:
                    return "getTrustAgentConfiguration";
                case 198:
                    return "addCrossProfileWidgetProvider";
                case 199:
                    return "removeCrossProfileWidgetProvider";
                case 200:
                    return "getCrossProfileWidgetProviders";
                case 201:
                    return "setAutoTimeRequired";
                case 202:
                    return "getAutoTimeRequired";
                case 203:
                    return "setAutoTimeEnabled";
                case 204:
                    return "getAutoTimeEnabled";
                case 205:
                    return "setAutoTimeZoneEnabled";
                case 206:
                    return "getAutoTimeZoneEnabled";
                case 207:
                    return "setForceEphemeralUsers";
                case 208:
                    return "getForceEphemeralUsers";
                case 209:
                    return "isRemovingAdmin";
                case 210:
                    return "setUserIcon";
                case 211:
                    return "setSystemUpdatePolicy";
                case 212:
                    return "getSystemUpdatePolicy";
                case 213:
                    return "clearSystemUpdatePolicyFreezePeriodRecord";
                case 214:
                    return "setKeyguardDisabled";
                case 215:
                    return "setStatusBarDisabled";
                case 216:
                    return "getDoNotAskCredentialsOnBoot";
                case 217:
                    return "notifyPendingSystemUpdate";
                case 218:
                    return "getPendingSystemUpdate";
                case 219:
                    return "setPermissionPolicy";
                case 220:
                    return "getPermissionPolicy";
                case 221:
                    return "setPermissionGrantState";
                case 222:
                    return "getPermissionGrantState";
                case 223:
                    return "isProvisioningAllowed";
                case 224:
                    return "checkProvisioningPreCondition";
                case 225:
                    return "setKeepUninstalledPackages";
                case 226:
                    return "getKeepUninstalledPackages";
                case 227:
                    return "isManagedProfile";
                case 228:
                    return "getWifiMacAddress";
                case 229:
                    return "reboot";
                case 230:
                    return "setShortSupportMessage";
                case 231:
                    return "getShortSupportMessage";
                case 232:
                    return "setLongSupportMessage";
                case 233:
                    return "getLongSupportMessage";
                case 234:
                    return "getShortSupportMessageForUser";
                case 235:
                    return "getLongSupportMessageForUser";
                case 236:
                    return "isSeparateProfileChallengeAllowed";
                case 237:
                    return "setOrganizationColor";
                case 238:
                    return "setOrganizationColorForUser";
                case 239:
                    return "getOrganizationColor";
                case 240:
                    return "getOrganizationColorForUser";
                case 241:
                    return "setOrganizationName";
                case 242:
                    return "getOrganizationName";
                case 243:
                    return "getDeviceOwnerOrganizationName";
                case 244:
                    return "getOrganizationNameForUser";
                case 245:
                    return "getUserProvisioningState";
                case 246:
                    return "setUserProvisioningState";
                case 247:
                    return "setAffiliationIds";
                case 248:
                    return "getAffiliationIds";
                case 249:
                    return "isCallingUserAffiliated";
                case 250:
                    return "isAffiliatedUser";
                case 251:
                    return "setSecurityLoggingEnabled";
                case 252:
                    return "isSecurityLoggingEnabled";
                case 253:
                    return "retrieveSecurityLogs";
                case 254:
                    return "retrievePreRebootSecurityLogs";
                case 255:
                    return "forceNetworkLogs";
                case 256:
                    return "forceSecurityLogs";
                case 257:
                    return "isUninstallInQueue";
                case 258:
                    return "uninstallPackageWithActiveAdmins";
                case 259:
                    return "isDeviceProvisioned";
                case 260:
                    return "isDeviceProvisioningConfigApplied";
                case 261:
                    return "setDeviceProvisioningConfigApplied";
                case 262:
                    return "forceUpdateUserSetupComplete";
                case 263:
                    return "setBackupServiceEnabled";
                case 264:
                    return "isBackupServiceEnabled";
                case 265:
                    return "setNetworkLoggingEnabled";
                case 266:
                    return "isNetworkLoggingEnabled";
                case 267:
                    return "retrieveNetworkLogs";
                case 268:
                    return "bindDeviceAdminServiceAsUser";
                case 269:
                    return "getBindDeviceAdminTargetUsers";
                case 270:
                    return "isEphemeralUser";
                case 271:
                    return "getLastSecurityLogRetrievalTime";
                case 272:
                    return "getLastBugReportRequestTime";
                case 273:
                    return "getLastNetworkLogRetrievalTime";
                case 274:
                    return "setResetPasswordToken";
                case 275:
                    return "clearResetPasswordToken";
                case 276:
                    return "isResetPasswordTokenActive";
                case 277:
                    return "resetPasswordWithToken";
                case 278:
                    return "isCurrentInputMethodSetByOwner";
                case 279:
                    return "getOwnerInstalledCaCerts";
                case 280:
                    return "clearApplicationUserData";
                case 281:
                    return "setLogoutEnabled";
                case 282:
                    return "isLogoutEnabled";
                case 283:
                    return "getDisallowedSystemApps";
                case 284:
                    return "transferOwnership";
                case 285:
                    return "getTransferOwnershipBundle";
                case 286:
                    return "setStartUserSessionMessage";
                case 287:
                    return "setEndUserSessionMessage";
                case 288:
                    return "getStartUserSessionMessage";
                case 289:
                    return "getEndUserSessionMessage";
                case 290:
                    return "setMeteredDataDisabledPackages";
                case 291:
                    return "getMeteredDataDisabledPackages";
                case 292:
                    return "addOverrideApn";
                case 293:
                    return "updateOverrideApn";
                case 294:
                    return "removeOverrideApn";
                case 295:
                    return "getOverrideApns";
                case 296:
                    return "setOverrideApnsEnabled";
                case 297:
                    return "isOverrideApnEnabled";
                case 298:
                    return "isMeteredDataDisabledPackageForUser";
                case 299:
                    return "setGlobalPrivateDns";
                case 300:
                    return "getGlobalPrivateDnsMode";
                case 301:
                    return "getGlobalPrivateDnsHost";
                case 302:
                    return "markProfileOwnerOnOrganizationOwnedDevice";
                case 303:
                    return "installUpdateFromFile";
                case 304:
                    return "setCrossProfileCalendarPackages";
                case 305:
                    return "getCrossProfileCalendarPackages";
                case 306:
                    return "isPackageAllowedToAccessCalendarForUser";
                case 307:
                    return "getCrossProfileCalendarPackagesForUser";
                case 308:
                    return "setCrossProfilePackages";
                case 309:
                    return "getCrossProfilePackages";
                case 310:
                    return "getAllCrossProfilePackages";
                case 311:
                    return "getDefaultCrossProfilePackages";
                case 312:
                    return "isManagedKiosk";
                case 313:
                    return "isUnattendedManagedKiosk";
                case 314:
                    return "startViewCalendarEventInManagedProfile";
                case 315:
                    return "setKeyGrantForApp";
                case 316:
                    return "getKeyPairGrants";
                case 317:
                    return "setKeyGrantToWifiAuth";
                case 318:
                    return "isKeyPairGrantedToWifiAuth";
                case 319:
                    return "setUserControlDisabledPackages";
                case 320:
                    return "getUserControlDisabledPackages";
                case 321:
                    return "setCommonCriteriaModeEnabled";
                case 322:
                    return "isCommonCriteriaModeEnabled";
                case 323:
                    return "getPersonalAppsSuspendedReasons";
                case 324:
                    return "setPersonalAppsSuspended";
                case 325:
                    return "getManagedProfileMaximumTimeOff";
                case 326:
                    return "setManagedProfileMaximumTimeOff";
                case 327:
                    return "acknowledgeDeviceCompliant";
                case 328:
                    return "isComplianceAcknowledgementRequired";
                case 329:
                    return "canProfileOwnerResetPasswordWhenLocked";
                case 330:
                    return "setNextOperationSafety";
                case 331:
                    return "isSafeOperation";
                case 332:
                    return "getEnrollmentSpecificId";
                case 333:
                    return "setOrganizationIdForUser";
                case 334:
                    return "createAndProvisionManagedProfile";
                case 335:
                    return "provisionFullyManagedDevice";
                case 336:
                    return "setDeviceOwnerType";
                case 337:
                    return "getDeviceOwnerType";
                case 338:
                    return "resetDefaultCrossProfileIntentFilters";
                case 339:
                    return "canAdminGrantSensorsPermissionsForUser";
                case 340:
                    return "setUsbDataSignalingEnabled";
                case 341:
                    return "isUsbDataSignalingEnabled";
                case 342:
                    return "isUsbDataSignalingEnabledForUser";
                case 343:
                    return "canUsbDataSignalingBeDisabled";
                case 344:
                    return "listForegroundAffiliatedUsers";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ComponentName _arg0;
            ComponentName _arg02;
            ComponentName _arg03;
            ComponentName _arg04;
            ComponentName _arg05;
            ComponentName _arg06;
            ComponentName _arg07;
            ComponentName _arg08;
            ComponentName _arg09;
            ComponentName _arg010;
            ComponentName _arg011;
            ComponentName _arg012;
            ComponentName _arg013;
            ComponentName _arg014;
            ComponentName _arg015;
            ComponentName _arg016;
            boolean _arg1;
            ComponentName _arg017;
            ComponentName _arg018;
            ComponentName _arg019;
            ComponentName _arg020;
            ComponentName _arg021;
            ComponentName _arg022;
            UserHandle _arg023;
            Account _arg12;
            ComponentName _arg024;
            ComponentName _arg025;
            ComponentName _arg026;
            ComponentName _arg027;
            ComponentName _arg028;
            ComponentName _arg029;
            ComponentName _arg030;
            FactoryResetProtectionPolicy _arg13;
            ComponentName _arg031;
            ComponentName _arg032;
            ProxyInfo _arg14;
            ComponentName _arg033;
            ComponentName _arg034;
            ComponentName _arg035;
            ComponentName _arg036;
            ComponentName _arg037;
            ComponentName _arg038;
            boolean _arg039;
            UserHandle _arg040;
            ComponentName _arg041;
            ComponentName _arg042;
            ComponentName _arg043;
            ComponentName _arg044;
            CharSequence _arg15;
            ComponentName _arg045;
            ComponentName _arg046;
            ComponentName _arg047;
            ComponentName _arg048;
            ComponentName _arg049;
            ComponentName _arg050;
            ComponentName _arg051;
            ComponentName _arg052;
            ComponentName _arg053;
            ComponentName _arg054;
            ComponentName _arg055;
            ComponentName _arg056;
            ComponentName _arg16;
            ComponentName _arg057;
            boolean _arg17;
            ComponentName _arg058;
            ComponentName _arg059;
            ComponentName _arg060;
            ComponentName _arg061;
            ComponentName _arg062;
            ComponentName _arg063;
            ComponentName _arg064;
            UserHandle _arg18;
            ComponentName _arg065;
            UserHandle _arg19;
            ComponentName _arg066;
            UserHandle _arg110;
            ComponentName _arg067;
            UserHandle _arg111;
            ComponentName _arg068;
            ComponentName _arg069;
            ComponentName _arg070;
            UserHandle _arg071;
            ComponentName _arg072;
            ComponentName _arg073;
            ComponentName _arg074;
            ComponentName _arg075;
            ComponentName _arg076;
            ComponentName _arg077;
            ComponentName _arg078;
            ComponentName _arg079;
            ComponentName _arg080;
            ComponentName _arg081;
            ComponentName _arg082;
            ComponentName _arg083;
            ComponentName _arg084;
            ComponentName _arg085;
            ComponentName _arg086;
            ComponentName _arg087;
            ComponentName _arg088;
            ComponentName _arg089;
            ComponentName _arg090;
            ComponentName _arg091;
            ComponentName _arg092;
            ComponentName _arg093;
            ComponentName _arg094;
            ComponentName _arg095;
            ComponentName _arg096;
            ComponentName _arg097;
            ComponentName _arg098;
            ComponentName _arg099;
            ComponentName _arg0100;
            ComponentName _arg0101;
            Bitmap _arg112;
            ComponentName _arg0102;
            SystemUpdatePolicy _arg113;
            ComponentName _arg0103;
            ComponentName _arg0104;
            SystemUpdateInfo _arg0105;
            ComponentName _arg0106;
            ComponentName _arg0107;
            ComponentName _arg0108;
            ComponentName _arg0109;
            ComponentName _arg0110;
            ComponentName _arg0111;
            ComponentName _arg0112;
            CharSequence _arg114;
            ComponentName _arg0113;
            ComponentName _arg0114;
            CharSequence _arg115;
            ComponentName _arg0115;
            ComponentName _arg0116;
            ComponentName _arg0117;
            ComponentName _arg0118;
            ComponentName _arg0119;
            ComponentName _arg0120;
            CharSequence _arg116;
            ComponentName _arg0121;
            ComponentName _arg0122;
            ComponentName _arg0123;
            ComponentName _arg0124;
            ComponentName _arg0125;
            ComponentName _arg0126;
            ComponentName _arg0127;
            ComponentName _arg0128;
            ComponentName _arg0129;
            ComponentName _arg0130;
            ComponentName _arg0131;
            ComponentName _arg0132;
            ComponentName _arg0133;
            ComponentName _arg0134;
            UserHandle _arg0135;
            ComponentName _arg0136;
            ComponentName _arg0137;
            CharSequence _arg117;
            ComponentName _arg0138;
            CharSequence _arg118;
            ComponentName _arg0139;
            ComponentName _arg0140;
            ComponentName _arg0141;
            ComponentName _arg0142;
            ComponentName _arg0143;
            ApnSetting _arg119;
            ComponentName _arg0144;
            ComponentName _arg0145;
            ComponentName _arg0146;
            ComponentName _arg0147;
            ComponentName _arg0148;
            ComponentName _arg0149;
            ComponentName _arg0150;
            ComponentName _arg0151;
            ComponentName _arg0152;
            ComponentName _arg0153;
            ComponentName _arg0154;
            ComponentName _arg0155;
            ComponentName _arg0156;
            ComponentName _arg0157;
            ComponentName _arg0158;
            ComponentName _arg0159;
            ComponentName _arg0160;
            ComponentName _arg0161;
            ComponentName _arg0162;
            ManagedProfileProvisioningParams _arg0163;
            FullyManagedDeviceProvisioningParams _arg0164;
            ComponentName _arg0165;
            ComponentName _arg0166;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    boolean _arg120 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            int _arg121 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordQuality(_arg0, _arg121, _arg120);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            int _arg122 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result = getPasswordQuality(_arg02, _arg122, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 3:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            int _arg123 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordMinimumLength(_arg03, _arg123, _arg120);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            int _arg124 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result2 = getPasswordMinimumLength(_arg04, _arg124, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 5:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            int _arg125 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordMinimumUpperCase(_arg05, _arg125, _arg120);
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            int _arg126 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result3 = getPasswordMinimumUpperCase(_arg06, _arg126, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 7:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg07 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg07 = null;
                            }
                            int _arg127 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordMinimumLowerCase(_arg07, _arg127, _arg120);
                            reply.writeNoException();
                            return true;
                        case 8:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg08 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg08 = null;
                            }
                            int _arg128 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result4 = getPasswordMinimumLowerCase(_arg08, _arg128, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 9:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg09 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg09 = null;
                            }
                            int _arg129 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordMinimumLetters(_arg09, _arg129, _arg120);
                            reply.writeNoException();
                            return true;
                        case 10:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg010 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg010 = null;
                            }
                            int _arg130 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result5 = getPasswordMinimumLetters(_arg010, _arg130, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 11:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg011 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg011 = null;
                            }
                            int _arg131 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordMinimumNumeric(_arg011, _arg131, _arg120);
                            reply.writeNoException();
                            return true;
                        case 12:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg012 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg012 = null;
                            }
                            int _arg132 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result6 = getPasswordMinimumNumeric(_arg012, _arg132, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result6);
                            return true;
                        case 13:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg013 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg013 = null;
                            }
                            int _arg133 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordMinimumSymbols(_arg013, _arg133, _arg120);
                            reply.writeNoException();
                            return true;
                        case 14:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg014 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg014 = null;
                            }
                            int _arg134 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result7 = getPasswordMinimumSymbols(_arg014, _arg134, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 15:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg015 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg015 = null;
                            }
                            int _arg135 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordMinimumNonLetter(_arg015, _arg135, _arg120);
                            reply.writeNoException();
                            return true;
                        case 16:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg016 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg016 = null;
                            }
                            int _arg136 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result8 = getPasswordMinimumNonLetter(_arg016, _arg136, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result8);
                            return true;
                        case 17:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0167 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg1 = true;
                            } else {
                                _arg1 = false;
                            }
                            PasswordMetrics _result9 = getPasswordMinimumMetrics(_arg0167, _arg1);
                            reply.writeNoException();
                            if (_result9 != null) {
                                reply.writeInt(1);
                                _result9.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 18:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg017 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg017 = null;
                            }
                            int _arg137 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordHistoryLength(_arg017, _arg137, _arg120);
                            reply.writeNoException();
                            return true;
                        case 19:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg018 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg018 = null;
                            }
                            int _arg138 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result10 = getPasswordHistoryLength(_arg018, _arg138, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result10);
                            return true;
                        case 20:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg019 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg019 = null;
                            }
                            long _arg139 = data.readLong();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPasswordExpirationTimeout(_arg019, _arg139, _arg120);
                            reply.writeNoException();
                            return true;
                        case 21:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg020 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg020 = null;
                            }
                            int _arg140 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            long _result11 = getPasswordExpirationTimeout(_arg020, _arg140, _arg120);
                            reply.writeNoException();
                            reply.writeLong(_result11);
                            return true;
                        case 22:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg021 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg021 = null;
                            }
                            int _arg141 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            long _result12 = getPasswordExpiration(_arg021, _arg141, _arg120);
                            reply.writeNoException();
                            reply.writeLong(_result12);
                            return true;
                        case 23:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0168 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            boolean isActivePasswordSufficient = isActivePasswordSufficient(_arg0168, _arg120);
                            reply.writeNoException();
                            reply.writeInt(isActivePasswordSufficient ? 1 : 0);
                            return true;
                        case 24:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isActivePasswordSufficientForDeviceRequirement = isActivePasswordSufficientForDeviceRequirement();
                            reply.writeNoException();
                            reply.writeInt(isActivePasswordSufficientForDeviceRequirement ? 1 : 0);
                            return true;
                        case 25:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0169 = data.readInt();
                            boolean isPasswordSufficientAfterProfileUnification = isPasswordSufficientAfterProfileUnification(_arg0169, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isPasswordSufficientAfterProfileUnification ? 1 : 0);
                            return true;
                        case 26:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result13 = getPasswordComplexity(_arg120);
                            reply.writeNoException();
                            reply.writeInt(_result13);
                            return true;
                        case 27:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0170 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setRequiredPasswordComplexity(_arg0170, _arg120);
                            reply.writeNoException();
                            return true;
                        case 28:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result14 = getRequiredPasswordComplexity(_arg120);
                            reply.writeNoException();
                            reply.writeInt(_result14);
                            return true;
                        case 29:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0171 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result15 = getAggregatedPasswordComplexityForUser(_arg0171, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result15);
                            return true;
                        case 30:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg022 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg022 = null;
                            }
                            boolean isUsingUnifiedPassword = isUsingUnifiedPassword(_arg022);
                            reply.writeNoException();
                            reply.writeInt(isUsingUnifiedPassword ? 1 : 0);
                            return true;
                        case 31:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0172 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result16 = getCurrentFailedPasswordAttempts(_arg0172, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result16);
                            return true;
                        case 32:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0173 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result17 = getProfileWithMinimumFailedPasswordsForWipe(_arg0173, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result17);
                            return true;
                        case 33:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg023 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg023 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg12 = Account.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            finalizeWorkProfileProvisioning(_arg023, _arg12);
                            reply.writeNoException();
                            return true;
                        case 34:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg024 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg024 = null;
                            }
                            int _arg142 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setMaximumFailedPasswordsForWipe(_arg024, _arg142, _arg120);
                            reply.writeNoException();
                            return true;
                        case 35:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg025 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg025 = null;
                            }
                            int _arg143 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result18 = getMaximumFailedPasswordsForWipe(_arg025, _arg143, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result18);
                            return true;
                        case 36:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0174 = data.readString();
                            boolean resetPassword = resetPassword(_arg0174, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(resetPassword ? 1 : 0);
                            return true;
                        case 37:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg026 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg026 = null;
                            }
                            long _arg144 = data.readLong();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setMaximumTimeToLock(_arg026, _arg144, _arg120);
                            reply.writeNoException();
                            return true;
                        case 38:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg027 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg027 = null;
                            }
                            int _arg145 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            long _result19 = getMaximumTimeToLock(_arg027, _arg145, _arg120);
                            reply.writeNoException();
                            reply.writeLong(_result19);
                            return true;
                        case 39:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg028 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg028 = null;
                            }
                            long _arg146 = data.readLong();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setRequiredStrongAuthTimeout(_arg028, _arg146, _arg120);
                            reply.writeNoException();
                            return true;
                        case 40:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg029 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg029 = null;
                            }
                            int _arg147 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            long _result20 = getRequiredStrongAuthTimeout(_arg029, _arg147, _arg120);
                            reply.writeNoException();
                            reply.writeLong(_result20);
                            return true;
                        case 41:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0175 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            lockNow(_arg0175, _arg120);
                            reply.writeNoException();
                            return true;
                        case 42:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0176 = data.readInt();
                            String _arg148 = data.readString();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            wipeDataWithReason(_arg0176, _arg148, _arg120);
                            reply.writeNoException();
                            return true;
                        case 43:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg030 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg030 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg13 = FactoryResetProtectionPolicy.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            setFactoryResetProtectionPolicy(_arg030, _arg13);
                            reply.writeNoException();
                            return true;
                        case 44:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg031 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg031 = null;
                            }
                            FactoryResetProtectionPolicy _result21 = getFactoryResetProtectionPolicy(_arg031);
                            reply.writeNoException();
                            if (_result21 != null) {
                                reply.writeInt(1);
                                _result21.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 45:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isFactoryResetProtectionPolicySupported = isFactoryResetProtectionPolicySupported();
                            reply.writeNoException();
                            reply.writeInt(isFactoryResetProtectionPolicySupported ? 1 : 0);
                            return true;
                        case 46:
                            return onTransact$setGlobalProxy$(data, reply);
                        case 47:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0177 = data.readInt();
                            ComponentName _result22 = getGlobalProxyAdmin(_arg0177);
                            reply.writeNoException();
                            if (_result22 != null) {
                                reply.writeInt(1);
                                _result22.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 48:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg032 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg032 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg14 = (ProxyInfo) ProxyInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            setRecommendedGlobalProxy(_arg032, _arg14);
                            reply.writeNoException();
                            return true;
                        case 49:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg033 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg033 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            int _result23 = setStorageEncryption(_arg033, _arg120);
                            reply.writeNoException();
                            reply.writeInt(_result23);
                            return true;
                        case 50:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg034 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg034 = null;
                            }
                            boolean storageEncryption = getStorageEncryption(_arg034, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(storageEncryption ? 1 : 0);
                            return true;
                        case 51:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0178 = data.readString();
                            int _result24 = getStorageEncryptionStatus(_arg0178, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(_result24);
                            return true;
                        case 52:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg035 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg035 = null;
                            }
                            boolean requestBugreport = requestBugreport(_arg035);
                            reply.writeNoException();
                            reply.writeInt(requestBugreport ? 1 : 0);
                            return true;
                        case 53:
                            return onTransact$setCameraDisabled$(data, reply);
                        case 54:
                            return onTransact$getCameraDisabled$(data, reply);
                        case 55:
                            return onTransact$setScreenCaptureDisabled$(data, reply);
                        case 56:
                            return onTransact$getScreenCaptureDisabled$(data, reply);
                        case 57:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0179 = data.readInt();
                            setNearbyNotificationStreamingPolicy(_arg0179);
                            reply.writeNoException();
                            return true;
                        case 58:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0180 = data.readInt();
                            int _result25 = getNearbyNotificationStreamingPolicy(_arg0180);
                            reply.writeNoException();
                            reply.writeInt(_result25);
                            return true;
                        case 59:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0181 = data.readInt();
                            setNearbyAppStreamingPolicy(_arg0181);
                            reply.writeNoException();
                            return true;
                        case 60:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0182 = data.readInt();
                            int _result26 = getNearbyAppStreamingPolicy(_arg0182);
                            reply.writeNoException();
                            reply.writeInt(_result26);
                            return true;
                        case 61:
                            return onTransact$setKeyguardDisabledFeatures$(data, reply);
                        case 62:
                            return onTransact$getKeyguardDisabledFeatures$(data, reply);
                        case 63:
                            return onTransact$setActiveAdmin$(data, reply);
                        case 64:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg036 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg036 = null;
                            }
                            boolean isAdminActive = isAdminActive(_arg036, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isAdminActive ? 1 : 0);
                            return true;
                        case 65:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0183 = data.readInt();
                            List<ComponentName> _result27 = getActiveAdmins(_arg0183);
                            reply.writeNoException();
                            reply.writeTypedList(_result27);
                            return true;
                        case 66:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0184 = data.readString();
                            boolean packageHasActiveAdmins = packageHasActiveAdmins(_arg0184, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(packageHasActiveAdmins ? 1 : 0);
                            return true;
                        case 67:
                            return onTransact$getRemoveWarning$(data, reply);
                        case 68:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg037 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg037 = null;
                            }
                            removeActiveAdmin(_arg037, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 69:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg038 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg038 = null;
                            }
                            forceRemoveActiveAdmin(_arg038, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 70:
                            return onTransact$hasGrantedPolicy$(data, reply);
                        case 71:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0185 = data.readInt();
                            reportPasswordChanged(_arg0185);
                            reply.writeNoException();
                            return true;
                        case 72:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0186 = data.readInt();
                            reportFailedPasswordAttempt(_arg0186);
                            reply.writeNoException();
                            return true;
                        case 73:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0187 = data.readInt();
                            reportSuccessfulPasswordAttempt(_arg0187);
                            reply.writeNoException();
                            return true;
                        case 74:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0188 = data.readInt();
                            reportFailedBiometricAttempt(_arg0188);
                            reply.writeNoException();
                            return true;
                        case 75:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0189 = data.readInt();
                            reportSuccessfulBiometricAttempt(_arg0189);
                            reply.writeNoException();
                            return true;
                        case 76:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0190 = data.readInt();
                            reportKeyguardDismissed(_arg0190);
                            reply.writeNoException();
                            return true;
                        case 77:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0191 = data.readInt();
                            reportKeyguardSecured(_arg0191);
                            reply.writeNoException();
                            return true;
                        case 78:
                            return onTransact$setDeviceOwner$(data, reply);
                        case 79:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg039 = true;
                            } else {
                                _arg039 = false;
                            }
                            ComponentName _result28 = getDeviceOwnerComponent(_arg039);
                            reply.writeNoException();
                            if (_result28 != null) {
                                reply.writeInt(1);
                                _result28.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 80:
                            data.enforceInterface(DESCRIPTOR);
                            boolean hasDeviceOwner = hasDeviceOwner();
                            reply.writeNoException();
                            reply.writeInt(hasDeviceOwner ? 1 : 0);
                            return true;
                        case 81:
                            data.enforceInterface(DESCRIPTOR);
                            String _result29 = getDeviceOwnerName();
                            reply.writeNoException();
                            reply.writeString(_result29);
                            return true;
                        case 82:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0192 = data.readString();
                            clearDeviceOwner(_arg0192);
                            reply.writeNoException();
                            return true;
                        case 83:
                            data.enforceInterface(DESCRIPTOR);
                            int _result30 = getDeviceOwnerUserId();
                            reply.writeNoException();
                            reply.writeInt(_result30);
                            return true;
                        case 84:
                            return onTransact$setProfileOwner$(data, reply);
                        case 85:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0193 = data.readInt();
                            ComponentName _result31 = getProfileOwnerAsUser(_arg0193);
                            reply.writeNoException();
                            if (_result31 != null) {
                                reply.writeInt(1);
                                _result31.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 86:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg040 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg040 = null;
                            }
                            ComponentName _result32 = getProfileOwnerOrDeviceOwnerSupervisionComponent(_arg040);
                            reply.writeNoException();
                            if (_result32 != null) {
                                reply.writeInt(1);
                                _result32.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 87:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0194 = data.readInt();
                            String _result33 = getProfileOwnerName(_arg0194);
                            reply.writeNoException();
                            reply.writeString(_result33);
                            return true;
                        case 88:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg041 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg041 = null;
                            }
                            setProfileEnabled(_arg041);
                            reply.writeNoException();
                            return true;
                        case 89:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg042 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg042 = null;
                            }
                            setProfileName(_arg042, data.readString());
                            reply.writeNoException();
                            return true;
                        case 90:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg043 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg043 = null;
                            }
                            clearProfileOwner(_arg043);
                            reply.writeNoException();
                            return true;
                        case 91:
                            data.enforceInterface(DESCRIPTOR);
                            boolean hasUserSetupCompleted = hasUserSetupCompleted();
                            reply.writeNoException();
                            reply.writeInt(hasUserSetupCompleted ? 1 : 0);
                            return true;
                        case 92:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isOrganizationOwnedDeviceWithManagedProfile = isOrganizationOwnedDeviceWithManagedProfile();
                            reply.writeNoException();
                            reply.writeInt(isOrganizationOwnedDeviceWithManagedProfile ? 1 : 0);
                            return true;
                        case 93:
                            return onTransact$checkDeviceIdentifierAccess$(data, reply);
                        case 94:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg044 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg044 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg15 = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg15 = null;
                            }
                            setDeviceOwnerLockScreenInfo(_arg044, _arg15);
                            reply.writeNoException();
                            return true;
                        case 95:
                            data.enforceInterface(DESCRIPTOR);
                            CharSequence _result34 = getDeviceOwnerLockScreenInfo();
                            reply.writeNoException();
                            if (_result34 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result34, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 96:
                            return onTransact$setPackagesSuspended$(data, reply);
                        case 97:
                            return onTransact$isPackageSuspended$(data, reply);
                        case 98:
                            data.enforceInterface(DESCRIPTOR);
                            List<String> _result35 = listPolicyExemptApps();
                            reply.writeNoException();
                            reply.writeStringList(_result35);
                            return true;
                        case 99:
                            return onTransact$installCaCert$(data, reply);
                        case 100:
                            return onTransact$uninstallCaCerts$(data, reply);
                        case 101:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg045 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg045 = null;
                            }
                            enforceCanManageCaCerts(_arg045, data.readString());
                            reply.writeNoException();
                            return true;
                        case 102:
                            return onTransact$approveCaCert$(data, reply);
                        case 103:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0195 = data.readString();
                            boolean isCaCertApproved = isCaCertApproved(_arg0195, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isCaCertApproved ? 1 : 0);
                            return true;
                        case 104:
                            return onTransact$installKeyPair$(data, reply);
                        case 105:
                            return onTransact$removeKeyPair$(data, reply);
                        case 106:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0196 = data.readString();
                            boolean hasKeyPair = hasKeyPair(_arg0196, data.readString());
                            reply.writeNoException();
                            reply.writeInt(hasKeyPair ? 1 : 0);
                            return true;
                        case 107:
                            return onTransact$generateKeyPair$(data, reply);
                        case 108:
                            return onTransact$setKeyPairCertificate$(data, reply);
                        case 109:
                            return onTransact$choosePrivateKeyAlias$(data, reply);
                        case 110:
                            return onTransact$setDelegatedScopes$(data, reply);
                        case 111:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg046 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg046 = null;
                            }
                            List<String> _result36 = getDelegatedScopes(_arg046, data.readString());
                            reply.writeNoException();
                            reply.writeStringList(_result36);
                            return true;
                        case 112:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg047 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg047 = null;
                            }
                            List<String> _result37 = getDelegatePackages(_arg047, data.readString());
                            reply.writeNoException();
                            reply.writeStringList(_result37);
                            return true;
                        case 113:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg048 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg048 = null;
                            }
                            setCertInstallerPackage(_arg048, data.readString());
                            reply.writeNoException();
                            return true;
                        case 114:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg049 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg049 = null;
                            }
                            String _result38 = getCertInstallerPackage(_arg049);
                            reply.writeNoException();
                            reply.writeString(_result38);
                            return true;
                        case 115:
                            return onTransact$setAlwaysOnVpnPackage$(data, reply);
                        case 116:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg050 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg050 = null;
                            }
                            String _result39 = getAlwaysOnVpnPackage(_arg050);
                            reply.writeNoException();
                            reply.writeString(_result39);
                            return true;
                        case 117:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0197 = data.readInt();
                            String _result40 = getAlwaysOnVpnPackageForUser(_arg0197);
                            reply.writeNoException();
                            reply.writeString(_result40);
                            return true;
                        case 118:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg051 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg051 = null;
                            }
                            boolean isAlwaysOnVpnLockdownEnabled = isAlwaysOnVpnLockdownEnabled(_arg051);
                            reply.writeNoException();
                            reply.writeInt(isAlwaysOnVpnLockdownEnabled ? 1 : 0);
                            return true;
                        case 119:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0198 = data.readInt();
                            boolean isAlwaysOnVpnLockdownEnabledForUser = isAlwaysOnVpnLockdownEnabledForUser(_arg0198);
                            reply.writeNoException();
                            reply.writeInt(isAlwaysOnVpnLockdownEnabledForUser ? 1 : 0);
                            return true;
                        case 120:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg052 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg052 = null;
                            }
                            List<String> _result41 = getAlwaysOnVpnLockdownAllowlist(_arg052);
                            reply.writeNoException();
                            reply.writeStringList(_result41);
                            return true;
                        case 121:
                            return onTransact$addPersistentPreferredActivity$(data, reply);
                        case 122:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg053 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg053 = null;
                            }
                            clearPackagePersistentPreferredActivities(_arg053, data.readString());
                            reply.writeNoException();
                            return true;
                        case 123:
                            return onTransact$setDefaultSmsApplication$(data, reply);
                        case 124:
                            return onTransact$setApplicationRestrictions$(data, reply);
                        case 125:
                            return onTransact$getApplicationRestrictions$(data, reply);
                        case 126:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg054 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg054 = null;
                            }
                            boolean applicationRestrictionsManagingPackage = setApplicationRestrictionsManagingPackage(_arg054, data.readString());
                            reply.writeNoException();
                            reply.writeInt(applicationRestrictionsManagingPackage ? 1 : 0);
                            return true;
                        case 127:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg055 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg055 = null;
                            }
                            String _result42 = getApplicationRestrictionsManagingPackage(_arg055);
                            reply.writeNoException();
                            reply.writeString(_result42);
                            return true;
                        case 128:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0199 = data.readString();
                            boolean isCallerApplicationRestrictionsManagingPackage = isCallerApplicationRestrictionsManagingPackage(_arg0199);
                            reply.writeNoException();
                            reply.writeInt(isCallerApplicationRestrictionsManagingPackage ? 1 : 0);
                            return true;
                        case 129:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg056 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg056 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg16 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg16 = null;
                            }
                            setRestrictionsProvider(_arg056, _arg16);
                            reply.writeNoException();
                            return true;
                        case 130:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0200 = data.readInt();
                            ComponentName _result43 = getRestrictionsProvider(_arg0200);
                            reply.writeNoException();
                            if (_result43 != null) {
                                reply.writeInt(1);
                                _result43.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 131:
                            return onTransact$setUserRestriction$(data, reply);
                        case 132:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg057 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg057 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg17 = true;
                            } else {
                                _arg17 = false;
                            }
                            Bundle _result44 = getUserRestrictions(_arg057, _arg17);
                            reply.writeNoException();
                            if (_result44 != null) {
                                reply.writeInt(1);
                                _result44.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 133:
                            return onTransact$addCrossProfileIntentFilter$(data, reply);
                        case 134:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg058 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg058 = null;
                            }
                            clearCrossProfileIntentFilters(_arg058);
                            reply.writeNoException();
                            return true;
                        case 135:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg059 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg059 = null;
                            }
                            ClassLoader cl = getClass().getClassLoader();
                            boolean permittedAccessibilityServices = setPermittedAccessibilityServices(_arg059, data.readArrayList(cl));
                            reply.writeNoException();
                            reply.writeInt(permittedAccessibilityServices ? 1 : 0);
                            return true;
                        case 136:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg060 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg060 = null;
                            }
                            List _result45 = getPermittedAccessibilityServices(_arg060);
                            reply.writeNoException();
                            reply.writeList(_result45);
                            return true;
                        case 137:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0201 = data.readInt();
                            List _result46 = getPermittedAccessibilityServicesForUser(_arg0201);
                            reply.writeNoException();
                            reply.writeList(_result46);
                            return true;
                        case 138:
                            return onTransact$isAccessibilityServicePermittedByAdmin$(data, reply);
                        case 139:
                            return onTransact$setPermittedInputMethods$(data, reply);
                        case 140:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg061 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg061 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            List _result47 = getPermittedInputMethods(_arg061, _arg120);
                            reply.writeNoException();
                            reply.writeList(_result47);
                            return true;
                        case 141:
                            data.enforceInterface(DESCRIPTOR);
                            List _result48 = getPermittedInputMethodsForCurrentUser();
                            reply.writeNoException();
                            reply.writeList(_result48);
                            return true;
                        case 142:
                            return onTransact$isInputMethodPermittedByAdmin$(data, reply);
                        case 143:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg062 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg062 = null;
                            }
                            boolean permittedCrossProfileNotificationListeners = setPermittedCrossProfileNotificationListeners(_arg062, data.createStringArrayList());
                            reply.writeNoException();
                            reply.writeInt(permittedCrossProfileNotificationListeners ? 1 : 0);
                            return true;
                        case 144:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg063 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg063 = null;
                            }
                            List<String> _result49 = getPermittedCrossProfileNotificationListeners(_arg063);
                            reply.writeNoException();
                            reply.writeStringList(_result49);
                            return true;
                        case 145:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0202 = data.readString();
                            boolean isNotificationListenerServicePermitted = isNotificationListenerServicePermitted(_arg0202, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isNotificationListenerServicePermitted ? 1 : 0);
                            return true;
                        case 146:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0203 = data.readString();
                            Intent _result50 = createAdminSupportIntent(_arg0203);
                            reply.writeNoException();
                            if (_result50 != null) {
                                reply.writeInt(1);
                                _result50.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 147:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0204 = data.readInt();
                            Bundle _result51 = getEnforcingAdminAndUserDetails(_arg0204, data.readString());
                            reply.writeNoException();
                            if (_result51 != null) {
                                reply.writeInt(1);
                                _result51.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 148:
                            return onTransact$setApplicationHidden$(data, reply);
                        case 149:
                            return onTransact$isApplicationHidden$(data, reply);
                        case 150:
                            return onTransact$createAndManageUser$(data, reply);
                        case 151:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg064 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg064 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg18 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg18 = null;
                            }
                            boolean removeUser = removeUser(_arg064, _arg18);
                            reply.writeNoException();
                            reply.writeInt(removeUser ? 1 : 0);
                            return true;
                        case 152:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg065 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg065 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg19 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg19 = null;
                            }
                            boolean switchUser = switchUser(_arg065, _arg19);
                            reply.writeNoException();
                            reply.writeInt(switchUser ? 1 : 0);
                            return true;
                        case 153:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg066 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg066 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg110 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg110 = null;
                            }
                            int _result52 = startUserInBackground(_arg066, _arg110);
                            reply.writeNoException();
                            reply.writeInt(_result52);
                            return true;
                        case 154:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg067 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg067 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg111 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg111 = null;
                            }
                            int _result53 = stopUser(_arg067, _arg111);
                            reply.writeNoException();
                            reply.writeInt(_result53);
                            return true;
                        case 155:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg068 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg068 = null;
                            }
                            int _result54 = logoutUser(_arg068);
                            reply.writeNoException();
                            reply.writeInt(_result54);
                            return true;
                        case 156:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg069 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg069 = null;
                            }
                            List<UserHandle> _result55 = getSecondaryUsers(_arg069);
                            reply.writeNoException();
                            reply.writeTypedList(_result55);
                            return true;
                        case 157:
                            data.enforceInterface(DESCRIPTOR);
                            resetNewUserDisclaimer();
                            reply.writeNoException();
                            return true;
                        case 158:
                            return onTransact$enableSystemApp$(data, reply);
                        case 159:
                            return onTransact$enableSystemAppWithIntent$(data, reply);
                        case 160:
                            return onTransact$installExistingPackage$(data, reply);
                        case 161:
                            return onTransact$setAccountManagementDisabled$(data, reply);
                        case 162:
                            data.enforceInterface(DESCRIPTOR);
                            String[] _result56 = getAccountTypesWithManagementDisabled();
                            reply.writeNoException();
                            reply.writeStringArray(_result56);
                            return true;
                        case 163:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0205 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            String[] _result57 = getAccountTypesWithManagementDisabledAsUser(_arg0205, _arg120);
                            reply.writeNoException();
                            reply.writeStringArray(_result57);
                            return true;
                        case 164:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg070 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg070 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setSecondaryLockscreenEnabled(_arg070, _arg120);
                            reply.writeNoException();
                            return true;
                        case 165:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg071 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg071 = null;
                            }
                            boolean isSecondaryLockscreenEnabled = isSecondaryLockscreenEnabled(_arg071);
                            reply.writeNoException();
                            reply.writeInt(isSecondaryLockscreenEnabled ? 1 : 0);
                            return true;
                        case 166:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPreferentialNetworkServiceEnabled(_arg120);
                            reply.writeNoException();
                            return true;
                        case 167:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0206 = data.readInt();
                            boolean isPreferentialNetworkServiceEnabled = isPreferentialNetworkServiceEnabled(_arg0206);
                            reply.writeNoException();
                            reply.writeInt(isPreferentialNetworkServiceEnabled ? 1 : 0);
                            return true;
                        case 168:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg072 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg072 = null;
                            }
                            setLockTaskPackages(_arg072, data.createStringArray());
                            reply.writeNoException();
                            return true;
                        case 169:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg073 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg073 = null;
                            }
                            String[] _result58 = getLockTaskPackages(_arg073);
                            reply.writeNoException();
                            reply.writeStringArray(_result58);
                            return true;
                        case 170:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0207 = data.readString();
                            boolean isLockTaskPermitted = isLockTaskPermitted(_arg0207);
                            reply.writeNoException();
                            reply.writeInt(isLockTaskPermitted ? 1 : 0);
                            return true;
                        case 171:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg074 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg074 = null;
                            }
                            setLockTaskFeatures(_arg074, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 172:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg075 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg075 = null;
                            }
                            int _result59 = getLockTaskFeatures(_arg075);
                            reply.writeNoException();
                            reply.writeInt(_result59);
                            return true;
                        case 173:
                            return onTransact$setGlobalSetting$(data, reply);
                        case 174:
                            return onTransact$setSystemSetting$(data, reply);
                        case 175:
                            return onTransact$setSecureSetting$(data, reply);
                        case 176:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg076 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg076 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setConfiguredNetworksLockdownState(_arg076, _arg120);
                            reply.writeNoException();
                            return true;
                        case 177:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg077 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg077 = null;
                            }
                            boolean hasLockdownAdminConfiguredNetworks = hasLockdownAdminConfiguredNetworks(_arg077);
                            reply.writeNoException();
                            reply.writeInt(hasLockdownAdminConfiguredNetworks ? 1 : 0);
                            return true;
                        case 178:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg078 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg078 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setLocationEnabled(_arg078, _arg120);
                            reply.writeNoException();
                            return true;
                        case 179:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg079 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg079 = null;
                            }
                            boolean time = setTime(_arg079, data.readLong());
                            reply.writeNoException();
                            reply.writeInt(time ? 1 : 0);
                            return true;
                        case 180:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg080 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg080 = null;
                            }
                            boolean timeZone = setTimeZone(_arg080, data.readString());
                            reply.writeNoException();
                            reply.writeInt(timeZone ? 1 : 0);
                            return true;
                        case 181:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg081 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg081 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setMasterVolumeMuted(_arg081, _arg120);
                            reply.writeNoException();
                            return true;
                        case 182:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg082 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg082 = null;
                            }
                            boolean isMasterVolumeMuted = isMasterVolumeMuted(_arg082);
                            reply.writeNoException();
                            reply.writeInt(isMasterVolumeMuted ? 1 : 0);
                            return true;
                        case 183:
                            return onTransact$notifyLockTaskModeChanged$(data, reply);
                        case 184:
                            return onTransact$setUninstallBlocked$(data, reply);
                        case 185:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg083 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg083 = null;
                            }
                            boolean isUninstallBlocked = isUninstallBlocked(_arg083, data.readString());
                            reply.writeNoException();
                            reply.writeInt(isUninstallBlocked ? 1 : 0);
                            return true;
                        case 186:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg084 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg084 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setCrossProfileCallerIdDisabled(_arg084, _arg120);
                            reply.writeNoException();
                            return true;
                        case 187:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg085 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg085 = null;
                            }
                            boolean crossProfileCallerIdDisabled = getCrossProfileCallerIdDisabled(_arg085);
                            reply.writeNoException();
                            reply.writeInt(crossProfileCallerIdDisabled ? 1 : 0);
                            return true;
                        case 188:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0208 = data.readInt();
                            boolean crossProfileCallerIdDisabledForUser = getCrossProfileCallerIdDisabledForUser(_arg0208);
                            reply.writeNoException();
                            reply.writeInt(crossProfileCallerIdDisabledForUser ? 1 : 0);
                            return true;
                        case 189:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg086 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg086 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setCrossProfileContactsSearchDisabled(_arg086, _arg120);
                            reply.writeNoException();
                            return true;
                        case 190:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg087 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg087 = null;
                            }
                            boolean crossProfileContactsSearchDisabled = getCrossProfileContactsSearchDisabled(_arg087);
                            reply.writeNoException();
                            reply.writeInt(crossProfileContactsSearchDisabled ? 1 : 0);
                            return true;
                        case 191:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0209 = data.readInt();
                            boolean crossProfileContactsSearchDisabledForUser = getCrossProfileContactsSearchDisabledForUser(_arg0209);
                            reply.writeNoException();
                            reply.writeInt(crossProfileContactsSearchDisabledForUser ? 1 : 0);
                            return true;
                        case 192:
                            return onTransact$startManagedQuickContact$(data, reply);
                        case 193:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg088 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg088 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setBluetoothContactSharingDisabled(_arg088, _arg120);
                            reply.writeNoException();
                            return true;
                        case 194:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg089 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg089 = null;
                            }
                            boolean bluetoothContactSharingDisabled = getBluetoothContactSharingDisabled(_arg089);
                            reply.writeNoException();
                            reply.writeInt(bluetoothContactSharingDisabled ? 1 : 0);
                            return true;
                        case 195:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0210 = data.readInt();
                            boolean bluetoothContactSharingDisabledForUser = getBluetoothContactSharingDisabledForUser(_arg0210);
                            reply.writeNoException();
                            reply.writeInt(bluetoothContactSharingDisabledForUser ? 1 : 0);
                            return true;
                        case 196:
                            return onTransact$setTrustAgentConfiguration$(data, reply);
                        case 197:
                            return onTransact$getTrustAgentConfiguration$(data, reply);
                        case 198:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg090 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg090 = null;
                            }
                            boolean addCrossProfileWidgetProvider = addCrossProfileWidgetProvider(_arg090, data.readString());
                            reply.writeNoException();
                            reply.writeInt(addCrossProfileWidgetProvider ? 1 : 0);
                            return true;
                        case 199:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg091 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg091 = null;
                            }
                            boolean removeCrossProfileWidgetProvider = removeCrossProfileWidgetProvider(_arg091, data.readString());
                            reply.writeNoException();
                            reply.writeInt(removeCrossProfileWidgetProvider ? 1 : 0);
                            return true;
                        case 200:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg092 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg092 = null;
                            }
                            List<String> _result60 = getCrossProfileWidgetProviders(_arg092);
                            reply.writeNoException();
                            reply.writeStringList(_result60);
                            return true;
                        case 201:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg093 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg093 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setAutoTimeRequired(_arg093, _arg120);
                            reply.writeNoException();
                            return true;
                        case 202:
                            data.enforceInterface(DESCRIPTOR);
                            boolean autoTimeRequired = getAutoTimeRequired();
                            reply.writeNoException();
                            reply.writeInt(autoTimeRequired ? 1 : 0);
                            return true;
                        case 203:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg094 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg094 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setAutoTimeEnabled(_arg094, _arg120);
                            reply.writeNoException();
                            return true;
                        case 204:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg095 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg095 = null;
                            }
                            boolean autoTimeEnabled = getAutoTimeEnabled(_arg095);
                            reply.writeNoException();
                            reply.writeInt(autoTimeEnabled ? 1 : 0);
                            return true;
                        case 205:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg096 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg096 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setAutoTimeZoneEnabled(_arg096, _arg120);
                            reply.writeNoException();
                            return true;
                        case 206:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg097 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg097 = null;
                            }
                            boolean autoTimeZoneEnabled = getAutoTimeZoneEnabled(_arg097);
                            reply.writeNoException();
                            reply.writeInt(autoTimeZoneEnabled ? 1 : 0);
                            return true;
                        case 207:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg098 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg098 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setForceEphemeralUsers(_arg098, _arg120);
                            reply.writeNoException();
                            return true;
                        case 208:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg099 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg099 = null;
                            }
                            boolean forceEphemeralUsers = getForceEphemeralUsers(_arg099);
                            reply.writeNoException();
                            reply.writeInt(forceEphemeralUsers ? 1 : 0);
                            return true;
                        case 209:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0100 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0100 = null;
                            }
                            boolean isRemovingAdmin = isRemovingAdmin(_arg0100, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isRemovingAdmin ? 1 : 0);
                            return true;
                        case 210:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0101 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0101 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg112 = Bitmap.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg112 = null;
                            }
                            setUserIcon(_arg0101, _arg112);
                            reply.writeNoException();
                            return true;
                        case 211:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0102 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0102 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg113 = SystemUpdatePolicy.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg113 = null;
                            }
                            setSystemUpdatePolicy(_arg0102, _arg113);
                            reply.writeNoException();
                            return true;
                        case 212:
                            data.enforceInterface(DESCRIPTOR);
                            SystemUpdatePolicy _result61 = getSystemUpdatePolicy();
                            reply.writeNoException();
                            if (_result61 != null) {
                                reply.writeInt(1);
                                _result61.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 213:
                            data.enforceInterface(DESCRIPTOR);
                            clearSystemUpdatePolicyFreezePeriodRecord();
                            reply.writeNoException();
                            return true;
                        case 214:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0103 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0103 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            boolean keyguardDisabled = setKeyguardDisabled(_arg0103, _arg120);
                            reply.writeNoException();
                            reply.writeInt(keyguardDisabled ? 1 : 0);
                            return true;
                        case 215:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0104 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0104 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            boolean statusBarDisabled = setStatusBarDisabled(_arg0104, _arg120);
                            reply.writeNoException();
                            reply.writeInt(statusBarDisabled ? 1 : 0);
                            return true;
                        case 216:
                            data.enforceInterface(DESCRIPTOR);
                            boolean doNotAskCredentialsOnBoot = getDoNotAskCredentialsOnBoot();
                            reply.writeNoException();
                            reply.writeInt(doNotAskCredentialsOnBoot ? 1 : 0);
                            return true;
                        case 217:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0105 = SystemUpdateInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0105 = null;
                            }
                            notifyPendingSystemUpdate(_arg0105);
                            reply.writeNoException();
                            return true;
                        case 218:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0106 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0106 = null;
                            }
                            SystemUpdateInfo _result62 = getPendingSystemUpdate(_arg0106);
                            reply.writeNoException();
                            if (_result62 != null) {
                                reply.writeInt(1);
                                _result62.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 219:
                            return onTransact$setPermissionPolicy$(data, reply);
                        case 220:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0107 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0107 = null;
                            }
                            int _result63 = getPermissionPolicy(_arg0107);
                            reply.writeNoException();
                            reply.writeInt(_result63);
                            return true;
                        case 221:
                            return onTransact$setPermissionGrantState$(data, reply);
                        case 222:
                            return onTransact$getPermissionGrantState$(data, reply);
                        case 223:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0211 = data.readString();
                            boolean isProvisioningAllowed = isProvisioningAllowed(_arg0211, data.readString());
                            reply.writeNoException();
                            reply.writeInt(isProvisioningAllowed ? 1 : 0);
                            return true;
                        case 224:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0212 = data.readString();
                            int _result64 = checkProvisioningPreCondition(_arg0212, data.readString());
                            reply.writeNoException();
                            reply.writeInt(_result64);
                            return true;
                        case 225:
                            return onTransact$setKeepUninstalledPackages$(data, reply);
                        case 226:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0108 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0108 = null;
                            }
                            List<String> _result65 = getKeepUninstalledPackages(_arg0108, data.readString());
                            reply.writeNoException();
                            reply.writeStringList(_result65);
                            return true;
                        case 227:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0109 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0109 = null;
                            }
                            boolean isManagedProfile = isManagedProfile(_arg0109);
                            reply.writeNoException();
                            reply.writeInt(isManagedProfile ? 1 : 0);
                            return true;
                        case 228:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0110 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0110 = null;
                            }
                            String _result66 = getWifiMacAddress(_arg0110);
                            reply.writeNoException();
                            reply.writeString(_result66);
                            return true;
                        case 229:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0111 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0111 = null;
                            }
                            reboot(_arg0111);
                            reply.writeNoException();
                            return true;
                        case 230:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0112 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0112 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg114 = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg114 = null;
                            }
                            setShortSupportMessage(_arg0112, _arg114);
                            reply.writeNoException();
                            return true;
                        case 231:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0113 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0113 = null;
                            }
                            CharSequence _result67 = getShortSupportMessage(_arg0113);
                            reply.writeNoException();
                            if (_result67 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result67, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 232:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0114 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0114 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg115 = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg115 = null;
                            }
                            setLongSupportMessage(_arg0114, _arg115);
                            reply.writeNoException();
                            return true;
                        case 233:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0115 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0115 = null;
                            }
                            CharSequence _result68 = getLongSupportMessage(_arg0115);
                            reply.writeNoException();
                            if (_result68 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result68, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 234:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0116 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0116 = null;
                            }
                            CharSequence _result69 = getShortSupportMessageForUser(_arg0116, data.readInt());
                            reply.writeNoException();
                            if (_result69 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result69, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 235:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0117 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0117 = null;
                            }
                            CharSequence _result70 = getLongSupportMessageForUser(_arg0117, data.readInt());
                            reply.writeNoException();
                            if (_result70 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result70, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 236:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0213 = data.readInt();
                            boolean isSeparateProfileChallengeAllowed = isSeparateProfileChallengeAllowed(_arg0213);
                            reply.writeNoException();
                            reply.writeInt(isSeparateProfileChallengeAllowed ? 1 : 0);
                            return true;
                        case 237:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0118 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0118 = null;
                            }
                            setOrganizationColor(_arg0118, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 238:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0214 = data.readInt();
                            setOrganizationColorForUser(_arg0214, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 239:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0119 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0119 = null;
                            }
                            int _result71 = getOrganizationColor(_arg0119);
                            reply.writeNoException();
                            reply.writeInt(_result71);
                            return true;
                        case 240:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0215 = data.readInt();
                            int _result72 = getOrganizationColorForUser(_arg0215);
                            reply.writeNoException();
                            reply.writeInt(_result72);
                            return true;
                        case 241:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0120 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0120 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg116 = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg116 = null;
                            }
                            setOrganizationName(_arg0120, _arg116);
                            reply.writeNoException();
                            return true;
                        case 242:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0121 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0121 = null;
                            }
                            CharSequence _result73 = getOrganizationName(_arg0121);
                            reply.writeNoException();
                            if (_result73 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result73, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 243:
                            data.enforceInterface(DESCRIPTOR);
                            CharSequence _result74 = getDeviceOwnerOrganizationName();
                            reply.writeNoException();
                            if (_result74 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result74, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 244:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0216 = data.readInt();
                            CharSequence _result75 = getOrganizationNameForUser(_arg0216);
                            reply.writeNoException();
                            if (_result75 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result75, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 245:
                            data.enforceInterface(DESCRIPTOR);
                            int _result76 = getUserProvisioningState();
                            reply.writeNoException();
                            reply.writeInt(_result76);
                            return true;
                        case 246:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0217 = data.readInt();
                            setUserProvisioningState(_arg0217, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 247:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0122 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0122 = null;
                            }
                            setAffiliationIds(_arg0122, data.createStringArrayList());
                            reply.writeNoException();
                            return true;
                        case 248:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0123 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0123 = null;
                            }
                            List<String> _result77 = getAffiliationIds(_arg0123);
                            reply.writeNoException();
                            reply.writeStringList(_result77);
                            return true;
                        case 249:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isCallingUserAffiliated = isCallingUserAffiliated();
                            reply.writeNoException();
                            reply.writeInt(isCallingUserAffiliated ? 1 : 0);
                            return true;
                        case 250:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0218 = data.readInt();
                            boolean isAffiliatedUser = isAffiliatedUser(_arg0218);
                            reply.writeNoException();
                            reply.writeInt(isAffiliatedUser ? 1 : 0);
                            return true;
                        case 251:
                            return onTransact$setSecurityLoggingEnabled$(data, reply);
                        case 252:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0124 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0124 = null;
                            }
                            boolean isSecurityLoggingEnabled = isSecurityLoggingEnabled(_arg0124, data.readString());
                            reply.writeNoException();
                            reply.writeInt(isSecurityLoggingEnabled ? 1 : 0);
                            return true;
                        case 253:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0125 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0125 = null;
                            }
                            ParceledListSlice _result78 = retrieveSecurityLogs(_arg0125, data.readString());
                            reply.writeNoException();
                            if (_result78 != null) {
                                reply.writeInt(1);
                                _result78.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 254:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0126 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0126 = null;
                            }
                            ParceledListSlice _result79 = retrievePreRebootSecurityLogs(_arg0126, data.readString());
                            reply.writeNoException();
                            if (_result79 != null) {
                                reply.writeInt(1);
                                _result79.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 255:
                            data.enforceInterface(DESCRIPTOR);
                            long _result80 = forceNetworkLogs();
                            reply.writeNoException();
                            reply.writeLong(_result80);
                            return true;
                        case 256:
                            data.enforceInterface(DESCRIPTOR);
                            long _result81 = forceSecurityLogs();
                            reply.writeNoException();
                            reply.writeLong(_result81);
                            return true;
                        case 257:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0219 = data.readString();
                            boolean isUninstallInQueue = isUninstallInQueue(_arg0219);
                            reply.writeNoException();
                            reply.writeInt(isUninstallInQueue ? 1 : 0);
                            return true;
                        case 258:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0220 = data.readString();
                            uninstallPackageWithActiveAdmins(_arg0220);
                            reply.writeNoException();
                            return true;
                        case 259:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isDeviceProvisioned = isDeviceProvisioned();
                            reply.writeNoException();
                            reply.writeInt(isDeviceProvisioned ? 1 : 0);
                            return true;
                        case 260:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isDeviceProvisioningConfigApplied = isDeviceProvisioningConfigApplied();
                            reply.writeNoException();
                            reply.writeInt(isDeviceProvisioningConfigApplied ? 1 : 0);
                            return true;
                        case 261:
                            data.enforceInterface(DESCRIPTOR);
                            setDeviceProvisioningConfigApplied();
                            reply.writeNoException();
                            return true;
                        case 262:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0221 = data.readInt();
                            forceUpdateUserSetupComplete(_arg0221);
                            reply.writeNoException();
                            return true;
                        case 263:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0127 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0127 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setBackupServiceEnabled(_arg0127, _arg120);
                            reply.writeNoException();
                            return true;
                        case 264:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0128 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0128 = null;
                            }
                            boolean isBackupServiceEnabled = isBackupServiceEnabled(_arg0128);
                            reply.writeNoException();
                            reply.writeInt(isBackupServiceEnabled ? 1 : 0);
                            return true;
                        case 265:
                            return onTransact$setNetworkLoggingEnabled$(data, reply);
                        case 266:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0129 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0129 = null;
                            }
                            boolean isNetworkLoggingEnabled = isNetworkLoggingEnabled(_arg0129, data.readString());
                            reply.writeNoException();
                            reply.writeInt(isNetworkLoggingEnabled ? 1 : 0);
                            return true;
                        case 267:
                            return onTransact$retrieveNetworkLogs$(data, reply);
                        case 268:
                            return onTransact$bindDeviceAdminServiceAsUser$(data, reply);
                        case 269:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0130 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0130 = null;
                            }
                            List<UserHandle> _result82 = getBindDeviceAdminTargetUsers(_arg0130);
                            reply.writeNoException();
                            reply.writeTypedList(_result82);
                            return true;
                        case 270:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0131 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0131 = null;
                            }
                            boolean isEphemeralUser = isEphemeralUser(_arg0131);
                            reply.writeNoException();
                            reply.writeInt(isEphemeralUser ? 1 : 0);
                            return true;
                        case 271:
                            data.enforceInterface(DESCRIPTOR);
                            long _result83 = getLastSecurityLogRetrievalTime();
                            reply.writeNoException();
                            reply.writeLong(_result83);
                            return true;
                        case 272:
                            data.enforceInterface(DESCRIPTOR);
                            long _result84 = getLastBugReportRequestTime();
                            reply.writeNoException();
                            reply.writeLong(_result84);
                            return true;
                        case 273:
                            data.enforceInterface(DESCRIPTOR);
                            long _result85 = getLastNetworkLogRetrievalTime();
                            reply.writeNoException();
                            reply.writeLong(_result85);
                            return true;
                        case 274:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0132 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0132 = null;
                            }
                            boolean resetPasswordToken = setResetPasswordToken(_arg0132, data.createByteArray());
                            reply.writeNoException();
                            reply.writeInt(resetPasswordToken ? 1 : 0);
                            return true;
                        case 275:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0133 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0133 = null;
                            }
                            boolean clearResetPasswordToken = clearResetPasswordToken(_arg0133);
                            reply.writeNoException();
                            reply.writeInt(clearResetPasswordToken ? 1 : 0);
                            return true;
                        case 276:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0134 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0134 = null;
                            }
                            boolean isResetPasswordTokenActive = isResetPasswordTokenActive(_arg0134);
                            reply.writeNoException();
                            reply.writeInt(isResetPasswordTokenActive ? 1 : 0);
                            return true;
                        case 277:
                            return onTransact$resetPasswordWithToken$(data, reply);
                        case 278:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isCurrentInputMethodSetByOwner = isCurrentInputMethodSetByOwner();
                            reply.writeNoException();
                            reply.writeInt(isCurrentInputMethodSetByOwner ? 1 : 0);
                            return true;
                        case 279:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0135 = UserHandle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0135 = null;
                            }
                            StringParceledListSlice _result86 = getOwnerInstalledCaCerts(_arg0135);
                            reply.writeNoException();
                            if (_result86 != null) {
                                reply.writeInt(1);
                                _result86.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 280:
                            return onTransact$clearApplicationUserData$(data, reply);
                        case 281:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0136 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0136 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setLogoutEnabled(_arg0136, _arg120);
                            reply.writeNoException();
                            return true;
                        case 282:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isLogoutEnabled = isLogoutEnabled();
                            reply.writeNoException();
                            reply.writeInt(isLogoutEnabled ? 1 : 0);
                            return true;
                        case 283:
                            return onTransact$getDisallowedSystemApps$(data, reply);
                        case 284:
                            return onTransact$transferOwnership$(data, reply);
                        case 285:
                            data.enforceInterface(DESCRIPTOR);
                            PersistableBundle _result87 = getTransferOwnershipBundle();
                            reply.writeNoException();
                            if (_result87 != null) {
                                reply.writeInt(1);
                                _result87.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 286:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0137 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0137 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg117 = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg117 = null;
                            }
                            setStartUserSessionMessage(_arg0137, _arg117);
                            reply.writeNoException();
                            return true;
                        case 287:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0138 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0138 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg118 = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg118 = null;
                            }
                            setEndUserSessionMessage(_arg0138, _arg118);
                            reply.writeNoException();
                            return true;
                        case 288:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0139 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0139 = null;
                            }
                            CharSequence _result88 = getStartUserSessionMessage(_arg0139);
                            reply.writeNoException();
                            if (_result88 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result88, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 289:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0140 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0140 = null;
                            }
                            CharSequence _result89 = getEndUserSessionMessage(_arg0140);
                            reply.writeNoException();
                            if (_result89 != null) {
                                reply.writeInt(1);
                                TextUtils.writeToParcel(_result89, reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 290:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0141 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0141 = null;
                            }
                            List<String> _result90 = setMeteredDataDisabledPackages(_arg0141, data.createStringArrayList());
                            reply.writeNoException();
                            reply.writeStringList(_result90);
                            return true;
                        case 291:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0142 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0142 = null;
                            }
                            List<String> _result91 = getMeteredDataDisabledPackages(_arg0142);
                            reply.writeNoException();
                            reply.writeStringList(_result91);
                            return true;
                        case 292:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0143 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0143 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg119 = ApnSetting.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg119 = null;
                            }
                            int _result92 = addOverrideApn(_arg0143, _arg119);
                            reply.writeNoException();
                            reply.writeInt(_result92);
                            return true;
                        case 293:
                            return onTransact$updateOverrideApn$(data, reply);
                        case 294:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0144 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0144 = null;
                            }
                            boolean removeOverrideApn = removeOverrideApn(_arg0144, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(removeOverrideApn ? 1 : 0);
                            return true;
                        case 295:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0145 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0145 = null;
                            }
                            List<ApnSetting> _result93 = getOverrideApns(_arg0145);
                            reply.writeNoException();
                            reply.writeTypedList(_result93);
                            return true;
                        case 296:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0146 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0146 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setOverrideApnsEnabled(_arg0146, _arg120);
                            reply.writeNoException();
                            return true;
                        case 297:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0147 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0147 = null;
                            }
                            boolean isOverrideApnEnabled = isOverrideApnEnabled(_arg0147);
                            reply.writeNoException();
                            reply.writeInt(isOverrideApnEnabled ? 1 : 0);
                            return true;
                        case 298:
                            return onTransact$isMeteredDataDisabledPackageForUser$(data, reply);
                        case 299:
                            return onTransact$setGlobalPrivateDns$(data, reply);
                        case 300:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0148 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0148 = null;
                            }
                            int _result94 = getGlobalPrivateDnsMode(_arg0148);
                            reply.writeNoException();
                            reply.writeInt(_result94);
                            return true;
                        case 301:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0149 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0149 = null;
                            }
                            String _result95 = getGlobalPrivateDnsHost(_arg0149);
                            reply.writeNoException();
                            reply.writeString(_result95);
                            return true;
                        case 302:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0150 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0150 = null;
                            }
                            markProfileOwnerOnOrganizationOwnedDevice(_arg0150, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 303:
                            return onTransact$installUpdateFromFile$(data, reply);
                        case 304:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0151 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0151 = null;
                            }
                            setCrossProfileCalendarPackages(_arg0151, data.createStringArrayList());
                            reply.writeNoException();
                            return true;
                        case 305:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0152 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0152 = null;
                            }
                            List<String> _result96 = getCrossProfileCalendarPackages(_arg0152);
                            reply.writeNoException();
                            reply.writeStringList(_result96);
                            return true;
                        case 306:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0222 = data.readString();
                            boolean isPackageAllowedToAccessCalendarForUser = isPackageAllowedToAccessCalendarForUser(_arg0222, data.readInt());
                            reply.writeNoException();
                            reply.writeInt(isPackageAllowedToAccessCalendarForUser ? 1 : 0);
                            return true;
                        case 307:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0223 = data.readInt();
                            List<String> _result97 = getCrossProfileCalendarPackagesForUser(_arg0223);
                            reply.writeNoException();
                            reply.writeStringList(_result97);
                            return true;
                        case 308:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0153 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0153 = null;
                            }
                            setCrossProfilePackages(_arg0153, data.createStringArrayList());
                            reply.writeNoException();
                            return true;
                        case 309:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0154 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0154 = null;
                            }
                            List<String> _result98 = getCrossProfilePackages(_arg0154);
                            reply.writeNoException();
                            reply.writeStringList(_result98);
                            return true;
                        case 310:
                            data.enforceInterface(DESCRIPTOR);
                            List<String> _result99 = getAllCrossProfilePackages();
                            reply.writeNoException();
                            reply.writeStringList(_result99);
                            return true;
                        case 311:
                            data.enforceInterface(DESCRIPTOR);
                            List<String> _result100 = getDefaultCrossProfilePackages();
                            reply.writeNoException();
                            reply.writeStringList(_result100);
                            return true;
                        case 312:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isManagedKiosk = isManagedKiosk();
                            reply.writeNoException();
                            reply.writeInt(isManagedKiosk ? 1 : 0);
                            return true;
                        case 313:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isUnattendedManagedKiosk = isUnattendedManagedKiosk();
                            reply.writeNoException();
                            reply.writeInt(isUnattendedManagedKiosk ? 1 : 0);
                            return true;
                        case 314:
                            return onTransact$startViewCalendarEventInManagedProfile$(data, reply);
                        case 315:
                            return onTransact$setKeyGrantForApp$(data, reply);
                        case 316:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0224 = data.readString();
                            ParcelableGranteeMap _result101 = getKeyPairGrants(_arg0224, data.readString());
                            reply.writeNoException();
                            if (_result101 != null) {
                                reply.writeInt(1);
                                _result101.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 317:
                            return onTransact$setKeyGrantToWifiAuth$(data, reply);
                        case 318:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0225 = data.readString();
                            boolean isKeyPairGrantedToWifiAuth = isKeyPairGrantedToWifiAuth(_arg0225, data.readString());
                            reply.writeNoException();
                            reply.writeInt(isKeyPairGrantedToWifiAuth ? 1 : 0);
                            return true;
                        case 319:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0155 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0155 = null;
                            }
                            setUserControlDisabledPackages(_arg0155, data.createStringArrayList());
                            reply.writeNoException();
                            return true;
                        case 320:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0156 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0156 = null;
                            }
                            List<String> _result102 = getUserControlDisabledPackages(_arg0156);
                            reply.writeNoException();
                            reply.writeStringList(_result102);
                            return true;
                        case 321:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0157 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0157 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setCommonCriteriaModeEnabled(_arg0157, _arg120);
                            reply.writeNoException();
                            return true;
                        case 322:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0158 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0158 = null;
                            }
                            boolean isCommonCriteriaModeEnabled = isCommonCriteriaModeEnabled(_arg0158);
                            reply.writeNoException();
                            reply.writeInt(isCommonCriteriaModeEnabled ? 1 : 0);
                            return true;
                        case 323:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0159 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0159 = null;
                            }
                            int _result103 = getPersonalAppsSuspendedReasons(_arg0159);
                            reply.writeNoException();
                            reply.writeInt(_result103);
                            return true;
                        case 324:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0160 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0160 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setPersonalAppsSuspended(_arg0160, _arg120);
                            reply.writeNoException();
                            return true;
                        case 325:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0161 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0161 = null;
                            }
                            long _result104 = getManagedProfileMaximumTimeOff(_arg0161);
                            reply.writeNoException();
                            reply.writeLong(_result104);
                            return true;
                        case 326:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0162 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0162 = null;
                            }
                            setManagedProfileMaximumTimeOff(_arg0162, data.readLong());
                            reply.writeNoException();
                            return true;
                        case 327:
                            data.enforceInterface(DESCRIPTOR);
                            acknowledgeDeviceCompliant();
                            reply.writeNoException();
                            return true;
                        case 328:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isComplianceAcknowledgementRequired = isComplianceAcknowledgementRequired();
                            reply.writeNoException();
                            reply.writeInt(isComplianceAcknowledgementRequired ? 1 : 0);
                            return true;
                        case 329:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0226 = data.readInt();
                            boolean canProfileOwnerResetPasswordWhenLocked = canProfileOwnerResetPasswordWhenLocked(_arg0226);
                            reply.writeNoException();
                            reply.writeInt(canProfileOwnerResetPasswordWhenLocked ? 1 : 0);
                            return true;
                        case 330:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0227 = data.readInt();
                            setNextOperationSafety(_arg0227, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 331:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0228 = data.readInt();
                            boolean isSafeOperation = isSafeOperation(_arg0228);
                            reply.writeNoException();
                            reply.writeInt(isSafeOperation ? 1 : 0);
                            return true;
                        case 332:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0229 = data.readString();
                            String _result105 = getEnrollmentSpecificId(_arg0229);
                            reply.writeNoException();
                            reply.writeString(_result105);
                            return true;
                        case 333:
                            return onTransact$setOrganizationIdForUser$(data, reply);
                        case 334:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0163 = ManagedProfileProvisioningParams.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0163 = null;
                            }
                            UserHandle _result106 = createAndProvisionManagedProfile(_arg0163, data.readString());
                            reply.writeNoException();
                            if (_result106 != null) {
                                reply.writeInt(1);
                                _result106.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 335:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0164 = FullyManagedDeviceProvisioningParams.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0164 = null;
                            }
                            provisionFullyManagedDevice(_arg0164, data.readString());
                            reply.writeNoException();
                            return true;
                        case 336:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0165 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0165 = null;
                            }
                            setDeviceOwnerType(_arg0165, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 337:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0166 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0166 = null;
                            }
                            int _result107 = getDeviceOwnerType(_arg0166);
                            reply.writeNoException();
                            reply.writeInt(_result107);
                            return true;
                        case 338:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0230 = data.readInt();
                            resetDefaultCrossProfileIntentFilters(_arg0230);
                            reply.writeNoException();
                            return true;
                        case 339:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0231 = data.readInt();
                            boolean canAdminGrantSensorsPermissionsForUser = canAdminGrantSensorsPermissionsForUser(_arg0231);
                            reply.writeNoException();
                            reply.writeInt(canAdminGrantSensorsPermissionsForUser ? 1 : 0);
                            return true;
                        case 340:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0232 = data.readString();
                            if (data.readInt() != 0) {
                                _arg120 = true;
                            }
                            setUsbDataSignalingEnabled(_arg0232, _arg120);
                            reply.writeNoException();
                            return true;
                        case 341:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg0233 = data.readString();
                            boolean isUsbDataSignalingEnabled = isUsbDataSignalingEnabled(_arg0233);
                            reply.writeNoException();
                            reply.writeInt(isUsbDataSignalingEnabled ? 1 : 0);
                            return true;
                        case 342:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg0234 = data.readInt();
                            boolean isUsbDataSignalingEnabledForUser = isUsbDataSignalingEnabledForUser(_arg0234);
                            reply.writeNoException();
                            reply.writeInt(isUsbDataSignalingEnabledForUser ? 1 : 0);
                            return true;
                        case 343:
                            data.enforceInterface(DESCRIPTOR);
                            boolean canUsbDataSignalingBeDisabled = canUsbDataSignalingBeDisabled();
                            reply.writeNoException();
                            reply.writeInt(canUsbDataSignalingBeDisabled ? 1 : 0);
                            return true;
                        case 344:
                            data.enforceInterface(DESCRIPTOR);
                            List<UserHandle> _result108 = listForegroundAffiliatedUsers();
                            reply.writeNoException();
                            reply.writeTypedList(_result108);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IDevicePolicyManager {
            public static IDevicePolicyManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordQuality(ComponentName who, int quality, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(quality);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordQuality(who, quality, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordQuality(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordQuality(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumLength(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumLength(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumUpperCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumUpperCase(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumUpperCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumUpperCase(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumLowerCase(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumLowerCase(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumLowerCase(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumLowerCase(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumLetters(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumLetters(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumLetters(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumLetters(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumNumeric(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumNumeric(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumNumeric(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumNumeric(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumSymbols(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumSymbols(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumSymbols(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumSymbols(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordMinimumNonLetter(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordMinimumNonLetter(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordMinimumNonLetter(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumNonLetter(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public PasswordMetrics getPasswordMinimumMetrics(int userHandle, boolean deviceWideOnly) throws RemoteException {
                PasswordMetrics _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(deviceWideOnly ? 1 : 0);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordMinimumMetrics(userHandle, deviceWideOnly);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PasswordMetrics.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordHistoryLength(ComponentName who, int length, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(length);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordHistoryLength(who, length, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordHistoryLength(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordHistoryLength(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPasswordExpirationTimeout(ComponentName who, long expiration, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(expiration);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPasswordExpirationTimeout(who, expiration, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getPasswordExpirationTimeout(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordExpirationTimeout(who, userHandle, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getPasswordExpiration(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordExpiration(who, userHandle, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isActivePasswordSufficient(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isActivePasswordSufficient(userHandle, parent);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isActivePasswordSufficientForDeviceRequirement() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isActivePasswordSufficientForDeviceRequirement();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isPasswordSufficientAfterProfileUnification(int userHandle, int profileUser) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(profileUser);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPasswordSufficientAfterProfileUnification(userHandle, profileUser);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPasswordComplexity(boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPasswordComplexity(parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setRequiredPasswordComplexity(int passwordComplexity, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(passwordComplexity);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRequiredPasswordComplexity(passwordComplexity, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getRequiredPasswordComplexity(boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRequiredPasswordComplexity(parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getAggregatedPasswordComplexityForUser(int userId, boolean deviceWideOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(deviceWideOnly ? 1 : 0);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAggregatedPasswordComplexityForUser(userId, deviceWideOnly);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUsingUnifiedPassword(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUsingUnifiedPassword(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getCurrentFailedPasswordAttempts(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentFailedPasswordAttempts(userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getProfileWithMinimumFailedPasswordsForWipe(int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileWithMinimumFailedPasswordsForWipe(userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void finalizeWorkProfileProvisioning(UserHandle managedProfileUser, Account migratedAccount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (managedProfileUser != null) {
                        _data.writeInt(1);
                        managedProfileUser.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (migratedAccount != null) {
                        _data.writeInt(1);
                        migratedAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().finalizeWorkProfileProvisioning(managedProfileUser, migratedAccount);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setMaximumFailedPasswordsForWipe(ComponentName admin, int num, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(num);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMaximumFailedPasswordsForWipe(admin, num, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getMaximumFailedPasswordsForWipe(ComponentName admin, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaximumFailedPasswordsForWipe(admin, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean resetPassword(String password, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    _data.writeInt(flags);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resetPassword(password, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setMaximumTimeToLock(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMaximumTimeToLock(who, timeMs, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getMaximumTimeToLock(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMaximumTimeToLock(who, userHandle, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setRequiredStrongAuthTimeout(ComponentName who, long timeMs, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeMs);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRequiredStrongAuthTimeout(who, timeMs, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getRequiredStrongAuthTimeout(ComponentName who, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRequiredStrongAuthTimeout(who, userId, parent);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void lockNow(int flags, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().lockNow(flags, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void wipeDataWithReason(int flags, String wipeReasonForUser, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeString(wipeReasonForUser);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().wipeDataWithReason(flags, wipeReasonForUser, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setFactoryResetProtectionPolicy(ComponentName who, FactoryResetProtectionPolicy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setFactoryResetProtectionPolicy(who, policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public FactoryResetProtectionPolicy getFactoryResetProtectionPolicy(ComponentName who) throws RemoteException {
                FactoryResetProtectionPolicy _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getFactoryResetProtectionPolicy(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = FactoryResetProtectionPolicy.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isFactoryResetProtectionPolicySupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isFactoryResetProtectionPolicySupported();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName setGlobalProxy(ComponentName admin, String proxySpec, String exclusionList) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(proxySpec);
                    _data.writeString(exclusionList);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setGlobalProxy(admin, proxySpec, exclusionList);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getGlobalProxyAdmin(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGlobalProxyAdmin(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setRecommendedGlobalProxy(ComponentName admin, ProxyInfo proxyInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (proxyInfo != null) {
                        _data.writeInt(1);
                        proxyInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRecommendedGlobalProxy(admin, proxyInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int setStorageEncryption(ComponentName who, boolean encrypt) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!encrypt) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setStorageEncryption(who, encrypt);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getStorageEncryption(ComponentName who, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStorageEncryption(who, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getStorageEncryptionStatus(String callerPackage, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStorageEncryptionStatus(callerPackage, userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean requestBugreport(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().requestBugreport(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCameraDisabled(ComponentName who, boolean disabled, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCameraDisabled(who, disabled, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCameraDisabled(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCameraDisabled(who, userHandle, parent);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setScreenCaptureDisabled(ComponentName who, boolean disabled, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setScreenCaptureDisabled(who, disabled, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getScreenCaptureDisabled(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getScreenCaptureDisabled(who, userHandle, parent);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setNearbyNotificationStreamingPolicy(int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(policy);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNearbyNotificationStreamingPolicy(policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getNearbyNotificationStreamingPolicy(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNearbyNotificationStreamingPolicy(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setNearbyAppStreamingPolicy(int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(policy);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNearbyAppStreamingPolicy(policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getNearbyAppStreamingPolicy(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNearbyAppStreamingPolicy(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setKeyguardDisabledFeatures(ComponentName who, int which, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(which);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setKeyguardDisabledFeatures(who, which, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getKeyguardDisabledFeatures(ComponentName who, int userHandle, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKeyguardDisabledFeatures(who, userHandle, parent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setActiveAdmin(ComponentName policyReceiver, boolean refreshing, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!refreshing) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setActiveAdmin(policyReceiver, refreshing, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAdminActive(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAdminActive(policyReceiver, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<ComponentName> getActiveAdmins(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActiveAdmins(userHandle);
                    }
                    _reply.readException();
                    List<ComponentName> _result = _reply.createTypedArrayList(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean packageHasActiveAdmins(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().packageHasActiveAdmins(packageName, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void getRemoveWarning(ComponentName policyReceiver, RemoteCallback result, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getRemoveWarning(policyReceiver, result, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void removeActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeActiveAdmin(policyReceiver, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void forceRemoveActiveAdmin(ComponentName policyReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceRemoveActiveAdmin(policyReceiver, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasGrantedPolicy(ComponentName policyReceiver, int usesPolicy, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (policyReceiver != null) {
                        _data.writeInt(1);
                        policyReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(usesPolicy);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasGrantedPolicy(policyReceiver, usesPolicy, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportPasswordChanged(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportPasswordChanged(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportFailedPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportFailedPasswordAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportSuccessfulPasswordAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportSuccessfulPasswordAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportFailedBiometricAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportFailedBiometricAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportSuccessfulBiometricAttempt(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportSuccessfulBiometricAttempt(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportKeyguardDismissed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportKeyguardDismissed(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reportKeyguardSecured(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportKeyguardSecured(userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setDeviceOwner(ComponentName who, String ownerName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setDeviceOwner(who, ownerName, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getDeviceOwnerComponent(boolean callingUserOnly) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUserOnly ? 1 : 0);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerComponent(callingUserOnly);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasDeviceOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasDeviceOwner();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getDeviceOwnerName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerName();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearDeviceOwner(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getDeviceOwnerUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerUserId();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setProfileOwner(ComponentName who, String ownerName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(ownerName);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setProfileOwner(who, ownerName, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getProfileOwnerAsUser(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileOwnerAsUser(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getProfileOwnerOrDeviceOwnerSupervisionComponent(UserHandle userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileOwnerOrDeviceOwnerSupervisionComponent(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getProfileOwnerName(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProfileOwnerName(userHandle);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setProfileEnabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProfileEnabled(who);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setProfileName(ComponentName who, String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(profileName);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProfileName(who, profileName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearProfileOwner(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearProfileOwner(who);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasUserSetupCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasUserSetupCompleted();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isOrganizationOwnedDeviceWithManagedProfile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isOrganizationOwnedDeviceWithManagedProfile();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean checkDeviceIdentifierAccess(String packageName, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkDeviceIdentifierAccess(packageName, pid, uid);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDeviceOwnerLockScreenInfo(ComponentName who, CharSequence deviceOwnerInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deviceOwnerInfo != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(deviceOwnerInfo, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceOwnerLockScreenInfo(who, deviceOwnerInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getDeviceOwnerLockScreenInfo() throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerLockScreenInfo();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String[] setPackagesSuspended(ComponentName admin, String callerPackage, String[] packageNames, boolean suspended) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringArray(packageNames);
                    if (!suspended) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPackagesSuspended(admin, callerPackage, packageNames, suspended);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isPackageSuspended(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageSuspended(admin, callerPackage, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> listPolicyExemptApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().listPolicyExemptApps();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean installCaCert(ComponentName admin, String callerPackage, byte[] certBuffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeByteArray(certBuffer);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().installCaCert(admin, callerPackage, certBuffer);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void uninstallCaCerts(ComponentName admin, String callerPackage, String[] aliases) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringArray(aliases);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().uninstallCaCerts(admin, callerPackage, aliases);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void enforceCanManageCaCerts(ComponentName admin, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enforceCanManageCaCerts(admin, callerPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean approveCaCert(String alias, int userHandle, boolean approval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    _data.writeInt(approval ? 1 : 0);
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().approveCaCert(alias, userHandle, approval);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCaCertApproved(String alias, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(userHandle);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCaCertApproved(alias, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean installKeyPair(ComponentName who, String callerPackage, byte[] privKeyBuffer, byte[] certBuffer, byte[] certChainBuffer, String alias, boolean requestAccess, boolean isUserSelectable) throws RemoteException {
                boolean _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    _data.writeByteArray(privKeyBuffer);
                    try {
                        _data.writeByteArray(certBuffer);
                        _data.writeByteArray(certChainBuffer);
                        _data.writeString(alias);
                        _data.writeInt(requestAccess ? 1 : 0);
                        _data.writeInt(isUserSelectable ? 1 : 0);
                        boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            boolean installKeyPair = Stub.getDefaultImpl().installKeyPair(who, callerPackage, privKeyBuffer, certBuffer, certChainBuffer, alias, requestAccess, isUserSelectable);
                            _reply.recycle();
                            _data.recycle();
                            return installKeyPair;
                        }
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean removeKeyPair(ComponentName who, String callerPackage, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeKeyPair(who, callerPackage, alias);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasKeyPair(String callerPackage, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasKeyPair(callerPackage, alias);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean generateKeyPair(ComponentName who, String callerPackage, String algorithm, ParcelableKeyGenParameterSpec keySpec, int idAttestationFlags, KeymasterCertificateChain attestationChain) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                        try {
                            _data.writeString(algorithm);
                            if (keySpec != null) {
                                _data.writeInt(1);
                                keySpec.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(idAttestationFlags);
                                try {
                                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        boolean generateKeyPair = Stub.getDefaultImpl().generateKeyPair(who, callerPackage, algorithm, keySpec, idAttestationFlags, attestationChain);
                                        _reply.recycle();
                                        _data.recycle();
                                        return generateKeyPair;
                                    }
                                    _reply.readException();
                                    if (_reply.readInt() == 0) {
                                        _result = false;
                                    }
                                    if (_reply.readInt() != 0) {
                                        try {
                                            attestationChain.readFromParcel(_reply);
                                        } catch (Throwable th) {
                                            th = th;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th2) {
                                    th = th2;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setKeyPairCertificate(ComponentName who, String callerPackage, String alias, byte[] certBuffer, byte[] certChainBuffer, boolean isUserSelectable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                        try {
                            _data.writeString(alias);
                            try {
                                _data.writeByteArray(certBuffer);
                                try {
                                    _data.writeByteArray(certChainBuffer);
                                    _data.writeInt(isUserSelectable ? 1 : 0);
                                    try {
                                        boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                                        if (!_status && Stub.getDefaultImpl() != null) {
                                            boolean keyPairCertificate = Stub.getDefaultImpl().setKeyPairCertificate(who, callerPackage, alias, certBuffer, certChainBuffer, isUserSelectable);
                                            _reply.recycle();
                                            _data.recycle();
                                            return keyPairCertificate;
                                        }
                                        _reply.readException();
                                        if (_reply.readInt() == 0) {
                                            _result = false;
                                        }
                                        _reply.recycle();
                                        _data.recycle();
                                        return _result;
                                    } catch (Throwable th) {
                                        th = th;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void choosePrivateKeyAlias(int uid, Uri uri, String alias, IBinder aliasCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(alias);
                    _data.writeStrongBinder(aliasCallback);
                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().choosePrivateKeyAlias(uid, uri, alias, aliasCallback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDelegatedScopes(ComponentName who, String delegatePackage, List<String> scopes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(delegatePackage);
                    _data.writeStringList(scopes);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDelegatedScopes(who, delegatePackage, scopes);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getDelegatedScopes(ComponentName who, String delegatePackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(delegatePackage);
                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDelegatedScopes(who, delegatePackage);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getDelegatePackages(ComponentName who, String scope) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(scope);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDelegatePackages(who, scope);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCertInstallerPackage(ComponentName who, String installerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(installerPackage);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCertInstallerPackage(who, installerPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getCertInstallerPackage(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCertInstallerPackage(who);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setAlwaysOnVpnPackage(ComponentName who, String vpnPackage, boolean lockdown, List<String> lockdownAllowlist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(vpnPackage);
                    _data.writeInt(lockdown ? 1 : 0);
                    _data.writeStringList(lockdownAllowlist);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setAlwaysOnVpnPackage(who, vpnPackage, lockdown, lockdownAllowlist);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getAlwaysOnVpnPackage(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAlwaysOnVpnPackage(who);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getAlwaysOnVpnPackageForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAlwaysOnVpnPackageForUser(userHandle);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAlwaysOnVpnLockdownEnabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAlwaysOnVpnLockdownEnabled(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAlwaysOnVpnLockdownEnabledForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAlwaysOnVpnLockdownEnabledForUser(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getAlwaysOnVpnLockdownAllowlist(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAlwaysOnVpnLockdownAllowlist(who);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void addPersistentPreferredActivity(ComponentName admin, IntentFilter filter, ComponentName activity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addPersistentPreferredActivity(admin, filter, activity);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearPackagePersistentPreferredActivities(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearPackagePersistentPreferredActivities(admin, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDefaultSmsApplication(ComponentName admin, String packageName, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDefaultSmsApplication(admin, packageName, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setApplicationRestrictions(ComponentName who, String callerPackage, String packageName, Bundle settings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (settings != null) {
                        _data.writeInt(1);
                        settings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setApplicationRestrictions(who, callerPackage, packageName, settings);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public Bundle getApplicationRestrictions(ComponentName who, String callerPackage, String packageName) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getApplicationRestrictions(who, callerPackage, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setApplicationRestrictionsManagingPackage(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setApplicationRestrictionsManagingPackage(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getApplicationRestrictionsManagingPackage(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getApplicationRestrictionsManagingPackage(admin);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCallerApplicationRestrictionsManagingPackage(String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCallerApplicationRestrictionsManagingPackage(callerPackage);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setRestrictionsProvider(ComponentName who, ComponentName provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRestrictionsProvider(who, provider);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ComponentName getRestrictionsProvider(int userHandle) throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRestrictionsProvider(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUserRestriction(ComponentName who, String key, boolean enable, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(key);
                    _data.writeInt(enable ? 1 : 0);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserRestriction(who, key, enable, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public Bundle getUserRestrictions(ComponentName who, boolean parent) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUserRestrictions(who, parent);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void addCrossProfileIntentFilter(ComponentName admin, IntentFilter filter, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (filter != null) {
                        _data.writeInt(1);
                        filter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addCrossProfileIntentFilter(admin, filter, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearCrossProfileIntentFilters(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearCrossProfileIntentFilters(admin);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setPermittedAccessibilityServices(ComponentName admin, List packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeList(packageList);
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPermittedAccessibilityServices(admin, packageList);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List getPermittedAccessibilityServices(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedAccessibilityServices(admin);
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List getPermittedAccessibilityServicesForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedAccessibilityServicesForUser(userId);
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAccessibilityServicePermittedByAdmin(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAccessibilityServicePermittedByAdmin(admin, packageName, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setPermittedInputMethods(ComponentName admin, List packageList, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeList(packageList);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPermittedInputMethods(admin, packageList, parent);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List getPermittedInputMethods(ComponentName admin, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedInputMethods(admin, parent);
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List getPermittedInputMethodsForCurrentUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(141, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedInputMethodsForCurrentUser();
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    List _result = _reply.readArrayList(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isInputMethodPermittedByAdmin(ComponentName admin, String packageName, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInputMethodPermittedByAdmin(admin, packageName, userId, parent);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setPermittedCrossProfileNotificationListeners(ComponentName admin, List<String> packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageList);
                    boolean _status = this.mRemote.transact(143, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPermittedCrossProfileNotificationListeners(admin, packageList);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getPermittedCrossProfileNotificationListeners(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermittedCrossProfileNotificationListeners(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isNotificationListenerServicePermitted(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isNotificationListenerServicePermitted(packageName, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public Intent createAdminSupportIntent(String restriction) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(restriction);
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createAdminSupportIntent(restriction);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public Bundle getEnforcingAdminAndUserDetails(int userId, String restriction) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(restriction);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEnforcingAdminAndUserDetails(userId, restriction);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean hidden, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                        try {
                            _data.writeString(packageName);
                            _data.writeInt(hidden ? 1 : 0);
                            _data.writeInt(parent ? 1 : 0);
                            try {
                                boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    boolean applicationHidden = Stub.getDefaultImpl().setApplicationHidden(admin, callerPackage, packageName, hidden, parent);
                                    _reply.recycle();
                                    _data.recycle();
                                    return applicationHidden;
                                }
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isApplicationHidden(ComponentName admin, String callerPackage, String packageName, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isApplicationHidden(admin, callerPackage, packageName, parent);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public UserHandle createAndManageUser(ComponentName who, String name, ComponentName profileOwner, PersistableBundle adminExtras, int flags) throws RemoteException {
                UserHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(name);
                    if (profileOwner != null) {
                        _data.writeInt(1);
                        profileOwner.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (adminExtras != null) {
                        _data.writeInt(1);
                        adminExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createAndManageUser(who, name, profileOwner, adminExtras, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UserHandle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean removeUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeUser(who, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean switchUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().switchUser(who, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int startUserInBackground(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startUserInBackground(who, userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int stopUser(ComponentName who, UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopUser(who, userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int logoutUser(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().logoutUser(who);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<UserHandle> getSecondaryUsers(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSecondaryUsers(who);
                    }
                    _reply.readException();
                    List<UserHandle> _result = _reply.createTypedArrayList(UserHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void resetNewUserDisclaimer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetNewUserDisclaimer();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void enableSystemApp(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enableSystemApp(admin, callerPackage, packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int enableSystemAppWithIntent(ComponentName admin, String callerPackage, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enableSystemAppWithIntent(admin, callerPackage, intent);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean installExistingPackage(ComponentName admin, String callerPackage, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().installExistingPackage(admin, callerPackage, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAccountManagementDisabled(ComponentName who, String accountType, boolean disabled, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(accountType);
                    _data.writeInt(disabled ? 1 : 0);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAccountManagementDisabled(who, accountType, disabled, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String[] getAccountTypesWithManagementDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAccountTypesWithManagementDisabled();
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String[] getAccountTypesWithManagementDisabledAsUser(int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(parent ? 1 : 0);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAccountTypesWithManagementDisabledAsUser(userId, parent);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSecondaryLockscreenEnabled(ComponentName who, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSecondaryLockscreenEnabled(who, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isSecondaryLockscreenEnabled(UserHandle userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (userHandle != null) {
                        _data.writeInt(1);
                        userHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSecondaryLockscreenEnabled(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPreferentialNetworkServiceEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPreferentialNetworkServiceEnabled(enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isPreferentialNetworkServiceEnabled(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPreferentialNetworkServiceEnabled(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLockTaskPackages(ComponentName who, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLockTaskPackages(who, packages);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String[] getLockTaskPackages(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLockTaskPackages(who);
                    }
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isLockTaskPermitted(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLockTaskPermitted(pkg);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLockTaskFeatures(ComponentName who, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLockTaskFeatures(who, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getLockTaskFeatures(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLockTaskFeatures(who);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setGlobalSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setGlobalSetting(who, setting, value);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSystemSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSystemSetting(who, setting, value);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSecureSetting(ComponentName who, String setting, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(setting);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSecureSetting(who, setting, value);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setConfiguredNetworksLockdownState(ComponentName who, boolean lockdown) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!lockdown) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setConfiguredNetworksLockdownState(who, lockdown);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean hasLockdownAdminConfiguredNetworks(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasLockdownAdminConfiguredNetworks(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLocationEnabled(ComponentName who, boolean locationEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!locationEnabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLocationEnabled(who, locationEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setTime(ComponentName who, long millis) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(millis);
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setTime(who, millis);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setTimeZone(ComponentName who, String timeZone) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(timeZone);
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setTimeZone(who, timeZone);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setMasterVolumeMuted(ComponentName admin, boolean on) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!on) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMasterVolumeMuted(admin, on);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isMasterVolumeMuted(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMasterVolumeMuted(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void notifyLockTaskModeChanged(boolean isEnabled, String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyLockTaskModeChanged(isEnabled, pkg, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUninstallBlocked(ComponentName admin, String callerPackage, String packageName, boolean uninstallBlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    if (!uninstallBlocked) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(184, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUninstallBlocked(admin, callerPackage, packageName, uninstallBlocked);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUninstallBlocked(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(185, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUninstallBlocked(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCrossProfileCallerIdDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(186, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCrossProfileCallerIdDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileCallerIdDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(187, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCallerIdDisabled(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileCallerIdDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(188, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCallerIdDisabledForUser(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCrossProfileContactsSearchDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(189, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCrossProfileContactsSearchDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileContactsSearchDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(190, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileContactsSearchDisabled(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getCrossProfileContactsSearchDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(191, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileContactsSearchDisabledForUser(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void startManagedQuickContact(String lookupKey, long contactId, boolean isContactIdIgnored, long directoryId, Intent originalIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(lookupKey);
                    try {
                        _data.writeLong(contactId);
                        _data.writeInt(isContactIdIgnored ? 1 : 0);
                        _data.writeLong(directoryId);
                        if (originalIntent != null) {
                            _data.writeInt(1);
                            originalIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            boolean _status = this.mRemote.transact(192, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().startManagedQuickContact(lookupKey, contactId, isContactIdIgnored, directoryId, originalIntent);
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setBluetoothContactSharingDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!disabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(193, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBluetoothContactSharingDisabled(who, disabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getBluetoothContactSharingDisabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(194, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBluetoothContactSharingDisabled(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getBluetoothContactSharingDisabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(195, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBluetoothContactSharingDisabledForUser(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setTrustAgentConfiguration(ComponentName admin, ComponentName agent, PersistableBundle args, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (agent != null) {
                        _data.writeInt(1);
                        agent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(196, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTrustAgentConfiguration(admin, agent, args, parent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<PersistableBundle> getTrustAgentConfiguration(ComponentName admin, ComponentName agent, int userId, boolean parent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (agent != null) {
                        _data.writeInt(1);
                        agent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!parent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(197, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTrustAgentConfiguration(admin, agent, userId, parent);
                    }
                    _reply.readException();
                    List<PersistableBundle> _result = _reply.createTypedArrayList(PersistableBundle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean addCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(198, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addCrossProfileWidgetProvider(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean removeCrossProfileWidgetProvider(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(199, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeCrossProfileWidgetProvider(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getCrossProfileWidgetProviders(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(200, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileWidgetProviders(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAutoTimeRequired(ComponentName who, boolean required) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!required) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(201, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAutoTimeRequired(who, required);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getAutoTimeRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(202, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAutoTimeRequired();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAutoTimeEnabled(ComponentName who, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(203, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAutoTimeEnabled(who, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getAutoTimeEnabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(204, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAutoTimeEnabled(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAutoTimeZoneEnabled(ComponentName who, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(205, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAutoTimeZoneEnabled(who, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getAutoTimeZoneEnabled(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(206, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAutoTimeZoneEnabled(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setForceEphemeralUsers(ComponentName who, boolean forceEpehemeralUsers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!forceEpehemeralUsers) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(207, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setForceEphemeralUsers(who, forceEpehemeralUsers);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getForceEphemeralUsers(ComponentName who) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(208, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getForceEphemeralUsers(who);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isRemovingAdmin(ComponentName adminReceiver, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (adminReceiver != null) {
                        _data.writeInt(1);
                        adminReceiver.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(209, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isRemovingAdmin(adminReceiver, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUserIcon(ComponentName admin, Bitmap icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(210, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserIcon(admin, icon);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSystemUpdatePolicy(ComponentName who, SystemUpdatePolicy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(211, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSystemUpdatePolicy(who, policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public SystemUpdatePolicy getSystemUpdatePolicy() throws RemoteException {
                SystemUpdatePolicy _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(212, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSystemUpdatePolicy();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SystemUpdatePolicy.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearSystemUpdatePolicyFreezePeriodRecord() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(213, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearSystemUpdatePolicyFreezePeriodRecord();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setKeyguardDisabled(ComponentName admin, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(214, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setKeyguardDisabled(admin, disabled);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setStatusBarDisabled(ComponentName who, boolean disabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(disabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(215, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setStatusBarDisabled(who, disabled);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean getDoNotAskCredentialsOnBoot() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(216, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDoNotAskCredentialsOnBoot();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void notifyPendingSystemUpdate(SystemUpdateInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(217, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPendingSystemUpdate(info);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public SystemUpdateInfo getPendingSystemUpdate(ComponentName admin) throws RemoteException {
                SystemUpdateInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(218, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPendingSystemUpdate(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SystemUpdateInfo.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPermissionPolicy(ComponentName admin, String callerPackage, int policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeInt(policy);
                    boolean _status = this.mRemote.transact(219, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPermissionPolicy(admin, callerPackage, policy);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPermissionPolicy(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(220, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionPolicy(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission, int grantState, RemoteCallback resultReceiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                        try {
                            _data.writeString(packageName);
                            try {
                                _data.writeString(permission);
                                try {
                                    _data.writeInt(grantState);
                                    if (resultReceiver != null) {
                                        _data.writeInt(1);
                                        resultReceiver.writeToParcel(_data, 0);
                                    } else {
                                        _data.writeInt(0);
                                    }
                                    boolean _status = this.mRemote.transact(221, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        Stub.getDefaultImpl().setPermissionGrantState(admin, callerPackage, packageName, permission, grantState, resultReceiver);
                                        _reply.recycle();
                                        _data.recycle();
                                        return;
                                    }
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                } catch (Throwable th) {
                                    th = th;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPermissionGrantState(ComponentName admin, String callerPackage, String packageName, String permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    boolean _status = this.mRemote.transact(222, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPermissionGrantState(admin, callerPackage, packageName, permission);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isProvisioningAllowed(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(223, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isProvisioningAllowed(action, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int checkProvisioningPreCondition(String action, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(224, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkProvisioningPreCondition(action, packageName);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setKeepUninstalledPackages(ComponentName admin, String callerPackage, List<String> packageList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    _data.writeStringList(packageList);
                    boolean _status = this.mRemote.transact(225, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setKeepUninstalledPackages(admin, callerPackage, packageList);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getKeepUninstalledPackages(ComponentName admin, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    boolean _status = this.mRemote.transact(226, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKeepUninstalledPackages(admin, callerPackage);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isManagedProfile(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(227, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isManagedProfile(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getWifiMacAddress(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(228, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWifiMacAddress(admin);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void reboot(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(229, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reboot(admin);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setShortSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(230, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setShortSupportMessage(admin, message);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getShortSupportMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(231, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getShortSupportMessage(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLongSupportMessage(ComponentName admin, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(232, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLongSupportMessage(admin, message);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getLongSupportMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(233, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLongSupportMessage(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getShortSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(234, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getShortSupportMessageForUser(admin, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getLongSupportMessageForUser(ComponentName admin, int userHandle) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(235, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLongSupportMessageForUser(admin, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isSeparateProfileChallengeAllowed(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(236, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSeparateProfileChallengeAllowed(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOrganizationColor(ComponentName admin, int color) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(color);
                    boolean _status = this.mRemote.transact(237, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOrganizationColor(admin, color);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOrganizationColorForUser(int color, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(color);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(238, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOrganizationColorForUser(color, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getOrganizationColor(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(239, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationColor(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getOrganizationColorForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(240, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationColorForUser(userHandle);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOrganizationName(ComponentName admin, CharSequence title) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (title != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(title, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(241, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOrganizationName(admin, title);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getOrganizationName(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(242, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationName(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getDeviceOwnerOrganizationName() throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(243, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerOrganizationName();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getOrganizationNameForUser(int userHandle) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(244, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOrganizationNameForUser(userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getUserProvisioningState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(245, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUserProvisioningState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUserProvisioningState(int state, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(246, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserProvisioningState(state, userHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setAffiliationIds(ComponentName admin, List<String> ids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(ids);
                    boolean _status = this.mRemote.transact(247, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAffiliationIds(admin, ids);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getAffiliationIds(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(248, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAffiliationIds(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCallingUserAffiliated() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(249, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCallingUserAffiliated();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isAffiliatedUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(250, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAffiliatedUser(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setSecurityLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(251, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSecurityLoggingEnabled(admin, packageName, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isSecurityLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(252, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSecurityLoggingEnabled(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ParceledListSlice retrieveSecurityLogs(ComponentName admin, String packageName) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(253, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().retrieveSecurityLogs(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ParceledListSlice retrievePreRebootSecurityLogs(ComponentName admin, String packageName) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(254, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().retrievePreRebootSecurityLogs(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long forceNetworkLogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(255, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().forceNetworkLogs();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long forceSecurityLogs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(256, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().forceSecurityLogs();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUninstallInQueue(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(257, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUninstallInQueue(packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void uninstallPackageWithActiveAdmins(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(258, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().uninstallPackageWithActiveAdmins(packageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isDeviceProvisioned() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(259, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDeviceProvisioned();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(260, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDeviceProvisioningConfigApplied();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDeviceProvisioningConfigApplied() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(261, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceProvisioningConfigApplied();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void forceUpdateUserSetupComplete(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(262, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forceUpdateUserSetupComplete(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setBackupServiceEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(263, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBackupServiceEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isBackupServiceEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(264, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isBackupServiceEnabled(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setNetworkLoggingEnabled(ComponentName admin, String packageName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(265, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNetworkLoggingEnabled(admin, packageName, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isNetworkLoggingEnabled(ComponentName admin, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(266, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isNetworkLoggingEnabled(admin, packageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<NetworkEvent> retrieveNetworkLogs(ComponentName admin, String packageName, long batchToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeLong(batchToken);
                    boolean _status = this.mRemote.transact(267, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().retrieveNetworkLogs(admin, packageName, batchToken);
                    }
                    _reply.readException();
                    List<NetworkEvent> _result = _reply.createTypedArrayList(NetworkEvent.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean bindDeviceAdminServiceAsUser(ComponentName admin, IApplicationThread caller, IBinder token, Intent service, IServiceConnection connection, int flags, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IBinder iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeStrongBinder(token);
                        if (service != null) {
                            _data.writeInt(1);
                            service.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (connection != null) {
                            iBinder = connection.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        try {
                            _data.writeInt(flags);
                            try {
                                _data.writeInt(targetUserId);
                                boolean _status = this.mRemote.transact(268, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    boolean bindDeviceAdminServiceAsUser = Stub.getDefaultImpl().bindDeviceAdminServiceAsUser(admin, caller, token, service, connection, flags, targetUserId);
                                    _reply.recycle();
                                    _data.recycle();
                                    return bindDeviceAdminServiceAsUser;
                                }
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(269, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBindDeviceAdminTargetUsers(admin);
                    }
                    _reply.readException();
                    List<UserHandle> _result = _reply.createTypedArrayList(UserHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isEphemeralUser(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(270, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isEphemeralUser(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getLastSecurityLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(271, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastSecurityLogRetrievalTime();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getLastBugReportRequestTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(272, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastBugReportRequestTime();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getLastNetworkLogRetrievalTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(273, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastNetworkLogRetrievalTime();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setResetPasswordToken(ComponentName admin, byte[] token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(token);
                    boolean _status = this.mRemote.transact(274, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setResetPasswordToken(admin, token);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean clearResetPasswordToken(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(275, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().clearResetPasswordToken(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isResetPasswordTokenActive(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(276, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isResetPasswordTokenActive(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean resetPasswordWithToken(ComponentName admin, String password, byte[] token, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(password);
                    _data.writeByteArray(token);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(277, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resetPasswordWithToken(admin, password, token, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCurrentInputMethodSetByOwner() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(278, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCurrentInputMethodSetByOwner();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public StringParceledListSlice getOwnerInstalledCaCerts(UserHandle user) throws RemoteException {
                StringParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(279, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOwnerInstalledCaCerts(user);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StringParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void clearApplicationUserData(ComponentName admin, String packageName, IPackageDataObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(280, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearApplicationUserData(admin, packageName, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setLogoutEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(281, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLogoutEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isLogoutEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(282, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isLogoutEnabled();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getDisallowedSystemApps(ComponentName admin, int userId, String provisioningAction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeString(provisioningAction);
                    boolean _status = this.mRemote.transact(283, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDisallowedSystemApps(admin, userId, provisioningAction);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void transferOwnership(ComponentName admin, ComponentName target, PersistableBundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (target != null) {
                        _data.writeInt(1);
                        target.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(284, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().transferOwnership(admin, target, bundle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public PersistableBundle getTransferOwnershipBundle() throws RemoteException {
                PersistableBundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(285, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTransferOwnershipBundle();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PersistableBundle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setStartUserSessionMessage(ComponentName admin, CharSequence startUserSessionMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (startUserSessionMessage != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(startUserSessionMessage, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(286, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStartUserSessionMessage(admin, startUserSessionMessage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setEndUserSessionMessage(ComponentName admin, CharSequence endUserSessionMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (endUserSessionMessage != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(endUserSessionMessage, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(287, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setEndUserSessionMessage(admin, endUserSessionMessage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getStartUserSessionMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(288, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStartUserSessionMessage(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public CharSequence getEndUserSessionMessage(ComponentName admin) throws RemoteException {
                CharSequence _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(289, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEndUserSessionMessage(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> setMeteredDataDisabledPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageNames);
                    boolean _status = this.mRemote.transact(290, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setMeteredDataDisabledPackages(admin, packageNames);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getMeteredDataDisabledPackages(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(291, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMeteredDataDisabledPackages(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int addOverrideApn(ComponentName admin, ApnSetting apnSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (apnSetting != null) {
                        _data.writeInt(1);
                        apnSetting.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(292, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addOverrideApn(admin, apnSetting);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean updateOverrideApn(ComponentName admin, int apnId, ApnSetting apnSetting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(apnId);
                    if (apnSetting != null) {
                        _data.writeInt(1);
                        apnSetting.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(293, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateOverrideApn(admin, apnId, apnSetting);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean removeOverrideApn(ComponentName admin, int apnId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(apnId);
                    boolean _status = this.mRemote.transact(294, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeOverrideApn(admin, apnId);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<ApnSetting> getOverrideApns(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(295, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getOverrideApns(admin);
                    }
                    _reply.readException();
                    List<ApnSetting> _result = _reply.createTypedArrayList(ApnSetting.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOverrideApnsEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(296, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOverrideApnsEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isOverrideApnEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(297, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isOverrideApnEnabled(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isMeteredDataDisabledPackageForUser(ComponentName admin, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(298, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMeteredDataDisabledPackageForUser(admin, packageName, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int setGlobalPrivateDns(ComponentName admin, int mode, String privateDnsHost) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeString(privateDnsHost);
                    boolean _status = this.mRemote.transact(299, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setGlobalPrivateDns(admin, mode, privateDnsHost);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getGlobalPrivateDnsMode(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(300, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGlobalPrivateDnsMode(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getGlobalPrivateDnsHost(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(301, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getGlobalPrivateDnsHost(admin);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void markProfileOwnerOnOrganizationOwnedDevice(ComponentName who, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (who != null) {
                        _data.writeInt(1);
                        who.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(302, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().markProfileOwnerOnOrganizationOwnedDevice(who, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void installUpdateFromFile(ComponentName admin, ParcelFileDescriptor updateFileDescriptor, StartInstallingUpdateCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (updateFileDescriptor != null) {
                        _data.writeInt(1);
                        updateFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(303, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().installUpdateFromFile(admin, updateFileDescriptor, listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCrossProfileCalendarPackages(ComponentName admin, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageNames);
                    boolean _status = this.mRemote.transact(304, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCrossProfileCalendarPackages(admin, packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getCrossProfileCalendarPackages(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(305, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCalendarPackages(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isPackageAllowedToAccessCalendarForUser(String packageName, int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userHandle);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(306, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageAllowedToAccessCalendarForUser(packageName, userHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getCrossProfileCalendarPackagesForUser(int userHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userHandle);
                    boolean _status = this.mRemote.transact(307, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfileCalendarPackagesForUser(userHandle);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCrossProfilePackages(ComponentName admin, List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packageNames);
                    boolean _status = this.mRemote.transact(308, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCrossProfilePackages(admin, packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getCrossProfilePackages(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(309, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCrossProfilePackages(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getAllCrossProfilePackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(310, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllCrossProfilePackages();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getDefaultCrossProfilePackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(311, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDefaultCrossProfilePackages();
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isManagedKiosk() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(312, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isManagedKiosk();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUnattendedManagedKiosk() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(313, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUnattendedManagedKiosk();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean startViewCalendarEventInManagedProfile(String packageName, long eventId, long start, long end, boolean allDay, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                        try {
                            _data.writeLong(eventId);
                            _data.writeLong(start);
                            _data.writeLong(end);
                            boolean _result = true;
                            _data.writeInt(allDay ? 1 : 0);
                            _data.writeInt(flags);
                            boolean _status = this.mRemote.transact(314, _data, _reply, 0);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                boolean startViewCalendarEventInManagedProfile = Stub.getDefaultImpl().startViewCalendarEventInManagedProfile(packageName, eventId, start, end, allDay, flags);
                                _reply.recycle();
                                _data.recycle();
                                return startViewCalendarEventInManagedProfile;
                            }
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th) {
                            th = th;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setKeyGrantForApp(ComponentName admin, String callerPackage, String alias, String packageName, boolean hasGrant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(callerPackage);
                        try {
                            _data.writeString(alias);
                            try {
                                _data.writeString(packageName);
                                _data.writeInt(hasGrant ? 1 : 0);
                                try {
                                    boolean _status = this.mRemote.transact(315, _data, _reply, 0);
                                    if (!_status && Stub.getDefaultImpl() != null) {
                                        boolean keyGrantForApp = Stub.getDefaultImpl().setKeyGrantForApp(admin, callerPackage, alias, packageName, hasGrant);
                                        _reply.recycle();
                                        _data.recycle();
                                        return keyGrantForApp;
                                    }
                                    _reply.readException();
                                    if (_reply.readInt() == 0) {
                                        _result = false;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th) {
                                    th = th;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public ParcelableGranteeMap getKeyPairGrants(String callerPackage, String alias) throws RemoteException {
                ParcelableGranteeMap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    boolean _status = this.mRemote.transact(316, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKeyPairGrants(callerPackage, alias);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelableGranteeMap.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean setKeyGrantToWifiAuth(String callerPackage, String alias, boolean hasGrant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    boolean _result = true;
                    _data.writeInt(hasGrant ? 1 : 0);
                    boolean _status = this.mRemote.transact(317, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setKeyGrantToWifiAuth(callerPackage, alias, hasGrant);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isKeyPairGrantedToWifiAuth(String callerPackage, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeString(alias);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(318, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isKeyPairGrantedToWifiAuth(callerPackage, alias);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUserControlDisabledPackages(ComponentName admin, List<String> packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringList(packages);
                    boolean _status = this.mRemote.transact(319, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUserControlDisabledPackages(admin, packages);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<String> getUserControlDisabledPackages(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(320, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUserControlDisabledPackages(admin);
                    }
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setCommonCriteriaModeEnabled(ComponentName admin, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(321, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCommonCriteriaModeEnabled(admin, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isCommonCriteriaModeEnabled(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(322, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCommonCriteriaModeEnabled(admin);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getPersonalAppsSuspendedReasons(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(323, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPersonalAppsSuspendedReasons(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setPersonalAppsSuspended(ComponentName admin, boolean suspended) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!suspended) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(324, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPersonalAppsSuspended(admin, suspended);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public long getManagedProfileMaximumTimeOff(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(325, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getManagedProfileMaximumTimeOff(admin);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setManagedProfileMaximumTimeOff(ComponentName admin, long timeoutMs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(timeoutMs);
                    boolean _status = this.mRemote.transact(326, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setManagedProfileMaximumTimeOff(admin, timeoutMs);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void acknowledgeDeviceCompliant() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(327, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().acknowledgeDeviceCompliant();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isComplianceAcknowledgementRequired() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(328, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isComplianceAcknowledgementRequired();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean canProfileOwnerResetPasswordWhenLocked(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(329, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().canProfileOwnerResetPasswordWhenLocked(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setNextOperationSafety(int operation, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(operation);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(330, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNextOperationSafety(operation, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isSafeOperation(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(331, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSafeOperation(reason);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public String getEnrollmentSpecificId(String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    boolean _status = this.mRemote.transact(332, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEnrollmentSpecificId(callerPackage);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setOrganizationIdForUser(String callerPackage, String enterpriseId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeString(enterpriseId);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(333, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setOrganizationIdForUser(callerPackage, enterpriseId, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public UserHandle createAndProvisionManagedProfile(ManagedProfileProvisioningParams provisioningParams, String callerPackage) throws RemoteException {
                UserHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provisioningParams != null) {
                        _data.writeInt(1);
                        provisioningParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    boolean _status = this.mRemote.transact(334, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createAndProvisionManagedProfile(provisioningParams, callerPackage);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = UserHandle.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void provisionFullyManagedDevice(FullyManagedDeviceProvisioningParams provisioningParams, String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provisioningParams != null) {
                        _data.writeInt(1);
                        provisioningParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callerPackage);
                    boolean _status = this.mRemote.transact(335, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().provisionFullyManagedDevice(provisioningParams, callerPackage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setDeviceOwnerType(ComponentName admin, int deviceOwnerType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(deviceOwnerType);
                    boolean _status = this.mRemote.transact(336, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDeviceOwnerType(admin, deviceOwnerType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public int getDeviceOwnerType(ComponentName admin) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (admin != null) {
                        _data.writeInt(1);
                        admin.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(337, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceOwnerType(admin);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void resetDefaultCrossProfileIntentFilters(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(338, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetDefaultCrossProfileIntentFilters(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean canAdminGrantSensorsPermissionsForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(339, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().canAdminGrantSensorsPermissionsForUser(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public void setUsbDataSignalingEnabled(String callerPackage, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    _data.writeInt(enabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(340, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUsbDataSignalingEnabled(callerPackage, enabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUsbDataSignalingEnabled(String callerPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callerPackage);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(341, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUsbDataSignalingEnabled(callerPackage);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean isUsbDataSignalingEnabledForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(342, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isUsbDataSignalingEnabledForUser(userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public boolean canUsbDataSignalingBeDisabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(343, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().canUsbDataSignalingBeDisabled();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.admin.IDevicePolicyManager
            public List<UserHandle> listForegroundAffiliatedUsers() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(344, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().listForegroundAffiliatedUsers();
                    }
                    _reply.readException();
                    List<UserHandle> _result = _reply.createTypedArrayList(UserHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        private boolean onTransact$setGlobalProxy$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            ComponentName _result = setGlobalProxy(_arg0, _arg1, _arg2);
            reply.writeNoException();
            if (_result != null) {
                reply.writeInt(1);
                _result.writeToParcel(reply, 1);
            } else {
                reply.writeInt(0);
            }
            return true;
        }

        private boolean onTransact$setCameraDisabled$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            boolean _arg2 = false;
            boolean _arg1 = data.readInt() != 0;
            if (data.readInt() != 0) {
                _arg2 = true;
            }
            setCameraDisabled(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getCameraDisabled$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            boolean cameraDisabled = getCameraDisabled(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(cameraDisabled ? 1 : 0);
            return true;
        }

        private boolean onTransact$setScreenCaptureDisabled$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            boolean _arg2 = false;
            boolean _arg1 = data.readInt() != 0;
            if (data.readInt() != 0) {
                _arg2 = true;
            }
            setScreenCaptureDisabled(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getScreenCaptureDisabled$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            boolean screenCaptureDisabled = getScreenCaptureDisabled(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(screenCaptureDisabled ? 1 : 0);
            return true;
        }

        private boolean onTransact$setKeyguardDisabledFeatures$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            setKeyguardDisabledFeatures(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getKeyguardDisabledFeatures$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            int _result = getKeyguardDisabledFeatures(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$setActiveAdmin$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            boolean _arg1 = data.readInt() != 0;
            int _arg2 = data.readInt();
            setActiveAdmin(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getRemoveWarning$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            RemoteCallback _arg1;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            int _arg2 = data.readInt();
            getRemoveWarning(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$hasGrantedPolicy$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            boolean hasGrantedPolicy = hasGrantedPolicy(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(hasGrantedPolicy ? 1 : 0);
            return true;
        }

        private boolean onTransact$setDeviceOwner$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            boolean deviceOwner = setDeviceOwner(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(deviceOwner ? 1 : 0);
            return true;
        }

        private boolean onTransact$setProfileOwner$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            boolean profileOwner = setProfileOwner(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(profileOwner ? 1 : 0);
            return true;
        }

        private boolean onTransact$checkDeviceIdentifierAccess$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            int _arg2 = data.readInt();
            boolean checkDeviceIdentifierAccess = checkDeviceIdentifierAccess(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(checkDeviceIdentifierAccess ? 1 : 0);
            return true;
        }

        private boolean onTransact$setPackagesSuspended$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String[] _arg2 = data.createStringArray();
            boolean _arg3 = data.readInt() != 0;
            String[] _result = setPackagesSuspended(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeStringArray(_result);
            return true;
        }

        private boolean onTransact$isPackageSuspended$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            boolean isPackageSuspended = isPackageSuspended(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(isPackageSuspended ? 1 : 0);
            return true;
        }

        private boolean onTransact$installCaCert$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            byte[] _arg2 = data.createByteArray();
            boolean installCaCert = installCaCert(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(installCaCert ? 1 : 0);
            return true;
        }

        private boolean onTransact$uninstallCaCerts$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String[] _arg2 = data.createStringArray();
            uninstallCaCerts(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$approveCaCert$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            int _arg1 = data.readInt();
            boolean _arg2 = data.readInt() != 0;
            boolean approveCaCert = approveCaCert(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(approveCaCert ? 1 : 0);
            return true;
        }

        private boolean onTransact$installKeyPair$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            byte[] _arg2 = data.createByteArray();
            byte[] _arg3 = data.createByteArray();
            byte[] _arg4 = data.createByteArray();
            String _arg5 = data.readString();
            boolean _arg6 = data.readInt() != 0;
            boolean _arg7 = data.readInt() != 0;
            boolean installKeyPair = installKeyPair(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
            reply.writeNoException();
            reply.writeInt(installKeyPair ? 1 : 0);
            return true;
        }

        private boolean onTransact$removeKeyPair$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            boolean removeKeyPair = removeKeyPair(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(removeKeyPair ? 1 : 0);
            return true;
        }

        private boolean onTransact$generateKeyPair$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            ParcelableKeyGenParameterSpec _arg3;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            if (data.readInt() != 0) {
                _arg3 = ParcelableKeyGenParameterSpec.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg3 = null;
            }
            int _arg4 = data.readInt();
            KeymasterCertificateChain _arg5 = new KeymasterCertificateChain();
            boolean generateKeyPair = generateKeyPair(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(generateKeyPair ? 1 : 0);
            reply.writeInt(1);
            _arg5.writeToParcel(reply, 1);
            return true;
        }

        private boolean onTransact$setKeyPairCertificate$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            byte[] _arg3 = data.createByteArray();
            byte[] _arg4 = data.createByteArray();
            boolean _arg5 = data.readInt() != 0;
            boolean keyPairCertificate = setKeyPairCertificate(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(keyPairCertificate ? 1 : 0);
            return true;
        }

        private boolean onTransact$choosePrivateKeyAlias$(Parcel data, Parcel reply) throws RemoteException {
            Uri _arg1;
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            if (data.readInt() != 0) {
                _arg1 = Uri.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            String _arg2 = data.readString();
            IBinder _arg3 = data.readStrongBinder();
            choosePrivateKeyAlias(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setDelegatedScopes$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            List<String> _arg2 = data.createStringArrayList();
            setDelegatedScopes(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setAlwaysOnVpnPackage$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            boolean _arg2 = data.readInt() != 0;
            List<String> _arg3 = data.createStringArrayList();
            boolean alwaysOnVpnPackage = setAlwaysOnVpnPackage(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(alwaysOnVpnPackage ? 1 : 0);
            return true;
        }

        private boolean onTransact$addPersistentPreferredActivity$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            IntentFilter _arg1;
            ComponentName _arg2;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = IntentFilter.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            if (data.readInt() != 0) {
                _arg2 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg2 = null;
            }
            addPersistentPreferredActivity(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setDefaultSmsApplication$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            boolean _arg2 = data.readInt() != 0;
            setDefaultSmsApplication(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setApplicationRestrictions$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            Bundle _arg3;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            if (data.readInt() != 0) {
                _arg3 = Bundle.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg3 = null;
            }
            setApplicationRestrictions(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getApplicationRestrictions$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            Bundle _result = getApplicationRestrictions(_arg0, _arg1, _arg2);
            reply.writeNoException();
            if (_result != null) {
                reply.writeInt(1);
                _result.writeToParcel(reply, 1);
            } else {
                reply.writeInt(0);
            }
            return true;
        }

        private boolean onTransact$setUserRestriction$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            boolean _arg3 = false;
            boolean _arg2 = data.readInt() != 0;
            if (data.readInt() != 0) {
                _arg3 = true;
            }
            setUserRestriction(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$addCrossProfileIntentFilter$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            IntentFilter _arg1;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = IntentFilter.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            int _arg2 = data.readInt();
            addCrossProfileIntentFilter(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$isAccessibilityServicePermittedByAdmin$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            boolean isAccessibilityServicePermittedByAdmin = isAccessibilityServicePermittedByAdmin(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(isAccessibilityServicePermittedByAdmin ? 1 : 0);
            return true;
        }

        private boolean onTransact$setPermittedInputMethods$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            ClassLoader cl = getClass().getClassLoader();
            List _arg1 = data.readArrayList(cl);
            boolean _arg2 = data.readInt() != 0;
            boolean permittedInputMethods = setPermittedInputMethods(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(permittedInputMethods ? 1 : 0);
            return true;
        }

        private boolean onTransact$isInputMethodPermittedByAdmin$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            boolean _arg3 = data.readInt() != 0;
            boolean isInputMethodPermittedByAdmin = isInputMethodPermittedByAdmin(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(isInputMethodPermittedByAdmin ? 1 : 0);
            return true;
        }

        private boolean onTransact$setApplicationHidden$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            boolean _arg3 = data.readInt() != 0;
            boolean _arg4 = data.readInt() != 0;
            boolean applicationHidden = setApplicationHidden(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            reply.writeInt(applicationHidden ? 1 : 0);
            return true;
        }

        private boolean onTransact$isApplicationHidden$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            boolean _arg3 = data.readInt() != 0;
            boolean isApplicationHidden = isApplicationHidden(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(isApplicationHidden ? 1 : 0);
            return true;
        }

        private boolean onTransact$createAndManageUser$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            ComponentName _arg2;
            PersistableBundle _arg3;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                _arg2 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg2 = null;
            }
            if (data.readInt() != 0) {
                _arg3 = PersistableBundle.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg3 = null;
            }
            int _arg4 = data.readInt();
            UserHandle _result = createAndManageUser(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            if (_result != null) {
                reply.writeInt(1);
                _result.writeToParcel(reply, 1);
            } else {
                reply.writeInt(0);
            }
            return true;
        }

        private boolean onTransact$enableSystemApp$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            enableSystemApp(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$enableSystemAppWithIntent$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            Intent _arg2;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            if (data.readInt() != 0) {
                _arg2 = Intent.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg2 = null;
            }
            int _result = enableSystemAppWithIntent(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$installExistingPackage$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            boolean installExistingPackage = installExistingPackage(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(installExistingPackage ? 1 : 0);
            return true;
        }

        private boolean onTransact$setAccountManagementDisabled$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            boolean _arg3 = false;
            boolean _arg2 = data.readInt() != 0;
            if (data.readInt() != 0) {
                _arg3 = true;
            }
            setAccountManagementDisabled(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setGlobalSetting$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            setGlobalSetting(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setSystemSetting$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            setSystemSetting(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setSecureSetting$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            setSecureSetting(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$notifyLockTaskModeChanged$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            boolean _arg0 = data.readInt() != 0;
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            notifyLockTaskModeChanged(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setUninstallBlocked$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            boolean _arg3 = data.readInt() != 0;
            setUninstallBlocked(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$startManagedQuickContact$(Parcel data, Parcel reply) throws RemoteException {
            Intent _arg4;
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            long _arg1 = data.readLong();
            boolean _arg2 = data.readInt() != 0;
            long _arg3 = data.readLong();
            if (data.readInt() != 0) {
                _arg4 = Intent.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg4 = null;
            }
            startManagedQuickContact(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setTrustAgentConfiguration$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            ComponentName _arg1;
            PersistableBundle _arg2;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            if (data.readInt() != 0) {
                _arg2 = PersistableBundle.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg2 = null;
            }
            boolean _arg3 = data.readInt() != 0;
            setTrustAgentConfiguration(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getTrustAgentConfiguration$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            ComponentName _arg1;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            int _arg2 = data.readInt();
            boolean _arg3 = data.readInt() != 0;
            List<PersistableBundle> _result = getTrustAgentConfiguration(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeTypedList(_result);
            return true;
        }

        private boolean onTransact$setPermissionPolicy$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            setPermissionPolicy(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setPermissionGrantState$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            RemoteCallback _arg5;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            String _arg3 = data.readString();
            int _arg4 = data.readInt();
            if (data.readInt() != 0) {
                _arg5 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg5 = null;
            }
            setPermissionGrantState(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getPermissionGrantState$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            String _arg3 = data.readString();
            int _result = getPermissionGrantState(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$setKeepUninstalledPackages$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            List<String> _arg2 = data.createStringArrayList();
            setKeepUninstalledPackages(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setSecurityLoggingEnabled$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            boolean _arg2 = data.readInt() != 0;
            setSecurityLoggingEnabled(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$setNetworkLoggingEnabled$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            boolean _arg2 = data.readInt() != 0;
            setNetworkLoggingEnabled(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$retrieveNetworkLogs$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            long _arg2 = data.readLong();
            List<NetworkEvent> _result = retrieveNetworkLogs(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeTypedList(_result);
            return true;
        }

        private boolean onTransact$bindDeviceAdminServiceAsUser$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            Intent _arg3;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            IApplicationThread _arg1 = IApplicationThread.Stub.asInterface(data.readStrongBinder());
            IBinder _arg2 = data.readStrongBinder();
            if (data.readInt() != 0) {
                _arg3 = Intent.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg3 = null;
            }
            IServiceConnection _arg4 = IServiceConnection.Stub.asInterface(data.readStrongBinder());
            int _arg5 = data.readInt();
            int _arg6 = data.readInt();
            boolean bindDeviceAdminServiceAsUser = bindDeviceAdminServiceAsUser(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
            reply.writeNoException();
            reply.writeInt(bindDeviceAdminServiceAsUser ? 1 : 0);
            return true;
        }

        private boolean onTransact$resetPasswordWithToken$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            byte[] _arg2 = data.createByteArray();
            int _arg3 = data.readInt();
            boolean resetPasswordWithToken = resetPasswordWithToken(_arg0, _arg1, _arg2, _arg3);
            reply.writeNoException();
            reply.writeInt(resetPasswordWithToken ? 1 : 0);
            return true;
        }

        private boolean onTransact$clearApplicationUserData$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            IPackageDataObserver _arg2 = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
            clearApplicationUserData(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$getDisallowedSystemApps$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            String _arg2 = data.readString();
            List<String> _result = getDisallowedSystemApps(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeStringList(_result);
            return true;
        }

        private boolean onTransact$transferOwnership$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            ComponentName _arg1;
            PersistableBundle _arg2;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            if (data.readInt() != 0) {
                _arg2 = PersistableBundle.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg2 = null;
            }
            transferOwnership(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$updateOverrideApn$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            ApnSetting _arg2;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            if (data.readInt() != 0) {
                _arg2 = ApnSetting.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg2 = null;
            }
            boolean updateOverrideApn = updateOverrideApn(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(updateOverrideApn ? 1 : 0);
            return true;
        }

        private boolean onTransact$isMeteredDataDisabledPackageForUser$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            boolean isMeteredDataDisabledPackageForUser = isMeteredDataDisabledPackageForUser(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(isMeteredDataDisabledPackageForUser ? 1 : 0);
            return true;
        }

        private boolean onTransact$setGlobalPrivateDns$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            int _arg1 = data.readInt();
            String _arg2 = data.readString();
            int _result = setGlobalPrivateDns(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(_result);
            return true;
        }

        private boolean onTransact$installUpdateFromFile$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            ParcelFileDescriptor _arg1;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            if (data.readInt() != 0) {
                _arg1 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg1 = null;
            }
            StartInstallingUpdateCallback _arg2 = StartInstallingUpdateCallback.Stub.asInterface(data.readStrongBinder());
            installUpdateFromFile(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        private boolean onTransact$startViewCalendarEventInManagedProfile$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            long _arg1 = data.readLong();
            long _arg2 = data.readLong();
            long _arg3 = data.readLong();
            boolean _arg4 = data.readInt() != 0;
            int _arg5 = data.readInt();
            boolean startViewCalendarEventInManagedProfile = startViewCalendarEventInManagedProfile(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
            reply.writeNoException();
            reply.writeInt(startViewCalendarEventInManagedProfile ? 1 : 0);
            return true;
        }

        private boolean onTransact$setKeyGrantForApp$(Parcel data, Parcel reply) throws RemoteException {
            ComponentName _arg0;
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                _arg0 = ComponentName.CREATOR.mo3559createFromParcel(data);
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            String _arg2 = data.readString();
            String _arg3 = data.readString();
            boolean _arg4 = data.readInt() != 0;
            boolean keyGrantForApp = setKeyGrantForApp(_arg0, _arg1, _arg2, _arg3, _arg4);
            reply.writeNoException();
            reply.writeInt(keyGrantForApp ? 1 : 0);
            return true;
        }

        private boolean onTransact$setKeyGrantToWifiAuth$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            String _arg1 = data.readString();
            boolean _arg2 = data.readInt() != 0;
            boolean keyGrantToWifiAuth = setKeyGrantToWifiAuth(_arg0, _arg1, _arg2);
            reply.writeNoException();
            reply.writeInt(keyGrantToWifiAuth ? 1 : 0);
            return true;
        }

        private boolean onTransact$setOrganizationIdForUser$(Parcel data, Parcel reply) throws RemoteException {
            data.enforceInterface(DESCRIPTOR);
            String _arg0 = data.readString();
            String _arg1 = data.readString();
            int _arg2 = data.readInt();
            setOrganizationIdForUser(_arg0, _arg1, _arg2);
            reply.writeNoException();
            return true;
        }

        public static boolean setDefaultImpl(IDevicePolicyManager impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IDevicePolicyManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
