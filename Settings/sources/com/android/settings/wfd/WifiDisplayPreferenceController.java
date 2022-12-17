package com.android.settings.wfd;

import android.content.Context;
import android.content.IntentFilter;
import android.media.MediaRouter;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class WifiDisplayPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop {
    /* access modifiers changed from: private */
    public Preference mPreference;
    private final MediaRouter mRouter;
    private final MediaRouter.Callback mRouterCallback = new MediaRouter.SimpleCallback() {
        public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            WifiDisplayPreferenceController wifiDisplayPreferenceController = WifiDisplayPreferenceController.this;
            wifiDisplayPreferenceController.refreshSummary(wifiDisplayPreferenceController.mPreference);
        }

        public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            WifiDisplayPreferenceController wifiDisplayPreferenceController = WifiDisplayPreferenceController.this;
            wifiDisplayPreferenceController.refreshSummary(wifiDisplayPreferenceController.mPreference);
        }

        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            WifiDisplayPreferenceController wifiDisplayPreferenceController = WifiDisplayPreferenceController.this;
            wifiDisplayPreferenceController.refreshSummary(wifiDisplayPreferenceController.mPreference);
        }

        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            WifiDisplayPreferenceController wifiDisplayPreferenceController = WifiDisplayPreferenceController.this;
            wifiDisplayPreferenceController.refreshSummary(wifiDisplayPreferenceController.mPreference);
        }

        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            WifiDisplayPreferenceController wifiDisplayPreferenceController = WifiDisplayPreferenceController.this;
            wifiDisplayPreferenceController.refreshSummary(wifiDisplayPreferenceController.mPreference);
        }
    };

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public WifiDisplayPreferenceController(Context context, String str) {
        super(context, str);
        MediaRouter mediaRouter = (MediaRouter) context.getSystemService(MediaRouter.class);
        this.mRouter = mediaRouter;
        mediaRouter.setRouterGroupId("android.media.mirroring_group");
    }

    public int getAvailabilityStatus() {
        return WifiDisplaySettings.isAvailable(this.mContext) ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public CharSequence getSummary() {
        String string = this.mContext.getString(R$string.disconnected);
        int routeCount = this.mRouter.getRouteCount();
        int i = 0;
        while (i < routeCount) {
            MediaRouter.RouteInfo routeAt = this.mRouter.getRouteAt(i);
            if (!routeAt.matchesTypes(4) || !routeAt.isSelected() || routeAt.isConnecting()) {
                i++;
            } else {
                CharSequence status = routeAt.getStatus();
                return !TextUtils.isEmpty(status) ? status : this.mContext.getString(R$string.wifi_display_status_connected);
            }
        }
        return string;
    }

    public void onStart() {
        this.mRouter.addCallback(4, this.mRouterCallback);
    }

    public void onStop() {
        this.mRouter.removeCallback(this.mRouterCallback);
    }
}
