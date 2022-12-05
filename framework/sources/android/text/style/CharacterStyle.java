package android.text.style;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
/* loaded from: classes3.dex */
public abstract class CharacterStyle {
    public abstract void updateDrawState(TextPaint textPaint);

    public static CharacterStyle wrap(CharacterStyle cs) {
        if (cs instanceof MetricAffectingSpan) {
            return new MetricAffectingSpan.Passthrough((MetricAffectingSpan) cs);
        }
        return new Passthrough(cs);
    }

    /* renamed from: getUnderlying */
    public CharacterStyle mo2745getUnderlying() {
        return this;
    }

    /* loaded from: classes3.dex */
    private static class Passthrough extends CharacterStyle {
        private CharacterStyle mStyle;

        public Passthrough(CharacterStyle cs) {
            this.mStyle = cs;
        }

        @Override // android.text.style.CharacterStyle
        public void updateDrawState(TextPaint tp) {
            this.mStyle.updateDrawState(tp);
        }

        @Override // android.text.style.CharacterStyle
        /* renamed from: getUnderlying */
        public CharacterStyle mo2745getUnderlying() {
            return this.mStyle.mo2745getUnderlying();
        }
    }
}
