package android.net.wifi;

import android.net.wifi.WifiConfiguration;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.BitSet;
import java.util.Objects;

public final class SecurityParams implements Parcelable {
    public static final Parcelable.Creator<SecurityParams> CREATOR = new Parcelable.Creator<SecurityParams>() {
        public SecurityParams createFromParcel(Parcel parcel) {
            SecurityParams securityParams = new SecurityParams();
            securityParams.mSecurityType = parcel.readInt();
            securityParams.mEnabled = parcel.readBoolean();
            securityParams.mAllowedKeyManagement = SecurityParams.readBitSet(parcel);
            securityParams.mAllowedProtocols = SecurityParams.readBitSet(parcel);
            securityParams.mAllowedAuthAlgorithms = SecurityParams.readBitSet(parcel);
            securityParams.mAllowedPairwiseCiphers = SecurityParams.readBitSet(parcel);
            securityParams.mAllowedGroupCiphers = SecurityParams.readBitSet(parcel);
            securityParams.mAllowedGroupManagementCiphers = SecurityParams.readBitSet(parcel);
            securityParams.mAllowedSuiteBCiphers = SecurityParams.readBitSet(parcel);
            securityParams.mRequirePmf = parcel.readBoolean();
            securityParams.mIsAddedByAutoUpgrade = parcel.readBoolean();
            securityParams.mIsSaeH2eOnlyMode = parcel.readBoolean();
            securityParams.mIsSaePkOnlyMode = parcel.readBoolean();
            return securityParams;
        }

        public SecurityParams[] newArray(int i) {
            return new SecurityParams[i];
        }
    };
    public static final int PASSPOINT_R1 = 1;
    public static final int PASSPOINT_R2 = 2;
    public static final int PASSPOINT_R3 = 3;
    private static final String TAG = "SecurityParams";
    /* access modifiers changed from: private */
    public BitSet mAllowedAuthAlgorithms;
    /* access modifiers changed from: private */
    public BitSet mAllowedGroupCiphers;
    /* access modifiers changed from: private */
    public BitSet mAllowedGroupManagementCiphers;
    /* access modifiers changed from: private */
    public BitSet mAllowedKeyManagement;
    /* access modifiers changed from: private */
    public BitSet mAllowedPairwiseCiphers;
    /* access modifiers changed from: private */
    public BitSet mAllowedProtocols;
    /* access modifiers changed from: private */
    public BitSet mAllowedSuiteBCiphers;
    /* access modifiers changed from: private */
    public boolean mEnabled;
    /* access modifiers changed from: private */
    public boolean mIsAddedByAutoUpgrade;
    /* access modifiers changed from: private */
    public boolean mIsSaeH2eOnlyMode;
    /* access modifiers changed from: private */
    public boolean mIsSaePkOnlyMode;
    private int mPasspointRelease;
    /* access modifiers changed from: private */
    public boolean mRequirePmf;
    /* access modifiers changed from: private */
    public int mSecurityType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PasspointRelease {
    }

    public int describeContents() {
        return 0;
    }

    private SecurityParams() {
        this.mSecurityType = 2;
        this.mEnabled = true;
        this.mAllowedKeyManagement = new BitSet();
        this.mAllowedProtocols = new BitSet();
        this.mAllowedAuthAlgorithms = new BitSet();
        this.mAllowedPairwiseCiphers = new BitSet();
        this.mAllowedGroupCiphers = new BitSet();
        this.mAllowedGroupManagementCiphers = new BitSet();
        this.mAllowedSuiteBCiphers = new BitSet();
        this.mRequirePmf = false;
        this.mPasspointRelease = 2;
        this.mIsSaeH2eOnlyMode = false;
        this.mIsSaePkOnlyMode = false;
        this.mIsAddedByAutoUpgrade = false;
    }

