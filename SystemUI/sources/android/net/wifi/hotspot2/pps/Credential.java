package android.net.wifi.hotspot2.pps;

import android.net.wifi.ParcelUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Credential implements Parcelable {
    public static final Parcelable.Creator<Credential> CREATOR = new Parcelable.Creator<Credential>() {
        public Credential createFromParcel(Parcel parcel) {
            Credential credential = new Credential();
            credential.setCreationTimeInMillis(parcel.readLong());
            credential.setExpirationTimeInMillis(parcel.readLong());
            credential.setRealm(parcel.readString());
            credential.setCheckAaaServerCertStatus(parcel.readInt() != 0);
            credential.setUserCredential((UserCredential) parcel.readParcelable((ClassLoader) null));
            credential.setCertCredential((CertificateCredential) parcel.readParcelable((ClassLoader) null));
            credential.setSimCredential((SimCredential) parcel.readParcelable((ClassLoader) null));
            credential.setCaCertificates(ParcelUtil.readCertificates(parcel));
            credential.setClientCertificateChain(ParcelUtil.readCertificates(parcel));
            credential.setClientPrivateKey(ParcelUtil.readPrivateKey(parcel));
            return credential;
        }

        public Credential[] newArray(int i) {
            return new Credential[i];
        }
    };
    private static final int MAX_REALM_BYTES = 253;
    private static final String TAG = "Credential";
    private X509Certificate[] mCaCertificates = null;
    private CertificateCredential mCertCredential = null;
    private boolean mCheckAaaServerCertStatus = false;
    private X509Certificate[] mClientCertificateChain = null;
    private PrivateKey mClientPrivateKey = null;
    private long mCreationTimeInMillis = Long.MIN_VALUE;
    private long mExpirationTimeInMillis = Long.MIN_VALUE;
    private String mRealm = null;
    private SimCredential mSimCredential = null;
    private UserCredential mUserCredential = null;

    public int describeContents() {
        return 0;
    }

    public void setCreationTimeInMillis(long j) {
        this.mCreationTimeInMillis = j;
    }

    public long getCreationTimeInMillis() {
        return this.mCreationTimeInMillis;
    }

    public void setExpirationTimeInMillis(long j) {
        this.mExpirationTimeInMillis = j;
    }

    public long getExpirationTimeInMillis() {
        return this.mExpirationTimeInMillis;
    }

    public void setRealm(String str) {
        this.mRealm = str;
    }

    public String getRealm() {
        return this.mRealm;
    }

    public void setCheckAaaServerCertStatus(boolean z) {
        this.mCheckAaaServerCertStatus = z;
    }

    public boolean getCheckAaaServerCertStatus() {
        return this.mCheckAaaServerCertStatus;
    }

    public static final class UserCredential implements Parcelable {
        public static final String AUTH_METHOD_MSCHAP = "MS-CHAP";
        public static final String AUTH_METHOD_MSCHAPV2 = "MS-CHAP-V2";
        public static final String AUTH_METHOD_PAP = "PAP";
        public static final Parcelable.Creator<UserCredential> CREATOR = new Parcelable.Creator<UserCredential>() {
            public UserCredential createFromParcel(Parcel parcel) {
                UserCredential userCredential = new UserCredential();
                userCredential.setUsername(parcel.readString());
                userCredential.setPassword(parcel.readString());
                boolean z = true;
                userCredential.setMachineManaged(parcel.readInt() != 0);
                userCredential.setSoftTokenApp(parcel.readString());
                if (parcel.readInt() == 0) {
                    z = false;
                }
                userCredential.setAbleToShare(z);
                userCredential.setEapType(parcel.readInt());
                userCredential.setNonEapInnerMethod(parcel.readString());
                return userCredential;
            }

            public UserCredential[] newArray(int i) {
                return new UserCredential[i];
            }
        };
        private static final int MAX_PASSWORD_BYTES = 255;
        private static final int MAX_USERNAME_BYTES = 63;
        private static final Set<String> SUPPORTED_AUTH = new HashSet(Arrays.asList(AUTH_METHOD_PAP, AUTH_METHOD_MSCHAP, AUTH_METHOD_MSCHAPV2));
        private boolean mAbleToShare = false;
        private int mEapType = Integer.MIN_VALUE;
        private boolean mMachineManaged = false;
        private String mNonEapInnerMethod = null;
        private String mPassword = null;
        private String mSoftTokenApp = null;
        private String mUsername = null;

        public int describeContents() {
            return 0;
        }

        public void setUsername(String str) {
            this.mUsername = str;
        }

        public String getUsername() {
            return this.mUsername;
        }

        public void setPassword(String str) {
            this.mPassword = str;
        }

        public String getPassword() {
            return this.mPassword;
        }

        public void setMachineManaged(boolean z) {
            this.mMachineManaged = z;
        }

        public boolean getMachineManaged() {
            return this.mMachineManaged;
        }

        public void setSoftTokenApp(String str) {
            this.mSoftTokenApp = str;
        }

        public String getSoftTokenApp() {
            return this.mSoftTokenApp;
        }

        public void setAbleToShare(boolean z) {
            this.mAbleToShare = z;
        }

        public boolean getAbleToShare() {
            return this.mAbleToShare;
        }

        public void setEapType(int i) {
            this.mEapType = i;
        }

        public int getEapType() {
            return this.mEapType;
        }

        public void setNonEapInnerMethod(String str) {
            this.mNonEapInnerMethod = str;
        }

        public String getNonEapInnerMethod() {
            return this.mNonEapInnerMethod;
        }

        public UserCredential() {
        }

        public UserCredential(UserCredential userCredential) {
            if (userCredential != null) {
                this.mUsername = userCredential.mUsername;
                this.mPassword = userCredential.mPassword;
                this.mMachineManaged = userCredential.mMachineManaged;
                this.mSoftTokenApp = userCredential.mSoftTokenApp;
                this.mAbleToShare = userCredential.mAbleToShare;
                this.mEapType = userCredential.mEapType;
                this.mNonEapInnerMethod = userCredential.mNonEapInnerMethod;
            }
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mUsername);
            parcel.writeString(this.mPassword);
            parcel.writeInt(this.mMachineManaged ? 1 : 0);
            parcel.writeString(this.mSoftTokenApp);
            parcel.writeInt(this.mAbleToShare ? 1 : 0);
            parcel.writeInt(this.mEapType);
            parcel.writeString(this.mNonEapInnerMethod);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof UserCredential)) {
                return false;
            }
            UserCredential userCredential = (UserCredential) obj;
            if (!TextUtils.equals(this.mUsername, userCredential.mUsername) || !TextUtils.equals(this.mPassword, userCredential.mPassword) || this.mMachineManaged != userCredential.mMachineManaged || !TextUtils.equals(this.mSoftTokenApp, userCredential.mSoftTokenApp) || this.mAbleToShare != userCredential.mAbleToShare || this.mEapType != userCredential.mEapType || !TextUtils.equals(this.mNonEapInnerMethod, userCredential.mNonEapInnerMethod)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mUsername, this.mPassword, Boolean.valueOf(this.mMachineManaged), this.mSoftTokenApp, Boolean.valueOf(this.mAbleToShare), Integer.valueOf(this.mEapType), this.mNonEapInnerMethod);
        }

        public String toString() {
            return "Username: " + this.mUsername + "\nMachineManaged: " + this.mMachineManaged + "\nSoftTokenApp: " + this.mSoftTokenApp + "\nAbleToShare: " + this.mAbleToShare + "\nEAPType: " + this.mEapType + "\nAuthMethod: " + this.mNonEapInnerMethod + "\n";
        }

        public boolean validate() {
            if (TextUtils.isEmpty(this.mUsername)) {
                Log.d(Credential.TAG, "Missing username");
                return false;
            } else if (this.mUsername.getBytes(StandardCharsets.UTF_8).length > 63) {
                Log.d(Credential.TAG, "username exceeding maximum length: " + this.mUsername.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (TextUtils.isEmpty(this.mPassword)) {
                Log.d(Credential.TAG, "Missing password");
                return false;
            } else if (this.mPassword.getBytes(StandardCharsets.UTF_8).length > 255) {
                Log.d(Credential.TAG, "password exceeding maximum length: " + this.mPassword.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (this.mEapType != 21) {
                Log.d(Credential.TAG, "Invalid EAP Type for user credential: " + this.mEapType);
                return false;
            } else if (SUPPORTED_AUTH.contains(this.mNonEapInnerMethod)) {
                return true;
            } else {
                Log.d(Credential.TAG, "Invalid non-EAP inner method for EAP-TTLS: " + this.mNonEapInnerMethod);
                return false;
            }
        }

        public int getUniqueId() {
            return Objects.hash(this.mUsername);
        }
    }

    public void setUserCredential(UserCredential userCredential) {
        this.mUserCredential = userCredential;
    }

    public UserCredential getUserCredential() {
        return this.mUserCredential;
    }

    public static final class CertificateCredential implements Parcelable {
        private static final int CERT_SHA256_FINGER_PRINT_LENGTH = 32;
        public static final String CERT_TYPE_X509V3 = "x509v3";
        public static final Parcelable.Creator<CertificateCredential> CREATOR = new Parcelable.Creator<CertificateCredential>() {
            public CertificateCredential createFromParcel(Parcel parcel) {
                CertificateCredential certificateCredential = new CertificateCredential();
                certificateCredential.setCertType(parcel.readString());
                certificateCredential.setCertSha256Fingerprint(parcel.createByteArray());
                return certificateCredential;
            }

            public CertificateCredential[] newArray(int i) {
                return new CertificateCredential[i];
            }
        };
        private byte[] mCertSha256Fingerprint = null;
        private String mCertType = null;

        public int describeContents() {
            return 0;
        }

        public void setCertType(String str) {
            this.mCertType = str;
        }

        public String getCertType() {
            return this.mCertType;
        }

        public void setCertSha256Fingerprint(byte[] bArr) {
            this.mCertSha256Fingerprint = bArr;
        }

        public byte[] getCertSha256Fingerprint() {
            return this.mCertSha256Fingerprint;
        }

        public CertificateCredential() {
        }

        public CertificateCredential(CertificateCredential certificateCredential) {
            if (certificateCredential != null) {
                this.mCertType = certificateCredential.mCertType;
                byte[] bArr = certificateCredential.mCertSha256Fingerprint;
                if (bArr != null) {
                    this.mCertSha256Fingerprint = Arrays.copyOf(bArr, bArr.length);
                }
            }
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mCertType);
            parcel.writeByteArray(this.mCertSha256Fingerprint);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CertificateCredential)) {
                return false;
            }
            CertificateCredential certificateCredential = (CertificateCredential) obj;
            if (!TextUtils.equals(this.mCertType, certificateCredential.mCertType) || !Arrays.equals(this.mCertSha256Fingerprint, certificateCredential.mCertSha256Fingerprint)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mCertType, Integer.valueOf(Arrays.hashCode(this.mCertSha256Fingerprint)));
        }

        public String toString() {
            return "CertificateType: " + this.mCertType + "\n";
        }

        public boolean validate() {
            if (!TextUtils.equals(CERT_TYPE_X509V3, this.mCertType)) {
                Log.d(Credential.TAG, "Unsupported certificate type: " + this.mCertType);
                return false;
            }
            byte[] bArr = this.mCertSha256Fingerprint;
            if (bArr != null && bArr.length == 32) {
                return true;
            }
            Log.d(Credential.TAG, "Invalid SHA-256 fingerprint");
            return false;
        }
    }

    public void setCertCredential(CertificateCredential certificateCredential) {
        this.mCertCredential = certificateCredential;
    }

    public CertificateCredential getCertCredential() {
        return this.mCertCredential;
    }

    public static final class SimCredential implements Parcelable {
        public static final Parcelable.Creator<SimCredential> CREATOR = new Parcelable.Creator<SimCredential>() {
            public SimCredential createFromParcel(Parcel parcel) {
                SimCredential simCredential = new SimCredential();
                simCredential.setImsi(parcel.readString());
                simCredential.setEapType(parcel.readInt());
                return simCredential;
            }

            public SimCredential[] newArray(int i) {
                return new SimCredential[i];
            }
        };
        private static final int MAX_IMSI_LENGTH = 15;
        private int mEapType = Integer.MIN_VALUE;
        private String mImsi = null;

        public int describeContents() {
            return 0;
        }

        public void setImsi(String str) {
            this.mImsi = str;
        }

        public String getImsi() {
            return this.mImsi;
        }

        public void setEapType(int i) {
            this.mEapType = i;
        }

        public int getEapType() {
            return this.mEapType;
        }

        public SimCredential() {
        }

        public SimCredential(SimCredential simCredential) {
            if (simCredential != null) {
                this.mImsi = simCredential.mImsi;
                this.mEapType = simCredential.mEapType;
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SimCredential)) {
                return false;
            }
            SimCredential simCredential = (SimCredential) obj;
            if (!TextUtils.equals(this.mImsi, simCredential.mImsi) || this.mEapType != simCredential.mEapType) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.mImsi, Integer.valueOf(this.mEapType));
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder();
            String str2 = this.mImsi;
            if (str2 != null) {
                if (str2.length() <= 6 || this.mImsi.charAt(6) == '*') {
                    str = this.mImsi;
                } else {
                    str = this.mImsi.substring(0, 6) + "****";
                }
                sb.append("IMSI: ");
                sb.append(str);
                sb.append("\n");
            }
            sb.append("EAPType: ");
            sb.append(this.mEapType);
            sb.append("\n");
            return sb.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mImsi);
            parcel.writeInt(this.mEapType);
        }

        public boolean validate() {
            if (!verifyImsi()) {
                return false;
            }
            int i = this.mEapType;
            if (i == 18 || i == 23 || i == 50) {
                return true;
            }
            Log.d(Credential.TAG, "Invalid EAP Type for SIM credential: " + this.mEapType);
            return false;
        }

        private boolean verifyImsi() {
            if (TextUtils.isEmpty(this.mImsi)) {
                Log.d(Credential.TAG, "Missing IMSI");
                return false;
            } else if (this.mImsi.length() > 15) {
                Log.d(Credential.TAG, "IMSI exceeding maximum length: " + this.mImsi.length());
                return false;
            } else {
                int i = 0;
                char c = 0;
                while (i < this.mImsi.length() && (c = this.mImsi.charAt(i)) >= '0' && c <= '9') {
                    i++;
                }
                if (i == this.mImsi.length()) {
                    return true;
                }
                if (i == this.mImsi.length() - 1 && c == '*') {
                    return true;
                }
                return false;
            }
        }
    }

    public void setSimCredential(SimCredential simCredential) {
        this.mSimCredential = simCredential;
    }

    public SimCredential getSimCredential() {
        return this.mSimCredential;
    }

    public void setCaCertificate(X509Certificate x509Certificate) {
        this.mCaCertificates = null;
        if (x509Certificate != null) {
            this.mCaCertificates = new X509Certificate[]{x509Certificate};
        }
    }

    public void setCaCertificates(X509Certificate[] x509CertificateArr) {
        this.mCaCertificates = x509CertificateArr;
    }

    public X509Certificate getCaCertificate() {
        X509Certificate[] x509CertificateArr = this.mCaCertificates;
        if (x509CertificateArr == null || x509CertificateArr.length > 1) {
            return null;
        }
        return x509CertificateArr[0];
    }

    public X509Certificate[] getCaCertificates() {
        return this.mCaCertificates;
    }

    public void setClientCertificateChain(X509Certificate[] x509CertificateArr) {
        this.mClientCertificateChain = x509CertificateArr;
    }

    public X509Certificate[] getClientCertificateChain() {
        return this.mClientCertificateChain;
    }

    public void setClientPrivateKey(PrivateKey privateKey) {
        this.mClientPrivateKey = privateKey;
    }

    public PrivateKey getClientPrivateKey() {
        return this.mClientPrivateKey;
    }

    public Credential() {
    }

    public Credential(Credential credential) {
        if (credential != null) {
            this.mCreationTimeInMillis = credential.mCreationTimeInMillis;
            this.mExpirationTimeInMillis = credential.mExpirationTimeInMillis;
            this.mRealm = credential.mRealm;
            this.mCheckAaaServerCertStatus = credential.mCheckAaaServerCertStatus;
            if (credential.mUserCredential != null) {
                this.mUserCredential = new UserCredential(credential.mUserCredential);
            }
            if (credential.mCertCredential != null) {
                this.mCertCredential = new CertificateCredential(credential.mCertCredential);
            }
            if (credential.mSimCredential != null) {
                this.mSimCredential = new SimCredential(credential.mSimCredential);
            }
            X509Certificate[] x509CertificateArr = credential.mClientCertificateChain;
            if (x509CertificateArr != null) {
                this.mClientCertificateChain = (X509Certificate[]) Arrays.copyOf((T[]) x509CertificateArr, x509CertificateArr.length);
            }
            X509Certificate[] x509CertificateArr2 = credential.mCaCertificates;
            if (x509CertificateArr2 != null) {
                this.mCaCertificates = (X509Certificate[]) Arrays.copyOf((T[]) x509CertificateArr2, x509CertificateArr2.length);
            }
            this.mClientPrivateKey = credential.mClientPrivateKey;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mCreationTimeInMillis);
        parcel.writeLong(this.mExpirationTimeInMillis);
        parcel.writeString(this.mRealm);
        parcel.writeInt(this.mCheckAaaServerCertStatus ? 1 : 0);
        parcel.writeParcelable(this.mUserCredential, i);
        parcel.writeParcelable(this.mCertCredential, i);
        parcel.writeParcelable(this.mSimCredential, i);
        ParcelUtil.writeCertificates(parcel, this.mCaCertificates);
        ParcelUtil.writeCertificates(parcel, this.mClientCertificateChain);
        ParcelUtil.writePrivateKey(parcel, this.mClientPrivateKey);
    }

    public boolean equals(Object obj) {
        UserCredential userCredential;
        CertificateCredential certificateCredential;
        SimCredential simCredential;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Credential)) {
            return false;
        }
        Credential credential = (Credential) obj;
        if (!TextUtils.equals(this.mRealm, credential.mRealm) || this.mCreationTimeInMillis != credential.mCreationTimeInMillis || this.mExpirationTimeInMillis != credential.mExpirationTimeInMillis || this.mCheckAaaServerCertStatus != credential.mCheckAaaServerCertStatus || ((userCredential = this.mUserCredential) != null ? !userCredential.equals(credential.mUserCredential) : credential.mUserCredential != null) || ((certificateCredential = this.mCertCredential) != null ? !certificateCredential.equals(credential.mCertCredential) : credential.mCertCredential != null) || ((simCredential = this.mSimCredential) != null ? !simCredential.equals(credential.mSimCredential) : credential.mSimCredential != null) || !isX509CertificatesEquals(this.mCaCertificates, credential.mCaCertificates) || !isX509CertificatesEquals(this.mClientCertificateChain, credential.mClientCertificateChain) || !isPrivateKeyEquals(this.mClientPrivateKey, credential.mClientPrivateKey)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mCreationTimeInMillis), Long.valueOf(this.mExpirationTimeInMillis), this.mRealm, Boolean.valueOf(this.mCheckAaaServerCertStatus), this.mUserCredential, this.mCertCredential, this.mSimCredential, this.mClientPrivateKey, Integer.valueOf(Arrays.hashCode((Object[]) this.mCaCertificates)), Integer.valueOf(Arrays.hashCode((Object[]) this.mClientCertificateChain)));
    }

    public int getUniqueId() {
        Object[] objArr = new Object[4];
        UserCredential userCredential = this.mUserCredential;
        objArr[0] = Integer.valueOf(userCredential != null ? userCredential.getUniqueId() : 0);
        objArr[1] = this.mCertCredential;
        objArr[2] = this.mSimCredential;
        objArr[3] = this.mRealm;
        return Objects.hash(objArr);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Realm: ");
        sb.append(this.mRealm);
        sb.append("\nCreationTime: ");
        Object obj = "Not specified";
        sb.append(this.mCreationTimeInMillis != Long.MIN_VALUE ? new Date(this.mCreationTimeInMillis) : obj);
        sb.append("\nExpirationTime: ");
        if (this.mExpirationTimeInMillis != Long.MIN_VALUE) {
            obj = new Date(this.mExpirationTimeInMillis);
        }
        sb.append(obj);
        sb.append("\nCheckAAAServerStatus: ");
        sb.append(this.mCheckAaaServerCertStatus);
        sb.append("\n");
        if (this.mUserCredential != null) {
            sb.append("UserCredential Begin ---\n");
            sb.append((Object) this.mUserCredential);
            sb.append("UserCredential End ---\n");
        }
        if (this.mCertCredential != null) {
            sb.append("CertificateCredential Begin ---\n");
            sb.append((Object) this.mCertCredential);
            sb.append("CertificateCredential End ---\n");
        }
        if (this.mSimCredential != null) {
            sb.append("SIMCredential Begin ---\n");
            sb.append((Object) this.mSimCredential);
            sb.append("SIMCredential End ---\n");
        }
        return sb.toString();
    }

    public boolean validate() {
        if (TextUtils.isEmpty(this.mRealm)) {
            Log.d(TAG, "Missing realm");
            return false;
        } else if (this.mRealm.getBytes(StandardCharsets.UTF_8).length > 253) {
            Log.d(TAG, "realm exceeding maximum length: " + this.mRealm.getBytes(StandardCharsets.UTF_8).length);
            return false;
        } else if (this.mUserCredential != null) {
            if (!verifyUserCredential()) {
                return false;
            }
            return true;
        } else if (this.mCertCredential != null) {
            if (!verifyCertCredential()) {
                return false;
            }
            return true;
        } else if (this.mSimCredential == null) {
            Log.d(TAG, "Missing required credential");
            return false;
        } else if (!verifySimCredential()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyUserCredential() {
        UserCredential userCredential = this.mUserCredential;
        if (userCredential == null) {
            Log.d(TAG, "Missing user credential");
            return false;
        } else if (this.mCertCredential != null || this.mSimCredential != null) {
            Log.d(TAG, "Contained more than one type of credential");
            return false;
        } else if (!userCredential.validate()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean verifyCertCredential() {
        CertificateCredential certificateCredential = this.mCertCredential;
        if (certificateCredential == null) {
            Log.d(TAG, "Missing certificate credential");
            return false;
        } else if (this.mUserCredential != null || this.mSimCredential != null) {
            Log.d(TAG, "Contained more than one type of credential");
            return false;
        } else if (!certificateCredential.validate()) {
            return false;
        } else {
            if (this.mClientPrivateKey == null) {
                Log.d(TAG, "Missing client private key for certificate credential");
                return false;
            }
            try {
                if (verifySha256Fingerprint(this.mClientCertificateChain, this.mCertCredential.getCertSha256Fingerprint())) {
                    return true;
                }
                Log.d(TAG, "SHA-256 fingerprint mismatch");
                return false;
            } catch (NoSuchAlgorithmException | CertificateEncodingException e) {
                Log.d(TAG, "Failed to verify SHA-256 fingerprint: " + e.getMessage());
                return false;
            }
        }
    }

    private boolean verifySimCredential() {
        SimCredential simCredential = this.mSimCredential;
        if (simCredential == null) {
            Log.d(TAG, "Missing SIM credential");
            return false;
        } else if (this.mUserCredential == null && this.mCertCredential == null) {
            return simCredential.validate();
        } else {
            Log.d(TAG, "Contained more than one type of credential");
            return false;
        }
    }

    private static boolean isPrivateKeyEquals(PrivateKey privateKey, PrivateKey privateKey2) {
        if (privateKey == null && privateKey2 == null) {
            return true;
        }
        if (privateKey == null || privateKey2 == null) {
            return false;
        }
        if (!TextUtils.equals(privateKey.getAlgorithm(), privateKey2.getAlgorithm()) || !Arrays.equals(privateKey.getEncoded(), privateKey2.getEncoded())) {
            return false;
        }
        return true;
    }

    public static boolean isX509CertificateEquals(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
        if (x509Certificate == null && x509Certificate2 == null) {
            return true;
        }
        if (x509Certificate == null || x509Certificate2 == null) {
            return false;
        }
        try {
            return Arrays.equals(x509Certificate.getEncoded(), x509Certificate2.getEncoded());
        } catch (CertificateEncodingException unused) {
            return false;
        }
    }

    private static boolean isX509CertificatesEquals(X509Certificate[] x509CertificateArr, X509Certificate[] x509CertificateArr2) {
        if (x509CertificateArr == null && x509CertificateArr2 == null) {
            return true;
        }
        if (x509CertificateArr == null || x509CertificateArr2 == null || x509CertificateArr.length != x509CertificateArr2.length) {
            return false;
        }
        for (int i = 0; i < x509CertificateArr.length; i++) {
            if (!isX509CertificateEquals(x509CertificateArr[i], x509CertificateArr2[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean verifySha256Fingerprint(X509Certificate[] x509CertificateArr, byte[] bArr) throws NoSuchAlgorithmException, CertificateEncodingException {
        if (x509CertificateArr == null) {
            return false;
        }
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        for (X509Certificate x509Certificate : x509CertificateArr) {
            instance.reset();
            if (Arrays.equals(bArr, instance.digest(x509Certificate.getEncoded()))) {
                return true;
            }
        }
        return false;
    }
}
