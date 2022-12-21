package java.beans;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class PropertyChangeSupport implements Serializable {
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("children", Hashtable.class), new ObjectStreamField("source", Object.class), new ObjectStreamField("propertyChangeSupportSerializedDataVersion", Integer.TYPE)};
    static final long serialVersionUID = 6401253773779951803L;
    private PropertyChangeListenerMap map = new PropertyChangeListenerMap();
    private Object source;

    public PropertyChangeSupport(Object obj) {
        obj.getClass();
        this.source = obj;
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            if (propertyChangeListener instanceof PropertyChangeListenerProxy) {
                PropertyChangeListenerProxy propertyChangeListenerProxy = (PropertyChangeListenerProxy) propertyChangeListener;
                addPropertyChangeListener(propertyChangeListenerProxy.getPropertyName(), (PropertyChangeListener) propertyChangeListenerProxy.getListener());
                return;
            }
            this.map.add((String) null, propertyChangeListener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener != null) {
            if (propertyChangeListener instanceof PropertyChangeListenerProxy) {
                PropertyChangeListenerProxy propertyChangeListenerProxy = (PropertyChangeListenerProxy) propertyChangeListener;
                removePropertyChangeListener(propertyChangeListenerProxy.getPropertyName(), (PropertyChangeListener) propertyChangeListenerProxy.getListener());
                return;
            }
            this.map.remove((String) null, propertyChangeListener);
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return (PropertyChangeListener[]) this.map.getListeners();
    }

    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        PropertyChangeListener extract;
        if (propertyChangeListener != null && str != null && (extract = this.map.extract(propertyChangeListener)) != null) {
            this.map.add(str, extract);
        }
    }

    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        PropertyChangeListener extract;
        if (propertyChangeListener != null && str != null && (extract = this.map.extract(propertyChangeListener)) != null) {
            this.map.remove(str, extract);
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String str) {
        return (PropertyChangeListener[]) this.map.getListeners(str);
    }

    public void firePropertyChange(String str, Object obj, Object obj2) {
        if (obj == null || obj2 == null || !obj.equals(obj2)) {
            firePropertyChange(new PropertyChangeEvent(this.source, str, obj, obj2));
        }
    }

    public void firePropertyChange(String str, int i, int i2) {
        if (i != i2) {
            firePropertyChange(str, (Object) Integer.valueOf(i), (Object) Integer.valueOf(i2));
        }
    }

    public void firePropertyChange(String str, boolean z, boolean z2) {
        if (z != z2) {
            firePropertyChange(str, (Object) Boolean.valueOf(z), (Object) Boolean.valueOf(z2));
        }
    }

    /* JADX WARNING: type inference failed for: r3v2, types: [java.util.EventListener[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void firePropertyChange(java.beans.PropertyChangeEvent r4) {
        /*
            r3 = this;
            java.lang.Object r0 = r4.getOldValue()
            java.lang.Object r1 = r4.getNewValue()
            if (r0 == 0) goto L_0x0012
            if (r1 == 0) goto L_0x0012
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x0030
        L_0x0012:
            java.lang.String r0 = r4.getPropertyName()
            java.beans.PropertyChangeSupport$PropertyChangeListenerMap r1 = r3.map
            r2 = 0
            java.util.EventListener[] r1 = r1.get(r2)
            java.beans.PropertyChangeListener[] r1 = (java.beans.PropertyChangeListener[]) r1
            if (r0 == 0) goto L_0x002a
            java.beans.PropertyChangeSupport$PropertyChangeListenerMap r3 = r3.map
            java.util.EventListener[] r3 = r3.get(r0)
            r2 = r3
            java.beans.PropertyChangeListener[] r2 = (java.beans.PropertyChangeListener[]) r2
        L_0x002a:
            fire(r1, r4)
            fire(r2, r4)
        L_0x0030:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.beans.PropertyChangeSupport.firePropertyChange(java.beans.PropertyChangeEvent):void");
    }

    private static void fire(PropertyChangeListener[] propertyChangeListenerArr, PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeListenerArr != null) {
            for (PropertyChangeListener propertyChange : propertyChangeListenerArr) {
                propertyChange.propertyChange(propertyChangeEvent);
            }
        }
    }

    public void fireIndexedPropertyChange(String str, int i, Object obj, Object obj2) {
        if (obj == null || obj2 == null || !obj.equals(obj2)) {
            firePropertyChange(new IndexedPropertyChangeEvent(this.source, str, obj, obj2, i));
        }
    }

    public void fireIndexedPropertyChange(String str, int i, int i2, int i3) {
        if (i2 != i3) {
            fireIndexedPropertyChange(str, i, (Object) Integer.valueOf(i2), (Object) Integer.valueOf(i3));
        }
    }

    public void fireIndexedPropertyChange(String str, int i, boolean z, boolean z2) {
        if (z != z2) {
            fireIndexedPropertyChange(str, i, (Object) Boolean.valueOf(z), (Object) Boolean.valueOf(z2));
        }
    }

    public boolean hasListeners(String str) {
        return this.map.hasListeners(str);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable;
        PropertyChangeListener[] propertyChangeListenerArr;
        synchronized (this.map) {
            hashtable = null;
            propertyChangeListenerArr = null;
            for (Map.Entry next : this.map.getEntries()) {
                String str = (String) next.getKey();
                if (str == null) {
                    propertyChangeListenerArr = (PropertyChangeListener[]) next.getValue();
                } else {
                    if (hashtable == null) {
                        hashtable = new Hashtable();
                    }
                    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this.source);
                    propertyChangeSupport.map.set((String) null, (PropertyChangeListener[]) next.getValue());
                    hashtable.put(str, propertyChangeSupport);
                }
            }
        }
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("children", (Object) hashtable);
        putFields.put("source", this.source);
        putFields.put("propertyChangeSupportSerializedDataVersion", 2);
        objectOutputStream.writeFields();
        if (propertyChangeListenerArr != null) {
            for (PropertyChangeListener propertyChangeListener : propertyChangeListenerArr) {
                if (propertyChangeListener instanceof Serializable) {
                    objectOutputStream.writeObject(propertyChangeListener);
                }
            }
        }
        objectOutputStream.writeObject((Object) null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        this.map = new PropertyChangeListenerMap();
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        Hashtable hashtable = (Hashtable) readFields.get("children", (Object) null);
        this.source = readFields.get("source", (Object) null);
        readFields.get("propertyChangeSupportSerializedDataVersion", 2);
        while (true) {
            Object readObject = objectInputStream.readObject();
            if (readObject == null) {
                break;
            }
            this.map.add((String) null, (PropertyChangeListener) readObject);
        }
        if (hashtable != null) {
            for (Map.Entry entry : hashtable.entrySet()) {
                for (PropertyChangeListener add : ((PropertyChangeSupport) entry.getValue()).getPropertyChangeListeners()) {
                    this.map.add((String) entry.getKey(), add);
                }
            }
        }
    }

    private static final class PropertyChangeListenerMap extends ChangeListenerMap<PropertyChangeListener> {
        private static final PropertyChangeListener[] EMPTY = new PropertyChangeListener[0];

        private PropertyChangeListenerMap() {
        }

        /* access modifiers changed from: protected */
        public PropertyChangeListener[] newArray(int i) {
            if (i > 0) {
                return new PropertyChangeListener[i];
            }
            return EMPTY;
        }

        /* access modifiers changed from: protected */
        public PropertyChangeListener newProxy(String str, PropertyChangeListener propertyChangeListener) {
            return new PropertyChangeListenerProxy(str, propertyChangeListener);
        }

        /* JADX WARNING: type inference failed for: r0v2, types: [java.util.EventListener] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final java.beans.PropertyChangeListener extract(java.beans.PropertyChangeListener r1) {
            /*
                r0 = this;
            L_0x0000:
                boolean r0 = r1 instanceof java.beans.PropertyChangeListenerProxy
                if (r0 == 0) goto L_0x000e
                java.beans.PropertyChangeListenerProxy r1 = (java.beans.PropertyChangeListenerProxy) r1
                java.util.EventListener r0 = r1.getListener()
                r1 = r0
                java.beans.PropertyChangeListener r1 = (java.beans.PropertyChangeListener) r1
                goto L_0x0000
            L_0x000e:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.beans.PropertyChangeSupport.PropertyChangeListenerMap.extract(java.beans.PropertyChangeListener):java.beans.PropertyChangeListener");
        }
    }
}
