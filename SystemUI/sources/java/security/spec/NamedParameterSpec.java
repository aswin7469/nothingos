package java.security.spec;

import java.util.Objects;

public class NamedParameterSpec implements AlgorithmParameterSpec {
    public static final NamedParameterSpec ED25519 = new NamedParameterSpec("Ed25519");
    public static final NamedParameterSpec ED448 = new NamedParameterSpec("Ed448");
    public static final NamedParameterSpec X25519 = new NamedParameterSpec("X25519");
    public static final NamedParameterSpec X448 = new NamedParameterSpec("X448");
    private String name;

    public NamedParameterSpec(String str) {
        Objects.requireNonNull(str, "stdName must not be null");
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
