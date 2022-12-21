package androidx.emoji2.text;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.p004os.TraceCompat;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;
import androidx.core.util.Preconditions;
import androidx.emoji2.text.EmojiCompat;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.ByteBuffer;

public class FontRequestEmojiCompatConfig extends EmojiCompat.Config {
    private static final FontProviderHelper DEFAULT_FONTS_CONTRACT = new FontProviderHelper();

    public static abstract class RetryPolicy {
        public abstract long getRetryDelay();
    }

    public static class ExponentialBackoffRetryPolicy extends RetryPolicy {
        private long mRetryOrigin;
        private final long mTotalMs;

        public ExponentialBackoffRetryPolicy(long j) {
            this.mTotalMs = j;
        }

        public long getRetryDelay() {
            if (this.mRetryOrigin == 0) {
                this.mRetryOrigin = SystemClock.uptimeMillis();
                return 0;
            }
            long uptimeMillis = SystemClock.uptimeMillis() - this.mRetryOrigin;
            if (uptimeMillis > this.mTotalMs) {
                return -1;
            }
            return Math.min(Math.max(uptimeMillis, 1000), this.mTotalMs - uptimeMillis);
        }
    }

    public FontRequestEmojiCompatConfig(Context context, FontRequest fontRequest) {
        super(new FontRequestMetadataLoader(context, fontRequest, DEFAULT_FONTS_CONTRACT));
    }

    public FontRequestEmojiCompatConfig(Context context, FontRequest fontRequest, FontProviderHelper fontProviderHelper) {
        super(new FontRequestMetadataLoader(context, fontRequest, fontProviderHelper));
    }

    public FontRequestEmojiCompatConfig setHandler(Handler handler) {
        ((FontRequestMetadataLoader) getMetadataRepoLoader()).setHandler(handler);
        return this;
    }

    public FontRequestEmojiCompatConfig setRetryPolicy(RetryPolicy retryPolicy) {
        ((FontRequestMetadataLoader) getMetadataRepoLoader()).setRetryPolicy(retryPolicy);
        return this;
    }

    private static class FontRequestMetadataLoader implements EmojiCompat.MetadataRepoLoader {
        private static final String S_TRACE_BUILD_TYPEFACE = "EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface";
        private static final String S_TRACE_THREAD_CREATION = "EmojiCompat.FontRequestEmojiCompatConfig.threadCreation";
        EmojiCompat.MetadataRepoLoaderCallback mCallback;
        private final Context mContext;
        private final FontProviderHelper mFontProviderHelper;
        private Runnable mHandleMetadataCreationRunner;
        private Handler mHandler;
        private final Object mLock = new Object();
        private ContentObserver mObserver;
        private final FontRequest mRequest;
        private RetryPolicy mRetryPolicy;
        private HandlerThread mThread;

        FontRequestMetadataLoader(Context context, FontRequest fontRequest, FontProviderHelper fontProviderHelper) {
            Preconditions.checkNotNull(context, "Context cannot be null");
            Preconditions.checkNotNull(fontRequest, "FontRequest cannot be null");
            this.mContext = context.getApplicationContext();
            this.mRequest = fontRequest;
            this.mFontProviderHelper = fontProviderHelper;
        }

        public void setHandler(Handler handler) {
            synchronized (this.mLock) {
                this.mHandler = handler;
            }
        }

        public void setRetryPolicy(RetryPolicy retryPolicy) {
            synchronized (this.mLock) {
                this.mRetryPolicy = retryPolicy;
            }
        }

