package okhttp3.internal.concurrent;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

@Metadata(mo14006bv = {1, 0, 3}, mo14007d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo14008d2 = {"okhttp3/internal/concurrent/TaskQueue$execute$1", "Lokhttp3/internal/concurrent/Task;", "runOnce", "", "okhttp"}, mo14009k = 1, mo14010mv = {1, 4, 0})
/* compiled from: TaskQueue.kt */
public final class TaskQueue$execute$1 extends Task {
    final /* synthetic */ Function0 $block;
    final /* synthetic */ boolean $cancelable;
    final /* synthetic */ String $name;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public TaskQueue$execute$1(Function0 function0, String str, boolean z, String str2, boolean z2) {
        super(str2, z2);
        this.$block = function0;
        this.$name = str;
        this.$cancelable = z;
    }

    public long runOnce() {
        this.$block.invoke();
        return -1;
    }
}
