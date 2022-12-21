package android.net.connectivity.android.net.mdns.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class GetAddressInfo implements Parcelable {
    public static final Parcelable.Creator<GetAddressInfo> CREATOR = new Parcelable.Creator<GetAddressInfo>() {
        public GetAddressInfo createFromParcel(Parcel parcel) {
            return GetAddressInfo.internalCreateFromParcel(parcel);
        }

        public GetAddressInfo[] newArray(int i) {
            return new GetAddressInfo[i];
        }
    };
    public final String address;
    public final String hostname;

    /* renamed from: id */
    public final int f43id;
    public final int interfaceIdx;
    public final int netId;
    public final int result;

    public int describeContents() {
        return 0;
    }

    public static final class Builder {
        private String address;
        private String hostname;

        /* renamed from: id */
        private int f44id = 0;
        private int interfaceIdx = 0;
        private int netId = 0;
        private int result = 0;

        public Builder setId(int i) {
            this.f44id = i;
            return this;
        }

        public Builder setResult(int i) {
            this.result = i;
            return this;
        }

        public Builder setHostname(String str) {
            this.hostname = str;
            return this;
        }

        public Builder setAddress(String str) {
            this.address = str;
            return this;
        }

        public Builder setInterfaceIdx(int i) {
            this.interfaceIdx = i;
            return this;
        }

        public Builder setNetId(int i) {
            this.netId = i;
            return this;
        }

        public GetAddressInfo build() {
            return new GetAddressInfo(this.f44id, this.result, this.hostname, this.address, this.interfaceIdx, this.netId);
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.f43id);
        parcel.writeInt(this.result);
        parcel.writeString(this.hostname);
        parcel.writeString(this.address);
        parcel.writeInt(this.interfaceIdx);
        parcel.writeInt(this.netId);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public GetAddressInfo(int i, int i2, String str, String str2, int i3, int i4) {
        this.f43id = i;
        this.result = i2;
        this.hostname = str;
        this.address = str2;
        this.interfaceIdx = i3;
        this.netId = i4;
    }

    /* access modifiers changed from: private */
    public static GetAddressInfo internalCreateFromParcel(Parcel parcel) {
        Builder builder = new Builder();
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        if (readInt >= 4) {
            try {
                builder.build();
                if (parcel.dataPosition() - dataPosition >= readInt) {
                    builder.build();
                    if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                } else {
                    builder.setId(parcel.readInt());
                    if (parcel.dataPosition() - dataPosition >= readInt) {
                        builder.build();
                        if (dataPosition > Integer.MAX_VALUE - readInt) {
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        }
                    } else {
                        builder.setResult(parcel.readInt());
                        if (parcel.dataPosition() - dataPosition >= readInt) {
                            builder.build();
                            if (dataPosition > Integer.MAX_VALUE - readInt) {
                                throw new BadParcelableException("Overflow in the size of parcelable");
                            }
                        } else {
                            builder.setHostname(parcel.readString());
                            if (parcel.dataPosition() - dataPosition >= readInt) {
                                builder.build();
                                if (dataPosition > Integer.MAX_VALUE - readInt) {
                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                }
                            } else {
                                builder.setAddress(parcel.readString());
                                if (parcel.dataPosition() - dataPosition >= readInt) {
                                    builder.build();
                                    if (dataPosition > Integer.MAX_VALUE - readInt) {
                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                    }
                                } else {
                                    builder.setInterfaceIdx(parcel.readInt());
                                    if (parcel.dataPosition() - dataPosition >= readInt) {
                                        builder.build();
                                        if (dataPosition > Integer.MAX_VALUE - readInt) {
                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                        }
                                    } else {
                                        builder.setNetId(parcel.readInt());
                                        if (dataPosition > Integer.MAX_VALUE - readInt) {
                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable unused) {
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
            }
        } else {
            throw new BadParcelableException("Parcelable too small");
        }
        parcel.setDataPosition(dataPosition + readInt);
        return builder.build();
    }

    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
        stringJoiner.add("id: " + this.f43id);
        stringJoiner.add("result: " + this.result);
        stringJoiner.add("hostname: " + Objects.toString(this.hostname));
        stringJoiner.add("address: " + Objects.toString(this.address));
        stringJoiner.add("interfaceIdx: " + this.interfaceIdx);
        stringJoiner.add("netId: " + this.netId);
        return "android.net.connectivity.android.net.mdns.aidl.GetAddressInfo" + stringJoiner.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof GetAddressInfo)) {
            return false;
        }
        GetAddressInfo getAddressInfo = (GetAddressInfo) obj;
        return Objects.deepEquals(Integer.valueOf(this.f43id), Integer.valueOf(getAddressInfo.f43id)) && Objects.deepEquals(Integer.valueOf(this.result), Integer.valueOf(getAddressInfo.result)) && Objects.deepEquals(this.hostname, getAddressInfo.hostname) && Objects.deepEquals(this.address, getAddressInfo.address) && Objects.deepEquals(Integer.valueOf(this.interfaceIdx), Integer.valueOf(getAddressInfo.interfaceIdx)) && Objects.deepEquals(Integer.valueOf(this.netId), Integer.valueOf(getAddressInfo.netId));
    }

    public int hashCode() {
        return Arrays.deepHashCode(Arrays.asList(Integer.valueOf(this.f43id), Integer.valueOf(this.result), this.hostname, this.address, Integer.valueOf(this.interfaceIdx), Integer.valueOf(this.netId)).toArray());
    }
}
