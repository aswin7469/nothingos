package java.security.spec;

public class ECGenParameterSpec extends NamedParameterSpec implements AlgorithmParameterSpec {
    public ECGenParameterSpec(String str) {
        super(str);
    }

    public String getName() {
        return super.getName();
    }
}
