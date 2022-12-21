package java.security;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;
import javax.crypto.SecretKey;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public abstract class KeyStoreSpi {
    public abstract Enumeration<String> engineAliases();

    public abstract boolean engineContainsAlias(String str);

    public abstract void engineDeleteEntry(String str) throws KeyStoreException;

    public abstract Certificate engineGetCertificate(String str);

    public abstract String engineGetCertificateAlias(Certificate certificate);

    public abstract Certificate[] engineGetCertificateChain(String str);

    public abstract Date engineGetCreationDate(String str);

    public abstract Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException;

    public abstract boolean engineIsCertificateEntry(String str);

    public abstract boolean engineIsKeyEntry(String str);

    public abstract void engineLoad(InputStream inputStream, char[] cArr) throws IOException, NoSuchAlgorithmException, CertificateException;

    public abstract void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException;

    public abstract void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException;

    public abstract void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException;

    public abstract int engineSize();

    public abstract void engineStore(OutputStream outputStream, char[] cArr) throws IOException, NoSuchAlgorithmException, CertificateException;

    public void engineStore(KeyStore.LoadStoreParameter loadStoreParameter) throws IOException, NoSuchAlgorithmException, CertificateException {
        throw new UnsupportedOperationException();
    }

    public void engineLoad(KeyStore.LoadStoreParameter loadStoreParameter) throws IOException, NoSuchAlgorithmException, CertificateException {
        engineLoad((InputStream) null, loadStoreParameter);
    }

    /* access modifiers changed from: package-private */
    public void engineLoad(InputStream inputStream, KeyStore.LoadStoreParameter loadStoreParameter) throws IOException, NoSuchAlgorithmException, CertificateException {
        char[] cArr;
        if (loadStoreParameter == null) {
            InputStream inputStream2 = null;
            char[] cArr2 = null;
            engineLoad((InputStream) null, (char[]) null);
        } else if (loadStoreParameter instanceof KeyStore.SimpleLoadStoreParameter) {
            KeyStore.ProtectionParameter protectionParameter = loadStoreParameter.getProtectionParameter();
            if (protectionParameter instanceof KeyStore.PasswordProtection) {
                cArr = ((KeyStore.PasswordProtection) protectionParameter).getPassword();
            } else if (protectionParameter instanceof KeyStore.CallbackHandlerProtection) {
                CallbackHandler callbackHandler = ((KeyStore.CallbackHandlerProtection) protectionParameter).getCallbackHandler();
                PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
                try {
                    callbackHandler.handle(new Callback[]{passwordCallback});
                    cArr = passwordCallback.getPassword();
                    passwordCallback.clearPassword();
                    if (cArr == null) {
                        throw new NoSuchAlgorithmException("No password provided");
                    }
                } catch (UnsupportedCallbackException e) {
                    throw new NoSuchAlgorithmException("Could not obtain password", e);
                }
            } else {
                throw new NoSuchAlgorithmException("ProtectionParameter must be PasswordProtection or CallbackHandlerProtection");
            }
            engineLoad((InputStream) null, cArr);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public KeyStore.Entry engineGetEntry(String str, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
        boolean z;
        char[] cArr = null;
        if (str == null) {
            return null;
        }
        if (engineIsKeyEntry(str)) {
            z = false;
        } else if (!engineContainsAlias(str)) {
            return null;
        } else {
            z = true;
        }
        if (protectionParameter == null && z) {
            return new KeyStore.TrustedCertificateEntry(engineGetCertificate(str));
        }
        if (protectionParameter == null || (protectionParameter instanceof KeyStore.PasswordProtection)) {
            if (!z) {
                if (protectionParameter != null) {
                    cArr = ((KeyStore.PasswordProtection) protectionParameter).getPassword();
                }
                Key engineGetKey = engineGetKey(str, cArr);
                if (engineGetKey instanceof PrivateKey) {
                    return new KeyStore.PrivateKeyEntry((PrivateKey) engineGetKey, engineGetCertificateChain(str));
                } else if (engineGetKey instanceof SecretKey) {
                    return new KeyStore.SecretKeyEntry((SecretKey) engineGetKey);
                }
            } else {
                throw new UnsupportedOperationException("trusted certificate entries are not password-protected");
            }
        }
        throw new UnsupportedOperationException();
    }

    public void engineSetEntry(String str, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
        char[] cArr;
        if (protectionParameter == null || (protectionParameter instanceof KeyStore.PasswordProtection)) {
            KeyStore.PasswordProtection passwordProtection = protectionParameter != null ? (KeyStore.PasswordProtection) protectionParameter : null;
            if (passwordProtection == null) {
                cArr = null;
            } else {
                cArr = passwordProtection.getPassword();
            }
            if (entry instanceof KeyStore.TrustedCertificateEntry) {
                engineSetCertificateEntry(str, ((KeyStore.TrustedCertificateEntry) entry).getTrustedCertificate());
            } else if (entry instanceof KeyStore.PrivateKeyEntry) {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;
                engineSetKeyEntry(str, privateKeyEntry.getPrivateKey(), cArr, privateKeyEntry.getCertificateChain());
            } else if (entry instanceof KeyStore.SecretKeyEntry) {
                Certificate[] certificateArr = null;
                engineSetKeyEntry(str, ((KeyStore.SecretKeyEntry) entry).getSecretKey(), cArr, (Certificate[]) null);
            } else {
                throw new KeyStoreException("unsupported entry type: " + entry.getClass().getName());
            }
        } else {
            throw new KeyStoreException("unsupported protection parameter");
        }
    }

    public boolean engineEntryInstanceOf(String str, Class<? extends KeyStore.Entry> cls) {
        if (cls == KeyStore.TrustedCertificateEntry.class) {
            return engineIsCertificateEntry(str);
        }
        if (cls == KeyStore.PrivateKeyEntry.class) {
            if (!engineIsKeyEntry(str) || engineGetCertificate(str) == null) {
                return false;
            }
            return true;
        } else if (cls != KeyStore.SecretKeyEntry.class) {
            return false;
        } else {
            if (!engineIsKeyEntry(str) || engineGetCertificate(str) != null) {
                return false;
            }
            return true;
        }
    }

    public boolean engineProbe(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            return false;
        }
        throw new NullPointerException("input stream must not be null");
    }
}
