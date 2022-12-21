package androidx.databinding.adapters;

import android.widget.TimePicker;
import androidx.databinding.InverseBindingListener;

public class TimePickerBindingAdapter {
    public static void setHour(TimePicker timePicker, int i) {
        if (timePicker.getHour() != i) {
            timePicker.setHour(i);
        }
    }

    public static void setMinute(TimePicker timePicker, int i) {
        if (timePicker.getMinute() != i) {
            timePicker.setMinute(i);
        }
    }

    public static int getHour(TimePicker timePicker) {
        return timePicker.getHour();
    }

    public static int getMinute(TimePicker timePicker) {
        return timePicker.getMinute();
    }

    public static void setListeners(TimePicker timePicker, final TimePicker.OnTimeChangedListener onTimeChangedListener, final InverseBindingListener inverseBindingListener, final InverseBindingListener inverseBindingListener2) {
        if (inverseBindingListener == null && inverseBindingListener2 == null) {
            timePicker.setOnTimeChangedListener(onTimeChangedListener);
        } else {
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                public void onTimeChanged(TimePicker timePicker, int i, int i2) {
                    TimePicker.OnTimeChangedListener onTimeChangedListener = onTimeChangedListener;
                    if (onTimeChangedListener != null) {
                        onTimeChangedListener.onTimeChanged(timePicker, i, i2);
                    }
                    InverseBindingListener inverseBindingListener = inverseBindingListener;
                    if (inverseBindingListener != null) {
                        inverseBindingListener.onChange();
                    }
                    InverseBindingListener inverseBindingListener2 = inverseBindingListener2;
                    if (inverseBindingListener2 != null) {
                        inverseBindingListener2.onChange();
                    }
                }
            });
        }
    }
}
