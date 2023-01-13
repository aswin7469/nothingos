package com.android.systemui.decor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.RegionInterceptingFrameLayout;
import java.p026io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0010\u0015\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u000bJ\u0016\u0010\u0017\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u001d\u001a\u00020\u000bJ\u0014\u0010\u001e\u001a\u00020\u001f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00120\nJ9\u0010!\u001a\u00020\u00142\u0010\b\u0002\u0010\"\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010#2\u0006\u0010$\u001a\u00020\u000b2\u0006\u0010\u0016\u001a\u00020\u000b2\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u001b¢\u0006\u0002\u0010&J\u0010\u0010'\u001a\u00020\u00142\b\u0010(\u001a\u0004\u0018\u00010)J\u000e\u0010*\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n8F¢\u0006\u0006\u001a\u0004\b\f\u0010\rR&\u0010\u000e\u001a\u001a\u0012\u0004\u0012\u00020\u000b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00120\u00100\u000fX\u0004¢\u0006\u0002\n\u0000¨\u0006+"}, mo65043d2 = {"Lcom/android/systemui/decor/OverlayWindow;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "rootView", "Landroid/view/ViewGroup;", "getRootView", "()Landroid/view/ViewGroup;", "viewIds", "", "", "getViewIds", "()Ljava/util/List;", "viewProviderMap", "", "Lkotlin/Pair;", "Landroid/view/View;", "Lcom/android/systemui/decor/DecorProvider;", "addDecorProvider", "", "decorProvider", "rotation", "dump", "pw", "Ljava/io/PrintWriter;", "name", "", "getView", "id", "hasSameProviders", "", "newProviders", "onReloadResAndMeasure", "filterIds", "", "reloadToken", "displayUniqueId", "([Ljava/lang/Integer;IILjava/lang/String;)V", "removeRedundantViews", "expectExistViewIds", "", "removeView", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: OverlayWindow.kt */
public final class OverlayWindow {
    private final Context context;
    private final ViewGroup rootView;
    private final Map<Integer, Pair<View, DecorProvider>> viewProviderMap = new LinkedHashMap();

    public OverlayWindow(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        this.rootView = new RegionInterceptingFrameLayout(context2);
    }

    public final ViewGroup getRootView() {
        return this.rootView;
    }

    public final List<Integer> getViewIds() {
        return CollectionsKt.toList(this.viewProviderMap.keySet());
    }

    public final void addDecorProvider(DecorProvider decorProvider, int i) {
        Intrinsics.checkNotNullParameter(decorProvider, "decorProvider");
        this.viewProviderMap.put(Integer.valueOf(decorProvider.getViewId()), new Pair(decorProvider.inflateView(this.context, this.rootView, i), decorProvider));
    }

    public final View getView(int i) {
        Pair pair = this.viewProviderMap.get(Integer.valueOf(i));
        if (pair != null) {
            return (View) pair.getFirst();
        }
        return null;
    }

    public final void removeView(int i) {
        View view = getView(i);
        if (view != null) {
            this.rootView.removeView(view);
            this.viewProviderMap.remove(Integer.valueOf(i));
        }
    }

    public final void removeRedundantViews(int[] iArr) {
        for (Number intValue : getViewIds()) {
            int intValue2 = intValue.intValue();
            if (iArr == null || !ArraysKt.contains(iArr, intValue2)) {
                removeView(intValue2);
            }
        }
    }

    public final boolean hasSameProviders(List<? extends DecorProvider> list) {
        boolean z;
        boolean z2;
        Intrinsics.checkNotNullParameter(list, "newProviders");
        if (list.size() != this.viewProviderMap.size()) {
            return false;
        }
        Iterable iterable = list;
        if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
            Iterator it = iterable.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (getView(((DecorProvider) it.next()).getViewId()) != null) {
                    z2 = true;
                    continue;
                } else {
                    z2 = false;
                    continue;
                }
                if (!z2) {
                    z = false;
                    break;
                }
            }
        }
        z = true;
        if (z) {
            return true;
        }
        return false;
    }

    public static /* synthetic */ void onReloadResAndMeasure$default(OverlayWindow overlayWindow, Integer[] numArr, int i, int i2, String str, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            numArr = null;
        }
        if ((i3 & 8) != 0) {
            str = null;
        }
        overlayWindow.onReloadResAndMeasure(numArr, i, i2, str);
    }

    public final void dump(PrintWriter printWriter, String str) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        printWriter.println("  " + str + '=');
        printWriter.println("    rootView=" + this.rootView);
        int childCount = this.rootView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            printWriter.println("    child[" + i + "]=" + this.rootView.getChildAt(i));
        }
    }

    public final void onReloadResAndMeasure(Integer[] numArr, int i, int i2, String str) {
        Unit unit;
        if (numArr != null) {
            for (Integer intValue : numArr) {
                Pair pair = this.viewProviderMap.get(Integer.valueOf(intValue.intValue()));
                if (pair != null) {
                    ((DecorProvider) pair.getSecond()).onReloadResAndMeasure((View) pair.getFirst(), i, i2, str);
                }
            }
            unit = Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            OverlayWindow overlayWindow = this;
            for (Pair pair2 : this.viewProviderMap.values()) {
                ((DecorProvider) pair2.getSecond()).onReloadResAndMeasure((View) pair2.getFirst(), i, i2, str);
            }
        }
    }
}
