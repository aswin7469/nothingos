package java.security;

import android.net.wifi.WifiEnterpriseConfig;
import java.p026io.BufferedInputStream;
import java.p026io.DataInputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import sun.security.util.Debug;

public class KeyStore {
    private static final String KEYSTORE_TYPE = "keystore.type";
    private static final Debug kdebug = Debug.getInstance(WifiEnterpriseConfig.ENGINE_ID_KEYSTORE);
    /* access modifiers changed from: private */
    public boolean initialized = false;
    private KeyStoreSpi keyStoreSpi;
    private Provider provider;
    private String type;

    public interface LoadStoreParameter {
        ProtectionParameter getProtectionParameter();
    }

    public interface ProtectionParameter {
    }

    public static class PasswordProtection implements ProtectionParameter, Destroyable {
        private volatile boolean destroyed = false;
        private final char[] password;
        private final String protectionAlgorithm;
        private final AlgorithmParameterSpec protectionParameters;

        public PasswordProtection(char[] cArr) {
            this.password = cArr == null ? null : (char[]) cArr.clone();
            this.protectionAlgorithm = null;
            this.protectionParameters = null;
        }

        public PasswordProtection(char[] cArr, String str, AlgorithmParameterSpec algorithmParameterSpec) {
            if (str != null) {
                this.password = cArr == null ? null : (char[]) cArr.clone();
                this.protectionAlgorithm = str;
                this.protectionParameters = algorithmParameterSpec;
                return;
            }
            throw new NullPointerException("invalid null input");
        }

        public String getProtectionAlgorithm() {
            return this.protectionAlgorithm;
        }

        public AlgorithmParameterSpec getProtectionParameters() {
            return this.protectionParameters;
        }

        public synchronized char[] getPassword() {
            if (!this.destroyed) {
            } else {
                throw new IllegalStateException("password has been cleared");
            }
            return this.password;
        }

        public synchronized void destroy() throws DestroyFailedException {
            this.destroyed = true;
            char[] cArr = this.password;
            if (cArr != null) {
                Arrays.fill(cArr, ' ');
            }
        }

        public synchronized boolean isDestroyed() {
            return this.destroyed;
        }
    }

    public static class CallbackHandlerProtection implements ProtectionParameter {
        private final CallbackHandler handler;

        public CallbackHandlerProtection(CallbackHandler callbackHandler) {
            if (callbackHandler != null) {
                this.handler = callbackHandler;
                return;
            }
            throw new NullPointerException("handler must not be null");
        }

        public CallbackHandler getCallbackHandler() {
            return this.handler;
        }
    }

    public interface Entry {

        public interface Attribute {
            String getName();

            String getValue();
        }

        Set<Attribute> getAttributes() {
            return Collections.emptySet();
        }
    }

    public static final class PrivateKeyEntry implements Entry {
        private final Set<Entry.Attribute> attributes;
        private final Certificate[] chain;
        private final PrivateKey privKey;

        public PrivateKeyEntry(PrivateKey privateKey, Certificate[] certificateArr) {
            this(privateKey, certificateArr, Collections.emptySet());
        }

        public PrivateKeyEntry(PrivateKey privateKey, Certificate[] certificateArr, Set<Entry.Attribute> set) {
            if (privateKey == null || certificateArr == null || set == null) {
                throw new NullPointerException("invalid null input");
            } else if (certificateArr.length != 0) {
                Certificate[] certificateArr2 = (Certificate[]) certificateArr.clone();
                String type = certificateArr2[0].getType();
                int i = 1;
                while (i < certificateArr2.length) {
                    if (type.equals(certificateArr2[i].getType())) {
                        i++;
                    } else {
                        throw new IllegalArgumentException("chain does not contain certificates of the same type");
                    }
                }
                if (privateKey.getAlgorithm().equals(certificateArr2[0].getPublicKey().getAlgorithm())) {
                    this.privKey = privateKey;
                    if (!(certificateArr2[0] instanceof X509Certificate) || (certificateArr2 instanceof X509Certificate[])) {
                        this.chain = certificateArr2;
                    } else {
                        X509Certificate[] x509CertificateArr = new X509Certificate[certificateArr2.length];
                        this.chain = x509CertificateArr;
                        System.arraycopy((Object) certificateArr2, 0, (Object) x509CertificateArr, 0, certificateArr2.length);
                    }
                    this.attributes = Collections.unmodifiableSet(new HashSet(set));
                    return;
                }
                throw new IllegalArgumentException("private key algorithm does not match algorithm of public key in end entity certificate (at index 0)");
            } else {
                throw new IllegalArgumentException("invalid zero-length input chain");
            }
        }

