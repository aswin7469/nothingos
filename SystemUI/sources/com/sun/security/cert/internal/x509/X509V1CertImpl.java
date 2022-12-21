package com.sun.security.cert.internal.x509;

import java.math.BigInteger;
import java.p026io.ByteArrayInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Date;
import javax.security.cert.CertificateEncodingException;
import javax.security.cert.CertificateExpiredException;
import javax.security.cert.CertificateNotYetValidException;
import javax.security.cert.X509Certificate;

public class X509V1CertImpl extends X509Certificate implements Serializable {
    static final long serialVersionUID = -2048442350420423405L;
    private java.security.cert.X509Certificate wrappedCert;

    private static synchronized CertificateFactory getFactory() throws CertificateException {
        CertificateFactory instance;
        synchronized (X509V1CertImpl.class) {
            instance = CertificateFactory.getInstance("X.509");
        }
        return instance;
    }

    public X509V1CertImpl() {
    }

    public X509V1CertImpl(byte[] bArr) throws javax.security.cert.CertificateException {
        try {
            this.wrappedCert = (java.security.cert.X509Certificate) getFactory().generateCertificate(new ByteArrayInputStream(bArr));
        } catch (CertificateException e) {
            throw new javax.security.cert.CertificateException(e.getMessage());
        }
    }

    public X509V1CertImpl(InputStream inputStream) throws javax.security.cert.CertificateException {
        try {
            this.wrappedCert = (java.security.cert.X509Certificate) getFactory().generateCertificate(inputStream);
        } catch (CertificateException e) {
            throw new javax.security.cert.CertificateException(e.getMessage());
        }
    }

    public byte[] getEncoded() throws CertificateEncodingException {
        try {
            return this.wrappedCert.getEncoded();
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new CertificateEncodingException(e.getMessage());
        }
    }

    public void verify(PublicKey publicKey) throws javax.security.cert.CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        try {
            this.wrappedCert.verify(publicKey);
        } catch (CertificateException e) {
            throw new javax.security.cert.CertificateException(e.getMessage());
        }
    }

    public void verify(PublicKey publicKey, String str) throws javax.security.cert.CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        try {
            this.wrappedCert.verify(publicKey, str);
        } catch (CertificateException e) {
            throw new javax.security.cert.CertificateException(e.getMessage());
        }
    }

    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        checkValidity(new Date());
    }

    public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        try {
            this.wrappedCert.checkValidity(date);
        } catch (java.security.cert.CertificateNotYetValidException e) {
            throw new CertificateNotYetValidException(e.getMessage());
        } catch (java.security.cert.CertificateExpiredException e2) {
            throw new CertificateExpiredException(e2.getMessage());
        }
    }

    public String toString() {
        return this.wrappedCert.toString();
    }

    public PublicKey getPublicKey() {
        return this.wrappedCert.getPublicKey();
    }

    public int getVersion() {
        return this.wrappedCert.getVersion() - 1;
    }

    public BigInteger getSerialNumber() {
        return this.wrappedCert.getSerialNumber();
    }

    public Principal getSubjectDN() {
        return this.wrappedCert.getSubjectDN();
    }

    public Principal getIssuerDN() {
        return this.wrappedCert.getIssuerDN();
    }

    public Date getNotBefore() {
        return this.wrappedCert.getNotBefore();
    }

    public Date getNotAfter() {
        return this.wrappedCert.getNotAfter();
    }

    public String getSigAlgName() {
        return this.wrappedCert.getSigAlgName();
    }

    public String getSigAlgOID() {
        return this.wrappedCert.getSigAlgOID();
    }

    public byte[] getSigAlgParams() {
        return this.wrappedCert.getSigAlgParams();
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        try {
            objectOutputStream.write(getEncoded());
        } catch (CertificateEncodingException e) {
            throw new IOException("getEncoded failed: " + e.getMessage());
        }
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException {
        try {
            this.wrappedCert = (java.security.cert.X509Certificate) getFactory().generateCertificate(objectInputStream);
        } catch (CertificateException e) {
            throw new IOException("generateCertificate failed: " + e.getMessage());
        }
    }

    public java.security.cert.X509Certificate getX509Certificate() {
        return this.wrappedCert;
    }
}
