package javax.crypto;

import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class SealedObject implements Serializable {
    static final long serialVersionUID = 4482838265551344752L;
    protected byte[] encodedParams = null;
    private byte[] encryptedContent = null;
    private String paramsAlg = null;
    private String sealAlg = null;

    public SealedObject(Serializable serializable, Cipher cipher) throws IOException, IllegalBlockSizeException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        try {
            objectOutputStream.writeObject(serializable);
            objectOutputStream.flush();
            try {
                this.encryptedContent = cipher.doFinal(byteArrayOutputStream.toByteArray());
            } catch (BadPaddingException unused) {
            }
            if (cipher.getParameters() != null) {
                this.encodedParams = cipher.getParameters().getEncoded();
                this.paramsAlg = cipher.getParameters().getAlgorithm();
            }
            this.sealAlg = cipher.getAlgorithm();
        } finally {
            objectOutputStream.close();
        }
    }

    protected SealedObject(SealedObject sealedObject) {
        this.encryptedContent = (byte[]) sealedObject.encryptedContent.clone();
        this.sealAlg = sealedObject.sealAlg;
        this.paramsAlg = sealedObject.paramsAlg;
        byte[] bArr = sealedObject.encodedParams;
        if (bArr != null) {
            this.encodedParams = (byte[]) bArr.clone();
        } else {
            this.encodedParams = null;
        }
    }

    public final String getAlgorithm() {
        return this.sealAlg;
    }

    public final Object getObject(Key key) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {
        if (key != null) {
            try {
                return unseal(key, (String) null);
            } catch (NoSuchProviderException unused) {
                throw new NoSuchAlgorithmException("algorithm not found");
            } catch (IllegalBlockSizeException e) {
                throw new InvalidKeyException(e.getMessage());
            } catch (BadPaddingException e2) {
                throw new InvalidKeyException(e2.getMessage());
            }
        } else {
            throw new NullPointerException("key is null");
        }
    }

    public final Object getObject(Cipher cipher) throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
        extObjectInputStream extobjectinputstream = new extObjectInputStream(new ByteArrayInputStream(cipher.doFinal(this.encryptedContent)));
        try {
            return extobjectinputstream.readObject();
        } finally {
            extobjectinputstream.close();
        }
    }

    public final Object getObject(Key key, String str) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        } else {
            try {
                return unseal(key, str);
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                throw new InvalidKeyException(e.getMessage());
            }
        }
    }

    private Object unseal(Key key, String str) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        AlgorithmParameters algorithmParameters;
        Cipher cipher;
        if (this.encodedParams != null) {
            if (str != null) {
                try {
                    algorithmParameters = AlgorithmParameters.getInstance(this.paramsAlg, str);
                } catch (NoSuchProviderException e) {
                    if (str == null) {
                        throw new NoSuchAlgorithmException(this.paramsAlg + " not found");
                    }
                    throw new NoSuchProviderException(e.getMessage());
                }
            } else {
                algorithmParameters = AlgorithmParameters.getInstance(this.paramsAlg);
            }
            algorithmParameters.init(this.encodedParams);
        } else {
            algorithmParameters = null;
        }
        if (str != null) {
            try {
                cipher = Cipher.getInstance(this.sealAlg, str);
            } catch (NoSuchPaddingException unused) {
                throw new NoSuchAlgorithmException("Padding that was used in sealing operation not available");
            } catch (NoSuchProviderException e2) {
                if (str == null) {
                    throw new NoSuchAlgorithmException(this.sealAlg + " not found");
                }
                throw new NoSuchProviderException(e2.getMessage());
            }
        } else {
            cipher = Cipher.getInstance(this.sealAlg);
        }
        if (algorithmParameters != null) {
            try {
                cipher.init(2, key, algorithmParameters);
            } catch (InvalidAlgorithmParameterException e3) {
                throw new RuntimeException(e3.getMessage());
            }
        } else {
            cipher.init(2, key);
        }
        extObjectInputStream extobjectinputstream = new extObjectInputStream(new ByteArrayInputStream(cipher.doFinal(this.encryptedContent)));
        try {
            return extobjectinputstream.readObject();
        } finally {
            extobjectinputstream.close();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        byte[] bArr = this.encryptedContent;
        if (bArr != null) {
            this.encryptedContent = (byte[]) bArr.clone();
        }
        byte[] bArr2 = this.encodedParams;
        if (bArr2 != null) {
            this.encodedParams = (byte[]) bArr2.clone();
        }
    }
}
