package android.icu.text;

import android.icu.text.MessagePattern;
import android.icu.util.ULocale;
import java.p026io.InvalidObjectException;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MessageFormat extends UFormat {
    public MessageFormat(String str) {
        throw new RuntimeException("Stub!");
    }

    public MessageFormat(String str, Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public MessageFormat(String str, ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public void setLocale(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public void setLocale(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public Locale getLocale() {
        throw new RuntimeException("Stub!");
    }

    public ULocale getULocale() {
        throw new RuntimeException("Stub!");
    }

    public void applyPattern(String str) {
        throw new RuntimeException("Stub!");
    }

    public void applyPattern(String str, MessagePattern.ApostropheMode apostropheMode) {
        throw new RuntimeException("Stub!");
    }

    public MessagePattern.ApostropheMode getApostropheMode() {
        throw new RuntimeException("Stub!");
    }

    public String toPattern() {
        throw new RuntimeException("Stub!");
    }

    public void setFormatsByArgumentIndex(Format[] formatArr) {
        throw new RuntimeException("Stub!");
    }

    public void setFormatsByArgumentName(Map<String, Format> map) {
        throw new RuntimeException("Stub!");
    }

    public void setFormats(Format[] formatArr) {
        throw new RuntimeException("Stub!");
    }

    public void setFormatByArgumentIndex(int i, Format format) {
        throw new RuntimeException("Stub!");
    }

    public void setFormatByArgumentName(String str, Format format) {
        throw new RuntimeException("Stub!");
    }

    public void setFormat(int i, Format format) {
        throw new RuntimeException("Stub!");
    }

    public Format[] getFormatsByArgumentIndex() {
        throw new RuntimeException("Stub!");
    }

    public Format[] getFormats() {
        throw new RuntimeException("Stub!");
    }

    public Set<String> getArgumentNames() {
        throw new RuntimeException("Stub!");
    }

    public Format getFormatByArgumentName(String str) {
        throw new RuntimeException("Stub!");
    }

    public final StringBuffer format(Object[] objArr, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public final StringBuffer format(Map<String, Object> map, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public static String format(String str, Object... objArr) {
        throw new RuntimeException("Stub!");
    }

    public static String format(String str, Map<String, Object> map) {
        throw new RuntimeException("Stub!");
    }

    public boolean usesNamedArguments() {
        throw new RuntimeException("Stub!");
    }

    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        throw new RuntimeException("Stub!");
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public Object[] parse(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public Map<String, Object> parseToMap(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public Object[] parse(String str) throws ParseException {
        throw new RuntimeException("Stub!");
    }

    public Map<String, Object> parseToMap(String str) throws ParseException {
        throw new RuntimeException("Stub!");
    }

    public Object parseObject(String str, ParsePosition parsePosition) {
        throw new RuntimeException("Stub!");
    }

    public Object clone() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public static String autoQuoteApostrophe(String str) {
        throw new RuntimeException("Stub!");
    }

    public static class Field extends Format.Field {
        public static final Field ARGUMENT = null;

        protected Field(String str) {
            super((String) null);
            throw new RuntimeException("Stub!");
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws InvalidObjectException {
            throw new RuntimeException("Stub!");
        }
    }
}
