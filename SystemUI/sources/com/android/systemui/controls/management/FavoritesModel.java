package com.android.systemui.controls.management;

import android.content.ComponentName;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.management.ControlsModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\u0018\u0000 /2\u00020\u0001:\u0002/0B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u0014\u0010\u001f\u001a\u00020 2\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\rH\u0016J\u0018\u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u001aH\u0016J\u0018\u0010%\u001a\u00020 2\u0006\u0010&\u001a\u00020\u000f2\u0006\u0010'\u001a\u00020\u000fH\u0002J\u0018\u0010(\u001a\u00020 2\u0006\u0010&\u001a\u00020\u000f2\u0006\u0010'\u001a\u00020\u000fH\u0016J\u0018\u0010)\u001a\u00020 2\u0006\u0010&\u001a\u00020\u000f2\u0006\u0010'\u001a\u00020\u000fH\u0002J\u0018\u0010*\u001a\u00020 2\u0006\u0010&\u001a\u00020\u000f2\u0006\u0010'\u001a\u00020\u000fH\u0002J\u0018\u0010+\u001a\u00020 2\u0006\u0010,\u001a\u00020\u000f2\u0006\u0010-\u001a\u00020\u001aH\u0002J\u0018\u0010.\u001a\u00020 2\u0006\u0010,\u001a\u00020\u000f2\u0006\u0010-\u001a\u00020\u001aH\u0002R\u0014\u0010\f\u001a\b\u0012\u0002\b\u0003\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u0007X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0013R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0015\u001a\u00020\u0016¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u000e\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e¨\u00061"}, mo65043d2 = {"Lcom/android/systemui/controls/management/FavoritesModel;", "Lcom/android/systemui/controls/management/ControlsModel;", "customIconCache", "Lcom/android/systemui/controls/CustomIconCache;", "componentName", "Landroid/content/ComponentName;", "favorites", "", "Lcom/android/systemui/controls/controller/ControlInfo;", "favoritesModelCallback", "Lcom/android/systemui/controls/management/FavoritesModel$FavoritesModelCallback;", "(Lcom/android/systemui/controls/CustomIconCache;Landroid/content/ComponentName;Ljava/util/List;Lcom/android/systemui/controls/management/FavoritesModel$FavoritesModelCallback;)V", "adapter", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "dividerPosition", "", "elements", "Lcom/android/systemui/controls/management/ElementWrapper;", "getElements", "()Ljava/util/List;", "getFavorites", "itemTouchHelperCallback", "Landroidx/recyclerview/widget/ItemTouchHelper$SimpleCallback;", "getItemTouchHelperCallback", "()Landroidx/recyclerview/widget/ItemTouchHelper$SimpleCallback;", "modified", "", "moveHelper", "Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "getMoveHelper", "()Lcom/android/systemui/controls/management/ControlsModel$MoveHelper;", "attachAdapter", "", "changeFavoriteStatus", "controlId", "", "favorite", "moveElement", "from", "to", "onMoveItem", "onMoveItemInternal", "updateDivider", "updateDividerNone", "oldDividerPosition", "show", "updateDividerShow", "Companion", "FavoritesModelCallback", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FavoritesModel.kt */
public final class FavoritesModel implements ControlsModel {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "FavoritesModel";
    private RecyclerView.Adapter<?> adapter;
    private final ComponentName componentName;
    private final CustomIconCache customIconCache;
    /* access modifiers changed from: private */
    public int dividerPosition;
    private final List<ElementWrapper> elements;
    private final FavoritesModelCallback favoritesModelCallback;
    private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private boolean modified;
    private final ControlsModel.MoveHelper moveHelper = new FavoritesModel$moveHelper$1(this);

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/controls/management/FavoritesModel$FavoritesModelCallback;", "Lcom/android/systemui/controls/management/ControlsModel$ControlsModelCallback;", "onNoneChanged", "", "showNoFavorites", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: FavoritesModel.kt */
    public interface FavoritesModelCallback extends ControlsModel.ControlsModelCallback {
        void onNoneChanged(boolean z);
    }

