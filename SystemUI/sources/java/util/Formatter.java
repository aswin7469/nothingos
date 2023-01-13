package java.util;

import android.net.wifi.WifiManager;
import androidx.exifinterface.media.ExifInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.p026io.BufferedWriter;
import java.p026io.Closeable;
import java.p026io.File;
import java.p026io.FileNotFoundException;
import java.p026io.FileOutputStream;
import java.p026io.Flushable;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.p026io.OutputStreamWriter;
import java.p026io.PrintStream;
import java.p026io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import jdk.internal.math.FormattedFloatingDecimal;
import kotlin.text.Typography;
import libcore.icu.DecimalFormatData;
import libcore.icu.LocaleData;

public final class Formatter implements Closeable, Flushable {
    private static final int MAX_FD_CHARS = 30;
    /* access modifiers changed from: private */
    public static double scaleUp;
    /* access modifiers changed from: private */

    /* renamed from: a */
    public Appendable f675a;
    /* access modifiers changed from: private */

    /* renamed from: l */
    public final Locale f676l;
    private IOException lastException;
    /* access modifiers changed from: private */
    public final char zero;

    public enum BigDecimalLayoutForm {
        SCIENTIFIC,
        DECIMAL_FLOAT
    }

    private interface FormatString {
        int index();

        void print(Object obj, Locale locale) throws IOException;

        String toString();
    }

