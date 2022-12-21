package sun.security.util;

import java.p026io.IOException;
import java.security.CodeSigner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.Manifest;
import sun.security.jca.Providers;

public class ManifestEntryVerifier {
    private static final Debug debug = Debug.getInstance("jar");
    private static final char[] hexc = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    HashMap<String, MessageDigest> createdDigests = new HashMap<>(11);
    ArrayList<MessageDigest> digests = new ArrayList<>();
    private JarEntry entry;
    private Manifest man;
    ArrayList<byte[]> manifestHashes = new ArrayList<>();
    private String name = null;
    private CodeSigner[] signers = null;
    private boolean skip = true;

    private static class SunProviderHolder {
        /* access modifiers changed from: private */
        public static final Provider instance = Providers.getSunProvider();

        private SunProviderHolder() {
        }
    }

    public ManifestEntryVerifier(Manifest manifest) {
        this.man = manifest;
    }

    public void setEntry(String str, JarEntry jarEntry) throws IOException {
        this.digests.clear();
        this.manifestHashes.clear();
        this.name = str;
        this.entry = jarEntry;
        this.skip = true;
        this.signers = null;
        Manifest manifest = this.man;
        if (manifest != null && str != null) {
            Attributes attributes = manifest.getAttributes(str);
            if (attributes == null) {
                Manifest manifest2 = this.man;
                attributes = manifest2.getAttributes("./" + str);
                if (attributes == null) {
                    Manifest manifest3 = this.man;
                    attributes = manifest3.getAttributes("/" + str);
                    if (attributes == null) {
                        return;
                    }
                }
            }
            for (Map.Entry next : attributes.entrySet()) {
                String obj = next.getKey().toString();
                if (obj.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST")) {
                    String substring = obj.substring(0, obj.length() - 7);
                    MessageDigest messageDigest = this.createdDigests.get(substring);
                    if (messageDigest == null) {
                        try {
                            messageDigest = MessageDigest.getInstance(substring, SunProviderHolder.instance);
                            this.createdDigests.put(substring, messageDigest);
                        } catch (NoSuchAlgorithmException unused) {
                        }
                    }
                    if (messageDigest != null) {
                        this.skip = false;
                        messageDigest.reset();
                        this.digests.add(messageDigest);
                        this.manifestHashes.add(Base64.getMimeDecoder().decode((String) next.getValue()));
                    }
                }
            }
        }
    }

    public void update(byte b) {
        if (!this.skip) {
            for (int i = 0; i < this.digests.size(); i++) {
                this.digests.get(i).update(b);
            }
        }
    }

    public void update(byte[] bArr, int i, int i2) {
        if (!this.skip) {
            for (int i3 = 0; i3 < this.digests.size(); i3++) {
                this.digests.get(i3).update(bArr, i, i2);
            }
        }
    }

    public JarEntry getEntry() {
        return this.entry;
    }

    public CodeSigner[] verify(Hashtable<String, CodeSigner[]> hashtable, Hashtable<String, CodeSigner[]> hashtable2) throws JarException {
        if (this.skip) {
            return null;
        }
        CodeSigner[] codeSignerArr = this.signers;
        if (codeSignerArr != null) {
            return codeSignerArr;
        }
        int i = 0;
        while (i < this.digests.size()) {
            MessageDigest messageDigest = this.digests.get(i);
            byte[] bArr = this.manifestHashes.get(i);
            byte[] digest = messageDigest.digest();
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("Manifest Entry: " + this.name + " digest=" + messageDigest.getAlgorithm());
                StringBuilder sb = new StringBuilder("  manifest ");
                sb.append(toHex(bArr));
                debug2.println(sb.toString());
                debug2.println("  computed " + toHex(digest));
                debug2.println();
            }
            if (MessageDigest.isEqual(digest, bArr)) {
                i++;
            } else {
                throw new SecurityException(messageDigest.getAlgorithm() + " digest error for " + this.name);
            }
        }
        CodeSigner[] remove = hashtable2.remove(this.name);
        this.signers = remove;
        if (remove != null) {
            hashtable.put(this.name, remove);
        }
        return this.signers;
    }

    static String toHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            char[] cArr = hexc;
            stringBuffer.append(cArr[(bArr[i] >> 4) & 15]);
            stringBuffer.append(cArr[bArr[i] & 15]);
        }
        return stringBuffer.toString();
    }
}
