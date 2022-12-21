package com.android.p019wm.shell.bubbles;

import android.content.Context;
import android.content.LocusId;
import android.content.pm.LauncherApps;
import android.content.pm.UserInfo;
import com.android.p019wm.shell.bubbles.storage.BubbleEntity;
import com.android.p019wm.shell.bubbles.storage.BubblePersistentRepository;
import com.android.p019wm.shell.bubbles.storage.BubbleVolatileRepository;
import com.android.p019wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;

@Metadata(mo64986d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u001c\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00160\u0019J*\u0010\u001a\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u00142\u0018\u0010\u001b\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u0019\u0012\u0004\u0012\u00020\u00120\u001cH\u0007J\b\u0010\u001d\u001a\u00020\u0012H\u0002J\u001c\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00160\u0019J\u0016\u0010\u001f\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\u0014J\u0014\u0010!\u001a\u00020\u00122\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0019J\u001c\u0010$\u001a\b\u0012\u0004\u0012\u00020%0\u00192\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00160\u0019H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000¨\u0006&"}, mo64987d2 = {"Lcom/android/wm/shell/bubbles/BubbleDataRepository;", "", "context", "Landroid/content/Context;", "launcherApps", "Landroid/content/pm/LauncherApps;", "mainExecutor", "Lcom/android/wm/shell/common/ShellExecutor;", "(Landroid/content/Context;Landroid/content/pm/LauncherApps;Lcom/android/wm/shell/common/ShellExecutor;)V", "ioScope", "Lkotlinx/coroutines/CoroutineScope;", "job", "Lkotlinx/coroutines/Job;", "persistentRepository", "Lcom/android/wm/shell/bubbles/storage/BubblePersistentRepository;", "volatileRepository", "Lcom/android/wm/shell/bubbles/storage/BubbleVolatileRepository;", "addBubble", "", "userId", "", "bubble", "Lcom/android/wm/shell/bubbles/Bubble;", "addBubbles", "bubbles", "", "loadBubbles", "cb", "Lkotlin/Function1;", "persistToDisk", "removeBubbles", "removeBubblesForUser", "parentId", "sanitizeBubbles", "users", "Landroid/content/pm/UserInfo;", "transform", "Lcom/android/wm/shell/bubbles/storage/BubbleEntity;", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.bubbles.BubbleDataRepository */
/* compiled from: BubbleDataRepository.kt */
public final class BubbleDataRepository {
    private final CoroutineScope ioScope = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
    private Job job;
    /* access modifiers changed from: private */
    public final LauncherApps launcherApps;
    /* access modifiers changed from: private */
    public final ShellExecutor mainExecutor;
    /* access modifiers changed from: private */
    public final BubblePersistentRepository persistentRepository;
    /* access modifiers changed from: private */
    public final BubbleVolatileRepository volatileRepository;

    public BubbleDataRepository(Context context, LauncherApps launcherApps2, ShellExecutor shellExecutor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(launcherApps2, "launcherApps");
        Intrinsics.checkNotNullParameter(shellExecutor, "mainExecutor");
        this.launcherApps = launcherApps2;
        this.mainExecutor = shellExecutor;
        this.volatileRepository = new BubbleVolatileRepository(launcherApps2);
        this.persistentRepository = new BubblePersistentRepository(context);
    }

    public final void addBubble(int i, Bubble bubble) {
        Intrinsics.checkNotNullParameter(bubble, "bubble");
        addBubbles(i, CollectionsKt.listOf(bubble));
    }

    public final void addBubbles(int i, List<? extends Bubble> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        List<BubbleEntity> transform = transform(list);
        this.volatileRepository.addBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    public final void removeBubbles(int i, List<? extends Bubble> list) {
        Intrinsics.checkNotNullParameter(list, "bubbles");
        List<BubbleEntity> transform = transform(list);
        this.volatileRepository.removeBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    public final void removeBubblesForUser(int i, int i2) {
        if (this.volatileRepository.removeBubblesForUser(i, i2)) {
            persistToDisk();
        }
    }

    public final void sanitizeBubbles(List<? extends UserInfo> list) {
        Intrinsics.checkNotNullParameter(list, "users");
        Iterable<UserInfo> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (UserInfo userInfo : iterable) {
            arrayList.add(Integer.valueOf(userInfo.id));
        }
        if (this.volatileRepository.sanitizeBubbles((List) arrayList)) {
            persistToDisk();
        }
    }

    private final List<BubbleEntity> transform(List<? extends Bubble> list) {
        Collection arrayList = new ArrayList();
        for (Bubble bubble : list) {
            int identifier = bubble.getUser().getIdentifier();
            String packageName = bubble.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName, "b.packageName");
            String metadataShortcutId = bubble.getMetadataShortcutId();
            BubbleEntity bubbleEntity = null;
            if (metadataShortcutId != null) {
                Intrinsics.checkNotNullExpressionValue(metadataShortcutId, "b.metadataShortcutId ?: return@mapNotNull null");
                String key = bubble.getKey();
                Intrinsics.checkNotNullExpressionValue(key, "b.key");
                int rawDesiredHeight = bubble.getRawDesiredHeight();
                int rawDesiredHeightResId = bubble.getRawDesiredHeightResId();
                String title = bubble.getTitle();
                int taskId = bubble.getTaskId();
                LocusId locusId = bubble.getLocusId();
                bubbleEntity = new BubbleEntity(identifier, packageName, metadataShortcutId, key, rawDesiredHeight, rawDesiredHeightResId, title, taskId, locusId != null ? locusId.getId() : null);
            }
            if (bubbleEntity != null) {
                arrayList.add(bubbleEntity);
            }
        }
        return (List) arrayList;
    }

    private final void persistToDisk() {
        this.job = BuildersKt__Builders_commonKt.launch$default(this.ioScope, (CoroutineContext) null, (CoroutineStart) null, new BubbleDataRepository$persistToDisk$1(this.job, this, (Continuation<? super BubbleDataRepository$persistToDisk$1>) null), 3, (Object) null);
    }

    public final Job loadBubbles(int i, Function1<? super List<? extends Bubble>, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "cb");
        return BuildersKt__Builders_commonKt.launch$default(this.ioScope, (CoroutineContext) null, (CoroutineStart) null, new BubbleDataRepository$loadBubbles$1(this, i, function1, (Continuation<? super BubbleDataRepository$loadBubbles$1>) null), 3, (Object) null);
    }
}
