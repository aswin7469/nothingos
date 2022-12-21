package java.text;

import java.p026io.Serializable;
import java.text.AttributedCharacterIterator;

public abstract class Format implements Serializable, Cloneable {
    private static final long serialVersionUID = -299282585814624189L;

    interface FieldDelegate {
        void formatted(int i, Field field, Object obj, int i2, int i3, StringBuffer stringBuffer);

        void formatted(Field field, Object obj, int i, int i2, StringBuffer stringBuffer);
    }

    public abstract StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Object parseObject(String str, ParsePosition parsePosition);

    protected Format() {
    }

    public final String format(Object obj) {
        return format(obj, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        return createAttributedCharacterIterator(format(obj));
    }

    public Object parseObject(String str) throws ParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Object parseObject = parseObject(str, parsePosition);
        if (parsePosition.index != 0) {
            return parseObject;
        }
        throw new ParseException("Format.parseObject(String) failed", parsePosition.errorIndex);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public AttributedCharacterIterator createAttributedCharacterIterator(String str) {
        return new AttributedString(str).getIterator();
    }

    /* access modifiers changed from: package-private */
    public AttributedCharacterIterator createAttributedCharacterIterator(AttributedCharacterIterator[] attributedCharacterIteratorArr) {
        return new AttributedString(attributedCharacterIteratorArr).getIterator();
    }

    /* access modifiers changed from: package-private */
    public AttributedCharacterIterator createAttributedCharacterIterator(String str, AttributedCharacterIterator.Attribute attribute, Object obj) {
        AttributedString attributedString = new AttributedString(str);
        attributedString.addAttribute(attribute, obj);
        return attributedString.getIterator();
    }

    /* access modifiers changed from: package-private */
    public AttributedCharacterIterator createAttributedCharacterIterator(AttributedCharacterIterator attributedCharacterIterator, AttributedCharacterIterator.Attribute attribute, Object obj) {
        AttributedString attributedString = new AttributedString(attributedCharacterIterator);
        attributedString.addAttribute(attribute, obj);
        return attributedString.getIterator();
    }

    public static class Field extends AttributedCharacterIterator.Attribute {
        private static final long serialVersionUID = 276966692217360283L;

        protected Field(String str) {
            super(str);
        }
    }
}
