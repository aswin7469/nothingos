package java.util.regex;

import com.android.icu.util.regex.MatcherNative;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

public final class Matcher implements MatchResult {
    boolean anchoringBounds = true;
    int appendPos = 0;
    int from;
    int[] groups;
    private boolean matchFound;
    private MatcherNative nativeMatcher;
    private CharSequence originalInput;
    private Pattern parentPattern;
    String text;

    /* renamed from: to */
    int f773to;
    boolean transparentBounds = false;

    Matcher(Pattern pattern, CharSequence charSequence) {
        usePattern(pattern);
        reset(charSequence);
    }

    public Pattern pattern() {
        return this.parentPattern;
    }

    public MatchResult toMatchResult() {
        ensureMatch();
        return new OffsetBasedMatchResult(this.text, this.groups);
    }

    public Matcher usePattern(Pattern pattern) {
        if (pattern != null) {
            synchronized (this) {
                this.nativeMatcher = MatcherNative.create(pattern.nativePattern);
            }
            this.parentPattern = pattern;
            if (this.text != null) {
                resetForInput();
            }
            this.groups = new int[((groupCount() + 1) * 2)];
            this.matchFound = false;
            return this;
        }
        throw new IllegalArgumentException("Pattern cannot be null");
    }

    public Matcher reset() {
        CharSequence charSequence = this.originalInput;
        return reset(charSequence, 0, charSequence.length());
    }

    public Matcher reset(CharSequence charSequence) {
        return reset(charSequence, 0, charSequence.length());
    }

    public int start() {
        return start(0);
    }

    public int start(int i) {
        ensureMatch();
        if (i >= 0 && i <= groupCount()) {
            return this.groups[i * 2];
        }
        throw new IndexOutOfBoundsException("No group " + i);
    }

    public int start(String str) {
        return this.groups[getMatchedGroupIndex(str) * 2];
    }

    public int end() {
        return end(0);
    }

    public int end(int i) {
        ensureMatch();
        if (i >= 0 && i <= groupCount()) {
            return this.groups[(i * 2) + 1];
        }
        throw new IndexOutOfBoundsException("No group " + i);
    }

    public int end(String str) {
        return this.groups[(getMatchedGroupIndex(str) * 2) + 1];
    }

    public String group() {
        return group(0);
    }

    public String group(int i) {
        int i2;
        ensureMatch();
        if (i < 0 || i > groupCount()) {
            throw new IndexOutOfBoundsException("No group " + i);
        }
        int[] iArr = this.groups;
        int i3 = i * 2;
        int i4 = iArr[i3];
        if (i4 == -1 || (i2 = iArr[i3 + 1]) == -1) {
            return null;
        }
        return getSubSequence(i4, i2).toString();
    }

    public String group(String str) {
        int i;
        int matchedGroupIndex = getMatchedGroupIndex(str);
        int[] iArr = this.groups;
        int i2 = matchedGroupIndex * 2;
        int i3 = iArr[i2];
        if (i3 == -1 || (i = iArr[i2 + 1]) == -1) {
            return null;
        }
        return getSubSequence(i3, i).toString();
    }

    public int groupCount() {
        int groupCount;
        synchronized (this) {
            groupCount = this.nativeMatcher.groupCount();
        }
        return groupCount;
    }

    public boolean matches() {
        boolean matches;
        synchronized (this) {
            matches = this.nativeMatcher.matches(this.groups);
            this.matchFound = matches;
        }
        return matches;
    }

    public boolean find() {
        boolean findNext;
        synchronized (this) {
            findNext = this.nativeMatcher.findNext(this.groups);
            this.matchFound = findNext;
        }
        return findNext;
    }

    public boolean find(int i) {
        boolean find;
        int textLength = getTextLength();
        if (i < 0 || i > textLength) {
            throw new IndexOutOfBoundsException("Illegal start index");
        }
        reset();
        synchronized (this) {
            find = this.nativeMatcher.find(i, this.groups);
            this.matchFound = find;
        }
        return find;
    }

    public boolean lookingAt() {
        boolean lookingAt;
        synchronized (this) {
            lookingAt = this.nativeMatcher.lookingAt(this.groups);
            this.matchFound = lookingAt;
        }
        return lookingAt;
    }

