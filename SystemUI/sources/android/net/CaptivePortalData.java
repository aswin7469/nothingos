package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class CaptivePortalData implements Parcelable {
    public static final int CAPTIVE_PORTAL_DATA_SOURCE_OTHER = 0;
    public static final int CAPTIVE_PORTAL_DATA_SOURCE_PASSPOINT = 1;
    public static final Parcelable.Creator<CaptivePortalData> CREATOR = new Parcelable.Creator<CaptivePortalData>() {
        public CaptivePortalData createFromParcel(Parcel parcel) {
            return new CaptivePortalData(parcel);
        }

        public CaptivePortalData[] newArray(int i) {
            return new CaptivePortalData[i];
        }
    };
    /* access modifiers changed from: private */
    public final long mByteLimit;
    /* access modifiers changed from: private */
    public final boolean mCaptive;
    /* access modifiers changed from: private */
    public final long mExpiryTimeMillis;
    /* access modifiers changed from: private */
    public final boolean mIsSessionExtendable;
    /* access modifiers changed from: private */
    public final long mRefreshTimeMillis;
    /* access modifiers changed from: private */
    public final Uri mUserPortalUrl;
    /* access modifiers changed from: private */
    public final int mUserPortalUrlSource;
    /* access modifiers changed from: private */
    public final String mVenueFriendlyName;
    /* access modifiers changed from: private */
    public final Uri mVenueInfoUrl;
    /* access modifiers changed from: private */
    public final int mVenueInfoUrlSource;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CaptivePortalDataSource {
    }

    public int describeContents() {
        return 0;
    }

    private CaptivePortalData(long j, Uri uri, Uri uri2, boolean z, long j2, long j3, boolean z2, CharSequence charSequence, int i, int i2) {
        String str;
        this.mRefreshTimeMillis = j;
        this.mUserPortalUrl = uri;
        this.mVenueInfoUrl = uri2;
        this.mIsSessionExtendable = z;
        this.mByteLimit = j2;
        this.mExpiryTimeMillis = j3;
        this.mCaptive = z2;
        if (charSequence == null) {
            str = null;
        } else {
            str = charSequence.toString();
        }
        this.mVenueFriendlyName = str;
        this.mVenueInfoUrlSource = i;
        this.mUserPortalUrlSource = i2;
    }

    private CaptivePortalData(Parcel parcel) {
        this(parcel.readLong(), (Uri) parcel.readParcelable((ClassLoader) null), (Uri) parcel.readParcelable((ClassLoader) null), parcel.readBoolean(), parcel.readLong(), parcel.readLong(), parcel.readBoolean(), parcel.readString(), parcel.readInt(), parcel.readInt());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mRefreshTimeMillis);
        parcel.writeParcelable(this.mUserPortalUrl, 0);
        parcel.writeParcelable(this.mVenueInfoUrl, 0);
        parcel.writeBoolean(this.mIsSessionExtendable);
        parcel.writeLong(this.mByteLimit);
        parcel.writeLong(this.mExpiryTimeMillis);
        parcel.writeBoolean(this.mCaptive);
        parcel.writeString(this.mVenueFriendlyName);
        parcel.writeInt(this.mVenueInfoUrlSource);
        parcel.writeInt(this.mUserPortalUrlSource);
    }

    public static class Builder {
        private long mBytesRemaining = -1;
        private boolean mCaptive;
        private long mExpiryTime = -1;
        private boolean mIsSessionExtendable;
        private long mRefreshTime;
        private Uri mUserPortalUrl;
        private int mUserPortalUrlSource = 0;
        private CharSequence mVenueFriendlyName;
        private Uri mVenueInfoUrl;
        private int mVenueInfoUrlSource = 0;

        public Builder() {
        }

        public Builder(CaptivePortalData captivePortalData) {
            if (captivePortalData != null) {
                setRefreshTime(captivePortalData.mRefreshTimeMillis).setUserPortalUrl(captivePortalData.mUserPortalUrl, captivePortalData.mUserPortalUrlSource).setVenueInfoUrl(captivePortalData.mVenueInfoUrl, captivePortalData.mVenueInfoUrlSource).setSessionExtendable(captivePortalData.mIsSessionExtendable).setBytesRemaining(captivePortalData.mByteLimit).setExpiryTime(captivePortalData.mExpiryTimeMillis).setCaptive(captivePortalData.mCaptive).setVenueFriendlyName(captivePortalData.mVenueFriendlyName);
            }
        }

        public Builder setRefreshTime(long j) {
            this.mRefreshTime = j;
            return this;
        }

        public Builder setUserPortalUrl(Uri uri) {
            return setUserPortalUrl(uri, 0);
        }

        public Builder setUserPortalUrl(Uri uri, int i) {
            this.mUserPortalUrl = uri;
            this.mUserPortalUrlSource = i;
            return this;
        }

        public Builder setVenueInfoUrl(Uri uri) {
            return setVenueInfoUrl(uri, 0);
        }

        public Builder setVenueInfoUrl(Uri uri, int i) {
            this.mVenueInfoUrl = uri;
            this.mVenueInfoUrlSource = i;
            return this;
        }

        public Builder setSessionExtendable(boolean z) {
            this.mIsSessionExtendable = z;
            return this;
        }

        public Builder setBytesRemaining(long j) {
            this.mBytesRemaining = j;
            return this;
        }

        public Builder setExpiryTime(long j) {
            this.mExpiryTime = j;
            return this;
        }

        public Builder setCaptive(boolean z) {
            this.mCaptive = z;
            return this;
        }

        public Builder setVenueFriendlyName(CharSequence charSequence) {
            this.mVenueFriendlyName = charSequence;
            return this;
        }

        public CaptivePortalData build() {
            return new CaptivePortalData(this.mRefreshTime, this.mUserPortalUrl, this.mVenueInfoUrl, this.mIsSessionExtendable, this.mBytesRemaining, this.mExpiryTime, this.mCaptive, this.mVenueFriendlyName, this.mVenueInfoUrlSource, this.mUserPortalUrlSource);
        }
    }

    public long getRefreshTimeMillis() {
        return this.mRefreshTimeMillis;
    }

    public Uri getUserPortalUrl() {
        return this.mUserPortalUrl;
    }

    public Uri getVenueInfoUrl() {
        return this.mVenueInfoUrl;
    }

    public boolean isSessionExtendable() {
        return this.mIsSessionExtendable;
    }

    public long getByteLimit() {
        return this.mByteLimit;
    }

    public long getExpiryTimeMillis() {
        return this.mExpiryTimeMillis;
    }

    public boolean isCaptive() {
        return this.mCaptive;
    }

    public int getVenueInfoUrlSource() {
        return this.mVenueInfoUrlSource;
    }

    public int getUserPortalUrlSource() {
        return this.mUserPortalUrlSource;
    }

    public CharSequence getVenueFriendlyName() {
        return this.mVenueFriendlyName;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mRefreshTimeMillis), this.mUserPortalUrl, this.mVenueInfoUrl, Boolean.valueOf(this.mIsSessionExtendable), Long.valueOf(this.mByteLimit), Long.valueOf(this.mExpiryTimeMillis), Boolean.valueOf(this.mCaptive), this.mVenueFriendlyName, Integer.valueOf(this.mVenueInfoUrlSource), Integer.valueOf(this.mUserPortalUrlSource));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CaptivePortalData)) {
            return false;
        }
        CaptivePortalData captivePortalData = (CaptivePortalData) obj;
        if (this.mRefreshTimeMillis == captivePortalData.mRefreshTimeMillis && Objects.equals(this.mUserPortalUrl, captivePortalData.mUserPortalUrl) && Objects.equals(this.mVenueInfoUrl, captivePortalData.mVenueInfoUrl) && this.mIsSessionExtendable == captivePortalData.mIsSessionExtendable && this.mByteLimit == captivePortalData.mByteLimit && this.mExpiryTimeMillis == captivePortalData.mExpiryTimeMillis && this.mCaptive == captivePortalData.mCaptive && Objects.equals(this.mVenueFriendlyName, captivePortalData.mVenueFriendlyName) && this.mVenueInfoUrlSource == captivePortalData.mVenueInfoUrlSource && this.mUserPortalUrlSource == captivePortalData.mUserPortalUrlSource) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "CaptivePortalData {refreshTime: " + this.mRefreshTimeMillis + ", userPortalUrl: " + this.mUserPortalUrl + ", venueInfoUrl: " + this.mVenueInfoUrl + ", isSessionExtendable: " + this.mIsSessionExtendable + ", byteLimit: " + this.mByteLimit + ", expiryTime: " + this.mExpiryTimeMillis + ", captive: " + this.mCaptive + ", venueFriendlyName: " + this.mVenueFriendlyName + ", venueInfoUrlSource: " + this.mVenueInfoUrlSource + ", userPortalUrlSource: " + this.mUserPortalUrlSource + "}";
    }
}
