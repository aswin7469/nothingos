package javax.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.jca.GetInstance;

public class ExemptionMechanism {
    private boolean done = false;
    private ExemptionMechanismSpi exmechSpi;
    private boolean initialized = false;
    private Key keyStored = null;
    private String mechanism;
    private Provider provider;

    protected ExemptionMechanism(ExemptionMechanismSpi exemptionMechanismSpi, Provider provider2, String str) {
        this.exmechSpi = exemptionMechanismSpi;
        this.provider = provider2;
        this.mechanism = str;
    }

    public final String getName() {
        return this.mechanism;
    }

    public static final ExemptionMechanism getInstance(String str) throws NoSuchAlgorithmException {
        GetInstance.Instance instance = JceSecurity.getInstance("ExemptionMechanism", ExemptionMechanismSpi.class, str);
        return new ExemptionMechanism((ExemptionMechanismSpi) instance.impl, instance.provider, str);
    }

    public static final ExemptionMechanism getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance instance = JceSecurity.getInstance("ExemptionMechanism", (Class<?>) ExemptionMechanismSpi.class, str, str2);
        return new ExemptionMechanism((ExemptionMechanismSpi) instance.impl, instance.provider, str);
    }

    public static final ExemptionMechanism getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        GetInstance.Instance instance = JceSecurity.getInstance("ExemptionMechanism", (Class<?>) ExemptionMechanismSpi.class, str, provider2);
        return new ExemptionMechanism((ExemptionMechanismSpi) instance.impl, instance.provider, str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final boolean isCryptoAllowed(Key key) throws ExemptionMechanismException {
        if (!this.done || key == null) {
            return false;
        }
        return this.keyStored.equals(key);
    }

    public final int getOutputSize(int i) throws IllegalStateException {
        if (!this.initialized) {
            throw new IllegalStateException("ExemptionMechanism not initialized");
        } else if (i >= 0) {
            return this.exmechSpi.engineGetOutputSize(i);
        } else {
            throw new IllegalArgumentException("Input size must be equal to or greater than zero");
        }
    }

    public final void init(Key key) throws InvalidKeyException, ExemptionMechanismException {
        this.done = false;
        this.initialized = false;
        this.keyStored = key;
        this.exmechSpi.engineInit(key);
        this.initialized = true;
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException, ExemptionMechanismException {
        this.done = false;
        this.initialized = false;
        this.keyStored = key;
        this.exmechSpi.engineInit(key, algorithmParameterSpec);
        this.initialized = true;
    }

    public final void init(Key key, AlgorithmParameters algorithmParameters) throws InvalidKeyException, InvalidAlgorithmParameterException, ExemptionMechanismException {
        this.done = false;
        this.initialized = false;
        this.keyStored = key;
        this.exmechSpi.engineInit(key, algorithmParameters);
        this.initialized = true;
    }

    public final byte[] genExemptionBlob() throws IllegalStateException, ExemptionMechanismException {
        if (this.initialized) {
            byte[] engineGenExemptionBlob = this.exmechSpi.engineGenExemptionBlob();
            this.done = true;
            return engineGenExemptionBlob;
        }
        throw new IllegalStateException("ExemptionMechanism not initialized");
    }

    public final int genExemptionBlob(byte[] bArr) throws IllegalStateException, ShortBufferException, ExemptionMechanismException {
        if (this.initialized) {
            int engineGenExemptionBlob = this.exmechSpi.engineGenExemptionBlob(bArr, 0);
            this.done = true;
            return engineGenExemptionBlob;
        }
        throw new IllegalStateException("ExemptionMechanism not initialized");
    }

    public final int genExemptionBlob(byte[] bArr, int i) throws IllegalStateException, ShortBufferException, ExemptionMechanismException {
        if (this.initialized) {
            int engineGenExemptionBlob = this.exmechSpi.engineGenExemptionBlob(bArr, i);
            this.done = true;
            return engineGenExemptionBlob;
        }
        throw new IllegalStateException("ExemptionMechanism not initialized");
    }
}
