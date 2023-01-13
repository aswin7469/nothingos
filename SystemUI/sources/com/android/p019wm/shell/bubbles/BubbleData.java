package com.android.p019wm.shell.bubbles;

import android.app.PendingIntent;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.ShortcutInfo;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.bubbles.BubbleLogger;
import com.android.p019wm.shell.bubbles.Bubbles;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/* renamed from: com.android.wm.shell.bubbles.BubbleData */
public class BubbleData {
    private static final Comparator<Bubble> BUBBLES_BY_SORT_KEY_DESCENDING = Comparator.comparing(new BubbleData$$ExternalSyntheticLambda5()).reversed();
    private static final String TAG = "Bubbles";
    private Bubbles.BubbleMetadataFlagListener mBubbleMetadataFlagListener;
    private final List<Bubble> mBubbles;
    private Bubbles.PendingIntentCanceledListener mCancelledListener;
    private final Context mContext;
    private int mCurrentUserId;
    private boolean mExpanded;
    private Listener mListener;
    private BubbleLogger mLogger;
    private final Executor mMainExecutor;
    private int mMaxBubbles;
    private int mMaxOverflowBubbles;
    private boolean mNeedsTrimming;
    private final BubbleOverflow mOverflow;
    private final List<Bubble> mOverflowBubbles;
    private final HashMap<String, Bubble> mPendingBubbles;
    private final BubblePositioner mPositioner;
    private BubbleViewProvider mSelectedBubble;
    private boolean mShowingOverflow;
    private Update mStateChange;
    private final ArrayMap<LocusId, Bubble> mSuppressedBubbles = new ArrayMap<>();
    private HashMap<String, String> mSuppressedGroupKeys = new HashMap<>();
    private TimeSource mTimeSource = new BubbleData$$ExternalSyntheticLambda9();
    private final ArraySet<LocusId> mVisibleLocusIds = new ArraySet<>();

    /* renamed from: com.android.wm.shell.bubbles.BubbleData$Listener */
    interface Listener {
        void applyUpdate(Update update);
    }

    /* renamed from: com.android.wm.shell.bubbles.BubbleData$TimeSource */
    interface TimeSource {
        long currentTimeMillis();
    }

    /* renamed from: com.android.wm.shell.bubbles.BubbleData$Update */
    static final class Update {
        Bubble addedBubble;
        Bubble addedOverflowBubble;
        final List<Bubble> bubbles;
        boolean expanded;
        boolean expandedChanged;
        boolean orderChanged;
        final List<Bubble> overflowBubbles;
        final List<Pair<Bubble, Integer>> removedBubbles;
        Bubble removedOverflowBubble;
        BubbleViewProvider selectedBubble;
        boolean selectionChanged;
        Bubble suppressedBubble;
        boolean suppressedSummaryChanged;
        String suppressedSummaryGroup;
        Bubble unsuppressedBubble;
        Bubble updatedBubble;

        private Update(List<Bubble> list, List<Bubble> list2) {
            this.removedBubbles = new ArrayList();
            this.bubbles = Collections.unmodifiableList(list);
            this.overflowBubbles = Collections.unmodifiableList(list2);
        }

        /* access modifiers changed from: package-private */
        public boolean anythingChanged() {
            return (!this.expandedChanged && !this.selectionChanged && this.addedBubble == null && this.updatedBubble == null && this.removedBubbles.isEmpty() && this.addedOverflowBubble == null && this.removedOverflowBubble == null && !this.orderChanged && this.suppressedBubble == null && this.unsuppressedBubble == null && !this.suppressedSummaryChanged && this.suppressedSummaryGroup == null) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public void bubbleRemoved(Bubble bubble, int i) {
            this.removedBubbles.add(new Pair(bubble, Integer.valueOf(i)));
        }
    }

    public BubbleData(Context context, BubbleLogger bubbleLogger, BubblePositioner bubblePositioner, Executor executor) {
        this.mContext = context;
        this.mLogger = bubbleLogger;
        this.mPositioner = bubblePositioner;
        this.mMainExecutor = executor;
        this.mOverflow = new BubbleOverflow(context, bubblePositioner);
        ArrayList arrayList = new ArrayList();
        this.mBubbles = arrayList;
        ArrayList arrayList2 = new ArrayList();
        this.mOverflowBubbles = arrayList2;
        this.mPendingBubbles = new HashMap<>();
        this.mStateChange = new Update(arrayList, arrayList2);
        this.mMaxBubbles = bubblePositioner.getMaxBubbles();
        this.mMaxOverflowBubbles = context.getResources().getInteger(C3353R.integer.bubbles_max_overflow);
    }

