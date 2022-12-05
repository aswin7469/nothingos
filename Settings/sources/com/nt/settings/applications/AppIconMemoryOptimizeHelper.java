package com.nt.settings.applications;

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
import com.android.settings.R;
import java.util.HashMap;
/* loaded from: classes2.dex */
public class AppIconMemoryOptimizeHelper {
    private static AppIconMemoryOptimizeHelper mInstance;
    private BackgroundHandler mBackgroundHandler;
    private Context mContext;
    private Drawable mDefaultIcon;
    private Handler mMainHandler;
    private PackageManager mPackageManager;
    private int mRequreSize;
    private static final boolean DBG = Log.isLoggable("AppIconMemoryOptimizeHelper", 3);
    private static final String[] IGONORE_LIST = {"com.android.calendar"};
    private static final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 512);
    private static Object sLock = new Object();
    private LruCache<String, Drawable> mMemoryCache = new LruCache<>(6);
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.nt.settings.applications.AppIconMemoryOptimizeHelper.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            AppIconMemoryOptimizeHelper.printLog("mReceiver onReceive intent = " + action);
            if (action.equals("com.nt.theme.change")) {
                AppIconMemoryOptimizeHelper.this.emptyCache();
            }
        }
    };
    private HashMap<String, OnIconLoadedListener> mIconLoadedMap = new HashMap<>();

    /* loaded from: classes2.dex */
    public interface OnIconLoadedListener {
        void onIconLoaded(String str, Drawable drawable);
    }

    private AppIconMemoryOptimizeHelper(Context context) {
        this.mRequreSize = context.getResources().getDimensionPixelSize(R.dimen.app_icon_size);
        printLog(" AppIconMemoryOptimizeHelper maxMemory  = " + maxMemory + "KB cacheSize = 6KB");
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
        if (this.mRequreSize <= 0) {
            throw new IllegalArgumentException("must init sRequreSize before scaleIcon");
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            Drawable bitmapFromMemCache = getBitmapFromMemCache(str);
            if (bitmapFromMemCache != null) {
                return bitmapFromMemCache;
            }
            Drawable orignalIcon = getOrignalIcon(packageManager, applicationInfo);
            if (orignalIcon != null) {
                printLog("get icon from app " + str);
                addBitmapToMemoryCache(applicationInfo.packageName, orignalIcon);
                return orignalIcon;
            }
            return this.mDefaultIcon;
        } catch (PackageManager.NameNotFoundException unused) {
            return this.mDefaultIcon;
        }
    }

    private Drawable getOrignalIcon(PackageManager packageManager, ApplicationInfo applicationInfo) {
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

    /* JADX INFO: Access modifiers changed from: private */
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
            Log.d("AppIconMemoryOptimizeHelper", str);
        }
    }

    public void startLoadIcon() {
        HandlerThread handlerThread = new HandlerThread("LoadAppIcon");
        handlerThread.start();
        this.mBackgroundHandler = new BackgroundHandler(handlerThread.getLooper());
        this.mMainHandler = new Handler(Looper.getMainLooper()) { // from class: com.nt.settings.applications.AppIconMemoryOptimizeHelper.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                String str = (String) message.obj;
                OnIconLoadedListener onIconLoadedListener = (OnIconLoadedListener) AppIconMemoryOptimizeHelper.this.mIconLoadedMap.get(str);
                if (onIconLoadedListener == null) {
                    return;
                }
                onIconLoadedListener.onIconLoaded(str, AppIconMemoryOptimizeHelper.this.getBitmapFromMemCache(str));
            }
        };
    }

    public void stopLoadIcon() {
        this.mBackgroundHandler.getLooper().quitSafely();
        this.mIconLoadedMap.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            String str = (String) message.obj;
            AppIconMemoryOptimizeHelper appIconMemoryOptimizeHelper = AppIconMemoryOptimizeHelper.this;
            appIconMemoryOptimizeHelper.scaleIcon(appIconMemoryOptimizeHelper.mPackageManager, str);
            AppIconMemoryOptimizeHelper.this.mMainHandler.obtainMessage(1, str).sendToTarget();
        }
    }
}
