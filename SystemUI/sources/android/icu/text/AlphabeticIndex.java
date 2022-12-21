package android.icu.text;

import android.icu.util.ULocale;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class AlphabeticIndex<V> implements Iterable<Bucket<V>> {
    public AlphabeticIndex(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex(RuleBasedCollator ruleBasedCollator) {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> addLabels(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> addLabels(ULocale... uLocaleArr) {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> addLabels(Locale... localeArr) {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> setOverflowLabel(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getUnderflowLabel() {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> setUnderflowLabel(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getOverflowLabel() {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> setInflowLabel(String str) {
        throw new RuntimeException("Stub!");
    }

    public String getInflowLabel() {
        throw new RuntimeException("Stub!");
    }

    public int getMaxLabelCount() {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> setMaxLabelCount(int i) {
        throw new RuntimeException("Stub!");
    }

    public ImmutableIndex<V> buildImmutableIndex() {
        throw new RuntimeException("Stub!");
    }

    public List<String> getBucketLabels() {
        throw new RuntimeException("Stub!");
    }

    public RuleBasedCollator getCollator() {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> addRecord(CharSequence charSequence, V v) {
        throw new RuntimeException("Stub!");
    }

    public int getBucketIndex(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public AlphabeticIndex<V> clearRecords() {
        throw new RuntimeException("Stub!");
    }

    public int getBucketCount() {
        throw new RuntimeException("Stub!");
    }

    public int getRecordCount() {
        throw new RuntimeException("Stub!");
    }

    public Iterator<Bucket<V>> iterator() {
        throw new RuntimeException("Stub!");
    }

    public static class Bucket<V> implements Iterable<Record<V>> {

        public enum LabelType {
            NORMAL,
            UNDERFLOW,
            INFLOW,
            OVERFLOW
        }

        private Bucket() {
            throw new RuntimeException("Stub!");
        }

        public String getLabel() {
            throw new RuntimeException("Stub!");
        }

        public LabelType getLabelType() {
            throw new RuntimeException("Stub!");
        }

        public int size() {
            throw new RuntimeException("Stub!");
        }

        public Iterator<Record<V>> iterator() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }

    public static final class ImmutableIndex<V> implements Iterable<Bucket<V>> {
        private ImmutableIndex() {
            throw new RuntimeException("Stub!");
        }

        public int getBucketCount() {
            throw new RuntimeException("Stub!");
        }

        public int getBucketIndex(CharSequence charSequence) {
            throw new RuntimeException("Stub!");
        }

        public Bucket<V> getBucket(int i) {
            throw new RuntimeException("Stub!");
        }

        public Iterator<Bucket<V>> iterator() {
            throw new RuntimeException("Stub!");
        }
    }

    public static class Record<V> {
        private Record() {
            throw new RuntimeException("Stub!");
        }

        public CharSequence getName() {
            throw new RuntimeException("Stub!");
        }

        public V getData() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }
}
