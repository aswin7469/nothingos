package com.android.settingslib.suggestions;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.service.settings.suggestions.Suggestion;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.suggestions.SuggestionController;
import java.util.List;

public class SuggestionControllerMixinCompat implements SuggestionController.ServiceConnectionListener, LifecycleObserver, LoaderManager.LoaderCallbacks<List<Suggestion>> {
    private static final boolean DEBUG = false;
    private static final String TAG = "SuggestionCtrlMixin";
    private final Context mContext;
    private final SuggestionControllerHost mHost;
    private final SuggestionController mSuggestionController;
    private boolean mSuggestionLoaded;

    public interface SuggestionControllerHost {
        LoaderManager getLoaderManager();

        void onSuggestionReady(List<Suggestion> list);
    }

    public SuggestionControllerMixinCompat(Context context, SuggestionControllerHost suggestionControllerHost, Lifecycle lifecycle, ComponentName componentName) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mHost = suggestionControllerHost;
        this.mSuggestionController = new SuggestionController(applicationContext, componentName, this);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        this.mSuggestionController.start();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        this.mSuggestionController.stop();
    }

    public void onServiceConnected() {
        LoaderManager loaderManager = this.mHost.getLoaderManager();
        if (loaderManager != null) {
            loaderManager.restartLoader(42, (Bundle) null, this);
        }
    }

    public void onServiceDisconnected() {
        LoaderManager loaderManager = this.mHost.getLoaderManager();
        if (loaderManager != null) {
            loaderManager.destroyLoader(42);
        }
    }

    public Loader<List<Suggestion>> onCreateLoader(int i, Bundle bundle) {
        if (i == 42) {
            this.mSuggestionLoaded = false;
            return new SuggestionLoaderCompat(this.mContext, this.mSuggestionController);
        }
        throw new IllegalArgumentException("This loader id is not supported " + i);
    }

    public void onLoadFinished(Loader<List<Suggestion>> loader, List<Suggestion> list) {
        this.mSuggestionLoaded = true;
        this.mHost.onSuggestionReady(list);
    }

    public void onLoaderReset(Loader<List<Suggestion>> loader) {
        this.mSuggestionLoaded = false;
    }

    public boolean isSuggestionLoaded() {
        return this.mSuggestionLoaded;
    }

    public void dismissSuggestion(Suggestion suggestion) {
        this.mSuggestionController.dismissSuggestions(suggestion);
    }

    public void launchSuggestion(Suggestion suggestion) {
        this.mSuggestionController.launchSuggestion(suggestion);
    }
}
