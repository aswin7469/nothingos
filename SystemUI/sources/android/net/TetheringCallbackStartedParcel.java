package android.net;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collection;
import java.util.List;

public class TetheringCallbackStartedParcel implements Parcelable {
    public static final Parcelable.Creator<TetheringCallbackStartedParcel> CREATOR = new Parcelable.Creator<TetheringCallbackStartedParcel>() {
        public TetheringCallbackStartedParcel createFromParcel(Parcel parcel) {
            TetheringCallbackStartedParcel tetheringCallbackStartedParcel = new TetheringCallbackStartedParcel();
            tetheringCallbackStartedParcel.readFromParcel(parcel);
            return tetheringCallbackStartedParcel;
        }

        public TetheringCallbackStartedParcel[] newArray(int i) {
            return new TetheringCallbackStartedParcel[i];
        }
    };
    public TetheringConfigurationParcel config;
    public int offloadStatus = 0;
    public TetherStatesParcel states;
    public long supportedTypes = 0;
    public List<TetheredClient> tetheredClients;
    public Network upstreamNetwork;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeLong(this.supportedTypes);
        parcel.writeTypedObject(this.upstreamNetwork, i);
        parcel.writeTypedObject(this.config, i);
        parcel.writeTypedObject(this.states, i);
        parcel.writeTypedList(this.tetheredClients);
        parcel.writeInt(this.offloadStatus);
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
                    this.supportedTypes = parcel.readLong();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.upstreamNetwork = (Network) parcel.readTypedObject(Network.CREATOR);
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.config = (TetheringConfigurationParcel) parcel.readTypedObject(TetheringConfigurationParcel.CREATOR);
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.states = (TetherStatesParcel) parcel.readTypedObject(TetherStatesParcel.CREATOR);
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.tetheredClients = parcel.createTypedArrayList(TetheredClient.CREATOR);
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.offloadStatus = parcel.readInt();
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

    public int describeContents() {
        return describeContents(this.tetheredClients) | describeContents(this.upstreamNetwork) | 0 | describeContents(this.config) | describeContents(this.states);
    }

    private int describeContents(Object obj) {
        int i = 0;
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Collection) {
            for (Object describeContents : (Collection) obj) {
                i |= describeContents(describeContents);
            }
            return i;
        } else if (obj instanceof Parcelable) {
            return ((Parcelable) obj).describeContents();
        } else {
            return 0;
        }
    }
}