        public void load(final EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback) {
            Preconditions.checkNotNull(metadataRepoLoaderCallback, "LoaderCallback cannot be null");
            synchronized (this.mLock) {
                try {
                    TraceCompat.beginSection(S_TRACE_THREAD_CREATION);
                    if (this.mHandler == null) {
                        HandlerThread handlerThread = new HandlerThread("emojiCompat", 10);
                        this.mThread = handlerThread;
                        handlerThread.start();
                        this.mHandler = new Handler(this.mThread.getLooper());
                    }
                    TraceCompat.endSection();
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            FontRequestMetadataLoader.this.mCallback = metadataRepoLoaderCallback;
                            FontRequestMetadataLoader.this.createMetadata();
                        }
                    });
                } catch (Throwable th) {
                    TraceCompat.endSection();
                    throw th;
                }
            }
        }

        private FontsContractCompat.FontInfo retrieveFontInfo() {
            try {
                FontsContractCompat.FontFamilyResult fetchFonts = this.mFontProviderHelper.fetchFonts(this.mContext, this.mRequest);
                if (fetchFonts.getStatusCode() == 0) {
                    FontsContractCompat.FontInfo[] fonts = fetchFonts.getFonts();
                    if (fonts != null && fonts.length != 0) {
                        return fonts[0];
                    }
                    throw new RuntimeException("fetchFonts failed (empty result)");
                }
                throw new RuntimeException("fetchFonts failed (" + fetchFonts.getStatusCode() + NavigationBarInflaterView.KEY_CODE_END);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException("provider not found", e);
            }
        }

        private void scheduleRetry(Uri uri, long j) {
            synchronized (this.mLock) {
                if (this.mObserver == null) {
                    C06572 r1 = new ContentObserver(this.mHandler) {
                        public void onChange(boolean z, Uri uri) {
                            FontRequestMetadataLoader.this.createMetadata();
                        }
                    };
                    this.mObserver = r1;
                    this.mFontProviderHelper.registerObserver(this.mContext, uri, r1);
                }
                if (this.mHandleMetadataCreationRunner == null) {
                    this.mHandleMetadataCreationRunner = new Runnable() {
                        public void run() {
                            FontRequestMetadataLoader.this.createMetadata();
                        }
                    };
                }
                this.mHandler.postDelayed(this.mHandleMetadataCreationRunner, j);
            }
        }

        private void cleanUp() {
            this.mCallback = null;
            ContentObserver contentObserver = this.mObserver;
            if (contentObserver != null) {
                this.mFontProviderHelper.unregisterObserver(this.mContext, contentObserver);
                this.mObserver = null;
            }
            synchronized (this.mLock) {
                this.mHandler.removeCallbacks(this.mHandleMetadataCreationRunner);
                HandlerThread handlerThread = this.mThread;
                if (handlerThread != null) {
                    handlerThread.quit();
                }
                this.mHandler = null;
                this.mThread = null;
            }
        }

        /* access modifiers changed from: package-private */
        public void createMetadata() {
            if (this.mCallback != null) {
                try {
                    FontsContractCompat.FontInfo retrieveFontInfo = retrieveFontInfo();
                    int resultCode = retrieveFontInfo.getResultCode();
                    if (resultCode == 2) {
                        synchronized (this.mLock) {
                            RetryPolicy retryPolicy = this.mRetryPolicy;
                            if (retryPolicy != null) {
                                long retryDelay = retryPolicy.getRetryDelay();
                                if (retryDelay >= 0) {
                                    scheduleRetry(retrieveFontInfo.getUri(), retryDelay);
                                    return;
                                }
                            }
                        }
                    }
                    if (resultCode == 0) {
                        TraceCompat.beginSection(S_TRACE_BUILD_TYPEFACE);
                        Typeface buildTypeface = this.mFontProviderHelper.buildTypeface(this.mContext, retrieveFontInfo);
                        ByteBuffer mmap = TypefaceCompatUtil.mmap(this.mContext, (CancellationSignal) null, retrieveFontInfo.getUri());
                        if (mmap != null) {
                            MetadataRepo create = MetadataRepo.create(buildTypeface, mmap);
                            TraceCompat.endSection();
                            this.mCallback.onLoaded(create);
                            cleanUp();
                            return;
                        }
                        throw new RuntimeException("Unable to open file.");
                    }
                    throw new RuntimeException("fetchFonts result is not OK. (" + resultCode + NavigationBarInflaterView.KEY_CODE_END);
                } catch (Throwable th) {
                    this.mCallback.onFailed(th);
                    cleanUp();
                }
            }
        }
    }

    public static class FontProviderHelper {
        public FontsContractCompat.FontFamilyResult fetchFonts(Context context, FontRequest fontRequest) throws PackageManager.NameNotFoundException {
            return FontsContractCompat.fetchFonts(context, (CancellationSignal) null, fontRequest);
        }

        public Typeface buildTypeface(Context context, FontsContractCompat.FontInfo fontInfo) throws PackageManager.NameNotFoundException {
            return FontsContractCompat.buildTypeface(context, (CancellationSignal) null, new FontsContractCompat.FontInfo[]{fontInfo});
        }

        public void registerObserver(Context context, Uri uri, ContentObserver contentObserver) {
            context.getContentResolver().registerContentObserver(uri, false, contentObserver);
        }

        public void unregisterObserver(Context context, ContentObserver contentObserver) {
            context.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }
}
