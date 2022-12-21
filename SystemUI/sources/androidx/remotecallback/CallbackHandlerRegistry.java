package androidx.remotecallback;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import androidx.collection.ArrayMap;
import java.lang.reflect.InvocationTargetException;

public class CallbackHandlerRegistry {
    private static final String TAG = "CallbackHandlerRegistry";
    public static final CallbackHandlerRegistry sInstance = new CallbackHandlerRegistry();
    private final ArrayMap<Class<? extends CallbackReceiver>, ClsHandler> mClsLookup = new ArrayMap<>();

    public interface CallbackHandler<T extends CallbackReceiver> {
        void executeCallback(Context context, T t, Bundle bundle);
    }

    public <T extends CallbackReceiver> T getAndResetStub(Class<? extends CallbackReceiver> cls, Context context, String str) {
        ensureInitialized(cls);
        ClsHandler findMap = findMap(cls);
        initStub(findMap.mCallStub, cls, context, str);
        return findMap.mCallStub;
    }

    private void initStub(CallbackReceiver callbackReceiver, Class<? extends CallbackReceiver> cls, Context context, String str) {
        ClsHandler findMap = findMap(cls);
        findMap.mContext = context;
        if (callbackReceiver instanceof ContentProvider) {
            findMap.mAuthority = determineAuthority(context, str, cls);
        } else {
            findMap.mAuthority = null;
        }
    }

    private String determineAuthority(Context context, String str, Class<?> cls) {
        if (str != null) {
            return str;
        }
        try {
            return context.getPackageManager().getProviderInfo(new ComponentName(context.getPackageName(), cls.getName()), 0).authority;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Couldn't find provider " + cls, e);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public <T extends CallbackReceiver> void ensureInitialized(Class<T> cls) {
        synchronized (this) {
            if (!this.mClsLookup.containsKey(cls)) {
                runInit(cls);
            }
        }
    }

    public <T extends CallbackReceiver> void invokeCallback(Context context, T t, Intent intent) {
        invokeCallback(context, t, intent.getExtras());
    }

    public <T extends CallbackReceiver> void invokeCallback(Context context, T t, Bundle bundle) {
        Class<?> cls = t.getClass();
        ensureInitialized(cls);
        ClsHandler findMap = findMap(cls);
        if (findMap == null) {
            Log.e(TAG, "No map found for " + cls.getName());
            return;
        }
        String string = bundle.getString(RemoteCallback.EXTRA_METHOD);
        CallbackHandler callbackHandler = findMap.mHandlers.get(string);
        if (callbackHandler == null) {
            Log.e(TAG, "No handler found for " + string + " on " + cls.getName());
        } else {
            callbackHandler.executeCallback(context, t, bundle);
        }
    }

    private ClsHandler findMap(Class<?> cls) {
        ClsHandler clsHandler;
        synchronized (this) {
            clsHandler = this.mClsLookup.get(cls);
        }
        if (clsHandler != null) {
            return clsHandler;
        }
        if (cls.getSuperclass() != null) {
            return findMap(cls.getSuperclass());
        }
        return null;
    }

    private <T extends CallbackReceiver> void runInit(Class<T> cls) {
        try {
            ClsHandler clsHandler = new ClsHandler();
            this.mClsLookup.put(cls, clsHandler);
            clsHandler.mCallStub = (CallbackReceiver) findInitClass(cls).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (InstantiationException e) {
            Log.e(TAG, "Unable to initialize " + cls.getName(), e);
        } catch (IllegalAccessException e2) {
            Log.e(TAG, "Unable to initialize " + cls.getName(), e2);
        } catch (InvocationTargetException e3) {
            Log.e(TAG, "Unable to initialize " + cls.getName(), e3);
        } catch (NoSuchMethodException e4) {
            Log.e(TAG, "Unable to initialize " + cls.getName(), e4);
        } catch (ClassNotFoundException e5) {
            Log.e(TAG, "Unable to initialize " + cls.getName(), e5);
        }
    }

    private <T extends CallbackReceiver> void registerHandler(Class<T> cls, String str, CallbackHandler<T> callbackHandler) {
        ClsHandler clsHandler = this.mClsLookup.get(cls);
        if (clsHandler != null) {
            clsHandler.mHandlers.put(str, callbackHandler);
            return;
        }
        throw new IllegalStateException("registerHandler called before init was run");
    }

    private static Class<? extends Runnable> findInitClass(Class<? extends CallbackReceiver> cls) throws ClassNotFoundException {
        return Class.forName(String.format("%s.%sInitializer", cls.getPackage().getName(), cls.getSimpleName()), false, cls.getClassLoader());
    }

    public static <T extends CallbackReceiver> void registerCallbackHandler(Class<T> cls, String str, CallbackHandler<T> callbackHandler) {
        sInstance.registerHandler(cls, str, callbackHandler);
    }

    public static RemoteCallback stubToRemoteCallback(CallbackReceiver callbackReceiver, Class<? extends CallbackReceiver> cls, Bundle bundle, String str) {
        if (callbackReceiver instanceof CallbackBase) {
            ClsHandler findMap = sInstance.findMap(cls);
            Context context = findMap.mContext;
            String str2 = findMap.mAuthority;
            findMap.mContext = null;
            findMap.mAuthority = null;
            return ((CallbackBase) callbackReceiver).toRemoteCallback(cls, context, str2, bundle, str);
        }
        throw new IllegalArgumentException("May only be called on classes that extend a *WithCallbacks base class.");
    }

    static class ClsHandler {
        public String mAuthority;
        CallbackReceiver mCallStub;
        Context mContext;
        final ArrayMap<String, CallbackHandler<? extends CallbackReceiver>> mHandlers = new ArrayMap<>();

        ClsHandler() {
        }
    }
}
