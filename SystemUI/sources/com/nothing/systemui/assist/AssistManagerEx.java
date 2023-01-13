package com.nothing.systemui.assist;

import android.content.Context;
import com.android.systemui.dagger.SysUISingleton;
import com.google.android.collect.Lists;
import com.nothing.systemui.util.NTLogUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.inject.Inject;

@SysUISingleton
public class AssistManagerEx {
    private static final String TAG = "AssistManagerEx";
    private final ArrayList<WeakReference<Callback>> mCallbacks = Lists.newArrayList();
    private Context mContext;
    private boolean mVoiceSessionWindowVisible = false;

    public interface Callback {
        void onVoiceSessionWindowVisibilityChanged(boolean z);
    }

    public void addCallback(Callback callback) {
        if (callback != null) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                if (this.mCallbacks.get(i).get() == callback) {
                    NTLogUtil.m1687e(TAG, "Object tried to add another callback");
                    return;
                }
            }
            NTLogUtil.m1686d(TAG, "add callback:" + callback);
            this.mCallbacks.add(new WeakReference(callback));
            callback.onVoiceSessionWindowVisibilityChanged(this.mVoiceSessionWindowVisible);
        }
    }

    public void removeCallback(Callback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).get() == callback) {
                NTLogUtil.m1686d(TAG, "remove callback:" + callback);
                this.mCallbacks.remove((Object) callback);
                return;
            }
        }
    }

    @Inject
    public AssistManagerEx(Context context) {
        this.mContext = context;
    }

    public boolean isVoiceSessionWindowVisible() {
        return this.mVoiceSessionWindowVisible;
    }

    public void onVoiceSessionWindowVisibilityChanged(boolean z) {
        NTLogUtil.m1686d(TAG, "Window visibility changed: " + z);
        this.mVoiceSessionWindowVisible = z;
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (!(this.mCallbacks.get(i) == null || this.mCallbacks.get(i).get() == null)) {
                ((Callback) this.mCallbacks.get(i).get()).onVoiceSessionWindowVisibilityChanged(z);
            }
        }
    }
}
