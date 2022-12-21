package java.security;

import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import java.util.Objects;
import sun.security.jca.GetInstance;
import sun.security.jca.JCAUtil;
import sun.security.jca.Providers;

public abstract class KeyPairGenerator extends KeyPairGeneratorSpi {
    private final String algorithm;
    Provider provider;

    /* access modifiers changed from: package-private */
    public void disableFailover() {
    }

    public KeyPair generateKeyPair() {
        return null;
    }

    public void initialize(int i, SecureRandom secureRandom) {
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
    }

    protected KeyPairGenerator(String str) {
        this.algorithm = str;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    private static KeyPairGenerator getInstance(GetInstance.Instance instance, String str) {
        KeyPairGenerator keyPairGenerator;
        if (instance.impl instanceof KeyPairGenerator) {
            keyPairGenerator = (KeyPairGenerator) instance.impl;
        } else {
            keyPairGenerator = new Delegate((KeyPairGeneratorSpi) instance.impl, str);
        }
        keyPairGenerator.provider = instance.provider;
        return keyPairGenerator;
    }

    public static KeyPairGenerator getInstance(String str) throws NoSuchAlgorithmException {
        Objects.requireNonNull(str, "null algorithm name");
        Iterator<Provider.Service> it = GetInstance.getServices("KeyPairGenerator", str).iterator();
        if (it.hasNext()) {
            NoSuchAlgorithmException noSuchAlgorithmException = null;
            do {
                try {
                    GetInstance.Instance instance = GetInstance.getInstance(it.next(), KeyPairGeneratorSpi.class);
                    if (instance.impl instanceof KeyPairGenerator) {
                        return getInstance(instance, str);
                    }
                    return new Delegate(instance, it, str);
                } catch (NoSuchAlgorithmException e) {
                    if (noSuchAlgorithmException == null) {
                        noSuchAlgorithmException = e;
                    }
                    if (!it.hasNext()) {
                        throw noSuchAlgorithmException;
                    }
                }
            } while (!it.hasNext());
            throw noSuchAlgorithmException;
        }
        throw new NoSuchAlgorithmException(str + " KeyPairGenerator not available");
    }

    public static KeyPairGenerator getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Objects.requireNonNull(str, "null algorithm name");
        Providers.checkBouncyCastleDeprecation(str2, "KeyPairGenerator", str);
        return getInstance(GetInstance.getInstance("KeyPairGenerator", (Class<?>) KeyPairGeneratorSpi.class, str, str2), str);
    }

