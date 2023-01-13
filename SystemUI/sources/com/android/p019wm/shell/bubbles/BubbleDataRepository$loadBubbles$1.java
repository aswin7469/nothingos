package com.android.p019wm.shell.bubbles;

import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.UserHandle;
import com.android.p019wm.shell.bubbles.storage.BubbleEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@"}, mo65043d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@DebugMetadata(mo65296c = "com.android.wm.shell.bubbles.BubbleDataRepository$loadBubbles$1", mo65297f = "BubbleDataRepository.kt", mo65298i = {}, mo65299l = {}, mo65300m = "invokeSuspend", mo65301n = {}, mo65302s = {})
/* renamed from: com.android.wm.shell.bubbles.BubbleDataRepository$loadBubbles$1 */
/* compiled from: BubbleDataRepository.kt */
final class BubbleDataRepository$loadBubbles$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function1<List<? extends Bubble>, Unit> $cb;
    final /* synthetic */ int $userId;
    int label;
    final /* synthetic */ BubbleDataRepository this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    BubbleDataRepository$loadBubbles$1(BubbleDataRepository bubbleDataRepository, int i, Function1<? super List<? extends Bubble>, Unit> function1, Continuation<? super BubbleDataRepository$loadBubbles$1> continuation) {
        super(2, continuation);
        this.this$0 = bubbleDataRepository;
        this.$userId = i;
        this.$cb = function1;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new BubbleDataRepository$loadBubbles$1(this.this$0, this.$userId, this.$cb, continuation);
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((BubbleDataRepository$loadBubbles$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        Object obj2;
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            List list = this.this$0.persistentRepository.readFromDisk().get(this.$userId);
            if (list == null) {
                return Unit.INSTANCE;
            }
            this.this$0.volatileRepository.addBubbles(this.$userId, list);
            Iterable<BubbleEntity> iterable = list;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (BubbleEntity bubbleEntity : iterable) {
                arrayList.add(new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName()));
            }
            BubbleDataRepository bubbleDataRepository = this.this$0;
            Collection arrayList2 = new ArrayList();
            for (ShortcutKey shortcutKey : CollectionsKt.toSet((List) arrayList)) {
                List<ShortcutInfo> shortcuts = bubbleDataRepository.launcherApps.getShortcuts(new LauncherApps.ShortcutQuery().setPackage(shortcutKey.getPkg()).setQueryFlags(1041), UserHandle.of(shortcutKey.getUserId()));
                if (shortcuts == null) {
                    shortcuts = CollectionsKt.emptyList();
                }
                CollectionsKt.addAll(arrayList2, shortcuts);
            }
            Map linkedHashMap = new LinkedHashMap();
            for (Object next : (List) arrayList2) {
                ShortcutInfo shortcutInfo = (ShortcutInfo) next;
                int userId = shortcutInfo.getUserId();
                String str = shortcutInfo.getPackage();
                Intrinsics.checkNotNullExpressionValue(str, "it.`package`");
                ShortcutKey shortcutKey2 = new ShortcutKey(userId, str);
                Object obj3 = linkedHashMap.get(shortcutKey2);
                if (obj3 == null) {
                    obj3 = new ArrayList();
                    linkedHashMap.put(shortcutKey2, obj3);
                }
                ((List) obj3).add(next);
            }
            BubbleDataRepository bubbleDataRepository2 = this.this$0;
            Collection arrayList3 = new ArrayList();
            for (BubbleEntity bubbleEntity2 : iterable) {
                List list2 = (List) linkedHashMap.get(new ShortcutKey(bubbleEntity2.getUserId(), bubbleEntity2.getPackageName()));
                Bubble bubble = null;
                if (list2 != null) {
                    Iterator it = list2.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            obj2 = null;
                            break;
                        }
                        obj2 = it.next();
                        if (Intrinsics.areEqual((Object) bubbleEntity2.getShortcutId(), (Object) ((ShortcutInfo) obj2).getId())) {
                            break;
                        }
                    }
                    ShortcutInfo shortcutInfo2 = (ShortcutInfo) obj2;
                    if (shortcutInfo2 != null) {
                        bubble = new Bubble(bubbleEntity2.getKey(), shortcutInfo2, bubbleEntity2.getDesiredHeight(), bubbleEntity2.getDesiredHeightResId(), bubbleEntity2.getTitle(), bubbleEntity2.getTaskId(), bubbleEntity2.getLocus(), bubbleDataRepository2.mainExecutor);
                    }
                }
                if (bubble != null) {
                    arrayList3.add(bubble);
                }
            }
            this.this$0.mainExecutor.execute(new BubbleDataRepository$loadBubbles$1$$ExternalSyntheticLambda0(this.$cb, (List) arrayList3));
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeSuspend$lambda-6  reason: not valid java name */
    public static final void m3418invokeSuspend$lambda6(Function1 function1, List list) {
        function1.invoke(list);
    }
}
