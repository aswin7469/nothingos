package com.android.systemui.statusbar.connectivity;

import android.icu.text.PluralRules;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyDisplayInfo;
import com.android.settingslib.Utils;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.settingslib.mobile.TelephonyIcons;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B½\u0001\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\f\u001a\u00020\u0006\u0012\b\b\u0002\u0010\r\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u000f¢\u0006\u0002\u0010\u0017J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0001H\u0014J\u0013\u0010#\u001a\u00020\u00062\b\u0010\"\u001a\u0004\u0018\u00010$H\u0002J\u0006\u0010%\u001a\u00020\u000fJ\u0006\u0010&\u001a\u00020\u0003J\u0006\u0010'\u001a\u00020\u000fJ\u0006\u0010(\u001a\u00020\u000fJ\u0006\u0010)\u001a\u00020\u0006J\u0006\u0010*\u001a\u00020\u0006J\b\u0010+\u001a\u00020\u000fH\u0016J\u0006\u0010,\u001a\u00020\u0006J\u0006\u0010-\u001a\u00020\u0006J\u0006\u0010.\u001a\u00020\u0006J\u0006\u0010/\u001a\u00020\u0006J\u0006\u00100\u001a\u00020\u0006J\u0006\u00101\u001a\u00020\u0006J\u000e\u00102\u001a\u00020!2\u0006\u00103\u001a\u000204J\u0006\u00105\u001a\u00020\u0006J\u000e\u00106\u001a\b\u0012\u0004\u0012\u00020\u000307H\u0014J\u000e\u00108\u001a\b\u0012\u0004\u0012\u00020\u000307H\u0014J\u0014\u00109\u001a\u00020!2\n\u0010:\u001a\u00060;j\u0002`<H\u0014R\u0012\u0010\t\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0010\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0016\u001a\u00020\u000f8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0018\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019R\u0012\u0010\u000b\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0014\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0015\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u001b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\u0004\u0018\u00010\u001d8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u001e\u001a\u00020\u001f8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0013\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0012\u001a\u00020\u00068\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006="}, mo65043d2 = {"Lcom/android/systemui/statusbar/connectivity/MobileState;", "Lcom/android/systemui/statusbar/connectivity/ConnectivityState;", "networkName", "", "networkNameData", "dataSim", "", "dataConnected", "isEmergency", "airplaneMode", "carrierNetworkChangeMode", "isDefault", "userSetup", "roaming", "dataState", "", "defaultDataOff", "imsRegistered", "voiceCapable", "videoCapable", "mobileDataEnabled", "roamingDataEnabled", "imsRegistrationTech", "(Ljava/lang/String;Ljava/lang/String;ZZZZZZZZIZZZZZZI)V", "isDataDisabledOrNotDefault", "()Z", "serviceState", "Landroid/telephony/ServiceState;", "signalStrength", "Landroid/telephony/SignalStrength;", "telephonyDisplayInfo", "Landroid/telephony/TelephonyDisplayInfo;", "copyFrom", "", "other", "equals", "", "getDataNetworkType", "getOperatorAlphaShort", "getVoiceNetworkType", "getVoiceServiceState", "hasActivityIn", "hasActivityOut", "hashCode", "isCdma", "isDataConnected", "isEmergencyOnly", "isInService", "isNoCalling", "isRoaming", "setFromMobileStatus", "mobileStatus", "Lcom/android/settingslib/mobile/MobileStatusTracker$MobileStatus;", "showQuickSettingsRatIcon", "tableColumns", "", "tableData", "toString", "builder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MobileState.kt */
public final class MobileState extends ConnectivityState {
    public boolean airplaneMode;
    public boolean carrierNetworkChangeMode;
    public boolean dataConnected;
    public boolean dataSim;
    public int dataState;
    public boolean defaultDataOff;
    public boolean imsRegistered;
    public int imsRegistrationTech;
    public boolean isDefault;
    public boolean isEmergency;
    public boolean mobileDataEnabled;
    public String networkName;
    public String networkNameData;
    public boolean roaming;
    public boolean roamingDataEnabled;
    public ServiceState serviceState;
    public SignalStrength signalStrength;
    public TelephonyDisplayInfo telephonyDisplayInfo;
    public boolean userSetup;
    public boolean videoCapable;
    public boolean voiceCapable;

