package com.android.systemui.utils;

import android.content.Context;
import android.content.res.Resources;
import androidx.slice.core.SliceHints;
import java.util.HashMap;
import javax.xml.XMLConstants;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\u0018\u0000 \u00032\u00020\u0001:\u0002\u0003\u0004B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/utils/FwkResIdLoader;", "", "()V", "Companion", "ResIdCache", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FwkResIdLoader.kt */
public final class FwkResIdLoader {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String PACKAGE = "android";
    private static final int SIZE_CACHE_MAP_LARGE = 32;
    private static final int SIZE_CACHE_MAP_MIDDLE = 8;
    private static final int SIZE_CACHE_MAP_SMALL = 4;
    /* access modifiers changed from: private */
    public static final HashMap<String, ResIdCache> sCaches = new HashMap<>();
    /* access modifiers changed from: private */
    public static Context sContext;
    /* access modifiers changed from: private */
    public static Resources sRes;

    @JvmStatic
    public static final int anim(String str) {
        return Companion.anim(str);
    }

    @JvmStatic
    public static final int animator(String str) {
        return Companion.animator(str);
    }

    @JvmStatic
    public static final int array(String str) {
        return Companion.array(str);
    }

    @JvmStatic
    public static final int attr(String str) {
        return Companion.attr(str);
    }

    @JvmStatic
    public static final int bool(String str) {
        return Companion.bool(str);
    }

    @JvmStatic
    public static final int color(String str) {
        return Companion.color(str);
    }

    @JvmStatic
    public static final int dimen(String str) {
        return Companion.dimen(str);
    }

    @JvmStatic
    public static final int drawable(String str) {
        return Companion.drawable(str);
    }

    @JvmStatic
    public static final int fraction(String str) {
        return Companion.fraction(str);
    }

    @JvmStatic
    public static final Context getApplicationContext() {
        return Companion.getApplicationContext();
    }

    @JvmStatic
    public static final int getResId(Context context, String str, String str2) {
        return Companion.getResId(context, str, str2);
    }

    @JvmStatic
    public static final int getResId(Resources resources, String str, String str2) {
        return Companion.getResId(resources, str, str2);
    }

    @JvmStatic
    /* renamed from: id */
    public static final int m1219id(String str) {
        return Companion.mo47217id(str);
    }

    @JvmStatic
    public static final void init(Context context) {
        Companion.init(context);
    }

    @JvmStatic
    public static final int integer(String str) {
        return Companion.integer(str);
    }

    @JvmStatic
    public static final int interpolator(String str) {
        return Companion.interpolator(str);
    }

    @JvmStatic
    public static final int layout(String str) {
        return Companion.layout(str);
    }

    @JvmStatic
    public static final int mipmap(String str) {
        return Companion.mipmap(str);
    }

    @JvmStatic
    public static final int raw(String str) {
        return Companion.raw(str);
    }

    @JvmStatic
    public static final int string(String str) {
        return Companion.string(str);
    }

    @JvmStatic
    public static final int style(String str) {
        return Companion.style(str);
    }

    @JvmStatic
    public static final int styleable(String str) {
        return Companion.styleable(str);
    }

    @JvmStatic
    public static final int xml(String str) {
        return Companion.xml(str);
    }

