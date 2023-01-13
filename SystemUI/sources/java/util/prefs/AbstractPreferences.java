package java.util.prefs;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.TreeSet;

public abstract class AbstractPreferences extends Preferences {
    private static final AbstractPreferences[] EMPTY_ABSTRACT_PREFS_ARRAY = new AbstractPreferences[0];
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static Thread eventDispatchThread = null;
    /* access modifiers changed from: private */
    public static final List<EventObject> eventQueue = new LinkedList();
    private final String absolutePath;
    private Map<String, AbstractPreferences> kidCache = new HashMap();
    protected final Object lock = new Object();
    private final String name;
    protected boolean newNode = false;
    private final ArrayList<NodeChangeListener> nodeListeners = new ArrayList<>();
    final AbstractPreferences parent;
    private final ArrayList<PreferenceChangeListener> prefListeners = new ArrayList<>();
    private boolean removed = false;
    /* access modifiers changed from: private */
    public final AbstractPreferences root;

    /* access modifiers changed from: protected */
    public abstract AbstractPreferences childSpi(String str);

    /* access modifiers changed from: protected */
    public abstract String[] childrenNamesSpi() throws BackingStoreException;

    /* access modifiers changed from: protected */
    public abstract void flushSpi() throws BackingStoreException;

    /* access modifiers changed from: protected */
    public abstract String getSpi(String str);

    /* access modifiers changed from: protected */
    public abstract String[] keysSpi() throws BackingStoreException;

    /* access modifiers changed from: protected */
    public abstract void putSpi(String str, String str2);

    /* access modifiers changed from: protected */
    public abstract void removeNodeSpi() throws BackingStoreException;

    /* access modifiers changed from: protected */
    public abstract void removeSpi(String str);

    /* access modifiers changed from: protected */
    public abstract void syncSpi() throws BackingStoreException;

    protected AbstractPreferences(AbstractPreferences abstractPreferences, String str) {
        String str2;
        if (abstractPreferences == null) {
            if (str.equals("")) {
                this.absolutePath = "/";
                this.root = this;
            } else {
                throw new IllegalArgumentException("Root name '" + str + "' must be \"\"");
            }
        } else if (str.indexOf(47) != -1) {
            throw new IllegalArgumentException("Name '" + str + "' contains '/'");
        } else if (!str.equals("")) {
            AbstractPreferences abstractPreferences2 = abstractPreferences.root;
            this.root = abstractPreferences2;
            if (abstractPreferences == abstractPreferences2) {
                str2 = "/" + str;
            } else {
                str2 = abstractPreferences.absolutePath() + "/" + str;
            }
            this.absolutePath = str2;
        } else {
            throw new IllegalArgumentException("Illegal name: empty string");
        }
        this.name = str;
        this.parent = abstractPreferences;
    }

    public void put(String str, String str2) {
        if (str == null || str2 == null) {
            throw null;
        } else if (str.length() > 80) {
            throw new IllegalArgumentException("Key too long: " + str);
        } else if (str2.length() <= 8192) {
            synchronized (this.lock) {
                if (!this.removed) {
                    putSpi(str, str2);
                    enqueuePreferenceChangeEvent(str, str2);
                } else {
                    throw new IllegalStateException("Node has been removed.");
                }
            }
        } else {
            throw new IllegalArgumentException("Value too long: " + str2);
        }
    }

    public String get(String str, String str2) {
        String str3;
        if (str != null) {
            synchronized (this.lock) {
                if (!this.removed) {
                    try {
                        str3 = getSpi(str);
                    } catch (Exception unused) {
                        str3 = null;
                    }
                    if (str3 != null) {
                        str2 = str3;
                    }
                } else {
                    throw new IllegalStateException("Node has been removed.");
                }
            }
            return str2;
        }
        throw new NullPointerException("Null key");
    }

    public void remove(String str) {
        Objects.requireNonNull(str, "Specified key cannot be null");
        synchronized (this.lock) {
            if (!this.removed) {
                removeSpi(str);
                enqueuePreferenceChangeEvent(str, (String) null);
            } else {
                throw new IllegalStateException("Node has been removed.");
            }
        }
    }

    public void clear() throws BackingStoreException {
        synchronized (this.lock) {
            String[] keys = keys();
            for (String remove : keys) {
                remove(remove);
            }
        }
    }