    public MobileState() {
        this((String) null, (String) null, false, false, false, false, false, false, false, false, 0, false, false, false, false, false, false, 0, 262143, (DefaultConstructorMarker) null);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ MobileState(java.lang.String r20, java.lang.String r21, boolean r22, boolean r23, boolean r24, boolean r25, boolean r26, boolean r27, boolean r28, boolean r29, int r30, boolean r31, boolean r32, boolean r33, boolean r34, boolean r35, boolean r36, int r37, int r38, kotlin.jvm.internal.DefaultConstructorMarker r39) {
        /*
            r19 = this;
            r0 = r38
            r1 = r0 & 1
            r2 = 0
            if (r1 == 0) goto L_0x0009
            r1 = r2
            goto L_0x000b
        L_0x0009:
            r1 = r20
        L_0x000b:
            r3 = r0 & 2
            if (r3 == 0) goto L_0x0010
            goto L_0x0012
        L_0x0010:
            r2 = r21
        L_0x0012:
            r3 = r0 & 4
            if (r3 == 0) goto L_0x0018
            r3 = 0
            goto L_0x001a
        L_0x0018:
            r3 = r22
        L_0x001a:
            r5 = r0 & 8
            if (r5 == 0) goto L_0x0020
            r5 = 0
            goto L_0x0022
        L_0x0020:
            r5 = r23
        L_0x0022:
            r6 = r0 & 16
            if (r6 == 0) goto L_0x0028
            r6 = 0
            goto L_0x002a
        L_0x0028:
            r6 = r24
        L_0x002a:
            r7 = r0 & 32
            if (r7 == 0) goto L_0x0030
            r7 = 0
            goto L_0x0032
        L_0x0030:
            r7 = r25
        L_0x0032:
            r8 = r0 & 64
            if (r8 == 0) goto L_0x0038
            r8 = 0
            goto L_0x003a
        L_0x0038:
            r8 = r26
        L_0x003a:
            r9 = r0 & 128(0x80, float:1.794E-43)
            if (r9 == 0) goto L_0x0040
            r9 = 0
            goto L_0x0042
        L_0x0040:
            r9 = r27
        L_0x0042:
            r10 = r0 & 256(0x100, float:3.59E-43)
            if (r10 == 0) goto L_0x0048
            r10 = 0
            goto L_0x004a
        L_0x0048:
            r10 = r28
        L_0x004a:
            r11 = r0 & 512(0x200, float:7.175E-43)
            if (r11 == 0) goto L_0x0050
            r11 = 0
            goto L_0x0052
        L_0x0050:
            r11 = r29
        L_0x0052:
            r12 = r0 & 1024(0x400, float:1.435E-42)
            if (r12 == 0) goto L_0x0058
            r12 = 0
            goto L_0x005a
        L_0x0058:
            r12 = r30
        L_0x005a:
            r13 = r0 & 2048(0x800, float:2.87E-42)
            if (r13 == 0) goto L_0x0060
            r13 = 0
            goto L_0x0062
        L_0x0060:
            r13 = r31
        L_0x0062:
            r14 = r0 & 4096(0x1000, float:5.74E-42)
            if (r14 == 0) goto L_0x0068
            r14 = 0
            goto L_0x006a
        L_0x0068:
            r14 = r32
        L_0x006a:
            r15 = r0 & 8192(0x2000, float:1.14794E-41)
            if (r15 == 0) goto L_0x0070
            r15 = 0
            goto L_0x0072
        L_0x0070:
            r15 = r33
        L_0x0072:
            r4 = r0 & 16384(0x4000, float:2.2959E-41)
            if (r4 == 0) goto L_0x0078
            r4 = 0
            goto L_0x007a
        L_0x0078:
            r4 = r34
        L_0x007a:
            r16 = 32768(0x8000, float:4.5918E-41)
            r16 = r0 & r16
            if (r16 == 0) goto L_0x0084
            r16 = 0
            goto L_0x0086
        L_0x0084:
            r16 = r35
        L_0x0086:
            r17 = 65536(0x10000, float:9.18355E-41)
            r17 = r0 & r17
            if (r17 == 0) goto L_0x008f
            r17 = 0
            goto L_0x0091
        L_0x008f:
            r17 = r36
        L_0x0091:
            r18 = 131072(0x20000, float:1.83671E-40)
            r0 = r0 & r18
            if (r0 == 0) goto L_0x0099
            r0 = -1
            goto L_0x009b
        L_0x0099:
            r0 = r37
        L_0x009b:
            r20 = r1
            r21 = r2
            r22 = r3
            r23 = r5
            r24 = r6
            r25 = r7
            r26 = r8
            r27 = r9
            r28 = r10
            r29 = r11
            r30 = r12
            r31 = r13
            r32 = r14
            r33 = r15
            r34 = r4
            r35 = r16
            r36 = r17
            r37 = r0
            r19.<init>(r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r37)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileState.<init>(java.lang.String, java.lang.String, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, int, boolean, boolean, boolean, boolean, boolean, boolean, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public MobileState(String str, String str2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, int i, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13, boolean z14, int i2) {
        this.networkName = str;
        this.networkNameData = str2;
        this.dataSim = z;
        this.dataConnected = z2;
        this.isEmergency = z3;
        this.airplaneMode = z4;
        this.carrierNetworkChangeMode = z5;
        this.isDefault = z6;
        this.userSetup = z7;
        this.roaming = z8;
        this.dataState = i;
        this.defaultDataOff = z9;
        this.imsRegistered = z10;
        this.voiceCapable = z11;
        this.videoCapable = z12;
        this.mobileDataEnabled = z13;
        this.roamingDataEnabled = z14;
        this.imsRegistrationTech = i2;
        this.telephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
    }

    public final boolean isDataDisabledOrNotDefault() {
        return (this.iconGroup == TelephonyIcons.DATA_DISABLED || this.iconGroup == TelephonyIcons.NOT_DEFAULT_DATA) && this.userSetup;
    }

    public final boolean hasActivityIn() {
        return this.dataConnected && !this.carrierNetworkChangeMode && this.activityIn;
    }

    public final boolean hasActivityOut() {
        return this.dataConnected && !this.carrierNetworkChangeMode && this.activityOut;
    }

    public final boolean showQuickSettingsRatIcon() {
        return this.dataConnected || isDataDisabledOrNotDefault();
    }

    /* access modifiers changed from: protected */
    public void copyFrom(ConnectivityState connectivityState) {
        Intrinsics.checkNotNullParameter(connectivityState, PluralRules.KEYWORD_OTHER);
        MobileState mobileState = connectivityState instanceof MobileState ? (MobileState) connectivityState : null;
        if (mobileState != null) {
            super.copyFrom(mobileState);
            this.networkName = mobileState.networkName;
            this.networkNameData = mobileState.networkNameData;
            this.dataSim = mobileState.dataSim;
            this.dataConnected = mobileState.dataConnected;
            this.isEmergency = mobileState.isEmergency;
            this.airplaneMode = mobileState.airplaneMode;
            this.carrierNetworkChangeMode = mobileState.carrierNetworkChangeMode;
            this.isDefault = mobileState.isDefault;
            this.userSetup = mobileState.userSetup;
            this.roaming = mobileState.roaming;
            this.dataState = mobileState.dataState;
            this.defaultDataOff = mobileState.defaultDataOff;
            this.imsRegistered = mobileState.imsRegistered;
            this.imsRegistrationTech = mobileState.imsRegistrationTech;
            this.voiceCapable = mobileState.voiceCapable;
            this.videoCapable = mobileState.videoCapable;
            this.mobileDataEnabled = mobileState.mobileDataEnabled;
            this.roamingDataEnabled = mobileState.roamingDataEnabled;
            this.telephonyDisplayInfo = mobileState.telephonyDisplayInfo;
            this.serviceState = mobileState.serviceState;
            this.signalStrength = mobileState.signalStrength;
            return;
        }
        throw new IllegalArgumentException("MobileState can only update from another MobileState");
    }

    public final boolean isDataConnected() {
        return this.connected && this.dataState == 2;
    }

    public final int getVoiceServiceState() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 != null) {
            return serviceState2.getState();
        }
        return -1;
    }

