package android.net.wifi;

import android.annotation.SystemApi;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.android.wifi.p018x.com.android.modules.utils.build.SdkLevel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.util.locale.BaseLocale;

public class WifiEnterpriseConfig implements Parcelable {
    public static final String ALTSUBJECT_MATCH_KEY = "altsubject_match";
    public static final String ANON_IDENTITY_KEY = "anonymous_identity";
    private static final String CA_CERTIFICATE = "CACERT_";
    public static final String CA_CERT_ALIAS_DELIMITER = " ";
    public static final String CA_CERT_KEY = "ca_cert";
    public static final String CA_CERT_PREFIX = "keystore://CACERT_";
    public static final String CA_PATH_KEY = "ca_path";
    public static final String CLIENT_CERT_KEY = "client_cert";
    public static final String CLIENT_CERT_PREFIX = "keystore://USRCERT_";
    public static final Parcelable.Creator<WifiEnterpriseConfig> CREATOR = new Parcelable.Creator<WifiEnterpriseConfig>() {
        public WifiEnterpriseConfig createFromParcel(Parcel parcel) {
            WifiEnterpriseConfig wifiEnterpriseConfig = new WifiEnterpriseConfig();
            int readInt = parcel.readInt();
            for (int i = 0; i < readInt; i++) {
                wifiEnterpriseConfig.mFields.put(parcel.readString(), parcel.readString());
            }
            wifiEnterpriseConfig.mEapMethod = parcel.readInt();
            wifiEnterpriseConfig.mPhase2Method = parcel.readInt();
            wifiEnterpriseConfig.mCaCerts = ParcelUtil.readCertificates(parcel);
            wifiEnterpriseConfig.mClientPrivateKey = ParcelUtil.readPrivateKey(parcel);
            wifiEnterpriseConfig.mClientCertificateChain = ParcelUtil.readCertificates(parcel);
            wifiEnterpriseConfig.mKeyChainAlias = parcel.readString();
            wifiEnterpriseConfig.mIsAppInstalledDeviceKeyAndCert = parcel.readBoolean();
            wifiEnterpriseConfig.mIsAppInstalledCaCert = parcel.readBoolean();
            wifiEnterpriseConfig.mOcsp = parcel.readInt();
            wifiEnterpriseConfig.mIsTrustOnFirstUseEnabled = parcel.readBoolean();
            wifiEnterpriseConfig.mUserApproveNoCaCert = parcel.readBoolean();
            return wifiEnterpriseConfig;
        }

        public WifiEnterpriseConfig[] newArray(int i) {
            return new WifiEnterpriseConfig[i];
        }
    };
    public static final String DECORATED_IDENTITY_PREFIX_KEY = "decorated_username_prefix";
    public static final String DOM_SUFFIX_MATCH_KEY = "domain_suffix_match";
    public static final String EAP_ERP = "eap_erp";
    public static final String EAP_KEY = "eap";
    public static final String EMPTY_VALUE = "NULL";
    public static final String ENGINE_DISABLE = "0";
    public static final String ENGINE_ENABLE = "1";
    public static final String ENGINE_ID_KEY = "engine_id";
    public static final String ENGINE_ID_KEYSTORE = "keystore";
    public static final String ENGINE_KEY = "engine";
    public static final String EXTRA_WAPI_AS_CERTIFICATE_DATA = "android.net.wifi.extra.WAPI_AS_CERTIFICATE_DATA";
    public static final String EXTRA_WAPI_AS_CERTIFICATE_NAME = "android.net.wifi.extra.WAPI_AS_CERTIFICATE_NAME";
    public static final String EXTRA_WAPI_USER_CERTIFICATE_DATA = "android.net.wifi.extra.WAPI_USER_CERTIFICATE_DATA";
    public static final String EXTRA_WAPI_USER_CERTIFICATE_NAME = "android.net.wifi.extra.WAPI_USER_CERTIFICATE_NAME";
    public static final String IDENTITY_KEY = "identity";
    public static final String KEYSTORES_URI = "keystores://";
    public static final String KEYSTORE_URI = "keystore://";
    public static final String OCSP = "ocsp";
    @SystemApi
    public static final int OCSP_NONE = 0;
    @SystemApi
    public static final int OCSP_REQUEST_CERT_STATUS = 1;
    @SystemApi
    public static final int OCSP_REQUIRE_ALL_NON_TRUSTED_CERTS_STATUS = 3;
    @SystemApi
    public static final int OCSP_REQUIRE_CERT_STATUS = 2;
    public static final String OPP_KEY_CACHING = "proactive_key_caching";
    public static final String PASSWORD_KEY = "password";
    public static final String PHASE2_KEY = "phase2";
    public static final String PLMN_KEY = "plmn";
    public static final String PRIVATE_KEY_ID_KEY = "key_id";
    public static final String REALM_KEY = "realm";
    public static final String SUBJECT_MATCH_KEY = "subject_match";
    private static final String[] SUPPLICANT_CONFIG_KEYS = {IDENTITY_KEY, ANON_IDENTITY_KEY, PASSWORD_KEY, CLIENT_CERT_KEY, CA_CERT_KEY, SUBJECT_MATCH_KEY, ENGINE_KEY, ENGINE_ID_KEY, "key_id", ALTSUBJECT_MATCH_KEY, DOM_SUFFIX_MATCH_KEY, CA_PATH_KEY};
    private static final String TAG = "WifiEnterpriseConfig";
    private static final List<String> UNQUOTED_KEYS = Arrays.asList(ENGINE_KEY, OPP_KEY_CACHING, EAP_ERP);
    private static final String USER_CERTIFICATE = "USRCERT_";
    private static final String USER_PRIVATE_KEY = "USRPKEY_";
    public static final String WAPI_AS_CERTIFICATE = "WAPIAS_";
    public static final String WAPI_CERT_SUITE_KEY = "wapi_cert_suite";
    public static final String WAPI_USER_CERTIFICATE = "WAPIUSR_";
    /* access modifiers changed from: private */
    public X509Certificate[] mCaCerts;
    /* access modifiers changed from: private */
    public X509Certificate[] mClientCertificateChain;
    /* access modifiers changed from: private */
    public PrivateKey mClientPrivateKey;
    /* access modifiers changed from: private */
    public int mEapMethod = -1;
    /* access modifiers changed from: private */
    public HashMap<String, String> mFields = new HashMap<>();
    /* access modifiers changed from: private */
    public boolean mIsAppInstalledCaCert = false;
    /* access modifiers changed from: private */
    public boolean mIsAppInstalledDeviceKeyAndCert = false;
    /* access modifiers changed from: private */
    public boolean mIsTrustOnFirstUseEnabled = false;
    /* access modifiers changed from: private */
    public String mKeyChainAlias;
    /* access modifiers changed from: private */
    public int mOcsp = 0;
    /* access modifiers changed from: private */
    public int mPhase2Method = 0;
    private long mSelectedRcoi = 0;
    /* access modifiers changed from: private */
    public boolean mUserApproveNoCaCert = false;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Ocsp {
    }

