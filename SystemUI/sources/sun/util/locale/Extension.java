package sun.util.locale;

class Extension {

    /* renamed from: id */
    private String f936id;
    private final char key;
    private String value;

    protected Extension(char c) {
        this.key = c;
    }

    Extension(char c, String str) {
        this.key = c;
        setValue(str);
    }

    /* access modifiers changed from: protected */
    public void setValue(String str) {
        this.value = str;
        this.f936id = this.key + LanguageTag.SEP + str;
    }

    public char getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getID() {
        return this.f936id;
    }

    public String toString() {
        return getID();
    }
}
