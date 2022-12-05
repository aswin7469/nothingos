package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.Intent;
import android.telephony.SubscriptionInfo;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.demomode.DemoMode;
import com.android.wifitrackerlib.MergedCarrierEntry;
import com.android.wifitrackerlib.WifiEntry;
import java.util.List;
/* loaded from: classes2.dex */
public interface NetworkController extends CallbackController<SignalCallback>, DemoMode {

    /* loaded from: classes2.dex */
    public interface AccessPointController {

        /* loaded from: classes2.dex */
        public interface AccessPointCallback {
            void onAccessPointsChanged(List<WifiEntry> list);

            void onSettingsActivityTriggered(Intent intent);
        }

        void addAccessPointCallback(AccessPointCallback accessPointCallback);

        boolean canConfigMobileData();

        boolean canConfigWifi();

        boolean connect(WifiEntry wifiEntry);

        int getIcon(WifiEntry wifiEntry);

        MergedCarrierEntry getMergedCarrierEntry();

        void removeAccessPointCallback(AccessPointCallback accessPointCallback);

        void scanForAccessPoints();
    }

    /* loaded from: classes2.dex */
    public interface EmergencyListener {
        void setEmergencyCallsOnly(boolean z);
    }

    /* loaded from: classes2.dex */
    public interface SignalCallback {
        default void setCallIndicator(IconState iconState, int i) {
        }

        default void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
        }

        default void setEthernetIndicators(IconState iconState) {
        }

        default void setIsAirplaneMode(IconState iconState) {
        }

        default void setMobileDataEnabled(boolean z) {
        }

        default void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
        }

        default void setNoSims(boolean z, boolean z2) {
        }

        default void setSubs(List<SubscriptionInfo> list) {
        }

        default void setWifiIndicators(WifiIndicators wifiIndicators) {
        }
    }

    DataSaverController getDataSaverController();

    DataUsageController getMobileDataController();

    String getMobileDataNetworkName();

    int getNumberSubscriptions();

    boolean hasEmergencyCryptKeeperText();

    boolean hasMobileDataFeature();

    boolean hasVoiceCallingFeature();

    boolean isRadioOn();

    void setWifiEnabled(boolean z);

    /* loaded from: classes2.dex */
    public static final class WifiIndicators {
        public boolean activityIn;
        public boolean activityOut;
        public String description;
        public boolean enabled;
        public boolean isTransient;
        public IconState qsIcon;
        public IconState statusIcon;
        public String statusLabel;

        public WifiIndicators(boolean z, IconState iconState, IconState iconState2, boolean z2, boolean z3, String str, boolean z4, String str2) {
            this.enabled = z;
            this.statusIcon = iconState;
            this.qsIcon = iconState2;
            this.activityIn = z2;
            this.activityOut = z3;
            this.description = str;
            this.isTransient = z4;
            this.statusLabel = str2;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("WifiIndicators[");
            sb.append("enabled=");
            sb.append(this.enabled);
            sb.append(",statusIcon=");
            IconState iconState = this.statusIcon;
            String str = "";
            sb.append(iconState == null ? str : iconState.toString());
            sb.append(",qsIcon=");
            IconState iconState2 = this.qsIcon;
            if (iconState2 != null) {
                str = iconState2.toString();
            }
            sb.append(str);
            sb.append(",activityIn=");
            sb.append(this.activityIn);
            sb.append(",activityOut=");
            sb.append(this.activityOut);
            sb.append(",description=");
            sb.append(this.description);
            sb.append(",isTransient=");
            sb.append(this.isTransient);
            sb.append(",statusLabel=");
            sb.append(this.statusLabel);
            sb.append(']');
            return sb.toString();
        }
    }

    /* loaded from: classes2.dex */
    public static final class MobileDataIndicators {
        public boolean activityIn;
        public boolean activityOut;
        public CharSequence description;
        public boolean isWide;
        public IconState qsIcon;
        public int qsType;
        public boolean roaming;
        public boolean showTriangle;
        public IconState statusIcon;
        public int statusType;
        public int subId;
        public CharSequence typeContentDescription;
        public CharSequence typeContentDescriptionHtml;
        public int volteIcon;

        public MobileDataIndicators(IconState iconState, IconState iconState2, int i, int i2, boolean z, boolean z2, int i3, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, boolean z3, int i4, boolean z4, boolean z5) {
            this.statusIcon = iconState;
            this.qsIcon = iconState2;
            this.statusType = i;
            this.qsType = i2;
            this.activityIn = z;
            this.activityOut = z2;
            this.volteIcon = i3;
            this.typeContentDescription = charSequence;
            this.typeContentDescriptionHtml = charSequence2;
            this.description = charSequence3;
            this.isWide = z3;
            this.subId = i4;
            this.roaming = z4;
            this.showTriangle = z5;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("MobileDataIndicators[");
            sb.append("statusIcon=");
            IconState iconState = this.statusIcon;
            String str = "";
            sb.append(iconState == null ? str : iconState.toString());
            sb.append(",qsIcon=");
            IconState iconState2 = this.qsIcon;
            if (iconState2 != null) {
                str = iconState2.toString();
            }
            sb.append(str);
            sb.append(",statusType=");
            sb.append(this.statusType);
            sb.append(",qsType=");
            sb.append(this.qsType);
            sb.append(",activityIn=");
            sb.append(this.activityIn);
            sb.append(",activityOut=");
            sb.append(this.activityOut);
            sb.append(",volteIcon=");
            sb.append(this.volteIcon);
            sb.append(",typeContentDescription=");
            sb.append(this.typeContentDescription);
            sb.append(",typeContentDescriptionHtml=");
            sb.append(this.typeContentDescriptionHtml);
            sb.append(",description=");
            sb.append(this.description);
            sb.append(",isWide=");
            sb.append(this.isWide);
            sb.append(",subId=");
            sb.append(this.subId);
            sb.append(",roaming=");
            sb.append(this.roaming);
            sb.append(",showTriangle=");
            sb.append(this.showTriangle);
            sb.append(']');
            return sb.toString();
        }
    }

    /* loaded from: classes2.dex */
    public static class IconState {
        public final String contentDescription;
        public final int icon;
        public final boolean visible;

        public IconState(boolean z, int i, String str) {
            this.visible = z;
            this.icon = i;
            this.contentDescription = str;
        }

        public IconState(boolean z, int i, int i2, Context context) {
            this(z, i, context.getString(i2));
        }

        public String toString() {
            return "[visible=" + this.visible + ",icon=" + this.icon + ",contentDescription=" + this.contentDescription + ']';
        }
    }
}