    public final boolean isNoCalling() {
        ServiceState serviceState2 = this.serviceState;
        boolean z = false;
        if (serviceState2 != null && serviceState2.getState() == 0) {
            z = true;
        }
        return !z;
    }

    public final String getOperatorAlphaShort() {
        ServiceState serviceState2 = this.serviceState;
        String operatorAlphaShort = serviceState2 != null ? serviceState2.getOperatorAlphaShort() : null;
        return operatorAlphaShort == null ? "" : operatorAlphaShort;
    }

    public final boolean isCdma() {
        SignalStrength signalStrength2 = this.signalStrength;
        if (signalStrength2 != null) {
            Intrinsics.checkNotNull(signalStrength2);
            if (!signalStrength2.isGsm()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isEmergencyOnly() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 != null) {
            Intrinsics.checkNotNull(serviceState2);
            if (serviceState2.isEmergencyOnly()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isInService() {
        return Utils.isInService(this.serviceState);
    }

    public final boolean isRoaming() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 != null) {
            Intrinsics.checkNotNull(serviceState2);
            if (serviceState2.getRoaming()) {
                return true;
            }
        }
        return false;
    }

    public final int getVoiceNetworkType() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 != null) {
            return serviceState2.getVoiceNetworkType();
        }
        return 0;
    }

