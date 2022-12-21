package java.security;

import java.security.Provider;
import java.util.Iterator;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;
import sun.security.util.Debug;

public class KeyFactory {
    private static final Debug debug = Debug.getInstance("jca", "KeyFactory");
    private final String algorithm;
    private final Object lock = new Object();
    private Provider provider;
    private Iterator<Provider.Service> serviceIterator;
    private volatile KeyFactorySpi spi;

    protected KeyFactory(KeyFactorySpi keyFactorySpi, Provider provider2, String str) {
        this.spi = keyFactorySpi;
        this.provider = provider2;
        this.algorithm = str;
    }

    private KeyFactory(String str) throws NoSuchAlgorithmException {
        this.algorithm = str;
        this.serviceIterator = GetInstance.getServices("KeyFactory", str).iterator();
        if (nextSpi((KeyFactorySpi) null) == null) {
            throw new NoSuchAlgorithmException(str + " KeyFactory not available");
        }
    }

    public static KeyFactory getInstance(String str) throws NoSuchAlgorithmException {
        return new KeyFactory(str);
    }

    public static KeyFactory getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Providers.checkBouncyCastleDeprecation(str2, "KeyFactory", str);
        GetInstance.Instance instance = GetInstance.getInstance("KeyFactory", (Class<?>) KeyFactorySpi.class, str, str2);
        return new KeyFactory((KeyFactorySpi) instance.impl, instance.provider, str);
    }

    public static KeyFactory getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Providers.checkBouncyCastleDeprecation(provider2, "KeyFactory", str);
        GetInstance.Instance instance = GetInstance.getInstance("KeyFactory", (Class<?>) KeyFactorySpi.class, str, provider2);
        return new KeyFactory((KeyFactorySpi) instance.impl, instance.provider, str);
    }

    public final Provider getProvider() {
        Provider provider2;
        synchronized (this.lock) {
            this.serviceIterator = null;
            provider2 = this.provider;
        }
        return provider2;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    private KeyFactorySpi nextSpi(KeyFactorySpi keyFactorySpi) {
        synchronized (this.lock) {
            if (keyFactorySpi != null) {
                if (keyFactorySpi != this.spi) {
                    KeyFactorySpi keyFactorySpi2 = this.spi;
                    return keyFactorySpi2;
                }
            }
            if (this.serviceIterator == null) {
                return null;
            }
            while (this.serviceIterator.hasNext()) {
                Provider.Service next = this.serviceIterator.next();
                try {
                    Object newInstance = next.newInstance((Object) null);
                    if (newInstance instanceof KeyFactorySpi) {
                        KeyFactorySpi keyFactorySpi3 = (KeyFactorySpi) newInstance;
                        this.provider = next.getProvider();
                        this.spi = keyFactorySpi3;
                        return keyFactorySpi3;
                    }
                } catch (NoSuchAlgorithmException unused) {
                }
            }
            this.serviceIterator = null;
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.security.PublicKey generatePublic(java.security.spec.KeySpec r4) throws java.security.spec.InvalidKeySpecException {
        /*
            r3 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r3.serviceIterator
            if (r0 != 0) goto L_0x000b
            java.security.KeyFactorySpi r3 = r3.spi
            java.security.PublicKey r3 = r3.engineGeneratePublic(r4)
            return r3
        L_0x000b:
            java.security.KeyFactorySpi r0 = r3.spi
            r1 = 0
        L_0x000e:
            java.security.PublicKey r3 = r0.engineGeneratePublic(r4)     // Catch:{ Exception -> 0x0013 }
            return r3
        L_0x0013:
            r2 = move-exception
            if (r1 != 0) goto L_0x0017
            r1 = r2
        L_0x0017:
            java.security.KeyFactorySpi r0 = r3.nextSpi(r0)
            if (r0 != 0) goto L_0x000e
            boolean r3 = r1 instanceof java.lang.RuntimeException
            if (r3 != 0) goto L_0x0030
            boolean r3 = r1 instanceof java.security.spec.InvalidKeySpecException
            if (r3 == 0) goto L_0x0028
            java.security.spec.InvalidKeySpecException r1 = (java.security.spec.InvalidKeySpecException) r1
            throw r1
        L_0x0028:
            java.security.spec.InvalidKeySpecException r3 = new java.security.spec.InvalidKeySpecException
            java.lang.String r4 = "Could not generate public key"
            r3.<init>(r4, r1)
            throw r3
        L_0x0030:
            java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.KeyFactory.generatePublic(java.security.spec.KeySpec):java.security.PublicKey");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.security.PrivateKey generatePrivate(java.security.spec.KeySpec r4) throws java.security.spec.InvalidKeySpecException {
        /*
            r3 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r3.serviceIterator
            if (r0 != 0) goto L_0x000b
            java.security.KeyFactorySpi r3 = r3.spi
            java.security.PrivateKey r3 = r3.engineGeneratePrivate(r4)
            return r3
        L_0x000b:
            java.security.KeyFactorySpi r0 = r3.spi
            r1 = 0
        L_0x000e:
            java.security.PrivateKey r3 = r0.engineGeneratePrivate(r4)     // Catch:{ Exception -> 0x0013 }
            return r3
        L_0x0013:
            r2 = move-exception
            if (r1 != 0) goto L_0x0017
            r1 = r2
        L_0x0017:
            java.security.KeyFactorySpi r0 = r3.nextSpi(r0)
            if (r0 != 0) goto L_0x000e
            boolean r3 = r1 instanceof java.lang.RuntimeException
            if (r3 != 0) goto L_0x0030
            boolean r3 = r1 instanceof java.security.spec.InvalidKeySpecException
            if (r3 == 0) goto L_0x0028
            java.security.spec.InvalidKeySpecException r1 = (java.security.spec.InvalidKeySpecException) r1
            throw r1
        L_0x0028:
            java.security.spec.InvalidKeySpecException r3 = new java.security.spec.InvalidKeySpecException
            java.lang.String r4 = "Could not generate private key"
            r3.<init>(r4, r1)
            throw r3
        L_0x0030:
            java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.KeyFactory.generatePrivate(java.security.spec.KeySpec):java.security.PrivateKey");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <T extends java.security.spec.KeySpec> T getKeySpec(java.security.Key r4, java.lang.Class<T> r5) throws java.security.spec.InvalidKeySpecException {
        /*
            r3 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r3.serviceIterator
            if (r0 != 0) goto L_0x000b
            java.security.KeyFactorySpi r3 = r3.spi
            java.security.spec.KeySpec r3 = r3.engineGetKeySpec(r4, r5)
            return r3
        L_0x000b:
            java.security.KeyFactorySpi r0 = r3.spi
            r1 = 0
        L_0x000e:
            java.security.spec.KeySpec r3 = r0.engineGetKeySpec(r4, r5)     // Catch:{ Exception -> 0x0013 }
            return r3
        L_0x0013:
            r2 = move-exception
            if (r1 != 0) goto L_0x0017
            r1 = r2
        L_0x0017:
            java.security.KeyFactorySpi r0 = r3.nextSpi(r0)
            if (r0 != 0) goto L_0x000e
            boolean r3 = r1 instanceof java.lang.RuntimeException
            if (r3 != 0) goto L_0x0030
            boolean r3 = r1 instanceof java.security.spec.InvalidKeySpecException
            if (r3 == 0) goto L_0x0028
            java.security.spec.InvalidKeySpecException r1 = (java.security.spec.InvalidKeySpecException) r1
            throw r1
        L_0x0028:
            java.security.spec.InvalidKeySpecException r3 = new java.security.spec.InvalidKeySpecException
            java.lang.String r4 = "Could not get key spec"
            r3.<init>(r4, r1)
            throw r3
        L_0x0030:
            java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.KeyFactory.getKeySpec(java.security.Key, java.lang.Class):java.security.spec.KeySpec");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.security.Key translateKey(java.security.Key r4) throws java.security.InvalidKeyException {
        /*
            r3 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r3.serviceIterator
            if (r0 != 0) goto L_0x000b
            java.security.KeyFactorySpi r3 = r3.spi
            java.security.Key r3 = r3.engineTranslateKey(r4)
            return r3
        L_0x000b:
            java.security.KeyFactorySpi r0 = r3.spi
            r1 = 0
        L_0x000e:
            java.security.Key r3 = r0.engineTranslateKey(r4)     // Catch:{ Exception -> 0x0013 }
            return r3
        L_0x0013:
            r2 = move-exception
            if (r1 != 0) goto L_0x0017
            r1 = r2
        L_0x0017:
            java.security.KeyFactorySpi r0 = r3.nextSpi(r0)
            if (r0 != 0) goto L_0x000e
            boolean r3 = r1 instanceof java.lang.RuntimeException
            if (r3 != 0) goto L_0x0030
            boolean r3 = r1 instanceof java.security.InvalidKeyException
            if (r3 == 0) goto L_0x0028
            java.security.InvalidKeyException r1 = (java.security.InvalidKeyException) r1
            throw r1
        L_0x0028:
            java.security.InvalidKeyException r3 = new java.security.InvalidKeyException
            java.lang.String r4 = "Could not translate key"
            r3.<init>(r4, r1)
            throw r3
        L_0x0030:
            java.lang.RuntimeException r1 = (java.lang.RuntimeException) r1
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.KeyFactory.translateKey(java.security.Key):java.security.Key");
    }
}
