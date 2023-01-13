package java.time.format;

import android.icu.text.LocaleDisplayNames;
import android.icu.text.TimeZoneNames;
import android.icu.util.ULocale;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeTextProvider;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.JulianFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.time.zone.ZoneRulesProvider;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import libcore.icu.ICU;
import sun.util.locale.LanguageTag;

public final class DateTimeFormatterBuilder {
    private static final Map<Character, TemporalField> FIELD_MAP;
    static final Comparator<String> LENGTH_SORT = new Comparator<String>() {
        public int compare(String str, String str2) {
            return str.length() == str2.length() ? str.compareTo(str2) : str.length() - str2.length();
        }
    };
    private static final TemporalQuery<ZoneId> QUERY_REGION_ONLY = new DateTimeFormatterBuilder$$ExternalSyntheticLambda0();
    private DateTimeFormatterBuilder active;
    private final boolean optional;
    private char padNextChar;
    private int padNextWidth;
    private final DateTimeFormatterBuilder parent;
    private final List<DateTimePrinterParser> printerParsers;
    private int valueParserIndex;

    interface DateTimePrinterParser {
        boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb);

        int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i);
    }

    static {
        HashMap hashMap = new HashMap();
        FIELD_MAP = hashMap;
        hashMap.put('G', ChronoField.ERA);
        hashMap.put('y', ChronoField.YEAR_OF_ERA);
        hashMap.put('u', ChronoField.YEAR);
        hashMap.put('Q', IsoFields.QUARTER_OF_YEAR);
        hashMap.put('q', IsoFields.QUARTER_OF_YEAR);
        hashMap.put('M', ChronoField.MONTH_OF_YEAR);
        hashMap.put('L', ChronoField.MONTH_OF_YEAR);
        hashMap.put('D', ChronoField.DAY_OF_YEAR);
        hashMap.put('d', ChronoField.DAY_OF_MONTH);
        hashMap.put('F', ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        hashMap.put('E', ChronoField.DAY_OF_WEEK);
        hashMap.put('c', ChronoField.DAY_OF_WEEK);
        hashMap.put('e', ChronoField.DAY_OF_WEEK);
        hashMap.put('a', ChronoField.AMPM_OF_DAY);
        hashMap.put('H', ChronoField.HOUR_OF_DAY);
        hashMap.put('k', ChronoField.CLOCK_HOUR_OF_DAY);
        hashMap.put('K', ChronoField.HOUR_OF_AMPM);
        hashMap.put('h', ChronoField.CLOCK_HOUR_OF_AMPM);
        hashMap.put('m', ChronoField.MINUTE_OF_HOUR);
        hashMap.put('s', ChronoField.SECOND_OF_MINUTE);
        hashMap.put('S', ChronoField.NANO_OF_SECOND);
        hashMap.put('A', ChronoField.MILLI_OF_DAY);
        hashMap.put('n', ChronoField.NANO_OF_SECOND);
        hashMap.put('N', ChronoField.NANO_OF_DAY);
        hashMap.put('g', JulianFields.MODIFIED_JULIAN_DAY);
    }

    static /* synthetic */ ZoneId lambda$static$0(TemporalAccessor temporalAccessor) {
        ZoneId zoneId = (ZoneId) temporalAccessor.query(TemporalQueries.zoneId());
        if (zoneId == null || (zoneId instanceof ZoneOffset)) {
            return null;
        }
        return zoneId;
    }

    public static String getLocalizedDateTimePattern(FormatStyle formatStyle, FormatStyle formatStyle2, Chronology chronology, Locale locale) {
        Objects.requireNonNull(locale, "locale");
        Objects.requireNonNull(chronology, "chrono");
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either dateStyle or timeStyle must be non-null");
        }
        return ICU.transformIcuDateTimePattern_forJavaTime(ICU.getExtendedCalendar(locale, chronology instanceof IsoChronology ? "gregorian" : chronology.getCalendarType()).getDateTimePattern(convertStyle(formatStyle), convertStyle(formatStyle2)));
    }

    private static int convertStyle(FormatStyle formatStyle) {
        if (formatStyle == null) {
            return -1;
        }
        return formatStyle.ordinal();
    }

    public DateTimeFormatterBuilder() {
        this.active = this;
        this.printerParsers = new ArrayList();
        this.valueParserIndex = -1;
        this.parent = null;
        this.optional = false;
    }

    private DateTimeFormatterBuilder(DateTimeFormatterBuilder dateTimeFormatterBuilder, boolean z) {
        this.active = this;
        this.printerParsers = new ArrayList();
        this.valueParserIndex = -1;
        this.parent = dateTimeFormatterBuilder;
        this.optional = z;
    }

    public DateTimeFormatterBuilder parseCaseSensitive() {
        appendInternal(SettingsParser.SENSITIVE);
        return this;
    }

    public DateTimeFormatterBuilder parseCaseInsensitive() {
        appendInternal(SettingsParser.INSENSITIVE);
        return this;
    }

    public DateTimeFormatterBuilder parseStrict() {
        appendInternal(SettingsParser.STRICT);
        return this;
    }

    public DateTimeFormatterBuilder parseLenient() {
        appendInternal(SettingsParser.LENIENT);
        return this;
    }

    public DateTimeFormatterBuilder parseDefaulting(TemporalField temporalField, long j) {
        Objects.requireNonNull(temporalField, "field");
        appendInternal(new DefaultValueParser(temporalField, j));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField) {
        Objects.requireNonNull(temporalField, "field");
        appendValue(new NumberPrinterParser(temporalField, 1, 19, SignStyle.NORMAL));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField, int i) {
        Objects.requireNonNull(temporalField, "field");
        if (i < 1 || i > 19) {
            throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + i);
        }
        appendValue(new NumberPrinterParser(temporalField, i, i, SignStyle.NOT_NEGATIVE));
        return this;
    }

    public DateTimeFormatterBuilder appendValue(TemporalField temporalField, int i, int i2, SignStyle signStyle) {
        if (i == i2 && signStyle == SignStyle.NOT_NEGATIVE) {
            return appendValue(temporalField, i2);
        }
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(signStyle, "signStyle");
        if (i < 1 || i > 19) {
            throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + i);
        } else if (i2 < 1 || i2 > 19) {
            throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + i2);
        } else if (i2 >= i) {
            appendValue(new NumberPrinterParser(temporalField, i, i2, signStyle));
            return this;
        } else {
            throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + i2 + " < " + i);
        }
    }

    public DateTimeFormatterBuilder appendValueReduced(TemporalField temporalField, int i, int i2, int i3) {
        Objects.requireNonNull(temporalField, "field");
        appendValue((NumberPrinterParser) new ReducedPrinterParser(temporalField, i, i2, i3, (ChronoLocalDate) null));
        return this;
    }

    public DateTimeFormatterBuilder appendValueReduced(TemporalField temporalField, int i, int i2, ChronoLocalDate chronoLocalDate) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(chronoLocalDate, "baseDate");
        appendValue((NumberPrinterParser) new ReducedPrinterParser(temporalField, i, i2, 0, chronoLocalDate));
        return this;
    }

    private DateTimeFormatterBuilder appendValue(NumberPrinterParser numberPrinterParser) {
        NumberPrinterParser numberPrinterParser2;
        DateTimeFormatterBuilder dateTimeFormatterBuilder = this.active;
        int i = dateTimeFormatterBuilder.valueParserIndex;
        if (i >= 0) {
            NumberPrinterParser numberPrinterParser3 = (NumberPrinterParser) dateTimeFormatterBuilder.printerParsers.get(i);
            if (numberPrinterParser.minWidth == numberPrinterParser.maxWidth && numberPrinterParser.signStyle == SignStyle.NOT_NEGATIVE) {
                numberPrinterParser2 = numberPrinterParser3.withSubsequentWidth(numberPrinterParser.maxWidth);
                appendInternal(numberPrinterParser.withFixedWidth());
                this.active.valueParserIndex = i;
            } else {
                numberPrinterParser2 = numberPrinterParser3.withFixedWidth();
                this.active.valueParserIndex = appendInternal(numberPrinterParser);
            }
            this.active.printerParsers.set(i, numberPrinterParser2);
        } else {
            dateTimeFormatterBuilder.valueParserIndex = appendInternal(numberPrinterParser);
        }
        return this;
    }

    public DateTimeFormatterBuilder appendFraction(TemporalField temporalField, int i, int i2, boolean z) {
        if (i != i2 || z) {
            appendInternal(new FractionPrinterParser(temporalField, i, i2, z));
        } else {
            appendValue((NumberPrinterParser) new FractionPrinterParser(temporalField, i, i2, z));
        }
        return this;
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField) {
        return appendText(temporalField, TextStyle.FULL);
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField, TextStyle textStyle) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(textStyle, "textStyle");
        appendInternal(new TextPrinterParser(temporalField, textStyle, DateTimeTextProvider.getInstance()));
        return this;
    }

    public DateTimeFormatterBuilder appendText(TemporalField temporalField, Map<Long, String> map) {
        Objects.requireNonNull(temporalField, "field");
        Objects.requireNonNull(map, "textLookup");
        final DateTimeTextProvider.LocaleStore localeStore = new DateTimeTextProvider.LocaleStore(Collections.singletonMap(TextStyle.FULL, new LinkedHashMap(map)));
        appendInternal(new TextPrinterParser(temporalField, TextStyle.FULL, new DateTimeTextProvider() {
            public String getText(Chronology chronology, TemporalField temporalField, long j, TextStyle textStyle, Locale locale) {
                return localeStore.getText(j, textStyle);
            }

            public String getText(TemporalField temporalField, long j, TextStyle textStyle, Locale locale) {
                return localeStore.getText(j, textStyle);
            }

            public Iterator<Map.Entry<String, Long>> getTextIterator(Chronology chronology, TemporalField temporalField, TextStyle textStyle, Locale locale) {
                return localeStore.getTextIterator(textStyle);
            }

            public Iterator<Map.Entry<String, Long>> getTextIterator(TemporalField temporalField, TextStyle textStyle, Locale locale) {
                return localeStore.getTextIterator(textStyle);
            }
        }));
        return this;
    }

    public DateTimeFormatterBuilder appendInstant() {
        appendInternal(new InstantPrinterParser(-2));
        return this;
    }

    public DateTimeFormatterBuilder appendInstant(int i) {
        if (i < -1 || i > 9) {
            throw new IllegalArgumentException("The fractional digits must be from -1 to 9 inclusive but was " + i);
        }
        appendInternal(new InstantPrinterParser(i));
        return this;
    }

    public DateTimeFormatterBuilder appendOffsetId() {
        appendInternal(OffsetIdPrinterParser.INSTANCE_ID_Z);
        return this;
    }

    public DateTimeFormatterBuilder appendOffset(String str, String str2) {
        appendInternal(new OffsetIdPrinterParser(str, str2));
        return this;
    }

    public DateTimeFormatterBuilder appendLocalizedOffset(TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "style");
        if (textStyle == TextStyle.FULL || textStyle == TextStyle.SHORT) {
            appendInternal(new LocalizedOffsetIdPrinterParser(textStyle));
            return this;
        }
        throw new IllegalArgumentException("Style must be either full or short");
    }

    public DateTimeFormatterBuilder appendZoneId() {
        appendInternal(new ZoneIdPrinterParser(TemporalQueries.zoneId(), "ZoneId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneRegionId() {
        appendInternal(new ZoneIdPrinterParser(QUERY_REGION_ONLY, "ZoneRegionId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneOrOffsetId() {
        appendInternal(new ZoneIdPrinterParser(TemporalQueries.zone(), "ZoneOrOffsetId()"));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneText(TextStyle textStyle) {
        appendInternal(new ZoneTextPrinterParser(textStyle, (Set<ZoneId>) null, false));
        return this;
    }

    public DateTimeFormatterBuilder appendZoneText(TextStyle textStyle, Set<ZoneId> set) {
        Objects.requireNonNull(set, "preferredZones");
        appendInternal(new ZoneTextPrinterParser(textStyle, set, false));
        return this;
    }

    public DateTimeFormatterBuilder appendGenericZoneText(TextStyle textStyle) {
        appendInternal(new ZoneTextPrinterParser(textStyle, (Set<ZoneId>) null, true));
        return this;
    }

    public DateTimeFormatterBuilder appendGenericZoneText(TextStyle textStyle, Set<ZoneId> set) {
        appendInternal(new ZoneTextPrinterParser(textStyle, set, true));
        return this;
    }

    public DateTimeFormatterBuilder appendChronologyId() {
        appendInternal(new ChronoPrinterParser((TextStyle) null));
        return this;
    }

    public DateTimeFormatterBuilder appendChronologyText(TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");
        appendInternal(new ChronoPrinterParser(textStyle));
        return this;
    }

    public DateTimeFormatterBuilder appendLocalized(FormatStyle formatStyle, FormatStyle formatStyle2) {
        if (formatStyle == null && formatStyle2 == null) {
            throw new IllegalArgumentException("Either the date or time style must be non-null");
        }
        appendInternal(new LocalizedPrinterParser(formatStyle, formatStyle2));
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(char c) {
        appendInternal(new CharLiteralPrinterParser(c));
        return this;
    }

    public DateTimeFormatterBuilder appendLiteral(String str) {
        Objects.requireNonNull(str, "literal");
        if (!str.isEmpty()) {
            if (str.length() == 1) {
                appendInternal(new CharLiteralPrinterParser(str.charAt(0)));
            } else {
                appendInternal(new StringLiteralPrinterParser(str));
            }
        }
        return this;
    }

    public DateTimeFormatterBuilder append(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        appendInternal(dateTimeFormatter.toPrinterParser(false));
        return this;
    }

    public DateTimeFormatterBuilder appendOptional(DateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter, "formatter");
        appendInternal(dateTimeFormatter.toPrinterParser(true));
        return this;
    }

    public DateTimeFormatterBuilder appendPattern(String str) {
        Objects.requireNonNull(str, "pattern");
        parsePattern(str);
        return this;
    }

    private void parsePattern(String str) {
        int i;
        int i2 = 0;
        while (i2 < str.length()) {
            char charAt = str.charAt(i2);
            if ((charAt >= 'A' && charAt <= 'Z') || (charAt >= 'a' && charAt <= 'z')) {
                int i3 = i2 + 1;
                while (i3 < str.length() && str.charAt(i3) == charAt) {
                    i3++;
                }
                int i4 = i3 - i2;
                if (charAt == 'p') {
                    if (i3 >= str.length() || (((charAt = str.charAt(i3)) < 'A' || charAt > 'Z') && (charAt < 'a' || charAt > 'z'))) {
                        i = i4;
                        i4 = 0;
                    } else {
                        int i5 = i3 + 1;
                        while (i5 < str.length() && str.charAt(i5) == charAt) {
                            i5++;
                        }
                        i = i5 - i3;
                        i3 = i5;
                    }
                    if (i4 != 0) {
                        padNext(i4);
                        i4 = i;
                    } else {
                        throw new IllegalArgumentException("Pad letter 'p' must be followed by valid pad pattern: " + str);
                    }
                }
                TemporalField temporalField = FIELD_MAP.get(Character.valueOf(charAt));
                if (temporalField != null) {
                    parseField(charAt, i4, temporalField);
                } else if (charAt == 'z') {
                    if (i4 > 4) {
                        throw new IllegalArgumentException("Too many pattern letters: " + charAt);
                    } else if (i4 == 4) {
                        appendZoneText(TextStyle.FULL);
                    } else {
                        appendZoneText(TextStyle.SHORT);
                    }
                } else if (charAt == 'V') {
                    if (i4 == 2) {
                        appendZoneId();
                    } else {
                        throw new IllegalArgumentException("Pattern letter count must be 2: " + charAt);
                    }
                } else if (charAt != 'v') {
                    String str2 = "+0000";
                    if (charAt == 'Z') {
                        if (i4 < 4) {
                            appendOffset("+HHMM", str2);
                        } else if (i4 == 4) {
                            appendLocalizedOffset(TextStyle.FULL);
                        } else if (i4 == 5) {
                            appendOffset("+HH:MM:ss", "Z");
                        } else {
                            throw new IllegalArgumentException("Too many pattern letters: " + charAt);
                        }
                    } else if (charAt == 'O') {
                        if (i4 == 1) {
                            appendLocalizedOffset(TextStyle.SHORT);
                        } else if (i4 == 4) {
                            appendLocalizedOffset(TextStyle.FULL);
                        } else {
                            throw new IllegalArgumentException("Pattern letter count must be 1 or 4: " + charAt);
                        }
                    } else if (charAt == 'X') {
                        if (i4 <= 5) {
                            appendOffset(OffsetIdPrinterParser.PATTERNS[i4 + (i4 == 1 ? 0 : 1)], "Z");
                        } else {
                            throw new IllegalArgumentException("Too many pattern letters: " + charAt);
                        }
                    } else if (charAt == 'x') {
                        if (i4 <= 5) {
                            if (i4 == 1) {
                                str2 = "+00";
                            } else if (i4 % 2 != 0) {
                                str2 = "+00:00";
                            }
                            appendOffset(OffsetIdPrinterParser.PATTERNS[i4 + (i4 == 1 ? 0 : 1)], str2);
                        } else {
                            throw new IllegalArgumentException("Too many pattern letters: " + charAt);
                        }
                    } else if (charAt == 'W') {
                        if (i4 <= 1) {
                            appendValue((NumberPrinterParser) new WeekBasedFieldPrinterParser(charAt, i4, i4, i4));
                        } else {
                            throw new IllegalArgumentException("Too many pattern letters: " + charAt);
                        }
                    } else if (charAt == 'w') {
                        if (i4 <= 2) {
                            appendValue((NumberPrinterParser) new WeekBasedFieldPrinterParser(charAt, i4, i4, 2));
                        } else {
                            throw new IllegalArgumentException("Too many pattern letters: " + charAt);
                        }
                    } else if (charAt != 'Y') {
                        throw new IllegalArgumentException("Unknown pattern letter: " + charAt);
                    } else if (i4 == 2) {
                        appendValue((NumberPrinterParser) new WeekBasedFieldPrinterParser(charAt, i4, i4, 2));
                    } else {
                        appendValue((NumberPrinterParser) new WeekBasedFieldPrinterParser(charAt, i4, i4, 19));
                    }
                } else if (i4 == 1) {
                    appendGenericZoneText(TextStyle.SHORT);
                } else if (i4 == 4) {
                    appendGenericZoneText(TextStyle.FULL);
                } else {
                    throw new IllegalArgumentException("Wrong number of  pattern letters: " + charAt);
                }
                i2 = i3 - 1;
            } else if (charAt == '\'') {
                int i6 = i2 + 1;
                int i7 = i6;
                while (i7 < str.length()) {
                    if (str.charAt(i7) == '\'') {
                        int i8 = i7 + 1;
                        if (i8 >= str.length() || str.charAt(i8) != '\'') {
                            break;
                        }
                        i7 = i8;
                    }
                    i7++;
                }
                if (i7 < str.length()) {
                    String substring = str.substring(i6, i7);
                    if (substring.isEmpty()) {
                        appendLiteral('\'');
                    } else {
                        appendLiteral(substring.replace((CharSequence) "''", (CharSequence) "'"));
                    }
                    i2 = i7;
                } else {
                    throw new IllegalArgumentException("Pattern ends with an incomplete string literal: " + str);
                }
            } else if (charAt == '[') {
                optionalStart();
            } else if (charAt == ']') {
                if (this.active.parent != null) {
                    optionalEnd();
                } else {
                    throw new IllegalArgumentException("Pattern invalid as it contains ] without previous [");
                }
            } else if (charAt == '{' || charAt == '}' || charAt == '#') {
                throw new IllegalArgumentException("Pattern includes reserved character: '" + charAt + "'");
            } else {
                appendLiteral(charAt);
            }
            i2++;
        }
    }

    private void parseField(char c, int i, TemporalField temporalField) {
        TextStyle textStyle;
        if (c != 'A') {
            boolean z = false;
            if (c != 'Q') {
                if (c == 'S') {
                    appendFraction(ChronoField.NANO_OF_SECOND, i, i, false);
                    return;
                } else if (c != 'a') {
                    if (c != 'k') {
                        if (c != 'q') {
                            if (c != 's') {
                                if (c == 'u' || c == 'y') {
                                    if (i == 2) {
                                        appendValueReduced(temporalField, 2, 2, (ChronoLocalDate) ReducedPrinterParser.BASE_DATE);
                                        return;
                                    } else if (i < 4) {
                                        appendValue(temporalField, i, 19, SignStyle.NORMAL);
                                        return;
                                    } else {
                                        appendValue(temporalField, i, 19, SignStyle.EXCEEDS_PAD);
                                        return;
                                    }
                                } else if (c == 'g') {
                                    appendValue(temporalField, i, 19, SignStyle.NORMAL);
                                    return;
                                } else if (!(c == 'h' || c == 'm')) {
                                    if (c != 'n') {
                                        switch (c) {
                                            case 'D':
                                                if (i == 1) {
                                                    appendValue(temporalField);
                                                    return;
                                                } else if (i == 2 || i == 3) {
                                                    appendValue(temporalField, i, 3, SignStyle.NOT_NEGATIVE);
                                                    return;
                                                } else {
                                                    throw new IllegalArgumentException("Too many pattern letters: " + c);
                                                }
                                            case 'E':
                                                break;
                                            case 'F':
                                                if (i == 1) {
                                                    appendValue(temporalField);
                                                    return;
                                                }
                                                throw new IllegalArgumentException("Too many pattern letters: " + c);
                                            case 'G':
                                                if (i == 1 || i == 2 || i == 3) {
                                                    appendText(temporalField, TextStyle.SHORT);
                                                    return;
                                                } else if (i == 4) {
                                                    appendText(temporalField, TextStyle.FULL);
                                                    return;
                                                } else if (i == 5) {
                                                    appendText(temporalField, TextStyle.NARROW);
                                                    return;
                                                } else {
                                                    throw new IllegalArgumentException("Too many pattern letters: " + c);
                                                }
                                            case 'H':
                                                break;
                                            default:
                                                switch (c) {
                                                    case 'K':
                                                        break;
                                                    case 'L':
                                                        break;
                                                    case 'M':
                                                        break;
                                                    case 'N':
                                                        break;
                                                    default:
                                                        switch (c) {
                                                            case 'c':
                                                                if (i == 1) {
                                                                    appendValue((NumberPrinterParser) new WeekBasedFieldPrinterParser(c, i, i, i));
                                                                    return;
                                                                } else if (i == 2) {
                                                                    throw new IllegalArgumentException("Invalid pattern \"cc\"");
                                                                }
                                                                break;
                                                            case 'd':
                                                                break;
                                                            case 'e':
                                                                break;
                                                            default:
                                                                if (i == 1) {
                                                                    appendValue(temporalField);
                                                                    return;
                                                                } else {
                                                                    appendValue(temporalField, i);
                                                                    return;
                                                                }
                                                        }
                                                }
                                        }
                                    }
                                }
                            }
                        }
                        z = true;
                    }
                    if (i == 1) {
                        appendValue(temporalField);
                        return;
                    } else if (i == 2) {
                        appendValue(temporalField, i);
                        return;
                    } else {
                        throw new IllegalArgumentException("Too many pattern letters: " + c);
                    }
                } else if (i == 1) {
                    appendText(temporalField, TextStyle.SHORT);
                    return;
                } else {
                    throw new IllegalArgumentException("Too many pattern letters: " + c);
                }
            }
            if (i == 1 || i == 2) {
                if (c == 'e') {
                    appendValue((NumberPrinterParser) new WeekBasedFieldPrinterParser(c, i, i, i));
                    return;
                } else if (c == 'E') {
                    appendText(temporalField, TextStyle.SHORT);
                    return;
                } else if (i == 1) {
                    appendValue(temporalField);
                    return;
                } else {
                    appendValue(temporalField, 2);
                    return;
                }
            } else if (i == 3) {
                if (z) {
                    textStyle = TextStyle.SHORT_STANDALONE;
                } else {
                    textStyle = TextStyle.SHORT;
                }
                appendText(temporalField, textStyle);
                return;
            } else if (i == 4) {
                appendText(temporalField, z ? TextStyle.FULL_STANDALONE : TextStyle.FULL);
                return;
            } else if (i == 5) {
                appendText(temporalField, z ? TextStyle.NARROW_STANDALONE : TextStyle.NARROW);
                return;
            } else {
                throw new IllegalArgumentException("Too many pattern letters: " + c);
            }
        }
        appendValue(temporalField, i, 19, SignStyle.NOT_NEGATIVE);
    }

    public DateTimeFormatterBuilder padNext(int i) {
        return padNext(i, ' ');
    }

    public DateTimeFormatterBuilder padNext(int i, char c) {
        if (i >= 1) {
            DateTimeFormatterBuilder dateTimeFormatterBuilder = this.active;
            dateTimeFormatterBuilder.padNextWidth = i;
            dateTimeFormatterBuilder.padNextChar = c;
            dateTimeFormatterBuilder.valueParserIndex = -1;
            return this;
        }
        throw new IllegalArgumentException("The pad width must be at least one but was " + i);
    }

    public DateTimeFormatterBuilder optionalStart() {
        DateTimeFormatterBuilder dateTimeFormatterBuilder = this.active;
        dateTimeFormatterBuilder.valueParserIndex = -1;
        this.active = new DateTimeFormatterBuilder(dateTimeFormatterBuilder, true);
        return this;
    }

    public DateTimeFormatterBuilder optionalEnd() {
        DateTimeFormatterBuilder dateTimeFormatterBuilder = this.active;
        if (dateTimeFormatterBuilder.parent != null) {
            if (dateTimeFormatterBuilder.printerParsers.size() > 0) {
                DateTimeFormatterBuilder dateTimeFormatterBuilder2 = this.active;
                CompositePrinterParser compositePrinterParser = new CompositePrinterParser(dateTimeFormatterBuilder2.printerParsers, dateTimeFormatterBuilder2.optional);
                this.active = this.active.parent;
                appendInternal(compositePrinterParser);
            } else {
                this.active = this.active.parent;
            }
            return this;
        }
        throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()");
    }

    private int appendInternal(DateTimePrinterParser dateTimePrinterParser) {
        Objects.requireNonNull(dateTimePrinterParser, "pp");
        DateTimeFormatterBuilder dateTimeFormatterBuilder = this.active;
        int i = dateTimeFormatterBuilder.padNextWidth;
        if (i > 0) {
            if (dateTimePrinterParser != null) {
                dateTimePrinterParser = new PadPrinterParserDecorator(dateTimePrinterParser, i, dateTimeFormatterBuilder.padNextChar);
            }
            DateTimeFormatterBuilder dateTimeFormatterBuilder2 = this.active;
            dateTimeFormatterBuilder2.padNextWidth = 0;
            dateTimeFormatterBuilder2.padNextChar = 0;
        }
        this.active.printerParsers.add(dateTimePrinterParser);
        DateTimeFormatterBuilder dateTimeFormatterBuilder3 = this.active;
        dateTimeFormatterBuilder3.valueParserIndex = -1;
        return dateTimeFormatterBuilder3.printerParsers.size() - 1;
    }

    public DateTimeFormatter toFormatter() {
        return toFormatter(Locale.getDefault(Locale.Category.FORMAT));
    }

    public DateTimeFormatter toFormatter(Locale locale) {
        return toFormatter(locale, ResolverStyle.SMART, (Chronology) null);
    }

    /* access modifiers changed from: package-private */
    public DateTimeFormatter toFormatter(ResolverStyle resolverStyle, Chronology chronology) {
        return toFormatter(Locale.getDefault(Locale.Category.FORMAT), resolverStyle, chronology);
    }

    private DateTimeFormatter toFormatter(Locale locale, ResolverStyle resolverStyle, Chronology chronology) {
        Objects.requireNonNull(locale, "locale");
        while (this.active.parent != null) {
            optionalEnd();
        }
        return new DateTimeFormatter(new CompositePrinterParser(this.printerParsers, false), locale, DecimalStyle.STANDARD, resolverStyle, (Set<TemporalField>) null, chronology, (ZoneId) null);
    }

    static final class CompositePrinterParser implements DateTimePrinterParser {
        private final boolean optional;
        private final DateTimePrinterParser[] printerParsers;

        CompositePrinterParser(List<DateTimePrinterParser> list, boolean z) {
            this((DateTimePrinterParser[]) list.toArray(new DateTimePrinterParser[list.size()]), z);
        }

        CompositePrinterParser(DateTimePrinterParser[] dateTimePrinterParserArr, boolean z) {
            this.printerParsers = dateTimePrinterParserArr;
            this.optional = z;
        }

        public CompositePrinterParser withOptional(boolean z) {
            if (z == this.optional) {
                return this;
            }
            return new CompositePrinterParser(this.printerParsers, z);
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            int length = sb.length();
            if (this.optional) {
                dateTimePrintContext.startOptional();
            }
            try {
                for (DateTimePrinterParser format : this.printerParsers) {
                    if (!format.format(dateTimePrintContext, sb)) {
                        sb.setLength(length);
                        return true;
                    }
                }
                if (this.optional) {
                    dateTimePrintContext.endOptional();
                }
                return true;
            } finally {
                if (this.optional) {
                    dateTimePrintContext.endOptional();
                }
            }
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (this.optional) {
                dateTimeParseContext.startOptional();
                int i2 = i;
                for (DateTimePrinterParser parse : this.printerParsers) {
                    i2 = parse.parse(dateTimeParseContext, charSequence, i2);
                    if (i2 < 0) {
                        dateTimeParseContext.endOptional(false);
                        return i;
                    }
                }
                dateTimeParseContext.endOptional(true);
                return i2;
            }
            for (DateTimePrinterParser parse2 : this.printerParsers) {
                i = parse2.parse(dateTimeParseContext, charSequence, i);
                if (i < 0) {
                    break;
                }
            }
            return i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.printerParsers != null) {
                sb.append(this.optional ? NavigationBarInflaterView.SIZE_MOD_START : NavigationBarInflaterView.KEY_CODE_START);
                for (DateTimePrinterParser append : this.printerParsers) {
                    sb.append((Object) append);
                }
                sb.append(this.optional ? NavigationBarInflaterView.SIZE_MOD_END : NavigationBarInflaterView.KEY_CODE_END);
            }
            return sb.toString();
        }
    }

    static final class PadPrinterParserDecorator implements DateTimePrinterParser {
        private final char padChar;
        private final int padWidth;
        private final DateTimePrinterParser printerParser;

        PadPrinterParserDecorator(DateTimePrinterParser dateTimePrinterParser, int i, char c) {
            this.printerParser = dateTimePrinterParser;
            this.padWidth = i;
            this.padChar = c;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            int length = sb.length();
            if (!this.printerParser.format(dateTimePrintContext, sb)) {
                return false;
            }
            int length2 = sb.length() - length;
            if (length2 <= this.padWidth) {
                for (int i = 0; i < this.padWidth - length2; i++) {
                    sb.insert(length, this.padChar);
                }
                return true;
            }
            throw new DateTimeException("Cannot print as output of " + length2 + " characters exceeds pad width of " + this.padWidth);
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            boolean isStrict = dateTimeParseContext.isStrict();
            if (i > charSequence.length()) {
                throw new IndexOutOfBoundsException();
            } else if (i == charSequence.length()) {
                return ~i;
            } else {
                int i2 = this.padWidth + i;
                if (i2 > charSequence.length()) {
                    if (isStrict) {
                        return ~i;
                    }
                    i2 = charSequence.length();
                }
                int i3 = i;
                while (i3 < i2 && dateTimeParseContext.charEquals(charSequence.charAt(i3), this.padChar)) {
                    i3++;
                }
                int parse = this.printerParser.parse(dateTimeParseContext, charSequence.subSequence(0, i2), i3);
                return (parse == i2 || !isStrict) ? parse : ~(i + i3);
            }
        }

        public String toString() {
            String str;
            StringBuilder sb = new StringBuilder("Pad(");
            sb.append((Object) this.printerParser);
            sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            sb.append(this.padWidth);
            if (this.padChar == ' ') {
                str = NavigationBarInflaterView.KEY_CODE_END;
            } else {
                str = ",'" + this.padChar + "')";
            }
            sb.append(str);
            return sb.toString();
        }
    }

    enum SettingsParser implements DateTimePrinterParser {
        SENSITIVE,
        INSENSITIVE,
        STRICT,
        LENIENT;

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int ordinal = ordinal();
            if (ordinal == 0) {
                dateTimeParseContext.setCaseSensitive(true);
            } else if (ordinal == 1) {
                dateTimeParseContext.setCaseSensitive(false);
            } else if (ordinal == 2) {
                dateTimeParseContext.setStrict(true);
            } else if (ordinal == 3) {
                dateTimeParseContext.setStrict(false);
            }
            return i;
        }

        public String toString() {
            int ordinal = ordinal();
            if (ordinal == 0) {
                return "ParseCaseSensitive(true)";
            }
            if (ordinal == 1) {
                return "ParseCaseSensitive(false)";
            }
            if (ordinal == 2) {
                return "ParseStrict(true)";
            }
            if (ordinal == 3) {
                return "ParseStrict(false)";
            }
            throw new IllegalStateException("Unreachable");
        }
    }

    static class DefaultValueParser implements DateTimePrinterParser {
        private final TemporalField field;
        private final long value;

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return true;
        }

        DefaultValueParser(TemporalField temporalField, long j) {
            this.field = temporalField;
            this.value = j;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (dateTimeParseContext.getParsed(this.field) == null) {
                dateTimeParseContext.setParsedField(this.field, this.value, i, i);
            }
            return i;
        }
    }

    static final class CharLiteralPrinterParser implements DateTimePrinterParser {
        private final char literal;

        CharLiteralPrinterParser(char c) {
            this.literal = c;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            sb.append(this.literal);
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (i == charSequence.length()) {
                return ~i;
            }
            char charAt = charSequence.charAt(i);
            return (charAt == this.literal || (!dateTimeParseContext.isCaseSensitive() && (Character.toUpperCase(charAt) == Character.toUpperCase(this.literal) || Character.toLowerCase(charAt) == Character.toLowerCase(this.literal)))) ? i + 1 : ~i;
        }

        public String toString() {
            if (this.literal == '\'') {
                return "''";
            }
            return "'" + this.literal + "'";
        }
    }

    static final class StringLiteralPrinterParser implements DateTimePrinterParser {
        private final String literal;

        StringLiteralPrinterParser(String str) {
            this.literal = str;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            sb.append(this.literal);
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            if (i > charSequence.length() || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            String str = this.literal;
            if (!dateTimeParseContext.subSequenceEquals(charSequence, i, str, 0, str.length())) {
                return ~i;
            }
            return i + this.literal.length();
        }

        public String toString() {
            String replace = this.literal.replace((CharSequence) "'", (CharSequence) "''");
            return "'" + replace + "'";
        }
    }

    static class NumberPrinterParser implements DateTimePrinterParser {
        static final long[] EXCEED_POINTS = {0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L};
        final TemporalField field;
        final int maxWidth;
        final int minWidth;
        /* access modifiers changed from: private */
        public final SignStyle signStyle;
        final int subsequentWidth;

        /* access modifiers changed from: package-private */
        public long getValue(DateTimePrintContext dateTimePrintContext, long j) {
            return j;
        }

        NumberPrinterParser(TemporalField temporalField, int i, int i2, SignStyle signStyle2) {
            this.field = temporalField;
            this.minWidth = i;
            this.maxWidth = i2;
            this.signStyle = signStyle2;
            this.subsequentWidth = 0;
        }

        protected NumberPrinterParser(TemporalField temporalField, int i, int i2, SignStyle signStyle2, int i3) {
            this.field = temporalField;
            this.minWidth = i;
            this.maxWidth = i2;
            this.signStyle = signStyle2;
            this.subsequentWidth = i3;
        }

        /* access modifiers changed from: package-private */
        public NumberPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, -1);
        }

        /* access modifiers changed from: package-private */
        public NumberPrinterParser withSubsequentWidth(int i) {
            return new NumberPrinterParser(this.field, this.minWidth, this.maxWidth, this.signStyle, this.subsequentWidth + i);
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            String str;
            Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            long value2 = getValue(dateTimePrintContext, value.longValue());
            DecimalStyle decimalStyle = dateTimePrintContext.getDecimalStyle();
            if (value2 == Long.MIN_VALUE) {
                str = "9223372036854775808";
            } else {
                str = Long.toString(Math.abs(value2));
            }
            if (str.length() <= this.maxWidth) {
                String convertNumberToI18N = decimalStyle.convertNumberToI18N(str);
                if (value2 >= 0) {
                    int i = C28833.$SwitchMap$java$time$format$SignStyle[this.signStyle.ordinal()];
                    if (i == 1) {
                        int i2 = this.minWidth;
                        if (i2 < 19 && value2 >= EXCEED_POINTS[i2]) {
                            sb.append(decimalStyle.getPositiveSign());
                        }
                    } else if (i == 2) {
                        sb.append(decimalStyle.getPositiveSign());
                    }
                } else {
                    int i3 = C28833.$SwitchMap$java$time$format$SignStyle[this.signStyle.ordinal()];
                    if (i3 == 1 || i3 == 2 || i3 == 3) {
                        sb.append(decimalStyle.getNegativeSign());
                    } else if (i3 == 4) {
                        throw new DateTimeException("Field " + this.field + " cannot be printed as the value " + value2 + " cannot be negative according to the SignStyle");
                    }
                }
                for (int i4 = 0; i4 < this.minWidth - convertNumberToI18N.length(); i4++) {
                    sb.append(decimalStyle.getZeroDigit());
                }
                sb.append(convertNumberToI18N);
                return true;
            }
            throw new DateTimeException("Field " + this.field + " cannot be printed as the value " + value2 + " exceeds the maximum print width of " + this.maxWidth);
        }

        /* access modifiers changed from: package-private */
        public boolean isFixedWidth(DateTimeParseContext dateTimeParseContext) {
            int i = this.subsequentWidth;
            return i == -1 || (i > 0 && this.minWidth == this.maxWidth && this.signStyle == SignStyle.NOT_NEGATIVE);
        }

        /* JADX WARNING: Removed duplicated region for block: B:106:0x015e  */
        /* JADX WARNING: Removed duplicated region for block: B:111:0x017b  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int parse(java.time.format.DateTimeParseContext r20, java.lang.CharSequence r21, int r22) {
            /*
                r19 = this;
                r0 = r19
                r1 = r22
                int r2 = r21.length()
                if (r1 != r2) goto L_0x000c
                int r0 = ~r1
                return r0
            L_0x000c:
                char r3 = r21.charAt(r22)
                java.time.format.DecimalStyle r4 = r20.getDecimalStyle()
                char r4 = r4.getPositiveSign()
                r5 = 0
                r6 = 1
                if (r3 != r4) goto L_0x0039
                java.time.format.SignStyle r3 = r0.signStyle
                boolean r4 = r20.isStrict()
                int r7 = r0.minWidth
                int r8 = r0.maxWidth
                if (r7 != r8) goto L_0x002a
                r7 = r6
                goto L_0x002b
            L_0x002a:
                r7 = r5
            L_0x002b:
                boolean r3 = r3.parse(r6, r4, r7)
                if (r3 != 0) goto L_0x0033
                int r0 = ~r1
                return r0
            L_0x0033:
                int r1 = r1 + 1
                r4 = r1
                r1 = r5
                r3 = r6
                goto L_0x0071
            L_0x0039:
                java.time.format.DecimalStyle r4 = r20.getDecimalStyle()
                char r4 = r4.getNegativeSign()
                if (r3 != r4) goto L_0x0060
                java.time.format.SignStyle r3 = r0.signStyle
                boolean r4 = r20.isStrict()
                int r7 = r0.minWidth
                int r8 = r0.maxWidth
                if (r7 != r8) goto L_0x0051
                r7 = r6
                goto L_0x0052
            L_0x0051:
                r7 = r5
            L_0x0052:
                boolean r3 = r3.parse(r5, r4, r7)
                if (r3 != 0) goto L_0x005a
                int r0 = ~r1
                return r0
            L_0x005a:
                int r1 = r1 + 1
                r4 = r1
                r3 = r5
                r1 = r6
                goto L_0x0071
            L_0x0060:
                java.time.format.SignStyle r3 = r0.signStyle
                java.time.format.SignStyle r4 = java.time.format.SignStyle.ALWAYS
                if (r3 != r4) goto L_0x006e
                boolean r3 = r20.isStrict()
                if (r3 == 0) goto L_0x006e
                int r0 = ~r1
                return r0
            L_0x006e:
                r4 = r1
                r1 = r5
                r3 = r1
            L_0x0071:
                boolean r7 = r20.isStrict()
                if (r7 != 0) goto L_0x0080
                boolean r7 = r19.isFixedWidth(r20)
                if (r7 == 0) goto L_0x007e
                goto L_0x0080
            L_0x007e:
                r7 = r6
                goto L_0x0082
            L_0x0080:
                int r7 = r0.minWidth
            L_0x0082:
                int r8 = r4 + r7
                if (r8 <= r2) goto L_0x0088
                int r0 = ~r4
                return r0
            L_0x0088:
                boolean r9 = r20.isStrict()
                if (r9 != 0) goto L_0x0098
                boolean r9 = r19.isFixedWidth(r20)
                if (r9 == 0) goto L_0x0095
                goto L_0x0098
            L_0x0095:
                r9 = 9
                goto L_0x009a
            L_0x0098:
                int r9 = r0.maxWidth
            L_0x009a:
                int r10 = r0.subsequentWidth
                int r10 = java.lang.Math.max((int) r10, (int) r5)
                int r9 = r9 + r10
            L_0x00a1:
                r10 = 2
                r13 = 0
                if (r5 >= r10) goto L_0x010c
                int r9 = r9 + r4
                int r9 = java.lang.Math.min((int) r9, (int) r2)
                r10 = r4
                r14 = 0
            L_0x00ad:
                if (r10 >= r9) goto L_0x00f6
                int r16 = r10 + 1
                r11 = r21
                char r10 = r11.charAt(r10)
                java.time.format.DecimalStyle r12 = r20.getDecimalStyle()
                int r10 = r12.convertToDigit(r10)
                if (r10 >= 0) goto L_0x00c7
                int r10 = r16 + -1
                if (r10 >= r8) goto L_0x00f8
                int r0 = ~r4
                return r0
            L_0x00c7:
                int r12 = r16 - r4
                r6 = 18
                if (r12 <= r6) goto L_0x00e6
                if (r13 != 0) goto L_0x00d3
                java.math.BigInteger r13 = java.math.BigInteger.valueOf((long) r14)
            L_0x00d3:
                java.math.BigInteger r6 = java.math.BigInteger.TEN
                java.math.BigInteger r6 = r13.multiply((java.math.BigInteger) r6)
                long r12 = (long) r10
                java.math.BigInteger r10 = java.math.BigInteger.valueOf((long) r12)
                java.math.BigInteger r13 = r6.add((java.math.BigInteger) r10)
                r6 = r8
                r22 = r9
                goto L_0x00ef
            L_0x00e6:
                r17 = 10
                long r14 = r14 * r17
                r6 = r8
                r22 = r9
                long r8 = (long) r10
                long r14 = r14 + r8
            L_0x00ef:
                r9 = r22
                r8 = r6
                r10 = r16
                r6 = 1
                goto L_0x00ad
            L_0x00f6:
                r11 = r21
            L_0x00f8:
                r6 = r8
                int r8 = r0.subsequentWidth
                if (r8 <= 0) goto L_0x010a
                if (r5 != 0) goto L_0x010a
                int r10 = r10 - r4
                int r10 = r10 - r8
                int r9 = java.lang.Math.max((int) r7, (int) r10)
                int r5 = r5 + 1
                r8 = r6
                r6 = 1
                goto L_0x00a1
            L_0x010a:
                r5 = r10
                goto L_0x010f
            L_0x010c:
                r5 = r4
                r14 = 0
            L_0x010f:
                if (r1 == 0) goto L_0x013d
                if (r13 == 0) goto L_0x012a
                java.math.BigInteger r1 = java.math.BigInteger.ZERO
                boolean r1 = r13.equals(r1)
                if (r1 == 0) goto L_0x0125
                boolean r1 = r20.isStrict()
                if (r1 == 0) goto L_0x0125
                r1 = 1
                int r4 = r4 - r1
                int r0 = ~r4
                return r0
            L_0x0125:
                java.math.BigInteger r13 = r13.negate()
                goto L_0x015b
            L_0x012a:
                r1 = 1
                r2 = 0
                int r2 = (r14 > r2 ? 1 : (r14 == r2 ? 0 : -1))
                if (r2 != 0) goto L_0x013a
                boolean r2 = r20.isStrict()
                if (r2 == 0) goto L_0x013a
                int r4 = r4 - r1
                int r0 = ~r4
                return r0
            L_0x013a:
                long r1 = -r14
                r2 = r1
                goto L_0x015c
            L_0x013d:
                java.time.format.SignStyle r1 = r0.signStyle
                java.time.format.SignStyle r2 = java.time.format.SignStyle.EXCEEDS_PAD
                if (r1 != r2) goto L_0x015b
                boolean r1 = r20.isStrict()
                if (r1 == 0) goto L_0x015b
                int r1 = r5 - r4
                if (r3 == 0) goto L_0x0155
                int r2 = r0.minWidth
                if (r1 > r2) goto L_0x015b
                r1 = 1
                int r4 = r4 - r1
                int r0 = ~r4
                return r0
            L_0x0155:
                int r2 = r0.minWidth
                if (r1 <= r2) goto L_0x015b
                int r0 = ~r4
                return r0
            L_0x015b:
                r2 = r14
            L_0x015c:
                if (r13 == 0) goto L_0x017b
                int r1 = r13.bitLength()
                r2 = 63
                if (r1 <= r2) goto L_0x016e
                java.math.BigInteger r1 = java.math.BigInteger.TEN
                java.math.BigInteger r13 = r13.divide(r1)
                int r5 = r5 + -1
            L_0x016e:
                long r2 = r13.longValue()
                r0 = r19
                r1 = r20
                int r0 = r0.setValue(r1, r2, r4, r5)
                return r0
            L_0x017b:
                r0 = r19
                r1 = r20
                int r0 = r0.setValue(r1, r2, r4, r5)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.NumberPrinterParser.parse(java.time.format.DateTimeParseContext, java.lang.CharSequence, int):int");
        }

        /* access modifiers changed from: package-private */
        public int setValue(DateTimeParseContext dateTimeParseContext, long j, int i, int i2) {
            return dateTimeParseContext.setParsedField(this.field, j, i, i2);
        }

        public String toString() {
            if (this.minWidth == 1 && this.maxWidth == 19 && this.signStyle == SignStyle.NORMAL) {
                return "Value(" + this.field + NavigationBarInflaterView.KEY_CODE_END;
            } else if (this.minWidth == this.maxWidth && this.signStyle == SignStyle.NOT_NEGATIVE) {
                return "Value(" + this.field + NavigationBarInflaterView.BUTTON_SEPARATOR + this.minWidth + NavigationBarInflaterView.KEY_CODE_END;
            } else {
                return "Value(" + this.field + NavigationBarInflaterView.BUTTON_SEPARATOR + this.minWidth + NavigationBarInflaterView.BUTTON_SEPARATOR + this.maxWidth + NavigationBarInflaterView.BUTTON_SEPARATOR + this.signStyle + NavigationBarInflaterView.KEY_CODE_END;
            }
        }
    }

    /* renamed from: java.time.format.DateTimeFormatterBuilder$3 */
    static /* synthetic */ class C28833 {
        static final /* synthetic */ int[] $SwitchMap$java$time$format$SignStyle;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                java.time.format.SignStyle[] r0 = java.time.format.SignStyle.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$time$format$SignStyle = r0
                java.time.format.SignStyle r1 = java.time.format.SignStyle.EXCEEDS_PAD     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$time$format$SignStyle     // Catch:{ NoSuchFieldError -> 0x001d }
                java.time.format.SignStyle r1 = java.time.format.SignStyle.ALWAYS     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$time$format$SignStyle     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.time.format.SignStyle r1 = java.time.format.SignStyle.NORMAL     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$time$format$SignStyle     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.time.format.SignStyle r1 = java.time.format.SignStyle.NOT_NEGATIVE     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.C28833.<clinit>():void");
        }
    }

    static final class ReducedPrinterParser extends NumberPrinterParser {
        static final LocalDate BASE_DATE = LocalDate.m906of(2000, 1, 1);
        private final ChronoLocalDate baseDate;
        private final int baseValue;

        ReducedPrinterParser(TemporalField temporalField, int i, int i2, int i3, ChronoLocalDate chronoLocalDate) {
            this(temporalField, i, i2, i3, chronoLocalDate, 0);
            if (i < 1 || i > 10) {
                throw new IllegalArgumentException("The minWidth must be from 1 to 10 inclusive but was " + i);
            } else if (i2 < 1 || i2 > 10) {
                throw new IllegalArgumentException("The maxWidth must be from 1 to 10 inclusive but was " + i);
            } else if (i2 < i) {
                throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + i2 + " < " + i);
            } else if (chronoLocalDate == null) {
                ValueRange range = temporalField.range();
                long j = (long) i3;
                if (!range.isValidValue(j)) {
                    throw new IllegalArgumentException("The base value must be within the range of the field");
                } else if (j + EXCEED_POINTS[i2] > 2147483647L) {
                    throw new DateTimeException("Unable to add printer-parser as the range exceeds the capacity of an int");
                }
            }
        }

        private ReducedPrinterParser(TemporalField temporalField, int i, int i2, int i3, ChronoLocalDate chronoLocalDate, int i4) {
            super(temporalField, i, i2, SignStyle.NOT_NEGATIVE, i4);
            this.baseValue = i3;
            this.baseDate = chronoLocalDate;
        }

        /* access modifiers changed from: package-private */
        public long getValue(DateTimePrintContext dateTimePrintContext, long j) {
            long abs = Math.abs(j);
            int i = this.baseValue;
            if (this.baseDate != null) {
                i = Chronology.from(dateTimePrintContext.getTemporal()).date(this.baseDate).get(this.field);
            }
            long j2 = (long) i;
            if (j < j2 || j >= j2 + EXCEED_POINTS[this.minWidth]) {
                return abs % EXCEED_POINTS[this.maxWidth];
            }
            return abs % EXCEED_POINTS[this.minWidth];
        }

        /* access modifiers changed from: package-private */
        public int setValue(DateTimeParseContext dateTimeParseContext, long j, int i, int i2) {
            int i3 = this.baseValue;
            if (this.baseDate != null) {
                i3 = dateTimeParseContext.getEffectiveChronology().date(this.baseDate).get(this.field);
                dateTimeParseContext.addChronoChangedListener(new C2886xdc213142(this, dateTimeParseContext, j, i, i2));
            }
            if (i2 - i == this.minWidth && j >= 0) {
                long j2 = EXCEED_POINTS[this.minWidth];
                long j3 = (long) i3;
                long j4 = j3 - (j3 % j2);
                j = i3 > 0 ? j4 + j : j4 - j;
                if (j < j3) {
                    j += j2;
                }
            }
            return dateTimeParseContext.setParsedField(this.field, j, i, i2);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setValue$0$java-time-format-DateTimeFormatterBuilder$ReducedPrinterParser */
        public /* synthetic */ void mo43515xdf3a601e(DateTimeParseContext dateTimeParseContext, long j, int i, int i2, Chronology chronology) {
            setValue(dateTimeParseContext, j, i, i2);
        }

        /* access modifiers changed from: package-private */
        public ReducedPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, -1);
        }

        /* access modifiers changed from: package-private */
        public ReducedPrinterParser withSubsequentWidth(int i) {
            return new ReducedPrinterParser(this.field, this.minWidth, this.maxWidth, this.baseValue, this.baseDate, this.subsequentWidth + i);
        }

        /* access modifiers changed from: package-private */
        public boolean isFixedWidth(DateTimeParseContext dateTimeParseContext) {
            if (!dateTimeParseContext.isStrict()) {
                return false;
            }
            return super.isFixedWidth(dateTimeParseContext);
        }

        public String toString() {
            return "ReducedValue(" + this.field + NavigationBarInflaterView.BUTTON_SEPARATOR + this.minWidth + NavigationBarInflaterView.BUTTON_SEPARATOR + this.maxWidth + NavigationBarInflaterView.BUTTON_SEPARATOR + Objects.requireNonNullElse(this.baseDate, Integer.valueOf(this.baseValue)) + NavigationBarInflaterView.KEY_CODE_END;
        }
    }

    static final class FractionPrinterParser extends NumberPrinterParser {
        private final boolean decimalPoint;

        FractionPrinterParser(TemporalField temporalField, int i, int i2, boolean z) {
            this(temporalField, i, i2, z, 0);
            Objects.requireNonNull(temporalField, "field");
            if (!temporalField.range().isFixed()) {
                throw new IllegalArgumentException("Field must have a fixed set of values: " + temporalField);
            } else if (i < 0 || i > 9) {
                throw new IllegalArgumentException("Minimum width must be from 0 to 9 inclusive but was " + i);
            } else if (i2 < 1 || i2 > 9) {
                throw new IllegalArgumentException("Maximum width must be from 1 to 9 inclusive but was " + i2);
            } else if (i2 < i) {
                throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + i2 + " < " + i);
            }
        }

        FractionPrinterParser(TemporalField temporalField, int i, int i2, boolean z, int i3) {
            super(temporalField, i, i2, SignStyle.NOT_NEGATIVE, i3);
            this.decimalPoint = z;
        }

        /* access modifiers changed from: package-private */
        public FractionPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new FractionPrinterParser(this.field, this.minWidth, this.maxWidth, this.decimalPoint, -1);
        }

        /* access modifiers changed from: package-private */
        public FractionPrinterParser withSubsequentWidth(int i) {
            return new FractionPrinterParser(this.field, this.minWidth, this.maxWidth, this.decimalPoint, this.subsequentWidth + i);
        }

        /* access modifiers changed from: package-private */
        public boolean isFixedWidth(DateTimeParseContext dateTimeParseContext) {
            return dateTimeParseContext.isStrict() && this.minWidth == this.maxWidth && !this.decimalPoint;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            DecimalStyle decimalStyle = dateTimePrintContext.getDecimalStyle();
            BigDecimal convertToFraction = convertToFraction(value.longValue());
            if (convertToFraction.scale() != 0) {
                String convertNumberToI18N = decimalStyle.convertNumberToI18N(convertToFraction.setScale(Math.min(Math.max(convertToFraction.scale(), this.minWidth), this.maxWidth), RoundingMode.FLOOR).toPlainString().substring(2));
                if (this.decimalPoint) {
                    sb.append(decimalStyle.getDecimalSeparator());
                }
                sb.append(convertNumberToI18N);
                return true;
            } else if (this.minWidth <= 0) {
                return true;
            } else {
                if (this.decimalPoint) {
                    sb.append(decimalStyle.getDecimalSeparator());
                }
                for (int i = 0; i < this.minWidth; i++) {
                    sb.append(decimalStyle.getZeroDigit());
                }
                return true;
            }
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int i2;
            int i3 = 0;
            int i4 = (dateTimeParseContext.isStrict() || isFixedWidth(dateTimeParseContext)) ? this.minWidth : 0;
            int i5 = (dateTimeParseContext.isStrict() || isFixedWidth(dateTimeParseContext)) ? this.maxWidth : 9;
            int length = charSequence.length();
            if (i == length) {
                return i4 > 0 ? ~i : i;
            }
            if (this.decimalPoint) {
                if (charSequence.charAt(i) != dateTimeParseContext.getDecimalStyle().getDecimalSeparator()) {
                    return i4 > 0 ? ~i : i;
                }
                i++;
            }
            int i6 = i;
            int i7 = i4 + i6;
            if (i7 > length) {
                return ~i6;
            }
            int min = Math.min(i5 + i6, length);
            int i8 = i6;
            while (true) {
                if (i8 >= min) {
                    i2 = i8;
                    break;
                }
                int i9 = i8 + 1;
                int convertToDigit = dateTimeParseContext.getDecimalStyle().convertToDigit(charSequence.charAt(i8));
                if (convertToDigit >= 0) {
                    i3 = (i3 * 10) + convertToDigit;
                    i8 = i9;
                } else if (i9 < i7) {
                    return ~i6;
                } else {
                    i2 = i9 - 1;
                }
            }
            return dateTimeParseContext.setParsedField(this.field, convertFromFraction(new BigDecimal(i3).movePointLeft(i2 - i6)), i6, i2);
        }

        private BigDecimal convertToFraction(long j) {
            ValueRange range = this.field.range();
            range.checkValidValue(j, this.field);
            BigDecimal valueOf = BigDecimal.valueOf(range.getMinimum());
            BigDecimal divide = BigDecimal.valueOf(j).subtract(valueOf).divide(BigDecimal.valueOf(range.getMaximum()).subtract(valueOf).add(BigDecimal.ONE), 9, RoundingMode.FLOOR);
            return divide.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : divide.stripTrailingZeros();
        }

        private long convertFromFraction(BigDecimal bigDecimal) {
            ValueRange range = this.field.range();
            BigDecimal valueOf = BigDecimal.valueOf(range.getMinimum());
            return bigDecimal.multiply(BigDecimal.valueOf(range.getMaximum()).subtract(valueOf).add(BigDecimal.ONE)).setScale(0, RoundingMode.FLOOR).add(valueOf).longValueExact();
        }

        public String toString() {
            String str = this.decimalPoint ? ",DecimalPoint" : "";
            return "Fraction(" + this.field + NavigationBarInflaterView.BUTTON_SEPARATOR + this.minWidth + NavigationBarInflaterView.BUTTON_SEPARATOR + this.maxWidth + str + NavigationBarInflaterView.KEY_CODE_END;
        }
    }

    static final class TextPrinterParser implements DateTimePrinterParser {
        private final TemporalField field;
        private volatile NumberPrinterParser numberPrinterParser;
        private final DateTimeTextProvider provider;
        private final TextStyle textStyle;

        TextPrinterParser(TemporalField temporalField, TextStyle textStyle2, DateTimeTextProvider dateTimeTextProvider) {
            this.field = temporalField;
            this.textStyle = textStyle2;
            this.provider = dateTimeTextProvider;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            String str;
            Long value = dateTimePrintContext.getValue(this.field);
            if (value == null) {
                return false;
            }
            Chronology chronology = (Chronology) dateTimePrintContext.getTemporal().query(TemporalQueries.chronology());
            if (chronology == null || chronology == IsoChronology.INSTANCE) {
                str = this.provider.getText(this.field, value.longValue(), this.textStyle, dateTimePrintContext.getLocale());
            } else {
                str = this.provider.getText(chronology, this.field, value.longValue(), this.textStyle, dateTimePrintContext.getLocale());
            }
            if (str == null) {
                return numberPrinterParser().format(dateTimePrintContext, sb);
            }
            sb.append(str);
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            Iterator<Map.Entry<String, Long>> it;
            int length = charSequence.length();
            if (i < 0 || i > length) {
                throw new IndexOutOfBoundsException();
            }
            TextStyle textStyle2 = dateTimeParseContext.isStrict() ? this.textStyle : null;
            Chronology effectiveChronology = dateTimeParseContext.getEffectiveChronology();
            if (effectiveChronology == null || effectiveChronology == IsoChronology.INSTANCE) {
                it = this.provider.getTextIterator(this.field, textStyle2, dateTimeParseContext.getLocale());
            } else {
                it = this.provider.getTextIterator(effectiveChronology, this.field, textStyle2, dateTimeParseContext.getLocale());
            }
            Iterator<Map.Entry<String, Long>> it2 = it;
            if (it2 != null) {
                while (it2.hasNext()) {
                    Map.Entry next = it2.next();
                    String str = (String) next.getKey();
                    if (dateTimeParseContext.subSequenceEquals(str, 0, charSequence, i, str.length())) {
                        return dateTimeParseContext.setParsedField(this.field, ((Long) next.getValue()).longValue(), i, i + str.length());
                    }
                }
                if (this.field == ChronoField.ERA && !dateTimeParseContext.isStrict()) {
                    for (Era next2 : effectiveChronology.eras()) {
                        String obj = next2.toString();
                        if (dateTimeParseContext.subSequenceEquals(obj, 0, charSequence, i, obj.length())) {
                            return dateTimeParseContext.setParsedField(this.field, (long) next2.getValue(), i, i + obj.length());
                        }
                    }
                }
                if (dateTimeParseContext.isStrict()) {
                    return ~i;
                }
            }
            return numberPrinterParser().parse(dateTimeParseContext, charSequence, i);
        }

        private NumberPrinterParser numberPrinterParser() {
            if (this.numberPrinterParser == null) {
                this.numberPrinterParser = new NumberPrinterParser(this.field, 1, 19, SignStyle.NORMAL);
            }
            return this.numberPrinterParser;
        }

        public String toString() {
            if (this.textStyle == TextStyle.FULL) {
                return "Text(" + this.field + NavigationBarInflaterView.KEY_CODE_END;
            }
            return "Text(" + this.field + NavigationBarInflaterView.BUTTON_SEPARATOR + this.textStyle + NavigationBarInflaterView.KEY_CODE_END;
        }
    }

    static final class InstantPrinterParser implements DateTimePrinterParser {
        private static final long SECONDS_0000_TO_1970 = 62167219200L;
        private static final long SECONDS_PER_10000_YEARS = 315569520000L;
        private final int fractionalDigits;

        public String toString() {
            return "Instant()";
        }

        InstantPrinterParser(int i) {
            this.fractionalDigits = i;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            StringBuilder sb2 = sb;
            Long value = dateTimePrintContext.getValue((TemporalField) ChronoField.INSTANT_SECONDS);
            Long valueOf = dateTimePrintContext.getTemporal().isSupported(ChronoField.NANO_OF_SECOND) ? Long.valueOf(dateTimePrintContext.getTemporal().getLong(ChronoField.NANO_OF_SECOND)) : null;
            int i = 0;
            if (value == null) {
                return false;
            }
            long longValue = value.longValue();
            int checkValidIntValue = ChronoField.NANO_OF_SECOND.checkValidIntValue(valueOf != null ? valueOf.longValue() : 0);
            if (longValue >= -62167219200L) {
                long j = (longValue - SECONDS_PER_10000_YEARS) + SECONDS_0000_TO_1970;
                long floorDiv = 1 + Math.floorDiv(j, (long) SECONDS_PER_10000_YEARS);
                LocalDateTime ofEpochSecond = LocalDateTime.ofEpochSecond(Math.floorMod(j, (long) SECONDS_PER_10000_YEARS) - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC);
                if (floorDiv > 0) {
                    sb2.append('+');
                    sb2.append(floorDiv);
                }
                sb2.append((Object) ofEpochSecond);
                if (ofEpochSecond.getSecond() == 0) {
                    sb2.append(":00");
                }
            } else {
                long j2 = longValue + SECONDS_0000_TO_1970;
                long j3 = j2 / SECONDS_PER_10000_YEARS;
                long j4 = j2 % SECONDS_PER_10000_YEARS;
                LocalDateTime ofEpochSecond2 = LocalDateTime.ofEpochSecond(j4 - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC);
                int length = sb.length();
                sb2.append((Object) ofEpochSecond2);
                if (ofEpochSecond2.getSecond() == 0) {
                    sb2.append(":00");
                }
                if (j3 < 0) {
                    if (ofEpochSecond2.getYear() == -10000) {
                        sb2.replace(length, length + 2, Long.toString(j3 - 1));
                    } else if (j4 == 0) {
                        sb2.insert(length, j3);
                    } else {
                        sb2.insert(length + 1, Math.abs(j3));
                    }
                }
            }
            int i2 = this.fractionalDigits;
            if ((i2 < 0 && checkValidIntValue > 0) || i2 > 0) {
                sb2.append('.');
                int i3 = 100000000;
                while (true) {
                    int i4 = this.fractionalDigits;
                    if ((i4 != -1 || checkValidIntValue <= 0) && ((i4 != -2 || (checkValidIntValue <= 0 && i % 3 == 0)) && i >= i4)) {
                        break;
                    }
                    int i5 = checkValidIntValue / i3;
                    sb2.append((char) (i5 + 48));
                    checkValidIntValue -= i5 * i3;
                    i3 /= 10;
                    i++;
                }
            }
            sb2.append('Z');
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int i2;
            int i3;
            int i4 = i;
            int i5 = this.fractionalDigits;
            int i6 = 0;
            int i7 = i5 < 0 ? 0 : i5;
            if (i5 < 0) {
                i5 = 9;
            }
            CompositePrinterParser printerParser = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR).appendValue(ChronoField.MINUTE_OF_HOUR, 2).appendLiteral((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR).appendValue(ChronoField.SECOND_OF_MINUTE, 2).appendFraction(ChronoField.NANO_OF_SECOND, i7, i5, true).appendLiteral('Z').toFormatter().toPrinterParser(false);
            DateTimeParseContext copy = dateTimeParseContext.copy();
            int parse = printerParser.parse(copy, charSequence, i4);
            if (parse < 0) {
                return parse;
            }
            long longValue = copy.getParsed(ChronoField.YEAR).longValue();
            int intValue = copy.getParsed(ChronoField.MONTH_OF_YEAR).intValue();
            int intValue2 = copy.getParsed(ChronoField.DAY_OF_MONTH).intValue();
            int intValue3 = copy.getParsed(ChronoField.HOUR_OF_DAY).intValue();
            int intValue4 = copy.getParsed(ChronoField.MINUTE_OF_HOUR).intValue();
            Long parsed = copy.getParsed(ChronoField.SECOND_OF_MINUTE);
            Long parsed2 = copy.getParsed(ChronoField.NANO_OF_SECOND);
            int intValue5 = parsed != null ? parsed.intValue() : 0;
            int intValue6 = parsed2 != null ? parsed2.intValue() : 0;
            if (intValue3 == 24 && intValue4 == 0 && intValue5 == 0 && intValue6 == 0) {
                i3 = 0;
                i6 = 1;
            } else if (intValue3 == 23 && intValue4 == 59 && intValue5 == 60) {
                dateTimeParseContext.setParsedLeapSecond();
                i3 = intValue3;
                i2 = 59;
                DateTimeParseContext dateTimeParseContext2 = dateTimeParseContext;
                int i8 = i;
                return dateTimeParseContext2.setParsedField(ChronoField.NANO_OF_SECOND, (long) intValue6, i8, dateTimeParseContext2.setParsedField(ChronoField.INSTANT_SECONDS, Math.multiplyExact(longValue / 10000, (long) SECONDS_PER_10000_YEARS) + LocalDateTime.m910of(((int) longValue) % 10000, intValue, intValue2, i3, intValue4, i2, 0).plusDays((long) i6).toEpochSecond(ZoneOffset.UTC), i8, parse));
            } else {
                i3 = intValue3;
            }
            i2 = intValue5;
            try {
                DateTimeParseContext dateTimeParseContext22 = dateTimeParseContext;
                int i82 = i;
                return dateTimeParseContext22.setParsedField(ChronoField.NANO_OF_SECOND, (long) intValue6, i82, dateTimeParseContext22.setParsedField(ChronoField.INSTANT_SECONDS, Math.multiplyExact(longValue / 10000, (long) SECONDS_PER_10000_YEARS) + LocalDateTime.m910of(((int) longValue) % 10000, intValue, intValue2, i3, intValue4, i2, 0).plusDays((long) i6).toEpochSecond(ZoneOffset.UTC), i82, parse));
            } catch (RuntimeException unused) {
                return ~i4;
            }
        }
    }

    static final class OffsetIdPrinterParser implements DateTimePrinterParser {
        static final OffsetIdPrinterParser INSTANCE_ID_Z = new OffsetIdPrinterParser("+HH:MM:ss", "Z");
        static final OffsetIdPrinterParser INSTANCE_ID_ZERO = new OffsetIdPrinterParser("+HH:MM:ss", "0");
        static final String[] PATTERNS = {"+HH", "+HHmm", "+HH:mm", "+HHMM", "+HH:MM", "+HHMMss", "+HH:MM:ss", "+HHMMSS", "+HH:MM:SS", "+HHmmss", "+HH:mm:ss", "+H", "+Hmm", "+H:mm", "+HMM", "+H:MM", "+HMMss", "+H:MM:ss", "+HMMSS", "+H:MM:SS", "+Hmmss", "+H:mm:ss"};
        private final String noOffsetText;
        private final int style;
        private final int type;

        OffsetIdPrinterParser(String str, String str2) {
            Objects.requireNonNull(str, "pattern");
            Objects.requireNonNull(str2, "noOffsetText");
            int checkPattern = checkPattern(str);
            this.type = checkPattern;
            this.style = checkPattern % 11;
            this.noOffsetText = str2;
        }

        private int checkPattern(String str) {
            int i = 0;
            while (true) {
                String[] strArr = PATTERNS;
                if (i >= strArr.length) {
                    throw new IllegalArgumentException("Invalid zone offset pattern: " + str);
                } else if (strArr[i].equals(str)) {
                    return i;
                } else {
                    i++;
                }
            }
        }

        private boolean isPaddedHour() {
            return this.type < 11;
        }

        private boolean isColon() {
            int i = this.style;
            return i > 0 && i % 2 == 0;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue((TemporalField) ChronoField.OFFSET_SECONDS);
            if (value == null) {
                return false;
            }
            int intExact = Math.toIntExact(value.longValue());
            if (intExact == 0) {
                sb.append(this.noOffsetText);
            } else {
                int abs = Math.abs((intExact / 3600) % 100);
                int abs2 = Math.abs((intExact / 60) % 60);
                int abs3 = Math.abs(intExact % 60);
                int length = sb.length();
                sb.append(intExact < 0 ? LanguageTag.SEP : "+");
                if (isPaddedHour() || abs >= 10) {
                    formatZeroPad(false, abs, sb);
                } else {
                    sb.append((char) (abs + 48));
                }
                int i = this.style;
                if ((i >= 3 && i <= 8) || ((i >= 9 && abs3 > 0) || (i >= 1 && abs2 > 0))) {
                    formatZeroPad(isColon(), abs2, sb);
                    abs += abs2;
                    int i2 = this.style;
                    if (i2 == 7 || i2 == 8 || (i2 >= 5 && abs3 > 0)) {
                        formatZeroPad(isColon(), abs3, sb);
                        abs += abs3;
                    }
                }
                if (abs == 0) {
                    sb.setLength(length);
                    sb.append(this.noOffsetText);
                }
            }
            return true;
        }

        private void formatZeroPad(boolean z, int i, StringBuilder sb) {
            sb.append(z ? ":" : "");
            sb.append((char) ((i / 10) + 48));
            sb.append((char) ((i % 10) + 48));
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int i2;
            int i3;
            int i4;
            int i5;
            CharSequence charSequence2 = charSequence;
            int i6 = i;
            int length = charSequence.length();
            int length2 = this.noOffsetText.length();
            if (length2 == 0) {
                if (i6 == length) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0, i, i);
                }
            } else if (i6 == length) {
                return ~i6;
            } else {
                if (dateTimeParseContext.subSequenceEquals(charSequence, i, this.noOffsetText, 0, length2)) {
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0, i, i6 + length2);
                }
            }
            char charAt = charSequence.charAt(i);
            if (charAt == '+' || charAt == '-') {
                int i7 = charAt == '-' ? -1 : 1;
                boolean isColon = isColon();
                boolean isPaddedHour = isPaddedHour();
                int[] iArr = new int[4];
                iArr[0] = i6 + 1;
                int i8 = this.type;
                if (!dateTimeParseContext.isStrict()) {
                    if (isPaddedHour) {
                        if (isColon || (i8 == 0 && length > (i5 = i6 + 3) && charSequence2.charAt(i5) == ':')) {
                            i8 = 10;
                        } else {
                            i8 = 9;
                        }
                    } else if (isColon || (i8 == 11 && length > (i4 = i6 + 3) && (charSequence2.charAt(i6 + 2) == ':' || charSequence2.charAt(i4) == ':'))) {
                        i8 = 21;
                    } else {
                        i8 = 20;
                    }
                    isColon = true;
                }
                switch (i8) {
                    case 0:
                    case 11:
                        parseHour(charSequence2, isPaddedHour, iArr);
                        break;
                    case 1:
                    case 2:
                    case 13:
                        parseHour(charSequence2, isPaddedHour, iArr);
                        parseMinute(charSequence2, isColon, false, iArr);
                        break;
                    case 3:
                    case 4:
                    case 15:
                        parseHour(charSequence2, isPaddedHour, iArr);
                        parseMinute(charSequence2, isColon, true, iArr);
                        break;
                    case 5:
                    case 6:
                    case 17:
                        parseHour(charSequence2, isPaddedHour, iArr);
                        parseMinute(charSequence2, isColon, true, iArr);
                        parseSecond(charSequence2, isColon, false, iArr);
                        break;
                    case 7:
                    case 8:
                    case 19:
                        parseHour(charSequence2, isPaddedHour, iArr);
                        parseMinute(charSequence2, isColon, true, iArr);
                        parseSecond(charSequence2, isColon, true, iArr);
                        break;
                    case 9:
                    case 10:
                    case 21:
                        parseHour(charSequence2, isPaddedHour, iArr);
                        parseOptionalMinuteSecond(charSequence2, isColon, iArr);
                        break;
                    case 12:
                        parseVariableWidthDigits(charSequence2, 1, 4, iArr);
                        break;
                    case 14:
                        parseVariableWidthDigits(charSequence2, 3, 4, iArr);
                        break;
                    case 16:
                        parseVariableWidthDigits(charSequence2, 3, 6, iArr);
                        break;
                    case 18:
                        parseVariableWidthDigits(charSequence2, 5, 6, iArr);
                        break;
                    case 20:
                        parseVariableWidthDigits(charSequence2, 1, 6, iArr);
                        break;
                }
                if (iArr[0] > 0) {
                    int i9 = iArr[1];
                    if (i9 > 23 || (i2 = iArr[2]) > 59 || (i3 = iArr[3]) > 59) {
                        throw new DateTimeException("Value out of range: Hour[0-23], Minute[0-59], Second[0-59]");
                    }
                    return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, ((long) i7) * ((((long) i9) * 3600) + (((long) i2) * 60) + ((long) i3)), i, iArr[0]);
                }
            }
            if (length2 != 0) {
                return ~i6;
            }
            return dateTimeParseContext.setParsedField(ChronoField.OFFSET_SECONDS, 0, i, i);
        }

        private void parseHour(CharSequence charSequence, boolean z, int[] iArr) {
            if (!z) {
                parseVariableWidthDigits(charSequence, 1, 2, iArr);
            } else if (!parseDigits(charSequence, false, 1, iArr)) {
                iArr[0] = ~iArr[0];
            }
        }

        private void parseMinute(CharSequence charSequence, boolean z, boolean z2, int[] iArr) {
            if (!parseDigits(charSequence, z, 2, iArr) && z2) {
                iArr[0] = ~iArr[0];
            }
        }

        private void parseSecond(CharSequence charSequence, boolean z, boolean z2, int[] iArr) {
            if (!parseDigits(charSequence, z, 3, iArr) && z2) {
                iArr[0] = ~iArr[0];
            }
        }

        private void parseOptionalMinuteSecond(CharSequence charSequence, boolean z, int[] iArr) {
            if (parseDigits(charSequence, z, 2, iArr)) {
                parseDigits(charSequence, z, 3, iArr);
            }
        }

        private boolean parseDigits(CharSequence charSequence, boolean z, int i, int[] iArr) {
            int i2;
            int i3 = iArr[0];
            if (i3 < 0) {
                return true;
            }
            if (z && i != 1) {
                int i4 = i3 + 1;
                if (i4 > charSequence.length() || charSequence.charAt(i3) != ':') {
                    return false;
                }
                i3 = i4;
            }
            if (i3 + 2 > charSequence.length()) {
                return false;
            }
            int i5 = i3 + 1;
            char charAt = charSequence.charAt(i3);
            int i6 = i5 + 1;
            char charAt2 = charSequence.charAt(i5);
            if (charAt < '0' || charAt > '9' || charAt2 < '0' || charAt2 > '9' || (i2 = ((charAt - '0') * 10) + (charAt2 - '0')) < 0 || i2 > 59) {
                return false;
            }
            iArr[i] = i2;
            iArr[0] = i6;
            return true;
        }

        private void parseVariableWidthDigits(CharSequence charSequence, int i, int i2, int[] iArr) {
            int i3;
            int i4 = iArr[0];
            char[] cArr = new char[i2];
            int i5 = 0;
            int i6 = 0;
            while (true) {
                if (i5 >= i2 || (i3 = i4 + 1) > charSequence.length()) {
                    break;
                }
                char charAt = charSequence.charAt(i4);
                if (charAt < '0' || charAt > '9') {
                    i4 = i3 - 1;
                } else {
                    cArr[i5] = charAt;
                    i6++;
                    i5++;
                    i4 = i3;
                }
            }
            i4 = i3 - 1;
            if (i6 < i) {
                iArr[0] = ~iArr[0];
                return;
            }
            switch (i6) {
                case 1:
                    iArr[1] = cArr[0] - '0';
                    break;
                case 2:
                    iArr[1] = ((cArr[0] - '0') * 10) + (cArr[1] - '0');
                    break;
                case 3:
                    iArr[1] = cArr[0] - '0';
                    iArr[2] = ((cArr[1] - '0') * 10) + (cArr[2] - '0');
                    break;
                case 4:
                    iArr[1] = ((cArr[0] - '0') * 10) + (cArr[1] - '0');
                    iArr[2] = ((cArr[2] - '0') * 10) + (cArr[3] - '0');
                    break;
                case 5:
                    iArr[1] = cArr[0] - '0';
                    iArr[2] = ((cArr[1] - '0') * 10) + (cArr[2] - '0');
                    iArr[3] = ((cArr[3] - '0') * 10) + (cArr[4] - '0');
                    break;
                case 6:
                    iArr[1] = ((cArr[0] - '0') * 10) + (cArr[1] - '0');
                    iArr[2] = ((cArr[2] - '0') * 10) + (cArr[3] - '0');
                    iArr[3] = ((cArr[4] - '0') * 10) + (cArr[5] - '0');
                    break;
            }
            iArr[0] = i4;
        }

        public String toString() {
            String replace = this.noOffsetText.replace((CharSequence) "'", (CharSequence) "''");
            return "Offset(" + PATTERNS[this.type] + ",'" + replace + "')";
        }
    }

    static final class LocalizedOffsetIdPrinterParser implements DateTimePrinterParser {
        private final TextStyle style;

        LocalizedOffsetIdPrinterParser(TextStyle textStyle) {
            this.style = textStyle;
        }

        private static StringBuilder appendHMS(StringBuilder sb, int i) {
            sb.append((char) ((i / 10) + 48));
            sb.append((char) ((i % 10) + 48));
            return sb;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Long value = dateTimePrintContext.getValue((TemporalField) ChronoField.OFFSET_SECONDS);
            if (value == null) {
                return false;
            }
            sb.append("GMT");
            int intExact = Math.toIntExact(value.longValue());
            if (intExact == 0) {
                return true;
            }
            int abs = Math.abs((intExact / 3600) % 100);
            int abs2 = Math.abs((intExact / 60) % 60);
            int abs3 = Math.abs(intExact % 60);
            sb.append(intExact < 0 ? LanguageTag.SEP : "+");
            if (this.style == TextStyle.FULL) {
                appendHMS(sb, abs);
                sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                appendHMS(sb, abs2);
                if (abs3 == 0) {
                    return true;
                }
                sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
                appendHMS(sb, abs3);
                return true;
            }
            if (abs >= 10) {
                sb.append((char) ((abs / 10) + 48));
            }
            sb.append((char) ((abs % 10) + 48));
            if (abs2 == 0 && abs3 == 0) {
                return true;
            }
            sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            appendHMS(sb, abs2);
            if (abs3 == 0) {
                return true;
            }
            sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            appendHMS(sb, abs3);
            return true;
        }

        /* access modifiers changed from: package-private */
        public int getDigit(CharSequence charSequence, int i) {
            char charAt = charSequence.charAt(i);
            if (charAt < '0' || charAt > '9') {
                return -1;
            }
            return charAt - '0';
        }

        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0082, code lost:
            if (r11 >= 0) goto L_0x00e0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x00de, code lost:
            if (r11 >= 0) goto L_0x00e0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x00e0, code lost:
            r4 = (r0 * 10) + r11;
            r7 = r7 + 3;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int parse(java.time.format.DateTimeParseContext r12, java.lang.CharSequence r13, int r14) {
            /*
                r11 = this;
                int r0 = r13.length()
                java.lang.String r4 = "GMT"
                r5 = 0
                r6 = 3
                r1 = r12
                r2 = r13
                r3 = r14
                boolean r1 = r1.subSequenceEquals(r2, r3, r4, r5, r6)
                if (r1 != 0) goto L_0x0013
                int r11 = ~r14
                return r11
            L_0x0013:
                int r5 = r14 + 3
                if (r5 != r0) goto L_0x0022
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.OFFSET_SECONDS
                r2 = 0
                r0 = r12
                r4 = r14
                int r11 = r0.setParsedField(r1, r2, r4, r5)
                return r11
            L_0x0022:
                char r1 = r13.charAt(r5)
                r2 = 43
                r3 = 1
                if (r1 != r2) goto L_0x002d
                r1 = r3
                goto L_0x0032
            L_0x002d:
                r2 = 45
                if (r1 != r2) goto L_0x0100
                r1 = -1
            L_0x0032:
                int r5 = r5 + r3
                java.time.format.TextStyle r2 = r11.style
                java.time.format.TextStyle r3 = java.time.format.TextStyle.FULL
                r4 = 0
                r6 = 58
                if (r2 != r3) goto L_0x008c
                int r2 = r5 + 1
                int r3 = r11.getDigit(r13, r5)
                int r5 = r2 + 1
                int r2 = r11.getDigit(r13, r2)
                if (r3 < 0) goto L_0x008a
                if (r2 < 0) goto L_0x008a
                int r7 = r5 + 1
                char r5 = r13.charAt(r5)
                if (r5 == r6) goto L_0x0055
                goto L_0x008a
            L_0x0055:
                int r3 = r3 * 10
                int r3 = r3 + r2
                int r2 = r7 + 1
                int r5 = r11.getDigit(r13, r7)
                int r7 = r2 + 1
                int r2 = r11.getDigit(r13, r2)
                if (r5 < 0) goto L_0x0088
                if (r2 >= 0) goto L_0x0069
                goto L_0x0088
            L_0x0069:
                int r5 = r5 * 10
                int r5 = r5 + r2
                int r2 = r7 + 2
                if (r2 >= r0) goto L_0x0085
                char r0 = r13.charAt(r7)
                if (r0 != r6) goto L_0x0085
                int r0 = r7 + 1
                int r0 = r11.getDigit(r13, r0)
                int r11 = r11.getDigit(r13, r2)
                if (r0 < 0) goto L_0x0085
                if (r11 < 0) goto L_0x0085
                goto L_0x00e0
            L_0x0085:
                r11 = r4
                r4 = r5
                goto L_0x00e8
            L_0x0088:
                int r11 = ~r14
                return r11
            L_0x008a:
                int r11 = ~r14
                return r11
            L_0x008c:
                int r7 = r5 + 1
                int r3 = r11.getDigit(r13, r5)
                if (r3 >= 0) goto L_0x0096
                int r11 = ~r14
                return r11
            L_0x0096:
                if (r7 >= r0) goto L_0x00e7
                int r2 = r11.getDigit(r13, r7)
                if (r2 < 0) goto L_0x00a3
                int r3 = r3 * 10
                int r3 = r3 + r2
                int r7 = r7 + 1
            L_0x00a3:
                int r2 = r7 + 2
                if (r2 >= r0) goto L_0x00e7
                char r5 = r13.charAt(r7)
                if (r5 != r6) goto L_0x00e7
                if (r2 >= r0) goto L_0x00e7
                char r5 = r13.charAt(r7)
                if (r5 != r6) goto L_0x00e7
                int r5 = r7 + 1
                int r5 = r11.getDigit(r13, r5)
                int r2 = r11.getDigit(r13, r2)
                if (r5 < 0) goto L_0x00e7
                if (r2 < 0) goto L_0x00e7
                int r5 = r5 * 10
                int r5 = r5 + r2
                int r7 = r7 + 3
                int r2 = r7 + 2
                if (r2 >= r0) goto L_0x0085
                char r0 = r13.charAt(r7)
                if (r0 != r6) goto L_0x0085
                int r0 = r7 + 1
                int r0 = r11.getDigit(r13, r0)
                int r11 = r11.getDigit(r13, r2)
                if (r0 < 0) goto L_0x0085
                if (r11 < 0) goto L_0x0085
            L_0x00e0:
                int r0 = r0 * 10
                int r4 = r0 + r11
                int r7 = r7 + 3
                goto L_0x0085
            L_0x00e7:
                r11 = r4
            L_0x00e8:
                r10 = r7
                long r0 = (long) r1
                long r2 = (long) r3
                r5 = 3600(0xe10, double:1.7786E-320)
                long r2 = r2 * r5
                long r4 = (long) r4
                r6 = 60
                long r4 = r4 * r6
                long r2 = r2 + r4
                long r4 = (long) r11
                long r2 = r2 + r4
                long r7 = r0 * r2
                java.time.temporal.ChronoField r6 = java.time.temporal.ChronoField.OFFSET_SECONDS
                r5 = r12
                r9 = r14
                int r11 = r5.setParsedField(r6, r7, r9, r10)
                return r11
            L_0x0100:
                java.time.temporal.ChronoField r1 = java.time.temporal.ChronoField.OFFSET_SECONDS
                r2 = 0
                r0 = r12
                r4 = r14
                int r11 = r0.setParsedField(r1, r2, r4, r5)
                return r11
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.LocalizedOffsetIdPrinterParser.parse(java.time.format.DateTimeParseContext, java.lang.CharSequence, int):int");
        }

        public String toString() {
            return "LocalizedOffset(" + this.style + NavigationBarInflaterView.KEY_CODE_END;
        }
    }

    static final class ZoneTextPrinterParser extends ZoneIdPrinterParser {
        private static final int DST = 1;
        private static final TimeZoneNames.NameType[] FULL_TYPES = {TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.LONG_GENERIC};
        private static final int GENERIC = 2;
        private static final TimeZoneNames.NameType[] SHORT_TYPES = {TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.SHORT_DAYLIGHT, TimeZoneNames.NameType.SHORT_GENERIC};
        private static final int STD = 0;
        private static final TimeZoneNames.NameType[] TYPES = {TimeZoneNames.NameType.LONG_STANDARD, TimeZoneNames.NameType.SHORT_STANDARD, TimeZoneNames.NameType.LONG_DAYLIGHT, TimeZoneNames.NameType.SHORT_DAYLIGHT, TimeZoneNames.NameType.LONG_GENERIC, TimeZoneNames.NameType.SHORT_GENERIC};
        private static final Map<String, SoftReference<Map<Locale, String[]>>> cache = new ConcurrentHashMap();
        private final Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> cachedTree = new HashMap();
        private final Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> cachedTreeCI = new HashMap();
        private final boolean isGeneric;
        private Set<String> preferredZones;
        private final TextStyle textStyle;

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        ZoneTextPrinterParser(java.time.format.TextStyle r4, java.util.Set<java.time.ZoneId> r5, boolean r6) {
            /*
                r3 = this;
                java.time.temporal.TemporalQuery r0 = java.time.temporal.TemporalQueries.zone()
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = "ZoneText("
                r1.<init>((java.lang.String) r2)
                r1.append((java.lang.Object) r4)
                java.lang.String r2 = ")"
                r1.append((java.lang.String) r2)
                java.lang.String r1 = r1.toString()
                r3.<init>(r0, r1)
                java.util.HashMap r0 = new java.util.HashMap
                r0.<init>()
                r3.cachedTree = r0
                java.util.HashMap r0 = new java.util.HashMap
                r0.<init>()
                r3.cachedTreeCI = r0
                java.lang.String r0 = "textStyle"
                java.lang.Object r4 = java.util.Objects.requireNonNull(r4, (java.lang.String) r0)
                java.time.format.TextStyle r4 = (java.time.format.TextStyle) r4
                r3.textStyle = r4
                r3.isGeneric = r6
                if (r5 == 0) goto L_0x005e
                int r4 = r5.size()
                if (r4 == 0) goto L_0x005e
                java.util.HashSet r4 = new java.util.HashSet
                r4.<init>()
                r3.preferredZones = r4
                java.util.Iterator r4 = r5.iterator()
            L_0x0048:
                boolean r5 = r4.hasNext()
                if (r5 == 0) goto L_0x005e
                java.lang.Object r5 = r4.next()
                java.time.ZoneId r5 = (java.time.ZoneId) r5
                java.util.Set<java.lang.String> r6 = r3.preferredZones
                java.lang.String r5 = r5.getId()
                r6.add(r5)
                goto L_0x0048
            L_0x005e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.ZoneTextPrinterParser.<init>(java.time.format.TextStyle, java.util.Set, boolean):void");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.util.Map} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.lang.String getDisplayName(java.lang.String r19, int r20, java.util.Locale r21) {
            /*
                r18 = this;
                r0 = r18
                r1 = r19
                r2 = r20
                r3 = r21
                java.time.format.TextStyle r4 = r0.textStyle
                java.time.format.TextStyle r5 = java.time.format.TextStyle.NARROW
                r6 = 0
                if (r4 != r5) goto L_0x0010
                return r6
            L_0x0010:
                java.util.Map<java.lang.String, java.lang.ref.SoftReference<java.util.Map<java.util.Locale, java.lang.String[]>>> r4 = cache
                java.lang.Object r5 = r4.get(r1)
                java.lang.ref.SoftReference r5 = (java.lang.ref.SoftReference) r5
                r7 = 5
                r8 = 3
                r9 = 1
                if (r5 == 0) goto L_0x002e
                java.lang.Object r5 = r5.get()
                r6 = r5
                java.util.Map r6 = (java.util.Map) r6
                if (r6 == 0) goto L_0x002e
                java.lang.Object r5 = r6.get(r3)
                java.lang.String[] r5 = (java.lang.String[]) r5
                if (r5 != 0) goto L_0x00b6
            L_0x002e:
                android.icu.text.TimeZoneNames r10 = android.icu.text.TimeZoneNames.getInstance((java.util.Locale) r21)
                android.icu.text.TimeZoneNames$NameType[] r12 = TYPES
                int r5 = r12.length
                int r5 = r5 + r9
                java.lang.String[] r5 = new java.lang.String[r5]
                r17 = 0
                r5[r17] = r1
                java.lang.String r11 = java.time.format.ZoneName.getSystemCanonicalID(r19)
                long r13 = java.lang.System.currentTimeMillis()
                r16 = 1
                r15 = r5
                libcore.icu.TimeZoneNames.getDisplayNames(r10, r11, r12, r13, r15, r16)
                r10 = r5[r9]
                r11 = 4
                r12 = 2
                if (r10 == 0) goto L_0x005c
                r10 = r5[r12]
                if (r10 == 0) goto L_0x005c
                r10 = r5[r8]
                if (r10 == 0) goto L_0x005c
                r10 = r5[r11]
                if (r10 != 0) goto L_0x0093
            L_0x005c:
                java.util.TimeZone r10 = java.util.TimeZone.getTimeZone((java.lang.String) r19)
                int r13 = r10.getRawOffset()
                java.lang.String r13 = java.util.TimeZone.createGmtOffsetString(r9, r9, r13)
                int r14 = r10.getRawOffset()
                int r10 = r10.getDSTSavings()
                int r14 = r14 + r10
                java.lang.String r10 = java.util.TimeZone.createGmtOffsetString(r9, r9, r14)
                r14 = r5[r9]
                if (r14 == 0) goto L_0x007a
                goto L_0x007b
            L_0x007a:
                r14 = r13
            L_0x007b:
                r5[r9] = r14
                r14 = r5[r12]
                if (r14 == 0) goto L_0x0082
                r13 = r14
            L_0x0082:
                r5[r12] = r13
                r12 = r5[r8]
                if (r12 == 0) goto L_0x0089
                goto L_0x008a
            L_0x0089:
                r12 = r10
            L_0x008a:
                r5[r8] = r12
                r12 = r5[r11]
                if (r12 == 0) goto L_0x0091
                r10 = r12
            L_0x0091:
                r5[r11] = r10
            L_0x0093:
                r10 = r5[r7]
                if (r10 != 0) goto L_0x009b
                r10 = r5[r17]
                r5[r7] = r10
            L_0x009b:
                r10 = 6
                r11 = r5[r10]
                if (r11 != 0) goto L_0x00a4
                r11 = r5[r17]
                r5[r10] = r11
            L_0x00a4:
                if (r6 != 0) goto L_0x00ab
                java.util.concurrent.ConcurrentHashMap r6 = new java.util.concurrent.ConcurrentHashMap
                r6.<init>()
            L_0x00ab:
                r6.put(r3, r5)
                java.lang.ref.SoftReference r3 = new java.lang.ref.SoftReference
                r3.<init>(r6)
                r4.put(r1, r3)
            L_0x00b6:
                if (r2 == 0) goto L_0x00ce
                if (r2 == r9) goto L_0x00c4
                java.time.format.TextStyle r0 = r0.textStyle
                int r0 = r0.zoneNameStyleIndex()
                int r0 = r0 + r7
                r0 = r5[r0]
                return r0
            L_0x00c4:
                java.time.format.TextStyle r0 = r0.textStyle
                int r0 = r0.zoneNameStyleIndex()
                int r0 = r0 + r8
                r0 = r5[r0]
                return r0
            L_0x00ce:
                java.time.format.TextStyle r0 = r0.textStyle
                int r0 = r0.zoneNameStyleIndex()
                int r0 = r0 + r9
                r0 = r5[r0]
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.ZoneTextPrinterParser.getDisplayName(java.lang.String, int, java.util.Locale):java.lang.String");
        }

        /* JADX WARNING: Removed duplicated region for block: B:20:0x0081  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean format(java.time.format.DateTimePrintContext r7, java.lang.StringBuilder r8) {
            /*
                r6 = this;
                java.time.temporal.TemporalQuery r0 = java.time.temporal.TemporalQueries.zoneId()
                java.lang.Object r0 = r7.getValue(r0)
                java.time.ZoneId r0 = (java.time.ZoneId) r0
                if (r0 != 0) goto L_0x000e
                r6 = 0
                return r6
            L_0x000e:
                java.lang.String r1 = r0.getId()
                boolean r2 = r0 instanceof java.time.ZoneOffset
                if (r2 != 0) goto L_0x0082
                java.time.temporal.TemporalAccessor r2 = r7.getTemporal()
                boolean r3 = r6.isGeneric
                if (r3 != 0) goto L_0x0076
                java.time.temporal.ChronoField r3 = java.time.temporal.ChronoField.INSTANT_SECONDS
                boolean r3 = r2.isSupported(r3)
                if (r3 == 0) goto L_0x0033
                java.time.zone.ZoneRules r0 = r0.getRules()
                java.time.Instant r2 = java.time.Instant.from(r2)
                boolean r0 = r0.isDaylightSavings(r2)
                goto L_0x0077
            L_0x0033:
                java.time.temporal.ChronoField r3 = java.time.temporal.ChronoField.EPOCH_DAY
                boolean r3 = r2.isSupported(r3)
                if (r3 == 0) goto L_0x0076
                java.time.temporal.ChronoField r3 = java.time.temporal.ChronoField.NANO_OF_DAY
                boolean r3 = r2.isSupported(r3)
                if (r3 == 0) goto L_0x0076
                java.time.temporal.ChronoField r3 = java.time.temporal.ChronoField.EPOCH_DAY
                long r3 = r2.getLong(r3)
                java.time.LocalDate r3 = java.time.LocalDate.ofEpochDay(r3)
                java.time.temporal.ChronoField r4 = java.time.temporal.ChronoField.NANO_OF_DAY
                long r4 = r2.getLong(r4)
                java.time.LocalTime r2 = java.time.LocalTime.ofNanoOfDay(r4)
                java.time.LocalDateTime r2 = r3.atTime((java.time.LocalTime) r2)
                java.time.zone.ZoneRules r3 = r0.getRules()
                java.time.zone.ZoneOffsetTransition r3 = r3.getTransition(r2)
                if (r3 != 0) goto L_0x0076
                java.time.zone.ZoneRules r3 = r0.getRules()
                java.time.ZonedDateTime r0 = r2.atZone((java.time.ZoneId) r0)
                java.time.Instant r0 = r0.toInstant()
                boolean r0 = r3.isDaylightSavings(r0)
                goto L_0x0077
            L_0x0076:
                r0 = 2
            L_0x0077:
                java.util.Locale r7 = r7.getLocale()
                java.lang.String r6 = r6.getDisplayName(r1, r0, r7)
                if (r6 == 0) goto L_0x0082
                r1 = r6
            L_0x0082:
                r8.append((java.lang.String) r1)
                r6 = 1
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: java.time.format.DateTimeFormatterBuilder.ZoneTextPrinterParser.format(java.time.format.DateTimePrintContext, java.lang.StringBuilder):boolean");
        }

        /* access modifiers changed from: protected */
        public PrefixTree getTree(DateTimeParseContext dateTimeParseContext) {
            PrefixTree prefixTree;
            ZoneTextPrinterParser zoneTextPrinterParser = this;
            if (zoneTextPrinterParser.textStyle == TextStyle.NARROW) {
                return super.getTree(dateTimeParseContext);
            }
            Locale locale = dateTimeParseContext.getLocale();
            boolean isCaseSensitive = dateTimeParseContext.isCaseSensitive();
            Set<String> availableZoneIds = ZoneRulesProvider.getAvailableZoneIds();
            int size = availableZoneIds.size();
            Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> map = isCaseSensitive ? zoneTextPrinterParser.cachedTree : zoneTextPrinterParser.cachedTreeCI;
            Map.Entry entry = map.get(locale);
            if (entry == null || ((Integer) entry.getKey()).intValue() != size || (prefixTree = (PrefixTree) ((SoftReference) entry.getValue()).get()) == null) {
                prefixTree = PrefixTree.newTree(dateTimeParseContext);
                TimeZoneNames instance = TimeZoneNames.getInstance(locale);
                long currentTimeMillis = System.currentTimeMillis();
                TimeZoneNames.NameType[] nameTypeArr = zoneTextPrinterParser.textStyle == TextStyle.FULL ? FULL_TYPES : SHORT_TYPES;
                int length = nameTypeArr.length;
                String[] strArr = new String[length];
                for (String next : availableZoneIds) {
                    prefixTree.add(next, next);
                    String zid = ZoneName.toZid(next, locale);
                    String[] strArr2 = strArr;
                    String str = zid;
                    Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> map2 = map;
                    int i = length;
                    TimeZoneNames.NameType[] nameTypeArr2 = nameTypeArr;
                    libcore.icu.TimeZoneNames.getDisplayNames(instance, zid, nameTypeArr, currentTimeMillis, strArr2, 0);
                    int i2 = 0;
                    while (i2 < i) {
                        String str2 = strArr2[i2];
                        String str3 = str;
                        if (str2 != null) {
                            prefixTree.add(str2, str3);
                        }
                        i2++;
                        str = str3;
                    }
                    length = i;
                    strArr = strArr2;
                    map = map2;
                    nameTypeArr = nameTypeArr2;
                }
                Map<Locale, Map.Entry<Integer, SoftReference<PrefixTree>>> map3 = map;
                String[] strArr3 = strArr;
                int i3 = length;
                TimeZoneNames.NameType[] nameTypeArr3 = nameTypeArr;
                if (zoneTextPrinterParser.preferredZones != null) {
                    for (String next2 : availableZoneIds) {
                        if (zoneTextPrinterParser.preferredZones.contains(next2)) {
                            String str4 = next2;
                            libcore.icu.TimeZoneNames.getDisplayNames(instance, ZoneName.toZid(next2, locale), nameTypeArr3, currentTimeMillis, strArr3, 0);
                            for (int i4 = 0; i4 < i3; i4++) {
                                String str5 = strArr3[i4];
                                if (str5 != null) {
                                    prefixTree.add(str5, str4);
                                }
                            }
                            zoneTextPrinterParser = this;
                        }
                    }
                }
                map3.put(locale, new AbstractMap.SimpleImmutableEntry(Integer.valueOf(size), new SoftReference(prefixTree)));
            }
            return prefixTree;
        }
    }

    static class ZoneIdPrinterParser implements DateTimePrinterParser {
        private static volatile Map.Entry<Integer, PrefixTree> cachedPrefixTree;
        private static volatile Map.Entry<Integer, PrefixTree> cachedPrefixTreeCI;
        private final String description;
        private final TemporalQuery<ZoneId> query;

        ZoneIdPrinterParser(TemporalQuery<ZoneId> temporalQuery, String str) {
            this.query = temporalQuery;
            this.description = str;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            ZoneId zoneId = (ZoneId) dateTimePrintContext.getValue(this.query);
            if (zoneId == null) {
                return false;
            }
            sb.append(zoneId.getId());
            return true;
        }

        /* access modifiers changed from: protected */
        public PrefixTree getTree(DateTimeParseContext dateTimeParseContext) {
            Set<String> availableZoneIds = ZoneRulesProvider.getAvailableZoneIds();
            int size = availableZoneIds.size();
            Map.Entry<Integer, PrefixTree> entry = dateTimeParseContext.isCaseSensitive() ? cachedPrefixTree : cachedPrefixTreeCI;
            if (entry == null || entry.getKey().intValue() != size) {
                synchronized (this) {
                    Map.Entry<Integer, PrefixTree> entry2 = dateTimeParseContext.isCaseSensitive() ? cachedPrefixTree : cachedPrefixTreeCI;
                    if (entry2 == null || entry2.getKey().intValue() != size) {
                        entry2 = new AbstractMap.SimpleImmutableEntry<>(Integer.valueOf(size), PrefixTree.newTree(availableZoneIds, dateTimeParseContext));
                        if (dateTimeParseContext.isCaseSensitive()) {
                            cachedPrefixTree = entry2;
                        } else {
                            cachedPrefixTreeCI = entry2;
                        }
                    }
                }
            }
            return entry.getValue();
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            int i2;
            int length = charSequence.length();
            if (i > length) {
                throw new IndexOutOfBoundsException();
            } else if (i == length) {
                return ~i;
            } else {
                char charAt = charSequence.charAt(i);
                if (charAt == '+' || charAt == '-') {
                    return parseOffsetBased(dateTimeParseContext, charSequence, i, i, OffsetIdPrinterParser.INSTANCE_ID_Z);
                }
                int i3 = i + 2;
                if (length >= i3) {
                    char charAt2 = charSequence.charAt(i + 1);
                    if (dateTimeParseContext.charEquals(charAt, 'U') && dateTimeParseContext.charEquals(charAt2, 'T')) {
                        int i4 = i + 3;
                        if (length < i4 || !dateTimeParseContext.charEquals(charSequence.charAt(i3), 'C')) {
                            return parseOffsetBased(dateTimeParseContext, charSequence, i, i3, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                        }
                        if (!(this instanceof ZoneTextPrinterParser)) {
                            return parseOffsetBased(dateTimeParseContext, charSequence, i, i4, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                        }
                    } else if (dateTimeParseContext.charEquals(charAt, 'G') && length >= (i2 = i + 3) && dateTimeParseContext.charEquals(charAt2, 'M') && dateTimeParseContext.charEquals(charSequence.charAt(i3), 'T')) {
                        int i5 = i + 4;
                        if (length < i5 || !dateTimeParseContext.charEquals(charSequence.charAt(i2), '0')) {
                            return parseOffsetBased(dateTimeParseContext, charSequence, i, i2, OffsetIdPrinterParser.INSTANCE_ID_ZERO);
                        }
                        dateTimeParseContext.setParsed(ZoneId.m930of("GMT0"));
                        return i5;
                    }
                }
                PrefixTree tree = getTree(dateTimeParseContext);
                ParsePosition parsePosition = new ParsePosition(i);
                String match = tree.match(charSequence, parsePosition);
                if (match != null) {
                    dateTimeParseContext.setParsed(ZoneId.m930of(match));
                    return parsePosition.getIndex();
                } else if (!dateTimeParseContext.charEquals(charAt, 'Z')) {
                    return ~i;
                } else {
                    dateTimeParseContext.setParsed((ZoneId) ZoneOffset.UTC);
                    return i + 1;
                }
            }
        }

        private int parseOffsetBased(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i, int i2, OffsetIdPrinterParser offsetIdPrinterParser) {
            String upperCase = charSequence.subSequence(i, i2).toString().toUpperCase();
            if (i2 >= charSequence.length()) {
                dateTimeParseContext.setParsed(ZoneId.m930of(upperCase));
                return i2;
            } else if (charSequence.charAt(i2) == '0' && upperCase.equals("GMT")) {
                dateTimeParseContext.setParsed(ZoneId.m930of("GMT0"));
                return i2 + 1;
            } else if (charSequence.charAt(i2) == '0' || dateTimeParseContext.charEquals(charSequence.charAt(i2), 'Z')) {
                dateTimeParseContext.setParsed(ZoneId.m930of(upperCase));
                return i2;
            } else {
                DateTimeParseContext copy = dateTimeParseContext.copy();
                int parse = offsetIdPrinterParser.parse(copy, charSequence, i2);
                if (parse < 0) {
                    try {
                        if (offsetIdPrinterParser == OffsetIdPrinterParser.INSTANCE_ID_Z) {
                            return ~i;
                        }
                        dateTimeParseContext.setParsed(ZoneId.m930of(upperCase));
                        return i2;
                    } catch (DateTimeException unused) {
                        return ~i;
                    }
                } else {
                    dateTimeParseContext.setParsed(ZoneId.ofOffset(upperCase, ZoneOffset.ofTotalSeconds((int) copy.getParsed(ChronoField.OFFSET_SECONDS).longValue())));
                    return parse;
                }
            }
        }

        public String toString() {
            return this.description;
        }
    }

    static class PrefixTree {

        /* renamed from: c0 */
        protected char f382c0;
        protected PrefixTree child;
        protected String key;
        protected PrefixTree sibling;
        protected String value;

        /* access modifiers changed from: protected */
        public boolean isEqual(char c, char c2) {
            return c == c2;
        }

        /* access modifiers changed from: protected */
        public String toKey(String str) {
            return str;
        }

        private PrefixTree(String str, String str2, PrefixTree prefixTree) {
            this.key = str;
            this.value = str2;
            this.child = prefixTree;
            if (str.isEmpty()) {
                this.f382c0 = 65535;
            } else {
                this.f382c0 = this.key.charAt(0);
            }
        }

        public static PrefixTree newTree(DateTimeParseContext dateTimeParseContext) {
            if (dateTimeParseContext.isCaseSensitive()) {
                return new PrefixTree("", (String) null, (PrefixTree) null);
            }
            return new C2884CI("", (String) null, (PrefixTree) null);
        }

        public static PrefixTree newTree(Set<String> set, DateTimeParseContext dateTimeParseContext) {
            PrefixTree newTree = newTree(dateTimeParseContext);
            for (String next : set) {
                newTree.add0(next, next);
            }
            return newTree;
        }

        public PrefixTree copyTree() {
            PrefixTree prefixTree = new PrefixTree(this.key, this.value, (PrefixTree) null);
            PrefixTree prefixTree2 = this.child;
            if (prefixTree2 != null) {
                prefixTree.child = prefixTree2.copyTree();
            }
            PrefixTree prefixTree3 = this.sibling;
            if (prefixTree3 != null) {
                prefixTree.sibling = prefixTree3.copyTree();
            }
            return prefixTree;
        }

        public boolean add(String str, String str2) {
            return add0(str, str2);
        }

        private boolean add0(String str, String str2) {
            String key2 = toKey(str);
            int prefixLength = prefixLength(key2);
            if (prefixLength != this.key.length()) {
                PrefixTree newNode = newNode(this.key.substring(prefixLength), this.value, this.child);
                this.key = key2.substring(0, prefixLength);
                this.child = newNode;
                if (prefixLength < key2.length()) {
                    this.child.sibling = newNode(key2.substring(prefixLength), str2, (PrefixTree) null);
                    this.value = null;
                } else {
                    this.value = str2;
                }
                return true;
            } else if (prefixLength < key2.length()) {
                String substring = key2.substring(prefixLength);
                for (PrefixTree prefixTree = this.child; prefixTree != null; prefixTree = prefixTree.sibling) {
                    if (isEqual(prefixTree.f382c0, substring.charAt(0))) {
                        return prefixTree.add0(substring, str2);
                    }
                }
                PrefixTree newNode2 = newNode(substring, str2, (PrefixTree) null);
                newNode2.sibling = this.child;
                this.child = newNode2;
                return true;
            } else {
                this.value = str2;
                return true;
            }
        }

        public String match(CharSequence charSequence, int i, int i2) {
            int length;
            if (!prefixOf(charSequence, i, i2)) {
                return null;
            }
            if (this.child == null || (length = i + this.key.length()) == i2) {
                return this.value;
            }
            PrefixTree prefixTree = this.child;
            while (!isEqual(prefixTree.f382c0, charSequence.charAt(length))) {
                prefixTree = prefixTree.sibling;
                if (prefixTree == null) {
                    return this.value;
                }
            }
            String match = prefixTree.match(charSequence, length, i2);
            if (match != null) {
                return match;
            }
            return this.value;
        }

        public String match(CharSequence charSequence, ParsePosition parsePosition) {
            int index = parsePosition.getIndex();
            int length = charSequence.length();
            if (!prefixOf(charSequence, index, length)) {
                return null;
            }
            int length2 = index + this.key.length();
            PrefixTree prefixTree = this.child;
            if (prefixTree != null && length2 != length) {
                while (true) {
                    if (!isEqual(prefixTree.f382c0, charSequence.charAt(length2))) {
                        prefixTree = prefixTree.sibling;
                        if (prefixTree == null) {
                            break;
                        }
                    } else {
                        parsePosition.setIndex(length2);
                        String match = prefixTree.match(charSequence, parsePosition);
                        if (match != null) {
                            return match;
                        }
                    }
                }
            }
            parsePosition.setIndex(length2);
            return this.value;
        }

        /* access modifiers changed from: protected */
        public PrefixTree newNode(String str, String str2, PrefixTree prefixTree) {
            return new PrefixTree(str, str2, prefixTree);
        }

        /* access modifiers changed from: protected */
        public boolean prefixOf(CharSequence charSequence, int i, int i2) {
            if (charSequence instanceof String) {
                return ((String) charSequence).startsWith(this.key, i);
            }
            int length = this.key.length();
            if (length > i2 - i) {
                return false;
            }
            int i3 = 0;
            while (true) {
                int i4 = length - 1;
                if (length <= 0) {
                    return true;
                }
                int i5 = i3 + 1;
                int i6 = i + 1;
                if (!isEqual(this.key.charAt(i3), charSequence.charAt(i))) {
                    return false;
                }
                i = i6;
                length = i4;
                i3 = i5;
            }
        }

        private int prefixLength(String str) {
            int i = 0;
            while (i < str.length() && i < this.key.length() && isEqual(str.charAt(i), this.key.charAt(i))) {
                i++;
            }
            return i;
        }

        /* renamed from: java.time.format.DateTimeFormatterBuilder$PrefixTree$CI */
        private static class C2884CI extends PrefixTree {
            private C2884CI(String str, String str2, PrefixTree prefixTree) {
                super(str, str2, prefixTree);
            }

            /* access modifiers changed from: protected */
            public C2884CI newNode(String str, String str2, PrefixTree prefixTree) {
                return new C2884CI(str, str2, prefixTree);
            }

            /* access modifiers changed from: protected */
            public boolean isEqual(char c, char c2) {
                return DateTimeParseContext.charEqualsIgnoreCase(c, c2);
            }

            /* access modifiers changed from: protected */
            public boolean prefixOf(CharSequence charSequence, int i, int i2) {
                int length = this.key.length();
                if (length > i2 - i) {
                    return false;
                }
                int i3 = 0;
                while (true) {
                    int i4 = length - 1;
                    if (length <= 0) {
                        return true;
                    }
                    int i5 = i3 + 1;
                    int i6 = i + 1;
                    if (!isEqual(this.key.charAt(i3), charSequence.charAt(i))) {
                        return false;
                    }
                    i = i6;
                    length = i4;
                    i3 = i5;
                }
            }
        }

        private static class LENIENT extends C2884CI {
            private boolean isLenientChar(char c) {
                return c == ' ' || c == '_' || c == '/';
            }

            private LENIENT(String str, String str2, PrefixTree prefixTree) {
                super(str, str2, prefixTree);
            }

            /* access modifiers changed from: protected */
            public C2884CI newNode(String str, String str2, PrefixTree prefixTree) {
                return new LENIENT(str, str2, prefixTree);
            }

            /* access modifiers changed from: protected */
            public String toKey(String str) {
                int i = 0;
                while (i < str.length()) {
                    if (isLenientChar(str.charAt(i))) {
                        StringBuilder sb = new StringBuilder(str.length());
                        sb.append((CharSequence) str, 0, i);
                        while (true) {
                            i++;
                            if (i >= str.length()) {
                                return sb.toString();
                            }
                            if (!isLenientChar(str.charAt(i))) {
                                sb.append(str.charAt(i));
                            }
                        }
                    } else {
                        i++;
                    }
                }
                return str;
            }

            public String match(CharSequence charSequence, ParsePosition parsePosition) {
                int index = parsePosition.getIndex();
                int length = charSequence.length();
                int length2 = this.key.length();
                int i = 0;
                while (i < length2 && index < length) {
                    if (isLenientChar(charSequence.charAt(index))) {
                        index++;
                    } else {
                        int i2 = i + 1;
                        int i3 = index + 1;
                        if (!isEqual(this.key.charAt(i), charSequence.charAt(index))) {
                            return null;
                        }
                        index = i3;
                        i = i2;
                    }
                }
                if (i != length2) {
                    return null;
                }
                if (this.child != null && index != length) {
                    int i4 = index;
                    while (i4 < length && isLenientChar(charSequence.charAt(i4))) {
                        i4++;
                    }
                    if (i4 < length) {
                        PrefixTree prefixTree = this.child;
                        while (true) {
                            if (!isEqual(prefixTree.f382c0, charSequence.charAt(i4))) {
                                prefixTree = prefixTree.sibling;
                                if (prefixTree == null) {
                                    break;
                                }
                            } else {
                                parsePosition.setIndex(i4);
                                String match = prefixTree.match(charSequence, parsePosition);
                                if (match != null) {
                                    return match;
                                }
                            }
                        }
                    }
                }
                parsePosition.setIndex(index);
                return this.value;
            }
        }
    }

    static final class ChronoPrinterParser implements DateTimePrinterParser {
        private final TextStyle textStyle;

        ChronoPrinterParser(TextStyle textStyle2) {
            this.textStyle = textStyle2;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            Chronology chronology = (Chronology) dateTimePrintContext.getValue(TemporalQueries.chronology());
            if (chronology == null) {
                return false;
            }
            if (this.textStyle == null) {
                sb.append(chronology.getId());
                return true;
            }
            sb.append(getChronologyName(chronology, dateTimePrintContext.getLocale()));
            return true;
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            String str;
            if (i < 0 || i > charSequence.length()) {
                throw new IndexOutOfBoundsException();
            }
            Chronology chronology = null;
            int i2 = -1;
            for (Chronology next : Chronology.getAvailableChronologies()) {
                if (this.textStyle == null) {
                    str = next.getId();
                } else {
                    str = getChronologyName(next, dateTimeParseContext.getLocale());
                }
                String str2 = str;
                int length = str2.length();
                if (length > i2 && dateTimeParseContext.subSequenceEquals(charSequence, i, str2, 0, length)) {
                    chronology = next;
                    i2 = length;
                }
            }
            if (chronology == null) {
                return ~i;
            }
            dateTimeParseContext.setParsed(chronology);
            return i + i2;
        }

        private String getChronologyName(Chronology chronology, Locale locale) {
            return (String) Objects.requireNonNullElseGet(LocaleDisplayNames.getInstance(ULocale.forLocale(locale)).keyValueDisplayName("calendar", chronology.getCalendarType()), new C2885xfcd16acd(chronology));
        }
    }

    static final class LocalizedPrinterParser implements DateTimePrinterParser {
        private static final ConcurrentMap<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap(16, 0.75f, 2);
        private final FormatStyle dateStyle;
        private final FormatStyle timeStyle;

        LocalizedPrinterParser(FormatStyle formatStyle, FormatStyle formatStyle2) {
            this.dateStyle = formatStyle;
            this.timeStyle = formatStyle2;
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return formatter(dateTimePrintContext.getLocale(), Chronology.from(dateTimePrintContext.getTemporal())).toPrinterParser(false).format(dateTimePrintContext, sb);
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            return formatter(dateTimeParseContext.getLocale(), dateTimeParseContext.getEffectiveChronology()).toPrinterParser(false).parse(dateTimeParseContext, charSequence, i);
        }

        private DateTimeFormatter formatter(Locale locale, Chronology chronology) {
            String str = chronology.getId() + '|' + locale.toString() + '|' + this.dateStyle + this.timeStyle;
            ConcurrentMap<String, DateTimeFormatter> concurrentMap = FORMATTER_CACHE;
            DateTimeFormatter dateTimeFormatter = concurrentMap.get(str);
            if (dateTimeFormatter != null) {
                return dateTimeFormatter;
            }
            DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(DateTimeFormatterBuilder.getLocalizedDateTimePattern(this.dateStyle, this.timeStyle, chronology, locale)).toFormatter(locale);
            DateTimeFormatter putIfAbsent = concurrentMap.putIfAbsent(str, formatter);
            return putIfAbsent != null ? putIfAbsent : formatter;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Localized(");
            Object obj = this.dateStyle;
            Object obj2 = "";
            if (obj == null) {
                obj = obj2;
            }
            sb.append(obj);
            sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            Object obj3 = this.timeStyle;
            if (obj3 != null) {
                obj2 = obj3;
            }
            sb.append(obj2);
            sb.append(NavigationBarInflaterView.KEY_CODE_END);
            return sb.toString();
        }
    }

    static final class WeekBasedFieldPrinterParser extends NumberPrinterParser {
        private char chr;
        private int count;

        WeekBasedFieldPrinterParser(char c, int i, int i2, int i3) {
            this(c, i, i2, i3, 0);
        }

        WeekBasedFieldPrinterParser(char c, int i, int i2, int i3, int i4) {
            super((TemporalField) null, i2, i3, SignStyle.NOT_NEGATIVE, i4);
            this.chr = c;
            this.count = i;
        }

        /* access modifiers changed from: package-private */
        public WeekBasedFieldPrinterParser withFixedWidth() {
            if (this.subsequentWidth == -1) {
                return this;
            }
            return new WeekBasedFieldPrinterParser(this.chr, this.count, this.minWidth, this.maxWidth, -1);
        }

        /* access modifiers changed from: package-private */
        public WeekBasedFieldPrinterParser withSubsequentWidth(int i) {
            return new WeekBasedFieldPrinterParser(this.chr, this.count, this.minWidth, this.maxWidth, this.subsequentWidth + i);
        }

        public boolean format(DateTimePrintContext dateTimePrintContext, StringBuilder sb) {
            return printerParser(dateTimePrintContext.getLocale()).format(dateTimePrintContext, sb);
        }

        public int parse(DateTimeParseContext dateTimeParseContext, CharSequence charSequence, int i) {
            return printerParser(dateTimeParseContext.getLocale()).parse(dateTimeParseContext, charSequence, i);
        }

        private DateTimePrinterParser printerParser(Locale locale) {
            TemporalField temporalField;
            SignStyle signStyle;
            WeekFields of = WeekFields.m957of(locale);
            char c = this.chr;
            if (c == 'W') {
                temporalField = of.weekOfMonth();
            } else if (c == 'Y') {
                TemporalField weekBasedYear = of.weekBasedYear();
                if (this.count == 2) {
                    return new ReducedPrinterParser(weekBasedYear, 2, 2, 0, ReducedPrinterParser.BASE_DATE, this.subsequentWidth);
                }
                int i = this.count;
                if (i < 4) {
                    signStyle = SignStyle.NORMAL;
                } else {
                    signStyle = SignStyle.EXCEEDS_PAD;
                }
                return new NumberPrinterParser(weekBasedYear, i, 19, signStyle, this.subsequentWidth);
            } else if (c == 'c' || c == 'e') {
                temporalField = of.dayOfWeek();
            } else if (c == 'w') {
                temporalField = of.weekOfWeekBasedYear();
            } else {
                throw new IllegalStateException("unreachable");
            }
            return new NumberPrinterParser(temporalField, this.minWidth, this.maxWidth, SignStyle.NOT_NEGATIVE, this.subsequentWidth);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(30);
            sb.append("Localized(");
            char c = this.chr;
            if (c == 'Y') {
                int i = this.count;
                if (i == 1) {
                    sb.append("WeekBasedYear");
                } else if (i == 2) {
                    sb.append("ReducedValue(WeekBasedYear,2,2,2000-01-01)");
                } else {
                    sb.append("WeekBasedYear,");
                    sb.append(this.count);
                    sb.append(",19,");
                    sb.append((Object) this.count < 4 ? SignStyle.NORMAL : SignStyle.EXCEEDS_PAD);
                }
            } else {
                if (c == 'W') {
                    sb.append("WeekOfMonth");
                } else if (c == 'c' || c == 'e') {
                    sb.append("DayOfWeek");
                } else if (c == 'w') {
                    sb.append("WeekOfWeekBasedYear");
                }
                sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
                sb.append(this.count);
            }
            sb.append(NavigationBarInflaterView.KEY_CODE_END);
            return sb.toString();
        }
    }
}
