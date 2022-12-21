package java.security;

import androidx.core.p004os.EnvironmentCompat;
import java.security.Provider;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.security.jca.GetInstance;
import sun.security.jca.Providers;

public class SecureRandom extends Random {
    private static volatile SecureRandom seedGenerator = null;
    static final long serialVersionUID = 4940670005562187L;
    private String algorithm;
    private long counter;
    private MessageDigest digest;
    private Provider provider;
    private byte[] randomBytes;
    private int randomBytesUsed;
    private SecureRandomSpi secureRandomSpi;
    private byte[] state;

    public SecureRandom() {
        super(0);
        this.provider = null;
        this.secureRandomSpi = null;
        this.digest = null;
        getDefaultPRNG(false, (byte[]) null);
    }

    public SecureRandom(byte[] bArr) {
        super(0);
        this.provider = null;
        this.secureRandomSpi = null;
        this.digest = null;
        getDefaultPRNG(true, bArr);
    }

    private void getDefaultPRNG(boolean z, byte[] bArr) {
        String prngAlgorithm = getPrngAlgorithm();
        if (prngAlgorithm != null) {
            try {
                SecureRandom instance = getInstance(prngAlgorithm);
                this.secureRandomSpi = instance.getSecureRandomSpi();
                this.provider = instance.getProvider();
                if (z) {
                    this.secureRandomSpi.engineSetSeed(bArr);
                }
                if (getClass() == SecureRandom.class) {
                    this.algorithm = prngAlgorithm;
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException((Throwable) e);
            }
        } else {
            throw new IllegalStateException("No SecureRandom implementation!");
        }
    }

    protected SecureRandom(SecureRandomSpi secureRandomSpi2, Provider provider2) {
        this(secureRandomSpi2, provider2, (String) null);
    }

    private SecureRandom(SecureRandomSpi secureRandomSpi2, Provider provider2, String str) {
        super(0);
        this.digest = null;
        this.secureRandomSpi = secureRandomSpi2;
        this.provider = provider2;
        this.algorithm = str;
    }

    public static SecureRandom getInstance(String str) throws NoSuchAlgorithmException {
        GetInstance.Instance instance = GetInstance.getInstance("SecureRandom", (Class<?>) SecureRandomSpi.class, str);
        return new SecureRandom((SecureRandomSpi) instance.impl, instance.provider, str);
    }

    public static SecureRandom getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance instance = GetInstance.getInstance("SecureRandom", (Class<?>) SecureRandomSpi.class, str, str2);
        return new SecureRandom((SecureRandomSpi) instance.impl, instance.provider, str);
    }

    public static SecureRandom getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        GetInstance.Instance instance = GetInstance.getInstance("SecureRandom", (Class<?>) SecureRandomSpi.class, str, provider2);
        return new SecureRandom((SecureRandomSpi) instance.impl, instance.provider, str);
    }

    /* access modifiers changed from: package-private */
    public SecureRandomSpi getSecureRandomSpi() {
        return this.secureRandomSpi;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public String getAlgorithm() {
        String str = this.algorithm;
        return str != null ? str : EnvironmentCompat.MEDIA_UNKNOWN;
    }

    public synchronized void setSeed(byte[] bArr) {
        this.secureRandomSpi.engineSetSeed(bArr);
    }

    public void setSeed(long j) {
        if (j != 0) {
            this.secureRandomSpi.engineSetSeed(longToByteArray(j));
        }
    }

    public synchronized void nextBytes(byte[] bArr) {
        this.secureRandomSpi.engineNextBytes(bArr);
    }

    /* access modifiers changed from: protected */
    public final int next(int i) {
        int i2 = (i + 7) / 8;
        byte[] bArr = new byte[i2];
        nextBytes(bArr);
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 = (i3 << 8) + (bArr[i4] & 255);
        }
        return i3 >>> ((i2 * 8) - i);
    }

    public static byte[] getSeed(int i) {
        if (seedGenerator == null) {
            seedGenerator = new SecureRandom();
        }
        return seedGenerator.generateSeed(i);
    }

    public byte[] generateSeed(int i) {
        return this.secureRandomSpi.engineGenerateSeed(i);
    }

    private static byte[] longToByteArray(long j) {
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((int) j);
            j >>= 8;
        }
        return bArr;
    }

    private static String getPrngAlgorithm() {
        for (Provider services : Providers.getProviderList().providers()) {
            Iterator<Provider.Service> it = services.getServices().iterator();
            while (true) {
                if (it.hasNext()) {
                    Provider.Service next = it.next();
                    if (next.getType().equals("SecureRandom")) {
                        return next.getAlgorithm();
                    }
                }
            }
        }
        return null;
    }

    private static final class StrongPatternHolder {
        /* access modifiers changed from: private */
        public static Pattern pattern = Pattern.compile("\\s*([\\S&&[^:,]]*)(\\:([\\S&&[^,]]*))?\\s*(\\,(.*))?");

        private StrongPatternHolder() {
        }
    }

    public static SecureRandom getInstanceStrong() throws NoSuchAlgorithmException {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return Security.getProperty("securerandom.strongAlgorithms");
            }
        });
        if (str == null || str.length() == 0) {
            throw new NoSuchAlgorithmException("Null/empty securerandom.strongAlgorithms Security Property");
        }
        String str2 = str;
        while (str2 != null) {
            Matcher matcher = StrongPatternHolder.pattern.matcher(str2);
            if (matcher.matches()) {
                String group = matcher.group(1);
                String group2 = matcher.group(3);
                if (group2 != null) {
                    return getInstance(group, group2);
                }
                try {
                    return getInstance(group);
                } catch (NoSuchAlgorithmException | NoSuchProviderException unused) {
                    str2 = matcher.group(5);
                }
            } else {
                str2 = null;
            }
        }
        throw new NoSuchAlgorithmException("No strong SecureRandom impls available: " + str);
    }
}
