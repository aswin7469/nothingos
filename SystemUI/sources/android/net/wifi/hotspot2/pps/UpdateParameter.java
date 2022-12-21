package android.net.wifi.hotspot2.pps;

import android.net.wifi.ParcelUtil;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Objects;

public final class UpdateParameter implements Parcelable {
    private static final int CERTIFICATE_SHA256_BYTES = 32;
    public static final Parcelable.Creator<UpdateParameter> CREATOR = new Parcelable.Creator<UpdateParameter>() {
        public UpdateParameter createFromParcel(Parcel parcel) {
            UpdateParameter updateParameter = new UpdateParameter();
            updateParameter.setUpdateIntervalInMinutes(parcel.readLong());
            updateParameter.setUpdateMethod(parcel.readString());
            updateParameter.setRestriction(parcel.readString());
            updateParameter.setServerUri(parcel.readString());
            updateParameter.setUsername(parcel.readString());
            updateParameter.setBase64EncodedPassword(parcel.readString());
            updateParameter.setTrustRootCertUrl(parcel.readString());
            updateParameter.setTrustRootCertSha256Fingerprint(parcel.createByteArray());
            updateParameter.setCaCertificate(ParcelUtil.readCertificate(parcel));
            return updateParameter;
        }

        public UpdateParameter[] newArray(int i) {
            return new UpdateParameter[i];
        }
    };
    private static final int MAX_PASSWORD_BYTES = 255;
    private static final int MAX_URI_BYTES = 1023;
    private static final int MAX_URL_BYTES = 1023;
    private static final int MAX_USERNAME_BYTES = 63;
    private static final String TAG = "UpdateParameter";
    public static final long UPDATE_CHECK_INTERVAL_NEVER = 4294967295L;
    public static final String UPDATE_METHOD_OMADM = "OMA-DM-ClientInitiated";
    public static final String UPDATE_METHOD_SPP = "SPP-ClientInitiated";
    public static final String UPDATE_RESTRICTION_HOMESP = "HomeSP";
    public static final String UPDATE_RESTRICTION_ROAMING_PARTNER = "RoamingPartner";
    public static final String UPDATE_RESTRICTION_UNRESTRICTED = "Unrestricted";
    private String mBase64EncodedPassword = null;
    private X509Certificate mCaCertificate;
    private String mRestriction = null;
    private String mServerUri = null;
    private byte[] mTrustRootCertSha256Fingerprint = null;
    private String mTrustRootCertUrl = null;
    private long mUpdateIntervalInMinutes = Long.MIN_VALUE;
    private String mUpdateMethod = null;
    private String mUsername = null;

    public int describeContents() {
        return 0;
    }

    public void setUpdateIntervalInMinutes(long j) {
        this.mUpdateIntervalInMinutes = j;
    }

    public long getUpdateIntervalInMinutes() {
        return this.mUpdateIntervalInMinutes;
    }

    public void setUpdateMethod(String str) {
        this.mUpdateMethod = str;
    }

    public String getUpdateMethod() {
        return this.mUpdateMethod;
    }

    public void setRestriction(String str) {
        this.mRestriction = str;
    }

    public String getRestriction() {
        return this.mRestriction;
    }

    public void setServerUri(String str) {
        this.mServerUri = str;
    }

    public String getServerUri() {
        return this.mServerUri;
    }

    public void setUsername(String str) {
        this.mUsername = str;
    }

    public String getUsername() {
        return this.mUsername;
    }

    public void setBase64EncodedPassword(String str) {
        this.mBase64EncodedPassword = str;
    }

    public String getBase64EncodedPassword() {
        return this.mBase64EncodedPassword;
    }

    public void setTrustRootCertUrl(String str) {
        this.mTrustRootCertUrl = str;
    }

    public String getTrustRootCertUrl() {
        return this.mTrustRootCertUrl;
    }

    public void setTrustRootCertSha256Fingerprint(byte[] bArr) {
        this.mTrustRootCertSha256Fingerprint = bArr;
    }

    public byte[] getTrustRootCertSha256Fingerprint() {
        return this.mTrustRootCertSha256Fingerprint;
    }

    public void setCaCertificate(X509Certificate x509Certificate) {
        this.mCaCertificate = x509Certificate;
    }

    public X509Certificate getCaCertificate() {
        return this.mCaCertificate;
    }

    public UpdateParameter() {
    }

