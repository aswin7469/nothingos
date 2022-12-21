package java.sql;

import java.util.Date;

public class Time extends Date {
    static final long serialVersionUID = 8397324403548013681L;

    @Deprecated
    public Time(int i, int i2, int i3) {
        super(70, 0, 1, i, i2, i3);
    }

    public Time(long j) {
        super(j);
    }

    public void setTime(long j) {
        super.setTime(j);
    }

    public static Time valueOf(String str) {
        if (str != null) {
            int indexOf = str.indexOf(58);
            int i = indexOf + 1;
            int indexOf2 = str.indexOf(58, i);
            if (((indexOf > 0) & (indexOf2 > 0)) && (indexOf2 < str.length() - 1)) {
                return new Time(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(i, indexOf2)), Integer.parseInt(str.substring(indexOf2 + 1)));
            }
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }

    public String toString() {
        String str;
        String str2;
        String str3;
        int hours = super.getHours();
        int minutes = super.getMinutes();
        int seconds = super.getSeconds();
        if (hours < 10) {
            str = "0" + hours;
        } else {
            str = Integer.toString(hours);
        }
        if (minutes < 10) {
            str2 = "0" + minutes;
        } else {
            str2 = Integer.toString(minutes);
        }
        if (seconds < 10) {
            str3 = "0" + seconds;
        } else {
            str3 = Integer.toString(seconds);
        }
        return str + ":" + str2 + ":" + str3;
    }

    @Deprecated
    public int getYear() {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public int getMonth() {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public int getDay() {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public int getDate() {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public void setYear(int i) {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public void setMonth(int i) {
        throw new IllegalArgumentException();
    }

    @Deprecated
    public void setDate(int i) {
        throw new IllegalArgumentException();
    }
}