    public final int getDataNetworkType() {
        ServiceState serviceState2 = this.serviceState;
        if (serviceState2 != null) {
            return serviceState2.getDataNetworkType();
        }
        return 0;
    }

    public final void setFromMobileStatus(MobileStatusTracker.MobileStatus mobileStatus) {
        Intrinsics.checkNotNullParameter(mobileStatus, "mobileStatus");
        this.activityIn = mobileStatus.activityIn;
        this.activityOut = mobileStatus.activityOut;
        this.dataSim = mobileStatus.dataSim;
        this.carrierNetworkChangeMode = mobileStatus.carrierNetworkChangeMode;
        this.dataState = mobileStatus.dataState;
        this.signalStrength = mobileStatus.signalStrength;
        TelephonyDisplayInfo telephonyDisplayInfo2 = mobileStatus.telephonyDisplayInfo;
        Intrinsics.checkNotNullExpressionValue(telephonyDisplayInfo2, "mobileStatus.telephonyDisplayInfo");
        this.telephonyDisplayInfo = telephonyDisplayInfo2;
        this.serviceState = mobileStatus.serviceState;
    }

    /* access modifiers changed from: protected */
    public void toString(StringBuilder sb) {
        String str;
        String access$minLog;
        Intrinsics.checkNotNullParameter(sb, "builder");
        super.toString(sb);
        sb.append(',');
        sb.append("dataSim=" + this.dataSim + ',');
        sb.append("networkName=" + this.networkName + ',');
        sb.append("networkNameData=" + this.networkNameData + ',');
        sb.append("dataConnected=" + this.dataConnected + ',');
        sb.append("roaming=" + this.roaming + ',');
        sb.append("isDefault=" + this.isDefault + ',');
        sb.append("isEmergency=" + this.isEmergency + ',');
        sb.append("airplaneMode=" + this.airplaneMode + ',');
        sb.append("carrierNetworkChangeMode=" + this.carrierNetworkChangeMode + ',');
        sb.append("userSetup=" + this.userSetup + ',');
        sb.append("dataState=" + this.dataState + ',');
        sb.append("defaultDataOff=" + this.defaultDataOff + ',');
        sb.append("imsRegistered=" + this.imsRegistered + ',');
        sb.append("imsRegistrationTech=" + this.imsRegistrationTech + ',');
        sb.append("voiceCapable=" + this.voiceCapable + ',');
        sb.append("videoCapable=" + this.videoCapable + ',');
        sb.append("mobileDataEnabled=" + this.mobileDataEnabled + ',');
        sb.append("roamingDataEnabled=" + this.roamingDataEnabled + ',');
        sb.append("showQuickSettingsRatIcon=" + showQuickSettingsRatIcon() + ',');
        sb.append("voiceServiceState=" + getVoiceServiceState() + ',');
        sb.append("isInService=" + isInService() + ',');
        StringBuilder sb2 = new StringBuilder("serviceState=");
        ServiceState serviceState2 = this.serviceState;
        String str2 = "(null)";
        if (serviceState2 == null || (str = MobileStateKt.minLog(serviceState2)) == null) {
            str = str2;
        }
        sb.append(sb2.append(str).append(',').toString());
        StringBuilder sb3 = new StringBuilder("signalStrength=");
        SignalStrength signalStrength2 = this.signalStrength;
        if (!(signalStrength2 == null || (access$minLog = MobileStateKt.minLog(signalStrength2)) == null)) {
            str2 = access$minLog;
        }
        sb.append(sb3.append(str2).append(',').toString());
        sb.append("displayInfo=" + this.telephonyDisplayInfo);
    }

    /* access modifiers changed from: protected */
    public List<String> tableColumns() {
        return CollectionsKt.plus(super.tableColumns(), CollectionsKt.listOf("dataSim", "networkName", "networkNameData", "dataConnected", "roaming", "isDefault", "isEmergency", "airplaneMode", "carrierNetworkChangeMode", "userSetup", "dataState", "defaultDataOff", "showQuickSettingsRatIcon", "voiceServiceState", "isInService", "serviceState", "signalStrength", "displayInfo"));
    }