    public FavoritesModel(CustomIconCache customIconCache2, ComponentName componentName2, List<ControlInfo> list, FavoritesModelCallback favoritesModelCallback2) {
        Intrinsics.checkNotNullParameter(customIconCache2, "customIconCache");
        Intrinsics.checkNotNullParameter(componentName2, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(list, "favorites");
        Intrinsics.checkNotNullParameter(favoritesModelCallback2, "favoritesModelCallback");
        this.customIconCache = customIconCache2;
        this.componentName = componentName2;
        this.favoritesModelCallback = favoritesModelCallback2;
        Iterable<ControlInfo> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (ControlInfo controlInfoWrapper : iterable) {
            arrayList.add(new ControlInfoWrapper(this.componentName, controlInfoWrapper, true, new FavoritesModel$elements$1$1(this.customIconCache)));
        }
        this.elements = CollectionsKt.plus((List) arrayList, new DividerWrapper(false, false, 3, (DefaultConstructorMarker) null));
        this.dividerPosition = getElements().size() - 1;
        this.itemTouchHelperCallback = new FavoritesModel$itemTouchHelperCallback$1(this);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/controls/management/FavoritesModel$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: FavoritesModel.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public ControlsModel.MoveHelper getMoveHelper() {
        return this.moveHelper;
    }

    public void attachAdapter(RecyclerView.Adapter<?> adapter2) {
        Intrinsics.checkNotNullParameter(adapter2, "adapter");
        this.adapter = adapter2;
    }

    public List<ControlInfo> getFavorites() {
        Iterable<ElementWrapper> take = CollectionsKt.take(getElements(), this.dividerPosition);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(take, 10));
        for (ElementWrapper elementWrapper : take) {
            arrayList.add(((ControlInfoWrapper) elementWrapper).getControlInfo());
        }
        return (List) arrayList;
    }

    public List<ElementWrapper> getElements() {
        return this.elements;
    }

    public void changeFavoriteStatus(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "controlId");
        Iterator<ElementWrapper> it = getElements().iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            }
            ElementWrapper next = it.next();
            if ((next instanceof ControlInterface) && Intrinsics.areEqual((Object) ((ControlInterface) next).getControlId(), (Object) str)) {
                break;
            }
            i++;
        }
        if (i != -1) {
            int i2 = this.dividerPosition;
            if (i < i2 && z) {
                return;
            }
            if (i > i2 && !z) {
                return;
            }
            if (z) {
                onMoveItemInternal(i, i2);
            } else {
                onMoveItemInternal(i, getElements().size() - 1);
            }
        }
    }

    public void onMoveItem(int i, int i2) {
        onMoveItemInternal(i, i2);
    }

    private final void updateDividerNone(int i, boolean z) {
        ((DividerWrapper) getElements().get(i)).setShowNone(z);
        this.favoritesModelCallback.onNoneChanged(z);
    }

    private final void updateDividerShow(int i, boolean z) {
        ((DividerWrapper) getElements().get(i)).setShowDivider(z);
    }

    private final void onMoveItemInternal(int i, int i2) {
        RecyclerView.Adapter<?> adapter2;
        int i3 = this.dividerPosition;
        if (i != i3) {
            boolean z = false;
            if ((i < i3 && i2 >= i3) || (i > i3 && i2 <= i3)) {
                if (i < i3 && i2 >= i3) {
                    ((ControlInfoWrapper) getElements().get(i)).setFavorite(false);
                } else if (i > i3 && i2 <= i3) {
                    ((ControlInfoWrapper) getElements().get(i)).setFavorite(true);
                }
                updateDivider(i, i2);
                z = true;
            }
            moveElement(i, i2);
            RecyclerView.Adapter<?> adapter3 = this.adapter;
            if (adapter3 != null) {
                adapter3.notifyItemMoved(i, i2);
            }
            if (z && (adapter2 = this.adapter) != null) {
                adapter2.notifyItemChanged(i2, new Object());
            }
            if (!this.modified) {
                this.modified = true;
                this.favoritesModelCallback.onFirstChange();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateDivider(int r5, int r6) {
        /*
            r4 = this;
            int r0 = r4.dividerPosition
            r1 = 1
            r2 = 0
            if (r5 >= r0) goto L_0x0024
            if (r6 < r0) goto L_0x0024
            int r5 = r0 + -1
            r4.dividerPosition = r5
            if (r5 != 0) goto L_0x0012
            r4.updateDividerNone(r0, r1)
            r2 = r1
        L_0x0012:
            int r5 = r4.dividerPosition
            java.util.List r6 = r4.getElements()
            int r6 = r6.size()
            int r6 = r6 + -2
            if (r5 != r6) goto L_0x0047
            r4.updateDividerShow(r0, r1)
            goto L_0x0048
        L_0x0024:
            if (r5 <= r0) goto L_0x0047
            if (r6 > r0) goto L_0x0047
            int r5 = r0 + 1
            r4.dividerPosition = r5
            if (r5 != r1) goto L_0x0033
            r4.updateDividerNone(r0, r2)
            r5 = r1
            goto L_0x0034
        L_0x0033:
            r5 = r2
        L_0x0034:
            int r6 = r4.dividerPosition
            java.util.List r3 = r4.getElements()
            int r3 = r3.size()
            int r3 = r3 - r1
            if (r6 != r3) goto L_0x0045
            r4.updateDividerShow(r0, r2)
            goto L_0x0048
        L_0x0045:
            r1 = r5
            goto L_0x0048
        L_0x0047:
            r1 = r2
        L_0x0048:
            if (r1 == 0) goto L_0x0051
            androidx.recyclerview.widget.RecyclerView$Adapter<?> r4 = r4.adapter
            if (r4 == 0) goto L_0x0051
            r4.notifyItemChanged(r0)
        L_0x0051:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.FavoritesModel.updateDivider(int, int):void");
    }

    private final void moveElement(int i, int i2) {
        if (i < i2) {
            while (i < i2) {
                int i3 = i + 1;
                Collections.swap((List<?>) getElements(), i, i3);
                i = i3;
            }
            return;
        }
        int i4 = i2 + 1;
        if (i4 <= i) {
            while (true) {
                Collections.swap((List<?>) getElements(), i, i - 1);
                if (i != i4) {
                    i--;
                } else {
                    return;
                }
            }
        }
    }

    public final ItemTouchHelper.SimpleCallback getItemTouchHelperCallback() {
        return this.itemTouchHelperCallback;
    }
}
