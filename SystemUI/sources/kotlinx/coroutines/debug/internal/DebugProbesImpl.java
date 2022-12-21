package kotlinx.coroutines.debug.internal;

import java.p026io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.KotlinVersion;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobSupport;
import kotlinx.coroutines.internal.ScopeCoroutine;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;

@Metadata(mo64986d1 = {"\u0000Ò\u0001\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\bÀ\u0002\u0018\u00002\u00020\u0013:\u0002\u0001B\t\b\u0002¢\u0006\u0004\b\u0001\u0010\u0002J3\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\"\u0004\b\u0000\u0010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006H\u0002¢\u0006\u0004\b\b\u0010\tJ\u0015\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\n¢\u0006\u0004\b\r\u0010\u000eJ\u0013\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f¢\u0006\u0004\b\u0011\u0010\u0012J>\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u000f\"\b\b\u0000\u0010\u0014*\u00020\u00132\u001c\u0010\u0018\u001a\u0018\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0016\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00028\u00000\u0015H\b¢\u0006\u0004\b\u0019\u0010\u001aJ\u0017\u0010\u001b\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\nH\u0002¢\u0006\u0004\b\u001b\u0010\u000eJ\u0013\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000f¢\u0006\u0004\b\u001d\u0010\u0012J)\u0010!\u001a\b\u0012\u0004\u0012\u00020\u001f0\u000f2\u0006\u0010\u001e\u001a\u00020\u00102\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u000f¢\u0006\u0004\b!\u0010\"J5\u0010'\u001a\b\u0012\u0004\u0012\u00020\u001f0\u000f2\u0006\u0010$\u001a\u00020#2\b\u0010&\u001a\u0004\u0018\u00010%2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u000fH\u0002¢\u0006\u0004\b'\u0010(J?\u0010.\u001a\u000e\u0012\u0004\u0012\u00020)\u0012\u0004\u0012\u00020)0-2\u0006\u0010*\u001a\u00020)2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u001f0+2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u000fH\u0002¢\u0006\u0004\b.\u0010/J3\u00101\u001a\u00020)2\u0006\u00100\u001a\u00020)2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u001f0+2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u000fH\u0002¢\u0006\u0004\b1\u00102J\u001d\u00105\u001a\u0010\u0012\u0004\u0012\u000204\u0012\u0004\u0012\u00020\f\u0018\u000103H\u0002¢\u0006\u0004\b5\u00106J\u0015\u00109\u001a\u00020#2\u0006\u00108\u001a\u000207¢\u0006\u0004\b9\u0010:J\r\u0010;\u001a\u00020\f¢\u0006\u0004\b;\u0010\u0002J%\u0010=\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\n2\f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u001f0\u000fH\u0002¢\u0006\u0004\b=\u0010>J\u001b\u0010@\u001a\u00020\f2\n\u0010?\u001a\u0006\u0012\u0002\b\u00030\u0016H\u0002¢\u0006\u0004\b@\u0010AJ)\u0010D\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\"\u0004\b\u0000\u0010\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0000¢\u0006\u0004\bB\u0010CJ\u001b\u0010G\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0000¢\u0006\u0004\bE\u0010FJ\u001b\u0010I\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u0004H\u0000¢\u0006\u0004\bH\u0010FJ'\u0010L\u001a\b\u0012\u0004\u0012\u00020\u001f0\u000f\"\b\b\u0000\u0010\u0003*\u00020J2\u0006\u0010K\u001a\u00028\u0000H\u0002¢\u0006\u0004\bL\u0010MJ\u000f\u0010N\u001a\u00020\fH\u0002¢\u0006\u0004\bN\u0010\u0002J\u000f\u0010O\u001a\u00020\fH\u0002¢\u0006\u0004\bO\u0010\u0002J\r\u0010P\u001a\u00020\f¢\u0006\u0004\bP\u0010\u0002J\u001f\u0010R\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020Q2\u0006\u0010$\u001a\u00020#H\u0002¢\u0006\u0004\bR\u0010SJ#\u0010T\u001a\u00020\f2\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010$\u001a\u00020#H\u0002¢\u0006\u0004\bT\u0010UJ/\u0010T\u001a\u00020\f2\n\u0010?\u001a\u0006\u0012\u0002\b\u00030\u00162\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010$\u001a\u00020#H\u0002¢\u0006\u0004\bT\u0010VJ;\u0010^\u001a\u00020\f*\u0002072\u0012\u0010Y\u001a\u000e\u0012\u0004\u0012\u000207\u0012\u0004\u0012\u00020X0W2\n\u0010\\\u001a\u00060Zj\u0002`[2\u0006\u0010]\u001a\u00020#H\u0002¢\u0006\u0004\b^\u0010_J\u0017\u0010`\u001a\u000204*\u0006\u0012\u0002\b\u00030\u0016H\u0002¢\u0006\u0004\b`\u0010aJ\u001d\u0010?\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0016*\u0006\u0012\u0002\b\u00030\u0004H\u0002¢\u0006\u0004\b?\u0010bJ\u001a\u0010?\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0016*\u00020QH\u0010¢\u0006\u0004\b?\u0010cJ\u0016\u0010d\u001a\u0004\u0018\u00010Q*\u00020QH\u0010¢\u0006\u0004\bd\u0010eJ\u001b\u0010f\u001a\u0004\u0018\u00010\u0006*\b\u0012\u0004\u0012\u00020\u001f0\u000fH\u0002¢\u0006\u0004\bf\u0010gR\u0016\u0010h\u001a\u00020#8\u0002@\u0002XT¢\u0006\u0006\n\u0004\bh\u0010iR\"\u0010k\u001a\u000e\u0012\u0004\u0012\u00020Q\u0012\u0004\u0012\u00020X0j8\u0002@\u0002X\u0004¢\u0006\u0006\n\u0004\bk\u0010lR \u0010p\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00160m8B@\u0002X\u0004¢\u0006\u0006\u001a\u0004\bn\u0010oR&\u0010q\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0016\u0012\u0004\u0012\u0002040j8\u0002@\u0002X\u0004¢\u0006\u0006\n\u0004\bq\u0010lR\u0016\u0010s\u001a\u00020r8\u0002@\u0002X\u0004¢\u0006\u0006\n\u0004\bs\u0010tR\u0016\u0010v\u001a\u00020u8\u0002@\u0002X\u0004¢\u0006\u0006\n\u0004\bv\u0010wR$\u0010x\u001a\u0010\u0012\u0004\u0012\u000204\u0012\u0004\u0012\u00020\f\u0018\u0001038\u0002@\u0002X\u0004¢\u0006\u0006\n\u0004\bx\u0010yR\"\u0010z\u001a\u0002048\u0006@\u0006X\u000e¢\u0006\u0012\n\u0004\bz\u0010{\u001a\u0004\b|\u0010}\"\u0004\b~\u0010R\u0019\u0010\u0001\u001a\u00020)8\u0002@\u0002X\u000e¢\u0006\b\n\u0006\b\u0001\u0010\u0001R\u0018\u0010\u0001\u001a\u0002048@@\u0000X\u0004¢\u0006\u0007\u001a\u0005\b\u0001\u0010}R&\u0010\u0001\u001a\u0002048\u0006@\u0006X\u000e¢\u0006\u0015\n\u0005\b\u0001\u0010{\u001a\u0005\b\u0001\u0010}\"\u0005\b\u0001\u0010R\u001b\u0010\u0001\u001a\u0004\u0018\u00010%8\u0002@\u0002X\u000e¢\u0006\b\n\u0006\b\u0001\u0010\u0001R$\u0010\u0001\u001a\u00020#*\u0002078B@\u0002X\u0004¢\u0006\u000f\u0012\u0006\b\u0001\u0010\u0001\u001a\u0005\b\u0001\u0010:R\u001d\u0010\u0001\u001a\u000204*\u00020\u001f8B@\u0002X\u0004¢\u0006\b\u001a\u0006\b\u0001\u0010\u0001¨\u0006\u0001"}, mo64987d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl;", "<init>", "()V", "T", "Lkotlin/coroutines/Continuation;", "completion", "Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "frame", "createOwner", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/debug/internal/StackTraceFrame;)Lkotlin/coroutines/Continuation;", "Ljava/io/PrintStream;", "out", "", "dumpCoroutines", "(Ljava/io/PrintStream;)V", "", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;", "dumpCoroutinesInfo", "()Ljava/util/List;", "", "R", "Lkotlin/Function2;", "Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "Lkotlin/coroutines/CoroutineContext;", "create", "dumpCoroutinesInfoImpl", "(Lkotlin/jvm/functions/Function2;)Ljava/util/List;", "dumpCoroutinesSynchronized", "Lkotlinx/coroutines/debug/internal/DebuggerInfo;", "dumpDebuggerInfo", "info", "Ljava/lang/StackTraceElement;", "coroutineTrace", "enhanceStackTraceWithThreadDump", "(Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;Ljava/util/List;)Ljava/util/List;", "", "state", "Ljava/lang/Thread;", "thread", "enhanceStackTraceWithThreadDumpImpl", "(Ljava/lang/String;Ljava/lang/Thread;Ljava/util/List;)Ljava/util/List;", "", "indexOfResumeWith", "", "actualTrace", "Lkotlin/Pair;", "findContinuationStartIndex", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)Lkotlin/Pair;", "frameIndex", "findIndexOfFrame", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)I", "Lkotlin/Function1;", "", "getDynamicAttach", "()Lkotlin/jvm/functions/Function1;", "Lkotlinx/coroutines/Job;", "job", "hierarchyToString", "(Lkotlinx/coroutines/Job;)Ljava/lang/String;", "install", "frames", "printStackTrace", "(Ljava/io/PrintStream;Ljava/util/List;)V", "owner", "probeCoroutineCompleted", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;)V", "probeCoroutineCreated$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "probeCoroutineCreated", "probeCoroutineResumed$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)V", "probeCoroutineResumed", "probeCoroutineSuspended$kotlinx_coroutines_core", "probeCoroutineSuspended", "", "throwable", "sanitizeStackTrace", "(Ljava/lang/Throwable;)Ljava/util/List;", "startWeakRefCleanerThread", "stopWeakRefCleanerThread", "uninstall", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "updateRunningState", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;Ljava/lang/String;)V", "updateState", "(Lkotlin/coroutines/Continuation;Ljava/lang/String;)V", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;Lkotlin/coroutines/Continuation;Ljava/lang/String;)V", "", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "map", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "builder", "indent", "build", "(Lkotlinx/coroutines/Job;Ljava/util/Map;Ljava/lang/StringBuilder;Ljava/lang/String;)V", "isFinished", "(Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;)Z", "(Lkotlin/coroutines/Continuation;)Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "realCaller", "(Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "toStackTraceFrame", "(Ljava/util/List;)Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "ARTIFICIAL_FRAME_MESSAGE", "Ljava/lang/String;", "Lkotlinx/coroutines/debug/internal/ConcurrentWeakMap;", "callerInfoCache", "Lkotlinx/coroutines/debug/internal/ConcurrentWeakMap;", "", "getCapturedCoroutines", "()Ljava/util/Set;", "capturedCoroutines", "capturedCoroutinesMap", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "coroutineStateLock", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "Ljava/text/SimpleDateFormat;", "dateFormat", "Ljava/text/SimpleDateFormat;", "dynamicAttach", "Lkotlin/jvm/functions/Function1;", "enableCreationStackTraces", "Z", "getEnableCreationStackTraces", "()Z", "setEnableCreationStackTraces", "(Z)V", "installations", "I", "isInstalled$kotlinx_coroutines_core", "isInstalled", "sanitizeStackTraces", "getSanitizeStackTraces", "setSanitizeStackTraces", "weakRefCleanerThread", "Ljava/lang/Thread;", "getDebugString", "getDebugString$annotations", "(Lkotlinx/coroutines/Job;)V", "debugString", "isInternalMethod", "(Ljava/lang/StackTraceElement;)Z", "CoroutineOwner", "kotlinx-coroutines-core"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* compiled from: DebugProbesImpl.kt */
public final class DebugProbesImpl {
    private static final String ARTIFICIAL_FRAME_MESSAGE = "Coroutine creation stacktrace";
    public static final DebugProbesImpl INSTANCE;
    /* access modifiers changed from: private */
    public static final ConcurrentWeakMap<CoroutineStackFrame, DebugCoroutineInfoImpl> callerInfoCache = new ConcurrentWeakMap<>(true);
    private static final ConcurrentWeakMap<CoroutineOwner<?>, Boolean> capturedCoroutinesMap = new ConcurrentWeakMap<>(false, 1, (DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final ReentrantReadWriteLock coroutineStateLock = new ReentrantReadWriteLock();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final /* synthetic */ SequenceNumberRefVolatile debugProbesImpl$SequenceNumberRefVolatile = new SequenceNumberRefVolatile(0);
    private static final Function1<Boolean, Unit> dynamicAttach;
    private static boolean enableCreationStackTraces = true;
    private static volatile int installations;
    private static boolean sanitizeStackTraces = true;
    private static final /* synthetic */ AtomicLongFieldUpdater sequenceNumber$FU = AtomicLongFieldUpdater.newUpdater(SequenceNumberRefVolatile.class, "sequenceNumber");
    private static Thread weakRefCleanerThread;

    /* synthetic */ class SequenceNumberRefVolatile {
        volatile long sequenceNumber;

        public SequenceNumberRefVolatile(long j) {
            this.sequenceNumber = j;
        }
    }

    private static /* synthetic */ void getDebugString$annotations(Job job) {
    }

    private DebugProbesImpl() {
    }

    static {
        DebugProbesImpl debugProbesImpl = new DebugProbesImpl();
        INSTANCE = debugProbesImpl;
        dynamicAttach = debugProbesImpl.getDynamicAttach();
    }

    /* access modifiers changed from: private */
    public final Set<CoroutineOwner<?>> getCapturedCoroutines() {
        return capturedCoroutinesMap.keySet();
    }

    public final boolean isInstalled$kotlinx_coroutines_core() {
        return installations > 0;
    }

    public final boolean getSanitizeStackTraces() {
        return sanitizeStackTraces;
    }

    public final void setSanitizeStackTraces(boolean z) {
        sanitizeStackTraces = z;
    }

    public final boolean getEnableCreationStackTraces() {
        return enableCreationStackTraces;
    }

    public final void setEnableCreationStackTraces(boolean z) {
        enableCreationStackTraces = z;
    }

    private final Function1<Boolean, Unit> getDynamicAttach() {
        Object obj;
        try {
            Result.Companion companion = Result.Companion;
            DebugProbesImpl debugProbesImpl = this;
            Object newInstance = Class.forName("kotlinx.coroutines.debug.internal.ByteBuddyDynamicAttach").getConstructors()[0].newInstance(new Object[0]);
            if (newInstance != null) {
                obj = Result.m3936constructorimpl((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(newInstance, 1));
                if (Result.m3942isFailureimpl(obj)) {
                    obj = null;
                }
                return (Function1) obj;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Function1<kotlin.Boolean, kotlin.Unit>");
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m3936constructorimpl(ResultKt.createFailure(th));
        }
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final void install() {
        /*
            r6 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r6 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r6.readLock()
            int r1 = r6.getWriteHoldCount()
            r2 = 0
            if (r1 != 0) goto L_0x0012
            int r1 = r6.getReadHoldCount()
            goto L_0x0013
        L_0x0012:
            r1 = r2
        L_0x0013:
            r3 = r2
        L_0x0014:
            if (r3 >= r1) goto L_0x001c
            r0.unlock()
            int r3 = r3 + 1
            goto L_0x0014
        L_0x001c:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r6 = r6.writeLock()
            r6.lock()
            kotlinx.coroutines.debug.internal.DebugProbesImpl r3 = INSTANCE     // Catch:{ all -> 0x006c }
            int r4 = installations     // Catch:{ all -> 0x006c }
            r5 = 1
            int r4 = r4 + r5
            installations = r4     // Catch:{ all -> 0x006c }
            int r4 = installations     // Catch:{ all -> 0x006c }
            if (r4 <= r5) goto L_0x003b
        L_0x002f:
            if (r2 >= r1) goto L_0x0037
            r0.lock()
            int r2 = r2 + 1
            goto L_0x002f
        L_0x0037:
            r6.unlock()
            return
        L_0x003b:
            r3.startWeakRefCleanerThread()     // Catch:{ all -> 0x006c }
            kotlinx.coroutines.debug.AgentPremain r3 = kotlinx.coroutines.debug.AgentPremain.INSTANCE     // Catch:{ all -> 0x006c }
            boolean r3 = r3.isInstalledStatically()     // Catch:{ all -> 0x006c }
            if (r3 == 0) goto L_0x0052
        L_0x0046:
            if (r2 >= r1) goto L_0x004e
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0046
        L_0x004e:
            r6.unlock()
            return
        L_0x0052:
            kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> r3 = dynamicAttach     // Catch:{ all -> 0x006c }
            if (r3 != 0) goto L_0x0057
            goto L_0x005e
        L_0x0057:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf((boolean) r5)     // Catch:{ all -> 0x006c }
            r3.invoke(r4)     // Catch:{ all -> 0x006c }
        L_0x005e:
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x006c }
        L_0x0060:
            if (r2 >= r1) goto L_0x0068
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0060
        L_0x0068:
            r6.unlock()
            return
        L_0x006c:
            r3 = move-exception
        L_0x006d:
            if (r2 >= r1) goto L_0x0075
            r0.lock()
            int r2 = r2 + 1
            goto L_0x006d
        L_0x0075:
            r6.unlock()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.install():void");
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final void uninstall() {
        /*
            r5 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r5 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r5.readLock()
            int r1 = r5.getWriteHoldCount()
            r2 = 0
            if (r1 != 0) goto L_0x0012
            int r1 = r5.getReadHoldCount()
            goto L_0x0013
        L_0x0012:
            r1 = r2
        L_0x0013:
            r3 = r2
        L_0x0014:
            if (r3 >= r1) goto L_0x001c
            r0.unlock()
            int r3 = r3 + 1
            goto L_0x0014
        L_0x001c:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r5 = r5.writeLock()
            r5.lock()
            kotlinx.coroutines.debug.internal.DebugProbesImpl r3 = INSTANCE     // Catch:{ all -> 0x008a }
            boolean r4 = r3.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x008a }
            if (r4 == 0) goto L_0x007c
            int r4 = installations     // Catch:{ all -> 0x008a }
            int r4 = r4 + -1
            installations = r4     // Catch:{ all -> 0x008a }
            int r4 = installations     // Catch:{ all -> 0x008a }
            if (r4 == 0) goto L_0x0041
        L_0x0035:
            if (r2 >= r1) goto L_0x003d
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0035
        L_0x003d:
            r5.unlock()
            return
        L_0x0041:
            r3.stopWeakRefCleanerThread()     // Catch:{ all -> 0x008a }
            kotlinx.coroutines.debug.internal.ConcurrentWeakMap<kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner<?>, java.lang.Boolean> r3 = capturedCoroutinesMap     // Catch:{ all -> 0x008a }
            r3.clear()     // Catch:{ all -> 0x008a }
            kotlinx.coroutines.debug.internal.ConcurrentWeakMap<kotlin.coroutines.jvm.internal.CoroutineStackFrame, kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl> r3 = callerInfoCache     // Catch:{ all -> 0x008a }
            r3.clear()     // Catch:{ all -> 0x008a }
            kotlinx.coroutines.debug.AgentPremain r3 = kotlinx.coroutines.debug.AgentPremain.INSTANCE     // Catch:{ all -> 0x008a }
            boolean r3 = r3.isInstalledStatically()     // Catch:{ all -> 0x008a }
            if (r3 == 0) goto L_0x0062
        L_0x0056:
            if (r2 >= r1) goto L_0x005e
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0056
        L_0x005e:
            r5.unlock()
            return
        L_0x0062:
            kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> r3 = dynamicAttach     // Catch:{ all -> 0x008a }
            if (r3 != 0) goto L_0x0067
            goto L_0x006e
        L_0x0067:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf((boolean) r2)     // Catch:{ all -> 0x008a }
            r3.invoke(r4)     // Catch:{ all -> 0x008a }
        L_0x006e:
            kotlin.Unit r3 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x008a }
        L_0x0070:
            if (r2 >= r1) goto L_0x0078
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0070
        L_0x0078:
            r5.unlock()
            return
        L_0x007c:
            java.lang.String r3 = "Agent was not installed"
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x008a }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x008a }
            r4.<init>((java.lang.String) r3)     // Catch:{ all -> 0x008a }
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ all -> 0x008a }
            throw r4     // Catch:{ all -> 0x008a }
        L_0x008a:
            r3 = move-exception
        L_0x008b:
            if (r2 >= r1) goto L_0x0093
            r0.lock()
            int r2 = r2 + 1
            goto L_0x008b
        L_0x0093:
            r5.unlock()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.uninstall():void");
    }

    private final void startWeakRefCleanerThread() {
        weakRefCleanerThread = ThreadsKt.thread$default(false, true, (ClassLoader) null, "Coroutines Debugger Cleaner", 0, DebugProbesImpl$startWeakRefCleanerThread$1.INSTANCE, 21, (Object) null);
    }

    private final void stopWeakRefCleanerThread() {
        Thread thread = weakRefCleanerThread;
        if (thread != null) {
            thread.interrupt();
        }
        weakRefCleanerThread = null;
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final java.lang.String hierarchyToString(kotlinx.coroutines.Job r9) {
        /*
            r8 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r8 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r8.readLock()
            int r1 = r8.getWriteHoldCount()
            r2 = 0
            if (r1 != 0) goto L_0x0012
            int r1 = r8.getReadHoldCount()
            goto L_0x0013
        L_0x0012:
            r1 = r2
        L_0x0013:
            r3 = r2
        L_0x0014:
            if (r3 >= r1) goto L_0x001c
            r0.unlock()
            int r3 = r3 + 1
            goto L_0x0014
        L_0x001c:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r8 = r8.writeLock()
            r8.lock()
            kotlinx.coroutines.debug.internal.DebugProbesImpl r3 = INSTANCE     // Catch:{ all -> 0x00cf }
            boolean r4 = r3.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x00cf }
            if (r4 == 0) goto L_0x00c1
            java.util.Set r3 = r3.getCapturedCoroutines()     // Catch:{ all -> 0x00cf }
            java.lang.Iterable r3 = (java.lang.Iterable) r3     // Catch:{ all -> 0x00cf }
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ all -> 0x00cf }
            r4.<init>()     // Catch:{ all -> 0x00cf }
            java.util.Collection r4 = (java.util.Collection) r4     // Catch:{ all -> 0x00cf }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x00cf }
        L_0x003c:
            boolean r5 = r3.hasNext()     // Catch:{ all -> 0x00cf }
            if (r5 == 0) goto L_0x0062
            java.lang.Object r5 = r3.next()     // Catch:{ all -> 0x00cf }
            r6 = r5
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r6 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r6     // Catch:{ all -> 0x00cf }
            kotlin.coroutines.Continuation<T> r6 = r6.delegate     // Catch:{ all -> 0x00cf }
            kotlin.coroutines.CoroutineContext r6 = r6.getContext()     // Catch:{ all -> 0x00cf }
            kotlinx.coroutines.Job$Key r7 = kotlinx.coroutines.Job.Key     // Catch:{ all -> 0x00cf }
            kotlin.coroutines.CoroutineContext$Key r7 = (kotlin.coroutines.CoroutineContext.Key) r7     // Catch:{ all -> 0x00cf }
            kotlin.coroutines.CoroutineContext$Element r6 = r6.get(r7)     // Catch:{ all -> 0x00cf }
            if (r6 == 0) goto L_0x005b
            r6 = 1
            goto L_0x005c
        L_0x005b:
            r6 = r2
        L_0x005c:
            if (r6 == 0) goto L_0x003c
            r4.add(r5)     // Catch:{ all -> 0x00cf }
            goto L_0x003c
        L_0x0062:
            java.util.List r4 = (java.util.List) r4     // Catch:{ all -> 0x00cf }
            java.lang.Iterable r4 = (java.lang.Iterable) r4     // Catch:{ all -> 0x00cf }
            r3 = 10
            int r3 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r4, r3)     // Catch:{ all -> 0x00cf }
            int r3 = kotlin.collections.MapsKt.mapCapacity(r3)     // Catch:{ all -> 0x00cf }
            r5 = 16
            int r3 = kotlin.ranges.RangesKt.coerceAtLeast((int) r3, (int) r5)     // Catch:{ all -> 0x00cf }
            java.util.LinkedHashMap r5 = new java.util.LinkedHashMap     // Catch:{ all -> 0x00cf }
            r5.<init>((int) r3)     // Catch:{ all -> 0x00cf }
            java.util.Map r5 = (java.util.Map) r5     // Catch:{ all -> 0x00cf }
            java.util.Iterator r3 = r4.iterator()     // Catch:{ all -> 0x00cf }
        L_0x0081:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x00cf }
            if (r4 == 0) goto L_0x00a0
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x00cf }
            r6 = r4
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r6 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r6     // Catch:{ all -> 0x00cf }
            kotlin.coroutines.Continuation<T> r6 = r6.delegate     // Catch:{ all -> 0x00cf }
            kotlin.coroutines.CoroutineContext r6 = r6.getContext()     // Catch:{ all -> 0x00cf }
            kotlinx.coroutines.Job r6 = kotlinx.coroutines.JobKt.getJob(r6)     // Catch:{ all -> 0x00cf }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r4 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r4     // Catch:{ all -> 0x00cf }
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r4 = r4.info     // Catch:{ all -> 0x00cf }
            r5.put(r6, r4)     // Catch:{ all -> 0x00cf }
            goto L_0x0081
        L_0x00a0:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00cf }
            r3.<init>()     // Catch:{ all -> 0x00cf }
            kotlinx.coroutines.debug.internal.DebugProbesImpl r4 = INSTANCE     // Catch:{ all -> 0x00cf }
            java.lang.String r6 = ""
            r4.build(r9, r5, r3, r6)     // Catch:{ all -> 0x00cf }
            java.lang.String r9 = r3.toString()     // Catch:{ all -> 0x00cf }
            java.lang.String r3 = "StringBuilder().apply(builderAction).toString()"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r3)     // Catch:{ all -> 0x00cf }
        L_0x00b5:
            if (r2 >= r1) goto L_0x00bd
            r0.lock()
            int r2 = r2 + 1
            goto L_0x00b5
        L_0x00bd:
            r8.unlock()
            return r9
        L_0x00c1:
            java.lang.String r9 = "Debug probes are not installed"
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00cf }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x00cf }
            r3.<init>((java.lang.String) r9)     // Catch:{ all -> 0x00cf }
            java.lang.Throwable r3 = (java.lang.Throwable) r3     // Catch:{ all -> 0x00cf }
            throw r3     // Catch:{ all -> 0x00cf }
        L_0x00cf:
            r9 = move-exception
        L_0x00d0:
            if (r2 >= r1) goto L_0x00d8
            r0.lock()
            int r2 = r2 + 1
            goto L_0x00d0
        L_0x00d8:
            r8.unlock()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.hierarchyToString(kotlinx.coroutines.Job):java.lang.String");
    }

    private final void build(Job job, Map<Job, DebugCoroutineInfoImpl> map, StringBuilder sb, String str) {
        DebugCoroutineInfoImpl debugCoroutineInfoImpl = map.get(job);
        if (debugCoroutineInfoImpl != null) {
            sb.append(str + getDebugString(job) + ", continuation is " + debugCoroutineInfoImpl.getState() + " at line " + ((StackTraceElement) CollectionsKt.firstOrNull(debugCoroutineInfoImpl.lastObservedStackTrace())) + 10);
            str = Intrinsics.stringPlus(str, "\t");
        } else if (!(job instanceof ScopeCoroutine)) {
            sb.append(str + getDebugString(job) + 10);
            str = Intrinsics.stringPlus(str, "\t");
        }
        for (Job build : job.getChildren()) {
            build(build, map, sb, str);
        }
    }

    private final String getDebugString(Job job) {
        return job instanceof JobSupport ? ((JobSupport) job).toDebugString() : job.toString();
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    private final <R> java.util.List<R> dumpCoroutinesInfoImpl(kotlin.jvm.functions.Function2<? super kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, ? super kotlin.coroutines.CoroutineContext, ? extends R> r10) {
        /*
            r9 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r9 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r9.readLock()
            int r1 = r9.getWriteHoldCount()
            r2 = 0
            if (r1 != 0) goto L_0x0014
            int r1 = r9.getReadHoldCount()
            goto L_0x0015
        L_0x0014:
            r1 = r2
        L_0x0015:
            r3 = r2
        L_0x0016:
            if (r3 >= r1) goto L_0x001e
            r0.unlock()
            int r3 = r3 + 1
            goto L_0x0016
        L_0x001e:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r9 = r9.writeLock()
            r9.lock()
            r3 = 1
            kotlinx.coroutines.debug.internal.DebugProbesImpl r4 = INSTANCE     // Catch:{ all -> 0x0097 }
            boolean r5 = r4.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x0097 }
            if (r5 == 0) goto L_0x0089
            java.util.Set r4 = r4.getCapturedCoroutines()     // Catch:{ all -> 0x0097 }
            java.lang.Iterable r4 = (java.lang.Iterable) r4     // Catch:{ all -> 0x0097 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-14$$inlined$sortedBy$1 r5 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-14$$inlined$sortedBy$1     // Catch:{ all -> 0x0097 }
            r5.<init>()     // Catch:{ all -> 0x0097 }
            java.util.Comparator r5 = (java.util.Comparator) r5     // Catch:{ all -> 0x0097 }
            java.util.List r4 = kotlin.collections.CollectionsKt.sortedWith(r4, r5)     // Catch:{ all -> 0x0097 }
            java.lang.Iterable r4 = (java.lang.Iterable) r4     // Catch:{ all -> 0x0097 }
            java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ all -> 0x0097 }
            r5.<init>()     // Catch:{ all -> 0x0097 }
            java.util.Collection r5 = (java.util.Collection) r5     // Catch:{ all -> 0x0097 }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x0097 }
        L_0x004c:
            boolean r6 = r4.hasNext()     // Catch:{ all -> 0x0097 }
            if (r6 == 0) goto L_0x0075
            java.lang.Object r6 = r4.next()     // Catch:{ all -> 0x0097 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r6 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r6     // Catch:{ all -> 0x0097 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl r7 = INSTANCE     // Catch:{ all -> 0x0097 }
            boolean r7 = r7.isFinished(r6)     // Catch:{ all -> 0x0097 }
            r8 = 0
            if (r7 == 0) goto L_0x0062
            goto L_0x006f
        L_0x0062:
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r7 = r6.info     // Catch:{ all -> 0x0097 }
            kotlin.coroutines.CoroutineContext r7 = r7.getContext()     // Catch:{ all -> 0x0097 }
            if (r7 != 0) goto L_0x006b
            goto L_0x006f
        L_0x006b:
            java.lang.Object r8 = r10.invoke(r6, r7)     // Catch:{ all -> 0x0097 }
        L_0x006f:
            if (r8 == 0) goto L_0x004c
            r5.add(r8)     // Catch:{ all -> 0x0097 }
            goto L_0x004c
        L_0x0075:
            java.util.List r5 = (java.util.List) r5     // Catch:{ all -> 0x0097 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
        L_0x007a:
            if (r2 >= r1) goto L_0x0082
            r0.lock()
            int r2 = r2 + 1
            goto L_0x007a
        L_0x0082:
            r9.unlock()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            return r5
        L_0x0089:
            java.lang.String r10 = "Debug probes are not installed"
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0097 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x0097 }
            r4.<init>((java.lang.String) r10)     // Catch:{ all -> 0x0097 }
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ all -> 0x0097 }
            throw r4     // Catch:{ all -> 0x0097 }
        L_0x0097:
            r10 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r3)
        L_0x009b:
            if (r2 >= r1) goto L_0x00a3
            r0.lock()
            int r2 = r2 + 1
            goto L_0x009b
        L_0x00a3:
            r9.unlock()
            kotlin.jvm.internal.InlineMarker.finallyEnd(r3)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesInfoImpl(kotlin.jvm.functions.Function2):java.util.List");
    }

    public final void dumpCoroutines(PrintStream printStream) {
        synchronized (printStream) {
            INSTANCE.dumpCoroutinesSynchronized(printStream);
            Unit unit = Unit.INSTANCE;
        }
    }

    /* access modifiers changed from: private */
    public final boolean isFinished(CoroutineOwner<?> coroutineOwner) {
        CoroutineContext context = coroutineOwner.info.getContext();
        Job job = context == null ? null : (Job) context.get(Job.Key);
        if (job == null || !job.isCompleted()) {
            return false;
        }
        capturedCoroutinesMap.remove(coroutineOwner);
        return true;
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    private final void dumpCoroutinesSynchronized(java.p026io.PrintStream r13) {
        /*
            r12 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r12 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r12.readLock()
            int r1 = r12.getWriteHoldCount()
            r2 = 0
            if (r1 != 0) goto L_0x0012
            int r1 = r12.getReadHoldCount()
            goto L_0x0013
        L_0x0012:
            r1 = r2
        L_0x0013:
            r3 = r2
        L_0x0014:
            if (r3 >= r1) goto L_0x001c
            r0.unlock()
            int r3 = r3 + 1
            goto L_0x0014
        L_0x001c:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r12 = r12.writeLock()
            r12.lock()
            kotlinx.coroutines.debug.internal.DebugProbesImpl r3 = INSTANCE     // Catch:{ all -> 0x00fd }
            boolean r4 = r3.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x00fd }
            if (r4 == 0) goto L_0x00ef
            java.lang.String r4 = "Coroutines dump "
            java.text.SimpleDateFormat r5 = dateFormat     // Catch:{ all -> 0x00fd }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00fd }
            java.lang.Long r6 = java.lang.Long.valueOf((long) r6)     // Catch:{ all -> 0x00fd }
            java.lang.String r5 = r5.format(r6)     // Catch:{ all -> 0x00fd }
            java.lang.String r4 = kotlin.jvm.internal.Intrinsics.stringPlus(r4, r5)     // Catch:{ all -> 0x00fd }
            r13.print((java.lang.String) r4)     // Catch:{ all -> 0x00fd }
            java.util.Set r3 = r3.getCapturedCoroutines()     // Catch:{ all -> 0x00fd }
            java.lang.Iterable r3 = (java.lang.Iterable) r3     // Catch:{ all -> 0x00fd }
            kotlin.sequences.Sequence r3 = kotlin.collections.CollectionsKt.asSequence(r3)     // Catch:{ all -> 0x00fd }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesSynchronized$1$2 r4 = kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesSynchronized$1$2.INSTANCE     // Catch:{ all -> 0x00fd }
            kotlin.jvm.functions.Function1 r4 = (kotlin.jvm.functions.Function1) r4     // Catch:{ all -> 0x00fd }
            kotlin.sequences.Sequence r3 = kotlin.sequences.SequencesKt.filter(r3, r4)     // Catch:{ all -> 0x00fd }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesSynchronized$lambda-21$$inlined$sortedBy$1 r4 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesSynchronized$lambda-21$$inlined$sortedBy$1     // Catch:{ all -> 0x00fd }
            r4.<init>()     // Catch:{ all -> 0x00fd }
            java.util.Comparator r4 = (java.util.Comparator) r4     // Catch:{ all -> 0x00fd }
            kotlin.sequences.Sequence r3 = kotlin.sequences.SequencesKt.sortedWith(r3, r4)     // Catch:{ all -> 0x00fd }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x00fd }
        L_0x0063:
            boolean r4 = r3.hasNext()     // Catch:{ all -> 0x00fd }
            if (r4 == 0) goto L_0x00e1
            java.lang.Object r4 = r3.next()     // Catch:{ all -> 0x00fd }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r4 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r4     // Catch:{ all -> 0x00fd }
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r5 = r4.info     // Catch:{ all -> 0x00fd }
            java.util.List r6 = r5.lastObservedStackTrace()     // Catch:{ all -> 0x00fd }
            kotlinx.coroutines.debug.internal.DebugProbesImpl r7 = INSTANCE     // Catch:{ all -> 0x00fd }
            java.lang.String r8 = r5.getState()     // Catch:{ all -> 0x00fd }
            java.lang.Thread r9 = r5.lastObservedThread     // Catch:{ all -> 0x00fd }
            java.util.List r8 = r7.enhanceStackTraceWithThreadDumpImpl(r8, r9, r6)     // Catch:{ all -> 0x00fd }
            java.lang.String r9 = r5.getState()     // Catch:{ all -> 0x00fd }
            java.lang.String r10 = "RUNNING"
            boolean r9 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r9, (java.lang.Object) r10)     // Catch:{ all -> 0x00fd }
            if (r9 == 0) goto L_0x009a
            if (r8 != r6) goto L_0x009a
            java.lang.String r9 = r5.getState()     // Catch:{ all -> 0x00fd }
            java.lang.String r10 = " (Last suspension stacktrace, not an actual stacktrace)"
            java.lang.String r9 = kotlin.jvm.internal.Intrinsics.stringPlus(r9, r10)     // Catch:{ all -> 0x00fd }
            goto L_0x009e
        L_0x009a:
            java.lang.String r9 = r5.getState()     // Catch:{ all -> 0x00fd }
        L_0x009e:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x00fd }
            r10.<init>()     // Catch:{ all -> 0x00fd }
            java.lang.String r11 = "\n\nCoroutine "
            java.lang.StringBuilder r10 = r10.append((java.lang.String) r11)     // Catch:{ all -> 0x00fd }
            kotlin.coroutines.Continuation<T> r4 = r4.delegate     // Catch:{ all -> 0x00fd }
            java.lang.StringBuilder r4 = r10.append((java.lang.Object) r4)     // Catch:{ all -> 0x00fd }
            java.lang.String r10 = ", state: "
            java.lang.StringBuilder r4 = r4.append((java.lang.String) r10)     // Catch:{ all -> 0x00fd }
            java.lang.StringBuilder r4 = r4.append((java.lang.String) r9)     // Catch:{ all -> 0x00fd }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x00fd }
            r13.print((java.lang.String) r4)     // Catch:{ all -> 0x00fd }
            boolean r4 = r6.isEmpty()     // Catch:{ all -> 0x00fd }
            if (r4 == 0) goto L_0x00dd
            java.lang.String r4 = "\n\tat "
            java.lang.String r6 = "Coroutine creation stacktrace"
            java.lang.StackTraceElement r6 = kotlinx.coroutines.internal.StackTraceRecoveryKt.artificialFrame(r6)     // Catch:{ all -> 0x00fd }
            java.lang.String r4 = kotlin.jvm.internal.Intrinsics.stringPlus(r4, r6)     // Catch:{ all -> 0x00fd }
            r13.print((java.lang.String) r4)     // Catch:{ all -> 0x00fd }
            java.util.List r4 = r5.getCreationStackTrace()     // Catch:{ all -> 0x00fd }
            r7.printStackTrace(r13, r4)     // Catch:{ all -> 0x00fd }
            goto L_0x0063
        L_0x00dd:
            r7.printStackTrace(r13, r8)     // Catch:{ all -> 0x00fd }
            goto L_0x0063
        L_0x00e1:
            kotlin.Unit r13 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00fd }
        L_0x00e3:
            if (r2 >= r1) goto L_0x00eb
            r0.lock()
            int r2 = r2 + 1
            goto L_0x00e3
        L_0x00eb:
            r12.unlock()
            return
        L_0x00ef:
            java.lang.String r13 = "Debug probes are not installed"
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00fd }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x00fd }
            r3.<init>((java.lang.String) r13)     // Catch:{ all -> 0x00fd }
            java.lang.Throwable r3 = (java.lang.Throwable) r3     // Catch:{ all -> 0x00fd }
            throw r3     // Catch:{ all -> 0x00fd }
        L_0x00fd:
            r13 = move-exception
        L_0x00fe:
            if (r2 >= r1) goto L_0x0106
            r0.lock()
            int r2 = r2 + 1
            goto L_0x00fe
        L_0x0106:
            r12.unlock()
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesSynchronized(java.io.PrintStream):void");
    }

    private final void printStackTrace(PrintStream printStream, List<StackTraceElement> list) {
        for (StackTraceElement stringPlus : list) {
            printStream.print(Intrinsics.stringPlus("\n\tat ", stringPlus));
        }
    }

    public final List<StackTraceElement> enhanceStackTraceWithThreadDump(DebugCoroutineInfo debugCoroutineInfo, List<StackTraceElement> list) {
        return enhanceStackTraceWithThreadDumpImpl(debugCoroutineInfo.getState(), debugCoroutineInfo.getLastObservedThread(), list);
    }

    private final List<StackTraceElement> enhanceStackTraceWithThreadDumpImpl(String str, Thread thread, List<StackTraceElement> list) {
        Object obj;
        if (!Intrinsics.areEqual((Object) str, (Object) DebugCoroutineInfoImplKt.RUNNING) || thread == null) {
            return list;
        }
        try {
            Result.Companion companion = Result.Companion;
            DebugProbesImpl debugProbesImpl = this;
            obj = Result.m3936constructorimpl(thread.getStackTrace());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            obj = Result.m3936constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m3942isFailureimpl(obj)) {
            obj = null;
        }
        StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) obj;
        if (stackTraceElementArr == null) {
            return list;
        }
        int length = stackTraceElementArr.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                i2 = -1;
                break;
            }
            StackTraceElement stackTraceElement = stackTraceElementArr[i2];
            if (Intrinsics.areEqual((Object) stackTraceElement.getClassName(), (Object) "kotlin.coroutines.jvm.internal.BaseContinuationImpl") && Intrinsics.areEqual((Object) stackTraceElement.getMethodName(), (Object) "resumeWith") && Intrinsics.areEqual((Object) stackTraceElement.getFileName(), (Object) "ContinuationImpl.kt")) {
                break;
            }
            i2++;
        }
        Pair<Integer, Integer> findContinuationStartIndex = findContinuationStartIndex(i2, stackTraceElementArr, list);
        int intValue = findContinuationStartIndex.component1().intValue();
        int intValue2 = findContinuationStartIndex.component2().intValue();
        if (intValue == -1) {
            return list;
        }
        ArrayList arrayList = new ArrayList((((list.size() + i2) - intValue) - 1) - intValue2);
        int i3 = i2 - intValue2;
        if (i3 > 0) {
            while (true) {
                int i4 = i + 1;
                arrayList.add(stackTraceElementArr[i]);
                if (i4 >= i3) {
                    break;
                }
                i = i4;
            }
        }
        int i5 = intValue + 1;
        int size = list.size();
        if (i5 < size) {
            while (true) {
                int i6 = i5 + 1;
                arrayList.add(list.get(i5));
                if (i6 >= size) {
                    break;
                }
                i5 = i6;
            }
        }
        return arrayList;
    }

    private final Pair<Integer, Integer> findContinuationStartIndex(int i, StackTraceElement[] stackTraceElementArr, List<StackTraceElement> list) {
        for (int i2 = 0; i2 < 3; i2++) {
            int findIndexOfFrame = INSTANCE.findIndexOfFrame((i - 1) - i2, stackTraceElementArr, list);
            if (findIndexOfFrame != -1) {
                return TuplesKt.m1796to(Integer.valueOf(findIndexOfFrame), Integer.valueOf(i2));
            }
        }
        return TuplesKt.m1796to(-1, 0);
    }

    private final int findIndexOfFrame(int i, StackTraceElement[] stackTraceElementArr, List<StackTraceElement> list) {
        StackTraceElement stackTraceElement = (StackTraceElement) ArraysKt.getOrNull((T[]) stackTraceElementArr, i);
        if (stackTraceElement == null) {
            return -1;
        }
        int i2 = 0;
        for (StackTraceElement next : list) {
            if (Intrinsics.areEqual((Object) next.getFileName(), (Object) stackTraceElement.getFileName()) && Intrinsics.areEqual((Object) next.getClassName(), (Object) stackTraceElement.getClassName()) && Intrinsics.areEqual((Object) next.getMethodName(), (Object) stackTraceElement.getMethodName())) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public final void probeCoroutineResumed$kotlinx_coroutines_core(Continuation<?> continuation) {
        updateState(continuation, DebugCoroutineInfoImplKt.RUNNING);
    }

    public final void probeCoroutineSuspended$kotlinx_coroutines_core(Continuation<?> continuation) {
        updateState(continuation, DebugCoroutineInfoImplKt.SUSPENDED);
    }

    private final void updateState(Continuation<?> continuation, String str) {
        if (isInstalled$kotlinx_coroutines_core()) {
            if (!Intrinsics.areEqual((Object) str, (Object) DebugCoroutineInfoImplKt.RUNNING) || !KotlinVersion.CURRENT.isAtLeast(1, 3, 30)) {
                CoroutineOwner<?> owner = owner(continuation);
                if (owner != null) {
                    updateState(owner, continuation, str);
                    return;
                }
                return;
            }
            CoroutineStackFrame coroutineStackFrame = continuation instanceof CoroutineStackFrame ? (CoroutineStackFrame) continuation : null;
            if (coroutineStackFrame != null) {
                updateRunningState(coroutineStackFrame, str);
            }
        }
    }

    private final void updateRunningState(CoroutineStackFrame coroutineStackFrame, String str) {
        ReentrantReadWriteLock.ReadLock readLock = coroutineStateLock.readLock();
        readLock.lock();
        try {
            DebugProbesImpl debugProbesImpl = INSTANCE;
            if (debugProbesImpl.isInstalled$kotlinx_coroutines_core()) {
                ConcurrentWeakMap<CoroutineStackFrame, DebugCoroutineInfoImpl> concurrentWeakMap = callerInfoCache;
                DebugCoroutineInfoImpl remove = concurrentWeakMap.remove(coroutineStackFrame);
                if (remove == null) {
                    CoroutineOwner<?> owner = debugProbesImpl.owner(coroutineStackFrame);
                    CoroutineStackFrame coroutineStackFrame2 = null;
                    remove = owner == null ? null : owner.info;
                    if (remove == null) {
                        readLock.unlock();
                        return;
                    }
                    CoroutineStackFrame lastObservedFrame$kotlinx_coroutines_core = remove.getLastObservedFrame$kotlinx_coroutines_core();
                    if (lastObservedFrame$kotlinx_coroutines_core != null) {
                        coroutineStackFrame2 = debugProbesImpl.realCaller(lastObservedFrame$kotlinx_coroutines_core);
                    }
                    if (coroutineStackFrame2 != null) {
                        concurrentWeakMap.remove(coroutineStackFrame2);
                    }
                }
                remove.updateState$kotlinx_coroutines_core(str, (Continuation) coroutineStackFrame);
                CoroutineStackFrame realCaller = debugProbesImpl.realCaller(coroutineStackFrame);
                if (realCaller == null) {
                    readLock.unlock();
                    return;
                }
                concurrentWeakMap.put(realCaller, remove);
                Unit unit = Unit.INSTANCE;
                readLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    private final CoroutineStackFrame realCaller(CoroutineStackFrame coroutineStackFrame) {
        do {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame == null) {
                return null;
            }
        } while (coroutineStackFrame.getStackTraceElement() == null);
        return coroutineStackFrame;
    }

    private final void updateState(CoroutineOwner<?> coroutineOwner, Continuation<?> continuation, String str) {
        ReentrantReadWriteLock.ReadLock readLock = coroutineStateLock.readLock();
        readLock.lock();
        try {
            if (INSTANCE.isInstalled$kotlinx_coroutines_core()) {
                coroutineOwner.info.updateState$kotlinx_coroutines_core(str, continuation);
                Unit unit = Unit.INSTANCE;
                readLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    private final CoroutineOwner<?> owner(Continuation<?> continuation) {
        CoroutineStackFrame coroutineStackFrame = continuation instanceof CoroutineStackFrame ? (CoroutineStackFrame) continuation : null;
        if (coroutineStackFrame == null) {
            return null;
        }
        return owner(coroutineStackFrame);
    }

    private final CoroutineOwner<?> owner(CoroutineStackFrame coroutineStackFrame) {
        while (!(coroutineStackFrame instanceof CoroutineOwner)) {
            coroutineStackFrame = coroutineStackFrame.getCallerFrame();
            if (coroutineStackFrame == null) {
                return null;
            }
        }
        return (CoroutineOwner) coroutineStackFrame;
    }

    public final <T> Continuation<T> probeCoroutineCreated$kotlinx_coroutines_core(Continuation<? super T> continuation) {
        StackTraceFrame stackTraceFrame;
        if (!isInstalled$kotlinx_coroutines_core() || owner((Continuation<?>) continuation) != null) {
            return continuation;
        }
        if (enableCreationStackTraces) {
            stackTraceFrame = toStackTraceFrame(sanitizeStackTrace(new Exception()));
        } else {
            stackTraceFrame = null;
            StackTraceFrame stackTraceFrame2 = null;
        }
        return createOwner(continuation, stackTraceFrame);
    }

    private final <T> Continuation<T> createOwner(Continuation<? super T> continuation, StackTraceFrame stackTraceFrame) {
        if (!isInstalled$kotlinx_coroutines_core()) {
            return continuation;
        }
        CoroutineOwner coroutineOwner = new CoroutineOwner(continuation, new DebugCoroutineInfoImpl(continuation.getContext(), stackTraceFrame, sequenceNumber$FU.incrementAndGet(debugProbesImpl$SequenceNumberRefVolatile)), stackTraceFrame);
        ConcurrentWeakMap<CoroutineOwner<?>, Boolean> concurrentWeakMap = capturedCoroutinesMap;
        concurrentWeakMap.put(coroutineOwner, true);
        if (!isInstalled$kotlinx_coroutines_core()) {
            concurrentWeakMap.clear();
        }
        return coroutineOwner;
    }

    /* access modifiers changed from: private */
    public final void probeCoroutineCompleted(CoroutineOwner<?> coroutineOwner) {
        capturedCoroutinesMap.remove(coroutineOwner);
        CoroutineStackFrame lastObservedFrame$kotlinx_coroutines_core = coroutineOwner.info.getLastObservedFrame$kotlinx_coroutines_core();
        CoroutineStackFrame realCaller = lastObservedFrame$kotlinx_coroutines_core == null ? null : realCaller(lastObservedFrame$kotlinx_coroutines_core);
        if (realCaller != null) {
            callerInfoCache.remove(realCaller);
        }
    }

    @Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B%\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\bJ\n\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J\u001e\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0015H\u0016ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0016\u0010\t\u001a\u0004\u0018\u00010\u00038VX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0012\u0010\f\u001a\u00020\rX\u0005¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u0019"}, mo64987d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "T", "Lkotlin/coroutines/Continuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "delegate", "info", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "frame", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "resumeWith", "", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "toString", "", "kotlinx-coroutines-core"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
    /* compiled from: DebugProbesImpl.kt */
    private static final class CoroutineOwner<T> implements Continuation<T>, CoroutineStackFrame {
        public final Continuation<T> delegate;
        private final CoroutineStackFrame frame;
        public final DebugCoroutineInfoImpl info;

        public CoroutineContext getContext() {
            return this.delegate.getContext();
        }

        public CoroutineOwner(Continuation<? super T> continuation, DebugCoroutineInfoImpl debugCoroutineInfoImpl, CoroutineStackFrame coroutineStackFrame) {
            this.delegate = continuation;
            this.info = debugCoroutineInfoImpl;
            this.frame = coroutineStackFrame;
        }

        public CoroutineStackFrame getCallerFrame() {
            CoroutineStackFrame coroutineStackFrame = this.frame;
            if (coroutineStackFrame == null) {
                return null;
            }
            return coroutineStackFrame.getCallerFrame();
        }

        public StackTraceElement getStackTraceElement() {
            CoroutineStackFrame coroutineStackFrame = this.frame;
            if (coroutineStackFrame == null) {
                return null;
            }
            return coroutineStackFrame.getStackTraceElement();
        }

        public void resumeWith(Object obj) {
            DebugProbesImpl.INSTANCE.probeCoroutineCompleted(this);
            this.delegate.resumeWith(obj);
        }

        public String toString() {
            return this.delegate.toString();
        }
    }

    private final <T extends Throwable> List<StackTraceElement> sanitizeStackTrace(T t) {
        int i;
        StackTraceElement[] stackTrace = t.getStackTrace();
        int length = stackTrace.length;
        int i2 = -1;
        int length2 = stackTrace.length - 1;
        while (true) {
            if (length2 < 0) {
                break;
            } else if (Intrinsics.areEqual((Object) stackTrace[length2].getClassName(), (Object) "kotlin.coroutines.jvm.internal.DebugProbesKt")) {
                i2 = length2;
                break;
            } else {
                length2--;
            }
        }
        if (!sanitizeStackTraces) {
            int i3 = length - i;
            ArrayList arrayList = new ArrayList(i3);
            int i4 = 0;
            while (i4 < i3) {
                arrayList.add(i4 == 0 ? StackTraceRecoveryKt.artificialFrame(ARTIFICIAL_FRAME_MESSAGE) : stackTrace[i4 + i]);
                i4++;
            }
            return arrayList;
        }
        ArrayList arrayList2 = new ArrayList((length - i) + 1);
        Collection collection = arrayList2;
        collection.add(StackTraceRecoveryKt.artificialFrame(ARTIFICIAL_FRAME_MESSAGE));
        while (true) {
            i++;
            while (i < length) {
                if (isInternalMethod(stackTrace[i])) {
                    collection.add(stackTrace[i]);
                    int i5 = i + 1;
                    while (i5 < length && isInternalMethod(stackTrace[i5])) {
                        i5++;
                    }
                    int i6 = i5 - 1;
                    int i7 = i6;
                    while (i7 > i && stackTrace[i7].getFileName() == null) {
                        i7--;
                    }
                    if (i7 > i && i7 < i6) {
                        collection.add(stackTrace[i7]);
                    }
                    collection.add(stackTrace[i6]);
                    i = i5;
                } else {
                    collection.add(stackTrace[i]);
                }
            }
            return arrayList2;
        }
    }

    private final boolean isInternalMethod(StackTraceElement stackTraceElement) {
        return StringsKt.startsWith$default(stackTraceElement.getClassName(), "kotlinx.coroutines", false, 2, (Object) null);
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final java.util.List<kotlinx.coroutines.debug.internal.DebugCoroutineInfo> dumpCoroutinesInfo() {
        /*
            r8 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r8 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r8.readLock()
            int r1 = r8.getWriteHoldCount()
            r2 = 0
            if (r1 != 0) goto L_0x0014
            int r1 = r8.getReadHoldCount()
            goto L_0x0015
        L_0x0014:
            r1 = r2
        L_0x0015:
            r3 = r2
        L_0x0016:
            if (r3 >= r1) goto L_0x001e
            r0.unlock()
            int r3 = r3 + 1
            goto L_0x0016
        L_0x001e:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r8 = r8.writeLock()
            r8.lock()
            kotlinx.coroutines.debug.internal.DebugProbesImpl r3 = INSTANCE     // Catch:{ all -> 0x0093 }
            boolean r4 = r3.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x0093 }
            if (r4 == 0) goto L_0x0085
            java.util.Set r3 = r3.getCapturedCoroutines()     // Catch:{ all -> 0x0093 }
            java.lang.Iterable r3 = (java.lang.Iterable) r3     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-14$$inlined$sortedBy$1 r4 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-14$$inlined$sortedBy$1     // Catch:{ all -> 0x0093 }
            r4.<init>()     // Catch:{ all -> 0x0093 }
            java.util.Comparator r4 = (java.util.Comparator) r4     // Catch:{ all -> 0x0093 }
            java.util.List r3 = kotlin.collections.CollectionsKt.sortedWith(r3, r4)     // Catch:{ all -> 0x0093 }
            java.lang.Iterable r3 = (java.lang.Iterable) r3     // Catch:{ all -> 0x0093 }
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ all -> 0x0093 }
            r4.<init>()     // Catch:{ all -> 0x0093 }
            java.util.Collection r4 = (java.util.Collection) r4     // Catch:{ all -> 0x0093 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0093 }
        L_0x004b:
            boolean r5 = r3.hasNext()     // Catch:{ all -> 0x0093 }
            if (r5 == 0) goto L_0x0077
            java.lang.Object r5 = r3.next()     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r5 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r5     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl r6 = INSTANCE     // Catch:{ all -> 0x0093 }
            boolean r6 = r6.isFinished(r5)     // Catch:{ all -> 0x0093 }
            r7 = 0
            if (r6 == 0) goto L_0x0061
            goto L_0x0071
        L_0x0061:
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r6 = r5.info     // Catch:{ all -> 0x0093 }
            kotlin.coroutines.CoroutineContext r6 = r6.getContext()     // Catch:{ all -> 0x0093 }
            if (r6 != 0) goto L_0x006a
            goto L_0x0071
        L_0x006a:
            kotlinx.coroutines.debug.internal.DebugCoroutineInfo r7 = new kotlinx.coroutines.debug.internal.DebugCoroutineInfo     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r5 = r5.info     // Catch:{ all -> 0x0093 }
            r7.<init>(r5, r6)     // Catch:{ all -> 0x0093 }
        L_0x0071:
            if (r7 == 0) goto L_0x004b
            r4.add(r7)     // Catch:{ all -> 0x0093 }
            goto L_0x004b
        L_0x0077:
            java.util.List r4 = (java.util.List) r4     // Catch:{ all -> 0x0093 }
        L_0x0079:
            if (r2 >= r1) goto L_0x0081
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0079
        L_0x0081:
            r8.unlock()
            return r4
        L_0x0085:
            java.lang.String r3 = "Debug probes are not installed"
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0093 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0093 }
            r4.<init>((java.lang.String) r3)     // Catch:{ all -> 0x0093 }
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ all -> 0x0093 }
            throw r4     // Catch:{ all -> 0x0093 }
        L_0x0093:
            r3 = move-exception
        L_0x0094:
            if (r2 >= r1) goto L_0x009c
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0094
        L_0x009c:
            r8.unlock()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesInfo():java.util.List");
    }

    /*  JADX ERROR: StackOverflow in pass: MarkFinallyVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public final java.util.List<kotlinx.coroutines.debug.internal.DebuggerInfo> dumpDebuggerInfo() {
        /*
            r8 = this;
            java.util.concurrent.locks.ReentrantReadWriteLock r8 = coroutineStateLock
            java.util.concurrent.locks.ReentrantReadWriteLock$ReadLock r0 = r8.readLock()
            int r1 = r8.getWriteHoldCount()
            r2 = 0
            if (r1 != 0) goto L_0x0014
            int r1 = r8.getReadHoldCount()
            goto L_0x0015
        L_0x0014:
            r1 = r2
        L_0x0015:
            r3 = r2
        L_0x0016:
            if (r3 >= r1) goto L_0x001e
            r0.unlock()
            int r3 = r3 + 1
            goto L_0x0016
        L_0x001e:
            java.util.concurrent.locks.ReentrantReadWriteLock$WriteLock r8 = r8.writeLock()
            r8.lock()
            kotlinx.coroutines.debug.internal.DebugProbesImpl r3 = INSTANCE     // Catch:{ all -> 0x0093 }
            boolean r4 = r3.isInstalled$kotlinx_coroutines_core()     // Catch:{ all -> 0x0093 }
            if (r4 == 0) goto L_0x0085
            java.util.Set r3 = r3.getCapturedCoroutines()     // Catch:{ all -> 0x0093 }
            java.lang.Iterable r3 = (java.lang.Iterable) r3     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-14$$inlined$sortedBy$1 r4 = new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$lambda-14$$inlined$sortedBy$1     // Catch:{ all -> 0x0093 }
            r4.<init>()     // Catch:{ all -> 0x0093 }
            java.util.Comparator r4 = (java.util.Comparator) r4     // Catch:{ all -> 0x0093 }
            java.util.List r3 = kotlin.collections.CollectionsKt.sortedWith(r3, r4)     // Catch:{ all -> 0x0093 }
            java.lang.Iterable r3 = (java.lang.Iterable) r3     // Catch:{ all -> 0x0093 }
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ all -> 0x0093 }
            r4.<init>()     // Catch:{ all -> 0x0093 }
            java.util.Collection r4 = (java.util.Collection) r4     // Catch:{ all -> 0x0093 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0093 }
        L_0x004b:
            boolean r5 = r3.hasNext()     // Catch:{ all -> 0x0093 }
            if (r5 == 0) goto L_0x0077
            java.lang.Object r5 = r3.next()     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl$CoroutineOwner r5 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) r5     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugProbesImpl r6 = INSTANCE     // Catch:{ all -> 0x0093 }
            boolean r6 = r6.isFinished(r5)     // Catch:{ all -> 0x0093 }
            r7 = 0
            if (r6 == 0) goto L_0x0061
            goto L_0x0071
        L_0x0061:
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r6 = r5.info     // Catch:{ all -> 0x0093 }
            kotlin.coroutines.CoroutineContext r6 = r6.getContext()     // Catch:{ all -> 0x0093 }
            if (r6 != 0) goto L_0x006a
            goto L_0x0071
        L_0x006a:
            kotlinx.coroutines.debug.internal.DebuggerInfo r7 = new kotlinx.coroutines.debug.internal.DebuggerInfo     // Catch:{ all -> 0x0093 }
            kotlinx.coroutines.debug.internal.DebugCoroutineInfoImpl r5 = r5.info     // Catch:{ all -> 0x0093 }
            r7.<init>(r5, r6)     // Catch:{ all -> 0x0093 }
        L_0x0071:
            if (r7 == 0) goto L_0x004b
            r4.add(r7)     // Catch:{ all -> 0x0093 }
            goto L_0x004b
        L_0x0077:
            java.util.List r4 = (java.util.List) r4     // Catch:{ all -> 0x0093 }
        L_0x0079:
            if (r2 >= r1) goto L_0x0081
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0079
        L_0x0081:
            r8.unlock()
            return r4
        L_0x0085:
            java.lang.String r3 = "Debug probes are not installed"
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0093 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0093 }
            r4.<init>((java.lang.String) r3)     // Catch:{ all -> 0x0093 }
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ all -> 0x0093 }
            throw r4     // Catch:{ all -> 0x0093 }
        L_0x0093:
            r3 = move-exception
        L_0x0094:
            if (r2 >= r1) goto L_0x009c
            r0.lock()
            int r2 = r2 + 1
            goto L_0x0094
        L_0x009c:
            r8.unlock()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpDebuggerInfo():java.util.List");
    }

    private final StackTraceFrame toStackTraceFrame(List<StackTraceElement> list) {
        StackTraceFrame stackTraceFrame = null;
        if (!list.isEmpty()) {
            ListIterator<StackTraceElement> listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                stackTraceFrame = new StackTraceFrame(stackTraceFrame, listIterator.previous());
            }
        }
        return stackTraceFrame;
    }
}
