package com.android.internal.content;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes4.dex */
public class ReferrerIntent extends Intent {
    public static final Parcelable.Creator<ReferrerIntent> CREATOR = new Parcelable.Creator<ReferrerIntent>() { // from class: com.android.internal.content.ReferrerIntent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ReferrerIntent mo3559createFromParcel(Parcel source) {
            return new ReferrerIntent(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ReferrerIntent[] mo3560newArray(int size) {
            return new ReferrerIntent[size];
        }
    };
    public final String mReferrer;

    public ReferrerIntent(Intent baseIntent, String referrer) {
        super(baseIntent);
        this.mReferrer = referrer;
    }

    @Override // android.content.Intent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeString(this.mReferrer);
    }

    ReferrerIntent(Parcel in) {
        readFromParcel(in);
        this.mReferrer = in.readString();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ReferrerIntent)) {
            return false;
        }
        ReferrerIntent other = (ReferrerIntent) obj;
        return filterEquals(other) && Objects.equals(this.mReferrer, other.mReferrer);
    }

    public int hashCode() {
        int result = (17 * 31) + filterHashCode();
        return (result * 31) + Objects.hashCode(this.mReferrer);
    }
}
