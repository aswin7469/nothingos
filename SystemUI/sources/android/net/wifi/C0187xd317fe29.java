package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$SuggestionConnectionStatusListenerProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0187xd317fe29 implements Runnable {
    public final /* synthetic */ WifiManager.SuggestionConnectionStatusListenerProxy f$0;
    public final /* synthetic */ WifiNetworkSuggestion f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ C0187xd317fe29(WifiManager.SuggestionConnectionStatusListenerProxy suggestionConnectionStatusListenerProxy, WifiNetworkSuggestion wifiNetworkSuggestion, int i) {
        this.f$0 = suggestionConnectionStatusListenerProxy;
        this.f$1 = wifiNetworkSuggestion;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.mo5112x2fd5a31c(this.f$1, this.f$2);
    }
}
