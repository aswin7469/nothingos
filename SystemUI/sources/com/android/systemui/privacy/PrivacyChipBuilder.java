package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.systemui.C1894R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u0014\u0010\u0014\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u00150\u00150\u0005J\u0006\u0010\u0016\u001a\u00020\u000fJ\u001c\u0010\u0017\u001a\u00060\u0018j\u0002`\u0019\"\u0004\b\u0000\u0010\u001a*\b\u0012\u0004\u0012\u0002H\u001a0\u0005H\u0002R)\u0010\b\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00050\t0\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000fX\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000fX\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\r¨\u0006\u001b"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyChipBuilder;", "", "context", "Landroid/content/Context;", "itemsList", "", "Lcom/android/systemui/privacy/PrivacyItem;", "(Landroid/content/Context;Ljava/util/List;)V", "appsAndTypes", "Lkotlin/Pair;", "Lcom/android/systemui/privacy/PrivacyApplication;", "Lcom/android/systemui/privacy/PrivacyType;", "getAppsAndTypes", "()Ljava/util/List;", "lastSeparator", "", "kotlin.jvm.PlatformType", "separator", "types", "getTypes", "generateIcons", "Landroid/graphics/drawable/Drawable;", "joinTypes", "joinWithAnd", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "T", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyChipBuilder.kt */
public final class PrivacyChipBuilder {
    private final List<Pair<PrivacyApplication, List<PrivacyType>>> appsAndTypes;
    private final Context context;
    private final String lastSeparator;
    private final String separator;
    private final List<PrivacyType> types;

    public PrivacyChipBuilder(Context context2, List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(list, "itemsList");
        this.context = context2;
        this.separator = context2.getString(C1894R.string.ongoing_privacy_dialog_separator);
        this.lastSeparator = context2.getString(C1894R.string.ongoing_privacy_dialog_last_separator);
        Iterable<PrivacyItem> iterable = list;
        Map linkedHashMap = new LinkedHashMap();
        for (PrivacyItem privacyItem : iterable) {
            PrivacyApplication application = privacyItem.getApplication();
            Object obj = linkedHashMap.get(application);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(application, obj);
            }
            ((List) obj).add(privacyItem.getPrivacyType());
        }
        this.appsAndTypes = CollectionsKt.sortedWith(MapsKt.toList(linkedHashMap), ComparisonsKt.compareBy((Function1<? super T, ? extends Comparable<?>>[]) new Function1[]{C23143.INSTANCE, C23154.INSTANCE}));
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (PrivacyItem privacyType : iterable) {
            arrayList.add(privacyType.getPrivacyType());
        }
        this.types = CollectionsKt.sorted(CollectionsKt.distinct((List) arrayList));
    }

    public final List<Pair<PrivacyApplication, List<PrivacyType>>> getAppsAndTypes() {
        return this.appsAndTypes;
    }

    public final List<PrivacyType> getTypes() {
        return this.types;
    }

    public final List<Drawable> generateIcons() {
        Iterable<PrivacyType> iterable = this.types;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (PrivacyType icon : iterable) {
            arrayList.add(icon.getIcon(this.context));
        }
        return (List) arrayList;
    }

    private final <T> StringBuilder joinWithAnd(List<? extends T> list) {
        String str = this.separator;
        Intrinsics.checkNotNullExpressionValue(str, "separator");
        StringBuilder sb = (StringBuilder) CollectionsKt.joinTo$default(list.subList(0, list.size() - 1), new StringBuilder(), str, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 124, (Object) null);
        sb.append(this.lastSeparator);
        sb.append(CollectionsKt.last(list));
        return sb;
    }

    public final String joinTypes() {
        int size = this.types.size();
        if (size == 0) {
            return "";
        }
        if (size != 1) {
            Iterable<PrivacyType> iterable = this.types;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (PrivacyType name : iterable) {
                arrayList.add(name.getName(this.context));
            }
            String sb = joinWithAnd((List) arrayList).toString();
            Intrinsics.checkNotNullExpressionValue(sb, "types.map { it.getName(c….joinWithAnd().toString()");
            return sb;
        }
        String name2 = this.types.get(0).getName(this.context);
        Intrinsics.checkNotNullExpressionValue(name2, "types[0].getName(context)");
        return name2;
    }
}
