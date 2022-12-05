package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$styleable;
import androidx.leanback.widget.picker.PickerUtility;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
/* loaded from: classes.dex */
public class DatePicker extends Picker {
    private static final int[] DATE_FIELDS = {5, 2, 1};
    private int mColDayIndex;
    private int mColMonthIndex;
    private int mColYearIndex;
    private PickerUtility.DateConstant mConstant;
    private Calendar mCurrentDate;
    private final DateFormat mDateFormat;
    private String mDatePickerFormat;
    private PickerColumn mDayColumn;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private PickerColumn mMonthColumn;
    private Calendar mTempDate;
    private PickerColumn mYearColumn;

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, R$attr.datePickerStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        updateCurrentLocale();
        int[] iArr = R$styleable.lbDatePicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, iArr);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attrs, obtainStyledAttributes, 0, 0);
        try {
            String string = obtainStyledAttributes.getString(R$styleable.lbDatePicker_android_minDate);
            String string2 = obtainStyledAttributes.getString(R$styleable.lbDatePicker_android_maxDate);
            String string3 = obtainStyledAttributes.getString(R$styleable.lbDatePicker_datePickerFormat);
            obtainStyledAttributes.recycle();
            this.mTempDate.clear();
            if (!TextUtils.isEmpty(string)) {
                if (!parseDate(string, this.mTempDate)) {
                    this.mTempDate.set(1900, 0, 1);
                }
            } else {
                this.mTempDate.set(1900, 0, 1);
            }
            this.mMinDate.setTimeInMillis(this.mTempDate.getTimeInMillis());
            this.mTempDate.clear();
            if (!TextUtils.isEmpty(string2)) {
                if (!parseDate(string2, this.mTempDate)) {
                    this.mTempDate.set(2100, 0, 1);
                }
            } else {
                this.mTempDate.set(2100, 0, 1);
            }
            this.mMaxDate.setTimeInMillis(this.mTempDate.getTimeInMillis());
            setDatePickerFormat(TextUtils.isEmpty(string3) ? new String(android.text.format.DateFormat.getDateFormatOrder(context)) : string3);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private boolean parseDate(String date, Calendar outDate) {
        try {
            outDate.setTime(this.mDateFormat.parse(date));
            return true;
        } catch (ParseException unused) {
            Log.w("DatePicker", "Date: " + date + " not in format: MM/dd/yyyy");
            return false;
        }
    }

    String getBestYearMonthDayPattern(String datePickerFormat) {
        String localizedPattern;
        if (PickerUtility.SUPPORTS_BEST_DATE_TIME_PATTERN) {
            localizedPattern = android.text.format.DateFormat.getBestDateTimePattern(this.mConstant.locale, datePickerFormat);
        } else {
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            localizedPattern = dateFormat instanceof SimpleDateFormat ? ((SimpleDateFormat) dateFormat).toLocalizedPattern() : "MM/dd/yyyy";
        }
        return TextUtils.isEmpty(localizedPattern) ? "MM/dd/yyyy" : localizedPattern;
    }

    List<CharSequence> extractSeparators() {
        String bestYearMonthDayPattern = getBestYearMonthDayPattern(this.mDatePickerFormat);
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        char[] cArr = {'Y', 'y', 'M', 'm', 'D', 'd'};
        boolean z = false;
        char c = 0;
        for (int i = 0; i < bestYearMonthDayPattern.length(); i++) {
            char charAt = bestYearMonthDayPattern.charAt(i);
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

    public void setDatePickerFormat(String datePickerFormat) {
        if (TextUtils.isEmpty(datePickerFormat)) {
            datePickerFormat = new String(android.text.format.DateFormat.getDateFormatOrder(getContext()));
        }
        if (TextUtils.equals(this.mDatePickerFormat, datePickerFormat)) {
            return;
        }
        this.mDatePickerFormat = datePickerFormat;
        List<CharSequence> extractSeparators = extractSeparators();
        if (extractSeparators.size() != datePickerFormat.length() + 1) {
            throw new IllegalStateException("Separators size: " + extractSeparators.size() + " must equal the size of datePickerFormat: " + datePickerFormat.length() + " + 1");
        }
        setSeparators(extractSeparators);
        this.mDayColumn = null;
        this.mMonthColumn = null;
        this.mYearColumn = null;
        this.mColMonthIndex = -1;
        this.mColDayIndex = -1;
        this.mColYearIndex = -1;
        String upperCase = datePickerFormat.toUpperCase(this.mConstant.locale);
        ArrayList arrayList = new ArrayList(3);
        for (int i = 0; i < upperCase.length(); i++) {
            char charAt = upperCase.charAt(i);
            if (charAt != 'D') {
                if (charAt != 'M') {
                    if (charAt == 'Y') {
                        if (this.mYearColumn != null) {
                            throw new IllegalArgumentException("datePicker format error");
                        }
                        PickerColumn pickerColumn = new PickerColumn();
                        this.mYearColumn = pickerColumn;
                        arrayList.add(pickerColumn);
                        this.mColYearIndex = i;
                        this.mYearColumn.setLabelFormat("%d");
                    } else {
                        throw new IllegalArgumentException("datePicker format error");
                    }
                } else if (this.mMonthColumn != null) {
                    throw new IllegalArgumentException("datePicker format error");
                } else {
                    PickerColumn pickerColumn2 = new PickerColumn();
                    this.mMonthColumn = pickerColumn2;
                    arrayList.add(pickerColumn2);
                    this.mMonthColumn.setStaticLabels(this.mConstant.months);
                    this.mColMonthIndex = i;
                }
            } else if (this.mDayColumn != null) {
                throw new IllegalArgumentException("datePicker format error");
            } else {
                PickerColumn pickerColumn3 = new PickerColumn();
                this.mDayColumn = pickerColumn3;
                arrayList.add(pickerColumn3);
                this.mDayColumn.setLabelFormat("%02d");
                this.mColDayIndex = i;
            }
        }
        setColumns(arrayList);
        updateSpinners(false);
    }

    private void updateCurrentLocale() {
        PickerUtility.DateConstant dateConstantInstance = PickerUtility.getDateConstantInstance(Locale.getDefault(), getContext().getResources());
        this.mConstant = dateConstantInstance;
        this.mTempDate = PickerUtility.getCalendarForLocale(this.mTempDate, dateConstantInstance.locale);
        this.mMinDate = PickerUtility.getCalendarForLocale(this.mMinDate, this.mConstant.locale);
        this.mMaxDate = PickerUtility.getCalendarForLocale(this.mMaxDate, this.mConstant.locale);
        this.mCurrentDate = PickerUtility.getCalendarForLocale(this.mCurrentDate, this.mConstant.locale);
        PickerColumn pickerColumn = this.mMonthColumn;
        if (pickerColumn != null) {
            pickerColumn.setStaticLabels(this.mConstant.months);
            setColumnAt(this.mColMonthIndex, this.mMonthColumn);
        }
    }

    @Override // androidx.leanback.widget.picker.Picker
    public final void onColumnValueChanged(int column, int newVal) {
        this.mTempDate.setTimeInMillis(this.mCurrentDate.getTimeInMillis());
        int currentValue = getColumnAt(column).getCurrentValue();
        if (column == this.mColDayIndex) {
            this.mTempDate.add(5, newVal - currentValue);
        } else if (column == this.mColMonthIndex) {
            this.mTempDate.add(2, newVal - currentValue);
        } else if (column == this.mColYearIndex) {
            this.mTempDate.add(1, newVal - currentValue);
        } else {
            throw new IllegalArgumentException();
        }
        setDate(this.mTempDate.get(1), this.mTempDate.get(2), this.mTempDate.get(5));
    }

    private void setDate(int year, int month, int dayOfMonth) {
        setDate(year, month, dayOfMonth, false);
    }

    public void setDate(int year, int month, int dayOfMonth, boolean animation) {
        if (!isNewDate(year, month, dayOfMonth)) {
            return;
        }
        this.mCurrentDate.set(year, month, dayOfMonth);
        if (this.mCurrentDate.before(this.mMinDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
        } else if (this.mCurrentDate.after(this.mMaxDate)) {
            this.mCurrentDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
        updateSpinners(animation);
    }

    private boolean isNewDate(int year, int month, int dayOfMonth) {
        return (this.mCurrentDate.get(1) == year && this.mCurrentDate.get(2) == dayOfMonth && this.mCurrentDate.get(5) == month) ? false : true;
    }

    private static boolean updateMin(PickerColumn column, int value) {
        if (value != column.getMinValue()) {
            column.setMinValue(value);
            return true;
        }
        return false;
    }

    private static boolean updateMax(PickerColumn column, int value) {
        if (value != column.getMaxValue()) {
            column.setMaxValue(value);
            return true;
        }
        return false;
    }

    void updateSpinnersImpl(boolean animation) {
        boolean updateMin;
        boolean updateMax;
        int[] iArr = {this.mColDayIndex, this.mColMonthIndex, this.mColYearIndex};
        boolean z = true;
        boolean z2 = true;
        for (int length = DATE_FIELDS.length - 1; length >= 0; length--) {
            if (iArr[length] >= 0) {
                int i = DATE_FIELDS[length];
                PickerColumn columnAt = getColumnAt(iArr[length]);
                if (z) {
                    updateMin = updateMin(columnAt, this.mMinDate.get(i));
                } else {
                    updateMin = updateMin(columnAt, this.mCurrentDate.getActualMinimum(i));
                }
                boolean z3 = updateMin | false;
                if (z2) {
                    updateMax = updateMax(columnAt, this.mMaxDate.get(i));
                } else {
                    updateMax = updateMax(columnAt, this.mCurrentDate.getActualMaximum(i));
                }
                boolean z4 = z3 | updateMax;
                z &= this.mCurrentDate.get(i) == this.mMinDate.get(i);
                z2 &= this.mCurrentDate.get(i) == this.mMaxDate.get(i);
                if (z4) {
                    setColumnAt(iArr[length], columnAt);
                }
                setColumnValue(iArr[length], this.mCurrentDate.get(i), animation);
            }
        }
    }

    private void updateSpinners(final boolean animation) {
        post(new Runnable() { // from class: androidx.leanback.widget.picker.DatePicker.1
            @Override // java.lang.Runnable
            public void run() {
                DatePicker.this.updateSpinnersImpl(animation);
            }
        });
    }
}
