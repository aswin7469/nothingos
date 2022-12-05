package com.android.settings.wifi.calling;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsMmTelManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import com.android.settings.R;
import com.android.settings.network.ims.WifiCallingQueryImsState;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settings.slices.SliceBroadcastReceiver;
import com.android.settingslib.Utils;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public class WifiCallingSliceHelper {
    private final Context mContext;

    public WifiCallingSliceHelper(Context context) {
        this.mContext = context;
    }

    public Slice createWifiCallingSlice(Uri uri) {
        int defaultVoiceSubId = getDefaultVoiceSubId();
        if (!SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            Log.d("WifiCallingSliceHelper", "Invalid subscription Id");
            return null;
        } else if (!queryImsState(defaultVoiceSubId).isWifiCallingProvisioned()) {
            Log.d("WifiCallingSliceHelper", "Wifi calling is either not provisioned or not enabled by Platform");
            return null;
        } else {
            boolean isWifiCallingEnabled = isWifiCallingEnabled();
            if (getWifiCallingCarrierActivityIntent(defaultVoiceSubId) != null && !isWifiCallingEnabled) {
                Log.d("WifiCallingSliceHelper", "Needs Activation");
                Resources resourcesForSubId = getResourcesForSubId(defaultVoiceSubId);
                int wfcTitle = MobileNetworkUtils.getWfcTitle(this.mContext, defaultVoiceSubId);
                Log.d("WifiCallingSliceHelper", "createWifiCallingSlice wfcTitle=" + resourcesForSubId.getText(wfcTitle).toString() + " subId=" + defaultVoiceSubId);
                return getNonActionableWifiCallingSlice(resourcesForSubId.getText(wfcTitle), resourcesForSubId.getText(R.string.wifi_calling_settings_activation_instructions), uri, getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"));
            }
            return getWifiCallingSlice(uri, isWifiCallingEnabled, defaultVoiceSubId);
        }
    }

    private boolean isWifiCallingEnabled() {
        WifiCallingQueryImsState queryImsState = queryImsState(getDefaultVoiceSubId());
        return queryImsState.isEnabledByUser() && queryImsState.isAllowUserControl();
    }

    private Slice getWifiCallingSlice(Uri uri, boolean z, int i) {
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.wifi_signal);
        Resources resourcesForSubId = getResourcesForSubId(i);
        int wfcTitle = MobileNetworkUtils.getWfcTitle(this.mContext, i);
        Log.d("WifiCallingSliceHelper", "getWifiCallingSlice wfcTitle=" + resourcesForSubId.getText(wfcTitle).toString() + " subId=" + i);
        return new ListBuilder(this.mContext, uri, -1L).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext)).addRow(new ListBuilder.RowBuilder().setTitle(resourcesForSubId.getText(wfcTitle)).addEndItem(SliceAction.createToggle(getBroadcastIntent("com.android.settings.wifi.calling.action.WIFI_CALLING_CHANGED"), null, z)).setPrimaryAction(SliceAction.createDeeplink(getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"), createWithResource, 0, resourcesForSubId.getText(wfcTitle)))).build();
    }

    public Slice createWifiCallingPreferenceSlice(Uri uri) {
        int defaultVoiceSubId = getDefaultVoiceSubId();
        if (!SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            Log.d("WifiCallingSliceHelper", "Invalid Subscription Id");
            return null;
        }
        boolean isCarrierConfigManagerKeyEnabled = isCarrierConfigManagerKeyEnabled("editable_wfc_mode_bool", defaultVoiceSubId, false);
        boolean isCarrierConfigManagerKeyEnabled2 = isCarrierConfigManagerKeyEnabled("carrier_wfc_supports_wifi_only_bool", defaultVoiceSubId, true);
        if (!isCarrierConfigManagerKeyEnabled) {
            Log.d("WifiCallingSliceHelper", "Wifi calling preference is not editable");
            return null;
        } else if (!queryImsState(defaultVoiceSubId).isWifiCallingProvisioned()) {
            Log.d("WifiCallingSliceHelper", "Wifi calling is either not provisioned or not enabled by platform");
            return null;
        } else {
            try {
                ImsMmTelManager imsMmTelManager = getImsMmTelManager(defaultVoiceSubId);
                boolean isWifiCallingEnabled = isWifiCallingEnabled();
                int wfcMode = getWfcMode(imsMmTelManager);
                if (!isWifiCallingEnabled) {
                    Resources resourcesForSubId = getResourcesForSubId(defaultVoiceSubId);
                    return getNonActionableWifiCallingSlice(resourcesForSubId.getText(R.string.wifi_calling_mode_title), resourcesForSubId.getText(R.string.wifi_calling_turn_on), uri, getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"));
                }
                return getWifiCallingPreferenceSlice(isCarrierConfigManagerKeyEnabled2, wfcMode, uri, defaultVoiceSubId);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Log.e("WifiCallingSliceHelper", "Unable to get wifi calling preferred mode", e);
                return null;
            }
        }
    }

    private Slice getWifiCallingPreferenceSlice(boolean z, int i, Uri uri, int i2) {
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.wifi_signal);
        Resources resourcesForSubId = getResourcesForSubId(i2);
        ListBuilder accentColor = new ListBuilder(this.mContext, uri, -1L).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext));
        ListBuilder.HeaderBuilder headerBuilder = new ListBuilder.HeaderBuilder();
        int i3 = R.string.wifi_calling_mode_title;
        ListBuilder.HeaderBuilder primaryAction = headerBuilder.setTitle(resourcesForSubId.getText(i3)).setPrimaryAction(SliceAction.createDeeplink(getActivityIntent("android.settings.WIFI_CALLING_SETTINGS"), createWithResource, 0, resourcesForSubId.getText(i3)));
        if (!com.android.settings.Utils.isSettingsIntelligence(this.mContext)) {
            primaryAction.setSubtitle(getWifiCallingPreferenceSummary(i, i2));
        }
        accentColor.setHeader(primaryAction);
        if (z) {
            accentColor.addRow(wifiPreferenceRowBuilder(accentColor, 17041618, "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_ONLY", i == 0, i2));
        }
        accentColor.addRow(wifiPreferenceRowBuilder(accentColor, 17041619, "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_PREFERRED", i == 2, i2));
        accentColor.addRow(wifiPreferenceRowBuilder(accentColor, 17041616, "com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_CELLULAR_PREFERRED", i == 1, i2));
        return accentColor.build();
    }

    private ListBuilder.RowBuilder wifiPreferenceRowBuilder(ListBuilder listBuilder, int i, String str, boolean z, int i2) {
        IconCompat createWithResource = IconCompat.createWithResource(this.mContext, R.drawable.radio_button_check);
        Resources resourcesForSubId = getResourcesForSubId(i2);
        return new ListBuilder.RowBuilder().setTitle(resourcesForSubId.getText(i)).setTitleItem(SliceAction.createToggle(getBroadcastIntent(str), createWithResource, resourcesForSubId.getText(i), z));
    }

    private CharSequence getWifiCallingPreferenceSummary(int i, int i2) {
        Resources resourcesForSubId = getResourcesForSubId(i2);
        if (i != 0) {
            if (i == 1) {
                return resourcesForSubId.getText(17041616);
            }
            if (i == 2) {
                return resourcesForSubId.getText(17041619);
            }
            return null;
        }
        return resourcesForSubId.getText(17041618);
    }

    protected ImsMmTelManager getImsMmTelManager(int i) {
        return ImsMmTelManager.createForSubscriptionId(i);
    }

    private int getWfcMode(final ImsMmTelManager imsMmTelManager) throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask futureTask = new FutureTask(new Callable<Integer>() { // from class: com.android.settings.wifi.calling.WifiCallingSliceHelper.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Integer mo572call() {
                return Integer.valueOf(imsMmTelManager.getVoWiFiModeSetting());
            }
        });
        Executors.newSingleThreadExecutor().execute(futureTask);
        return ((Integer) futureTask.get(2000L, TimeUnit.MILLISECONDS)).intValue();
    }

    public void handleWifiCallingChanged(Intent intent) {
        int defaultVoiceSubId = getDefaultVoiceSubId();
        if (SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            WifiCallingQueryImsState queryImsState = queryImsState(defaultVoiceSubId);
            if (queryImsState.isWifiCallingProvisioned()) {
                boolean z = queryImsState.isEnabledByUser() && queryImsState.isAllowUserControl();
                boolean booleanExtra = intent.getBooleanExtra("android.app.slice.extra.TOGGLE_STATE", z);
                Intent wifiCallingCarrierActivityIntent = getWifiCallingCarrierActivityIntent(defaultVoiceSubId);
                if ((!booleanExtra || wifiCallingCarrierActivityIntent == null) && booleanExtra != z) {
                    getImsMmTelManager(defaultVoiceSubId).setVoWiFiSettingEnabled(booleanExtra);
                }
            }
        }
        this.mContext.getContentResolver().notifyChange(CustomSliceRegistry.WIFI_CALLING_URI, null);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
        if (r3 != false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void handleWifiCallingPreferenceChanged(Intent intent) {
        boolean z;
        int defaultVoiceSubId = getDefaultVoiceSubId();
        if (SubscriptionManager.isValidSubscriptionId(defaultVoiceSubId)) {
            int i = 0;
            boolean isCarrierConfigManagerKeyEnabled = isCarrierConfigManagerKeyEnabled("editable_wfc_mode_bool", defaultVoiceSubId, false);
            boolean isCarrierConfigManagerKeyEnabled2 = isCarrierConfigManagerKeyEnabled("carrier_wfc_supports_wifi_only_bool", defaultVoiceSubId, true);
            WifiCallingQueryImsState queryImsState = queryImsState(defaultVoiceSubId);
            if (isCarrierConfigManagerKeyEnabled && queryImsState.isWifiCallingProvisioned() && queryImsState.isEnabledByUser() && queryImsState.isAllowUserControl()) {
                ImsMmTelManager imsMmTelManager = getImsMmTelManager(defaultVoiceSubId);
                int voWiFiModeSetting = imsMmTelManager.getVoWiFiModeSetting();
                String action = intent.getAction();
                action.hashCode();
                switch (action.hashCode()) {
                    case -86230637:
                        if (action.equals("com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_PREFERRED")) {
                            z = false;
                            break;
                        }
                        z = true;
                        break;
                    case 176882490:
                        if (action.equals("com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_WIFI_ONLY")) {
                            z = true;
                            break;
                        }
                        z = true;
                        break;
                    case 495970216:
                        if (action.equals("com.android.settings.slice.action.WIFI_CALLING_PREFERENCE_CELLULAR_PREFERRED")) {
                            z = true;
                            break;
                        }
                        z = true;
                        break;
                    default:
                        z = true;
                        break;
                }
                switch (z) {
                    case false:
                        i = 2;
                        break;
                    case true:
                        break;
                    case true:
                        i = 1;
                        break;
                    default:
                        i = -1;
                        break;
                }
                if (i != -1 && i != voWiFiModeSetting) {
                    imsMmTelManager.setVoWiFiModeSetting(i);
                }
            }
        }
        this.mContext.getContentResolver().notifyChange(CustomSliceRegistry.WIFI_CALLING_PREFERENCE_URI, null);
    }

    private Slice getNonActionableWifiCallingSlice(CharSequence charSequence, CharSequence charSequence2, Uri uri, PendingIntent pendingIntent) {
        ListBuilder.RowBuilder primaryAction = new ListBuilder.RowBuilder().setTitle(charSequence).setPrimaryAction(SliceAction.createDeeplink(pendingIntent, IconCompat.createWithResource(this.mContext, R.drawable.wifi_signal), 1, charSequence));
        if (!com.android.settings.Utils.isSettingsIntelligence(this.mContext)) {
            primaryAction.setSubtitle(charSequence2);
        }
        return new ListBuilder(this.mContext, uri, -1L).setAccentColor(Utils.getColorAccentDefaultColor(this.mContext)).addRow(primaryAction).build();
    }

    protected boolean isCarrierConfigManagerKeyEnabled(String str, int i, boolean z) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = getCarrierConfigManager(this.mContext);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return false;
        }
        return configForSubId.getBoolean(str, z);
    }

    protected CarrierConfigManager getCarrierConfigManager(Context context) {
        return (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    protected int getDefaultVoiceSubId() {
        return SubscriptionManager.getDefaultVoiceSubscriptionId();
    }

    protected Intent getWifiCallingCarrierActivityIntent(int i) {
        PersistableBundle configForSubId;
        ComponentName unflattenFromString;
        CarrierConfigManager carrierConfigManager = getCarrierConfigManager(this.mContext);
        if (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i)) == null) {
            return null;
        }
        String string = configForSubId.getString("wfc_emergency_address_carrier_app_string");
        if (TextUtils.isEmpty(string) || (unflattenFromString = ComponentName.unflattenFromString(string)) == null) {
            return null;
        }
        Intent intent = new Intent();
        intent.setComponent(unflattenFromString);
        return intent;
    }

    private PendingIntent getBroadcastIntent(String str) {
        Intent intent = new Intent(str);
        intent.setClass(this.mContext, SliceBroadcastReceiver.class);
        intent.addFlags(268435456);
        return PendingIntent.getBroadcast(this.mContext, 0, intent, 335544320);
    }

    private PendingIntent getActivityIntent(String str) {
        Intent intent = new Intent(str);
        intent.setPackage("com.android.settings");
        intent.addFlags(268435456);
        return PendingIntent.getActivity(this.mContext, 0, intent, 67108864);
    }

    private Resources getResourcesForSubId(int i) {
        return SubscriptionManager.getResourcesForSubId(this.mContext, i);
    }

    WifiCallingQueryImsState queryImsState(int i) {
        return new WifiCallingQueryImsState(this.mContext, i);
    }
}
