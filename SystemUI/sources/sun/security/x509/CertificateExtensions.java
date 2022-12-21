package sun.security.x509;

import java.lang.reflect.InvocationTargetException;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import sun.misc.HexDumpEncoder;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class CertificateExtensions implements CertAttrSet<Extension> {
    public static final String IDENT = "x509.info.extensions";
    public static final String NAME = "extensions";
    private static Class[] PARAMS = {Boolean.class, Object.class};
    private static final Debug debug = Debug.getInstance(X509CertImpl.NAME);
    private Map<String, Extension> map = Collections.synchronizedMap(new TreeMap());
    private Map<String, Extension> unparseableExtensions;
    private boolean unsupportedCritExt = false;

    public String getName() {
        return "extensions";
    }

    public CertificateExtensions() {
    }

    public CertificateExtensions(DerInputStream derInputStream) throws IOException {
        init(derInputStream);
    }

    private void init(DerInputStream derInputStream) throws IOException {
        DerValue[] sequence = derInputStream.getSequence(5);
        for (DerValue extension : sequence) {
            parseExtension(new Extension(extension));
        }
    }

    private void parseExtension(Extension extension) throws IOException {
        try {
            Class<?> cls = OIDMap.getClass(extension.getExtensionId());
            if (cls == null) {
                if (extension.isCritical()) {
                    this.unsupportedCritExt = true;
                }
                if (this.map.put(extension.getExtensionId().toString(), extension) != null) {
                    throw new IOException("Duplicate extensions not allowed");
                }
                return;
            }
            CertAttrSet certAttrSet = (CertAttrSet) cls.getConstructor(PARAMS).newInstance(Boolean.valueOf(extension.isCritical()), extension.getExtensionValue());
            if (this.map.put(certAttrSet.getName(), (Extension) certAttrSet) != null) {
                throw new IOException("Duplicate extensions not allowed");
            }
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (!extension.isCritical()) {
                if (this.unparseableExtensions == null) {
                    this.unparseableExtensions = new TreeMap();
                }
                this.unparseableExtensions.put(extension.getExtensionId().toString(), new UnparseableExtension(extension, targetException));
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("Error parsing extension: " + extension);
                    targetException.printStackTrace();
                    System.err.println(new HexDumpEncoder().encodeBuffer(extension.getExtensionValue()));
                }
            } else if (targetException instanceof IOException) {
                throw ((IOException) targetException);
            } else {
                throw new IOException(targetException);
            }
        } catch (IOException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new IOException((Throwable) e3);
        }
    }

    public void encode(OutputStream outputStream) throws CertificateException, IOException {
        encode(outputStream, false);
    }

    public void encode(OutputStream outputStream, boolean z) throws CertificateException, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        Object[] array = this.map.values().toArray();
        for (Object obj : array) {
            if (obj instanceof CertAttrSet) {
                ((CertAttrSet) obj).encode(derOutputStream);
            } else if (obj instanceof Extension) {
                ((Extension) obj).encode(derOutputStream);
            } else {
                throw new CertificateException("Illegal extension object");
            }
        }
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write((byte) 48, derOutputStream);
        if (!z) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream2);
            derOutputStream2 = derOutputStream3;
        }
        outputStream.write(derOutputStream2.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (obj instanceof Extension) {
            this.map.put(str, (Extension) obj);
            return;
        }
        throw new IOException("Unknown extension type.");
    }

    public Extension get(String str) throws IOException {
        Extension extension = this.map.get(str);
        if (extension != null) {
            return extension;
        }
        throw new IOException("No extension found with name " + str);
    }

    /* access modifiers changed from: package-private */
    public Extension getExtension(String str) {
        return this.map.get(str);
    }

    public void delete(String str) throws IOException {
        if (this.map.get(str) != null) {
            this.map.remove(str);
            return;
        }
        throw new IOException("No extension found with name " + str);
    }

    public String getNameByOid(ObjectIdentifier objectIdentifier) throws IOException {
        for (String next : this.map.keySet()) {
            if (this.map.get(next).getExtensionId().equals((Object) objectIdentifier)) {
                return next;
            }
        }
        return null;
    }

    public Enumeration<Extension> getElements() {
        return Collections.enumeration(this.map.values());
    }

    public Collection<Extension> getAllExtensions() {
        return this.map.values();
    }

    public Map<String, Extension> getUnparseableExtensions() {
        Map<String, Extension> map2 = this.unparseableExtensions;
        return map2 == null ? Collections.emptyMap() : map2;
    }

    public boolean hasUnsupportedCriticalExtension() {
        return this.unsupportedCritExt;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CertificateExtensions)) {
            return false;
        }
        CertificateExtensions certificateExtensions = (CertificateExtensions) obj;
        Object[] array = certificateExtensions.getAllExtensions().toArray();
        int length = array.length;
        if (length != this.map.size()) {
            return false;
        }
        String str = null;
        for (int i = 0; i < length; i++) {
            Object obj2 = array[i];
            if (obj2 instanceof CertAttrSet) {
                str = ((CertAttrSet) obj2).getName();
            }
            Extension extension = (Extension) array[i];
            if (str == null) {
                str = extension.getExtensionId().toString();
            }
            Extension extension2 = this.map.get(str);
            if (extension2 == null || !extension2.equals(extension)) {
                return false;
            }
        }
        return getUnparseableExtensions().equals(certificateExtensions.getUnparseableExtensions());
    }

    public int hashCode() {
        return this.map.hashCode() + getUnparseableExtensions().hashCode();
    }

    public String toString() {
        return this.map.toString();
    }
}
