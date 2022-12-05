package android.hardware.camera2.extension;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.impl.PhysicalCaptureResultInfo;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
public class ParcelTotalCaptureResult implements Parcelable {
    public static final Parcelable.Creator<ParcelTotalCaptureResult> CREATOR = new Parcelable.Creator<ParcelTotalCaptureResult>() { // from class: android.hardware.camera2.extension.ParcelTotalCaptureResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ParcelTotalCaptureResult mo3559createFromParcel(Parcel _aidl_source) {
            ParcelTotalCaptureResult _aidl_out = new ParcelTotalCaptureResult();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ParcelTotalCaptureResult[] mo3560newArray(int _aidl_size) {
            return new ParcelTotalCaptureResult[_aidl_size];
        }
    };
    public String logicalCameraId;
    public CaptureRequest parent;
    public List<ParcelCaptureResult> partials;
    public List<PhysicalCaptureResultInfo> physicalResult;
    public CameraMetadataNative results;
    public int sequenceId = 0;
    public long frameNumber = 0;
    public int sessionId = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeString(this.logicalCameraId);
        if (this.results != null) {
            _aidl_parcel.writeInt(1);
            this.results.writeToParcel(_aidl_parcel, 0);
        } else {
            _aidl_parcel.writeInt(0);
        }
        if (this.parent != null) {
            _aidl_parcel.writeInt(1);
            this.parent.writeToParcel(_aidl_parcel, 0);
        } else {
            _aidl_parcel.writeInt(0);
        }
        _aidl_parcel.writeInt(this.sequenceId);
        _aidl_parcel.writeLong(this.frameNumber);
        _aidl_parcel.writeTypedList(this.partials);
        _aidl_parcel.writeInt(this.sessionId);
        _aidl_parcel.writeTypedList(this.physicalResult);
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
            this.logicalCameraId = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            if (_aidl_parcel.readInt() != 0) {
                this.results = CameraMetadataNative.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.results = null;
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            if (_aidl_parcel.readInt() != 0) {
                this.parent = CaptureRequest.CREATOR.mo3559createFromParcel(_aidl_parcel);
            } else {
                this.parent = null;
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.sequenceId = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.frameNumber = _aidl_parcel.readLong();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.partials = _aidl_parcel.createTypedArrayList(ParcelCaptureResult.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.sessionId = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.physicalResult = _aidl_parcel.createTypedArrayList(PhysicalCaptureResultInfo.CREATOR);
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
        int _mask = 0 | describeContents(this.results);
        return _mask | describeContents(this.parent) | describeContents(this.partials) | describeContents(this.physicalResult);
    }

    private int describeContents(Object _v) {
        if (_v == null) {
            return 0;
        }
        if (_v instanceof Collection) {
            int _mask = 0;
            for (Object o : (Collection) _v) {
                _mask |= describeContents(o);
            }
            return _mask;
        } else if (!(_v instanceof Parcelable)) {
            return 0;
        } else {
            return ((Parcelable) _v).describeContents();
        }
    }
}
