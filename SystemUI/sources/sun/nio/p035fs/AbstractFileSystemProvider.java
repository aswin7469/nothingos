package sun.nio.p035fs;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.p026io.IOException;
import java.util.Map;

/* renamed from: sun.nio.fs.AbstractFileSystemProvider */
abstract class AbstractFileSystemProvider extends FileSystemProvider {
    /* access modifiers changed from: package-private */
    public abstract DynamicFileAttributeView getFileAttributeView(Path path, String str, LinkOption... linkOptionArr);

    /* access modifiers changed from: package-private */
    public abstract boolean implDelete(Path path, boolean z) throws IOException;

    protected AbstractFileSystemProvider() {
    }

    private static String[] split(String str) {
        String[] strArr = new String[2];
        int indexOf = str.indexOf(58);
        if (indexOf == -1) {
            strArr[0] = "basic";
            strArr[1] = str;
        } else {
            int i = indexOf + 1;
            strArr[0] = str.substring(0, indexOf);
            strArr[1] = i == str.length() ? "" : str.substring(i);
        }
        return strArr;
    }

    public final void setAttribute(Path path, String str, Object obj, LinkOption... linkOptionArr) throws IOException {
        String[] split = split(str);
        if (split[0].length() != 0) {
            DynamicFileAttributeView fileAttributeView = getFileAttributeView(path, split[0], linkOptionArr);
            if (fileAttributeView != null) {
                fileAttributeView.setAttribute(split[1], obj);
                return;
            }
            throw new UnsupportedOperationException("View '" + split[0] + "' not available");
        }
        throw new IllegalArgumentException(str);
    }

    public final Map<String, Object> readAttributes(Path path, String str, LinkOption... linkOptionArr) throws IOException {
        String[] split = split(str);
        if (split[0].length() != 0) {
            DynamicFileAttributeView fileAttributeView = getFileAttributeView(path, split[0], linkOptionArr);
            if (fileAttributeView != null) {
                return fileAttributeView.readAttributes(split[1].split(NavigationBarInflaterView.BUTTON_SEPARATOR));
            }
            throw new UnsupportedOperationException("View '" + split[0] + "' not available");
        }
        throw new IllegalArgumentException(str);
    }

    public final void delete(Path path) throws IOException {
        implDelete(path, true);
    }

    public final boolean deleteIfExists(Path path) throws IOException {
        return implDelete(path, false);
    }
}
