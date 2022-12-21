package java.util;

import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.p026io.Closeable;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import java.p026io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.LRUCache;
import sun.util.locale.LanguageTag;

public final class Scanner implements Iterator<String>, Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String BOOLEAN_PATTERN = "true|false";
    private static final int BUFFER_SIZE = 1024;
    private static Pattern FIND_ANY_PATTERN = Pattern.compile("(?s).*");
    private static final String LINE_PATTERN = ".*(\r\n|[\n\r  ])|.+$";
    private static final String LINE_SEPARATOR_PATTERN = "\r\n|[\n\r  ]";
    private static Pattern NON_ASCII_DIGIT = Pattern.compile("[\\p{javaDigit}&&[^0-9]]");
    private static Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");
    private static volatile Pattern boolPattern;
    private static volatile Pattern linePattern;
    private static volatile Pattern separatorPattern;
    private int SIMPLE_GROUP_INDEX;
    private CharBuffer buf;
    private boolean closed;
    private Pattern decimalPattern;
    private String decimalSeparator;
    private int defaultRadix;
    private Pattern delimPattern;
    private String digits;
    private Pattern floatPattern;
    private String groupSeparator;
    private Pattern hasNextPattern;
    private int hasNextPosition;
    private String hasNextResult;
    private String infinityString;
    private Pattern integerPattern;
    private IOException lastException;
    private Locale locale;
    private boolean matchValid;
    private Matcher matcher;
    private String nanString;
    private boolean needInput;
    private String negativePrefix;
    private String negativeSuffix;
    private String non0Digit;
    private LRUCache<String, Pattern> patternCache;
    private int position;
    private String positivePrefix;
    private String positiveSuffix;
    private int radix;
    private int savedScannerPosition;
    private boolean skipped;
    private Readable source;
    private boolean sourceClosed;
    private Object typeCache;

    private static Pattern boolPattern() {
        Pattern pattern = boolPattern;
        if (pattern != null) {
            return pattern;
        }
        Pattern compile = Pattern.compile(BOOLEAN_PATTERN, 2);
        boolPattern = compile;
        return compile;
    }

    private String buildIntegerPatternString() {
        String str = "((?i)[" + this.digits.substring(0, this.radix) + "]|\\p{javaDigit})";
        String str2 = "((" + str + "++)|" + (NavigationBarInflaterView.KEY_CODE_START + ("((?i)[" + this.digits.substring(1, this.radix) + "]|(" + this.non0Digit + "))") + str + "?" + str + "?(" + this.groupSeparator + str + str + str + ")+)") + NavigationBarInflaterView.KEY_CODE_END;
        String str3 = this.negativePrefix + str2 + this.negativeSuffix;
        return NavigationBarInflaterView.KEY_CODE_START + ("([-+]?(" + str2 + "))") + ")|(" + (this.positivePrefix + str2 + this.positiveSuffix) + ")|(" + str3 + NavigationBarInflaterView.KEY_CODE_END;
    }

    private Pattern integerPattern() {
        if (this.integerPattern == null) {
            this.integerPattern = this.patternCache.forName(buildIntegerPatternString());
        }
        return this.integerPattern;
    }

    private static Pattern separatorPattern() {
        Pattern pattern = separatorPattern;
        if (pattern != null) {
            return pattern;
        }
        Pattern compile = Pattern.compile(LINE_SEPARATOR_PATTERN);
        separatorPattern = compile;
        return compile;
    }

    private static Pattern linePattern() {
        Pattern pattern = linePattern;
        if (pattern != null) {
            return pattern;
        }
        Pattern compile = Pattern.compile(LINE_PATTERN);
        linePattern = compile;
        return compile;
    }

    private void buildFloatAndDecimalPattern() {
        String str = "((([0-9]|(\\p{javaDigit}))++)|" + (NavigationBarInflaterView.KEY_CODE_START + this.non0Digit + "([0-9]|(\\p{javaDigit}))?([0-9]|(\\p{javaDigit}))?(" + this.groupSeparator + "([0-9]|(\\p{javaDigit}))([0-9]|(\\p{javaDigit}))([0-9]|(\\p{javaDigit})))+)") + NavigationBarInflaterView.KEY_CODE_END;
        String str2 = NavigationBarInflaterView.KEY_CODE_START + str + "|" + str + this.decimalSeparator + "([0-9]|(\\p{javaDigit}))*+|" + this.decimalSeparator + "([0-9]|(\\p{javaDigit}))++)";
        String str3 = "(NaN|" + this.nanString + "|Infinity|" + this.infinityString + NavigationBarInflaterView.KEY_CODE_END;
        String str4 = "(([-+]?" + str2 + "([eE][+-]?([0-9]|(\\p{javaDigit}))+)?)|" + (NavigationBarInflaterView.KEY_CODE_START + this.positivePrefix + str2 + this.positiveSuffix + "([eE][+-]?([0-9]|(\\p{javaDigit}))+)?)") + "|" + (NavigationBarInflaterView.KEY_CODE_START + this.negativePrefix + str2 + this.negativeSuffix + "([eE][+-]?([0-9]|(\\p{javaDigit}))+)?)") + NavigationBarInflaterView.KEY_CODE_END;
        this.floatPattern = Pattern.compile(str4 + "|[-+]?0[xX][0-9a-fA-F]*\\.[0-9a-fA-F]+([pP][-+]?[0-9]+)?|" + ("(([-+]?" + str3 + ")|" + (NavigationBarInflaterView.KEY_CODE_START + this.positivePrefix + str3 + this.positiveSuffix + NavigationBarInflaterView.KEY_CODE_END) + "|" + (NavigationBarInflaterView.KEY_CODE_START + this.negativePrefix + str3 + this.negativeSuffix + NavigationBarInflaterView.KEY_CODE_END) + NavigationBarInflaterView.KEY_CODE_END));
        this.decimalPattern = Pattern.compile(str4);
    }

    private Pattern floatPattern() {
        if (this.floatPattern == null) {
            buildFloatAndDecimalPattern();
        }
        return this.floatPattern;
    }

    private Pattern decimalPattern() {
        if (this.decimalPattern == null) {
            buildFloatAndDecimalPattern();
        }
        return this.decimalPattern;
    }

    private Scanner(Readable readable, Pattern pattern) {
        this.sourceClosed = false;
        this.needInput = false;
        this.skipped = false;
        this.savedScannerPosition = -1;
        this.typeCache = null;
        this.matchValid = false;
        this.closed = false;
        this.radix = 10;
        this.defaultRadix = 10;
        this.locale = null;
        this.patternCache = new LRUCache<String, Pattern>(7) {
            /* access modifiers changed from: protected */
            public Pattern create(String str) {
                return Pattern.compile(str);
            }

            /* access modifiers changed from: protected */
            public boolean hasName(Pattern pattern, String str) {
                return pattern.pattern().equals(str);
            }
        };
        this.groupSeparator = "\\,";
        this.decimalSeparator = "\\.";
        this.nanString = "NaN";
        this.infinityString = "Infinity";
        this.positivePrefix = "";
        this.negativePrefix = "\\-";
        this.positiveSuffix = "";
        this.negativeSuffix = "";
        this.digits = "0123456789abcdefghijklmnopqrstuvwxyz";
        this.non0Digit = "[\\p{javaDigit}&&[^0]]";
        this.SIMPLE_GROUP_INDEX = 5;
        this.source = readable;
        this.delimPattern = pattern;
        CharBuffer allocate = CharBuffer.allocate(1024);
        this.buf = allocate;
        allocate.limit(0);
        Matcher matcher2 = this.delimPattern.matcher(this.buf);
        this.matcher = matcher2;
        matcher2.useTransparentBounds(true);
        this.matcher.useAnchoringBounds(false);
        useLocale(Locale.getDefault(Locale.Category.FORMAT));
    }

    public Scanner(Readable readable) {
        this((Readable) Objects.requireNonNull(readable, "source"), WHITESPACE_PATTERN);
    }

    public Scanner(InputStream inputStream) {
        this((Readable) new InputStreamReader(inputStream), WHITESPACE_PATTERN);
    }

    public Scanner(InputStream inputStream, String str) {
        this(makeReadable((InputStream) Objects.requireNonNull(inputStream, "source"), toCharset(str)), WHITESPACE_PATTERN);
    }

    private static Charset toCharset(String str) {
        Objects.requireNonNull(str, "charsetName");
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Readable makeReadable(InputStream inputStream, Charset charset) {
        return new InputStreamReader(inputStream, charset);
    }

    public Scanner(File file) throws FileNotFoundException {
        this((ReadableByteChannel) new FileInputStream(file).getChannel());
    }

    public Scanner(File file, String str) throws FileNotFoundException {
        this((File) Objects.requireNonNull(file), toDecoder(str));
    }

    private Scanner(File file, CharsetDecoder charsetDecoder) throws FileNotFoundException {
        this(makeReadable((ReadableByteChannel) new FileInputStream(file).getChannel(), charsetDecoder));
    }

    private static CharsetDecoder toDecoder(String str) {
        if (str != null) {
            try {
                return Charset.forName(str).newDecoder();
            } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
                throw new IllegalArgumentException(str);
            }
        } else {
            throw new IllegalArgumentException("charsetName == null");
        }
    }

    private static Readable makeReadable(ReadableByteChannel readableByteChannel, CharsetDecoder charsetDecoder) {
        return Channels.newReader(readableByteChannel, charsetDecoder, -1);
    }

    public Scanner(Path path) throws IOException {
        this(Files.newInputStream(path, new OpenOption[0]));
    }

    public Scanner(Path path, String str) throws IOException {
        this((Path) Objects.requireNonNull(path), toCharset(str));
    }

    private Scanner(Path path, Charset charset) throws IOException {
        this(makeReadable(Files.newInputStream(path, new OpenOption[0]), charset));
    }

    public Scanner(String str) {
        this((Readable) new StringReader(str), WHITESPACE_PATTERN);
    }

    public Scanner(ReadableByteChannel readableByteChannel) {
        this(makeReadable((ReadableByteChannel) Objects.requireNonNull(readableByteChannel, "source")), WHITESPACE_PATTERN);
    }

    private static Readable makeReadable(ReadableByteChannel readableByteChannel) {
        return makeReadable(readableByteChannel, Charset.defaultCharset().newDecoder());
    }

    public Scanner(ReadableByteChannel readableByteChannel, String str) {
        this(makeReadable((ReadableByteChannel) Objects.requireNonNull(readableByteChannel, "source"), toDecoder(str)), WHITESPACE_PATTERN);
    }

    private void saveState() {
        this.savedScannerPosition = this.position;
    }

    private void revertState() {
        this.position = this.savedScannerPosition;
        this.savedScannerPosition = -1;
        this.skipped = false;
    }

    private boolean revertState(boolean z) {
        this.position = this.savedScannerPosition;
        this.savedScannerPosition = -1;
        this.skipped = false;
        return z;
    }

    private void cacheResult() {
        this.hasNextResult = this.matcher.group();
        this.hasNextPosition = this.matcher.end();
        this.hasNextPattern = this.matcher.pattern();
    }

    private void cacheResult(String str) {
        this.hasNextResult = str;
        this.hasNextPosition = this.matcher.end();
        this.hasNextPattern = this.matcher.pattern();
    }

    private void clearCaches() {
        this.hasNextPattern = null;
        this.typeCache = null;
    }

    private String getCachedResult() {
        this.position = this.hasNextPosition;
        this.hasNextPattern = null;
        this.typeCache = null;
        return this.hasNextResult;
    }

    private void useTypeCache() {
        if (!this.closed) {
            this.position = this.hasNextPosition;
            this.hasNextPattern = null;
            this.typeCache = null;
            return;
        }
        throw new IllegalStateException("Scanner closed");
    }

    private void readInput() {
        int i;
        if (this.buf.limit() == this.buf.capacity()) {
            makeSpace();
        }
        int position2 = this.buf.position();
        CharBuffer charBuffer = this.buf;
        charBuffer.position(charBuffer.limit());
        CharBuffer charBuffer2 = this.buf;
        charBuffer2.limit(charBuffer2.capacity());
        try {
            i = this.source.read(this.buf);
        } catch (IOException e) {
            this.lastException = e;
            i = -1;
        }
        if (i == -1) {
            this.sourceClosed = true;
            this.needInput = false;
        }
        if (i > 0) {
            this.needInput = false;
        }
        CharBuffer charBuffer3 = this.buf;
        charBuffer3.limit(charBuffer3.position());
        this.buf.position(position2);
        this.matcher.reset(this.buf);
    }

    private boolean makeSpace() {
        clearCaches();
        int i = this.savedScannerPosition;
        if (i == -1) {
            i = this.position;
        }
        this.buf.position(i);
        if (i > 0) {
            this.buf.compact();
            translateSavedIndexes(i);
            this.position -= i;
            this.buf.flip();
            return true;
        }
        CharBuffer allocate = CharBuffer.allocate(this.buf.capacity() * 2);
        allocate.put(this.buf);
        allocate.flip();
        translateSavedIndexes(i);
        this.position -= i;
        this.buf = allocate;
        this.matcher.reset(allocate);
        return true;
    }

    private void translateSavedIndexes(int i) {
        int i2 = this.savedScannerPosition;
        if (i2 != -1) {
            this.savedScannerPosition = i2 - i;
        }
    }

    private void throwFor() {
        this.skipped = false;
        if (!this.sourceClosed || this.position != this.buf.limit()) {
            throw new InputMismatchException();
        }
        throw new NoSuchElementException();
    }

    private boolean hasTokenInBuffer() {
        this.matchValid = false;
        this.matcher.usePattern(this.delimPattern);
        this.matcher.region(this.position, this.buf.limit());
        if (this.matcher.lookingAt()) {
            this.position = this.matcher.end();
        }
        if (this.position == this.buf.limit()) {
            return false;
        }
        return true;
    }

    private String getCompleteTokenInBuffer(Pattern pattern) {
        this.matchValid = false;
        this.matcher.usePattern(this.delimPattern);
        if (!this.skipped) {
            this.matcher.region(this.position, this.buf.limit());
            if (this.matcher.lookingAt()) {
                if (!this.matcher.hitEnd() || this.sourceClosed) {
                    this.skipped = true;
                    this.position = this.matcher.end();
                } else {
                    this.needInput = true;
                    return null;
                }
            }
        }
        if (this.position != this.buf.limit()) {
            this.matcher.region(this.position, this.buf.limit());
            boolean find = this.matcher.find();
            if (find && this.matcher.end() == this.position) {
                find = this.matcher.find();
            }
            if (find) {
                if (!this.matcher.requireEnd() || this.sourceClosed) {
                    int start = this.matcher.start();
                    if (pattern == null) {
                        pattern = FIND_ANY_PATTERN;
                    }
                    this.matcher.usePattern(pattern);
                    this.matcher.region(this.position, start);
                    if (!this.matcher.matches()) {
                        return null;
                    }
                    String group = this.matcher.group();
                    this.position = this.matcher.end();
                    return group;
                }
                this.needInput = true;
                return null;
            } else if (this.sourceClosed) {
                if (pattern == null) {
                    pattern = FIND_ANY_PATTERN;
                }
                this.matcher.usePattern(pattern);
                this.matcher.region(this.position, this.buf.limit());
                if (!this.matcher.matches()) {
                    return null;
                }
                String group2 = this.matcher.group();
                this.position = this.matcher.end();
                return group2;
            } else {
                this.needInput = true;
                return null;
            }
        } else if (this.sourceClosed) {
            return null;
        } else {
            this.needInput = true;
            return null;
        }
    }

    private String findPatternInBuffer(Pattern pattern, int i) {
        int i2;
        this.matchValid = false;
        this.matcher.usePattern(pattern);
        int limit = this.buf.limit();
        if (i > 0) {
            i2 = this.position + i;
            if (i2 < limit) {
                limit = i2;
            }
        } else {
            i2 = -1;
        }
        this.matcher.region(this.position, limit);
        if (this.matcher.find()) {
            if (this.matcher.hitEnd() && !this.sourceClosed) {
                if (limit != i2) {
                    this.needInput = true;
                    return null;
                } else if (limit == i2 && this.matcher.requireEnd()) {
                    this.needInput = true;
                    return null;
                }
            }
            this.position = this.matcher.end();
            return this.matcher.group();
        } else if (this.sourceClosed) {
            return null;
        } else {
            if (i == 0 || limit != i2) {
                this.needInput = true;
            }
            return null;
        }
    }

    private String matchPatternInBuffer(Pattern pattern) {
        this.matchValid = false;
        this.matcher.usePattern(pattern);
        this.matcher.region(this.position, this.buf.limit());
        if (this.matcher.lookingAt()) {
            if (!this.matcher.hitEnd() || this.sourceClosed) {
                this.position = this.matcher.end();
                return this.matcher.group();
            }
            this.needInput = true;
            return null;
        } else if (this.sourceClosed) {
            return null;
        } else {
            this.needInput = true;
            return null;
        }
    }

    private void ensureOpen() {
        if (this.closed) {
            throw new IllegalStateException("Scanner closed");
        }
    }

    public void close() {
        if (!this.closed) {
            Readable readable = this.source;
            if (readable instanceof Closeable) {
                try {
                    ((Closeable) readable).close();
                } catch (IOException e) {
                    this.lastException = e;
                }
            }
            this.sourceClosed = true;
            this.source = null;
            this.closed = true;
        }
    }

    public IOException ioException() {
        return this.lastException;
    }

    public Pattern delimiter() {
        return this.delimPattern;
    }

    public Scanner useDelimiter(Pattern pattern) {
        this.delimPattern = pattern;
        return this;
    }

    public Scanner useDelimiter(String str) {
        this.delimPattern = this.patternCache.forName(str);
        return this;
    }

    public Locale locale() {
        return this.locale;
    }

    public Scanner useLocale(Locale locale2) {
        if (locale2.equals(this.locale)) {
            return this;
        }
        this.locale = locale2;
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale2);
        DecimalFormatSymbols instance = DecimalFormatSymbols.getInstance(locale2);
        this.groupSeparator = "\\" + instance.getGroupingSeparator();
        this.decimalSeparator = "\\" + instance.getDecimalSeparator();
        this.nanString = "\\Q" + instance.getNaN() + "\\E";
        this.infinityString = "\\Q" + instance.getInfinity() + "\\E";
        String positivePrefix2 = decimalFormat.getPositivePrefix();
        this.positivePrefix = positivePrefix2;
        if (positivePrefix2.length() > 0) {
            this.positivePrefix = "\\Q" + this.positivePrefix + "\\E";
        }
        String negativePrefix2 = decimalFormat.getNegativePrefix();
        this.negativePrefix = negativePrefix2;
        if (negativePrefix2.length() > 0) {
            this.negativePrefix = "\\Q" + this.negativePrefix + "\\E";
        }
        String positiveSuffix2 = decimalFormat.getPositiveSuffix();
        this.positiveSuffix = positiveSuffix2;
        if (positiveSuffix2.length() > 0) {
            this.positiveSuffix = "\\Q" + this.positiveSuffix + "\\E";
        }
        String negativeSuffix2 = decimalFormat.getNegativeSuffix();
        this.negativeSuffix = negativeSuffix2;
        if (negativeSuffix2.length() > 0) {
            this.negativeSuffix = "\\Q" + this.negativeSuffix + "\\E";
        }
        this.integerPattern = null;
        this.floatPattern = null;
        return this;
    }

    public int radix() {
        return this.defaultRadix;
    }

    public Scanner useRadix(int i) {
        if (i < 2 || i > 36) {
            throw new IllegalArgumentException("radix:" + i);
        } else if (this.defaultRadix == i) {
            return this;
        } else {
            this.defaultRadix = i;
            this.integerPattern = null;
            return this;
        }
    }

    private void setRadix(int i) {
        if (i > 36) {
            throw new IllegalArgumentException("radix == " + i);
        } else if (this.radix != i) {
            this.integerPattern = null;
            this.radix = i;
        }
    }

    public MatchResult match() {
        if (this.matchValid) {
            return this.matcher.toMatchResult();
        }
        throw new IllegalStateException("No match result available");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("java.util.Scanner");
        sb.append("[delimiters=" + this.delimPattern + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[position=" + this.position + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[match valid=" + this.matchValid + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[need input=" + this.needInput + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[source closed=" + this.sourceClosed + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[skipped=" + this.skipped + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[group separator=" + this.groupSeparator + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[decimal separator=" + this.decimalSeparator + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[positive prefix=" + this.positivePrefix + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[negative prefix=" + this.negativePrefix + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[positive suffix=" + this.positiveSuffix + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[negative suffix=" + this.negativeSuffix + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[NaN string=" + this.nanString + NavigationBarInflaterView.SIZE_MOD_END);
        sb.append("[infinity string=" + this.infinityString + NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public boolean hasNext() {
        ensureOpen();
        saveState();
        while (!this.sourceClosed) {
            if (hasTokenInBuffer()) {
                return revertState(true);
            }
            readInput();
        }
        return revertState(hasTokenInBuffer());
    }

    public String next() {
        ensureOpen();
        clearCaches();
        while (true) {
            String completeTokenInBuffer = getCompleteTokenInBuffer((Pattern) null);
            if (completeTokenInBuffer != null) {
                this.matchValid = true;
                this.skipped = false;
                return completeTokenInBuffer;
            } else if (this.needInput) {
                readInput();
            } else {
                throwFor();
            }
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext(String str) {
        return hasNext(this.patternCache.forName(str));
    }

    public String next(String str) {
        return next(this.patternCache.forName(str));
    }

    public boolean hasNext(Pattern pattern) {
        ensureOpen();
        pattern.getClass();
        this.hasNextPattern = null;
        saveState();
        while (getCompleteTokenInBuffer(pattern) == null) {
            if (!this.needInput) {
                return revertState(false);
            }
            readInput();
        }
        this.matchValid = true;
        cacheResult();
        return revertState(true);
    }

    public String next(Pattern pattern) {
        ensureOpen();
        pattern.getClass();
        if (this.hasNextPattern == pattern) {
            return getCachedResult();
        }
        clearCaches();
        while (true) {
            String completeTokenInBuffer = getCompleteTokenInBuffer(pattern);
            if (completeTokenInBuffer != null) {
                this.matchValid = true;
                this.skipped = false;
                return completeTokenInBuffer;
            } else if (this.needInput) {
                readInput();
            } else {
                throwFor();
            }
        }
    }

    public boolean hasNextLine() {
        saveState();
        String findWithinHorizon = findWithinHorizon(linePattern(), 0);
        if (findWithinHorizon != null) {
            String group = match().group(1);
            if (group != null) {
                findWithinHorizon = findWithinHorizon.substring(0, findWithinHorizon.length() - group.length());
                cacheResult(findWithinHorizon);
            } else {
                cacheResult();
            }
        }
        revertState();
        if (findWithinHorizon != null) {
            return true;
        }
        return false;
    }

    public String nextLine() {
        if (this.hasNextPattern == linePattern()) {
            return getCachedResult();
        }
        clearCaches();
        String findWithinHorizon = findWithinHorizon(linePattern, 0);
        if (findWithinHorizon != null) {
            String group = match().group(1);
            if (group != null) {
                findWithinHorizon = findWithinHorizon.substring(0, findWithinHorizon.length() - group.length());
            }
            if (findWithinHorizon != null) {
                return findWithinHorizon;
            }
            throw new NoSuchElementException();
        }
        throw new NoSuchElementException("No line found");
    }

    public String findInLine(String str) {
        return findInLine(this.patternCache.forName(str));
    }

    public String findInLine(Pattern pattern) {
        int start;
        ensureOpen();
        pattern.getClass();
        clearCaches();
        saveState();
        while (true) {
            if (findPatternInBuffer(separatorPattern(), 0) == null) {
                if (!this.needInput) {
                    start = this.buf.limit();
                    break;
                }
                readInput();
            } else {
                start = this.matcher.start();
                break;
            }
        }
        revertState();
        int i = start - this.position;
        if (i == 0) {
            return null;
        }
        return findWithinHorizon(pattern, i);
    }

    public String findWithinHorizon(String str, int i) {
        return findWithinHorizon(this.patternCache.forName(str), i);
    }

    public String findWithinHorizon(Pattern pattern, int i) {
        ensureOpen();
        pattern.getClass();
        if (i >= 0) {
            clearCaches();
            while (true) {
                String findPatternInBuffer = findPatternInBuffer(pattern, i);
                if (findPatternInBuffer != null) {
                    this.matchValid = true;
                    return findPatternInBuffer;
                } else if (!this.needInput) {
                    return null;
                } else {
                    readInput();
                }
            }
        } else {
            throw new IllegalArgumentException("horizon < 0");
        }
    }

    public Scanner skip(Pattern pattern) {
        ensureOpen();
        pattern.getClass();
        clearCaches();
        while (matchPatternInBuffer(pattern) == null) {
            if (this.needInput) {
                readInput();
            } else {
                throw new NoSuchElementException();
            }
        }
        this.matchValid = true;
        this.position = this.matcher.end();
        return this;
    }

    public Scanner skip(String str) {
        return skip(this.patternCache.forName(str));
    }

    public boolean hasNextBoolean() {
        return hasNext(boolPattern());
    }

    public boolean nextBoolean() {
        clearCaches();
        return Boolean.parseBoolean(next(boolPattern()));
    }

    public boolean hasNextByte() {
        return hasNextByte(this.defaultRadix);
    }

    public boolean hasNextByte(int i) {
        String str;
        setRadix(i);
        boolean hasNext = hasNext(integerPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                str = processIntegerToken(this.hasNextResult);
            } else {
                str = this.hasNextResult;
            }
            this.typeCache = Byte.valueOf(Byte.parseByte(str, i));
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public byte nextByte() {
        return nextByte(this.defaultRadix);
    }

    public byte nextByte(int i) {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof Byte) || this.radix != i) {
            setRadix(i);
            clearCaches();
            try {
                String next = next(integerPattern());
                if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                    next = processIntegerToken(next);
                }
                return Byte.parseByte(next, i);
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            byte byteValue = ((Byte) obj).byteValue();
            useTypeCache();
            return byteValue;
        }
    }

    public boolean hasNextShort() {
        return hasNextShort(this.defaultRadix);
    }

    public boolean hasNextShort(int i) {
        String str;
        setRadix(i);
        boolean hasNext = hasNext(integerPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                str = processIntegerToken(this.hasNextResult);
            } else {
                str = this.hasNextResult;
            }
            this.typeCache = Short.valueOf(Short.parseShort(str, i));
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public short nextShort() {
        return nextShort(this.defaultRadix);
    }

    public short nextShort(int i) {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof Short) || this.radix != i) {
            setRadix(i);
            clearCaches();
            try {
                String next = next(integerPattern());
                if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                    next = processIntegerToken(next);
                }
                return Short.parseShort(next, i);
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            short shortValue = ((Short) obj).shortValue();
            useTypeCache();
            return shortValue;
        }
    }

    public boolean hasNextInt() {
        return hasNextInt(this.defaultRadix);
    }

    public boolean hasNextInt(int i) {
        String str;
        setRadix(i);
        boolean hasNext = hasNext(integerPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                str = processIntegerToken(this.hasNextResult);
            } else {
                str = this.hasNextResult;
            }
            this.typeCache = Integer.valueOf(Integer.parseInt(str, i));
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    private String processIntegerToken(String str) {
        boolean z;
        String replaceAll = str.replaceAll("" + this.groupSeparator, "");
        int length = this.negativePrefix.length();
        boolean z2 = true;
        if (length <= 0 || !replaceAll.startsWith(this.negativePrefix)) {
            z = false;
        } else {
            replaceAll = replaceAll.substring(length);
            z = true;
        }
        int length2 = this.negativeSuffix.length();
        if (length2 <= 0 || !replaceAll.endsWith(this.negativeSuffix)) {
            z2 = z;
        } else {
            replaceAll = replaceAll.substring(replaceAll.length() - length2, replaceAll.length());
        }
        if (!z2) {
            return replaceAll;
        }
        return LanguageTag.SEP + replaceAll;
    }

    public int nextInt() {
        return nextInt(this.defaultRadix);
    }

    public int nextInt(int i) {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof Integer) || this.radix != i) {
            setRadix(i);
            clearCaches();
            try {
                String next = next(integerPattern());
                if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                    next = processIntegerToken(next);
                }
                return Integer.parseInt(next, i);
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            int intValue = ((Integer) obj).intValue();
            useTypeCache();
            return intValue;
        }
    }

    public boolean hasNextLong() {
        return hasNextLong(this.defaultRadix);
    }

    public boolean hasNextLong(int i) {
        String str;
        setRadix(i);
        boolean hasNext = hasNext(integerPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                str = processIntegerToken(this.hasNextResult);
            } else {
                str = this.hasNextResult;
            }
            this.typeCache = Long.valueOf(Long.parseLong(str, i));
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public long nextLong() {
        return nextLong(this.defaultRadix);
    }

    public long nextLong(int i) {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof Long) || this.radix != i) {
            setRadix(i);
            clearCaches();
            try {
                String next = next(integerPattern());
                if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                    next = processIntegerToken(next);
                }
                return Long.parseLong(next, i);
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            long longValue = ((Long) obj).longValue();
            useTypeCache();
            return longValue;
        }
    }

    private String processFloatToken(String str) {
        boolean z;
        String replaceAll = str.replaceAll(this.groupSeparator, "");
        if (!this.decimalSeparator.equals("\\.")) {
            replaceAll = replaceAll.replaceAll(this.decimalSeparator, BaseIconCache.EMPTY_CLASS_NAME);
        }
        int length = this.negativePrefix.length();
        boolean z2 = true;
        if (length <= 0 || !replaceAll.startsWith(this.negativePrefix)) {
            z = false;
        } else {
            replaceAll = replaceAll.substring(length);
            z = true;
        }
        int length2 = this.negativeSuffix.length();
        if (length2 <= 0 || !replaceAll.endsWith(this.negativeSuffix)) {
            z2 = z;
        } else {
            replaceAll = replaceAll.substring(replaceAll.length() - length2, replaceAll.length());
        }
        if (replaceAll.equals(this.nanString)) {
            replaceAll = "NaN";
        }
        String str2 = "Infinity";
        if (replaceAll.equals(this.infinityString)) {
            replaceAll = str2;
        }
        if (!replaceAll.equals("∞")) {
            str2 = replaceAll;
        }
        if (z2) {
            str2 = LanguageTag.SEP + str2;
        }
        if (!NON_ASCII_DIGIT.matcher(str2).find()) {
            return str2;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str2.length(); i++) {
            char charAt = str2.charAt(i);
            if (Character.isDigit(charAt)) {
                int digit = Character.digit(charAt, 10);
                if (digit != -1) {
                    sb.append(digit);
                } else {
                    sb.append(charAt);
                }
            } else {
                sb.append(charAt);
            }
        }
        return sb.toString();
    }

    public boolean hasNextFloat() {
        setRadix(10);
        boolean hasNext = hasNext(floatPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            this.typeCache = Float.valueOf(Float.parseFloat(processFloatToken(this.hasNextResult)));
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public float nextFloat() {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof Float)) {
            setRadix(10);
            clearCaches();
            try {
                return Float.parseFloat(processFloatToken(next(floatPattern())));
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            float floatValue = ((Float) obj).floatValue();
            useTypeCache();
            return floatValue;
        }
    }

    public boolean hasNextDouble() {
        setRadix(10);
        boolean hasNext = hasNext(floatPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            this.typeCache = Double.valueOf(Double.parseDouble(processFloatToken(this.hasNextResult)));
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public double nextDouble() {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof Double)) {
            setRadix(10);
            clearCaches();
            try {
                return Double.parseDouble(processFloatToken(next(floatPattern())));
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            double doubleValue = ((Double) obj).doubleValue();
            useTypeCache();
            return doubleValue;
        }
    }

    public boolean hasNextBigInteger() {
        return hasNextBigInteger(this.defaultRadix);
    }

    public boolean hasNextBigInteger(int i) {
        String str;
        setRadix(i);
        boolean hasNext = hasNext(integerPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                str = processIntegerToken(this.hasNextResult);
            } else {
                str = this.hasNextResult;
            }
            this.typeCache = new BigInteger(str, i);
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public BigInteger nextBigInteger() {
        return nextBigInteger(this.defaultRadix);
    }

    public BigInteger nextBigInteger(int i) {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof BigInteger) || this.radix != i) {
            setRadix(i);
            clearCaches();
            try {
                String next = next(integerPattern());
                if (this.matcher.group(this.SIMPLE_GROUP_INDEX) == null) {
                    next = processIntegerToken(next);
                }
                return new BigInteger(next, i);
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            BigInteger bigInteger = (BigInteger) obj;
            useTypeCache();
            return bigInteger;
        }
    }

    public boolean hasNextBigDecimal() {
        setRadix(10);
        boolean hasNext = hasNext(decimalPattern());
        if (!hasNext) {
            return hasNext;
        }
        try {
            this.typeCache = new BigDecimal(processFloatToken(this.hasNextResult));
            return hasNext;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public BigDecimal nextBigDecimal() {
        Object obj = this.typeCache;
        if (obj == null || !(obj instanceof BigDecimal)) {
            setRadix(10);
            clearCaches();
            try {
                return new BigDecimal(processFloatToken(next(decimalPattern())));
            } catch (NumberFormatException e) {
                this.position = this.matcher.start();
                throw new InputMismatchException(e.getMessage());
            }
        } else {
            BigDecimal bigDecimal = (BigDecimal) obj;
            useTypeCache();
            return bigDecimal;
        }
    }

    public Scanner reset() {
        this.delimPattern = WHITESPACE_PATTERN;
        useLocale(Locale.getDefault(Locale.Category.FORMAT));
        useRadix(10);
        clearCaches();
        return this;
    }
}
