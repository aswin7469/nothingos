package sun.util.calendar;

import java.util.TimeZone;
import sun.util.calendar.BaseCalendar;

public class LocalGregorianCalendar extends BaseCalendar {
    private Era[] eras;
    private String name;

    public static class Date extends BaseCalendar.Date {
        private int gregorianYear = Integer.MIN_VALUE;

        protected Date() {
        }

        protected Date(TimeZone timeZone) {
            super(timeZone);
        }

        public Date setEra(Era era) {
            if (getEra() != era) {
                super.setEra(era);
                this.gregorianYear = Integer.MIN_VALUE;
            }
            return this;
        }

        public Date addYear(int i) {
            super.addYear(i);
            this.gregorianYear += i;
            return this;
        }

        public Date setYear(int i) {
            if (getYear() != i) {
                super.setYear(i);
                this.gregorianYear = Integer.MIN_VALUE;
            }
            return this;
        }

        public int getNormalizedYear() {
            return this.gregorianYear;
        }

        public void setNormalizedYear(int i) {
            this.gregorianYear = i;
        }

        /* access modifiers changed from: package-private */
        public void setLocalEra(Era era) {
            super.setEra(era);
        }

        /* access modifiers changed from: package-private */
        public void setLocalYear(int i) {
            super.setYear(i);
        }

