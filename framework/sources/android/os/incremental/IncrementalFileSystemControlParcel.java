package android.os.incremental;

import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class IncrementalFileSystemControlParcel implements Parcelable {
    public static final Parcelable.Creator<IncrementalFileSystemControlParcel> CREATOR = new Parcelable.Creator<IncrementalFileSystemControlParcel>() { // from class: android.os.incremental.IncrementalFileSystemControlParcel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public IncrementalFileSystemControlParcel mo3559createFromParcel(Parcel _aidl_source) {
            IncrementalFileSystemControlParcel _aidl_out = new IncrementalFileSystemControlParcel();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public IncrementalFileSystemControlParcel[] mo3560newArray(int _aidl_size) {
            return new IncrementalFileSystemControlParcel[_aidl_size];
        }
    };
    public ParcelFileDescriptor blocksWritten;
    public ParcelFileDescriptor cmd;
    public ParcelFileDescriptor log;
    public ParcelFileDescriptor pendingReads;

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        if (this.cmd != null) {
            _aidl_parcel.writeInt(1);
            this.cmd.writeToParcel(_aidl_parcel, 0);
        } else {
            _aidl_parcel.writeInt(0);
        }
        if (this.pendingReads != null) {
            _aidl_parcel.writeInt(1);
            this.pendingReads.writeToParcel(_aidl_parcel, 0);
        } else {
            _aidl_parcel.writeInt(0);
        }
        if (this.log != null) {
            _aidl_parcel.writeInt(1);
            this.log.writeToParcel(_aidl_parcel, 0);
        } else {
            _aidl_parcel.writeInt(0);
        }
        if (this.blocksWritten != null) {
            _aidl_parcel.writeInt(1);
            this.blocksWritten.writeToParcel(_aidl_parcel, 0);
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
                this.cmd = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.cmd = null;
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            if (_aidl_parcel.readInt() != 0) {
                this.pendingReads = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.pendingReads = null;
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            if (_aidl_parcel.readInt() != 0) {
                this.log = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.log = null;
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            if (_aidl_parcel.readInt() != 0) {
                this.blocksWritten = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.blocksWritten = null;
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
        int _mask = 0 | describeContents(this.cmd);
        return _mask | describeContents(this.pendingReads) | describeContents(this.log) | describeContents(this.blocksWritten);
    }

    private int describeContents(Object _v) {
        if (_v == null || !(_v instanceof Parcelable)) {
            return 0;
        }
        return ((Parcelable) _v).describeContents();
    }
}
