package java.sql;

import android.net.wifi.WifiEnterpriseConfig;
import com.airbnb.lottie.utils.Utils;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.util.Date;
import kotlin.time.DurationKt;
import sun.util.locale.LanguageTag;

public class Timestamp extends Date {
    static final long serialVersionUID = 2745179027874758501L;
    private int nanos;

    @Deprecated
    public Timestamp(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        super(i, i2, i3, i4, i5, i6);
        if (i7 > 999999999 || i7 < 0) {
            throw new IllegalArgumentException("nanos > 999999999 or < 0");
        }
        this.nanos = i7;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Timestamp(long r7) {
        /*
            r6 = this;
            r0 = 1000(0x3e8, double:4.94E-321)
            long r2 = r7 / r0
            long r4 = r2 * r0
            r6.<init>((long) r4)
            long r7 = r7 % r0
            r4 = 1000000(0xf4240, double:4.940656E-318)
            long r7 = r7 * r4
            int r7 = (int) r7
            r6.nanos = r7
            if (r7 >= 0) goto L_0x0020
            r8 = 1000000000(0x3b9aca00, float:0.0047237873)
            int r7 = r7 + r8
            r6.nanos = r7
            r7 = 1
            long r2 = r2 - r7
            long r2 = r2 * r0
            super.setTime(r2)
        L_0x0020:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.sql.Timestamp.<init>(long):void");
    }

    public void setTime(long j) {
        long j2 = j / 1000;
        super.setTime(j2 * 1000);
        int i = (int) ((j % 1000) * 1000000);
        this.nanos = i;
        if (i < 0) {
            this.nanos = i + Utils.SECOND_IN_NANOS;
            super.setTime((j2 - 1) * 1000);
        }
    }

    public long getTime() {
        return super.getTime() + ((long) (this.nanos / DurationKt.NANOS_IN_MILLIS));
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0147  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.sql.Timestamp valueOf(java.lang.String r21) {
        /*
            if (r21 == 0) goto L_0x0159
            java.lang.String r0 = r21.trim()
            r1 = 32
            int r1 = r0.indexOf((int) r1)
            java.lang.String r2 = "Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]"
            if (r1 <= 0) goto L_0x0153
            r3 = 0
            java.lang.String r4 = r0.substring(r3, r1)
            r5 = 1
            int r1 = r1 + r5
            java.lang.String r0 = r0.substring(r1)
            r1 = 45
            int r6 = r4.indexOf((int) r1)
            int r7 = r6 + 1
            int r1 = r4.indexOf((int) r1, (int) r7)
            if (r0 == 0) goto L_0x014d
            r8 = 58
            int r9 = r0.indexOf((int) r8)
            int r10 = r9 + 1
            int r8 = r0.indexOf((int) r8, (int) r10)
            int r11 = r8 + 1
            r12 = 46
            int r12 = r0.indexOf((int) r12, (int) r11)
            if (r6 <= 0) goto L_0x0095
            if (r1 <= 0) goto L_0x0095
            int r13 = r4.length()
            int r13 = r13 - r5
            if (r1 >= r13) goto L_0x0095
            java.lang.String r6 = r4.substring(r3, r6)
            java.lang.String r7 = r4.substring(r7, r1)
            int r1 = r1 + r5
            java.lang.String r1 = r4.substring(r1)
            int r4 = r6.length()
            r13 = 4
            if (r4 != r13) goto L_0x0095
            int r4 = r7.length()
            if (r4 < r5) goto L_0x0095
            int r4 = r7.length()
            r13 = 2
            if (r4 > r13) goto L_0x0095
            int r4 = r1.length()
            if (r4 < r5) goto L_0x0095
            int r4 = r1.length()
            if (r4 > r13) goto L_0x0095
            int r4 = java.lang.Integer.parseInt(r6)
            int r6 = java.lang.Integer.parseInt(r7)
            int r1 = java.lang.Integer.parseInt(r1)
            if (r6 < r5) goto L_0x0091
            r7 = 12
            if (r6 > r7) goto L_0x0091
            if (r1 < r5) goto L_0x0091
            r7 = 31
            if (r1 > r7) goto L_0x0091
            r16 = r1
            r1 = r5
            goto L_0x009a
        L_0x0091:
            r16 = r1
            r1 = r3
            goto L_0x009a
        L_0x0095:
            r1 = r3
            r4 = r1
            r6 = r4
            r16 = r6
        L_0x009a:
            if (r1 == 0) goto L_0x0147
            if (r9 <= 0) goto L_0x00a0
            r1 = r5
            goto L_0x00a1
        L_0x00a0:
            r1 = r3
        L_0x00a1:
            if (r8 <= 0) goto L_0x00a5
            r7 = r5
            goto L_0x00a6
        L_0x00a5:
            r7 = r3
        L_0x00a6:
            r1 = r1 & r7
            int r7 = r0.length()
            int r7 = r7 - r5
            if (r8 >= r7) goto L_0x00b0
            r7 = r5
            goto L_0x00b1
        L_0x00b0:
            r7 = r3
        L_0x00b1:
            r1 = r1 & r7
            if (r1 == 0) goto L_0x0141
            java.lang.String r1 = r0.substring(r3, r9)
            int r17 = java.lang.Integer.parseInt(r1)
            java.lang.String r1 = r0.substring(r10, r8)
            int r18 = java.lang.Integer.parseInt(r1)
            if (r12 <= 0) goto L_0x00c8
            r1 = r5
            goto L_0x00c9
        L_0x00c8:
            r1 = r3
        L_0x00c9:
            int r7 = r0.length()
            int r7 = r7 - r5
            if (r12 >= r7) goto L_0x00d2
            r7 = r5
            goto L_0x00d3
        L_0x00d2:
            r7 = r3
        L_0x00d3:
            r1 = r1 & r7
            if (r1 == 0) goto L_0x0122
            java.lang.String r1 = r0.substring(r11, r12)
            int r1 = java.lang.Integer.parseInt(r1)
            int r12 = r12 + r5
            java.lang.String r0 = r0.substring(r12)
            int r7 = r0.length()
            r8 = 9
            if (r7 > r8) goto L_0x011c
            char r7 = r0.charAt(r3)
            boolean r7 = java.lang.Character.isDigit((char) r7)
            if (r7 == 0) goto L_0x0116
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r0)
            int r0 = r0.length()
            int r8 = r8 - r0
            java.lang.String r0 = "000000000"
            java.lang.String r0 = r0.substring(r3, r8)
            r2.append((java.lang.String) r0)
            java.lang.String r0 = r2.toString()
            int r3 = java.lang.Integer.parseInt(r0)
            r19 = r1
            goto L_0x012e
        L_0x0116:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>((java.lang.String) r2)
            throw r0
        L_0x011c:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>((java.lang.String) r2)
            throw r0
        L_0x0122:
            if (r12 > 0) goto L_0x013b
            java.lang.String r0 = r0.substring(r11)
            int r0 = java.lang.Integer.parseInt(r0)
            r19 = r0
        L_0x012e:
            r20 = r3
            java.sql.Timestamp r0 = new java.sql.Timestamp
            int r14 = r4 + -1900
            int r15 = r6 + -1
            r13 = r0
            r13.<init>(r14, r15, r16, r17, r18, r19, r20)
            return r0
        L_0x013b:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>((java.lang.String) r2)
            throw r0
        L_0x0141:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>((java.lang.String) r2)
            throw r0
        L_0x0147:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>((java.lang.String) r2)
            throw r0
        L_0x014d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>((java.lang.String) r2)
            throw r0
        L_0x0153:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r0.<init>((java.lang.String) r2)
            throw r0
        L_0x0159:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "null string"
            r0.<init>((java.lang.String) r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.sql.Timestamp.valueOf(java.lang.String):java.sql.Timestamp");
    }

    public String toString() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        int year = super.getYear() + 1900;
        int month = super.getMonth() + 1;
        int date = super.getDate();
        int hours = super.getHours();
        int minutes = super.getMinutes();
        int seconds = super.getSeconds();
        if (year < 1000) {
            String str7 = "" + year;
            str = "0000".substring(0, 4 - str7.length()) + str7;
        } else {
            str = "" + year;
        }
        String str8 = "0";
        if (month < 10) {
            str2 = str8 + month;
        } else {
            str2 = Integer.toString(month);
        }
        if (date < 10) {
            str3 = str8 + date;
        } else {
            str3 = Integer.toString(date);
        }
        if (hours < 10) {
            str4 = str8 + hours;
        } else {
            str4 = Integer.toString(hours);
        }
        if (minutes < 10) {
            str5 = str8 + minutes;
        } else {
            str5 = Integer.toString(minutes);
        }
        if (seconds < 10) {
            str6 = str8 + seconds;
        } else {
            str6 = Integer.toString(seconds);
        }
        int i = this.nanos;
        if (i != 0) {
            String num = Integer.toString(i);
            String str9 = "000000000".substring(0, 9 - num.length()) + num;
            char[] cArr = new char[str9.length()];
            str9.getChars(0, str9.length(), cArr, 0);
            int i2 = 8;
            while (cArr[i2] == '0') {
                i2--;
            }
            str8 = new String(cArr, 0, i2 + 1);
        }
        StringBuffer stringBuffer = new StringBuffer(str8.length() + 20);
        stringBuffer.append(str);
        stringBuffer.append(LanguageTag.SEP);
        stringBuffer.append(str2);
        stringBuffer.append(LanguageTag.SEP);
        stringBuffer.append(str3);
        stringBuffer.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuffer.append(str4);
        stringBuffer.append(":");
        stringBuffer.append(str5);
        stringBuffer.append(":");
        stringBuffer.append(str6);
        stringBuffer.append(BaseIconCache.EMPTY_CLASS_NAME);
        stringBuffer.append(str8);
        return stringBuffer.toString();
    }

    public int getNanos() {
        return this.nanos;
    }

    public void setNanos(int i) {
        if (i > 999999999 || i < 0) {
            throw new IllegalArgumentException("nanos > 999999999 or < 0");
        }
        this.nanos = i;
    }

    public boolean equals(Timestamp timestamp) {
        if (!super.equals(timestamp) || this.nanos != timestamp.nanos) {
            return false;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Timestamp) {
            return equals((Timestamp) obj);
        }
        return false;
    }

    public boolean before(Timestamp timestamp) {
        return compareTo(timestamp) < 0;
    }

    public boolean after(Timestamp timestamp) {
        return compareTo(timestamp) > 0;
    }

    public int compareTo(Timestamp timestamp) {
        int i = (getTime() > timestamp.getTime() ? 1 : (getTime() == timestamp.getTime() ? 0 : -1));
        int i2 = i < 0 ? -1 : i == 0 ? 0 : 1;
        if (i2 == 0) {
            int i3 = this.nanos;
            int i4 = timestamp.nanos;
            if (i3 > i4) {
                return 1;
            }
            if (i3 < i4) {
                return -1;
            }
        }
        return i2;
    }

    public int compareTo(Date date) {
        if (date instanceof Timestamp) {
            return compareTo((Timestamp) date);
        }
        return compareTo(new Timestamp(date.getTime()));
    }

    public int hashCode() {
        return super.hashCode();
    }
}
