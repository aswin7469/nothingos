package java.text;

import java.text.AttributedCharacterIterator;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class AttributedString {
    private static final int INITIAL_CAPACITY = 10;
    Vector<Object>[] runAttributeValues;
    Vector<AttributedCharacterIterator.Attribute>[] runAttributes;
    int runCount;
    int[] runStarts;
    String text;

    AttributedString(AttributedCharacterIterator[] attributedCharacterIteratorArr) {
        if (attributedCharacterIteratorArr == null) {
            throw new NullPointerException("Iterators must not be null");
        } else if (attributedCharacterIteratorArr.length == 0) {
            this.text = "";
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            for (AttributedCharacterIterator appendContents : attributedCharacterIteratorArr) {
                appendContents(stringBuffer, appendContents);
            }
            String stringBuffer2 = stringBuffer.toString();
            this.text = stringBuffer2;
            if (!stringBuffer2.isEmpty()) {
                Map<AttributedCharacterIterator.Attribute, Object> map = null;
                int i = 0;
                for (AttributedCharacterIterator attributedCharacterIterator : attributedCharacterIteratorArr) {
                    int beginIndex = attributedCharacterIterator.getBeginIndex();
                    int endIndex = attributedCharacterIterator.getEndIndex();
                    int i2 = beginIndex;
                    while (i2 < endIndex) {
                        attributedCharacterIterator.setIndex(i2);
                        Map<AttributedCharacterIterator.Attribute, Object> attributes = attributedCharacterIterator.getAttributes();
                        if (mapsDiffer(map, attributes)) {
                            setAttributes(attributes, (i2 - beginIndex) + i);
                        }
                        i2 = attributedCharacterIterator.getRunLimit();
                        map = attributes;
                    }
                    i += endIndex - beginIndex;
                }
            }
        }
    }

    public AttributedString(String str) {
        str.getClass();
        this.text = str;
    }

    public AttributedString(String str, Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        if (str == null || map == null) {
            throw null;
        }
        this.text = str;
        if (!str.isEmpty()) {
            int size = map.size();
            if (size > 0) {
                createRunAttributeDataVectors();
                Vector<AttributedCharacterIterator.Attribute> vector = new Vector<>(size);
                Vector<Object> vector2 = new Vector<>(size);
                this.runAttributes[0] = vector;
                this.runAttributeValues[0] = vector2;
                for (Map.Entry next : map.entrySet()) {
                    vector.addElement((AttributedCharacterIterator.Attribute) next.getKey());
                    vector2.addElement(next.getValue());
                }
            }
        } else if (!map.isEmpty()) {
            throw new IllegalArgumentException("Can't add attribute to 0-length text");
        }
    }

    public AttributedString(AttributedCharacterIterator attributedCharacterIterator) {
        this(attributedCharacterIterator, attributedCharacterIterator.getBeginIndex(), attributedCharacterIterator.getEndIndex(), (AttributedCharacterIterator.Attribute[]) null);
    }

    public AttributedString(AttributedCharacterIterator attributedCharacterIterator, int i, int i2) {
        this(attributedCharacterIterator, i, i2, (AttributedCharacterIterator.Attribute[]) null);
    }

    public AttributedString(AttributedCharacterIterator attributedCharacterIterator, int i, int i2, AttributedCharacterIterator.Attribute[] attributeArr) {
        attributedCharacterIterator.getClass();
        int beginIndex = attributedCharacterIterator.getBeginIndex();
        int endIndex = attributedCharacterIterator.getEndIndex();
        if (i < beginIndex || i2 > endIndex || i > i2) {
            throw new IllegalArgumentException("Invalid substring range");
        }
        StringBuilder sb = new StringBuilder();
        attributedCharacterIterator.setIndex(i);
        char current = attributedCharacterIterator.current();
        while (attributedCharacterIterator.getIndex() < i2) {
            sb.append(current);
            current = attributedCharacterIterator.next();
        }
        this.text = sb.toString();
        if (i != i2) {
            HashSet hashSet = new HashSet();
            if (attributeArr == null) {
                hashSet.addAll(attributedCharacterIterator.getAllAttributeKeys());
            } else {
                for (AttributedCharacterIterator.Attribute add : attributeArr) {
                    hashSet.add(add);
                }
                hashSet.retainAll(attributedCharacterIterator.getAllAttributeKeys());
            }
            if (!hashSet.isEmpty()) {
                Iterator it = hashSet.iterator();
                while (it.hasNext()) {
                    AttributedCharacterIterator.Attribute attribute = (AttributedCharacterIterator.Attribute) it.next();
                    attributedCharacterIterator.setIndex(beginIndex);
                    while (attributedCharacterIterator.getIndex() < i2) {
                        int runStart = attributedCharacterIterator.getRunStart(attribute);
                        int runLimit = attributedCharacterIterator.getRunLimit(attribute);
                        Object attribute2 = attributedCharacterIterator.getAttribute(attribute);
                        if (attribute2 != null) {
                            if (attribute2 instanceof Annotation) {
                                if (runStart >= i && runLimit <= i2) {
                                    addAttribute(attribute, attribute2, runStart - i, runLimit - i);
                                } else if (runLimit > i2) {
                                    break;
                                }
                            } else if (runStart >= i2) {
                                break;
                            } else if (runLimit > i) {
                                runStart = runStart < i ? i : runStart;
                                runLimit = runLimit > i2 ? i2 : runLimit;
                                if (runStart != runLimit) {
                                    addAttribute(attribute, attribute2, runStart - i, runLimit - i);
                                }
                            }
                        }
                        attributedCharacterIterator.setIndex(runLimit);
                    }
                }
            }
        }
    }

    public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj) {
        attribute.getClass();
        int length = length();
        if (length != 0) {
            addAttributeImpl(attribute, obj, 0, length);
            return;
        }
        throw new IllegalArgumentException("Can't add attribute to 0-length text");
    }

    public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj, int i, int i2) {
        attribute.getClass();
        if (i < 0 || i2 > length() || i >= i2) {
            throw new IllegalArgumentException("Invalid substring range");
        }
        addAttributeImpl(attribute, obj, i, i2);
    }

    public void addAttributes(Map<? extends AttributedCharacterIterator.Attribute, ?> map, int i, int i2) {
        map.getClass();
        if (i < 0 || i2 > length() || i > i2) {
            throw new IllegalArgumentException("Invalid substring range");
        } else if (i != i2) {
            if (this.runCount == 0) {
                createRunAttributeDataVectors();
            }
            int ensureRunBreak = ensureRunBreak(i);
            int ensureRunBreak2 = ensureRunBreak(i2);
            for (Map.Entry next : map.entrySet()) {
                addAttributeRunData((AttributedCharacterIterator.Attribute) next.getKey(), next.getValue(), ensureRunBreak, ensureRunBreak2);
            }
        } else if (!map.isEmpty()) {
            throw new IllegalArgumentException("Can't add attribute to 0-length text");
        }
    }

    private synchronized void addAttributeImpl(AttributedCharacterIterator.Attribute attribute, Object obj, int i, int i2) {
        if (this.runCount == 0) {
            createRunAttributeDataVectors();
        }
        addAttributeRunData(attribute, obj, ensureRunBreak(i), ensureRunBreak(i2));
    }

    private final void createRunAttributeDataVectors() {
        this.runStarts = new int[10];
        this.runAttributes = new Vector[10];
        this.runAttributeValues = new Vector[10];
        this.runCount = 1;
    }

    private final int ensureRunBreak(int i) {
        return ensureRunBreak(i, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x001f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0020  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int ensureRunBreak(int r7, boolean r8) {
        /*
            r6 = this;
            int r0 = r6.length()
            if (r7 != r0) goto L_0x0009
            int r6 = r6.runCount
            return r6
        L_0x0009:
            r0 = 0
        L_0x000a:
            int r1 = r6.runCount
            if (r0 >= r1) goto L_0x0017
            int[] r2 = r6.runStarts
            r2 = r2[r0]
            if (r2 >= r7) goto L_0x0017
            int r0 = r0 + 1
            goto L_0x000a
        L_0x0017:
            if (r0 >= r1) goto L_0x0020
            int[] r2 = r6.runStarts
            r2 = r2[r0]
            if (r2 != r7) goto L_0x0020
            return r0
        L_0x0020:
            int[] r2 = r6.runStarts
            int r3 = r2.length
            if (r1 != r3) goto L_0x0042
            int r1 = r3 >> 2
            int r3 = r3 + r1
            int[] r1 = java.util.Arrays.copyOf((int[]) r2, (int) r3)
            java.util.Vector<java.text.AttributedCharacterIterator$Attribute>[] r2 = r6.runAttributes
            java.lang.Object[] r2 = java.util.Arrays.copyOf((T[]) r2, (int) r3)
            java.util.Vector[] r2 = (java.util.Vector[]) r2
            java.util.Vector<java.lang.Object>[] r4 = r6.runAttributeValues
            java.lang.Object[] r3 = java.util.Arrays.copyOf((T[]) r4, (int) r3)
            java.util.Vector[] r3 = (java.util.Vector[]) r3
            r6.runStarts = r1
            r6.runAttributes = r2
            r6.runAttributeValues = r3
        L_0x0042:
            r1 = 0
            if (r8 == 0) goto L_0x0062
            java.util.Vector<java.text.AttributedCharacterIterator$Attribute>[] r8 = r6.runAttributes
            int r2 = r0 + -1
            r8 = r8[r2]
            java.util.Vector<java.lang.Object>[] r3 = r6.runAttributeValues
            r2 = r3[r2]
            if (r8 == 0) goto L_0x0057
            java.util.Vector r3 = new java.util.Vector
            r3.<init>(r8)
            goto L_0x0058
        L_0x0057:
            r3 = r1
        L_0x0058:
            if (r2 == 0) goto L_0x005f
            java.util.Vector r1 = new java.util.Vector
            r1.<init>(r2)
        L_0x005f:
            r8 = r1
            r1 = r3
            goto L_0x0063
        L_0x0062:
            r8 = r1
        L_0x0063:
            int r2 = r6.runCount
            int r2 = r2 + 1
            r6.runCount = r2
            int r2 = r2 + -1
        L_0x006b:
            if (r2 <= r0) goto L_0x0084
            int[] r3 = r6.runStarts
            int r4 = r2 + -1
            r5 = r3[r4]
            r3[r2] = r5
            java.util.Vector<java.text.AttributedCharacterIterator$Attribute>[] r3 = r6.runAttributes
            r5 = r3[r4]
            r3[r2] = r5
            java.util.Vector<java.lang.Object>[] r3 = r6.runAttributeValues
            r4 = r3[r4]
            r3[r2] = r4
            int r2 = r2 + -1
            goto L_0x006b
        L_0x0084:
            int[] r2 = r6.runStarts
            r2[r0] = r7
            java.util.Vector<java.text.AttributedCharacterIterator$Attribute>[] r7 = r6.runAttributes
            r7[r0] = r1
            java.util.Vector<java.lang.Object>[] r6 = r6.runAttributeValues
            r6[r0] = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.AttributedString.ensureRunBreak(int, boolean):int");
    }

    private void addAttributeRunData(AttributedCharacterIterator.Attribute attribute, Object obj, int i, int i2) {
        int i3;
        while (i < i2) {
            Vector<AttributedCharacterIterator.Attribute> vector = this.runAttributes[i];
            if (vector == null) {
                Vector<AttributedCharacterIterator.Attribute> vector2 = new Vector<>();
                Vector<Object> vector3 = new Vector<>();
                this.runAttributes[i] = vector2;
                this.runAttributeValues[i] = vector3;
                i3 = -1;
            } else {
                i3 = vector.indexOf(attribute);
            }
            if (i3 == -1) {
                int size = this.runAttributes[i].size();
                this.runAttributes[i].addElement(attribute);
                try {
                    this.runAttributeValues[i].addElement(obj);
                } catch (Exception unused) {
                    this.runAttributes[i].setSize(size);
                    this.runAttributeValues[i].setSize(size);
                }
            } else {
                this.runAttributeValues[i].set(i3, obj);
            }
            i++;
        }
    }

    public AttributedCharacterIterator getIterator() {
        return getIterator((AttributedCharacterIterator.Attribute[]) null, 0, length());
    }

    public AttributedCharacterIterator getIterator(AttributedCharacterIterator.Attribute[] attributeArr) {
        return getIterator(attributeArr, 0, length());
    }

    public AttributedCharacterIterator getIterator(AttributedCharacterIterator.Attribute[] attributeArr, int i, int i2) {
        return new AttributedStringIterator(attributeArr, i, i2);
    }

    /* access modifiers changed from: package-private */
    public int length() {
        return this.text.length();
    }

    /* access modifiers changed from: private */
    public char charAt(int i) {
        return this.text.charAt(i);
    }

    /* access modifiers changed from: private */
    public synchronized Object getAttribute(AttributedCharacterIterator.Attribute attribute, int i) {
        Vector<AttributedCharacterIterator.Attribute> vector = this.runAttributes[i];
        Vector<Object> vector2 = this.runAttributeValues[i];
        if (vector == null) {
            return null;
        }
        int indexOf = vector.indexOf(attribute);
        if (indexOf == -1) {
            return null;
        }
        return vector2.elementAt(indexOf);
    }

    /* access modifiers changed from: private */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003e  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0057 A[EDGE_INSN: B:29:0x0057->B:23:0x0057 ?: BREAK  , SYNTHETIC] */
    public java.lang.Object getAttributeCheckRange(java.text.AttributedCharacterIterator.Attribute r6, int r7, int r8, int r9) {
        /*
            r5 = this;
            java.lang.Object r0 = r5.getAttribute(r6, r7)
            boolean r1 = r0 instanceof java.text.Annotation
            if (r1 == 0) goto L_0x005a
            r1 = 0
            if (r8 <= 0) goto L_0x0028
            int[] r2 = r5.runStarts
            r2 = r2[r7]
            r3 = r7
        L_0x0010:
            if (r2 < r8) goto L_0x0025
            int r4 = r3 + -1
            java.lang.Object r4 = r5.getAttribute(r6, r4)
            boolean r4 = valuesMatch(r0, r4)
            if (r4 == 0) goto L_0x0025
            int r3 = r3 + -1
            int[] r2 = r5.runStarts
            r2 = r2[r3]
            goto L_0x0010
        L_0x0025:
            if (r2 >= r8) goto L_0x0028
            return r1
        L_0x0028:
            int r8 = r5.length()
            if (r9 >= r8) goto L_0x005a
            int r2 = r5.runCount
            int r2 = r2 + -1
            if (r7 >= r2) goto L_0x003b
            int[] r2 = r5.runStarts
            int r3 = r7 + 1
            r2 = r2[r3]
            goto L_0x003c
        L_0x003b:
            r2 = r8
        L_0x003c:
            if (r2 > r9) goto L_0x0057
            int r7 = r7 + 1
            java.lang.Object r3 = r5.getAttribute(r6, r7)
            boolean r3 = valuesMatch(r0, r3)
            if (r3 == 0) goto L_0x0057
            int r2 = r5.runCount
            int r2 = r2 + -1
            if (r7 >= r2) goto L_0x003b
            int[] r2 = r5.runStarts
            int r3 = r7 + 1
            r2 = r2[r3]
            goto L_0x003c
        L_0x0057:
            if (r2 <= r9) goto L_0x005a
            return r1
        L_0x005a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.AttributedString.getAttributeCheckRange(java.text.AttributedCharacterIterator$Attribute, int, int, int):java.lang.Object");
    }

    /* access modifiers changed from: private */
    public boolean attributeValuesMatch(Set<? extends AttributedCharacterIterator.Attribute> set, int i, int i2) {
        for (AttributedCharacterIterator.Attribute attribute : set) {
            if (!valuesMatch(getAttribute(attribute, i), getAttribute(attribute, i2))) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static final boolean valuesMatch(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    private final void appendContents(StringBuffer stringBuffer, CharacterIterator characterIterator) {
        int endIndex = characterIterator.getEndIndex();
        for (int beginIndex = characterIterator.getBeginIndex(); beginIndex < endIndex; beginIndex++) {
            characterIterator.setIndex(beginIndex);
            stringBuffer.append(characterIterator.current());
        }
    }

    private void setAttributes(Map<AttributedCharacterIterator.Attribute, Object> map, int i) {
        int size;
        if (this.runCount == 0) {
            createRunAttributeDataVectors();
        }
        int ensureRunBreak = ensureRunBreak(i, false);
        if (map != null && (size = map.size()) > 0) {
            Vector<AttributedCharacterIterator.Attribute> vector = new Vector<>(size);
            Vector<Object> vector2 = new Vector<>(size);
            for (Map.Entry next : map.entrySet()) {
                vector.add((AttributedCharacterIterator.Attribute) next.getKey());
                vector2.add(next.getValue());
            }
            this.runAttributes[ensureRunBreak] = vector;
            this.runAttributeValues[ensureRunBreak] = vector2;
        }
    }

    private static <K, V> boolean mapsDiffer(Map<K, V> map, Map<K, V> map2) {
        if (map == null) {
            return map2 != null && map2.size() > 0;
        }
        return !map.equals(map2);
    }

    private final class AttributedStringIterator implements AttributedCharacterIterator {
        private int beginIndex;
        private int currentIndex;
        private int currentRunIndex;
        private int currentRunLimit;
        private int currentRunStart;
        private int endIndex;
        private AttributedCharacterIterator.Attribute[] relevantAttributes;

        AttributedStringIterator(AttributedCharacterIterator.Attribute[] attributeArr, int i, int i2) {
            if (i < 0 || i > i2 || i2 > AttributedString.this.length()) {
                throw new IllegalArgumentException("Invalid substring range");
            }
            this.beginIndex = i;
            this.endIndex = i2;
            this.currentIndex = i;
            updateRunInfo();
            if (attributeArr != null) {
                this.relevantAttributes = (AttributedCharacterIterator.Attribute[]) attributeArr.clone();
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AttributedStringIterator)) {
                return false;
            }
            AttributedStringIterator attributedStringIterator = (AttributedStringIterator) obj;
            return AttributedString.this == attributedStringIterator.getString() && this.currentIndex == attributedStringIterator.currentIndex && this.beginIndex == attributedStringIterator.beginIndex && this.endIndex == attributedStringIterator.endIndex;
        }

        public int hashCode() {
            return this.endIndex ^ ((AttributedString.this.text.hashCode() ^ this.currentIndex) ^ this.beginIndex);
        }

        public Object clone() {
            try {
                return (AttributedStringIterator) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError((Throwable) e);
            }
        }

        public char first() {
            return internalSetIndex(this.beginIndex);
        }

        public char last() {
            int i = this.endIndex;
            if (i == this.beginIndex) {
                return internalSetIndex(i);
            }
            return internalSetIndex(i - 1);
        }

        public char current() {
            int i = this.currentIndex;
            if (i == this.endIndex) {
                return 65535;
            }
            return AttributedString.this.charAt(i);
        }

        public char next() {
            int i = this.currentIndex;
            if (i < this.endIndex) {
                return internalSetIndex(i + 1);
            }
            return 65535;
        }

        public char previous() {
            int i = this.currentIndex;
            if (i > this.beginIndex) {
                return internalSetIndex(i - 1);
            }
            return 65535;
        }

        public char setIndex(int i) {
            if (i >= this.beginIndex && i <= this.endIndex) {
                return internalSetIndex(i);
            }
            throw new IllegalArgumentException("Invalid index");
        }

        public int getBeginIndex() {
            return this.beginIndex;
        }

        public int getEndIndex() {
            return this.endIndex;
        }

        public int getIndex() {
            return this.currentIndex;
        }

        public int getRunStart() {
            return this.currentRunStart;
        }

        public int getRunStart(AttributedCharacterIterator.Attribute attribute) {
            int i = this.currentRunStart;
            if (i == this.beginIndex || this.currentRunIndex == -1) {
                return i;
            }
            Object attribute2 = getAttribute(attribute);
            int i2 = this.currentRunStart;
            int i3 = this.currentRunIndex;
            while (i2 > this.beginIndex && AttributedString.valuesMatch(attribute2, AttributedString.this.getAttribute(attribute, i3 - 1))) {
                i3--;
                i2 = AttributedString.this.runStarts[i3];
            }
            int i4 = this.beginIndex;
            return i2 < i4 ? i4 : i2;
        }

        public int getRunStart(Set<? extends AttributedCharacterIterator.Attribute> set) {
            int i = this.currentRunStart;
            if (i == this.beginIndex || (r1 = this.currentRunIndex) == -1) {
                return i;
            }
            while (i > this.beginIndex && AttributedString.this.attributeValuesMatch(set, this.currentRunIndex, r1 - 1)) {
                int i2 = i2 - 1;
                i = AttributedString.this.runStarts[i2];
            }
            int i3 = this.beginIndex;
            return i < i3 ? i3 : i;
        }

        public int getRunLimit() {
            return this.currentRunLimit;
        }

        public int getRunLimit(AttributedCharacterIterator.Attribute attribute) {
            int i = this.currentRunLimit;
            if (i == this.endIndex || this.currentRunIndex == -1) {
                return i;
            }
            Object attribute2 = getAttribute(attribute);
            int i2 = this.currentRunLimit;
            int i3 = this.currentRunIndex;
            while (i2 < this.endIndex) {
                i3++;
                if (!AttributedString.valuesMatch(attribute2, AttributedString.this.getAttribute(attribute, i3))) {
                    break;
                }
                i2 = i3 < AttributedString.this.runCount + -1 ? AttributedString.this.runStarts[i3 + 1] : this.endIndex;
            }
            int i4 = this.endIndex;
            return i2 > i4 ? i4 : i2;
        }

        public int getRunLimit(Set<? extends AttributedCharacterIterator.Attribute> set) {
            int i = this.currentRunLimit;
            if (i == this.endIndex || (r1 = this.currentRunIndex) == -1) {
                return i;
            }
            while (i < this.endIndex) {
                int i2 = i2 + 1;
                if (!AttributedString.this.attributeValuesMatch(set, this.currentRunIndex, i2)) {
                    break;
                }
                i = i2 < AttributedString.this.runCount + -1 ? AttributedString.this.runStarts[i2 + 1] : this.endIndex;
            }
            int i3 = this.endIndex;
            return i > i3 ? i3 : i;
        }

        public Map<AttributedCharacterIterator.Attribute, Object> getAttributes() {
            if (!(AttributedString.this.runAttributes == null || this.currentRunIndex == -1)) {
                Vector<AttributedCharacterIterator.Attribute>[] vectorArr = AttributedString.this.runAttributes;
                int i = this.currentRunIndex;
                if (vectorArr[i] != null) {
                    return new AttributeMap(i, this.beginIndex, this.endIndex);
                }
            }
            return new Hashtable();
        }

        public Set<AttributedCharacterIterator.Attribute> getAllAttributeKeys() {
            HashSet hashSet;
            Vector<AttributedCharacterIterator.Attribute> vector;
            if (AttributedString.this.runAttributes == null) {
                return new HashSet();
            }
            synchronized (AttributedString.this) {
                hashSet = new HashSet();
                for (int i = 0; i < AttributedString.this.runCount; i++) {
                    if (AttributedString.this.runStarts[i] < this.endIndex && ((i == AttributedString.this.runCount - 1 || AttributedString.this.runStarts[i + 1] > this.beginIndex) && (vector = AttributedString.this.runAttributes[i]) != null)) {
                        int size = vector.size();
                        while (true) {
                            int i2 = size - 1;
                            if (size <= 0) {
                                break;
                            }
                            hashSet.add(vector.get(i2));
                            size = i2;
                        }
                    }
                }
            }
            return hashSet;
        }

        public Object getAttribute(AttributedCharacterIterator.Attribute attribute) {
            int i = this.currentRunIndex;
            if (i < 0) {
                return null;
            }
            return AttributedString.this.getAttributeCheckRange(attribute, i, this.beginIndex, this.endIndex);
        }

        private AttributedString getString() {
            return AttributedString.this;
        }

        private char internalSetIndex(int i) {
            this.currentIndex = i;
            if (i < this.currentRunStart || i >= this.currentRunLimit) {
                updateRunInfo();
            }
            if (this.currentIndex == this.endIndex) {
                return 65535;
            }
            return AttributedString.this.charAt(i);
        }

        private void updateRunInfo() {
            int i = this.currentIndex;
            int i2 = this.endIndex;
            int i3 = -1;
            if (i == i2) {
                this.currentRunLimit = i2;
                this.currentRunStart = i2;
                this.currentRunIndex = -1;
                return;
            }
            synchronized (AttributedString.this) {
                while (i3 < AttributedString.this.runCount - 1) {
                    int i4 = i3 + 1;
                    if (AttributedString.this.runStarts[i4] > this.currentIndex) {
                        break;
                    }
                    i3 = i4;
                }
                this.currentRunIndex = i3;
                if (i3 >= 0) {
                    int i5 = AttributedString.this.runStarts[i3];
                    this.currentRunStart = i5;
                    int i6 = this.beginIndex;
                    if (i5 < i6) {
                        this.currentRunStart = i6;
                    }
                } else {
                    this.currentRunStart = this.beginIndex;
                }
                if (i3 < AttributedString.this.runCount - 1) {
                    int i7 = AttributedString.this.runStarts[i3 + 1];
                    this.currentRunLimit = i7;
                    int i8 = this.endIndex;
                    if (i7 > i8) {
                        this.currentRunLimit = i8;
                    }
                } else {
                    this.currentRunLimit = this.endIndex;
                }
            }
        }
    }

    private final class AttributeMap extends AbstractMap<AttributedCharacterIterator.Attribute, Object> {
        int beginIndex;
        int endIndex;
        int runIndex;

        AttributeMap(int i, int i2, int i3) {
            this.runIndex = i;
            this.beginIndex = i2;
            this.endIndex = i3;
        }

        public Set<Map.Entry<AttributedCharacterIterator.Attribute, Object>> entrySet() {
            HashSet hashSet = new HashSet();
            synchronized (AttributedString.this) {
                int size = AttributedString.this.runAttributes[this.runIndex].size();
                for (int i = 0; i < size; i++) {
                    AttributedCharacterIterator.Attribute attribute = AttributedString.this.runAttributes[this.runIndex].get(i);
                    Object obj = AttributedString.this.runAttributeValues[this.runIndex].get(i);
                    if (!(obj instanceof Annotation) || (obj = AttributedString.this.getAttributeCheckRange(attribute, this.runIndex, this.beginIndex, this.endIndex)) != null) {
                        hashSet.add(new AttributeEntry(attribute, obj));
                    }
                }
            }
            return hashSet;
        }

        public Object get(Object obj) {
            return AttributedString.this.getAttributeCheckRange((AttributedCharacterIterator.Attribute) obj, this.runIndex, this.beginIndex, this.endIndex);
        }
    }
}
