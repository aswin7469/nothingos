package javax.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;

public class KeyAgreement {
    private static final int I_NO_PARAMS = 1;
    private static final int I_PARAMS = 2;
    private static int warnCount = 10;
    private final String algorithm;
    private final Object lock;
    private Provider provider;
    private KeyAgreementSpi spi;

    protected KeyAgreement(KeyAgreementSpi keyAgreementSpi, Provider provider2, String str) {
        this.spi = keyAgreementSpi;
        this.provider = provider2;
        this.algorithm = str;
        this.lock = null;
    }

    private KeyAgreement(String str) {
        this.algorithm = str;
        this.lock = new Object();
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final KeyAgreement getInstance(String str) throws NoSuchAlgorithmException {
        for (Provider.Service provider2 : GetInstance.getServices("KeyAgreement", str)) {
            if (JceSecurity.canUseProvider(provider2.getProvider())) {
                return new KeyAgreement(str);
            }
        }
        throw new NoSuchAlgorithmException("Algorithm " + str + " not available");
    }

    public static final KeyAgreement getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Providers.checkBouncyCastleDeprecation(str2, "KeyAgreement", str);
        GetInstance.Instance instance = JceSecurity.getInstance("KeyAgreement", (Class<?>) KeyAgreementSpi.class, str, str2);
        return new KeyAgreement((KeyAgreementSpi) instance.impl, instance.provider, str);
    }

    public static final KeyAgreement getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Providers.checkBouncyCastleDeprecation(provider2, "KeyAgreement", str);
        GetInstance.Instance instance = JceSecurity.getInstance("KeyAgreement", (Class<?>) KeyAgreementSpi.class, str, provider2);
        return new KeyAgreement((KeyAgreementSpi) instance.impl, instance.provider, str);
    }

    /* access modifiers changed from: package-private */
    public void chooseFirstProvider() {
        if (this.spi == null) {
            synchronized (this.lock) {
                if (this.spi == null) {
                    Exception e = null;
                    for (Provider.Service next : GetInstance.getServices("KeyAgreement", this.algorithm)) {
                        if (JceSecurity.canUseProvider(next.getProvider())) {
                            try {
                                Object newInstance = next.newInstance((Object) null);
                                if (newInstance instanceof KeyAgreementSpi) {
                                    this.spi = (KeyAgreementSpi) newInstance;
                                    this.provider = next.getProvider();
                                    return;
                                }
                            } catch (Exception e2) {
                                e = e2;
                            }
                        }
                    }
                    ProviderException providerException = new ProviderException("Could not construct KeyAgreementSpi instance");
                    if (e != null) {
                        providerException.initCause(e);
                    }
                    throw providerException;
                }
            }
        }
    }

    private void implInit(KeyAgreementSpi keyAgreementSpi, int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (i == 1) {
            keyAgreementSpi.engineInit(key, secureRandom);
        } else {
            keyAgreementSpi.engineInit(key, algorithmParameterSpec, secureRandom);
        }
    }

    private void chooseProvider(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        Key key2 = key;
        synchronized (this.lock) {
            KeyAgreementSpi keyAgreementSpi = this.spi;
            if (keyAgreementSpi == null || key2 != null) {
                Exception exc = null;
                for (Provider.Service next : GetInstance.getServices("KeyAgreement", this.algorithm)) {
                    if (next.supportsParameter(key2)) {
                        if (!JceSecurity.canUseProvider(next.getProvider())) {
                            continue;
                        } else {
                            try {
                                KeyAgreementSpi keyAgreementSpi2 = (KeyAgreementSpi) next.newInstance((Object) null);
                                implInit(keyAgreementSpi2, i, key, algorithmParameterSpec, secureRandom);
                                this.provider = next.getProvider();
                                this.spi = keyAgreementSpi2;
                                return;
                            } catch (Exception e) {
                                if (exc == null) {
                                    exc = e;
                                }
                            }
                        }
                    }
                }
                if (exc instanceof InvalidKeyException) {
                    throw ((InvalidKeyException) exc);
                } else if (exc instanceof InvalidAlgorithmParameterException) {
                    throw ((InvalidAlgorithmParameterException) exc);
                } else if (!(exc instanceof RuntimeException)) {
                    throw new InvalidKeyException("No installed provider supports this key: " + (key2 != null ? key.getClass().getName() : "(null)"), exc);
                } else {
                    throw exc;
                }
            } else {
                implInit(keyAgreementSpi, i, key, algorithmParameterSpec, secureRandom);
            }
        }
    }

    public final Provider getProvider() {
        chooseFirstProvider();
        return this.provider;
    }

    public final void init(Key key) throws InvalidKeyException {
        init(key, JceSecurity.RANDOM);
    }

    public final void init(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        KeyAgreementSpi keyAgreementSpi = this.spi;
        if (keyAgreementSpi == null || !(key == null || this.lock == null)) {
            try {
                chooseProvider(1, key, (AlgorithmParameterSpec) null, secureRandom);
            } catch (InvalidAlgorithmParameterException e) {
                throw new InvalidKeyException((Throwable) e);
            }
        } else {
            keyAgreementSpi.engineInit(key, secureRandom);
        }
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(key, algorithmParameterSpec, JceSecurity.RANDOM);
    }

    private String getProviderName() {
        Provider provider2 = this.provider;
        return provider2 == null ? "(no provider)" : provider2.getName();
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        KeyAgreementSpi keyAgreementSpi = this.spi;
        if (keyAgreementSpi != null) {
            keyAgreementSpi.engineInit(key, algorithmParameterSpec, secureRandom);
        } else {
            chooseProvider(2, key, algorithmParameterSpec, secureRandom);
        }
    }

    public final Key doPhase(Key key, boolean z) throws InvalidKeyException, IllegalStateException {
        chooseFirstProvider();
        return this.spi.engineDoPhase(key, z);
    }

    public final byte[] generateSecret() throws IllegalStateException {
        chooseFirstProvider();
        return this.spi.engineGenerateSecret();
    }

    public final int generateSecret(byte[] bArr, int i) throws IllegalStateException, ShortBufferException {
        chooseFirstProvider();
        return this.spi.engineGenerateSecret(bArr, i);
    }

    public final SecretKey generateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        chooseFirstProvider();
        return this.spi.engineGenerateSecret(str);
    }
}