        public PrivateKey getPrivateKey() {
            return this.privKey;
        }

        public Certificate[] getCertificateChain() {
            return (Certificate[]) this.chain.clone();
        }

        public Certificate getCertificate() {
            return this.chain[0];
        }

        public Set<Entry.Attribute> getAttributes() {
            return this.attributes;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Private key entry and certificate chain with " + this.chain.length + " elements:\r\n");
            for (Certificate append : this.chain) {
                sb.append((Object) append);
                sb.append("\r\n");
            }
            return sb.toString();
        }
    }

    public static final class SecretKeyEntry implements Entry {
        private final Set<Entry.Attribute> attributes;
        private final SecretKey sKey;

        public SecretKeyEntry(SecretKey secretKey) {
            if (secretKey != null) {
                this.sKey = secretKey;
                this.attributes = Collections.emptySet();
                return;
            }
            throw new NullPointerException("invalid null input");
        }

        public SecretKeyEntry(SecretKey secretKey, Set<Entry.Attribute> set) {
            if (secretKey == null || set == null) {
                throw new NullPointerException("invalid null input");
            }
            this.sKey = secretKey;
            this.attributes = Collections.unmodifiableSet(new HashSet(set));
        }

        public SecretKey getSecretKey() {
            return this.sKey;
        }

        public Set<Entry.Attribute> getAttributes() {
            return this.attributes;
        }

        public String toString() {
            return "Secret key entry with algorithm " + this.sKey.getAlgorithm();
        }
    }

    public static final class TrustedCertificateEntry implements Entry {
        private final Set<Entry.Attribute> attributes;
        private final Certificate cert;

        public TrustedCertificateEntry(Certificate certificate) {
            if (certificate != null) {
                this.cert = certificate;
                this.attributes = Collections.emptySet();
                return;
            }
            throw new NullPointerException("invalid null input");
        }

        public TrustedCertificateEntry(Certificate certificate, Set<Entry.Attribute> set) {
            if (certificate == null || set == null) {
                throw new NullPointerException("invalid null input");
            }
            this.cert = certificate;
            this.attributes = Collections.unmodifiableSet(new HashSet(set));
        }

        public Certificate getTrustedCertificate() {
            return this.cert;
        }

        public Set<Entry.Attribute> getAttributes() {
            return this.attributes;
        }

        public String toString() {
            return "Trusted certificate entry:\r\n" + this.cert.toString();
        }
    }

    protected KeyStore(KeyStoreSpi keyStoreSpi2, Provider provider2, String str) {
        this.keyStoreSpi = keyStoreSpi2;
        this.provider = provider2;
        this.type = str;
    }

    public static KeyStore getInstance(String str) throws KeyStoreException {
        try {
            String str2 = null;
            Object[] impl = Security.getImpl(str, "KeyStore", (String) null);
            return new KeyStore((KeyStoreSpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreException(str + " not found", e);
        } catch (NoSuchProviderException e2) {
            throw new KeyStoreException(str + " not found", e2);
        }
    }

    public static KeyStore getInstance(String str, String str2) throws KeyStoreException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        try {
            Object[] impl = Security.getImpl(str, "KeyStore", str2);
            return new KeyStore((KeyStoreSpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreException(str + " not found", e);
        }
    }

    public static KeyStore getInstance(String str, Provider provider2) throws KeyStoreException {
        if (provider2 != null) {
            Objects.requireNonNull(str, "null type name");
            try {
                Object[] impl = Security.getImpl(str, "KeyStore", provider2);
                return new KeyStore((KeyStoreSpi) impl[0], (Provider) impl[1], str);
            } catch (NoSuchAlgorithmException e) {
                throw new KeyStoreException(str + " not found", e);
            }
        } else {
            throw new IllegalArgumentException("missing provider");
        }
    }

    public static final String getDefaultType() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return Security.getProperty(KeyStore.KEYSTORE_TYPE);
            }
        });
        return str == null ? "jks" : str;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final String getType() {
        return this.type;
    }

