package com.qti.extphone;

import android.os.Parcel;
import android.os.Parcelable;

public class Token implements Parcelable {
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator() {
        public Token createFromParcel(Parcel parcel) {
            return new Token(parcel);
        }

        public Token[] newArray(int i) {
            return new Token[i];
        }
    };
    private static final String TAG = "Token";
    public static final int UNSOL = -1;
    private int mValue;

    public int describeContents() {
        return 0;
    }

    public Token(int i) {
        this.mValue = i;
    }

    public Token(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public int get() {
        return this.mValue;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mValue);
    }

    public void readFromParcel(Parcel parcel) {
        this.mValue = parcel.readInt();
    }

    public String toString() {
        return "Token: " + get();
    }
}
