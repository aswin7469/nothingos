package java.security;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;

final class UnresolvedPermissionCollection extends PermissionCollection implements Serializable {
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("permissions", Hashtable.class)};
    private static final long serialVersionUID = -7176153071733132400L;
    private transient ConcurrentHashMap<String, List<UnresolvedPermission>> perms = new ConcurrentHashMap<>(11);

    public boolean implies(Permission permission) {
        return false;
    }

    public void add(Permission permission) {
        if (permission instanceof UnresolvedPermission) {
            final UnresolvedPermission unresolvedPermission = (UnresolvedPermission) permission;
            this.perms.compute(unresolvedPermission.getName(), new BiFunction<String, List<UnresolvedPermission>, List<UnresolvedPermission>>() {
                public List<UnresolvedPermission> apply(String str, List<UnresolvedPermission> list) {
                    if (list == null) {
                        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
                        copyOnWriteArrayList.add(unresolvedPermission);
                        return copyOnWriteArrayList;
                    }
                    list.add(unresolvedPermission);
                    return list;
                }
            });
            return;
        }
        throw new IllegalArgumentException("invalid permission: " + permission);
    }

    /* access modifiers changed from: package-private */
    public List<UnresolvedPermission> getUnresolvedPermissions(Permission permission) {
        return this.perms.get(permission.getClass().getName());
    }

    public Enumeration<Permission> elements() {
        ArrayList arrayList = new ArrayList();
        for (List<UnresolvedPermission> addAll : this.perms.values()) {
            arrayList.addAll(addAll);
        }
        return Collections.enumeration(arrayList);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable = new Hashtable(this.perms.size() * 2);
        for (Map.Entry next : this.perms.entrySet()) {
            hashtable.put((String) next.getKey(), new Vector((List) next.getValue()));
        }
        objectOutputStream.putFields().put("permissions", (Object) hashtable);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Hashtable hashtable = (Hashtable) objectInputStream.readFields().get("permissions", (Object) null);
        this.perms = new ConcurrentHashMap<>(hashtable.size() * 2);
        for (Map.Entry entry : hashtable.entrySet()) {
            this.perms.put((String) entry.getKey(), new CopyOnWriteArrayList((Vector) entry.getValue()));
        }
    }
}