    /* access modifiers changed from: protected */
    public List<String> tableData() {
        String str;
        String access$minLog;
        Object[] objArr = new Object[18];
        objArr[0] = Boolean.valueOf(this.dataSim);
        objArr[1] = this.networkName;
        objArr[2] = this.networkNameData;
        objArr[3] = Boolean.valueOf(this.dataConnected);
        objArr[4] = Boolean.valueOf(this.roaming);
        objArr[5] = Boolean.valueOf(this.isDefault);
        objArr[6] = Boolean.valueOf(this.isEmergency);
        objArr[7] = Boolean.valueOf(this.airplaneMode);
        objArr[8] = Boolean.valueOf(this.carrierNetworkChangeMode);
        objArr[9] = Boolean.valueOf(this.userSetup);
        objArr[10] = Integer.valueOf(this.dataState);
        objArr[11] = Boolean.valueOf(this.defaultDataOff);
        objArr[12] = Boolean.valueOf(showQuickSettingsRatIcon());
        objArr[13] = Integer.valueOf(getVoiceServiceState());
        objArr[14] = Boolean.valueOf(isInService());
        ServiceState serviceState2 = this.serviceState;
        String str2 = "(null)";
        if (serviceState2 == null || (str = MobileStateKt.minLog(serviceState2)) == null) {
            str = str2;
        }
        objArr[15] = str;
        SignalStrength signalStrength2 = this.signalStrength;
        if (!(signalStrength2 == null || (access$minLog = MobileStateKt.minLog(signalStrength2)) == null)) {
            str2 = access$minLog;
        }
        objArr[16] = str2;
        objArr[17] = this.telephonyDisplayInfo;
        Iterable<Object> listOf = CollectionsKt.listOf(objArr);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(listOf, 10));
        for (Object valueOf : listOf) {
            arrayList.add(String.valueOf(valueOf));
        }
        return CollectionsKt.plus(super.tableData(), (List) arrayList);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!Intrinsics.areEqual((Object) getClass(), (Object) obj != null ? obj.getClass() : null) || !super.equals(obj)) {
            return false;
        }
        if (obj != null) {
            MobileState mobileState = (MobileState) obj;
            return Intrinsics.areEqual((Object) this.networkName, (Object) mobileState.networkName) && Intrinsics.areEqual((Object) this.networkNameData, (Object) mobileState.networkNameData) && this.dataSim == mobileState.dataSim && this.dataConnected == mobileState.dataConnected && this.isEmergency == mobileState.isEmergency && this.airplaneMode == mobileState.airplaneMode && this.carrierNetworkChangeMode == mobileState.carrierNetworkChangeMode && this.isDefault == mobileState.isDefault && this.userSetup == mobileState.userSetup && this.roaming == mobileState.roaming && this.dataState == mobileState.dataState && this.defaultDataOff == mobileState.defaultDataOff && this.imsRegistered == mobileState.imsRegistered && this.imsRegistrationTech == mobileState.imsRegistrationTech && this.voiceCapable == mobileState.voiceCapable && this.videoCapable == mobileState.videoCapable && this.mobileDataEnabled == mobileState.mobileDataEnabled && this.roamingDataEnabled == mobileState.roamingDataEnabled && Intrinsics.areEqual((Object) this.telephonyDisplayInfo, (Object) mobileState.telephonyDisplayInfo) && Intrinsics.areEqual((Object) this.serviceState, (Object) mobileState.serviceState) && Intrinsics.areEqual((Object) this.signalStrength, (Object) mobileState.signalStrength);
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.connectivity.MobileState");
    }

    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        String str = this.networkName;
        int i = 0;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.networkNameData;
        int hashCode3 = (((((((((((((((((((((((((((((((((((hashCode2 + (str2 != null ? str2.hashCode() : 0)) * 31) + Boolean.hashCode(this.dataSim)) * 31) + Boolean.hashCode(this.dataConnected)) * 31) + Boolean.hashCode(this.isEmergency)) * 31) + Boolean.hashCode(this.airplaneMode)) * 31) + Boolean.hashCode(this.carrierNetworkChangeMode)) * 31) + Boolean.hashCode(this.isDefault)) * 31) + Boolean.hashCode(this.userSetup)) * 31) + Boolean.hashCode(this.roaming)) * 31) + this.dataState) * 31) + Boolean.hashCode(this.defaultDataOff)) * 31) + Boolean.hashCode(this.imsRegistered)) * 31) + Integer.hashCode(this.imsRegistrationTech)) * 31) + Boolean.hashCode(this.voiceCapable)) * 31) + Boolean.hashCode(this.videoCapable)) * 31) + Boolean.hashCode(this.mobileDataEnabled)) * 31) + Boolean.hashCode(this.roamingDataEnabled)) * 31) + this.telephonyDisplayInfo.hashCode()) * 31;
        ServiceState serviceState2 = this.serviceState;
        int hashCode4 = (hashCode3 + (serviceState2 != null ? serviceState2.hashCode() : 0)) * 31;
        SignalStrength signalStrength2 = this.signalStrength;
        if (signalStrength2 != null) {
            i = signalStrength2.hashCode();
        }
        return hashCode4 + i;
    }
}
