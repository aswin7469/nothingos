package javax.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.Iterator;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;

public class SecretKeyFactory {
    private final String algorithm;
    private final Object lock = new Object();
    private Provider provider;
    private Iterator<Provider.Service> serviceIterator;
    private volatile SecretKeyFactorySpi spi;

    protected SecretKeyFactory(SecretKeyFactorySpi secretKeyFactorySpi, Provider provider2, String str) {
        this.spi = secretKeyFactorySpi;
        this.provider = provider2;
        this.algorithm = str;
    }

    private SecretKeyFactory(String str) throws NoSuchAlgorithmException {
        this.algorithm = str;
        this.serviceIterator = GetInstance.getServices("SecretKeyFactory", str).iterator();
        if (nextSpi((SecretKeyFactorySpi) null) == null) {
            throw new NoSuchAlgorithmException(str + " SecretKeyFactory not available");
        }
    }

    public static final SecretKeyFactory getInstance(String str) throws NoSuchAlgorithmException {
        return new SecretKeyFactory(str);
    }

    public static final SecretKeyFactory getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Providers.checkBouncyCastleDeprecation(str2, "SecretKeyFactory", str);
        GetInstance.Instance instance = JceSecurity.getInstance("SecretKeyFactory", (Class<?>) SecretKeyFactorySpi.class, str, str2);
        return new SecretKeyFactory((SecretKeyFactorySpi) instance.impl, instance.provider, str);
    }

    public static final SecretKeyFactory getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Providers.checkBouncyCastleDeprecation(provider2, "SecretKeyFactory", str);
        GetInstance.Instance instance = JceSecurity.getInstance("SecretKeyFactory", (Class<?>) SecretKeyFactorySpi.class, str, provider2);
        return new SecretKeyFactory((SecretKeyFactorySpi) instance.impl, instance.provider, str);
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

    private SecretKeyFactorySpi nextSpi(SecretKeyFactorySpi secretKeyFactorySpi) {
        synchronized (this.lock) {
            if (secretKeyFactorySpi != null) {
                if (secretKeyFactorySpi != this.spi) {
                    SecretKeyFactorySpi secretKeyFactorySpi2 = this.spi;
                    return secretKeyFactorySpi2;
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
                        if (newInstance instanceof SecretKeyFactorySpi) {
                            SecretKeyFactorySpi secretKeyFactorySpi3 = (SecretKeyFactorySpi) newInstance;
                            this.provider = next.getProvider();
                            this.spi = secretKeyFactorySpi3;
                            return secretKeyFactorySpi3;
                        }
                    } catch (NoSuchAlgorithmException unused) {
                        continue;
                    }
                }
            }
            this.serviceIterator = null;
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final javax.crypto.SecretKey generateSecret(java.security.spec.KeySpec r4) throws java.security.spec.InvalidKeySpecException {
        /*
            r3 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r3.serviceIterator
            if (r0 != 0) goto L_0x000b
            javax.crypto.SecretKeyFactorySpi r3 = r3.spi
            javax.crypto.SecretKey r3 = r3.engineGenerateSecret(r4)
            return r3
        L_0x000b:
            javax.crypto.SecretKeyFactorySpi r0 = r3.spi
            r1 = 0
        L_0x000e:
            javax.crypto.SecretKey r3 = r0.engineGenerateSecret(r4)     // Catch:{ Exception -> 0x0013 }
            return r3
        L_0x0013:
            r2 = move-exception
            if (r1 != 0) goto L_0x0017
            r1 = r2
        L_0x0017:
            javax.crypto.SecretKeyFactorySpi r0 = r3.nextSpi(r0)
            if (r0 != 0) goto L_0x000e
            boolean r3 = r1 instanceof java.security.spec.InvalidKeySpecException
            if (r3 == 0) goto L_0x0024
            java.security.spec.InvalidKeySpecException r1 = (java.security.spec.InvalidKeySpecException) r1
            throw r1
        L_0x0024:
            java.security.spec.InvalidKeySpecException r3 = new java.security.spec.InvalidKeySpecException
            java.lang.String r4 = "Could not generate secret key"
            r3.<init>(r4, r1)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.SecretKeyFactory.generateSecret(java.security.spec.KeySpec):javax.crypto.SecretKey");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.security.spec.KeySpec getKeySpec(javax.crypto.SecretKey r4, java.lang.Class<?> r5) throws java.security.spec.InvalidKeySpecException {
        /*
            r3 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r3.serviceIterator
            if (r0 != 0) goto L_0x000b
            javax.crypto.SecretKeyFactorySpi r3 = r3.spi
            java.security.spec.KeySpec r3 = r3.engineGetKeySpec(r4, r5)
            return r3
        L_0x000b:
            javax.crypto.SecretKeyFactorySpi r0 = r3.spi
            r1 = 0
        L_0x000e:
            java.security.spec.KeySpec r3 = r0.engineGetKeySpec(r4, r5)     // Catch:{ Exception -> 0x0013 }
            return r3
        L_0x0013:
            r2 = move-exception
            if (r1 != 0) goto L_0x0017
            r1 = r2
        L_0x0017:
            javax.crypto.SecretKeyFactorySpi r0 = r3.nextSpi(r0)
            if (r0 != 0) goto L_0x000e
            boolean r3 = r1 instanceof java.security.spec.InvalidKeySpecException
            if (r3 == 0) goto L_0x0024
            java.security.spec.InvalidKeySpecException r1 = (java.security.spec.InvalidKeySpecException) r1
            throw r1
        L_0x0024:
            java.security.spec.InvalidKeySpecException r3 = new java.security.spec.InvalidKeySpecException
            java.lang.String r4 = "Could not get key spec"
            r3.<init>(r4, r1)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.SecretKeyFactory.getKeySpec(javax.crypto.SecretKey, java.lang.Class):java.security.spec.KeySpec");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final javax.crypto.SecretKey translateKey(javax.crypto.SecretKey r4) throws java.security.InvalidKeyException {
        /*
            r3 = this;
            java.util.Iterator<java.security.Provider$Service> r0 = r3.serviceIterator
            if (r0 != 0) goto L_0x000b
            javax.crypto.SecretKeyFactorySpi r3 = r3.spi
            javax.crypto.SecretKey r3 = r3.engineTranslateKey(r4)
            return r3
        L_0x000b:
            javax.crypto.SecretKeyFactorySpi r0 = r3.spi
            r1 = 0
        L_0x000e:
            javax.crypto.SecretKey r3 = r0.engineTranslateKey(r4)     // Catch:{ Exception -> 0x0013 }
            return r3
        L_0x0013:
            r2 = move-exception
            if (r1 != 0) goto L_0x0017
            r1 = r2
        L_0x0017:
            javax.crypto.SecretKeyFactorySpi r0 = r3.nextSpi(r0)
            if (r0 != 0) goto L_0x000e
            boolean r3 = r1 instanceof java.security.InvalidKeyException
            if (r3 == 0) goto L_0x0024
            java.security.InvalidKeyException r1 = (java.security.InvalidKeyException) r1
            throw r1
        L_0x0024:
            java.security.InvalidKeyException r3 = new java.security.InvalidKeyException
            java.lang.String r4 = "Could not translate key"
            r3.<init>(r4, r1)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.SecretKeyFactory.translateKey(javax.crypto.SecretKey):javax.crypto.SecretKey");
    }
}
