package android.nearby.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class FastPairDiscoveryItemParcel implements Parcelable {
    public static final Parcelable.Creator<FastPairDiscoveryItemParcel> CREATOR = new Parcelable.Creator<FastPairDiscoveryItemParcel>() {
        public FastPairDiscoveryItemParcel createFromParcel(Parcel parcel) {
            FastPairDiscoveryItemParcel fastPairDiscoveryItemParcel = new FastPairDiscoveryItemParcel();
            fastPairDiscoveryItemParcel.readFromParcel(parcel);
            return fastPairDiscoveryItemParcel;
        }

        public FastPairDiscoveryItemParcel[] newArray(int i) {
            return new FastPairDiscoveryItemParcel[i];
        }
    };
    public String actionUrl;
    public int actionUrlType = 0;
    public String appName;
    public byte[] authenticationPublicKeySecp256r1;
    public String description;
    public String deviceName;
    public String displayUrl;
    public long firstObservationTimestampMillis = 0;
    public String iconFifeUrl;
    public byte[] iconPng;

    /* renamed from: id */
    public String f39id;
    public long lastObservationTimestampMillis = 0;
    public String macAddress;
    public String packageName;
    public long pendingAppInstallTimestampMillis = 0;
    public int rssi = 0;
    public int state = 0;
    public String title;
    public String triggerId;
    public int txPower = 0;

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeString(this.f39id);
        parcel.writeString(this.macAddress);
        parcel.writeString(this.actionUrl);
        parcel.writeString(this.deviceName);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
        parcel.writeString(this.displayUrl);
        parcel.writeLong(this.lastObservationTimestampMillis);
        parcel.writeLong(this.firstObservationTimestampMillis);
        parcel.writeInt(this.state);
        parcel.writeInt(this.actionUrlType);
        parcel.writeLong(this.pendingAppInstallTimestampMillis);
        parcel.writeInt(this.rssi);
        parcel.writeInt(this.txPower);
        parcel.writeString(this.appName);
        parcel.writeString(this.packageName);
        parcel.writeString(this.triggerId);
        parcel.writeByteArray(this.iconPng);
        parcel.writeString(this.iconFifeUrl);
        parcel.writeByteArray(this.authenticationPublicKeySecp256r1);
        int dataPosition2 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition2 - dataPosition);
        parcel.setDataPosition(dataPosition2);
    }

    public final void readFromParcel(Parcel parcel) {
        int dataPosition = parcel.dataPosition();
        int readInt = parcel.readInt();
        if (readInt >= 4) {
            try {
                if (parcel.dataPosition() - dataPosition < readInt) {
                    this.f39id = parcel.readString();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.macAddress = parcel.readString();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.actionUrl = parcel.readString();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.deviceName = parcel.readString();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.title = parcel.readString();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.description = parcel.readString();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.displayUrl = parcel.readString();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.lastObservationTimestampMillis = parcel.readLong();
                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                    this.firstObservationTimestampMillis = parcel.readLong();
                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                        this.state = parcel.readInt();
                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                            this.actionUrlType = parcel.readInt();
                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                this.pendingAppInstallTimestampMillis = parcel.readLong();
                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                    this.rssi = parcel.readInt();
                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                        this.txPower = parcel.readInt();
                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                            this.appName = parcel.readString();
                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                this.packageName = parcel.readString();
                                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                    this.triggerId = parcel.readString();
                                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                        this.iconPng = parcel.createByteArray();
                                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                            this.iconFifeUrl = parcel.readString();
                                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                this.authenticationPublicKeySecp256r1 = parcel.createByteArray();
                                                                                                if (dataPosition <= Integer.MAX_VALUE - readInt) {
                                                                                                    parcel.setDataPosition(dataPosition + readInt);
                                                                                                    return;
                                                                                                }
                                                                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                                                                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                                                                            }
                                                                                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                                                                        }
                                                                                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                                                                    }
                                                                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                                                                }
                                                                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                                                            }
                                                                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                                                        }
                                                                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                                                    }
                                                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                                                }
                                                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                                            }
                                                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                                        }
                                                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                                    }
                                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                                }
                                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                                throw new BadParcelableException("Overflow in the size of parcelable");
                                            }
                                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                            throw new BadParcelableException("Overflow in the size of parcelable");
                                        }
                                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                        throw new BadParcelableException("Overflow in the size of parcelable");
                                    }
                                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                    throw new BadParcelableException("Overflow in the size of parcelable");
                                }
                            } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                                throw new BadParcelableException("Overflow in the size of parcelable");
                            }
                        } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                            throw new BadParcelableException("Overflow in the size of parcelable");
                        }
                    } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                        throw new BadParcelableException("Overflow in the size of parcelable");
                    }
                } else if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
            } catch (Throwable th) {
                if (dataPosition > Integer.MAX_VALUE - readInt) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                parcel.setDataPosition(dataPosition + readInt);
                throw th;
            }
        } else {
            throw new BadParcelableException("Parcelable too small");
        }
    }
}
