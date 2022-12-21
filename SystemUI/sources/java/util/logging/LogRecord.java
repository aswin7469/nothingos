package java.util.logging;

import dalvik.system.VMStack;
import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;

public class LogRecord implements Serializable {
    private static final int MIN_SEQUENTIAL_THREAD_ID = 1073741823;
    private static final AtomicLong globalSequenceNumber = new AtomicLong(0);
    private static final AtomicInteger nextThreadId = new AtomicInteger(1073741823);
    private static final long serialVersionUID = 5372048053134512534L;
    private static final ThreadLocal<Integer> threadIds = new ThreadLocal<>();
    private Level level;
    private String loggerName;
    private String message;
    private long millis = System.currentTimeMillis();
    private transient boolean needToInferCaller = true;
    private transient Object[] parameters;
    private transient ResourceBundle resourceBundle;
    private String resourceBundleName;
    private long sequenceNumber = globalSequenceNumber.getAndIncrement();
    private String sourceClassName;
    private String sourceMethodName;
    private int threadID = defaultThreadID();
    private Throwable thrown;

    private int defaultThreadID() {
        long id = Thread.currentThread().getId();
        if (id < LockFreeTaskQueueCore.HEAD_MASK) {
            return (int) id;
        }
        ThreadLocal<Integer> threadLocal = threadIds;
        Integer num = threadLocal.get();
        if (num == null) {
            num = Integer.valueOf(nextThreadId.getAndIncrement());
            threadLocal.set(num);
        }
        return num.intValue();
    }

    public LogRecord(Level level2, String str) {
        level2.getClass();
        this.level = level2;
        this.message = str;
    }

    public String getLoggerName() {
        return this.loggerName;
    }