    public static KeyPairGenerator getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Objects.requireNonNull(str, "null algorithm name");
        Providers.checkBouncyCastleDeprecation(provider2, "KeyPairGenerator", str);
        return getInstance(GetInstance.getInstance("KeyPairGenerator", (Class<?>) KeyPairGeneratorSpi.class, str, provider2), str);
    }

    public final Provider getProvider() {
        disableFailover();
        return this.provider;
    }

    public void initialize(int i) {
        initialize(i, JCAUtil.getSecureRandom());
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        initialize(algorithmParameterSpec, JCAUtil.getSecureRandom());
    }

    public final KeyPair genKeyPair() {
        return generateKeyPair();
    }

    private static final class Delegate extends KeyPairGenerator {
        private static final int I_NONE = 1;
        private static final int I_PARAMS = 3;
        private static final int I_SIZE = 2;
        private int initKeySize;
        private AlgorithmParameterSpec initParams;
        private SecureRandom initRandom;
        private int initType;
        private final Object lock = new Object();
        private Iterator<Provider.Service> serviceIterator;
        private volatile KeyPairGeneratorSpi spi;

        Delegate(KeyPairGeneratorSpi keyPairGeneratorSpi, String str) {
            super(str);
            this.spi = keyPairGeneratorSpi;
        }

        Delegate(GetInstance.Instance instance, Iterator<Provider.Service> it, String str) {
            super(str);
            this.spi = (KeyPairGeneratorSpi) instance.impl;
            this.provider = instance.provider;
            this.serviceIterator = it;
            this.initType = 1;
        }

        private KeyPairGeneratorSpi nextSpi(KeyPairGeneratorSpi keyPairGeneratorSpi, boolean z) {
            synchronized (this.lock) {
                if (keyPairGeneratorSpi != null) {
                    if (keyPairGeneratorSpi != this.spi) {
                        KeyPairGeneratorSpi keyPairGeneratorSpi2 = this.spi;
                        return keyPairGeneratorSpi2;
                    }
                }
                if (this.serviceIterator == null) {
                    return null;
                }
                while (this.serviceIterator.hasNext()) {
                    Provider.Service next = this.serviceIterator.next();
                    try {
                        Object newInstance = next.newInstance((Object) null);
                        if (newInstance instanceof KeyPairGeneratorSpi) {
                            if (!(newInstance instanceof KeyPairGenerator)) {
                                KeyPairGeneratorSpi keyPairGeneratorSpi3 = (KeyPairGeneratorSpi) newInstance;
                                if (z) {
                                    int i = this.initType;
                                    if (i == 2) {
                                        keyPairGeneratorSpi3.initialize(this.initKeySize, this.initRandom);
                                    } else if (i == 3) {
                                        keyPairGeneratorSpi3.initialize(this.initParams, this.initRandom);
                                    } else if (i != 1) {
                                        throw new AssertionError((Object) "KeyPairGenerator initType: " + this.initType);
                                    }
                                }
                                this.provider = next.getProvider();
                                this.spi = keyPairGeneratorSpi3;
                                return keyPairGeneratorSpi3;
                            }
                        }
                    } catch (Exception unused) {
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

        public void initialize(int i, SecureRandom secureRandom) {
            if (this.serviceIterator == null) {
                this.spi.initialize(i, secureRandom);
                return;
            }
            KeyPairGeneratorSpi keyPairGeneratorSpi = this.spi;
            RuntimeException runtimeException = null;
            do {
                try {
                    keyPairGeneratorSpi.initialize(i, secureRandom);
                    this.initType = 2;
                    this.initKeySize = i;
                    this.initParams = null;
                    this.initRandom = secureRandom;
                    return;
                } catch (RuntimeException e) {
                    if (runtimeException == null) {
                        runtimeException = e;
                    }
                    keyPairGeneratorSpi = nextSpi(keyPairGeneratorSpi, false);
                    if (keyPairGeneratorSpi == null) {
                        throw runtimeException;
                    }
                }
            } while (keyPairGeneratorSpi == null);
            throw runtimeException;
        }

        /* JADX WARNING: Removed duplicated region for block: B:16:0x0029  */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x002c  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void initialize(java.security.spec.AlgorithmParameterSpec r5, java.security.SecureRandom r6) throws java.security.InvalidAlgorithmParameterException {
            /*
                r4 = this;
                java.util.Iterator<java.security.Provider$Service> r0 = r4.serviceIterator
                if (r0 != 0) goto L_0x000a
                java.security.KeyPairGeneratorSpi r4 = r4.spi
                r4.initialize((java.security.spec.AlgorithmParameterSpec) r5, (java.security.SecureRandom) r6)
                return
            L_0x000a:
                java.security.KeyPairGeneratorSpi r0 = r4.spi
                r1 = 0
            L_0x000d:
                r2 = 0
                r0.initialize((java.security.spec.AlgorithmParameterSpec) r5, (java.security.SecureRandom) r6)     // Catch:{ Exception -> 0x001b }
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
                java.security.KeyPairGeneratorSpi r0 = r4.nextSpi(r0, r2)
                if (r0 != 0) goto L_0x000d
                boolean r4 = r1 instanceof java.lang.RuntimeException
                if (r4 == 0) goto L_0x002c
                java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1
                throw r1
            L_0x002c:
                java.security.InvalidAlgorithmParameterException r1 = (java.security.InvalidAlgorithmParameterException) r1
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.security.KeyPairGenerator.Delegate.initialize(java.security.spec.AlgorithmParameterSpec, java.security.SecureRandom):void");
        }

        public KeyPair generateKeyPair() {
            if (this.serviceIterator == null) {
                return this.spi.generateKeyPair();
            }
            KeyPairGeneratorSpi keyPairGeneratorSpi = this.spi;
            RuntimeException runtimeException = null;
            do {
                try {
                    return keyPairGeneratorSpi.generateKeyPair();
                } catch (RuntimeException e) {
                    if (runtimeException == null) {
                        runtimeException = e;
                    }
                    keyPairGeneratorSpi = nextSpi(keyPairGeneratorSpi, true);
                    if (keyPairGeneratorSpi == null) {
                        throw runtimeException;
                    }
                }
            } while (keyPairGeneratorSpi == null);
            throw runtimeException;
        }
    }
}
