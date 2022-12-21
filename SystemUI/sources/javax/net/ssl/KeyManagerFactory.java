package javax.net.ssl;

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import sun.security.jca.GetInstance;

public class KeyManagerFactory {
    private String algorithm;
    private KeyManagerFactorySpi factorySpi;
    private Provider provider;

    public static final String getDefaultAlgorithm() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return Security.getProperty("ssl.KeyManagerFactory.algorithm");
            }
        });
        return str == null ? "SunX509" : str;
    }

    protected KeyManagerFactory(KeyManagerFactorySpi keyManagerFactorySpi, Provider provider2, String str) {
        this.factorySpi = keyManagerFactorySpi;
        this.provider = provider2;
        this.algorithm = str;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final KeyManagerFactory getInstance(String str) throws NoSuchAlgorithmException {
        GetInstance.Instance instance = GetInstance.getInstance("KeyManagerFactory", (Class<?>) KeyManagerFactorySpi.class, str);
        return new KeyManagerFactory((KeyManagerFactorySpi) instance.impl, instance.provider, str);
    }

    public static final KeyManagerFactory getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance instance = GetInstance.getInstance("KeyManagerFactory", (Class<?>) KeyManagerFactorySpi.class, str, str2);
        return new KeyManagerFactory((KeyManagerFactorySpi) instance.impl, instance.provider, str);
    }

    public static final KeyManagerFactory getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        GetInstance.Instance instance = GetInstance.getInstance("KeyManagerFactory", (Class<?>) KeyManagerFactorySpi.class, str, provider2);
        return new KeyManagerFactory((KeyManagerFactorySpi) instance.impl, instance.provider, str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final void init(KeyStore keyStore, char[] cArr) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        this.factorySpi.engineInit(keyStore, cArr);
    }

    public final void init(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        this.factorySpi.engineInit(managerFactoryParameters);
    }

    public final KeyManager[] getKeyManagers() {
        return this.factorySpi.engineGetKeyManagers();
    }
}
