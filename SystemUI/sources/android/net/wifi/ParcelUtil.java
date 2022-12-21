package android.net.wifi;

import android.os.Parcel;
import java.p026io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class ParcelUtil {
    public static void writePrivateKey(Parcel parcel, PrivateKey privateKey) {
        if (privateKey == null) {
            parcel.writeString((String) null);
            return;
        }
        parcel.writeString(privateKey.getAlgorithm());
        parcel.writeByteArray(privateKey.getEncoded());
    }

    public static PrivateKey readPrivateKey(Parcel parcel) {
        String readString = parcel.readString();
        if (readString == null) {
            return null;
        }
        try {
            return KeyFactory.getInstance(readString).generatePrivate(new PKCS8EncodedKeySpec(parcel.createByteArray()));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException unused) {
            return null;
        }
    }

    public static void writeCertificate(Parcel parcel, X509Certificate x509Certificate) {
        byte[] bArr;
        if (x509Certificate != null) {
            try {
                bArr = x509Certificate.getEncoded();
            } catch (CertificateEncodingException unused) {
            }
            parcel.writeByteArray(bArr);
        }
        bArr = null;
        parcel.writeByteArray(bArr);
    }

    public static X509Certificate readCertificate(Parcel parcel) {
        byte[] createByteArray = parcel.createByteArray();
        if (createByteArray == null) {
            return null;
        }
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(createByteArray));
        } catch (CertificateException unused) {
            return null;
        }
    }

    public static void writeCertificates(Parcel parcel, X509Certificate[] x509CertificateArr) {
        if (x509CertificateArr == null || x509CertificateArr.length == 0) {
            parcel.writeInt(0);
            return;
        }
        parcel.writeInt(x509CertificateArr.length);
        for (X509Certificate writeCertificate : x509CertificateArr) {
            writeCertificate(parcel, writeCertificate);
        }
    }

    public static X509Certificate[] readCertificates(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt == 0) {
            return null;
        }
        X509Certificate[] x509CertificateArr = new X509Certificate[readInt];
        for (int i = 0; i < readInt; i++) {
            x509CertificateArr[i] = readCertificate(parcel);
        }
        return x509CertificateArr;
    }
}
