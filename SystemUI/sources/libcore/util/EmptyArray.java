package libcore.util;

import android.annotation.SystemApi;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class EmptyArray {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final boolean[] BOOLEAN = new boolean[0];
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final byte[] BYTE = new byte[0];
    public static final char[] CHAR = new char[0];
    public static final Class<?>[] CLASS = new Class[0];
    public static final double[] DOUBLE = new double[0];
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final float[] FLOAT = new float[0];
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int[] INT = new int[0];
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final long[] LONG = new long[0];
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final Object[] OBJECT = new Object[0];
    public static final StackTraceElement[] STACK_TRACE_ELEMENT = new StackTraceElement[0];
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final String[] STRING = new String[0];
    public static final Throwable[] THROWABLE = new Throwable[0];
    public static final Type[] TYPE = new Type[0];
    public static final TypeVariable[] TYPE_VARIABLE = new TypeVariable[0];

    private EmptyArray() {
    }
}
