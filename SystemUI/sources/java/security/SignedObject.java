package java.security;

import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import sun.security.x509.X509CertImpl;

public final class SignedObject implements Serializable {
    private static final long serialVersionUID = 720502720485447167L;
    private byte[] content;
    private byte[] signature;
    private String thealgorithm;

    public SignedObject(Serializable serializable, PrivateKey privateKey, Signature signature2) throws IOException, InvalidKeyException, SignatureException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(serializable);
        objectOutputStream.flush();
        objectOutputStream.close();
        this.content = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        sign(privateKey, signature2);
    }

    public Object getObject() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.content);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object readObject = objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return readObject;
    }

    public byte[] getSignature() {
        return (byte[]) this.signature.clone();
    }

    public String getAlgorithm() {
        return this.thealgorithm;
    }

    public boolean verify(PublicKey publicKey, Signature signature2) throws InvalidKeyException, SignatureException {
        signature2.initVerify(publicKey);
        signature2.update((byte[]) this.content.clone());
        return signature2.verify((byte[]) this.signature.clone());
    }

    private void sign(PrivateKey privateKey, Signature signature2) throws InvalidKeyException, SignatureException {
        signature2.initSign(privateKey);
        signature2.update((byte[]) this.content.clone());
        this.signature = (byte[]) signature2.sign().clone();
        this.thealgorithm = signature2.getAlgorithm();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        this.content = (byte[]) ((byte[]) readFields.get("content", (Object) null)).clone();
        this.signature = (byte[]) ((byte[]) readFields.get(X509CertImpl.SIGNATURE, (Object) null)).clone();
        this.thealgorithm = (String) readFields.get("thealgorithm", (Object) null);
    }
}