    @Metadata(mo64986d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u0002\n\u0002\b\u000e\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\b\u0010\u001b\u001a\u00020\u000eH\u0007J \u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0004H\u0007J \u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0004H\u0007J\u0018\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0004H\u0002J\u0010\u0010 \u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010!\u001a\u00020\"2\u0006\u0010\u001d\u001a\u00020\u000eH\u0007J\u0010\u0010#\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010$\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010%\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010&\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\b\u0010'\u001a\u00020\"H\u0002J\b\u0010(\u001a\u00020\"H\u0002J\u0018\u0010)\u001a\u00020\"2\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010*\u001a\u00020\u0006H\u0002J\u0018\u0010)\u001a\u00020\"2\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0004H\u0002J\u0010\u0010+\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010,\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010-\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010.\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u0010\u0010/\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R*\u0010\t\u001a\u001e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000b0\nj\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000b`\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000¨\u00060"}, mo64987d2 = {"Lcom/android/systemui/utils/FwkResIdLoader$Companion;", "", "()V", "PACKAGE", "", "SIZE_CACHE_MAP_LARGE", "", "SIZE_CACHE_MAP_MIDDLE", "SIZE_CACHE_MAP_SMALL", "sCaches", "Ljava/util/HashMap;", "Lcom/android/systemui/utils/FwkResIdLoader$ResIdCache;", "Lkotlin/collections/HashMap;", "sContext", "Landroid/content/Context;", "sRes", "Landroid/content/res/Resources;", "anim", "resName", "animator", "array", "attr", "bool", "color", "dimen", "drawable", "fraction", "getApplicationContext", "getResId", "context", "resType", "resources", "id", "init", "", "integer", "interpolator", "layout", "mipmap", "preloadFirstLevelCache", "preloadSecondLevelCache", "put", "size", "raw", "string", "style", "styleable", "xml", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FwkResIdLoader.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private final void preloadSecondLevelCache() {
        }

        private Companion() {
        }

        @JvmStatic
        public final void init(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            FwkResIdLoader.sContext = context.getApplicationContext();
            FwkResIdLoader.sRes = context.getResources();
            preloadFirstLevelCache();
            preloadSecondLevelCache();
        }

        @JvmStatic
        public final Context getApplicationContext() {
            Context access$getSContext$cp = FwkResIdLoader.sContext;
            Intrinsics.checkNotNull(access$getSContext$cp);
            return access$getSContext$cp;
        }

        @JvmStatic
        public final int anim(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("anim", str);
        }

        @JvmStatic
        public final int animator(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("animator", str);
        }

        @JvmStatic
        public final int array(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("array", str);
        }

        @JvmStatic
        public final int attr(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("attr", str);
        }

        @JvmStatic
        public final int bool(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("bool", str);
        }

        @JvmStatic
        public final int color(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("color", str);
        }

        @JvmStatic
        public final int dimen(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("dimen", str);
        }

        @JvmStatic
        public final int drawable(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("drawable", str);
        }

        @JvmStatic
        public final int fraction(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("fraction", str);
        }

        @JvmStatic
        /* renamed from: id */
        public final int mo47217id(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("id", str);
        }

        @JvmStatic
        public final int integer(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("integer", str);
        }

        @JvmStatic
        public final int interpolator(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("interpolator", str);
        }

        @JvmStatic
        public final int layout(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("layout", str);
        }

        @JvmStatic
        public final int mipmap(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("mipmap", str);
        }

        @JvmStatic
        public final int raw(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId(SliceHints.HINT_RAW, str);
        }

        @JvmStatic
        public final int string(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("string", str);
        }

        @JvmStatic
        public final int style(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("style", str);
        }

        @JvmStatic
        public final int styleable(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId("styleable", str);
        }

        @JvmStatic
        public final int xml(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            return getResId(XMLConstants.XML_NS_PREFIX, str);
        }

        @JvmStatic
        public final int getResId(Context context, String str, String str2) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(str, "resName");
            Intrinsics.checkNotNullParameter(str2, "resType");
            return context.getResources().getIdentifier(str, str2, "android");
        }

        @JvmStatic
        public final int getResId(Resources resources, String str, String str2) {
            Intrinsics.checkNotNullParameter(resources, "resources");
            Intrinsics.checkNotNullParameter(str, "resName");
            Intrinsics.checkNotNullParameter(str2, "resType");
            return resources.getIdentifier(str, str2, "android");
        }

        private final void preloadFirstLevelCache() {
            put("styleable", 4);
            put("bool", 8);
            put("integer", 8);
            put("color", 32);
            put("drawable", 32);
            put("dimen", 32);
            put("id", 32);
            put("string", 32);
            put("attr", 8);
        }

        private final void put(String str, int i) {
            FwkResIdLoader.sCaches.put(str, new ResIdCache(str, i));
        }

        private final void put(String str, String str2) {
            ResIdCache resIdCache = (ResIdCache) FwkResIdLoader.sCaches.get(str2);
            if (resIdCache == null) {
                resIdCache = new ResIdCache(str2, 4);
            }
            resIdCache.add(str);
        }

        private final int getResId(String str, String str2) {
            ResIdCache resIdCache = (ResIdCache) FwkResIdLoader.sCaches.get(str);
            if (resIdCache == null) {
                resIdCache = new ResIdCache(str, 4);
            }
            return resIdCache.get(str2);
        }
    }

    @Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0003J\u000e\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0003R*\u0010\u0007\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00050\bj\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0005`\tX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0012"}, mo64987d2 = {"Lcom/android/systemui/utils/FwkResIdLoader$ResIdCache;", "", "type", "", "size", "", "(Ljava/lang/String;I)V", "mMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getSize", "()I", "getType", "()Ljava/lang/String;", "add", "", "resName", "get", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FwkResIdLoader.kt */
    public static final class ResIdCache {
        private final HashMap<String, Integer> mMap;
        private final int size;
        private final String type;

        public ResIdCache(String str, int i) {
            Intrinsics.checkNotNullParameter(str, "type");
            this.type = str;
            this.size = i;
            this.mMap = new HashMap<>(i);
        }

        public final int getSize() {
            return this.size;
        }

        public final String getType() {
            return this.type;
        }

        public final int get(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            if (this.mMap.containsKey(str)) {
                Integer num = this.mMap.get(str);
                Intrinsics.checkNotNull(num);
                return num.intValue();
            }
            Resources access$getSRes$cp = FwkResIdLoader.sRes;
            Intrinsics.checkNotNull(access$getSRes$cp);
            int identifier = access$getSRes$cp.getIdentifier(str, this.type, "android");
            this.mMap.put(str, Integer.valueOf(identifier));
            return identifier;
        }

        public final void add(String str) {
            Intrinsics.checkNotNullParameter(str, "resName");
            Resources access$getSRes$cp = FwkResIdLoader.sRes;
            Intrinsics.checkNotNull(access$getSRes$cp);
            this.mMap.put(str, Integer.valueOf(access$getSRes$cp.getIdentifier(str, this.type, "android")));
        }
    }
}
