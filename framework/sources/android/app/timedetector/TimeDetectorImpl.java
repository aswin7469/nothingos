package android.app.timedetector;

import android.app.timedetector.ITimeDetectorService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
/* loaded from: classes.dex */
public final class TimeDetectorImpl implements TimeDetector {
    private static final boolean DEBUG = true;
    private static final String TAG = "timedetector.TimeDetector";
    private final ITimeDetectorService mITimeDetectorService = ITimeDetectorService.Stub.asInterface(ServiceManager.getServiceOrThrow("time_detector"));

    @Override // android.app.timedetector.TimeDetector
    public void suggestTelephonyTime(TelephonyTimeSuggestion timeSuggestion) {
        Log.d(TAG, "suggestTelephonyTime called: " + timeSuggestion);
        try {
            this.mITimeDetectorService.suggestTelephonyTime(timeSuggestion);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.app.timedetector.TimeDetector
    public boolean suggestManualTime(ManualTimeSuggestion timeSuggestion) {
        Log.d(TAG, "suggestManualTime called: " + timeSuggestion);
        try {
            return this.mITimeDetectorService.suggestManualTime(timeSuggestion);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.app.timedetector.TimeDetector
    public void suggestNetworkTime(NetworkTimeSuggestion timeSuggestion) {
        Log.d(TAG, "suggestNetworkTime called: " + timeSuggestion);
        try {
            this.mITimeDetectorService.suggestNetworkTime(timeSuggestion);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.app.timedetector.TimeDetector
    public void suggestGnssTime(GnssTimeSuggestion timeSuggestion) {
        Log.d(TAG, "suggestGnssTime called: " + timeSuggestion);
        try {
            this.mITimeDetectorService.suggestGnssTime(timeSuggestion);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
