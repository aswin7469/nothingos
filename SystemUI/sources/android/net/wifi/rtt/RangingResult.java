package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

public final class RangingResult implements Parcelable {
    public static final Parcelable.Creator<RangingResult> CREATOR = new Parcelable.Creator<RangingResult>() {
        public RangingResult[] newArray(int i) {
            return new RangingResult[i];
        }

        public RangingResult createFromParcel(Parcel parcel) {
            int readInt = parcel.readInt();
            PeerHandle peerHandle = null;
            MacAddress createFromParcel = parcel.readBoolean() ? MacAddress.CREATOR.createFromParcel(parcel) : null;
            boolean readBoolean = parcel.readBoolean();
            if (readBoolean) {
                peerHandle = new PeerHandle(parcel.readInt());
            }
            int readInt2 = parcel.readInt();
            int readInt3 = parcel.readInt();
            int readInt4 = parcel.readInt();
            int readInt5 = parcel.readInt();
            int readInt6 = parcel.readInt();
            byte[] createByteArray = parcel.createByteArray();
            byte[] createByteArray2 = parcel.createByteArray();
            ResponderLocation responderLocation = (ResponderLocation) parcel.readParcelable(getClass().getClassLoader());
            long readLong = parcel.readLong();
            boolean readBoolean2 = parcel.readBoolean();
            if (readBoolean) {
                return new RangingResult(readInt, peerHandle, readInt2, readInt3, readInt4, readInt5, readInt6, createByteArray, createByteArray2, responderLocation, readLong);
            }
            return new RangingResult(readInt, createFromParcel, readInt2, readInt3, readInt4, readInt5, readInt6, createByteArray, createByteArray2, responderLocation, readLong, readBoolean2);
        }
    };
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final int STATUS_FAIL = 1;
    public static final int STATUS_RESPONDER_DOES_NOT_SUPPORT_IEEE80211MC = 2;
    public static final int STATUS_SUCCESS = 0;
    private static final String TAG = "RangingResult";
    public final int mDistanceMm;
    public final int mDistanceStdDevMm;
    public final boolean mIs80211mcMeasurement;
    public final byte[] mLci;
    public final byte[] mLcr;
    public final MacAddress mMac;
    public final int mNumAttemptedMeasurements;
    public final int mNumSuccessfulMeasurements;
    public final PeerHandle mPeerHandle;
    public final ResponderLocation mResponderLocation;
    public final int mRssi;
    public final int mStatus;
    public final long mTimestamp;

    @Retention(RetentionPolicy.SOURCE)
    public @interface RangeResultStatus {
    }

    public int describeContents() {
        return 0;
    }

    public RangingResult(int i, MacAddress macAddress, int i2, int i3, int i4, int i5, int i6, byte[] bArr, byte[] bArr2, ResponderLocation responderLocation, long j, boolean z) {
        this.mStatus = i;
        this.mMac = macAddress;
        this.mPeerHandle = null;
        this.mDistanceMm = i2;
        this.mDistanceStdDevMm = i3;
        this.mRssi = i4;
        this.mNumAttemptedMeasurements = i5;
        this.mNumSuccessfulMeasurements = i6;
        this.mLci = bArr == null ? EMPTY_BYTE_ARRAY : bArr;
        this.mLcr = bArr2 == null ? EMPTY_BYTE_ARRAY : bArr2;
        this.mResponderLocation = responderLocation;
        this.mTimestamp = j;
        this.mIs80211mcMeasurement = z;
    }

