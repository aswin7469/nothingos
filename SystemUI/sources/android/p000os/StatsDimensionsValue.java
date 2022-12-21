package android.p000os;

import android.annotation.SystemApi;
import android.os.Parcelable;
import android.util.Log;
import androidx.core.p004os.EnvironmentCompat;
import java.util.ArrayList;
import java.util.List;

@SystemApi
/* renamed from: android.os.StatsDimensionsValue */
public final class StatsDimensionsValue implements Parcelable {
    public static final int BOOLEAN_VALUE_TYPE = 5;
    public static final Parcelable.Creator<StatsDimensionsValue> CREATOR = new Parcelable.Creator<StatsDimensionsValue>() {
        public StatsDimensionsValue createFromParcel(Parcel parcel) {
            return new StatsDimensionsValue(parcel);
        }

        public StatsDimensionsValue[] newArray(int i) {
            return new StatsDimensionsValue[i];
        }
    };
    public static final int FLOAT_VALUE_TYPE = 6;
    public static final int INT_VALUE_TYPE = 3;
    public static final int LONG_VALUE_TYPE = 4;
    public static final int STRING_VALUE_TYPE = 2;
    private static final String TAG = "StatsDimensionsValue";
    public static final int TUPLE_VALUE_TYPE = 7;
    private final StatsDimensionsValueParcel mInner;

    public int describeContents() {
        return 0;
    }

    public StatsDimensionsValue(Parcel parcel) {
        this.mInner = StatsDimensionsValueParcel.CREATOR.createFromParcel(parcel);
    }

    public StatsDimensionsValue(StatsDimensionsValueParcel statsDimensionsValueParcel) {
        this.mInner = statsDimensionsValueParcel;
    }

    public int getField() {
        return this.mInner.field;
    }

    public String getStringValue() {
        if (this.mInner.valueType == 2) {
            return this.mInner.stringValue;
        }
        Log.w(TAG, "Value type is " + getValueTypeAsString() + ", not string.");
        return null;
    }

    public int getIntValue() {
        if (this.mInner.valueType == 3) {
            return this.mInner.intValue;
        }
        Log.w(TAG, "Value type is " + getValueTypeAsString() + ", not int.");
        return 0;
    }

    public long getLongValue() {
        if (this.mInner.valueType == 4) {
            return this.mInner.longValue;
        }
        Log.w(TAG, "Value type is " + getValueTypeAsString() + ", not long.");
        return 0;
    }

    public boolean getBooleanValue() {
        if (this.mInner.valueType == 5) {
            return this.mInner.boolValue;
        }
        Log.w(TAG, "Value type is " + getValueTypeAsString() + ", not boolean.");
        return false;
    }

    public float getFloatValue() {
        if (this.mInner.valueType == 6) {
            return this.mInner.floatValue;
        }
        Log.w(TAG, "Value type is " + getValueTypeAsString() + ", not float.");
        return 0.0f;
    }

    public List<StatsDimensionsValue> getTupleValueList() {
        if (this.mInner.valueType == 7) {
            int length = this.mInner.tupleValue == null ? 0 : this.mInner.tupleValue.length;
            ArrayList arrayList = new ArrayList(length);
            for (int i = 0; i < length; i++) {
                arrayList.add(new StatsDimensionsValue(this.mInner.tupleValue[i]));
            }
            return arrayList;
        }
        Log.w(TAG, "Value type is " + getValueTypeAsString() + ", not tuple.");
        return null;
    }

    public int getValueType() {
        return this.mInner.valueType;
    }

    public boolean isValueType(int i) {
        return this.mInner.valueType == i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mInner.field);
        sb.append(":");
        switch (this.mInner.valueType) {
            case 2:
                sb.append(this.mInner.stringValue);
                break;
            case 3:
                sb.append(String.valueOf(this.mInner.intValue));
                break;
            case 4:
                sb.append(String.valueOf(this.mInner.longValue));
                break;
            case 5:
                sb.append(String.valueOf(this.mInner.boolValue));
                break;
            case 6:
                sb.append(String.valueOf(this.mInner.floatValue));
                break;
            case 7:
                sb.append("{");
                int length = this.mInner.tupleValue == null ? 0 : this.mInner.tupleValue.length;
                for (int i = 0; i < length; i++) {
                    sb.append(new StatsDimensionsValue(this.mInner.tupleValue[i]).toString());
                    sb.append("|");
                }
                sb.append("}");
                break;
            default:
                Log.w(TAG, "Incorrect value type");
                break;
        }
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        this.mInner.writeToParcel(parcel, i);
    }

    private String getValueTypeAsString() {
        switch (this.mInner.valueType) {
            case 2:
                return "string";
            case 3:
                return "int";
            case 4:
                return "long";
            case 5:
                return "boolean";
            case 6:
                return "float";
            case 7:
                return "tuple";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }
}
