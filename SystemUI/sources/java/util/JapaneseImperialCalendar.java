package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.CalendarUtils;
import sun.util.calendar.Era;
import sun.util.calendar.Gregorian;
import sun.util.calendar.LocalGregorianCalendar;
import sun.util.locale.provider.CalendarDataUtility;

class JapaneseImperialCalendar extends Calendar {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int BEFORE_MEIJI = 0;
    private static final Era BEFORE_MEIJI_ERA;
    private static final int EPOCH_OFFSET = 719163;
    public static final int HEISEI = 4;
    static final int[] LEAST_MAX_VALUES = {0, 0, 0, 0, 4, 28, 0, 7, 4, 1, 11, 23, 59, 59, 999, 50400000, 1200000};
    static final int[] MAX_VALUES = {0, 292278994, 11, 53, 6, 31, 366, 7, 6, 1, 11, 23, 59, 59, 999, 50400000, 7200000};
    public static final int MEIJI = 1;
    static final int[] MIN_VALUES = {0, -292275055, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, -46800000, 0};
    private static final long ONE_DAY = 86400000;
    private static final int ONE_HOUR = 3600000;
    private static final int ONE_MINUTE = 60000;
    private static final int ONE_SECOND = 1000;
    public static final int REIWA = 5;
    public static final int SHOWA = 3;
    public static final int TAISHO = 2;
    private static final int currentEra = 5;
    private static final Era[] eras;
    private static final Gregorian gcal;
    private static final LocalGregorianCalendar jcal;
    private static final long serialVersionUID = -3364572813905467929L;
    private static final long[] sinceFixedDates;
    private transient long cachedFixedDate = Long.MIN_VALUE;
    private transient LocalGregorianCalendar.Date jdate;
    private transient int[] originalFields;
    private transient int[] zoneOffsets;

    public String getCalendarType() {
        return "japanese";
    }

    static {
        LocalGregorianCalendar localGregorianCalendar = (LocalGregorianCalendar) CalendarSystem.forName("japanese");
        jcal = localGregorianCalendar;
        Gregorian gregorianCalendar = CalendarSystem.getGregorianCalendar();
        gcal = gregorianCalendar;
        Era era = new Era("BeforeMeiji", "BM", Long.MIN_VALUE, false);
        BEFORE_MEIJI_ERA = era;
        Era[] eras2 = localGregorianCalendar.getEras();
        int length = eras2.length + 1;
        Era[] eraArr = new Era[length];
        eras = eraArr;
        long[] jArr = new long[length];
        sinceFixedDates = jArr;
        jArr[0] = gregorianCalendar.getFixedDate(era.getSinceDate());
        eraArr[0] = era;
        int length2 = eras2.length;
        int i = 0;
        int i2 = 1;
        while (i < length2) {
            Era era2 = eras2[i];
            sinceFixedDates[i2] = gcal.getFixedDate(era2.getSinceDate());
            eras[i2] = era2;
            i++;
            i2++;
        }
        int[] iArr = LEAST_MAX_VALUES;
        int[] iArr2 = MAX_VALUES;
        int length3 = eras.length - 1;
        iArr2[0] = length3;
        iArr[0] = length3;
        Gregorian.Date newCalendarDate = gcal.newCalendarDate(TimeZone.NO_TIMEZONE);
        int i3 = Integer.MAX_VALUE;
        int i4 = Integer.MAX_VALUE;
        int i5 = 1;
        while (true) {
            Era[] eraArr2 = eras;
            if (i5 < eraArr2.length) {
                long j = sinceFixedDates[i5];
                CalendarDate sinceDate = eraArr2[i5].getSinceDate();
                newCalendarDate.setDate(sinceDate.getYear(), 1, 1);
                Gregorian gregorian = gcal;
                long fixedDate = gregorian.getFixedDate(newCalendarDate);
                if (j != fixedDate) {
                    i4 = Math.min(((int) (j - fixedDate)) + 1, i4);
                }
                newCalendarDate.setDate(sinceDate.getYear(), 12, 31);
                long fixedDate2 = gregorian.getFixedDate(newCalendarDate);
                if (j != fixedDate2) {
                    i4 = Math.min(((int) (fixedDate2 - j)) + 1, i4);
                }
                LocalGregorianCalendar.Date calendarDate = getCalendarDate(j - 1);
                int year = calendarDate.getYear();
                if (calendarDate.getMonth() != 1 || calendarDate.getDayOfMonth() != 1) {
                    year--;
                }
                i3 = Math.min(year, i3);
                i5++;
            } else {
                int[] iArr3 = LEAST_MAX_VALUES;
                iArr3[1] = i3;
                iArr3[6] = i4;
                return;
            }
        }
    }

    JapaneseImperialCalendar(TimeZone timeZone, Locale locale) {
        super(timeZone, locale);
        this.jdate = jcal.newCalendarDate(timeZone);
        setTimeInMillis(System.currentTimeMillis());
    }

    JapaneseImperialCalendar(TimeZone timeZone, Locale locale, boolean z) {
        super(timeZone, locale);
        this.jdate = jcal.newCalendarDate(timeZone);
    }

    public boolean equals(Object obj) {
        return (obj instanceof JapaneseImperialCalendar) && super.equals(obj);
    }

    public int hashCode() {
        return this.jdate.hashCode() ^ super.hashCode();
    }

    public void add(int i, int i2) {
        long j;
        long j2;
        int i3 = i;
        int i4 = i2;
        if (i4 != 0) {
            if (i3 < 0 || i3 >= 15) {
                throw new IllegalArgumentException();
            }
            complete();
            if (i3 == 1) {
                LocalGregorianCalendar.Date date = (LocalGregorianCalendar.Date) this.jdate.clone();
                date.addYear(i4);
                pinDayOfMonth(date);
                set(0, getEraIndex(date));
                set(1, date.getYear());
                set(2, date.getMonth() - 1);
                set(5, date.getDayOfMonth());
            } else if (i3 == 2) {
                LocalGregorianCalendar.Date date2 = (LocalGregorianCalendar.Date) this.jdate.clone();
                date2.addMonth(i4);
                pinDayOfMonth(date2);
                set(0, getEraIndex(date2));
                set(1, date2.getYear());
                set(2, date2.getMonth() - 1);
                set(5, date2.getDayOfMonth());
            } else if (i3 == 0) {
                int internalGet = internalGet(0) + i4;
                if (internalGet < 0) {
                    internalGet = 0;
                } else {
                    Era[] eraArr = eras;
                    if (internalGet > eraArr.length - 1) {
                        internalGet = eraArr.length - 1;
                    }
                }
                set(0, internalGet);
            } else {
                long j3 = (long) i4;
                if (!(i3 == 3 || i3 == 4)) {
                    switch (i3) {
                        case 8:
                            break;
                        case 9:
                            j3 = (long) (i4 / 2);
                            j = (long) ((i4 % 2) * 12);
                            break;
                        case 10:
                        case 11:
                            j2 = 3600000;
                            break;
                        case 12:
                            j2 = 60000;
                            break;
                        case 13:
                            j3 *= 1000;
                            break;
                    }
                }
                j2 = 7;
                j3 *= j2;
                j = 0;
                if (i3 >= 10) {
                    setTimeInMillis(this.time + j3);
                    return;
                }
                long j4 = this.cachedFixedDate;
                long j5 = j3;
                long internalGet2 = ((((((j + ((long) internalGet(11))) * 60) + ((long) internalGet(12))) * 60) + ((long) internalGet(13))) * 1000) + ((long) internalGet(14));
                if (internalGet2 >= ONE_DAY) {
                    j4++;
                    internalGet2 -= ONE_DAY;
                } else if (internalGet2 < 0) {
                    j4--;
                    internalGet2 += ONE_DAY;
                }
                long j6 = j4 + j5;
                int internalGet3 = internalGet(15) + internalGet(16);
                setTimeInMillis((((j6 - 719163) * ONE_DAY) + internalGet2) - ((long) internalGet3));
                int internalGet4 = internalGet3 - (internalGet(15) + internalGet(16));
                if (internalGet4 != 0) {
                    long j7 = (long) internalGet4;
                    setTimeInMillis(this.time + j7);
                    if (this.cachedFixedDate != j6) {
                        setTimeInMillis(this.time - j7);
                    }
                }
            }
        }
    }

