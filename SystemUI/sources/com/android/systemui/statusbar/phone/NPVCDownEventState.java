package com.android.systemui.statusbar.phone;

import com.android.systemui.util.collection.RingBuffer;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0007\u0018\u0000 \u00192\u00020\u0001:\u0002\u0018\u0019Bu\b\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\b\u0012\b\b\u0002\u0010\u000b\u001a\u00020\b\u0012\b\b\u0002\u0010\f\u001a\u00020\b\u0012\b\b\u0002\u0010\r\u001a\u00020\b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\b¢\u0006\u0002\u0010\u0010R\u000e\u0010\r\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R!\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00128FX\u0002¢\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u000b\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001a"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/NPVCDownEventState;", "", "timeStamp", "", "x", "", "y", "qsTouchAboveFalsingThreshold", "", "dozing", "collapsed", "canCollapseOnQQS", "listenForHeadsUp", "allowExpandForSmallExpansion", "touchSlopExceededBeforeDown", "lastEventSynthesized", "(JFFZZZZZZZZ)V", "asStringList", "", "", "getAsStringList", "()Ljava/util/List;", "asStringList$delegate", "Lkotlin/Lazy;", "Buffer", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NPVCDownEventState.kt */
public final class NPVCDownEventState {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final List<String> TABLE_HEADERS = CollectionsKt.listOf("Timestamp", "X", "Y", "QSTouchAboveFalsingThreshold", "Dozing", "Collapsed", "CanCollapseOnQQS", "ListenForHeadsUp", "AllowExpandForSmallExpansion", "TouchSlopExceededBeforeDown", "LastEventSynthesized");
    /* access modifiers changed from: private */
    public boolean allowExpandForSmallExpansion;
    private final Lazy asStringList$delegate;
    /* access modifiers changed from: private */
    public boolean canCollapseOnQQS;
    /* access modifiers changed from: private */
    public boolean collapsed;
    /* access modifiers changed from: private */
    public boolean dozing;
    /* access modifiers changed from: private */
    public boolean lastEventSynthesized;
    /* access modifiers changed from: private */
    public boolean listenForHeadsUp;
    /* access modifiers changed from: private */
    public boolean qsTouchAboveFalsingThreshold;
    /* access modifiers changed from: private */
    public long timeStamp;
    /* access modifiers changed from: private */
    public boolean touchSlopExceededBeforeDown;
    /* access modifiers changed from: private */

    /* renamed from: x */
    public float f386x;
    /* access modifiers changed from: private */

    /* renamed from: y */
    public float f387y;

