package sun.security.util;

import java.math.BigInteger;
import java.p026io.ByteArrayInputStream;
import java.p026io.IOException;
import java.util.Date;
import java.util.TimeZone;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.Gregorian;

class DerInputBuffer extends ByteArrayInputStream implements Cloneable {
    DerInputBuffer(byte[] bArr) {
        super(bArr);
    }

    DerInputBuffer(byte[] bArr, int i, int i2) {
        super(bArr, i, i2);
    }

    /* access modifiers changed from: package-private */
    public DerInputBuffer dup() {
        try {
            DerInputBuffer derInputBuffer = (DerInputBuffer) clone();
            derInputBuffer.mark(Integer.MAX_VALUE);
            return derInputBuffer;
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    /* access modifiers changed from: package-private */
    public byte[] toByteArray() {
        int available = available();
        if (available <= 0) {
            return null;
        }
        byte[] bArr = new byte[available];
        System.arraycopy((Object) this.buf, this.pos, (Object) bArr, 0, available);
        return bArr;
    }

    /* access modifiers changed from: package-private */
    public int getPos() {
        return this.pos;
    }

    /* access modifiers changed from: package-private */
    public byte[] getSlice(int i, int i2) {
        byte[] bArr = new byte[i2];
        System.arraycopy((Object) this.buf, i, (Object) bArr, 0, i2);
        return bArr;
    }

    /* access modifiers changed from: package-private */
    public int peek() throws IOException {
        if (this.pos < this.count) {
            return this.buf[this.pos];
        }
        throw new IOException("out of data");
    }

    public boolean equals(Object obj) {
        if (obj instanceof DerInputBuffer) {
            return equals((DerInputBuffer) obj);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean equals(DerInputBuffer derInputBuffer) {
        if (this == derInputBuffer) {
            return true;
        }
        int available = available();
        if (derInputBuffer.available() != available) {
            return false;
        }
        for (int i = 0; i < available; i++) {
            if (this.buf[this.pos + i] != derInputBuffer.buf[derInputBuffer.pos + i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int available = available();
        int i = this.pos;
        int i2 = 0;
        for (int i3 = 0; i3 < available; i3++) {
            i2 += this.buf[i + i3] * i3;
        }
        return i2;
    }

    /* access modifiers changed from: package-private */
    public void truncate(int i) throws IOException {
        if (i <= available()) {
            this.count = this.pos + i;
            return;
        }
        throw new IOException("insufficient data");
    }

    /* access modifiers changed from: package-private */
    public BigInteger getBigInteger(int i, boolean z) throws IOException {
        if (i > available()) {
            throw new IOException("short read of integer");
        } else if (i != 0) {
            byte[] bArr = new byte[i];
            System.arraycopy((Object) this.buf, this.pos, (Object) bArr, 0, i);
            skip((long) i);
            if (i >= 2 && bArr[0] == 0 && bArr[1] >= 0) {
                throw new IOException("Invalid encoding: redundant leading 0s");
            } else if (z) {
                return new BigInteger(1, bArr);
            } else {
                return new BigInteger(bArr);
            }
        } else {
            throw new IOException("Invalid encoding: zero length Int value");
        }
    }

    public int getInteger(int i) throws IOException {
        BigInteger bigInteger = getBigInteger(i, false);
        if (bigInteger.compareTo(BigInteger.valueOf(-2147483648L)) < 0) {
            throw new IOException("Integer below minimum valid value");
        } else if (bigInteger.compareTo(BigInteger.valueOf(2147483647L)) <= 0) {
            return bigInteger.intValue();
        } else {
            throw new IOException("Integer exceeds maximum valid value");
        }
    }

    public byte[] getBitString(int i) throws IOException {
        if (i > available()) {
            throw new IOException("short read of bit string");
        } else if (i != 0) {
            byte b = this.buf[this.pos];
            if (b < 0 || b > 7) {
                throw new IOException("Invalid number of padding bits");
            }
            int i2 = i - 1;
            byte[] bArr = new byte[i2];
            System.arraycopy((Object) this.buf, this.pos + 1, (Object) bArr, 0, i2);
            if (b != 0) {
                int i3 = i - 2;
                bArr[i3] = (byte) ((255 << b) & bArr[i3]);
            }
            skip((long) i);
            return bArr;
        } else {
            throw new IOException("Invalid encoding: zero length bit string");
        }
    }

    /* access modifiers changed from: package-private */
    public byte[] getBitString() throws IOException {
        return getBitString(available());
    }

    /* access modifiers changed from: package-private */
    public BitArray getUnalignedBitString() throws IOException {
        if (this.pos >= this.count) {
            return null;
        }
        int available = available();
        byte b = this.buf[this.pos] & 255;
        if (b <= 7) {
            int i = available - 1;
            byte[] bArr = new byte[i];
            int i2 = i == 0 ? 0 : (i * 8) - b;
            System.arraycopy((Object) this.buf, this.pos + 1, (Object) bArr, 0, i);
            BitArray bitArray = new BitArray(i2, bArr);
            this.pos = this.count;
            return bitArray;
        }
        throw new IOException("Invalid value for unused bits: " + b);
    }

    public Date getUTCTime(int i) throws IOException {
        if (i > available()) {
            throw new IOException("short read of DER UTC Time");
        } else if (i >= 11 && i <= 17) {
            return getTime(i, false);
        } else {
            throw new IOException("DER UTC Time length error");
        }
    }

    public Date getGeneralizedTime(int i) throws IOException {
        if (i > available()) {
            throw new IOException("short read of DER Generalized Time");
        } else if (i >= 13 && i <= 23) {
            return getTime(i, true);
        } else {
            throw new IOException("DER Generalized Time length error");
        }
    }

    private Date getTime(int i, boolean z) throws IOException {
        String str;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (z) {
            byte[] bArr = this.buf;
            int i7 = this.pos;
            this.pos = i7 + 1;
            byte[] bArr2 = this.buf;
            int i8 = this.pos;
            this.pos = i8 + 1;
            int digit = (Character.digit((char) bArr[i7], 10) * 1000) + (Character.digit((char) bArr2[i8], 10) * 100);
            byte[] bArr3 = this.buf;
            int i9 = this.pos;
            this.pos = i9 + 1;
            int digit2 = digit + (Character.digit((char) bArr3[i9], 10) * 10);
            byte[] bArr4 = this.buf;
            int i10 = this.pos;
            this.pos = i10 + 1;
            i3 = digit2 + Character.digit((char) bArr4[i10], 10);
            i2 = i - 2;
            str = "Generalized";
        } else {
            byte[] bArr5 = this.buf;
            int i11 = this.pos;
            this.pos = i11 + 1;
            byte[] bArr6 = this.buf;
            int i12 = this.pos;
            this.pos = i12 + 1;
            int digit3 = (Character.digit((char) bArr5[i11], 10) * 10) + Character.digit((char) bArr6[i12], 10);
            str = "UTC";
            i3 = digit3 < 50 ? digit3 + 2000 : digit3 + 1900;
            i2 = i;
        }
        byte[] bArr7 = this.buf;
        int i13 = this.pos;
        this.pos = i13 + 1;
        byte[] bArr8 = this.buf;
        int i14 = this.pos;
        this.pos = i14 + 1;
        int digit4 = (Character.digit((char) bArr7[i13], 10) * 10) + Character.digit((char) bArr8[i14], 10);
        byte[] bArr9 = this.buf;
        int i15 = this.pos;
        this.pos = i15 + 1;
        byte[] bArr10 = this.buf;
        int i16 = this.pos;
        this.pos = i16 + 1;
        int digit5 = (Character.digit((char) bArr9[i15], 10) * 10) + Character.digit((char) bArr10[i16], 10);
        byte[] bArr11 = this.buf;
        int i17 = this.pos;
        this.pos = i17 + 1;
        byte[] bArr12 = this.buf;
        int i18 = this.pos;
        this.pos = i18 + 1;
        int digit6 = (Character.digit((char) bArr11[i17], 10) * 10) + Character.digit((char) bArr12[i18], 10);
        byte[] bArr13 = this.buf;
        int i19 = this.pos;
        this.pos = i19 + 1;
        byte[] bArr14 = this.buf;
        int i20 = this.pos;
        this.pos = i20 + 1;
        int digit7 = (Character.digit((char) bArr13[i19], 10) * 10) + Character.digit((char) bArr14[i20], 10);
        int i21 = i2 - 10;
        if (i21 <= 2 || i21 >= 12) {
            i5 = 0;
            i4 = 0;
        } else {
            byte[] bArr15 = this.buf;
            int i22 = this.pos;
            this.pos = i22 + 1;
            byte[] bArr16 = this.buf;
            int i23 = this.pos;
            this.pos = i23 + 1;
            int digit8 = (Character.digit((char) bArr15[i22], 10) * 10) + Character.digit((char) bArr16[i23], 10);
            i21 -= 2;
            if (this.buf[this.pos] == 46 || this.buf[this.pos] == 44) {
                int i24 = i21 - 1;
                this.pos++;
                int i25 = this.pos;
                int i26 = 0;
                for (byte b = 90; this.buf[i25] != b && this.buf[i25] != 43 && this.buf[i25] != 45; b = 90) {
                    i25++;
                    i26++;
                }
                if (i26 == 1) {
                    byte[] bArr17 = this.buf;
                    int i27 = this.pos;
                    this.pos = i27 + 1;
                    i6 = 0 + (Character.digit((char) bArr17[i27], 10) * 100);
                } else if (i26 == 2) {
                    byte[] bArr18 = this.buf;
                    int i28 = this.pos;
                    this.pos = i28 + 1;
                    int digit9 = 0 + (Character.digit((char) bArr18[i28], 10) * 100);
                    byte[] bArr19 = this.buf;
                    int i29 = this.pos;
                    this.pos = i29 + 1;
                    i6 = digit9 + (Character.digit((char) bArr19[i29], 10) * 10);
                } else if (i26 == 3) {
                    byte[] bArr20 = this.buf;
                    int i30 = this.pos;
                    this.pos = i30 + 1;
                    byte[] bArr21 = this.buf;
                    int i31 = this.pos;
                    this.pos = i31 + 1;
                    int digit10 = (Character.digit((char) bArr20[i30], 10) * 100) + 0 + (Character.digit((char) bArr21[i31], 10) * 10);
                    byte[] bArr22 = this.buf;
                    int i32 = this.pos;
                    this.pos = i32 + 1;
                    i6 = digit10 + Character.digit((char) bArr22[i32], 10);
                } else {
                    throw new IOException("Parse " + str + " time, unsupported precision for seconds value");
                }
                i21 = i24 - i26;
                i5 = i6;
                i4 = digit8;
            } else {
                i4 = digit8;
                i5 = 0;
            }
        }
        if (digit4 == 0 || digit5 == 0 || digit4 > 12 || digit5 > 31 || digit6 >= 24 || digit7 >= 60 || i4 >= 60) {
            throw new IOException("Parse " + str + " time, invalid format");
        }
        Gregorian gregorianCalendar = CalendarSystem.getGregorianCalendar();
        CalendarDate newCalendarDate = gregorianCalendar.newCalendarDate((TimeZone) null);
        newCalendarDate.setDate(i3, digit4, digit5);
        newCalendarDate.setTimeOfDay(digit6, digit7, i4, i5);
        long time = gregorianCalendar.getTime(newCalendarDate);
        if (i21 == 1 || i21 == 5) {
            byte[] bArr23 = this.buf;
            int i33 = this.pos;
            this.pos = i33 + 1;
            byte b2 = bArr23[i33];
            if (b2 == 43) {
                byte[] bArr24 = this.buf;
                int i34 = this.pos;
                this.pos = i34 + 1;
                byte[] bArr25 = this.buf;
                int i35 = this.pos;
                this.pos = i35 + 1;
                int digit11 = (Character.digit((char) bArr24[i34], 10) * 10) + Character.digit((char) bArr25[i35], 10);
                byte[] bArr26 = this.buf;
                int i36 = this.pos;
                this.pos = i36 + 1;
                byte[] bArr27 = this.buf;
                int i37 = this.pos;
                this.pos = i37 + 1;
                int digit12 = (Character.digit((char) bArr26[i36], 10) * 10) + Character.digit((char) bArr27[i37], 10);
                if (digit11 >= 24 || digit12 >= 60) {
                    throw new IOException("Parse " + str + " time, +hhmm");
                }
                time -= (long) ((((digit11 * 60) + digit12) * 60) * 1000);
            } else if (b2 == 45) {
                byte[] bArr28 = this.buf;
                int i38 = this.pos;
                this.pos = i38 + 1;
                byte[] bArr29 = this.buf;
                int i39 = this.pos;
                this.pos = i39 + 1;
                int digit13 = (Character.digit((char) bArr28[i38], 10) * 10) + Character.digit((char) bArr29[i39], 10);
                byte[] bArr30 = this.buf;
                int i40 = this.pos;
                this.pos = i40 + 1;
                byte[] bArr31 = this.buf;
                int i41 = this.pos;
                this.pos = i41 + 1;
                int digit14 = (Character.digit((char) bArr30[i40], 10) * 10) + Character.digit((char) bArr31[i41], 10);
                if (digit13 >= 24 || digit14 >= 60) {
                    throw new IOException("Parse " + str + " time, -hhmm");
                }
                time += (long) (((digit13 * 60) + digit14) * 60 * 1000);
            } else if (b2 != 90) {
                throw new IOException("Parse " + str + " time, garbage offset");
            }
            return new Date(time);
        }
        throw new IOException("Parse " + str + " time, invalid offset");
    }
}
