package sun.nio.p035fs;

import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.p026io.IOException;

/* renamed from: sun.nio.fs.UnixUserPrincipals */
class UnixUserPrincipals {
    static final User SPECIAL_EVERYONE = createSpecial("EVERYONE@");
    static final User SPECIAL_GROUP = createSpecial("GROUP@");
    static final User SPECIAL_OWNER = createSpecial("OWNER@");

    UnixUserPrincipals() {
    }

    private static User createSpecial(String str) {
        return new User(-1, str);
    }

    /* renamed from: sun.nio.fs.UnixUserPrincipals$User */
    static class User implements UserPrincipal {

        /* renamed from: id */
        private final int f913id;
        private final boolean isGroup;
        private final String name;

        private User(int i, boolean z, String str) {
            this.f913id = i;
            this.isGroup = z;
            this.name = str;
        }

        User(int i, String str) {
            this(i, false, str);
        }

        /* access modifiers changed from: package-private */
        public int uid() {
            if (!this.isGroup) {
                return this.f913id;
            }
            throw new AssertionError();
        }

        /* access modifiers changed from: package-private */
        public int gid() {
            if (this.isGroup) {
                return this.f913id;
            }
            throw new AssertionError();
        }

        /* access modifiers changed from: package-private */
        public boolean isSpecial() {
            return this.f913id == -1;
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:6:0x000a, code lost:
            r7 = (sun.nio.p035fs.UnixUserPrincipals.User) r7;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r7) {
            /*
                r6 = this;
                r0 = 1
                if (r7 != r6) goto L_0x0004
                return r0
            L_0x0004:
                boolean r1 = r7 instanceof sun.nio.p035fs.UnixUserPrincipals.User
                r2 = 0
                if (r1 != 0) goto L_0x000a
                return r2
            L_0x000a:
                sun.nio.fs.UnixUserPrincipals$User r7 = (sun.nio.p035fs.UnixUserPrincipals.User) r7
                int r1 = r6.f913id
                int r3 = r7.f913id
                if (r1 != r3) goto L_0x0028
                boolean r4 = r6.isGroup
                boolean r5 = r7.isGroup
                if (r4 == r5) goto L_0x0019
                goto L_0x0028
            L_0x0019:
                r2 = -1
                if (r1 != r2) goto L_0x0027
                if (r3 != r2) goto L_0x0027
                java.lang.String r6 = r6.name
                java.lang.String r7 = r7.name
                boolean r6 = r6.equals(r7)
                return r6
            L_0x0027:
                return r0
            L_0x0028:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixUserPrincipals.User.equals(java.lang.Object):boolean");
        }

        public int hashCode() {
            int i = this.f913id;
            return i != -1 ? i : this.name.hashCode();
        }
    }

    /* renamed from: sun.nio.fs.UnixUserPrincipals$Group */
    static class Group extends User implements GroupPrincipal {
        Group(int i, String str) {
            super(i, true, str);
        }
    }

    static User fromUid(int i) {
        String str;
        try {
            str = Util.toString(UnixNativeDispatcher.getpwuid(i));
        } catch (UnixException unused) {
            str = Integer.toString(i);
        }
        return new User(i, str);
    }

    static Group fromGid(int i) {
        String str;
        try {
            str = Util.toString(UnixNativeDispatcher.getgrgid(i));
        } catch (UnixException unused) {
            str = Integer.toString(i);
        }
        return new Group(i, str);
    }

    private static int lookupName(String str, boolean z) throws IOException {
        int i;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("lookupUserInformation"));
        }
        if (z) {
            try {
                i = UnixNativeDispatcher.getgrnam(str);
            } catch (UnixException e) {
                throw new IOException(str + ": " + e.errorString());
            }
        } else {
            i = UnixNativeDispatcher.getpwnam(str);
        }
        if (i != -1) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            throw new UserPrincipalNotFoundException(str);
        }
    }

    static UserPrincipal lookupUser(String str) throws IOException {
        User user = SPECIAL_OWNER;
        if (str.equals(user.getName())) {
            return user;
        }
        User user2 = SPECIAL_GROUP;
        if (str.equals(user2.getName())) {
            return user2;
        }
        User user3 = SPECIAL_EVERYONE;
        if (str.equals(user3.getName())) {
            return user3;
        }
        return new User(lookupName(str, false), str);
    }

    static GroupPrincipal lookupGroup(String str) throws IOException {
        return new Group(lookupName(str, true), str);
    }
}
