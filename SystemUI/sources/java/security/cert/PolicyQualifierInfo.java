package java.security.cert;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import sun.security.util.DerValue;
import sun.security.util.HexDumpEncoder;

public class PolicyQualifierInfo {
    private byte[] mData;
    private byte[] mEncoded;
    private String mId;
    private String pqiString;

    public PolicyQualifierInfo(byte[] bArr) throws IOException {
        byte[] bArr2 = (byte[]) bArr.clone();
        this.mEncoded = bArr2;
        DerValue derValue = new DerValue(bArr2);
        if (derValue.tag == 48) {
            this.mId = derValue.data.getDerValue().getOID().toString();
            byte[] byteArray = derValue.data.toByteArray();
            if (byteArray == null) {
                this.mData = null;
                return;
            }
            byte[] bArr3 = new byte[byteArray.length];
            this.mData = bArr3;
            System.arraycopy((Object) byteArray, 0, (Object) bArr3, 0, byteArray.length);
            return;
        }
        throw new IOException("Invalid encoding for PolicyQualifierInfo");
    }

    public final String getPolicyQualifierId() {
        return this.mId;
    }

    public final byte[] getEncoded() {
        return (byte[]) this.mEncoded.clone();
    }

    public final byte[] getPolicyQualifier() {
        byte[] bArr = this.mData;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public String toString() {
        String str = this.pqiString;
        if (str != null) {
            return str;
        }
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        StringBuilder sb = new StringBuilder("PolicyQualifierInfo: [\n");
        sb.append("  qualifierID: " + this.mId + "\n");
        StringBuilder sb2 = new StringBuilder("  qualifier: ");
        byte[] bArr = this.mData;
        sb2.append(bArr == null ? "null" : hexDumpEncoder.encodeBuffer(bArr));
        sb2.append("\n");
        sb.append(sb2.toString());
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        String sb3 = sb.toString();
        this.pqiString = sb3;
        return sb3;
    }
}
