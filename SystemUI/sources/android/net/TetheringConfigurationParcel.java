package android.net;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class TetheringConfigurationParcel implements Parcelable {
    public static final Parcelable.Creator<TetheringConfigurationParcel> CREATOR = new Parcelable.Creator<TetheringConfigurationParcel>() {
        public TetheringConfigurationParcel createFromParcel(Parcel parcel) {
            TetheringConfigurationParcel tetheringConfigurationParcel = new TetheringConfigurationParcel();
            tetheringConfigurationParcel.readFromParcel(parcel);
            return tetheringConfigurationParcel;
        }

        public TetheringConfigurationParcel[] newArray(int i) {
            return new TetheringConfigurationParcel[i];
        }
    };
    public boolean chooseUpstreamAutomatically = false;
    public String[] defaultIPv4DNS;
    public boolean enableLegacyDhcpServer = false;
    public boolean isDunRequired = false;
    public String[] legacyDhcpRanges;
    public int[] preferredUpstreamIfaceTypes;
    public String[] provisioningApp;
    public String provisioningAppNoUi;
    public int provisioningCheckPeriod = 0;
    public int subId = 0;
    public String[] tetherableBluetoothRegexs;
    public String[] tetherableUsbRegexs;
    public String[] tetherableWifiRegexs;

    public int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.subId);
        parcel.writeStringArray(this.tetherableUsbRegexs);
        parcel.writeStringArray(this.tetherableWifiRegexs);
        parcel.writeStringArray(this.tetherableBluetoothRegexs);
        parcel.writeBoolean(this.isDunRequired);
        parcel.writeBoolean(this.chooseUpstreamAutomatically);
        parcel.writeIntArray(this.preferredUpstreamIfaceTypes);
        parcel.writeStringArray(this.legacyDhcpRanges);
        parcel.writeStringArray(this.defaultIPv4DNS);
        parcel.writeBoolean(this.enableLegacyDhcpServer);
        parcel.writeStringArray(this.provisioningApp);
        parcel.writeString(this.provisioningAppNoUi);
        parcel.writeInt(this.provisioningCheckPeriod);
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
                    this.subId = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.tetherableUsbRegexs = parcel.createStringArray();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.tetherableWifiRegexs = parcel.createStringArray();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.tetherableBluetoothRegexs = parcel.createStringArray();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.isDunRequired = parcel.readBoolean();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.chooseUpstreamAutomatically = parcel.readBoolean();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.preferredUpstreamIfaceTypes = parcel.createIntArray();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.legacyDhcpRanges = parcel.createStringArray();
                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                    this.defaultIPv4DNS = parcel.createStringArray();
                                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                                        this.enableLegacyDhcpServer = parcel.readBoolean();
                                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                                            this.provisioningApp = parcel.createStringArray();
                                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                                this.provisioningAppNoUi = parcel.readString();
                                                                if (parcel.dataPosition() - dataPosition < readInt) {
                                                                    this.provisioningCheckPeriod = parcel.readInt();
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
