package androidx.emoji2.text;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import androidx.core.p004os.TraceCompat;
import androidx.emoji2.text.EmojiCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleInitializer;
import androidx.startup.AppInitializer;
import androidx.startup.Initializer;
import java.util.Collections;
import java.util.List;

public class EmojiCompatInitializer implements Initializer<Boolean> {
    private static final long STARTUP_THREAD_CREATION_DELAY_MS = 500;
    private static final String S_INITIALIZER_THREAD_NAME = "EmojiCompatInitializer";

    public Boolean create(Context context) {
        EmojiCompat.init((EmojiCompat.Config) new BackgroundDefaultConfig(context));
        delayUntilFirstResume(context);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void delayUntilFirstResume(Context context) {
        final Lifecycle lifecycle = ((LifecycleOwner) AppInitializer.getInstance(context).initializeComponent(ProcessLifecycleInitializer.class)).getLifecycle();
        lifecycle.addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void onResume() {
                EmojiCompatInitializer.this.loadEmojiCompatAfterDelay();
                lifecycle.removeObserver(this);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void loadEmojiCompatAfterDelay() {
        Handler28Impl.createAsync(Looper.getMainLooper()).postDelayed(new LoadEmojiCompatRunnable(), 500);
    }

    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.singletonList(ProcessLifecycleInitializer.class);
    }

    static class LoadEmojiCompatRunnable implements Runnable {
        LoadEmojiCompatRunnable() {
        }

        public void run() {
            try {
                TraceCompat.beginSection("EmojiCompat.EmojiCompatInitializer.run");
                if (EmojiCompat.isConfigured()) {
                    EmojiCompat.get().load();
                }
            } finally {
                TraceCompat.endSection();
            }
        }
    }

    static class BackgroundDefaultConfig extends EmojiCompat.Config {
        protected BackgroundDefaultConfig(Context context) {
            super(new BackgroundDefaultLoader(context));
            setMetadataLoadStrategy(1);
        }
    }

    static class BackgroundDefaultLoader implements EmojiCompat.MetadataRepoLoader {
        private final Context mContext;
        private HandlerThread mThread;

        BackgroundDefaultLoader(Context context) {
            this.mContext = context.getApplicationContext();
        }

        public void load(EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback) {
            Handler threadHandler = getThreadHandler();
            threadHandler.post(new C0655xfb0118ab(this, metadataRepoLoaderCallback, threadHandler));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: doLoad */
        public void mo14342x5cc8028a(final EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback, Handler handler) {
            try {
                FontRequestEmojiCompatConfig create = DefaultEmojiCompatConfig.create(this.mContext);
                if (create != null) {
                    create.setHandler(handler);
                    create.getMetadataRepoLoader().load(new EmojiCompat.MetadataRepoLoaderCallback() {
                        public void onLoaded(MetadataRepo metadataRepo) {
                            try {
                                metadataRepoLoaderCallback.onLoaded(metadataRepo);
                            } finally {
                                BackgroundDefaultLoader.this.quitHandlerThread();
                            }
                        }

                        public void onFailed(Throwable th) {
                            try {
                                metadataRepoLoaderCallback.onFailed(th);
                            } finally {
                                BackgroundDefaultLoader.this.quitHandlerThread();
                            }
                        }
                    });
                    return;
                }
                throw new RuntimeException("EmojiCompat font provider not available on this device.");
            } catch (Throwable th) {
                metadataRepoLoaderCallback.onFailed(th);
                quitHandlerThread();
            }
        }

        /* access modifiers changed from: package-private */
        public void quitHandlerThread() {
            HandlerThread handlerThread = this.mThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
            }
        }

        private Handler getThreadHandler() {
            HandlerThread handlerThread = new HandlerThread(EmojiCompatInitializer.S_INITIALIZER_THREAD_NAME, 10);
            this.mThread = handlerThread;
            handlerThread.start();
            return new Handler(this.mThread.getLooper());
        }
    }

    private static class Handler28Impl {
        private Handler28Impl() {
        }

        public static Handler createAsync(Looper looper) {
            return Handler.createAsync(looper);
        }
    }
}
