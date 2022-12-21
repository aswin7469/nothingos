package java.time.format;

import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.Period;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import libcore.icu.ICU;

public final class DateTimeFormatter {
    public static final DateTimeFormatter BASIC_ISO_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(ChronoField.YEAR, 4).appendValue(ChronoField.MONTH_OF_YEAR, 2).appendValue(ChronoField.DAY_OF_MONTH, 2).optionalStart().parseLenient().appendOffset("+HHMMss", "Z").parseStrict().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_DATE;
    public static final DateTimeFormatter ISO_DATE_TIME;
    public static final DateTimeFormatter ISO_INSTANT = new DateTimeFormatterBuilder().parseCaseInsensitive().appendInstant().toFormatter(ResolverStyle.STRICT, (Chronology) null);
    public static final DateTimeFormatter ISO_LOCAL_DATE;
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter ISO_LOCAL_TIME;
    public static final DateTimeFormatter ISO_OFFSET_DATE;
    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME;
    public static final DateTimeFormatter ISO_OFFSET_TIME;
    public static final DateTimeFormatter ISO_ORDINAL_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.DAY_OF_YEAR, 3).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_TIME;
    public static final DateTimeFormatter ISO_WEEK_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().appendValue(IsoFields.WEEK_BASED_YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral("-W").appendValue(IsoFields.WEEK_OF_WEEK_BASED_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_WEEK, 1).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
    public static final DateTimeFormatter ISO_ZONED_DATE_TIME;
    private static final TemporalQuery<Period> PARSED_EXCESS_DAYS = new DateTimeFormatter$$ExternalSyntheticLambda0();
    private static final TemporalQuery<Boolean> PARSED_LEAP_SECOND = new DateTimeFormatter$$ExternalSyntheticLambda1();
    public static final DateTimeFormatter RFC_1123_DATE_TIME;
    private final Chronology chrono;
    private final DecimalStyle decimalStyle;
    private final Locale locale;
    private final DateTimeFormatterBuilder.CompositePrinterParser printerParser;
    /* access modifiers changed from: private */
    public final Set<TemporalField> resolverFields;
    /* access modifiers changed from: private */
    public final ResolverStyle resolverStyle;
    private final ZoneId zone;

    public static DateTimeFormatter ofPattern(String str) {
        return new DateTimeFormatterBuilder().appendPattern(str).toFormatter();
    }

    public static DateTimeFormatter ofPattern(String str, Locale locale2) {
        return new DateTimeFormatterBuilder().appendPattern(str).toFormatter(locale2);
    }

    public static DateTimeFormatter ofLocalizedDate(FormatStyle formatStyle) {
        Objects.requireNonNull(formatStyle, "dateStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, (FormatStyle) null).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }

    public static DateTimeFormatter ofLocalizedTime(FormatStyle formatStyle) {
        Objects.requireNonNull(formatStyle, "timeStyle");
        return new DateTimeFormatterBuilder().appendLocalized((FormatStyle) null, formatStyle).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }

    public static DateTimeFormatter ofLocalizedDateTime(FormatStyle formatStyle) {
        Objects.requireNonNull(formatStyle, "dateTimeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, formatStyle).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }

    public static DateTimeFormatter ofLocalizedDateTime(FormatStyle formatStyle, FormatStyle formatStyle2) {
        Objects.requireNonNull(formatStyle, "dateStyle");
        Objects.requireNonNull(formatStyle2, "timeStyle");
        return new DateTimeFormatterBuilder().appendLocalized(formatStyle, formatStyle2).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }

    static {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 2).toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_LOCAL_DATE = formatter;
        ISO_OFFSET_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().append(formatter).appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_DATE = new DateTimeFormatterBuilder().parseCaseInsensitive().append(formatter).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        DateTimeFormatter formatter2 = new DateTimeFormatterBuilder().appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR).appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR).appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter(ResolverStyle.STRICT, (Chronology) null);
        ISO_LOCAL_TIME = formatter2;
        ISO_OFFSET_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(formatter2).appendOffsetId().toFormatter(ResolverStyle.STRICT, (Chronology) null);
        ISO_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().append(formatter2).optionalStart().appendOffsetId().toFormatter(ResolverStyle.STRICT, (Chronology) null);
        DateTimeFormatter formatter3 = new DateTimeFormatterBuilder().parseCaseInsensitive().append(formatter).appendLiteral('T').append(formatter2).toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_LOCAL_DATE_TIME = formatter3;
        DateTimeFormatter formatter4 = new DateTimeFormatterBuilder().parseCaseInsensitive().append(formatter3).parseLenient().appendOffsetId().parseStrict().toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_OFFSET_DATE_TIME = formatter4;
        ISO_ZONED_DATE_TIME = new DateTimeFormatterBuilder().append(formatter4).optionalStart().appendLiteral('[').parseCaseSensitive().appendZoneRegionId().appendLiteral(']').toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        ISO_DATE_TIME = new DateTimeFormatterBuilder().append(formatter3).optionalStart().appendOffsetId().optionalStart().appendLiteral('[').parseCaseSensitive().appendZoneRegionId().appendLiteral(']').toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE);
        HashMap hashMap = new HashMap();
        hashMap.put(1L, "Mon");
        hashMap.put(2L, "Tue");
        hashMap.put(3L, "Wed");
        hashMap.put(4L, "Thu");
        hashMap.put(5L, "Fri");
        hashMap.put(6L, "Sat");
        hashMap.put(7L, "Sun");
        HashMap hashMap2 = new HashMap();
        hashMap2.put(1L, "Jan");
        hashMap2.put(2L, "Feb");
        hashMap2.put(3L, "Mar");
        hashMap2.put(4L, "Apr");
        hashMap2.put(5L, "May");
        hashMap2.put(6L, "Jun");
        hashMap2.put(7L, "Jul");
        hashMap2.put(8L, "Aug");
        hashMap2.put(9L, "Sep");
        hashMap2.put(10L, "Oct");
        hashMap2.put(11L, "Nov");
        hashMap2.put(12L, "Dec");
        RFC_1123_DATE_TIME = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().optionalStart().appendText((TemporalField) ChronoField.DAY_OF_WEEK, (Map<Long, String>) hashMap).appendLiteral(", ").optionalEnd().appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE).appendLiteral(' ').appendText((TemporalField) ChronoField.MONTH_OF_YEAR, (Map<Long, String>) hashMap2).appendLiteral(' ').appendValue(ChronoField.YEAR, 4).appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR).appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR).appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalEnd().appendLiteral(' ').appendOffset("+HHMM", "GMT").toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE);
    }

    public static final TemporalQuery<Period> parsedExcessDays() {
        return PARSED_EXCESS_DAYS;
    }

    static /* synthetic */ Period lambda$static$0(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Parsed) {
            return ((Parsed) temporalAccessor).excessDays;
        }
        return Period.ZERO;
    }

    public static final TemporalQuery<Boolean> parsedLeapSecond() {
        return PARSED_LEAP_SECOND;
    }

    static /* synthetic */ Boolean lambda$static$1(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Parsed) {
            return Boolean.valueOf(((Parsed) temporalAccessor).leapSecond);
        }
        return Boolean.FALSE;
    }

    DateTimeFormatter(DateTimeFormatterBuilder.CompositePrinterParser compositePrinterParser, Locale locale2, DecimalStyle decimalStyle2, ResolverStyle resolverStyle2, Set<TemporalField> set, Chronology chronology, ZoneId zoneId) {
        this.printerParser = (DateTimeFormatterBuilder.CompositePrinterParser) Objects.requireNonNull(compositePrinterParser, "printerParser");
        this.resolverFields = set;
        this.locale = (Locale) Objects.requireNonNull(locale2, "locale");
        this.decimalStyle = (DecimalStyle) Objects.requireNonNull(decimalStyle2, "decimalStyle");
        this.resolverStyle = (ResolverStyle) Objects.requireNonNull(resolverStyle2, "resolverStyle");
        this.chrono = chronology;
        this.zone = zoneId;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public DateTimeFormatter withLocale(Locale locale2) {
        if (this.locale.equals(locale2)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, locale2, this.decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, this.zone);
    }

    public DateTimeFormatter localizedBy(Locale locale2) {
        ZoneId zoneId;
        if (this.locale.equals(locale2)) {
            return this;
        }
        Chronology ofLocale = locale2.getUnicodeLocaleType("ca") != null ? Chronology.ofLocale(locale2) : this.chrono;
        DecimalStyle of = locale2.getUnicodeLocaleType("nu") != null ? DecimalStyle.m954of(locale2) : this.decimalStyle;
        String unicodeLocaleType = locale2.getUnicodeLocaleType("tz");
        if (unicodeLocaleType != null) {
            zoneId = (ZoneId) Optional.ofNullable(ICU.convertToTzId(unicodeLocaleType)).map(new DateTimeFormatter$$ExternalSyntheticLambda2()).orElse(this.zone);
        } else {
            zoneId = this.zone;
        }
        return new DateTimeFormatter(this.printerParser, locale2, of, this.resolverStyle, this.resolverFields, ofLocale, zoneId);
    }

    public DecimalStyle getDecimalStyle() {
        return this.decimalStyle;
    }

    public DateTimeFormatter withDecimalStyle(DecimalStyle decimalStyle2) {
        if (this.decimalStyle.equals(decimalStyle2)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, decimalStyle2, this.resolverStyle, this.resolverFields, this.chrono, this.zone);
    }

    public Chronology getChronology() {
        return this.chrono;
    }

    public DateTimeFormatter withChronology(Chronology chronology) {
        if (Objects.equals(this.chrono, chronology)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, this.resolverFields, chronology, this.zone);
    }

    public ZoneId getZone() {
        return this.zone;
    }

    public DateTimeFormatter withZone(ZoneId zoneId) {
        if (Objects.equals(this.zone, zoneId)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, this.resolverFields, this.chrono, zoneId);
    }

    public ResolverStyle getResolverStyle() {
        return this.resolverStyle;
    }

    public DateTimeFormatter withResolverStyle(ResolverStyle resolverStyle2) {
        Objects.requireNonNull(resolverStyle2, "resolverStyle");
        if (Objects.equals(this.resolverStyle, resolverStyle2)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, resolverStyle2, this.resolverFields, this.chrono, this.zone);
    }

    public Set<TemporalField> getResolverFields() {
        return this.resolverFields;
    }

    public DateTimeFormatter withResolverFields(TemporalField... temporalFieldArr) {
        Set unmodifiableSet = temporalFieldArr != null ? Collections.unmodifiableSet(new HashSet(Arrays.asList(temporalFieldArr))) : null;
        if (Objects.equals(this.resolverFields, unmodifiableSet)) {
            return this;
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, unmodifiableSet, this.chrono, this.zone);
    }

    public DateTimeFormatter withResolverFields(Set<TemporalField> set) {
        if (Objects.equals(this.resolverFields, set)) {
            return this;
        }
        if (set != null) {
            set = Collections.unmodifiableSet(new HashSet(set));
        }
        return new DateTimeFormatter(this.printerParser, this.locale, this.decimalStyle, this.resolverStyle, set, this.chrono, this.zone);
    }

    public String format(TemporalAccessor temporalAccessor) {
        StringBuilder sb = new StringBuilder(32);
        formatTo(temporalAccessor, sb);
        return sb.toString();
    }

    public void formatTo(TemporalAccessor temporalAccessor, Appendable appendable) {
        Objects.requireNonNull(temporalAccessor, "temporal");
        Objects.requireNonNull(appendable, "appendable");
        try {
            DateTimePrintContext dateTimePrintContext = new DateTimePrintContext(temporalAccessor, this);
            if (appendable instanceof StringBuilder) {
                this.printerParser.format(dateTimePrintContext, (StringBuilder) appendable);
                return;
            }
            StringBuilder sb = new StringBuilder(32);
            this.printerParser.format(dateTimePrintContext, sb);
            appendable.append((CharSequence) sb);
        } catch (IOException e) {
            throw new DateTimeException(e.getMessage(), e);
        }
    }

    public TemporalAccessor parse(CharSequence charSequence) {
        Objects.requireNonNull(charSequence, "text");
        try {
            return parseResolved0(charSequence, (ParsePosition) null);
        } catch (DateTimeParseException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw createError(charSequence, e2);
        }
    }

    public TemporalAccessor parse(CharSequence charSequence, ParsePosition parsePosition) {
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(parsePosition, "position");
        try {
            return parseResolved0(charSequence, parsePosition);
        } catch (IndexOutOfBoundsException | DateTimeParseException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw createError(charSequence, e2);
        }
    }

    public <T> T parse(CharSequence charSequence, TemporalQuery<T> temporalQuery) {
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(temporalQuery, "query");
        try {
            return parseResolved0(charSequence, (ParsePosition) null).query(temporalQuery);
        } catch (DateTimeParseException e) {
            throw e;
        } catch (RuntimeException e2) {
            throw createError(charSequence, e2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0022, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0034, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033 A[ExcHandler: DateTimeParseException (r4v2 'e' java.time.format.DateTimeParseException A[CUSTOM_DECLARE]), Splitter:B:3:0x0011] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.time.temporal.TemporalAccessor parseBest(java.lang.CharSequence r5, java.time.temporal.TemporalQuery<?>... r6) {
        /*
            r4 = this;
            java.lang.String r0 = "text"
            java.util.Objects.requireNonNull(r5, (java.lang.String) r0)
            java.lang.String r0 = "queries"
            java.util.Objects.requireNonNull(r6, (java.lang.String) r0)
            int r0 = r6.length
            r1 = 2
            if (r0 < r1) goto L_0x0035
            r0 = 0
            java.time.temporal.TemporalAccessor r0 = r4.parseResolved0(r5, r0)     // Catch:{ DateTimeParseException -> 0x0033, RuntimeException -> 0x002d }
            int r1 = r6.length     // Catch:{ DateTimeParseException -> 0x0033, RuntimeException -> 0x002d }
            r2 = 0
        L_0x0017:
            if (r2 >= r1) goto L_0x0025
            r3 = r6[r2]     // Catch:{ DateTimeParseException -> 0x0033, RuntimeException -> 0x002d }
            java.lang.Object r3 = r0.query(r3)     // Catch:{ RuntimeException -> 0x0022, DateTimeParseException -> 0x0033 }
            java.time.temporal.TemporalAccessor r3 = (java.time.temporal.TemporalAccessor) r3     // Catch:{ RuntimeException -> 0x0022, DateTimeParseException -> 0x0033 }
            return r3
        L_0x0022:
            int r2 = r2 + 1
            goto L_0x0017
        L_0x0025:
            java.time.DateTimeException r6 = new java.time.DateTimeException     // Catch:{ DateTimeParseException -> 0x0033, RuntimeException -> 0x002d }
            java.lang.String r0 = "Unable to convert parsed text using any of the specified queries"
            r6.<init>(r0)     // Catch:{ DateTimeParseException -> 0x0033, RuntimeException -> 0x002d }
            throw r6     // Catch:{ DateTimeParseException -> 0x0033, RuntimeException -> 0x002d }
        L_0x002d:
            r6 = move-exception
            java.time.format.DateTimeParseException r4 = r4.createError(r5, r6)
            throw r4
        L_0x0033:
            r4 = move-exception
            throw r4
        L_0x0035:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = "At least two queries must be specified"
            r4.<init>((java.lang.String) r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatter.parseBest(java.lang.CharSequence, java.time.temporal.TemporalQuery[]):java.time.temporal.TemporalAccessor");
    }

    private DateTimeParseException createError(CharSequence charSequence, RuntimeException runtimeException) {
        String str;
        if (charSequence.length() > 64) {
            str = charSequence.subSequence(0, 64).toString() + "...";
        } else {
            str = charSequence.toString();
        }
        return new DateTimeParseException("Text '" + str + "' could not be parsed: " + runtimeException.getMessage(), charSequence, 0, runtimeException);
    }

    /* access modifiers changed from: private */
    public TemporalAccessor parseResolved0(CharSequence charSequence, ParsePosition parsePosition) {
        String str;
        ParsePosition parsePosition2 = parsePosition != null ? parsePosition : new ParsePosition(0);
        DateTimeParseContext parseUnresolved0 = parseUnresolved0(charSequence, parsePosition2);
        if (parseUnresolved0 != null && parsePosition2.getErrorIndex() < 0 && (parsePosition != null || parsePosition2.getIndex() >= charSequence.length())) {
            return parseUnresolved0.toResolved(this.resolverStyle, this.resolverFields);
        }
        if (charSequence.length() > 64) {
            str = charSequence.subSequence(0, 64).toString() + "...";
        } else {
            str = charSequence.toString();
        }
        if (parsePosition2.getErrorIndex() >= 0) {
            throw new DateTimeParseException("Text '" + str + "' could not be parsed at index " + parsePosition2.getErrorIndex(), charSequence, parsePosition2.getErrorIndex());
        }
        throw new DateTimeParseException("Text '" + str + "' could not be parsed, unparsed text found at index " + parsePosition2.getIndex(), charSequence, parsePosition2.getIndex());
    }

    public TemporalAccessor parseUnresolved(CharSequence charSequence, ParsePosition parsePosition) {
        DateTimeParseContext parseUnresolved0 = parseUnresolved0(charSequence, parsePosition);
        if (parseUnresolved0 == null) {
            return null;
        }
        return parseUnresolved0.toUnresolved();
    }

    /* access modifiers changed from: private */
    public DateTimeParseContext parseUnresolved0(CharSequence charSequence, ParsePosition parsePosition) {
        Objects.requireNonNull(charSequence, "text");
        Objects.requireNonNull(parsePosition, "position");
        DateTimeParseContext dateTimeParseContext = new DateTimeParseContext(this);
        int parse = this.printerParser.parse(dateTimeParseContext, charSequence, parsePosition.getIndex());
        if (parse < 0) {
            parsePosition.setErrorIndex(~parse);
            return null;
        }
        parsePosition.setIndex(parse);
        return dateTimeParseContext;
    }

    /* access modifiers changed from: package-private */
    public DateTimeFormatterBuilder.CompositePrinterParser toPrinterParser(boolean z) {
        return this.printerParser.withOptional(z);
    }

    public Format toFormat() {
        return new ClassicFormat(this, (TemporalQuery<?>) null);
    }

    public Format toFormat(TemporalQuery<?> temporalQuery) {
        Objects.requireNonNull(temporalQuery, "parseQuery");
        return new ClassicFormat(this, temporalQuery);
    }

    public String toString() {
        String compositePrinterParser = this.printerParser.toString();
        return compositePrinterParser.startsWith(NavigationBarInflaterView.SIZE_MOD_START) ? compositePrinterParser : compositePrinterParser.substring(1, compositePrinterParser.length() - 1);
    }

    static class ClassicFormat extends Format {
        private final DateTimeFormatter formatter;
        private final TemporalQuery<?> parseType;

        public ClassicFormat(DateTimeFormatter dateTimeFormatter, TemporalQuery<?> temporalQuery) {
            this.formatter = dateTimeFormatter;
            this.parseType = temporalQuery;
        }

        public StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
            Objects.requireNonNull(obj, "obj");
            Objects.requireNonNull(stringBuffer, "toAppendTo");
            Objects.requireNonNull(fieldPosition, "pos");
            if (obj instanceof TemporalAccessor) {
                fieldPosition.setBeginIndex(0);
                fieldPosition.setEndIndex(0);
                try {
                    this.formatter.formatTo((TemporalAccessor) obj, stringBuffer);
                    return stringBuffer;
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            } else {
                throw new IllegalArgumentException("Format target must implement TemporalAccessor");
            }
        }

        public Object parseObject(String str) throws ParseException {
            Objects.requireNonNull(str, "text");
            try {
                TemporalQuery<?> temporalQuery = this.parseType;
                if (temporalQuery == null) {
                    return this.formatter.parseResolved0(str, (ParsePosition) null);
                }
                return this.formatter.parse((CharSequence) str, temporalQuery);
            } catch (DateTimeParseException e) {
                throw new ParseException(e.getMessage(), e.getErrorIndex());
            } catch (RuntimeException e2) {
                throw ((ParseException) new ParseException(e2.getMessage(), 0).initCause(e2));
            }
        }

        public Object parseObject(String str, ParsePosition parsePosition) {
            Objects.requireNonNull(str, "text");
            try {
                DateTimeParseContext r5 = this.formatter.parseUnresolved0(str, parsePosition);
                if (r5 == null) {
                    if (parsePosition.getErrorIndex() < 0) {
                        parsePosition.setErrorIndex(0);
                    }
                    return null;
                }
                try {
                    TemporalAccessor resolved = r5.toResolved(this.formatter.resolverStyle, this.formatter.resolverFields);
                    TemporalQuery<?> temporalQuery = this.parseType;
                    if (temporalQuery == null) {
                        return resolved;
                    }
                    return resolved.query(temporalQuery);
                } catch (RuntimeException unused) {
                    parsePosition.setErrorIndex(0);
                    return null;
                }
            } catch (IndexOutOfBoundsException unused2) {
                if (parsePosition.getErrorIndex() < 0) {
                    parsePosition.setErrorIndex(0);
                }
                return null;
            }
        }
    }
}
