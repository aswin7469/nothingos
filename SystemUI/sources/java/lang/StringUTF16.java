package java.lang;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class StringUTF16 {
    StringUTF16() {
    }

    static char getChar(String str, int i) {
        return str.charAt(i);
    }

    public static int length(String str) {
        return str.length();
    }

    public static int indexOfNonWhitespace(String str) {
        int length = str.length();
        int i = 0;
        while (i < length) {
            int codePointAt = str.codePointAt(i);
            if (codePointAt != 32 && codePointAt != 9 && !Character.isWhitespace(codePointAt)) {
                break;
            }
            i += Character.charCount(codePointAt);
        }
        return i;
    }

    public static int lastIndexOfNonWhitespace(String str) {
        int length = str.length();
        while (length > 0) {
            int codePointBefore = str.codePointBefore(length);
            if (codePointBefore != 32 && codePointBefore != 9 && !Character.isWhitespace(codePointBefore)) {
                break;
            }
            length -= Character.charCount(codePointBefore);
        }
        return length;
    }

    public static String strip(String str) {
        int length = str.length();
        int indexOfNonWhitespace = indexOfNonWhitespace(str);
        if (indexOfNonWhitespace == length) {
            return "";
        }
        int lastIndexOfNonWhitespace = lastIndexOfNonWhitespace(str);
        if (indexOfNonWhitespace > 0 || lastIndexOfNonWhitespace < length) {
            return newString(str, indexOfNonWhitespace, lastIndexOfNonWhitespace - indexOfNonWhitespace);
        }
        return null;
    }

    public static String stripLeading(String str) {
        int length = str.length();
        int indexOfNonWhitespace = indexOfNonWhitespace(str);
        if (indexOfNonWhitespace == length) {
            return "";
        }
        if (indexOfNonWhitespace != 0) {
            return newString(str, indexOfNonWhitespace, length - indexOfNonWhitespace);
        }
        return null;
    }

    public static String stripTrailing(String str) {
        int length = str.length();
        int lastIndexOfNonWhitespace = lastIndexOfNonWhitespace(str);
        if (lastIndexOfNonWhitespace == 0) {
            return "";
        }
        if (lastIndexOfNonWhitespace != length) {
            return newString(str, 0, lastIndexOfNonWhitespace);
        }
        return null;
    }

    private static final class LinesSpliterator implements Spliterator<String> {
        private final int fence;
        private int index;
        private String value;

        public int characteristics() {
            return 1296;
        }

        LinesSpliterator(String str) {
            this(str, 0, str.length());
        }

        LinesSpliterator(String str, int i, int i2) {
            this.value = str;
            this.index = i;
            this.fence = i + i2;
        }

        private int indexOfLineSeparator(int i) {
            while (true) {
                int i2 = this.fence;
                if (i >= i2) {
                    return i2;
                }
                char c = StringUTF16.getChar(this.value, i);
                if (c == 10 || c == 13) {
                    return i;
                }
                i++;
            }
            return i;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000e, code lost:
            r0 = r3 + 1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int skipLineSeparator(int r3) {
            /*
                r2 = this;
                int r0 = r2.fence
                if (r3 >= r0) goto L_0x0024
                java.lang.String r0 = r2.value
                char r0 = java.lang.StringUTF16.getChar(r0, r3)
                r1 = 13
                if (r0 != r1) goto L_0x0021
                int r0 = r3 + 1
                int r1 = r2.fence
                if (r0 >= r1) goto L_0x0021
                java.lang.String r2 = r2.value
                char r2 = java.lang.StringUTF16.getChar(r2, r0)
                r1 = 10
                if (r2 != r1) goto L_0x0021
                int r0 = r0 + 1
                return r0
            L_0x0021:
                int r3 = r3 + 1
                return r3
            L_0x0024:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.StringUTF16.LinesSpliterator.skipLineSeparator(int):int");
        }

        private String next() {
            int i = this.index;
            int indexOfLineSeparator = indexOfLineSeparator(i);
            this.index = skipLineSeparator(indexOfLineSeparator);
            return StringUTF16.newString(this.value, i, indexOfLineSeparator - i);
        }

        public boolean tryAdvance(Consumer<? super String> consumer) {
            if (consumer == null) {
                throw new NullPointerException("tryAdvance action missing");
            } else if (this.index == this.fence) {
                return false;
            } else {
                consumer.accept(next());
                return true;
            }
        }

        public void forEachRemaining(Consumer<? super String> consumer) {
            if (consumer != null) {
                while (this.index != this.fence) {
                    consumer.accept(next());
                }
                return;
            }
            throw new NullPointerException("forEachRemaining action missing");
        }

        public Spliterator<String> trySplit() {
            int skipLineSeparator = skipLineSeparator(indexOfLineSeparator((this.fence + this.index) >>> 1));
            if (skipLineSeparator >= this.fence) {
                return null;
            }
            int i = this.index;
            this.index = skipLineSeparator;
            return new LinesSpliterator(this.value, i, skipLineSeparator - i);
        }

        public long estimateSize() {
            return (long) ((this.fence - this.index) + 1);
        }
    }

    static Stream<String> lines(String str) {
        return StreamSupport.stream(new LinesSpliterator(str), false);
    }

    public static String newString(String str, int i, int i2) {
        return str.substring(i, i2 + i);
    }

    static class CharsSpliterator implements Spliterator.OfInt {
        private final String array;

        /* renamed from: cs */
        private final int f546cs;
        private final int fence;
        private int index;

        CharsSpliterator(String str, int i) {
            this(str, 0, str.length(), i);
        }

        CharsSpliterator(String str, int i, int i2, int i3) {
            this.array = str;
            this.index = i;
            this.fence = i2;
            this.f546cs = i3 | 16 | 64 | 16384;
        }

        public Spliterator.OfInt trySplit() {
            int i = this.index;
            int i2 = (this.fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            String str = this.array;
            this.index = i2;
            return new CharsSpliterator(str, i, i2, this.f546cs);
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            int i;
            intConsumer.getClass();
            String str = this.array;
            int length = str.length();
            int i2 = this.fence;
            if (length >= i2 && (i = this.index) >= 0) {
                this.index = i2;
                if (i < i2) {
                    do {
                        intConsumer.accept(StringUTF16.charAt(str, i));
                        i++;
                    } while (i < i2);
                }
            }
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            intConsumer.getClass();
            int i = this.index;
            if (i < 0 || i >= this.fence) {
                return false;
            }
            intConsumer.accept(StringUTF16.charAt(this.array, i));
            this.index++;
            return true;
        }

        public long estimateSize() {
            return (long) (this.fence - this.index);
        }

        public int characteristics() {
            return this.f546cs;
        }
    }

    static class CodePointsSpliterator implements Spliterator.OfInt {
        private final String array;

        /* renamed from: cs */
        private final int f547cs;
        private final int fence;
        private int index;

        CodePointsSpliterator(String str, int i) {
            this(str, 0, str.length(), i);
        }

        CodePointsSpliterator(String str, int i, int i2, int i3) {
            this.array = str;
            this.index = i;
            this.fence = i2;
            this.f547cs = i3 | 16;
        }

        public Spliterator.OfInt trySplit() {
            int i = this.index;
            int i2 = (this.fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            if (Character.isLowSurrogate(StringUTF16.charAt(this.array, i2))) {
                int i3 = i2 - 1;
                if (Character.isHighSurrogate(StringUTF16.charAt(this.array, i3))) {
                    if (i >= i3) {
                        return null;
                    }
                    String str = this.array;
                    this.index = i3;
                    return new CodePointsSpliterator(str, i, i3, this.f547cs);
                }
            }
            String str2 = this.array;
            this.index = i2;
            return new CodePointsSpliterator(str2, i, i2, this.f547cs);
        }

        public void forEachRemaining(IntConsumer intConsumer) {
            int i;
            intConsumer.getClass();
            String str = this.array;
            int length = str.length();
            int i2 = this.fence;
            if (length >= i2 && (i = this.index) >= 0) {
                this.index = i2;
                if (i < i2) {
                    do {
                        i = advance(str, i, i2, intConsumer);
                    } while (i < i2);
                }
            }
        }

        public boolean tryAdvance(IntConsumer intConsumer) {
            int i;
            intConsumer.getClass();
            int i2 = this.index;
            if (i2 < 0 || i2 >= (i = this.fence)) {
                return false;
            }
            this.index = advance(this.array, i2, i, intConsumer);
            return true;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: char} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: char} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: char} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: char} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: char} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static int advance(java.lang.String r2, int r3, int r4, java.util.function.IntConsumer r5) {
            /*
                int r0 = r3 + 1
                char r3 = java.lang.StringUTF16.charAt(r2, r3)
                boolean r1 = java.lang.Character.isHighSurrogate(r3)
                if (r1 == 0) goto L_0x001e
                if (r0 >= r4) goto L_0x001e
                char r2 = java.lang.StringUTF16.charAt(r2, r0)
                boolean r4 = java.lang.Character.isLowSurrogate(r2)
                if (r4 == 0) goto L_0x001e
                int r0 = r0 + 1
                int r3 = java.lang.Character.toCodePoint(r3, r2)
            L_0x001e:
                r5.accept(r3)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.StringUTF16.CodePointsSpliterator.advance(java.lang.String, int, int, java.util.function.IntConsumer):int");
        }

        public long estimateSize() {
            return (long) (this.fence - this.index);
        }

        public int characteristics() {
            return this.f547cs;
        }
    }

    public static char charAt(String str, int i) {
        checkIndex(i, str);
        return getChar(str, i);
    }

    public static void checkIndex(int i, String str) {
        String.checkIndex(i, length(str));
    }
}