    public RangingResult(int i, PeerHandle peerHandle, int i2, int i3, int i4, int i5, int i6, byte[] bArr, byte[] bArr2, ResponderLocation responderLocation, long j) {
        this.mStatus = i;
        this.mMac = null;
        this.mPeerHandle = peerHandle;
        this.mDistanceMm = i2;
        this.mDistanceStdDevMm = i3;
        this.mRssi = i4;
        this.mNumAttemptedMeasurements = i5;
        this.mNumSuccessfulMeasurements = i6;
        this.mLci = bArr == null ? EMPTY_BYTE_ARRAY : bArr;
        this.mLcr = bArr2 == null ? EMPTY_BYTE_ARRAY : bArr2;
        this.mResponderLocation = responderLocation;
        this.mTimestamp = j;
        this.mIs80211mcMeasurement = true;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public MacAddress getMacAddress() {
        return this.mMac;
    }

    public PeerHandle getPeerHandle() {
        return this.mPeerHandle;
    }

    public int getDistanceMm() {
        if (this.mStatus == 0) {
            return this.mDistanceMm;
        }
        throw new IllegalStateException("getDistanceMm(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public int getDistanceStdDevMm() {
        if (this.mStatus == 0) {
            return this.mDistanceStdDevMm;
        }
        throw new IllegalStateException("getDistanceStdDevMm(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public int getRssi() {
        if (this.mStatus == 0) {
            return this.mRssi;
        }
        throw new IllegalStateException("getRssi(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public int getNumAttemptedMeasurements() {
        if (this.mStatus == 0) {
            return this.mNumAttemptedMeasurements;
        }
        throw new IllegalStateException("getNumAttemptedMeasurements(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public int getNumSuccessfulMeasurements() {
        if (this.mStatus == 0) {
            return this.mNumSuccessfulMeasurements;
        }
        throw new IllegalStateException("getNumSuccessfulMeasurements(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public ResponderLocation getUnverifiedResponderLocation() {
        if (this.mStatus == 0) {
            return this.mResponderLocation;
        }
        throw new IllegalStateException("getUnverifiedResponderLocation(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    @SystemApi
    public byte[] getLci() {
        if (this.mStatus == 0) {
            return this.mLci;
        }
        throw new IllegalStateException("getLci(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    @SystemApi
    public byte[] getLcr() {
        if (this.mStatus == 0) {
            return this.mLcr;
        }
        throw new IllegalStateException("getReportedLocationCivic(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public long getRangingTimestampMillis() {
        if (this.mStatus == 0) {
            return this.mTimestamp;
        }
        throw new IllegalStateException("getRangingTimestampMillis(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public boolean is80211mcMeasurement() {
        if (this.mStatus == 0) {
            return this.mIs80211mcMeasurement;
        }
        throw new IllegalStateException("is80211mcMeasurementResult(): invoked on an invalid result: getStatus()=" + this.mStatus);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mStatus);
        if (this.mMac == null) {
            parcel.writeBoolean(false);
        } else {
            parcel.writeBoolean(true);
            this.mMac.writeToParcel(parcel, i);
        }
        if (this.mPeerHandle == null) {
            parcel.writeBoolean(false);
        } else {
            parcel.writeBoolean(true);
            parcel.writeInt(this.mPeerHandle.peerId);
        }
        parcel.writeInt(this.mDistanceMm);
        parcel.writeInt(this.mDistanceStdDevMm);
        parcel.writeInt(this.mRssi);
        parcel.writeInt(this.mNumAttemptedMeasurements);
        parcel.writeInt(this.mNumSuccessfulMeasurements);
        parcel.writeByteArray(this.mLci);
        parcel.writeByteArray(this.mLcr);
        parcel.writeParcelable(this.mResponderLocation, i);
        parcel.writeLong(this.mTimestamp);
        parcel.writeBoolean(this.mIs80211mcMeasurement);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("RangingResult: [status=");
        sb.append(this.mStatus);
        sb.append(", mac=");
        sb.append((Object) this.mMac);
        sb.append(", peerHandle=");
        PeerHandle peerHandle = this.mPeerHandle;
        sb.append(peerHandle == null ? "<null>" : Integer.valueOf(peerHandle.peerId));
        sb.append(", distanceMm=");
        sb.append(this.mDistanceMm);
        sb.append(", distanceStdDevMm=");
        sb.append(this.mDistanceStdDevMm);
        sb.append(", rssi=");
        sb.append(this.mRssi);
        sb.append(", numAttemptedMeasurements=");
        sb.append(this.mNumAttemptedMeasurements);
        sb.append(", numSuccessfulMeasurements=");
        sb.append(this.mNumSuccessfulMeasurements);
        sb.append(", lci=");
        sb.append((Object) this.mLci);
        sb.append(", lcr=");
        sb.append((Object) this.mLcr);
        sb.append(", responderLocation=");
        sb.append((Object) this.mResponderLocation);
        sb.append(", timestamp=");
        sb.append(this.mTimestamp);
        sb.append(", is80211mcMeasurement=");
        sb.append(this.mIs80211mcMeasurement);
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RangingResult)) {
            return false;
        }
        RangingResult rangingResult = (RangingResult) obj;
        if (this.mStatus == rangingResult.mStatus && Objects.equals(this.mMac, rangingResult.mMac) && Objects.equals(this.mPeerHandle, rangingResult.mPeerHandle) && this.mDistanceMm == rangingResult.mDistanceMm && this.mDistanceStdDevMm == rangingResult.mDistanceStdDevMm && this.mRssi == rangingResult.mRssi && this.mNumAttemptedMeasurements == rangingResult.mNumAttemptedMeasurements && this.mNumSuccessfulMeasurements == rangingResult.mNumSuccessfulMeasurements && Arrays.equals(this.mLci, rangingResult.mLci) && Arrays.equals(this.mLcr, rangingResult.mLcr) && this.mTimestamp == rangingResult.mTimestamp && this.mIs80211mcMeasurement == rangingResult.mIs80211mcMeasurement && Objects.equals(this.mResponderLocation, rangingResult.mResponderLocation)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mStatus), this.mMac, this.mPeerHandle, Integer.valueOf(this.mDistanceMm), Integer.valueOf(this.mDistanceStdDevMm), Integer.valueOf(this.mRssi), Integer.valueOf(this.mNumAttemptedMeasurements), Integer.valueOf(this.mNumSuccessfulMeasurements), this.mLci, this.mLcr, this.mResponderLocation, Long.valueOf(this.mTimestamp), Boolean.valueOf(this.mIs80211mcMeasurement));
    }
}