    public void setSuppressionChangedListener(Bubbles.BubbleMetadataFlagListener bubbleMetadataFlagListener) {
        this.mBubbleMetadataFlagListener = bubbleMetadataFlagListener;
    }

    public void setPendingIntentCancelledListener(Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener) {
        this.mCancelledListener = pendingIntentCanceledListener;
    }

    public void onMaxBubblesChanged() {
        this.mMaxBubbles = this.mPositioner.getMaxBubbles();
        if (!this.mExpanded) {
            trim();
            dispatchPendingChanges();
            return;
        }
        this.mNeedsTrimming = true;
    }

    public boolean hasBubbles() {
        return !this.mBubbles.isEmpty();
    }

    public boolean hasOverflowBubbles() {
        return !this.mOverflowBubbles.isEmpty();
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    public boolean hasAnyBubbleWithKey(String str) {
        return hasBubbleInStackWithKey(str) || hasOverflowBubbleWithKey(str) || hasSuppressedBubbleWithKey(str);
    }

    public boolean hasBubbleInStackWithKey(String str) {
        return getBubbleInStackWithKey(str) != null;
    }

    public boolean hasOverflowBubbleWithKey(String str) {
        return getOverflowBubbleWithKey(str) != null;
    }

    public boolean hasSuppressedBubbleWithKey(String str) {
        return this.mSuppressedBubbles.values().stream().anyMatch(new BubbleData$$ExternalSyntheticLambda6(str));
    }

    public boolean isSuppressedWithLocusId(LocusId locusId) {
        return this.mSuppressedBubbles.get(locusId) != null;
    }

    public BubbleViewProvider getSelectedBubble() {
        return this.mSelectedBubble;
    }

    public BubbleOverflow getOverflow() {
        return this.mOverflow;
    }

    public List<Bubble> getActiveBubbles() {
        return Collections.unmodifiableList(this.mBubbles);
    }

    public void setExpanded(boolean z) {
        setExpandedInternal(z);
        dispatchPendingChanges();
    }

    public void setSelectedBubble(BubbleViewProvider bubbleViewProvider) {
        setSelectedBubbleInternal(bubbleViewProvider);
        dispatchPendingChanges();
    }

    /* access modifiers changed from: package-private */
    public void setShowingOverflow(boolean z) {
        this.mShowingOverflow = z;
    }

    /* access modifiers changed from: package-private */
    public boolean isShowingOverflow() {
        return this.mShowingOverflow && (isExpanded() || this.mPositioner.showingInTaskbar());
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.p019wm.shell.bubbles.Bubble getOrCreateBubble(com.android.p019wm.shell.bubbles.BubbleEntry r5, com.android.p019wm.shell.bubbles.Bubble r6) {
        /*
            r4 = this;
            if (r6 == 0) goto L_0x0007
            java.lang.String r0 = r6.getKey()
            goto L_0x000b
        L_0x0007:
            java.lang.String r0 = r5.getKey()
        L_0x000b:
            com.android.wm.shell.bubbles.Bubble r1 = r4.getBubbleInStackWithKey(r0)
            if (r1 != 0) goto L_0x003c
            com.android.wm.shell.bubbles.Bubble r1 = r4.getOverflowBubbleWithKey(r0)
            if (r1 == 0) goto L_0x001d
            java.util.List<com.android.wm.shell.bubbles.Bubble> r6 = r4.mOverflowBubbles
            r6.remove((java.lang.Object) r1)
            goto L_0x003c
        L_0x001d:
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r1 = r4.mPendingBubbles
            boolean r1 = r1.containsKey(r0)
            if (r1 == 0) goto L_0x002e
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r6 = r4.mPendingBubbles
            java.lang.Object r6 = r6.get(r0)
            com.android.wm.shell.bubbles.Bubble r6 = (com.android.p019wm.shell.bubbles.Bubble) r6
            goto L_0x003d
        L_0x002e:
            if (r5 == 0) goto L_0x003d
            com.android.wm.shell.bubbles.Bubble r6 = new com.android.wm.shell.bubbles.Bubble
            com.android.wm.shell.bubbles.Bubbles$BubbleMetadataFlagListener r1 = r4.mBubbleMetadataFlagListener
            com.android.wm.shell.bubbles.Bubbles$PendingIntentCanceledListener r2 = r4.mCancelledListener
            java.util.concurrent.Executor r3 = r4.mMainExecutor
            r6.<init>(r5, r1, r2, r3)
            goto L_0x003d
        L_0x003c:
            r6 = r1
        L_0x003d:
            if (r5 == 0) goto L_0x0042
            r6.setEntry(r5)
        L_0x0042:
            java.util.HashMap<java.lang.String, com.android.wm.shell.bubbles.Bubble> r4 = r4.mPendingBubbles
            r4.put(r0, r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.bubbles.BubbleData.getOrCreateBubble(com.android.wm.shell.bubbles.BubbleEntry, com.android.wm.shell.bubbles.Bubble):com.android.wm.shell.bubbles.Bubble");
    }

    /* access modifiers changed from: package-private */
    public void notificationEntryUpdated(Bubble bubble, boolean z, boolean z2) {
        this.mPendingBubbles.remove(bubble.getKey());
        Bubble bubbleInStackWithKey = getBubbleInStackWithKey(bubble.getKey());
        boolean z3 = z | (!bubble.isTextChanged());
        if (bubbleInStackWithKey == null) {
            bubble.setSuppressFlyout(z3);
            bubble.markUpdatedAt(this.mTimeSource.currentTimeMillis());
            doAdd(bubble);
            trim();
        } else {
            bubble.setSuppressFlyout(z3);
            doUpdate(bubble, !z3);
        }
        boolean z4 = false;
        if (bubble.shouldAutoExpand()) {
            bubble.setShouldAutoExpand(false);
            setSelectedBubbleInternal(bubble);
            if (!this.mExpanded) {
                setExpandedInternal(true);
            }
        }
        boolean z5 = this.mExpanded && this.mSelectedBubble == bubble;
        if (z5 || !z2 || !bubble.showInShade()) {
            z4 = true;
        }
        bubble.setSuppressNotification(z4);
        bubble.setShowDot(!z5);
        LocusId locusId = bubble.getLocusId();
        if (locusId != null) {
            boolean containsKey = this.mSuppressedBubbles.containsKey(locusId);
            if (containsKey && (!bubble.isSuppressed() || !bubble.isSuppressable())) {
                this.mSuppressedBubbles.remove(locusId);
                doUnsuppress(bubble);
            } else if (!containsKey && (bubble.isSuppressed() || (bubble.isSuppressable() && this.mVisibleLocusIds.contains(locusId)))) {
                this.mSuppressedBubbles.put(locusId, bubble);
                doSuppress(bubble);
            }
        }
        dispatchPendingChanges();
    }

    public void dismissBubbleWithKey(String str, int i) {
        doRemove(str, i);
        dispatchPendingChanges();
    }

    /* access modifiers changed from: package-private */
    public void addSummaryToSuppress(String str, String str2) {
        this.mSuppressedGroupKeys.put(str, str2);
        this.mStateChange.suppressedSummaryChanged = true;
        this.mStateChange.suppressedSummaryGroup = str;
        dispatchPendingChanges();
    }

    /* access modifiers changed from: package-private */
    public String getSummaryKey(String str) {
        return this.mSuppressedGroupKeys.get(str);
    }

    /* access modifiers changed from: package-private */
    public void removeSuppressedSummary(String str) {
        this.mSuppressedGroupKeys.remove(str);
        this.mStateChange.suppressedSummaryChanged = true;
        this.mStateChange.suppressedSummaryGroup = str;
        dispatchPendingChanges();
    }

    public boolean isSummarySuppressed(String str) {
        return this.mSuppressedGroupKeys.containsKey(str);
    }

    public void removeBubblesWithInvalidShortcuts(String str, List<ShortcutInfo> list, int i) {
        HashSet hashSet = new HashSet();
        for (ShortcutInfo id : list) {
            hashSet.add(id.getId());
        }
        BubbleData$$ExternalSyntheticLambda7 bubbleData$$ExternalSyntheticLambda7 = new BubbleData$$ExternalSyntheticLambda7(str, hashSet);
        BubbleData$$ExternalSyntheticLambda8 bubbleData$$ExternalSyntheticLambda8 = new BubbleData$$ExternalSyntheticLambda8(this, i);
        performActionOnBubblesMatching(getBubbles(), bubbleData$$ExternalSyntheticLambda7, bubbleData$$ExternalSyntheticLambda8);
        performActionOnBubblesMatching(getOverflowBubbles(), bubbleData$$ExternalSyntheticLambda7, bubbleData$$ExternalSyntheticLambda8);
    }

    static /* synthetic */ boolean lambda$removeBubblesWithInvalidShortcuts$1(String str, Set set, Bubble bubble) {
        boolean equals = str.equals(bubble.getPackageName());
        boolean hasMetadataShortcutId = bubble.hasMetadataShortcutId();
        if (!equals || !hasMetadataShortcutId) {
            return false;
        }
        boolean z = bubble.hasMetadataShortcutId() && bubble.getShortcutInfo() != null && bubble.getShortcutInfo().isEnabled() && set.contains(bubble.getShortcutInfo().getId());
        if (!equals || z) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeBubblesWithInvalidShortcuts$2$com-android-wm-shell-bubbles-BubbleData */
    public /* synthetic */ void mo48493x12646c12(int i, Bubble bubble) {
        dismissBubbleWithKey(bubble.getKey(), i);
    }

    public void removeBubblesWithPackageName(String str, int i) {
        BubbleData$$ExternalSyntheticLambda10 bubbleData$$ExternalSyntheticLambda10 = new BubbleData$$ExternalSyntheticLambda10(str);
        BubbleData$$ExternalSyntheticLambda11 bubbleData$$ExternalSyntheticLambda11 = new BubbleData$$ExternalSyntheticLambda11(this, i);
        performActionOnBubblesMatching(getBubbles(), bubbleData$$ExternalSyntheticLambda10, bubbleData$$ExternalSyntheticLambda11);
        performActionOnBubblesMatching(getOverflowBubbles(), bubbleData$$ExternalSyntheticLambda10, bubbleData$$ExternalSyntheticLambda11);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeBubblesWithPackageName$4$com-android-wm-shell-bubbles-BubbleData */
    public /* synthetic */ void mo48494x60f571b7(int i, Bubble bubble) {
        dismissBubbleWithKey(bubble.getKey(), i);
    }

    public void removeBubblesForUser(int i) {
        List<Bubble> filterAllBubbles = filterAllBubbles(new BubbleData$$ExternalSyntheticLambda4(i));
        for (Bubble key : filterAllBubbles) {
            doRemove(key.getKey(), 16);
        }
        if (!filterAllBubbles.isEmpty()) {
            dispatchPendingChanges();
        }
    }

    static /* synthetic */ boolean lambda$removeBubblesForUser$5(int i, Bubble bubble) {
        return i == bubble.getUser().getIdentifier();
    }

    private void doAdd(Bubble bubble) {
        this.mBubbles.add(0, bubble);
        this.mStateChange.addedBubble = bubble;
        Update update = this.mStateChange;
        boolean z = true;
        if (this.mBubbles.size() <= 1) {
            z = false;
        }
        update.orderChanged = z;
        if (!isExpanded()) {
            setSelectedBubbleInternal(this.mBubbles.get(0));
        }
    }

    private void trim() {
        if (this.mBubbles.size() > this.mMaxBubbles) {
            int size = this.mBubbles.size() - this.mMaxBubbles;
            ArrayList arrayList = new ArrayList();
            this.mBubbles.stream().sorted(Comparator.comparingLong(new BubbleData$$ExternalSyntheticLambda12())).filter(new BubbleData$$ExternalSyntheticLambda1(this)).forEachOrdered(new BubbleData$$ExternalSyntheticLambda2(arrayList, size));
            arrayList.forEach(new BubbleData$$ExternalSyntheticLambda3(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$trim$6$com-android-wm-shell-bubbles-BubbleData  reason: not valid java name */
    public /* synthetic */ boolean m3416lambda$trim$6$comandroidwmshellbubblesBubbleData(Bubble bubble) {
        return !bubble.equals(this.mSelectedBubble);
    }

    static /* synthetic */ void lambda$trim$7(ArrayList arrayList, int i, Bubble bubble) {
        if (arrayList.size() < i) {
            arrayList.add(bubble);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$trim$8$com-android-wm-shell-bubbles-BubbleData  reason: not valid java name */
    public /* synthetic */ void m3417lambda$trim$8$comandroidwmshellbubblesBubbleData(Bubble bubble) {
        doRemove(bubble.getKey(), 2);
    }

    private void doUpdate(Bubble bubble, boolean z) {
        this.mStateChange.updatedBubble = bubble;
        if (!isExpanded() && z) {
            int indexOf = this.mBubbles.indexOf(bubble);
            this.mBubbles.remove((Object) bubble);
            this.mBubbles.add(0, bubble);
            this.mStateChange.orderChanged = indexOf != 0;
            setSelectedBubbleInternal(this.mBubbles.get(0));
        }
    }

    private void performActionOnBubblesMatching(List<Bubble> list, Predicate<Bubble> predicate, Consumer<Bubble> consumer) {
        ArrayList<Bubble> arrayList = new ArrayList<>();
        for (Bubble next : list) {
            if (predicate.test(next)) {
                arrayList.add(next);
            }
        }
        for (Bubble accept : arrayList) {
            consumer.accept(accept);
        }
    }

    private void doRemove(String str, int i) {
        Bubble suppressedBubbleWithKey;
        if (this.mPendingBubbles.containsKey(str)) {
            this.mPendingBubbles.remove(str);
        }
        boolean z = i == 5 || i == 9 || i == 7 || i == 4 || i == 12 || i == 13 || i == 8 || i == 16;
        int indexForKey = indexForKey(str);
        if (indexForKey == -1) {
            if (hasOverflowBubbleWithKey(str) && z) {
                Bubble overflowBubbleWithKey = getOverflowBubbleWithKey(str);
                if (overflowBubbleWithKey != null) {
                    overflowBubbleWithKey.stopInflation();
                }
                this.mLogger.logOverflowRemove(overflowBubbleWithKey, i);
                this.mOverflowBubbles.remove((Object) overflowBubbleWithKey);
                this.mStateChange.bubbleRemoved(overflowBubbleWithKey, i);
                this.mStateChange.removedOverflowBubble = overflowBubbleWithKey;
            }
            if (hasSuppressedBubbleWithKey(str) && z && (suppressedBubbleWithKey = getSuppressedBubbleWithKey(str)) != null) {
                this.mSuppressedBubbles.remove(suppressedBubbleWithKey.getLocusId());
                suppressedBubbleWithKey.stopInflation();
                this.mStateChange.bubbleRemoved(suppressedBubbleWithKey, i);
                return;
            }
            return;
        }
        Bubble bubble = this.mBubbles.get(indexForKey);
        bubble.stopInflation();
        overflowBubble(i, bubble);
        if (this.mBubbles.size() == 1) {
            setExpandedInternal(false);
            this.mSelectedBubble = null;
        }
        if (indexForKey < this.mBubbles.size() - 1) {
            this.mStateChange.orderChanged = true;
        }
        this.mBubbles.remove(indexForKey);
        this.mStateChange.bubbleRemoved(bubble, i);
        if (!isExpanded()) {
            this.mStateChange.orderChanged |= repackAll();
        }
        if (Objects.equals(this.mSelectedBubble, bubble)) {
            setNewSelectedIndex(indexForKey);
        }
        maybeSendDeleteIntent(i, bubble);
    }

    private void setNewSelectedIndex(int i) {
        if (this.mBubbles.isEmpty()) {
            Log.w("Bubbles", "Bubbles list empty when attempting to select index: " + i);
            return;
        }
        setSelectedBubbleInternal(this.mBubbles.get(Math.min(i, this.mBubbles.size() - 1)));
    }

    private void doSuppress(Bubble bubble) {
        this.mStateChange.suppressedBubble = bubble;
        boolean z = true;
        bubble.setSuppressBubble(true);
        int indexOf = this.mBubbles.indexOf(bubble);
        Update update = this.mStateChange;
        if (this.mBubbles.size() - 1 == indexOf) {
            z = false;
        }
        update.orderChanged = z;
        this.mBubbles.remove(indexOf);
        if (!Objects.equals(this.mSelectedBubble, bubble)) {
            return;
        }
        if (this.mBubbles.isEmpty()) {
            this.mSelectedBubble = null;
        } else {
            setNewSelectedIndex(0);
        }
    }

    private void doUnsuppress(Bubble bubble) {
        bubble.setSuppressBubble(false);
        this.mStateChange.unsuppressedBubble = bubble;
        this.mBubbles.add(bubble);
        if (this.mBubbles.size() > 1) {
            repackAll();
            this.mStateChange.orderChanged = true;
        }
        if (this.mBubbles.get(0) == bubble) {
            setNewSelectedIndex(0);
        }
    }

    /* access modifiers changed from: package-private */
    public void overflowBubble(int i, Bubble bubble) {
        if (bubble.getPendingIntentCanceled()) {
            return;
        }
        if (i == 2 || i == 1 || i == 15) {
            this.mLogger.logOverflowAdd(bubble, i);
            this.mOverflowBubbles.remove((Object) bubble);
            this.mOverflowBubbles.add(0, bubble);
            this.mStateChange.addedOverflowBubble = bubble;
            bubble.stopInflation();
            if (this.mOverflowBubbles.size() == this.mMaxOverflowBubbles + 1) {
                List<Bubble> list = this.mOverflowBubbles;
                Bubble bubble2 = list.get(list.size() - 1);
                this.mStateChange.bubbleRemoved(bubble2, 11);
                this.mLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_MAX_REACHED);
                this.mOverflowBubbles.remove((Object) bubble2);
                this.mStateChange.removedOverflowBubble = bubble2;
            }
        }
    }

    public void dismissAll(int i) {
        if (!this.mBubbles.isEmpty() || !this.mSuppressedBubbles.isEmpty()) {
            setExpandedInternal(false);
            setSelectedBubbleInternal((BubbleViewProvider) null);
            while (!this.mBubbles.isEmpty()) {
                doRemove(this.mBubbles.get(0).getKey(), i);
            }
            while (!this.mSuppressedBubbles.isEmpty()) {
                doRemove(this.mSuppressedBubbles.removeAt(0).getKey(), i);
            }
            dispatchPendingChanges();
        }
    }

    public void onLocusVisibilityChanged(int i, LocusId locusId, boolean z) {
        Bubble bubbleInStackWithLocusId = getBubbleInStackWithLocusId(locusId);
        if (!z || (bubbleInStackWithLocusId != null && bubbleInStackWithLocusId.getTaskId() == i)) {
            this.mVisibleLocusIds.remove(locusId);
        } else {
            this.mVisibleLocusIds.add(locusId);
        }
        if (bubbleInStackWithLocusId != null || (bubbleInStackWithLocusId = this.mSuppressedBubbles.get(locusId)) != null) {
            boolean z2 = this.mSuppressedBubbles.get(locusId) != null;
            if (z && !z2 && bubbleInStackWithLocusId.isSuppressable() && i != bubbleInStackWithLocusId.getTaskId()) {
                this.mSuppressedBubbles.put(locusId, bubbleInStackWithLocusId);
                doSuppress(bubbleInStackWithLocusId);
                dispatchPendingChanges();
            } else if (!z) {
                Bubble remove = this.mSuppressedBubbles.remove(locusId);
                if (remove != null) {
                    doUnsuppress(remove);
                }
                dispatchPendingChanges();
            }
        }
    }

    public void clearOverflow() {
        while (!this.mOverflowBubbles.isEmpty()) {
            doRemove(this.mOverflowBubbles.get(0).getKey(), 8);
        }
        dispatchPendingChanges();
    }

    private void dispatchPendingChanges() {
        if (this.mListener != null && this.mStateChange.anythingChanged()) {
            this.mListener.applyUpdate(this.mStateChange);
        }
        this.mStateChange = new Update(this.mBubbles, this.mOverflowBubbles);
    }

    private void setSelectedBubbleInternal(BubbleViewProvider bubbleViewProvider) {
        if (!Objects.equals(bubbleViewProvider, this.mSelectedBubble)) {
            boolean z = bubbleViewProvider != null && BubbleOverflow.KEY.equals(bubbleViewProvider.getKey());
            if (bubbleViewProvider == null || this.mBubbles.contains(bubbleViewProvider) || this.mOverflowBubbles.contains(bubbleViewProvider) || z) {
                if (this.mExpanded && bubbleViewProvider != null && !z) {
                    ((Bubble) bubbleViewProvider).markAsAccessedAt(this.mTimeSource.currentTimeMillis());
                }
                this.mSelectedBubble = bubbleViewProvider;
                this.mStateChange.selectedBubble = bubbleViewProvider;
                this.mStateChange.selectionChanged = true;
                return;
            }
            Log.e("Bubbles", "Cannot select bubble which doesn't exist! (" + bubbleViewProvider + ") bubbles=" + this.mBubbles);
        }
    }

    /* access modifiers changed from: package-private */
    public void setCurrentUserId(int i) {
        this.mCurrentUserId = i;
    }

    /* access modifiers changed from: package-private */
    public void logBubbleEvent(BubbleViewProvider bubbleViewProvider, int i, String str, int i2, int i3, float f, float f2) {
        if (bubbleViewProvider == null) {
            this.mLogger.logStackUiChanged(str, i, i2, f, f2);
        } else if (!bubbleViewProvider.getKey().equals(BubbleOverflow.KEY)) {
            this.mLogger.logBubbleUiChanged((Bubble) bubbleViewProvider, str, i, i2, f, f2, i3);
        } else if (i == 3) {
            this.mLogger.logShowOverflow(str, this.mCurrentUserId);
        }
    }

    private void setExpandedInternal(boolean z) {
        if (this.mExpanded != z) {
            if (z) {
                if (!this.mBubbles.isEmpty() || this.mShowingOverflow) {
                    BubbleViewProvider bubbleViewProvider = this.mSelectedBubble;
                    if (bubbleViewProvider == null) {
                        Log.e("Bubbles", "Attempt to expand stack without selected bubble!");
                        return;
                    }
                    if (bubbleViewProvider.getKey().equals(this.mOverflow.getKey()) && !this.mBubbles.isEmpty()) {
                        setSelectedBubbleInternal(this.mBubbles.get(0));
                    }
                    BubbleViewProvider bubbleViewProvider2 = this.mSelectedBubble;
                    if (bubbleViewProvider2 instanceof Bubble) {
                        ((Bubble) bubbleViewProvider2).markAsAccessedAt(this.mTimeSource.currentTimeMillis());
                    }
                    this.mStateChange.orderChanged |= repackAll();
                } else {
                    Log.e("Bubbles", "Attempt to expand stack when empty!");
                    return;
                }
            } else if (!this.mBubbles.isEmpty()) {
                this.mStateChange.orderChanged |= repackAll();
                if (this.mBubbles.indexOf(this.mSelectedBubble) > 0 && this.mBubbles.indexOf(this.mSelectedBubble) != 0) {
                    this.mBubbles.remove((Object) (Bubble) this.mSelectedBubble);
                    this.mBubbles.add(0, (Bubble) this.mSelectedBubble);
                    this.mStateChange.orderChanged = true;
                }
            }
            if (this.mNeedsTrimming) {
                this.mNeedsTrimming = false;
                trim();
            }
            this.mExpanded = z;
            this.mStateChange.expanded = z;
            this.mStateChange.expandedChanged = true;
        }
    }

    /* access modifiers changed from: private */
    public static long sortKey(Bubble bubble) {
        return bubble.getLastActivity();
    }

    private boolean repackAll() {
        if (this.mBubbles.isEmpty()) {
            return false;
        }
        ArrayList arrayList = new ArrayList(this.mBubbles.size());
        Stream<Bubble> sorted = this.mBubbles.stream().sorted(BUBBLES_BY_SORT_KEY_DESCENDING);
        Objects.requireNonNull(arrayList);
        sorted.forEachOrdered(new BubbleData$$ExternalSyntheticLambda0(arrayList));
        if (arrayList.equals(this.mBubbles)) {
            return false;
        }
        this.mBubbles.clear();
        this.mBubbles.addAll(arrayList);
        return true;
    }

    private void maybeSendDeleteIntent(int i, Bubble bubble) {
        PendingIntent deleteIntent;
        if (i == 1 && (deleteIntent = bubble.getDeleteIntent()) != null) {
            try {
                deleteIntent.send();
            } catch (PendingIntent.CanceledException unused) {
                Log.w("Bubbles", "Failed to send delete intent for bubble with key: " + bubble.getKey());
            }
        }
    }

    private int indexForKey(String str) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            if (this.mBubbles.get(i).getKey().equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public List<Bubble> getBubbles() {
        return Collections.unmodifiableList(this.mBubbles);
    }

    public List<Bubble> getOverflowBubbles() {
        return Collections.unmodifiableList(this.mOverflowBubbles);
    }

    /* access modifiers changed from: package-private */
    public Bubble getAnyBubbleWithkey(String str) {
        Bubble bubbleInStackWithKey = getBubbleInStackWithKey(str);
        if (bubbleInStackWithKey == null) {
            bubbleInStackWithKey = getOverflowBubbleWithKey(str);
        }
        return bubbleInStackWithKey == null ? getSuppressedBubbleWithKey(str) : bubbleInStackWithKey;
    }

    /* access modifiers changed from: package-private */
    public Bubble getAnyBubbleWithShortcutId(String str) {
        String str2;
        String str3;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = this.mBubbles.get(i);
            if (bubble.getShortcutInfo() != null) {
                str3 = bubble.getShortcutInfo().getId();
            } else {
                str3 = bubble.getMetadataShortcutId();
            }
            if (str.equals(str3)) {
                return bubble;
            }
        }
        for (int i2 = 0; i2 < this.mOverflowBubbles.size(); i2++) {
            Bubble bubble2 = this.mOverflowBubbles.get(i2);
            if (bubble2.getShortcutInfo() != null) {
                str2 = bubble2.getShortcutInfo().getId();
            } else {
                str2 = bubble2.getMetadataShortcutId();
            }
            if (str.equals(str2)) {
                return bubble2;
            }
        }
        return null;
    }

    public Bubble getBubbleInStackWithKey(String str) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = this.mBubbles.get(i);
            if (bubble.getKey().equals(str)) {
                return bubble;
            }
        }
        return null;
    }

    private Bubble getBubbleInStackWithLocusId(LocusId locusId) {
        if (locusId == null) {
            return null;
        }
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = this.mBubbles.get(i);
            if (locusId.equals(bubble.getLocusId())) {
                return bubble;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Bubble getBubbleWithView(View view) {
        for (int i = 0; i < this.mBubbles.size(); i++) {
            Bubble bubble = this.mBubbles.get(i);
            if (bubble.getIconView() != null && bubble.getIconView().equals(view)) {
                return bubble;
            }
        }
        return null;
    }

    public Bubble getOverflowBubbleWithKey(String str) {
        for (int i = 0; i < this.mOverflowBubbles.size(); i++) {
            Bubble bubble = this.mOverflowBubbles.get(i);
            if (bubble.getKey().equals(str)) {
                return bubble;
            }
        }
        return null;
    }

    public Bubble getSuppressedBubbleWithKey(String str) {
        for (Bubble next : this.mSuppressedBubbles.values()) {
            if (next.getKey().equals(str)) {
                return next;
            }
        }
        return null;
    }

    public Bubble getPendingBubbleWithKey(String str) {
        for (Bubble next : this.mPendingBubbles.values()) {
            if (next.getKey().equals(str)) {
                return next;
            }
        }
        return null;
    }

    private List<Bubble> filterAllBubbles(Predicate<Bubble> predicate) {
        ArrayList arrayList = new ArrayList();
        for (Bubble next : this.mPendingBubbles.values()) {
            if (predicate.test(next)) {
                arrayList.add(next);
            }
        }
        for (Bubble next2 : this.mSuppressedBubbles.values()) {
            if (predicate.test(next2)) {
                arrayList.add(next2);
            }
        }
        for (Bubble next3 : this.mBubbles) {
            if (predicate.test(next3)) {
                arrayList.add(next3);
            }
        }
        for (Bubble next4 : this.mOverflowBubbles) {
            if (predicate.test(next4)) {
                arrayList.add(next4);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void setTimeSource(TimeSource timeSource) {
        this.mTimeSource = timeSource;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void setMaxOverflowBubbles(int i) {
        this.mMaxOverflowBubbles = i;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("selected: ");
        BubbleViewProvider bubbleViewProvider = this.mSelectedBubble;
        printWriter.println(bubbleViewProvider != null ? bubbleViewProvider.getKey() : "null");
        printWriter.print("expanded: ");
        printWriter.println(this.mExpanded);
        printWriter.print("stack bubble count:    ");
        printWriter.println(this.mBubbles.size());
        for (Bubble dump : this.mBubbles) {
            dump.dump(printWriter, strArr);
        }
        printWriter.print("overflow bubble count:    ");
        printWriter.println(this.mOverflowBubbles.size());
        for (Bubble dump2 : this.mOverflowBubbles) {
            dump2.dump(printWriter, strArr);
        }
        printWriter.print("summaryKeys: ");
        printWriter.println(this.mSuppressedGroupKeys.size());
        for (String str : this.mSuppressedGroupKeys.keySet()) {
            printWriter.println("   suppressing: " + str);
        }
    }
}