    public final Key getKey(String str, char[] cArr) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (this.initialized) {
            return this.keyStoreSpi.engineGetKey(str, cArr);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final Certificate[] getCertificateChain(String str) throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineGetCertificateChain(str);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final Certificate getCertificate(String str) throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineGetCertificate(str);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final Date getCreationDate(String str) throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineGetCreationDate(str);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final void setKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        } else if (!(key instanceof PrivateKey) || !(certificateArr == null || certificateArr.length == 0)) {
            this.keyStoreSpi.engineSetKeyEntry(str, key, cArr, certificateArr);
        } else {
            throw new IllegalArgumentException("Private key must be accompanied by certificate chain");
        }
    }

    public final void setKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        if (this.initialized) {
            this.keyStoreSpi.engineSetKeyEntry(str, bArr, certificateArr);
            return;
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final void setCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        if (this.initialized) {
            this.keyStoreSpi.engineSetCertificateEntry(str, certificate);
            return;
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final void deleteEntry(String str) throws KeyStoreException {
        if (this.initialized) {
            this.keyStoreSpi.engineDeleteEntry(str);
            return;
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final Enumeration<String> aliases() throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineAliases();
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final boolean containsAlias(String str) throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineContainsAlias(str);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final int size() throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineSize();
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final boolean isKeyEntry(String str) throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineIsKeyEntry(str);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final boolean isCertificateEntry(String str) throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineIsCertificateEntry(str);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final String getCertificateAlias(Certificate certificate) throws KeyStoreException {
        if (this.initialized) {
            return this.keyStoreSpi.engineGetCertificateAlias(certificate);
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final void store(OutputStream outputStream, char[] cArr) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        if (this.initialized) {
            this.keyStoreSpi.engineStore(outputStream, cArr);
            return;
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final void store(LoadStoreParameter loadStoreParameter) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        if (this.initialized) {
            this.keyStoreSpi.engineStore(loadStoreParameter);
            return;
        }
        throw new KeyStoreException("Uninitialized keystore");
    }

    public final void load(InputStream inputStream, char[] cArr) throws IOException, NoSuchAlgorithmException, CertificateException {
        this.keyStoreSpi.engineLoad(inputStream, cArr);
        this.initialized = true;
    }

    public final void load(LoadStoreParameter loadStoreParameter) throws IOException, NoSuchAlgorithmException, CertificateException {
        this.keyStoreSpi.engineLoad(loadStoreParameter);
        this.initialized = true;
    }

    public final Entry getEntry(String str, ProtectionParameter protectionParameter) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
        if (str == null) {
            throw new NullPointerException("invalid null input");
        } else if (this.initialized) {
            return this.keyStoreSpi.engineGetEntry(str, protectionParameter);
        } else {
            throw new KeyStoreException("Uninitialized keystore");
        }
    }

    public final void setEntry(String str, Entry entry, ProtectionParameter protectionParameter) throws KeyStoreException {
        if (str == null || entry == null) {
            throw new NullPointerException("invalid null input");
        } else if (this.initialized) {
            this.keyStoreSpi.engineSetEntry(str, entry, protectionParameter);
        } else {
            throw new KeyStoreException("Uninitialized keystore");
        }
    }

    public final boolean entryInstanceOf(String str, Class<? extends Entry> cls) throws KeyStoreException {
        if (str == null || cls == null) {
            throw new NullPointerException("invalid null input");
        } else if (this.initialized) {
            return this.keyStoreSpi.engineEntryInstanceOf(str, cls);
        } else {
            throw new KeyStoreException("Uninitialized keystore");
        }
    }

    public static final KeyStore getInstance(File file, char[] cArr) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        return getInstance(file, cArr, (LoadStoreParameter) null, true);
    }

    public static final KeyStore getInstance(File file, LoadStoreParameter loadStoreParameter) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        return getInstance(file, (char[]) null, loadStoreParameter, false);
    }

    private static final KeyStore getInstance(File file, char[] cArr, LoadStoreParameter loadStoreParameter, boolean z) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore;
        String next;
        file.getClass();
        if (file.isFile()) {
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            try {
                dataInputStream.mark(Integer.MAX_VALUE);
                Iterator<String> it = Security.getAlgorithms("KeyStore").iterator();
                while (true) {
                    keyStore = null;
                    if (!it.hasNext()) {
                        break;
                    }
                    next = it.next();
                    String str = null;
                    Object[] impl = Security.getImpl(next, "KeyStore", (String) null);
                    KeyStoreSpi keyStoreSpi2 = (KeyStoreSpi) impl[0];
                    if (keyStoreSpi2.engineProbe(dataInputStream)) {
                        Debug debug = kdebug;
                        if (debug != null) {
                            debug.println(next + " keystore detected: " + file);
                        }
                        keyStore = new KeyStore(keyStoreSpi2, (Provider) impl[1], next);
                    } else {
                        dataInputStream.reset();
                    }
                }
                if (keyStore != null) {
                    dataInputStream.reset();
                    if (z) {
                        keyStore.load(dataInputStream, cArr);
                    } else {
                        keyStore.keyStoreSpi.engineLoad((InputStream) dataInputStream, loadStoreParameter);
                        keyStore.initialized = true;
                    }
                    dataInputStream.close();
                    return keyStore;
                }
                dataInputStream.close();
                throw new KeyStoreException("Unrecognized keystore format. Please load it with a specified type");
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                Debug debug2 = kdebug;
                if (debug2 != null) {
                    debug2.println(next + " not found - " + e);
                }
            } catch (IOException e2) {
                Debug debug3 = kdebug;
                if (debug3 != null) {
                    debug3.println("I/O error in " + file + " - " + e2);
                }
            } catch (Throwable th) {
                try {
                    dataInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } else {
            throw new IllegalArgumentException("File does not exist or it does not refer to a normal file: " + file);
        }
    }

    public static abstract class Builder {
        static final int MAX_CALLBACK_TRIES = 3;

        public abstract KeyStore getKeyStore() throws KeyStoreException;

        public abstract ProtectionParameter getProtectionParameter(String str) throws KeyStoreException;

        protected Builder() {
        }

        public static Builder newInstance(final KeyStore keyStore, final ProtectionParameter protectionParameter) {
            if (keyStore == null || protectionParameter == null) {
                throw null;
            } else if (keyStore.initialized) {
                return new Builder() {
                    private volatile boolean getCalled;

                    public KeyStore getKeyStore() {
                        this.getCalled = true;
                        return KeyStore.this;
                    }

                    public ProtectionParameter getProtectionParameter(String str) {
                        str.getClass();
                        if (this.getCalled) {
                            return protectionParameter;
                        }
                        throw new IllegalStateException("getKeyStore() must be called first");
                    }
                };
            } else {
                throw new IllegalArgumentException("KeyStore not initialized");
            }
        }

        public static Builder newInstance(String str, Provider provider, File file, ProtectionParameter protectionParameter) {
            if (str == null || file == null || protectionParameter == null) {
                throw null;
            } else if (!(protectionParameter instanceof PasswordProtection) && !(protectionParameter instanceof CallbackHandlerProtection)) {
                throw new IllegalArgumentException("Protection must be PasswordProtection or CallbackHandlerProtection");
            } else if (file.isFile()) {
                return new FileBuilder(str, provider, file, protectionParameter, AccessController.getContext());
            } else {
                throw new IllegalArgumentException("File does not exist or it does not refer to a normal file: " + file);
            }
        }

        public static Builder newInstance(File file, ProtectionParameter protectionParameter) {
            return newInstance("", (Provider) null, file, protectionParameter);
        }

        private static final class FileBuilder extends Builder {
            private final AccessControlContext context;
            /* access modifiers changed from: private */
            public final File file;
            /* access modifiers changed from: private */
            public ProtectionParameter keyProtection;
            private KeyStore keyStore;
            private Throwable oldException;
            /* access modifiers changed from: private */
            public ProtectionParameter protection;
            /* access modifiers changed from: private */
            public final Provider provider;
            /* access modifiers changed from: private */
            public final String type;

            FileBuilder(String str, Provider provider2, File file2, ProtectionParameter protectionParameter, AccessControlContext accessControlContext) {
                this.type = str;
                this.provider = provider2;
                this.file = file2;
                this.protection = protectionParameter;
                this.context = accessControlContext;
            }

            public synchronized KeyStore getKeyStore() throws KeyStoreException {
                KeyStore keyStore2 = this.keyStore;
                if (keyStore2 != null) {
                    return keyStore2;
                }
                Throwable th = this.oldException;
                if (th == null) {
                    try {
                        KeyStore keyStore3 = (KeyStore) AccessController.doPrivileged(new PrivilegedExceptionAction<KeyStore>() {
                            public KeyStore run() throws Exception {
                                if (!(FileBuilder.this.protection instanceof CallbackHandlerProtection)) {
                                    return run0();
                                }
                                int i = 0;
                                do {
                                    i++;
                                    try {
                                        return run0();
                                    } catch (IOException e) {
                                        if (i >= 3 || (e.getCause() instanceof UnrecoverableKeyException)) {
                                            throw e;
                                        }
                                        do {
                                            i++;
                                            return run0();
                                        } while (e.getCause() instanceof UnrecoverableKeyException);
                                        throw e;
                                    }
                                } while (e.getCause() instanceof UnrecoverableKeyException);
                                throw e;
                            }

                            public KeyStore run0() throws Exception {
                                char[] cArr;
                                KeyStore keyStore;
                                if (FileBuilder.this.protection instanceof PasswordProtection) {
                                    cArr = ((PasswordProtection) FileBuilder.this.protection).getPassword();
                                    FileBuilder fileBuilder = FileBuilder.this;
                                    fileBuilder.keyProtection = fileBuilder.protection;
                                } else {
                                    CallbackHandler callbackHandler = ((CallbackHandlerProtection) FileBuilder.this.protection).getCallbackHandler();
                                    PasswordCallback passwordCallback = new PasswordCallback("Password for keystore " + FileBuilder.this.file.getName(), false);
                                    callbackHandler.handle(new Callback[]{passwordCallback});
                                    cArr = passwordCallback.getPassword();
                                    if (cArr != null) {
                                        passwordCallback.clearPassword();
                                        FileBuilder.this.keyProtection = new PasswordProtection(cArr);
                                    } else {
                                        throw new KeyStoreException("No password provided");
                                    }
                                }
                                if (FileBuilder.this.type.isEmpty()) {
                                    return KeyStore.getInstance(FileBuilder.this.file, cArr);
                                }
                                if (FileBuilder.this.provider == null) {
                                    keyStore = KeyStore.getInstance(FileBuilder.this.type);
                                } else {
                                    keyStore = KeyStore.getInstance(FileBuilder.this.type, FileBuilder.this.provider);
                                }
                                FileInputStream fileInputStream = new FileInputStream(FileBuilder.this.file);
                                try {
                                    keyStore.load(fileInputStream, cArr);
                                    fileInputStream.close();
                                    return keyStore;
                                } catch (Throwable th) {
                                    th.addSuppressed(th);
                                }
                                throw th;
                            }
                        }, this.context);
                        this.keyStore = keyStore3;
                        return keyStore3;
                    } catch (PrivilegedActionException e) {
                        Throwable cause = e.getCause();
                        this.oldException = cause;
                        throw new KeyStoreException("KeyStore instantiation failed", cause);
                    }
                } else {
                    throw new KeyStoreException("Previous KeyStore instantiation failed", th);
                }
            }

            public synchronized ProtectionParameter getProtectionParameter(String str) {
                if (str != null) {
                    try {
                        if (this.keyStore != null) {
                        } else {
                            throw new IllegalStateException("getKeyStore() must be called first");
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                } else {
                    throw new NullPointerException();
                }
                return this.keyProtection;
            }
        }

        public static Builder newInstance(final String str, final Provider provider, final ProtectionParameter protectionParameter) {
            if (str == null || protectionParameter == null) {
                throw null;
            }
            final AccessControlContext context = AccessController.getContext();
            return new Builder() {
                private final PrivilegedExceptionAction<KeyStore> action = new PrivilegedExceptionAction<KeyStore>() {
                    public KeyStore run() throws Exception {
                        KeyStore keyStore;
                        if (Provider.this == null) {
                            keyStore = KeyStore.getInstance(str);
                        } else {
                            keyStore = KeyStore.getInstance(str, Provider.this);
                        }
                        SimpleLoadStoreParameter simpleLoadStoreParameter = new SimpleLoadStoreParameter(protectionParameter);
                        if (!(protectionParameter instanceof CallbackHandlerProtection)) {
                            keyStore.load(simpleLoadStoreParameter);
                        } else {
                            int i = 0;
                            while (true) {
                                i++;
                                try {
                                    keyStore.load(simpleLoadStoreParameter);
                                    break;
                                } catch (IOException e) {
                                    if (e.getCause() instanceof UnrecoverableKeyException) {
                                        if (i >= 3) {
                                            C15592.this.oldException = e;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                    throw e;
                                }
                            }
                            throw e;
                        }
                        C15592.this.getCalled = true;
                        return keyStore;
                    }
                };
                /* access modifiers changed from: private */
                public volatile boolean getCalled;
                /* access modifiers changed from: private */
                public IOException oldException;

                public synchronized KeyStore getKeyStore() throws KeyStoreException {
                    IOException iOException = this.oldException;
                    if (iOException == null) {
                        try {
                        } catch (PrivilegedActionException e) {
                            throw new KeyStoreException("KeyStore instantiation failed", e.getCause());
                        }
                    } else {
                        throw new KeyStoreException("Previous KeyStore instantiation failed", iOException);
                    }
                    return (KeyStore) AccessController.doPrivileged(this.action, context);
                }

                public ProtectionParameter getProtectionParameter(String str) {
                    str.getClass();
                    if (this.getCalled) {
                        return protectionParameter;
                    }
                    throw new IllegalStateException("getKeyStore() must be called first");
                }
            };
        }
    }

    static class SimpleLoadStoreParameter implements LoadStoreParameter {
        private final ProtectionParameter protection;

        SimpleLoadStoreParameter(ProtectionParameter protectionParameter) {
            this.protection = protectionParameter;
        }

        public ProtectionParameter getProtectionParameter() {
            return this.protection;
        }
    }
}
