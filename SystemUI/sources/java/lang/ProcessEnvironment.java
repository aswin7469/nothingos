package java.lang;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

final class ProcessEnvironment {
    static final int MIN_NAME_LENGTH = 0;

    private static native byte[][] environ();

    private static Map<String, String> buildEnvironment() {
        byte[][] environ = environ();
        HashMap hashMap = new HashMap((environ.length / 2) + 3);
        for (int length = environ.length - 1; length > 0; length -= 2) {
            hashMap.put(Variable.valueOf(environ[length - 1]), Value.valueOf(environ[length]));
        }
        return new StringEnvironment(hashMap);
    }

    static Map<String, String> getenv() {
        return Collections.unmodifiableMap(buildEnvironment());
    }

    static Map<String, String> environment() {
        return buildEnvironment();
    }

    static Map<String, String> emptyEnvironment(int i) {
        return new StringEnvironment(new HashMap(i));
    }

    private ProcessEnvironment() {
    }

    /* access modifiers changed from: private */
    public static void validateVariable(String str) {
        if (str.indexOf(61) != -1 || str.indexOf(0) != -1) {
            throw new IllegalArgumentException("Invalid environment variable name: \"" + str + "\"");
        }
    }

    /* access modifiers changed from: private */
    public static void validateValue(String str) {
        if (str.indexOf(0) != -1) {
            throw new IllegalArgumentException("Invalid environment variable value: \"" + str + "\"");
        }
    }

    private static abstract class ExternalData {
        protected final byte[] bytes;
        protected final String str;

        protected ExternalData(String str2, byte[] bArr) {
            this.str = str2;
            this.bytes = bArr;
        }

        public byte[] getBytes() {
            return this.bytes;
        }

        public String toString() {
            return this.str;
        }

        public boolean equals(Object obj) {
            return (obj instanceof ExternalData) && ProcessEnvironment.arrayEquals(getBytes(), ((ExternalData) obj).getBytes());
        }

        public int hashCode() {
            return ProcessEnvironment.arrayHash(getBytes());
        }
    }

    private static class Variable extends ExternalData implements Comparable<Variable> {
        protected Variable(String str, byte[] bArr) {
            super(str, bArr);
        }

        public static Variable valueOfQueryOnly(Object obj) {
            return valueOfQueryOnly((String) obj);
        }

        public static Variable valueOfQueryOnly(String str) {
            return new Variable(str, str.getBytes());
        }

        public static Variable valueOf(String str) {
            ProcessEnvironment.validateVariable(str);
            return valueOfQueryOnly(str);
        }

        public static Variable valueOf(byte[] bArr) {
            return new Variable(new String(bArr), bArr);
        }

        public int compareTo(Variable variable) {
            return ProcessEnvironment.arrayCompare(getBytes(), variable.getBytes());
        }

        public boolean equals(Object obj) {
            return (obj instanceof Variable) && super.equals(obj);
        }
    }

    private static class Value extends ExternalData implements Comparable<Value> {
        protected Value(String str, byte[] bArr) {
            super(str, bArr);
        }

        public static Value valueOfQueryOnly(Object obj) {
            return valueOfQueryOnly((String) obj);
        }

        public static Value valueOfQueryOnly(String str) {
            return new Value(str, str.getBytes());
        }

        public static Value valueOf(String str) {
            ProcessEnvironment.validateValue(str);
            return valueOfQueryOnly(str);
        }

        public static Value valueOf(byte[] bArr) {
            return new Value(new String(bArr), bArr);
        }

        public int compareTo(Value value) {
            return ProcessEnvironment.arrayCompare(getBytes(), value.getBytes());
        }

        public boolean equals(Object obj) {
            return (obj instanceof Value) && super.equals(obj);
        }
    }

    private static class StringEnvironment extends AbstractMap<String, String> {

        /* renamed from: m */
        private Map<Variable, Value> f541m;

        private static String toString(Value value) {
            if (value == null) {
                return null;
            }
            return value.toString();
        }

        public StringEnvironment(Map<Variable, Value> map) {
            this.f541m = map;
        }

