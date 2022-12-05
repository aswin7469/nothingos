package android.security.authorization;

import android.hardware.security.keymint.HardwareAuthToken;
import android.hardware.security.secureclock.TimeStampToken;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class AuthorizationTokens implements Parcelable {
    public static final Parcelable.Creator<AuthorizationTokens> CREATOR = new Parcelable.Creator<AuthorizationTokens>() { // from class: android.security.authorization.AuthorizationTokens.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public AuthorizationTokens mo3559createFromParcel(Parcel _aidl_source) {
            AuthorizationTokens _aidl_out = new AuthorizationTokens();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public AuthorizationTokens[] mo3560newArray(int _aidl_size) {
            return new AuthorizationTokens[_aidl_size];
        }
    };
    public HardwareAuthToken authToken;
    public TimeStampToken timestampToken;

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        if (this.authToken != null) {
            _aidl_parcel.writeInt(1);
            this.authToken.writeToParcel(_aidl_parcel, 0);
        } else {
            _aidl_parcel.writeInt(0);
        }
        if (this.timestampToken != null) {
            _aidl_parcel.writeInt(1);
            this.timestampToken.writeToParcel(_aidl_parcel, 0);
        } else {
            _aidl_parcel.writeInt(0);
        }
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public final void readFromParcel(Parcel _aidl_parcel) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        int _aidl_parcelable_size = _aidl_parcel.readInt();
        if (_aidl_parcelable_size < 0) {
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                throw new BadParcelableException("Overflow in the size of parcelable");
            }
            _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            return;
        }
        try {
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            if (_aidl_parcel.readInt() != 0) {
                this.authToken = HardwareAuthToken.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.authToken = null;
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            if (_aidl_parcel.readInt() != 0) {
                this.timestampToken = TimeStampToken.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.timestampToken = null;
            }
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                throw new BadParcelableException("Overflow in the size of parcelable");
            }
            _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
        } catch (Throwable th) {
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                throw new BadParcelableException("Overflow in the size of parcelable");
            }
            _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            throw th;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.authToken);
        return _mask | describeContents(this.timestampToken);
    }

    private int describeContents(Object _v) {
        if (_v == null || !(_v instanceof Parcelable)) {
            return 0;
        }
        return ((Parcelable) _v).describeContents();
    }
}
