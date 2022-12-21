package java.nio.file.attribute;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class PosixFilePermissions {
    private PosixFilePermissions() {
    }

    private static void writeBits(StringBuilder sb, boolean z, boolean z2, boolean z3) {
        if (z) {
            sb.append('r');
        } else {
            sb.append('-');
        }
        if (z2) {
            sb.append('w');
        } else {
            sb.append('-');
        }
        if (z3) {
            sb.append('x');
        } else {
            sb.append('-');
        }
    }

    public static String toString(Set<PosixFilePermission> set) {
        StringBuilder sb = new StringBuilder(9);
        writeBits(sb, set.contains(PosixFilePermission.OWNER_READ), set.contains(PosixFilePermission.OWNER_WRITE), set.contains(PosixFilePermission.OWNER_EXECUTE));
        writeBits(sb, set.contains(PosixFilePermission.GROUP_READ), set.contains(PosixFilePermission.GROUP_WRITE), set.contains(PosixFilePermission.GROUP_EXECUTE));
        writeBits(sb, set.contains(PosixFilePermission.OTHERS_READ), set.contains(PosixFilePermission.OTHERS_WRITE), set.contains(PosixFilePermission.OTHERS_EXECUTE));
        return sb.toString();
    }

    private static boolean isSet(char c, char c2) {
        if (c == c2) {
            return true;
        }
        if (c == '-') {
            return false;
        }
        throw new IllegalArgumentException("Invalid mode");
    }

    private static boolean isR(char c) {
        return isSet(c, 'r');
    }

    private static boolean isW(char c) {
        return isSet(c, 'w');
    }

    private static boolean isX(char c) {
        return isSet(c, 'x');
    }

    public static Set<PosixFilePermission> fromString(String str) {
        if (str.length() == 9) {
            EnumSet<E> noneOf = EnumSet.noneOf(PosixFilePermission.class);
            if (isR(str.charAt(0))) {
                noneOf.add(PosixFilePermission.OWNER_READ);
            }
            if (isW(str.charAt(1))) {
                noneOf.add(PosixFilePermission.OWNER_WRITE);
            }
            if (isX(str.charAt(2))) {
                noneOf.add(PosixFilePermission.OWNER_EXECUTE);
            }
            if (isR(str.charAt(3))) {
                noneOf.add(PosixFilePermission.GROUP_READ);
            }
            if (isW(str.charAt(4))) {
                noneOf.add(PosixFilePermission.GROUP_WRITE);
            }
            if (isX(str.charAt(5))) {
                noneOf.add(PosixFilePermission.GROUP_EXECUTE);
            }
            if (isR(str.charAt(6))) {
                noneOf.add(PosixFilePermission.OTHERS_READ);
            }
            if (isW(str.charAt(7))) {
                noneOf.add(PosixFilePermission.OTHERS_WRITE);
            }
            if (isX(str.charAt(8))) {
                noneOf.add(PosixFilePermission.OTHERS_EXECUTE);
            }
            return noneOf;
        }
        throw new IllegalArgumentException("Invalid mode");
    }

    public static FileAttribute<Set<PosixFilePermission>> asFileAttribute(Set<PosixFilePermission> set) {
        final HashSet<PosixFilePermission> hashSet = new HashSet<>(set);
        for (PosixFilePermission posixFilePermission : hashSet) {
            posixFilePermission.getClass();
        }
        return new FileAttribute<Set<PosixFilePermission>>() {
            public String name() {
                return "posix:permissions";
            }

            public Set<PosixFilePermission> value() {
                return Collections.unmodifiableSet(Set.this);
            }
        };
    }
}
