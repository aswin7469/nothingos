package java.text;

import libcore.icu.CollationKeyICU;

public class RuleBasedCollator extends Collator {
    RuleBasedCollator(android.icu.text.RuleBasedCollator ruleBasedCollator) {
        super(ruleBasedCollator);
    }

    public RuleBasedCollator(String str) throws ParseException {
        if (str != null) {
            try {
                this.icuColl = new android.icu.text.RuleBasedCollator(str);
            } catch (Exception e) {
                if (e instanceof ParseException) {
                    throw ((ParseException) e);
                }
                throw new ParseException(e.getMessage(), -1);
            }
        } else {
            throw new NullPointerException("rules == null");
        }
    }

    public String getRules() {
        return collAsICU().getRules();
    }

    public CollationElementIterator getCollationElementIterator(String str) {
        if (str != null) {
            return new CollationElementIterator(collAsICU().getCollationElementIterator(str));
        }
        throw new NullPointerException("source == null");
    }

    public CollationElementIterator getCollationElementIterator(CharacterIterator characterIterator) {
        if (characterIterator != null) {
            return new CollationElementIterator(collAsICU().getCollationElementIterator(characterIterator));
        }
        throw new NullPointerException("source == null");
    }

    public synchronized int compare(String str, String str2) {
        if (str == null || str2 == null) {
            throw new NullPointerException();
        }
        return this.icuColl.compare(str, str2);
    }

    public synchronized CollationKey getCollationKey(String str) {
        if (str == null) {
            return null;
        }
        return new CollationKeyICU(str, this.icuColl.getCollationKey(str));
    }

    public Object clone() {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return this.icuColl.hashCode();
    }

    private android.icu.text.RuleBasedCollator collAsICU() {
        return (android.icu.text.RuleBasedCollator) this.icuColl;
    }
}