    public interface SupplicantLoader {
        String loadValue(String str);
    }

    public interface SupplicantSaver {
        boolean saveValue(String str, String str2);
    }

    public int describeContents() {
        return 0;
    }

    public WifiEnterpriseConfig() {
    }

    private void copyFrom(WifiEnterpriseConfig wifiEnterpriseConfig, boolean z, String str) {
        for (String next : wifiEnterpriseConfig.mFields.keySet()) {
            if (!z || !next.equals(PASSWORD_KEY) || !TextUtils.equals(wifiEnterpriseConfig.mFields.get(next), str)) {
                this.mFields.put(next, wifiEnterpriseConfig.mFields.get(next));
            }
        }
        X509Certificate[] x509CertificateArr = wifiEnterpriseConfig.mCaCerts;
        if (x509CertificateArr != null) {
            this.mCaCerts = (X509Certificate[]) Arrays.copyOf((T[]) x509CertificateArr, x509CertificateArr.length);
        } else {
            this.mCaCerts = null;
        }
        this.mClientPrivateKey = wifiEnterpriseConfig.mClientPrivateKey;
        X509Certificate[] x509CertificateArr2 = wifiEnterpriseConfig.mClientCertificateChain;
        if (x509CertificateArr2 != null) {
            this.mClientCertificateChain = (X509Certificate[]) Arrays.copyOf((T[]) x509CertificateArr2, x509CertificateArr2.length);
        } else {
            this.mClientCertificateChain = null;
        }
        this.mKeyChainAlias = wifiEnterpriseConfig.mKeyChainAlias;
        this.mEapMethod = wifiEnterpriseConfig.mEapMethod;
        this.mPhase2Method = wifiEnterpriseConfig.mPhase2Method;
        this.mIsAppInstalledDeviceKeyAndCert = wifiEnterpriseConfig.mIsAppInstalledDeviceKeyAndCert;
        this.mIsAppInstalledCaCert = wifiEnterpriseConfig.mIsAppInstalledCaCert;
        this.mOcsp = wifiEnterpriseConfig.mOcsp;
        this.mIsTrustOnFirstUseEnabled = wifiEnterpriseConfig.mIsTrustOnFirstUseEnabled;
        this.mUserApproveNoCaCert = wifiEnterpriseConfig.mUserApproveNoCaCert;
        this.mSelectedRcoi = wifiEnterpriseConfig.mSelectedRcoi;
    }

