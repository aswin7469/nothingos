package java.text;

import java.text.PatternEntry;
import java.util.ArrayList;

final class MergeCollation {
    private final byte BITARRAYMASK = 1;
    private final int BYTEMASK = 7;
    private final int BYTEPOWER = 3;
    private transient StringBuffer excess = new StringBuffer();
    private transient PatternEntry lastEntry = null;
    ArrayList<PatternEntry> patterns = new ArrayList<>();
    private transient PatternEntry saveEntry = null;
    private transient byte[] statusArray = new byte[8192];

    public MergeCollation(String str) throws ParseException {
        int i = 0;
        while (true) {
            byte[] bArr = this.statusArray;
            if (i < bArr.length) {
                bArr[i] = 0;
                i++;
            } else {
                setPattern(str);
                return;
            }
        }
    }

    public String getPattern() {
        return getPattern(true);
    }

    public String getPattern(boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList arrayList = null;
        int i = 0;
        while (i < this.patterns.size()) {
            PatternEntry patternEntry = this.patterns.get(i);
            if (!patternEntry.extension.isEmpty()) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(patternEntry);
            } else {
                if (arrayList != null) {
                    PatternEntry findLastWithNoExtension = findLastWithNoExtension(i - 1);
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        ((PatternEntry) arrayList.get(size)).addToBuffer(stringBuffer, false, z, findLastWithNoExtension);
                    }
                    arrayList = null;
                }
                patternEntry.addToBuffer(stringBuffer, false, z, (PatternEntry) null);
            }
            i++;
        }
        if (arrayList != null) {
            PatternEntry findLastWithNoExtension2 = findLastWithNoExtension(i - 1);
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                ((PatternEntry) arrayList.get(size2)).addToBuffer(stringBuffer, false, z, findLastWithNoExtension2);
            }
        }
        return stringBuffer.toString();
    }

    private final PatternEntry findLastWithNoExtension(int i) {
        PatternEntry patternEntry;
        do {
            i--;
            if (i < 0) {
                return null;
            }
            patternEntry = this.patterns.get(i);
        } while (!patternEntry.extension.isEmpty());
        return patternEntry;
    }

    public String emitPattern() {
        return emitPattern(true);
    }

    public String emitPattern(boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.patterns.size(); i++) {
            PatternEntry patternEntry = this.patterns.get(i);
            if (patternEntry != null) {
                patternEntry.addToBuffer(stringBuffer, true, z, (PatternEntry) null);
            }
        }
        return stringBuffer.toString();
    }

    public void setPattern(String str) throws ParseException {
        this.patterns.clear();
        addPattern(str);
    }

    public void addPattern(String str) throws ParseException {
        if (str != null) {
            PatternEntry.Parser parser = new PatternEntry.Parser(str);
            for (PatternEntry next = parser.next(); next != null; next = parser.next()) {
                fixEntry(next);
            }
        }
    }

    public int getCount() {
        return this.patterns.size();
    }

    public PatternEntry getItemAt(int i) {
        return this.patterns.get(i);
    }

    private final void fixEntry(PatternEntry patternEntry) throws ParseException {
        int i;
        if (this.lastEntry == null || !patternEntry.chars.equals(this.lastEntry.chars) || !patternEntry.extension.equals(this.lastEntry.extension)) {
            boolean z = true;
            if (patternEntry.strength != -2) {
                if (patternEntry.chars.length() == 1) {
                    char charAt = patternEntry.chars.charAt(0);
                    int i2 = charAt >> 3;
                    byte[] bArr = this.statusArray;
                    byte b = bArr[i2];
                    byte b2 = (byte) (1 << (charAt & 7));
                    if (b == 0 || (b & b2) == 0) {
                        bArr[i2] = (byte) (b2 | b);
                        i = -1;
                    } else {
                        i = this.patterns.lastIndexOf(patternEntry);
                    }
                } else {
                    i = this.patterns.lastIndexOf(patternEntry);
                }
                if (i != -1) {
                    this.patterns.remove(i);
                }
                this.excess.setLength(0);
                int findLastEntry = findLastEntry(this.lastEntry, this.excess);
                if (this.excess.length() != 0) {
                    patternEntry.extension = this.excess + patternEntry.extension;
                    if (findLastEntry != this.patterns.size()) {
                        this.lastEntry = this.saveEntry;
                        z = false;
                    }
                }
                if (findLastEntry == this.patterns.size()) {
                    this.patterns.add(patternEntry);
                    this.saveEntry = patternEntry;
                } else {
                    this.patterns.add(findLastEntry, patternEntry);
                }
            }
            if (z) {
                this.lastEntry = patternEntry;
            }
        } else if (patternEntry.strength != 3 && patternEntry.strength != -2) {
            throw new ParseException("The entries " + this.lastEntry + " and " + patternEntry + " are adjacent in the rules, but have conflicting strengths: A character can't be unequal to itself.", -1);
        }
    }

    private final int findLastEntry(PatternEntry patternEntry, StringBuffer stringBuffer) throws ParseException {
        int i;
        if (patternEntry == null) {
            return 0;
        }
        if (patternEntry.strength != -2) {
            if (patternEntry.chars.length() == 1) {
                i = (this.statusArray[patternEntry.chars.charAt(0) >> 3] & (1 << (patternEntry.chars.charAt(0) & 7))) != 0 ? this.patterns.lastIndexOf(patternEntry) : -1;
            } else {
                i = this.patterns.lastIndexOf(patternEntry);
            }
            if (i != -1) {
                return i + 1;
            }
            throw new ParseException("couldn't find last entry: " + patternEntry, i);
        }
        int size = this.patterns.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            PatternEntry patternEntry2 = this.patterns.get(size);
            if (patternEntry2.chars.regionMatches(0, patternEntry.chars, 0, patternEntry2.chars.length())) {
                stringBuffer.append((CharSequence) patternEntry.chars, patternEntry2.chars.length(), patternEntry.chars.length());
                break;
            }
            size--;
        }
        if (size != -1) {
            return size + 1;
        }
        throw new ParseException("couldn't find: " + patternEntry, size);
    }
}
