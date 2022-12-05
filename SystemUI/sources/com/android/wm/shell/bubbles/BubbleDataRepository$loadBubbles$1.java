package com.android.wm.shell.bubbles;

import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.os.UserHandle;
import com.android.wm.shell.bubbles.storage.BubbleEntity;
import com.android.wm.shell.bubbles.storage.BubblePersistentRepository;
import com.android.wm.shell.bubbles.storage.BubbleVolatileRepository;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BubbleDataRepository.kt */
@DebugMetadata(c = "com.android.wm.shell.bubbles.BubbleDataRepository$loadBubbles$1", f = "BubbleDataRepository.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class BubbleDataRepository$loadBubbles$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function1<List<? extends Bubble>, Unit> $cb;
    final /* synthetic */ int $userId;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ BubbleDataRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public BubbleDataRepository$loadBubbles$1(BubbleDataRepository bubbleDataRepository, int i, Function1<? super List<? extends Bubble>, Unit> function1, Continuation<? super BubbleDataRepository$loadBubbles$1> continuation) {
        super(2, continuation);
        this.this$0 = bubbleDataRepository;
        this.$userId = i;
        this.$cb = function1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        BubbleDataRepository$loadBubbles$1 bubbleDataRepository$loadBubbles$1 = new BubbleDataRepository$loadBubbles$1(this.this$0, this.$userId, this.$cb, continuation);
        bubbleDataRepository$loadBubbles$1.p$ = (CoroutineScope) obj;
        return bubbleDataRepository$loadBubbles$1;
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Object mo1950invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
        return ((BubbleDataRepository$loadBubbles$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        BubblePersistentRepository bubblePersistentRepository;
        BubbleVolatileRepository bubbleVolatileRepository;
        int collectionSizeOrDefault;
        Set<ShortcutKey> set;
        ShellExecutor shellExecutor;
        Object obj2;
        ShellExecutor shellExecutor2;
        LauncherApps launcherApps;
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            bubblePersistentRepository = this.this$0.persistentRepository;
            List<BubbleEntity> list = bubblePersistentRepository.readFromDisk().get(this.$userId);
            if (list == null) {
                return Unit.INSTANCE;
            }
            bubbleVolatileRepository = this.this$0.volatileRepository;
            bubbleVolatileRepository.addBubbles(this.$userId, list);
            collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
            ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
            for (BubbleEntity bubbleEntity : list) {
                arrayList.add(new ShortcutKey(bubbleEntity.getUserId(), bubbleEntity.getPackageName()));
            }
            set = CollectionsKt___CollectionsKt.toSet(arrayList);
            BubbleDataRepository bubbleDataRepository = this.this$0;
            ArrayList arrayList2 = new ArrayList();
            for (ShortcutKey shortcutKey : set) {
                launcherApps = bubbleDataRepository.launcherApps;
                List<ShortcutInfo> shortcuts = launcherApps.getShortcuts(new LauncherApps.ShortcutQuery().setPackage(shortcutKey.getPkg()).setQueryFlags(1041), UserHandle.of(shortcutKey.getUserId()));
                if (shortcuts == null) {
                    shortcuts = CollectionsKt__CollectionsKt.emptyList();
                }
                CollectionsKt__MutableCollectionsKt.addAll(arrayList2, shortcuts);
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Object obj3 : arrayList2) {
                ShortcutInfo shortcutInfo = (ShortcutInfo) obj3;
                int userId = shortcutInfo.getUserId();
                String str = shortcutInfo.getPackage();
                Intrinsics.checkNotNullExpressionValue(str, "it.`package`");
                ShortcutKey shortcutKey2 = new ShortcutKey(userId, str);
                Object obj4 = linkedHashMap.get(shortcutKey2);
                if (obj4 == null) {
                    obj4 = new ArrayList();
                    linkedHashMap.put(shortcutKey2, obj4);
                }
                ((List) obj4).add(obj3);
            }
            BubbleDataRepository bubbleDataRepository2 = this.this$0;
            final ArrayList arrayList3 = new ArrayList();
            for (BubbleEntity bubbleEntity2 : list) {
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
                        if (Boxing.boxBoolean(Intrinsics.areEqual(bubbleEntity2.getShortcutId(), ((ShortcutInfo) obj2).getId())).booleanValue()) {
                            break;
                        }
                    }
                    ShortcutInfo shortcutInfo2 = (ShortcutInfo) obj2;
                    if (shortcutInfo2 != null) {
                        String key = bubbleEntity2.getKey();
                        int desiredHeight = bubbleEntity2.getDesiredHeight();
                        int desiredHeightResId = bubbleEntity2.getDesiredHeightResId();
                        String title = bubbleEntity2.getTitle();
                        int taskId = bubbleEntity2.getTaskId();
                        String locus = bubbleEntity2.getLocus();
                        shellExecutor2 = bubbleDataRepository2.mainExecutor;
                        bubble = new Bubble(key, shortcutInfo2, desiredHeight, desiredHeightResId, title, taskId, locus, shellExecutor2);
                    }
                }
                if (bubble != null) {
                    arrayList3.add(bubble);
                }
            }
            shellExecutor = this.this$0.mainExecutor;
            final Function1<List<? extends Bubble>, Unit> function1 = this.$cb;
            shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.bubbles.BubbleDataRepository$loadBubbles$1.1
                @Override // java.lang.Runnable
                public final void run() {
                    function1.mo1949invoke(arrayList3);
                }
            });
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
