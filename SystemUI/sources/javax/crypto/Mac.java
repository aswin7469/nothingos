package javax.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;

public class Mac implements Cloneable {
    private static int warnCount = 10;
    private final String algorithm;
    private boolean initialized = false;
    private final Object lock;
    private Provider provider;
    private MacSpi spi;

    protected Mac(MacSpi macSpi, Provider provider2, String str) {
        this.spi = macSpi;
        this.provider = provider2;
        this.algorithm = str;
        this.lock = null;
    }

    private Mac(String str) {
        this.algorithm = str;
        this.lock = new Object();
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final Mac getInstance(String str) throws NoSuchAlgorithmException {
        for (Provider.Service provider2 : GetInstance.getServices("Mac", str)) {
            if (JceSecurity.canUseProvider(provider2.getProvider())) {
                return new Mac(str);
            }
        }
        throw new NoSuchAlgorithmException("Algorithm " + str + " not available");
    }

    public static final Mac getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Providers.checkBouncyCastleDeprecation(str2, "Mac", str);
        GetInstance.Instance instance = JceSecurity.getInstance("Mac", (Class<?>) MacSpi.class, str, str2);
        return new Mac((MacSpi) instance.impl, instance.provider, str);
    }

    public static final Mac getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Providers.checkBouncyCastleDeprecation(provider2, "Mac", str);
        GetInstance.Instance instance = JceSecurity.getInstance("Mac", (Class<?>) MacSpi.class, str, provider2);
        return new Mac((MacSpi) instance.impl, instance.provider, str);
    }

    /* access modifiers changed from: package-private */
    public void chooseFirstProvider() {
        Object obj;
        if (this.spi == null && (obj = this.lock) != null) {
            synchronized (obj) {
                if (this.spi == null) {
                    NoSuchAlgorithmException e = null;
                    for (Provider.Service next : GetInstance.getServices("Mac", this.algorithm)) {
                        if (JceSecurity.canUseProvider(next.getProvider())) {
                            try {
                                Object newInstance = next.newInstance((Object) null);
                                if (newInstance instanceof MacSpi) {
                                    this.spi = (MacSpi) newInstance;
                                    this.provider = next.getProvider();
                                    return;
                                }
                            } catch (NoSuchAlgorithmException e2) {
                                e = e2;
                            }
                        }
                    }
                    ProviderException providerException = new ProviderException("Could not construct MacSpi instance");
                    if (e != null) {
                        providerException.initCause(e);
                    }
                    throw providerException;
                }
            }
        }
    }

    private void chooseProvider(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        synchronized (this.lock) {
            MacSpi macSpi = this.spi;
            if (macSpi == null || !(key == null || this.lock == null)) {
                Exception exc = null;
                for (Provider.Service next : GetInstance.getServices("Mac", this.algorithm)) {
                    if (next.supportsParameter(key)) {
                        if (!JceSecurity.canUseProvider(next.getProvider())) {
                            continue;
                        } else {
                            try {
                                MacSpi macSpi2 = (MacSpi) next.newInstance((Object) null);
                                macSpi2.engineInit(key, algorithmParameterSpec);
                                this.provider = next.getProvider();
                                this.spi = macSpi2;
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
                    throw new InvalidKeyException("No installed provider supports this key: " + (key != null ? key.getClass().getName() : "(null)"), exc);
                } else {
                    throw exc;
                }
            } else {
                macSpi.engineInit(key, algorithmParameterSpec);
            }
        }
    }

    public final Provider getProvider() {
        chooseFirstProvider();
        return this.provider;
    }

    public final int getMacLength() {
        chooseFirstProvider();
        return this.spi.engineGetMacLength();
    }

    public final void init(Key key) throws InvalidKeyException {
        try {
            MacSpi macSpi = this.spi;
            if (macSpi == null || !(key == null || this.lock == null)) {
                chooseProvider(key, (AlgorithmParameterSpec) null);
            } else {
                macSpi.engineInit(key, (AlgorithmParameterSpec) null);
            }
            this.initialized = true;
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidKeyException("init() failed", e);
        }
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        MacSpi macSpi = this.spi;
        if (macSpi == null || !(key == null || this.lock == null)) {
            chooseProvider(key, algorithmParameterSpec);
        } else {
            macSpi.engineInit(key, algorithmParameterSpec);
        }
        this.initialized = true;
    }

    public final void update(byte b) throws IllegalStateException {
        chooseFirstProvider();
        if (this.initialized) {
            this.spi.engineUpdate(b);
            return;
        }
        throw new IllegalStateException("MAC not initialized");
    }

    public final void update(byte[] bArr) throws IllegalStateException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        } else if (bArr != null) {
            this.spi.engineUpdate(bArr, 0, bArr.length);
        }
    }

    public final void update(byte[] bArr, int i, int i2) throws IllegalStateException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        } else if (bArr == null) {
        } else {
            if (i < 0 || i2 > bArr.length - i || i2 < 0) {
                throw new IllegalArgumentException("Bad arguments");
            }
            this.spi.engineUpdate(bArr, i, i2);
        }
    }

    public final void update(ByteBuffer byteBuffer) {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        } else if (byteBuffer != null) {
            this.spi.engineUpdate(byteBuffer);
        } else {
            throw new IllegalArgumentException("Buffer must not be null");
        }
    }

    public final byte[] doFinal() throws IllegalStateException {
        chooseFirstProvider();
        if (this.initialized) {
            byte[] engineDoFinal = this.spi.engineDoFinal();
            this.spi.engineReset();
            return engineDoFinal;
        }
        throw new IllegalStateException("MAC not initialized");
    }

    public final void doFinal(byte[] bArr, int i) throws ShortBufferException, IllegalStateException {
        chooseFirstProvider();
        if (this.initialized) {
            int macLength = getMacLength();
            if (bArr == null || bArr.length - i < macLength) {
                throw new ShortBufferException("Cannot store MAC in output buffer");
            }
            System.arraycopy((Object) doFinal(), 0, (Object) bArr, i, macLength);
            return;
        }
        throw new IllegalStateException("MAC not initialized");
    }

    public final byte[] doFinal(byte[] bArr) throws IllegalStateException {
        chooseFirstProvider();
        if (this.initialized) {
            update(bArr);
            return doFinal();
        }
        throw new IllegalStateException("MAC not initialized");
    }

    public final void reset() {
        chooseFirstProvider();
        this.spi.engineReset();
    }

    public final Object clone() throws CloneNotSupportedException {
        chooseFirstProvider();
        Mac mac = (Mac) super.clone();
        mac.spi = (MacSpi) this.spi.clone();
        return mac;
    }

    public MacSpi getCurrentSpi() {
        return this.spi;
    }
}
