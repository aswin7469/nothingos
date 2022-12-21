package java.text;

import android.icu.text.Normalizer2;
import java.util.function.Supplier;

public final class Normalizer {
    private Normalizer() {
    }

    public enum Form {
        NFD(new Normalizer$Form$$ExternalSyntheticLambda0()),
        NFC(new Normalizer$Form$$ExternalSyntheticLambda1()),
        NFKD(new Normalizer$Form$$ExternalSyntheticLambda2()),
        NFKC(new Normalizer$Form$$ExternalSyntheticLambda3());
        
        /* access modifiers changed from: private */
        public final Supplier<Normalizer2> icuNormalizer;

        private Form(Supplier<Normalizer2> supplier) {
            this.icuNormalizer = supplier;
        }
    }

    public static String normalize(CharSequence charSequence, Form form) {
        return ((Normalizer2) form.icuNormalizer.get()).normalize(charSequence);
    }

    public static boolean isNormalized(CharSequence charSequence, Form form) {
        return ((Normalizer2) form.icuNormalizer.get()).isNormalized(charSequence);
    }
}
