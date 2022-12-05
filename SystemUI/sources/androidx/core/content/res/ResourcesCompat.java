package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public final class ResourcesCompat {
    private static final ThreadLocal<TypedValue> sTempTypedValue = new ThreadLocal<>();
    private static final WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>> sColorStateCaches = new WeakHashMap<>(0);
    private static final Object sColorStateCacheLock = new Object();

    public static Drawable getDrawable(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return res.getDrawable(id, theme);
        }
        return res.getDrawable(id);
    }

    public static ColorStateList getColorStateList(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return res.getColorStateList(id, theme);
        }
        ColorStateListCacheKey colorStateListCacheKey = new ColorStateListCacheKey(res, theme);
        ColorStateList cachedColorStateList = getCachedColorStateList(colorStateListCacheKey, id);
        if (cachedColorStateList != null) {
            return cachedColorStateList;
        }
        ColorStateList inflateColorStateList = inflateColorStateList(res, id, theme);
        if (inflateColorStateList != null) {
            addColorStateListToCache(colorStateListCacheKey, id, inflateColorStateList);
            return inflateColorStateList;
        }
        return res.getColorStateList(id);
    }

    private static ColorStateList inflateColorStateList(Resources resources, int resId, Resources.Theme theme) {
        if (isColorInt(resources, resId)) {
            return null;
        }
        try {
            return ColorStateListInflaterCompat.createFromXml(resources, resources.getXml(resId), theme);
        } catch (Exception e) {
            Log.e("ResourcesCompat", "Failed to inflate ColorStateList, leaving it to the framework", e);
            return null;
        }
    }

    private static ColorStateList getCachedColorStateList(ColorStateListCacheKey key, int resId) {
        ColorStateListCacheEntry colorStateListCacheEntry;
        synchronized (sColorStateCacheLock) {
            SparseArray<ColorStateListCacheEntry> sparseArray = sColorStateCaches.get(key);
            if (sparseArray != null && sparseArray.size() > 0 && (colorStateListCacheEntry = sparseArray.get(resId)) != null) {
                if (colorStateListCacheEntry.mConfiguration.equals(key.mResources.getConfiguration())) {
                    return colorStateListCacheEntry.mValue;
                }
                sparseArray.remove(resId);
            }
            return null;
        }
    }

    private static void addColorStateListToCache(ColorStateListCacheKey key, int resId, ColorStateList value) {
        synchronized (sColorStateCacheLock) {
            WeakHashMap<ColorStateListCacheKey, SparseArray<ColorStateListCacheEntry>> weakHashMap = sColorStateCaches;
            SparseArray<ColorStateListCacheEntry> sparseArray = weakHashMap.get(key);
            if (sparseArray == null) {
                sparseArray = new SparseArray<>();
                weakHashMap.put(key, sparseArray);
            }
            sparseArray.append(resId, new ColorStateListCacheEntry(value, key.mResources.getConfiguration()));
        }
    }

    private static boolean isColorInt(Resources resources, int resId) {
        TypedValue typedValue = getTypedValue();
        resources.getValue(resId, typedValue, true);
        int i = typedValue.type;
        return i >= 28 && i <= 31;
    }

    private static TypedValue getTypedValue() {
        ThreadLocal<TypedValue> threadLocal = sTempTypedValue;
        TypedValue typedValue = threadLocal.get();
        if (typedValue == null) {
            TypedValue typedValue2 = new TypedValue();
            threadLocal.set(typedValue2);
            return typedValue2;
        }
        return typedValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ColorStateListCacheKey {
        final Resources mResources;
        final Resources.Theme mTheme;

        ColorStateListCacheKey(Resources resources, Resources.Theme theme) {
            this.mResources = resources;
            this.mTheme = theme;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || ColorStateListCacheKey.class != o.getClass()) {
                return false;
            }
            ColorStateListCacheKey colorStateListCacheKey = (ColorStateListCacheKey) o;
            return this.mResources.equals(colorStateListCacheKey.mResources) && ObjectsCompat.equals(this.mTheme, colorStateListCacheKey.mTheme);
        }

        public int hashCode() {
            return ObjectsCompat.hash(this.mResources, this.mTheme);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ColorStateListCacheEntry {
        final Configuration mConfiguration;
        final ColorStateList mValue;

        ColorStateListCacheEntry(ColorStateList value, Configuration configuration) {
            this.mValue = value;
            this.mConfiguration = configuration;
        }
    }

    public static Typeface getFont(Context context, int id) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, new TypedValue(), 0, null, null, false, false);
    }

    public static Typeface getCachedFont(Context context, int id) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, new TypedValue(), 0, null, null, false, true);
    }

    /* loaded from: classes.dex */
    public static abstract class FontCallback {
        public abstract void onFontRetrievalFailed(int reason);

        public abstract void onFontRetrieved(Typeface typeface);

        public final void callbackSuccessAsync(final Typeface typeface, Handler handler) {
            getHandler(handler).post(new Runnable() { // from class: androidx.core.content.res.ResourcesCompat.FontCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    FontCallback.this.onFontRetrieved(typeface);
                }
            });
        }

        public final void callbackFailAsync(final int reason, Handler handler) {
            getHandler(handler).post(new Runnable() { // from class: androidx.core.content.res.ResourcesCompat.FontCallback.2
                @Override // java.lang.Runnable
                public void run() {
                    FontCallback.this.onFontRetrievalFailed(reason);
                }
            });
        }

        public static Handler getHandler(Handler handler) {
            return handler == null ? new Handler(Looper.getMainLooper()) : handler;
        }
    }

    public static void getFont(Context context, int id, FontCallback fontCallback, Handler handler) throws Resources.NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
        } else {
            loadFont(context, id, new TypedValue(), 0, fontCallback, handler, false, false);
        }
    }

    public static Typeface getFont(Context context, int id, TypedValue value, int style, FontCallback fontCallback) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return loadFont(context, id, value, style, fontCallback, null, true, false);
    }

    private static Typeface loadFont(Context context, int id, TypedValue value, int style, FontCallback fontCallback, Handler handler, boolean isRequestFromLayoutInflator, boolean isCachedOnly) {
        Resources resources = context.getResources();
        resources.getValue(id, value, true);
        Typeface loadFont = loadFont(context, resources, value, id, style, fontCallback, handler, isRequestFromLayoutInflator, isCachedOnly);
        if (loadFont == null && fontCallback == null && !isCachedOnly) {
            throw new Resources.NotFoundException("Font resource ID #0x" + Integer.toHexString(id) + " could not be retrieved.");
        }
        return loadFont;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00a6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Typeface loadFont(Context context, Resources wrapper, TypedValue value, int id, int style, FontCallback fontCallback, Handler handler, boolean isRequestFromLayoutInflator, boolean isCachedOnly) {
        CharSequence charSequence = value.string;
        if (charSequence == null) {
            throw new Resources.NotFoundException("Resource \"" + wrapper.getResourceName(id) + "\" (" + Integer.toHexString(id) + ") is not a Font: " + value);
        }
        String charSequence2 = charSequence.toString();
        if (!charSequence2.startsWith("res/")) {
            if (fontCallback != null) {
                fontCallback.callbackFailAsync(-3, handler);
            }
            return null;
        }
        Typeface findFromCache = TypefaceCompat.findFromCache(wrapper, id, style);
        if (findFromCache != null) {
            if (fontCallback != null) {
                fontCallback.callbackSuccessAsync(findFromCache, handler);
            }
            return findFromCache;
        } else if (isCachedOnly) {
            return null;
        } else {
            try {
                if (charSequence2.toLowerCase().endsWith(".xml")) {
                    FontResourcesParserCompat.FamilyResourceEntry parse = FontResourcesParserCompat.parse(wrapper.getXml(id), wrapper);
                    if (parse == null) {
                        Log.e("ResourcesCompat", "Failed to find font-family tag");
                        if (fontCallback != null) {
                            fontCallback.callbackFailAsync(-3, handler);
                        }
                        return null;
                    }
                    return TypefaceCompat.createFromResourcesFamilyXml(context, parse, wrapper, id, style, fontCallback, handler, isRequestFromLayoutInflator);
                }
                Typeface createFromResourcesFontFile = TypefaceCompat.createFromResourcesFontFile(context, wrapper, id, charSequence2, style);
                if (fontCallback != null) {
                    if (createFromResourcesFontFile != null) {
                        fontCallback.callbackSuccessAsync(createFromResourcesFontFile, handler);
                    } else {
                        fontCallback.callbackFailAsync(-3, handler);
                    }
                }
                return createFromResourcesFontFile;
            } catch (IOException e) {
                Log.e("ResourcesCompat", "Failed to read xml resource " + charSequence2, e);
                if (fontCallback != null) {
                    fontCallback.callbackFailAsync(-3, handler);
                }
                return null;
            } catch (XmlPullParserException e2) {
                Log.e("ResourcesCompat", "Failed to parse xml resource " + charSequence2, e2);
                if (fontCallback != null) {
                }
                return null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class ThemeCompat {
        public static void rebase(Resources.Theme theme) {
            int i = Build.VERSION.SDK_INT;
            if (i >= 29) {
                ImplApi29.rebase(theme);
            } else if (i < 23) {
            } else {
                ImplApi23.rebase(theme);
            }
        }

        /* loaded from: classes.dex */
        static class ImplApi29 {
            static void rebase(Resources.Theme theme) {
                theme.rebase();
            }
        }

        /* loaded from: classes.dex */
        static class ImplApi23 {
            private static Method sRebaseMethod;
            private static boolean sRebaseMethodFetched;
            private static final Object sRebaseMethodLock = new Object();

            static void rebase(Resources.Theme theme) {
                synchronized (sRebaseMethodLock) {
                    if (!sRebaseMethodFetched) {
                        try {
                            Method declaredMethod = Resources.Theme.class.getDeclaredMethod("rebase", new Class[0]);
                            sRebaseMethod = declaredMethod;
                            declaredMethod.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            Log.i("ResourcesCompat", "Failed to retrieve rebase() method", e);
                        }
                        sRebaseMethodFetched = true;
                    }
                    Method method = sRebaseMethod;
                    if (method != null) {
                        try {
                            method.invoke(theme, new Object[0]);
                        } catch (IllegalAccessException | InvocationTargetException e2) {
                            Log.i("ResourcesCompat", "Failed to invoke rebase() method via reflection", e2);
                            sRebaseMethod = null;
                        }
                    }
                }
            }
        }
    }
}
