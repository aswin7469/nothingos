package android.net.connectivity.android.net.mdns.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class DiscoveryInfo implements Parcelable {
    public static final Parcelable.Creator<DiscoveryInfo> CREATOR = new Parcelable.Creator<DiscoveryInfo>() {
        public DiscoveryInfo createFromParcel(Parcel parcel) {
            return DiscoveryInfo.internalCreateFromParcel(parcel);
        }

        public DiscoveryInfo[] newArray(int i) {
            return new DiscoveryInfo[i];
        }
    };
    public final String domainName;

    /* renamed from: id */
    public final int f41id;
    public final int interfaceIdx;
    public final int netId;
    public final String registrationType;
    public final int result;
    public final String serviceName;

    public int describeContents() {
        return 0;
    }

    public static final class Builder {
        private String domainName;

        /* renamed from: id */
        private int f42id = 0;
        private int interfaceIdx = 0;
        private int netId = 0;
        private String registrationType;
        private int result = 0;
        private String serviceName;

        public Builder setId(int i) {
            this.f42id = i;
            return this;
        }

        public Builder setResult(int i) {
            this.result = i;
            return this;
        }

        public Builder setServiceName(String str) {
            this.serviceName = str;
            return this;
        }

        public Builder setRegistrationType(String str) {
            this.registrationType = str;
            return this;
        }

        public Builder setDomainName(String str) {
            this.domainName = str;
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

        public DiscoveryInfo build() {
            return new DiscoveryInfo(this.f42id, this.result, this.serviceName, this.registrationType, this.domainName, this.interfaceIdx, this.netId);
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.f41id);
        parcel.writeInt(this.result);
        parcel.writeString(this.serviceName);
        parcel.writeString(this.registrationType);
        parcel.writeString(this.domainName);
        parcel.writeInt(this.interfaceIdx);
        parcel.writeInt(this.netId);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public DiscoveryInfo(int i, int i2, String str, String str2, String str3, int i3, int i4) {
        this.f41id = i;
        this.result = i2;
        this.serviceName = str;
        this.registrationType = str2;
        this.domainName = str3;
        this.interfaceIdx = i3;
        this.netId = i4;
    }

    /* access modifiers changed from: private */
    public static DiscoveryInfo internalCreateFromParcel(Parcel parcel) {
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
                            builder.setServiceName(parcel.readString());
                            if (parcel.dataPosition() - dataPosition >= readInt) {
                                builder.build();
                                if (dataPosition > Integer.MAX_VALUE - readInt) {
                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                }
                            } else {
                                builder.setRegistrationType(parcel.readString());
                                if (parcel.dataPosition() - dataPosition >= readInt) {
                                    builder.build();
                                    if (dataPosition > Integer.MAX_VALUE - readInt) {
                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                    }
                                } else {
                                    builder.setDomainName(parcel.readString());
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
        stringJoiner.add("id: " + this.f41id);
        stringJoiner.add("result: " + this.result);
        stringJoiner.add("serviceName: " + Objects.toString(this.serviceName));
        stringJoiner.add("registrationType: " + Objects.toString(this.registrationType));
        stringJoiner.add("domainName: " + Objects.toString(this.domainName));
        stringJoiner.add("interfaceIdx: " + this.interfaceIdx);
        stringJoiner.add("netId: " + this.netId);
        return "android.net.connectivity.android.net.mdns.aidl.DiscoveryInfo" + stringJoiner.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof DiscoveryInfo)) {
            return false;
        }
        DiscoveryInfo discoveryInfo = (DiscoveryInfo) obj;
        return Objects.deepEquals(Integer.valueOf(this.f41id), Integer.valueOf(discoveryInfo.f41id)) && Objects.deepEquals(Integer.valueOf(this.result), Integer.valueOf(discoveryInfo.result)) && Objects.deepEquals(this.serviceName, discoveryInfo.serviceName) && Objects.deepEquals(this.registrationType, discoveryInfo.registrationType) && Objects.deepEquals(this.domainName, discoveryInfo.domainName) && Objects.deepEquals(Integer.valueOf(this.interfaceIdx), Integer.valueOf(discoveryInfo.interfaceIdx)) && Objects.deepEquals(Integer.valueOf(this.netId), Integer.valueOf(discoveryInfo.netId));
    }

    public int hashCode() {
        return Arrays.deepHashCode(Arrays.asList(Integer.valueOf(this.f41id), Integer.valueOf(this.result), this.serviceName, this.registrationType, this.domainName, Integer.valueOf(this.interfaceIdx), Integer.valueOf(this.netId)).toArray());
    }
}
