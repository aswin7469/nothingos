package com.android.settings.location;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$string;
import com.android.settings.widget.RestrictedAppPreference;
import com.android.settingslib.location.InjectedSetting;
import com.android.settingslib.widget.AppPreference;
import dalvik.system.DexClassLoader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DimmableIZatIconPreference {
    /* access modifiers changed from: private */
    public static Method mGetConsentMethod;
    /* access modifiers changed from: private */
    public static Method mGetXtProxyMethod;
    private static String mIzatPackage;
    /* access modifiers changed from: private */
    public static DexClassLoader mLoader;
    /* access modifiers changed from: private */
    public static Class mNotifierClz;
    private static Method mShowIzatMethod;
    private static Class mXtProxyClz;

    private static void load(Context context) {
        if (mLoader == null) {
            try {
                if (mXtProxyClz == null || mNotifierClz == null) {
                    DexClassLoader dexClassLoader = new DexClassLoader("/system_ext/framework/izat.xt.srv.jar", context.getFilesDir().getAbsolutePath(), (String) null, ClassLoader.getSystemClassLoader());
                    mLoader = dexClassLoader;
                    mXtProxyClz = Class.forName("com.qti.izat.XTProxy", true, dexClassLoader);
                    mNotifierClz = Class.forName("com.qti.izat.XTProxy$Notifier", true, mLoader);
                    mIzatPackage = (String) mXtProxyClz.getField("IZAT_XT_PACKAGE").get((Object) null);
                    mGetXtProxyMethod = mXtProxyClz.getMethod("getXTProxy", new Class[]{Context.class, mNotifierClz});
                    mGetConsentMethod = mXtProxyClz.getMethod("getUserConsent", new Class[0]);
                    mShowIzatMethod = mXtProxyClz.getMethod("showIzat", new Class[]{Context.class, String.class});
                }
            } catch (ClassNotFoundException | IllegalAccessException | LinkageError | NoSuchFieldException | NoSuchMethodException | NullPointerException | SecurityException e) {
                mXtProxyClz = null;
                mNotifierClz = null;
                mIzatPackage = null;
                mGetXtProxyMethod = null;
                mGetConsentMethod = null;
                mShowIzatMethod = null;
                e.printStackTrace();
            }
        }
    }

    static boolean showIzat(Context context, String str) {
        load(context);
        try {
            Method method = mShowIzatMethod;
            if (method == null) {
                return true;
            }
            return ((Boolean) method.invoke((Object) null, new Object[]{context, str})).booleanValue();
        } catch (ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return true;
        }
    }

    private static boolean isIzatPackage(Context context, InjectedSetting injectedSetting) {
        String str = mIzatPackage;
        return str != null && str.equals(injectedSetting.packageName);
    }

    /* access modifiers changed from: private */
    public static void dimIcon(AppPreference appPreference, boolean z) {
        Drawable icon = appPreference.getIcon();
        if (icon != null) {
            icon.mutate().setAlpha(z ? 102 : 255);
            appPreference.setIcon(icon);
        }
        appPreference.setSummary(appPreference.getSummary());
    }

    private static class IZatAppPreference extends AppPreference {
        /* access modifiers changed from: private */
        public boolean mChecked;
        private Context mContext;
        /* access modifiers changed from: private */
        public Handler mHandler;

        private IZatAppPreference(Context context) {
            super(context);
            this.mContext = context;
            this.mHandler = new Handler(Looper.getMainLooper());
            Object newProxyInstance = Proxy.newProxyInstance(DimmableIZatIconPreference.mLoader, new Class[]{DimmableIZatIconPreference.mNotifierClz}, new InvocationHandler() {
                public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                    Boolean bool;
                    boolean booleanValue;
                    if (!method.getName().equals("userConsentNotify") || (bool = objArr[0]) == null || !(bool instanceof Boolean) || IZatAppPreference.this.mChecked == (booleanValue = bool.booleanValue())) {
                        return null;
                    }
                    IZatAppPreference.this.mChecked = booleanValue;
                    IZatAppPreference.this.mHandler.post(new C1045xd91596e7(this));
                    return null;
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$invoke$0() {
                    IZatAppPreference iZatAppPreference = IZatAppPreference.this;
                    DimmableIZatIconPreference.dimIcon(iZatAppPreference, !iZatAppPreference.isEnabled() || !IZatAppPreference.this.mChecked);
                }
            });
            try {
                this.mChecked = ((Boolean) DimmableIZatIconPreference.mGetConsentMethod.invoke(DimmableIZatIconPreference.mGetXtProxyMethod.invoke((Object) null, new Object[]{context, newProxyInstance}), new Object[0])).booleanValue();
            } catch (ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public CharSequence getSummary() {
            int i;
            if (!isEnabled() || !this.mChecked) {
                i = R$string.notification_toggle_off;
            } else {
                i = R$string.notification_toggle_on;
            }
            return this.mContext.getString(i);
        }

        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            super.onBindViewHolder(preferenceViewHolder);
            this.mHandler.post(new C1044x3dabcf5a(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0() {
            DimmableIZatIconPreference.dimIcon(this, !isEnabled() || !this.mChecked);
        }
    }

    private static class IZatRestrictedAppPreference extends RestrictedAppPreference {
        /* access modifiers changed from: private */
        public boolean mChecked;
        /* access modifiers changed from: private */
        public Handler mHandler;

        private IZatRestrictedAppPreference(Context context, String str) {
            super(context, str);
            this.mHandler = new Handler(Looper.getMainLooper());
            Object newProxyInstance = Proxy.newProxyInstance(DimmableIZatIconPreference.mLoader, new Class[]{DimmableIZatIconPreference.mNotifierClz}, new InvocationHandler() {
                public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                    Boolean bool;
                    boolean booleanValue;
                    if (!method.getName().equals("userConsentNotify") || (bool = objArr[0]) == null || !(bool instanceof Boolean) || IZatRestrictedAppPreference.this.mChecked == (booleanValue = bool.booleanValue())) {
                        return null;
                    }
                    IZatRestrictedAppPreference.this.mChecked = booleanValue;
                    IZatRestrictedAppPreference.this.mHandler.post(new C1047x37b5320c(this));
                    return null;
                }

                /* access modifiers changed from: private */
                public /* synthetic */ void lambda$invoke$0() {
                    IZatRestrictedAppPreference iZatRestrictedAppPreference = IZatRestrictedAppPreference.this;
                    DimmableIZatIconPreference.dimIcon(iZatRestrictedAppPreference, !iZatRestrictedAppPreference.isEnabled() || !IZatRestrictedAppPreference.this.mChecked);
                }
            });
            try {
                this.mChecked = ((Boolean) DimmableIZatIconPreference.mGetConsentMethod.invoke(DimmableIZatIconPreference.mGetXtProxyMethod.invoke((Object) null, new Object[]{context, newProxyInstance}), new Object[0])).booleanValue();
            } catch (ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            super.onBindViewHolder(preferenceViewHolder);
            this.mHandler.post(new C1046xed51efbf(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0() {
            DimmableIZatIconPreference.dimIcon(this, !isEnabled() || !this.mChecked);
        }
    }

    static AppPreference getAppPreference(Context context, InjectedSetting injectedSetting) {
        if (isIzatPackage(context, injectedSetting)) {
            return new IZatAppPreference(context);
        }
        return new AppPreference(context);
    }

    static RestrictedAppPreference getRestrictedAppPreference(Context context, InjectedSetting injectedSetting) {
        if (isIzatPackage(context, injectedSetting)) {
            return new IZatRestrictedAppPreference(context, injectedSetting.userRestriction);
        }
        return new RestrictedAppPreference(context, injectedSetting.userRestriction);
    }
}