    public void roll(int i, boolean z) {
        roll(i, z ? 1 : -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x02b4, code lost:
        if (gcal.getYearFromFixedDate(r7) != r3) goto L_0x02cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x02c9, code lost:
        if (r7 < r6.getFixedDate(r6.getCalendarDate(Long.MIN_VALUE, getZone()))) goto L_0x02cb;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void roll(int r17, int r18) {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r18
            if (r2 != 0) goto L_0x0009
            return
        L_0x0009:
            if (r1 < 0) goto L_0x0521
            r3 = 15
            if (r1 >= r3) goto L_0x0521
            r16.complete()
            int r4 = r16.getMinimum(r17)
            int r5 = r16.getMaximum(r17)
            r8 = 0
            r9 = 7
            r11 = 12
            r12 = 2
            r13 = 3
            r14 = 5
            r15 = 7
            r6 = 1
            switch(r1) {
                case 1: goto L_0x050d;
                case 2: goto L_0x034d;
                case 3: goto L_0x024d;
                case 4: goto L_0x01d6;
                case 5: goto L_0x019d;
                case 6: goto L_0x0164;
                case 7: goto L_0x0104;
                case 8: goto L_0x00a8;
                case 9: goto L_0x0027;
                case 10: goto L_0x0029;
                case 11: goto L_0x0029;
                default: goto L_0x0027;
            }
        L_0x0027:
            goto L_0x0515
        L_0x0029:
            int r5 = r5 + r6
            int r4 = r16.internalGet(r17)
            int r2 = r2 + r4
            int r2 = r2 % r5
            if (r2 >= 0) goto L_0x0033
            int r2 = r2 + r5
        L_0x0033:
            long r7 = r0.time
            r9 = 3600000(0x36ee80, float:5.044674E-39)
            int r2 = r2 - r4
            int r2 = r2 * r9
            long r9 = (long) r2
            long r7 = r7 + r9
            r0.time = r7
            sun.util.calendar.LocalGregorianCalendar r2 = jcal
            long r7 = r0.time
            java.util.TimeZone r4 = r16.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r4 = r2.getCalendarDate((long) r7, (java.util.TimeZone) r4)
            int r7 = r0.internalGet(r14)
            int r8 = r4.getDayOfMonth()
            r9 = 10
            if (r7 == r8) goto L_0x007a
            sun.util.calendar.LocalGregorianCalendar$Date r7 = r0.jdate
            sun.util.calendar.Era r7 = r7.getEra()
            r4.setEra(r7)
            int r7 = r0.internalGet(r6)
            int r8 = r0.internalGet(r12)
            int r8 = r8 + r6
            int r6 = r0.internalGet(r14)
            r4.setDate(r7, r8, r6)
            if (r1 != r9) goto L_0x0074
            r4.addHours(r11)
        L_0x0074:
            long r6 = r2.getTime(r4)
            r0.time = r6
        L_0x007a:
            int r2 = r4.getHours()
            int r5 = r2 % r5
            r0.internalSet(r1, r5)
            if (r1 != r9) goto L_0x008b
            r1 = 11
            r0.internalSet(r1, r2)
            goto L_0x0096
        L_0x008b:
            int r1 = r2 / 12
            r5 = 9
            r0.internalSet(r5, r1)
            int r2 = r2 % r11
            r0.internalSet(r9, r2)
        L_0x0096:
            int r1 = r4.getZoneOffset()
            int r2 = r4.getDaylightSaving()
            int r1 = r1 - r2
            r0.internalSet(r3, r1)
            r1 = 16
            r0.internalSet(r1, r2)
            return
        L_0x00a8:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getNormalizedYear()
            boolean r3 = r0.isTransitionYear(r3)
            if (r3 != 0) goto L_0x00d4
            int r3 = r0.internalGet(r14)
            sun.util.calendar.LocalGregorianCalendar r4 = jcal
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r0.jdate
            int r4 = r4.getMonthLength(r5)
            int r5 = r4 % 7
            int r4 = r4 / r15
            int r3 = r3 - r6
            int r3 = r3 % r15
            if (r3 >= r5) goto L_0x00c9
            int r4 = r4 + 1
        L_0x00c9:
            r5 = r4
            int r3 = r0.internalGet(r15)
            r0.set(r15, r3)
            r4 = r6
            goto L_0x0515
        L_0x00d4:
            long r3 = r0.cachedFixedDate
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r0.jdate
            long r7 = r0.getFixedDateMonth1(r5, r3)
            int r5 = r16.actualMonthLength()
            int r9 = r5 % 7
            int r5 = r5 / r15
            long r3 = r3 - r7
            int r3 = (int) r3
            int r3 = r3 % r15
            if (r3 >= r9) goto L_0x00ea
            int r5 = r5 + 1
        L_0x00ea:
            int r1 = r16.internalGet(r17)
            int r1 = getRolledValue(r1, r2, r6, r5)
            int r1 = r1 - r6
            int r1 = r1 * r15
            long r1 = (long) r1
            long r7 = r7 + r1
            long r1 = (long) r3
            long r7 = r7 + r1
            sun.util.calendar.LocalGregorianCalendar$Date r1 = getCalendarDate(r7)
            int r1 = r1.getDayOfMonth()
            r0.set(r14, r1)
            return
        L_0x0104:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getNormalizedYear()
            boolean r5 = r0.isTransitionYear(r3)
            if (r5 != 0) goto L_0x012b
            int r3 = r3 - r6
            boolean r3 = r0.isTransitionYear(r3)
            if (r3 != 0) goto L_0x012b
            int r3 = r0.internalGet(r13)
            if (r3 <= r6) goto L_0x012b
            r5 = 52
            if (r3 >= r5) goto L_0x012b
            int r3 = r0.internalGet(r13)
            r0.set(r13, r3)
            r5 = r15
            goto L_0x0515
        L_0x012b:
            int r1 = r2 % 7
            if (r1 != 0) goto L_0x0130
            return
        L_0x0130:
            long r2 = r0.cachedFixedDate
            int r4 = r16.getFirstDayOfWeek()
            long r4 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r2, r4)
            long r11 = (long) r1
            long r2 = r2 + r11
            int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r1 >= 0) goto L_0x0142
            long r2 = r2 + r9
            goto L_0x0148
        L_0x0142:
            long r4 = r4 + r9
            int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r1 < 0) goto L_0x0148
            long r2 = r2 - r9
        L_0x0148:
            sun.util.calendar.LocalGregorianCalendar$Date r1 = getCalendarDate(r2)
            int r2 = getEraIndex(r1)
            r0.set(r8, r2)
            int r2 = r1.getYear()
            int r3 = r1.getMonth()
            int r3 = r3 - r6
            int r1 = r1.getDayOfMonth()
            r0.set(r2, r3, r1)
            return
        L_0x0164:
            int r5 = r16.getActualMaximum(r17)
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getNormalizedYear()
            boolean r3 = r0.isTransitionYear(r3)
            if (r3 != 0) goto L_0x0176
            goto L_0x0515
        L_0x0176:
            r1 = 6
            int r3 = r0.internalGet(r1)
            int r2 = getRolledValue(r3, r2, r4, r5)
            long r3 = r0.cachedFixedDate
            int r1 = r0.internalGet(r1)
            long r7 = (long) r1
            long r3 = r3 - r7
            long r1 = (long) r2
            long r3 = r3 + r1
            sun.util.calendar.LocalGregorianCalendar$Date r1 = getCalendarDate(r3)
            int r2 = r1.getMonth()
            int r2 = r2 - r6
            r0.set(r12, r2)
            int r1 = r1.getDayOfMonth()
            r0.set(r14, r1)
            return
        L_0x019d:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getNormalizedYear()
            boolean r3 = r0.isTransitionYear(r3)
            if (r3 != 0) goto L_0x01b3
            sun.util.calendar.LocalGregorianCalendar r3 = jcal
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r0.jdate
            int r5 = r3.getMonthLength(r5)
            goto L_0x0515
        L_0x01b3:
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r0.jdate
            long r3 = r0.cachedFixedDate
            long r3 = r0.getFixedDateMonth1(r1, r3)
            long r9 = r0.cachedFixedDate
            long r9 = r9 - r3
            int r1 = (int) r9
            int r5 = r16.actualMonthLength()
            int r5 = r5 - r6
            int r1 = getRolledValue(r1, r2, r8, r5)
            long r1 = (long) r1
            long r3 = r3 + r1
            sun.util.calendar.LocalGregorianCalendar$Date r1 = getCalendarDate(r3)
            int r1 = r1.getDayOfMonth()
            r0.set(r14, r1)
            return
        L_0x01d6:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getNormalizedYear()
            boolean r3 = r0.isTransitionYear(r3)
            int r4 = r0.internalGet(r15)
            int r5 = r16.getFirstDayOfWeek()
            int r4 = r4 - r5
            if (r4 >= 0) goto L_0x01ed
            int r4 = r4 + 7
        L_0x01ed:
            long r7 = r0.cachedFixedDate
            r11 = 1
            if (r3 == 0) goto L_0x01fe
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            long r7 = r0.getFixedDateMonth1(r3, r7)
            int r3 = r16.actualMonthLength()
            goto L_0x020d
        L_0x01fe:
            int r3 = r0.internalGet(r14)
            long r14 = (long) r3
            long r7 = r7 - r14
            long r7 = r7 + r11
            sun.util.calendar.LocalGregorianCalendar r3 = jcal
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r0.jdate
            int r3 = r3.getMonthLength(r5)
        L_0x020d:
            r13 = 6
            long r13 = r13 + r7
            int r5 = r16.getFirstDayOfWeek()
            long r13 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r13, r5)
            long r11 = r13 - r7
            int r5 = (int) r11
            int r11 = r16.getMinimalDaysInFirstWeek()
            if (r5 < r11) goto L_0x0222
            long r13 = r13 - r9
        L_0x0222:
            int r5 = r16.getActualMaximum(r17)
            int r1 = r16.internalGet(r17)
            int r1 = getRolledValue(r1, r2, r6, r5)
            int r1 = r1 - r6
            r2 = 7
            int r1 = r1 * r2
            long r1 = (long) r1
            long r13 = r13 + r1
            long r1 = (long) r4
            long r13 = r13 + r1
            int r1 = (r13 > r7 ? 1 : (r13 == r7 ? 0 : -1))
            if (r1 >= 0) goto L_0x023b
            r13 = r7
            goto L_0x0245
        L_0x023b:
            long r1 = (long) r3
            long r1 = r1 + r7
            int r3 = (r13 > r1 ? 1 : (r13 == r1 ? 0 : -1))
            if (r3 < 0) goto L_0x0245
            r3 = 1
            long r13 = r1 - r3
        L_0x0245:
            long r13 = r13 - r7
            int r1 = (int) r13
            int r1 = r1 + r6
            r2 = 5
            r0.set(r2, r1)
            return
        L_0x024d:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getNormalizedYear()
            int r5 = r0.getActualMaximum(r13)
            r7 = 7
            int r8 = r0.internalGet(r7)
            r0.set(r7, r8)
            int r7 = r0.internalGet(r13)
            int r8 = r7 + r2
            sun.util.calendar.LocalGregorianCalendar$Date r9 = r0.jdate
            int r9 = r9.getNormalizedYear()
            boolean r9 = r0.isTransitionYear(r9)
            if (r9 != 0) goto L_0x02e3
            sun.util.calendar.LocalGregorianCalendar$Date r9 = r0.jdate
            int r9 = r9.getYear()
            int r10 = r0.getMaximum(r6)
            if (r9 != r10) goto L_0x0282
            int r5 = r0.getActualMaximum(r13)
            goto L_0x0298
        L_0x0282:
            int r10 = r0.getMinimum(r6)
            if (r9 != r10) goto L_0x0298
            int r4 = r0.getActualMinimum(r13)
            int r5 = r0.getActualMaximum(r13)
            if (r8 <= r4) goto L_0x0298
            if (r8 >= r5) goto L_0x0298
            r0.set(r13, r8)
            return
        L_0x0298:
            if (r8 <= r4) goto L_0x02a0
            if (r8 >= r5) goto L_0x02a0
            r0.set(r13, r8)
            return
        L_0x02a0:
            long r10 = r0.cachedFixedDate
            int r7 = r7 - r4
            r8 = 7
            int r7 = r7 * r8
            long r7 = (long) r7
            long r7 = r10 - r7
            int r6 = r0.getMinimum(r6)
            if (r9 == r6) goto L_0x02b7
            sun.util.calendar.Gregorian r6 = gcal
            int r6 = r6.getYearFromFixedDate(r7)
            if (r6 == r3) goto L_0x02cd
            goto L_0x02cb
        L_0x02b7:
            sun.util.calendar.LocalGregorianCalendar r6 = jcal
            java.util.TimeZone r9 = r16.getZone()
            r14 = -9223372036854775808
            sun.util.calendar.LocalGregorianCalendar$Date r9 = r6.getCalendarDate((long) r14, (java.util.TimeZone) r9)
            long r14 = r6.getFixedDate(r9)
            int r6 = (r7 > r14 ? 1 : (r7 == r14 ? 0 : -1))
            if (r6 >= 0) goto L_0x02cd
        L_0x02cb:
            int r4 = r4 + 1
        L_0x02cd:
            int r6 = r0.internalGet(r13)
            int r6 = r5 - r6
            r7 = 7
            int r6 = r6 * r7
            long r6 = (long) r6
            long r10 = r10 + r6
            sun.util.calendar.Gregorian r6 = gcal
            int r6 = r6.getYearFromFixedDate(r10)
            if (r6 == r3) goto L_0x0515
            int r5 = r5 + -1
            goto L_0x0515
        L_0x02e3:
            long r8 = r0.cachedFixedDate
            int r1 = r7 - r4
            r3 = 7
            int r1 = r1 * r3
            long r10 = (long) r1
            long r10 = r8 - r10
            sun.util.calendar.LocalGregorianCalendar$Date r1 = getCalendarDate(r10)
            sun.util.calendar.Era r3 = r1.getEra()
            sun.util.calendar.LocalGregorianCalendar$Date r13 = r0.jdate
            sun.util.calendar.Era r13 = r13.getEra()
            if (r3 != r13) goto L_0x0308
            int r3 = r1.getYear()
            sun.util.calendar.LocalGregorianCalendar$Date r13 = r0.jdate
            int r13 = r13.getYear()
            if (r3 == r13) goto L_0x030a
        L_0x0308:
            int r4 = r4 + 1
        L_0x030a:
            int r3 = r5 - r7
            r13 = 7
            int r3 = r3 * r13
            long r13 = (long) r3
            long r8 = r8 + r13
            sun.util.calendar.LocalGregorianCalendar r3 = jcal
            r3.getCalendarDateFromFixedDate(r1, r8)
            sun.util.calendar.Era r3 = r1.getEra()
            sun.util.calendar.LocalGregorianCalendar$Date r8 = r0.jdate
            sun.util.calendar.Era r8 = r8.getEra()
            if (r3 != r8) goto L_0x032d
            int r1 = r1.getYear()
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getYear()
            if (r1 == r3) goto L_0x032f
        L_0x032d:
            int r5 = r5 + -1
        L_0x032f:
            int r1 = getRolledValue(r7, r2, r4, r5)
            int r1 = r1 - r6
            r2 = 7
            int r1 = r1 * r2
            long r1 = (long) r1
            long r10 = r10 + r1
            sun.util.calendar.LocalGregorianCalendar$Date r1 = getCalendarDate(r10)
            int r2 = r1.getMonth()
            int r2 = r2 - r6
            r0.set(r12, r2)
            int r1 = r1.getDayOfMonth()
            r2 = 5
            r0.set(r2, r1)
            return
        L_0x034d:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getNormalizedYear()
            boolean r3 = r0.isTransitionYear(r3)
            if (r3 != 0) goto L_0x047d
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = r3.getYear()
            int r7 = r0.getMaximum(r6)
            if (r3 != r7) goto L_0x03e1
            sun.util.calendar.LocalGregorianCalendar r3 = jcal
            long r7 = r0.time
            java.util.TimeZone r5 = r16.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r3.getCalendarDate((long) r7, (java.util.TimeZone) r5)
            r7 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            java.util.TimeZone r9 = r16.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r7 = r3.getCalendarDate((long) r7, (java.util.TimeZone) r9)
            int r8 = r7.getMonth()
            int r8 = r8 - r6
            int r1 = r16.internalGet(r17)
            int r1 = getRolledValue(r1, r2, r4, r8)
            if (r1 != r8) goto L_0x03dc
            r2 = -400(0xfffffffffffffe70, float:NaN)
            r5.addYear(r2)
            int r2 = r1 + 1
            r5.setMonth(r2)
            int r4 = r5.getDayOfMonth()
            int r8 = r7.getDayOfMonth()
            if (r4 <= r8) goto L_0x03ab
            int r4 = r7.getDayOfMonth()
            r5.setDayOfMonth(r4)
            r3.normalize(r5)
        L_0x03ab:
            int r4 = r5.getDayOfMonth()
            int r8 = r7.getDayOfMonth()
            if (r4 != r8) goto L_0x03d4
            long r8 = r5.getTimeOfDay()
            long r10 = r7.getTimeOfDay()
            int r4 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r4 <= 0) goto L_0x03d4
            r5.setMonth(r2)
            int r1 = r7.getDayOfMonth()
            int r1 = r1 - r6
            r5.setDayOfMonth(r1)
            r3.normalize(r5)
            int r1 = r5.getMonth()
            int r1 = r1 - r6
        L_0x03d4:
            int r2 = r5.getDayOfMonth()
            r3 = 5
            r0.set(r3, r2)
        L_0x03dc:
            r0.set(r12, r1)
            goto L_0x050c
        L_0x03e1:
            int r4 = r0.getMinimum(r6)
            if (r3 != r4) goto L_0x0460
            sun.util.calendar.LocalGregorianCalendar r3 = jcal
            long r7 = r0.time
            java.util.TimeZone r4 = r16.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r4 = r3.getCalendarDate((long) r7, (java.util.TimeZone) r4)
            java.util.TimeZone r7 = r16.getZone()
            r8 = -9223372036854775808
            sun.util.calendar.LocalGregorianCalendar$Date r7 = r3.getCalendarDate((long) r8, (java.util.TimeZone) r7)
            int r8 = r7.getMonth()
            int r8 = r8 - r6
            int r1 = r16.internalGet(r17)
            int r1 = getRolledValue(r1, r2, r8, r5)
            if (r1 != r8) goto L_0x045b
            r2 = 400(0x190, float:5.6E-43)
            r4.addYear(r2)
            int r2 = r1 + 1
            r4.setMonth(r2)
            int r5 = r4.getDayOfMonth()
            int r8 = r7.getDayOfMonth()
            if (r5 >= r8) goto L_0x042a
            int r5 = r7.getDayOfMonth()
            r4.setDayOfMonth(r5)
            r3.normalize(r4)
        L_0x042a:
            int r5 = r4.getDayOfMonth()
            int r8 = r7.getDayOfMonth()
            if (r5 != r8) goto L_0x0453
            long r8 = r4.getTimeOfDay()
            long r10 = r7.getTimeOfDay()
            int r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r5 >= 0) goto L_0x0453
            r4.setMonth(r2)
            int r1 = r7.getDayOfMonth()
            int r1 = r1 + r6
            r4.setDayOfMonth(r1)
            r3.normalize(r4)
            int r1 = r4.getMonth()
            int r1 = r1 - r6
        L_0x0453:
            int r2 = r4.getDayOfMonth()
            r3 = 5
            r0.set(r3, r2)
        L_0x045b:
            r0.set(r12, r1)
            goto L_0x050c
        L_0x0460:
            int r1 = r0.internalGet(r12)
            int r1 = r1 + r2
            int r1 = r1 % r11
            if (r1 >= 0) goto L_0x046a
            int r1 = r1 + 12
        L_0x046a:
            r0.set(r12, r1)
            int r1 = r0.monthLength(r1)
            r2 = 5
            int r3 = r0.internalGet(r2)
            if (r3 <= r1) goto L_0x050c
            r0.set(r2, r1)
            goto L_0x050c
        L_0x047d:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = getEraIndex(r3)
            sun.util.calendar.LocalGregorianCalendar$Date r7 = r0.jdate
            int r7 = r7.getYear()
            if (r7 != r6) goto L_0x0499
            sun.util.calendar.Era[] r4 = eras
            r3 = r4[r3]
            sun.util.calendar.CalendarDate r3 = r3.getSinceDate()
            int r4 = r3.getMonth()
            int r4 = r4 - r6
            goto L_0x04c1
        L_0x0499:
            sun.util.calendar.Era[] r7 = eras
            int r8 = r7.length
            int r8 = r8 - r6
            if (r3 >= r8) goto L_0x04c0
            int r3 = r3 + r6
            r3 = r7[r3]
            sun.util.calendar.CalendarDate r3 = r3.getSinceDate()
            int r7 = r3.getYear()
            sun.util.calendar.LocalGregorianCalendar$Date r8 = r0.jdate
            int r8 = r8.getNormalizedYear()
            if (r7 != r8) goto L_0x04c1
            int r5 = r3.getMonth()
            int r5 = r5 - r6
            int r7 = r3.getDayOfMonth()
            if (r7 != r6) goto L_0x04c1
            int r5 = r5 + -1
            goto L_0x04c1
        L_0x04c0:
            r3 = 0
        L_0x04c1:
            if (r4 != r5) goto L_0x04c4
            return
        L_0x04c4:
            int r1 = r16.internalGet(r17)
            int r1 = getRolledValue(r1, r2, r4, r5)
            r0.set(r12, r1)
            if (r1 != r4) goto L_0x04f2
            int r1 = r3.getMonth()
            if (r1 != r6) goto L_0x04dd
            int r1 = r3.getDayOfMonth()
            if (r1 == r6) goto L_0x050c
        L_0x04dd:
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r0.jdate
            int r1 = r1.getDayOfMonth()
            int r2 = r3.getDayOfMonth()
            if (r1 >= r2) goto L_0x050c
            int r1 = r3.getDayOfMonth()
            r2 = 5
            r0.set(r2, r1)
            goto L_0x050c
        L_0x04f2:
            if (r1 != r5) goto L_0x050c
            int r2 = r3.getMonth()
            int r2 = r2 - r6
            if (r2 != r1) goto L_0x050c
            int r1 = r3.getDayOfMonth()
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r0.jdate
            int r2 = r2.getDayOfMonth()
            if (r2 < r1) goto L_0x050c
            int r1 = r1 - r6
            r2 = 5
            r0.set(r2, r1)
        L_0x050c:
            return
        L_0x050d:
            int r4 = r16.getActualMinimum(r17)
            int r5 = r16.getActualMaximum(r17)
        L_0x0515:
            int r3 = r16.internalGet(r17)
            int r2 = getRolledValue(r3, r2, r4, r5)
            r0.set(r1, r2)
            return
        L_0x0521:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.JapaneseImperialCalendar.roll(int, int):void");
    }

    public String getDisplayName(int i, int i2, Locale locale) {
        if (!checkDisplayNameParams(i, i2, 1, 4, locale, 647)) {
            return null;
        }
        int i3 = get(i);
        if (i == 1 && (getBaseStyle(i2) != 2 || i3 != 1 || get(0) == 0)) {
            return null;
        }
        String retrieveFieldValueName = CalendarDataUtility.retrieveFieldValueName(getCalendarType(), i, i3, i2, locale);
        if ((retrieveFieldValueName != null && !retrieveFieldValueName.isEmpty()) || i != 0) {
            return retrieveFieldValueName;
        }
        Era[] eraArr = eras;
        if (i3 >= eraArr.length) {
            return retrieveFieldValueName;
        }
        Era era = eraArr[i3];
        return i2 == 1 ? era.getAbbreviation() : era.getName();
    }

    public Map<String, Integer> getDisplayNames(int i, int i2, Locale locale) {
        if (!checkDisplayNameParams(i, i2, 0, 4, locale, 647)) {
            return null;
        }
        Map<String, Integer> retrieveFieldValueNames = CalendarDataUtility.retrieveFieldValueNames(getCalendarType(), i, i2, locale);
        if (retrieveFieldValueNames != null && i == 0) {
            int size = retrieveFieldValueNames.size();
            if (i2 == 0) {
                HashSet hashSet = new HashSet();
                for (String str : retrieveFieldValueNames.keySet()) {
                    hashSet.add(retrieveFieldValueNames.get(str));
                }
                size = hashSet.size();
            }
            if (size < eras.length) {
                int baseStyle = getBaseStyle(i2);
                int i3 = 0;
                while (true) {
                    Era[] eraArr = eras;
                    if (i3 >= eraArr.length) {
                        break;
                    }
                    if (!retrieveFieldValueNames.values().contains(Integer.valueOf(i3))) {
                        Era era = eraArr[i3];
                        if (baseStyle == 0 || baseStyle == 1 || baseStyle == 4) {
                            retrieveFieldValueNames.put(era.getAbbreviation(), Integer.valueOf(i3));
                        }
                        if (baseStyle == 0 || baseStyle == 2) {
                            retrieveFieldValueNames.put(era.getName(), Integer.valueOf(i3));
                        }
                    }
                    i3++;
                }
            }
        }
        return retrieveFieldValueNames;
    }

    public int getMinimum(int i) {
        return MIN_VALUES[i];
    }

    public int getMaximum(int i) {
        if (i != 1) {
            return MAX_VALUES[i];
        }
        return Math.max(LEAST_MAX_VALUES[1], jcal.getCalendarDate(Long.MAX_VALUE, getZone()).getYear());
    }

    public int getGreatestMinimum(int i) {
        if (i == 1) {
            return 1;
        }
        return MIN_VALUES[i];
    }

    public int getLeastMaximum(int i) {
        if (i != 1) {
            return LEAST_MAX_VALUES[i];
        }
        return Math.min(LEAST_MAX_VALUES[1], getMaximum(1));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00a2, code lost:
        if (r0.getDayOfMonth() < r10.getDayOfMonth()) goto L_0x00a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00a4, code lost:
        r11 = r11 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00fb, code lost:
        if (getYearOffsetInMillis(r0) < getYearOffsetInMillis(r2)) goto L_0x00a4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        return r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getActualMinimum(int r11) {
        /*
            r10 = this;
            r0 = 14
            boolean r0 = isFieldSet(r0, r11)
            if (r0 != 0) goto L_0x000d
            int r10 = r10.getMinimum(r11)
            return r10
        L_0x000d:
            java.util.JapaneseImperialCalendar r0 = r10.getNormalizedCalendar()
            sun.util.calendar.LocalGregorianCalendar r1 = jcal
            long r2 = r0.getTimeInMillis()
            java.util.TimeZone r0 = r10.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r0 = r1.getCalendarDate((long) r2, (java.util.TimeZone) r0)
            int r2 = getEraIndex(r0)
            r3 = 400(0x190, float:5.6E-43)
            r4 = -9223372036854775808
            r6 = 2
            r7 = 1
            if (r11 == r7) goto L_0x00aa
            if (r11 == r6) goto L_0x0079
            r2 = 3
            if (r11 == r2) goto L_0x0032
            goto L_0x00a8
        L_0x0032:
            java.util.TimeZone r11 = r10.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r11 = r1.getCalendarDate((long) r4, (java.util.TimeZone) r11)
            r11.addYear(r3)
            r1.normalize(r11)
            sun.util.calendar.Era r2 = r11.getEra()
            r0.setEra((sun.util.calendar.Era) r2)
            int r2 = r11.getYear()
            r0.setYear((int) r2)
            r1.normalize(r0)
            long r2 = r1.getFixedDate(r11)
            long r4 = r1.getFixedDate(r0)
            int r10 = r10.getWeekNumber(r2, r4)
            int r10 = r10 - r7
            int r10 = r10 * 7
            long r8 = (long) r10
            long r4 = r4 - r8
            int r10 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r10 < 0) goto L_0x00fe
            if (r10 != 0) goto L_0x0076
            long r0 = r0.getTimeOfDay()
            long r10 = r11.getTimeOfDay()
            int r10 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
            if (r10 >= 0) goto L_0x0076
            goto L_0x00fe
        L_0x0076:
            r6 = r7
            goto L_0x00fe
        L_0x0079:
            if (r2 <= r7) goto L_0x00a8
            int r11 = r0.getYear()
            if (r11 != r7) goto L_0x00a8
            sun.util.calendar.Era[] r11 = eras
            r11 = r11[r2]
            java.util.TimeZone r2 = r10.getZone()
            long r2 = r11.getSince(r2)
            java.util.TimeZone r10 = r10.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r10 = r1.getCalendarDate((long) r2, (java.util.TimeZone) r10)
            int r11 = r10.getMonth()
            int r11 = r11 - r7
            int r0 = r0.getDayOfMonth()
            int r10 = r10.getDayOfMonth()
            if (r0 >= r10) goto L_0x00a6
        L_0x00a4:
            int r11 = r11 + 1
        L_0x00a6:
            r6 = r11
            goto L_0x00fe
        L_0x00a8:
            r6 = 0
            goto L_0x00fe
        L_0x00aa:
            if (r2 <= 0) goto L_0x00d7
            sun.util.calendar.Era[] r11 = eras
            r11 = r11[r2]
            java.util.TimeZone r2 = r10.getZone()
            long r2 = r11.getSince(r2)
            java.util.TimeZone r11 = r10.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r11 = r1.getCalendarDate((long) r2, (java.util.TimeZone) r11)
            int r2 = r11.getYear()
            r0.setYear((int) r2)
            r1.normalize(r0)
            long r0 = r10.getYearOffsetInMillis(r0)
            long r10 = r10.getYearOffsetInMillis(r11)
            int r10 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
            if (r10 >= 0) goto L_0x0076
            goto L_0x00fe
        L_0x00d7:
            int r11 = r10.getMinimum(r11)
            java.util.TimeZone r2 = r10.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r1.getCalendarDate((long) r4, (java.util.TimeZone) r2)
            int r4 = r2.getYear()
            if (r4 <= r3) goto L_0x00eb
            int r4 = r4 + -400
        L_0x00eb:
            r0.setYear((int) r4)
            r1.normalize(r0)
            long r0 = r10.getYearOffsetInMillis(r0)
            long r2 = r10.getYearOffsetInMillis(r2)
            int r10 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r10 >= 0) goto L_0x00a6
            goto L_0x00a4
        L_0x00fe:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.JapaneseImperialCalendar.getActualMinimum(int):int");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r14v12, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.util.JapaneseImperialCalendar} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getActualMaximum(int r14) {
        /*
            r13 = this;
            r0 = 1
            int r1 = r0 << r14
            r2 = 130689(0x1fe81, float:1.83134E-40)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x000e
            int r13 = r13.getMaximum(r14)
            return r13
        L_0x000e:
            java.util.JapaneseImperialCalendar r1 = r13.getNormalizedCalendar()
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r1.jdate
            r2.getNormalizedYear()
            r3 = -9223372036854775808
            r5 = 400(0x190, float:5.6E-43)
            r6 = 1
            r8 = 3
            r9 = 6
            r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r12 = 7
            switch(r14) {
                case 1: goto L_0x02a5;
                case 2: goto L_0x024a;
                case 3: goto L_0x0160;
                case 4: goto L_0x00f4;
                case 5: goto L_0x00ec;
                case 6: goto L_0x0052;
                case 7: goto L_0x0028;
                case 8: goto L_0x002e;
                default: goto L_0x0028;
            }
        L_0x0028:
            java.lang.ArrayIndexOutOfBoundsException r13 = new java.lang.ArrayIndexOutOfBoundsException
            r13.<init>((int) r14)
            throw r13
        L_0x002e:
            int r13 = r2.getDayOfWeek()
            java.lang.Object r14 = r2.clone()
            sun.util.calendar.BaseCalendar$Date r14 = (sun.util.calendar.BaseCalendar.Date) r14
            sun.util.calendar.LocalGregorianCalendar r1 = jcal
            int r2 = r1.getMonthLength(r14)
            r14.setDayOfMonth(r0)
            r1.normalize(r14)
            int r14 = r14.getDayOfWeek()
            int r13 = r13 - r14
            if (r13 >= 0) goto L_0x004d
            int r13 = r13 + 7
        L_0x004d:
            int r2 = r2 - r13
            int r2 = r2 + r9
            int r2 = r2 / r12
            goto L_0x02fd
        L_0x0052:
            int r14 = r2.getNormalizedYear()
            boolean r14 = r13.isTransitionYear(r14)
            if (r14 == 0) goto L_0x0094
            int r13 = getEraIndex(r2)
            int r14 = r2.getYear()
            if (r14 == r0) goto L_0x0068
            int r13 = r13 + 1
        L_0x0068:
            long[] r14 = sinceFixedDates
            r13 = r14[r13]
            long r3 = r1.cachedFixedDate
            sun.util.calendar.Gregorian r1 = gcal
            java.util.TimeZone r5 = java.util.TimeZone.NO_TIMEZONE
            sun.util.calendar.Gregorian$Date r5 = r1.newCalendarDate((java.util.TimeZone) r5)
            int r2 = r2.getNormalizedYear()
            r5.setDate(r2, r0, r0)
            int r2 = (r3 > r13 ? 1 : (r3 == r13 ? 0 : -1))
            if (r2 >= 0) goto L_0x0088
            long r0 = r1.getFixedDate(r5)
            long r13 = r13 - r0
        L_0x0086:
            int r13 = (int) r13
            goto L_0x0091
        L_0x0088:
            r5.addYear(r0)
            long r0 = r1.getFixedDate(r5)
            long r0 = r0 - r13
            int r13 = (int) r0
        L_0x0091:
            r2 = r13
            goto L_0x02fd
        L_0x0094:
            sun.util.calendar.LocalGregorianCalendar r14 = jcal
            java.util.TimeZone r1 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r14.getCalendarDate((long) r10, (java.util.TimeZone) r1)
            sun.util.calendar.Era r5 = r2.getEra()
            sun.util.calendar.Era r6 = r1.getEra()
            if (r5 != r6) goto L_0x00be
            int r5 = r2.getYear()
            int r6 = r1.getYear()
            if (r5 != r6) goto L_0x00be
            long r2 = r14.getFixedDate(r1)
            long r13 = r13.getFixedDateJan1(r1, r2)
            long r2 = r2 - r13
            int r13 = (int) r2
            int r13 = r13 + r0
            goto L_0x0091
        L_0x00be:
            int r1 = r2.getYear()
            int r5 = r13.getMinimum(r0)
            if (r1 != r5) goto L_0x00e7
            java.util.TimeZone r13 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r13 = r14.getCalendarDate((long) r3, (java.util.TimeZone) r13)
            long r1 = r14.getFixedDate(r13)
            r13.addYear(r0)
            sun.util.calendar.CalendarDate r3 = r13.setMonth(r0)
            r3.setDayOfMonth(r0)
            r14.normalize(r13)
            long r13 = r14.getFixedDate(r13)
            long r13 = r13 - r1
            goto L_0x0086
        L_0x00e7:
            int r13 = r14.getYearLength(r2)
            goto L_0x0091
        L_0x00ec:
            sun.util.calendar.LocalGregorianCalendar r13 = jcal
            int r2 = r13.getMonthLength(r2)
            goto L_0x02fd
        L_0x00f4:
            sun.util.calendar.LocalGregorianCalendar r14 = jcal
            java.util.TimeZone r1 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r14.getCalendarDate((long) r10, (java.util.TimeZone) r1)
            sun.util.calendar.Era r3 = r2.getEra()
            sun.util.calendar.Era r4 = r1.getEra()
            if (r3 != r4) goto L_0x0125
            int r3 = r2.getYear()
            int r4 = r1.getYear()
            if (r3 == r4) goto L_0x0113
            goto L_0x0125
        L_0x0113:
            long r2 = r14.getFixedDate(r1)
            int r14 = r1.getDayOfMonth()
            long r0 = (long) r14
            long r0 = r2 - r0
            long r0 = r0 + r6
            int r13 = r13.getWeekNumber(r0, r2)
            goto L_0x0091
        L_0x0125:
            sun.util.calendar.Gregorian r14 = gcal
            java.util.TimeZone r1 = java.util.TimeZone.NO_TIMEZONE
            sun.util.calendar.Gregorian$Date r1 = r14.newCalendarDate((java.util.TimeZone) r1)
            int r3 = r2.getNormalizedYear()
            int r2 = r2.getMonth()
            r1.setDate(r3, r2, r0)
            int r0 = r14.getDayOfWeek(r1)
            int r14 = r14.getMonthLength(r1)
            int r1 = r13.getFirstDayOfWeek()
            int r0 = r0 - r1
            if (r0 >= 0) goto L_0x0149
            int r0 = r0 + 7
        L_0x0149:
            int r0 = 7 - r0
            int r13 = r13.getMinimalDaysInFirstWeek()
            if (r0 < r13) goto L_0x0152
            r8 = 4
        L_0x0152:
            int r0 = r0 + 21
            int r14 = r14 - r0
            if (r14 <= 0) goto L_0x015d
            int r8 = r8 + 1
            if (r14 <= r12) goto L_0x015d
            int r8 = r8 + 1
        L_0x015d:
            r2 = r8
            goto L_0x02fd
        L_0x0160:
            int r14 = r2.getNormalizedYear()
            boolean r14 = r13.isTransitionYear(r14)
            if (r14 != 0) goto L_0x0225
            sun.util.calendar.LocalGregorianCalendar r14 = jcal
            java.util.TimeZone r1 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r14.getCalendarDate((long) r10, (java.util.TimeZone) r1)
            sun.util.calendar.Era r6 = r2.getEra()
            sun.util.calendar.Era r7 = r1.getEra()
            if (r6 != r7) goto L_0x0196
            int r6 = r2.getYear()
            int r7 = r1.getYear()
            if (r6 != r7) goto L_0x0196
            long r2 = r14.getFixedDate(r1)
            long r0 = r13.getFixedDateJan1(r1, r2)
            int r2 = r13.getWeekNumber(r0, r2)
            goto L_0x02fd
        L_0x0196:
            sun.util.calendar.Era r6 = r2.getEra()
            if (r6 != 0) goto L_0x01eb
            int r6 = r2.getYear()
            int r7 = r13.getMinimum(r0)
            if (r6 != r7) goto L_0x01eb
            java.util.TimeZone r2 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r14.getCalendarDate((long) r3, (java.util.TimeZone) r2)
            r2.addYear(r5)
            r14.normalize(r2)
            sun.util.calendar.Era r3 = r2.getEra()
            r1.setEra((sun.util.calendar.Era) r3)
            int r3 = r2.getYear()
            int r3 = r3 + r0
            r1.setDate(r3, r0, r0)
            r14.normalize(r1)
            long r2 = r14.getFixedDate(r2)
            long r0 = r14.getFixedDate(r1)
            r4 = 6
            long r4 = r4 + r0
            int r14 = r13.getFirstDayOfWeek()
            long r4 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r4, r14)
            long r0 = r4 - r0
            int r14 = (int) r0
            int r0 = r13.getMinimalDaysInFirstWeek()
            if (r14 < r0) goto L_0x01e5
            r0 = 7
            long r4 = r4 - r0
        L_0x01e5:
            int r2 = r13.getWeekNumber(r2, r4)
            goto L_0x02fd
        L_0x01eb:
            sun.util.calendar.Gregorian r14 = gcal
            java.util.TimeZone r1 = java.util.TimeZone.NO_TIMEZONE
            sun.util.calendar.Gregorian$Date r1 = r14.newCalendarDate((java.util.TimeZone) r1)
            int r3 = r2.getNormalizedYear()
            r1.setDate(r3, r0, r0)
            int r14 = r14.getDayOfWeek(r1)
            int r1 = r13.getFirstDayOfWeek()
            int r14 = r14 - r1
            if (r14 >= 0) goto L_0x0207
            int r14 = r14 + 7
        L_0x0207:
            int r13 = r13.getMinimalDaysInFirstWeek()
            int r14 = r14 + r13
            int r14 = r14 - r0
            if (r14 == r9) goto L_0x0221
            boolean r13 = r2.isLeapYear()
            if (r13 == 0) goto L_0x021d
            r13 = 5
            if (r14 == r13) goto L_0x0221
            r13 = 12
            if (r14 != r13) goto L_0x021d
            goto L_0x0221
        L_0x021d:
            r13 = 52
            goto L_0x0091
        L_0x0221:
            r13 = 53
            goto L_0x0091
        L_0x0225:
            if (r1 != r13) goto L_0x022e
            java.lang.Object r14 = r1.clone()
            r1 = r14
            java.util.JapaneseImperialCalendar r1 = (java.util.JapaneseImperialCalendar) r1
        L_0x022e:
            int r13 = r13.getActualMaximum(r9)
            r1.set(r9, r13)
            int r14 = r1.get(r8)
            if (r14 != r0) goto L_0x0247
            if (r13 <= r12) goto L_0x0247
            r13 = -1
            r1.add(r8, r13)
            int r13 = r1.get(r8)
            goto L_0x0091
        L_0x0247:
            r2 = r14
            goto L_0x02fd
        L_0x024a:
            int r14 = r2.getNormalizedYear()
            boolean r14 = r13.isTransitionYear(r14)
            r3 = 11
            if (r14 == 0) goto L_0x0282
            int r13 = getEraIndex(r2)
            int r14 = r2.getYear()
            if (r14 == r0) goto L_0x0262
            int r13 = r13 + 1
        L_0x0262:
            long[] r14 = sinceFixedDates
            r13 = r14[r13]
            long r4 = r1.cachedFixedDate
            int r1 = (r4 > r13 ? 1 : (r4 == r13 ? 0 : -1))
            if (r1 >= 0) goto L_0x027f
            java.lang.Object r1 = r2.clone()
            sun.util.calendar.LocalGregorianCalendar$Date r1 = (sun.util.calendar.LocalGregorianCalendar.Date) r1
            sun.util.calendar.LocalGregorianCalendar r2 = jcal
            long r13 = r13 - r6
            r2.getCalendarDateFromFixedDate(r1, r13)
            int r13 = r1.getMonth()
        L_0x027c:
            int r13 = r13 - r0
            goto L_0x0091
        L_0x027f:
            r2 = r3
            goto L_0x02fd
        L_0x0282:
            sun.util.calendar.LocalGregorianCalendar r14 = jcal
            java.util.TimeZone r13 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r13 = r14.getCalendarDate((long) r10, (java.util.TimeZone) r13)
            sun.util.calendar.Era r14 = r2.getEra()
            sun.util.calendar.Era r1 = r13.getEra()
            if (r14 != r1) goto L_0x027f
            int r14 = r2.getYear()
            int r1 = r13.getYear()
            if (r14 != r1) goto L_0x027f
            int r13 = r13.getMonth()
            goto L_0x027c
        L_0x02a5:
            sun.util.calendar.LocalGregorianCalendar r14 = jcal
            long r3 = r1.getTimeInMillis()
            java.util.TimeZone r1 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r14.getCalendarDate((long) r3, (java.util.TimeZone) r1)
            int r2 = getEraIndex(r2)
            sun.util.calendar.Era[] r3 = eras
            int r4 = r3.length
            int r4 = r4 - r0
            if (r2 != r4) goto L_0x02d1
            java.util.TimeZone r0 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r0 = r14.getCalendarDate((long) r10, (java.util.TimeZone) r0)
            int r2 = r0.getYear()
            if (r2 <= r5) goto L_0x02ec
            int r3 = r2 + -400
            r1.setYear(r3)
            goto L_0x02ec
        L_0x02d1:
            int r2 = r2 + r0
            r0 = r3[r2]
            java.util.TimeZone r2 = r13.getZone()
            long r2 = r0.getSince(r2)
            long r2 = r2 - r6
            java.util.TimeZone r0 = r13.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r0 = r14.getCalendarDate((long) r2, (java.util.TimeZone) r0)
            int r2 = r0.getYear()
            r1.setYear(r2)
        L_0x02ec:
            r14.normalize(r1)
            long r3 = r13.getYearOffsetInMillis(r1)
            long r13 = r13.getYearOffsetInMillis(r0)
            int r13 = (r3 > r13 ? 1 : (r3 == r13 ? 0 : -1))
            if (r13 <= 0) goto L_0x02fd
            int r2 = r2 + -1
        L_0x02fd:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.JapaneseImperialCalendar.getActualMaximum(int):int");
    }

    private long getYearOffsetInMillis(CalendarDate calendarDate) {
        return (((jcal.getDayOfYear(calendarDate) - 1) * ONE_DAY) + calendarDate.getTimeOfDay()) - ((long) calendarDate.getZoneOffset());
    }

    public Object clone() {
        JapaneseImperialCalendar japaneseImperialCalendar = (JapaneseImperialCalendar) super.clone();
        japaneseImperialCalendar.jdate = (LocalGregorianCalendar.Date) this.jdate.clone();
        japaneseImperialCalendar.originalFields = null;
        japaneseImperialCalendar.zoneOffsets = null;
        return japaneseImperialCalendar;
    }

    public TimeZone getTimeZone() {
        TimeZone timeZone = super.getTimeZone();
        this.jdate.setZone(timeZone);
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        super.setTimeZone(timeZone);
        this.jdate.setZone(timeZone);
    }

    /* access modifiers changed from: protected */
    public void computeFields() {
        int i = 131071;
        if (isPartiallyNormalized()) {
            int setStateFields = getSetStateFields();
            int i2 = 131071 & (~setStateFields);
            if (i2 != 0 || this.cachedFixedDate == Long.MIN_VALUE) {
                setStateFields |= computeFields(i2, 98304 & setStateFields);
            }
            i = setStateFields;
        } else {
            computeFields(131071, 0);
        }
        setFieldsComputed(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:64:0x01d7, code lost:
        if (r6.isLeapYear() != false) goto L_0x01d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0222, code lost:
        if (r6.isLeapYear() != false) goto L_0x01d9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x019d  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x01a4  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x01bd  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0251  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int computeFields(int r22, int r23) {
        /*
            r21 = this;
            r0 = r21
            r1 = r22
            r2 = r23
            java.util.TimeZone r3 = r21.getZone()
            int[] r4 = r0.zoneOffsets
            r5 = 2
            if (r4 != 0) goto L_0x0013
            int[] r4 = new int[r5]
            r0.zoneOffsets = r4
        L_0x0013:
            r4 = 98304(0x18000, float:1.37753E-40)
            r6 = 1
            r7 = 0
            if (r2 == r4) goto L_0x0041
            boolean r8 = r3 instanceof libcore.util.ZoneInfo
            if (r8 == 0) goto L_0x0029
            libcore.util.ZoneInfo r3 = (libcore.util.ZoneInfo) r3
            long r8 = r0.time
            int[] r10 = r0.zoneOffsets
            int r3 = r3.getOffsetsByUtcTime(r8, r10)
            goto L_0x0042
        L_0x0029:
            long r8 = r0.time
            int r8 = r3.getOffset(r8)
            int[] r9 = r0.zoneOffsets
            int r3 = r3.getRawOffset()
            r9[r7] = r3
            int[] r3 = r0.zoneOffsets
            r9 = r3[r7]
            int r9 = r8 - r9
            r3[r6] = r9
            r3 = r8
            goto L_0x0042
        L_0x0041:
            r3 = r7
        L_0x0042:
            r8 = 16
            r9 = 15
            if (r2 == 0) goto L_0x006b
            boolean r3 = isFieldSet(r2, r9)
            if (r3 == 0) goto L_0x0056
            int[] r3 = r0.zoneOffsets
            int r10 = r0.internalGet(r9)
            r3[r7] = r10
        L_0x0056:
            boolean r2 = isFieldSet(r2, r8)
            if (r2 == 0) goto L_0x0064
            int[] r2 = r0.zoneOffsets
            int r3 = r0.internalGet(r8)
            r2[r6] = r3
        L_0x0064:
            int[] r2 = r0.zoneOffsets
            r3 = r2[r7]
            r2 = r2[r6]
            int r3 = r3 + r2
        L_0x006b:
            long r10 = (long) r3
            r12 = 86400000(0x5265c00, double:4.2687272E-316)
            long r10 = r10 / r12
            r2 = 86400000(0x5265c00, float:7.82218E-36)
            int r3 = r3 % r2
            long r14 = r0.time
            long r14 = r14 / r12
            long r10 = r10 + r14
            long r14 = r0.time
            long r14 = r14 % r12
            int r2 = (int) r14
            int r3 = r3 + r2
            long r14 = (long) r3
            int r2 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
            r16 = 1
            if (r2 < 0) goto L_0x0089
            long r14 = r14 - r12
            int r2 = (int) r14
            long r10 = r10 + r16
            goto L_0x0092
        L_0x0089:
            if (r3 >= 0) goto L_0x0091
            long r2 = (long) r3
            long r2 = r2 + r12
            int r3 = (int) r2
            long r10 = r10 - r16
            goto L_0x0089
        L_0x0091:
            r2 = r3
        L_0x0092:
            r12 = 719163(0xaf93b, double:3.553137E-318)
            long r10 = r10 + r12
            long r12 = r0.cachedFixedDate
            int r3 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r3 != 0) goto L_0x00a2
            r12 = 0
            int r3 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r3 >= 0) goto L_0x00ab
        L_0x00a2:
            sun.util.calendar.LocalGregorianCalendar r3 = jcal
            sun.util.calendar.LocalGregorianCalendar$Date r12 = r0.jdate
            r3.getCalendarDateFromFixedDate(r12, r10)
            r0.cachedFixedDate = r10
        L_0x00ab:
            sun.util.calendar.LocalGregorianCalendar$Date r3 = r0.jdate
            int r3 = getEraIndex(r3)
            sun.util.calendar.LocalGregorianCalendar$Date r12 = r0.jdate
            int r12 = r12.getYear()
            r0.internalSet(r7, r3)
            r0.internalSet(r6, r12)
            r12 = r1 | 3
            sun.util.calendar.LocalGregorianCalendar$Date r13 = r0.jdate
            int r13 = r13.getMonth()
            int r13 = r13 - r6
            sun.util.calendar.LocalGregorianCalendar$Date r14 = r0.jdate
            int r14 = r14.getDayOfMonth()
            r15 = r1 & 164(0xa4, float:2.3E-43)
            r8 = 7
            r6 = 5
            if (r15 == 0) goto L_0x00e3
            r0.internalSet(r5, r13)
            r0.internalSet(r6, r14)
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r0.jdate
            int r5 = r5.getDayOfWeek()
            r0.internalSet(r8, r5)
            r12 = r12 | 164(0xa4, float:2.3E-43)
        L_0x00e3:
            r5 = r1 & 32256(0x7e00, float:4.52E-41)
            if (r5 == 0) goto L_0x0137
            r5 = 14
            r13 = 13
            r15 = 10
            r6 = 9
            r8 = 11
            r9 = 12
            if (r2 == 0) goto L_0x011f
            r20 = 3600000(0x36ee80, float:5.044674E-39)
            int r4 = r2 / r20
            r0.internalSet(r8, r4)
            int r8 = r4 / 12
            r0.internalSet(r6, r8)
            int r4 = r4 % r9
            r0.internalSet(r15, r4)
            int r2 = r2 % r20
            r4 = 60000(0xea60, float:8.4078E-41)
            int r4 = r2 / r4
            r0.internalSet(r9, r4)
            r4 = 60000(0xea60, float:8.4078E-41)
            int r2 = r2 % r4
            int r4 = r2 / 1000
            r0.internalSet(r13, r4)
            int r2 = r2 % 1000
            r0.internalSet(r5, r2)
            goto L_0x0131
        L_0x011f:
            r0.internalSet(r8, r7)
            r0.internalSet(r6, r7)
            r0.internalSet(r15, r7)
            r0.internalSet(r9, r7)
            r0.internalSet(r13, r7)
            r0.internalSet(r5, r7)
        L_0x0131:
            r12 = r12 | 32256(0x7e00, float:4.52E-41)
            r2 = 98304(0x18000, float:1.37753E-40)
            goto L_0x0138
        L_0x0137:
            r2 = r4
        L_0x0138:
            r4 = r1 & r2
            if (r4 == 0) goto L_0x0150
            int[] r4 = r0.zoneOffsets
            r4 = r4[r7]
            r5 = 15
            r0.internalSet(r5, r4)
            int[] r4 = r0.zoneOffsets
            r5 = 1
            r4 = r4[r5]
            r5 = 16
            r0.internalSet(r5, r4)
            r12 = r12 | r2
        L_0x0150:
            r1 = r1 & 344(0x158, float:4.82E-43)
            if (r1 == 0) goto L_0x02f3
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r0.jdate
            int r1 = r1.getNormalizedYear()
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r0.jdate
            int r2 = r2.getNormalizedYear()
            boolean r2 = r0.isTransitionYear(r2)
            if (r2 == 0) goto L_0x0172
            sun.util.calendar.LocalGregorianCalendar$Date r4 = r0.jdate
            long r4 = r0.getFixedDateJan1(r4, r10)
            long r6 = r10 - r4
            int r6 = (int) r6
            r7 = 1
        L_0x0170:
            int r6 = r6 + r7
            goto L_0x019b
        L_0x0172:
            r7 = 1
            int[] r4 = MIN_VALUES
            r4 = r4[r7]
            if (r1 != r4) goto L_0x018d
            sun.util.calendar.LocalGregorianCalendar r4 = jcal
            r5 = -9223372036854775808
            java.util.TimeZone r8 = r21.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r4.getCalendarDate((long) r5, (java.util.TimeZone) r8)
            long r4 = r4.getFixedDate(r5)
            long r8 = r10 - r4
            int r6 = (int) r8
            goto L_0x0170
        L_0x018d:
            sun.util.calendar.LocalGregorianCalendar r4 = jcal
            sun.util.calendar.LocalGregorianCalendar$Date r5 = r0.jdate
            long r4 = r4.getDayOfYear(r5)
            int r6 = (int) r4
            long r4 = (long) r6
            long r4 = r10 - r4
            long r4 = r4 + r16
        L_0x019b:
            if (r2 == 0) goto L_0x01a4
            sun.util.calendar.LocalGregorianCalendar$Date r7 = r0.jdate
            long r7 = r0.getFixedDateMonth1(r7, r10)
            goto L_0x01a9
        L_0x01a4:
            long r7 = (long) r14
            long r7 = r10 - r7
            long r7 = r7 + r16
        L_0x01a9:
            r9 = 6
            r0.internalSet(r9, r6)
            r6 = 1
            int r14 = r14 - r6
            r9 = 7
            int r14 = r14 / r9
            int r14 = r14 + r6
            r6 = 8
            r0.internalSet(r6, r14)
            int r6 = r0.getWeekNumber(r4, r10)
            if (r6 != 0) goto L_0x0251
            long r13 = r4 - r16
            sun.util.calendar.LocalGregorianCalendar$Date r6 = getCalendarDate(r13)
            if (r2 != 0) goto L_0x01dc
            int r9 = r6.getNormalizedYear()
            boolean r9 = r0.isTransitionYear(r9)
            if (r9 != 0) goto L_0x01dc
            r18 = 365(0x16d, double:1.803E-321)
            long r4 = r4 - r18
            boolean r1 = r6.isLeapYear()
            if (r1 == 0) goto L_0x024b
        L_0x01d9:
            long r4 = r4 - r16
            goto L_0x024b
        L_0x01dc:
            if (r2 == 0) goto L_0x0225
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r0.jdate
            int r2 = r2.getYear()
            r9 = 1
            if (r2 != r9) goto L_0x021b
            r2 = 5
            if (r3 <= r2) goto L_0x0209
            sun.util.calendar.Era[] r2 = eras
            int r3 = r3 - r9
            r2 = r2[r3]
            sun.util.calendar.CalendarDate r2 = r2.getSinceDate()
            int r3 = r2.getYear()
            if (r1 != r3) goto L_0x0211
            int r1 = r2.getMonth()
            sun.util.calendar.CalendarDate r1 = r6.setMonth(r1)
            int r2 = r2.getDayOfMonth()
            r1.setDayOfMonth(r2)
            goto L_0x0211
        L_0x0209:
            r1 = r9
            sun.util.calendar.CalendarDate r2 = r6.setMonth(r1)
            r2.setDayOfMonth(r1)
        L_0x0211:
            sun.util.calendar.LocalGregorianCalendar r1 = jcal
            r1.normalize(r6)
            long r4 = r1.getFixedDate(r6)
            goto L_0x024b
        L_0x021b:
            r1 = 365(0x16d, double:1.803E-321)
            long r4 = r4 - r1
            boolean r1 = r6.isLeapYear()
            if (r1 == 0) goto L_0x024b
            goto L_0x01d9
        L_0x0225:
            sun.util.calendar.Era[] r1 = eras
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r0.jdate
            int r2 = getEraIndex(r2)
            r1 = r1[r2]
            sun.util.calendar.CalendarDate r1 = r1.getSinceDate()
            int r2 = r1.getMonth()
            sun.util.calendar.CalendarDate r2 = r6.setMonth(r2)
            int r1 = r1.getDayOfMonth()
            r2.setDayOfMonth(r1)
            sun.util.calendar.LocalGregorianCalendar r1 = jcal
            r1.normalize(r6)
            long r4 = r1.getFixedDate(r6)
        L_0x024b:
            int r6 = r0.getWeekNumber(r4, r13)
            goto L_0x02e5
        L_0x0251:
            if (r2 != 0) goto L_0x0281
            r1 = 52
            if (r6 < r1) goto L_0x02e5
            r1 = 365(0x16d, double:1.803E-321)
            long r4 = r4 + r1
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r0.jdate
            boolean r1 = r1.isLeapYear()
            if (r1 == 0) goto L_0x0264
            long r4 = r4 + r16
        L_0x0264:
            r1 = 6
            long r1 = r1 + r4
            int r3 = r21.getFirstDayOfWeek()
            long r1 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r1, r3)
            long r3 = r1 - r4
            int r3 = (int) r3
            int r4 = r21.getMinimalDaysInFirstWeek()
            if (r3 < r4) goto L_0x02e5
            r3 = 7
            long r1 = r1 - r3
            int r1 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r1 < 0) goto L_0x02e5
            r6 = 1
            goto L_0x02e5
        L_0x0281:
            sun.util.calendar.LocalGregorianCalendar$Date r1 = r0.jdate
            java.lang.Object r1 = r1.clone()
            sun.util.calendar.LocalGregorianCalendar$Date r1 = (sun.util.calendar.LocalGregorianCalendar.Date) r1
            sun.util.calendar.LocalGregorianCalendar$Date r2 = r0.jdate
            int r2 = r2.getYear()
            r3 = 1
            if (r2 != r3) goto L_0x02a3
            r1.addYear((int) r3)
            sun.util.calendar.CalendarDate r2 = r1.setMonth(r3)
            r2.setDayOfMonth(r3)
            sun.util.calendar.LocalGregorianCalendar r2 = jcal
            long r1 = r2.getFixedDate(r1)
            goto L_0x02c9
        L_0x02a3:
            int r2 = getEraIndex(r1)
            int r2 = r2 + r3
            sun.util.calendar.Era[] r4 = eras
            r5 = r4[r2]
            sun.util.calendar.CalendarDate r5 = r5.getSinceDate()
            r2 = r4[r2]
            r1.setEra((sun.util.calendar.Era) r2)
            int r2 = r5.getMonth()
            int r4 = r5.getDayOfMonth()
            r1.setDate(r3, r2, r4)
            sun.util.calendar.LocalGregorianCalendar r2 = jcal
            r2.normalize(r1)
            long r1 = r2.getFixedDate(r1)
        L_0x02c9:
            r4 = 6
            long r4 = r4 + r1
            int r9 = r21.getFirstDayOfWeek()
            long r4 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r4, r9)
            long r1 = r4 - r1
            int r1 = (int) r1
            int r2 = r21.getMinimalDaysInFirstWeek()
            if (r1 < r2) goto L_0x02e5
            r1 = 7
            long r4 = r4 - r1
            int r1 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r1 < 0) goto L_0x02e5
            r6 = r3
        L_0x02e5:
            r1 = 3
            r0.internalSet(r1, r6)
            r1 = 4
            int r2 = r0.getWeekNumber(r7, r10)
            r0.internalSet(r1, r2)
            r12 = r12 | 344(0x158, float:4.82E-43)
        L_0x02f3:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.JapaneseImperialCalendar.computeFields(int, int):int");
    }

    private int getWeekNumber(long j, long j2) {
        int floorDivide;
        long dayOfWeekDateOnOrBefore = LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(6 + j, getFirstDayOfWeek());
        if (((int) (dayOfWeekDateOnOrBefore - j)) >= getMinimalDaysInFirstWeek()) {
            dayOfWeekDateOnOrBefore -= 7;
        }
        int i = (int) (j2 - dayOfWeekDateOnOrBefore);
        if (i >= 0) {
            floorDivide = i / 7;
        } else {
            floorDivide = CalendarUtils.floorDivide(i, 7);
        }
        return floorDivide + 1;
    }

    /* access modifiers changed from: protected */
    public void computeTime() {
        int i;
        int i2;
        long j;
        if (!isLenient()) {
            if (this.originalFields == null) {
                this.originalFields = new int[17];
            }
            int i3 = 0;
            while (i3 < 17) {
                int internalGet = internalGet(i3);
                if (!isExternallySet(i3) || (internalGet >= getMinimum(i3) && internalGet <= getMaximum(i3))) {
                    this.originalFields[i3] = internalGet;
                    i3++;
                } else {
                    throw new IllegalArgumentException(getFieldName(i3));
                }
            }
        }
        int selectFields = selectFields();
        if (isSet(0)) {
            i2 = internalGet(0);
            i = isSet(1) ? internalGet(1) : 1;
        } else if (isSet(1)) {
            i2 = currentEra;
            i = internalGet(1);
        } else {
            i2 = 3;
            i = 45;
        }
        if (isFieldSet(selectFields, 11)) {
            j = ((long) internalGet(11)) + 0;
        } else {
            j = ((long) internalGet(10)) + 0;
            if (isFieldSet(selectFields, 9)) {
                j += (long) (internalGet(9) * 12);
            }
        }
        long internalGet2 = (((((j * 60) + ((long) internalGet(12))) * 60) + ((long) internalGet(13))) * 1000) + ((long) internalGet(14));
        long j2 = internalGet2 / ONE_DAY;
        long j3 = internalGet2 % ONE_DAY;
        while (j3 < 0) {
            j3 += ONE_DAY;
            j2--;
        }
        long fixedDate = (((j2 + getFixedDate(i2, i, selectFields)) - 719163) * ONE_DAY) + j3;
        TimeZone zone = getZone();
        if (this.zoneOffsets == null) {
            this.zoneOffsets = new int[2];
        }
        int i4 = selectFields & 98304;
        if (i4 != 98304) {
            zone.getOffsets(fixedDate - ((long) zone.getRawOffset()), this.zoneOffsets);
        }
        if (i4 != 0) {
            if (isFieldSet(i4, 15)) {
                this.zoneOffsets[0] = internalGet(15);
            }
            if (isFieldSet(i4, 16)) {
                this.zoneOffsets[1] = internalGet(16);
            }
        }
        int[] iArr = this.zoneOffsets;
        this.time = fixedDate - ((long) (iArr[0] + iArr[1]));
        int computeFields = computeFields(selectFields | getSetStateFields(), i4);
        if (!isLenient()) {
            for (int i5 = 0; i5 < 17; i5++) {
                if (isExternallySet(i5) && this.originalFields[i5] != internalGet(i5)) {
                    int internalGet3 = internalGet(i5);
                    System.arraycopy((Object) this.originalFields, 0, (Object) this.fields, 0, this.fields.length);
                    throw new IllegalArgumentException(getFieldName(i5) + "=" + internalGet3 + ", expected " + this.originalFields[i5]);
                }
            }
        }
        setFieldsNormalized(computeFields);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0116  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long getFixedDate(int r18, int r19, int r20) {
        /*
            r17 = this;
            r0 = r17
            r1 = r19
            r2 = r20
            r3 = 2
            boolean r4 = isFieldSet(r2, r3)
            r5 = 0
            r6 = 1
            if (r4 == 0) goto L_0x002d
            int r4 = r0.internalGet(r3)
            r7 = 11
            if (r4 <= r7) goto L_0x001d
            int r5 = r4 / 12
            int r1 = r1 + r5
            int r5 = r4 % 12
            goto L_0x0043
        L_0x001d:
            if (r4 >= 0) goto L_0x002b
            int[] r7 = new int[r6]
            r8 = 12
            int r4 = sun.util.calendar.CalendarUtils.floorDivide((int) r4, (int) r8, (int[]) r7)
            int r1 = r1 + r4
            r5 = r7[r5]
            goto L_0x0043
        L_0x002b:
            r5 = r4
            goto L_0x0043
        L_0x002d:
            if (r1 != r6) goto L_0x0043
            if (r18 == 0) goto L_0x0043
            sun.util.calendar.Era[] r4 = eras
            r4 = r4[r18]
            sun.util.calendar.CalendarDate r4 = r4.getSinceDate()
            int r5 = r4.getMonth()
            int r5 = r5 - r6
            int r4 = r4.getDayOfMonth()
            goto L_0x0044
        L_0x0043:
            r4 = r6
        L_0x0044:
            int[] r7 = MIN_VALUES
            r7 = r7[r6]
            if (r1 != r7) goto L_0x0064
            sun.util.calendar.LocalGregorianCalendar r7 = jcal
            r8 = -9223372036854775808
            java.util.TimeZone r10 = r17.getZone()
            sun.util.calendar.LocalGregorianCalendar$Date r7 = r7.getCalendarDate((long) r8, (java.util.TimeZone) r10)
            int r8 = r7.getMonth()
            int r8 = r8 - r6
            if (r5 >= r8) goto L_0x005e
            r5 = r8
        L_0x005e:
            if (r5 != r8) goto L_0x0064
            int r4 = r7.getDayOfMonth()
        L_0x0064:
            sun.util.calendar.LocalGregorianCalendar r7 = jcal
            java.util.TimeZone r8 = java.util.TimeZone.NO_TIMEZONE
            sun.util.calendar.LocalGregorianCalendar$Date r8 = r7.newCalendarDate((java.util.TimeZone) r8)
            if (r18 <= 0) goto L_0x0073
            sun.util.calendar.Era[] r9 = eras
            r9 = r9[r18]
            goto L_0x0074
        L_0x0073:
            r9 = 0
        L_0x0074:
            r8.setEra((sun.util.calendar.Era) r9)
            int r9 = r5 + 1
            r8.setDate(r1, r9, r4)
            r7.normalize(r8)
            long r9 = r7.getFixedDate(r8)
            boolean r3 = isFieldSet(r2, r3)
            r11 = 7
            r13 = 1
            r15 = 6
            r7 = 7
            if (r3 == 0) goto L_0x0116
            r3 = 5
            boolean r8 = isFieldSet(r2, r3)
            if (r8 == 0) goto L_0x00a7
            boolean r1 = r0.isSet(r3)
            if (r1 == 0) goto L_0x0168
            int r0 = r0.internalGet(r3)
            long r0 = (long) r0
            long r9 = r9 + r0
            long r0 = (long) r4
            long r9 = r9 - r0
            goto L_0x0168
        L_0x00a7:
            r3 = 4
            boolean r4 = isFieldSet(r2, r3)
            if (r4 == 0) goto L_0x00de
            long r4 = r9 + r15
            int r1 = r17.getFirstDayOfWeek()
            long r4 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r4, r1)
            long r8 = r4 - r9
            int r1 = r17.getMinimalDaysInFirstWeek()
            long r13 = (long) r1
            int r1 = (r8 > r13 ? 1 : (r8 == r13 ? 0 : -1))
            if (r1 < 0) goto L_0x00c4
            long r4 = r4 - r11
        L_0x00c4:
            boolean r1 = isFieldSet(r2, r7)
            if (r1 == 0) goto L_0x00d3
            long r4 = r4 + r15
            int r1 = r0.internalGet(r7)
            long r4 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r4, r1)
        L_0x00d3:
            int r0 = r0.internalGet(r3)
            int r0 = r0 - r6
            int r0 = r0 * r7
            long r0 = (long) r0
            long r9 = r4 + r0
            goto L_0x0168
        L_0x00de:
            boolean r3 = isFieldSet(r2, r7)
            if (r3 == 0) goto L_0x00e9
            int r3 = r0.internalGet(r7)
            goto L_0x00ed
        L_0x00e9:
            int r3 = r17.getFirstDayOfWeek()
        L_0x00ed:
            r4 = 8
            boolean r2 = isFieldSet(r2, r4)
            if (r2 == 0) goto L_0x00fa
            int r2 = r0.internalGet(r4)
            goto L_0x00fb
        L_0x00fa:
            r2 = r6
        L_0x00fb:
            if (r2 < 0) goto L_0x0106
            int r2 = r2 * r7
            long r0 = (long) r2
            long r9 = r9 + r0
            long r9 = r9 - r13
            long r0 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r9, r3)
            goto L_0x0114
        L_0x0106:
            int r0 = r0.monthLength(r5, r1)
            int r2 = r2 + r6
            int r2 = r2 * r7
            int r0 = r0 + r2
            long r0 = (long) r0
            long r9 = r9 + r0
            long r9 = r9 - r13
            long r0 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r9, r3)
        L_0x0114:
            r9 = r0
            goto L_0x0168
        L_0x0116:
            r1 = 6
            boolean r3 = isFieldSet(r2, r1)
            if (r3 == 0) goto L_0x0133
            int r2 = r8.getNormalizedYear()
            boolean r2 = r0.isTransitionYear(r2)
            if (r2 == 0) goto L_0x012b
            long r9 = r0.getFixedDateJan1(r8, r9)
        L_0x012b:
            int r0 = r0.internalGet(r1)
            long r0 = (long) r0
            long r9 = r9 + r0
            long r9 = r9 - r13
            goto L_0x0168
        L_0x0133:
            long r3 = r9 + r15
            int r1 = r17.getFirstDayOfWeek()
            long r3 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r3, r1)
            long r5 = r3 - r9
            int r1 = r17.getMinimalDaysInFirstWeek()
            long r8 = (long) r1
            int r1 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1))
            if (r1 < 0) goto L_0x0149
            long r3 = r3 - r11
        L_0x0149:
            boolean r1 = isFieldSet(r2, r7)
            if (r1 == 0) goto L_0x015e
            int r1 = r0.internalGet(r7)
            int r2 = r17.getFirstDayOfWeek()
            if (r1 == r2) goto L_0x015e
            long r3 = r3 + r15
            long r3 = sun.util.calendar.LocalGregorianCalendar.getDayOfWeekDateOnOrBefore(r3, r1)
        L_0x015e:
            r1 = 3
            int r0 = r0.internalGet(r1)
            long r0 = (long) r0
            long r0 = r0 - r13
            long r0 = r0 * r11
            long r9 = r3 + r0
        L_0x0168:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.JapaneseImperialCalendar.getFixedDate(int, int, int):long");
    }

    private long getFixedDateJan1(LocalGregorianCalendar.Date date, long j) {
        date.getEra();
        if (date.getEra() != null && date.getYear() == 1) {
            for (int eraIndex = getEraIndex(date); eraIndex > 0; eraIndex--) {
                long fixedDate = gcal.getFixedDate(eras[eraIndex].getSinceDate());
                if (fixedDate <= j) {
                    return fixedDate;
                }
            }
        }
        Gregorian gregorian = gcal;
        Gregorian.Date newCalendarDate = gregorian.newCalendarDate(TimeZone.NO_TIMEZONE);
        newCalendarDate.setDate(date.getNormalizedYear(), 1, 1);
        return gregorian.getFixedDate(newCalendarDate);
    }

    private long getFixedDateMonth1(LocalGregorianCalendar.Date date, long j) {
        int transitionEraIndex = getTransitionEraIndex(date);
        if (transitionEraIndex != -1) {
            long j2 = sinceFixedDates[transitionEraIndex];
            if (j2 <= j) {
                return j2;
            }
        }
        return (j - ((long) date.getDayOfMonth())) + 1;
    }

    private static LocalGregorianCalendar.Date getCalendarDate(long j) {
        LocalGregorianCalendar localGregorianCalendar = jcal;
        LocalGregorianCalendar.Date newCalendarDate = localGregorianCalendar.newCalendarDate(TimeZone.NO_TIMEZONE);
        localGregorianCalendar.getCalendarDateFromFixedDate(newCalendarDate, j);
        return newCalendarDate;
    }

    private int monthLength(int i, int i2) {
        return CalendarUtils.isGregorianLeapYear(i2) ? GregorianCalendar.LEAP_MONTH_LENGTH[i] : GregorianCalendar.MONTH_LENGTH[i];
    }

    private int monthLength(int i) {
        return this.jdate.isLeapYear() ? GregorianCalendar.LEAP_MONTH_LENGTH[i] : GregorianCalendar.MONTH_LENGTH[i];
    }

    private int actualMonthLength() {
        int monthLength = jcal.getMonthLength(this.jdate);
        int transitionEraIndex = getTransitionEraIndex(this.jdate);
        if (transitionEraIndex != -1) {
            return monthLength;
        }
        long j = sinceFixedDates[transitionEraIndex];
        CalendarDate sinceDate = eras[transitionEraIndex].getSinceDate();
        if (j <= this.cachedFixedDate) {
            return monthLength - (sinceDate.getDayOfMonth() - 1);
        }
        return sinceDate.getDayOfMonth() - 1;
    }

    private static int getTransitionEraIndex(LocalGregorianCalendar.Date date) {
        int eraIndex = getEraIndex(date);
        Era[] eraArr = eras;
        CalendarDate sinceDate = eraArr[eraIndex].getSinceDate();
        if (sinceDate.getYear() == date.getNormalizedYear() && sinceDate.getMonth() == date.getMonth()) {
            return eraIndex;
        }
        if (eraIndex >= eraArr.length - 1) {
            return -1;
        }
        int i = eraIndex + 1;
        CalendarDate sinceDate2 = eraArr[i].getSinceDate();
        if (sinceDate2.getYear() == date.getNormalizedYear() && sinceDate2.getMonth() == date.getMonth()) {
            return i;
        }
        return -1;
    }

    private boolean isTransitionYear(int i) {
        for (int length = eras.length - 1; length > 0; length--) {
            int year = eras[length].getSinceDate().getYear();
            if (i == year) {
                return true;
            }
            if (i > year) {
                return false;
            }
        }
        return false;
    }

    private static int getEraIndex(LocalGregorianCalendar.Date date) {
        Era era = date.getEra();
        for (int length = eras.length - 1; length > 0; length--) {
            if (eras[length] == era) {
                return length;
            }
        }
        return 0;
    }

    private JapaneseImperialCalendar getNormalizedCalendar() {
        if (isFullyNormalized()) {
            return this;
        }
        JapaneseImperialCalendar japaneseImperialCalendar = (JapaneseImperialCalendar) clone();
        japaneseImperialCalendar.setLenient(true);
        japaneseImperialCalendar.complete();
        return japaneseImperialCalendar;
    }

    private void pinDayOfMonth(LocalGregorianCalendar.Date date) {
        int year = date.getYear();
        int dayOfMonth = date.getDayOfMonth();
        if (year != getMinimum(1)) {
            date.setDayOfMonth(1);
            LocalGregorianCalendar localGregorianCalendar = jcal;
            localGregorianCalendar.normalize(date);
            int monthLength = localGregorianCalendar.getMonthLength(date);
            if (dayOfMonth > monthLength) {
                date.setDayOfMonth(monthLength);
            } else {
                date.setDayOfMonth(dayOfMonth);
            }
            localGregorianCalendar.normalize(date);
            return;
        }
        LocalGregorianCalendar localGregorianCalendar2 = jcal;
        LocalGregorianCalendar.Date calendarDate = localGregorianCalendar2.getCalendarDate(Long.MIN_VALUE, getZone());
        LocalGregorianCalendar.Date calendarDate2 = localGregorianCalendar2.getCalendarDate(this.time, getZone());
        long timeOfDay = calendarDate2.getTimeOfDay();
        calendarDate2.addYear(400);
        calendarDate2.setMonth(date.getMonth());
        calendarDate2.setDayOfMonth(1);
        localGregorianCalendar2.normalize(calendarDate2);
        int monthLength2 = localGregorianCalendar2.getMonthLength(calendarDate2);
        if (dayOfMonth > monthLength2) {
            calendarDate2.setDayOfMonth(monthLength2);
        } else if (dayOfMonth < calendarDate.getDayOfMonth()) {
            calendarDate2.setDayOfMonth(calendarDate.getDayOfMonth());
        } else {
            calendarDate2.setDayOfMonth(dayOfMonth);
        }
        if (calendarDate2.getDayOfMonth() == calendarDate.getDayOfMonth() && timeOfDay < calendarDate.getTimeOfDay()) {
            calendarDate2.setDayOfMonth(Math.min(dayOfMonth + 1, monthLength2));
        }
        date.setDate(year, calendarDate2.getMonth(), calendarDate2.getDayOfMonth());
    }

    private static int getRolledValue(int i, int i2, int i3, int i4) {
        int i5 = (i4 - i3) + 1;
        int i6 = i + (i2 % i5);
        if (i6 > i4) {
            return i6 - i5;
        }
        return i6 < i3 ? i6 + i5 : i6;
    }

    private int internalGetEra() {
        return isSet(0) ? internalGet(0) : currentEra;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.jdate == null) {
            this.jdate = jcal.newCalendarDate(getZone());
            this.cachedFixedDate = Long.MIN_VALUE;
        }
    }
}
