package javax.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;

public class KeyGenerator {
    private static final int I_NONE = 1;
    private static final int I_PARAMS = 3;
    private static final int I_RANDOM = 2;
    private static final int I_SIZE = 4;
    private final String algorithm;
    private int initKeySize;
    private AlgorithmParameterSpec initParams;
    private SecureRandom initRandom;
    private int initType;
    private final Object lock = new Object();
    private Provider provider;
    private Iterator<Provider.Service> serviceIterator;
    private volatile KeyGeneratorSpi spi;

    protected KeyGenerator(KeyGeneratorSpi keyGeneratorSpi, Provider provider2, String str) {
        this.spi = keyGeneratorSpi;
        this.provider = provider2;
        this.algorithm = str;
    }

    private KeyGenerator(String str) throws NoSuchAlgorithmException {
        this.algorithm = str;
        this.serviceIterator = GetInstance.getServices("KeyGenerator", str).iterator();
        this.initType = 1;
        if (nextSpi((KeyGeneratorSpi) null, false) == null) {
            throw new NoSuchAlgorithmException(str + " KeyGenerator not available");
        }
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final KeyGenerator getInstance(String str) throws NoSuchAlgorithmException {
        return new KeyGenerator(str);
    }

    public static final KeyGenerator getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Providers.checkBouncyCastleDeprecation(str2, "KeyGenerator", str);
        GetInstance.Instance instance = JceSecurity.getInstance("KeyGenerator", (Class<?>) KeyGeneratorSpi.class, str, str2);
        return new KeyGenerator((KeyGeneratorSpi) instance.impl, instance.provider, str);
    }

