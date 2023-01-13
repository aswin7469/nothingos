package java.util.regex;

import android.net.TrafficStats;
import com.android.icu.util.regex.PatternNative;
import dalvik.system.VMRuntime;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import libcore.util.EmptyArray;

public final class Pattern implements Serializable {
    public static final int CANON_EQ = 128;
    public static final int CASE_INSENSITIVE = 2;
    public static final int COMMENTS = 4;
    public static final int DOTALL = 32;
    private static final String FASTSPLIT_METACHARACTERS = "\\?*+[](){}^$.|";
    public static final int LITERAL = 16;
    public static final int MULTILINE = 8;
    public static final int UNICODE_CASE = 64;
    public static final int UNICODE_CHARACTER_CLASS = 256;
    public static final int UNIX_LINES = 1;
    private static final long serialVersionUID = 5073258162644648461L;
    private final int flags;
    transient PatternNative nativePattern;
    private final String pattern;

    public static Pattern compile(String str) {
        return new Pattern(str, 0);
    }

    public static Pattern compile(String str, int i) {
        return new Pattern(str, i);
    }

    public String pattern() {
        return this.pattern;
    }

    public String toString() {
        return this.pattern;
    }

    public Matcher matcher(CharSequence charSequence) {
        return new Matcher(this, charSequence);
    }

    public int flags() {
        return this.flags;
    }

    public static boolean matches(String str, CharSequence charSequence) {
        return compile(str).matcher(charSequence).matches();
    }

    public String[] split(CharSequence charSequence, int i) {
        int i2;
        String[] fastSplit = fastSplit(this.pattern, charSequence.toString(), i);
        if (fastSplit != null) {
            return fastSplit;
        }
        boolean z = i > 0;
        ArrayList arrayList = new ArrayList();
        Matcher matcher = matcher(charSequence);
        int i3 = 0;
        while (matcher.find()) {
            if (!z || arrayList.size() < i - 1) {
                if (i3 != 0 || i3 != matcher.start() || matcher.start() != matcher.end() || VMRuntime.getRuntime().getTargetSdkVersion() <= 28) {
                    arrayList.add(charSequence.subSequence(i3, matcher.start()).toString());
                    i3 = matcher.end();
                }
            } else if (arrayList.size() == i2) {
                arrayList.add(charSequence.subSequence(i3, charSequence.length()).toString());
                i3 = matcher.end();
            }
        }
        if (i3 == 0) {
            return new String[]{charSequence.toString()};
        }
        if (!z || arrayList.size() < i) {
            arrayList.add(charSequence.subSequence(i3, charSequence.length()).toString());
        }
        int size = arrayList.size();
        if (i == 0) {
            while (size > 0 && ((String) arrayList.get(size - 1)).equals("")) {
                size--;
            }
        }
        return (String[]) arrayList.subList(0, size).toArray(new String[size]);
    }