    public void putInt(String str, int i) {
        put(str, Integer.toString(i));
    }

    public int getInt(String str, int i) {
        try {
            String str2 = get(str, (String) null);
            return str2 != null ? Integer.parseInt(str2) : i;
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public void putLong(String str, long j) {
        put(str, Long.toString(j));
    }

    public long getLong(String str, long j) {
        try {
            String str2 = get(str, (String) null);
            return str2 != null ? Long.parseLong(str2) : j;
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    public void putBoolean(String str, boolean z) {
        put(str, String.valueOf(z));
    }

    public boolean getBoolean(String str, boolean z) {
        String str2 = get(str, (String) null);
        if (str2 == null) {
            return z;
        }
        if (str2.equalsIgnoreCase("true")) {
            return true;
        }
        if (str2.equalsIgnoreCase("false")) {
            return false;
        }
        return z;
    }

    public void putFloat(String str, float f) {
        put(str, Float.toString(f));
    }

    public float getFloat(String str, float f) {
        try {
            String str2 = get(str, (String) null);
            return str2 != null ? Float.parseFloat(str2) : f;
        } catch (NumberFormatException unused) {
            return f;
        }
    }

    public void putDouble(String str, double d) {
        put(str, Double.toString(d));
    }

    public double getDouble(String str, double d) {
        try {
            String str2 = get(str, (String) null);
            return str2 != null ? Double.parseDouble(str2) : d;
        } catch (NumberFormatException unused) {
            return d;
        }
    }

    public void putByteArray(String str, byte[] bArr) {
        put(str, Base64.byteArrayToBase64(bArr));
    }

    public byte[] getByteArray(String str, byte[] bArr) {
        String str2 = get(str, (String) null);
        if (str2 == null) {
            return bArr;
        }
        try {
            return Base64.base64ToByteArray(str2);
        } catch (RuntimeException unused) {
            return bArr;
        }
    }

    public String[] keys() throws BackingStoreException {
        String[] keysSpi;
        synchronized (this.lock) {
            if (!this.removed) {
                keysSpi = keysSpi();
            } else {
                throw new IllegalStateException("Node has been removed.");
            }
        }
        return keysSpi;
    }

    public String[] childrenNames() throws BackingStoreException {
        String[] strArr;
        synchronized (this.lock) {
            if (!this.removed) {
                TreeSet treeSet = new TreeSet(this.kidCache.keySet());
                for (String add : childrenNamesSpi()) {
                    treeSet.add(add);
                }
                strArr = (String[]) treeSet.toArray(EMPTY_STRING_ARRAY);
            } else {
                throw new IllegalStateException("Node has been removed.");
            }
        }
        return strArr;
    }

    /* access modifiers changed from: protected */
    public final AbstractPreferences[] cachedChildren() {
        return (AbstractPreferences[]) this.kidCache.values().toArray((T[]) EMPTY_ABSTRACT_PREFS_ARRAY);
    }

    public Preferences parent() {
        AbstractPreferences abstractPreferences;
        synchronized (this.lock) {
            if (!this.removed) {
                abstractPreferences = this.parent;
            } else {
                throw new IllegalStateException("Node has been removed.");
            }
        }
        return abstractPreferences;
    }

    public Preferences node(String str) {
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            } else if (str.equals("")) {
                return this;
            } else {
                if (str.equals("/")) {
                    AbstractPreferences abstractPreferences = this.root;
                    return abstractPreferences;
                } else if (str.charAt(0) == '/') {
                    return this.root.node(new StringTokenizer(str.substring(1), "/", true));
                } else {
                    Preferences node = node(new StringTokenizer(str, "/", true));
                    return node;
                }
            }
        }
    }

    private Preferences node(StringTokenizer stringTokenizer) {
        String nextToken = stringTokenizer.nextToken();
        if (!nextToken.equals("/")) {
            synchronized (this.lock) {
                AbstractPreferences abstractPreferences = this.kidCache.get(nextToken);
                if (abstractPreferences == null) {
                    if (nextToken.length() <= 80) {
                        abstractPreferences = childSpi(nextToken);
                        if (abstractPreferences.newNode) {
                            enqueueNodeAddedEvent(abstractPreferences);
                        }
                        this.kidCache.put(nextToken, abstractPreferences);
                    } else {
                        throw new IllegalArgumentException("Node name " + nextToken + " too long");
                    }
                }
                if (!stringTokenizer.hasMoreTokens()) {
                    return abstractPreferences;
                }
                stringTokenizer.nextToken();
                if (stringTokenizer.hasMoreTokens()) {
                    Preferences node = abstractPreferences.node(stringTokenizer);
                    return node;
                }
                throw new IllegalArgumentException("Path ends with slash");
            }
        }
        throw new IllegalArgumentException("Consecutive slashes in path");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0013, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean nodeExists(java.lang.String r5) throws java.util.prefs.BackingStoreException {
        /*
            r4 = this;
            java.lang.Object r0 = r4.lock
            monitor-enter(r0)
            java.lang.String r1 = ""
            boolean r1 = r5.equals(r1)     // Catch:{ all -> 0x0052 }
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x0014
            boolean r4 = r4.removed     // Catch:{ all -> 0x0052 }
            if (r4 != 0) goto L_0x0012
            r2 = r3
        L_0x0012:
            monitor-exit(r0)     // Catch:{ all -> 0x0052 }
            return r2
        L_0x0014:
            boolean r1 = r4.removed     // Catch:{ all -> 0x0052 }
            if (r1 != 0) goto L_0x004a
            java.lang.String r1 = "/"
            boolean r1 = r5.equals(r1)     // Catch:{ all -> 0x0052 }
            if (r1 == 0) goto L_0x0022
            monitor-exit(r0)     // Catch:{ all -> 0x0052 }
            return r3
        L_0x0022:
            char r1 = r5.charAt(r2)     // Catch:{ all -> 0x0052 }
            r2 = 47
            if (r1 == r2) goto L_0x0037
            java.util.StringTokenizer r1 = new java.util.StringTokenizer     // Catch:{ all -> 0x0052 }
            java.lang.String r2 = "/"
            r1.<init>(r5, r2, r3)     // Catch:{ all -> 0x0052 }
            boolean r4 = r4.nodeExists((java.util.StringTokenizer) r1)     // Catch:{ all -> 0x0052 }
            monitor-exit(r0)     // Catch:{ all -> 0x0052 }
            return r4
        L_0x0037:
            monitor-exit(r0)     // Catch:{ all -> 0x0052 }
            java.util.prefs.AbstractPreferences r4 = r4.root
            java.util.StringTokenizer r0 = new java.util.StringTokenizer
            java.lang.String r5 = r5.substring(r3)
            java.lang.String r1 = "/"
            r0.<init>(r5, r1, r3)
            boolean r4 = r4.nodeExists((java.util.StringTokenizer) r0)
            return r4
        L_0x004a:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0052 }
            java.lang.String r5 = "Node has been removed."
            r4.<init>((java.lang.String) r5)     // Catch:{ all -> 0x0052 }
            throw r4     // Catch:{ all -> 0x0052 }
        L_0x0052:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0052 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.AbstractPreferences.nodeExists(java.lang.String):boolean");
    }

    private boolean nodeExists(StringTokenizer stringTokenizer) throws BackingStoreException {
        String nextToken = stringTokenizer.nextToken();
        if (!nextToken.equals("/")) {
            synchronized (this.lock) {
                AbstractPreferences abstractPreferences = this.kidCache.get(nextToken);
                if (abstractPreferences == null) {
                    abstractPreferences = getChild(nextToken);
                }
                if (abstractPreferences == null) {
                    return false;
                }
                if (!stringTokenizer.hasMoreTokens()) {
                    return true;
                }
                stringTokenizer.nextToken();
                if (stringTokenizer.hasMoreTokens()) {
                    boolean nodeExists = abstractPreferences.nodeExists(stringTokenizer);
                    return nodeExists;
                }
                throw new IllegalArgumentException("Path ends with slash");
            }
        }
        throw new IllegalArgumentException("Consecutive slashes in path");
    }

    public void removeNode() throws BackingStoreException {
        if (this != this.root) {
            synchronized (this.parent.lock) {
                removeNode2();
                this.parent.kidCache.remove(this.name);
            }
            return;
        }
        throw new UnsupportedOperationException("Can't remove the root!");
    }

    private void removeNode2() throws BackingStoreException {
        synchronized (this.lock) {
            if (!this.removed) {
                String[] childrenNamesSpi = childrenNamesSpi();
                for (int i = 0; i < childrenNamesSpi.length; i++) {
                    if (!this.kidCache.containsKey(childrenNamesSpi[i])) {
                        Map<String, AbstractPreferences> map = this.kidCache;
                        String str = childrenNamesSpi[i];
                        map.put(str, childSpi(str));
                    }
                }
                Iterator<AbstractPreferences> it = this.kidCache.values().iterator();
                while (it.hasNext()) {
                    try {
                        it.next().removeNode2();
                        it.remove();
                    } catch (BackingStoreException unused) {
                    }
                }
                removeNodeSpi();
                this.removed = true;
                this.parent.enqueueNodeRemovedEvent(this);
            } else {
                throw new IllegalStateException("Node already removed.");
            }
        }
    }

    public String name() {
        return this.name;
    }

    public String absolutePath() {
        return this.absolutePath;
    }

    public boolean isUserNode() {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                return Boolean.valueOf(AbstractPreferences.this.root == Preferences.userRoot());
            }
        })).booleanValue();
    }

    public void addPreferenceChangeListener(PreferenceChangeListener preferenceChangeListener) {
        if (preferenceChangeListener != null) {
            synchronized (this.lock) {
                if (!this.removed) {
                    this.prefListeners.add(preferenceChangeListener);
                } else {
                    throw new IllegalStateException("Node has been removed.");
                }
            }
            startEventDispatchThreadIfNecessary();
            return;
        }
        throw new NullPointerException("Change listener is null.");
    }

    public void removePreferenceChangeListener(PreferenceChangeListener preferenceChangeListener) {
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            } else if (this.prefListeners.contains(preferenceChangeListener)) {
                this.prefListeners.remove((Object) preferenceChangeListener);
            } else {
                throw new IllegalArgumentException("Listener not registered.");
            }
        }
    }

    public void addNodeChangeListener(NodeChangeListener nodeChangeListener) {
        if (nodeChangeListener != null) {
            synchronized (this.lock) {
                if (!this.removed) {
                    this.nodeListeners.add(nodeChangeListener);
                } else {
                    throw new IllegalStateException("Node has been removed.");
                }
            }
            startEventDispatchThreadIfNecessary();
            return;
        }
        throw new NullPointerException("Change listener is null.");
    }

    public void removeNodeChangeListener(NodeChangeListener nodeChangeListener) {
        synchronized (this.lock) {
            if (this.removed) {
                throw new IllegalStateException("Node has been removed.");
            } else if (this.nodeListeners.contains(nodeChangeListener)) {
                this.nodeListeners.remove((Object) nodeChangeListener);
            } else {
                throw new IllegalArgumentException("Listener not registered.");
            }
        }
    }

    /* access modifiers changed from: protected */
    public AbstractPreferences getChild(String str) throws BackingStoreException {
        synchronized (this.lock) {
            String[] childrenNames = childrenNames();
            for (int i = 0; i < childrenNames.length; i++) {
                if (childrenNames[i].equals(str)) {
                    AbstractPreferences childSpi = childSpi(childrenNames[i]);
                    return childSpi;
                }
            }
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isUserNode() ? "User" : "System");
        sb.append(" Preference Node: ");
        sb.append(absolutePath());
        return sb.toString();
    }

    public void sync() throws BackingStoreException {
        sync2();
    }

    private void sync2() throws BackingStoreException {
        AbstractPreferences[] cachedChildren;
        synchronized (this.lock) {
            if (!this.removed) {
                syncSpi();
                cachedChildren = cachedChildren();
            } else {
                throw new IllegalStateException("Node has been removed");
            }
        }
        for (AbstractPreferences sync2 : cachedChildren) {
            sync2.sync2();
        }
    }

    public void flush() throws BackingStoreException {
        flush2();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0013, code lost:
        if (r0 >= r2.length) goto L_0x001d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0015, code lost:
        r2[r0].flush2();
        r0 = r0 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0011, code lost:
        r0 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void flush2() throws java.util.prefs.BackingStoreException {
        /*
            r2 = this;
            java.lang.Object r0 = r2.lock
            monitor-enter(r0)
            r2.flushSpi()     // Catch:{ all -> 0x001e }
            boolean r1 = r2.removed     // Catch:{ all -> 0x001e }
            if (r1 == 0) goto L_0x000c
            monitor-exit(r0)     // Catch:{ all -> 0x001e }
            return
        L_0x000c:
            java.util.prefs.AbstractPreferences[] r2 = r2.cachedChildren()     // Catch:{ all -> 0x001e }
            monitor-exit(r0)     // Catch:{ all -> 0x001e }
            r0 = 0
        L_0x0012:
            int r1 = r2.length
            if (r0 >= r1) goto L_0x001d
            r1 = r2[r0]
            r1.flush2()
            int r0 = r0 + 1
            goto L_0x0012
        L_0x001d:
            return
        L_0x001e:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001e }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.AbstractPreferences.flush2():void");
    }

    /* access modifiers changed from: protected */
    public boolean isRemoved() {
        boolean z;
        synchronized (this.lock) {
            z = this.removed;
        }
        return z;
    }

    private class NodeAddedEvent extends NodeChangeEvent {
        private static final long serialVersionUID = -6743557530157328528L;

        NodeAddedEvent(Preferences preferences, Preferences preferences2) {
            super(preferences, preferences2);
        }
    }

    private class NodeRemovedEvent extends NodeChangeEvent {
        private static final long serialVersionUID = 8735497392918824837L;

        NodeRemovedEvent(Preferences preferences, Preferences preferences2) {
            super(preferences, preferences2);
        }
    }

    private static class EventDispatchThread extends Thread {
        private EventDispatchThread() {
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(3:24|25|26) */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x002b, code lost:
            if ((r0 instanceof java.util.prefs.PreferenceChangeEvent) == false) goto L_0x003e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
            r0 = (java.util.prefs.PreferenceChangeEvent) r0;
            r3 = r3.prefListeners();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0034, code lost:
            if (r1 >= r3.length) goto L_0x0000;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0036, code lost:
            r3[r1].preferenceChange(r0);
            r1 = r1 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x003e, code lost:
            r0 = (java.util.prefs.NodeChangeEvent) r0;
            r3 = r3.nodeListeners();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0046, code lost:
            if ((r0 instanceof java.util.prefs.AbstractPreferences.NodeAddedEvent) == false) goto L_0x0053;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0049, code lost:
            if (r1 >= r3.length) goto L_0x0000;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
            r3[r1].childAdded(r0);
            r1 = r1 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0054, code lost:
            if (r1 >= r3.length) goto L_0x0000;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
            r3[r1].childRemoved(r0);
            r1 = r1 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0061, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0017, code lost:
            r1 = 0;
            r0 = (java.util.EventObject) java.util.prefs.AbstractPreferences.m3856$$Nest$sfgeteventQueue().remove(0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
            r3 = (java.util.prefs.AbstractPreferences) r0.getSource();
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0060 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r3 = this;
            L_0x0000:
                java.util.List r3 = java.util.prefs.AbstractPreferences.eventQueue
                monitor-enter(r3)
            L_0x0005:
                java.util.List r0 = java.util.prefs.AbstractPreferences.eventQueue     // Catch:{ InterruptedException -> 0x0060 }
                boolean r0 = r0.isEmpty()     // Catch:{ InterruptedException -> 0x0060 }
                if (r0 == 0) goto L_0x0017
                java.util.List r0 = java.util.prefs.AbstractPreferences.eventQueue     // Catch:{ InterruptedException -> 0x0060 }
                r0.wait()     // Catch:{ InterruptedException -> 0x0060 }
                goto L_0x0005
            L_0x0017:
                java.util.List r0 = java.util.prefs.AbstractPreferences.eventQueue     // Catch:{ InterruptedException -> 0x0060 }
                r1 = 0
                java.lang.Object r0 = r0.remove((int) r1)     // Catch:{ InterruptedException -> 0x0060 }
                java.util.EventObject r0 = (java.util.EventObject) r0     // Catch:{ InterruptedException -> 0x0060 }
                monitor-exit(r3)     // Catch:{ all -> 0x005e }
                java.lang.Object r3 = r0.getSource()
                java.util.prefs.AbstractPreferences r3 = (java.util.prefs.AbstractPreferences) r3
                boolean r2 = r0 instanceof java.util.prefs.PreferenceChangeEvent
                if (r2 == 0) goto L_0x003e
                java.util.prefs.PreferenceChangeEvent r0 = (java.util.prefs.PreferenceChangeEvent) r0
                java.util.prefs.PreferenceChangeListener[] r3 = r3.prefListeners()
            L_0x0033:
                int r2 = r3.length
                if (r1 >= r2) goto L_0x0000
                r2 = r3[r1]
                r2.preferenceChange(r0)
                int r1 = r1 + 1
                goto L_0x0033
            L_0x003e:
                java.util.prefs.NodeChangeEvent r0 = (java.util.prefs.NodeChangeEvent) r0
                java.util.prefs.NodeChangeListener[] r3 = r3.nodeListeners()
                boolean r2 = r0 instanceof java.util.prefs.AbstractPreferences.NodeAddedEvent
                if (r2 == 0) goto L_0x0053
            L_0x0048:
                int r2 = r3.length
                if (r1 >= r2) goto L_0x0000
                r2 = r3[r1]
                r2.childAdded(r0)
                int r1 = r1 + 1
                goto L_0x0048
            L_0x0053:
                int r2 = r3.length
                if (r1 >= r2) goto L_0x0000
                r2 = r3[r1]
                r2.childRemoved(r0)
                int r1 = r1 + 1
                goto L_0x0053
            L_0x005e:
                r0 = move-exception
                goto L_0x0062
            L_0x0060:
                monitor-exit(r3)     // Catch:{ all -> 0x005e }
                return
            L_0x0062:
                monitor-exit(r3)     // Catch:{ all -> 0x005e }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.AbstractPreferences.EventDispatchThread.run():void");
        }
    }

    private static synchronized void startEventDispatchThreadIfNecessary() {
        synchronized (AbstractPreferences.class) {
            if (eventDispatchThread == null) {
                EventDispatchThread eventDispatchThread2 = new EventDispatchThread();
                eventDispatchThread = eventDispatchThread2;
                eventDispatchThread2.setDaemon(true);
                eventDispatchThread.start();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public PreferenceChangeListener[] prefListeners() {
        PreferenceChangeListener[] preferenceChangeListenerArr;
        synchronized (this.lock) {
            ArrayList<PreferenceChangeListener> arrayList = this.prefListeners;
            preferenceChangeListenerArr = (PreferenceChangeListener[]) arrayList.toArray(new PreferenceChangeListener[arrayList.size()]);
        }
        return preferenceChangeListenerArr;
    }

    /* access modifiers changed from: package-private */
    public NodeChangeListener[] nodeListeners() {
        NodeChangeListener[] nodeChangeListenerArr;
        synchronized (this.lock) {
            ArrayList<NodeChangeListener> arrayList = this.nodeListeners;
            nodeChangeListenerArr = (NodeChangeListener[]) arrayList.toArray(new NodeChangeListener[arrayList.size()]);
        }
        return nodeChangeListenerArr;
    }

    private void enqueuePreferenceChangeEvent(String str, String str2) {
        if (!this.prefListeners.isEmpty()) {
            List<EventObject> list = eventQueue;
            synchronized (list) {
                list.add(new PreferenceChangeEvent(this, str, str2));
                list.notify();
            }
        }
    }

    private void enqueueNodeAddedEvent(Preferences preferences) {
        if (!this.nodeListeners.isEmpty()) {
            List<EventObject> list = eventQueue;
            synchronized (list) {
                list.add(new NodeAddedEvent(this, preferences));
                list.notify();
            }
        }
    }

    private void enqueueNodeRemovedEvent(Preferences preferences) {
        if (!this.nodeListeners.isEmpty()) {
            List<EventObject> list = eventQueue;
            synchronized (list) {
                list.add(new NodeRemovedEvent(this, preferences));
                list.notify();
            }
        }
    }

    public void exportNode(OutputStream outputStream) throws IOException, BackingStoreException {
        XmlSupport.export(outputStream, this, false);
    }

    public void exportSubtree(OutputStream outputStream) throws IOException, BackingStoreException {
        XmlSupport.export(outputStream, this, true);
    }
}
