package com.android.systemui.tracing;

import android.content.Context;
import android.os.SystemClock;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.tracing.FrameProtoTracer;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.tracing.nano.SystemUiTraceEntryProto;
import com.android.systemui.tracing.nano.SystemUiTraceFileProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.google.protobuf.nano.MessageNano;
import java.p026io.File;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import javax.inject.Inject;

@SysUISingleton
public class ProtoTracer implements Dumpable, FrameProtoTracer.ProtoTraceParams<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> {
    private static final long MAGIC_NUMBER_VALUE = 4851032422572317011L;
    private static final String TAG = "ProtoTracer";
    private final Context mContext;
    private final FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> mProtoTracer = new FrameProtoTracer<>(this);

    @Inject
    public ProtoTracer(Context context, DumpManager dumpManager) {
        this.mContext = context;
        dumpManager.registerDumpable(this);
    }

    public File getTraceFile() {
        return new File(this.mContext.getFilesDir(), "sysui_trace.pb");
    }

    public SystemUiTraceFileProto getEncapsulatingTraceProto() {
        return new SystemUiTraceFileProto();
    }

    public SystemUiTraceEntryProto updateBufferProto(SystemUiTraceEntryProto systemUiTraceEntryProto, ArrayList<ProtoTraceable<SystemUiTraceProto>> arrayList) {
        if (systemUiTraceEntryProto == null) {
            systemUiTraceEntryProto = new SystemUiTraceEntryProto();
        }
        systemUiTraceEntryProto.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        systemUiTraceEntryProto.systemUi = systemUiTraceEntryProto.systemUi != null ? systemUiTraceEntryProto.systemUi : new SystemUiTraceProto();
        Iterator<ProtoTraceable<SystemUiTraceProto>> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().writeToProto(systemUiTraceEntryProto.systemUi);
        }
        return systemUiTraceEntryProto;
    }

    public byte[] serializeEncapsulatingProto(SystemUiTraceFileProto systemUiTraceFileProto, Queue<SystemUiTraceEntryProto> queue) {
        systemUiTraceFileProto.magicNumber = MAGIC_NUMBER_VALUE;
        systemUiTraceFileProto.entry = (SystemUiTraceEntryProto[]) queue.toArray((T[]) new SystemUiTraceEntryProto[0]);
        return MessageNano.toByteArray(systemUiTraceFileProto);
    }

    public byte[] getProtoBytes(MessageNano messageNano) {
        return MessageNano.toByteArray(messageNano);
    }

    public int getProtoSize(MessageNano messageNano) {
        return messageNano.getCachedSize();
    }

    public void start() {
        this.mProtoTracer.start();
    }

    public void stop() {
        this.mProtoTracer.stop();
    }

    public boolean isEnabled() {
        return this.mProtoTracer.isEnabled();
    }

    public void add(ProtoTraceable<SystemUiTraceProto> protoTraceable) {
        this.mProtoTracer.add(protoTraceable);
    }

    public void remove(ProtoTraceable<SystemUiTraceProto> protoTraceable) {
        this.mProtoTracer.remove(protoTraceable);
    }

    public void scheduleFrameUpdate() {
        this.mProtoTracer.scheduleFrameUpdate();
    }

    public void update() {
        this.mProtoTracer.update();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("ProtoTracer:");
        printWriter.print("    ");
        printWriter.println("enabled: " + this.mProtoTracer.isEnabled());
        printWriter.print("    ");
        printWriter.println("usagePct: " + this.mProtoTracer.getBufferUsagePct());
        printWriter.print("    ");
        printWriter.println("file: " + getTraceFile());
    }
}
