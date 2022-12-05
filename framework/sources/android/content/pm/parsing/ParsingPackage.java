package android.content.pm.parsing;

import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureGroupInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.parsing.component.ParsedActivity;
import android.content.pm.parsing.component.ParsedAttribution;
import android.content.pm.parsing.component.ParsedInstrumentation;
import android.content.pm.parsing.component.ParsedIntentInfo;
import android.content.pm.parsing.component.ParsedPermission;
import android.content.pm.parsing.component.ParsedPermissionGroup;
import android.content.pm.parsing.component.ParsedProcess;
import android.content.pm.parsing.component.ParsedProvider;
import android.content.pm.parsing.component.ParsedService;
import android.content.pm.parsing.component.ParsedUsesPermission;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.security.PublicKey;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public interface ParsingPackage extends ParsingPackageRead {
    /* renamed from: addActivity */
    ParsingPackage mo865addActivity(ParsedActivity parsedActivity);

    /* renamed from: addAdoptPermission */
    ParsingPackage mo866addAdoptPermission(String str);

    /* renamed from: addAttribution */
    ParsingPackage mo867addAttribution(ParsedAttribution parsedAttribution);

    /* renamed from: addConfigPreference */
    ParsingPackage mo868addConfigPreference(ConfigurationInfo configurationInfo);

    /* renamed from: addFeatureGroup */
    ParsingPackage mo869addFeatureGroup(FeatureGroupInfo featureGroupInfo);

    /* renamed from: addImplicitPermission */
    ParsingPackage mo870addImplicitPermission(String str);

    /* renamed from: addInstrumentation */
    ParsingPackage mo871addInstrumentation(ParsedInstrumentation parsedInstrumentation);

    /* renamed from: addKeySet */
    ParsingPackage mo872addKeySet(String str, PublicKey publicKey);

    /* renamed from: addLibraryName */
    ParsingPackage mo873addLibraryName(String str);

    /* renamed from: addOriginalPackage */
    ParsingPackage mo874addOriginalPackage(String str);

    ParsingPackage addOverlayable(String str, String str2);

    /* renamed from: addPermission */
    ParsingPackage mo875addPermission(ParsedPermission parsedPermission);

    /* renamed from: addPermissionGroup */
    ParsingPackage mo876addPermissionGroup(ParsedPermissionGroup parsedPermissionGroup);

    /* renamed from: addPreferredActivityFilter */
    ParsingPackage mo877addPreferredActivityFilter(String str, ParsedIntentInfo parsedIntentInfo);

    /* renamed from: addProperty */
    ParsingPackage mo878addProperty(PackageManager.Property property);

    /* renamed from: addProtectedBroadcast */
    ParsingPackage mo879addProtectedBroadcast(String str);

    /* renamed from: addProvider */
    ParsingPackage mo880addProvider(ParsedProvider parsedProvider);

    /* renamed from: addQueriesIntent */
    ParsingPackage mo881addQueriesIntent(Intent intent);

    /* renamed from: addQueriesPackage */
    ParsingPackage mo882addQueriesPackage(String str);

    /* renamed from: addQueriesProvider */
    ParsingPackage mo883addQueriesProvider(String str);

    /* renamed from: addReceiver */
    ParsingPackage mo884addReceiver(ParsedActivity parsedActivity);

    /* renamed from: addReqFeature */
    ParsingPackage mo885addReqFeature(FeatureInfo featureInfo);

    /* renamed from: addService */
    ParsingPackage mo886addService(ParsedService parsedService);

    /* renamed from: addUsesLibrary */
    ParsingPackage mo887addUsesLibrary(String str);

    /* renamed from: addUsesNativeLibrary */
    ParsingPackage mo888addUsesNativeLibrary(String str);

    /* renamed from: addUsesOptionalLibrary */
    ParsingPackage mo889addUsesOptionalLibrary(String str);

    /* renamed from: addUsesOptionalNativeLibrary */
    ParsingPackage mo890addUsesOptionalNativeLibrary(String str);

    /* renamed from: addUsesPermission */
    ParsingPackage mo891addUsesPermission(ParsedUsesPermission parsedUsesPermission);

    /* renamed from: addUsesStaticLibrary */
    ParsingPackage mo892addUsesStaticLibrary(String str);

    /* renamed from: addUsesStaticLibraryCertDigests */
    ParsingPackage mo893addUsesStaticLibraryCertDigests(String[] strArr);

    /* renamed from: addUsesStaticLibraryVersion */
    ParsingPackage mo894addUsesStaticLibraryVersion(long j);

    /* renamed from: asSplit */
    ParsingPackage mo895asSplit(String[] strArr, String[] strArr2, int[] iArr, SparseArray<int[]> sparseArray);

    Object hideAsParsed();

    /* renamed from: removeUsesOptionalLibrary */
    ParsingPackage mo896removeUsesOptionalLibrary(String str);

    /* renamed from: removeUsesOptionalNativeLibrary */
    ParsingPackage mo897removeUsesOptionalNativeLibrary(String str);

    /* renamed from: setAllowAudioPlaybackCapture */
    ParsingPackage mo898setAllowAudioPlaybackCapture(boolean z);

    /* renamed from: setAllowBackup */
    ParsingPackage mo899setAllowBackup(boolean z);

    /* renamed from: setAllowClearUserData */
    ParsingPackage mo900setAllowClearUserData(boolean z);

    /* renamed from: setAllowClearUserDataOnFailedRestore */
    ParsingPackage mo901setAllowClearUserDataOnFailedRestore(boolean z);

    /* renamed from: setAllowNativeHeapPointerTagging */
    ParsingPackage mo902setAllowNativeHeapPointerTagging(boolean z);

    /* renamed from: setAllowTaskReparenting */
    ParsingPackage mo903setAllowTaskReparenting(boolean z);

    /* renamed from: setAnyDensity */
    ParsingPackage mo904setAnyDensity(int i);

    /* renamed from: setAppComponentFactory */
    ParsingPackage mo905setAppComponentFactory(String str);

    ParsingPackage setAttributionsAreUserVisible(boolean z);

    /* renamed from: setAutoRevokePermissions */
    ParsingPackage mo906setAutoRevokePermissions(int i);

    /* renamed from: setBackupAgentName */
    ParsingPackage mo907setBackupAgentName(String str);

    /* renamed from: setBackupInForeground */
    ParsingPackage mo908setBackupInForeground(boolean z);

    /* renamed from: setBanner */
    ParsingPackage mo909setBanner(int i);

    /* renamed from: setBaseHardwareAccelerated */
    ParsingPackage mo910setBaseHardwareAccelerated(boolean z);

    /* renamed from: setBaseRevisionCode */
    ParsingPackage mo911setBaseRevisionCode(int i);

    /* renamed from: setCantSaveState */
    ParsingPackage mo912setCantSaveState(boolean z);

    /* renamed from: setCategory */
    ParsingPackage mo913setCategory(int i);

    /* renamed from: setClassLoaderName */
    ParsingPackage mo914setClassLoaderName(String str);

    /* renamed from: setClassName */
    ParsingPackage mo915setClassName(String str);

    /* renamed from: setCompatibleWidthLimitDp */
    ParsingPackage mo916setCompatibleWidthLimitDp(int i);

    /* renamed from: setCompileSdkVersion */
    ParsingPackage mo917setCompileSdkVersion(int i);

    ParsingPackage setCompileSdkVersionCodename(String str);

    /* renamed from: setCrossProfile */
    ParsingPackage mo918setCrossProfile(boolean z);

    /* renamed from: setDataExtractionRules */
    ParsingPackage mo919setDataExtractionRules(int i);

    /* renamed from: setDebuggable */
    ParsingPackage mo920setDebuggable(boolean z);

    /* renamed from: setDefaultToDeviceProtectedStorage */
    ParsingPackage mo921setDefaultToDeviceProtectedStorage(boolean z);

    /* renamed from: setDescriptionRes */
    ParsingPackage mo922setDescriptionRes(int i);

    /* renamed from: setDirectBootAware */
    ParsingPackage mo923setDirectBootAware(boolean z);

    /* renamed from: setEnabled */
    ParsingPackage mo924setEnabled(boolean z);

    /* renamed from: setExternalStorage */
    ParsingPackage mo925setExternalStorage(boolean z);

    /* renamed from: setExtractNativeLibs */
    ParsingPackage mo926setExtractNativeLibs(boolean z);

    /* renamed from: setForceQueryable */
    ParsingPackage mo927setForceQueryable(boolean z);

    /* renamed from: setFullBackupContent */
    ParsingPackage mo928setFullBackupContent(int i);

    /* renamed from: setFullBackupOnly */
    ParsingPackage mo929setFullBackupOnly(boolean z);

    /* renamed from: setGame */
    ParsingPackage mo930setGame(boolean z);

    /* renamed from: setGwpAsanMode */
    ParsingPackage mo931setGwpAsanMode(int i);

    /* renamed from: setHasCode */
    ParsingPackage mo932setHasCode(boolean z);

    /* renamed from: setHasDomainUrls */
    ParsingPackage mo933setHasDomainUrls(boolean z);

    /* renamed from: setHasFragileUserData */
    ParsingPackage mo934setHasFragileUserData(boolean z);

    /* renamed from: setIconRes */
    ParsingPackage mo935setIconRes(int i);

    /* renamed from: setInstallLocation */
    ParsingPackage mo936setInstallLocation(int i);

    /* renamed from: setIsolatedSplitLoading */
    ParsingPackage mo937setIsolatedSplitLoading(boolean z);

    /* renamed from: setKillAfterRestore */
    ParsingPackage mo938setKillAfterRestore(boolean z);

    /* renamed from: setLabelRes */
    ParsingPackage mo939setLabelRes(int i);

    /* renamed from: setLargeHeap */
    ParsingPackage mo940setLargeHeap(boolean z);

    /* renamed from: setLargestWidthLimitDp */
    ParsingPackage mo941setLargestWidthLimitDp(int i);

    /* renamed from: setLogo */
    ParsingPackage mo942setLogo(int i);

    /* renamed from: setManageSpaceActivityName */
    ParsingPackage mo943setManageSpaceActivityName(String str);

    /* renamed from: setMaxAspectRatio */
    ParsingPackage mo944setMaxAspectRatio(float f);

    /* renamed from: setMemtagMode */
    ParsingPackage mo945setMemtagMode(int i);

    /* renamed from: setMetaData */
    ParsingPackage mo946setMetaData(Bundle bundle);

    /* renamed from: setMinAspectRatio */
    ParsingPackage mo947setMinAspectRatio(float f);

    /* renamed from: setMinExtensionVersions */
    ParsingPackage mo948setMinExtensionVersions(SparseIntArray sparseIntArray);

    /* renamed from: setMinSdkVersion */
    ParsingPackage mo949setMinSdkVersion(int i);

    /* renamed from: setMultiArch */
    ParsingPackage mo950setMultiArch(boolean z);

    /* renamed from: setNativeHeapZeroInitialized */
    ParsingPackage mo951setNativeHeapZeroInitialized(int i);

    /* renamed from: setNetworkSecurityConfigRes */
    ParsingPackage mo952setNetworkSecurityConfigRes(int i);

    /* renamed from: setNonLocalizedLabel */
    ParsingPackage mo953setNonLocalizedLabel(CharSequence charSequence);

    /* renamed from: setOverlay */
    ParsingPackage mo954setOverlay(boolean z);

    /* renamed from: setOverlayCategory */
    ParsingPackage mo955setOverlayCategory(String str);

    /* renamed from: setOverlayIsStatic */
    ParsingPackage mo956setOverlayIsStatic(boolean z);

    /* renamed from: setOverlayPriority */
    ParsingPackage mo957setOverlayPriority(int i);

    /* renamed from: setOverlayTarget */
    ParsingPackage mo958setOverlayTarget(String str);

    /* renamed from: setOverlayTargetName */
    ParsingPackage mo959setOverlayTargetName(String str);

    /* renamed from: setPartiallyDirectBootAware */
    ParsingPackage mo960setPartiallyDirectBootAware(boolean z);

    /* renamed from: setPermission */
    ParsingPackage mo961setPermission(String str);

    /* renamed from: setPersistent */
    ParsingPackage mo962setPersistent(boolean z);

    /* renamed from: setPreserveLegacyExternalStorage */
    ParsingPackage mo963setPreserveLegacyExternalStorage(boolean z);

    /* renamed from: setProcessName */
    ParsingPackage mo964setProcessName(String str);

    /* renamed from: setProcesses */
    ParsingPackage mo965setProcesses(Map<String, ParsedProcess> map);

    /* renamed from: setProfileable */
    ParsingPackage mo966setProfileable(boolean z);

    /* renamed from: setProfileableByShell */
    ParsingPackage mo967setProfileableByShell(boolean z);

    /* renamed from: setRealPackage */
    ParsingPackage mo968setRealPackage(String str);

    /* renamed from: setRequestForegroundServiceExemption */
    ParsingPackage mo969setRequestForegroundServiceExemption(boolean z);

    /* renamed from: setRequestLegacyExternalStorage */
    ParsingPackage mo970setRequestLegacyExternalStorage(boolean z);

    /* renamed from: setRequestRawExternalStorageAccess */
    ParsingPackage mo971setRequestRawExternalStorageAccess(Boolean bool);

    /* renamed from: setRequiredAccountType */
    ParsingPackage mo972setRequiredAccountType(String str);

    /* renamed from: setRequiredForAllUsers */
    ParsingPackage mo973setRequiredForAllUsers(boolean z);

    /* renamed from: setRequiresSmallestWidthDp */
    ParsingPackage mo974setRequiresSmallestWidthDp(int i);

    /* renamed from: setResizeable */
    ParsingPackage mo975setResizeable(int i);

    /* renamed from: setResizeableActivity */
    ParsingPackage mo976setResizeableActivity(Boolean bool);

    /* renamed from: setResizeableActivityViaSdkVersion */
    ParsingPackage mo977setResizeableActivityViaSdkVersion(boolean z);

    /* renamed from: setRestoreAnyVersion */
    ParsingPackage mo978setRestoreAnyVersion(boolean z);

    /* renamed from: setRestrictUpdateHash */
    ParsingPackage mo979setRestrictUpdateHash(byte[] bArr);

    /* renamed from: setRestrictedAccountType */
    ParsingPackage mo980setRestrictedAccountType(String str);

    /* renamed from: setRoundIconRes */
    ParsingPackage mo981setRoundIconRes(int i);

    /* renamed from: setSharedUserId */
    ParsingPackage mo982setSharedUserId(String str);

    /* renamed from: setSharedUserLabel */
    ParsingPackage mo983setSharedUserLabel(int i);

    /* renamed from: setSigningDetails */
    ParsingPackage mo984setSigningDetails(PackageParser.SigningDetails signingDetails);

    /* renamed from: setSplitClassLoaderName */
    ParsingPackage mo985setSplitClassLoaderName(int i, String str);

    /* renamed from: setSplitHasCode */
    ParsingPackage mo986setSplitHasCode(int i, boolean z);

    /* renamed from: setStaticSharedLibName */
    ParsingPackage mo987setStaticSharedLibName(String str);

    /* renamed from: setStaticSharedLibVersion */
    ParsingPackage mo988setStaticSharedLibVersion(long j);

    /* renamed from: setStaticSharedLibrary */
    ParsingPackage mo989setStaticSharedLibrary(boolean z);

    /* renamed from: setSupportsExtraLargeScreens */
    ParsingPackage mo990setSupportsExtraLargeScreens(int i);

    /* renamed from: setSupportsLargeScreens */
    ParsingPackage mo991setSupportsLargeScreens(int i);

    /* renamed from: setSupportsNormalScreens */
    ParsingPackage mo992setSupportsNormalScreens(int i);

    /* renamed from: setSupportsRtl */
    ParsingPackage mo993setSupportsRtl(boolean z);

    /* renamed from: setSupportsSmallScreens */
    ParsingPackage mo994setSupportsSmallScreens(int i);

    /* renamed from: setTargetSandboxVersion */
    ParsingPackage mo995setTargetSandboxVersion(int i);

    /* renamed from: setTargetSdkVersion */
    ParsingPackage mo996setTargetSdkVersion(int i);

    /* renamed from: setTaskAffinity */
    ParsingPackage mo997setTaskAffinity(String str);

    /* renamed from: setTestOnly */
    ParsingPackage mo998setTestOnly(boolean z);

    /* renamed from: setTheme */
    ParsingPackage mo999setTheme(int i);

    /* renamed from: setUiOptions */
    ParsingPackage mo1000setUiOptions(int i);

    /* renamed from: setUpgradeKeySets */
    ParsingPackage mo1001setUpgradeKeySets(Set<String> set);

    /* renamed from: setUse32BitAbi */
    ParsingPackage mo1002setUse32BitAbi(boolean z);

    /* renamed from: setUseEmbeddedDex */
    ParsingPackage mo1003setUseEmbeddedDex(boolean z);

    /* renamed from: setUsesCleartextTraffic */
    ParsingPackage mo1004setUsesCleartextTraffic(boolean z);

    /* renamed from: setUsesNonSdkApi */
    ParsingPackage mo1005setUsesNonSdkApi(boolean z);

    /* renamed from: setVersionName */
    ParsingPackage mo1006setVersionName(String str);

    /* renamed from: setVisibleToInstantApps */
    ParsingPackage mo1007setVisibleToInstantApps(boolean z);

    /* renamed from: setVmSafeMode */
    ParsingPackage mo1008setVmSafeMode(boolean z);

    /* renamed from: setVolumeUuid */
    ParsingPackage mo1009setVolumeUuid(String str);

    /* renamed from: setZygotePreloadName */
    ParsingPackage mo1010setZygotePreloadName(String str);

    /* renamed from: sortActivities */
    ParsingPackage mo1011sortActivities();

    /* renamed from: sortReceivers */
    ParsingPackage mo1012sortReceivers();

    /* renamed from: sortServices */
    ParsingPackage mo1013sortServices();
}