    public void setLoggerName(String str) {
        this.loggerName = str;
    }

    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle2) {
        this.resourceBundle = resourceBundle2;
    }

    public String getResourceBundleName() {
        return this.resourceBundleName;
    }

    public void setResourceBundleName(String str) {
        this.resourceBundleName = str;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level2) {
        level2.getClass();
        this.level = level2;
    }

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(long j) {
        this.sequenceNumber = j;
    }

    public String getSourceClassName() {
        if (this.needToInferCaller) {
            inferCaller();
        }
        return this.sourceClassName;
    }

    public void setSourceClassName(String str) {
        this.sourceClassName = str;
        this.needToInferCaller = false;
    }

    public String getSourceMethodName() {
        if (this.needToInferCaller) {
            inferCaller();
        }
        return this.sourceMethodName;
    }

    public void setSourceMethodName(String str) {
        this.sourceMethodName = str;
        this.needToInferCaller = false;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public void setParameters(Object[] objArr) {
        this.parameters = objArr;
    }

    public int getThreadID() {
        return this.threadID;
    }

    public void setThreadID(int i) {
        this.threadID = i;
    }

    public long getMillis() {
        return this.millis;
    }

    public void setMillis(long j) {
        this.millis = j;
    }

    public Throwable getThrown() {
        return this.thrown;
    }

    public void setThrown(Throwable th) {
        this.thrown = th;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeByte(1);
        int i = 0;
        objectOutputStream.writeByte(0);
        Object[] objArr = this.parameters;
        if (objArr == null) {
            objectOutputStream.writeInt(-1);
            return;
        }
        objectOutputStream.writeInt(objArr.length);
        while (true) {
            Object[] objArr2 = this.parameters;
            if (i < objArr2.length) {
                Object obj = objArr2[i];
                if (obj == null) {
                    objectOutputStream.writeObject((Object) null);
                } else {
                    objectOutputStream.writeObject(obj.toString());
                }
                i++;
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:19|20|21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007f, code lost:
        r6.resourceBundle = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x006a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.p026io.ObjectInputStream r7) throws java.p026io.IOException, java.lang.ClassNotFoundException {
        /*
            r6 = this;
            r7.defaultReadObject()
            byte r0 = r7.readByte()
            byte r1 = r7.readByte()
            r2 = 1
            if (r0 != r2) goto L_0x008a
            int r0 = r7.readInt()
            r1 = -1
            if (r0 < r1) goto L_0x0084
            r2 = 0
            r3 = 0
            if (r0 != r1) goto L_0x001c
            r6.parameters = r2
            goto L_0x0057
        L_0x001c:
            r1 = 255(0xff, float:3.57E-43)
            if (r0 >= r1) goto L_0x0033
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r6.parameters = r0
            r0 = r3
        L_0x0025:
            java.lang.Object[] r1 = r6.parameters
            int r4 = r1.length
            if (r0 >= r4) goto L_0x0057
            java.lang.Object r4 = r7.readObject()
            r1[r0] = r4
            int r0 = r0 + 1
            goto L_0x0025
        L_0x0033:
            java.util.ArrayList r1 = new java.util.ArrayList
            r4 = 1024(0x400, float:1.435E-42)
            int r4 = java.lang.Math.min((int) r0, (int) r4)
            r1.<init>((int) r4)
            r4 = r3
        L_0x003f:
            if (r4 >= r0) goto L_0x004b
            java.lang.Object r5 = r7.readObject()
            r1.add(r5)
            int r4 = r4 + 1
            goto L_0x003f
        L_0x004b:
            int r7 = r1.size()
            java.lang.Object[] r7 = new java.lang.Object[r7]
            java.lang.Object[] r7 = r1.toArray(r7)
            r6.parameters = r7
        L_0x0057:
            java.lang.String r7 = r6.resourceBundleName
            if (r7 == 0) goto L_0x0081
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch:{ MissingResourceException -> 0x006a }
            java.lang.ClassLoader r1 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ MissingResourceException -> 0x006a }
            java.util.ResourceBundle r7 = java.util.ResourceBundle.getBundle((java.lang.String) r7, (java.util.Locale) r0, (java.lang.ClassLoader) r1)     // Catch:{ MissingResourceException -> 0x006a }
            r6.resourceBundle = r7     // Catch:{ MissingResourceException -> 0x006a }
            goto L_0x0081
        L_0x006a:
            java.lang.String r7 = r6.resourceBundleName     // Catch:{ MissingResourceException -> 0x007f }
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch:{ MissingResourceException -> 0x007f }
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ MissingResourceException -> 0x007f }
            java.lang.ClassLoader r1 = r1.getContextClassLoader()     // Catch:{ MissingResourceException -> 0x007f }
            java.util.ResourceBundle r7 = java.util.ResourceBundle.getBundle((java.lang.String) r7, (java.util.Locale) r0, (java.lang.ClassLoader) r1)     // Catch:{ MissingResourceException -> 0x007f }
            r6.resourceBundle = r7     // Catch:{ MissingResourceException -> 0x007f }
            goto L_0x0081
        L_0x007f:
            r6.resourceBundle = r2
        L_0x0081:
            r6.needToInferCaller = r3
            return
        L_0x0084:
            java.lang.NegativeArraySizeException r6 = new java.lang.NegativeArraySizeException
            r6.<init>()
            throw r6
        L_0x008a:
            java.io.IOException r6 = new java.io.IOException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r2 = "LogRecord: bad version: "
            r7.<init>((java.lang.String) r2)
            r7.append((int) r0)
            java.lang.String r0 = "."
            r7.append((java.lang.String) r0)
            r7.append((int) r1)
            java.lang.String r7 = r7.toString()
            r6.<init>((java.lang.String) r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.LogRecord.readObject(java.io.ObjectInputStream):void");
    }

    private void inferCaller() {
        this.needToInferCaller = false;
        boolean z = true;
        for (StackTraceElement stackTraceElement : VMStack.getThreadStackTrace(Thread.currentThread())) {
            String className = stackTraceElement.getClassName();
            boolean isLoggerImplFrame = isLoggerImplFrame(className);
            if (z) {
                if (isLoggerImplFrame) {
                    z = false;
                }
            } else if (!isLoggerImplFrame && !className.startsWith("java.lang.reflect.") && !className.startsWith("sun.reflect.")) {
                setSourceClassName(className);
                setSourceMethodName(stackTraceElement.getMethodName());
                return;
            }
        }
    }

    private boolean isLoggerImplFrame(String str) {
        return str.equals("java.util.logging.Logger") || str.startsWith("java.util.logging.LoggingProxyImpl") || str.startsWith("sun.util.logging.");
    }
}
