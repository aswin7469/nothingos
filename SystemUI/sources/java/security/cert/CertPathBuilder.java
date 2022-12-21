package java.security.cert;

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.util.Objects;
import sun.security.jca.GetInstance;

public class CertPathBuilder {
    private static final String CPB_TYPE = "certpathbuilder.type";
    private final String algorithm;
    private final CertPathBuilderSpi builderSpi;
    private final Provider provider;

    protected CertPathBuilder(CertPathBuilderSpi certPathBuilderSpi, Provider provider2, String str) {
        this.builderSpi = certPathBuilderSpi;
        this.provider = provider2;
        this.algorithm = str;
    }

    public static CertPathBuilder getInstance(String str) throws NoSuchAlgorithmException {
        Objects.requireNonNull(str, "null algorithm name");
        GetInstance.Instance instance = GetInstance.getInstance("CertPathBuilder", (Class<?>) CertPathBuilderSpi.class, str);
        return new CertPathBuilder((CertPathBuilderSpi) instance.impl, instance.provider, str);
    }

    public static CertPathBuilder getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Objects.requireNonNull(str, "null algorithm name");
        GetInstance.Instance instance = GetInstance.getInstance("CertPathBuilder", (Class<?>) CertPathBuilderSpi.class, str, str2);
        return new CertPathBuilder((CertPathBuilderSpi) instance.impl, instance.provider, str);
    }

    public static CertPathBuilder getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Objects.requireNonNull(str, "null algorithm name");
        GetInstance.Instance instance = GetInstance.getInstance("CertPathBuilder", (Class<?>) CertPathBuilderSpi.class, str, provider2);
        return new CertPathBuilder((CertPathBuilderSpi) instance.impl, instance.provider, str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public final CertPathBuilderResult build(CertPathParameters certPathParameters) throws CertPathBuilderException, InvalidAlgorithmParameterException {
        return this.builderSpi.engineBuild(certPathParameters);
    }

    public static final String getDefaultType() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return Security.getProperty(CertPathBuilder.CPB_TYPE);
            }
        });
        return str == null ? "PKIX" : str;
    }

    public final CertPathChecker getRevocationChecker() {
        return this.builderSpi.engineGetRevocationChecker();
    }
}
