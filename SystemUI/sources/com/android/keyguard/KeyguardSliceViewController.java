package com.android.keyguard;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Trace;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.slice.Slice;
import androidx.slice.SliceViewManager;
import androidx.slice.widget.ListContent;
import androidx.slice.widget.RowContent;
import androidx.slice.widget.SliceContent;
import androidx.slice.widget.SliceLiveData;
import com.android.keyguard.dagger.KeyguardStatusViewScope;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
import java.p026io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;

@KeyguardStatusViewScope
public class KeyguardSliceViewController extends ViewController<KeyguardSliceView> implements Dumpable {
    private static final String TAG = "KeyguardSliceViewCtrl";
    /* access modifiers changed from: private */
    public final ActivityStarter mActivityStarter;
    /* access modifiers changed from: private */
    public Map<View, PendingIntent> mClickActions;
    private final ConfigurationController mConfigurationController;
    ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onDensityOrFontScaleChanged() {
            ((KeyguardSliceView) KeyguardSliceViewController.this.mView).onDensityOrFontScaleChanged();
        }

        public void onThemeChanged() {
            ((KeyguardSliceView) KeyguardSliceViewController.this.mView).onOverlayChanged();
        }
    };
    private int mDisplayId;
    private final DumpManager mDumpManager;
    private Uri mKeyguardSliceUri;
    private LiveData<Slice> mLiveData;
    Observer<Slice> mObserver = new Observer<Slice>() {
        public void onChanged(Slice slice) {
            Slice unused = KeyguardSliceViewController.this.mSlice = slice;
            KeyguardSliceViewController.this.showSlice(slice);
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            PendingIntent pendingIntent = (PendingIntent) KeyguardSliceViewController.this.mClickActions.get(view);
            if (pendingIntent != null && KeyguardSliceViewController.this.mActivityStarter != null) {
                KeyguardSliceViewController.this.mActivityStarter.startPendingIntentDismissingKeyguard(pendingIntent);
            }
        }
    };
    /* access modifiers changed from: private */
    public Slice mSlice;
    TunerService.Tunable mTunable = new KeyguardSliceViewController$$ExternalSyntheticLambda0(this);
    private final TunerService mTunerService;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardSliceViewController  reason: not valid java name */
    public /* synthetic */ void m2297lambda$new$0$comandroidkeyguardKeyguardSliceViewController(String str, String str2) {
        setupUri(str2);
    }

    @Inject
    public KeyguardSliceViewController(KeyguardSliceView keyguardSliceView, ActivityStarter activityStarter, ConfigurationController configurationController, TunerService tunerService, DumpManager dumpManager) {
        super(keyguardSliceView);
        this.mActivityStarter = activityStarter;
        this.mConfigurationController = configurationController;
        this.mTunerService = tunerService;
        this.mDumpManager = dumpManager;
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        LiveData<Slice> liveData;
        Display display = ((KeyguardSliceView) this.mView).getDisplay();
        if (display != null) {
            this.mDisplayId = display.getDisplayId();
        }
        this.mTunerService.addTunable(this.mTunable, "keyguard_slice_uri");
        if (this.mDisplayId == 0 && (liveData = this.mLiveData) != null) {
            liveData.observeForever(this.mObserver);
        }
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mDumpManager.registerDumpable("KeyguardSliceViewCtrl@" + Integer.toHexString(hashCode()), this);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        if (this.mDisplayId == 0) {
            this.mLiveData.removeObserver(this.mObserver);
        }
        this.mTunerService.removeTunable(this.mTunable);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mDumpManager.unregisterDumpable("KeyguardSliceViewCtrl@" + Integer.toHexString(hashCode()));
    }

    /* access modifiers changed from: package-private */
    public void updateTopMargin(float f) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) ((KeyguardSliceView) this.mView).getLayoutParams();
        marginLayoutParams.topMargin = (int) f;
        ((KeyguardSliceView) this.mView).setLayoutParams(marginLayoutParams);
    }

    public void setupUri(String str) {
        boolean z;
        if (str == null) {
            str = KeyguardSliceProvider.KEYGUARD_SLICE_URI;
        }
        LiveData<Slice> liveData = this.mLiveData;
        if (liveData == null || !liveData.hasActiveObservers()) {
            z = false;
        } else {
            this.mLiveData.removeObserver(this.mObserver);
            z = true;
        }
        this.mKeyguardSliceUri = Uri.parse(str);
        LiveData<Slice> fromUri = SliceLiveData.fromUri(((KeyguardSliceView) this.mView).getContext(), this.mKeyguardSliceUri);
        this.mLiveData = fromUri;
        if (z) {
            fromUri.observeForever(this.mObserver);
        }
    }

    public void refresh() {
        Slice slice;
        Trace.beginSection("KeyguardSliceViewController#refresh");
        if (KeyguardSliceProvider.KEYGUARD_SLICE_URI.equals(this.mKeyguardSliceUri.toString())) {
            KeyguardSliceProvider attachedInstance = KeyguardSliceProvider.getAttachedInstance();
            if (attachedInstance != null) {
                slice = attachedInstance.onBindSlice(this.mKeyguardSliceUri);
            } else {
                Log.w(TAG, "Keyguard slice not bound yet?");
                slice = null;
            }
        } else {
            slice = SliceViewManager.getInstance(((KeyguardSliceView) this.mView).getContext()).bindSlice(this.mKeyguardSliceUri);
        }
        this.mObserver.onChanged(slice);
        Trace.endSection();
    }

    /* access modifiers changed from: package-private */
    public void showSlice(Slice slice) {
        Trace.beginSection("KeyguardSliceViewController#showSlice");
        if (slice == null) {
            ((KeyguardSliceView) this.mView).hideSlice();
            Trace.endSection();
            return;
        }
        ListContent listContent = new ListContent(slice);
        RowContent header = listContent.getHeader();
        boolean z = header != null && !header.getSliceItem().hasHint("list_item");
        List list = (List) listContent.getRowItems().stream().filter(new KeyguardSliceViewController$$ExternalSyntheticLambda1()).collect(Collectors.toList());
        KeyguardSliceView keyguardSliceView = (KeyguardSliceView) this.mView;
        if (!z) {
            header = null;
        }
        this.mClickActions = keyguardSliceView.showSlice(header, list);
        Trace.endSection();
    }

    static /* synthetic */ boolean lambda$showSlice$1(SliceContent sliceContent) {
        return !KeyguardSliceProvider.KEYGUARD_ACTION_URI.equals(sliceContent.getSliceItem().getSlice().getUri().toString());
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("  mSlice: " + this.mSlice);
        printWriter.println("  mClickActions: " + this.mClickActions);
    }
}
