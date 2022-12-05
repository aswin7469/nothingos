package com.android.wm.shell.bubbles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.LauncherApps;
import com.android.wm.shell.bubbles.storage.BubbleEntity;
import com.android.wm.shell.bubbles.storage.BubblePersistentRepository;
import com.android.wm.shell.bubbles.storage.BubbleVolatileRepository;
import com.android.wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: BubbleDataRepository.kt */
/* loaded from: classes2.dex */
public final class BubbleDataRepository {
    @NotNull
    private final CoroutineScope ioScope = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
    @Nullable
    private Job job;
    @NotNull
    private final LauncherApps launcherApps;
    @NotNull
    private final ShellExecutor mainExecutor;
    @NotNull
    private final BubblePersistentRepository persistentRepository;
    @NotNull
    private final BubbleVolatileRepository volatileRepository;

    public BubbleDataRepository(@NotNull Context context, @NotNull LauncherApps launcherApps, @NotNull ShellExecutor mainExecutor) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        Intrinsics.checkNotNullParameter(mainExecutor, "mainExecutor");
        this.launcherApps = launcherApps;
        this.mainExecutor = mainExecutor;
        this.volatileRepository = new BubbleVolatileRepository(launcherApps);
        this.persistentRepository = new BubblePersistentRepository(context);
        Dispatchers dispatchers = Dispatchers.INSTANCE;
    }

    public final void addBubble(int i, @NotNull Bubble bubble) {
        List<? extends Bubble> listOf;
        Intrinsics.checkNotNullParameter(bubble, "bubble");
        listOf = CollectionsKt__CollectionsJVMKt.listOf(bubble);
        addBubbles(i, listOf);
    }

    public final void addBubbles(int i, @NotNull List<? extends Bubble> bubbles) {
        Intrinsics.checkNotNullParameter(bubbles, "bubbles");
        List<BubbleEntity> transform = transform(bubbles);
        this.volatileRepository.addBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    public final void removeBubbles(int i, @NotNull List<? extends Bubble> bubbles) {
        Intrinsics.checkNotNullParameter(bubbles, "bubbles");
        List<BubbleEntity> transform = transform(bubbles);
        this.volatileRepository.removeBubbles(i, transform);
        if (!transform.isEmpty()) {
            persistToDisk();
        }
    }

    private final void persistToDisk() {
        Job launch$default;
        launch$default = BuildersKt__Builders_commonKt.launch$default(this.ioScope, null, null, new BubbleDataRepository$persistToDisk$1(this.job, this, null), 3, null);
        this.job = launch$default;
    }

    @SuppressLint({"WrongConstant"})
    @NotNull
    public final Job loadBubbles(int i, @NotNull Function1<? super List<? extends Bubble>, Unit> cb) {
        Job launch$default;
        Intrinsics.checkNotNullParameter(cb, "cb");
        launch$default = BuildersKt__Builders_commonKt.launch$default(this.ioScope, null, null, new BubbleDataRepository$loadBubbles$1(this, i, cb, null), 3, null);
        return launch$default;
    }

    private final List<BubbleEntity> transform(List<? extends Bubble> list) {
        ArrayList arrayList = new ArrayList();
        for (Bubble bubble : list) {
            int identifier = bubble.getUser().getIdentifier();
            String packageName = bubble.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName, "b.packageName");
            String metadataShortcutId = bubble.getMetadataShortcutId();
            BubbleEntity bubbleEntity = null;
            if (metadataShortcutId != null) {
                String key = bubble.getKey();
                Intrinsics.checkNotNullExpressionValue(key, "b.key");
                int rawDesiredHeight = bubble.getRawDesiredHeight();
                int rawDesiredHeightResId = bubble.getRawDesiredHeightResId();
                String title = bubble.getTitle();
                int taskId = bubble.getTaskId();
                LocusId locusId = bubble.getLocusId();
                bubbleEntity = new BubbleEntity(identifier, packageName, metadataShortcutId, key, rawDesiredHeight, rawDesiredHeightResId, title, taskId, locusId == null ? null : locusId.getId());
            }
            if (bubbleEntity != null) {
                arrayList.add(bubbleEntity);
            }
        }
        return arrayList;
    }
}