    public static String[] fastSplit(String str, String str2, int i) {
        int indexOf;
        int length = str.length();
        if (length == 0) {
            return null;
        }
        char charAt = str.charAt(0);
        if (length == 1) {
            if (Character.isSurrogate(charAt) || FASTSPLIT_METACHARACTERS.indexOf((int) charAt) != -1) {
                return null;
            }
        } else if (length != 2 || charAt != '\\') {
            return null;
        } else {
            charAt = str.charAt(1);
            if (FASTSPLIT_METACHARACTERS.indexOf((int) charAt) == -1) {
                return null;
            }
        }
        if (str2.isEmpty()) {
            return new String[]{""};
        }
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i2 + 1;
            if (i4 == i || (indexOf = str2.indexOf((int) charAt, i3)) == -1) {
                int length2 = str2.length();
            } else {
                i3 = indexOf + 1;
                i2 = i4;
            }
        }
        int length22 = str2.length();
        if (i == 0 && i3 == length22) {
            if (i2 == length22) {
                return EmptyArray.STRING;
            }
            do {
                i3--;
            } while (str2.charAt(i3 - 1) == charAt);
            i2 -= str2.length() - i3;
            length22 = i3;
        }
        String[] strArr = new String[(i2 + 1)];
        int i5 = 0;
        for (int i6 = 0; i6 != i2; i6++) {
            int indexOf2 = str2.indexOf((int) charAt, i5);
            strArr[i6] = str2.substring(i5, indexOf2);
            i5 = indexOf2 + 1;
        }
        strArr[i2] = str2.substring(i5, length22);
        return strArr;
    }

    public String[] split(CharSequence charSequence) {
        return split(charSequence, 0);
    }

    public static String quote(String str) {
        if (str.indexOf("\\E") == -1) {
            return "\\Q" + str + "\\E";
        }
        StringBuilder sb = new StringBuilder(str.length() * 2);
        sb.append("\\Q");
        int i = 0;
        while (true) {
            int indexOf = str.indexOf("\\E", i);
            if (indexOf != -1) {
                sb.append(str.substring(i, indexOf));
                i = indexOf + 2;
                sb.append("\\E\\\\E\\Q");
            } else {
                sb.append(str.substring(i, str.length()));
                sb.append("\\E");
                return sb.toString();
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        compile();
    }

    private Pattern(String str, int i) {
        this.pattern = str;
        this.flags = i;
        if ((i & 128) == 0) {
            int i2 = i & TrafficStats.TAG_NETWORK_STACK_IMPERSONATION_RANGE_START;
            if (i2 == 0) {
                compile();
                return;
            }
            throw new IllegalArgumentException("Unsupported flags: " + i2);
        }
        throw new UnsupportedOperationException("CANON_EQ flag not supported");
    }

    private void compile() throws PatternSyntaxException {
        String str = this.pattern;
        if (str != null) {
            if ((this.flags & 16) != 0) {
                str = quote(str);
            }
            this.nativePattern = PatternNative.create(str, this.flags & 47);
            return;
        }
        throw new NullPointerException("pattern == null");
    }

    public Predicate<String> asPredicate() {
        return new Pattern$$ExternalSyntheticLambda0(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$asPredicate$0$java-util-regex-Pattern  reason: not valid java name */
    public /* synthetic */ boolean m3883lambda$asPredicate$0$javautilregexPattern(String str) {
        return matcher(str).find();
    }

    public Stream<String> splitAsStream(CharSequence charSequence) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>(charSequence) {
            private int current;
            private int emptyElementCount;
            private final Matcher matcher;
            private String nextElement;
            final /* synthetic */ CharSequence val$input;

            {
                this.val$input = r2;
                this.matcher = Pattern.this.matcher(r2);
            }

            public String next() {
                if (hasNext()) {
                    int i = this.emptyElementCount;
                    if (i == 0) {
                        String str = this.nextElement;
                        this.nextElement = null;
                        return str;
                    }
                    this.emptyElementCount = i - 1;
                    return "";
                }
                throw new NoSuchElementException();
            }

            public boolean hasNext() {
                if (this.nextElement != null || this.emptyElementCount > 0) {
                    return true;
                }
                if (this.current == this.val$input.length()) {
                    return false;
                }
                while (this.matcher.find()) {
                    this.nextElement = this.val$input.subSequence(this.current, this.matcher.start()).toString();
                    this.current = this.matcher.end();
                    if (!this.nextElement.isEmpty()) {
                        return true;
                    }
                    if (this.current > 0) {
                        this.emptyElementCount++;
                    }
                }
                CharSequence charSequence = this.val$input;
                this.nextElement = charSequence.subSequence(this.current, charSequence.length()).toString();
                this.current = this.val$input.length();
                if (!this.nextElement.isEmpty()) {
                    return true;
                }
                this.emptyElementCount = 0;
                this.nextElement = null;
                return false;
            }
        }, 272), false);
    }
}
