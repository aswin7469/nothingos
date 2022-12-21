package android.net;

import android.content.res.Resources;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class IpSecAlgorithm implements Parcelable {
    public static final Map<String, Integer> ALGO_TO_REQUIRED_FIRST_SDK;
    public static final String AUTH_AES_CMAC = "cmac(aes)";
    public static final String AUTH_AES_XCBC = "xcbc(aes)";
    public static final String AUTH_CRYPT_AES_GCM = "rfc4106(gcm(aes))";
    public static final String AUTH_CRYPT_CHACHA20_POLY1305 = "rfc7539esp(chacha20,poly1305)";
    public static final String AUTH_HMAC_MD5 = "hmac(md5)";
    public static final String AUTH_HMAC_SHA1 = "hmac(sha1)";
    public static final String AUTH_HMAC_SHA256 = "hmac(sha256)";
    public static final String AUTH_HMAC_SHA384 = "hmac(sha384)";
    public static final String AUTH_HMAC_SHA512 = "hmac(sha512)";
    public static final Parcelable.Creator<IpSecAlgorithm> CREATOR = new Parcelable.Creator<IpSecAlgorithm>() {
        public IpSecAlgorithm createFromParcel(Parcel parcel) {
            return new IpSecAlgorithm(parcel.readString(), parcel.createByteArray(), parcel.readInt());
        }

        public IpSecAlgorithm[] newArray(int i) {
            return new IpSecAlgorithm[i];
        }
    };
    public static final String CRYPT_AES_CBC = "cbc(aes)";
    public static final String CRYPT_AES_CTR = "rfc3686(ctr(aes))";
    public static final String CRYPT_NULL = "ecb(cipher_null)";
    private static final Set<String> ENABLED_ALGOS = Collections.unmodifiableSet(loadAlgos(Resources.getSystem()));
    private static final int SDK_VERSION_ZERO = 0;
    private static final String TAG = "IpSecAlgorithm";
    private final byte[] mKey;
    private final String mName;
    private final int mTruncLenBits;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AlgorithmName {
    }

    public int describeContents() {
        return 0;
    }

    static {
        HashMap hashMap = new HashMap();
        ALGO_TO_REQUIRED_FIRST_SDK = hashMap;
        hashMap.put(CRYPT_AES_CBC, 0);
        hashMap.put(AUTH_HMAC_MD5, 0);
        hashMap.put(AUTH_HMAC_SHA1, 0);
        hashMap.put(AUTH_HMAC_SHA256, 0);
        hashMap.put(AUTH_HMAC_SHA384, 0);
        hashMap.put(AUTH_HMAC_SHA512, 0);
        hashMap.put(AUTH_CRYPT_AES_GCM, 0);
        hashMap.put(CRYPT_AES_CTR, 31);
        hashMap.put(AUTH_AES_XCBC, 31);
        hashMap.put(AUTH_AES_CMAC, 31);
        hashMap.put(AUTH_CRYPT_CHACHA20_POLY1305, 31);
    }

    public IpSecAlgorithm(String str, byte[] bArr) {
        this(str, bArr, 0);
    }

    public IpSecAlgorithm(String str, byte[] bArr, int i) {
        this.mName = str;
        byte[] bArr2 = (byte[]) bArr.clone();
        this.mKey = bArr2;
        this.mTruncLenBits = i;
        checkValidOrThrow(str, bArr2.length * 8, i);
    }

    public String getName() {
        return this.mName;
    }

    public byte[] getKey() {
        return (byte[]) this.mKey.clone();
    }

    public int getTruncationLengthBits() {
        return this.mTruncLenBits;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName);
        parcel.writeByteArray(this.mKey);
        parcel.writeInt(this.mTruncLenBits);
    }

    public static Set<String> getSupportedAlgorithms() {
        return ENABLED_ALGOS;
    }

    public static Set<String> loadAlgos(Resources resources) {
        HashSet hashSet = new HashSet();
        for (String str : resources.getStringArray(17235974)) {
            if (!ALGO_TO_REQUIRED_FIRST_SDK.containsKey(str) || !hashSet.add(str)) {
                throw new IllegalArgumentException("Invalid or repeated algorithm " + str);
            }
        }
        for (Map.Entry next : ALGO_TO_REQUIRED_FIRST_SDK.entrySet()) {
            if (Build.VERSION.DEVICE_INITIAL_SDK_INT >= ((Integer) next.getValue()).intValue()) {
                hashSet.add((String) next.getKey());
            }
        }
        return hashSet;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0135, code lost:
        if (r12 == 96) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0138, code lost:
        if (r10 == false) goto L_0x0151;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x013a, code lost:
        if (r2 == false) goto L_0x013d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x013c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0150, code lost:
        throw new java.lang.IllegalArgumentException("Invalid truncation keyLength: " + r12);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x0164, code lost:
        throw new java.lang.IllegalArgumentException("Invalid key material keyLength: " + r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00c7, code lost:
        if (r12 <= 160) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c9, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00cf, code lost:
        if (r11 != 288) goto L_0x0113;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00d8, code lost:
        if (r12 == 96) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00e2, code lost:
        if (r12 <= 128) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00e5, code lost:
        r2 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00f1, code lost:
        if (r12 <= 512) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00fd, code lost:
        if (r12 <= 384) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00ff, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0100, code lost:
        r10 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0109, code lost:
        if (r12 <= 256) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0110, code lost:
        if (r11 != 256) goto L_0x0113;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0112, code lost:
        r1 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0113, code lost:
        r10 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x011a, code lost:
        if (r12 == 128) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x012d, code lost:
        if (r12 != 128) goto L_0x00e5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void checkValidOrThrow(java.lang.String r10, int r11, int r12) {
        /*
            java.util.Set r0 = getSupportedAlgorithms()
            boolean r0 = r0.contains(r10)
            if (r0 == 0) goto L_0x0165
            r10.hashCode()
            int r0 = r10.hashCode()
            r1 = 0
            r2 = 1
            r3 = -1
            switch(r0) {
                case -1751374730: goto L_0x0090;
                case -1137603038: goto L_0x0085;
                case -878787135: goto L_0x007a;
                case 394796030: goto L_0x006f;
                case 559425185: goto L_0x0064;
                case 559457797: goto L_0x0059;
                case 559510590: goto L_0x004e;
                case 759177996: goto L_0x0043;
                case 1206161110: goto L_0x0035;
                case 1574008592: goto L_0x0027;
                case 2065384259: goto L_0x0019;
                default: goto L_0x0017;
            }
        L_0x0017:
            goto L_0x009b
        L_0x0019:
            java.lang.String r0 = "hmac(sha1)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x0023
            goto L_0x009b
        L_0x0023:
            r3 = 10
            goto L_0x009b
        L_0x0027:
            java.lang.String r0 = "rfc3686(ctr(aes))"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x0031
            goto L_0x009b
        L_0x0031:
            r3 = 9
            goto L_0x009b
        L_0x0035:
            java.lang.String r0 = "cmac(aes)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x003f
            goto L_0x009b
        L_0x003f:
            r3 = 8
            goto L_0x009b
        L_0x0043:
            java.lang.String r0 = "hmac(md5)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x004c
            goto L_0x009b
        L_0x004c:
            r3 = 7
            goto L_0x009b
        L_0x004e:
            java.lang.String r0 = "hmac(sha512)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x0057
            goto L_0x009b
        L_0x0057:
            r3 = 6
            goto L_0x009b
        L_0x0059:
            java.lang.String r0 = "hmac(sha384)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x0062
            goto L_0x009b
        L_0x0062:
            r3 = 5
            goto L_0x009b
        L_0x0064:
            java.lang.String r0 = "hmac(sha256)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x006d
            goto L_0x009b
        L_0x006d:
            r3 = 4
            goto L_0x009b
        L_0x006f:
            java.lang.String r0 = "cbc(aes)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x0078
            goto L_0x009b
        L_0x0078:
            r3 = 3
            goto L_0x009b
        L_0x007a:
            java.lang.String r0 = "rfc7539esp(chacha20,poly1305)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x0083
            goto L_0x009b
        L_0x0083:
            r3 = 2
            goto L_0x009b
        L_0x0085:
            java.lang.String r0 = "rfc4106(gcm(aes))"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x008e
            goto L_0x009b
        L_0x008e:
            r3 = r2
            goto L_0x009b
        L_0x0090:
            java.lang.String r0 = "xcbc(aes)"
            boolean r0 = r10.equals(r0)
            if (r0 != 0) goto L_0x009a
            goto L_0x009b
        L_0x009a:
            r3 = r1
        L_0x009b:
            r0 = 224(0xe0, float:3.14E-43)
            r4 = 192(0xc0, float:2.69E-43)
            r5 = 288(0x120, float:4.04E-43)
            r6 = 256(0x100, float:3.59E-43)
            r7 = 160(0xa0, float:2.24E-43)
            r8 = 96
            r9 = 128(0x80, float:1.794E-43)
            switch(r3) {
                case 0: goto L_0x0130;
                case 1: goto L_0x011d;
                case 2: goto L_0x0115;
                case 3: goto L_0x010c;
                case 4: goto L_0x0102;
                case 5: goto L_0x00f4;
                case 6: goto L_0x00e8;
                case 7: goto L_0x00db;
                case 8: goto L_0x00d3;
                case 9: goto L_0x00cb;
                case 10: goto L_0x00c0;
                default: goto L_0x00ac;
            }
        L_0x00ac:
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "Couldn't find an algorithm: "
            r12.<init>((java.lang.String) r0)
            r12.append((java.lang.String) r10)
            java.lang.String r10 = r12.toString()
            r11.<init>((java.lang.String) r10)
            throw r11
        L_0x00c0:
            if (r11 != r7) goto L_0x00c4
            r10 = r2
            goto L_0x00c5
        L_0x00c4:
            r10 = r1
        L_0x00c5:
            if (r12 < r8) goto L_0x00e5
            if (r12 > r7) goto L_0x00e5
        L_0x00c9:
            r1 = r2
            goto L_0x00e5
        L_0x00cb:
            if (r11 == r7) goto L_0x0112
            if (r11 == r0) goto L_0x0112
            if (r11 != r5) goto L_0x0113
            goto L_0x0112
        L_0x00d3:
            if (r11 != r9) goto L_0x00d7
            r10 = r2
            goto L_0x00d8
        L_0x00d7:
            r10 = r1
        L_0x00d8:
            if (r12 != r8) goto L_0x00e5
            goto L_0x00c9
        L_0x00db:
            if (r11 != r9) goto L_0x00df
            r10 = r2
            goto L_0x00e0
        L_0x00df:
            r10 = r1
        L_0x00e0:
            if (r12 < r8) goto L_0x00e5
            if (r12 > r9) goto L_0x00e5
            goto L_0x00c9
        L_0x00e5:
            r2 = r1
            goto L_0x0138
        L_0x00e8:
            r10 = 512(0x200, float:7.175E-43)
            if (r11 != r10) goto L_0x00ee
            r0 = r2
            goto L_0x00ef
        L_0x00ee:
            r0 = r1
        L_0x00ef:
            if (r12 < r6) goto L_0x0100
            if (r12 > r10) goto L_0x0100
            goto L_0x00ff
        L_0x00f4:
            r10 = 384(0x180, float:5.38E-43)
            if (r11 != r10) goto L_0x00fa
            r0 = r2
            goto L_0x00fb
        L_0x00fa:
            r0 = r1
        L_0x00fb:
            if (r12 < r4) goto L_0x0100
            if (r12 > r10) goto L_0x0100
        L_0x00ff:
            r1 = r2
        L_0x0100:
            r10 = r0
            goto L_0x00e5
        L_0x0102:
            if (r11 != r6) goto L_0x0106
            r10 = r2
            goto L_0x0107
        L_0x0106:
            r10 = r1
        L_0x0107:
            if (r12 < r8) goto L_0x00e5
            if (r12 > r6) goto L_0x00e5
            goto L_0x00c9
        L_0x010c:
            if (r11 == r9) goto L_0x0112
            if (r11 == r4) goto L_0x0112
            if (r11 != r6) goto L_0x0113
        L_0x0112:
            r1 = r2
        L_0x0113:
            r10 = r1
            goto L_0x0138
        L_0x0115:
            if (r11 != r5) goto L_0x0119
            r10 = r2
            goto L_0x011a
        L_0x0119:
            r10 = r1
        L_0x011a:
            if (r12 != r9) goto L_0x00e5
            goto L_0x00c9
        L_0x011d:
            if (r11 == r7) goto L_0x0126
            if (r11 == r0) goto L_0x0126
            if (r11 != r5) goto L_0x0124
            goto L_0x0126
        L_0x0124:
            r10 = r1
            goto L_0x0127
        L_0x0126:
            r10 = r2
        L_0x0127:
            r0 = 64
            if (r12 == r0) goto L_0x00c9
            if (r12 == r8) goto L_0x00c9
            if (r12 != r9) goto L_0x00e5
            goto L_0x00c9
        L_0x0130:
            if (r11 != r9) goto L_0x0134
            r10 = r2
            goto L_0x0135
        L_0x0134:
            r10 = r1
        L_0x0135:
            if (r12 != r8) goto L_0x00e5
            goto L_0x00c9
        L_0x0138:
            if (r10 == 0) goto L_0x0151
            if (r2 == 0) goto L_0x013d
            return
        L_0x013d:
            java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r0 = "Invalid truncation keyLength: "
            r11.<init>((java.lang.String) r0)
            r11.append((int) r12)
            java.lang.String r11 = r11.toString()
            r10.<init>((java.lang.String) r11)
            throw r10
        L_0x0151:
            java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "Invalid key material keyLength: "
            r12.<init>((java.lang.String) r0)
            r12.append((int) r11)
            java.lang.String r11 = r12.toString()
            r10.<init>((java.lang.String) r11)
            throw r10
        L_0x0165:
            java.lang.IllegalArgumentException r11 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "Unsupported algorithm: "
            r12.<init>((java.lang.String) r0)
            r12.append((java.lang.String) r10)
            java.lang.String r10 = r12.toString()
            r11.<init>((java.lang.String) r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.IpSecAlgorithm.checkValidOrThrow(java.lang.String, int, int):void");
    }

    public boolean isAuthentication() {
        String name = getName();
        name.hashCode();
        char c = 65535;
        switch (name.hashCode()) {
            case -1751374730:
                if (name.equals(AUTH_AES_XCBC)) {
                    c = 0;
                    break;
                }
                break;
            case 559425185:
                if (name.equals(AUTH_HMAC_SHA256)) {
                    c = 1;
                    break;
                }
                break;
            case 559457797:
                if (name.equals(AUTH_HMAC_SHA384)) {
                    c = 2;
                    break;
                }
                break;
            case 559510590:
                if (name.equals(AUTH_HMAC_SHA512)) {
                    c = 3;
                    break;
                }
                break;
            case 759177996:
                if (name.equals(AUTH_HMAC_MD5)) {
                    c = 4;
                    break;
                }
                break;
            case 1206161110:
                if (name.equals(AUTH_AES_CMAC)) {
                    c = 5;
                    break;
                }
                break;
            case 2065384259:
                if (name.equals(AUTH_HMAC_SHA1)) {
                    c = 6;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public boolean isEncryption() {
        String name = getName();
        name.hashCode();
        return name.equals(CRYPT_AES_CBC) || name.equals(CRYPT_AES_CTR);
    }

    public boolean isAead() {
        String name = getName();
        name.hashCode();
        return name.equals(AUTH_CRYPT_AES_GCM) || name.equals(AUTH_CRYPT_CHACHA20_POLY1305);
    }

    public String toString() {
        return "{mName=" + this.mName + ", mTruncLenBits=" + this.mTruncLenBits + "}";
    }

    public static boolean equals(IpSecAlgorithm ipSecAlgorithm, IpSecAlgorithm ipSecAlgorithm2) {
        if (ipSecAlgorithm == null || ipSecAlgorithm2 == null) {
            if (ipSecAlgorithm == ipSecAlgorithm2) {
                return true;
            }
            return false;
        } else if (!ipSecAlgorithm.mName.equals(ipSecAlgorithm2.mName) || !Arrays.equals(ipSecAlgorithm.mKey, ipSecAlgorithm2.mKey) || ipSecAlgorithm.mTruncLenBits != ipSecAlgorithm2.mTruncLenBits) {
            return false;
        } else {
            return true;
        }
    }
}
