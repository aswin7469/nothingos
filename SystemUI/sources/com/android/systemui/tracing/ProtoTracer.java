package com.android.systemui.tracing;

import android.content.Context;
import android.os.SystemClock;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.tracing.FrameProtoTracer;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.tracing.nano.SystemUiTraceEntryProto;
import com.android.systemui.tracing.nano.SystemUiTraceFileProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.google.protobuf.nano.MessageNano;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
/* loaded from: classes2.dex */
public class ProtoTracer implements Dumpable, FrameProtoTracer.ProtoTraceParams<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> {
    private final Context mContext;
    private final FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> mProtoTracer = new FrameProtoTracer<>(this);

    public ProtoTracer(Context context, DumpManager dumpManager) {
        this.mContext = context;
        dumpManager.registerDumpable(ProtoTracer.class.getName(), this);
    }

    @Override // com.android.systemui.shared.tracing.FrameProtoTracer.ProtoTraceParams
    public File getTraceFile() {
        return new File(this.mContext.getFilesDir(), "sysui_trace.pb");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.shared.tracing.FrameProtoTracer.ProtoTraceParams
    /* renamed from: getEncapsulatingTraceProto */
    public SystemUiTraceFileProto mo1360getEncapsulatingTraceProto() {
        return new SystemUiTraceFileProto();
    }

    @Override // com.android.systemui.shared.tracing.FrameProtoTracer.ProtoTraceParams
    public SystemUiTraceEntryProto updateBufferProto(SystemUiTraceEntryProto systemUiTraceEntryProto, ArrayList<ProtoTraceable<SystemUiTraceProto>> arrayList) {
        if (systemUiTraceEntryProto == null) {
            systemUiTraceEntryProto = new SystemUiTraceEntryProto();
        }
        systemUiTraceEntryProto.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        SystemUiTraceProto systemUiTraceProto = systemUiTraceEntryProto.systemUi;
        if (systemUiTraceProto == null) {
            systemUiTraceProto = new SystemUiTraceProto();
        }
        systemUiTraceEntryProto.systemUi = systemUiTraceProto;
        Iterator<ProtoTraceable<SystemUiTraceProto>> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().writeToProto(systemUiTraceEntryProto.systemUi);
        }
        return systemUiTraceEntryProto;
    }

    @Override // com.android.systemui.shared.tracing.FrameProtoTracer.ProtoTraceParams
    public byte[] serializeEncapsulatingProto(SystemUiTraceFileProto systemUiTraceFileProto, Queue<SystemUiTraceEntryProto> queue) {
        systemUiTraceFileProto.magicNumber = 4851032422572317011L;
        systemUiTraceFileProto.entry = (SystemUiTraceEntryProto[]) queue.toArray(new SystemUiTraceEntryProto[0]);
        return MessageNano.toByteArray(systemUiTraceFileProto);
    }

    @Override // com.android.systemui.shared.tracing.FrameProtoTracer.ProtoTraceParams
    public byte[] getProtoBytes(MessageNano messageNano) {
        return MessageNano.toByteArray(messageNano);
    }

    @Override // com.android.systemui.shared.tracing.FrameProtoTracer.ProtoTraceParams
    public int getProtoSize(MessageNano messageNano) {
        return messageNano.getCachedSize();
    }

    public void start() {
        this.mProtoTracer.start();
    }

    public void stop() {
        this.mProtoTracer.stop();
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

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ProtoTracer:");
        printWriter.print("    ");
        printWriter.println("enabled: " + this.mProtoTracer.isEnabled());
        printWriter.print("    ");
        printWriter.println("usagePct: " + this.mProtoTracer.getBufferUsagePct());
        printWriter.print("    ");
        printWriter.println("file: " + getTraceFile());
    }
}
