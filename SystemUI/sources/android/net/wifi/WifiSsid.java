package android.net.wifi;

import android.net.wifi.util.HexEncoding;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.p026io.ByteArrayOutputStream;
import java.util.Arrays;

public final class WifiSsid implements Parcelable {
    public static final Parcelable.Creator<WifiSsid> CREATOR = new Parcelable.Creator<WifiSsid>() {
        public WifiSsid createFromParcel(Parcel parcel) {
            return new WifiSsid(parcel.createByteArray());
        }

        public WifiSsid[] newArray(int i) {
            return new WifiSsid[i];
        }
    };
    public static final String NONE = "<unknown ssid>";
    private final byte[] mBytes;
    public final ByteArrayOutputStream octets;

    public int describeContents() {
        return 0;
    }

    private WifiSsid(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(32);
        this.octets = byteArrayOutputStream;
        bArr = bArr == null ? new byte[0] : bArr;
        if (bArr.length <= 32) {
            this.mBytes = bArr;
            byteArrayOutputStream.write(bArr, 0, bArr.length);
            return;
        }
        throw new IllegalArgumentException("Max SSID length is 32 bytes, but received " + bArr.length + " bytes!");
    }

    public static WifiSsid fromBytes(byte[] bArr) {
        return new WifiSsid(bArr);
    }

    public byte[] getBytes() {
        return this.mBytes;
    }

    public static WifiSsid fromUtf8Text(CharSequence charSequence) {
        if (charSequence == null) {
            return new WifiSsid((byte[]) null);
        }
        return new WifiSsid(charSequence.toString().getBytes(StandardCharsets.UTF_8));
    }

    public CharSequence getUtf8Text() {
        return decodeSsid(this.mBytes, StandardCharsets.UTF_8);
    }

    public static WifiSsid fromString(String str) {
        if (str == null) {
            return new WifiSsid((byte[]) null);
        }
        int length = str.length();
        if (length > 1 && str.charAt(0) == '\"') {
            int i = length - 1;
            if (str.charAt(i) == '\"') {
                return new WifiSsid(str.substring(1, i).getBytes(StandardCharsets.UTF_8));
            }
        }
        return new WifiSsid(HexEncoding.decode(str));
    }

    public String toString() {
        String decodeSsid = decodeSsid(this.mBytes, StandardCharsets.UTF_8);
        if (TextUtils.isEmpty(decodeSsid)) {
            return HexEncoding.encodeToString(this.mBytes);
        }
        return "\"" + decodeSsid + "\"";
    }

    private static String decodeSsid(byte[] bArr, Charset charset) {
        CharsetDecoder onUnmappableCharacter = charset.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        CharBuffer allocate = CharBuffer.allocate(32);
        CoderResult decode = onUnmappableCharacter.decode(ByteBuffer.wrap(bArr), allocate, true);
        allocate.flip();
        if (decode.isError()) {
            return null;
        }
        return allocate.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiSsid)) {
            return false;
        }
        return Arrays.equals(this.mBytes, ((WifiSsid) obj).mBytes);
    }

    public int hashCode() {
        return Arrays.hashCode(this.mBytes);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.mBytes);
    }

    public static WifiSsid createFromAsciiEncoded(String str) {
        return fromUtf8Text(str);
    }

    public byte[] getOctets() {
        return getBytes();
    }
}
