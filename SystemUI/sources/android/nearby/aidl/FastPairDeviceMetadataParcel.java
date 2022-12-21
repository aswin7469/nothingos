package android.nearby.aidl;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class FastPairDeviceMetadataParcel implements Parcelable {
    public static final Parcelable.Creator<FastPairDeviceMetadataParcel> CREATOR = new Parcelable.Creator<FastPairDeviceMetadataParcel>() {
        public FastPairDeviceMetadataParcel createFromParcel(Parcel parcel) {
            FastPairDeviceMetadataParcel fastPairDeviceMetadataParcel = new FastPairDeviceMetadataParcel();
            fastPairDeviceMetadataParcel.readFromParcel(parcel);
            return fastPairDeviceMetadataParcel;
        }

        public FastPairDeviceMetadataParcel[] newArray(int i) {
            return new FastPairDeviceMetadataParcel[i];
        }
    };
    public int bleTxPower = 0;
    public String connectSuccessCompanionAppInstalled;
    public String connectSuccessCompanionAppNotInstalled;
    public int deviceType = 0;
    public String downloadCompanionAppDescription;
    public String failConnectGoToSettingsDescription;
    public byte[] image;
    public String imageUrl;
    public String initialNotificationDescription;
    public String initialNotificationDescriptionNoAccount;
    public String initialPairingDescription;
    public String intentUri;
    public String name;
    public String openCompanionAppDescription;
    public String retroactivePairingDescription;
    public String subsequentPairingDescription;
    public float triggerDistance = 0.0f;
    public String trueWirelessImageUrlCase;
    public String trueWirelessImageUrlLeftBud;
    public String trueWirelessImageUrlRightBud;
    public String unableToConnectDescription;
    public String unableToConnectTitle;
    public String updateCompanionAppDescription;
    public String waitLaunchCompanionAppDescription;

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeString(this.imageUrl);
        parcel.writeString(this.intentUri);
        parcel.writeInt(this.bleTxPower);
        parcel.writeFloat(this.triggerDistance);
        parcel.writeByteArray(this.image);
        parcel.writeString(this.name);
        parcel.writeInt(this.deviceType);
        parcel.writeString(this.trueWirelessImageUrlLeftBud);
        parcel.writeString(this.trueWirelessImageUrlRightBud);
        parcel.writeString(this.trueWirelessImageUrlCase);
        parcel.writeString(this.initialNotificationDescription);
        parcel.writeString(this.initialNotificationDescriptionNoAccount);
        parcel.writeString(this.openCompanionAppDescription);
        parcel.writeString(this.updateCompanionAppDescription);
        parcel.writeString(this.downloadCompanionAppDescription);
        parcel.writeString(this.unableToConnectTitle);
        parcel.writeString(this.unableToConnectDescription);
        parcel.writeString(this.initialPairingDescription);
        parcel.writeString(this.connectSuccessCompanionAppInstalled);
        parcel.writeString(this.connectSuccessCompanionAppNotInstalled);
        parcel.writeString(this.subsequentPairingDescription);
        parcel.writeString(this.retroactivePairingDescription);
        parcel.writeString(this.waitLaunchCompanionAppDescription);
        parcel.writeString(this.failConnectGoToSettingsDescription);
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
                    this.imageUrl = parcel.readString();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.intentUri = parcel.readString();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.bleTxPower = parcel.readInt();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.triggerDistance = parcel.readFloat();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.image = parcel.createByteArray();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.name = parcel.readString();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.deviceType = parcel.readInt();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.trueWirelessImageUrlLeftBud = parcel.readString();
                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                    this.trueWirelessImageUrlRightBud = parcel.readString();
                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                        this.trueWirelessImageUrlCase = parcel.readString();
                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                            this.initialNotificationDescription = parcel.readString();
                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                this.initialNotificationDescriptionNoAccount = parcel.readString();
                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                    this.openCompanionAppDescription = parcel.readString();
                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                        this.updateCompanionAppDescription = parcel.readString();
                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                            this.downloadCompanionAppDescription = parcel.readString();
                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                this.unableToConnectTitle = parcel.readString();
                                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                    this.unableToConnectDescription = parcel.readString();
                                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                        this.initialPairingDescription = parcel.readString();
                                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                            this.connectSuccessCompanionAppInstalled = parcel.readString();
                                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                this.connectSuccessCompanionAppNotInstalled = parcel.readString();
                                                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                    this.subsequentPairingDescription = parcel.readString();
                                                                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                        this.retroactivePairingDescription = parcel.readString();
                                                                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                            this.waitLaunchCompanionAppDescription = parcel.readString();
                                                                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                                                                this.failConnectGoToSettingsDescription = parcel.readString();
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
