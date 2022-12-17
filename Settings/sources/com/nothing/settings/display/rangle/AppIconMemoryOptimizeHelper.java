package com.nothing.settings.display.rangle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import com.android.settings.R$dimen;
import java.util.HashMap;

public class AppIconMemoryOptimizeHelper {
    private static final boolean DBG = Log.isLoggable("RAngle", 3);
    private static final String[] IGONORE_LIST = {"com.android.calendar"};
    private static final int cacheSize;
    private static AppIconMemoryOptimizeHelper mInstance;
    private static final int maxMemory;
    private static Object sLock = new Object();
    private BackgroundHandler mBackgroundHandler;
    private Context mContext;
    private Drawable mDefaultIcon;
    /* access modifiers changed from: private */
    public HashMap<String, OnIconLoadedListener> mIconLoadedMap = new HashMap<>();
    /* access modifiers changed from: private */
    public Handler mMainHandler;
    private LruCache<String, Drawable> mMemoryCache;
    /* access modifiers changed from: private */
    public PackageManager mPackageManager;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            AppIconMemoryOptimizeHelper.printLog("mReceiver onReceive intent = " + action);
            if (action.equals("com.nt.theme.change")) {
                AppIconMemoryOptimizeHelper.this.emptyCache();
            }
        }
    };
    private int mRequireSize;

    public interface OnIconLoadedListener {
        void onIconLoaded(String str, Drawable drawable);
    }

    static {
        int maxMemory2 = (int) (Runtime.getRuntime().maxMemory() / 1024);
        maxMemory = maxMemory2;
        cacheSize = maxMemory2 / 16;
    }

    private AppIconMemoryOptimizeHelper(Context context) {
        int i = cacheSize;
        this.mMemoryCache = new LruCache<>(i);
        this.mRequireSize = context.getResources().getDimensionPixelSize(R$dimen.app_icon_size);
        printLog(" AppIconMemoryOptimizeHelper maxMemory  = " + maxMemory + "KB cacheSize = " + i + "KB");
        this.mDefaultIcon = context.getPackageManager().getDefaultActivityIcon();
        this.mContext = context.getApplicationContext();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.nt.theme.change");
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
        this.mPackageManager = this.mContext.getPackageManager();
    }

    public static AppIconMemoryOptimizeHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (sLock) {
                if (mInstance == null) {
                    mInstance = new AppIconMemoryOptimizeHelper(context);
                }
            }
        }
        return mInstance;
    }

    public Drawable scaleIcon(PackageManager packageManager, String str) {
        if (this.mRequireSize > 0) {
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
                Drawable bitmapFromMemCache = getBitmapFromMemCache(str);
                if (bitmapFromMemCache != null) {
                    return bitmapFromMemCache;
                }
                Drawable originalIcon = getOriginalIcon(packageManager, applicationInfo);
                if (originalIcon == null) {
                    return this.mDefaultIcon;
                }
                printLog("get icon from app " + str);
                addBitmapToMemoryCache(applicationInfo.packageName, originalIcon);
                return originalIcon;
            } catch (PackageManager.NameNotFoundException unused) {
                return this.mDefaultIcon;
            }
        } else {
            throw new IllegalArgumentException("must init mRequireSize before scaleIcon");
        }
    }

    private Drawable getOriginalIcon(PackageManager packageManager, ApplicationInfo applicationInfo) {
        try {
            packageManager.getResourcesForApplication(applicationInfo);
            Drawable loadIcon = applicationInfo.loadIcon(packageManager);
            if (loadIcon != null) {
                addBitmapToMemoryCache(applicationInfo.packageName, loadIcon);
            }
            return loadIcon;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public void addBitmapToMemoryCache(String str, Drawable drawable) {
        if (getBitmapFromMemCache(str) == null) {
            synchronized (sLock) {
                if (getBitmapFromMemCache(str) == null) {
                    this.mMemoryCache.put(str, drawable);
                    printLog("addBitmapToMemoryCache key = " + str + "  bitmap = " + drawable);
                }
            }
        }
    }

    public Drawable getBitmapFromMemCache(String str) {
        return this.mMemoryCache.get(str);
    }

    public void emptyCache() {
        printLog("emptyCache");
        synchronized (sLock) {
            this.mMemoryCache.evictAll();
            System.gc();
        }
    }

    public static void printLog(String str) {
        if (DBG) {
            Log.d("RAngle", str);
        }
    }

    public void startLoadIcon() {
        HandlerThread handlerThread = new HandlerThread("LoadAppIcon");
        handlerThread.start();
        this.mBackgroundHandler = new BackgroundHandler(handlerThread.getLooper());
        this.mMainHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                if (message.what == 1) {
                    String str = (String) message.obj;
                    OnIconLoadedListener onIconLoadedListener = (OnIconLoadedListener) AppIconMemoryOptimizeHelper.this.mIconLoadedMap.get(str);
                    if (onIconLoadedListener != null) {
                        onIconLoadedListener.onIconLoaded(str, AppIconMemoryOptimizeHelper.this.getBitmapFromMemCache(str));
                    }
                }
            }
        };
    }

    public void stopLoadIcon() {
        this.mBackgroundHandler.getLooper().quitSafely();
        this.mIconLoadedMap.clear();
    }

    public void getIcon(String str, OnIconLoadedListener onIconLoadedListener) {
        this.mIconLoadedMap.remove(str);
        this.mIconLoadedMap.put(str, onIconLoadedListener);
        Drawable bitmapFromMemCache = getBitmapFromMemCache(str);
        if (bitmapFromMemCache != null) {
            onIconLoadedListener.onIconLoaded(str, bitmapFromMemCache);
            return;
        }
        onIconLoadedListener.onIconLoaded(str, this.mDefaultIcon);
        this.mBackgroundHandler.obtainMessage(1, str).sendToTarget();
    }

    class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                String str = (String) message.obj;
                AppIconMemoryOptimizeHelper appIconMemoryOptimizeHelper = AppIconMemoryOptimizeHelper.this;
                appIconMemoryOptimizeHelper.scaleIcon(appIconMemoryOptimizeHelper.mPackageManager, str);
                AppIconMemoryOptimizeHelper.this.mMainHandler.obtainMessage(1, str).sendToTarget();
            }
        }
    }
}
