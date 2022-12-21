package java.security;

import java.nio.ByteBuffer;
import java.p026io.ByteArrayOutputStream;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;
import sun.security.jca.ServiceId;

public abstract class Signature extends SignatureSpi {
    private static final String RSA_CIPHER = "RSA/ECB/PKCS1Padding";
    private static final String RSA_SIGNATURE = "NONEwithRSA";
    protected static final int SIGN = 2;
    protected static final int UNINITIALIZED = 0;
    protected static final int VERIFY = 3;
    /* access modifiers changed from: private */
    public static final List<ServiceId> rsaIds = Arrays.asList(new ServiceId("Signature", RSA_SIGNATURE), new ServiceId("Cipher", RSA_CIPHER), new ServiceId("Cipher", "RSA/ECB"), new ServiceId("Cipher", "RSA//PKCS1Padding"), new ServiceId("Cipher", "RSA"));
    private static final Map<String, Boolean> signatureInfo;
    /* access modifiers changed from: private */
    public String algorithm;
    Provider provider;
    protected int state = 0;

    /* access modifiers changed from: package-private */
    public void chooseFirstProvider() {
    }

    public SignatureSpi getCurrentSpi() {
        return null;
    }

    protected Signature(String str) {
        this.algorithm = str;
    }

    static {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        signatureInfo = concurrentHashMap;
        Boolean bool = Boolean.TRUE;
        concurrentHashMap.put("sun.security.provider.DSA$RawDSA", bool);
        concurrentHashMap.put("sun.security.provider.DSA$SHA1withDSA", bool);
        concurrentHashMap.put("sun.security.rsa.RSASignature$MD2withRSA", bool);
        concurrentHashMap.put("sun.security.rsa.RSASignature$MD5withRSA", bool);
        concurrentHashMap.put("sun.security.rsa.RSASignature$SHA1withRSA", bool);
        concurrentHashMap.put("sun.security.rsa.RSASignature$SHA256withRSA", bool);
        concurrentHashMap.put("sun.security.rsa.RSASignature$SHA384withRSA", bool);
        concurrentHashMap.put("sun.security.rsa.RSASignature$SHA512withRSA", bool);
        concurrentHashMap.put("com.sun.net.ssl.internal.ssl.RSASignature", bool);
        concurrentHashMap.put("sun.security.pkcs11.P11Signature", bool);
    }

    public static Signature getInstance(String str) throws NoSuchAlgorithmException {
        List<Provider.Service> list;
        if (str.equalsIgnoreCase(RSA_SIGNATURE)) {
            list = GetInstance.getServices(rsaIds);
        } else {
            list = GetInstance.getServices("Signature", str);
        }
        Iterator<Provider.Service> it = list.iterator();
        if (it.hasNext()) {
            do {
                Provider.Service next = it.next();
                if (isSpi(next)) {
                    return new Delegate(str);
                }
                try {
                    return getInstance(GetInstance.getInstance(next, SignatureSpi.class), str);
                } catch (NoSuchAlgorithmException e) {
                    if (!it.hasNext()) {
                        throw e;
                    }
                }
            } while (!it.hasNext());
            throw e;
        }
        throw new NoSuchAlgorithmException(str + " Signature not available");
    }

    private static Signature getInstance(GetInstance.Instance instance, String str) {
        Signature signature;
        if (instance.impl instanceof Signature) {
            signature = (Signature) instance.impl;
            signature.algorithm = str;
        } else {
            signature = new Delegate((SignatureSpi) instance.impl, str);
        }
        signature.provider = instance.provider;
        return signature;
    }

    /* access modifiers changed from: private */
    public static boolean isSpi(Provider.Service service) {
        boolean z = true;
        if (service.getType().equals("Cipher")) {
            return true;
        }
        String className = service.getClassName();
        Map<String, Boolean> map = signatureInfo;
        Boolean bool = map.get(className);
        if (bool == null) {
            try {
                Object newInstance = service.newInstance((Object) null);
                if (!(newInstance instanceof SignatureSpi) || (newInstance instanceof Signature)) {
                    z = false;
                }
                bool = Boolean.valueOf(z);
                map.put(className, bool);
            } catch (Exception unused) {
                return false;
            }
        }
        return bool.booleanValue();
    }

