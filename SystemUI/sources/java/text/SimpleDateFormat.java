package java.text;

import android.icu.text.TimeZoneNames;
import android.icu.util.ULocale;
import com.android.icu.text.ExtendedTimeZoneNames;
import java.text.DateFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import libcore.icu.SimpleDateFormatData;

public class SimpleDateFormat extends DateFormat {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String GMT = "GMT";
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int[] PATTERN_INDEX_TO_CALENDAR_FIELD = {0, 1, 2, 5, 11, 11, 12, 13, 14, 7, 6, 8, 3, 4, 9, 10, 10, 15, 15, 17, 1000, 15, 2, 7, 9, 9};
    private static final int[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 17, 1, 9, 17, 2, 9, 14, 14};
    private static final DateFormat.Field[] PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID = {DateFormat.Field.ERA, DateFormat.Field.YEAR, DateFormat.Field.MONTH, DateFormat.Field.DAY_OF_MONTH, DateFormat.Field.HOUR_OF_DAY1, DateFormat.Field.HOUR_OF_DAY0, DateFormat.Field.MINUTE, DateFormat.Field.SECOND, DateFormat.Field.MILLISECOND, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.DAY_OF_YEAR, DateFormat.Field.DAY_OF_WEEK_IN_MONTH, DateFormat.Field.WEEK_OF_YEAR, DateFormat.Field.WEEK_OF_MONTH, DateFormat.Field.AM_PM, DateFormat.Field.HOUR1, DateFormat.Field.HOUR0, DateFormat.Field.TIME_ZONE, DateFormat.Field.TIME_ZONE, DateFormat.Field.YEAR, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.TIME_ZONE, DateFormat.Field.MONTH, DateFormat.Field.DAY_OF_WEEK, DateFormat.Field.AM_PM, DateFormat.Field.AM_PM};
    private static final int[] REST_OF_STYLES = {Calendar.SHORT_STANDALONE, 2, Calendar.LONG_STANDALONE};
    private static final int TAG_QUOTE_ASCII_CHAR = 100;
    private static final int TAG_QUOTE_CHARS = 101;
    private static final ConcurrentMap<Locale, NumberFormat> cachedNumberFormatData = new ConcurrentHashMap(3);
    static final int currentSerialVersion = 1;
    static final long serialVersionUID = 4774881970558875024L;
    private transient char[] compiledPattern;
    private Date defaultCenturyStart;
    private transient int defaultCenturyStartYear;
    private DateFormatSymbols formatData;
    private transient boolean hasFollowingMinusSign;
    private Locale locale;
    private transient char minusSign;
    private transient NumberFormat originalNumberFormat;
    private transient String originalNumberPattern;
    private String pattern;
    private int serialVersionOnStream;
    private transient ExtendedTimeZoneNames timeZoneNames;
    transient boolean useDateFormatSymbols;
    private transient char zeroDigit;

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean shouldObeyCount(int i, int i2) {
        switch (i) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 19:
            case 20:
                return true;
            case 2:
            case 22:
                return i2 <= 2;
            default:
                return false;
        }
    }

    public SimpleDateFormat() {
        this(3, 3, Locale.getDefault(Locale.Category.FORMAT));
    }

    SimpleDateFormat(int i, int i2, Locale locale2) {
        this(getDateTimeFormat(i, i2, locale2), locale2);
    }

    private static String getDateTimeFormat(int i, int i2, Locale locale2) {
        SimpleDateFormatData instance = SimpleDateFormatData.getInstance(locale2);
        if (i >= 0 && i2 >= 0) {
            return MessageFormat.format("{0} {1}", instance.getDateFormat(i2), instance.getTimeFormat(i));
        } else if (i >= 0) {
            return instance.getTimeFormat(i);
        } else {
            if (i2 >= 0) {
                return instance.getDateFormat(i2);
            }
            throw new IllegalArgumentException("No date or time style specified");
        }
    }

    public SimpleDateFormat(String str) {
        this(str, Locale.getDefault(Locale.Category.FORMAT));
    }

    public SimpleDateFormat(String str, Locale locale2) {
        this.serialVersionOnStream = 1;
        this.minusSign = '-';
        this.hasFollowingMinusSign = false;
        if (str == null || locale2 == null) {
            throw null;
        }
        initializeCalendar(locale2);
        this.pattern = str;
        this.formatData = DateFormatSymbols.getInstanceRef(locale2);
        this.locale = locale2;
        initialize(locale2);
    }

    public SimpleDateFormat(String str, DateFormatSymbols dateFormatSymbols) {
        this.serialVersionOnStream = 1;
        this.minusSign = '-';
        this.hasFollowingMinusSign = false;
        if (str == null || dateFormatSymbols == null) {
            throw null;
        }
        this.pattern = str;
        this.formatData = (DateFormatSymbols) dateFormatSymbols.clone();
        Locale locale2 = Locale.getDefault(Locale.Category.FORMAT);
        this.locale = locale2;
        initializeCalendar(locale2);
        initialize(this.locale);
        this.useDateFormatSymbols = true;
    }

    private void initialize(Locale locale2) {
        this.compiledPattern = compile(this.pattern);
        ConcurrentMap<Locale, NumberFormat> concurrentMap = cachedNumberFormatData;
        this.numberFormat = concurrentMap.get(locale2);
        if (this.numberFormat == null) {
            this.numberFormat = NumberFormat.getIntegerInstance(locale2);
            this.numberFormat.setGroupingUsed(false);
            concurrentMap.putIfAbsent(locale2, this.numberFormat);
        }
        this.numberFormat = (NumberFormat) this.numberFormat.clone();
        initializeDefaultCentury();
    }

    private void initializeCalendar(Locale locale2) {
        if (this.calendar == null) {
            this.calendar = Calendar.getInstance(locale2);
        }
    }

    private char[] compile(String str) {
        int i;
        char charAt;
        String str2 = str;
        int length = str.length();
        StringBuilder sb = new StringBuilder(length * 2);
        int i2 = 0;
        StringBuilder sb2 = null;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = -1;
        while (i3 < length) {
            char charAt2 = str2.charAt(i3);
            if (charAt2 == '\'') {
                int i7 = i3 + 1;
                if (i7 < length && (charAt = str2.charAt(i7)) == '\'') {
                    if (i5 != 0) {
                        encode(i6, i5, sb);
                        i5 = i2;
                        i6 = -1;
                    }
                    if (i4 != 0) {
                        sb2.append(charAt);
                    } else {
                        sb.append((char) (charAt | 25600));
                    }
                    i3 = i7;
                } else if (i4 == 0) {
                    if (i5 != 0) {
                        encode(i6, i5, sb);
                        i5 = i2;
                        i6 = -1;
                    }
                    if (sb2 == null) {
                        sb2 = new StringBuilder(length);
                    } else {
                        sb2.setLength(i2);
                    }
                    i = 1;
                    i4 = 1;
                    i3 += i;
                    i2 = 0;
                } else {
                    int length2 = sb2.length();
                    if (length2 == 1) {
                        char charAt3 = sb2.charAt(i2);
                        if (charAt3 < 128) {
                            sb.append((char) (charAt3 | 25600));
                        } else {
                            sb.append(25857);
                            sb.append(charAt3);
                        }
                    } else {
                        encode(101, length2, sb);
                        sb.append((CharSequence) sb2);
                    }
                    i4 = i2;
                }
            } else if (i4 != 0) {
                sb2.append(charAt2);
            } else {
                if ((charAt2 < 'a' || charAt2 > 'z') && (charAt2 < 'A' || charAt2 > 'Z')) {
                    if (i5 != 0) {
                        encode(i6, i5, sb);
                        i6 = -1;
                        i5 = 0;
                    }
                    if (charAt2 < 128) {
                        sb.append((char) (charAt2 | 25600));
                    } else {
                        int i8 = i3 + 1;
                        while (i8 < length) {
                            char charAt4 = str2.charAt(i8);
                            if (charAt4 == '\'' || ((charAt4 >= 'a' && charAt4 <= 'z') || (charAt4 >= 'A' && charAt4 <= 'Z'))) {
                                break;
                            }
                            i8++;
                        }
                        sb.append((char) ((i8 - i3) | 25856));
                        while (i3 < i8) {
                            sb.append(str2.charAt(i3));
                            i3++;
                        }
                        i3--;
                    }
                } else {
                    int indexOf = "GyMdkHmsSEDFwWahKzZYuXLcbB".indexOf((int) charAt2);
                    if (indexOf == -1) {
                        throw new IllegalArgumentException("Illegal pattern character '" + charAt2 + "'");
                    } else if (i6 == -1 || i6 == indexOf) {
                        i5++;
                        i6 = indexOf;
                    } else {
                        encode(i6, i5, sb);
                        i6 = indexOf;
                        i = 1;
                        i5 = 1;
                        i3 += i;
                        i2 = 0;
                    }
                }
                i = 1;
                i3 += i;
                i2 = 0;
            }
            i = 1;
            i3 += i;
            i2 = 0;
        }
        if (i4 == 0) {
            if (i5 != 0) {
                encode(i6, i5, sb);
            }
            int length3 = sb.length();
            char[] cArr = new char[length3];
            sb.getChars(0, length3, cArr, 0);
            return cArr;
        }
        throw new IllegalArgumentException("Unterminated quote");
    }

    private static void encode(int i, int i2, StringBuilder sb) {
        if (i == 21 && i2 >= 4) {
            throw new IllegalArgumentException("invalid ISO 8601 format: length=" + i2);
        } else if (i2 < 255) {
            sb.append((char) ((i << 8) | i2));
        } else {
            sb.append((char) ((i << 8) | 255));
            sb.append((char) (i2 >>> 16));
            sb.append((char) (65535 & i2));
        }
    }

    private void initializeDefaultCentury() {
        this.calendar.setTimeInMillis(System.currentTimeMillis());
        this.calendar.add(1, -80);
        parseAmbiguousDatesAsAfter(this.calendar.getTime());
    }

    private void parseAmbiguousDatesAsAfter(Date date) {
        this.defaultCenturyStart = date;
        this.calendar.setTime(date);
        this.defaultCenturyStartYear = this.calendar.get(1);
    }

    public void set2DigitYearStart(Date date) {
        parseAmbiguousDatesAsAfter(new Date(date.getTime()));
    }

    public Date get2DigitYearStart() {
        return (Date) this.defaultCenturyStart.clone();
    }

    public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        fieldPosition.endIndex = 0;
        fieldPosition.beginIndex = 0;
        return format(date, stringBuffer, fieldPosition.getFieldDelegate());
    }

    private StringBuffer format(Date date, StringBuffer stringBuffer, Format.FieldDelegate fieldDelegate) {
        char c;
        this.calendar.setTime(date);
        boolean useDateFormatSymbols2 = useDateFormatSymbols();
        int i = 0;
        while (true) {
            char[] cArr = this.compiledPattern;
            if (i >= cArr.length) {
                return stringBuffer;
            }
            char c2 = cArr[i];
            int i2 = c2 >>> 8;
            i++;
            char c3 = c2 & 255;
            if (c3 == 255) {
                int i3 = i + 1;
                int i4 = i3 + 1;
                c = (cArr[i] << 16) | cArr[i3];
                i = i4;
            } else {
                c = c3;
            }
            if (i2 == 100) {
                stringBuffer.append((char) c);
            } else if (i2 != 101) {
                subFormat(i2, c, fieldDelegate, stringBuffer, useDateFormatSymbols2);
            } else {
                stringBuffer.append(cArr, i, (int) c);
                i += c;
            }
        }
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        StringBuffer stringBuffer = new StringBuffer();
        CharacterIteratorFieldDelegate characterIteratorFieldDelegate = new CharacterIteratorFieldDelegate();
        if (obj instanceof Date) {
            format((Date) obj, stringBuffer, (Format.FieldDelegate) characterIteratorFieldDelegate);
        } else if (obj instanceof Number) {
            format(new Date(((Number) obj).longValue()), stringBuffer, (Format.FieldDelegate) characterIteratorFieldDelegate);
        } else if (obj == null) {
            throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
        } else {
            throw new IllegalArgumentException("Cannot format given Object as a Date");
        }
        return characterIteratorFieldDelegate.getIterator(stringBuffer.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:113:0x020c, code lost:
        if (r6 == null) goto L_0x008a;
     */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x01ff  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0212  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x004d  */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0062  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void subFormat(int r17, int r18, java.text.Format.FieldDelegate r19, java.lang.StringBuffer r20, boolean r21) {
        /*
            r16 = this;
            r0 = r16
            r1 = r18
            r9 = r20
            r5 = r21
            int r10 = r20.length()
            int[] r2 = PATTERN_INDEX_TO_CALENDAR_FIELD
            r3 = r2[r17]
            r4 = 17
            r6 = 1000(0x3e8, float:1.401E-42)
            r7 = 1
            if (r3 != r4) goto L_0x0032
            java.util.Calendar r4 = r0.calendar
            boolean r4 = r4.isWeekDateSupported()
            if (r4 == 0) goto L_0x0026
            java.util.Calendar r2 = r0.calendar
            int r2 = r2.getWeekYear()
            goto L_0x0046
        L_0x0026:
            r2 = r2[r7]
            java.util.Calendar r3 = r0.calendar
            int r3 = r3.get(r2)
            r8 = r2
            r2 = r3
            r11 = r7
            goto L_0x0049
        L_0x0032:
            if (r3 != r6) goto L_0x0040
            java.util.Calendar r2 = r0.calendar
            r4 = 7
            int r2 = r2.get(r4)
            int r2 = java.text.CalendarBuilder.toISODayOfWeek(r2)
            goto L_0x0046
        L_0x0040:
            java.util.Calendar r2 = r0.calendar
            int r2 = r2.get(r3)
        L_0x0046:
            r11 = r17
            r8 = r3
        L_0x0049:
            r3 = 4
            r4 = 2
            if (r1 < r3) goto L_0x004f
            r12 = r4
            goto L_0x0050
        L_0x004f:
            r12 = r7
        L_0x0050:
            if (r5 != 0) goto L_0x005d
            if (r8 == r6) goto L_0x005d
            java.util.Calendar r6 = r0.calendar
            java.util.Locale r13 = r0.locale
            java.lang.String r6 = r6.getDisplayName(r8, r12, r13)
            goto L_0x005e
        L_0x005d:
            r6 = 0
        L_0x005e:
            java.lang.String r13 = ""
            if (r11 == 0) goto L_0x01ff
            r14 = 2147483647(0x7fffffff, float:NaN)
            if (r11 == r7) goto L_0x01e6
            if (r11 == r4) goto L_0x01d3
            if (r11 == r3) goto L_0x01be
            r15 = 8
            if (r11 == r15) goto L_0x01a8
            r15 = 9
            if (r11 == r15) goto L_0x019f
            r15 = 14
            if (r11 == r15) goto L_0x0193
            r15 = 15
            if (r11 == r15) goto L_0x017c
            r3 = 16
            switch(r11) {
                case 17: goto L_0x0109;
                case 18: goto L_0x00e9;
                case 19: goto L_0x01e6;
                default: goto L_0x0080;
            }
        L_0x0080:
            switch(r11) {
                case 21: goto L_0x00a9;
                case 22: goto L_0x0095;
                case 23: goto L_0x008d;
                case 24: goto L_0x008a;
                case 25: goto L_0x008a;
                default: goto L_0x0083;
            }
        L_0x0083:
            if (r6 != 0) goto L_0x0210
            r0.zeroPaddingNumber(r2, r1, r14, r9)
            goto L_0x0210
        L_0x008a:
            r6 = r13
            goto L_0x0210
        L_0x008d:
            if (r6 != 0) goto L_0x0210
            java.lang.String r6 = r0.formatWeekday(r1, r2, r5, r7)
            goto L_0x0210
        L_0x0095:
            r6 = 1
            r3 = 2147483647(0x7fffffff, float:NaN)
            r0 = r16
            r1 = r18
            r4 = r20
            r5 = r21
            r7 = r8
            r8 = r12
            java.lang.String r6 = r0.formatMonth(r1, r2, r3, r4, r5, r6, r7, r8)
            goto L_0x0210
        L_0x00a9:
            java.util.Calendar r2 = r0.calendar
            int r2 = r2.get(r15)
            java.util.Calendar r0 = r0.calendar
            int r0 = r0.get(r3)
            int r2 = r2 + r0
            if (r2 != 0) goto L_0x00bf
            r0 = 90
            r9.append((char) r0)
            goto L_0x0210
        L_0x00bf:
            r0 = 60000(0xea60, float:8.4078E-41)
            int r2 = r2 / r0
            if (r2 < 0) goto L_0x00cb
            r0 = 43
            r9.append((char) r0)
            goto L_0x00d1
        L_0x00cb:
            r0 = 45
            r9.append((char) r0)
            int r2 = -r2
        L_0x00d1:
            int r0 = r2 / 60
            sun.util.calendar.CalendarUtils.sprintf0d((java.lang.StringBuffer) r9, (int) r0, (int) r4)
            if (r1 != r7) goto L_0x00da
            goto L_0x0210
        L_0x00da:
            r0 = 3
            if (r1 != r0) goto L_0x00e2
            r0 = 58
            r9.append((char) r0)
        L_0x00e2:
            int r2 = r2 % 60
            sun.util.calendar.CalendarUtils.sprintf0d((java.lang.StringBuffer) r9, (int) r2, (int) r4)
            goto L_0x0210
        L_0x00e9:
            java.util.Calendar r2 = r0.calendar
            int r2 = r2.get(r15)
            java.util.Calendar r0 = r0.calendar
            int r0 = r0.get(r3)
            int r2 = r2 + r0
            r0 = 4
            if (r1 < r0) goto L_0x00fb
            r3 = r7
            goto L_0x00fc
        L_0x00fb:
            r3 = 0
        L_0x00fc:
            if (r1 != r0) goto L_0x00ff
            goto L_0x0100
        L_0x00ff:
            r7 = 0
        L_0x0100:
            java.lang.String r0 = java.util.TimeZone.createGmtOffsetString(r7, r3, r2)
            r9.append((java.lang.String) r0)
            goto L_0x0210
        L_0x0109:
            if (r6 != 0) goto L_0x0210
            java.util.Calendar r2 = r0.calendar
            java.util.TimeZone r2 = r2.getTimeZone()
            java.util.Calendar r4 = r0.calendar
            int r4 = r4.get(r3)
            if (r4 == 0) goto L_0x011b
            r4 = r7
            goto L_0x011c
        L_0x011b:
            r4 = 0
        L_0x011c:
            java.text.DateFormatSymbols r5 = r0.formatData
            boolean r5 = r5.isZoneStringsSet
            if (r5 == 0) goto L_0x0137
            r5 = 4
            if (r1 >= r5) goto L_0x0127
            r1 = 0
            goto L_0x0128
        L_0x0127:
            r1 = r7
        L_0x0128:
            java.text.DateFormatSymbols r5 = r0.formatData
            java.lang.String[][] r5 = r5.getZoneStringsWrapper()
            java.lang.String r2 = r2.getID()
            java.lang.String r1 = libcore.icu.TimeZoneNames.getDisplayName(r5, r2, r4, r1)
            goto L_0x015f
        L_0x0137:
            r5 = 4
            if (r1 >= r5) goto L_0x0142
            if (r4 == 0) goto L_0x013f
            android.icu.text.TimeZoneNames$NameType r1 = android.icu.text.TimeZoneNames.NameType.SHORT_DAYLIGHT
            goto L_0x0149
        L_0x013f:
            android.icu.text.TimeZoneNames$NameType r1 = android.icu.text.TimeZoneNames.NameType.SHORT_STANDARD
            goto L_0x0149
        L_0x0142:
            if (r4 == 0) goto L_0x0147
            android.icu.text.TimeZoneNames$NameType r1 = android.icu.text.TimeZoneNames.NameType.LONG_DAYLIGHT
            goto L_0x0149
        L_0x0147:
            android.icu.text.TimeZoneNames$NameType r1 = android.icu.text.TimeZoneNames.NameType.LONG_STANDARD
        L_0x0149:
            java.lang.String r2 = r2.getID()
            java.lang.String r2 = android.icu.util.TimeZone.getCanonicalID(r2)
            android.icu.text.TimeZoneNames r4 = r16.getTimeZoneNames()
            java.util.Calendar r5 = r0.calendar
            long r12 = r5.getTimeInMillis()
            java.lang.String r1 = r4.getDisplayName(r2, r1, r12)
        L_0x015f:
            if (r1 == 0) goto L_0x0166
            r9.append((java.lang.String) r1)
            goto L_0x0210
        L_0x0166:
            java.util.Calendar r1 = r0.calendar
            int r1 = r1.get(r15)
            java.util.Calendar r0 = r0.calendar
            int r0 = r0.get(r3)
            int r1 = r1 + r0
            java.lang.String r0 = java.util.TimeZone.createGmtOffsetString(r7, r7, r1)
            r9.append((java.lang.String) r0)
            goto L_0x0210
        L_0x017c:
            if (r6 != 0) goto L_0x0210
            if (r2 != 0) goto L_0x018e
            java.util.Calendar r2 = r0.calendar
            r3 = 10
            int r2 = r2.getLeastMaximum(r3)
            int r2 = r2 + r7
            r0.zeroPaddingNumber(r2, r1, r14, r9)
            goto L_0x0210
        L_0x018e:
            r0.zeroPaddingNumber(r2, r1, r14, r9)
            goto L_0x0210
        L_0x0193:
            if (r5 == 0) goto L_0x0210
            java.text.DateFormatSymbols r0 = r0.formatData
            java.lang.String[] r0 = r0.getAmPmStrings()
            r6 = r0[r2]
            goto L_0x0210
        L_0x019f:
            if (r6 != 0) goto L_0x0210
            r3 = 0
            java.lang.String r6 = r0.formatWeekday(r1, r2, r5, r3)
            goto L_0x0210
        L_0x01a8:
            if (r6 != 0) goto L_0x0210
            double r2 = (double) r2
            r4 = 4652007308841189376(0x408f400000000000, double:1000.0)
            double r2 = r2 / r4
            r4 = 4621819117588971520(0x4024000000000000, double:10.0)
            double r7 = (double) r1
            double r4 = java.lang.Math.pow(r4, r7)
            double r2 = r2 * r4
            int r2 = (int) r2
            r0.zeroPaddingNumber(r2, r1, r1, r9)
            goto L_0x0210
        L_0x01be:
            if (r6 != 0) goto L_0x0210
            if (r2 != 0) goto L_0x01cf
            java.util.Calendar r2 = r0.calendar
            r3 = 11
            int r2 = r2.getMaximum(r3)
            int r2 = r2 + r7
            r0.zeroPaddingNumber(r2, r1, r14, r9)
            goto L_0x0210
        L_0x01cf:
            r0.zeroPaddingNumber(r2, r1, r14, r9)
            goto L_0x0210
        L_0x01d3:
            r6 = 0
            r3 = 2147483647(0x7fffffff, float:NaN)
            r0 = r16
            r1 = r18
            r4 = r20
            r5 = r21
            r7 = r8
            r8 = r12
            java.lang.String r6 = r0.formatMonth(r1, r2, r3, r4, r5, r6, r7, r8)
            goto L_0x0210
        L_0x01e6:
            java.util.Calendar r3 = r0.calendar
            boolean r3 = r3 instanceof java.util.GregorianCalendar
            if (r3 == 0) goto L_0x01f6
            if (r1 == r4) goto L_0x01f2
            r0.zeroPaddingNumber(r2, r1, r14, r9)
            goto L_0x0210
        L_0x01f2:
            r0.zeroPaddingNumber(r2, r4, r4, r9)
            goto L_0x0210
        L_0x01f6:
            if (r6 != 0) goto L_0x0210
            if (r12 != r4) goto L_0x01fb
            r1 = r7
        L_0x01fb:
            r0.zeroPaddingNumber(r2, r1, r14, r9)
            goto L_0x0210
        L_0x01ff:
            if (r5 == 0) goto L_0x020c
            java.text.DateFormatSymbols r0 = r0.formatData
            java.lang.String[] r0 = r0.getEras()
            int r1 = r0.length
            if (r2 >= r1) goto L_0x020c
            r6 = r0[r2]
        L_0x020c:
            if (r6 != 0) goto L_0x0210
            goto L_0x008a
        L_0x0210:
            if (r6 == 0) goto L_0x0215
            r9.append((java.lang.String) r6)
        L_0x0215:
            int[] r0 = PATTERN_INDEX_TO_DATE_FORMAT_FIELD
            r1 = r0[r11]
            java.text.DateFormat$Field[] r0 = PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID
            r3 = r0[r11]
            int r5 = r20.length()
            r0 = r19
            r2 = r3
            r4 = r10
            r6 = r20
            r0.formatted(r1, r2, r3, r4, r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.subFormat(int, int, java.text.Format$FieldDelegate, java.lang.StringBuffer, boolean):void");
    }

    private String formatWeekday(int i, int i2, boolean z, boolean z2) {
        String[] strArr;
        if (!z) {
            return null;
        }
        if (i == 4) {
            DateFormatSymbols dateFormatSymbols = this.formatData;
            strArr = z2 ? dateFormatSymbols.getStandAloneWeekdays() : dateFormatSymbols.getWeekdays();
        } else if (i == 5) {
            DateFormatSymbols dateFormatSymbols2 = this.formatData;
            strArr = z2 ? dateFormatSymbols2.getTinyStandAloneWeekdays() : dateFormatSymbols2.getTinyWeekdays();
        } else {
            DateFormatSymbols dateFormatSymbols3 = this.formatData;
            strArr = z2 ? dateFormatSymbols3.getShortStandAloneWeekdays() : dateFormatSymbols3.getShortWeekdays();
        }
        return strArr[i2];
    }

    private String formatMonth(int i, int i2, int i3, StringBuffer stringBuffer, boolean z, boolean z2, int i4, int i5) {
        String[] strArr;
        String str = null;
        if (z) {
            if (i == 4) {
                DateFormatSymbols dateFormatSymbols = this.formatData;
                strArr = z2 ? dateFormatSymbols.getStandAloneMonths() : dateFormatSymbols.getMonths();
            } else if (i == 5) {
                DateFormatSymbols dateFormatSymbols2 = this.formatData;
                strArr = z2 ? dateFormatSymbols2.getTinyStandAloneMonths() : dateFormatSymbols2.getTinyMonths();
            } else if (i == 3) {
                DateFormatSymbols dateFormatSymbols3 = this.formatData;
                strArr = z2 ? dateFormatSymbols3.getShortStandAloneMonths() : dateFormatSymbols3.getShortMonths();
            } else {
                strArr = null;
            }
            if (strArr != null) {
                str = strArr[i2];
            }
        } else if (i >= 3) {
            if (z2) {
                i5 = Calendar.toStandaloneStyle(i5);
            }
            str = this.calendar.getDisplayName(i4, i5, this.locale);
        }
        if (str == null) {
            zeroPaddingNumber(i2 + 1, i, i3, stringBuffer);
        }
        return str;
    }

    private void zeroPaddingNumber(int i, int i2, int i3, StringBuffer stringBuffer) {
        try {
            if (this.zeroDigit == 0) {
                this.zeroDigit = ((DecimalFormat) this.numberFormat).getDecimalFormatSymbols().getZeroDigit();
            }
            if (i >= 0) {
                if (i >= 100 || i2 < 1 || i2 > 2) {
                    if (i >= 1000 && i < 10000) {
                        if (i2 == 4) {
                            stringBuffer.append((char) (this.zeroDigit + (i / 1000)));
                            int i4 = i % 1000;
                            stringBuffer.append((char) (this.zeroDigit + (i4 / 100)));
                            int i5 = i4 % 100;
                            stringBuffer.append((char) (this.zeroDigit + (i5 / 10)));
                            stringBuffer.append((char) (this.zeroDigit + (i5 % 10)));
                            return;
                        } else if (i2 == 2 && i3 == 2) {
                            zeroPaddingNumber(i % 100, 2, 2, stringBuffer);
                            return;
                        }
                    }
                } else if (i < 10) {
                    if (i2 == 2) {
                        stringBuffer.append(this.zeroDigit);
                    }
                    stringBuffer.append((char) (this.zeroDigit + i));
                    return;
                } else {
                    stringBuffer.append((char) (this.zeroDigit + (i / 10)));
                    stringBuffer.append((char) (this.zeroDigit + (i % 10)));
                    return;
                }
            }
        } catch (Exception unused) {
        }
        this.numberFormat.setMinimumIntegerDigits(i2);
        this.numberFormat.setMaximumIntegerDigits(i3);
        this.numberFormat.format((long) i, stringBuffer, DontCareFieldPosition.INSTANCE);
    }

    public Date parse(String str, ParsePosition parsePosition) {
        TimeZone timeZone = getTimeZone();
        try {
            return parseInternal(str, parsePosition);
        } finally {
            setTimeZone(timeZone);
        }
    }

    /* JADX WARNING: type inference failed for: r0v13, types: [int] */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00ad, code lost:
        r12.index = r13;
        r12.errorIndex = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b1, code lost:
        return null;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Date parseInternal(java.lang.String r21, java.text.ParsePosition r22) {
        /*
            r20 = this;
            r10 = r20
            r11 = r21
            r12 = r22
            r20.checkNegativeNumberExpression()
            int r13 = r12.index
            int r14 = r21.length()
            r15 = 1
            boolean[] r9 = new boolean[r15]
            r16 = 0
            r9[r16] = r16
            java.text.CalendarBuilder r8 = new java.text.CalendarBuilder
            r8.<init>()
            r2 = r13
            r0 = r16
        L_0x001e:
            char[] r1 = r10.compiledPattern
            int r3 = r1.length
            r17 = 0
            r4 = 100
            if (r0 >= r3) goto L_0x00d2
            char r3 = r1[r0]
            int r5 = r3 >>> 8
            int r0 = r0 + 1
            r6 = 255(0xff, float:3.57E-43)
            r3 = r3 & r6
            if (r3 != r6) goto L_0x003f
            int r3 = r0 + 1
            char r0 = r1[r0]
            int r0 = r0 << 16
            int r7 = r3 + 1
            char r3 = r1[r3]
            r0 = r0 | r3
            r3 = r0
            goto L_0x0040
        L_0x003f:
            r7 = r0
        L_0x0040:
            if (r5 == r4) goto L_0x00b4
            r0 = 101(0x65, float:1.42E-43)
            if (r5 == r0) goto L_0x008f
            int r15 = r1.length
            if (r7 >= r15) goto L_0x006d
            char r1 = r1[r7]
            int r15 = r1 >>> 8
            r1 = r1 & r6
            boolean r6 = r10.shouldObeyCount(r15, r1)
            boolean r0 = r10.hasFollowingMinusSign
            if (r0 == 0) goto L_0x006a
            if (r15 == r4) goto L_0x005c
            r0 = 101(0x65, float:1.42E-43)
            if (r15 != r0) goto L_0x006a
        L_0x005c:
            if (r15 == r4) goto L_0x0064
            char[] r0 = r10.compiledPattern
            int r1 = r7 + 1
            char r1 = r0[r1]
        L_0x0064:
            char r0 = r10.minusSign
            if (r1 != r0) goto L_0x006a
            r15 = 1
            goto L_0x0070
        L_0x006a:
            r15 = r16
            goto L_0x0070
        L_0x006d:
            r6 = r16
            r15 = r6
        L_0x0070:
            r0 = r20
            r1 = r21
            r4 = r3
            r3 = r5
            r5 = r6
            r6 = r9
            r18 = r7
            r7 = r22
            r19 = r8
            r8 = r15
            r15 = r9
            r9 = r19
            int r0 = r0.subParse(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            if (r0 >= 0) goto L_0x008b
            r12.index = r13
            return r17
        L_0x008b:
            r2 = r0
        L_0x008c:
            r0 = r18
            goto L_0x00c7
        L_0x008f:
            r4 = r3
            r18 = r7
            r19 = r8
            r15 = r9
        L_0x0095:
            int r0 = r3 + -1
            if (r3 <= 0) goto L_0x00b2
            if (r2 >= r14) goto L_0x00ad
            char r1 = r11.charAt(r2)
            char[] r3 = r10.compiledPattern
            int r4 = r7 + 1
            char r3 = r3[r7]
            if (r1 == r3) goto L_0x00a8
            goto L_0x00ad
        L_0x00a8:
            int r2 = r2 + 1
            r3 = r0
            r7 = r4
            goto L_0x0095
        L_0x00ad:
            r12.index = r13
            r12.errorIndex = r2
            return r17
        L_0x00b2:
            r0 = r7
            goto L_0x00c7
        L_0x00b4:
            r4 = r3
            r18 = r7
            r19 = r8
            r15 = r9
            if (r2 >= r14) goto L_0x00cd
            char r0 = r11.charAt(r2)
            char r1 = (char) r4
            if (r0 == r1) goto L_0x00c4
            goto L_0x00cd
        L_0x00c4:
            int r2 = r2 + 1
            goto L_0x008c
        L_0x00c7:
            r9 = r15
            r8 = r19
            r15 = 1
            goto L_0x001e
        L_0x00cd:
            r12.index = r13
            r12.errorIndex = r2
            return r17
        L_0x00d2:
            r19 = r8
            r15 = r9
            r12.index = r2
            java.util.Calendar r0 = r10.calendar     // Catch:{ IllegalArgumentException -> 0x00fe }
            r1 = r19
            java.util.Calendar r0 = r1.establish(r0)     // Catch:{ IllegalArgumentException -> 0x00fe }
            java.util.Date r0 = r0.getTime()     // Catch:{ IllegalArgumentException -> 0x00fe }
            boolean r3 = r15[r16]     // Catch:{ IllegalArgumentException -> 0x00fe }
            if (r3 == 0) goto L_0x00fd
            java.util.Date r3 = r10.defaultCenturyStart     // Catch:{ IllegalArgumentException -> 0x00fe }
            boolean r3 = r0.before(r3)     // Catch:{ IllegalArgumentException -> 0x00fe }
            if (r3 == 0) goto L_0x00fd
            java.text.CalendarBuilder r0 = r1.addYear(r4)     // Catch:{ IllegalArgumentException -> 0x00fe }
            java.util.Calendar r1 = r10.calendar     // Catch:{ IllegalArgumentException -> 0x00fe }
            java.util.Calendar r0 = r0.establish(r1)     // Catch:{ IllegalArgumentException -> 0x00fe }
            java.util.Date r0 = r0.getTime()     // Catch:{ IllegalArgumentException -> 0x00fe }
        L_0x00fd:
            return r0
        L_0x00fe:
            r12.errorIndex = r2
            r12.index = r13
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.parseInternal(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    private int matchString(String str, int i, int i2, String[] strArr, CalendarBuilder calendarBuilder) {
        int i3 = i;
        int i4 = i2;
        String[] strArr2 = strArr;
        int length = strArr2.length;
        int i5 = 0;
        int i6 = -1;
        for (int i7 = i4 == 7 ? 1 : 0; i7 < length; i7++) {
            int length2 = strArr2[i7].length();
            if (length2 > i5) {
                if (str.regionMatches(true, i, strArr2[i7], 0, length2)) {
                    i6 = i7;
                    i5 = length2;
                }
            }
            int i8 = length2 - 1;
            if (strArr2[i7].charAt(i8) == '.' && i8 > i5) {
                if (str.regionMatches(true, i, strArr2[i7], 0, i8)) {
                    i6 = i7;
                    i5 = i8;
                }
            }
        }
        if (i6 < 0) {
            return -i3;
        }
        calendarBuilder.set(i4, i6);
        return i3 + i5;
    }

    private int matchString(String str, int i, int i2, Map<String, Integer> map, CalendarBuilder calendarBuilder) {
        int length;
        if (map != null) {
            if (map instanceof NavigableMap) {
                NavigableMap navigableMap = (NavigableMap) map;
                if (navigableMap.comparator() == null) {
                    for (String str2 : navigableMap.descendingKeySet()) {
                        if (str.regionMatches(true, i, str2, 0, str2.length())) {
                            calendarBuilder.set(i2, map.get(str2).intValue());
                            length = str2.length();
                            return i + length;
                        }
                    }
                    return -i;
                }
            }
            String str3 = null;
            for (String next : map.keySet()) {
                int length2 = next.length();
                if ((str3 == null || length2 > str3.length()) && str.regionMatches(true, i, next, 0, length2)) {
                    str3 = next;
                }
            }
            if (str3 != null) {
                calendarBuilder.set(i2, map.get(str3).intValue());
                length = str3.length();
                return i + length;
            }
        }
        return -i;
    }

    private int matchZoneString(String str, int i, String[] strArr) {
        for (int i2 = 1; i2 <= 4; i2++) {
            String str2 = strArr[i2];
            if (str.regionMatches(true, i, str2, 0, str2.length())) {
                return i2;
            }
        }
        return -1;
    }

    private int subParseZoneString(String str, int i, CalendarBuilder calendarBuilder) {
        if (this.formatData.isZoneStringsSet) {
            return subParseZoneStringFromSymbols(str, i, calendarBuilder);
        }
        return subParseZoneStringFromICU(str, i, calendarBuilder);
    }

    private ExtendedTimeZoneNames getExtendedTimeZoneNames() {
        if (this.timeZoneNames == null) {
            this.timeZoneNames = ExtendedTimeZoneNames.getInstance(ULocale.forLocale(this.locale));
        }
        return this.timeZoneNames;
    }

    private TimeZoneNames getTimeZoneNames() {
        return getExtendedTimeZoneNames().getTimeZoneNames();
    }

    private int subParseZoneStringFromICU(String str, int i, CalendarBuilder calendarBuilder) {
        String canonicalID = android.icu.util.TimeZone.getCanonicalID(getTimeZone().getID());
        ExtendedTimeZoneNames.Match matchName = getExtendedTimeZoneNames().matchName(str, i, canonicalID);
        if (matchName == null) {
            return -i;
        }
        String tzId = matchName.getTzId();
        TimeZone timeZone = TimeZone.getTimeZone(tzId);
        if (!canonicalID.equals(tzId)) {
            setTimeZone(timeZone);
        }
        boolean isDst = matchName.isDst();
        int dSTSavings = isDst ? timeZone.getDSTSavings() : 0;
        if (!isDst || dSTSavings != 0) {
            calendarBuilder.clear(15).set(16, dSTSavings);
        }
        return matchName.getMatchLength() + i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0055, code lost:
        r7 = r2[r9];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int subParseZoneStringFromSymbols(java.lang.String r13, int r14, java.text.CalendarBuilder r15) {
        /*
            r12 = this;
            java.util.TimeZone r0 = r12.getTimeZone()
            java.text.DateFormatSymbols r1 = r12.formatData
            java.lang.String r2 = r0.getID()
            int r1 = r1.getZoneIndex(r2)
            java.text.DateFormatSymbols r2 = r12.formatData
            java.lang.String[][] r2 = r2.getZoneStringsWrapper()
            r3 = 2
            r4 = 0
            r5 = 0
            r6 = -1
            if (r1 == r6) goto L_0x0040
            r1 = r2[r1]
            int r7 = r12.matchZoneString(r13, r14, r1)
            if (r7 <= 0) goto L_0x003c
            if (r7 > r3) goto L_0x002f
            r5 = r1[r7]
            int r8 = r7 + 2
            r8 = r1[r8]
            boolean r5 = r5.equalsIgnoreCase(r8)
            goto L_0x0030
        L_0x002f:
            r5 = r4
        L_0x0030:
            r8 = r1[r4]
            java.util.TimeZone r8 = java.util.TimeZone.getTimeZone((java.lang.String) r8)
            r11 = r7
            r7 = r1
            r1 = r5
            r5 = r8
            r8 = r11
            goto L_0x0043
        L_0x003c:
            r8 = r7
            r7 = r1
            r1 = r4
            goto L_0x0043
        L_0x0040:
            r1 = r4
            r8 = r1
            r7 = r5
        L_0x0043:
            if (r5 != 0) goto L_0x006f
            java.text.DateFormatSymbols r9 = r12.formatData
            java.util.TimeZone r10 = java.util.TimeZone.getDefault()
            java.lang.String r10 = r10.getID()
            int r9 = r9.getZoneIndex(r10)
            if (r9 == r6) goto L_0x006f
            r7 = r2[r9]
            int r8 = r12.matchZoneString(r13, r14, r7)
            if (r8 <= 0) goto L_0x006f
            if (r8 > r3) goto L_0x0069
            r1 = r7[r8]
            int r5 = r8 + 2
            r5 = r7[r5]
            boolean r1 = r1.equalsIgnoreCase(r5)
        L_0x0069:
            r5 = r7[r4]
            java.util.TimeZone r5 = java.util.TimeZone.getTimeZone((java.lang.String) r5)
        L_0x006f:
            if (r5 != 0) goto L_0x0094
            int r6 = r2.length
            r9 = r4
        L_0x0073:
            if (r9 >= r6) goto L_0x0094
            r7 = r2[r9]
            int r8 = r12.matchZoneString(r13, r14, r7)
            if (r8 <= 0) goto L_0x0091
            if (r8 > r3) goto L_0x008a
            r13 = r7[r8]
            int r1 = r8 + 2
            r1 = r7[r1]
            boolean r13 = r13.equalsIgnoreCase(r1)
            r1 = r13
        L_0x008a:
            r13 = r7[r4]
            java.util.TimeZone r5 = java.util.TimeZone.getTimeZone((java.lang.String) r13)
            goto L_0x0094
        L_0x0091:
            int r9 = r9 + 1
            goto L_0x0073
        L_0x0094:
            if (r5 == 0) goto L_0x00bf
            boolean r13 = r5.equals(r0)
            if (r13 != 0) goto L_0x009f
            r12.setTimeZone(r5)
        L_0x009f:
            r12 = 3
            if (r8 < r12) goto L_0x00a6
            int r4 = r5.getDSTSavings()
        L_0x00a6:
            if (r1 != 0) goto L_0x00b7
            if (r8 < r12) goto L_0x00ac
            if (r4 == 0) goto L_0x00b7
        L_0x00ac:
            r12 = 15
            java.text.CalendarBuilder r12 = r15.clear(r12)
            r13 = 16
            r12.set(r13, r4)
        L_0x00b7:
            r12 = r7[r8]
            int r12 = r12.length()
            int r14 = r14 + r12
            return r14
        L_0x00bf:
            int r12 = -r14
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.subParseZoneStringFromSymbols(java.lang.String, int, java.text.CalendarBuilder):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0042, code lost:
        if (r10 != false) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0063, code lost:
        if (r0 > 59) goto L_0x0065;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int subParseNumericZone(java.lang.String r6, int r7, int r8, int r9, boolean r10, java.text.CalendarBuilder r11) {
        /*
            r5 = this;
            int r0 = r7 + 1
            r1 = 1
            char r7 = r6.charAt(r7)     // Catch:{ IndexOutOfBoundsException -> 0x007e }
            boolean r2 = r5.isDigit(r7)     // Catch:{ IndexOutOfBoundsException -> 0x007e }
            if (r2 != 0) goto L_0x000f
            goto L_0x007e
        L_0x000f:
            int r7 = r7 + -48
            int r2 = r0 + 1
            char r0 = r6.charAt(r0)     // Catch:{ IndexOutOfBoundsException -> 0x007d }
            boolean r3 = r5.isDigit(r0)     // Catch:{ IndexOutOfBoundsException -> 0x007d }
            if (r3 == 0) goto L_0x0023
            int r7 = r7 * 10
            int r0 = r0 + -48
            int r7 = r7 + r0
            goto L_0x0025
        L_0x0023:
            int r2 = r2 + -1
        L_0x0025:
            r0 = r2
            r2 = 23
            if (r7 <= r2) goto L_0x002b
            goto L_0x007e
        L_0x002b:
            r2 = 0
            if (r9 == r1) goto L_0x0067
            int r9 = r0 + 1
            char r0 = r6.charAt(r0)     // Catch:{ IndexOutOfBoundsException -> 0x0044 }
            r3 = 58
            if (r0 != r3) goto L_0x0042
            int r0 = r9 + 1
            char r9 = r6.charAt(r9)     // Catch:{ IndexOutOfBoundsException -> 0x007e }
            r4 = r0
            r0 = r9
            r9 = r4
            goto L_0x0046
        L_0x0042:
            if (r10 == 0) goto L_0x0046
        L_0x0044:
            r0 = r9
            goto L_0x007e
        L_0x0046:
            boolean r10 = r5.isDigit(r0)     // Catch:{ IndexOutOfBoundsException -> 0x0044 }
            if (r10 != 0) goto L_0x004d
            goto L_0x0044
        L_0x004d:
            int r0 = r0 + -48
            int r10 = r9 + 1
            char r6 = r6.charAt(r9)     // Catch:{ IndexOutOfBoundsException -> 0x0065 }
            boolean r5 = r5.isDigit(r6)     // Catch:{ IndexOutOfBoundsException -> 0x0065 }
            if (r5 != 0) goto L_0x005c
            goto L_0x0065
        L_0x005c:
            int r0 = r0 * 10
            int r6 = r6 + -48
            int r0 = r0 + r6
            r5 = 59
            if (r0 <= r5) goto L_0x0069
        L_0x0065:
            r0 = r10
            goto L_0x007e
        L_0x0067:
            r10 = r0
            r0 = r2
        L_0x0069:
            int r7 = r7 * 60
            int r0 = r0 + r7
            r5 = 60000(0xea60, float:8.4078E-41)
            int r0 = r0 * r5
            int r0 = r0 * r8
            r5 = 15
            java.text.CalendarBuilder r5 = r11.set(r5, r0)     // Catch:{ IndexOutOfBoundsException -> 0x0065 }
            r6 = 16
            r5.set(r6, r2)     // Catch:{ IndexOutOfBoundsException -> 0x0065 }
            return r10
        L_0x007d:
            r0 = r2
        L_0x007e:
            int r1 = r1 - r0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.subParseNumericZone(java.lang.String, int, int, int, boolean, java.text.CalendarBuilder):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:153:0x0286, code lost:
        if (r12 <= 12) goto L_0x028b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x02ea, code lost:
        if (r12 <= 24) goto L_0x02ef;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007f, code lost:
        if ((r0.calendar instanceof java.util.GregorianCalendar) == false) goto L_0x00b7;
     */
    /* JADX WARNING: Removed duplicated region for block: B:134:0x0229 A[Catch:{ IndexOutOfBoundsException -> 0x03ae }] */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x0238 A[Catch:{ IndexOutOfBoundsException -> 0x03ae }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int subParse(java.lang.String r18, int r19, int r20, int r21, boolean r22, boolean[] r23, java.text.ParsePosition r24, boolean r25, java.text.CalendarBuilder r26) {
        /*
            r17 = this;
            r0 = r17
            r7 = r18
            r8 = r19
            r4 = r21
            r10 = r24
            r9 = r26
            java.text.ParsePosition r11 = new java.text.ParsePosition
            r12 = 0
            r11.<init>(r12)
            r11.index = r8
            r13 = 1
            r1 = 19
            r2 = r20
            if (r2 != r1) goto L_0x0024
            java.util.Calendar r3 = r0.calendar
            boolean r3 = r3.isWeekDateSupported()
            if (r3 != 0) goto L_0x0024
            r2 = r13
        L_0x0024:
            int[] r3 = PATTERN_INDEX_TO_CALENDAR_FIELD
            r5 = r3[r2]
        L_0x0028:
            int r3 = r11.index
            int r6 = r18.length()
            r14 = -1
            if (r3 < r6) goto L_0x0034
            r10.errorIndex = r8
            return r14
        L_0x0034:
            int r3 = r11.index
            char r3 = r7.charAt(r3)
            r6 = 32
            if (r3 == r6) goto L_0x03b4
            r6 = 9
            if (r3 == r6) goto L_0x03b4
            int r3 = r11.index
            r15 = 4
            r14 = 15
            r6 = 2
            if (r2 == r15) goto L_0x005a
            if (r2 == r14) goto L_0x005a
            if (r2 != r6) goto L_0x0050
            if (r4 <= r6) goto L_0x005a
        L_0x0050:
            r14 = 22
            if (r2 != r14) goto L_0x0056
            if (r4 <= r6) goto L_0x005a
        L_0x0056:
            if (r2 == r13) goto L_0x005a
            if (r2 != r1) goto L_0x00b7
        L_0x005a:
            if (r22 == 0) goto L_0x0071
            int r1 = r8 + r4
            int r14 = r18.length()
            if (r1 <= r14) goto L_0x0066
            goto L_0x03ae
        L_0x0066:
            java.text.NumberFormat r14 = r0.numberFormat
            java.lang.String r1 = r7.substring(r12, r1)
            java.lang.Number r1 = r14.parse(r1, r11)
            goto L_0x0077
        L_0x0071:
            java.text.NumberFormat r1 = r0.numberFormat
            java.lang.Number r1 = r1.parse(r7, r11)
        L_0x0077:
            if (r1 != 0) goto L_0x0083
            if (r2 != r13) goto L_0x03ae
            java.util.Calendar r1 = r0.calendar
            boolean r1 = r1 instanceof java.util.GregorianCalendar
            if (r1 == 0) goto L_0x00b7
            goto L_0x03ae
        L_0x0083:
            int r1 = r1.intValue()
            if (r25 == 0) goto L_0x00b6
            if (r1 >= 0) goto L_0x00b6
            int r14 = r11.index
            int r12 = r18.length()
            if (r14 >= r12) goto L_0x009d
            int r12 = r11.index
            char r12 = r7.charAt(r12)
            char r14 = r0.minusSign
            if (r12 != r14) goto L_0x00b0
        L_0x009d:
            int r12 = r11.index
            int r14 = r18.length()
            if (r12 != r14) goto L_0x00b6
            int r12 = r11.index
            int r12 = r12 - r13
            char r12 = r7.charAt(r12)
            char r14 = r0.minusSign
            if (r12 != r14) goto L_0x00b6
        L_0x00b0:
            int r1 = -r1
            int r12 = r11.index
            int r12 = r12 - r13
            r11.index = r12
        L_0x00b6:
            r12 = r1
        L_0x00b7:
            boolean r14 = r17.useDateFormatSymbols()
            if (r2 == 0) goto L_0x0380
            if (r2 == r13) goto L_0x0317
            if (r2 == r6) goto L_0x0301
            if (r2 == r15) goto L_0x02df
            r1 = 9
            if (r2 == r1) goto L_0x02cc
            r1 = 14
            if (r2 == r1) goto L_0x029d
            r1 = 15
            if (r2 == r1) goto L_0x027b
            r1 = 16
            r6 = 45
            r15 = 43
            switch(r2) {
                case 17: goto L_0x01d7;
                case 18: goto L_0x01d7;
                case 19: goto L_0x0317;
                default: goto L_0x00d8;
            }
        L_0x00d8:
            switch(r2) {
                case 21: goto L_0x0184;
                case 22: goto L_0x016e;
                case 23: goto L_0x015b;
                default: goto L_0x00db;
            }
        L_0x00db:
            int r1 = r11.getIndex()
            if (r22 == 0) goto L_0x00f7
            int r3 = r8 + r4
            int r4 = r18.length()
            if (r3 <= r4) goto L_0x00eb
            goto L_0x03ae
        L_0x00eb:
            java.text.NumberFormat r4 = r0.numberFormat
            r6 = 0
            java.lang.String r3 = r7.substring(r6, r3)
            java.lang.Number r3 = r4.parse(r3, r11)
            goto L_0x00fd
        L_0x00f7:
            java.text.NumberFormat r3 = r0.numberFormat
            java.lang.Number r3 = r3.parse(r7, r11)
        L_0x00fd:
            if (r3 == 0) goto L_0x03ae
            r4 = 8
            if (r2 != r4) goto L_0x011e
            double r2 = r3.doubleValue()
            int r4 = r11.getIndex()
            int r4 = r4 - r1
            r14 = 4621819117588971520(0x4024000000000000, double:10.0)
            r16 = r5
            double r4 = (double) r4
            double r4 = java.lang.Math.pow(r14, r4)
            double r2 = r2 / r4
            r4 = 4652007308841189376(0x408f400000000000, double:1000.0)
            double r2 = r2 * r4
            int r1 = (int) r2
            goto L_0x0124
        L_0x011e:
            r16 = r5
            int r1 = r3.intValue()
        L_0x0124:
            if (r25 == 0) goto L_0x0153
            if (r1 >= 0) goto L_0x0153
            int r2 = r11.index
            int r3 = r18.length()
            if (r2 >= r3) goto L_0x013a
            int r2 = r11.index
            char r2 = r7.charAt(r2)
            char r3 = r0.minusSign
            if (r2 != r3) goto L_0x014d
        L_0x013a:
            int r2 = r11.index
            int r3 = r18.length()
            if (r2 != r3) goto L_0x0153
            int r2 = r11.index
            int r2 = r2 - r13
            char r2 = r7.charAt(r2)
            char r0 = r0.minusSign
            if (r2 != r0) goto L_0x0153
        L_0x014d:
            int r1 = -r1
            int r0 = r11.index
            int r0 = r0 - r13
            r11.index = r0
        L_0x0153:
            r5 = r16
            r9.set(r5, r1)
            int r0 = r11.index
            return r0
        L_0x015b:
            r6 = 1
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r5
            r4 = r14
            r5 = r6
            r6 = r26
            int r0 = r0.parseWeekday(r1, r2, r3, r4, r5, r6)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x016e:
            r13 = 1
            r0 = r17
            r1 = r18
            r2 = r21
            r3 = r12
            r4 = r19
            r6 = r11
            r7 = r14
            r8 = r13
            r9 = r26
            int r0 = r0.parseMonth(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x0184:
            int r2 = r18.length()
            int r3 = r11.index
            int r2 = r2 - r3
            if (r2 > 0) goto L_0x018f
            goto L_0x03ae
        L_0x018f:
            int r2 = r11.index
            char r2 = r7.charAt(r2)
            r3 = 90
            if (r2 != r3) goto L_0x01a9
            r3 = 15
            r5 = 0
            java.text.CalendarBuilder r0 = r9.set(r3, r5)
            r0.set(r1, r5)
            int r0 = r11.index
            int r0 = r0 + r13
            r11.index = r0
            return r0
        L_0x01a9:
            if (r2 != r15) goto L_0x01ad
            r3 = r13
            goto L_0x01b0
        L_0x01ad:
            if (r2 != r6) goto L_0x01d0
            r3 = -1
        L_0x01b0:
            int r1 = r11.index
            int r2 = r1 + 1
            r11.index = r2
            r1 = 3
            if (r4 != r1) goto L_0x01bb
            r5 = r13
            goto L_0x01bc
        L_0x01bb:
            r5 = 0
        L_0x01bc:
            r0 = r17
            r1 = r18
            r4 = r21
            r6 = r26
            int r0 = r0.subParseNumericZone(r1, r2, r3, r4, r5, r6)
            if (r0 <= 0) goto L_0x01cb
            return r0
        L_0x01cb:
            int r0 = -r0
            r11.index = r0
            goto L_0x03ae
        L_0x01d0:
            int r0 = r11.index
            int r0 = r0 + r13
            r11.index = r0
            goto L_0x03ae
        L_0x01d7:
            int r2 = r11.index     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            char r2 = r7.charAt(r2)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            if (r2 != r15) goto L_0x01e1
            r12 = r13
            goto L_0x01e6
        L_0x01e1:
            if (r2 != r6) goto L_0x01e5
            r12 = -1
            goto L_0x01e6
        L_0x01e5:
            r12 = 0
        L_0x01e6:
            if (r12 != 0) goto L_0x0260
            r3 = 71
            if (r2 == r3) goto L_0x01f0
            r3 = 103(0x67, float:1.44E-43)
            if (r2 != r3) goto L_0x0252
        L_0x01f0:
            int r2 = r18.length()     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            int r2 = r2 - r8
            r3 = 3
            if (r2 < r3) goto L_0x0252
            r2 = 1
            java.lang.String r4 = "GMT"
            r5 = 0
            r14 = 3
            r3 = r1
            r1 = r18
            r13 = r3
            r3 = r19
            r13 = r6
            r6 = r14
            boolean r1 = r1.regionMatches(r2, r3, r4, r5, r6)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            if (r1 == 0) goto L_0x0252
            r1 = 3
            int r1 = r1 + r8
            r11.index = r1     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            int r1 = r18.length()     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            int r2 = r11.index     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            int r1 = r1 - r2
            if (r1 <= 0) goto L_0x0226
            int r1 = r11.index     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            char r1 = r7.charAt(r1)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            if (r1 != r15) goto L_0x0222
            r3 = 1
            goto L_0x0227
        L_0x0222:
            if (r1 != r13) goto L_0x0226
            r3 = -1
            goto L_0x0227
        L_0x0226:
            r3 = r12
        L_0x0227:
            if (r3 != 0) goto L_0x0238
            r1 = 15
            r2 = 0
            java.text.CalendarBuilder r0 = r9.set(r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            r1 = 16
            r0.set(r1, r2)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            int r0 = r11.index     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            return r0
        L_0x0238:
            int r1 = r11.index     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            r2 = 1
            int r2 = r2 + r1
            r11.index = r2     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            r4 = 0
            r5 = 0
            r0 = r17
            r1 = r18
            r6 = r26
            int r0 = r0.subParseNumericZone(r1, r2, r3, r4, r5, r6)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            if (r0 <= 0) goto L_0x024d
            return r0
        L_0x024d:
            int r0 = -r0
            r11.index = r0     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            goto L_0x03ae
        L_0x0252:
            int r1 = r11.index     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            int r0 = r0.subParseZoneString(r7, r1, r9)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            if (r0 <= 0) goto L_0x025b
            return r0
        L_0x025b:
            int r0 = -r0
            r11.index = r0     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            goto L_0x03ae
        L_0x0260:
            int r1 = r11.index     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            r2 = 1
            int r2 = r2 + r1
            r11.index = r2     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            r4 = 0
            r5 = 0
            r0 = r17
            r1 = r18
            r3 = r12
            r6 = r26
            int r0 = r0.subParseNumericZone(r1, r2, r3, r4, r5, r6)     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            if (r0 <= 0) goto L_0x0276
            return r0
        L_0x0276:
            int r0 = -r0
            r11.index = r0     // Catch:{ IndexOutOfBoundsException -> 0x03ae }
            goto L_0x03ae
        L_0x027b:
            boolean r1 = r17.isLenient()
            if (r1 != 0) goto L_0x028a
            r1 = 1
            if (r12 < r1) goto L_0x03ae
            r2 = 12
            if (r12 <= r2) goto L_0x028b
            goto L_0x03ae
        L_0x028a:
            r1 = 1
        L_0x028b:
            java.util.Calendar r0 = r0.calendar
            r2 = 10
            int r0 = r0.getLeastMaximum(r2)
            int r0 = r0 + r1
            if (r12 != r0) goto L_0x0297
            r12 = 0
        L_0x0297:
            r9.set(r2, r12)
            int r0 = r11.index
            return r0
        L_0x029d:
            if (r14 == 0) goto L_0x02b6
            r3 = 9
            java.text.DateFormatSymbols r1 = r0.formatData
            java.lang.String[] r4 = r1.getAmPmStrings()
            r0 = r17
            r1 = r18
            r2 = r19
            r5 = r26
            int r0 = r0.matchString((java.lang.String) r1, (int) r2, (int) r3, (java.lang.String[]) r4, (java.text.CalendarBuilder) r5)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x02b6:
            java.util.Locale r1 = r0.locale
            java.util.Map r4 = r0.getDisplayNamesMap(r5, r1)
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r5
            r5 = r26
            int r0 = r0.matchString((java.lang.String) r1, (int) r2, (int) r3, (java.util.Map<java.lang.String, java.lang.Integer>) r4, (java.text.CalendarBuilder) r5)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x02cc:
            r6 = 0
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r5
            r4 = r14
            r5 = r6
            r6 = r26
            int r0 = r0.parseWeekday(r1, r2, r3, r4, r5, r6)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x02df:
            boolean r1 = r17.isLenient()
            if (r1 != 0) goto L_0x02ee
            r1 = 1
            if (r12 < r1) goto L_0x03ae
            r2 = 24
            if (r12 <= r2) goto L_0x02ef
            goto L_0x03ae
        L_0x02ee:
            r1 = 1
        L_0x02ef:
            java.util.Calendar r0 = r0.calendar
            r2 = 11
            int r0 = r0.getMaximum(r2)
            int r0 = r0 + r1
            if (r12 != r0) goto L_0x02fb
            r12 = 0
        L_0x02fb:
            r9.set(r2, r12)
            int r0 = r11.index
            return r0
        L_0x0301:
            r13 = 0
            r0 = r17
            r1 = r18
            r2 = r21
            r3 = r12
            r4 = r19
            r6 = r11
            r7 = r14
            r8 = r13
            r9 = r26
            int r0 = r0.parseMonth(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x0317:
            java.util.Calendar r1 = r0.calendar
            boolean r1 = r1 instanceof java.util.GregorianCalendar
            if (r1 != 0) goto L_0x0346
            r1 = 4
            if (r4 < r1) goto L_0x0322
            r13 = 2
            goto L_0x0323
        L_0x0322:
            r13 = 1
        L_0x0323:
            java.util.Calendar r1 = r0.calendar
            java.util.Locale r2 = r0.locale
            java.util.Map r1 = r1.getDisplayNames(r5, r13, r2)
            if (r1 == 0) goto L_0x0340
            r20 = r17
            r21 = r18
            r22 = r19
            r23 = r5
            r24 = r1
            r25 = r26
            int r0 = r20.matchString((java.lang.String) r21, (int) r22, (int) r23, (java.util.Map<java.lang.String, java.lang.Integer>) r24, (java.text.CalendarBuilder) r25)
            if (r0 <= 0) goto L_0x0340
            return r0
        L_0x0340:
            r9.set(r5, r12)
            int r0 = r11.index
            return r0
        L_0x0346:
            r1 = 2
            if (r4 > r1) goto L_0x037a
            int r2 = r11.index
            int r2 = r2 - r3
            if (r2 != r1) goto L_0x037a
            char r1 = r7.charAt(r3)
            boolean r1 = java.lang.Character.isDigit((char) r1)
            if (r1 == 0) goto L_0x037a
            r1 = 1
            int r3 = r3 + r1
            char r1 = r7.charAt(r3)
            boolean r1 = java.lang.Character.isDigit((char) r1)
            if (r1 == 0) goto L_0x037a
            int r0 = r0.defaultCenturyStartYear
            int r1 = r0 % 100
            r3 = 0
            if (r12 != r1) goto L_0x036d
            r13 = 1
            goto L_0x036e
        L_0x036d:
            r13 = 0
        L_0x036e:
            r23[r3] = r13
            r2 = 100
            int r0 = r0 / r2
            int r0 = r0 * r2
            if (r12 >= r1) goto L_0x0377
            goto L_0x0378
        L_0x0377:
            r2 = r3
        L_0x0378:
            int r0 = r0 + r2
            int r12 = r12 + r0
        L_0x037a:
            r9.set(r5, r12)
            int r0 = r11.index
            return r0
        L_0x0380:
            if (r14 == 0) goto L_0x0398
            r3 = 0
            java.text.DateFormatSymbols r1 = r0.formatData
            java.lang.String[] r4 = r1.getEras()
            r0 = r17
            r1 = r18
            r2 = r19
            r5 = r26
            int r0 = r0.matchString((java.lang.String) r1, (int) r2, (int) r3, (java.lang.String[]) r4, (java.text.CalendarBuilder) r5)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x0398:
            java.util.Locale r1 = r0.locale
            java.util.Map r4 = r0.getDisplayNamesMap(r5, r1)
            r0 = r17
            r1 = r18
            r2 = r19
            r3 = r5
            r5 = r26
            int r0 = r0.matchString((java.lang.String) r1, (int) r2, (int) r3, (java.util.Map<java.lang.String, java.lang.Integer>) r4, (java.text.CalendarBuilder) r5)
            if (r0 <= 0) goto L_0x03ae
            return r0
        L_0x03ae:
            int r0 = r11.index
            r10.errorIndex = r0
            r0 = -1
            return r0
        L_0x03b4:
            r3 = r12
            int r6 = r11.index
            r12 = 1
            int r6 = r6 + r12
            r11.index = r6
            r13 = r12
            r12 = r3
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.subParse(java.lang.String, int, int, int, boolean, boolean[], java.text.ParsePosition, boolean, java.text.CalendarBuilder):int");
    }

    private int parseMonth(String str, int i, int i2, int i3, int i4, ParsePosition parsePosition, boolean z, boolean z2, CalendarBuilder calendarBuilder) {
        int i5;
        if (i <= 2) {
            calendarBuilder.set(2, i2 - 1);
            return parsePosition.index;
        }
        if (z) {
            DateFormatSymbols dateFormatSymbols = this.formatData;
            int matchString = matchString(str, i3, 2, z2 ? dateFormatSymbols.getStandAloneMonths() : dateFormatSymbols.getMonths(), calendarBuilder);
            if (matchString > 0) {
                return matchString;
            }
            DateFormatSymbols dateFormatSymbols2 = this.formatData;
            i5 = matchString(str, i3, 2, z2 ? dateFormatSymbols2.getShortStandAloneMonths() : dateFormatSymbols2.getShortMonths(), calendarBuilder);
            if (i5 > 0) {
                return i5;
            }
        } else {
            i5 = matchString(str, i3, i4, getDisplayNamesMap(i4, this.locale), calendarBuilder);
            if (i5 > 0) {
            }
        }
        return i5;
    }

    private int parseWeekday(String str, int i, int i2, boolean z, boolean z2, CalendarBuilder calendarBuilder) {
        int i3;
        if (z) {
            DateFormatSymbols dateFormatSymbols = this.formatData;
            int matchString = matchString(str, i, 7, z2 ? dateFormatSymbols.getStandAloneWeekdays() : dateFormatSymbols.getWeekdays(), calendarBuilder);
            if (matchString > 0) {
                return matchString;
            }
            DateFormatSymbols dateFormatSymbols2 = this.formatData;
            i3 = matchString(str, i, 7, z2 ? dateFormatSymbols2.getShortStandAloneWeekdays() : dateFormatSymbols2.getShortWeekdays(), calendarBuilder);
            if (i3 > 0) {
                return i3;
            }
        } else {
            int[] iArr = {2, 1};
            i3 = -1;
            for (int i4 = 0; i4 < 2; i4++) {
                int i5 = i2;
                i3 = matchString(str, i, i2, this.calendar.getDisplayNames(i2, iArr[i4], this.locale), calendarBuilder);
                if (i3 > 0) {
                    return i3;
                }
            }
        }
        return i3;
    }

    private boolean useDateFormatSymbols() {
        return this.useDateFormatSymbols || "java.util.GregorianCalendar".equals(this.calendar.getClass().getName()) || this.locale == null;
    }

    private String translatePattern(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (z) {
                if (charAt == '\'') {
                    z = false;
                }
            } else if (charAt == '\'') {
                z = true;
            } else if ((charAt >= 'a' && charAt <= 'z') || (charAt >= 'A' && charAt <= 'Z')) {
                int indexOf = str2.indexOf((int) charAt);
                if (indexOf < 0) {
                    throw new IllegalArgumentException("Illegal pattern  character '" + charAt + "'");
                } else if (indexOf < str3.length()) {
                    charAt = str3.charAt(indexOf);
                }
            }
            sb.append(charAt);
        }
        if (!z) {
            return sb.toString();
        }
        throw new IllegalArgumentException("Unfinished quote in pattern");
    }

    public String toPattern() {
        return this.pattern;
    }

    public String toLocalizedPattern() {
        return translatePattern(this.pattern, "GyMdkHmsSEDFwWahKzZYuXLcbB", this.formatData.getLocalPatternChars());
    }

    public void applyPattern(String str) {
        applyPatternImpl(str);
    }

    private void applyPatternImpl(String str) {
        this.compiledPattern = compile(str);
        this.pattern = str;
    }

    public void applyLocalizedPattern(String str) {
        String translatePattern = translatePattern(str, this.formatData.getLocalPatternChars(), "GyMdkHmsSEDFwWahKzZYuXLcbB");
        this.compiledPattern = compile(translatePattern);
        this.pattern = translatePattern;
    }

    public DateFormatSymbols getDateFormatSymbols() {
        return (DateFormatSymbols) this.formatData.clone();
    }

    public void setDateFormatSymbols(DateFormatSymbols dateFormatSymbols) {
        this.formatData = (DateFormatSymbols) dateFormatSymbols.clone();
        this.useDateFormatSymbols = true;
    }

    public Object clone() {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) super.clone();
        simpleDateFormat.formatData = (DateFormatSymbols) this.formatData.clone();
        return simpleDateFormat;
    }

    public int hashCode() {
        return this.pattern.hashCode();
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) obj;
        if (!this.pattern.equals(simpleDateFormat.pattern) || !this.formatData.equals(simpleDateFormat.formatData)) {
            return false;
        }
        return true;
    }

    private Map<String, Integer> getDisplayNamesMap(int i, Locale locale2) {
        Map<String, Integer> displayNames = this.calendar.getDisplayNames(i, 1, locale2);
        for (int displayNames2 : REST_OF_STYLES) {
            Map<String, Integer> displayNames3 = this.calendar.getDisplayNames(i, displayNames2, locale2);
            if (displayNames3 != null) {
                displayNames.putAll(displayNames3);
            }
        }
        return displayNames;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        r0 = r3.getID();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.p026io.ObjectInputStream r3) throws java.p026io.IOException, java.lang.ClassNotFoundException {
        /*
            r2 = this;
            r3.defaultReadObject()
            java.lang.String r3 = r2.pattern     // Catch:{ Exception -> 0x0041 }
            char[] r3 = r2.compile(r3)     // Catch:{ Exception -> 0x0041 }
            r2.compiledPattern = r3     // Catch:{ Exception -> 0x0041 }
            int r3 = r2.serialVersionOnStream
            r0 = 1
            if (r3 >= r0) goto L_0x0014
            r2.initializeDefaultCentury()
            goto L_0x0019
        L_0x0014:
            java.util.Date r3 = r2.defaultCenturyStart
            r2.parseAmbiguousDatesAsAfter(r3)
        L_0x0019:
            r2.serialVersionOnStream = r0
            java.util.TimeZone r3 = r2.getTimeZone()
            boolean r0 = r3 instanceof java.util.SimpleTimeZone
            if (r0 == 0) goto L_0x0040
            java.lang.String r0 = r3.getID()
            java.util.TimeZone r1 = java.util.TimeZone.getTimeZone((java.lang.String) r0)
            if (r1 == 0) goto L_0x0040
            boolean r3 = r1.hasSameRules(r3)
            if (r3 == 0) goto L_0x0040
            java.lang.String r3 = r1.getID()
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x0040
            r2.setTimeZone(r1)
        L_0x0040:
            return
        L_0x0041:
            java.io.InvalidObjectException r2 = new java.io.InvalidObjectException
            java.lang.String r3 = "invalid pattern"
            r2.<init>(r3)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.SimpleDateFormat.readObject(java.io.ObjectInputStream):void");
    }

    private void checkNegativeNumberExpression() {
        int indexOf;
        if ((this.numberFormat instanceof DecimalFormat) && !this.numberFormat.equals(this.originalNumberFormat)) {
            String pattern2 = ((DecimalFormat) this.numberFormat).toPattern();
            if (!pattern2.equals(this.originalNumberPattern)) {
                this.hasFollowingMinusSign = false;
                int indexOf2 = pattern2.indexOf(59);
                if (indexOf2 > -1 && (indexOf = pattern2.indexOf(45, indexOf2)) > pattern2.lastIndexOf(48) && indexOf > pattern2.lastIndexOf(35)) {
                    this.hasFollowingMinusSign = true;
                    this.minusSign = ((DecimalFormat) this.numberFormat).getDecimalFormatSymbols().getMinusSign();
                }
                this.originalNumberPattern = pattern2;
            }
            this.originalNumberFormat = this.numberFormat;
        }
    }
}
