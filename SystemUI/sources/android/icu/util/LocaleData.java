package android.icu.util;

public final class LocaleData {
    public static final int ALT_QUOTATION_END = 3;
    public static final int ALT_QUOTATION_START = 2;
    public static final int QUOTATION_END = 1;
    public static final int QUOTATION_START = 0;

    private LocaleData() {
        throw new RuntimeException("Stub!");
    }

    public static LocaleData getInstance(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static LocaleData getInstance() {
        throw new RuntimeException("Stub!");
    }

    public void setNoSubstitute(boolean z) {
        throw new RuntimeException("Stub!");
    }

    public boolean getNoSubstitute() {
        throw new RuntimeException("Stub!");
    }

    public String getDelimiter(int i) {
        throw new RuntimeException("Stub!");
    }

    public static MeasurementSystem getMeasurementSystem(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static PaperSize getPaperSize(ULocale uLocale) {
        throw new RuntimeException("Stub!");
    }

    public static VersionInfo getCLDRVersion() {
        throw new RuntimeException("Stub!");
    }

    public static final class MeasurementSystem {

        /* renamed from: SI */
        public static final MeasurementSystem f32SI = null;

        /* renamed from: UK */
        public static final MeasurementSystem f33UK = null;

        /* renamed from: US */
        public static final MeasurementSystem f34US = null;

        private MeasurementSystem() {
            throw new RuntimeException("Stub!");
        }
    }

    public static final class PaperSize {
        private PaperSize() {
            throw new RuntimeException("Stub!");
        }

        public int getHeight() {
            throw new RuntimeException("Stub!");
        }

        public int getWidth() {
            throw new RuntimeException("Stub!");
        }
    }
}
