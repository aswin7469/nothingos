package com.android.systemui.fragments;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.p012qs.QSFragment;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.Subcomponent;
import java.lang.reflect.Method;
import java.p026io.FileDescriptor;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@SysUISingleton
public class FragmentService implements Dumpable {
    private static final String TAG = "FragmentService";
    private ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onConfigChanged(Configuration configuration) {
            for (FragmentHostState sendConfigurationChange : FragmentService.this.mHosts.values()) {
                sendConfigurationChange.sendConfigurationChange(configuration);
            }
        }
    };
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public final ArrayMap<View, FragmentHostState> mHosts = new ArrayMap<>();
    private final ArrayMap<String, FragmentInstantiationInfo> mInjectionMap = new ArrayMap<>();

    @Subcomponent
    public interface FragmentCreator {

        @Subcomponent.Factory
        public interface Factory {
            FragmentCreator build();
        }

        QSFragment createQSFragment();
    }

    @Inject
    public FragmentService(FragmentCreator.Factory factory, ConfigurationController configurationController, DumpManager dumpManager) {
        addFragmentInstantiationProvider(factory.build());
        configurationController.addCallback(this.mConfigurationListener);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    /* access modifiers changed from: package-private */
    public ArrayMap<String, FragmentInstantiationInfo> getInjectionMap() {
        return this.mInjectionMap;
    }

    public void addFragmentInstantiationProvider(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (Fragment.class.isAssignableFrom(method.getReturnType()) && (method.getModifiers() & 1) != 0) {
                String name = method.getReturnType().getName();
                if (this.mInjectionMap.containsKey(name)) {
                    Log.w(TAG, "Fragment " + name + " is already provided by different Dagger component; Not adding method");
                } else {
                    this.mInjectionMap.put(name, new FragmentInstantiationInfo(method, obj));
                }
            }
        }
    }

    public FragmentHostManager getFragmentHostManager(View view) {
        View rootView = view.getRootView();
        FragmentHostState fragmentHostState = this.mHosts.get(rootView);
        if (fragmentHostState == null) {
            fragmentHostState = new FragmentHostState(rootView);
            this.mHosts.put(rootView, fragmentHostState);
        }
        return fragmentHostState.getFragmentHostManager();
    }

    public void removeAndDestroy(View view) {
        FragmentHostState remove = this.mHosts.remove(view.getRootView());
        if (remove != null) {
            remove.mFragmentHostManager.destroy();
        }
    }

    public void destroyAll() {
        for (FragmentHostState access$100 : this.mHosts.values()) {
            access$100.mFragmentHostManager.destroy();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("Dumping fragments:");
        for (FragmentHostState access$100 : this.mHosts.values()) {
            access$100.mFragmentHostManager.getFragmentManager().dump("  ", (FileDescriptor) null, printWriter, strArr);
        }
    }

    private class FragmentHostState {
        /* access modifiers changed from: private */
        public FragmentHostManager mFragmentHostManager;
        private final View mView;

        public FragmentHostState(View view) {
            this.mView = view;
            this.mFragmentHostManager = new FragmentHostManager(FragmentService.this, view);
        }

        public void sendConfigurationChange(Configuration configuration) {
            FragmentService.this.mHandler.post(new FragmentService$FragmentHostState$$ExternalSyntheticLambda0(this, configuration));
        }

        public FragmentHostManager getFragmentHostManager() {
            return this.mFragmentHostManager;
        }

        /* access modifiers changed from: private */
        /* renamed from: handleSendConfigurationChange */
        public void mo32854x9e9fc2a7(Configuration configuration) {
            this.mFragmentHostManager.onConfigurationChanged(configuration);
        }
    }

    static class FragmentInstantiationInfo {
        final Object mDaggerComponent;
        final Method mMethod;

        FragmentInstantiationInfo(Method method, Object obj) {
            this.mMethod = method;
            this.mDaggerComponent = obj;
        }
    }
}
