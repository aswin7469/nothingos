package sun.nio.p035fs;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/* renamed from: sun.nio.fs.UnixFileModeAttribute */
class UnixFileModeAttribute {
    static final int ALL_PERMISSIONS = ((((((((UnixConstants.S_IRUSR | UnixConstants.S_IWUSR) | UnixConstants.S_IXUSR) | UnixConstants.S_IRGRP) | UnixConstants.S_IWGRP) | UnixConstants.S_IXGRP) | UnixConstants.S_IROTH) | UnixConstants.S_IWOTH) | UnixConstants.S_IXOTH);
    static final int ALL_READWRITE = (((((UnixConstants.S_IRUSR | UnixConstants.S_IWUSR) | UnixConstants.S_IRGRP) | UnixConstants.S_IWGRP) | UnixConstants.S_IROTH) | UnixConstants.S_IWOTH);
    static final int TEMPFILE_PERMISSIONS = ((UnixConstants.S_IRUSR | UnixConstants.S_IWUSR) | UnixConstants.S_IXUSR);

    private UnixFileModeAttribute() {
    }

    static int toUnixMode(Set<PosixFilePermission> set) {
        int i;
        int i2 = 0;
        for (PosixFilePermission next : set) {
            next.getClass();
            switch (C47921.$SwitchMap$java$nio$file$attribute$PosixFilePermission[next.ordinal()]) {
                case 1:
                    i = UnixConstants.S_IRUSR;
                    break;
                case 2:
                    i = UnixConstants.S_IWUSR;
                    break;
                case 3:
                    i = UnixConstants.S_IXUSR;
                    break;
                case 4:
                    i = UnixConstants.S_IRGRP;
                    break;
                case 5:
                    i = UnixConstants.S_IWGRP;
                    break;
                case 6:
                    i = UnixConstants.S_IXGRP;
                    break;
                case 7:
                    i = UnixConstants.S_IROTH;
                    break;
                case 8:
                    i = UnixConstants.S_IWOTH;
                    break;
                case 9:
                    i = UnixConstants.S_IXOTH;
                    break;
            }
            i2 |= i;
        }
        return i2;
    }

    /* renamed from: sun.nio.fs.UnixFileModeAttribute$1 */
    static /* synthetic */ class C47921 {
        static final /* synthetic */ int[] $SwitchMap$java$nio$file$attribute$PosixFilePermission;

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|(3:17|18|20)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                java.nio.file.attribute.PosixFilePermission[] r0 = java.nio.file.attribute.PosixFilePermission.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$nio$file$attribute$PosixFilePermission = r0
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.OWNER_READ     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x001d }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.OWNER_WRITE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.GROUP_READ     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x003e }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.GROUP_WRITE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.GROUP_EXECUTE     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.OTHERS_READ     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x0060 }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.OTHERS_WRITE     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$java$nio$file$attribute$PosixFilePermission     // Catch:{ NoSuchFieldError -> 0x006c }
                java.nio.file.attribute.PosixFilePermission r1 = java.nio.file.attribute.PosixFilePermission.OTHERS_EXECUTE     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixFileModeAttribute.C47921.<clinit>():void");
        }
    }

    static int toUnixMode(int i, FileAttribute<?>... fileAttributeArr) {
        int length = fileAttributeArr.length;
        int i2 = 0;
        while (i2 < length) {
            FileAttribute<?> fileAttribute = fileAttributeArr[i2];
            String name = fileAttribute.name();
            if (name.equals("posix:permissions") || name.equals("unix:permissions")) {
                i = toUnixMode((Set) fileAttribute.value());
                i2++;
            } else {
                throw new UnsupportedOperationException("'" + fileAttribute.name() + "' not supported as initial attribute");
            }
        }
        return i;
    }
}
