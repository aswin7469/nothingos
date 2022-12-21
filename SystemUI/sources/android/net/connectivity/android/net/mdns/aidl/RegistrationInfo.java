package android.net.connectivity.android.net.mdns.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class RegistrationInfo implements Parcelable {
    public static final Parcelable.Creator<RegistrationInfo> CREATOR = new Parcelable.Creator<RegistrationInfo>() {
        public RegistrationInfo createFromParcel(Parcel parcel) {
            return RegistrationInfo.internalCreateFromParcel(parcel);
        }

        public RegistrationInfo[] newArray(int i) {
            return new RegistrationInfo[i];
        }
    };

    /* renamed from: id */
    public final int f45id;
    public final int interfaceIdx;
    public final int port;
    public final String registrationType;
    public final int result;
    public final String serviceName;
    public final byte[] txtRecord;

    public int describeContents() {
        return 0;
    }

    public static final class Builder {

        /* renamed from: id */
        private int f46id = 0;
        private int interfaceIdx = 0;
        private int port = 0;
        private String registrationType;
        private int result = 0;
        private String serviceName;
        private byte[] txtRecord;

        public Builder setId(int i) {
            this.f46id = i;
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

        public Builder setPort(int i) {
            this.port = i;
            return this;
        }

        public Builder setTxtRecord(byte[] bArr) {
            this.txtRecord = bArr;
            return this;
        }

        public Builder setInterfaceIdx(int i) {
            this.interfaceIdx = i;
            return this;
        }

        public RegistrationInfo build() {
            return new RegistrationInfo(this.f46id, this.result, this.serviceName, this.registrationType, this.port, this.txtRecord, this.interfaceIdx);
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.f45id);
        parcel.writeInt(this.result);
        parcel.writeString(this.serviceName);
        parcel.writeString(this.registrationType);
        parcel.writeInt(this.port);
        parcel.writeByteArray(this.txtRecord);
        parcel.writeInt(this.interfaceIdx);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public RegistrationInfo(int i, int i2, String str, String str2, int i3, byte[] bArr, int i4) {
        this.f45id = i;
        this.result = i2;
        this.serviceName = str;
        this.registrationType = str2;
        this.port = i3;
        this.txtRecord = bArr;
        this.interfaceIdx = i4;
    }

    /* access modifiers changed from: private */
    public static RegistrationInfo internalCreateFromParcel(Parcel parcel) {
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
                                    builder.setPort(parcel.readInt());
                                    if (parcel.dataPosition() - dataPosition >= readInt) {
                                        builder.build();
                                        if (dataPosition > Integer.MAX_VALUE - readInt) {
                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                        }
                                    } else {
                                        builder.setTxtRecord(parcel.createByteArray());
                                        if (parcel.dataPosition() - dataPosition >= readInt) {
                                            builder.build();
                                            if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                            }
                                        } else {
                                            builder.setInterfaceIdx(parcel.readInt());
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
        stringJoiner.add("id: " + this.f45id);
        stringJoiner.add("result: " + this.result);
        stringJoiner.add("serviceName: " + Objects.toString(this.serviceName));
        stringJoiner.add("registrationType: " + Objects.toString(this.registrationType));
        stringJoiner.add("port: " + this.port);
        stringJoiner.add("txtRecord: " + Arrays.toString(this.txtRecord));
        stringJoiner.add("interfaceIdx: " + this.interfaceIdx);
        return "android.net.connectivity.android.net.mdns.aidl.RegistrationInfo" + stringJoiner.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof RegistrationInfo)) {
            return false;
        }
        RegistrationInfo registrationInfo = (RegistrationInfo) obj;
        return Objects.deepEquals(Integer.valueOf(this.f45id), Integer.valueOf(registrationInfo.f45id)) && Objects.deepEquals(Integer.valueOf(this.result), Integer.valueOf(registrationInfo.result)) && Objects.deepEquals(this.serviceName, registrationInfo.serviceName) && Objects.deepEquals(this.registrationType, registrationInfo.registrationType) && Objects.deepEquals(Integer.valueOf(this.port), Integer.valueOf(registrationInfo.port)) && Objects.deepEquals(this.txtRecord, registrationInfo.txtRecord) && Objects.deepEquals(Integer.valueOf(this.interfaceIdx), Integer.valueOf(registrationInfo.interfaceIdx));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: java.io.Serializable[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int hashCode() {
        /*
            r3 = this;
            r0 = 7
            java.io.Serializable[] r0 = new java.p026io.Serializable[r0]
            int r1 = r3.f45id
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
            r2 = 0
            r0[r2] = r1
            int r1 = r3.result
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
            r2 = 1
            r0[r2] = r1
            r1 = 2
            java.lang.String r2 = r3.serviceName
            r0[r1] = r2
            r1 = 3
            java.lang.String r2 = r3.registrationType
            r0[r1] = r2
            int r1 = r3.port
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r1)
            r2 = 4
            r0[r2] = r1
            r1 = 5
            byte[] r2 = r3.txtRecord
            r0[r1] = r2
            int r3 = r3.interfaceIdx
            java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)
            r1 = 6
            r0[r1] = r3
            java.util.List r3 = java.util.Arrays.asList(r0)
            java.lang.Object[] r3 = r3.toArray()
            int r3 = java.util.Arrays.deepHashCode(r3)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.connectivity.android.net.mdns.aidl.RegistrationInfo.hashCode():int");
    }
}
