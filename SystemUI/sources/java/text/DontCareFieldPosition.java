package java.text;

import java.text.Format;

class DontCareFieldPosition extends FieldPosition {
    static final FieldPosition INSTANCE = new DontCareFieldPosition();
    private final Format.FieldDelegate noDelegate = new Format.FieldDelegate() {
        public void formatted(int i, Format.Field field, Object obj, int i2, int i3, StringBuffer stringBuffer) {
        }

        public void formatted(Format.Field field, Object obj, int i, int i2, StringBuffer stringBuffer) {
        }
    };

    private DontCareFieldPosition() {
        super(0);
    }

    /* access modifiers changed from: package-private */
    public Format.FieldDelegate getFieldDelegate() {
        return this.noDelegate;
    }
}