    public SecurityParams(SecurityParams securityParams) {
        this.mSecurityType = 2;
        this.mEnabled = true;
        this.mAllowedKeyManagement = new BitSet();
        this.mAllowedProtocols = new BitSet();
        this.mAllowedAuthAlgorithms = new BitSet();
        this.mAllowedPairwiseCiphers = new BitSet();
        this.mAllowedGroupCiphers = new BitSet();
        this.mAllowedGroupManagementCiphers = new BitSet();
        this.mAllowedSuiteBCiphers = new BitSet();
        this.mRequirePmf = false;
        this.mPasspointRelease = 2;
        this.mIsSaeH2eOnlyMode = false;
        this.mIsSaePkOnlyMode = false;
        this.mIsAddedByAutoUpgrade = false;
        this.mSecurityType = securityParams.mSecurityType;
        this.mEnabled = securityParams.mEnabled;
        this.mAllowedKeyManagement = (BitSet) securityParams.mAllowedKeyManagement.clone();
        this.mAllowedProtocols = (BitSet) securityParams.mAllowedProtocols.clone();
        this.mAllowedAuthAlgorithms = (BitSet) securityParams.mAllowedAuthAlgorithms.clone();
        this.mAllowedPairwiseCiphers = (BitSet) securityParams.mAllowedPairwiseCiphers.clone();
        this.mAllowedGroupCiphers = (BitSet) securityParams.mAllowedGroupCiphers.clone();
        this.mAllowedGroupManagementCiphers = (BitSet) securityParams.mAllowedGroupManagementCiphers.clone();
        this.mAllowedSuiteBCiphers = (BitSet) securityParams.mAllowedSuiteBCiphers.clone();
        this.mRequirePmf = securityParams.mRequirePmf;
        this.mIsSaeH2eOnlyMode = securityParams.mIsSaeH2eOnlyMode;
        this.mIsSaePkOnlyMode = securityParams.mIsSaePkOnlyMode;
        this.mIsAddedByAutoUpgrade = securityParams.mIsAddedByAutoUpgrade;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SecurityParams)) {
            return false;
        }
        SecurityParams securityParams = (SecurityParams) obj;
        return this.mSecurityType == securityParams.mSecurityType && this.mEnabled == securityParams.mEnabled && this.mAllowedKeyManagement.equals(securityParams.mAllowedKeyManagement) && this.mAllowedProtocols.equals(securityParams.mAllowedProtocols) && this.mAllowedAuthAlgorithms.equals(securityParams.mAllowedAuthAlgorithms) && this.mAllowedPairwiseCiphers.equals(securityParams.mAllowedPairwiseCiphers) && this.mAllowedGroupCiphers.equals(securityParams.mAllowedGroupCiphers) && this.mAllowedGroupManagementCiphers.equals(securityParams.mAllowedGroupManagementCiphers) && this.mAllowedSuiteBCiphers.equals(securityParams.mAllowedSuiteBCiphers) && this.mRequirePmf == securityParams.mRequirePmf && this.mIsSaeH2eOnlyMode == securityParams.mIsSaeH2eOnlyMode && this.mIsSaePkOnlyMode == securityParams.mIsSaePkOnlyMode && this.mIsAddedByAutoUpgrade == securityParams.mIsAddedByAutoUpgrade;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mSecurityType), Boolean.valueOf(this.mEnabled), this.mAllowedKeyManagement, this.mAllowedProtocols, this.mAllowedAuthAlgorithms, this.mAllowedPairwiseCiphers, this.mAllowedGroupCiphers, this.mAllowedGroupManagementCiphers, this.mAllowedSuiteBCiphers, Boolean.valueOf(this.mRequirePmf), Boolean.valueOf(this.mIsSaeH2eOnlyMode), Boolean.valueOf(this.mIsSaePkOnlyMode), Boolean.valueOf(this.mIsAddedByAutoUpgrade));
    }

    public int getSecurityType() {
        return this.mSecurityType;
    }

    public boolean isSecurityType(int i) {
        return i == this.mSecurityType;
    }

    public boolean isSameSecurityType(SecurityParams securityParams) {
        return securityParams.mSecurityType == this.mSecurityType;
    }

    public void updateLegacyWifiConfiguration(WifiConfiguration wifiConfiguration) {
        wifiConfiguration.allowedKeyManagement = (BitSet) this.mAllowedKeyManagement.clone();
        wifiConfiguration.allowedProtocols = (BitSet) this.mAllowedProtocols.clone();
        wifiConfiguration.allowedAuthAlgorithms = (BitSet) this.mAllowedAuthAlgorithms.clone();
        wifiConfiguration.allowedPairwiseCiphers = (BitSet) this.mAllowedPairwiseCiphers.clone();
        wifiConfiguration.allowedGroupCiphers = (BitSet) this.mAllowedGroupCiphers.clone();
        wifiConfiguration.allowedGroupManagementCiphers = (BitSet) this.mAllowedGroupManagementCiphers.clone();
        wifiConfiguration.allowedSuiteBCiphers = (BitSet) this.mAllowedSuiteBCiphers.clone();
        wifiConfiguration.requirePmf = this.mRequirePmf;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void enableFils(boolean z, boolean z2) {
        if (z) {
            this.mAllowedKeyManagement.set(15);
        }
        if (z2) {
            this.mAllowedKeyManagement.set(16);
        }
    }

    public BitSet getAllowedKeyManagement() {
        return (BitSet) this.mAllowedKeyManagement.clone();
    }

    public BitSet getAllowedProtocols() {
        return (BitSet) this.mAllowedProtocols.clone();
    }

    public BitSet getAllowedAuthAlgorithms() {
        return (BitSet) this.mAllowedAuthAlgorithms.clone();
    }

    public BitSet getAllowedPairwiseCiphers() {
        return (BitSet) this.mAllowedPairwiseCiphers.clone();
    }

    public BitSet getAllowedGroupCiphers() {
        return (BitSet) this.mAllowedGroupCiphers.clone();
    }

    public BitSet getAllowedGroupManagementCiphers() {
        return (BitSet) this.mAllowedGroupManagementCiphers.clone();
    }

    public void enableSuiteBCiphers(boolean z, boolean z2) {
        if (z) {
            this.mAllowedSuiteBCiphers.set(0);
        } else {
            this.mAllowedSuiteBCiphers.clear(0);
        }
        if (z2) {
            this.mAllowedSuiteBCiphers.set(1);
        } else {
            this.mAllowedSuiteBCiphers.clear(1);
        }
    }

    public BitSet getAllowedSuiteBCiphers() {
        return (BitSet) this.mAllowedSuiteBCiphers.clone();
    }

    public void setRequirePmf(boolean z) {
        this.mRequirePmf = z;
    }

    public boolean isRequirePmf() {
        return this.mRequirePmf;
    }

    public boolean isOpenSecurityType() {
        if (isSecurityType(0) || isSecurityType(6)) {
            return true;
        }
        return false;
    }

    public boolean isEnterpriseSecurityType() {
        return this.mAllowedKeyManagement.get(2) || this.mAllowedKeyManagement.get(3) || this.mAllowedKeyManagement.get(10) || this.mAllowedKeyManagement.get(14);
    }

    public void enableSaeH2eOnlyMode(boolean z) {
        this.mIsSaeH2eOnlyMode = z;
    }

    public boolean isSaeH2eOnlyMode() {
        return this.mIsSaeH2eOnlyMode;
    }

    public void enableSaePkOnlyMode(boolean z) {
        this.mIsSaePkOnlyMode = z;
    }

    public boolean isSaePkOnlyMode() {
        return this.mIsSaePkOnlyMode;
    }

    public void setIsAddedByAutoUpgrade(boolean z) {
        this.mIsAddedByAutoUpgrade = z;
    }

    public boolean isAddedByAutoUpgrade() {
        return this.mIsAddedByAutoUpgrade;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Security Parameters:\n Type: ");
        sb.append(this.mSecurityType);
        sb.append("\n Enabled: ");
        sb.append(this.mEnabled);
        sb.append("\n KeyMgmt:");
        for (int i = 0; i < this.mAllowedKeyManagement.size(); i++) {
            if (this.mAllowedKeyManagement.get(i)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i < WifiConfiguration.KeyMgmt.strings.length) {
                    sb.append(WifiConfiguration.KeyMgmt.strings[i]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n Protocols:");
        for (int i2 = 0; i2 < this.mAllowedProtocols.size(); i2++) {
            if (this.mAllowedProtocols.get(i2)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i2 < WifiConfiguration.Protocol.strings.length) {
                    sb.append(WifiConfiguration.Protocol.strings[i2]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n AuthAlgorithms:");
        for (int i3 = 0; i3 < this.mAllowedAuthAlgorithms.size(); i3++) {
            if (this.mAllowedAuthAlgorithms.get(i3)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i3 < WifiConfiguration.AuthAlgorithm.strings.length) {
                    sb.append(WifiConfiguration.AuthAlgorithm.strings[i3]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n PairwiseCiphers:");
        for (int i4 = 0; i4 < this.mAllowedPairwiseCiphers.size(); i4++) {
            if (this.mAllowedPairwiseCiphers.get(i4)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i4 < WifiConfiguration.PairwiseCipher.strings.length) {
                    sb.append(WifiConfiguration.PairwiseCipher.strings[i4]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n GroupCiphers:");
        for (int i5 = 0; i5 < this.mAllowedGroupCiphers.size(); i5++) {
            if (this.mAllowedGroupCiphers.get(i5)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i5 < WifiConfiguration.GroupCipher.strings.length) {
                    sb.append(WifiConfiguration.GroupCipher.strings[i5]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n GroupMgmtCiphers:");
        for (int i6 = 0; i6 < this.mAllowedGroupManagementCiphers.size(); i6++) {
            if (this.mAllowedGroupManagementCiphers.get(i6)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i6 < WifiConfiguration.GroupMgmtCipher.strings.length) {
                    sb.append(WifiConfiguration.GroupMgmtCipher.strings[i6]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n SuiteBCiphers:");
        for (int i7 = 0; i7 < this.mAllowedSuiteBCiphers.size(); i7++) {
            if (this.mAllowedSuiteBCiphers.get(i7)) {
                sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                if (i7 < WifiConfiguration.SuiteBCipher.strings.length) {
                    sb.append(WifiConfiguration.SuiteBCipher.strings[i7]);
                } else {
                    sb.append("??");
                }
            }
        }
        sb.append("\n RequirePmf: ");
        sb.append(this.mRequirePmf);
        sb.append("\n IsAddedByAutoUpgrade: ");
        sb.append(this.mIsAddedByAutoUpgrade);
        sb.append("\n IsSaeH2eOnlyMode: ");
        sb.append(this.mIsSaeH2eOnlyMode);
        sb.append("\n IsSaePkOnlyMode: ");
        sb.append(this.mIsSaePkOnlyMode);
        sb.append("\n");
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static BitSet readBitSet(Parcel parcel) {
        int readInt = parcel.readInt();
        BitSet bitSet = new BitSet();
        for (int i = 0; i < readInt; i++) {
            bitSet.set(parcel.readInt());
        }
        return bitSet;
    }

    private static void writeBitSet(Parcel parcel, BitSet bitSet) {
        parcel.writeInt(bitSet.cardinality());
        int i = -1;
        while (true) {
            i = bitSet.nextSetBit(i + 1);
            if (i != -1) {
                parcel.writeInt(i);
            } else {
                return;
            }
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mSecurityType);
        parcel.writeBoolean(this.mEnabled);
        writeBitSet(parcel, this.mAllowedKeyManagement);
        writeBitSet(parcel, this.mAllowedProtocols);
        writeBitSet(parcel, this.mAllowedAuthAlgorithms);
        writeBitSet(parcel, this.mAllowedPairwiseCiphers);
        writeBitSet(parcel, this.mAllowedGroupCiphers);
        writeBitSet(parcel, this.mAllowedGroupManagementCiphers);
        writeBitSet(parcel, this.mAllowedSuiteBCiphers);
        parcel.writeBoolean(this.mRequirePmf);
        parcel.writeBoolean(this.mIsAddedByAutoUpgrade);
        parcel.writeBoolean(this.mIsSaeH2eOnlyMode);
        parcel.writeBoolean(this.mIsSaePkOnlyMode);
    }

    public static SecurityParams createSecurityParamsBySecurityType(int i) {
        switch (i) {
            case 0:
                return createOpenParams();
            case 1:
                return createWepParams();
            case 2:
                return createWpaWpa2PersonalParams();
            case 3:
                return createWpaWpa2EnterpriseParams();
            case 4:
                return createWpa3PersonalParams();
            case 5:
                return createWpa3Enterprise192BitParams();
            case 6:
                return createEnhancedOpenParams();
            case 7:
                return createWapiPskParams();
            case 8:
                return createWapiCertParams();
            case 9:
                return createWpa3EnterpriseParams();
            case 10:
                return createOsenParams();
            case 11:
                return createPasspointParams(2);
            case 12:
                return createPasspointParams(3);
            case 13:
                return createDppParams();
            default:
                throw new IllegalArgumentException("unknown security type " + i);
        }
    }

    private static SecurityParams createWpaWpa2EnterpriseParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 3;
        securityParams.mAllowedKeyManagement.set(2);
        securityParams.mAllowedKeyManagement.set(3);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedProtocols.set(0);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(1);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(2);
        return securityParams;
    }

    private static SecurityParams createPasspointParams(int i) {
        SecurityParams securityParams = new SecurityParams();
        if (i == 1 || i == 2) {
            securityParams.mSecurityType = 11;
        } else if (i == 3) {
            securityParams.mSecurityType = 12;
            securityParams.mRequirePmf = true;
        } else {
            throw new IllegalArgumentException("invalid passpoint release " + i);
        }
        securityParams.mAllowedKeyManagement.set(2);
        securityParams.mAllowedKeyManagement.set(3);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedGroupCiphers.set(3);
        return securityParams;
    }

    private static SecurityParams createEnhancedOpenParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 6;
        securityParams.mAllowedKeyManagement.set(9);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(5);
        securityParams.mAllowedPairwiseCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(7);
        securityParams.mAllowedGroupCiphers.set(5);
        securityParams.mRequirePmf = true;
        return securityParams;
    }

    private static SecurityParams createOpenParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 0;
        securityParams.mAllowedKeyManagement.set(0);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedProtocols.set(0);
        return securityParams;
    }

    private static SecurityParams createOsenParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 10;
        securityParams.mAllowedKeyManagement.set(5);
        securityParams.mAllowedProtocols.set(2);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(1);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(2);
        return securityParams;
    }

    private static SecurityParams createWapiCertParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 8;
        securityParams.mAllowedKeyManagement.set(14);
        securityParams.mAllowedProtocols.set(3);
        securityParams.mAllowedPairwiseCiphers.set(4);
        securityParams.mAllowedGroupCiphers.set(6);
        return securityParams;
    }

    private static SecurityParams createWapiPskParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 7;
        securityParams.mAllowedKeyManagement.set(13);
        securityParams.mAllowedProtocols.set(3);
        securityParams.mAllowedPairwiseCiphers.set(4);
        securityParams.mAllowedGroupCiphers.set(6);
        return securityParams;
    }

    private static SecurityParams createWepParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 1;
        securityParams.mAllowedKeyManagement.set(0);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedAuthAlgorithms.set(0);
        securityParams.mAllowedAuthAlgorithms.set(1);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(1);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(2);
        securityParams.mAllowedGroupCiphers.set(0);
        securityParams.mAllowedGroupCiphers.set(1);
        return securityParams;
    }

    private static SecurityParams createWpa3Enterprise192BitParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 5;
        securityParams.mAllowedKeyManagement.set(2);
        securityParams.mAllowedKeyManagement.set(3);
        securityParams.mAllowedKeyManagement.set(10);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedPairwiseCiphers.set(5);
        securityParams.mAllowedPairwiseCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(7);
        securityParams.mAllowedGroupCiphers.set(5);
        securityParams.mAllowedGroupManagementCiphers.set(2);
        securityParams.mRequirePmf = true;
        return securityParams;
    }

    private static SecurityParams createWpa3EnterpriseParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 9;
        securityParams.mAllowedKeyManagement.set(2);
        securityParams.mAllowedKeyManagement.set(3);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(5);
        securityParams.mRequirePmf = true;
        return securityParams;
    }

    private static SecurityParams createWpa3PersonalParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 4;
        securityParams.mAllowedKeyManagement.set(8);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(5);
        securityParams.mAllowedPairwiseCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(7);
        securityParams.mAllowedGroupCiphers.set(5);
        securityParams.mRequirePmf = true;
        return securityParams;
    }

    private static SecurityParams createWpaWpa2PersonalParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 2;
        securityParams.mAllowedKeyManagement.set(1);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedProtocols.set(0);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(1);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(2);
        securityParams.mAllowedGroupCiphers.set(0);
        securityParams.mAllowedGroupCiphers.set(1);
        return securityParams;
    }

    private static SecurityParams createDppParams() {
        SecurityParams securityParams = new SecurityParams();
        securityParams.mSecurityType = 13;
        securityParams.mAllowedKeyManagement.set(17);
        securityParams.mAllowedProtocols.set(1);
        securityParams.mAllowedPairwiseCiphers.set(2);
        securityParams.mAllowedPairwiseCiphers.set(5);
        securityParams.mAllowedPairwiseCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(3);
        securityParams.mAllowedGroupCiphers.set(7);
        securityParams.mAllowedGroupCiphers.set(5);
        securityParams.mRequirePmf = true;
        return securityParams;
    }
}