    private NPVCDownEventState(long j, float f, float f2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8) {
        this.timeStamp = j;
        this.f386x = f;
        this.f387y = f2;
        this.qsTouchAboveFalsingThreshold = z;
        this.dozing = z2;
        this.collapsed = z3;
        this.canCollapseOnQQS = z4;
        this.listenForHeadsUp = z5;
        this.allowExpandForSmallExpansion = z6;
        this.touchSlopExceededBeforeDown = z7;
        this.lastEventSynthesized = z8;
        this.asStringList$delegate = LazyKt.lazy(new NPVCDownEventState$asStringList$2(this));
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    /* synthetic */ NPVCDownEventState(long r14, float r16, float r17, boolean r18, boolean r19, boolean r20, boolean r21, boolean r22, boolean r23, boolean r24, boolean r25, int r26, kotlin.jvm.internal.DefaultConstructorMarker r27) {
        /*
            r13 = this;
            r0 = r26
            r1 = r0 & 1
            if (r1 == 0) goto L_0x0009
            r1 = 0
            goto L_0x000a
        L_0x0009:
            r1 = r14
        L_0x000a:
            r3 = r0 & 2
            r4 = 0
            if (r3 == 0) goto L_0x0011
            r3 = r4
            goto L_0x0013
        L_0x0011:
            r3 = r16
        L_0x0013:
            r5 = r0 & 4
            if (r5 == 0) goto L_0x0018
            goto L_0x001a
        L_0x0018:
            r4 = r17
        L_0x001a:
            r5 = r0 & 8
            r6 = 0
            if (r5 == 0) goto L_0x0021
            r5 = r6
            goto L_0x0023
        L_0x0021:
            r5 = r18
        L_0x0023:
            r7 = r0 & 16
            if (r7 == 0) goto L_0x0029
            r7 = r6
            goto L_0x002b
        L_0x0029:
            r7 = r19
        L_0x002b:
            r8 = r0 & 32
            if (r8 == 0) goto L_0x0031
            r8 = r6
            goto L_0x0033
        L_0x0031:
            r8 = r20
        L_0x0033:
            r9 = r0 & 64
            if (r9 == 0) goto L_0x0039
            r9 = r6
            goto L_0x003b
        L_0x0039:
            r9 = r21
        L_0x003b:
            r10 = r0 & 128(0x80, float:1.794E-43)
            if (r10 == 0) goto L_0x0041
            r10 = r6
            goto L_0x0043
        L_0x0041:
            r10 = r22
        L_0x0043:
            r11 = r0 & 256(0x100, float:3.59E-43)
            if (r11 == 0) goto L_0x0049
            r11 = r6
            goto L_0x004b
        L_0x0049:
            r11 = r23
        L_0x004b:
            r12 = r0 & 512(0x200, float:7.175E-43)
            if (r12 == 0) goto L_0x0051
            r12 = r6
            goto L_0x0053
        L_0x0051:
            r12 = r24
        L_0x0053:
            r0 = r0 & 1024(0x400, float:1.435E-42)
            if (r0 == 0) goto L_0x0058
            goto L_0x005a
        L_0x0058:
            r6 = r25
        L_0x005a:
            r14 = r1
            r16 = r3
            r17 = r4
            r18 = r5
            r19 = r7
            r20 = r8
            r21 = r9
            r22 = r10
            r23 = r11
            r24 = r12
            r25 = r6
            r13.<init>(r14, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NPVCDownEventState.<init>(long, float, float, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final List<String> getAsStringList() {
        return (List) this.asStringList$delegate.getValue();
    }

    @Metadata(mo64986d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J^\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u0010J\u0016\u0010\u0018\u001a\u0012\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\u001a0\u0019j\u0002`\u001b0\u0019R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/NPVCDownEventState$Buffer;", "", "capacity", "", "(I)V", "buffer", "Lcom/android/systemui/util/collection/RingBuffer;", "Lcom/android/systemui/statusbar/phone/NPVCDownEventState;", "insert", "", "timeStamp", "", "x", "", "y", "qsTouchAboveFalsingThreshold", "", "dozing", "collapsed", "canCollapseOnQQS", "listenForHeadsUp", "allowExpandForSmallExpansion", "touchSlopExceededBeforeDown", "lastEventSynthesized", "toList", "", "", "Lcom/android/systemui/dump/Row;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NPVCDownEventState.kt */
    public static final class Buffer {
        private final RingBuffer<NPVCDownEventState> buffer;

        public Buffer(int i) {
            this.buffer = new RingBuffer<>(i, NPVCDownEventState$Buffer$buffer$1.INSTANCE);
        }

        public final void insert(long j, float f, float f2, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8) {
            NPVCDownEventState advance = this.buffer.advance();
            advance.timeStamp = j;
            advance.f386x = f;
            advance.f387y = f2;
            advance.qsTouchAboveFalsingThreshold = z;
            advance.dozing = z2;
            advance.collapsed = z3;
            advance.canCollapseOnQQS = z4;
            advance.listenForHeadsUp = z5;
            advance.allowExpandForSmallExpansion = z6;
            advance.touchSlopExceededBeforeDown = z7;
            advance.lastEventSynthesized = z8;
        }

        public final List<List<String>> toList() {
            return SequencesKt.toList(SequencesKt.map(CollectionsKt.asSequence(this.buffer), NPVCDownEventState$Buffer$toList$1.INSTANCE));
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/NPVCDownEventState$Companion;", "", "()V", "TABLE_HEADERS", "", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NPVCDownEventState.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