    public static final KeyGenerator getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Providers.checkBouncyCastleDeprecation(provider2, "KeyGenerator", str);
        GetInstance.Instance instance = JceSecurity.getInstance("KeyGenerator", (Class<?>) KeyGeneratorSpi.class, str, provider2);
        return new KeyGenerator((KeyGeneratorSpi) instance.impl, instance.provider, str);
    }

    public final Provider getProvider() {
        Provider provider2;
        synchronized (this.lock) {
            disableFailover();
            provider2 = this.provider;
        }
        return provider2;
    }

    private KeyGeneratorSpi nextSpi(KeyGeneratorSpi keyGeneratorSpi, boolean z) {
        synchronized (this.lock) {
            if (keyGeneratorSpi != null) {
                if (keyGeneratorSpi != this.spi) {
                    KeyGeneratorSpi keyGeneratorSpi2 = this.spi;
                    return keyGeneratorSpi2;
                }
            }
            if (this.serviceIterator == null) {
                return null;
            }
            while (this.serviceIterator.hasNext()) {
                Provider.Service next = this.serviceIterator.next();
                if (JceSecurity.canUseProvider(next.getProvider())) {
                    try {
                        Object newInstance = next.newInstance((Object) null);
                        if (newInstance instanceof KeyGeneratorSpi) {
                            KeyGeneratorSpi keyGeneratorSpi3 = (KeyGeneratorSpi) newInstance;
                            if (z) {
                                int i = this.initType;
                                if (i == 4) {
                                    keyGeneratorSpi3.engineInit(this.initKeySize, this.initRandom);
                                } else if (i == 3) {
                                    keyGeneratorSpi3.engineInit(this.initParams, this.initRandom);
                                } else if (i == 2) {
                                    keyGeneratorSpi3.engineInit(this.initRandom);
                                } else if (i != 1) {
                                    throw new AssertionError((Object) "KeyGenerator initType: " + this.initType);
                                }
                            }
                            this.provider = next.getProvider();
                            this.spi = keyGeneratorSpi3;
                            return keyGeneratorSpi3;
                        }
                    } catch (Exception unused) {
                        continue;
                    }
                }
            }
            disableFailover();
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void disableFailover() {
        this.serviceIterator = null;
        this.initType = 0;
        this.initParams = null;
        this.initRandom = null;
    }

    public final void init(SecureRandom secureRandom) {
        if (this.serviceIterator == null) {
            this.spi.engineInit(secureRandom);
            return;
        }
        KeyGeneratorSpi keyGeneratorSpi = this.spi;
        RuntimeException runtimeException = null;
        do {
            try {
                keyGeneratorSpi.engineInit(secureRandom);
                this.initType = 2;
                this.initKeySize = 0;
                this.initParams = null;
                this.initRandom = secureRandom;
                return;
            } catch (RuntimeException e) {
                if (runtimeException == null) {
                    runtimeException = e;
                }
                keyGeneratorSpi = nextSpi(keyGeneratorSpi, false);
                if (keyGeneratorSpi == null) {
                    throw runtimeException;
                }
            }
        } while (keyGeneratorSpi == null);
        throw runtimeException;
    }

    public final void init(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        init(algorithmParameterSpec, JceSecurity.RANDOM);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0038  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void init(java.security.spec.AlgorithmParameterSpec r5, java.security.SecureRandom r6) throws java.security.InvalidAlgorithmParameterException {
        /*
            r4 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r4.serviceIterator
            if (r0 != 0) goto L_0x000a
            javax.crypto.KeyGeneratorSpi r4 = r4.spi
            r4.engineInit((java.security.spec.AlgorithmParameterSpec) r5, (java.security.SecureRandom) r6)
            return
        L_0x000a:
            javax.crypto.KeyGeneratorSpi r0 = r4.spi
            r1 = 0
        L_0x000d:
            r2 = 0
            r0.engineInit((java.security.spec.AlgorithmParameterSpec) r5, (java.security.SecureRandom) r6)     // Catch:{ Exception -> 0x001b }
            r3 = 3
            r4.initType = r3     // Catch:{ Exception -> 0x001b }
            r4.initKeySize = r2     // Catch:{ Exception -> 0x001b }
            r4.initParams = r5     // Catch:{ Exception -> 0x001b }
            r4.initRandom = r6     // Catch:{ Exception -> 0x001b }
            return
        L_0x001b:
            r3 = move-exception
            if (r1 != 0) goto L_0x001f
            r1 = r3
        L_0x001f:
            javax.crypto.KeyGeneratorSpi r0 = r4.nextSpi(r0, r2)
            if (r0 != 0) goto L_0x000d
            boolean r4 = r1 instanceof java.security.InvalidAlgorithmParameterException
            if (r4 != 0) goto L_0x0038
            boolean r4 = r1 instanceof java.lang.RuntimeException
            if (r4 == 0) goto L_0x0030
            java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1
            throw r1
        L_0x0030:
            java.security.InvalidAlgorithmParameterException r4 = new java.security.InvalidAlgorithmParameterException
            java.lang.String r5 = "init() failed"
            r4.<init>(r5, r1)
            throw r4
        L_0x0038:
            java.security.InvalidAlgorithmParameterException r1 = (java.security.InvalidAlgorithmParameterException) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.KeyGenerator.init(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void");
    }

    public final void init(int i) {
        init(i, JceSecurity.RANDOM);
    }

    public final void init(int i, SecureRandom secureRandom) {
        if (this.serviceIterator == null) {
            this.spi.engineInit(i, secureRandom);
            return;
        }
        KeyGeneratorSpi keyGeneratorSpi = this.spi;
        RuntimeException runtimeException = null;
        do {
            try {
                keyGeneratorSpi.engineInit(i, secureRandom);
                this.initType = 4;
                this.initKeySize = i;
                this.initParams = null;
                this.initRandom = secureRandom;
                return;
            } catch (RuntimeException e) {
                if (runtimeException == null) {
                    runtimeException = e;
                }
                keyGeneratorSpi = nextSpi(keyGeneratorSpi, false);
                if (keyGeneratorSpi == null) {
                    throw runtimeException;
                }
            }
        } while (keyGeneratorSpi == null);
        throw runtimeException;
    }

    public final SecretKey generateKey() {
        if (this.serviceIterator == null) {
            return this.spi.engineGenerateKey();
        }
        KeyGeneratorSpi keyGeneratorSpi = this.spi;
        RuntimeException runtimeException = null;
        do {
            try {
                return keyGeneratorSpi.engineGenerateKey();
            } catch (RuntimeException e) {
                if (runtimeException == null) {
                    runtimeException = e;
                }
                keyGeneratorSpi = nextSpi(keyGeneratorSpi, true);
                if (keyGeneratorSpi == null) {
                    throw runtimeException;
                }
            }
        } while (keyGeneratorSpi == null);
        throw runtimeException;
    }
}
