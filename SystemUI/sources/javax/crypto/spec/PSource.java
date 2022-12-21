package javax.crypto.spec;

public class PSource {
    private String pSrcName;

    protected PSource(String str) {
        if (str != null) {
            this.pSrcName = str;
            return;
        }
        throw new NullPointerException("pSource algorithm is null");
    }

    public String getAlgorithm() {
        return this.pSrcName;
    }

    public static final class PSpecified extends PSource {
        public static final PSpecified DEFAULT = new PSpecified(new byte[0]);

        /* renamed from: p */
        private byte[] f824p = new byte[0];

        public PSpecified(byte[] bArr) {
            super("PSpecified");
            this.f824p = (byte[]) bArr.clone();
        }

        public byte[] getValue() {
            byte[] bArr = this.f824p;
            return bArr.length == 0 ? bArr : (byte[]) bArr.clone();
        }
    }
}
