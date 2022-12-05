package android.content.res;

import android.content.res.Resources;
/* loaded from: classes.dex */
public abstract class ConstantState<T> {
    public abstract int getChangingConfigurations();

    /* renamed from: newInstance */
    public abstract T mo1064newInstance();

    public T newInstance(Resources res) {
        return mo1064newInstance();
    }

    public T newInstance(Resources res, Resources.Theme theme) {
        return newInstance(res);
    }
}