    private static Charset toCharset(String str) throws UnsupportedEncodingException {
        Objects.requireNonNull(str, "charsetName");
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
            throw new UnsupportedEncodingException(str);
        }
    }

    private static final Appendable nonNullAppendable(Appendable appendable) {
        return appendable == null ? new StringBuilder() : appendable;
    }

    private Formatter(Locale locale, Appendable appendable) {
        this.f675a = appendable;
        this.f676l = locale;
        this.zero = getZero(locale);
    }

    private Formatter(Charset charset, Locale locale, File file) throws FileNotFoundException {
        this(locale, (Appendable) new BufferedWriter(new OutputStreamWriter((OutputStream) new FileOutputStream(file), charset)));
    }

    public Formatter() {
        this(Locale.getDefault(Locale.Category.FORMAT), (Appendable) new StringBuilder());
    }

    public Formatter(Appendable appendable) {
        this(Locale.getDefault(Locale.Category.FORMAT), nonNullAppendable(appendable));
    }

    public Formatter(Locale locale) {
        this(locale, (Appendable) new StringBuilder());
    }

    public Formatter(Appendable appendable, Locale locale) {
        this(locale, nonNullAppendable(appendable));
    }

    public Formatter(String str) throws FileNotFoundException {
        this(Locale.getDefault(Locale.Category.FORMAT), (Appendable) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str))));
    }

    public Formatter(String str, String str2) throws FileNotFoundException, UnsupportedEncodingException {
        this(str, str2, Locale.getDefault(Locale.Category.FORMAT));
    }

    public Formatter(String str, String str2, Locale locale) throws FileNotFoundException, UnsupportedEncodingException {
        this(toCharset(str2), locale, new File(str));
    }

    public Formatter(File file) throws FileNotFoundException {
        this(Locale.getDefault(Locale.Category.FORMAT), (Appendable) new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))));
    }

    public Formatter(File file, String str) throws FileNotFoundException, UnsupportedEncodingException {
        this(file, str, Locale.getDefault(Locale.Category.FORMAT));
    }

    public Formatter(File file, String str, Locale locale) throws FileNotFoundException, UnsupportedEncodingException {
        this(toCharset(str), locale, file);
    }

    public Formatter(PrintStream printStream) {
        this(Locale.getDefault(Locale.Category.FORMAT), (Appendable) Objects.requireNonNull(printStream));
    }

    public Formatter(OutputStream outputStream) {
        this(Locale.getDefault(Locale.Category.FORMAT), (Appendable) new BufferedWriter(new OutputStreamWriter(outputStream)));
    }

    public Formatter(OutputStream outputStream, String str) throws UnsupportedEncodingException {
        this(outputStream, str, Locale.getDefault(Locale.Category.FORMAT));
    }

    public Formatter(OutputStream outputStream, String str, Locale locale) throws UnsupportedEncodingException {
        this(locale, (Appendable) new BufferedWriter(new OutputStreamWriter(outputStream, str)));
    }

    private static char getZero(Locale locale) {
        if (locale == null || locale.equals(Locale.f698US)) {
            return '0';
        }
        return DecimalFormatData.getInstance(LocaleData.mapInvalidAndNullLocales(locale)).getZeroDigit();
    }

    public Locale locale() {
        ensureOpen();
        return this.f676l;
    }

    public Appendable out() {
        ensureOpen();
        return this.f675a;
    }

    public String toString() {
        ensureOpen();
        return this.f675a.toString();
    }

    public void flush() {
        ensureOpen();
        Appendable appendable = this.f675a;
        if (appendable instanceof Flushable) {
            try {
                ((Flushable) appendable).flush();
            } catch (IOException e) {
                this.lastException = e;
            }
        }
    }

    public void close() {
        Appendable appendable = this.f675a;
        if (appendable != null) {
            try {
                if (appendable instanceof Closeable) {
                    ((Closeable) appendable).close();
                }
            } catch (IOException e) {
                this.lastException = e;
            } catch (Throwable th) {
                this.f675a = null;
                throw th;
            }
            this.f675a = null;
        }
    }

    private void ensureOpen() {
        if (this.f675a == null) {
            throw new FormatterClosedException();
        }
    }

    public IOException ioException() {
        return this.lastException;
    }

    public Formatter format(String str, Object... objArr) {
        return format(this.f676l, str, objArr);
    }

    public Formatter format(Locale locale, String str, Object... objArr) {
        ensureOpen();
        FormatString[] parse = parse(str);
        int i = -1;
        int i2 = -1;
        for (FormatString formatString : parse) {
            int index = formatString.index();
            Object obj = null;
            if (index == -2) {
                formatString.print((Object) null, locale);
            } else if (index == -1) {
                if (i >= 0) {
                    if (objArr != null) {
                        if (i > objArr.length - 1) {
                        }
                    }
                    if (objArr != null) {
                        obj = objArr[i];
                    }
                    formatString.print(obj, locale);
                }
                throw new MissingFormatArgumentException(formatString.toString());
            } else if (index != 0) {
                i = index - 1;
                if (objArr != null) {
                    try {
                        if (i > objArr.length - 1) {
                            throw new MissingFormatArgumentException(formatString.toString());
                        }
                    } catch (IOException e) {
                        e = e;
                        this.lastException = e;
                    }
                }
                if (objArr != null) {
                    obj = objArr[i];
                }
                formatString.print(obj, locale);
            } else {
                i = i2 + 1;
                if (objArr != null) {
                    try {
                        if (i > objArr.length - 1) {
                            throw new MissingFormatArgumentException(formatString.toString());
                        }
                    } catch (IOException e2) {
                        e = e2;
                        i2 = i;
                        this.lastException = e;
                    }
                }
                if (objArr != null) {
                    obj = objArr[i];
                }
                formatString.print(obj, locale);
                i2 = i;
            }
        }
        return this;
    }

    private FormatString[] parse(String str) {
        ArrayList arrayList = new ArrayList();
        int length = str.length();
        int i = 0;
        while (i < length) {
            int indexOf = str.indexOf(37, i);
            if (str.charAt(i) != '%') {
                if (indexOf == -1) {
                    indexOf = length;
                }
                arrayList.add(new FixedString(str.substring(i, indexOf)));
                i = indexOf;
            } else {
                FormatSpecifierParser formatSpecifierParser = new FormatSpecifierParser(str, i + 1);
                arrayList.add(formatSpecifierParser.getFormatSpecifier());
                i = formatSpecifierParser.getEndIdx();
            }
        }
        return (FormatString[]) arrayList.toArray(new FormatString[arrayList.size()]);
    }

    private class FormatSpecifierParser {
        private static final String FLAGS = ",-(+# 0<";
        private String conv;
        private int cursor;
        private String flags;
        private final String format;

        /* renamed from: fs */
        private FormatSpecifier f681fs;
        private String index;
        private String precision;

        /* renamed from: tT */
        private String f682tT;
        private String width;

        public FormatSpecifierParser(String str, int i) {
            this.format = str;
            this.cursor = i;
            if (nextIsInt()) {
                String nextInt = nextInt();
                if (peek() == '$') {
                    this.index = nextInt;
                    advance();
                } else if (nextInt.charAt(0) == '0') {
                    back(nextInt.length());
                } else {
                    this.width = nextInt;
                }
            }
            this.flags = "";
            while (this.width == null && FLAGS.indexOf((int) peek()) >= 0) {
                this.flags += advance();
            }
            if (this.width == null && nextIsInt()) {
                this.width = nextInt();
            }
            if (peek() == '.') {
                advance();
                if (nextIsInt()) {
                    this.precision = nextInt();
                } else {
                    throw new IllegalFormatPrecisionException(peek());
                }
            }
            if (peek() == 't' || peek() == 'T') {
                this.f682tT = String.valueOf(advance());
            }
            this.conv = String.valueOf(advance());
            this.f681fs = new FormatSpecifier(this.index, this.flags, this.width, this.precision, this.f682tT, this.conv);
        }

        private String nextInt() {
            int i = this.cursor;
            while (nextIsInt()) {
                advance();
            }
            return this.format.substring(i, this.cursor);
        }

        private boolean nextIsInt() {
            return !isEnd() && Character.isDigit(peek());
        }

        private char peek() {
            if (!isEnd()) {
                return this.format.charAt(this.cursor);
            }
            throw new UnknownFormatConversionException("End of String");
        }

        private char advance() {
            if (!isEnd()) {
                String str = this.format;
                int i = this.cursor;
                this.cursor = i + 1;
                return str.charAt(i);
            }
            throw new UnknownFormatConversionException("End of String");
        }

        private void back(int i) {
            this.cursor -= i;
        }

        private boolean isEnd() {
            return this.cursor == this.format.length();
        }

        public FormatSpecifier getFormatSpecifier() {
            return this.f681fs;
        }

        public int getEndIdx() {
            return this.cursor;
        }
    }

    private class FixedString implements FormatString {

        /* renamed from: s */
        private String f677s;

        public int index() {
            return -2;
        }

        FixedString(String str) {
            this.f677s = str;
        }

        public void print(Object obj, Locale locale) throws IOException {
            Formatter.this.f675a.append((CharSequence) this.f677s);
        }

        public String toString() {
            return this.f677s;
        }
    }

    private class FormatSpecifier implements FormatString {
        static final /* synthetic */ boolean $assertionsDisabled = false;

        /* renamed from: c */
        private char f678c;

        /* renamed from: dt */
        private boolean f679dt = false;

        /* renamed from: f */
        private Flags f680f = Flags.NONE;
        private int index = -1;
        private int precision;
        private int width;

        static {
            Class<Formatter> cls = Formatter.class;
        }

        private int index(String str) {
            if (str != null) {
                try {
                    this.index = Integer.parseInt(str);
                } catch (NumberFormatException unused) {
                }
            } else {
                this.index = 0;
            }
            return this.index;
        }

        public int index() {
            return this.index;
        }

        private Flags flags(String str) {
            Flags parse = Flags.parse(str);
            this.f680f = parse;
            if (parse.contains(Flags.PREVIOUS)) {
                this.index = -1;
            }
            return this.f680f;
        }

        /* access modifiers changed from: package-private */
        public Flags flags() {
            return this.f680f;
        }

        private int width(String str) {
            this.width = -1;
            if (str != null) {
                try {
                    int parseInt = Integer.parseInt(str);
                    this.width = parseInt;
                    if (parseInt < 0) {
                        throw new IllegalFormatWidthException(parseInt);
                    }
                } catch (NumberFormatException unused) {
                }
            }
            return this.width;
        }

        /* access modifiers changed from: package-private */
        public int width() {
            return this.width;
        }

        private int precision(String str) {
            this.precision = -1;
            if (str != null) {
                try {
                    int parseInt = Integer.parseInt(str);
                    this.precision = parseInt;
                    if (parseInt < 0) {
                        throw new IllegalFormatPrecisionException(parseInt);
                    }
                } catch (NumberFormatException unused) {
                }
            }
            return this.precision;
        }

        /* access modifiers changed from: package-private */
        public int precision() {
            return this.precision;
        }

        private char conversion(String str) {
            char charAt = str.charAt(0);
            this.f678c = charAt;
            if (!this.f679dt) {
                if (Conversion.isValid(charAt)) {
                    if (Character.isUpperCase(this.f678c)) {
                        Flags unused = this.f680f.add(Flags.UPPERCASE);
                    }
                    char lowerCase = Character.toLowerCase(this.f678c);
                    this.f678c = lowerCase;
                    if (Conversion.isText(lowerCase)) {
                        this.index = -2;
                    }
                } else {
                    throw new UnknownFormatConversionException(String.valueOf(this.f678c));
                }
            }
            return this.f678c;
        }

        private char conversion() {
            return this.f678c;
        }

        FormatSpecifier(String str, String str2, String str3, String str4, String str5, String str6) {
            index(str);
            flags(str2);
            width(str3);
            precision(str4);
            if (str5 != null) {
                this.f679dt = true;
                if (str5.equals(ExifInterface.GPS_DIRECTION_TRUE)) {
                    Flags unused = this.f680f.add(Flags.UPPERCASE);
                }
            }
            conversion(str6);
            if (this.f679dt) {
                checkDateTime();
            } else if (Conversion.isGeneral(this.f678c)) {
                checkGeneral();
            } else if (Conversion.isCharacter(this.f678c)) {
                checkCharacter();
            } else if (Conversion.isInteger(this.f678c)) {
                checkInteger();
            } else if (Conversion.isFloat(this.f678c)) {
                checkFloat();
            } else if (Conversion.isText(this.f678c)) {
                checkText();
            } else {
                throw new UnknownFormatConversionException(String.valueOf(this.f678c));
            }
        }

        public void print(Object obj, Locale locale) throws IOException {
            if (this.f679dt) {
                printDateTime(obj, locale);
                return;
            }
            char c = this.f678c;
            if (c != '%') {
                if (c != 'C') {
                    if (c != 's') {
                        if (c != 'x') {
                            if (c == 'n') {
                                Formatter.this.f675a.append((CharSequence) System.lineSeparator());
                                return;
                            } else if (c != 'o') {
                                switch (c) {
                                    case 'a':
                                    case 'e':
                                    case 'f':
                                    case 'g':
                                        printFloat(obj, locale);
                                        return;
                                    case 'b':
                                        printBoolean(obj);
                                        return;
                                    case 'c':
                                        break;
                                    case 'd':
                                        break;
                                    case 'h':
                                        printHashCode(obj);
                                        return;
                                    default:
                                        return;
                                }
                            }
                        }
                        printInteger(obj, locale);
                        return;
                    }
                    printString(obj, locale);
                    return;
                }
                printCharacter(obj);
                return;
            }
            Formatter.this.f675a.append('%');
        }

        private void printInteger(Object obj, Locale locale) throws IOException {
            if (obj == null) {
                print("null");
            } else if (obj instanceof Byte) {
                print(((Byte) obj).byteValue(), locale);
            } else if (obj instanceof Short) {
                print(((Short) obj).shortValue(), locale);
            } else if (obj instanceof Integer) {
                print(((Integer) obj).intValue(), locale);
            } else if (obj instanceof Long) {
                print(((Long) obj).longValue(), locale);
            } else if (obj instanceof BigInteger) {
                print((BigInteger) obj, locale);
            } else {
                failConversion(this.f678c, obj);
            }
        }

        private void printFloat(Object obj, Locale locale) throws IOException {
            if (obj == null) {
                print("null");
            } else if (obj instanceof Float) {
                print(((Float) obj).floatValue(), locale);
            } else if (obj instanceof Double) {
                print(((Double) obj).doubleValue(), locale);
            } else if (obj instanceof BigDecimal) {
                print((BigDecimal) obj, locale);
            } else {
                failConversion(this.f678c, obj);
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: java.util.Calendar} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void printDateTime(java.lang.Object r4, java.util.Locale r5) throws java.p026io.IOException {
            /*
                r3 = this;
                if (r4 != 0) goto L_0x0008
                java.lang.String r4 = "null"
                r3.print(r4)
                return
            L_0x0008:
                boolean r0 = r4 instanceof java.lang.Long
                if (r0 == 0) goto L_0x0020
                if (r5 != 0) goto L_0x0011
                java.util.Locale r0 = java.util.Locale.f698US
                goto L_0x0012
            L_0x0011:
                r0 = r5
            L_0x0012:
                java.util.Calendar r0 = java.util.Calendar.getInstance((java.util.Locale) r0)
                java.lang.Long r4 = (java.lang.Long) r4
                long r1 = r4.longValue()
                r0.setTimeInMillis(r1)
                goto L_0x0058
            L_0x0020:
                boolean r0 = r4 instanceof java.util.Date
                if (r0 == 0) goto L_0x0034
                if (r5 != 0) goto L_0x0029
                java.util.Locale r0 = java.util.Locale.f698US
                goto L_0x002a
            L_0x0029:
                r0 = r5
            L_0x002a:
                java.util.Calendar r0 = java.util.Calendar.getInstance((java.util.Locale) r0)
                java.util.Date r4 = (java.util.Date) r4
                r0.setTime(r4)
                goto L_0x0058
            L_0x0034:
                boolean r0 = r4 instanceof java.util.Calendar
                if (r0 == 0) goto L_0x0046
                java.util.Calendar r4 = (java.util.Calendar) r4
                java.lang.Object r4 = r4.clone()
                r0 = r4
                java.util.Calendar r0 = (java.util.Calendar) r0
                r4 = 1
                r0.setLenient(r4)
                goto L_0x0058
            L_0x0046:
                boolean r0 = r4 instanceof java.time.temporal.TemporalAccessor
                if (r0 == 0) goto L_0x0052
                java.time.temporal.TemporalAccessor r4 = (java.time.temporal.TemporalAccessor) r4
                char r0 = r3.f678c
                r3.print((java.time.temporal.TemporalAccessor) r4, (char) r0, (java.util.Locale) r5)
                return
            L_0x0052:
                char r0 = r3.f678c
                r3.failConversion(r0, r4)
                r0 = 0
            L_0x0058:
                char r4 = r3.f678c
                r3.print((java.util.Calendar) r0, (char) r4, (java.util.Locale) r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Formatter.FormatSpecifier.printDateTime(java.lang.Object, java.util.Locale):void");
        }

        private void printCharacter(Object obj) throws IOException {
            String str;
            String str2;
            if (obj == null) {
                print("null");
                return;
            }
            if (obj instanceof Character) {
                str = ((Character) obj).toString();
            } else {
                if (obj instanceof Byte) {
                    byte byteValue = ((Byte) obj).byteValue();
                    if (Character.isValidCodePoint(byteValue)) {
                        str2 = new String(Character.toChars(byteValue));
                    } else {
                        throw new IllegalFormatCodePointException(byteValue);
                    }
                } else if (obj instanceof Short) {
                    short shortValue = ((Short) obj).shortValue();
                    if (Character.isValidCodePoint(shortValue)) {
                        str2 = new String(Character.toChars(shortValue));
                    } else {
                        throw new IllegalFormatCodePointException(shortValue);
                    }
                } else if (obj instanceof Integer) {
                    int intValue = ((Integer) obj).intValue();
                    if (Character.isValidCodePoint(intValue)) {
                        str2 = new String(Character.toChars(intValue));
                    } else {
                        throw new IllegalFormatCodePointException(intValue);
                    }
                } else {
                    failConversion(this.f678c, obj);
                    str = null;
                }
                str = str2;
            }
            print(str);
        }

        private void printString(Object obj, Locale locale) throws IOException {
            if (obj instanceof Formattable) {
                Formatter formatter = Formatter.this;
                if (formatter.locale() != locale) {
                    formatter = new Formatter(formatter.out(), locale);
                }
                ((Formattable) obj).formatTo(formatter, this.f680f.valueOf(), this.width, this.precision);
                return;
            }
            if (this.f680f.contains(Flags.ALTERNATE)) {
                failMismatch(Flags.ALTERNATE, 's');
            }
            if (obj == null) {
                print("null");
            } else {
                print(obj.toString());
            }
        }

        private void printBoolean(Object obj) throws IOException {
            String str;
            if (obj == null) {
                str = Boolean.toString(false);
            } else if (obj instanceof Boolean) {
                str = ((Boolean) obj).toString();
            } else {
                str = Boolean.toString(true);
            }
            print(str);
        }

        private void printHashCode(Object obj) throws IOException {
            print(obj == null ? "null" : Integer.toHexString(obj.hashCode()));
        }

        private void print(String str) throws IOException {
            int i = this.precision;
            if (i != -1 && i < str.length()) {
                str = str.substring(0, this.precision);
            }
            if (this.f680f.contains(Flags.UPPERCASE)) {
                str = str.toUpperCase(Formatter.this.f676l != null ? Formatter.this.f676l : Locale.getDefault());
            }
            Formatter.this.f675a.append((CharSequence) justify(str));
        }

        private String justify(String str) {
            if (this.width == -1) {
                return str;
            }
            StringBuilder sb = new StringBuilder();
            boolean contains = this.f680f.contains(Flags.LEFT_JUSTIFY);
            int length = this.width - str.length();
            if (!contains) {
                for (int i = 0; i < length; i++) {
                    sb.append(' ');
                }
            }
            sb.append(str);
            if (contains) {
                for (int i2 = 0; i2 < length; i2++) {
                    sb.append(' ');
                }
            }
            return sb.toString();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("%");
            sb.append(this.f680f.dup().remove(Flags.UPPERCASE).toString());
            int i = this.index;
            if (i > 0) {
                sb.append(i);
                sb.append('$');
            }
            int i2 = this.width;
            if (i2 != -1) {
                sb.append(i2);
            }
            if (this.precision != -1) {
                sb.append('.');
                sb.append(this.precision);
            }
            if (this.f679dt) {
                sb.append(this.f680f.contains(Flags.UPPERCASE) ? 'T' : 't');
            }
            sb.append(this.f680f.contains(Flags.UPPERCASE) ? Character.toUpperCase(this.f678c) : this.f678c);
            return sb.toString();
        }

        private void checkGeneral() {
            char c = this.f678c;
            if ((c == 'b' || c == 'h') && this.f680f.contains(Flags.ALTERNATE)) {
                failMismatch(Flags.ALTERNATE, this.f678c);
            }
            if (this.width != -1 || !this.f680f.contains(Flags.LEFT_JUSTIFY)) {
                checkBadFlags(Flags.PLUS, Flags.LEADING_SPACE, Flags.ZERO_PAD, Flags.GROUP, Flags.PARENTHESES);
                return;
            }
            throw new MissingFormatWidthException(toString());
        }

        private void checkDateTime() {
            int i = this.precision;
            if (i != -1) {
                throw new IllegalFormatPrecisionException(i);
            } else if (DateTime.isValid(this.f678c)) {
                checkBadFlags(Flags.ALTERNATE, Flags.PLUS, Flags.LEADING_SPACE, Flags.ZERO_PAD, Flags.GROUP, Flags.PARENTHESES);
                if (this.width == -1 && this.f680f.contains(Flags.LEFT_JUSTIFY)) {
                    throw new MissingFormatWidthException(toString());
                }
            } else {
                throw new UnknownFormatConversionException("t" + this.f678c);
            }
        }

        private void checkCharacter() {
            int i = this.precision;
            if (i == -1) {
                checkBadFlags(Flags.ALTERNATE, Flags.PLUS, Flags.LEADING_SPACE, Flags.ZERO_PAD, Flags.GROUP, Flags.PARENTHESES);
                if (this.width == -1 && this.f680f.contains(Flags.LEFT_JUSTIFY)) {
                    throw new MissingFormatWidthException(toString());
                }
                return;
            }
            throw new IllegalFormatPrecisionException(i);
        }

        private void checkInteger() {
            checkNumeric();
            int i = this.precision;
            if (i == -1) {
                char c = this.f678c;
                if (c == 'd') {
                    checkBadFlags(Flags.ALTERNATE);
                } else if (c == 'o') {
                    checkBadFlags(Flags.GROUP);
                } else {
                    checkBadFlags(Flags.GROUP);
                }
            } else {
                throw new IllegalFormatPrecisionException(i);
            }
        }

        private void checkBadFlags(Flags... flagsArr) {
            for (int i = 0; i < flagsArr.length; i++) {
                if (this.f680f.contains(flagsArr[i])) {
                    failMismatch(flagsArr[i], this.f678c);
                }
            }
        }

        private void checkFloat() {
            checkNumeric();
            char c = this.f678c;
            if (c != 'f') {
                if (c == 'a') {
                    checkBadFlags(Flags.PARENTHESES, Flags.GROUP);
                } else if (c == 'e') {
                    checkBadFlags(Flags.GROUP);
                } else if (c == 'g') {
                    checkBadFlags(Flags.ALTERNATE);
                }
            }
        }

        private void checkNumeric() {
            int i = this.width;
            if (i == -1 || i >= 0) {
                int i2 = this.precision;
                if (i2 != -1 && i2 < 0) {
                    throw new IllegalFormatPrecisionException(i2);
                } else if (i == -1 && (this.f680f.contains(Flags.LEFT_JUSTIFY) || this.f680f.contains(Flags.ZERO_PAD))) {
                    throw new MissingFormatWidthException(toString());
                } else if ((this.f680f.contains(Flags.PLUS) && this.f680f.contains(Flags.LEADING_SPACE)) || (this.f680f.contains(Flags.LEFT_JUSTIFY) && this.f680f.contains(Flags.ZERO_PAD))) {
                    throw new IllegalFormatFlagsException(this.f680f.toString());
                }
            } else {
                throw new IllegalFormatWidthException(i);
            }
        }

        private void checkText() {
            int i = this.precision;
            if (i == -1) {
                char c = this.f678c;
                if (c != '%') {
                    if (c == 'n') {
                        int i2 = this.width;
                        if (i2 != -1) {
                            throw new IllegalFormatWidthException(i2);
                        } else if (this.f680f.valueOf() != Flags.NONE.valueOf()) {
                            throw new IllegalFormatFlagsException(this.f680f.toString());
                        }
                    }
                } else if (this.f680f.valueOf() != Flags.LEFT_JUSTIFY.valueOf() && this.f680f.valueOf() != Flags.NONE.valueOf()) {
                    throw new IllegalFormatFlagsException(this.f680f.toString());
                } else if (this.width == -1 && this.f680f.contains(Flags.LEFT_JUSTIFY)) {
                    throw new MissingFormatWidthException(toString());
                }
            } else {
                throw new IllegalFormatPrecisionException(i);
            }
        }

        private void print(byte b, Locale locale) throws IOException {
            char c;
            long j = (long) b;
            if (b < 0 && ((c = this.f678c) == 'o' || c == 'x')) {
                j += 256;
            }
            print(j, locale);
        }

        private void print(short s, Locale locale) throws IOException {
            char c;
            long j = (long) s;
            if (s < 0 && ((c = this.f678c) == 'o' || c == 'x')) {
                j += 65536;
            }
            print(j, locale);
        }

        private void print(int i, Locale locale) throws IOException {
            char c;
            long j = (long) i;
            if (i < 0 && ((c = this.f678c) == 'o' || c == 'x')) {
                j += WifiManager.WIFI_FEATURE_P2P_RAND_MAC;
            }
            print(j, locale);
        }

        private void print(long j, Locale locale) throws IOException {
            int i;
            int i2;
            char[] cArr;
            StringBuilder sb = new StringBuilder();
            char c = this.f678c;
            int i3 = 0;
            if (c == 'd') {
                int i4 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
                boolean z = i4 < 0;
                if (i4 < 0) {
                    cArr = Long.toString(j, 10).substring(1).toCharArray();
                } else {
                    cArr = Long.toString(j, 10).toCharArray();
                }
                leadingSign(sb, z);
                Flags flags = this.f680f;
                localizedMagnitude(sb, cArr, flags, adjustWidth(this.width, flags, z), locale);
                trailingSign(sb, z);
            } else if (c == 'o') {
                checkBadFlags(Flags.PARENTHESES, Flags.LEADING_SPACE, Flags.PLUS);
                String octalString = Long.toOctalString(j);
                if (this.f680f.contains(Flags.ALTERNATE)) {
                    i2 = octalString.length() + 1;
                } else {
                    i2 = octalString.length();
                }
                if (this.f680f.contains(Flags.ALTERNATE)) {
                    sb.append('0');
                }
                if (this.f680f.contains(Flags.ZERO_PAD)) {
                    while (i3 < this.width - i2) {
                        sb.append('0');
                        i3++;
                    }
                }
                sb.append(octalString);
            } else if (c == 'x') {
                checkBadFlags(Flags.PARENTHESES, Flags.LEADING_SPACE, Flags.PLUS);
                String hexString = Long.toHexString(j);
                if (this.f680f.contains(Flags.ALTERNATE)) {
                    i = hexString.length() + 2;
                } else {
                    i = hexString.length();
                }
                if (this.f680f.contains(Flags.ALTERNATE)) {
                    sb.append(this.f680f.contains(Flags.UPPERCASE) ? "0X" : "0x");
                }
                if (this.f680f.contains(Flags.ZERO_PAD)) {
                    while (i3 < this.width - i) {
                        sb.append('0');
                        i3++;
                    }
                }
                if (this.f680f.contains(Flags.UPPERCASE)) {
                    hexString = hexString.toUpperCase();
                }
                sb.append(hexString);
            }
            Formatter.this.f675a.append((CharSequence) justify(sb.toString()));
        }

        private StringBuilder leadingSign(StringBuilder sb, boolean z) {
            if (!z) {
                if (this.f680f.contains(Flags.PLUS)) {
                    sb.append('+');
                } else if (this.f680f.contains(Flags.LEADING_SPACE)) {
                    sb.append(' ');
                }
            } else if (this.f680f.contains(Flags.PARENTHESES)) {
                sb.append('(');
            } else {
                sb.append('-');
            }
            return sb;
        }

        private StringBuilder trailingSign(StringBuilder sb, boolean z) {
            if (z && this.f680f.contains(Flags.PARENTHESES)) {
                sb.append(')');
            }
            return sb;
        }

        private void print(BigInteger bigInteger, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            boolean z = false;
            boolean z2 = bigInteger.signum() == -1;
            BigInteger abs = bigInteger.abs();
            leadingSign(sb, z2);
            char c = this.f678c;
            if (c == 'd') {
                char[] charArray = abs.toString().toCharArray();
                Flags flags = this.f680f;
                localizedMagnitude(sb, charArray, flags, adjustWidth(this.width, flags, z2), locale);
            } else if (c == 'o') {
                String bigInteger2 = abs.toString(8);
                int length = bigInteger2.length() + sb.length();
                if (z2 && this.f680f.contains(Flags.PARENTHESES)) {
                    length++;
                }
                if (this.f680f.contains(Flags.ALTERNATE)) {
                    length++;
                    sb.append('0');
                }
                if (this.f680f.contains(Flags.ZERO_PAD)) {
                    for (int i = 0; i < this.width - length; i++) {
                        sb.append('0');
                    }
                }
                sb.append(bigInteger2);
            } else if (c == 'x') {
                String bigInteger3 = abs.toString(16);
                int length2 = bigInteger3.length() + sb.length();
                if (z2 && this.f680f.contains(Flags.PARENTHESES)) {
                    length2++;
                }
                if (this.f680f.contains(Flags.ALTERNATE)) {
                    length2 += 2;
                    sb.append(this.f680f.contains(Flags.UPPERCASE) ? "0X" : "0x");
                }
                if (this.f680f.contains(Flags.ZERO_PAD)) {
                    for (int i2 = 0; i2 < this.width - length2; i2++) {
                        sb.append('0');
                    }
                }
                if (this.f680f.contains(Flags.UPPERCASE)) {
                    bigInteger3 = bigInteger3.toUpperCase();
                }
                sb.append(bigInteger3);
            }
            if (bigInteger.signum() == -1) {
                z = true;
            }
            trailingSign(sb, z);
            Formatter.this.f675a.append((CharSequence) justify(sb.toString()));
        }

        private void print(float f, Locale locale) throws IOException {
            print((double) f, locale);
        }

        private void print(double d, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            boolean z = Double.compare(d, 0.0d) == -1;
            if (!Double.isNaN(d)) {
                double abs = Math.abs(d);
                leadingSign(sb, z);
                if (!Double.isInfinite(abs)) {
                    print(sb, abs, locale, this.f680f, this.f678c, this.precision, z);
                } else {
                    sb.append(this.f680f.contains(Flags.UPPERCASE) ? "INFINITY" : "Infinity");
                }
                trailingSign(sb, z);
            } else {
                sb.append(this.f680f.contains(Flags.UPPERCASE) ? "NAN" : "NaN");
            }
            Formatter.this.f675a.append((CharSequence) justify(sb.toString()));
        }

        private void print(StringBuilder sb, double d, Locale locale, Flags flags, char c, int i, boolean z) throws IOException {
            char[] cArr;
            char[] cArr2;
            int i2;
            char[] cArr3;
            Locale locale2;
            String str;
            StringBuilder sb2 = sb;
            double d2 = d;
            Flags flags2 = flags;
            char c2 = c;
            int i3 = i;
            boolean z2 = z;
            char c3 = 'e';
            if (c2 == 'e') {
                if (i3 == -1) {
                    i3 = 6;
                }
                FormattedFloatingDecimal valueOf = FormattedFloatingDecimal.valueOf(d2, i3, FormattedFloatingDecimal.Form.SCIENTIFIC);
                char[] addZeros = addZeros(valueOf.getMantissa(), i3);
                char[] addDot = (!flags2.contains(Flags.ALTERNATE) || i3 != 0) ? addZeros : addDot(addZeros);
                if (d2 == 0.0d) {
                    cArr3 = new char[]{'+', '0', '0'};
                } else {
                    cArr3 = valueOf.getExponent();
                }
                char[] cArr4 = cArr3;
                int i4 = this.width;
                if (i4 != -1) {
                    i4 = adjustWidth((i4 - cArr4.length) - 1, flags2, z2);
                }
                localizedMagnitude(sb, addDot, flags, i4, locale);
                if (locale != null) {
                    locale2 = locale;
                } else {
                    locale2 = Locale.getDefault();
                }
                DecimalFormatData instance = DecimalFormatData.getInstance(locale2);
                if (flags2.contains(Flags.UPPERCASE)) {
                    str = instance.getExponentSeparator().toUpperCase(locale2);
                } else {
                    str = instance.getExponentSeparator().toLowerCase(locale2);
                }
                sb2.append(str);
                Flags remove = flags.dup().remove(Flags.GROUP);
                sb2.append(cArr4[0]);
                char[] cArr5 = new char[(cArr4.length - 1)];
                System.arraycopy((Object) cArr4, 1, (Object) cArr5, 0, cArr4.length - 1);
                sb2.append((CharSequence) localizedMagnitude((StringBuilder) null, cArr5, remove, -1, locale));
            } else if (c2 == 'f') {
                if (i3 == -1) {
                    i3 = 6;
                }
                char[] addZeros2 = addZeros(FormattedFloatingDecimal.valueOf(d2, i3, FormattedFloatingDecimal.Form.DECIMAL_FLOAT).getMantissa(), i3);
                if (flags2.contains(Flags.ALTERNATE) && i3 == 0) {
                    addZeros2 = addDot(addZeros2);
                }
                char[] cArr6 = addZeros2;
                int i5 = this.width;
                if (i5 != -1) {
                    i5 = adjustWidth(i5, flags2, z2);
                }
                localizedMagnitude(sb, cArr6, flags, i5, locale);
            } else if (c2 == 'g') {
                if (i3 == -1) {
                    i3 = 6;
                } else if (i3 == 0) {
                    i3 = 1;
                }
                if (d2 == 0.0d) {
                    cArr2 = new char[]{'0'};
                    cArr = null;
                    i2 = 0;
                } else {
                    FormattedFloatingDecimal valueOf2 = FormattedFloatingDecimal.valueOf(d2, i3, FormattedFloatingDecimal.Form.GENERAL);
                    char[] exponent = valueOf2.getExponent();
                    cArr2 = valueOf2.getMantissa();
                    i2 = valueOf2.getExponentRounded();
                    cArr = exponent;
                }
                int i6 = cArr != null ? i3 - 1 : i3 - (i2 + 1);
                char[] addZeros3 = addZeros(cArr2, i6);
                if (flags2.contains(Flags.ALTERNATE) && i6 == 0) {
                    addZeros3 = addDot(addZeros3);
                }
                char[] cArr7 = addZeros3;
                int i7 = this.width;
                if (i7 != -1) {
                    if (cArr != null) {
                        i7 = adjustWidth((i7 - cArr.length) - 1, flags2, z2);
                    } else {
                        i7 = adjustWidth(i7, flags2, z2);
                    }
                }
                localizedMagnitude(sb, cArr7, flags, i7, locale);
                if (cArr != null) {
                    if (flags2.contains(Flags.UPPERCASE)) {
                        c3 = 'E';
                    }
                    sb2.append(c3);
                    Flags remove2 = flags.dup().remove(Flags.GROUP);
                    sb2.append(cArr[0]);
                    char[] cArr8 = new char[(cArr.length - 1)];
                    System.arraycopy((Object) cArr, 1, (Object) cArr8, 0, cArr.length - 1);
                    sb2.append((CharSequence) localizedMagnitude((StringBuilder) null, cArr8, remove2, -1, locale));
                }
            } else if (c2 == 'a') {
                if (i3 == -1) {
                    i3 = 0;
                } else if (i3 == 0) {
                    i3 = 1;
                }
                String hexDouble = hexDouble(d2, i3);
                boolean contains = flags2.contains(Flags.UPPERCASE);
                sb2.append(contains ? "0X" : "0x");
                if (flags2.contains(Flags.ZERO_PAD)) {
                    for (int i8 = 0; i8 < (this.width - hexDouble.length()) - 2; i8++) {
                        sb2.append('0');
                    }
                }
                char c4 = 'p';
                int indexOf = hexDouble.indexOf(112);
                char[] charArray = hexDouble.substring(0, indexOf).toCharArray();
                if (contains) {
                    charArray = new String(charArray).toUpperCase(Locale.f698US).toCharArray();
                }
                if (i3 != 0) {
                    charArray = addZeros(charArray, i3);
                }
                sb2.append(charArray);
                if (contains) {
                    c4 = 'P';
                }
                sb2.append(c4);
                sb2.append(hexDouble.substring(indexOf + 1));
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:17:0x0030  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private char[] addZeros(char[] r5, int r6) {
            /*
                r4 = this;
                r4 = 0
                r0 = r4
            L_0x0002:
                int r1 = r5.length
                r2 = 46
                if (r0 >= r1) goto L_0x000f
                char r1 = r5[r0]
                if (r1 != r2) goto L_0x000c
                goto L_0x000f
            L_0x000c:
                int r0 = r0 + 1
                goto L_0x0002
            L_0x000f:
                int r1 = r5.length
                if (r0 != r1) goto L_0x0014
                r1 = 1
                goto L_0x0015
            L_0x0014:
                r1 = r4
            L_0x0015:
                int r3 = r5.length
                int r3 = r3 - r0
                r0 = r1 ^ 1
                int r3 = r3 - r0
                if (r3 != r6) goto L_0x001d
                return r5
            L_0x001d:
                int r0 = r5.length
                int r0 = r0 + r6
                int r0 = r0 - r3
                int r0 = r0 + r1
                char[] r6 = new char[r0]
                int r3 = r5.length
                java.lang.System.arraycopy((java.lang.Object) r5, (int) r4, (java.lang.Object) r6, (int) r4, (int) r3)
                int r4 = r5.length
                if (r1 == 0) goto L_0x002e
                int r5 = r5.length
                r6[r5] = r2
                goto L_0x0034
            L_0x002e:
                if (r4 >= r0) goto L_0x0037
                r5 = 48
                r6[r4] = r5
            L_0x0034:
                int r4 = r4 + 1
                goto L_0x002e
            L_0x0037:
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Formatter.FormatSpecifier.addZeros(char[], int):char[]");
        }

        private String hexDouble(double d, int i) {
            double d2;
            int i2 = i;
            if (!Double.isFinite(d) || d == 0.0d || i2 == 0 || i2 >= 13) {
                return Double.toHexString(d).substring(2);
            }
            boolean z = true;
            boolean z2 = Math.getExponent(d) == -1023;
            if (z2) {
                Formatter.scaleUp = Math.scalb(1.0d, 54);
                d2 = Formatter.scaleUp * d;
                Math.getExponent(d2);
            } else {
                d2 = d;
            }
            int i3 = 53 - ((i2 * 4) + 1);
            long doubleToLongBits = Double.doubleToLongBits(d2);
            long j = (Long.MAX_VALUE & doubleToLongBits) >> i3;
            long j2 = (~(-1 << i3)) & doubleToLongBits;
            boolean z3 = (j & 1) == 0;
            long j3 = 1 << (i3 - 1);
            boolean z4 = (j3 & j2) != 0;
            if (i3 <= 1 || ((~j3) & j2) == 0) {
                z = false;
            }
            if ((z3 && z4 && z) || (!z3 && z4)) {
                j++;
            }
            double longBitsToDouble = Double.longBitsToDouble((Long.MIN_VALUE & doubleToLongBits) | (j << i3));
            if (Double.isInfinite(longBitsToDouble)) {
                return "1.0p1024";
            }
            String substring = Double.toHexString(longBitsToDouble).substring(2);
            if (!z2) {
                return substring;
            }
            int indexOf = substring.indexOf(112);
            if (indexOf == -1) {
                return null;
            }
            return substring.substring(0, indexOf) + "p" + Integer.toString(Integer.parseInt(substring.substring(indexOf + 1)) - 54);
        }

        private void print(BigDecimal bigDecimal, Locale locale) throws IOException {
            char c = this.f678c;
            if (c == 'a') {
                failConversion(c, bigDecimal);
            }
            StringBuilder sb = new StringBuilder();
            boolean z = bigDecimal.signum() == -1;
            BigDecimal abs = bigDecimal.abs();
            leadingSign(sb, z);
            print(sb, abs, locale, this.f680f, this.f678c, this.precision, z);
            trailingSign(sb, z);
            Formatter.this.f675a.append((CharSequence) justify(sb.toString()));
        }

        private void print(StringBuilder sb, BigDecimal bigDecimal, Locale locale, Flags flags, char c, int i, boolean z) throws IOException {
            BigDecimal bigDecimal2;
            int i2;
            int i3;
            StringBuilder sb2 = sb;
            BigDecimal bigDecimal3 = bigDecimal;
            Flags flags2 = flags;
            char c2 = c;
            int i4 = i;
            boolean z2 = z;
            int i5 = 0;
            char c3 = 'e';
            if (c2 == 'e') {
                if (i4 == -1) {
                    i4 = 6;
                }
                int scale = bigDecimal.scale();
                int precision2 = bigDecimal.precision();
                int i6 = precision2 - 1;
                if (i4 > i6) {
                    i2 = i4 - i6;
                    i3 = precision2;
                } else {
                    i3 = i4 + 1;
                    i2 = 0;
                }
                BigDecimal bigDecimal4 = new BigDecimal(bigDecimal.unscaledValue(), scale, new MathContext(i3));
                BigDecimalLayout bigDecimalLayout = new BigDecimalLayout(bigDecimal4.unscaledValue(), bigDecimal4.scale(), BigDecimalLayoutForm.SCIENTIFIC);
                char[] mantissa = bigDecimalLayout.mantissa();
                if ((precision2 == 1 || !bigDecimalLayout.hasDot()) && (i2 > 0 || flags2.contains(Flags.ALTERNATE))) {
                    mantissa = addDot(mantissa);
                }
                char[] trailingZeros = trailingZeros(mantissa, i2);
                char[] exponent = bigDecimalLayout.exponent();
                int i7 = this.width;
                if (i7 != -1) {
                    i7 = adjustWidth((i7 - exponent.length) - 1, flags2, z2);
                }
                localizedMagnitude(sb, trailingZeros, flags, i7, locale);
                if (flags2.contains(Flags.UPPERCASE)) {
                    c3 = 'E';
                }
                sb.append(c3);
                Flags remove = flags.dup().remove(Flags.GROUP);
                sb.append(exponent[0]);
                char[] cArr = new char[(exponent.length - 1)];
                System.arraycopy((Object) exponent, 1, (Object) cArr, 0, exponent.length - 1);
                sb.append((CharSequence) localizedMagnitude((StringBuilder) null, cArr, remove, -1, locale));
            } else if (c2 == 'f') {
                if (i4 == -1) {
                    i4 = 6;
                }
                int scale2 = bigDecimal.scale();
                if (scale2 > i4) {
                    int precision3 = bigDecimal.precision();
                    bigDecimal2 = precision3 <= scale2 ? bigDecimal3.setScale(i4, RoundingMode.HALF_UP) : new BigDecimal(bigDecimal.unscaledValue(), scale2, new MathContext(precision3 - (scale2 - i4)));
                } else {
                    bigDecimal2 = bigDecimal3;
                }
                BigDecimalLayout bigDecimalLayout2 = new BigDecimalLayout(bigDecimal2.unscaledValue(), bigDecimal2.scale(), BigDecimalLayoutForm.DECIMAL_FLOAT);
                char[] mantissa2 = bigDecimalLayout2.mantissa();
                if (bigDecimalLayout2.scale() < i4) {
                    i5 = i4 - bigDecimalLayout2.scale();
                }
                if (bigDecimalLayout2.scale() == 0 && (flags2.contains(Flags.ALTERNATE) || i5 > 0)) {
                    mantissa2 = addDot(bigDecimalLayout2.mantissa());
                }
                localizedMagnitude(sb, trailingZeros(mantissa2, i5), flags, adjustWidth(this.width, flags2, z2), locale);
            } else if (c2 == 'g') {
                if (i4 == -1) {
                    i4 = 6;
                } else if (i4 == 0) {
                    i4 = 1;
                }
                BigDecimal valueOf = BigDecimal.valueOf(1, 4);
                BigDecimal valueOf2 = BigDecimal.valueOf(1, -i4);
                if (bigDecimal3.equals(BigDecimal.ZERO) || (bigDecimal3.compareTo(valueOf) != -1 && bigDecimal3.compareTo(valueOf2) == -1)) {
                    print(sb, bigDecimal, locale, flags, 'f', (i4 - ((-bigDecimal.scale()) + (bigDecimal.unscaledValue().toString().length() - 1))) - 1, z);
                } else {
                    print(sb, bigDecimal, locale, flags, 'e', i4 - 1, z);
                }
            }
        }

        private class BigDecimalLayout {
            private boolean dot = false;
            private StringBuilder exp;
            private StringBuilder mant;
            private int scale;

            public BigDecimalLayout(BigInteger bigInteger, int i, BigDecimalLayoutForm bigDecimalLayoutForm) {
                layout(bigInteger, i, bigDecimalLayoutForm);
            }

            public boolean hasDot() {
                return this.dot;
            }

            public int scale() {
                return this.scale;
            }

            public char[] layoutChars() {
                StringBuilder sb = new StringBuilder((CharSequence) this.mant);
                if (this.exp != null) {
                    sb.append('E');
                    sb.append((CharSequence) this.exp);
                }
                return toCharArray(sb);
            }

            public char[] mantissa() {
                return toCharArray(this.mant);
            }

            public char[] exponent() {
                return toCharArray(this.exp);
            }

            private char[] toCharArray(StringBuilder sb) {
                if (sb == null) {
                    return null;
                }
                int length = sb.length();
                char[] cArr = new char[length];
                sb.getChars(0, length, cArr, 0);
                return cArr;
            }

            private void layout(BigInteger bigInteger, int i, BigDecimalLayoutForm bigDecimalLayoutForm) {
                char[] charArray = bigInteger.toString().toCharArray();
                this.scale = i;
                StringBuilder sb = new StringBuilder(charArray.length + 14);
                this.mant = sb;
                if (i == 0) {
                    int length = charArray.length;
                    if (length > 1) {
                        sb.append(charArray[0]);
                        if (bigDecimalLayoutForm == BigDecimalLayoutForm.SCIENTIFIC) {
                            this.mant.append('.');
                            this.dot = true;
                            int i2 = length - 1;
                            this.mant.append(charArray, 1, i2);
                            StringBuilder sb2 = new StringBuilder("+");
                            this.exp = sb2;
                            if (length < 10) {
                                sb2.append("0");
                                sb2.append(i2);
                                return;
                            }
                            sb2.append(i2);
                            return;
                        }
                        this.mant.append(charArray, 1, length - 1);
                        return;
                    }
                    sb.append(charArray);
                    if (bigDecimalLayoutForm == BigDecimalLayoutForm.SCIENTIFIC) {
                        this.exp = new StringBuilder("+00");
                        return;
                    }
                    return;
                }
                long length2 = (-((long) i)) + ((long) (charArray.length - 1));
                if (bigDecimalLayoutForm == BigDecimalLayoutForm.DECIMAL_FLOAT) {
                    int length3 = i - charArray.length;
                    if (length3 >= 0) {
                        this.mant.append("0.");
                        this.dot = true;
                        while (length3 > 0) {
                            this.mant.append('0');
                            length3--;
                        }
                        this.mant.append(charArray);
                        return;
                    }
                    int i3 = -length3;
                    if (i3 < charArray.length) {
                        this.mant.append(charArray, 0, i3);
                        this.mant.append('.');
                        this.dot = true;
                        this.mant.append(charArray, i3, i);
                        return;
                    }
                    this.mant.append(charArray, 0, charArray.length);
                    for (int i4 = 0; i4 < (-i); i4++) {
                        this.mant.append('0');
                    }
                    this.scale = 0;
                    return;
                }
                this.mant.append(charArray[0]);
                if (charArray.length > 1) {
                    this.mant.append('.');
                    this.dot = true;
                    this.mant.append(charArray, 1, charArray.length - 1);
                }
                StringBuilder sb3 = new StringBuilder();
                this.exp = sb3;
                int i5 = (length2 > 0 ? 1 : (length2 == 0 ? 0 : -1));
                if (i5 != 0) {
                    long abs = Math.abs(length2);
                    this.exp.append(i5 < 0 ? '-' : '+');
                    if (abs < 10) {
                        this.exp.append('0');
                    }
                    this.exp.append(abs);
                    return;
                }
                sb3.append("+00");
            }
        }

        private int adjustWidth(int i, Flags flags, boolean z) {
            return (i == -1 || !z || !flags.contains(Flags.PARENTHESES)) ? i : i - 1;
        }

        private char[] addDot(char[] cArr) {
            int length = cArr.length + 1;
            char[] cArr2 = new char[length];
            System.arraycopy((Object) cArr, 0, (Object) cArr2, 0, cArr.length);
            cArr2[length - 1] = '.';
            return cArr2;
        }

        private char[] trailingZeros(char[] cArr, int i) {
            if (i <= 0) {
                return cArr;
            }
            int length = cArr.length + i;
            char[] cArr2 = new char[length];
            System.arraycopy((Object) cArr, 0, (Object) cArr2, 0, cArr.length);
            for (int length2 = cArr.length; length2 < length; length2++) {
                cArr2[length2] = '0';
            }
            return cArr2;
        }

        private void print(Calendar calendar, char c, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            print(sb, calendar, c, locale);
            String justify = justify(sb.toString());
            if (this.f680f.contains(Flags.UPPERCASE)) {
                justify = justify.toUpperCase();
            }
            Formatter.this.f675a.append((CharSequence) justify);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0187, code lost:
            r0 = r1.get(7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x018c, code lost:
            if (r6 != null) goto L_0x0191;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:0x018e, code lost:
            r1 = java.util.Locale.f698US;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0191, code lost:
            r1 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x0192, code lost:
            r1 = java.text.DateFormatSymbols.getInstance(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x0198, code lost:
            if (r2 != 'A') goto L_0x01a5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x019a, code lost:
            r7.append(r1.getWeekdays()[r0]);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x01a5, code lost:
            r7.append(r1.getShortWeekdays()[r0]);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.lang.Appendable print(java.lang.StringBuilder r17, java.util.Calendar r18, char r19, java.util.Locale r20) throws java.p026io.IOException {
            /*
                r16 = this;
                r0 = r16
                r1 = r18
                r2 = r19
                r6 = r20
                if (r17 != 0) goto L_0x0011
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                r7 = r3
                goto L_0x0013
            L_0x0011:
                r7 = r17
            L_0x0013:
                r3 = 70
                r8 = 89
                r9 = 100
                if (r2 == r3) goto L_0x030e
                r3 = 104(0x68, float:1.46E-43)
                r10 = 2
                if (r2 == r3) goto L_0x02e8
                r3 = 112(0x70, float:1.57E-43)
                if (r2 == r3) goto L_0x02bc
                r11 = 72
                r13 = 73
                if (r2 == r11) goto L_0x0284
                if (r2 == r13) goto L_0x0284
                r14 = 121(0x79, float:1.7E-43)
                r15 = 1
                if (r2 == r8) goto L_0x025a
                r3 = 90
                if (r2 == r3) goto L_0x023c
                r13 = 114(0x72, float:1.6E-43)
                r5 = 32
                r12 = 77
                r4 = 58
                if (r2 == r13) goto L_0x0206
                r13 = 115(0x73, float:1.61E-43)
                if (r2 == r13) goto L_0x01ec
                if (r2 == r14) goto L_0x025a
                r13 = 122(0x7a, float:1.71E-43)
                if (r2 == r13) goto L_0x01b0
                switch(r2) {
                    case 65: goto L_0x0187;
                    case 66: goto L_0x02e8;
                    case 67: goto L_0x025a;
                    case 68: goto L_0x0170;
                    default: goto L_0x004c;
                }
            L_0x004c:
                r13 = 14
                switch(r2) {
                    case 76: goto L_0x0158;
                    case 77: goto L_0x013e;
                    case 78: goto L_0x0121;
                    default: goto L_0x0051;
                }
            L_0x0051:
                r13 = 84
                switch(r2) {
                    case 81: goto L_0x010b;
                    case 82: goto L_0x00f5;
                    case 83: goto L_0x00db;
                    case 84: goto L_0x00f5;
                    default: goto L_0x0056;
                }
            L_0x0056:
                switch(r2) {
                    case 97: goto L_0x0187;
                    case 98: goto L_0x02e8;
                    case 99: goto L_0x00af;
                    case 100: goto L_0x0090;
                    case 101: goto L_0x0090;
                    default: goto L_0x0059;
                }
            L_0x0059:
                switch(r2) {
                    case 106: goto L_0x0077;
                    case 107: goto L_0x0284;
                    case 108: goto L_0x0284;
                    case 109: goto L_0x005e;
                    default: goto L_0x005c;
                }
            L_0x005c:
                goto L_0x0323
            L_0x005e:
                int r1 = r1.get(r10)
                int r1 = r1 + r15
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x0077:
                r2 = 6
                int r1 = r1.get(r2)
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r5 = 3
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x0090:
                r3 = 5
                int r1 = r1.get(r3)
                if (r2 != r9) goto L_0x009a
                java.util.Formatter$Flags r2 = java.util.Formatter.Flags.ZERO_PAD
                goto L_0x009c
            L_0x009a:
                java.util.Formatter$Flags r2 = java.util.Formatter.Flags.NONE
            L_0x009c:
                r4 = r2
                r2 = 0
                long r8 = (long) r1
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x00af:
                r2 = 97
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r2, (java.util.Locale) r6)
                r2.append((char) r5)
                r2 = 98
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r2, (java.util.Locale) r6)
                r2.append((char) r5)
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r9, (java.util.Locale) r6)
                r2.append((char) r5)
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r13, (java.util.Locale) r6)
                r2.append((char) r5)
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r3, (java.util.Locale) r6)
                r2.append((char) r5)
                r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r8, (java.util.Locale) r6)
                goto L_0x0323
            L_0x00db:
                r2 = 13
                int r1 = r1.get(r2)
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x00f5:
                java.lang.Appendable r3 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r11, (java.util.Locale) r6)
                r3.append((char) r4)
                r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r12, (java.util.Locale) r6)
                if (r2 != r13) goto L_0x0323
                r7.append((char) r4)
                r2 = 83
                r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r2, (java.util.Locale) r6)
                goto L_0x0323
            L_0x010b:
                long r2 = r18.getTimeInMillis()
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.NONE
                r1 = 0
                int r5 = r0.width
                r0 = r16
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x0121:
                int r1 = r1.get(r13)
                r2 = 1000000(0xf4240, float:1.401298E-39)
                int r1 = r1 * r2
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r5 = 9
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x013e:
                r2 = 12
                int r1 = r1.get(r2)
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x0158:
                int r1 = r1.get(r13)
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r5 = 3
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x0170:
                r2 = 109(0x6d, float:1.53E-43)
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r2, (java.util.Locale) r6)
                r3 = 47
                r2.append((char) r3)
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r9, (java.util.Locale) r6)
                r2.append((char) r3)
                r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r14, (java.util.Locale) r6)
                goto L_0x0323
            L_0x0187:
                r0 = 7
                int r0 = r1.get(r0)
                if (r6 != 0) goto L_0x0191
                java.util.Locale r1 = java.util.Locale.f698US
                goto L_0x0192
            L_0x0191:
                r1 = r6
            L_0x0192:
                java.text.DateFormatSymbols r1 = java.text.DateFormatSymbols.getInstance(r1)
                r3 = 65
                if (r2 != r3) goto L_0x01a5
                java.lang.String[] r1 = r1.getWeekdays()
                r0 = r1[r0]
                r7.append((java.lang.String) r0)
                goto L_0x0323
            L_0x01a5:
                java.lang.String[] r1 = r1.getShortWeekdays()
                r0 = r1[r0]
                r7.append((java.lang.String) r0)
                goto L_0x0323
            L_0x01b0:
                r2 = 15
                int r2 = r1.get(r2)
                r3 = 16
                int r1 = r1.get(r3)
                int r2 = r2 + r1
                if (r2 >= 0) goto L_0x01c0
                goto L_0x01c1
            L_0x01c0:
                r15 = 0
            L_0x01c1:
                if (r15 == 0) goto L_0x01c6
                r4 = 45
                goto L_0x01c8
            L_0x01c6:
                r4 = 43
            L_0x01c8:
                r7.append((char) r4)
                if (r15 == 0) goto L_0x01ce
                int r2 = -r2
            L_0x01ce:
                r1 = 60000(0xea60, float:8.4078E-41)
                int r2 = r2 / r1
                int r1 = r2 / 60
                int r1 = r1 * r9
                int r2 = r2 % 60
                int r1 = r1 + r2
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r5 = 4
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x01ec:
                long r1 = r18.getTimeInMillis()
                r3 = 1000(0x3e8, double:4.94E-321)
                long r2 = r1 / r3
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.NONE
                r1 = 0
                int r5 = r0.width
                r0 = r16
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x0206:
                r2 = 73
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r2, (java.util.Locale) r6)
                r2.append((char) r4)
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r12, (java.util.Locale) r6)
                r2.append((char) r4)
                r2 = 83
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r2, (java.util.Locale) r6)
                r2.append((char) r5)
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                r3 = 112(0x70, float:1.57E-43)
                r0.print((java.lang.StringBuilder) r2, (java.util.Calendar) r1, (char) r3, (java.util.Locale) r6)
                java.lang.String r0 = r2.toString()
                if (r6 == 0) goto L_0x0231
                r1 = r6
                goto L_0x0233
            L_0x0231:
                java.util.Locale r1 = java.util.Locale.f698US
            L_0x0233:
                java.lang.String r0 = r0.toUpperCase(r1)
                r7.append((java.lang.String) r0)
                goto L_0x0323
            L_0x023c:
                java.util.TimeZone r0 = r18.getTimeZone()
                r2 = 16
                int r1 = r1.get(r2)
                if (r1 == 0) goto L_0x0249
                goto L_0x024a
            L_0x0249:
                r15 = 0
            L_0x024a:
                if (r6 != 0) goto L_0x024f
                java.util.Locale r1 = java.util.Locale.f698US
                goto L_0x0250
            L_0x024f:
                r1 = r6
            L_0x0250:
                r2 = 0
                java.lang.String r0 = r0.getDisplayName(r15, r2, r1)
                r7.append((java.lang.String) r0)
                goto L_0x0323
            L_0x025a:
                int r1 = r1.get(r15)
                r3 = 67
                if (r2 == r3) goto L_0x026e
                if (r2 == r8) goto L_0x026b
                if (r2 == r14) goto L_0x0268
            L_0x0266:
                r5 = r10
                goto L_0x0271
            L_0x0268:
                int r1 = r1 % 100
                goto L_0x0266
            L_0x026b:
                r2 = 4
                r5 = r2
                goto L_0x0271
            L_0x026e:
                int r1 = r1 / 100
                goto L_0x0266
            L_0x0271:
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD
                r2 = 0
                long r8 = (long) r1
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x0284:
                r3 = 11
                int r1 = r1.get(r3)
                r3 = 73
                if (r2 == r3) goto L_0x0292
                r3 = 108(0x6c, float:1.51E-43)
                if (r2 != r3) goto L_0x029e
            L_0x0292:
                r3 = 12
                if (r1 == 0) goto L_0x029c
                if (r1 != r3) goto L_0x0299
                goto L_0x029c
            L_0x0299:
                int r12 = r1 % 12
                goto L_0x029d
            L_0x029c:
                r12 = r3
            L_0x029d:
                r1 = r12
            L_0x029e:
                if (r2 == r11) goto L_0x02a8
                r3 = 73
                if (r2 != r3) goto L_0x02a5
                goto L_0x02a8
            L_0x02a5:
                java.util.Formatter$Flags r2 = java.util.Formatter.Flags.NONE
                goto L_0x02aa
            L_0x02a8:
                java.util.Formatter$Flags r2 = java.util.Formatter.Flags.ZERO_PAD
            L_0x02aa:
                r4 = r2
                r2 = 0
                long r8 = (long) r1
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r8
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)
                r7.append((java.lang.CharSequence) r0)
                goto L_0x0323
            L_0x02bc:
                java.lang.String r0 = "AM"
                java.lang.String r2 = "PM"
                java.lang.String[] r0 = new java.lang.String[]{r0, r2}
                if (r6 == 0) goto L_0x02d2
                java.util.Locale r2 = java.util.Locale.f698US
                if (r6 == r2) goto L_0x02d2
                java.text.DateFormatSymbols r0 = java.text.DateFormatSymbols.getInstance(r20)
                java.lang.String[] r0 = r0.getAmPmStrings()
            L_0x02d2:
                r2 = 9
                int r1 = r1.get(r2)
                r0 = r0[r1]
                if (r6 == 0) goto L_0x02de
                r1 = r6
                goto L_0x02e0
            L_0x02de:
                java.util.Locale r1 = java.util.Locale.f698US
            L_0x02e0:
                java.lang.String r0 = r0.toLowerCase(r1)
                r7.append((java.lang.String) r0)
                goto L_0x0323
            L_0x02e8:
                int r0 = r1.get(r10)
                if (r6 != 0) goto L_0x02f1
                java.util.Locale r1 = java.util.Locale.f698US
                goto L_0x02f2
            L_0x02f1:
                r1 = r6
            L_0x02f2:
                java.text.DateFormatSymbols r1 = java.text.DateFormatSymbols.getInstance(r1)
                r3 = 66
                if (r2 != r3) goto L_0x0304
                java.lang.String[] r1 = r1.getMonths()
                r0 = r1[r0]
                r7.append((java.lang.String) r0)
                goto L_0x0323
            L_0x0304:
                java.lang.String[] r1 = r1.getShortMonths()
                r0 = r1[r0]
                r7.append((java.lang.String) r0)
                goto L_0x0323
            L_0x030e:
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r8, (java.util.Locale) r6)
                r3 = 45
                r2.append((char) r3)
                r2 = 109(0x6d, float:1.53E-43)
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r2, (java.util.Locale) r6)
                r2.append((char) r3)
                r0.print((java.lang.StringBuilder) r7, (java.util.Calendar) r1, (char) r9, (java.util.Locale) r6)
            L_0x0323:
                return r7
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Formatter.FormatSpecifier.print(java.lang.StringBuilder, java.util.Calendar, char, java.util.Locale):java.lang.Appendable");
        }

        private void print(TemporalAccessor temporalAccessor, char c, Locale locale) throws IOException {
            StringBuilder sb = new StringBuilder();
            print(sb, temporalAccessor, c, locale);
            String justify = justify(sb.toString());
            if (this.f680f.contains(Flags.UPPERCASE)) {
                justify = justify.toUpperCase();
            }
            Formatter.this.f675a.append((CharSequence) justify);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:51:0x01cf, code lost:
            r0 = (r7.get(java.time.temporal.ChronoField.DAY_OF_WEEK) % 7) + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:52:0x01d9, code lost:
            if (r6 != null) goto L_0x01de;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x01db, code lost:
            r1 = java.util.Locale.f698US;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x01de, code lost:
            r1 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x01df, code lost:
            r1 = java.text.DateFormatSymbols.getInstance(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x01e5, code lost:
            if (r8 != 'A') goto L_0x01f2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x01e7, code lost:
            r9.append(r1.getWeekdays()[r0]);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:58:0x01f2, code lost:
            r9.append(r1.getShortWeekdays()[r0]);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.lang.Appendable print(java.lang.StringBuilder r17, java.time.temporal.TemporalAccessor r18, char r19, java.util.Locale r20) throws java.p026io.IOException {
            /*
                r16 = this;
                r0 = r16
                r7 = r18
                r8 = r19
                r6 = r20
                if (r17 != 0) goto L_0x0011
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                r9 = r1
                goto L_0x0013
            L_0x0011:
                r9 = r17
            L_0x0013:
                r1 = 70
                r3 = 109(0x6d, float:1.53E-43)
                r4 = 89
                r5 = 100
                if (r8 == r1) goto L_0x0384
                r1 = 104(0x68, float:1.46E-43)
                if (r8 == r1) goto L_0x035a
                r1 = 112(0x70, float:1.57E-43)
                if (r8 == r1) goto L_0x032e
                r11 = 72
                if (r8 == r11) goto L_0x0313
                r12 = 73
                if (r8 == r12) goto L_0x02f7
                r13 = 121(0x79, float:1.7E-43)
                if (r8 == r4) goto L_0x02ca
                r15 = 90
                if (r8 == r15) goto L_0x027b
                r2 = 114(0x72, float:1.6E-43)
                r14 = 83
                r1 = 77
                r12 = 32
                r10 = 58
                if (r8 == r2) goto L_0x0247
                r2 = 115(0x73, float:1.61E-43)
                if (r8 == r2) goto L_0x022f
                if (r8 == r13) goto L_0x02ca
                r2 = 122(0x7a, float:1.71E-43)
                if (r8 == r2) goto L_0x01fd
                switch(r8) {
                    case 65: goto L_0x01cf;
                    case 66: goto L_0x035a;
                    case 67: goto L_0x02ca;
                    case 68: goto L_0x01ba;
                    default: goto L_0x004e;
                }
            L_0x004e:
                switch(r8) {
                    case 76: goto L_0x01a0;
                    case 77: goto L_0x0186;
                    case 78: goto L_0x0167;
                    default: goto L_0x0051;
                }
            L_0x0051:
                r2 = 84
                switch(r8) {
                    case 81: goto L_0x0144;
                    case 82: goto L_0x0130;
                    case 83: goto L_0x0116;
                    case 84: goto L_0x0130;
                    default: goto L_0x0056;
                }
            L_0x0056:
                switch(r8) {
                    case 97: goto L_0x01cf;
                    case 98: goto L_0x035a;
                    case 99: goto L_0x00ea;
                    case 100: goto L_0x00ca;
                    case 101: goto L_0x00ca;
                    default: goto L_0x0059;
                }
            L_0x0059:
                switch(r8) {
                    case 106: goto L_0x00b0;
                    case 107: goto L_0x0094;
                    case 108: goto L_0x0078;
                    case 109: goto L_0x005e;
                    default: goto L_0x005c;
                }
            L_0x005c:
                goto L_0x0397
            L_0x005e:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.MONTH_OF_YEAR     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0078:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r3 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r5 = java.util.Formatter.Flags.NONE     // Catch:{ DateTimeException -> 0x0398 }
                r10 = 2
                r0 = r16
                r1 = r2
                r2 = r3
                r4 = r5
                r5 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0094:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.HOUR_OF_DAY     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r3 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r5 = java.util.Formatter.Flags.NONE     // Catch:{ DateTimeException -> 0x0398 }
                r10 = 2
                r0 = r16
                r1 = r2
                r2 = r3
                r4 = r5
                r5 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x00b0:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_YEAR     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 3
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x00ca:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.DAY_OF_MONTH     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                if (r8 != r5) goto L_0x00d5
                java.util.Formatter$Flags r2 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x00d7
            L_0x00d5:
                java.util.Formatter$Flags r2 = java.util.Formatter.Flags.NONE     // Catch:{ DateTimeException -> 0x0398 }
            L_0x00d7:
                r4 = r2
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x00ea:
                r1 = 97
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r1, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r12)     // Catch:{ DateTimeException -> 0x0398 }
                r1 = 98
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r1, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r12)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r12)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r2, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r12)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r15, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r12)     // Catch:{ DateTimeException -> 0x0398 }
                r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r4, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0116:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.SECOND_OF_MINUTE     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0130:
                java.lang.Appendable r3 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r11, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r3.append((char) r10)     // Catch:{ DateTimeException -> 0x0398 }
                r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r1, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                if (r8 != r2) goto L_0x0397
                r9.append((char) r10)     // Catch:{ DateTimeException -> 0x0398 }
                r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r14, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0144:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.INSTANT_SECONDS     // Catch:{ DateTimeException -> 0x0398 }
                long r1 = r7.getLong(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r3 = 1000(0x3e8, double:4.94E-321)
                long r1 = r1 * r3
                java.time.temporal.ChronoField r3 = java.time.temporal.ChronoField.MILLI_OF_SECOND     // Catch:{ DateTimeException -> 0x0398 }
                long r3 = r7.getLong(r3)     // Catch:{ DateTimeException -> 0x0398 }
                long r2 = r1 + r3
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.NONE     // Catch:{ DateTimeException -> 0x0398 }
                r1 = 0
                int r5 = r0.width     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r16
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0167:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.MILLI_OF_SECOND     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 1000000(0xf4240, float:1.401298E-39)
                int r1 = r1 * r2
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 9
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0186:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.MINUTE_OF_HOUR     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 2
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x01a0:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.MILLI_OF_SECOND     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 3
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x01ba:
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r3, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 47
                r1.append((char) r2)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r2)     // Catch:{ DateTimeException -> 0x0398 }
                r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r13, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x01cf:
                java.time.temporal.ChronoField r0 = java.time.temporal.ChronoField.DAY_OF_WEEK     // Catch:{ DateTimeException -> 0x0398 }
                int r0 = r7.get(r0)     // Catch:{ DateTimeException -> 0x0398 }
                int r0 = r0 % 7
                r1 = 1
                int r0 = r0 + r1
                if (r6 != 0) goto L_0x01de
                java.util.Locale r1 = java.util.Locale.f698US     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x01df
            L_0x01de:
                r1 = r6
            L_0x01df:
                java.text.DateFormatSymbols r1 = java.text.DateFormatSymbols.getInstance(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 65
                if (r8 != r2) goto L_0x01f2
                java.lang.String[] r1 = r1.getWeekdays()     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r1[r0]     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x01f2:
                java.lang.String[] r1 = r1.getShortWeekdays()     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r1[r0]     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x01fd:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.OFFSET_SECONDS     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                if (r1 >= 0) goto L_0x0207
                r10 = 1
                goto L_0x0208
            L_0x0207:
                r10 = 0
            L_0x0208:
                if (r10 == 0) goto L_0x020d
                r2 = 45
                goto L_0x020f
            L_0x020d:
                r2 = 43
            L_0x020f:
                r9.append((char) r2)     // Catch:{ DateTimeException -> 0x0398 }
                if (r10 == 0) goto L_0x0215
                int r1 = -r1
            L_0x0215:
                int r1 = r1 / 60
                int r2 = r1 / 60
                int r2 = r2 * r5
                int r1 = r1 % 60
                int r2 = r2 + r1
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r1 = 0
                long r2 = (long) r2     // Catch:{ DateTimeException -> 0x0398 }
                r5 = 4
                r0 = r16
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x022f:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.INSTANT_SECONDS     // Catch:{ DateTimeException -> 0x0398 }
                long r2 = r7.getLong(r1)     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.NONE     // Catch:{ DateTimeException -> 0x0398 }
                r1 = 0
                int r5 = r0.width     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r16
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0247:
                r2 = 73
                java.lang.Appendable r2 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r2, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r2.append((char) r10)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r1, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r10)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r14, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r12)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ DateTimeException -> 0x0398 }
                r1.<init>()     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 112(0x70, float:1.57E-43)
                r0.print((java.lang.StringBuilder) r1, (java.time.temporal.TemporalAccessor) r7, (char) r2, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.String r0 = r1.toString()     // Catch:{ DateTimeException -> 0x0398 }
                if (r6 == 0) goto L_0x0270
                r1 = r6
                goto L_0x0272
            L_0x0270:
                java.util.Locale r1 = java.util.Locale.f698US     // Catch:{ DateTimeException -> 0x0398 }
            L_0x0272:
                java.lang.String r0 = r0.toUpperCase(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x027b:
                java.time.temporal.TemporalQuery r0 = java.time.temporal.TemporalQueries.zone()     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Object r0 = r7.query(r0)     // Catch:{ DateTimeException -> 0x0398 }
                java.time.ZoneId r0 = (java.time.ZoneId) r0     // Catch:{ DateTimeException -> 0x0398 }
                if (r0 == 0) goto L_0x02c0
                boolean r1 = r0 instanceof java.time.ZoneOffset     // Catch:{ DateTimeException -> 0x0398 }
                if (r1 != 0) goto L_0x02b7
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.INSTANT_SECONDS     // Catch:{ DateTimeException -> 0x0398 }
                boolean r1 = r7.isSupported(r1)     // Catch:{ DateTimeException -> 0x0398 }
                if (r1 == 0) goto L_0x02b7
                java.time.Instant r1 = java.time.Instant.from(r18)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.String r2 = r0.getId()     // Catch:{ DateTimeException -> 0x0398 }
                java.util.TimeZone r2 = java.util.TimeZone.getTimeZone((java.lang.String) r2)     // Catch:{ DateTimeException -> 0x0398 }
                java.time.zone.ZoneRules r0 = r0.getRules()     // Catch:{ DateTimeException -> 0x0398 }
                boolean r0 = r0.isDaylightSavings(r1)     // Catch:{ DateTimeException -> 0x0398 }
                if (r6 != 0) goto L_0x02ac
                java.util.Locale r1 = java.util.Locale.f698US     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x02ad
            L_0x02ac:
                r1 = r6
            L_0x02ad:
                r3 = 0
                java.lang.String r0 = r2.getDisplayName(r0, r3, r1)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x02b7:
                java.lang.String r0 = r0.getId()     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x02c0:
                java.util.IllegalFormatConversionException r0 = new java.util.IllegalFormatConversionException     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Class r1 = r18.getClass()     // Catch:{ DateTimeException -> 0x0398 }
                r0.<init>(r8, r1)     // Catch:{ DateTimeException -> 0x0398 }
                throw r0     // Catch:{ DateTimeException -> 0x0398 }
            L_0x02ca:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.YEAR_OF_ERA     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 67
                r3 = 2
                if (r8 == r2) goto L_0x02e1
                if (r8 == r4) goto L_0x02de
                if (r8 == r13) goto L_0x02db
            L_0x02d9:
                r5 = r3
                goto L_0x02e4
            L_0x02db:
                int r1 = r1 % 100
                goto L_0x02d9
            L_0x02de:
                r2 = 4
                r5 = r2
                goto L_0x02e4
            L_0x02e1:
                int r1 = r1 / 100
                goto L_0x02d9
            L_0x02e4:
                java.util.Formatter$Flags r4 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r10 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r16
                r1 = r2
                r2 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x02f7:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.CLOCK_HOUR_OF_AMPM     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r3 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r5 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r10 = 2
                r0 = r16
                r1 = r2
                r2 = r3
                r4 = r5
                r5 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0313:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.HOUR_OF_DAY     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 0
                long r3 = (long) r1     // Catch:{ DateTimeException -> 0x0398 }
                java.util.Formatter$Flags r5 = java.util.Formatter.Flags.ZERO_PAD     // Catch:{ DateTimeException -> 0x0398 }
                r10 = 2
                r0 = r16
                r1 = r2
                r2 = r3
                r4 = r5
                r5 = r10
                r6 = r20
                java.lang.StringBuilder r0 = r0.localizedMagnitude((java.lang.StringBuilder) r1, (long) r2, (java.util.Formatter.Flags) r4, (int) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.CharSequence) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x032e:
                java.lang.String r0 = "AM"
                java.lang.String r1 = "PM"
                java.lang.String[] r0 = new java.lang.String[]{r0, r1}     // Catch:{ DateTimeException -> 0x0398 }
                if (r6 == 0) goto L_0x0344
                java.util.Locale r1 = java.util.Locale.f698US     // Catch:{ DateTimeException -> 0x0398 }
                if (r6 == r1) goto L_0x0344
                java.text.DateFormatSymbols r0 = java.text.DateFormatSymbols.getInstance(r20)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.String[] r0 = r0.getAmPmStrings()     // Catch:{ DateTimeException -> 0x0398 }
            L_0x0344:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.AMPM_OF_DAY     // Catch:{ DateTimeException -> 0x0398 }
                int r1 = r7.get(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r0[r1]     // Catch:{ DateTimeException -> 0x0398 }
                if (r6 == 0) goto L_0x0350
                r1 = r6
                goto L_0x0352
            L_0x0350:
                java.util.Locale r1 = java.util.Locale.f698US     // Catch:{ DateTimeException -> 0x0398 }
            L_0x0352:
                java.lang.String r0 = r0.toLowerCase(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x035a:
                java.time.temporal.ChronoField r0 = java.time.temporal.ChronoField.MONTH_OF_YEAR     // Catch:{ DateTimeException -> 0x0398 }
                int r0 = r7.get(r0)     // Catch:{ DateTimeException -> 0x0398 }
                r1 = 1
                int r0 = r0 - r1
                if (r6 != 0) goto L_0x0367
                java.util.Locale r1 = java.util.Locale.f698US     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0368
            L_0x0367:
                r1 = r6
            L_0x0368:
                java.text.DateFormatSymbols r1 = java.text.DateFormatSymbols.getInstance(r1)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 66
                if (r8 != r2) goto L_0x037a
                java.lang.String[] r1 = r1.getMonths()     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r1[r0]     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x037a:
                java.lang.String[] r1 = r1.getShortMonths()     // Catch:{ DateTimeException -> 0x0398 }
                r0 = r1[r0]     // Catch:{ DateTimeException -> 0x0398 }
                r9.append((java.lang.String) r0)     // Catch:{ DateTimeException -> 0x0398 }
                goto L_0x0397
            L_0x0384:
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r4, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r2 = 45
                r1.append((char) r2)     // Catch:{ DateTimeException -> 0x0398 }
                java.lang.Appendable r1 = r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r3, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
                r1.append((char) r2)     // Catch:{ DateTimeException -> 0x0398 }
                r0.print((java.lang.StringBuilder) r9, (java.time.temporal.TemporalAccessor) r7, (char) r5, (java.util.Locale) r6)     // Catch:{ DateTimeException -> 0x0398 }
            L_0x0397:
                return r9
            L_0x0398:
                java.util.IllegalFormatConversionException r0 = new java.util.IllegalFormatConversionException
                java.lang.Class r1 = r18.getClass()
                r0.<init>(r8, r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Formatter.FormatSpecifier.print(java.lang.StringBuilder, java.time.temporal.TemporalAccessor, char, java.util.Locale):java.lang.Appendable");
        }

        private void failMismatch(Flags flags, char c) {
            throw new FormatFlagsConversionMismatchException(flags.toString(), c);
        }

        private void failConversion(char c, Object obj) {
            throw new IllegalFormatConversionException(c, obj.getClass());
        }

        private char getZero(Locale locale) {
            if (locale == null || locale.equals(Formatter.this.locale())) {
                return Formatter.this.zero;
            }
            return DecimalFormatData.getInstance(LocaleData.mapInvalidAndNullLocales(locale)).getZeroDigit();
        }

        private StringBuilder localizedMagnitude(StringBuilder sb, long j, Flags flags, int i, Locale locale) {
            return localizedMagnitude(sb, Long.toString(j, 10).toCharArray(), flags, i, locale);
        }

        private StringBuilder localizedMagnitude(StringBuilder sb, char[] cArr, Flags flags, int i, Locale locale) {
            char c;
            int i2;
            char c2;
            if (sb == null) {
                sb = new StringBuilder();
            }
            int length = sb.length();
            char zero = getZero(locale);
            int length2 = cArr.length;
            int i3 = 0;
            while (true) {
                c = '.';
                if (i3 >= length2) {
                    i3 = length2;
                    break;
                } else if (cArr[i3] == '.') {
                    break;
                } else {
                    i3++;
                }
            }
            if (i3 >= length2) {
                c = 0;
            } else if (locale != null && !locale.equals(Locale.f698US)) {
                c = DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();
            }
            if (!flags.contains(Flags.GROUP)) {
                c2 = 0;
                i2 = -1;
            } else if (locale == null || locale.equals(Locale.f698US)) {
                c2 = ',';
                i2 = 3;
            } else {
                c2 = DecimalFormatSymbols.getInstance(locale).getGroupingSeparator();
                DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getIntegerInstance(locale);
                i2 = decimalFormat.getGroupingSize();
                if (!decimalFormat.isGroupingUsed() || decimalFormat.getGroupingSize() == 0) {
                    c2 = 0;
                }
            }
            for (int i4 = 0; i4 < length2; i4++) {
                if (i4 == i3) {
                    sb.append(c);
                    c2 = 0;
                } else {
                    sb.append((char) ((cArr[i4] - '0') + zero));
                    if (!(c2 == 0 || i4 == i3 - 1 || (i3 - i4) % i2 != 1)) {
                        sb.append(c2);
                    }
                }
            }
            int length3 = sb.length();
            if (i != -1 && flags.contains(Flags.ZERO_PAD)) {
                for (int i5 = 0; i5 < i - length3; i5++) {
                    sb.insert(length, zero);
                }
            }
            return sb;
        }
    }

    private static class Flags {
        static final Flags ALTERNATE = new Flags(4);
        static final Flags GROUP = new Flags(64);
        static final Flags LEADING_SPACE = new Flags(16);
        static final Flags LEFT_JUSTIFY = new Flags(1);
        static final Flags NONE = new Flags(0);
        static final Flags PARENTHESES = new Flags(128);
        static final Flags PLUS = new Flags(8);
        static final Flags PREVIOUS = new Flags(256);
        static final Flags UPPERCASE = new Flags(2);
        static final Flags ZERO_PAD = new Flags(32);
        private int flags;

        private Flags(int i) {
            this.flags = i;
        }

        public int valueOf() {
            return this.flags;
        }

        public boolean contains(Flags flags2) {
            return (this.flags & flags2.valueOf()) == flags2.valueOf();
        }

        public Flags dup() {
            return new Flags(this.flags);
        }

        /* access modifiers changed from: private */
        public Flags add(Flags flags2) {
            this.flags = flags2.valueOf() | this.flags;
            return this;
        }

        public Flags remove(Flags flags2) {
            this.flags = (~flags2.valueOf()) & this.flags;
            return this;
        }

        public static Flags parse(String str) {
            char[] charArray = str.toCharArray();
            int i = 0;
            Flags flags2 = new Flags(0);
            while (i < charArray.length) {
                Flags parse = parse(charArray[i]);
                if (!flags2.contains(parse)) {
                    flags2.add(parse);
                    i++;
                } else {
                    throw new DuplicateFormatFlagsException(parse.toString());
                }
            }
            return flags2;
        }

        private static Flags parse(char c) {
            if (c == ' ') {
                return LEADING_SPACE;
            }
            if (c == '#') {
                return ALTERNATE;
            }
            if (c == '(') {
                return PARENTHESES;
            }
            if (c == '0') {
                return ZERO_PAD;
            }
            if (c == '<') {
                return PREVIOUS;
            }
            switch (c) {
                case '+':
                    return PLUS;
                case ',':
                    return GROUP;
                case '-':
                    return LEFT_JUSTIFY;
                default:
                    throw new UnknownFormatFlagsException(String.valueOf(c));
            }
        }

        public static String toString(Flags flags2) {
            return flags2.toString();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (contains(LEFT_JUSTIFY)) {
                sb.append('-');
            }
            if (contains(UPPERCASE)) {
                sb.append('^');
            }
            if (contains(ALTERNATE)) {
                sb.append('#');
            }
            if (contains(PLUS)) {
                sb.append('+');
            }
            if (contains(LEADING_SPACE)) {
                sb.append(' ');
            }
            if (contains(ZERO_PAD)) {
                sb.append('0');
            }
            if (contains(GROUP)) {
                sb.append(',');
            }
            if (contains(PARENTHESES)) {
                sb.append('(');
            }
            if (contains(PREVIOUS)) {
                sb.append((char) Typography.less);
            }
            return sb.toString();
        }
    }

    private static class Conversion {
        static final char BOOLEAN = 'b';
        static final char BOOLEAN_UPPER = 'B';
        static final char CHARACTER = 'c';
        static final char CHARACTER_UPPER = 'C';
        static final char DATE_TIME = 't';
        static final char DATE_TIME_UPPER = 'T';
        static final char DECIMAL_FLOAT = 'f';
        static final char DECIMAL_INTEGER = 'd';
        static final char GENERAL = 'g';
        static final char GENERAL_UPPER = 'G';
        static final char HASHCODE = 'h';
        static final char HASHCODE_UPPER = 'H';
        static final char HEXADECIMAL_FLOAT = 'a';
        static final char HEXADECIMAL_FLOAT_UPPER = 'A';
        static final char HEXADECIMAL_INTEGER = 'x';
        static final char HEXADECIMAL_INTEGER_UPPER = 'X';
        static final char LINE_SEPARATOR = 'n';
        static final char OCTAL_INTEGER = 'o';
        static final char PERCENT_SIGN = '%';
        static final char SCIENTIFIC = 'e';
        static final char SCIENTIFIC_UPPER = 'E';
        static final char STRING = 's';
        static final char STRING_UPPER = 'S';

        static boolean isCharacter(char c) {
            return c == 'C' || c == 'c';
        }

        static boolean isFloat(char c) {
            if (c == 'A' || c == 'E' || c == 'G' || c == 'a') {
                return true;
            }
            switch (c) {
                case 'e':
                case 'f':
                case 'g':
                    return true;
                default:
                    return false;
            }
        }

        static boolean isGeneral(char c) {
            return c == 'B' || c == 'H' || c == 'S' || c == 'b' || c == 'h' || c == 's';
        }

        static boolean isInteger(char c) {
            return c == 'X' || c == 'd' || c == 'o' || c == 'x';
        }

        static boolean isText(char c) {
            return c == '%' || c == 'n';
        }

        private Conversion() {
        }

        static boolean isValid(char c) {
            return isGeneral(c) || isInteger(c) || isFloat(c) || isText(c) || c == 't' || isCharacter(c);
        }
    }

    private static class DateTime {
        static final char AM_PM = 'p';
        static final char CENTURY = 'C';
        static final char DATE = 'D';
        static final char DATE_TIME = 'c';
        static final char DAY_OF_MONTH = 'e';
        static final char DAY_OF_MONTH_0 = 'd';
        static final char DAY_OF_YEAR = 'j';
        static final char HOUR = 'l';
        static final char HOUR_0 = 'I';
        static final char HOUR_OF_DAY = 'k';
        static final char HOUR_OF_DAY_0 = 'H';
        static final char ISO_STANDARD_DATE = 'F';
        static final char MILLISECOND = 'L';
        static final char MILLISECOND_SINCE_EPOCH = 'Q';
        static final char MINUTE = 'M';
        static final char MONTH = 'm';
        static final char NAME_OF_DAY = 'A';
        static final char NAME_OF_DAY_ABBREV = 'a';
        static final char NAME_OF_MONTH = 'B';
        static final char NAME_OF_MONTH_ABBREV = 'b';
        static final char NAME_OF_MONTH_ABBREV_X = 'h';
        static final char NANOSECOND = 'N';
        static final char SECOND = 'S';
        static final char SECONDS_SINCE_EPOCH = 's';
        static final char TIME = 'T';
        static final char TIME_12_HOUR = 'r';
        static final char TIME_24_HOUR = 'R';
        static final char YEAR_2 = 'y';
        static final char YEAR_4 = 'Y';
        static final char ZONE = 'Z';
        static final char ZONE_NUMERIC = 'z';

        static boolean isValid(char c) {
            if (c == 'F' || c == 'h' || c == 'p' || c == 'H' || c == 'I' || c == 'Y' || c == 'Z' || c == 'r' || c == 's' || c == 'y' || c == 'z') {
                return true;
            }
            switch (c) {
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                    return true;
                default:
                    switch (c) {
                        case 'L':
                        case 'M':
                        case 'N':
                            return true;
                        default:
                            switch (c) {
                                case 'Q':
                                case 'R':
                                case 'S':
                                case 'T':
                                    return true;
                                default:
                                    switch (c) {
                                        case 'a':
                                        case 'b':
                                        case 'c':
                                        case 'd':
                                        case 'e':
                                            return true;
                                        default:
                                            switch (c) {
                                                case 'j':
                                                case 'k':
                                                case 'l':
                                                case 'm':
                                                    return true;
                                                default:
                                                    return false;
                                            }
                                    }
                            }
                    }
            }
        }

        private DateTime() {
        }
    }
}
