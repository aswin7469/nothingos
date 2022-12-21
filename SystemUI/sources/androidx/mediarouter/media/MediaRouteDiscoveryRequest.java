package androidx.mediarouter.media;

import android.os.Bundle;

public final class MediaRouteDiscoveryRequest {
    private static final String KEY_ACTIVE_SCAN = "activeScan";
    private static final String KEY_SELECTOR = "selector";
    private final Bundle mBundle;
    private MediaRouteSelector mSelector;

    public MediaRouteDiscoveryRequest(MediaRouteSelector mediaRouteSelector, boolean z) {
        if (mediaRouteSelector != null) {
            Bundle bundle = new Bundle();
            this.mBundle = bundle;
            this.mSelector = mediaRouteSelector;
            bundle.putBundle(KEY_SELECTOR, mediaRouteSelector.asBundle());
            bundle.putBoolean(KEY_ACTIVE_SCAN, z);
            return;
        }
        throw new IllegalArgumentException("selector must not be null");
    }

    private MediaRouteDiscoveryRequest(Bundle bundle) {
        this.mBundle = bundle;
    }

    public MediaRouteSelector getSelector() {
        ensureSelector();
        return this.mSelector;
    }

    private void ensureSelector() {
        if (this.mSelector == null) {
            MediaRouteSelector fromBundle = MediaRouteSelector.fromBundle(this.mBundle.getBundle(KEY_SELECTOR));
            this.mSelector = fromBundle;
            if (fromBundle == null) {
                this.mSelector = MediaRouteSelector.EMPTY;
            }
        }
    }

    public boolean isActiveScan() {
        return this.mBundle.getBoolean(KEY_ACTIVE_SCAN);
    }

    public boolean isValid() {
        ensureSelector();
        return this.mSelector.isValid();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MediaRouteDiscoveryRequest)) {
            return false;
        }
        MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest = (MediaRouteDiscoveryRequest) obj;
        if (!getSelector().equals(mediaRouteDiscoveryRequest.getSelector()) || isActiveScan() != mediaRouteDiscoveryRequest.isActiveScan()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return isActiveScan() ^ getSelector().hashCode() ? 1 : 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DiscoveryRequest{ selector=");
        sb.append((Object) getSelector());
        sb.append(", activeScan=").append(isActiveScan());
        sb.append(", isValid=").append(isValid());
        sb.append(" }");
        return sb.toString();
    }

    public Bundle asBundle() {
        return this.mBundle;
    }

    public static MediaRouteDiscoveryRequest fromBundle(Bundle bundle) {
        if (bundle != null) {
            return new MediaRouteDiscoveryRequest(bundle);
        }
        return null;
    }
}
