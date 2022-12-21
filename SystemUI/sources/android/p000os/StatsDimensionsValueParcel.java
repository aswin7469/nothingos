package android.p000os;

import android.os.Parcelable;

/* renamed from: android.os.StatsDimensionsValueParcel */
public class StatsDimensionsValueParcel implements Parcelable {
    public static final Parcelable.Creator<StatsDimensionsValueParcel> CREATOR = new Parcelable.Creator<StatsDimensionsValueParcel>() {
        public StatsDimensionsValueParcel createFromParcel(Parcel parcel) {
            StatsDimensionsValueParcel statsDimensionsValueParcel = new StatsDimensionsValueParcel();
            statsDimensionsValueParcel.readFromParcel(parcel);
            return statsDimensionsValueParcel;
        }

        public StatsDimensionsValueParcel[] newArray(int i) {
            return new StatsDimensionsValueParcel[i];
        }
    };
    public boolean boolValue = false;
    public int field = 0;
    public float floatValue = 0.0f;
    public int intValue = 0;
    public long longValue = 0;
    public String stringValue;
    public StatsDimensionsValueParcel[] tupleValue;
    public int valueType = 0;

    public final void writeToParcel(Parcel parcel, int i) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(0);
        parcel.writeInt(this.field);
        parcel.writeInt(this.valueType);
        parcel.writeString(this.stringValue);
        parcel.writeInt(this.intValue);
        parcel.writeLong(this.longValue);
        parcel.writeBoolean(this.boolValue);
        parcel.writeFloat(this.floatValue);
        parcel.writeTypedArray(this.tupleValue, i);
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
                    this.field = parcel.readInt();
                    if (parcel.dataPosition() - dataPosition < readInt) {
                        this.valueType = parcel.readInt();
                        if (parcel.dataPosition() - dataPosition < readInt) {
                            this.stringValue = parcel.readString();
                            if (parcel.dataPosition() - dataPosition < readInt) {
                                this.intValue = parcel.readInt();
                                if (parcel.dataPosition() - dataPosition < readInt) {
                                    this.longValue = parcel.readLong();
                                    if (parcel.dataPosition() - dataPosition < readInt) {
                                        this.boolValue = parcel.readBoolean();
                                        if (parcel.dataPosition() - dataPosition < readInt) {
                                            this.floatValue = parcel.readFloat();
                                            if (parcel.dataPosition() - dataPosition < readInt) {
                                                this.tupleValue = (StatsDimensionsValueParcel[]) parcel.createTypedArray(CREATOR);
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
        return describeContents(this.tupleValue) | 0;
    }

    private int describeContents(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Object[]) {
            int i = 0;
            for (Object describeContents : (Object[]) obj) {
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
