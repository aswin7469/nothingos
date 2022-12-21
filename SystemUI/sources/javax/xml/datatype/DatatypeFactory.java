package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import javax.xml.datatype.FactoryFinder;

public abstract class DatatypeFactory {
    public static final String DATATYPEFACTORY_IMPLEMENTATION_CLASS = new String("org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl");
    public static final String DATATYPEFACTORY_PROPERTY = "javax.xml.datatype.DatatypeFactory";

    public abstract Duration newDuration(long j);

    public abstract Duration newDuration(String str);

    public abstract Duration newDuration(boolean z, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigDecimal bigDecimal);

    public abstract XMLGregorianCalendar newXMLGregorianCalendar();

    public abstract XMLGregorianCalendar newXMLGregorianCalendar(String str);

    public abstract XMLGregorianCalendar newXMLGregorianCalendar(BigInteger bigInteger, int i, int i2, int i3, int i4, int i5, BigDecimal bigDecimal, int i6);

    public abstract XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar gregorianCalendar);

    protected DatatypeFactory() {
    }

    public static DatatypeFactory newInstance() throws DatatypeConfigurationException {
        try {
            return (DatatypeFactory) FactoryFinder.find(DATATYPEFACTORY_PROPERTY, DATATYPEFACTORY_IMPLEMENTATION_CLASS);
        } catch (FactoryFinder.ConfigurationError e) {
            throw new DatatypeConfigurationException(e.getMessage(), e.getException());
        }
    }

    public static DatatypeFactory newInstance(String str, ClassLoader classLoader) throws DatatypeConfigurationException {
        Class<?> cls;
        if (str != null) {
            if (classLoader == null) {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
            if (classLoader != null) {
                try {
                    cls = classLoader.loadClass(str);
                } catch (ClassNotFoundException e) {
                    throw new DatatypeConfigurationException((Throwable) e);
                } catch (InstantiationException e2) {
                    throw new DatatypeConfigurationException((Throwable) e2);
                } catch (IllegalAccessException e3) {
                    throw new DatatypeConfigurationException((Throwable) e3);
                }
            } else {
                cls = Class.forName(str);
            }
            return (DatatypeFactory) cls.newInstance();
        }
        throw new DatatypeConfigurationException("factoryClassName == null");
    }

    public Duration newDuration(boolean z, int i, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i;
        int i8 = i2;
        int i9 = i3;
        int i10 = i4;
        int i11 = i5;
        int i12 = i6;
        BigDecimal bigDecimal = null;
        BigInteger valueOf = i7 != Integer.MIN_VALUE ? BigInteger.valueOf((long) i7) : null;
        BigInteger valueOf2 = i8 != Integer.MIN_VALUE ? BigInteger.valueOf((long) i8) : null;
        BigInteger valueOf3 = i9 != Integer.MIN_VALUE ? BigInteger.valueOf((long) i9) : null;
        BigInteger valueOf4 = i10 != Integer.MIN_VALUE ? BigInteger.valueOf((long) i10) : null;
        BigInteger valueOf5 = i11 != Integer.MIN_VALUE ? BigInteger.valueOf((long) i11) : null;
        if (i12 != Integer.MIN_VALUE) {
            bigDecimal = BigDecimal.valueOf((long) i12);
        }
        return newDuration(z, valueOf, valueOf2, valueOf3, valueOf4, valueOf5, bigDecimal);
    }

    public Duration newDurationDayTime(String str) {
        if (str != null) {
            int indexOf = str.indexOf(84);
            if (indexOf < 0) {
                indexOf = str.length();
            }
            for (int i = 0; i < indexOf; i++) {
                char charAt = str.charAt(i);
                if (charAt == 'Y' || charAt == 'M') {
                    throw new IllegalArgumentException("Invalid dayTimeDuration value: " + str);
                }
            }
            return newDuration(str);
        }
        throw new NullPointerException("lexicalRepresentation == null");
    }

    public Duration newDurationDayTime(long j) {
        boolean z;
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i == 0) {
            return newDuration(true, Integer.MIN_VALUE, Integer.MIN_VALUE, 0, 0, 0, 0);
        }
        boolean z2 = false;
        boolean z3 = true;
        if (i < 0) {
            if (j == Long.MIN_VALUE) {
                j++;
            } else {
                z3 = false;
            }
            j *= -1;
            z = false;
            z2 = z3;
        } else {
            z = true;
        }
        int i2 = (int) (j % 60000);
        if (z2) {
            i2++;
        }
        if (i2 % 1000 == 0) {
            int i3 = i2 / 1000;
            long j2 = j / 60000;
            int i4 = (int) (j2 % 60);
            long j3 = j2 / 60;
            int i5 = (int) (j3 % 24);
            long j4 = j3 / 24;
            if (j4 <= 2147483647L) {
                return newDuration(z, Integer.MIN_VALUE, Integer.MIN_VALUE, (int) j4, i5, i4, i3);
            }
            return newDuration(z, (BigInteger) null, (BigInteger) null, BigInteger.valueOf(j4), BigInteger.valueOf((long) i5), BigInteger.valueOf((long) i4), BigDecimal.valueOf((long) i2, 3));
        }
        BigDecimal valueOf = BigDecimal.valueOf((long) i2, 3);
        long j5 = j / 60000;
        BigInteger valueOf2 = BigInteger.valueOf(j5 % 60);
        long j6 = j5 / 60;
        return newDuration(z, (BigInteger) null, (BigInteger) null, BigInteger.valueOf(j6 / 24), BigInteger.valueOf(j6 % 24), valueOf2, valueOf);
    }

    public Duration newDurationDayTime(boolean z, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        return newDuration(z, (BigInteger) null, (BigInteger) null, bigInteger, bigInteger2, bigInteger3, bigInteger4 != null ? new BigDecimal(bigInteger4) : null);
    }

    public Duration newDurationDayTime(boolean z, int i, int i2, int i3, int i4) {
        return newDuration(z, Integer.MIN_VALUE, Integer.MIN_VALUE, i, i2, i3, i4);
    }

    public Duration newDurationYearMonth(String str) {
        if (str != null) {
            int length = str.length();
            for (int i = 0; i < length; i++) {
                char charAt = str.charAt(i);
                if (charAt == 'D' || charAt == 'T') {
                    throw new IllegalArgumentException("Invalid yearMonthDuration value: " + str);
                }
            }
            return newDuration(str);
        }
        throw new NullPointerException("lexicalRepresentation == null");
    }

    public Duration newDurationYearMonth(long j) {
        return newDuration(j);
    }

    public Duration newDurationYearMonth(boolean z, BigInteger bigInteger, BigInteger bigInteger2) {
        return newDuration(z, bigInteger, bigInteger2, (BigInteger) null, (BigInteger) null, (BigInteger) null, (BigDecimal) null);
    }

    public Duration newDurationYearMonth(boolean z, int i, int i2) {
        return newDuration(z, i, i2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public XMLGregorianCalendar newXMLGregorianCalendar(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        BigInteger bigInteger;
        int i9 = i;
        int i10 = i7;
        BigDecimal bigDecimal = null;
        if (i9 != Integer.MIN_VALUE) {
            bigInteger = BigInteger.valueOf((long) i9);
        } else {
            bigInteger = null;
        }
        if (i10 != Integer.MIN_VALUE) {
            if (i10 < 0 || i10 > 1000) {
                throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendar(int year, int month, int day, int hour, int minute, int second, int millisecond, int timezone)with invalid millisecond: " + i10);
            }
            bigDecimal = BigDecimal.valueOf((long) i10, 3);
        }
        return newXMLGregorianCalendar(bigInteger, i2, i3, i4, i5, i6, bigDecimal, i8);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarDate(int i, int i2, int i3, int i4) {
        return newXMLGregorianCalendar(i, i2, i3, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, i4);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarTime(int i, int i2, int i3, int i4) {
        return newXMLGregorianCalendar(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, i, i2, i3, Integer.MIN_VALUE, i4);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarTime(int i, int i2, int i3, BigDecimal bigDecimal, int i4) {
        return newXMLGregorianCalendar((BigInteger) null, Integer.MIN_VALUE, Integer.MIN_VALUE, i, i2, i3, bigDecimal, i4);
    }

    public XMLGregorianCalendar newXMLGregorianCalendarTime(int i, int i2, int i3, int i4, int i5) {
        BigDecimal bigDecimal;
        if (i4 == Integer.MIN_VALUE) {
            bigDecimal = null;
        } else if (i4 < 0 || i4 > 1000) {
            throw new IllegalArgumentException("javax.xml.datatype.DatatypeFactory#newXMLGregorianCalendarTime(int hours, int minutes, int seconds, int milliseconds, int timezone)with invalid milliseconds: " + i4);
        } else {
            bigDecimal = BigDecimal.valueOf((long) i4, 3);
        }
        return newXMLGregorianCalendarTime(i, i2, i3, bigDecimal, i5);
    }
}
