package javax.crypto;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import sun.security.jca.Providers;

public class Cipher {
    private static final String ATTRIBUTE_MODES = "SupportedModes";
    private static final String ATTRIBUTE_PADDINGS = "SupportedPaddings";
    public static final int DECRYPT_MODE = 2;
    public static final int ENCRYPT_MODE = 1;
    private static final String KEY_USAGE_EXTENSION_OID = "2.5.29.15";
    public static final int PRIVATE_KEY = 2;
    public static final int PUBLIC_KEY = 1;
    public static final int SECRET_KEY = 3;
    public static final int UNWRAP_MODE = 4;
    public static final int WRAP_MODE = 3;
    private ExemptionMechanism exmech;
    private boolean initialized = false;
    private int opmode = 0;
    /* access modifiers changed from: private */
    public Provider provider;
    /* access modifiers changed from: private */
    public CipherSpi spi;
    private final SpiAndProviderUpdater spiAndProviderUpdater;
    /* access modifiers changed from: private */
    public final String[] tokenizedTransformation;
    private final String transformation;

    enum InitType {
        KEY,
        ALGORITHM_PARAMS,
        ALGORITHM_PARAM_SPEC
    }

    enum NeedToSet {
        NONE,
        MODE,
        PADDING,
        BOTH
    }

    private static String getOpmodeString(int i) {
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "" : "key unwrapping" : "key wrapping" : "decryption" : "encryption";
    }

    protected Cipher(CipherSpi cipherSpi, Provider provider2, String str) {
        if (cipherSpi == null) {
            throw new NullPointerException("cipherSpi == null");
        } else if ((cipherSpi instanceof NullCipherSpi) || provider2 != null) {
            this.spi = cipherSpi;
            this.provider = provider2;
            this.transformation = str;
            this.tokenizedTransformation = null;
            this.spiAndProviderUpdater = new SpiAndProviderUpdater(provider2, cipherSpi);
        } else {
            throw new NullPointerException("provider == null");
        }
    }

    private Cipher(CipherSpi cipherSpi, Provider provider2, String str, String[] strArr) {
        this.spi = cipherSpi;
        this.provider = provider2;
        this.transformation = str;
        this.tokenizedTransformation = strArr;
        this.spiAndProviderUpdater = new SpiAndProviderUpdater(provider2, cipherSpi);
    }

    private static String[] tokenizeTransformation(String str) throws NoSuchAlgorithmException {
        if (str == null || str.isEmpty()) {
            throw new NoSuchAlgorithmException("No transformation given");
        }
        String[] strArr = new String[3];
        StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
        int i = 0;
        while (stringTokenizer.hasMoreTokens() && i < 3) {
            try {
                strArr[i] = stringTokenizer.nextToken().trim();
                i++;
            } catch (NoSuchElementException unused) {
                throw new NoSuchAlgorithmException("Invalid transformation format:" + str);
            }
        }
        if (i == 0 || i == 2 || stringTokenizer.hasMoreTokens()) {
            throw new NoSuchAlgorithmException("Invalid transformation format:" + str);
        }
        String str2 = strArr[0];
        if (str2 != null && str2.length() != 0) {
            return strArr;
        }
        throw new NoSuchAlgorithmException("Invalid transformation:algorithm not specified-" + str);
    }

    public static final Cipher getInstance(String str) throws NoSuchAlgorithmException, NoSuchPaddingException {
        return createCipher(str, (Provider) null);
    }

