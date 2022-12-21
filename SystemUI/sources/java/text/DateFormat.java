package java.text;

import java.p026io.InvalidObjectException;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.TimeZone;
import libcore.icu.ICU;

public abstract class DateFormat extends Format {
    public static final int AM_PM_FIELD = 14;
    public static final int DATE_FIELD = 3;
    public static final int DAY_OF_WEEK_FIELD = 9;
    public static final int DAY_OF_WEEK_IN_MONTH_FIELD = 11;
    public static final int DAY_OF_YEAR_FIELD = 10;
    public static final int DEFAULT = 2;
    public static final int ERA_FIELD = 0;
    public static final int FULL = 0;
    public static final int HOUR0_FIELD = 16;
    public static final int HOUR1_FIELD = 15;
    public static final int HOUR_OF_DAY0_FIELD = 5;
    public static final int HOUR_OF_DAY1_FIELD = 4;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int MILLISECOND_FIELD = 8;
    public static final int MINUTE_FIELD = 6;
    public static final int MONTH_FIELD = 2;
    public static final int SECOND_FIELD = 7;
    public static final int SHORT = 3;
    public static final int TIMEZONE_FIELD = 17;
    public static final int WEEK_OF_MONTH_FIELD = 13;
    public static final int WEEK_OF_YEAR_FIELD = 12;
    public static final int YEAR_FIELD = 1;
    public static Boolean is24Hour = null;
    private static final long serialVersionUID = 7218322306649953788L;
    protected Calendar calendar;
    protected NumberFormat numberFormat;

    public abstract StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Date parse(String str, ParsePosition parsePosition);

    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if (obj instanceof Date) {
            return format((Date) obj, stringBuffer, fieldPosition);
        }
        if (obj instanceof Number) {
            return format(new Date(((Number) obj).longValue()), stringBuffer, fieldPosition);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Date");
    }

    public final String format(Date date) {
        return format(date, new StringBuffer(), DontCareFieldPosition.INSTANCE).toString();
    }

    public Date parse(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Date parse = parse(str, parsePosition);
        if (parsePosition.index != 0) {
            return parse;
        }
        throw new ParseException("Unparseable date: \"" + str + "\"", parsePosition.errorIndex);
    }

    public Object parseObject(String str, ParsePosition parsePosition) {
        return parse(str, parsePosition);
    }

