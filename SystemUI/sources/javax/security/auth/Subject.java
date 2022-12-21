package javax.security.auth;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.DomainCombiner;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import sun.security.util.ResourcesMgr;

public final class Subject implements Serializable {
    private static final ProtectionDomain[] NULL_PD_ARRAY = new ProtectionDomain[0];
    private static final int PRINCIPAL_SET = 1;
    private static final int PRIV_CREDENTIAL_SET = 3;
    private static final int PUB_CREDENTIAL_SET = 2;
    private static final long serialVersionUID = -8308522755600156056L;
    Set<Principal> principals;
    transient Set<Object> privCredentials;
    transient Set<Object> pubCredentials;
    private volatile boolean readOnly;

    public Subject() {
        this.readOnly = false;
        this.principals = Collections.synchronizedSet(new SecureSet(this, 1));
        this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2));
        this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3));
    }

    public Subject(boolean z, Set<? extends Principal> set, Set<?> set2, Set<?> set3) {
        this.readOnly = false;
        if (set == null || set2 == null || set3 == null) {
            throw new NullPointerException(ResourcesMgr.getString("invalid.null.input.s."));
        }
        this.principals = Collections.synchronizedSet(new SecureSet(this, 1, set));
        this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2, set2));
        this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3, set3));
        this.readOnly = z;
    }

    public void setReadOnly() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.SET_READ_ONLY_PERMISSION);
        }
        this.readOnly = true;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public static Subject getSubject(final AccessControlContext accessControlContext) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.GET_SUBJECT_PERMISSION);
        }
        if (accessControlContext != null) {
            return (Subject) AccessController.doPrivileged(new PrivilegedAction<Subject>() {
                public Subject run() {
                    DomainCombiner domainCombiner = AccessControlContext.this.getDomainCombiner();
                    if (!(domainCombiner instanceof SubjectDomainCombiner)) {
                        return null;
                    }
                    return ((SubjectDomainCombiner) domainCombiner).getSubject();
                }
            });
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.AccessControlContext.provided"));
    }

    public static <T> T doAs(Subject subject, PrivilegedAction<T> privilegedAction) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PERMISSION);
        }
        if (privilegedAction != null) {
            return AccessController.doPrivileged(privilegedAction, createContext(subject, AccessController.getContext()));
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
    }

    public static <T> T doAs(Subject subject, PrivilegedExceptionAction<T> privilegedExceptionAction) throws PrivilegedActionException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PERMISSION);
        }
        if (privilegedExceptionAction != null) {
            return AccessController.doPrivileged(privilegedExceptionAction, createContext(subject, AccessController.getContext()));
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
    }

    public static <T> T doAsPrivileged(Subject subject, PrivilegedAction<T> privilegedAction, AccessControlContext accessControlContext) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PRIVILEGED_PERMISSION);
        }
        if (privilegedAction != null) {
            if (accessControlContext == null) {
                accessControlContext = new AccessControlContext(NULL_PD_ARRAY);
            }
            return AccessController.doPrivileged(privilegedAction, createContext(subject, accessControlContext));
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
    }

    public static <T> T doAsPrivileged(Subject subject, PrivilegedExceptionAction<T> privilegedExceptionAction, AccessControlContext accessControlContext) throws PrivilegedActionException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(AuthPermissionHolder.DO_AS_PRIVILEGED_PERMISSION);
        }
        if (privilegedExceptionAction != null) {
            if (accessControlContext == null) {
                accessControlContext = new AccessControlContext(NULL_PD_ARRAY);
            }
            return AccessController.doPrivileged(privilegedExceptionAction, createContext(subject, accessControlContext));
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.action.provided"));
    }

    private static AccessControlContext createContext(Subject subject, final AccessControlContext accessControlContext) {
        return (AccessControlContext) AccessController.doPrivileged(new PrivilegedAction<AccessControlContext>() {
            public AccessControlContext run() {
                Subject subject = Subject.this;
                if (subject == null) {
                    return new AccessControlContext(accessControlContext, (DomainCombiner) null);
                }
                return new AccessControlContext(accessControlContext, new SubjectDomainCombiner(subject));
            }
        });
    }

    public Set<Principal> getPrincipals() {
        return this.principals;
    }

    public <T extends Principal> Set<T> getPrincipals(Class<T> cls) {
        if (cls != null) {
            return new ClassSet(1, cls);
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
    }

    public Set<Object> getPublicCredentials() {
        return this.pubCredentials;
    }

    public Set<Object> getPrivateCredentials() {
        return this.privCredentials;
    }

    public <T> Set<T> getPublicCredentials(Class<T> cls) {
        if (cls != null) {
            return new ClassSet(2, cls);
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
    }

    public <T> Set<T> getPrivateCredentials(Class<T> cls) {
        if (cls != null) {
            return new ClassSet(3, cls);
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.Class.provided"));
    }

    public boolean equals(Object obj) {
        HashSet hashSet;
        HashSet hashSet2;
        HashSet hashSet3;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Subject)) {
            return false;
        }
        Subject subject = (Subject) obj;
        synchronized (subject.principals) {
            hashSet = new HashSet(subject.principals);
        }
        if (!this.principals.equals(hashSet)) {
            return false;
        }
        synchronized (subject.pubCredentials) {
            hashSet2 = new HashSet(subject.pubCredentials);
        }
        if (!this.pubCredentials.equals(hashSet2)) {
            return false;
        }
        synchronized (subject.privCredentials) {
            hashSet3 = new HashSet(subject.privCredentials);
        }
        return this.privCredentials.equals(hashSet3);
    }

    public String toString() {
        return toString(true);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:25|26) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        r1 = r1 + sun.security.util.ResourcesMgr.getString(".Private.Credential.inaccessible.");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x00b9 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString(boolean r7) {
        /*
            r6 = this;
            java.lang.String r0 = "Subject."
            java.lang.String r0 = sun.security.util.ResourcesMgr.getString(r0)
            java.lang.String r1 = ""
            java.util.Set<java.security.Principal> r2 = r6.principals
            monitor-enter(r2)
            java.util.Set<java.security.Principal> r3 = r6.principals     // Catch:{ all -> 0x00e7 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x00e7 }
        L_0x0011:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x00e7 }
            if (r4 == 0) goto L_0x0043
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x00e7 }
            java.security.Principal r4 = (java.security.Principal) r4     // Catch:{ all -> 0x00e7 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e7 }
            r5.<init>()     // Catch:{ all -> 0x00e7 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e7 }
            java.lang.String r1 = ".Principal."
            java.lang.String r1 = sun.security.util.ResourcesMgr.getString(r1)     // Catch:{ all -> 0x00e7 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e7 }
            java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x00e7 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e7 }
            java.lang.String r1 = "NEWLINE"
            java.lang.String r1 = sun.security.util.ResourcesMgr.getString(r1)     // Catch:{ all -> 0x00e7 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e7 }
            java.lang.String r1 = r5.toString()     // Catch:{ all -> 0x00e7 }
            goto L_0x0011
        L_0x0043:
            monitor-exit(r2)     // Catch:{ all -> 0x00e7 }
            java.util.Set<java.lang.Object> r3 = r6.pubCredentials
            monitor-enter(r3)
            java.util.Set<java.lang.Object> r2 = r6.pubCredentials     // Catch:{ all -> 0x00e4 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x00e4 }
        L_0x004d:
            boolean r4 = r2.hasNext()     // Catch:{ all -> 0x00e4 }
            if (r4 == 0) goto L_0x007d
            java.lang.Object r4 = r2.next()     // Catch:{ all -> 0x00e4 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e4 }
            r5.<init>()     // Catch:{ all -> 0x00e4 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e4 }
            java.lang.String r1 = ".Public.Credential."
            java.lang.String r1 = sun.security.util.ResourcesMgr.getString(r1)     // Catch:{ all -> 0x00e4 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e4 }
            java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x00e4 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e4 }
            java.lang.String r1 = "NEWLINE"
            java.lang.String r1 = sun.security.util.ResourcesMgr.getString(r1)     // Catch:{ all -> 0x00e4 }
            r5.append((java.lang.String) r1)     // Catch:{ all -> 0x00e4 }
            java.lang.String r1 = r5.toString()     // Catch:{ all -> 0x00e4 }
            goto L_0x004d
        L_0x007d:
            monitor-exit(r3)     // Catch:{ all -> 0x00e4 }
            if (r7 == 0) goto L_0x00d4
            java.util.Set<java.lang.Object> r7 = r6.privCredentials
            monitor-enter(r7)
            java.util.Set<java.lang.Object> r6 = r6.privCredentials     // Catch:{ all -> 0x00d1 }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ all -> 0x00d1 }
        L_0x0089:
            boolean r2 = r6.hasNext()     // Catch:{ all -> 0x00d1 }
            if (r2 == 0) goto L_0x00cf
            java.lang.Object r2 = r6.next()     // Catch:{ SecurityException -> 0x00b9 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ SecurityException -> 0x00b9 }
            r3.<init>()     // Catch:{ SecurityException -> 0x00b9 }
            r3.append((java.lang.String) r1)     // Catch:{ SecurityException -> 0x00b9 }
            java.lang.String r4 = ".Private.Credential."
            java.lang.String r4 = sun.security.util.ResourcesMgr.getString(r4)     // Catch:{ SecurityException -> 0x00b9 }
            r3.append((java.lang.String) r4)     // Catch:{ SecurityException -> 0x00b9 }
            java.lang.String r2 = r2.toString()     // Catch:{ SecurityException -> 0x00b9 }
            r3.append((java.lang.String) r2)     // Catch:{ SecurityException -> 0x00b9 }
            java.lang.String r2 = "NEWLINE"
            java.lang.String r2 = sun.security.util.ResourcesMgr.getString(r2)     // Catch:{ SecurityException -> 0x00b9 }
            r3.append((java.lang.String) r2)     // Catch:{ SecurityException -> 0x00b9 }
            java.lang.String r1 = r3.toString()     // Catch:{ SecurityException -> 0x00b9 }
            goto L_0x0089
        L_0x00b9:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d1 }
            r6.<init>()     // Catch:{ all -> 0x00d1 }
            r6.append((java.lang.String) r1)     // Catch:{ all -> 0x00d1 }
            java.lang.String r1 = ".Private.Credential.inaccessible."
            java.lang.String r1 = sun.security.util.ResourcesMgr.getString(r1)     // Catch:{ all -> 0x00d1 }
            r6.append((java.lang.String) r1)     // Catch:{ all -> 0x00d1 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00d1 }
            r1 = r6
        L_0x00cf:
            monitor-exit(r7)     // Catch:{ all -> 0x00d1 }
            goto L_0x00d4
        L_0x00d1:
            r6 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x00d1 }
            throw r6
        L_0x00d4:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append((java.lang.String) r0)
            r6.append((java.lang.String) r1)
            java.lang.String r6 = r6.toString()
            return r6
        L_0x00e4:
            r6 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x00e4 }
            throw r6
        L_0x00e7:
            r6 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00e7 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.security.auth.Subject.toString(boolean):java.lang.String");
    }

    public int hashCode() {
        int i;
        synchronized (this.principals) {
            i = 0;
            for (Principal hashCode : this.principals) {
                i ^= hashCode.hashCode();
            }
        }
        synchronized (this.pubCredentials) {
            for (Object credHashCode : this.pubCredentials) {
                i ^= getCredHashCode(credHashCode);
            }
        }
        return i;
    }

    private int getCredHashCode(Object obj) {
        try {
            return obj.hashCode();
        } catch (IllegalStateException unused) {
            return obj.getClass().toString().hashCode();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        synchronized (this.principals) {
            objectOutputStream.defaultWriteObject();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        this.readOnly = readFields.get("readOnly", false);
        Set set = (Set) readFields.get("principals", (Object) null);
        if (set != null) {
            try {
                this.principals = Collections.synchronizedSet(new SecureSet(this, 1, set));
            } catch (NullPointerException unused) {
                this.principals = Collections.synchronizedSet(new SecureSet(this, 1));
            }
            this.pubCredentials = Collections.synchronizedSet(new SecureSet(this, 2));
            this.privCredentials = Collections.synchronizedSet(new SecureSet(this, 3));
            return;
        }
        throw new NullPointerException(ResourcesMgr.getString("invalid.null.input.s."));
    }

    private static class SecureSet<E> extends AbstractSet<E> implements Serializable {
        private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("this$0", Subject.class), new ObjectStreamField("elements", LinkedList.class), new ObjectStreamField("which", Integer.TYPE)};
        private static final long serialVersionUID = 7911754171111800359L;
        LinkedList<E> elements;
        Subject subject;
        /* access modifiers changed from: private */
        public int which;

        SecureSet(Subject subject2, int i) {
            this.subject = subject2;
            this.which = i;
            this.elements = new LinkedList<>();
        }

        SecureSet(Subject subject2, int i, Set<? extends E> set) {
            this.subject = subject2;
            this.which = i;
            this.elements = new LinkedList<>(set);
        }

        public int size() {
            return this.elements.size();
        }

        public Iterator<E> iterator() {
            return new Iterator<E>(this.elements) {

                /* renamed from: i */
                ListIterator<E> f829i;
                final /* synthetic */ LinkedList val$list;

                {
                    this.val$list = r2;
                    this.f829i = r2.listIterator(0);
                }

                public boolean hasNext() {
                    return this.f829i.hasNext();
                }

                public E next() {
                    if (SecureSet.this.which != 3) {
                        return this.f829i.next();
                    }
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkPermission(new PrivateCredentialPermission(this.val$list.get(this.f829i.nextIndex()).getClass().getName(), SecureSet.this.subject.getPrincipals()));
                        } catch (SecurityException e) {
                            this.f829i.next();
                            throw e;
                        }
                    }
                    return this.f829i.next();
                }

                public void remove() {
                    if (!SecureSet.this.subject.isReadOnly()) {
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            int r1 = SecureSet.this.which;
                            if (r1 == 1) {
                                securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRINCIPALS_PERMISSION);
                            } else if (r1 != 2) {
                                securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRIVATE_CREDENTIALS_PERMISSION);
                            } else {
                                securityManager.checkPermission(AuthPermissionHolder.MODIFY_PUBLIC_CREDENTIALS_PERMISSION);
                            }
                        }
                        this.f829i.remove();
                        return;
                    }
                    throw new IllegalStateException(ResourcesMgr.getString("Subject.is.read.only"));
                }
            };
        }

        public boolean add(E e) {
            if (!this.subject.isReadOnly()) {
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    int i = this.which;
                    if (i == 1) {
                        securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRINCIPALS_PERMISSION);
                    } else if (i != 2) {
                        securityManager.checkPermission(AuthPermissionHolder.MODIFY_PRIVATE_CREDENTIALS_PERMISSION);
                    } else {
                        securityManager.checkPermission(AuthPermissionHolder.MODIFY_PUBLIC_CREDENTIALS_PERMISSION);
                    }
                }
                if (this.which == 1 && !(e instanceof Principal)) {
                    throw new SecurityException(ResourcesMgr.getString("attempting.to.add.an.object.which.is.not.an.instance.of.java.security.Principal.to.a.Subject.s.Principal.Set"));
                } else if (!this.elements.contains(e)) {
                    return this.elements.add(e);
                } else {
                    return false;
                }
            } else {
                throw new IllegalStateException(ResourcesMgr.getString("Subject.is.read.only"));
            }
        }

        public boolean remove(Object obj) {
            Object obj2;
            final Iterator it = iterator();
            while (it.hasNext()) {
                if (this.which != 3) {
                    obj2 = it.next();
                } else {
                    obj2 = AccessController.doPrivileged(new PrivilegedAction<E>() {
                        public E run() {
                            return it.next();
                        }
                    });
                }
                if (obj2 == null) {
                    if (obj == null) {
                        it.remove();
                        return true;
                    }
                } else if (obj2.equals(obj)) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        public boolean contains(Object obj) {
            Object obj2;
            final Iterator it = iterator();
            while (it.hasNext()) {
                if (this.which != 3) {
                    obj2 = it.next();
                } else {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkPermission(new PrivateCredentialPermission(obj.getClass().getName(), this.subject.getPrincipals()));
                    }
                    obj2 = AccessController.doPrivileged(new PrivilegedAction<E>() {
                        public E run() {
                            return it.next();
                        }
                    });
                }
                if (obj2 == null) {
                    if (obj == null) {
                        return true;
                    }
                } else if (obj2.equals(obj)) {
                    return true;
                }
            }
            return false;
        }

        public boolean removeAll(Collection<?> collection) {
            Object obj;
            Objects.requireNonNull(collection);
            final Iterator it = iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (this.which != 3) {
                    obj = it.next();
                } else {
                    obj = AccessController.doPrivileged(new PrivilegedAction<E>() {
                        public E run() {
                            return it.next();
                        }
                    });
                }
                Iterator<?> it2 = collection.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    Object next = it2.next();
                    if (obj == null) {
                        if (next == null) {
                            it.remove();
                            break;
                        }
                    } else if (obj.equals(next)) {
                        it.remove();
                        break;
                    }
                }
                z = true;
            }
            return z;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:0x003c, code lost:
            r3 = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean retainAll(java.util.Collection<?> r8) {
            /*
                r7 = this;
                java.util.Objects.requireNonNull(r8)
                java.util.Iterator r0 = r7.iterator()
                r1 = 0
                r2 = r1
            L_0x0009:
                boolean r3 = r0.hasNext()
                if (r3 == 0) goto L_0x0046
                int r3 = r7.which
                r4 = 3
                if (r3 == r4) goto L_0x0019
                java.lang.Object r3 = r0.next()
                goto L_0x0022
            L_0x0019:
                javax.security.auth.Subject$SecureSet$5 r3 = new javax.security.auth.Subject$SecureSet$5
                r3.<init>(r0)
                java.lang.Object r3 = java.security.AccessController.doPrivileged(r3)
            L_0x0022:
                java.util.Iterator r4 = r8.iterator()
            L_0x0026:
                boolean r5 = r4.hasNext()
                r6 = 1
                if (r5 == 0) goto L_0x003e
                java.lang.Object r5 = r4.next()
                if (r3 != 0) goto L_0x0036
                if (r5 != 0) goto L_0x0026
                goto L_0x003c
            L_0x0036:
                boolean r5 = r3.equals(r5)
                if (r5 == 0) goto L_0x0026
            L_0x003c:
                r3 = r6
                goto L_0x003f
            L_0x003e:
                r3 = r1
            L_0x003f:
                if (r3 != 0) goto L_0x0009
                r0.remove()
                r2 = r6
                goto L_0x0009
            L_0x0046:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.security.auth.Subject.SecureSet.retainAll(java.util.Collection):boolean");
        }

        public void clear() {
            final Iterator it = iterator();
            while (it.hasNext()) {
                if (this.which != 3) {
                    it.next();
                } else {
                    AccessController.doPrivileged(new PrivilegedAction<E>() {
                        public E run() {
                            return it.next();
                        }
                    });
                }
                it.remove();
            }
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            if (this.which == 3) {
                Iterator it = iterator();
                while (it.hasNext()) {
                    it.next();
                }
            }
            ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
            putFields.put("this$0", (Object) this.subject);
            putFields.put("elements", (Object) this.elements);
            putFields.put("which", this.which);
            objectOutputStream.writeFields();
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            ObjectInputStream.GetField readFields = objectInputStream.readFields();
            this.subject = (Subject) readFields.get("this$0", (Object) null);
            this.which = readFields.get("which", 0);
            LinkedList<E> linkedList = (LinkedList) readFields.get("elements", (Object) null);
            if (linkedList.getClass() != LinkedList.class) {
                this.elements = new LinkedList<>(linkedList);
            } else {
                this.elements = linkedList;
            }
        }
    }

    private class ClassSet<T> extends AbstractSet<T> {

        /* renamed from: c */
        private Class<T> f828c;
        private Set<T> set = new HashSet();
        private int which;

        ClassSet(int i, Class<T> cls) {
            this.which = i;
            this.f828c = cls;
            if (i == 1) {
                synchronized (Subject.this.principals) {
                    populateSet();
                }
            } else if (i != 2) {
                synchronized (Subject.this.privCredentials) {
                    populateSet();
                }
            } else {
                synchronized (Subject.this.pubCredentials) {
                    populateSet();
                }
            }
        }

        private void populateSet() {
            final Iterator<Object> it;
            Object obj;
            int i = this.which;
            if (i == 1) {
                it = Subject.this.principals.iterator();
            } else if (i != 2) {
                it = Subject.this.privCredentials.iterator();
            } else {
                it = Subject.this.pubCredentials.iterator();
            }
            while (it.hasNext()) {
                if (this.which == 3) {
                    obj = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                        public Object run() {
                            return it.next();
                        }
                    });
                } else {
                    obj = it.next();
                }
                if (this.f828c.isAssignableFrom(obj.getClass())) {
                    if (this.which != 3) {
                        this.set.add(obj);
                    } else {
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            securityManager.checkPermission(new PrivateCredentialPermission(obj.getClass().getName(), Subject.this.getPrincipals()));
                        }
                        this.set.add(obj);
                    }
                }
            }
        }

        public int size() {
            return this.set.size();
        }

        public Iterator<T> iterator() {
            return this.set.iterator();
        }

        public boolean add(T t) {
            if (t.getClass().isAssignableFrom(this.f828c)) {
                return this.set.add(t);
            }
            throw new SecurityException(new MessageFormat(ResourcesMgr.getString("attempting.to.add.an.object.which.is.not.an.instance.of.class")).format(new Object[]{this.f828c.toString()}));
        }
    }

    static class AuthPermissionHolder {
        static final AuthPermission DO_AS_PERMISSION = new AuthPermission("doAs");
        static final AuthPermission DO_AS_PRIVILEGED_PERMISSION = new AuthPermission("doAsPrivileged");
        static final AuthPermission GET_SUBJECT_PERMISSION = new AuthPermission("getSubject");
        static final AuthPermission MODIFY_PRINCIPALS_PERMISSION = new AuthPermission("modifyPrincipals");
        static final AuthPermission MODIFY_PRIVATE_CREDENTIALS_PERMISSION = new AuthPermission("modifyPrivateCredentials");
        static final AuthPermission MODIFY_PUBLIC_CREDENTIALS_PERMISSION = new AuthPermission("modifyPublicCredentials");
        static final AuthPermission SET_READ_ONLY_PERMISSION = new AuthPermission("setReadOnly");

        AuthPermissionHolder() {
        }
    }
}
