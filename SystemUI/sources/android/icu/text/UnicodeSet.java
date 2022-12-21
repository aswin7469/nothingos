package android.icu.text;

import android.icu.util.Freezable;
import java.text.ParsePosition;
import java.util.Collection;
import java.util.Iterator;

public class UnicodeSet extends UnicodeFilter implements Iterable<String>, Comparable<UnicodeSet>, Freezable<UnicodeSet> {
    public static final int ADD_CASE_MAPPINGS = 4;
    public static final UnicodeSet ALL_CODE_POINTS = null;
    public static final int CASE = 2;
    public static final int CASE_INSENSITIVE = 2;
    public static final UnicodeSet EMPTY = null;
    public static final int IGNORE_SPACE = 1;
    public static final int MAX_VALUE = 1114111;
    public static final int MIN_VALUE = 0;

    public enum ComparisonStyle {
        SHORTER_FIRST,
        LEXICOGRAPHIC,
        LONGER_FIRST
    }

    public enum SpanCondition {
        NOT_CONTAINED,
        CONTAINED,
        SIMPLE,
        CONDITION_COUNT
    }

    public UnicodeSet() {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(int... iArr) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(String str) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(String str, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(String str, int i) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(String str, ParsePosition parsePosition, SymbolTable symbolTable) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet(String str, ParsePosition parsePosition, SymbolTable symbolTable, int i) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet set(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet set(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet applyPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet applyPattern(String str, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet applyPattern(String str, int i) {
        throw new RuntimeException("Stub!");
    }

    public String toPattern(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer _generatePattern(StringBuffer stringBuffer, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public StringBuffer _generatePattern(StringBuffer stringBuffer, boolean z, boolean z2) {
        throw new RuntimeException("Stub!");
    }

    public int size() {
        throw new RuntimeException("Stub!");
    }

    public boolean isEmpty() {
        throw new RuntimeException("Stub!");
    }

    public boolean matchesIndexValue(int i) {
        throw new RuntimeException("Stub!");
    }

    public int matches(Replaceable replaceable, int[] iArr, int i, boolean z) {
        throw new RuntimeException("Stub!");
    }

    public void addMatchSetTo(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public int indexOf(int i) {
        throw new RuntimeException("Stub!");
    }

    public int charAt(int i) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet add(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet addAll(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet add(int i) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet add(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet addAll(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet retainAll(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet complementAll(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet removeAll(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet removeAllStrings() {
        throw new RuntimeException("Stub!");
    }

    public static UnicodeSet from(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public static UnicodeSet fromAll(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet retain(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet retain(int i) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet retain(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet remove(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet remove(int i) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet remove(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet complement(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet complement(int i) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet complement() {
        throw new RuntimeException("Stub!");
    }

    public final UnicodeSet complement(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public boolean contains(int i) {
        throw new RuntimeException("Stub!");
    }

    public boolean contains(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final boolean contains(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public boolean containsAll(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public boolean containsAll(String str) {
        throw new RuntimeException("Stub!");
    }

    public boolean containsNone(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public boolean containsNone(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public boolean containsNone(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public final boolean containsSome(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public final boolean containsSome(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public final boolean containsSome(CharSequence charSequence) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet addAll(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet retainAll(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet removeAll(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet complementAll(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet clear() {
        throw new RuntimeException("Stub!");
    }

    public int getRangeCount() {
        throw new RuntimeException("Stub!");
    }

    public int getRangeStart(int i) {
        throw new RuntimeException("Stub!");
    }

    public int getRangeEnd(int i) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet compact() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public <T extends Collection<String>> T addAllTo(T t) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet add(Iterable<?> iterable) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet addAll(Iterable<?> iterable) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet applyIntPropertyValue(int i, int i2) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet applyPropertyAlias(String str, String str2) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet applyPropertyAlias(String str, String str2, SymbolTable symbolTable) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet closeOver(int i) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFrozen() {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet freeze() {
        throw new RuntimeException("Stub!");
    }

    public int span(CharSequence charSequence, SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }

    public int span(CharSequence charSequence, int i, SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }

    public int spanBack(CharSequence charSequence, SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }

    public int spanBack(CharSequence charSequence, int i, SpanCondition spanCondition) {
        throw new RuntimeException("Stub!");
    }

    public UnicodeSet cloneAsThawed() {
        throw new RuntimeException("Stub!");
    }

    public Iterable<EntryRange> ranges() {
        throw new RuntimeException("Stub!");
    }

    public Iterator<String> iterator() {
        throw new RuntimeException("Stub!");
    }

    public <T extends CharSequence> boolean containsAll(Iterable<T> iterable) {
        throw new RuntimeException("Stub!");
    }

    public <T extends CharSequence> boolean containsNone(Iterable<T> iterable) {
        throw new RuntimeException("Stub!");
    }

    public final <T extends CharSequence> boolean containsSome(Iterable<T> iterable) {
        throw new RuntimeException("Stub!");
    }

    public <T extends CharSequence> UnicodeSet addAll(T... tArr) {
        throw new RuntimeException("Stub!");
    }

    public <T extends CharSequence> UnicodeSet removeAll(Iterable<T> iterable) {
        throw new RuntimeException("Stub!");
    }

    public <T extends CharSequence> UnicodeSet retainAll(Iterable<T> iterable) {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(UnicodeSet unicodeSet) {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(UnicodeSet unicodeSet, ComparisonStyle comparisonStyle) {
        throw new RuntimeException("Stub!");
    }

    public int compareTo(Iterable<String> iterable) {
        throw new RuntimeException("Stub!");
    }

    public Collection<String> strings() {
        throw new RuntimeException("Stub!");
    }

    public static class EntryRange {
        public int codepoint;
        public int codepointEnd;

        EntryRange() {
            throw new RuntimeException("Stub!");
        }

        public String toString() {
            throw new RuntimeException("Stub!");
        }
    }
}