    public static Signature getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (!str.equalsIgnoreCase(RSA_SIGNATURE)) {
            Providers.checkBouncyCastleDeprecation(str2, "Signature", str);
            return getInstance(GetInstance.getInstance("Signature", (Class<?>) SignatureSpi.class, str, str2), str);
        } else if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        } else {
            Provider provider2 = Security.getProvider(str2);
            if (provider2 != null) {
                return getInstanceRSA(provider2);
            }
            throw new NoSuchProviderException("no such provider: " + str2);
        }
    }

    public static Signature getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        if (!str.equalsIgnoreCase(RSA_SIGNATURE)) {
            Providers.checkBouncyCastleDeprecation(provider2, "Signature", str);
            return getInstance(GetInstance.getInstance("Signature", (Class<?>) SignatureSpi.class, str, provider2), str);
        } else if (provider2 != null) {
            return getInstanceRSA(provider2);
        } else {
            throw new IllegalArgumentException("missing provider");
        }
    }

    private static Signature getInstanceRSA(Provider provider2) throws NoSuchAlgorithmException {
        Provider.Service service = provider2.getService("Signature", RSA_SIGNATURE);
        if (service != null) {
            return getInstance(GetInstance.getInstance(service, SignatureSpi.class), RSA_SIGNATURE);
        }
        try {
            return new Delegate(new CipherAdapter(Cipher.getInstance(RSA_CIPHER, provider2)), RSA_SIGNATURE);
        } catch (GeneralSecurityException e) {
            throw new NoSuchAlgorithmException("no such algorithm: NONEwithRSA for provider " + provider2.getName(), e);
        }
    }

    public final Provider getProvider() {
        chooseFirstProvider();
        return this.provider;
    }

    public final void initVerify(PublicKey publicKey) throws InvalidKeyException {
        engineInitVerify(publicKey);
        this.state = 3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = (java.security.cert.X509Certificate) r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void initVerify(java.security.cert.Certificate r4) throws java.security.InvalidKeyException {
        /*
            r3 = this;
            boolean r0 = r4 instanceof java.security.cert.X509Certificate
            if (r0 == 0) goto L_0x002f
            r0 = r4
            java.security.cert.X509Certificate r0 = (java.security.cert.X509Certificate) r0
            java.util.Set r1 = r0.getCriticalExtensionOIDs()
            if (r1 == 0) goto L_0x002f
            boolean r2 = r1.isEmpty()
            if (r2 != 0) goto L_0x002f
            java.lang.String r2 = "2.5.29.15"
            boolean r1 = r1.contains(r2)
            if (r1 == 0) goto L_0x002f
            boolean[] r0 = r0.getKeyUsage()
            if (r0 == 0) goto L_0x002f
            r1 = 0
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x0027
            goto L_0x002f
        L_0x0027:
            java.security.InvalidKeyException r3 = new java.security.InvalidKeyException
            java.lang.String r4 = "Wrong key usage"
            r3.<init>((java.lang.String) r4)
            throw r3
        L_0x002f:
            java.security.PublicKey r4 = r4.getPublicKey()
            r3.engineInitVerify(r4)
            r4 = 3
            r3.state = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.Signature.initVerify(java.security.cert.Certificate):void");
    }

    public final void initSign(PrivateKey privateKey) throws InvalidKeyException {
        engineInitSign(privateKey);
        this.state = 2;
    }

    public final void initSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        engineInitSign(privateKey, secureRandom);
        this.state = 2;
    }

    public final byte[] sign() throws SignatureException {
        if (this.state == 2) {
            return engineSign();
        }
        throw new SignatureException("object not initialized for signing");
    }

    public final int sign(byte[] bArr, int i, int i2) throws SignatureException {
        if (bArr == null) {
            throw new IllegalArgumentException("No output buffer given");
        } else if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("offset or len is less than 0");
        } else if (bArr.length - i < i2) {
            throw new IllegalArgumentException("Output buffer too small for specified offset and length");
        } else if (this.state == 2) {
            return engineSign(bArr, i, i2);
        } else {
            throw new SignatureException("object not initialized for signing");
        }
    }

    public final boolean verify(byte[] bArr) throws SignatureException {
        if (this.state == 3) {
            return engineVerify(bArr);
        }
        throw new SignatureException("object not initialized for verification");
    }

    public final boolean verify(byte[] bArr, int i, int i2) throws SignatureException {
        if (this.state != 3) {
            throw new SignatureException("object not initialized for verification");
        } else if (bArr == null) {
            throw new IllegalArgumentException("signature is null");
        } else if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("offset or length is less than 0");
        } else if (bArr.length - i >= i2) {
            return engineVerify(bArr, i, i2);
        } else {
            throw new IllegalArgumentException("signature too small for specified offset and length");
        }
    }

    public final void update(byte b) throws SignatureException {
        int i = this.state;
        if (i == 3 || i == 2) {
            engineUpdate(b);
            return;
        }
        throw new SignatureException("object not initialized for signature or verification");
    }

    public final void update(byte[] bArr) throws SignatureException {
        update(bArr, 0, bArr.length);
    }

    public final void update(byte[] bArr, int i, int i2) throws SignatureException {
        int i3 = this.state;
        if (i3 != 2 && i3 != 3) {
            throw new SignatureException("object not initialized for signature or verification");
        } else if (bArr == null) {
            throw new IllegalArgumentException("data is null");
        } else if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("off or len is less than 0");
        } else if (bArr.length - i >= i2) {
            engineUpdate(bArr, i, i2);
        } else {
            throw new IllegalArgumentException("data too small for specified offset and length");
        }
    }

    public final void update(ByteBuffer byteBuffer) throws SignatureException {
        int i = this.state;
        if (i == 2 || i == 3) {
            byteBuffer.getClass();
            engineUpdate(byteBuffer);
            return;
        }
        throw new SignatureException("object not initialized for signature or verification");
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public String toString() {
        int i = this.state;
        String str = i != 0 ? i != 2 ? i != 3 ? "" : "<initialized for verifying>" : "<initialized for signing>" : "<not initialized>";
        return "Signature object: " + getAlgorithm() + str;
    }

    @Deprecated
    public final void setParameter(String str, Object obj) throws InvalidParameterException {
        engineSetParameter(str, obj);
    }

    public final void setParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        engineSetParameter(algorithmParameterSpec);
    }

    public final AlgorithmParameters getParameters() {
        return engineGetParameters();
    }

    @Deprecated
    public final Object getParameter(String str) throws InvalidParameterException {
        return engineGetParameter(str);
    }

    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }

    private static class Delegate extends Signature {
        private static final int I_PRIV = 2;
        private static final int I_PRIV_SR = 3;
        private static final int I_PUB = 1;
        private static int warnCount = 10;
        private final Object lock;
        private SignatureSpi sigSpi;

        Delegate(SignatureSpi signatureSpi, String str) {
            super(str);
            this.sigSpi = signatureSpi;
            this.lock = null;
        }

        Delegate(String str) {
            super(str);
            this.lock = new Object();
        }

        public Object clone() throws CloneNotSupportedException {
            chooseFirstProvider();
            SignatureSpi signatureSpi = this.sigSpi;
            if (signatureSpi instanceof Cloneable) {
                Delegate delegate = new Delegate((SignatureSpi) signatureSpi.clone(), this.algorithm);
                delegate.provider = this.provider;
                return delegate;
            }
            throw new CloneNotSupportedException();
        }

        private static SignatureSpi newInstance(Provider.Service service) throws NoSuchAlgorithmException {
            if (service.getType().equals("Cipher")) {
                try {
                    return new CipherAdapter(Cipher.getInstance(Signature.RSA_CIPHER, service.getProvider()));
                } catch (NoSuchPaddingException e) {
                    throw new NoSuchAlgorithmException((Throwable) e);
                }
            } else {
                Object newInstance = service.newInstance((Object) null);
                if (newInstance instanceof SignatureSpi) {
                    return (SignatureSpi) newInstance;
                }
                throw new NoSuchAlgorithmException("Not a SignatureSpi: " + newInstance.getClass().getName());
            }
        }

        /* access modifiers changed from: package-private */
        public void chooseFirstProvider() {
            List<Provider.Service> list;
            if (this.sigSpi == null) {
                synchronized (this.lock) {
                    if (this.sigSpi == null) {
                        if (this.algorithm.equalsIgnoreCase(Signature.RSA_SIGNATURE)) {
                            list = GetInstance.getServices(Signature.rsaIds);
                        } else {
                            list = GetInstance.getServices("Signature", this.algorithm);
                        }
                        NoSuchAlgorithmException e = null;
                        for (Provider.Service next : list) {
                            if (Signature.isSpi(next)) {
                                try {
                                    this.sigSpi = newInstance(next);
                                    this.provider = next.getProvider();
                                    return;
                                } catch (NoSuchAlgorithmException e2) {
                                    e = e2;
                                }
                            }
                        }
                        ProviderException providerException = new ProviderException("Could not construct SignatureSpi instance");
                        if (e != null) {
                            providerException.initCause(e);
                        }
                        throw providerException;
                    }
                }
            }
        }

        private void chooseProvider(int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
            List<Provider.Service> list;
            synchronized (this.lock) {
                SignatureSpi signatureSpi = this.sigSpi;
                if (signatureSpi == null || key != null) {
                    if (this.algorithm.equalsIgnoreCase(Signature.RSA_SIGNATURE)) {
                        list = GetInstance.getServices(Signature.rsaIds);
                    } else {
                        list = GetInstance.getServices("Signature", this.algorithm);
                    }
                    InvalidKeyException invalidKeyException = null;
                    for (Provider.Service next : list) {
                        if (next.supportsParameter(key)) {
                            if (!Signature.isSpi(next)) {
                                continue;
                            } else {
                                try {
                                    SignatureSpi newInstance = newInstance(next);
                                    init(newInstance, i, key, secureRandom);
                                    this.provider = next.getProvider();
                                    this.sigSpi = newInstance;
                                    return;
                                } catch (Exception e) {
                                    if (invalidKeyException == null) {
                                        invalidKeyException = e;
                                    }
                                    if (invalidKeyException instanceof InvalidKeyException) {
                                        throw invalidKeyException;
                                    }
                                }
                            }
                        }
                    }
                    if (invalidKeyException instanceof InvalidKeyException) {
                        throw invalidKeyException;
                    } else if (!(invalidKeyException instanceof RuntimeException)) {
                        String name = key != null ? key.getClass().getName() : "(null)";
                        throw new InvalidKeyException("No installed provider supports this key: " + name, invalidKeyException);
                    } else {
                        throw ((RuntimeException) invalidKeyException);
                    }
                } else {
                    init(signatureSpi, i, key, secureRandom);
                }
            }
        }

        private void init(SignatureSpi signatureSpi, int i, Key key, SecureRandom secureRandom) throws InvalidKeyException {
            if (i == 1) {
                signatureSpi.engineInitVerify((PublicKey) key);
            } else if (i == 2) {
                signatureSpi.engineInitSign((PrivateKey) key);
            } else if (i == 3) {
                signatureSpi.engineInitSign((PrivateKey) key, secureRandom);
            } else {
                throw new AssertionError((Object) "Internal error: " + i);
            }
        }

        /* access modifiers changed from: protected */
        public void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
            SignatureSpi signatureSpi = this.sigSpi;
            if (signatureSpi == null || !(this.lock == null || publicKey == null)) {
                chooseProvider(1, publicKey, (SecureRandom) null);
            } else {
                signatureSpi.engineInitVerify(publicKey);
            }
        }

        /* access modifiers changed from: protected */
        public void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
            SignatureSpi signatureSpi = this.sigSpi;
            if (signatureSpi == null || !(this.lock == null || privateKey == null)) {
                chooseProvider(2, privateKey, (SecureRandom) null);
            } else {
                signatureSpi.engineInitSign(privateKey);
            }
        }

        /* access modifiers changed from: protected */
        public void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
            SignatureSpi signatureSpi = this.sigSpi;
            if (signatureSpi == null || !(this.lock == null || privateKey == null)) {
                chooseProvider(3, privateKey, secureRandom);
            } else {
                signatureSpi.engineInitSign(privateKey, secureRandom);
            }
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(byte b) throws SignatureException {
            chooseFirstProvider();
            this.sigSpi.engineUpdate(b);
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(byte[] bArr, int i, int i2) throws SignatureException {
            chooseFirstProvider();
            this.sigSpi.engineUpdate(bArr, i, i2);
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(ByteBuffer byteBuffer) {
            chooseFirstProvider();
            this.sigSpi.engineUpdate(byteBuffer);
        }

        /* access modifiers changed from: protected */
        public byte[] engineSign() throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineSign();
        }

        /* access modifiers changed from: protected */
        public int engineSign(byte[] bArr, int i, int i2) throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineSign(bArr, i, i2);
        }

        /* access modifiers changed from: protected */
        public boolean engineVerify(byte[] bArr) throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineVerify(bArr);
        }

        /* access modifiers changed from: protected */
        public boolean engineVerify(byte[] bArr, int i, int i2) throws SignatureException {
            chooseFirstProvider();
            return this.sigSpi.engineVerify(bArr, i, i2);
        }

        /* access modifiers changed from: protected */
        public void engineSetParameter(String str, Object obj) throws InvalidParameterException {
            chooseFirstProvider();
            this.sigSpi.engineSetParameter(str, obj);
        }

        /* access modifiers changed from: protected */
        public void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
            chooseFirstProvider();
            this.sigSpi.engineSetParameter(algorithmParameterSpec);
        }

        /* access modifiers changed from: protected */
        public Object engineGetParameter(String str) throws InvalidParameterException {
            chooseFirstProvider();
            return this.sigSpi.engineGetParameter(str);
        }

        /* access modifiers changed from: protected */
        public AlgorithmParameters engineGetParameters() {
            chooseFirstProvider();
            return this.sigSpi.engineGetParameters();
        }

        public SignatureSpi getCurrentSpi() {
            SignatureSpi signatureSpi;
            Object obj = this.lock;
            if (obj == null) {
                return this.sigSpi;
            }
            synchronized (obj) {
                signatureSpi = this.sigSpi;
            }
            return signatureSpi;
        }
    }

    private static class CipherAdapter extends SignatureSpi {
        private final Cipher cipher;
        private ByteArrayOutputStream data;

        CipherAdapter(Cipher cipher2) {
            this.cipher = cipher2;
        }

        /* access modifiers changed from: protected */
        public void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
            this.cipher.init(2, (Key) publicKey);
            ByteArrayOutputStream byteArrayOutputStream = this.data;
            if (byteArrayOutputStream == null) {
                this.data = new ByteArrayOutputStream(128);
            } else {
                byteArrayOutputStream.reset();
            }
        }

        /* access modifiers changed from: protected */
        public void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
            this.cipher.init(1, (Key) privateKey);
            this.data = null;
        }

        /* access modifiers changed from: protected */
        public void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
            this.cipher.init(1, (Key) privateKey, secureRandom);
            this.data = null;
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(byte b) throws SignatureException {
            engineUpdate(new byte[]{b}, 0, 1);
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(byte[] bArr, int i, int i2) throws SignatureException {
            ByteArrayOutputStream byteArrayOutputStream = this.data;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.write(bArr, i, i2);
                return;
            }
            byte[] update = this.cipher.update(bArr, i, i2);
            if (update != null && update.length != 0) {
                throw new SignatureException("Cipher unexpectedly returned data");
            }
        }

        /* access modifiers changed from: protected */
        public byte[] engineSign() throws SignatureException {
            try {
                return this.cipher.doFinal();
            } catch (IllegalBlockSizeException e) {
                throw new SignatureException("doFinal() failed", e);
            } catch (BadPaddingException e2) {
                throw new SignatureException("doFinal() failed", e2);
            }
        }

        /* access modifiers changed from: protected */
        public boolean engineVerify(byte[] bArr) throws SignatureException {
            try {
                byte[] doFinal = this.cipher.doFinal(bArr);
                byte[] byteArray = this.data.toByteArray();
                this.data.reset();
                return MessageDigest.isEqual(doFinal, byteArray);
            } catch (BadPaddingException unused) {
                return false;
            } catch (IllegalBlockSizeException e) {
                throw new SignatureException("doFinal() failed", e);
            }
        }

        /* access modifiers changed from: protected */
        public void engineSetParameter(String str, Object obj) throws InvalidParameterException {
            throw new InvalidParameterException("Parameters not supported");
        }

        /* access modifiers changed from: protected */
        public Object engineGetParameter(String str) throws InvalidParameterException {
            throw new InvalidParameterException("Parameters not supported");
        }
    }
}
