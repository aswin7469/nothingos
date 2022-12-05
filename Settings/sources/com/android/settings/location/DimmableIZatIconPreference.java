package com.android.settings.location;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.R$styleable;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.widget.RestrictedAppPreference;
import com.android.settingslib.location.InjectedSetting;
import com.android.settingslib.widget.AppPreference;
import dalvik.system.DexClassLoader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/* loaded from: classes.dex */
public class DimmableIZatIconPreference {
    private static Method mGetConsentMethod;
    private static Method mGetXtProxyMethod;
    private static String mIzatPackage;
    private static DexClassLoader mLoader;
    private static Class mNotifierClz;
    private static Method mShowIzatMethod;
    private static Class mXtProxyClz;

    private static void load(Context context) {
        if (mLoader == null) {
            try {
                if (mXtProxyClz != null && mNotifierClz != null) {
                    return;
                }
                DexClassLoader dexClassLoader = new DexClassLoader("/system_ext/framework/izat.xt.srv.jar", context.getFilesDir().getAbsolutePath(), null, ClassLoader.getSystemClassLoader());
                mLoader = dexClassLoader;
                mXtProxyClz = Class.forName("com.qti.izat.XTProxy", true, dexClassLoader);
                mNotifierClz = Class.forName("com.qti.izat.XTProxy$Notifier", true, mLoader);
                mIzatPackage = (String) mXtProxyClz.getField("IZAT_XT_PACKAGE").get(null);
                mGetXtProxyMethod = mXtProxyClz.getMethod("getXTProxy", Context.class, mNotifierClz);
                mGetConsentMethod = mXtProxyClz.getMethod("getUserConsent", new Class[0]);
                mShowIzatMethod = mXtProxyClz.getMethod("showIzat", Context.class, String.class);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean showIzat(Context context, String str) {
        load(context);
        try {
            Method method = mShowIzatMethod;
            if (method == null) {
                return true;
            }
            return ((Boolean) method.invoke(null, context, str)).booleanValue();
        } catch (ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            return true;
        }
    }

    private static boolean isIzatPackage(Context context, InjectedSetting injectedSetting) {
        String str = mIzatPackage;
        return str != null && str.equals(injectedSetting.packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dimIcon(AppPreference appPreference, boolean z) {
        Drawable icon = appPreference.getIcon();
        if (icon != null) {
            icon.mutate().setAlpha(z ? R$styleable.Constraint_layout_goneMarginStart : 255);
            appPreference.setIcon(icon);
        }
    }

    /* loaded from: classes.dex */
    private static class IZatAppPreference extends AppPreference {
        private boolean mChecked;
        private Context mContext;

        private IZatAppPreference(Context context) {
            super(context);
            this.mContext = context;
            try {
                this.mChecked = ((Boolean) DimmableIZatIconPreference.mGetConsentMethod.invoke(DimmableIZatIconPreference.mGetXtProxyMethod.invoke(null, context, Proxy.newProxyInstance(DimmableIZatIconPreference.mLoader, new Class[]{DimmableIZatIconPreference.mNotifierClz}, new InvocationHandler() { // from class: com.android.settings.location.DimmableIZatIconPreference.IZatAppPreference.1
                    @Override // java.lang.reflect.InvocationHandler
                    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                        boolean booleanValue;
                        if (method.getName().equals("userConsentNotify")) {
                            boolean z = false;
                            if (objArr[0] == null || !(objArr[0] instanceof Boolean) || IZatAppPreference.this.mChecked == (booleanValue = ((Boolean) objArr[0]).booleanValue())) {
                                return null;
                            }
                            IZatAppPreference.this.mChecked = booleanValue;
                            IZatAppPreference iZatAppPreference = IZatAppPreference.this;
                            if (!iZatAppPreference.isEnabled() || !IZatAppPreference.this.mChecked) {
                                z = true;
                            }
                            DimmableIZatIconPreference.dimIcon(iZatAppPreference, z);
                            return null;
                        }
                        return null;
                    }
                })), new Object[0])).booleanValue();
            } catch (ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override // androidx.preference.Preference
        public CharSequence getSummary() {
            int i;
            if (!isEnabled() || !this.mChecked) {
                i = R.string.notification_toggle_off;
            } else {
                i = R.string.notification_toggle_on;
            }
            return this.mContext.getString(i);
        }

        @Override // com.android.settingslib.widget.AppPreference, androidx.preference.Preference
        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            super.onBindViewHolder(preferenceViewHolder);
            DimmableIZatIconPreference.dimIcon(this, !isEnabled() || !this.mChecked);
        }
    }

    /* loaded from: classes.dex */
    private static class IZatRestrictedAppPreference extends RestrictedAppPreference {
        private boolean mChecked;

        private IZatRestrictedAppPreference(Context context, String str) {
            super(context, str);
            try {
                this.mChecked = ((Boolean) DimmableIZatIconPreference.mGetConsentMethod.invoke(DimmableIZatIconPreference.mGetXtProxyMethod.invoke(null, context, Proxy.newProxyInstance(DimmableIZatIconPreference.mLoader, new Class[]{DimmableIZatIconPreference.mNotifierClz}, new InvocationHandler() { // from class: com.android.settings.location.DimmableIZatIconPreference.IZatRestrictedAppPreference.1
                    @Override // java.lang.reflect.InvocationHandler
                    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                        boolean booleanValue;
                        if (method.getName().equals("userConsentNotify")) {
                            boolean z = false;
                            if (objArr[0] == null || !(objArr[0] instanceof Boolean) || IZatRestrictedAppPreference.this.mChecked == (booleanValue = ((Boolean) objArr[0]).booleanValue())) {
                                return null;
                            }
                            IZatRestrictedAppPreference.this.mChecked = booleanValue;
                            IZatRestrictedAppPreference iZatRestrictedAppPreference = IZatRestrictedAppPreference.this;
                            if (!iZatRestrictedAppPreference.isEnabled() || !IZatRestrictedAppPreference.this.mChecked) {
                                z = true;
                            }
                            DimmableIZatIconPreference.dimIcon(iZatRestrictedAppPreference, z);
                            return null;
                        }
                        return null;
                    }
                })), new Object[0])).booleanValue();
            } catch (ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @Override // com.android.settings.widget.RestrictedAppPreference, com.android.settingslib.widget.AppPreference, androidx.preference.Preference
        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            super.onBindViewHolder(preferenceViewHolder);
            DimmableIZatIconPreference.dimIcon(this, !isEnabled() || !this.mChecked);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static AppPreference getAppPreference(Context context, InjectedSetting injectedSetting) {
        if (isIzatPackage(context, injectedSetting)) {
            return new IZatAppPreference(context);
        }
        return new AppPreference(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static RestrictedAppPreference getRestrictedAppPreference(Context context, InjectedSetting injectedSetting) {
        if (isIzatPackage(context, injectedSetting)) {
            return new IZatRestrictedAppPreference(context, injectedSetting.userRestriction);
        }
        return new RestrictedAppPreference(context, injectedSetting.userRestriction);
    }
}