    public WifiEnterpriseConfig(WifiEnterpriseConfig wifiEnterpriseConfig) {
        copyFrom(wifiEnterpriseConfig, false, "");
    }

    public void copyFromExternal(WifiEnterpriseConfig wifiEnterpriseConfig, String str) {
        copyFrom(wifiEnterpriseConfig, true, convertToQuotedString(str));
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mFields.size());
        for (Map.Entry next : this.mFields.entrySet()) {
            parcel.writeString((String) next.getKey());
            parcel.writeString((String) next.getValue());
        }
        parcel.writeInt(this.mEapMethod);
        parcel.writeInt(this.mPhase2Method);
        ParcelUtil.writeCertificates(parcel, this.mCaCerts);
        ParcelUtil.writePrivateKey(parcel, this.mClientPrivateKey);
        ParcelUtil.writeCertificates(parcel, this.mClientCertificateChain);
        parcel.writeString(this.mKeyChainAlias);
        parcel.writeBoolean(this.mIsAppInstalledDeviceKeyAndCert);
        parcel.writeBoolean(this.mIsAppInstalledCaCert);
        parcel.writeInt(this.mOcsp);
        parcel.writeBoolean(this.mIsTrustOnFirstUseEnabled);
        parcel.writeBoolean(this.mUserApproveNoCaCert);
    }

    public static final class Eap {
        public static final int AKA = 5;
        public static final int AKA_PRIME = 6;
        public static final int NONE = -1;
        public static final int PEAP = 0;
        public static final int PWD = 3;
        public static final int SIM = 4;
        public static final int TLS = 1;
        public static final int TTLS = 2;
        public static final int UNAUTH_TLS = 7;
        public static final int WAPI_CERT = 8;
        public static final String[] strings = {"PEAP", "TLS", "TTLS", "PWD", "SIM", "AKA", "AKA'", "WFA-UNAUTH-TLS", "WAPI_CERT"};

        private Eap() {
        }
    }

    public static final class Phase2 {
        public static final int AKA = 6;
        public static final int AKA_PRIME = 7;
        private static final String AUTHEAP_PREFIX = "autheap=";
        private static final String AUTH_PREFIX = "auth=";
        public static final int GTC = 4;
        public static final int MSCHAP = 2;
        public static final int MSCHAPV2 = 3;
        public static final int NONE = 0;
        public static final int PAP = 1;
        public static final int SIM = 5;
        public static final String[] strings = {WifiEnterpriseConfig.EMPTY_VALUE, Credential.UserCredential.AUTH_METHOD_PAP, "MSCHAP", "MSCHAPV2", "GTC", "SIM", "AKA", "AKA'"};

        private Phase2() {
        }
    }

    public boolean saveToSupplicant(SupplicantSaver supplicantSaver) {
        int i;
        boolean z = false;
        if (!isEapMethodValid()) {
            return false;
        }
        int i2 = this.mEapMethod;
        boolean z2 = i2 == 4 || i2 == 5 || i2 == 6;
        for (String next : this.mFields.keySet()) {
            if ((!z2 || !ANON_IDENTITY_KEY.equals(next)) && !supplicantSaver.saveValue(next, this.mFields.get(next))) {
                return false;
            }
        }
        if (!supplicantSaver.saveValue(EAP_KEY, Eap.strings[this.mEapMethod])) {
            return false;
        }
        int i3 = this.mEapMethod;
        if (i3 != 1 && i3 != 7 && (i = this.mPhase2Method) != 0) {
            if (i3 == 2 && i == 4) {
                z = true;
            }
            return supplicantSaver.saveValue(PHASE2_KEY, convertToQuotedString((z ? "autheap=" : "auth=") + Phase2.strings[this.mPhase2Method]));
        } else if (this.mPhase2Method == 0) {
            return supplicantSaver.saveValue(PHASE2_KEY, (String) null);
        } else {
            Log.e(TAG, "WiFi enterprise configuration is invalid as it supplies a phase 2 method but the phase1 method does not support it.");
            return false;
        }
    }

    public void loadFromSupplicant(SupplicantLoader supplicantLoader) {
        for (String str : SUPPLICANT_CONFIG_KEYS) {
            String loadValue = supplicantLoader.loadValue(str);
            if (loadValue == null) {
                this.mFields.put(str, EMPTY_VALUE);
            } else {
                this.mFields.put(str, loadValue);
            }
        }
        this.mEapMethod = getStringIndex(Eap.strings, supplicantLoader.loadValue(EAP_KEY), -1);
        String removeDoubleQuotes = removeDoubleQuotes(supplicantLoader.loadValue(PHASE2_KEY));
        if (removeDoubleQuotes.startsWith("auth=")) {
            removeDoubleQuotes = removeDoubleQuotes.substring(5);
        } else if (removeDoubleQuotes.startsWith("autheap=")) {
            removeDoubleQuotes = removeDoubleQuotes.substring(8);
        }
        this.mPhase2Method = getStringIndex(Phase2.strings, removeDoubleQuotes, 0);
    }

    public void setEapMethod(int i) {
        switch (i) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                break;
            case 1:
            case 7:
                setPhase2Method(0);
                break;
            case 8:
                this.mEapMethod = i;
                setPhase2Method(0);
                return;
            default:
                throw new IllegalArgumentException("Unknown EAP method");
        }
        this.mEapMethod = i;
        setFieldValue(OPP_KEY_CACHING, "1");
    }

    public int getEapMethod() {
        return this.mEapMethod;
    }

    public void setPhase2Method(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                this.mPhase2Method = i;
                return;
            default:
                throw new IllegalArgumentException("Unknown Phase 2 method");
        }
    }

    public int getPhase2Method() {
        return this.mPhase2Method;
    }

    public void setIdentity(String str) {
        setFieldValue(IDENTITY_KEY, str, "");
    }

    public String getIdentity() {
        return getFieldValue(IDENTITY_KEY);
    }

    public void setAnonymousIdentity(String str) {
        setFieldValue(ANON_IDENTITY_KEY, str);
    }

    public String getAnonymousIdentity() {
        return getFieldValue(ANON_IDENTITY_KEY);
    }

    public void setPassword(String str) {
        setFieldValue(PASSWORD_KEY, str);
    }

    public String getPassword() {
        return getFieldValue(PASSWORD_KEY);
    }

    public static String encodeCaCertificateAlias(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", Integer.valueOf((int) bytes[i] & 255)));
        }
        return sb.toString();
    }

    public static String decodeCaCertificateAlias(String str) {
        byte[] bArr = new byte[(str.length() >> 1)];
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            int i3 = i + 2;
            bArr[i2] = (byte) Integer.parseInt(str.substring(i, i3), 16);
            i2++;
            i = i3;
        }
        try {
            return new String(bArr, StandardCharsets.UTF_8);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return str;
        }
    }

    public void setCaCertificateAlias(String str) {
        setFieldValue(CA_CERT_KEY, str, CA_CERT_PREFIX);
    }

    @SystemApi
    public void setCaCertificateAliases(String[] strArr) {
        if (strArr == null) {
            setFieldValue(CA_CERT_KEY, (String) null, CA_CERT_PREFIX);
            return;
        }
        if (strArr.length == 1) {
            setCaCertificateAlias(strArr[0]);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            if (i > 0) {
                sb.append(CA_CERT_ALIAS_DELIMITER);
            }
            sb.append(encodeCaCertificateAlias(CA_CERTIFICATE + strArr[i]));
        }
        setFieldValue(CA_CERT_KEY, sb.toString(), KEYSTORES_URI);
    }

    public boolean hasCaCertificate() {
        if (getCaCertificateAliases() == null && getCaCertificates() == null && TextUtils.isEmpty(getCaPath())) {
            return false;
        }
        return true;
    }

    public String getCaCertificateAlias() {
        return getFieldValue(CA_CERT_KEY, CA_CERT_PREFIX);
    }

    @SystemApi
    public String[] getCaCertificateAliases() {
        String fieldValue = getFieldValue(CA_CERT_KEY);
        if (fieldValue.startsWith(CA_CERT_PREFIX)) {
            return new String[]{getFieldValue(CA_CERT_KEY, CA_CERT_PREFIX)};
        } else if (fieldValue.startsWith(KEYSTORES_URI)) {
            String[] split = TextUtils.split(fieldValue.substring(12), CA_CERT_ALIAS_DELIMITER);
            for (int i = 0; i < split.length; i++) {
                String decodeCaCertificateAlias = decodeCaCertificateAlias(split[i]);
                split[i] = decodeCaCertificateAlias;
                if (decodeCaCertificateAlias.startsWith(CA_CERTIFICATE)) {
                    split[i] = split[i].substring(7);
                }
            }
            if (split.length != 0) {
                return split;
            }
            return null;
        } else if (TextUtils.isEmpty(fieldValue)) {
            return null;
        } else {
            return new String[]{fieldValue};
        }
    }

    public void setCaCertificate(X509Certificate x509Certificate) {
        if (x509Certificate == null) {
            this.mCaCerts = null;
        } else if (x509Certificate.getBasicConstraints() >= 0) {
            this.mIsAppInstalledCaCert = true;
            this.mCaCerts = new X509Certificate[]{x509Certificate};
        } else {
            this.mCaCerts = null;
            throw new IllegalArgumentException("Not a CA certificate");
        }
    }

    public void setCaCertificateForTrustOnFirstUse(X509Certificate x509Certificate) {
        if (x509Certificate == null) {
            this.mCaCerts = null;
        } else if (isTrustOnFirstUseEnabled()) {
            this.mIsAppInstalledCaCert = true;
            this.mCaCerts = new X509Certificate[]{x509Certificate};
        } else {
            this.mCaCerts = null;
            throw new IllegalArgumentException("Trust on First Use is not enabled.");
        }
    }

    public X509Certificate getCaCertificate() {
        X509Certificate[] x509CertificateArr = this.mCaCerts;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr[0];
    }

    public void setCaCertificates(X509Certificate[] x509CertificateArr) {
        if (x509CertificateArr != null) {
            X509Certificate[] x509CertificateArr2 = new X509Certificate[x509CertificateArr.length];
            int i = 0;
            while (i < x509CertificateArr.length) {
                if (x509CertificateArr[i].getBasicConstraints() >= 0) {
                    x509CertificateArr2[i] = x509CertificateArr[i];
                    i++;
                } else {
                    this.mCaCerts = null;
                    throw new IllegalArgumentException("Not a CA certificate");
                }
            }
            this.mCaCerts = x509CertificateArr2;
            this.mIsAppInstalledCaCert = true;
            return;
        }
        this.mCaCerts = null;
    }

    public X509Certificate[] getCaCertificates() {
        X509Certificate[] x509CertificateArr = this.mCaCerts;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr;
    }

    public void resetCaCertificate() {
        this.mCaCerts = null;
    }

    @SystemApi
    public void setCaPath(String str) {
        setFieldValue(CA_PATH_KEY, str);
    }

    @SystemApi
    public String getCaPath() {
        return getFieldValue(CA_PATH_KEY);
    }

    @SystemApi
    public void setClientCertificateAlias(String str) {
        setFieldValue(CLIENT_CERT_KEY, str, CLIENT_CERT_PREFIX);
        setFieldValue("key_id", str, USER_PRIVATE_KEY);
        if (TextUtils.isEmpty(str)) {
            setFieldValue(ENGINE_KEY, "0");
            setFieldValue(ENGINE_ID_KEY, "");
            return;
        }
        setFieldValue(ENGINE_KEY, "1");
        setFieldValue(ENGINE_ID_KEY, ENGINE_ID_KEYSTORE);
    }

    @SystemApi
    public String getClientCertificateAlias() {
        return getFieldValue(CLIENT_CERT_KEY, CLIENT_CERT_PREFIX);
    }

    public void setClientKeyEntry(PrivateKey privateKey, X509Certificate x509Certificate) {
        setClientKeyEntryWithCertificateChain(privateKey, x509Certificate != null ? new X509Certificate[]{x509Certificate} : null);
    }

    public void setClientKeyEntryWithCertificateChain(PrivateKey privateKey, X509Certificate[] x509CertificateArr) {
        X509Certificate[] x509CertificateArr2;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            x509CertificateArr2 = null;
        } else if (x509CertificateArr[0].getBasicConstraints() == -1) {
            int i = 1;
            while (i < x509CertificateArr.length) {
                if (x509CertificateArr[i].getBasicConstraints() != -1) {
                    i++;
                } else {
                    throw new IllegalArgumentException("All certificates following the first must be CA certificates");
                }
            }
            x509CertificateArr2 = (X509Certificate[]) Arrays.copyOf((T[]) x509CertificateArr, x509CertificateArr.length);
            if (privateKey == null) {
                throw new IllegalArgumentException("Client cert without a private key");
            } else if (privateKey.getEncoded() == null) {
                throw new IllegalArgumentException("Private key cannot be encoded");
            }
        } else {
            throw new IllegalArgumentException("First certificate in the chain must be a client end certificate");
        }
        this.mClientPrivateKey = privateKey;
        this.mClientCertificateChain = x509CertificateArr2;
        this.mIsAppInstalledDeviceKeyAndCert = true;
    }

    public void setClientKeyPairAlias(String str) {
        if (SdkLevel.isAtLeastS()) {
            this.mKeyChainAlias = str;
            return;
        }
        throw new UnsupportedOperationException();
    }

    public String getClientKeyPairAlias() {
        if (SdkLevel.isAtLeastS()) {
            return this.mKeyChainAlias;
        }
        throw new UnsupportedOperationException();
    }

    public String getClientKeyPairAliasInternal() {
        return this.mKeyChainAlias;
    }

    public X509Certificate getClientCertificate() {
        X509Certificate[] x509CertificateArr = this.mClientCertificateChain;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr[0];
    }

    public X509Certificate[] getClientCertificateChain() {
        X509Certificate[] x509CertificateArr = this.mClientCertificateChain;
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            return null;
        }
        return x509CertificateArr;
    }

    public void resetClientKeyEntry() {
        this.mClientPrivateKey = null;
        this.mClientCertificateChain = null;
    }

    public PrivateKey getClientPrivateKey() {
        return this.mClientPrivateKey;
    }

    public void setSubjectMatch(String str) {
        setFieldValue(SUBJECT_MATCH_KEY, str);
    }

    public String getSubjectMatch() {
        return getFieldValue(SUBJECT_MATCH_KEY);
    }

    public void setAltSubjectMatch(String str) {
        setFieldValue(ALTSUBJECT_MATCH_KEY, str);
    }

    public String getAltSubjectMatch() {
        return getFieldValue(ALTSUBJECT_MATCH_KEY);
    }

    public void setDomainSuffixMatch(String str) {
        setFieldValue(DOM_SUFFIX_MATCH_KEY, str);
    }

    public String getDomainSuffixMatch() {
        return getFieldValue(DOM_SUFFIX_MATCH_KEY);
    }

    public void setRealm(String str) {
        setFieldValue(REALM_KEY, str);
    }

    public String getRealm() {
        return getFieldValue(REALM_KEY);
    }

    public void setSelectedRcoi(long j) {
        this.mSelectedRcoi = j;
    }

    public long getSelectedRcoi() {
        return this.mSelectedRcoi;
    }

    public void setPlmn(String str) {
        setFieldValue(PLMN_KEY, str);
    }

    public String getPlmn() {
        return getFieldValue(PLMN_KEY);
    }

    public String getKeyId(WifiEnterpriseConfig wifiEnterpriseConfig) {
        if (this.mEapMethod == -1) {
            if (wifiEnterpriseConfig != null) {
                return wifiEnterpriseConfig.getKeyId((WifiEnterpriseConfig) null);
            }
            return EMPTY_VALUE;
        } else if (!isEapMethodValid()) {
            return EMPTY_VALUE;
        } else {
            return Eap.strings[this.mEapMethod] + BaseLocale.SEP + Phase2.strings[this.mPhase2Method];
        }
    }

    private String removeDoubleQuotes(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int length = str.length();
        if (length > 1 && str.charAt(0) == '\"') {
            int i = length - 1;
            if (str.charAt(i) == '\"') {
                return str.substring(1, i);
            }
        }
        return str;
    }

    private String convertToQuotedString(String str) {
        return "\"" + str + "\"";
    }

    private int getStringIndex(String[] strArr, String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (str.equals(strArr[i2])) {
                return i2;
            }
        }
        return i;
    }

    private String getFieldValue(String str, String str2) {
        String str3 = this.mFields.get(str);
        if (TextUtils.isEmpty(str3) || EMPTY_VALUE.equals(str3)) {
            return "";
        }
        String removeDoubleQuotes = removeDoubleQuotes(str3);
        return removeDoubleQuotes.startsWith(str2) ? removeDoubleQuotes.substring(str2.length()) : removeDoubleQuotes;
    }

    public String getFieldValue(String str) {
        return getFieldValue(str, "");
    }

    private void setFieldValue(String str, String str2, String str3) {
        String str4;
        if (TextUtils.isEmpty(str2)) {
            this.mFields.put(str, EMPTY_VALUE);
            return;
        }
        if (!UNQUOTED_KEYS.contains(str)) {
            str4 = convertToQuotedString(str3 + str2);
        } else {
            str4 = str3 + str2;
        }
        this.mFields.put(str, str4);
    }

    public void setFieldValue(String str, String str2) {
        setFieldValue(str, str2, "");
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (String next : this.mFields.keySet()) {
            stringBuffer.append(next).append(CA_CERT_ALIAS_DELIMITER).append(PASSWORD_KEY.equals(next) ? "<removed>" : this.mFields.get(next)).append("\n");
        }
        int i = this.mEapMethod;
        if (i >= 0 && i < Eap.strings.length) {
            stringBuffer.append("eap_method: ").append(Eap.strings[this.mEapMethod]).append("\n");
        }
        int i2 = this.mPhase2Method;
        if (i2 > 0 && i2 < Phase2.strings.length) {
            stringBuffer.append("phase2_method: ").append(Phase2.strings[this.mPhase2Method]).append("\n");
        }
        stringBuffer.append(" ocsp: ").append(this.mOcsp).append("\n trust_on_first_use: ");
        stringBuffer.append(this.mIsTrustOnFirstUseEnabled).append("\n user_approve_no_ca_cert: ");
        stringBuffer.append(this.mUserApproveNoCaCert).append("\n selected_rcoi: ");
        stringBuffer.append(this.mSelectedRcoi).append("\n");
        return stringBuffer.toString();
    }

    private boolean isEapMethodValid() {
        int i = this.mEapMethod;
        if (i == -1) {
            Log.e(TAG, "WiFi enterprise configuration is invalid as it supplies no EAP method.");
            return false;
        } else if (i < 0 || i >= Eap.strings.length) {
            Log.e(TAG, "mEapMethod is invald for WiFi enterprise configuration: " + this.mEapMethod);
            return false;
        } else {
            int i2 = this.mPhase2Method;
            if (i2 >= 0 && i2 < Phase2.strings.length) {
                return true;
            }
            Log.e(TAG, "mPhase2Method is invald for WiFi enterprise configuration: " + this.mPhase2Method);
            return false;
        }
    }

    public boolean isAppInstalledDeviceKeyAndCert() {
        return this.mIsAppInstalledDeviceKeyAndCert;
    }

    public void initIsAppInstalledDeviceKeyAndCert(boolean z) {
        this.mIsAppInstalledDeviceKeyAndCert = z;
    }

    public boolean isAppInstalledCaCert() {
        return this.mIsAppInstalledCaCert;
    }

    public void initIsAppInstalledCaCert(boolean z) {
        this.mIsAppInstalledCaCert = z;
    }

    @SystemApi
    public void setOcsp(int i) {
        if (i < 0 || i > 3) {
            throw new IllegalArgumentException("Invalid OCSP type.");
        }
        this.mOcsp = i;
    }

    @SystemApi
    public int getOcsp() {
        return this.mOcsp;
    }

    public boolean isAuthenticationSimBased() {
        int i = this.mEapMethod;
        if (i == 4 || i == 5 || i == 6) {
            return true;
        }
        if (i != 0) {
            return false;
        }
        int i2 = this.mPhase2Method;
        if (i2 == 5 || i2 == 6 || i2 == 7) {
            return true;
        }
        return false;
    }

    @SystemApi
    public void setWapiCertSuite(String str) {
        setFieldValue(WAPI_CERT_SUITE_KEY, str);
    }

    @SystemApi
    public String getWapiCertSuite() {
        return getFieldValue(WAPI_CERT_SUITE_KEY);
    }

    public boolean isEapMethodServerCertUsed() {
        int i = this.mEapMethod;
        return i == 0 || i == 1 || i == 2 || i == 7;
    }

    public boolean isServerCertValidationEnabled() {
        if (isEapMethodServerCertUsed()) {
            return isMandatoryParameterSetForServerCertValidation();
        }
        throw new IllegalStateException("Configuration doesn't use server certificates for authentication");
    }

    public boolean isMandatoryParameterSetForServerCertValidation() {
        if (TextUtils.isEmpty(getAltSubjectMatch()) && TextUtils.isEmpty(getDomainSuffixMatch())) {
            return false;
        }
        if (!this.mIsAppInstalledCaCert && getCaCertificateAliases() == null) {
            return !TextUtils.isEmpty(getCaPath());
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0061 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isSuiteBCipherCert(java.security.cert.X509Certificate r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.String r1 = r4.getSigAlgOID()
            java.lang.String r2 = "1.2.840.113549.1.1.12"
            boolean r2 = r1.equals(r2)
            r3 = 1
            if (r2 == 0) goto L_0x0032
            java.security.PublicKey r1 = r4.getPublicKey()
            boolean r1 = r1 instanceof java.security.interfaces.RSAPublicKey
            if (r1 == 0) goto L_0x0061
            java.security.PublicKey r4 = r4.getPublicKey()
            java.security.interfaces.RSAPublicKey r4 = (java.security.interfaces.RSAPublicKey) r4
            java.math.BigInteger r1 = r4.getModulus()
            if (r1 == 0) goto L_0x0061
            java.math.BigInteger r4 = r4.getModulus()
            int r4 = r4.bitLength()
            r1 = 3072(0xc00, float:4.305E-42)
            if (r4 < r1) goto L_0x0061
            return r3
        L_0x0032:
            java.lang.String r2 = "1.2.840.10045.4.3.3"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x0061
            java.security.PublicKey r1 = r4.getPublicKey()
            boolean r1 = r1 instanceof java.security.interfaces.ECPublicKey
            if (r1 == 0) goto L_0x0061
            java.security.PublicKey r4 = r4.getPublicKey()
            java.security.interfaces.ECPublicKey r4 = (java.security.interfaces.ECPublicKey) r4
            java.security.spec.ECParameterSpec r4 = r4.getParams()
            if (r4 == 0) goto L_0x0061
            java.math.BigInteger r1 = r4.getOrder()
            if (r1 == 0) goto L_0x0061
            java.math.BigInteger r4 = r4.getOrder()
            int r4 = r4.bitLength()
            r1 = 384(0x180, float:5.38E-43)
            if (r4 < r1) goto L_0x0061
            return r3
        L_0x0061:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiEnterpriseConfig.isSuiteBCipherCert(java.security.cert.X509Certificate):boolean");
    }

    public void setDecoratedIdentityPrefix(String str) {
        if (!SdkLevel.isAtLeastS()) {
            throw new UnsupportedOperationException();
        } else if (TextUtils.isEmpty(str) || str.endsWith("!")) {
            setFieldValue(DECORATED_IDENTITY_PREFIX_KEY, str);
        } else {
            throw new IllegalArgumentException("Decorated identity prefix must be delimited by '!'");
        }
    }

    public String getDecoratedIdentityPrefix() {
        if (SdkLevel.isAtLeastS()) {
            String fieldValue = getFieldValue(DECORATED_IDENTITY_PREFIX_KEY);
            if (fieldValue.isEmpty()) {
                return null;
            }
            return fieldValue;
        }
        throw new UnsupportedOperationException();
    }

    public void enableTrustOnFirstUse(boolean z) {
        this.mIsTrustOnFirstUseEnabled = z;
    }

    public boolean isTrustOnFirstUseEnabled() {
        return this.mIsTrustOnFirstUseEnabled;
    }

    public void setUserApproveNoCaCert(boolean z) {
        this.mUserApproveNoCaCert = z;
    }

    public boolean isUserApproveNoCaCert() {
        return this.mUserApproveNoCaCert;
    }
}
