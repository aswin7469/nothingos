package androidx.slice.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import androidx.collection.ArraySet;
import androidx.lifecycle.LiveData;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceMetadata;
import androidx.slice.SliceSpec;
import androidx.slice.SliceSpecs;
import androidx.slice.SliceStructure;
import androidx.slice.SliceUtils;
import androidx.slice.SliceViewManager;
import androidx.slice.core.SliceQuery;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class SliceLiveData {
    public static final SliceSpec OLD_BASIC;
    public static final SliceSpec OLD_LIST;
    public static final Set<SliceSpec> SUPPORTED_SPECS;
    private static final String TAG = "SliceLiveData";

    public interface OnErrorListener {
        public static final int ERROR_INVALID_INPUT = 3;
        public static final int ERROR_SLICE_NO_LONGER_PRESENT = 2;
        public static final int ERROR_STRUCTURE_CHANGED = 1;
        public static final int ERROR_UNKNOWN = 0;

        @Retention(RetentionPolicy.SOURCE)
        public @interface ErrorType {
        }

        void onSliceError(int i, Throwable th);
    }

    static {
        SliceSpec sliceSpec = new SliceSpec("androidx.app.slice.BASIC", 1);
        OLD_BASIC = sliceSpec;
        SliceSpec sliceSpec2 = new SliceSpec("androidx.app.slice.LIST", 1);
        OLD_LIST = sliceSpec2;
        SUPPORTED_SPECS = new ArraySet(Arrays.asList(SliceSpecs.BASIC, SliceSpecs.LIST, SliceSpecs.LIST_V2, sliceSpec, sliceSpec2));
    }

    public static LiveData<Slice> fromUri(Context context, Uri uri) {
        return new SliceLiveDataImpl(context.getApplicationContext(), uri, (OnErrorListener) null);
    }

    public static LiveData<Slice> fromUri(Context context, Uri uri, OnErrorListener onErrorListener) {
        return new SliceLiveDataImpl(context.getApplicationContext(), uri, onErrorListener);
    }

    public static LiveData<Slice> fromIntent(Context context, Intent intent) {
        return new SliceLiveDataImpl(context.getApplicationContext(), intent, (OnErrorListener) null);
    }

    public static LiveData<Slice> fromIntent(Context context, Intent intent, OnErrorListener onErrorListener) {
        return new SliceLiveDataImpl(context.getApplicationContext(), intent, onErrorListener);
    }

    public static LiveData<Slice> fromStream(Context context, InputStream inputStream, OnErrorListener onErrorListener) {
        return fromStream(context, SliceViewManager.getInstance(context), inputStream, onErrorListener);
    }

    public static CachedSliceLiveData fromCachedSlice(Context context, InputStream inputStream, OnErrorListener onErrorListener) {
        return fromStream(context, SliceViewManager.getInstance(context), inputStream, onErrorListener);
    }

    public static CachedSliceLiveData fromStream(Context context, SliceViewManager sliceViewManager, InputStream inputStream, OnErrorListener onErrorListener) {
        return new CachedSliceLiveData(context, sliceViewManager, inputStream, onErrorListener);
    }

    public static class CachedSliceLiveData extends LiveData<Slice> {
        private boolean mActive;
        final Context mContext;
        private boolean mInitialSliceLoaded;
        private InputStream mInput;
        private final OnErrorListener mListener;
        private boolean mLive;
        List<Context> mPendingContext = new ArrayList();
        List<Intent> mPendingIntent = new ArrayList();
        List<Uri> mPendingUri = new ArrayList();
        final SliceViewManager.SliceCallback mSliceCallback = new SliceViewManager.SliceCallback() {
            public void onSliceUpdated(Slice slice) {
                if (CachedSliceLiveData.this.mPendingUri.size() > 0) {
                    if (slice == null) {
                        CachedSliceLiveData.this.onSliceError(2, (Throwable) null);
                        return;
                    }
                    if (!CachedSliceLiveData.this.mStructure.equals(new SliceStructure(slice))) {
                        CachedSliceLiveData.this.onSliceError(1, (Throwable) null);
                        return;
                    } else if (SliceMetadata.from(CachedSliceLiveData.this.mContext, slice).getLoadingState() == 2) {
                        int i = 0;
                        while (i < CachedSliceLiveData.this.mPendingUri.size()) {
                            SliceItem findItem = SliceQuery.findItem(slice, CachedSliceLiveData.this.mPendingUri.get(i));
                            if (findItem != null) {
                                try {
                                    findItem.fireAction(CachedSliceLiveData.this.mPendingContext.get(i), CachedSliceLiveData.this.mPendingIntent.get(i));
                                    i++;
                                } catch (PendingIntent.CanceledException e) {
                                    CachedSliceLiveData.this.onSliceError(0, e);
                                    return;
                                }
                            } else {
                                CachedSliceLiveData.this.onSliceError(0, new NullPointerException());
                                return;
                            }
                        }
                        CachedSliceLiveData.this.mPendingUri.clear();
                        CachedSliceLiveData.this.mPendingContext.clear();
                        CachedSliceLiveData.this.mPendingIntent.clear();
                    }
                }
                CachedSliceLiveData.this.postValue(slice);
            }
        };
        private boolean mSliceCallbackRegistered;
        final SliceViewManager mSliceViewManager;
        SliceStructure mStructure;
        private final Runnable mUpdateSlice = new Runnable() {
            public void run() {
                CachedSliceLiveData.this.updateSlice();
            }
        };
        Uri mUri;

        CachedSliceLiveData(Context context, SliceViewManager sliceViewManager, InputStream inputStream, OnErrorListener onErrorListener) {
            this.mContext = context;
            this.mSliceViewManager = sliceViewManager;
            this.mUri = null;
            this.mListener = onErrorListener;
            this.mInput = inputStream;
        }

        public void parseStream() {
            loadInitialSlice();
        }

        public void goLive() {
            goLive((Uri) null, (Context) null, (Intent) null);
        }

        /* access modifiers changed from: protected */
        public synchronized void loadInitialSlice() {
            if (!this.mInitialSliceLoaded) {
                try {
                    Slice parseSlice = SliceUtils.parseSlice(this.mContext, this.mInput, "UTF-8", new SliceUtils.SliceActionListener() {
                        public void onSliceAction(Uri uri, Context context, Intent intent) {
                            CachedSliceLiveData.this.goLive(uri, context, intent);
                        }
                    });
                    this.mStructure = new SliceStructure(parseSlice);
                    this.mUri = parseSlice.getUri();
                    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                        setValue(parseSlice);
                    } else {
                        postValue(parseSlice);
                    }
                } catch (Exception e) {
                    this.mListener.onSliceError(3, e);
                }
                this.mInput = null;
                this.mInitialSliceLoaded = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void goLive(Uri uri, Context context, Intent intent) {
            this.mLive = true;
            if (uri != null) {
                this.mPendingUri.add(uri);
                this.mPendingContext.add(context);
                this.mPendingIntent.add(intent);
            }
            if (this.mActive && !this.mSliceCallbackRegistered) {
                AsyncTask.execute(this.mUpdateSlice);
                this.mSliceViewManager.registerSliceCallback(this.mUri, this.mSliceCallback);
                this.mSliceCallbackRegistered = true;
            }
        }

        /* access modifiers changed from: protected */
        public void onActive() {
            this.mActive = true;
            if (!this.mInitialSliceLoaded) {
                AsyncTask.execute(new Runnable() {
                    public void run() {
                        CachedSliceLiveData.this.loadInitialSlice();
                    }
                });
            }
            if (this.mLive && !this.mSliceCallbackRegistered) {
                AsyncTask.execute(this.mUpdateSlice);
                this.mSliceViewManager.registerSliceCallback(this.mUri, this.mSliceCallback);
                this.mSliceCallbackRegistered = true;
            }
        }

        /* access modifiers changed from: protected */
        public void onInactive() {
            this.mActive = false;
            if (this.mLive && this.mSliceCallbackRegistered) {
                this.mSliceViewManager.unregisterSliceCallback(this.mUri, this.mSliceCallback);
                this.mSliceCallbackRegistered = false;
            }
        }

        /* access modifiers changed from: package-private */
        public void onSliceError(int i, Throwable th) {
            this.mListener.onSliceError(i, th);
            if (this.mLive) {
                if (this.mSliceCallbackRegistered) {
                    this.mSliceViewManager.unregisterSliceCallback(this.mUri, this.mSliceCallback);
                    this.mSliceCallbackRegistered = false;
                }
                this.mLive = false;
            }
        }

        /* access modifiers changed from: protected */
        public void updateSlice() {
            try {
                this.mSliceCallback.onSliceUpdated(this.mSliceViewManager.bindSlice(this.mUri));
            } catch (Exception e) {
                this.mListener.onSliceError(0, e);
            }
        }
    }

    private static class SliceLiveDataImpl extends LiveData<Slice> {
        final Intent mIntent;
        final OnErrorListener mListener;
        final SliceViewManager.SliceCallback mSliceCallback = new SliceLiveData$SliceLiveDataImpl$$ExternalSyntheticLambda0(this);
        final SliceViewManager mSliceViewManager;
        private final Runnable mUpdateSlice = new Runnable() {
            public void run() {
                Slice slice;
                try {
                    if (SliceLiveDataImpl.this.mUri != null) {
                        slice = SliceLiveDataImpl.this.mSliceViewManager.bindSlice(SliceLiveDataImpl.this.mUri);
                    } else {
                        slice = SliceLiveDataImpl.this.mSliceViewManager.bindSlice(SliceLiveDataImpl.this.mIntent);
                    }
                    if (SliceLiveDataImpl.this.mUri == null && slice != null) {
                        SliceLiveDataImpl.this.mUri = slice.getUri();
                        SliceLiveDataImpl.this.mSliceViewManager.registerSliceCallback(SliceLiveDataImpl.this.mUri, SliceLiveDataImpl.this.mSliceCallback);
                    }
                    SliceLiveDataImpl.this.postValue(slice);
                } catch (IllegalArgumentException e) {
                    SliceLiveDataImpl.this.onSliceError(3, e);
                    SliceLiveDataImpl.this.postValue(null);
                } catch (Exception e2) {
                    SliceLiveDataImpl.this.onSliceError(0, e2);
                    SliceLiveDataImpl.this.postValue(null);
                }
            }
        };
        Uri mUri;

        SliceLiveDataImpl(Context context, Uri uri, OnErrorListener onErrorListener) {
            this.mSliceViewManager = SliceViewManager.getInstance(context);
            this.mUri = uri;
            this.mIntent = null;
            this.mListener = onErrorListener;
        }

        SliceLiveDataImpl(Context context, Intent intent, OnErrorListener onErrorListener) {
            this.mSliceViewManager = SliceViewManager.getInstance(context);
            this.mUri = null;
            this.mIntent = intent;
            this.mListener = onErrorListener;
        }

        /* access modifiers changed from: protected */
        public void onActive() {
            AsyncTask.execute(this.mUpdateSlice);
            Uri uri = this.mUri;
            if (uri != null) {
                this.mSliceViewManager.registerSliceCallback(uri, this.mSliceCallback);
            }
        }

        /* access modifiers changed from: protected */
        public void onInactive() {
            Uri uri = this.mUri;
            if (uri != null) {
                this.mSliceViewManager.unregisterSliceCallback(uri, this.mSliceCallback);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$new$0$androidx-slice-widget-SliceLiveData$SliceLiveDataImpl */
        public /* synthetic */ void mo22227x519941cd(Slice slice) {
            postValue(slice);
        }

        /* access modifiers changed from: package-private */
        public void onSliceError(int i, Throwable th) {
            OnErrorListener onErrorListener = this.mListener;
            if (onErrorListener != null) {
                onErrorListener.onSliceError(i, th);
            } else {
                Log.e(SliceLiveData.TAG, "Error binding slice", th);
            }
        }
    }

    private SliceLiveData() {
    }
}
