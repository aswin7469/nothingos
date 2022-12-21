package javax.crypto;

import java.net.URL;
import java.p026io.File;
import java.p026io.InputStream;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import sun.security.jca.GetInstance;

final class JceSecurity {
    /* access modifiers changed from: private */
    public static final URL NULL_URL;
    private static final Object PROVIDER_VERIFIED = Boolean.TRUE;
    static final SecureRandom RANDOM = new SecureRandom();
    private static final Map<Class<?>, URL> codeBaseCacheRef = new WeakHashMap();
    private static CryptoPermissions defaultPolicy = null;
    private static CryptoPermissions exemptPolicy = null;
    private static final Map<Provider, Object> verificationResults = new IdentityHashMap();
    private static final Map<Provider, Object> verifyingProviders = new IdentityHashMap();

    static boolean canUseProvider(Provider provider) {
        return true;
    }

    static {
        try {
            NULL_URL = new URL("http://null.sun.com/");
        } catch (Exception e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    private JceSecurity() {
    }

    static GetInstance.Instance getInstance(String str, Class<?> cls, String str2, String str3) throws NoSuchAlgorithmException, NoSuchProviderException {
        Provider.Service service = GetInstance.getService(str, str2, str3);
        Exception verificationResult = getVerificationResult(service.getProvider());
        if (verificationResult == null) {
            return GetInstance.getInstance(service, cls);
        }
        throw ((NoSuchProviderException) new NoSuchProviderException("JCE cannot authenticate the provider " + str3).initCause(verificationResult));
    }

    static GetInstance.Instance getInstance(String str, Class<?> cls, String str2, Provider provider) throws NoSuchAlgorithmException {
        Provider.Service service = GetInstance.getService(str, str2, provider);
        Exception verificationResult = getVerificationResult(provider);
        if (verificationResult == null) {
            return GetInstance.getInstance(service, cls);
        }
        throw new SecurityException("JCE cannot authenticate the provider " + provider.getName(), verificationResult);
    }

    static GetInstance.Instance getInstance(String str, Class<?> cls, String str2) throws NoSuchAlgorithmException {
        NoSuchAlgorithmException e = null;
        for (Provider.Service next : GetInstance.getServices(str, str2)) {
            if (canUseProvider(next.getProvider())) {
                try {
                    return GetInstance.getInstance(next, cls);
                } catch (NoSuchAlgorithmException e2) {
                    e = e2;
                }
            }
        }
        throw new NoSuchAlgorithmException("Algorithm " + str2 + " not available", e);
    }

    static CryptoPermissions verifyExemptJar(URL url) throws Exception {
        JarVerifier jarVerifier = new JarVerifier(url, true);
        jarVerifier.verify();
        return jarVerifier.getPermissions();
    }

    static void verifyProviderJar(URL url) throws Exception {
        new JarVerifier(url, false).verify();
    }

    static synchronized Exception getVerificationResult(Provider provider) {
        synchronized (JceSecurity.class) {
            Map<Provider, Object> map = verificationResults;
            Object obj = map.get(provider);
            Object obj2 = PROVIDER_VERIFIED;
            if (obj == obj2) {
                return null;
            }
            if (obj != null) {
                Exception exc = (Exception) obj;
                return exc;
            }
            Map<Provider, Object> map2 = verifyingProviders;
            if (map2.get(provider) != null) {
                NoSuchProviderException noSuchProviderException = new NoSuchProviderException("Recursion during verification");
                return noSuchProviderException;
            }
            try {
                map2.put(provider, Boolean.FALSE);
                verifyProviderJar(getCodeBase(provider.getClass()));
                map.put(provider, obj2);
                map2.remove(provider);
                return null;
            } catch (Exception e) {
                try {
                    verificationResults.put(provider, e);
                    return e;
                } finally {
                    verifyingProviders.remove(provider);
                }
            }
        }
    }

    static URL getCodeBase(final Class<?> cls) {
        URL url;
        Map<Class<?>, URL> map = codeBaseCacheRef;
        synchronized (map) {
            url = map.get(cls);
            if (url == null) {
                url = (URL) AccessController.doPrivileged(new PrivilegedAction<URL>() {
                    public URL run() {
                        CodeSource codeSource;
                        ProtectionDomain protectionDomain = Class.this.getProtectionDomain();
                        if (protectionDomain == null || (codeSource = protectionDomain.getCodeSource()) == null) {
                            return JceSecurity.NULL_URL;
                        }
                        return codeSource.getLocation();
                    }
                });
                map.put(cls, url);
            }
            if (url == NULL_URL) {
                url = null;
            }
        }
        return url;
    }

    private static void loadPolicies(File file, CryptoPermissions cryptoPermissions, CryptoPermissions cryptoPermissions2) throws Exception {
        InputStream inputStream;
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry nextElement = entries.nextElement();
            InputStream inputStream2 = null;
            try {
                if (nextElement.getName().startsWith("default_")) {
                    inputStream = jarFile.getInputStream(nextElement);
                    cryptoPermissions.load(inputStream);
                } else if (nextElement.getName().startsWith("exempt_")) {
                    inputStream = jarFile.getInputStream(nextElement);
                    cryptoPermissions2.load(inputStream);
                }
                JarVerifier.verifyPolicySigned(nextElement.getCertificates());
            } finally {
                if (inputStream2 != null) {
                    inputStream2.close();
                }
            }
        }
        jarFile.close();
    }

    static CryptoPermissions getDefaultPolicy() {
        return defaultPolicy;
    }

    static CryptoPermissions getExemptPolicy() {
        return exemptPolicy;
    }
}
