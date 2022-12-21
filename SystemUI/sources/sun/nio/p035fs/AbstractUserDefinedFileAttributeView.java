package sun.nio.p035fs;

import java.nio.ByteBuffer;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: sun.nio.fs.AbstractUserDefinedFileAttributeView */
abstract class AbstractUserDefinedFileAttributeView implements UserDefinedFileAttributeView, DynamicFileAttributeView {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public final String name() {
        return "user";
    }

    protected AbstractUserDefinedFileAttributeView() {
    }

    /* access modifiers changed from: protected */
    public void checkAccess(String str, boolean z, boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (z) {
                securityManager.checkRead(str);
            }
            if (z2) {
                securityManager.checkWrite(str);
            }
            securityManager.checkPermission(new RuntimePermission("accessUserDefinedAttributes"));
        }
    }

    public final void setAttribute(String str, Object obj) throws IOException {
        ByteBuffer byteBuffer;
        if (obj instanceof byte[]) {
            byteBuffer = ByteBuffer.wrap((byte[]) obj);
        } else {
            byteBuffer = (ByteBuffer) obj;
        }
        write(str, byteBuffer);
    }

    public final Map<String, Object> readAttributes(String[] strArr) throws IOException {
        List<String> arrayList = new ArrayList<>();
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String str = strArr[i];
            if (str.equals("*")) {
                arrayList = list();
                break;
            } else if (!str.isEmpty()) {
                arrayList.add(str);
                i++;
            } else {
                throw new IllegalArgumentException();
            }
        }
        HashMap hashMap = new HashMap();
        for (String str2 : arrayList) {
            int size = size(str2);
            byte[] bArr = new byte[size];
            int read = read(str2, ByteBuffer.wrap(bArr));
            if (read != size) {
                bArr = Arrays.copyOf(bArr, read);
            }
            hashMap.put(str2, bArr);
        }
        return hashMap;
    }
}
