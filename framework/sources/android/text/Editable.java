package android.text;
/* loaded from: classes3.dex */
public interface Editable extends CharSequence, GetChars, Spannable, Appendable {
    @Override // java.lang.Appendable
    /* renamed from: append */
    Editable mo2719append(char c);

    @Override // java.lang.Appendable
    /* renamed from: append */
    Editable mo2720append(CharSequence charSequence);

    @Override // java.lang.Appendable
    /* renamed from: append */
    Editable mo2721append(CharSequence charSequence, int i, int i2);

    void clear();

    void clearSpans();

    /* renamed from: delete */
    Editable mo2733delete(int i, int i2);

    InputFilter[] getFilters();

    /* renamed from: insert */
    Editable mo2734insert(int i, CharSequence charSequence);

    /* renamed from: insert */
    Editable mo2735insert(int i, CharSequence charSequence, int i2, int i3);

    /* renamed from: replace */
    Editable mo2736replace(int i, int i2, CharSequence charSequence);

    /* renamed from: replace */
    Editable mo2737replace(int i, int i2, CharSequence charSequence, int i3, int i4);

    void setFilters(InputFilter[] inputFilterArr);

    /* loaded from: classes3.dex */
    public static class Factory {
        private static Factory sInstance = new Factory();

        public static Factory getInstance() {
            return sInstance;
        }

        public Editable newEditable(CharSequence source) {
            return new SpannableStringBuilder(source);
        }
    }
}
