package java.security.spec;

public class PSSParameterSpec implements AlgorithmParameterSpec {
    public static final PSSParameterSpec DEFAULT = new PSSParameterSpec();
    private String mdName = "SHA-1";
    private String mgfName = "MGF1";
    private AlgorithmParameterSpec mgfSpec = MGF1ParameterSpec.SHA1;
    private int saltLen = 20;
    private int trailerField = 1;

    private PSSParameterSpec() {
    }

    public PSSParameterSpec(String str, String str2, AlgorithmParameterSpec algorithmParameterSpec, int i, int i2) {
        if (str == null) {
            throw new NullPointerException("digest algorithm is null");
        } else if (str2 == null) {
            throw new NullPointerException("mask generation function algorithm is null");
        } else if (i < 0) {
            throw new IllegalArgumentException("negative saltLen value: " + i);
        } else if (i2 >= 0) {
            this.mdName = str;
            this.mgfName = str2;
            this.mgfSpec = algorithmParameterSpec;
            this.saltLen = i;
            this.trailerField = i2;
        } else {
            throw new IllegalArgumentException("negative trailerField: " + i2);
        }
    }

    public PSSParameterSpec(int i) {
        if (i >= 0) {
            this.saltLen = i;
            return;
        }
        throw new IllegalArgumentException("negative saltLen value: " + i);
    }

    public String getDigestAlgorithm() {
        return this.mdName;
    }

    public String getMGFAlgorithm() {
        return this.mgfName;
    }

    public AlgorithmParameterSpec getMGFParameters() {
        return this.mgfSpec;
    }

    public int getSaltLength() {
        return this.saltLen;
    }

    public int getTrailerField() {
        return this.trailerField;
    }
}
