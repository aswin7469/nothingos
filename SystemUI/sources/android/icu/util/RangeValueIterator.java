package android.icu.util;

public interface RangeValueIterator {
    boolean next(Element element);

    void reset();

    public static class Element {
        public int limit;
        public int start;
        public int value;

        public Element() {
            throw new RuntimeException("Stub!");
        }
    }
}
