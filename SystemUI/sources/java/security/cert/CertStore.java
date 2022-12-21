package java.security.cert;

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.util.Collection;
import java.util.Objects;
import sun.security.jca.GetInstance;

public class CertStore {
    private static final String CERTSTORE_TYPE = "certstore.type";
    private CertStoreParameters params;
    private Provider provider;
    private CertStoreSpi storeSpi;
    private String type;

    protected CertStore(CertStoreSpi certStoreSpi, Provider provider2, String str, CertStoreParameters certStoreParameters) {
        this.storeSpi = certStoreSpi;
        this.provider = provider2;
        this.type = str;
        if (certStoreParameters != null) {
            this.params = (CertStoreParameters) certStoreParameters.clone();
        }
    }

    public final Collection<? extends Certificate> getCertificates(CertSelector certSelector) throws CertStoreException {
        return this.storeSpi.engineGetCertificates(certSelector);
    }

    public final Collection<? extends CRL> getCRLs(CRLSelector cRLSelector) throws CertStoreException {
        return this.storeSpi.engineGetCRLs(cRLSelector);
    }

    public static CertStore getInstance(String str, CertStoreParameters certStoreParameters) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        Objects.requireNonNull(str, "null type name");
        try {
            GetInstance.Instance instance = GetInstance.getInstance("CertStore", (Class<?>) CertStoreSpi.class, str, (Object) certStoreParameters);
            return new CertStore((CertStoreSpi) instance.impl, instance.provider, str, certStoreParameters);
        } catch (NoSuchAlgorithmException e) {
            return handleException(e);
        }
    }

    private static CertStore handleException(NoSuchAlgorithmException noSuchAlgorithmException) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Throwable cause = noSuchAlgorithmException.getCause();
        if (cause instanceof InvalidAlgorithmParameterException) {
            throw ((InvalidAlgorithmParameterException) cause);
        }
        throw noSuchAlgorithmException;
    }

    public static CertStore getInstance(String str, CertStoreParameters certStoreParameters, String str2) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Objects.requireNonNull(str, "null type name");
        try {
            GetInstance.Instance instance = GetInstance.getInstance("CertStore", (Class<?>) CertStoreSpi.class, str, (Object) certStoreParameters, str2);
            return new CertStore((CertStoreSpi) instance.impl, instance.provider, str, certStoreParameters);
        } catch (NoSuchAlgorithmException e) {
            return handleException(e);
        }
    }

    public static CertStore getInstance(String str, CertStoreParameters certStoreParameters, Provider provider2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Objects.requireNonNull(str, "null type name");
        try {
            GetInstance.Instance instance = GetInstance.getInstance("CertStore", (Class<?>) CertStoreSpi.class, str, (Object) certStoreParameters, provider2);
            return new CertStore((CertStoreSpi) instance.impl, instance.provider, str, certStoreParameters);
        } catch (NoSuchAlgorithmException e) {
            return handleException(e);
        }
    }

    public final CertStoreParameters getCertStoreParameters() {
        CertStoreParameters certStoreParameters = this.params;
        if (certStoreParameters == null) {
            return null;
        }
        return (CertStoreParameters) certStoreParameters.clone();
    }

    public final String getType() {
        return this.type;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public static final String getDefaultType() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return Security.getProperty(CertStore.CERTSTORE_TYPE);
            }
        });
        return str == null ? "LDAP" : str;
    }
}
