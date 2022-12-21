package com.nothing.experience.utils;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.HttpUrl;

public class AesEncryptUtil {
    private static final String AES = "AES";
    private static final String AES_CBC_NO_PADDING = "AES/CBC/NoPadding";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final byte BYTE20 = 32;
    private static final byte BYTE3 = 3;
    private static final byte BYTE30 = 48;
    private static final byte BYTE4 = 4;
    private static final byte BYTE41 = 65;
    private static final byte BYTE5 = 5;
    private static final byte BYTE5A = 90;
    private static final byte BYTE6 = 6;
    private static final byte BYTE63 = 99;
    private static final byte RANDOMNUMBER = 100;
    private static final String TAG = "AesEncryptUtil";
    private static final String UTF8 = "UTF-8";
    private static byte[] bKey = {BYTE30, 49, 49, 50, 103, BYTE5A, 104, 71, 71, 71, BYTE41, 53, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20};
    private static byte[] bVector = {49, BYTE30, BYTE30, 51, BYTE63, 101, BYTE20, BYTE20, BYTE20, 33, 33, BYTE20, BYTE20, BYTE20, BYTE20, BYTE20};

    private static SecretKeySpec create128BitsKey(String str) {
        if (str == null) {
            str = HttpUrl.FRAGMENT_ENCODE_SET;
        }
        byte[] bArr = null;
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(bArr, AES);
    }

    private static IvParameterSpec create128BitsIV(String str) {
        if (str == null) {
            str = HttpUrl.FRAGMENT_ENCODE_SET;
        }
        byte[] bArr = null;
        StringBuffer stringBuffer = new StringBuffer(16);
        stringBuffer.append(str);
        while (stringBuffer.length() < 16) {
            stringBuffer.append("0");
        }
        if (stringBuffer.length() > 16) {
            stringBuffer.setLength(16);
        }
        try {
            bArr = stringBuffer.toString().getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(bArr);
    }

    public static byte[] aesCbcPkcs5PaddingEncrypt(byte[] bArr, String str) {
        SecretKeySpec create128BitsKey = create128BitsKey(str);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bVector);
        try {
            Cipher instance = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            instance.init(1, create128BitsKey, ivParameterSpec);
            return instance.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] aesCbcPkcs5PaddingDecrypt(byte[] bArr, String str, String str2) {
        SecretKeySpec create128BitsKey = create128BitsKey(str);
        IvParameterSpec create128BitsIV = create128BitsIV(str2);
        try {
            Cipher instance = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            instance.init(2, create128BitsKey, create128BitsIV);
            return instance.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] aesCbcNoPaddingEncrypt(byte[] bArr, String str, String str2) {
        Cipher cipher;
        int length = bArr.length;
        while (length % 16 != 0) {
            length++;
        }
        byte[] bArr2 = new byte[length];
        for (int i = 0; i < length; i++) {
            if (i < bArr.length) {
                bArr2[i] = bArr[i];
            } else {
                bArr2[i] = 0;
            }
        }
        SecretKeySpec create128BitsKey = create128BitsKey(str);
        IvParameterSpec create128BitsIV = create128BitsIV(str2);
        try {
            cipher = Cipher.getInstance(AES_CBC_NO_PADDING);
            try {
                cipher.init(1, create128BitsKey, create128BitsIV);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            cipher = null;
            e.printStackTrace();
            return cipher.doFinal(bArr2);
        }
        try {
            return cipher.doFinal(bArr2);
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static byte[] aesCbcNoPaddingDecrypt(byte[] bArr, String str, String str2) {
        SecretKeySpec create128BitsKey = create128BitsKey(str);
        IvParameterSpec create128BitsIV = create128BitsIV(str2);
        try {
            Cipher instance = Cipher.getInstance(AES_CBC_NO_PADDING);
            instance.init(2, create128BitsKey, create128BitsIV);
            return instance.doFinal(bArr);
        } catch (Exception unused) {
            return null;
        }
    }
}