    public UpdateParameter(UpdateParameter updateParameter) {
        if (updateParameter != null) {
            this.mUpdateIntervalInMinutes = updateParameter.mUpdateIntervalInMinutes;
            this.mUpdateMethod = updateParameter.mUpdateMethod;
            this.mRestriction = updateParameter.mRestriction;
            this.mServerUri = updateParameter.mServerUri;
            this.mUsername = updateParameter.mUsername;
            this.mBase64EncodedPassword = updateParameter.mBase64EncodedPassword;
            this.mTrustRootCertUrl = updateParameter.mTrustRootCertUrl;
            byte[] bArr = updateParameter.mTrustRootCertSha256Fingerprint;
            if (bArr != null) {
                this.mTrustRootCertSha256Fingerprint = Arrays.copyOf(bArr, bArr.length);
            }
            this.mCaCertificate = updateParameter.mCaCertificate;
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mUpdateIntervalInMinutes);
        parcel.writeString(this.mUpdateMethod);
        parcel.writeString(this.mRestriction);
        parcel.writeString(this.mServerUri);
        parcel.writeString(this.mUsername);
        parcel.writeString(this.mBase64EncodedPassword);
        parcel.writeString(this.mTrustRootCertUrl);
        parcel.writeByteArray(this.mTrustRootCertSha256Fingerprint);
        ParcelUtil.writeCertificate(parcel, this.mCaCertificate);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UpdateParameter)) {
            return false;
        }
        UpdateParameter updateParameter = (UpdateParameter) obj;
        if (this.mUpdateIntervalInMinutes != updateParameter.mUpdateIntervalInMinutes || !TextUtils.equals(this.mUpdateMethod, updateParameter.mUpdateMethod) || !TextUtils.equals(this.mRestriction, updateParameter.mRestriction) || !TextUtils.equals(this.mServerUri, updateParameter.mServerUri) || !TextUtils.equals(this.mUsername, updateParameter.mUsername) || !TextUtils.equals(this.mBase64EncodedPassword, updateParameter.mBase64EncodedPassword) || !TextUtils.equals(this.mTrustRootCertUrl, updateParameter.mTrustRootCertUrl) || !Arrays.equals(this.mTrustRootCertSha256Fingerprint, updateParameter.mTrustRootCertSha256Fingerprint) || !Credential.isX509CertificateEquals(this.mCaCertificate, updateParameter.mCaCertificate)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mUpdateIntervalInMinutes), this.mUpdateMethod, this.mRestriction, this.mServerUri, this.mUsername, this.mBase64EncodedPassword, this.mTrustRootCertUrl, Integer.valueOf(Arrays.hashCode(this.mTrustRootCertSha256Fingerprint)), this.mCaCertificate);
    }

    public String toString() {
        return "UpdateInterval: " + this.mUpdateIntervalInMinutes + "\nUpdateMethod: " + this.mUpdateMethod + "\nRestriction: " + this.mRestriction + "\nServerURI: " + this.mServerUri + "\nUsername: " + this.mUsername + "\nTrustRootCertURL: " + this.mTrustRootCertUrl + "\n";
    }

    public boolean validate() {
        long j = this.mUpdateIntervalInMinutes;
        if (j == Long.MIN_VALUE) {
            Log.d(TAG, "Update interval not specified");
            return false;
        } else if (j == UPDATE_CHECK_INTERVAL_NEVER) {
            return true;
        } else {
            if (!TextUtils.equals(this.mUpdateMethod, UPDATE_METHOD_OMADM) && !TextUtils.equals(this.mUpdateMethod, UPDATE_METHOD_SPP)) {
                Log.d(TAG, "Unknown update method: " + this.mUpdateMethod);
                return false;
            } else if (!TextUtils.equals(this.mRestriction, UPDATE_RESTRICTION_HOMESP) && !TextUtils.equals(this.mRestriction, UPDATE_RESTRICTION_ROAMING_PARTNER) && !TextUtils.equals(this.mRestriction, UPDATE_RESTRICTION_UNRESTRICTED)) {
                Log.d(TAG, "Unknown restriction: " + this.mRestriction);
                return false;
            } else if (TextUtils.isEmpty(this.mServerUri)) {
                Log.d(TAG, "Missing update server URI");
                return false;
            } else if (this.mServerUri.getBytes(StandardCharsets.UTF_8).length > 1023) {
                Log.d(TAG, "URI bytes exceeded the max: " + this.mServerUri.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (TextUtils.isEmpty(this.mUsername)) {
                Log.d(TAG, "Missing username");
                return false;
            } else if (this.mUsername.getBytes(StandardCharsets.UTF_8).length > 63) {
                Log.d(TAG, "Username bytes exceeded the max: " + this.mUsername.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else if (TextUtils.isEmpty(this.mBase64EncodedPassword)) {
                Log.d(TAG, "Missing username");
                return false;
            } else if (this.mBase64EncodedPassword.getBytes(StandardCharsets.UTF_8).length > 255) {
                Log.d(TAG, "Password bytes exceeded the max: " + this.mBase64EncodedPassword.getBytes(StandardCharsets.UTF_8).length);
                return false;
            } else {
                try {
                    Base64.decode(this.mBase64EncodedPassword, 0);
                    if (TextUtils.isEmpty(this.mTrustRootCertUrl)) {
                        Log.d(TAG, "Missing trust root certificate URL");
                        return false;
                    } else if (this.mTrustRootCertUrl.getBytes(StandardCharsets.UTF_8).length > 1023) {
                        Log.d(TAG, "Trust root cert URL bytes exceeded the max: " + this.mTrustRootCertUrl.getBytes(StandardCharsets.UTF_8).length);
                        return false;
                    } else {
                        byte[] bArr = this.mTrustRootCertSha256Fingerprint;
                        if (bArr == null) {
                            Log.d(TAG, "Missing trust root certificate SHA-256 fingerprint");
                            return false;
                        } else if (bArr.length == 32) {
                            return true;
                        } else {
                            Log.d(TAG, "Incorrect size of trust root certificate SHA-256 fingerprint: " + this.mTrustRootCertSha256Fingerprint.length);
                            return false;
                        }
                    }
                } catch (IllegalArgumentException unused) {
                    Log.d(TAG, "Invalid encoding for password: " + this.mBase64EncodedPassword);
                    return false;
                }
            }
        }
    }
}
