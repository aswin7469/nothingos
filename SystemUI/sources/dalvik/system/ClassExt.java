package dalvik.system;

public final class ClassExt {
    private Throwable erroneousStateError;
    private Object instanceJfieldIDs;
    private Object jmethodIDs;
    private Class<?> obsoleteClass;
    private Object[] obsoleteDexCaches;
    private Object obsoleteMethods;
    private Object originalDexFile;
    private int preRedefineClassDefIndex;
    private long preRedefineDexFilePtr;
    private Object staticJfieldIDs;

    private ClassExt() {
    }
}
