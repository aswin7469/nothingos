package java.p026io;

import java.p026io.ObjectStreamClass;

/* renamed from: java.io.ObjectStreamClass$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ObjectStreamClass$$ExternalSyntheticLambda0 implements ObjectStreamClass.DefaultSUIDCompatibilityListener {
    public final void warnDefaultSUIDTargetVersionDependent(Class cls, long j) {
        System.logW("Class " + cls.getCanonicalName() + " relies on its default SUID which is dependent on the app's targetSdkVersion. To avoid problems during upgrade add the following to class " + cls.getCanonicalName() + "\n    private static final long serialVersionUID = " + j + "L;");
    }
}
