package com.android.settingslib;

import java.util.Objects;
/* loaded from: classes.dex */
public class SignalIcon$MobileState extends SignalIcon$State {
    public boolean airplaneMode;
    public boolean carrierNetworkChangeMode;
    public boolean dataConnected;
    public boolean dataSim;
    public boolean defaultDataOff;
    public boolean imsRegistered;
    public boolean isDefault;
    public boolean isEmergency;
    public boolean mobileDataEnabled;
    public String networkName;
    public String networkNameData;
    public boolean roaming;
    public boolean roamingDataEnabled;
    public boolean userSetup;
    public boolean videoCapable;
    public boolean voiceCapable;

    @Override // com.android.settingslib.SignalIcon$State
    public void copyFrom(SignalIcon$State signalIcon$State) {
        super.copyFrom(signalIcon$State);
        SignalIcon$MobileState signalIcon$MobileState = (SignalIcon$MobileState) signalIcon$State;
        this.dataSim = signalIcon$MobileState.dataSim;
        this.networkName = signalIcon$MobileState.networkName;
        this.networkNameData = signalIcon$MobileState.networkNameData;
        this.dataConnected = signalIcon$MobileState.dataConnected;
        this.isDefault = signalIcon$MobileState.isDefault;
        this.isEmergency = signalIcon$MobileState.isEmergency;
        this.airplaneMode = signalIcon$MobileState.airplaneMode;
        this.carrierNetworkChangeMode = signalIcon$MobileState.carrierNetworkChangeMode;
        this.userSetup = signalIcon$MobileState.userSetup;
        this.roaming = signalIcon$MobileState.roaming;
        this.defaultDataOff = signalIcon$MobileState.defaultDataOff;
        this.imsRegistered = signalIcon$MobileState.imsRegistered;
        this.voiceCapable = signalIcon$MobileState.voiceCapable;
        this.videoCapable = signalIcon$MobileState.videoCapable;
        this.mobileDataEnabled = signalIcon$MobileState.mobileDataEnabled;
        this.roamingDataEnabled = signalIcon$MobileState.roamingDataEnabled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settingslib.SignalIcon$State
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(',');
        sb.append("dataSim=");
        sb.append(this.dataSim);
        sb.append(',');
        sb.append("networkName=");
        sb.append(this.networkName);
        sb.append(',');
        sb.append("networkNameData=");
        sb.append(this.networkNameData);
        sb.append(',');
        sb.append("dataConnected=");
        sb.append(this.dataConnected);
        sb.append(',');
        sb.append("roaming=");
        sb.append(this.roaming);
        sb.append(',');
        sb.append("isDefault=");
        sb.append(this.isDefault);
        sb.append(',');
        sb.append("isEmergency=");
        sb.append(this.isEmergency);
        sb.append(',');
        sb.append("airplaneMode=");
        sb.append(this.airplaneMode);
        sb.append(',');
        sb.append("carrierNetworkChangeMode=");
        sb.append(this.carrierNetworkChangeMode);
        sb.append(',');
        sb.append("userSetup=");
        sb.append(this.userSetup);
        sb.append(',');
        sb.append("defaultDataOff=");
        sb.append(this.defaultDataOff);
        sb.append("imsRegistered=");
        sb.append(this.imsRegistered);
        sb.append(',');
        sb.append("voiceCapable=");
        sb.append(this.voiceCapable);
        sb.append(',');
        sb.append("videoCapable=");
        sb.append(this.videoCapable);
        sb.append(',');
        sb.append("mobileDataEnabled=");
        sb.append(this.mobileDataEnabled);
        sb.append(',');
        sb.append("roamingDataEnabled=");
        sb.append(this.roamingDataEnabled);
    }

    @Override // com.android.settingslib.SignalIcon$State
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            SignalIcon$MobileState signalIcon$MobileState = (SignalIcon$MobileState) obj;
            if (Objects.equals(signalIcon$MobileState.networkName, this.networkName) && Objects.equals(signalIcon$MobileState.networkNameData, this.networkNameData) && signalIcon$MobileState.dataSim == this.dataSim && signalIcon$MobileState.dataConnected == this.dataConnected && signalIcon$MobileState.isEmergency == this.isEmergency && signalIcon$MobileState.airplaneMode == this.airplaneMode && signalIcon$MobileState.carrierNetworkChangeMode == this.carrierNetworkChangeMode && signalIcon$MobileState.userSetup == this.userSetup && signalIcon$MobileState.isDefault == this.isDefault && signalIcon$MobileState.roaming == this.roaming && signalIcon$MobileState.defaultDataOff == this.defaultDataOff && signalIcon$MobileState.imsRegistered == this.imsRegistered && signalIcon$MobileState.voiceCapable == this.voiceCapable && signalIcon$MobileState.videoCapable == this.videoCapable && signalIcon$MobileState.mobileDataEnabled == this.mobileDataEnabled && signalIcon$MobileState.roamingDataEnabled == this.roamingDataEnabled) {
                return true;
            }
        }
        return false;
    }
}
