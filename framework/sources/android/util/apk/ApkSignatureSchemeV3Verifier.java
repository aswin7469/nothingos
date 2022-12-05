package android.util.apk;

import android.os.Build;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.apk.ApkSigningBlockUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/* loaded from: classes3.dex */
public class ApkSignatureSchemeV3Verifier {
    private static final int APK_SIGNATURE_SCHEME_V3_BLOCK_ID = -262969152;
    private static final int PROOF_OF_ROTATION_ATTR_ID = 1000370060;
    public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 3;

    public static boolean hasSignature(String apkFile) throws IOException {
        try {
            RandomAccessFile apk = new RandomAccessFile(apkFile, "r");
            findSignature(apk);
            apk.close();
            return true;
        } catch (SignatureNotFoundException e) {
            return false;
        }
    }

    public static VerifiedSigner verify(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, true);
    }

    public static VerifiedSigner unsafeGetCertsWithoutVerification(String apkFile) throws SignatureNotFoundException, SecurityException, IOException {
        return verify(apkFile, false);
    }

    private static VerifiedSigner verify(String apkFile, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        RandomAccessFile apk = new RandomAccessFile(apkFile, "r");
        try {
            VerifiedSigner verify = verify(apk, verifyIntegrity);
            apk.close();
            return verify;
        } catch (Throwable th) {
            try {
                apk.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static VerifiedSigner verify(RandomAccessFile apk, boolean verifyIntegrity) throws SignatureNotFoundException, SecurityException, IOException {
        SignatureInfo signatureInfo = findSignature(apk);
        return verify(apk, signatureInfo, verifyIntegrity);
    }

    public static SignatureInfo findSignature(RandomAccessFile apk) throws IOException, SignatureNotFoundException {
        return ApkSigningBlockUtils.findSignature(apk, APK_SIGNATURE_SCHEME_V3_BLOCK_ID);
    }

    private static VerifiedSigner verify(RandomAccessFile apk, SignatureInfo signatureInfo, boolean doVerifyIntegrity) throws SecurityException, IOException {
        int signerCount = 0;
        Map<Integer, byte[]> contentDigests = new ArrayMap<>();
        Pair<X509Certificate[], ApkSigningBlockUtils.VerifiedProofOfRotation> result = null;
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try {
                ByteBuffer signers = ApkSigningBlockUtils.getLengthPrefixedSlice(signatureInfo.signatureBlock);
                while (signers.hasRemaining()) {
                    try {
                        ByteBuffer signer = ApkSigningBlockUtils.getLengthPrefixedSlice(signers);
                        result = verifySigner(signer, contentDigests, certFactory);
                        signerCount++;
                    } catch (PlatformNotSupportedException e) {
                    } catch (IOException | SecurityException | BufferUnderflowException e2) {
                        throw new SecurityException("Failed to parse/verify signer #" + signerCount + " block", e2);
                    }
                }
                if (signerCount < 1 || result == null) {
                    throw new SecurityException("No signers found");
                }
                if (signerCount != 1) {
                    throw new SecurityException("APK Signature Scheme V3 only supports one signer: multiple signers found.");
                }
                if (contentDigests.isEmpty()) {
                    throw new SecurityException("No content digests found");
                }
                if (doVerifyIntegrity) {
                    ApkSigningBlockUtils.verifyIntegrity(contentDigests, apk, signatureInfo);
                }
                byte[] verityRootHash = null;
                if (contentDigests.containsKey(3)) {
                    byte[] verityDigest = contentDigests.get(3);
                    verityRootHash = ApkSigningBlockUtils.parseVerityDigestAndVerifySourceLength(verityDigest, apk.length(), signatureInfo);
                }
                return new VerifiedSigner(result.first, result.second, verityRootHash, contentDigests);
            } catch (IOException e3) {
                throw new SecurityException("Failed to read list of signers", e3);
            }
        } catch (CertificateException e4) {
            throw new RuntimeException("Failed to obtain X.509 CertificateFactory", e4);
        }
    }

    private static Pair<X509Certificate[], ApkSigningBlockUtils.VerifiedProofOfRotation> verifySigner(ByteBuffer signerBlock, Map<Integer, byte[]> contentDigests, CertificateFactory certFactory) throws SecurityException, IOException, PlatformNotSupportedException {
        ByteBuffer signedData = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        int minSdkVersion = signerBlock.getInt();
        int maxSdkVersion = signerBlock.getInt();
        if (Build.VERSION.SDK_INT < minSdkVersion || Build.VERSION.SDK_INT > maxSdkVersion) {
            throw new PlatformNotSupportedException("Signer not supported by this platform version. This platform: " + Build.VERSION.SDK_INT + ", signer minSdkVersion: " + minSdkVersion + ", maxSdkVersion: " + maxSdkVersion);
        }
        ByteBuffer signatures = ApkSigningBlockUtils.getLengthPrefixedSlice(signerBlock);
        byte[] publicKeyBytes = ApkSigningBlockUtils.readLengthPrefixedByteArray(signerBlock);
        List<Integer> signaturesSigAlgorithms = new ArrayList<>();
        byte[] bestSigAlgorithmSignatureBytes = null;
        int bestSigAlgorithm = -1;
        int signatureCount = 0;
        while (signatures.hasRemaining()) {
            signatureCount++;
            try {
                ByteBuffer signature = ApkSigningBlockUtils.getLengthPrefixedSlice(signatures);
                if (signature.remaining() < 8) {
                    throw new SecurityException("Signature record too short");
                }
                int sigAlgorithm = signature.getInt();
                signaturesSigAlgorithms.add(Integer.valueOf(sigAlgorithm));
                if (ApkSigningBlockUtils.isSupportedSignatureAlgorithm(sigAlgorithm)) {
                    if (bestSigAlgorithm == -1 || ApkSigningBlockUtils.compareSignatureAlgorithm(sigAlgorithm, bestSigAlgorithm) > 0) {
                        bestSigAlgorithm = sigAlgorithm;
                        bestSigAlgorithmSignatureBytes = ApkSigningBlockUtils.readLengthPrefixedByteArray(signature);
                    }
                }
            } catch (IOException | BufferUnderflowException e) {
                throw new SecurityException("Failed to parse signature record #" + signatureCount, e);
            }
        }
        if (bestSigAlgorithm == -1) {
            if (signatureCount == 0) {
                throw new SecurityException("No signatures found");
            }
            throw new SecurityException("No supported signatures found");
        }
        String keyAlgorithm = ApkSigningBlockUtils.getSignatureAlgorithmJcaKeyAlgorithm(bestSigAlgorithm);
        Pair<String, ? extends AlgorithmParameterSpec> signatureAlgorithmParams = ApkSigningBlockUtils.getSignatureAlgorithmJcaSignatureAlgorithm(bestSigAlgorithm);
        String jcaSignatureAlgorithm = signatureAlgorithmParams.first;
        AlgorithmParameterSpec jcaSignatureAlgorithmParams = (AlgorithmParameterSpec) signatureAlgorithmParams.second;
        try {
            PublicKey publicKey = KeyFactory.getInstance(keyAlgorithm).generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            Signature sig = Signature.getInstance(jcaSignatureAlgorithm);
            sig.initVerify(publicKey);
            if (jcaSignatureAlgorithmParams != null) {
                try {
                    sig.setParameter(jcaSignatureAlgorithmParams);
                } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e2) {
                    e = e2;
                    throw new SecurityException("Failed to verify " + jcaSignatureAlgorithm + " signature", e);
                }
            }
            sig.update(signedData);
            boolean sigVerified = sig.verify(bestSigAlgorithmSignatureBytes);
            if (!sigVerified) {
                throw new SecurityException(jcaSignatureAlgorithm + " signature did not verify");
            }
            signedData.clear();
            ByteBuffer digests = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
            List<Integer> digestsSigAlgorithms = new ArrayList<>();
            int digestCount = 0;
            byte[] contentDigest = null;
            while (digests.hasRemaining()) {
                int signatureCount2 = signatureCount;
                int signatureCount3 = digestCount + 1;
                try {
                    ByteBuffer digest = ApkSigningBlockUtils.getLengthPrefixedSlice(digests);
                    byte[] bestSigAlgorithmSignatureBytes2 = bestSigAlgorithmSignatureBytes;
                    try {
                        boolean sigVerified2 = sigVerified;
                        if (digest.remaining() < 8) {
                            throw new IOException("Record too short");
                        }
                        try {
                            int sigAlgorithm2 = digest.getInt();
                            String keyAlgorithm2 = keyAlgorithm;
                            List<Integer> digestsSigAlgorithms2 = digestsSigAlgorithms;
                            try {
                                digestsSigAlgorithms2.add(Integer.valueOf(sigAlgorithm2));
                                if (sigAlgorithm2 == bestSigAlgorithm) {
                                    contentDigest = ApkSigningBlockUtils.readLengthPrefixedByteArray(digest);
                                }
                                digestCount = signatureCount3;
                                digestsSigAlgorithms = digestsSigAlgorithms2;
                                signatureCount = signatureCount2;
                                bestSigAlgorithmSignatureBytes = bestSigAlgorithmSignatureBytes2;
                                sigVerified = sigVerified2;
                                keyAlgorithm = keyAlgorithm2;
                            } catch (IOException | BufferUnderflowException e3) {
                                e = e3;
                            }
                        } catch (IOException | BufferUnderflowException e4) {
                            e = e4;
                        }
                        e = e3;
                    } catch (IOException | BufferUnderflowException e5) {
                        e = e5;
                    }
                } catch (IOException | BufferUnderflowException e6) {
                    e = e6;
                }
                throw new IOException("Failed to parse digest record #" + signatureCount3, e);
            }
            if (!signaturesSigAlgorithms.equals(digestsSigAlgorithms)) {
                throw new SecurityException("Signature algorithms don't match between digests and signatures records");
            }
            int certificateCount = ApkSigningBlockUtils.getSignatureAlgorithmContentDigestAlgorithm(bestSigAlgorithm);
            byte[] previousSignerDigest = contentDigests.put(Integer.valueOf(certificateCount), contentDigest);
            if (previousSignerDigest != null && !MessageDigest.isEqual(previousSignerDigest, contentDigest)) {
                throw new SecurityException(ApkSigningBlockUtils.getContentDigestAlgorithmJcaDigestAlgorithm(certificateCount) + " contents digest does not match the digest specified by a preceding signer");
            }
            List<X509Certificate> certs = new ArrayList<>();
            int certificateCount2 = 0;
            for (ByteBuffer certificates = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData); certificates.hasRemaining(); certificates = certificates) {
                int digestAlgorithm = certificateCount;
                int digestAlgorithm2 = certificateCount2 + 1;
                int bestSigAlgorithm2 = bestSigAlgorithm;
                byte[] encodedCert = ApkSigningBlockUtils.readLengthPrefixedByteArray(certificates);
                try {
                    X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(encodedCert));
                    certs.add(new VerbatimX509Certificate(certificate, encodedCert));
                    certificateCount2 = digestAlgorithm2;
                    certificateCount = digestAlgorithm;
                    bestSigAlgorithm = bestSigAlgorithm2;
                } catch (CertificateException e7) {
                    throw new SecurityException("Failed to decode certificate #" + digestAlgorithm2, e7);
                }
            }
            if (certs.isEmpty()) {
                throw new SecurityException("No certificates listed");
            }
            X509Certificate mainCertificate = certs.get(0);
            byte[] certificatePublicKeyBytes = mainCertificate.getPublicKey().getEncoded();
            if (!Arrays.equals(publicKeyBytes, certificatePublicKeyBytes)) {
                throw new SecurityException("Public key mismatch between certificate and signature record");
            }
            int signedMinSDK = signedData.getInt();
            if (signedMinSDK != minSdkVersion) {
                throw new SecurityException("minSdkVersion mismatch between signed and unsigned in v3 signer block.");
            }
            int signedMaxSDK = signedData.getInt();
            if (signedMaxSDK != maxSdkVersion) {
                throw new SecurityException("maxSdkVersion mismatch between signed and unsigned in v3 signer block.");
            }
            ByteBuffer additionalAttrs = ApkSigningBlockUtils.getLengthPrefixedSlice(signedData);
            return verifyAdditionalAttributes(additionalAttrs, certs, certFactory);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | SignatureException | InvalidKeySpecException e8) {
            e = e8;
        }
    }

    private static Pair<X509Certificate[], ApkSigningBlockUtils.VerifiedProofOfRotation> verifyAdditionalAttributes(ByteBuffer attrs, List<X509Certificate> certs, CertificateFactory certFactory) throws IOException {
        X509Certificate[] certChain = (X509Certificate[]) certs.toArray(new X509Certificate[certs.size()]);
        ApkSigningBlockUtils.VerifiedProofOfRotation por = null;
        while (attrs.hasRemaining()) {
            ByteBuffer attr = ApkSigningBlockUtils.getLengthPrefixedSlice(attrs);
            if (attr.remaining() < 4) {
                throw new IOException("Remaining buffer too short to contain additional attribute ID. Remaining: " + attr.remaining());
            }
            int id = attr.getInt();
            switch (id) {
                case PROOF_OF_ROTATION_ATTR_ID /* 1000370060 */:
                    if (por != null) {
                        throw new SecurityException("Encountered multiple Proof-of-rotation records when verifying APK Signature Scheme v3 signature");
                    }
                    por = ApkSigningBlockUtils.verifyProofOfRotationStruct(attr, certFactory);
                    try {
                        if (por.certs.size() > 0 && !Arrays.equals(por.certs.get(por.certs.size() - 1).getEncoded(), certChain[0].getEncoded())) {
                            throw new SecurityException("Terminal certificate in Proof-of-rotation record does not match APK signing certificate");
                        }
                    } catch (CertificateEncodingException e) {
                        throw new SecurityException("Failed to encode certificate when comparing Proof-of-rotation record and signing certificate", e);
                    }
                    break;
            }
        }
        return Pair.create(certChain, por);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getVerityRootHash(String apkPath) throws IOException, SignatureNotFoundException, SecurityException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, "r");
        try {
            findSignature(apk);
            VerifiedSigner vSigner = verify(apk, false);
            byte[] bArr = vSigner.verityRootHash;
            apk.close();
            return bArr;
        } catch (Throwable th) {
            try {
                apk.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] generateApkVerity(String apkPath, ByteBufferFactory bufferFactory) throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, "r");
        try {
            SignatureInfo signatureInfo = findSignature(apk);
            byte[] generateApkVerity = VerityBuilder.generateApkVerity(apkPath, bufferFactory, signatureInfo);
            apk.close();
            return generateApkVerity;
        } catch (Throwable th) {
            try {
                apk.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] generateApkVerityRootHash(String apkPath) throws NoSuchAlgorithmException, DigestException, IOException, SignatureNotFoundException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, "r");
        try {
            SignatureInfo signatureInfo = findSignature(apk);
            VerifiedSigner vSigner = verify(apk, false);
            if (vSigner.verityRootHash != null) {
                byte[] generateApkVerityRootHash = VerityBuilder.generateApkVerityRootHash(apk, ByteBuffer.wrap(vSigner.verityRootHash), signatureInfo);
                apk.close();
                return generateApkVerityRootHash;
            }
            apk.close();
            return null;
        } catch (Throwable th) {
            try {
                apk.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    /* loaded from: classes3.dex */
    public static class VerifiedSigner {
        public final X509Certificate[] certs;
        public final Map<Integer, byte[]> contentDigests;
        public final ApkSigningBlockUtils.VerifiedProofOfRotation por;
        public final byte[] verityRootHash;

        public VerifiedSigner(X509Certificate[] certs, ApkSigningBlockUtils.VerifiedProofOfRotation por, byte[] verityRootHash, Map<Integer, byte[]> contentDigests) {
            this.certs = certs;
            this.por = por;
            this.verityRootHash = verityRootHash;
            this.contentDigests = contentDigests;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class PlatformNotSupportedException extends Exception {
        PlatformNotSupportedException(String s) {
            super(s);
        }
    }
}