    public static String quoteReplacement(String str) {
        if (str.indexOf(92) == -1 && str.indexOf(36) == -1) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '\\' || charAt == '$') {
                sb.append('\\');
            }
            sb.append(charAt);
        }
        return sb.toString();
    }

    public Matcher appendReplacement(StringBuffer stringBuffer, String str) {
        stringBuffer.append(this.text.substring(this.appendPos, start()));
        appendEvaluated(stringBuffer, str);
        this.appendPos = end();
        return this;
    }

    private void appendEvaluated(StringBuffer stringBuffer, String str) {
        int i = -1;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char charAt = str.charAt(i2);
            if (charAt == '\\' && !z) {
                z = true;
            } else if (charAt == '$' && !z) {
                z2 = true;
            } else if (charAt >= '0' && charAt <= '9' && z2 && !z3) {
                String group = group(charAt - '0');
                if (group != null) {
                    stringBuffer.append(group);
                }
                z2 = false;
            } else if (charAt != '{' || !z2) {
                if (charAt == '}' && z2 && z3) {
                    String group2 = group(str.substring(i + 1, i2));
                    if (group2 != null) {
                        stringBuffer.append(group2);
                    }
                    z2 = false;
                } else if (charAt == '}' || !z2 || !z3) {
                    stringBuffer.append(charAt);
                    z = false;
                    z2 = false;
                }
                z3 = z2;
            } else {
                i = i2;
                z3 = true;
            }
        }
        if (z) {
            throw new IllegalArgumentException("character to be escaped is missing");
        } else if (z2) {
            throw new IllegalArgumentException("Illegal group reference: group index is missing");
        } else if (z3) {
            throw new IllegalArgumentException("Missing ending brace '}' from replacement string");
        }
    }

    public StringBuffer appendTail(StringBuffer stringBuffer) {
        int i = this.appendPos;
        int i2 = this.f773to;
        if (i < i2) {
            stringBuffer.append(this.text.substring(i, i2));
        }
        return stringBuffer;
    }

    public String replaceAll(String str) {
        reset();
        if (!find()) {
            return this.text.toString();
        }
        StringBuffer stringBuffer = new StringBuffer();
        do {
            appendReplacement(stringBuffer, str);
        } while (find());
        appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public String replaceFirst(String str) {
        if (str != null) {
            reset();
            if (!find()) {
                return this.text.toString();
            }
            StringBuffer stringBuffer = new StringBuffer();
            appendReplacement(stringBuffer, str);
            appendTail(stringBuffer);
            return stringBuffer.toString();
        }
        throw new NullPointerException("replacement");
    }

    public Matcher region(int i, int i2) {
        return reset(this.originalInput, i, i2);
    }

    public int regionStart() {
        return this.from;
    }

    public int regionEnd() {
        return this.f773to;
    }

    public boolean hasTransparentBounds() {
        return this.transparentBounds;
    }

    public Matcher useTransparentBounds(boolean z) {
        synchronized (this) {
            this.transparentBounds = z;
            this.nativeMatcher.useTransparentBounds(z);
        }
        return this;
    }

    public boolean hasAnchoringBounds() {
        return this.anchoringBounds;
    }

    public Matcher useAnchoringBounds(boolean z) {
        synchronized (this) {
            this.anchoringBounds = z;
            this.nativeMatcher.useAnchoringBounds(z);
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("java.util.regex.Matcher");
        sb.append("[pattern=" + pattern());
        sb.append(" region=");
        sb.append(regionStart() + NavigationBarInflaterView.BUTTON_SEPARATOR + regionEnd());
        sb.append(" lastmatch=");
        if (this.matchFound && group() != null) {
            sb.append(group());
        }
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public boolean hitEnd() {
        boolean hitEnd;
        synchronized (this) {
            hitEnd = this.nativeMatcher.hitEnd();
        }
        return hitEnd;
    }

    public boolean requireEnd() {
        boolean requireEnd;
        synchronized (this) {
            requireEnd = this.nativeMatcher.requireEnd();
        }
        return requireEnd;
    }

    /* access modifiers changed from: package-private */
    public int getTextLength() {
        return this.text.length();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSubSequence(int i, int i2) {
        return this.text.subSequence(i, i2);
    }

    private Matcher reset(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            throw new IllegalArgumentException("input == null");
        } else if (i < 0 || i2 < 0 || i > charSequence.length() || i2 > charSequence.length() || i > i2) {
            throw new IndexOutOfBoundsException();
        } else {
            this.originalInput = charSequence;
            this.text = charSequence.toString();
            this.from = i;
            this.f773to = i2;
            resetForInput();
            this.matchFound = false;
            this.appendPos = 0;
            return this;
        }
    }

    private void resetForInput() {
        synchronized (this) {
            this.nativeMatcher.setInput(this.text, this.from, this.f773to);
            this.nativeMatcher.useAnchoringBounds(this.anchoringBounds);
            this.nativeMatcher.useTransparentBounds(this.transparentBounds);
        }
    }

    private void ensureMatch() {
        if (!this.matchFound) {
            throw new IllegalStateException("No successful match so far");
        }
    }

    private int getMatchedGroupIndex(String str) {
        ensureMatch();
        int matchedGroupIndex = this.nativeMatcher.getMatchedGroupIndex(str);
        if (matchedGroupIndex >= 0) {
            return matchedGroupIndex;
        }
        throw new IllegalArgumentException("No capturing group in the pattern with the name " + str);
    }

    static final class OffsetBasedMatchResult implements MatchResult {
        private final String input;
        private final int[] offsets;

        OffsetBasedMatchResult(String str, int[] iArr) {
            this.input = str;
            this.offsets = (int[]) iArr.clone();
        }

        public int start() {
            return start(0);
        }

        public int start(int i) {
            return this.offsets[i * 2];
        }

        public int end() {
            return end(0);
        }

        public int end(int i) {
            return this.offsets[(i * 2) + 1];
        }

        public String group() {
            return group(0);
        }

        public String group(int i) {
            int start = start(i);
            int end = end(i);
            if (start == -1 || end == -1) {
                return null;
            }
            return this.input.substring(start, end);
        }

        public int groupCount() {
            return (this.offsets.length / 2) - 1;
        }
    }
}
