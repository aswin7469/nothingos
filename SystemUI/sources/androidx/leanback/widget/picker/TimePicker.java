package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$styleable;
import androidx.leanback.widget.picker.PickerUtility;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
/* loaded from: classes.dex */
public class TimePicker extends Picker {
    PickerColumn mAmPmColumn;
    int mColAmPmIndex;
    int mColHourIndex;
    int mColMinuteIndex;
    private final PickerUtility.TimeConstant mConstant;
    private int mCurrentAmPmIndex;
    private int mCurrentHour;
    private int mCurrentMinute;
    PickerColumn mHourColumn;
    private boolean mIs24hFormat;
    PickerColumn mMinuteColumn;
    private String mTimePickerFormat;

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R$attr.timePickerStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PickerUtility.TimeConstant timeConstantInstance = PickerUtility.getTimeConstantInstance(Locale.getDefault(), context.getResources());
        this.mConstant = timeConstantInstance;
        int[] iArr = R$styleable.lbTimePicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, 0, 0);
        try {
            this.mIs24hFormat = obtainStyledAttributes.getBoolean(R$styleable.lbTimePicker_is24HourFormat, DateFormat.is24HourFormat(context));
            boolean z = obtainStyledAttributes.getBoolean(R$styleable.lbTimePicker_useCurrentTime, true);
            obtainStyledAttributes.recycle();
            updateColumns();
            updateColumnsRange();
            if (!z) {
                return;
            }
            Calendar calendarForLocale = PickerUtility.getCalendarForLocale(null, timeConstantInstance.locale);
            setHour(calendarForLocale.get(11));
            setMinute(calendarForLocale.get(12));
            setAmPmValue();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private static void updateMin(PickerColumn column, int value) {
        if (value != column.getMinValue()) {
            column.setMinValue(value);
        }
    }

    private static void updateMax(PickerColumn column, int value) {
        if (value != column.getMaxValue()) {
            column.setMaxValue(value);
        }
    }

    String getBestHourMinutePattern() {
        String str;
        if (PickerUtility.SUPPORTS_BEST_DATE_TIME_PATTERN) {
            str = DateFormat.getBestDateTimePattern(this.mConstant.locale, this.mIs24hFormat ? "Hma" : "hma");
        } else {
            java.text.DateFormat timeInstance = SimpleDateFormat.getTimeInstance(3, this.mConstant.locale);
            if (timeInstance instanceof SimpleDateFormat) {
                String replace = ((SimpleDateFormat) timeInstance).toPattern().replace("s", "");
                str = this.mIs24hFormat ? replace.replace('h', 'H').replace("a", "") : replace;
            } else {
                str = this.mIs24hFormat ? "H:mma" : "h:mma";
            }
        }
        return TextUtils.isEmpty(str) ? "h:mma" : str;
    }

    List<CharSequence> extractSeparators() {
        String bestHourMinutePattern = getBestHourMinutePattern();
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        char[] cArr = {'H', 'h', 'K', 'k', 'm', 'M', 'a'};
        boolean z = false;
        char c = 0;
        for (int i = 0; i < bestHourMinutePattern.length(); i++) {
            char charAt = bestHourMinutePattern.charAt(i);
            if (charAt != ' ') {
                if (charAt != '\'') {
                    if (z) {
                        sb.append(charAt);
                    } else if (!isAnyOf(charAt, cArr)) {
                        sb.append(charAt);
                    } else if (charAt != c) {
                        arrayList.add(sb.toString());
                        sb.setLength(0);
                    }
                    c = charAt;
                } else if (!z) {
                    sb.setLength(0);
                    z = true;
                } else {
                    z = false;
                }
            }
        }
        arrayList.add(sb.toString());
        return arrayList;
    }

    private static boolean isAnyOf(char c, char[] any) {
        for (char c2 : any) {
            if (c == c2) {
                return true;
            }
        }
        return false;
    }

    private String extractTimeFields() {
        StringBuilder sb;
        String bestHourMinutePattern = getBestHourMinutePattern();
        boolean z = false;
        boolean z2 = TextUtils.getLayoutDirectionFromLocale(this.mConstant.locale) == 1;
        if (bestHourMinutePattern.indexOf(97) < 0 || bestHourMinutePattern.indexOf("a") > bestHourMinutePattern.indexOf("m")) {
            z = true;
        }
        String str = z2 ? "mh" : "hm";
        if (is24Hour()) {
            return str;
        }
        if (z) {
            sb = new StringBuilder();
            sb.append(str);
            sb.append("a");
        } else {
            sb = new StringBuilder();
            sb.append("a");
            sb.append(str);
        }
        return sb.toString();
    }

    private void updateColumns() {
        String bestHourMinutePattern = getBestHourMinutePattern();
        if (TextUtils.equals(bestHourMinutePattern, this.mTimePickerFormat)) {
            return;
        }
        this.mTimePickerFormat = bestHourMinutePattern;
        String extractTimeFields = extractTimeFields();
        List<CharSequence> extractSeparators = extractSeparators();
        if (extractSeparators.size() != extractTimeFields.length() + 1) {
            throw new IllegalStateException("Separators size: " + extractSeparators.size() + " must equal the size of timeFieldsPattern: " + extractTimeFields.length() + " + 1");
        }
        setSeparators(extractSeparators);
        String upperCase = extractTimeFields.toUpperCase(this.mConstant.locale);
        this.mAmPmColumn = null;
        this.mMinuteColumn = null;
        this.mHourColumn = null;
        this.mColAmPmIndex = -1;
        this.mColMinuteIndex = -1;
        this.mColHourIndex = -1;
        ArrayList arrayList = new ArrayList(3);
        for (int i = 0; i < upperCase.length(); i++) {
            char charAt = upperCase.charAt(i);
            if (charAt == 'A') {
                PickerColumn pickerColumn = new PickerColumn();
                this.mAmPmColumn = pickerColumn;
                arrayList.add(pickerColumn);
                this.mAmPmColumn.setStaticLabels(this.mConstant.ampm);
                this.mColAmPmIndex = i;
                updateMin(this.mAmPmColumn, 0);
                updateMax(this.mAmPmColumn, 1);
            } else if (charAt == 'H') {
                PickerColumn pickerColumn2 = new PickerColumn();
                this.mHourColumn = pickerColumn2;
                arrayList.add(pickerColumn2);
                this.mHourColumn.setStaticLabels(this.mConstant.hours24);
                this.mColHourIndex = i;
            } else if (charAt == 'M') {
                PickerColumn pickerColumn3 = new PickerColumn();
                this.mMinuteColumn = pickerColumn3;
                arrayList.add(pickerColumn3);
                this.mMinuteColumn.setStaticLabels(this.mConstant.minutes);
                this.mColMinuteIndex = i;
            } else {
                throw new IllegalArgumentException("Invalid time picker format.");
            }
        }
        setColumns(arrayList);
    }

    private void updateColumnsRange() {
        updateMin(this.mHourColumn, !this.mIs24hFormat ? 1 : 0);
        updateMax(this.mHourColumn, this.mIs24hFormat ? 23 : 12);
        updateMin(this.mMinuteColumn, 0);
        updateMax(this.mMinuteColumn, 59);
        PickerColumn pickerColumn = this.mAmPmColumn;
        if (pickerColumn != null) {
            updateMin(pickerColumn, 0);
            updateMax(this.mAmPmColumn, 1);
        }
    }

    private void setAmPmValue() {
        if (!is24Hour()) {
            setColumnValue(this.mColAmPmIndex, this.mCurrentAmPmIndex, false);
        }
    }

    public void setHour(int hour) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("hour: " + hour + " is not in [0-23] range in");
        }
        this.mCurrentHour = hour;
        if (!is24Hour()) {
            int i = this.mCurrentHour;
            if (i >= 12) {
                this.mCurrentAmPmIndex = 1;
                if (i > 12) {
                    this.mCurrentHour = i - 12;
                }
            } else {
                this.mCurrentAmPmIndex = 0;
                if (i == 0) {
                    this.mCurrentHour = 12;
                }
            }
            setAmPmValue();
        }
        setColumnValue(this.mColHourIndex, this.mCurrentHour, false);
    }

    public void setMinute(int minute) {
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("minute: " + minute + " is not in [0-59] range.");
        }
        this.mCurrentMinute = minute;
        setColumnValue(this.mColMinuteIndex, minute, false);
    }

    public boolean is24Hour() {
        return this.mIs24hFormat;
    }

    @Override // androidx.leanback.widget.picker.Picker
    public void onColumnValueChanged(int columnIndex, int newValue) {
        if (columnIndex == this.mColHourIndex) {
            this.mCurrentHour = newValue;
        } else if (columnIndex == this.mColMinuteIndex) {
            this.mCurrentMinute = newValue;
        } else if (columnIndex == this.mColAmPmIndex) {
            this.mCurrentAmPmIndex = newValue;
        } else {
            throw new IllegalArgumentException("Invalid column index.");
        }
    }
}