    public static final DateFormat getTimeInstance() {
        return get(2, 0, 1, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getTimeInstance(int i) {
        return get(i, 0, 1, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getTimeInstance(int i, Locale locale) {
        return get(i, 0, 1, locale);
    }

    public static final DateFormat getDateInstance() {
        return get(0, 2, 2, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateInstance(int i) {
        return get(0, i, 2, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateInstance(int i, Locale locale) {
        return get(0, i, 2, locale);
    }

    public static final DateFormat getDateTimeInstance() {
        return get(2, 2, 3, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateTimeInstance(int i, int i2) {
        return get(i2, i, 3, Locale.getDefault(Locale.Category.FORMAT));
    }

    public static final DateFormat getDateTimeInstance(int i, int i2, Locale locale) {
        return get(i2, i, 3, locale);
    }

    public static final DateFormat getInstance() {
        return getDateTimeInstance(3, 3);
    }

    public static final void set24HourTimePref(Boolean bool) {
        is24Hour = bool;
    }

    public static Locale[] getAvailableLocales() {
        return ICU.getAvailableLocales();
    }

    public void setCalendar(Calendar calendar2) {
        this.calendar = calendar2;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void setNumberFormat(NumberFormat numberFormat2) {
        this.numberFormat = numberFormat2;
    }

    public NumberFormat getNumberFormat() {
        return this.numberFormat;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.calendar.setTimeZone(timeZone);
    }

    public TimeZone getTimeZone() {
        return this.calendar.getTimeZone();
    }

    public void setLenient(boolean z) {
        this.calendar.setLenient(z);
    }

    public boolean isLenient() {
        return this.calendar.isLenient();
    }

    public int hashCode() {
        return this.numberFormat.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DateFormat dateFormat = (DateFormat) obj;
        if (this.calendar.getFirstDayOfWeek() == dateFormat.calendar.getFirstDayOfWeek() && this.calendar.getMinimalDaysInFirstWeek() == dateFormat.calendar.getMinimalDaysInFirstWeek() && this.calendar.isLenient() == dateFormat.calendar.isLenient() && this.calendar.getTimeZone().equals(dateFormat.calendar.getTimeZone()) && this.numberFormat.equals(dateFormat.numberFormat)) {
            return true;
        }
        return false;
    }

    public Object clone() {
        DateFormat dateFormat = (DateFormat) super.clone();
        dateFormat.calendar = (Calendar) this.calendar.clone();
        dateFormat.numberFormat = (NumberFormat) this.numberFormat.clone();
        return dateFormat;
    }

    private static DateFormat get(int i, int i2, int i3, Locale locale) {
        if ((i3 & 1) == 0) {
            i = -1;
        } else if (i < 0 || i > 3) {
            throw new IllegalArgumentException("Illegal time style " + i);
        }
        if ((i3 & 2) == 0) {
            i2 = -1;
        } else if (i2 < 0 || i2 > 3) {
            throw new IllegalArgumentException("Illegal date style " + i2);
        }
        try {
            return new SimpleDateFormat(i, i2, locale);
        } catch (MissingResourceException unused) {
            return new SimpleDateFormat("M/d/yy h:mm a");
        }
    }

    protected DateFormat() {
    }

    public static class Field extends Format.Field {
        public static final Field AM_PM = new Field("am pm", 9);
        public static final Field DAY_OF_MONTH = new Field("day of month", 5);
        public static final Field DAY_OF_WEEK = new Field("day of week", 7);
        public static final Field DAY_OF_WEEK_IN_MONTH = new Field("day of week in month", 8);
        public static final Field DAY_OF_YEAR = new Field("day of year", 6);
        public static final Field ERA = new Field("era", 0);
        public static final Field HOUR0 = new Field("hour", 10);
        public static final Field HOUR1 = new Field("hour 1", -1);
        public static final Field HOUR_OF_DAY0 = new Field("hour of day", 11);
        public static final Field HOUR_OF_DAY1 = new Field("hour of day 1", -1);
        public static final Field MILLISECOND = new Field("millisecond", 14);
        public static final Field MINUTE = new Field("minute", 12);
        public static final Field MONTH = new Field("month", 2);
        public static final Field SECOND = new Field("second", 13);
        public static final Field TIME_ZONE = new Field("time zone", -1);
        public static final Field WEEK_OF_MONTH = new Field("week of month", 4);
        public static final Field WEEK_OF_YEAR = new Field("week of year", 3);
        public static final Field YEAR = new Field("year", 1);
        private static final Field[] calendarToFieldMapping = new Field[17];
        private static final Map<String, Field> instanceMap = new HashMap(18);
        private static final long serialVersionUID = 7441350119349544720L;
        private int calendarField;

        public static Field ofCalendarField(int i) {
            if (i >= 0) {
                Field[] fieldArr = calendarToFieldMapping;
                if (i < fieldArr.length) {
                    return fieldArr[i];
                }
            }
            throw new IllegalArgumentException("Unknown Calendar constant " + i);
        }

        protected Field(String str, int i) {
            super(str);
            this.calendarField = i;
            if (getClass() == Field.class) {
                instanceMap.put(str, this);
                if (i >= 0) {
                    calendarToFieldMapping[i] = this;
                }
            }
        }

        public int getCalendarField() {
            return this.calendarField;
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws InvalidObjectException {
            if (getClass() == Field.class) {
                Field field = instanceMap.get(getName());
                if (field != null) {
                    return field;
                }
                throw new InvalidObjectException("unknown attribute name");
            }
            throw new InvalidObjectException("subclass didn't correctly implement readResolve");
        }
    }
}