    public static final Cipher getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("Missing provider");
        }
        Provider provider2 = Security.getProvider(str2);
        if (provider2 != null) {
            return getInstance(str, provider2);
        }
        throw new NoSuchProviderException("No such provider: " + str2);
    }

    public static final Cipher getInstance(String str, Provider provider2) throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (provider2 != null) {
            return createCipher(str, provider2);
        }
        throw new IllegalArgumentException("Missing provider");
    }

    static final Cipher createCipher(String str, Provider provider2) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Providers.checkBouncyCastleDeprecation(provider2, "Cipher", str);
        String[] strArr = tokenizeTransformation(str);
        try {
            if (tryCombinations((InitParams) null, provider2, strArr) != null) {
                return new Cipher((CipherSpi) null, provider2, str, strArr);
            }
            if (provider2 == null) {
                throw new NoSuchAlgorithmException("No provider found for " + str);
            }
            throw new NoSuchAlgorithmException("Provider " + provider2.getName() + " does not provide " + str);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new IllegalStateException("Key/Algorithm excepton despite not passing one", e);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateProviderIfNeeded() {
        try {
            this.spiAndProviderUpdater.updateAndGetSpiAndProvider((InitParams) null, this.spi, this.provider);
        } catch (Exception e) {
            ProviderException providerException = new ProviderException("Could not construct CipherSpi instance");
            providerException.initCause(e);
            throw providerException;
        }
    }

    private void chooseProvider(InitType initType, int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            this.spiAndProviderUpdater.updateAndGetSpiAndProvider(new InitParams(initType, i, key, secureRandom, algorithmParameterSpec, algorithmParameters), this.spi, this.provider);
        } catch (Exception e) {
            if (e instanceof InvalidKeyException) {
                throw ((InvalidKeyException) e);
            } else if (e instanceof InvalidAlgorithmParameterException) {
                throw ((InvalidAlgorithmParameterException) e);
            } else if (!(e instanceof RuntimeException)) {
                String name = key != null ? key.getClass().getName() : "(null)";
                throw new InvalidKeyException("No installed provider supports this key: " + name, e);
            } else {
                throw ((RuntimeException) e);
            }
        }
    }

    public final Provider getProvider() {
        updateProviderIfNeeded();
        return this.provider;
    }

    public final String getAlgorithm() {
        return this.transformation;
    }

    public final int getBlockSize() {
        updateProviderIfNeeded();
        return this.spi.engineGetBlockSize();
    }

    public final int getOutputSize(int i) {
        if (!this.initialized && !(this instanceof NullCipher)) {
            throw new IllegalStateException("Cipher not initialized");
        } else if (i >= 0) {
            updateProviderIfNeeded();
            return this.spi.engineGetOutputSize(i);
        } else {
            throw new IllegalArgumentException("Input size must be equal to or greater than zero");
        }
    }

    public final byte[] getIV() {
        updateProviderIfNeeded();
        return this.spi.engineGetIV();
    }

    public final AlgorithmParameters getParameters() {
        updateProviderIfNeeded();
        return this.spi.engineGetParameters();
    }

    public final ExemptionMechanism getExemptionMechanism() {
        updateProviderIfNeeded();
        return this.exmech;
    }

    private static void checkOpmode(int i) {
        if (i < 1 || i > 4) {
            throw new InvalidParameterException("Invalid operation mode");
        }
    }

    public final void init(int i, Key key) throws InvalidKeyException {
        init(i, key, JceSecurity.RANDOM);
    }

    public final void init(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
        this.initialized = false;
        checkOpmode(i);
        try {
            chooseProvider(InitType.KEY, i, key, (AlgorithmParameterSpec) null, (AlgorithmParameters) null, secureRandom);
            this.initialized = true;
            this.opmode = i;
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidKeyException((Throwable) e);
        }
    }

    public final void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(i, key, algorithmParameterSpec, JceSecurity.RANDOM);
    }

    public final void init(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initialized = false;
        checkOpmode(i);
        chooseProvider(InitType.ALGORITHM_PARAM_SPEC, i, key, algorithmParameterSpec, (AlgorithmParameters) null, secureRandom);
        this.initialized = true;
        this.opmode = i;
    }

    public final void init(int i, Key key, AlgorithmParameters algorithmParameters) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(i, key, algorithmParameters, JceSecurity.RANDOM);
    }

    public final void init(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initialized = false;
        checkOpmode(i);
        chooseProvider(InitType.ALGORITHM_PARAMS, i, key, (AlgorithmParameterSpec) null, algorithmParameters, secureRandom);
        this.initialized = true;
        this.opmode = i;
    }

    public final void init(int i, Certificate certificate) throws InvalidKeyException {
        init(i, certificate, JceSecurity.RANDOM);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000b, code lost:
        r0 = (java.security.cert.X509Certificate) r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void init(int r10, java.security.cert.Certificate r11, java.security.SecureRandom r12) throws java.security.InvalidKeyException {
        /*
            r9 = this;
            r0 = 0
            r9.initialized = r0
            checkOpmode(r10)
            boolean r0 = r11 instanceof java.security.cert.X509Certificate
            r1 = 1
            if (r0 == 0) goto L_0x0045
            r0 = r11
            java.security.cert.X509Certificate r0 = (java.security.cert.X509Certificate) r0
            java.util.Set r2 = r0.getCriticalExtensionOIDs()
            if (r2 == 0) goto L_0x0045
            boolean r3 = r2.isEmpty()
            if (r3 != 0) goto L_0x0045
            java.lang.String r3 = "2.5.29.15"
            boolean r2 = r2.contains(r3)
            if (r2 == 0) goto L_0x0045
            boolean[] r0 = r0.getKeyUsage()
            if (r0 == 0) goto L_0x0045
            r2 = 3
            if (r10 != r1) goto L_0x0032
            int r3 = r0.length
            if (r3 <= r2) goto L_0x0032
            boolean r3 = r0[r2]
            if (r3 == 0) goto L_0x003d
        L_0x0032:
            if (r10 != r2) goto L_0x0045
            int r2 = r0.length
            r3 = 2
            if (r2 <= r3) goto L_0x0045
            boolean r0 = r0[r3]
            if (r0 == 0) goto L_0x003d
            goto L_0x0045
        L_0x003d:
            java.security.InvalidKeyException r9 = new java.security.InvalidKeyException
            java.lang.String r10 = "Wrong key usage"
            r9.<init>((java.lang.String) r10)
            throw r9
        L_0x0045:
            if (r11 != 0) goto L_0x0049
            r11 = 0
            goto L_0x004d
        L_0x0049:
            java.security.PublicKey r11 = r11.getPublicKey()
        L_0x004d:
            r5 = r11
            javax.crypto.Cipher$InitType r3 = javax.crypto.Cipher.InitType.KEY     // Catch:{ InvalidAlgorithmParameterException -> 0x005d }
            r6 = 0
            r7 = 0
            r2 = r9
            r4 = r10
            r8 = r12
            r2.chooseProvider(r3, r4, r5, r6, r7, r8)     // Catch:{ InvalidAlgorithmParameterException -> 0x005d }
            r9.initialized = r1
            r9.opmode = r10
            return
        L_0x005d:
            r9 = move-exception
            java.security.InvalidKeyException r10 = new java.security.InvalidKeyException
            r10.<init>((java.lang.Throwable) r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.Cipher.init(int, java.security.cert.Certificate, java.security.SecureRandom):void");
    }

    private void checkCipherState() {
        if (this instanceof NullCipher) {
            return;
        }
        if (this.initialized) {
            int i = this.opmode;
            if (i != 1 && i != 2) {
                throw new IllegalStateException("Cipher not initialized for encryption/decryption");
            }
            return;
        }
        throw new IllegalStateException("Cipher not initialized");
    }

    public final byte[] update(byte[] bArr) {
        checkCipherState();
        if (bArr != null) {
            updateProviderIfNeeded();
            if (bArr.length == 0) {
                return null;
            }
            return this.spi.engineUpdate(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("Null input buffer");
    }

    public final byte[] update(byte[] bArr, int i, int i2) {
        checkCipherState();
        if (bArr == null || i < 0 || i2 > bArr.length - i || i2 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (i2 == 0) {
            return null;
        }
        return this.spi.engineUpdate(bArr, i, i2);
    }

    public final int update(byte[] bArr, int i, int i2, byte[] bArr2) throws ShortBufferException {
        checkCipherState();
        if (bArr == null || i < 0 || i2 > bArr.length - i || i2 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (i2 == 0) {
            return 0;
        }
        return this.spi.engineUpdate(bArr, i, i2, bArr2, 0);
    }

    public final int update(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws ShortBufferException {
        checkCipherState();
        if (bArr == null || i < 0 || i2 > bArr.length - i || i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (i2 == 0) {
            return 0;
        }
        return this.spi.engineUpdate(bArr, i, i2, bArr2, i3);
    }

    public final int update(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException {
        checkCipherState();
        if (byteBuffer == null || byteBuffer2 == null) {
            throw new IllegalArgumentException("Buffers must not be null");
        } else if (byteBuffer == byteBuffer2) {
            throw new IllegalArgumentException("Input and output buffers must not be the same object, consider using buffer.duplicate()");
        } else if (!byteBuffer2.isReadOnly()) {
            updateProviderIfNeeded();
            return this.spi.engineUpdate(byteBuffer, byteBuffer2);
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final byte[] doFinal() throws IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        updateProviderIfNeeded();
        return this.spi.engineDoFinal((byte[]) null, 0, 0);
    }

    public final int doFinal(byte[] bArr, int i) throws IllegalBlockSizeException, ShortBufferException, BadPaddingException {
        checkCipherState();
        if (bArr == null || i < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal((byte[]) null, 0, 0, bArr, i);
    }

    public final byte[] doFinal(byte[] bArr) throws IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (bArr != null) {
            updateProviderIfNeeded();
            return this.spi.engineDoFinal(bArr, 0, bArr.length);
        }
        throw new IllegalArgumentException("Null input buffer");
    }

    public final byte[] doFinal(byte[] bArr, int i, int i2) throws IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (bArr == null || i < 0 || i2 > bArr.length - i || i2 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(bArr, i, i2);
    }

    public final int doFinal(byte[] bArr, int i, int i2, byte[] bArr2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (bArr == null || i < 0 || i2 > bArr.length - i || i2 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(bArr, i, i2, bArr2, 0);
    }

    public final int doFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (bArr == null || i < 0 || i2 > bArr.length - i || i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(bArr, i, i2, bArr2, i3);
    }

    public final int doFinal(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (byteBuffer == null || byteBuffer2 == null) {
            throw new IllegalArgumentException("Buffers must not be null");
        } else if (byteBuffer == byteBuffer2) {
            throw new IllegalArgumentException("Input and output buffers must not be the same object, consider using buffer.duplicate()");
        } else if (!byteBuffer2.isReadOnly()) {
            updateProviderIfNeeded();
            return this.spi.engineDoFinal(byteBuffer, byteBuffer2);
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final byte[] wrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            } else if (this.opmode != 3) {
                throw new IllegalStateException("Cipher not initialized for wrapping keys");
            }
        }
        updateProviderIfNeeded();
        return this.spi.engineWrap(key);
    }

    public final Key unwrap(byte[] bArr, String str, int i) throws InvalidKeyException, NoSuchAlgorithmException {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            } else if (this.opmode != 4) {
                throw new IllegalStateException("Cipher not initialized for unwrapping keys");
            }
        }
        if (i == 3 || i == 2 || i == 1) {
            updateProviderIfNeeded();
            return this.spi.engineUnwrap(bArr, str, i);
        }
        throw new InvalidParameterException("Invalid key type");
    }

    private AlgorithmParameterSpec getAlgorithmParameterSpec(AlgorithmParameters algorithmParameters) throws InvalidParameterSpecException {
        if (algorithmParameters == null) {
            return null;
        }
        String upperCase = algorithmParameters.getAlgorithm().toUpperCase(Locale.ENGLISH);
        if (upperCase.equalsIgnoreCase("RC2")) {
            return algorithmParameters.getParameterSpec(RC2ParameterSpec.class);
        }
        if (upperCase.equalsIgnoreCase("RC5")) {
            return algorithmParameters.getParameterSpec(RC5ParameterSpec.class);
        }
        if (upperCase.startsWith("PBE")) {
            return algorithmParameters.getParameterSpec(PBEParameterSpec.class);
        }
        if (upperCase.startsWith("DES")) {
            return algorithmParameters.getParameterSpec(IvParameterSpec.class);
        }
        return null;
    }

    public static final int getMaxAllowedKeyLength(String str) throws NoSuchAlgorithmException {
        if (str != null) {
            tokenizeTransformation(str);
            return Integer.MAX_VALUE;
        }
        throw new NullPointerException("transformation == null");
    }

    public static final AlgorithmParameterSpec getMaxAllowedParameterSpec(String str) throws NoSuchAlgorithmException {
        if (str != null) {
            tokenizeTransformation(str);
            return null;
        }
        throw new NullPointerException("transformation == null");
    }

    public final void updateAAD(byte[] bArr) {
        if (bArr != null) {
            updateAAD(bArr, 0, bArr.length);
            return;
        }
        throw new IllegalArgumentException("src buffer is null");
    }

    public final void updateAAD(byte[] bArr, int i, int i2) {
        checkCipherState();
        if (bArr == null || i < 0 || i2 < 0 || i2 + i > bArr.length) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (i2 != 0) {
            this.spi.engineUpdateAAD(bArr, i, i2);
        }
    }

    public final void updateAAD(ByteBuffer byteBuffer) {
        checkCipherState();
        if (byteBuffer != null) {
            updateProviderIfNeeded();
            if (byteBuffer.remaining() != 0) {
                this.spi.engineUpdateAAD(byteBuffer);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("src ByteBuffer is null");
    }

    public CipherSpi getCurrentSpi() {
        return this.spi;
    }

    static boolean matchAttribute(Provider.Service service, String str, String str2) {
        String attribute;
        if (str2 == null || (attribute = service.getAttribute(str)) == null) {
            return true;
        }
        return str2.toUpperCase(Locale.f700US).matches(attribute.toUpperCase(Locale.f700US));
    }

    static class Transform {
        /* access modifiers changed from: private */
        public final String name;
        /* access modifiers changed from: private */
        public final NeedToSet needToSet;

        public Transform(String str, NeedToSet needToSet2) {
            this.name = str;
            this.needToSet = needToSet2;
        }
    }

    static class InitParams {
        final InitType initType;
        final Key key;
        final int opmode;
        final AlgorithmParameters params;
        final SecureRandom random;
        final AlgorithmParameterSpec spec;

        InitParams(InitType initType2, int i, Key key2, SecureRandom secureRandom, AlgorithmParameterSpec algorithmParameterSpec, AlgorithmParameters algorithmParameters) {
            this.initType = initType2;
            this.opmode = i;
            this.key = key2;
            this.random = secureRandom;
            this.spec = algorithmParameterSpec;
            this.params = algorithmParameters;
        }
    }

    class SpiAndProviderUpdater {
        private final Object initSpiLock = new Object();
        private final Provider specifiedProvider;
        private final CipherSpi specifiedSpi;

        SpiAndProviderUpdater(Provider provider, CipherSpi cipherSpi) {
            this.specifiedProvider = provider;
            this.specifiedSpi = cipherSpi;
        }

        /* access modifiers changed from: package-private */
        public void setCipherSpiImplAndProvider(CipherSpi cipherSpi, Provider provider) {
            Cipher.this.spi = cipherSpi;
            Cipher.this.provider = provider;
        }

        /* access modifiers changed from: package-private */
        public CipherSpiAndProvider updateAndGetSpiAndProvider(InitParams initParams, CipherSpi cipherSpi, Provider provider) throws InvalidKeyException, InvalidAlgorithmParameterException {
            CipherSpi cipherSpi2 = this.specifiedSpi;
            if (cipherSpi2 != null) {
                return new CipherSpiAndProvider(cipherSpi2, provider);
            }
            synchronized (this.initSpiLock) {
                if (cipherSpi == null || initParams != null) {
                    CipherSpiAndProvider tryCombinations = Cipher.tryCombinations(initParams, this.specifiedProvider, Cipher.this.tokenizedTransformation);
                    if (tryCombinations != null) {
                        setCipherSpiImplAndProvider(tryCombinations.cipherSpi, tryCombinations.provider);
                        CipherSpiAndProvider cipherSpiAndProvider = new CipherSpiAndProvider(tryCombinations.cipherSpi, tryCombinations.provider);
                        return cipherSpiAndProvider;
                    }
                    throw new ProviderException("No provider found for " + Arrays.toString((Object[]) Cipher.this.tokenizedTransformation));
                }
                CipherSpiAndProvider cipherSpiAndProvider2 = new CipherSpiAndProvider(cipherSpi, provider);
                return cipherSpiAndProvider2;
            }
        }

        /* access modifiers changed from: package-private */
        public CipherSpiAndProvider updateAndGetSpiAndProvider(CipherSpi cipherSpi, Provider provider) {
            try {
                return updateAndGetSpiAndProvider((InitParams) null, cipherSpi, provider);
            } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
                throw new ProviderException("Exception thrown when params == null", e);
            }
        }

        /* access modifiers changed from: package-private */
        public CipherSpi getCurrentSpi(CipherSpi cipherSpi) {
            CipherSpi cipherSpi2 = this.specifiedSpi;
            if (cipherSpi2 != null) {
                return cipherSpi2;
            }
            synchronized (this.initSpiLock) {
            }
            return cipherSpi;
        }
    }

    static CipherSpiAndProvider tryCombinations(InitParams initParams, Provider provider2, String[] strArr) throws InvalidKeyException, InvalidAlgorithmParameterException {
        Exception exc;
        ArrayList arrayList = new ArrayList();
        if (!(strArr[1] == null || strArr[2] == null)) {
            arrayList.add(new Transform(strArr[0] + "/" + strArr[1] + "/" + strArr[2], NeedToSet.NONE));
        }
        if (strArr[1] != null) {
            arrayList.add(new Transform(strArr[0] + "/" + strArr[1], NeedToSet.PADDING));
        }
        if (strArr[2] != null) {
            arrayList.add(new Transform(strArr[0] + "//" + strArr[2], NeedToSet.MODE));
        }
        arrayList.add(new Transform(strArr[0], NeedToSet.BOTH));
        if (provider2 != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Transform transform = (Transform) it.next();
                Provider.Service service = provider2.getService("Cipher", transform.name);
                if (service != null) {
                    return tryTransformWithProvider(initParams, strArr, transform.needToSet, service);
                }
            }
            exc = null;
        } else {
            exc = null;
            for (Provider provider3 : Security.getProviders()) {
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    Transform transform2 = (Transform) it2.next();
                    Provider.Service service2 = provider3.getService("Cipher", transform2.name);
                    if (service2 != null && (initParams == null || initParams.key == null || service2.supportsParameter(initParams.key))) {
                        try {
                            CipherSpiAndProvider tryTransformWithProvider = tryTransformWithProvider(initParams, strArr, transform2.needToSet, service2);
                            if (tryTransformWithProvider != null) {
                                return tryTransformWithProvider;
                            }
                        } catch (Exception e) {
                            if (exc == null) {
                                exc = e;
                            }
                        }
                    }
                }
            }
        }
        if (exc instanceof InvalidKeyException) {
            throw ((InvalidKeyException) exc);
        } else if (exc instanceof InvalidAlgorithmParameterException) {
            throw ((InvalidAlgorithmParameterException) exc);
        } else if (exc instanceof RuntimeException) {
            throw exc;
        } else if (exc != null) {
            throw new InvalidKeyException("No provider can be initialized with given key", exc);
        } else if (initParams == null || initParams.key == null) {
            return null;
        } else {
            throw new InvalidKeyException("No provider offers " + Arrays.toString((Object[]) strArr) + " for " + initParams.key.getAlgorithm() + " key of class " + initParams.key.getClass().getName() + " and export format " + initParams.key.getFormat());
        }
    }

    static class CipherSpiAndProvider {
        CipherSpi cipherSpi;
        Provider provider;

        CipherSpiAndProvider(CipherSpi cipherSpi2, Provider provider2) {
            this.cipherSpi = cipherSpi2;
            this.provider = provider2;
        }
    }

    static CipherSpiAndProvider tryTransformWithProvider(InitParams initParams, String[] strArr, NeedToSet needToSet, Provider.Service service) throws InvalidKeyException, InvalidAlgorithmParameterException {
        String str;
        String str2;
        try {
            if (matchAttribute(service, ATTRIBUTE_MODES, strArr[1])) {
                if (matchAttribute(service, ATTRIBUTE_PADDINGS, strArr[2])) {
                    CipherSpiAndProvider cipherSpiAndProvider = new CipherSpiAndProvider((CipherSpi) service.newInstance((Object) null), service.getProvider());
                    if (cipherSpiAndProvider.cipherSpi != null) {
                        if (cipherSpiAndProvider.provider != null) {
                            CipherSpi cipherSpi = cipherSpiAndProvider.cipherSpi;
                            if ((needToSet == NeedToSet.MODE || needToSet == NeedToSet.BOTH) && (str2 = strArr[1]) != null) {
                                cipherSpi.engineSetMode(str2);
                            }
                            if ((needToSet == NeedToSet.PADDING || needToSet == NeedToSet.BOTH) && (str = strArr[2]) != null) {
                                cipherSpi.engineSetPadding(str);
                            }
                            if (initParams != null) {
                                int i = C45591.$SwitchMap$javax$crypto$Cipher$InitType[initParams.initType.ordinal()];
                                if (i == 1) {
                                    cipherSpi.engineInit(initParams.opmode, initParams.key, initParams.params, initParams.random);
                                } else if (i == 2) {
                                    cipherSpi.engineInit(initParams.opmode, initParams.key, initParams.spec, initParams.random);
                                } else if (i == 3) {
                                    cipherSpi.engineInit(initParams.opmode, initParams.key, initParams.random);
                                } else {
                                    throw new AssertionError((Object) "This should never be reached");
                                }
                            }
                            return new CipherSpiAndProvider(cipherSpi, cipherSpiAndProvider.provider);
                        }
                    }
                }
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException unused) {
        }
        return null;
    }

    /* renamed from: javax.crypto.Cipher$1 */
    static /* synthetic */ class C45591 {
        static final /* synthetic */ int[] $SwitchMap$javax$crypto$Cipher$InitType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                javax.crypto.Cipher$InitType[] r0 = javax.crypto.Cipher.InitType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$javax$crypto$Cipher$InitType = r0
                javax.crypto.Cipher$InitType r1 = javax.crypto.Cipher.InitType.ALGORITHM_PARAMS     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$javax$crypto$Cipher$InitType     // Catch:{ NoSuchFieldError -> 0x001d }
                javax.crypto.Cipher$InitType r1 = javax.crypto.Cipher.InitType.ALGORITHM_PARAM_SPEC     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$javax$crypto$Cipher$InitType     // Catch:{ NoSuchFieldError -> 0x0028 }
                javax.crypto.Cipher$InitType r1 = javax.crypto.Cipher.InitType.KEY     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.crypto.Cipher.C45591.<clinit>():void");
        }
    }
}
