package java.nio.file.attribute;

import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class AclEntry {
    private final Set<AclEntryFlag> flags;
    private volatile int hash;
    private final Set<AclEntryPermission> perms;
    private final AclEntryType type;
    private final UserPrincipal who;

    private AclEntry(AclEntryType aclEntryType, UserPrincipal userPrincipal, Set<AclEntryPermission> set, Set<AclEntryFlag> set2) {
        this.type = aclEntryType;
        this.who = userPrincipal;
        this.perms = set;
        this.flags = set2;
    }

    public static final class Builder {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private Set<AclEntryFlag> flags;
        private Set<AclEntryPermission> perms;
        private AclEntryType type;
        private UserPrincipal who;

        static {
            Class<AclEntry> cls = AclEntry.class;
        }

        private Builder(AclEntryType aclEntryType, UserPrincipal userPrincipal, Set<AclEntryPermission> set, Set<AclEntryFlag> set2) {
            this.type = aclEntryType;
            this.who = userPrincipal;
            this.perms = set;
            this.flags = set2;
        }

        public AclEntry build() {
            AclEntryType aclEntryType = this.type;
            if (aclEntryType != null) {
                UserPrincipal userPrincipal = this.who;
                if (userPrincipal != null) {
                    return new AclEntry(aclEntryType, userPrincipal, this.perms, this.flags);
                }
                throw new IllegalStateException("Missing who component");
            }
            throw new IllegalStateException("Missing type component");
        }

        public Builder setType(AclEntryType aclEntryType) {
            aclEntryType.getClass();
            this.type = aclEntryType;
            return this;
        }

        public Builder setPrincipal(UserPrincipal userPrincipal) {
            userPrincipal.getClass();
            this.who = userPrincipal;
            return this;
        }

        private static void checkSet(Set<?> set, Class<?> cls) {
            for (Object next : set) {
                next.getClass();
                cls.cast(next);
            }
        }

        /* JADX WARNING: type inference failed for: r2v0, types: [java.util.Collection, java.util.Set<java.nio.file.attribute.AclEntryPermission>, java.util.Set] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.nio.file.attribute.AclEntry.Builder setPermissions(java.util.Set<java.nio.file.attribute.AclEntryPermission> r2) {
            /*
                r1 = this;
                boolean r0 = r2.isEmpty()
                if (r0 == 0) goto L_0x000b
                java.util.Set r2 = java.util.Collections.emptySet()
                goto L_0x0014
            L_0x000b:
                java.util.EnumSet r2 = java.util.EnumSet.copyOf(r2)
                java.lang.Class<java.nio.file.attribute.AclEntryPermission> r0 = java.nio.file.attribute.AclEntryPermission.class
                checkSet(r2, r0)
            L_0x0014:
                r1.perms = r2
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.nio.file.attribute.AclEntry.Builder.setPermissions(java.util.Set):java.nio.file.attribute.AclEntry$Builder");
        }

        public Builder setPermissions(AclEntryPermission... aclEntryPermissionArr) {
            EnumSet<E> noneOf = EnumSet.noneOf(AclEntryPermission.class);
            for (AclEntryPermission aclEntryPermission : aclEntryPermissionArr) {
                aclEntryPermission.getClass();
                noneOf.add(aclEntryPermission);
            }
            this.perms = noneOf;
            return this;
        }

        /* JADX WARNING: type inference failed for: r2v0, types: [java.util.Set<java.nio.file.attribute.AclEntryFlag>, java.util.Collection, java.util.Set] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.nio.file.attribute.AclEntry.Builder setFlags(java.util.Set<java.nio.file.attribute.AclEntryFlag> r2) {
            /*
                r1 = this;
                boolean r0 = r2.isEmpty()
                if (r0 == 0) goto L_0x000b
                java.util.Set r2 = java.util.Collections.emptySet()
                goto L_0x0014
            L_0x000b:
                java.util.EnumSet r2 = java.util.EnumSet.copyOf(r2)
                java.lang.Class<java.nio.file.attribute.AclEntryFlag> r0 = java.nio.file.attribute.AclEntryFlag.class
                checkSet(r2, r0)
            L_0x0014:
                r1.flags = r2
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.nio.file.attribute.AclEntry.Builder.setFlags(java.util.Set):java.nio.file.attribute.AclEntry$Builder");
        }

        public Builder setFlags(AclEntryFlag... aclEntryFlagArr) {
            EnumSet<E> noneOf = EnumSet.noneOf(AclEntryFlag.class);
            for (AclEntryFlag aclEntryFlag : aclEntryFlagArr) {
                aclEntryFlag.getClass();
                noneOf.add(aclEntryFlag);
            }
            this.flags = noneOf;
            return this;
        }
    }

    public static Builder newBuilder() {
        return new Builder((AclEntryType) null, (UserPrincipal) null, Collections.emptySet(), Collections.emptySet());
    }

    public static Builder newBuilder(AclEntry aclEntry) {
        return new Builder(aclEntry.type, aclEntry.who, aclEntry.perms, aclEntry.flags);
    }

    public AclEntryType type() {
        return this.type;
    }

    public UserPrincipal principal() {
        return this.who;
    }

    public Set<AclEntryPermission> permissions() {
        return new HashSet(this.perms);
    }

    public Set<AclEntryFlag> flags() {
        return new HashSet(this.flags);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AclEntry)) {
            return false;
        }
        AclEntry aclEntry = (AclEntry) obj;
        return this.type == aclEntry.type && this.who.equals(aclEntry.who) && this.perms.equals(aclEntry.perms) && this.flags.equals(aclEntry.flags);
    }

    private static int hash(int i, Object obj) {
        return (i * 127) + obj.hashCode();
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = hash(hash(hash(this.type.hashCode(), this.who), this.perms), this.flags);
        return this.hash;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.who.getName());
        sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        for (AclEntryPermission name : this.perms) {
            sb.append(name.name());
            sb.append('/');
        }
        sb.setLength(sb.length() - 1);
        sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        if (!this.flags.isEmpty()) {
            for (AclEntryFlag name2 : this.flags) {
                sb.append(name2.name());
                sb.append('/');
            }
            sb.setLength(sb.length() - 1);
            sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        }
        sb.append(this.type.name());
        return sb.toString();
    }
}