        public String toString() {
            String abbreviation;
            String date = super.toString();
            String substring = date.substring(date.indexOf(84));
            StringBuffer stringBuffer = new StringBuffer();
            Era era = getEra();
            if (!(era == null || (abbreviation = era.getAbbreviation()) == null)) {
                stringBuffer.append(abbreviation);
            }
            stringBuffer.append(getYear()).append('.');
            CalendarUtils.sprintf0d(stringBuffer, getMonth(), 2).append('.');
            CalendarUtils.sprintf0d(stringBuffer, getDayOfMonth(), 2);
            stringBuffer.append(substring);
            return stringBuffer.toString();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b3, code lost:
        r2.add(new sun.util.calendar.Era(r8, r9, r10, r12));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static sun.util.calendar.LocalGregorianCalendar getLocalGregorianCalendar(java.lang.String r14) {
        /*
            java.util.Properties r0 = sun.util.calendar.CalendarSystem.getCalendarProperties()     // Catch:{ IOException | IllegalArgumentException -> 0x00e7 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "calendar."
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r14)
            java.lang.String r2 = ".eras"
            r1.append((java.lang.String) r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r0 = r0.getProperty(r1)
            r1 = 0
            if (r0 != 0) goto L_0x001f
            return r1
        L_0x001f:
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            java.util.StringTokenizer r3 = new java.util.StringTokenizer
            java.lang.String r4 = ";"
            r3.<init>(r0, r4)
        L_0x002b:
            boolean r0 = r3.hasMoreTokens()
            if (r0 == 0) goto L_0x00be
            java.lang.String r0 = r3.nextToken()
            java.lang.String r0 = r0.trim()
            java.util.StringTokenizer r4 = new java.util.StringTokenizer
            java.lang.String r5 = ","
            r4.<init>(r0, r5)
            r0 = 1
            r5 = 0
            r12 = r0
            r8 = r1
            r9 = r8
        L_0x0046:
            r10 = r5
        L_0x0047:
            boolean r5 = r4.hasMoreTokens()
            if (r5 == 0) goto L_0x00b3
            java.lang.String r5 = r4.nextToken()
            r6 = 61
            int r6 = r5.indexOf((int) r6)
            r7 = -1
            if (r6 != r7) goto L_0x005b
            return r1
        L_0x005b:
            r7 = 0
            java.lang.String r13 = r5.substring(r7, r6)
            int r6 = r6 + 1
            java.lang.String r5 = r5.substring(r6)
            java.lang.String r6 = "name"
            boolean r6 = r6.equals(r13)
            if (r6 == 0) goto L_0x0070
            r8 = r5
            goto L_0x0047
        L_0x0070:
            java.lang.String r6 = "since"
            boolean r6 = r6.equals(r13)
            if (r6 == 0) goto L_0x0095
            java.lang.String r6 = "u"
            boolean r6 = r5.endsWith(r6)
            if (r6 == 0) goto L_0x0090
            int r6 = r5.length()
            int r6 = r6 - r0
            java.lang.String r5 = r5.substring(r7, r6)
            long r5 = java.lang.Long.parseLong(r5)
            r10 = r5
            r12 = r7
            goto L_0x0047
        L_0x0090:
            long r5 = java.lang.Long.parseLong(r5)
            goto L_0x0046
        L_0x0095:
            java.lang.String r6 = "abbr"
            boolean r6 = r6.equals(r13)
            if (r6 == 0) goto L_0x009f
            r9 = r5
            goto L_0x0047
        L_0x009f:
            java.lang.RuntimeException r14 = new java.lang.RuntimeException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Unknown key word: "
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.String) r13)
            java.lang.String r0 = r0.toString()
            r14.<init>((java.lang.String) r0)
            throw r14
        L_0x00b3:
            sun.util.calendar.Era r0 = new sun.util.calendar.Era
            r7 = r0
            r7.<init>(r8, r9, r10, r12)
            r2.add(r0)
            goto L_0x002b
        L_0x00be:
            boolean r0 = r2.isEmpty()
            if (r0 != 0) goto L_0x00d3
            int r0 = r2.size()
            sun.util.calendar.Era[] r0 = new sun.util.calendar.Era[r0]
            r2.toArray(r0)
            sun.util.calendar.LocalGregorianCalendar r1 = new sun.util.calendar.LocalGregorianCalendar
            r1.<init>(r14, r0)
            return r1
        L_0x00d3:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "No eras for "
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.String) r14)
            java.lang.String r14 = r1.toString()
            r0.<init>((java.lang.String) r14)
            throw r0
        L_0x00e7:
            r14 = move-exception
            java.lang.InternalError r0 = new java.lang.InternalError
            r0.<init>((java.lang.Throwable) r14)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.util.calendar.LocalGregorianCalendar.getLocalGregorianCalendar(java.lang.String):sun.util.calendar.LocalGregorianCalendar");
    }

    private LocalGregorianCalendar(String str, Era[] eraArr) {
        this.name = str;
        this.eras = eraArr;
        setEras(eraArr);
    }

    public String getName() {
        return this.name;
    }

    public Date getCalendarDate() {
        return getCalendarDate(System.currentTimeMillis(), (CalendarDate) newCalendarDate());
    }

    public Date getCalendarDate(long j) {
        return getCalendarDate(j, (CalendarDate) newCalendarDate());
    }

    public Date getCalendarDate(long j, TimeZone timeZone) {
        return getCalendarDate(j, (CalendarDate) newCalendarDate(timeZone));
    }

    public Date getCalendarDate(long j, CalendarDate calendarDate) {
        Date date = (Date) super.getCalendarDate(j, calendarDate);
        return adjustYear(date, j, date.getZoneOffset());
    }

    private Date adjustYear(Date date, long j, int i) {
        int length = this.eras.length - 1;
        while (true) {
            if (length < 0) {
                break;
            }
            Era era = this.eras[length];
            long since = era.getSince((TimeZone) null);
            if (era.isLocalTime()) {
                since -= (long) i;
            }
            if (j >= since) {
                date.setLocalEra(era);
                date.setLocalYear((date.getNormalizedYear() - era.getSinceDate().getYear()) + 1);
                break;
            }
            length--;
        }
        if (length < 0) {
            date.setLocalEra((Era) null);
            date.setLocalYear(date.getNormalizedYear());
        }
        date.setNormalized(true);
        return date;
    }

    public Date newCalendarDate() {
        return new Date();
    }

    public Date newCalendarDate(TimeZone timeZone) {
        return new Date(timeZone);
    }

    public boolean validate(CalendarDate calendarDate) {
        Date date = (Date) calendarDate;
        Era era = date.getEra();
        if (era != null) {
            if (!validateEra(era)) {
                return false;
            }
            date.setNormalizedYear((era.getSinceDate().getYear() + date.getYear()) - 1);
            Date newCalendarDate = newCalendarDate(calendarDate.getZone());
            newCalendarDate.setEra(era).setDate(calendarDate.getYear(), calendarDate.getMonth(), calendarDate.getDayOfMonth());
            normalize(newCalendarDate);
            if (newCalendarDate.getEra() != era) {
                return false;
            }
        } else if (calendarDate.getYear() >= this.eras[0].getSinceDate().getYear()) {
            return false;
        } else {
            date.setNormalizedYear(date.getYear());
        }
        return super.validate(date);
    }

    private boolean validateEra(Era era) {
        int i = 0;
        while (true) {
            Era[] eraArr = this.eras;
            if (i >= eraArr.length) {
                return false;
            }
            if (era == eraArr[i]) {
                return true;
            }
            i++;
        }
    }

    public boolean normalize(CalendarDate calendarDate) {
        if (calendarDate.isNormalized()) {
            return true;
        }
        normalizeYear(calendarDate);
        Date date = (Date) calendarDate;
        super.normalize(date);
        int normalizedYear = date.getNormalizedYear();
        int length = this.eras.length - 1;
        boolean z = false;
        long j = 0;
        Era era = null;
        while (true) {
            if (length < 0) {
                break;
            }
            era = this.eras[length];
            if (era.isLocalTime()) {
                CalendarDate sinceDate = era.getSinceDate();
                int year = sinceDate.getYear();
                if (normalizedYear <= year) {
                    if (normalizedYear == year) {
                        int month = date.getMonth();
                        int month2 = sinceDate.getMonth();
                        if (month <= month2) {
                            if (month == month2) {
                                int dayOfMonth = date.getDayOfMonth();
                                int dayOfMonth2 = sinceDate.getDayOfMonth();
                                if (dayOfMonth > dayOfMonth2) {
                                    break;
                                } else if (dayOfMonth == dayOfMonth2) {
                                    if (date.getTimeOfDay() < sinceDate.getTimeOfDay()) {
                                        length--;
                                    }
                                }
                            } else {
                                continue;
                            }
                        } else {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    break;
                }
            } else {
                if (!z) {
                    j = super.getTime(calendarDate);
                    z = true;
                }
                if (j >= era.getSince(calendarDate.getZone())) {
                    break;
                }
            }
            length--;
        }
        if (length >= 0) {
            date.setLocalEra(era);
            date.setLocalYear((date.getNormalizedYear() - era.getSinceDate().getYear()) + 1);
        } else {
            date.setEra((Era) null);
            date.setLocalYear(normalizedYear);
            date.setNormalizedYear(normalizedYear);
        }
        date.setNormalized(true);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void normalizeMonth(CalendarDate calendarDate) {
        normalizeYear(calendarDate);
        super.normalizeMonth(calendarDate);
    }

    /* access modifiers changed from: package-private */
    public void normalizeYear(CalendarDate calendarDate) {
        Date date = (Date) calendarDate;
        Era era = date.getEra();
        if (era == null || !validateEra(era)) {
            date.setNormalizedYear(date.getYear());
        } else {
            date.setNormalizedYear((era.getSinceDate().getYear() + date.getYear()) - 1);
        }
    }

    public boolean isLeapYear(int i) {
        return CalendarUtils.isGregorianLeapYear(i);
    }

    public boolean isLeapYear(Era era, int i) {
        if (era == null) {
            return isLeapYear(i);
        }
        return isLeapYear((era.getSinceDate().getYear() + i) - 1);
    }

    public void getCalendarDateFromFixedDate(CalendarDate calendarDate, long j) {
        Date date = (Date) calendarDate;
        super.getCalendarDateFromFixedDate(date, j);
        adjustYear(date, (j - 719163) * 86400000, 0);
    }
}
