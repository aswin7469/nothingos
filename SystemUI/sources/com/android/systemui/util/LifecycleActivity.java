package com.android.systemui.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0005H\u0016J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0014J\u001c\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\b\u0010\r\u001a\u00020\bH\u0014J\b\u0010\u000e\u001a\u00020\bH\u0014J\b\u0010\u000f\u001a\u00020\bH\u0014J\b\u0010\u0010\u001a\u00020\bH\u0014J\b\u0010\u0011\u001a\u00020\bH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/systemui/util/LifecycleActivity;", "Landroid/app/Activity;", "Landroidx/lifecycle/LifecycleOwner;", "()V", "lifecycle", "Lcom/android/settingslib/core/lifecycle/Lifecycle;", "getLifecycle", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "persistentState", "Landroid/os/PersistableBundle;", "onDestroy", "onPause", "onResume", "onStart", "onStop", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LifecycleActivity.kt */
public class LifecycleActivity extends Activity implements LifecycleOwner {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final Lifecycle lifecycle = new Lifecycle(this);

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public Lifecycle getLifecycle() {
        return this.lifecycle;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle);
    }

    public void onCreate(Bundle bundle, PersistableBundle persistableBundle) {
        this.lifecycle.onAttach(this);
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle, persistableBundle);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }
}
