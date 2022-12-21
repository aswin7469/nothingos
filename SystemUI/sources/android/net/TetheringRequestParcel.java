package android.net;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;

public class TetheringRequestParcel implements Parcelable {
    public static final Parcelable.Creator<TetheringRequestParcel> CREATOR = new Parcelable.Creator<TetheringRequestParcel>() {
        public TetheringRequestParcel createFromParcel(Parcel parcel) {
            TetheringRequestParcel tetheringRequestParcel = new TetheringRequestParcel();
            tetheringRequestParcel.readFromParcel(parcel);
            return tetheringRequestParcel;
        }

        public TetheringRequestParcel[] newArray(int i) {
            return new TetheringRequestParcel[i];
        }
    };
    public int connectivityScope = 0;
    public boolean exemptFromEntitlementCheck = false;
    public LinkAddress localIPv4Address;
    public boolean showProvisioningUi = false;
    public LinkAddress staticClientAddress;
    public int tetheringType = 0;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.tetheringType);
        parcel.writeTypedObject(this.localIPv4Address, i);
        parcel.writeTypedObject(this.staticClientAddress, i);
        parcel.writeBoolean(this.exemptFromEntitlementCheck);
        parcel.writeBoolean(this.showProvisioningUi);
        parcel.writeInt(this.connectivityScope);
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
                    this.tetheringType = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.localIPv4Address = (LinkAddress) parcel.readTypedObject(LinkAddress.CREATOR);
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.staticClientAddress = (LinkAddress) parcel.readTypedObject(LinkAddress.CREATOR);
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.exemptFromEntitlementCheck = parcel.readBoolean();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.showProvisioningUi = parcel.readBoolean();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.connectivityScope = parcel.readInt();
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
        return describeContents(this.staticClientAddress) | describeContents(this.localIPv4Address) | 0;
    }

    private int describeContents(Object obj) {
        if (obj != null && (obj instanceof Parcelable)) {
            return ((Parcelable) obj).describeContents();
        }
        return 0;
    }
}
