package java.time.format;

import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

final class DateTimeParseContext {
    private boolean caseSensitive = true;
    private ArrayList<Consumer<Chronology>> chronoListeners;
    private DateTimeFormatter formatter;
    private final ArrayList<Parsed> parsed;
    private boolean strict = true;

    DateTimeParseContext(DateTimeFormatter dateTimeFormatter) {
        ArrayList<Parsed> arrayList = new ArrayList<>();
        this.parsed = arrayList;
        this.chronoListeners = null;
        this.formatter = dateTimeFormatter;
        arrayList.add(new Parsed());
    }

    /* access modifiers changed from: package-private */
    public DateTimeParseContext copy() {
        DateTimeParseContext dateTimeParseContext = new DateTimeParseContext(this.formatter);
        dateTimeParseContext.caseSensitive = this.caseSensitive;
        dateTimeParseContext.strict = this.strict;
        return dateTimeParseContext;
    }

    /* access modifiers changed from: package-private */
    public Locale getLocale() {
        return this.formatter.getLocale();
    }

    /* access modifiers changed from: package-private */
    public DecimalStyle getDecimalStyle() {
        return this.formatter.getDecimalStyle();
    }

    /* access modifiers changed from: package-private */
    public Chronology getEffectiveChronology() {
        Chronology chronology = currentParsed().chrono;
        if (chronology != null) {
            return chronology;
        }
        Chronology chronology2 = this.formatter.getChronology();
        return chronology2 == null ? IsoChronology.INSTANCE : chronology2;
    }

    /* access modifiers changed from: package-private */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /* access modifiers changed from: package-private */
    public void setCaseSensitive(boolean z) {
        this.caseSensitive = z;
    }

    /* access modifiers changed from: package-private */
    public boolean subSequenceEquals(CharSequence charSequence, int i, CharSequence charSequence2, int i2, int i3) {
        if (i + i3 > charSequence.length() || i2 + i3 > charSequence2.length()) {
            return false;
        }
        if (isCaseSensitive()) {
            for (int i4 = 0; i4 < i3; i4++) {
                if (charSequence.charAt(i + i4) != charSequence2.charAt(i2 + i4)) {
                    return false;
                }
            }
            return true;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            char charAt = charSequence.charAt(i + i5);
            char charAt2 = charSequence2.charAt(i2 + i5);
            if (charAt != charAt2 && Character.toUpperCase(charAt) != Character.toUpperCase(charAt2) && Character.toLowerCase(charAt) != Character.toLowerCase(charAt2)) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean charEquals(char c, char c2) {
        if (isCaseSensitive()) {
            return c == c2;
        }
        return charEqualsIgnoreCase(c, c2);
    }

    static boolean charEqualsIgnoreCase(char c, char c2) {
        return c == c2 || Character.toUpperCase(c) == Character.toUpperCase(c2) || Character.toLowerCase(c) == Character.toLowerCase(c2);
    }

    /* access modifiers changed from: package-private */
    public boolean isStrict() {
        return this.strict;
    }

    /* access modifiers changed from: package-private */
    public void setStrict(boolean z) {
        this.strict = z;
    }

    /* access modifiers changed from: package-private */
    public void startOptional() {
        this.parsed.add(currentParsed().copy());
    }

    /* access modifiers changed from: package-private */
    public void endOptional(boolean z) {
        if (z) {
            ArrayList<Parsed> arrayList = this.parsed;
            arrayList.remove(arrayList.size() - 2);
            return;
        }
        ArrayList<Parsed> arrayList2 = this.parsed;
        arrayList2.remove(arrayList2.size() - 1);
    }

    private Parsed currentParsed() {
        ArrayList<Parsed> arrayList = this.parsed;
        return arrayList.get(arrayList.size() - 1);
    }

    /* access modifiers changed from: package-private */
    public Parsed toUnresolved() {
        return currentParsed();
    }

    /* access modifiers changed from: package-private */
    public TemporalAccessor toResolved(ResolverStyle resolverStyle, Set<TemporalField> set) {
        Parsed currentParsed = currentParsed();
        currentParsed.chrono = getEffectiveChronology();
        currentParsed.zone = currentParsed.zone != null ? currentParsed.zone : this.formatter.getZone();
        return currentParsed.resolve(resolverStyle, set);
    }

    /* access modifiers changed from: package-private */
    public Long getParsed(TemporalField temporalField) {
        return currentParsed().fieldValues.get(temporalField);
    }

    /* access modifiers changed from: package-private */
    public int setParsedField(TemporalField temporalField, long j, int i, int i2) {
        Objects.requireNonNull(temporalField, "field");
        Long put = currentParsed().fieldValues.put(temporalField, Long.valueOf(j));
        return (put == null || put.longValue() == j) ? i2 : ~i;
    }

    /* access modifiers changed from: package-private */
    public void setParsed(Chronology chronology) {
        Objects.requireNonNull(chronology, "chrono");
        currentParsed().chrono = chronology;
        ArrayList<Consumer<Chronology>> arrayList = this.chronoListeners;
        if (arrayList != null && !arrayList.isEmpty()) {
            Consumer[] consumerArr = (Consumer[]) this.chronoListeners.toArray(new Consumer[1]);
            this.chronoListeners.clear();
            for (Consumer accept : consumerArr) {
                accept.accept(chronology);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addChronoChangedListener(Consumer<Chronology> consumer) {
        if (this.chronoListeners == null) {
            this.chronoListeners = new ArrayList<>();
        }
        this.chronoListeners.add(consumer);
    }

    /* access modifiers changed from: package-private */
    public void setParsed(ZoneId zoneId) {
        Objects.requireNonNull(zoneId, "zone");
        currentParsed().zone = zoneId;
    }

    /* access modifiers changed from: package-private */
    public void setParsedLeapSecond() {
        currentParsed().leapSecond = true;
    }

    public String toString() {
        return currentParsed().toString();
    }
}