        public int size() {
            return this.f541m.size();
        }

        public boolean isEmpty() {
            return this.f541m.isEmpty();
        }

        public void clear() {
            this.f541m.clear();
        }

        public boolean containsKey(Object obj) {
            return this.f541m.containsKey(Variable.valueOfQueryOnly(obj));
        }

        public boolean containsValue(Object obj) {
            return this.f541m.containsValue(Value.valueOfQueryOnly(obj));
        }

        public String get(Object obj) {
            return toString(this.f541m.get(Variable.valueOfQueryOnly(obj)));
        }

        public String put(String str, String str2) {
            return toString(this.f541m.put(Variable.valueOf(str), Value.valueOf(str2)));
        }

        public String remove(Object obj) {
            return toString(this.f541m.remove(Variable.valueOfQueryOnly(obj)));
        }

        public Set<String> keySet() {
            return new StringKeySet(this.f541m.keySet());
        }

        public Set<Map.Entry<String, String>> entrySet() {
            return new StringEntrySet(this.f541m.entrySet());
        }

        public Collection<String> values() {
            return new StringValues(this.f541m.values());
        }

        public byte[] toEnvironmentBlock(int[] iArr) {
            int size = this.f541m.size() * 2;
            for (Map.Entry next : this.f541m.entrySet()) {
                size = size + ((Variable) next.getKey()).getBytes().length + ((Value) next.getValue()).getBytes().length;
            }
            byte[] bArr = new byte[size];
            int i = 0;
            for (Map.Entry next2 : this.f541m.entrySet()) {
                byte[] bytes = ((Variable) next2.getKey()).getBytes();
                byte[] bytes2 = ((Value) next2.getValue()).getBytes();
                System.arraycopy((Object) bytes, 0, (Object) bArr, i, bytes.length);
                int length = i + bytes.length;
                int i2 = length + 1;
                bArr[length] = 61;
                System.arraycopy((Object) bytes2, 0, (Object) bArr, i2, bytes2.length);
                i = bytes2.length + 1 + i2;
            }
            iArr[0] = this.f541m.size();
            return bArr;
        }
    }

    static byte[] toEnvironmentBlock(Map<String, String> map, int[] iArr) {
        if (map == null) {
            return null;
        }
        return ((StringEnvironment) map).toEnvironmentBlock(iArr);
    }

    private static class StringEntry implements Map.Entry<String, String> {
        /* access modifiers changed from: private */

        /* renamed from: e */
        public final Map.Entry<Variable, Value> f538e;

        public StringEntry(Map.Entry<Variable, Value> entry) {
            this.f538e = entry;
        }

        public String getKey() {
            return this.f538e.getKey().toString();
        }

        public String getValue() {
            return this.f538e.getValue().toString();
        }

        public String setValue(String str) {
            return this.f538e.setValue(Value.valueOf(str)).toString();
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }

        public boolean equals(Object obj) {
            return (obj instanceof StringEntry) && this.f538e.equals(((StringEntry) obj).f538e);
        }

        public int hashCode() {
            return this.f538e.hashCode();
        }
    }

    private static class StringEntrySet extends AbstractSet<Map.Entry<String, String>> {
        /* access modifiers changed from: private */

        /* renamed from: s */
        public final Set<Map.Entry<Variable, Value>> f539s;

        public StringEntrySet(Set<Map.Entry<Variable, Value>> set) {
            this.f539s = set;
        }

        public int size() {
            return this.f539s.size();
        }

        public boolean isEmpty() {
            return this.f539s.isEmpty();
        }

        public void clear() {
            this.f539s.clear();
        }

        public Iterator<Map.Entry<String, String>> iterator() {
            return new Iterator<Map.Entry<String, String>>() {

                /* renamed from: i */
                Iterator<Map.Entry<Variable, Value>> f540i;

                {
                    this.f540i = StringEntrySet.this.f539s.iterator();
                }

                public boolean hasNext() {
                    return this.f540i.hasNext();
                }

                public Map.Entry<String, String> next() {
                    return new StringEntry(this.f540i.next());
                }

                public void remove() {
                    this.f540i.remove();
                }
            };
        }

