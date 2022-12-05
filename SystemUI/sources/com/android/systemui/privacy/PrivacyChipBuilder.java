package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.R$styleable;
import com.android.systemui.R$string;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PrivacyChipBuilder.kt */
/* loaded from: classes.dex */
public final class PrivacyChipBuilder {
    @NotNull
    private final List<Pair<PrivacyApplication, List<PrivacyType>>> appsAndTypes;
    @NotNull
    private final Context context;
    private final String lastSeparator;
    private final String separator;
    @NotNull
    private final List<PrivacyType> types;

    public PrivacyChipBuilder(@NotNull Context context, @NotNull List<PrivacyItem> itemsList) {
        List list;
        Comparator compareBy;
        List<Pair<PrivacyApplication, List<PrivacyType>>> sortedWith;
        int collectionSizeOrDefault;
        List distinct;
        List<PrivacyType> sorted;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(itemsList, "itemsList");
        this.context = context;
        this.separator = context.getString(R$string.ongoing_privacy_dialog_separator);
        this.lastSeparator = context.getString(R$string.ongoing_privacy_dialog_last_separator);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (PrivacyItem privacyItem : itemsList) {
            PrivacyApplication application = privacyItem.getApplication();
            Object obj = linkedHashMap.get(application);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(application, obj);
            }
            ((List) obj).add(privacyItem.getPrivacyType());
        }
        list = MapsKt___MapsKt.toList(linkedHashMap);
        compareBy = ComparisonsKt__ComparisonsKt.compareBy(AnonymousClass3.INSTANCE, AnonymousClass4.INSTANCE);
        sortedWith = CollectionsKt___CollectionsKt.sortedWith(list, compareBy);
        this.appsAndTypes = sortedWith;
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(itemsList, 10);
        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
        for (PrivacyItem privacyItem2 : itemsList) {
            arrayList.add(privacyItem2.getPrivacyType());
        }
        distinct = CollectionsKt___CollectionsKt.distinct(arrayList);
        sorted = CollectionsKt___CollectionsKt.sorted(distinct);
        this.types = sorted;
    }

    /* compiled from: PrivacyChipBuilder.kt */
    /* renamed from: com.android.systemui.privacy.PrivacyChipBuilder$3  reason: invalid class name */
    /* loaded from: classes.dex */
    static final class AnonymousClass3 extends Lambda implements Function1<Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>>, Comparable<?>> {
        public static final AnonymousClass3 INSTANCE = new AnonymousClass3();

        AnonymousClass3() {
            super(1);
        }

        @Nullable
        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final Comparable<?> invoke2(@NotNull Pair<PrivacyApplication, ? extends List<? extends PrivacyType>> it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Integer.valueOf(-it.getSecond().size());
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Comparable<?> mo1949invoke(Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>> pair) {
            return invoke2((Pair<PrivacyApplication, ? extends List<? extends PrivacyType>>) pair);
        }
    }

    /* compiled from: PrivacyChipBuilder.kt */
    /* renamed from: com.android.systemui.privacy.PrivacyChipBuilder$4  reason: invalid class name */
    /* loaded from: classes.dex */
    static final class AnonymousClass4 extends Lambda implements Function1<Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>>, Comparable<?>> {
        public static final AnonymousClass4 INSTANCE = new AnonymousClass4();

        AnonymousClass4() {
            super(1);
        }

        @Nullable
        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public final Comparable<?> invoke2(@NotNull Pair<PrivacyApplication, ? extends List<? extends PrivacyType>> it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return CollectionsKt.min(it.getSecond());
        }

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Comparable<?> mo1949invoke(Pair<? extends PrivacyApplication, ? extends List<? extends PrivacyType>> pair) {
            return invoke2((Pair<PrivacyApplication, ? extends List<? extends PrivacyType>>) pair);
        }
    }

    @NotNull
    public final List<Drawable> generateIcons() {
        int collectionSizeOrDefault;
        List<PrivacyType> list = this.types;
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
        ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
        for (PrivacyType privacyType : list) {
            arrayList.add(privacyType.getIcon(this.context));
        }
        return arrayList;
    }

    private final <T> StringBuilder joinWithAnd(List<? extends T> list) {
        Appendable joinTo$default;
        List<? extends T> subList = list.subList(0, list.size() - 1);
        StringBuilder sb = new StringBuilder();
        String separator = this.separator;
        Intrinsics.checkNotNullExpressionValue(separator, "separator");
        joinTo$default = CollectionsKt___CollectionsKt.joinTo$default(subList, sb, separator, null, null, 0, null, null, R$styleable.AppCompatTheme_windowMinWidthMajor, null);
        StringBuilder sb2 = (StringBuilder) joinTo$default;
        sb2.append(this.lastSeparator);
        sb2.append(CollectionsKt.last((List<? extends Object>) list));
        return sb2;
    }

    @NotNull
    public final String joinTypes() {
        int collectionSizeOrDefault;
        int size = this.types.size();
        if (size != 0) {
            if (size == 1) {
                String name = this.types.get(0).getName(this.context);
                Intrinsics.checkNotNullExpressionValue(name, "types[0].getName(context)");
                return name;
            }
            List<PrivacyType> list = this.types;
            collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
            ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
            for (PrivacyType privacyType : list) {
                arrayList.add(privacyType.getName(this.context));
            }
            String sb = joinWithAnd(arrayList).toString();
            Intrinsics.checkNotNullExpressionValue(sb, "types.map { it.getName(context) }.joinWithAnd().toString()");
            return sb;
        }
        return "";
    }
}
