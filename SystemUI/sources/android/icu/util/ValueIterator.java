package android.icu.util;

public interface ValueIterator {
    boolean next(Element element);

    void reset();

    void setRange(int i, int i2);

    public static final class Element {
        public int integer;
        public Object value;

        public Element() {
            throw new RuntimeException("Stub!");
        }
    }
}