        private static Map.Entry<Variable, Value> vvEntry(final Object obj) {
            if (obj instanceof StringEntry) {
                return ((StringEntry) obj).f538e;
            }
            return new Map.Entry<Variable, Value>() {
                public Variable getKey() {
                    return Variable.valueOfQueryOnly(((Map.Entry) Object.this).getKey());
                }

                public Value getValue() {
                    return Value.valueOfQueryOnly(((Map.Entry) Object.this).getValue());
                }

                public Value setValue(Value value) {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public boolean contains(Object obj) {
            return this.f539s.contains(vvEntry(obj));
        }

        public boolean remove(Object obj) {
            return this.f539s.remove(vvEntry(obj));
        }

        public boolean equals(Object obj) {
            return (obj instanceof StringEntrySet) && this.f539s.equals(((StringEntrySet) obj).f539s);
        }

        public int hashCode() {
            return this.f539s.hashCode();
        }
    }

    private static class StringValues extends AbstractCollection<String> {
        /* access modifiers changed from: private */

        /* renamed from: c */
        public final Collection<Value> f544c;

        public StringValues(Collection<Value> collection) {
            this.f544c = collection;
        }

        public int size() {
            return this.f544c.size();
        }

        public boolean isEmpty() {
            return this.f544c.isEmpty();
        }

        public void clear() {
            this.f544c.clear();
        }

        public Iterator<String> iterator() {
            return new Iterator<String>() {

                /* renamed from: i */
                Iterator<Value> f545i;

                {
                    this.f545i = StringValues.this.f544c.iterator();
                }

                public boolean hasNext() {
                    return this.f545i.hasNext();
                }

                public String next() {
                    return this.f545i.next().toString();
                }

                public void remove() {
                    this.f545i.remove();
                }
            };
        }

        public boolean contains(Object obj) {
            return this.f544c.contains(Value.valueOfQueryOnly(obj));
        }

        public boolean remove(Object obj) {
            return this.f544c.remove(Value.valueOfQueryOnly(obj));
        }

        public boolean equals(Object obj) {
            return (obj instanceof StringValues) && this.f544c.equals(((StringValues) obj).f544c);
        }

        public int hashCode() {
            return this.f544c.hashCode();
        }
    }

    private static class StringKeySet extends AbstractSet<String> {
        /* access modifiers changed from: private */

        /* renamed from: s */
        public final Set<Variable> f542s;

        public StringKeySet(Set<Variable> set) {
            this.f542s = set;
        }

        public int size() {
            return this.f542s.size();
        }

        public boolean isEmpty() {
            return this.f542s.isEmpty();
        }

        public void clear() {
            this.f542s.clear();
        }

        public Iterator<String> iterator() {
            return new Iterator<String>() {

                /* renamed from: i */
                Iterator<Variable> f543i;

                {
                    this.f543i = StringKeySet.this.f542s.iterator();
                }

                public boolean hasNext() {
                    return this.f543i.hasNext();
                }

                public String next() {
                    return this.f543i.next().toString();
                }

                public void remove() {
                    this.f543i.remove();
                }
            };
        }

        public boolean contains(Object obj) {
            return this.f542s.contains(Variable.valueOfQueryOnly(obj));
        }

        public boolean remove(Object obj) {
            return this.f542s.remove(Variable.valueOfQueryOnly(obj));
        }
    }

    /* access modifiers changed from: private */
    public static int arrayCompare(byte[] bArr, byte[] bArr2) {
        int length = bArr.length < bArr2.length ? bArr.length : bArr2.length;
        for (int i = 0; i < length; i++) {
            byte b = bArr[i];
            byte b2 = bArr2[i];
            if (b != b2) {
                return b - b2;
            }
        }
        return bArr.length - bArr2.length;
    }

    /* access modifiers changed from: private */
    public static boolean arrayEquals(byte[] bArr, byte[] bArr2) {
        if (bArr.length != bArr2.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static int arrayHash(byte[] bArr) {
        int i = 0;
        for (byte b : bArr) {
            i = (i * 31) + b;
        }
        return i;
    }
}
