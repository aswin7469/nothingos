package androidx.activity.result.contract;

import android.annotation.SuppressLint;
import android.content.Intent;
/* loaded from: classes.dex */
public abstract class ActivityResultContract<I, O> {
    @SuppressLint({"UnknownNullness"})
    /* renamed from: parseResult */
    public abstract O mo101parseResult(int i, Intent intent);
}
