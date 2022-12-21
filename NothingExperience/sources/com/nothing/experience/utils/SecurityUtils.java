package com.nothing.experience.utils;

import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class SecurityUtils {
    private static final String ALGORITHM_RSA = "RSA";
    public static final byte[] rsa_public_key = Base64.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBCDnqRY+7Rag1eCkzH/zeqc36oZk8Jm3fA1T4gbghzCTIA5f8joV7+5YA9N6ZPCkbCVqQ4A93RkBCHUOHzBa6dojThIADICyz+f3/kN5e543/ypEbxPQf7JOk5tW2crmm/iEKpTKcERYpEKjmRsx/cqHxn1X1qT+Bb205P3xP8QIDAQAB", 2);

    public static String getKey() throws Exception {
        return new StringUtils(32).getString();
    }

    public static byte[] encrypt(String str, String str2) throws Exception {
        return AesEncryptUtil.aesCbcPkcs5PaddingEncrypt(str.getBytes(StandardCharsets.UTF_8), str2);
    }

    public static String encryptionByPublicKey(String str, byte[] bArr) throws Exception {
        return encryptByPublicKey(str, getPublicKey(bArr));
    }

    public static String encryptByPublicKey(String str, PublicKey publicKey) throws Exception {
        KeyFactory.getInstance(ALGORITHM_RSA);
        Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        instance.init(1, publicKey);
        instance.getProvider().getClass().getName();
        return Base64.encodeToString(rsaSplitCodec(instance, 1, str.getBytes(StandardCharsets.UTF_8), ((RSAPublicKey) publicKey).getModulus().bitLength()), 2);
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int i, byte[] bArr, int i2) {
        int i3;
        byte[] bArr2;
        if (i == 2) {
            i3 = i2 / 8;
        } else {
            i3 = (i2 / 8) - 11;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i4 = 0;
        int i5 = 0;
        while (bArr.length > i4) {
            try {
                if (bArr.length - i4 > i3) {
                    bArr2 = cipher.doFinal(bArr, i4, i3);
                } else {
                    bArr2 = cipher.doFinal(bArr, i4, bArr.length - i4);
                }
                byteArrayOutputStream.write(bArr2, 0, bArr2.length);
                i5++;
                i4 = i5 * i3;
            } catch (Exception e) {
                throw new RuntimeException("decrypt [" + i3 + "] error", e);
            }
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return byteArray;
    }

    private static PrivateKey getPrivateKey(byte[] bArr) throws Exception {
        return KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(new PKCS8EncodedKeySpec(bArr));
    }

    public static String encodeHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", new Object[]{Byte.valueOf(bArr[i])}));
            sb.append(",");
        }
        return sb.toString();
    }

    public static PublicKey getPublicKey(byte[] bArr) throws Exception {
        return KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(new X509EncodedKeySpec(bArr));
    }
}
