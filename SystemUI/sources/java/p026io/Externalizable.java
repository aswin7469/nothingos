package java.p026io;

/* renamed from: java.io.Externalizable */
public interface Externalizable extends Serializable {
    void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException;

    void writeExternal(ObjectOutput objectOutput) throws IOException;
}
