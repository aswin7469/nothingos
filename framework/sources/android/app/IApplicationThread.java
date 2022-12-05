package android.app;

import android.app.IInstrumentationWatcher;
import android.app.IUiAutomationConnection;
import android.app.servertransaction.ClientTransaction;
import android.content.AutofillOptions;
import android.content.ComponentName;
import android.content.ContentCaptureOptions;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ProviderInfoList;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.view.autofill.AutofillId;
import android.view.translation.TranslationSpec;
import android.view.translation.UiTranslationSpec;
import com.android.internal.app.IVoiceInteractor;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public interface IApplicationThread extends IInterface {
    void attachAgent(String str) throws RemoteException;

    void attachStartupAgents(String str) throws RemoteException;

    void bindApplication(String str, ApplicationInfo applicationInfo, ProviderInfoList providerInfoList, ComponentName componentName, ProfilerInfo profilerInfo, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i, boolean z, boolean z2, boolean z3, boolean z4, Configuration configuration, CompatibilityInfo compatibilityInfo, Map map, Bundle bundle2, String str2, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions, long[] jArr, SharedMemory sharedMemory) throws RemoteException;

    void clearDnsCache() throws RemoteException;

    void dispatchPackageBroadcast(int i, String[] strArr) throws RemoteException;

    void dumpActivity(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String str, String[] strArr) throws RemoteException;

    void dumpCacheInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) throws RemoteException;

    void dumpDbInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) throws RemoteException;

    void dumpGfxInfo(ParcelFileDescriptor parcelFileDescriptor, String[] strArr) throws RemoteException;

    void dumpHeap(boolean z, boolean z2, boolean z3, String str, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException;

    void dumpMemInfo(ParcelFileDescriptor parcelFileDescriptor, Debug.MemoryInfo memoryInfo, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, String[] strArr) throws RemoteException;

    void dumpMemInfoProto(ParcelFileDescriptor parcelFileDescriptor, Debug.MemoryInfo memoryInfo, boolean z, boolean z2, boolean z3, boolean z4, String[] strArr) throws RemoteException;

    void dumpProvider(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strArr) throws RemoteException;

    void dumpService(ParcelFileDescriptor parcelFileDescriptor, IBinder iBinder, String[] strArr) throws RemoteException;

    void handleTrustStorageUpdate() throws RemoteException;

    void instrumentWithoutRestart(ComponentName componentName, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, ApplicationInfo applicationInfo) throws RemoteException;

    void notifyCleartextNetwork(byte[] bArr) throws RemoteException;

    void notifyContentProviderPublishStatus(ContentProviderHolder contentProviderHolder, String str, int i, boolean z) throws RemoteException;

    void performDirectAction(IBinder iBinder, String str, Bundle bundle, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) throws RemoteException;

    void processInBackground() throws RemoteException;

    void profilerControl(boolean z, ProfilerInfo profilerInfo, int i) throws RemoteException;

    void requestAssistContextExtras(IBinder iBinder, IBinder iBinder2, int i, int i2, int i3) throws RemoteException;

    void requestDirectActions(IBinder iBinder, IVoiceInteractor iVoiceInteractor, RemoteCallback remoteCallback, RemoteCallback remoteCallback2) throws RemoteException;

    void runIsolatedEntryPoint(String str, String[] strArr) throws RemoteException;

    void scheduleApplicationInfoChanged(ApplicationInfo applicationInfo) throws RemoteException;

    void scheduleBindService(IBinder iBinder, Intent intent, boolean z, int i) throws RemoteException;

    void scheduleCrash(String str, int i, Bundle bundle) throws RemoteException;

    void scheduleCreateBackupAgent(ApplicationInfo applicationInfo, CompatibilityInfo compatibilityInfo, int i, int i2, int i3) throws RemoteException;

    void scheduleCreateService(IBinder iBinder, ServiceInfo serviceInfo, CompatibilityInfo compatibilityInfo, int i) throws RemoteException;

    void scheduleDestroyBackupAgent(ApplicationInfo applicationInfo, CompatibilityInfo compatibilityInfo, int i) throws RemoteException;

    void scheduleEnterAnimationComplete(IBinder iBinder) throws RemoteException;

    void scheduleExit() throws RemoteException;

    void scheduleInstallProvider(ProviderInfo providerInfo) throws RemoteException;

    void scheduleLocalVoiceInteractionStarted(IBinder iBinder, IVoiceInteractor iVoiceInteractor) throws RemoteException;

    void scheduleLowMemory() throws RemoteException;

    void scheduleOnNewActivityOptions(IBinder iBinder, Bundle bundle) throws RemoteException;

    void scheduleReceiver(Intent intent, ActivityInfo activityInfo, CompatibilityInfo compatibilityInfo, int i, String str, Bundle bundle, boolean z, int i2, int i3) throws RemoteException;

    void scheduleRegisteredReceiver(IIntentReceiver iIntentReceiver, Intent intent, int i, String str, Bundle bundle, boolean z, boolean z2, int i2, int i3) throws RemoteException;

    void scheduleServiceArgs(IBinder iBinder, ParceledListSlice parceledListSlice) throws RemoteException;

    void scheduleStopService(IBinder iBinder) throws RemoteException;

    void scheduleSuicide() throws RemoteException;

    void scheduleTransaction(ClientTransaction clientTransaction) throws RemoteException;

    void scheduleTranslucentConversionComplete(IBinder iBinder, boolean z) throws RemoteException;

    void scheduleTrimMemory(int i) throws RemoteException;

    void scheduleUnbindService(IBinder iBinder, Intent intent) throws RemoteException;

    void setCoreSettings(Bundle bundle) throws RemoteException;

    void setNetworkBlockSeq(long j) throws RemoteException;

    void setProcessState(int i) throws RemoteException;

    void setSchedulingGroup(int i) throws RemoteException;

    void startBinderTracking() throws RemoteException;

    void stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void unstableProviderDied(IBinder iBinder) throws RemoteException;

    void updateHttpProxy() throws RemoteException;

    void updatePackageCompatibilityInfo(String str, CompatibilityInfo compatibilityInfo) throws RemoteException;

    void updateTimePrefs(int i) throws RemoteException;

    void updateTimeZone() throws RemoteException;

    void updateUiTranslationState(IBinder iBinder, int i, TranslationSpec translationSpec, TranslationSpec translationSpec2, List<AutofillId> list, UiTranslationSpec uiTranslationSpec) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IApplicationThread {
        @Override // android.app.IApplicationThread
        public void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleStopService(IBinder token) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void bindApplication(String packageName, ApplicationInfo info, ProviderInfoList providerList, ComponentName testName, ProfilerInfo profilerInfo, Bundle testArguments, IInstrumentationWatcher testWatcher, IUiAutomationConnection uiAutomationConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean restrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions, long[] disabledCompatChanges, SharedMemory serializedSystemFontMap) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleExit() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleServiceArgs(IBinder token, ParceledListSlice args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void updateTimeZone() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void processInBackground() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleUnbindService(IBinder token, Intent intent) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpService(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleLowMemory() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void setSchedulingGroup(int group) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode, int userId, int operationType) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int userId) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleOnNewActivityOptions(IBinder token, Bundle options) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleSuicide() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dispatchPackageBroadcast(int cmd, String[] packages) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleCrash(String msg, int typeId, Bundle extras) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpActivity(ParcelFileDescriptor fd, IBinder servicetoken, String prefix, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void clearDnsCache() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void updateHttpProxy() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void setCoreSettings(Bundle coreSettings) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleTrimMemory(int level) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpMemInfo(ParcelFileDescriptor fd, Debug.MemoryInfo mem, boolean checkin, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpMemInfoProto(ParcelFileDescriptor fd, Debug.MemoryInfo mem, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpGfxInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpCacheInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpProvider(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void dumpDbInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void unstableProviderDied(IBinder provider) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleTranslucentConversionComplete(IBinder token, boolean timeout) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void setProcessState(int state) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleInstallProvider(ProviderInfo provider) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void updateTimePrefs(int timeFormatPreference) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleEnterAnimationComplete(IBinder token) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void notifyCleartextNetwork(byte[] firstPacket) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void startBinderTracking() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void handleTrustStorageUpdate() throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void attachAgent(String path) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void attachStartupAgents(String dataDir) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleApplicationInfoChanged(ApplicationInfo ai) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void setNetworkBlockSeq(long procStateSeq) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void requestDirectActions(IBinder activityToken, IVoiceInteractor intractor, RemoteCallback cancellationCallback, RemoteCallback callback) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void performDirectAction(IBinder activityToken, String actionId, Bundle arguments, RemoteCallback cancellationCallback, RemoteCallback resultCallback) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void notifyContentProviderPublishStatus(ContentProviderHolder holder, String authorities, int userId, boolean published) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void instrumentWithoutRestart(ComponentName instrumentationName, Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher, IUiAutomationConnection instrumentationUiConnection, ApplicationInfo targetInfo) throws RemoteException {
        }

        @Override // android.app.IApplicationThread
        public void updateUiTranslationState(IBinder activityToken, int state, TranslationSpec sourceSpec, TranslationSpec targetSpec, List<AutofillId> viewIds, UiTranslationSpec uiTranslationSpec) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IApplicationThread {
        public static final String DESCRIPTOR = "android.app.IApplicationThread";
        static final int TRANSACTION_attachAgent = 48;
        static final int TRANSACTION_attachStartupAgents = 49;
        static final int TRANSACTION_bindApplication = 4;
        static final int TRANSACTION_clearDnsCache = 25;
        static final int TRANSACTION_dispatchPackageBroadcast = 21;
        static final int TRANSACTION_dumpActivity = 24;
        static final int TRANSACTION_dumpCacheInfo = 33;
        static final int TRANSACTION_dumpDbInfo = 35;
        static final int TRANSACTION_dumpGfxInfo = 32;
        static final int TRANSACTION_dumpHeap = 23;
        static final int TRANSACTION_dumpMemInfo = 30;
        static final int TRANSACTION_dumpMemInfoProto = 31;
        static final int TRANSACTION_dumpProvider = 34;
        static final int TRANSACTION_dumpService = 12;
        static final int TRANSACTION_handleTrustStorageUpdate = 47;
        static final int TRANSACTION_instrumentWithoutRestart = 56;
        static final int TRANSACTION_notifyCleartextNetwork = 43;
        static final int TRANSACTION_notifyContentProviderPublishStatus = 55;
        static final int TRANSACTION_performDirectAction = 54;
        static final int TRANSACTION_processInBackground = 9;
        static final int TRANSACTION_profilerControl = 15;
        static final int TRANSACTION_requestAssistContextExtras = 37;
        static final int TRANSACTION_requestDirectActions = 53;
        static final int TRANSACTION_runIsolatedEntryPoint = 5;
        static final int TRANSACTION_scheduleApplicationInfoChanged = 50;
        static final int TRANSACTION_scheduleBindService = 10;
        static final int TRANSACTION_scheduleCrash = 22;
        static final int TRANSACTION_scheduleCreateBackupAgent = 17;
        static final int TRANSACTION_scheduleCreateService = 2;
        static final int TRANSACTION_scheduleDestroyBackupAgent = 18;
        static final int TRANSACTION_scheduleEnterAnimationComplete = 42;
        static final int TRANSACTION_scheduleExit = 6;
        static final int TRANSACTION_scheduleInstallProvider = 40;
        static final int TRANSACTION_scheduleLocalVoiceInteractionStarted = 46;
        static final int TRANSACTION_scheduleLowMemory = 14;
        static final int TRANSACTION_scheduleOnNewActivityOptions = 19;
        static final int TRANSACTION_scheduleReceiver = 1;
        static final int TRANSACTION_scheduleRegisteredReceiver = 13;
        static final int TRANSACTION_scheduleServiceArgs = 7;
        static final int TRANSACTION_scheduleStopService = 3;
        static final int TRANSACTION_scheduleSuicide = 20;
        static final int TRANSACTION_scheduleTransaction = 52;
        static final int TRANSACTION_scheduleTranslucentConversionComplete = 38;
        static final int TRANSACTION_scheduleTrimMemory = 29;
        static final int TRANSACTION_scheduleUnbindService = 11;
        static final int TRANSACTION_setCoreSettings = 27;
        static final int TRANSACTION_setNetworkBlockSeq = 51;
        static final int TRANSACTION_setProcessState = 39;
        static final int TRANSACTION_setSchedulingGroup = 16;
        static final int TRANSACTION_startBinderTracking = 44;
        static final int TRANSACTION_stopBinderTrackingAndDump = 45;
        static final int TRANSACTION_unstableProviderDied = 36;
        static final int TRANSACTION_updateHttpProxy = 26;
        static final int TRANSACTION_updatePackageCompatibilityInfo = 28;
        static final int TRANSACTION_updateTimePrefs = 41;
        static final int TRANSACTION_updateTimeZone = 8;
        static final int TRANSACTION_updateUiTranslationState = 57;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IApplicationThread asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IApplicationThread)) {
                return (IApplicationThread) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "scheduleReceiver";
                case 2:
                    return "scheduleCreateService";
                case 3:
                    return "scheduleStopService";
                case 4:
                    return "bindApplication";
                case 5:
                    return "runIsolatedEntryPoint";
                case 6:
                    return "scheduleExit";
                case 7:
                    return "scheduleServiceArgs";
                case 8:
                    return "updateTimeZone";
                case 9:
                    return "processInBackground";
                case 10:
                    return "scheduleBindService";
                case 11:
                    return "scheduleUnbindService";
                case 12:
                    return "dumpService";
                case 13:
                    return "scheduleRegisteredReceiver";
                case 14:
                    return "scheduleLowMemory";
                case 15:
                    return "profilerControl";
                case 16:
                    return "setSchedulingGroup";
                case 17:
                    return "scheduleCreateBackupAgent";
                case 18:
                    return "scheduleDestroyBackupAgent";
                case 19:
                    return "scheduleOnNewActivityOptions";
                case 20:
                    return "scheduleSuicide";
                case 21:
                    return "dispatchPackageBroadcast";
                case 22:
                    return "scheduleCrash";
                case 23:
                    return "dumpHeap";
                case 24:
                    return "dumpActivity";
                case 25:
                    return "clearDnsCache";
                case 26:
                    return "updateHttpProxy";
                case 27:
                    return "setCoreSettings";
                case 28:
                    return "updatePackageCompatibilityInfo";
                case 29:
                    return "scheduleTrimMemory";
                case 30:
                    return "dumpMemInfo";
                case 31:
                    return "dumpMemInfoProto";
                case 32:
                    return "dumpGfxInfo";
                case 33:
                    return "dumpCacheInfo";
                case 34:
                    return "dumpProvider";
                case 35:
                    return "dumpDbInfo";
                case 36:
                    return "unstableProviderDied";
                case 37:
                    return "requestAssistContextExtras";
                case 38:
                    return "scheduleTranslucentConversionComplete";
                case 39:
                    return "setProcessState";
                case 40:
                    return "scheduleInstallProvider";
                case 41:
                    return "updateTimePrefs";
                case 42:
                    return "scheduleEnterAnimationComplete";
                case 43:
                    return "notifyCleartextNetwork";
                case 44:
                    return "startBinderTracking";
                case 45:
                    return "stopBinderTrackingAndDump";
                case 46:
                    return "scheduleLocalVoiceInteractionStarted";
                case 47:
                    return "handleTrustStorageUpdate";
                case 48:
                    return "attachAgent";
                case 49:
                    return "attachStartupAgents";
                case 50:
                    return "scheduleApplicationInfoChanged";
                case 51:
                    return "setNetworkBlockSeq";
                case 52:
                    return "scheduleTransaction";
                case 53:
                    return "requestDirectActions";
                case 54:
                    return "performDirectAction";
                case 55:
                    return "notifyContentProviderPublishStatus";
                case 56:
                    return "instrumentWithoutRestart";
                case 57:
                    return "updateUiTranslationState";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg0;
            ActivityInfo _arg1;
            CompatibilityInfo _arg2;
            Bundle _arg5;
            ServiceInfo _arg12;
            CompatibilityInfo _arg22;
            ApplicationInfo _arg13;
            ProviderInfoList _arg23;
            ComponentName _arg3;
            ProfilerInfo _arg4;
            Bundle _arg52;
            Configuration _arg132;
            CompatibilityInfo _arg14;
            Bundle _arg16;
            AutofillOptions _arg18;
            ContentCaptureOptions _arg19;
            SharedMemory _arg21;
            ParceledListSlice _arg15;
            Intent _arg17;
            Intent _arg110;
            ParcelFileDescriptor _arg02;
            Intent _arg111;
            Bundle _arg42;
            ProfilerInfo _arg112;
            ApplicationInfo _arg03;
            CompatibilityInfo _arg113;
            ApplicationInfo _arg04;
            CompatibilityInfo _arg114;
            Bundle _arg115;
            Bundle _arg24;
            ParcelFileDescriptor _arg43;
            RemoteCallback _arg53;
            ParcelFileDescriptor _arg05;
            Bundle _arg06;
            CompatibilityInfo _arg116;
            ParcelFileDescriptor _arg07;
            Debug.MemoryInfo _arg117;
            ParcelFileDescriptor _arg08;
            Debug.MemoryInfo _arg118;
            ParcelFileDescriptor _arg09;
            ParcelFileDescriptor _arg010;
            ParcelFileDescriptor _arg011;
            ParcelFileDescriptor _arg012;
            ProviderInfo _arg013;
            ParcelFileDescriptor _arg014;
            ApplicationInfo _arg015;
            ClientTransaction _arg016;
            RemoteCallback _arg25;
            RemoteCallback _arg32;
            Bundle _arg26;
            RemoteCallback _arg33;
            RemoteCallback _arg44;
            ContentProviderHolder _arg017;
            ComponentName _arg018;
            Bundle _arg119;
            ApplicationInfo _arg45;
            TranslationSpec _arg27;
            TranslationSpec _arg34;
            UiTranslationSpec _arg54;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    boolean _arg35 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg1 = ActivityInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg2 = CompatibilityInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            int _arg36 = data.readInt();
                            String _arg46 = data.readString();
                            if (data.readInt() != 0) {
                                _arg5 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg5 = null;
                            }
                            boolean _arg6 = data.readInt() != 0;
                            int _arg7 = data.readInt();
                            int _arg8 = data.readInt();
                            scheduleReceiver(_arg0, _arg1, _arg2, _arg36, _arg46, _arg5, _arg6, _arg7, _arg8);
                            return true;
                        case 2:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg019 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg12 = ServiceInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg22 = CompatibilityInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg22 = null;
                            }
                            scheduleCreateService(_arg019, _arg12, _arg22, data.readInt());
                            return true;
                        case 3:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg020 = data.readStrongBinder();
                            scheduleStopService(_arg020);
                            return true;
                        case 4:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg021 = data.readString();
                            if (data.readInt() != 0) {
                                _arg13 = ApplicationInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg23 = ProviderInfoList.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg23 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg3 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg3 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg4 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg4 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg52 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg52 = null;
                            }
                            IInstrumentationWatcher _arg62 = IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder());
                            IUiAutomationConnection _arg72 = IUiAutomationConnection.Stub.asInterface(data.readStrongBinder());
                            int _arg82 = data.readInt();
                            boolean _arg9 = data.readInt() != 0;
                            boolean _arg10 = data.readInt() != 0;
                            boolean _arg11 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                _arg35 = true;
                            }
                            boolean _arg122 = _arg35;
                            if (data.readInt() != 0) {
                                _arg132 = Configuration.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg132 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg14 = CompatibilityInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            ClassLoader cl = getClass().getClassLoader();
                            Map _arg152 = data.readHashMap(cl);
                            if (data.readInt() != 0) {
                                _arg16 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg16 = null;
                            }
                            String _arg172 = data.readString();
                            if (data.readInt() != 0) {
                                _arg18 = AutofillOptions.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg18 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg19 = ContentCaptureOptions.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg19 = null;
                            }
                            long[] _arg20 = data.createLongArray();
                            if (data.readInt() != 0) {
                                _arg21 = SharedMemory.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg21 = null;
                            }
                            bindApplication(_arg021, _arg13, _arg23, _arg3, _arg4, _arg52, _arg62, _arg72, _arg82, _arg9, _arg10, _arg11, _arg122, _arg132, _arg14, _arg152, _arg16, _arg172, _arg18, _arg19, _arg20, _arg21);
                            return true;
                        case 5:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg022 = data.readString();
                            String[] _arg120 = data.createStringArray();
                            runIsolatedEntryPoint(_arg022, _arg120);
                            return true;
                        case 6:
                            data.enforceInterface(DESCRIPTOR);
                            scheduleExit();
                            return true;
                        case 7:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg023 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg15 = ParceledListSlice.CREATOR.createFromParcel(data);
                            } else {
                                _arg15 = null;
                            }
                            scheduleServiceArgs(_arg023, _arg15);
                            return true;
                        case 8:
                            data.enforceInterface(DESCRIPTOR);
                            updateTimeZone();
                            return true;
                        case 9:
                            data.enforceInterface(DESCRIPTOR);
                            processInBackground();
                            return true;
                        case 10:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg024 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg17 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg17 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg35 = true;
                            }
                            scheduleBindService(_arg024, _arg17, _arg35, data.readInt());
                            return true;
                        case 11:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg025 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg110 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg110 = null;
                            }
                            scheduleUnbindService(_arg025, _arg110);
                            return true;
                        case 12:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            IBinder _arg121 = data.readStrongBinder();
                            String[] _arg28 = data.createStringArray();
                            dumpService(_arg02, _arg121, _arg28);
                            return true;
                        case 13:
                            data.enforceInterface(DESCRIPTOR);
                            IIntentReceiver _arg026 = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg111 = Intent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg111 = null;
                            }
                            int _arg29 = data.readInt();
                            String _arg37 = data.readString();
                            if (data.readInt() != 0) {
                                _arg42 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg42 = null;
                            }
                            boolean _arg55 = data.readInt() != 0;
                            boolean _arg63 = data.readInt() != 0;
                            int _arg73 = data.readInt();
                            int _arg83 = data.readInt();
                            scheduleRegisteredReceiver(_arg026, _arg111, _arg29, _arg37, _arg42, _arg55, _arg63, _arg73, _arg83);
                            return true;
                        case 14:
                            data.enforceInterface(DESCRIPTOR);
                            scheduleLowMemory();
                            return true;
                        case 15:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg35 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg112 = ProfilerInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg112 = null;
                            }
                            int _arg210 = data.readInt();
                            profilerControl(_arg35, _arg112, _arg210);
                            return true;
                        case 16:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg027 = data.readInt();
                            setSchedulingGroup(_arg027);
                            return true;
                        case 17:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = ApplicationInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg113 = CompatibilityInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg113 = null;
                            }
                            int _arg211 = data.readInt();
                            int _arg38 = data.readInt();
                            int _arg47 = data.readInt();
                            scheduleCreateBackupAgent(_arg03, _arg113, _arg211, _arg38, _arg47);
                            return true;
                        case 18:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = ApplicationInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg114 = CompatibilityInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg114 = null;
                            }
                            int _arg212 = data.readInt();
                            scheduleDestroyBackupAgent(_arg04, _arg114, _arg212);
                            return true;
                        case 19:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg028 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg115 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg115 = null;
                            }
                            scheduleOnNewActivityOptions(_arg028, _arg115);
                            return true;
                        case 20:
                            data.enforceInterface(DESCRIPTOR);
                            scheduleSuicide();
                            return true;
                        case 21:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg029 = data.readInt();
                            String[] _arg123 = data.createStringArray();
                            dispatchPackageBroadcast(_arg029, _arg123);
                            return true;
                        case 22:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg030 = data.readString();
                            int _arg124 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg24 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg24 = null;
                            }
                            scheduleCrash(_arg030, _arg124, _arg24);
                            return true;
                        case 23:
                            data.enforceInterface(DESCRIPTOR);
                            boolean _arg031 = data.readInt() != 0;
                            boolean _arg125 = data.readInt() != 0;
                            boolean _arg213 = data.readInt() != 0;
                            String _arg39 = data.readString();
                            if (data.readInt() != 0) {
                                _arg43 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg43 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg53 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg53 = null;
                            }
                            dumpHeap(_arg031, _arg125, _arg213, _arg39, _arg43, _arg53);
                            return true;
                        case 24:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            IBinder _arg126 = data.readStrongBinder();
                            String _arg214 = data.readString();
                            dumpActivity(_arg05, _arg126, _arg214, data.createStringArray());
                            return true;
                        case 25:
                            data.enforceInterface(DESCRIPTOR);
                            clearDnsCache();
                            return true;
                        case 26:
                            data.enforceInterface(DESCRIPTOR);
                            updateHttpProxy();
                            return true;
                        case 27:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            setCoreSettings(_arg06);
                            return true;
                        case 28:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg032 = data.readString();
                            if (data.readInt() != 0) {
                                _arg116 = CompatibilityInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg116 = null;
                            }
                            updatePackageCompatibilityInfo(_arg032, _arg116);
                            return true;
                        case 29:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg033 = data.readInt();
                            scheduleTrimMemory(_arg033);
                            return true;
                        case 30:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg07 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg07 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg117 = Debug.MemoryInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg117 = null;
                            }
                            boolean _arg215 = data.readInt() != 0;
                            boolean _arg310 = data.readInt() != 0;
                            boolean _arg48 = data.readInt() != 0;
                            boolean _arg56 = data.readInt() != 0;
                            boolean _arg64 = data.readInt() != 0;
                            String[] _arg74 = data.createStringArray();
                            dumpMemInfo(_arg07, _arg117, _arg215, _arg310, _arg48, _arg56, _arg64, _arg74);
                            return true;
                        case 31:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg08 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg08 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg118 = Debug.MemoryInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg118 = null;
                            }
                            boolean _arg216 = data.readInt() != 0;
                            boolean _arg311 = data.readInt() != 0;
                            boolean _arg49 = data.readInt() != 0;
                            boolean _arg57 = data.readInt() != 0;
                            String[] _arg65 = data.createStringArray();
                            dumpMemInfoProto(_arg08, _arg118, _arg216, _arg311, _arg49, _arg57, _arg65);
                            return true;
                        case 32:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg09 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg09 = null;
                            }
                            String[] _arg127 = data.createStringArray();
                            dumpGfxInfo(_arg09, _arg127);
                            return true;
                        case 33:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg010 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg010 = null;
                            }
                            String[] _arg128 = data.createStringArray();
                            dumpCacheInfo(_arg010, _arg128);
                            return true;
                        case 34:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg011 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg011 = null;
                            }
                            IBinder _arg129 = data.readStrongBinder();
                            String[] _arg217 = data.createStringArray();
                            dumpProvider(_arg011, _arg129, _arg217);
                            return true;
                        case 35:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg012 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg012 = null;
                            }
                            String[] _arg130 = data.createStringArray();
                            dumpDbInfo(_arg012, _arg130);
                            return true;
                        case 36:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg034 = data.readStrongBinder();
                            unstableProviderDied(_arg034);
                            return true;
                        case 37:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg035 = data.readStrongBinder();
                            IBinder _arg131 = data.readStrongBinder();
                            int _arg218 = data.readInt();
                            int _arg312 = data.readInt();
                            int _arg410 = data.readInt();
                            requestAssistContextExtras(_arg035, _arg131, _arg218, _arg312, _arg410);
                            return true;
                        case 38:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg036 = data.readStrongBinder();
                            if (data.readInt() != 0) {
                                _arg35 = true;
                            }
                            scheduleTranslucentConversionComplete(_arg036, _arg35);
                            return true;
                        case 39:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg037 = data.readInt();
                            setProcessState(_arg037);
                            return true;
                        case 40:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg013 = ProviderInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg013 = null;
                            }
                            scheduleInstallProvider(_arg013);
                            return true;
                        case 41:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg038 = data.readInt();
                            updateTimePrefs(_arg038);
                            return true;
                        case 42:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg039 = data.readStrongBinder();
                            scheduleEnterAnimationComplete(_arg039);
                            return true;
                        case 43:
                            data.enforceInterface(DESCRIPTOR);
                            byte[] _arg040 = data.createByteArray();
                            notifyCleartextNetwork(_arg040);
                            return true;
                        case 44:
                            data.enforceInterface(DESCRIPTOR);
                            startBinderTracking();
                            return true;
                        case 45:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg014 = ParcelFileDescriptor.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg014 = null;
                            }
                            stopBinderTrackingAndDump(_arg014);
                            return true;
                        case 46:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg041 = data.readStrongBinder();
                            IVoiceInteractor _arg133 = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                            scheduleLocalVoiceInteractionStarted(_arg041, _arg133);
                            return true;
                        case 47:
                            data.enforceInterface(DESCRIPTOR);
                            handleTrustStorageUpdate();
                            return true;
                        case 48:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg042 = data.readString();
                            attachAgent(_arg042);
                            return true;
                        case 49:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg043 = data.readString();
                            attachStartupAgents(_arg043);
                            return true;
                        case 50:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg015 = ApplicationInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg015 = null;
                            }
                            scheduleApplicationInfoChanged(_arg015);
                            return true;
                        case 51:
                            data.enforceInterface(DESCRIPTOR);
                            long _arg044 = data.readLong();
                            setNetworkBlockSeq(_arg044);
                            return true;
                        case 52:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg016 = ClientTransaction.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg016 = null;
                            }
                            scheduleTransaction(_arg016);
                            return true;
                        case 53:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg045 = data.readStrongBinder();
                            IVoiceInteractor _arg134 = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg25 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg25 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg32 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg32 = null;
                            }
                            requestDirectActions(_arg045, _arg134, _arg25, _arg32);
                            return true;
                        case 54:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg046 = data.readStrongBinder();
                            String _arg135 = data.readString();
                            if (data.readInt() != 0) {
                                _arg26 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg26 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg33 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg33 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg44 = RemoteCallback.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg44 = null;
                            }
                            performDirectAction(_arg046, _arg135, _arg26, _arg33, _arg44);
                            return true;
                        case 55:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg017 = ContentProviderHolder.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg017 = null;
                            }
                            String _arg136 = data.readString();
                            int _arg219 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg35 = true;
                            }
                            notifyContentProviderPublishStatus(_arg017, _arg136, _arg219, _arg35);
                            return true;
                        case 56:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg018 = ComponentName.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg018 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg119 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg119 = null;
                            }
                            IInstrumentationWatcher _arg220 = IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder());
                            IUiAutomationConnection _arg313 = IUiAutomationConnection.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg45 = ApplicationInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg45 = null;
                            }
                            instrumentWithoutRestart(_arg018, _arg119, _arg220, _arg313, _arg45);
                            return true;
                        case 57:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg047 = data.readStrongBinder();
                            int _arg137 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg27 = TranslationSpec.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg27 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg34 = TranslationSpec.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg34 = null;
                            }
                            List<AutofillId> _arg411 = data.createTypedArrayList(AutofillId.CREATOR);
                            if (data.readInt() != 0) {
                                _arg54 = UiTranslationSpec.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg54 = null;
                            }
                            updateUiTranslationState(_arg047, _arg137, _arg27, _arg34, _arg411, _arg54);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IApplicationThread {
            public static IApplicationThread sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.IApplicationThread
            public void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resultCode);
                    _data.writeString(data);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (sync) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(sendingUser);
                    _data.writeInt(processState);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleReceiver(intent, info, compatInfo, resultCode, data, extras, sync, sendingUser, processState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(processState);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleCreateService(token, info, compatInfo, processState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleStopService(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleStopService(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void bindApplication(String packageName, ApplicationInfo info, ProviderInfoList providerList, ComponentName testName, ProfilerInfo profilerInfo, Bundle testArguments, IInstrumentationWatcher testWatcher, IUiAutomationConnection uiAutomationConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean restrictedBackupMode, boolean persistent, Configuration config, CompatibilityInfo compatInfo, Map services, Bundle coreSettings, String buildSerial, AutofillOptions autofillOptions, ContentCaptureOptions contentCaptureOptions, long[] disabledCompatChanges, SharedMemory serializedSystemFontMap) throws RemoteException {
                Parcel _data;
                int i;
                int i2;
                Parcel _data2;
                Parcel _data3 = Parcel.obtain();
                try {
                    _data3.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data3.writeString(packageName);
                    int i3 = 0;
                    if (info != null) {
                        try {
                            _data3.writeInt(1);
                            info.writeToParcel(_data3, 0);
                        } catch (Throwable th) {
                            th = th;
                            _data = _data3;
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        _data3.writeInt(0);
                    }
                    if (providerList != null) {
                        _data3.writeInt(1);
                        providerList.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    if (testName != null) {
                        _data3.writeInt(1);
                        testName.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    if (profilerInfo != null) {
                        _data3.writeInt(1);
                        profilerInfo.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    if (testArguments != null) {
                        _data3.writeInt(1);
                        testArguments.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeStrongBinder(testWatcher != null ? testWatcher.asBinder() : null);
                    _data3.writeStrongBinder(uiAutomationConnection != null ? uiAutomationConnection.asBinder() : null);
                    _data3.writeInt(debugMode);
                    if (enableBinderTracking) {
                        i3 = 1;
                    }
                    _data3.writeInt(i3);
                    _data3.writeInt(trackAllocation ? 1 : 0);
                    _data3.writeInt(restrictedBackupMode ? 1 : 0);
                    _data3.writeInt(persistent ? 1 : 0);
                    if (config != null) {
                        _data3.writeInt(1);
                        i = 0;
                        config.writeToParcel(_data3, 0);
                    } else {
                        i = 0;
                        _data3.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data3.writeInt(1);
                        compatInfo.writeToParcel(_data3, i);
                    } else {
                        _data3.writeInt(i);
                    }
                    _data3.writeMap(services);
                    if (coreSettings != null) {
                        _data3.writeInt(1);
                        coreSettings.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    _data3.writeString(buildSerial);
                    if (autofillOptions != null) {
                        _data3.writeInt(1);
                        i2 = 0;
                        autofillOptions.writeToParcel(_data3, 0);
                    } else {
                        i2 = 0;
                        _data3.writeInt(0);
                    }
                    if (contentCaptureOptions != null) {
                        _data3.writeInt(1);
                        contentCaptureOptions.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(i2);
                    }
                    _data3.writeLongArray(disabledCompatChanges);
                    if (serializedSystemFontMap != null) {
                        _data3.writeInt(1);
                        serializedSystemFontMap.writeToParcel(_data3, 0);
                    } else {
                        _data3.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data3, null, 1);
                    if (_status) {
                        _data2 = _data3;
                    } else if (Stub.getDefaultImpl() != null) {
                        _data = _data3;
                        try {
                            Stub.getDefaultImpl().bindApplication(packageName, info, providerList, testName, profilerInfo, testArguments, testWatcher, uiAutomationConnection, debugMode, enableBinderTracking, trackAllocation, restrictedBackupMode, persistent, config, compatInfo, services, coreSettings, buildSerial, autofillOptions, contentCaptureOptions, disabledCompatChanges, serializedSystemFontMap);
                            _data.recycle();
                            return;
                        } catch (Throwable th2) {
                            th = th2;
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        _data2 = _data3;
                    }
                    _data2.recycle();
                } catch (Throwable th3) {
                    th = th3;
                    _data = _data3;
                }
            }

            @Override // android.app.IApplicationThread
            public void runIsolatedEntryPoint(String entryPoint, String[] entryPointArgs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(entryPoint);
                    _data.writeStringArray(entryPointArgs);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().runIsolatedEntryPoint(entryPoint, entryPointArgs);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleExit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleExit();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleServiceArgs(IBinder token, ParceledListSlice args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleServiceArgs(token, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void updateTimeZone() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateTimeZone();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void processInBackground() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().processInBackground();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 0;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (rebind) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeInt(processState);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleBindService(token, intent, rebind, processState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleUnbindService(IBinder token, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleUnbindService(token, intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpService(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeStringArray(args);
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpService(fd, servicetoken, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    int i = 0;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(resultCode);
                        try {
                            _data.writeString(data);
                            if (extras != null) {
                                _data.writeInt(1);
                                extras.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(ordered ? 1 : 0);
                            if (sticky) {
                                i = 1;
                            }
                            _data.writeInt(i);
                            _data.writeInt(sendingUser);
                            _data.writeInt(processState);
                            boolean _status = this.mRemote.transact(13, _data, null, 1);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().scheduleRegisteredReceiver(receiver, intent, resultCode, data, extras, ordered, sticky, sendingUser, processState);
                                _data.recycle();
                                return;
                            }
                            _data.recycle();
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleLowMemory() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleLowMemory();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(start ? 1 : 0);
                    if (profilerInfo != null) {
                        _data.writeInt(1);
                        profilerInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(profileType);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().profilerControl(start, profilerInfo, profileType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void setSchedulingGroup(int group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(group);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSchedulingGroup(group);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode, int userId, int operationType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(backupMode);
                    _data.writeInt(userId);
                    _data.writeInt(operationType);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleCreateBackupAgent(app, compatInfo, backupMode, userId, operationType);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (app != null) {
                        _data.writeInt(1);
                        app.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (compatInfo != null) {
                        _data.writeInt(1);
                        compatInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleDestroyBackupAgent(app, compatInfo, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleOnNewActivityOptions(IBinder token, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(19, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleOnNewActivityOptions(token, options);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleSuicide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(20, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleSuicide();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void dispatchPackageBroadcast(int cmd, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeStringArray(packages);
                    boolean _status = this.mRemote.transact(21, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchPackageBroadcast(cmd, packages);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleCrash(String msg, int typeId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(msg);
                    _data.writeInt(typeId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(22, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleCrash(msg, typeId, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpHeap(boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(managed ? 1 : 0);
                    _data.writeInt(mallocInfo ? 1 : 0);
                    _data.writeInt(runGc ? 1 : 0);
                    try {
                        _data.writeString(path);
                        if (fd != null) {
                            _data.writeInt(1);
                            fd.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (finishCallback != null) {
                            _data.writeInt(1);
                            finishCallback.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            boolean _status = this.mRemote.transact(23, _data, null, 1);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().dumpHeap(managed, mallocInfo, runGc, path, fd, finishCallback);
                                _data.recycle();
                                return;
                            }
                            _data.recycle();
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpActivity(ParcelFileDescriptor fd, IBinder servicetoken, String prefix, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeString(prefix);
                    _data.writeStringArray(args);
                    boolean _status = this.mRemote.transact(24, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpActivity(fd, servicetoken, prefix, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void clearDnsCache() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearDnsCache();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void updateHttpProxy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(26, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateHttpProxy();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void setCoreSettings(Bundle coreSettings) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (coreSettings != null) {
                        _data.writeInt(1);
                        coreSettings.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(27, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setCoreSettings(coreSettings);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(28, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePackageCompatibilityInfo(pkg, info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleTrimMemory(int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(level);
                    boolean _status = this.mRemote.transact(29, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleTrimMemory(level);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpMemInfo(ParcelFileDescriptor fd, Debug.MemoryInfo mem, boolean checkin, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (mem != null) {
                        _data.writeInt(1);
                        mem.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(checkin ? 1 : 0);
                    _data.writeInt(dumpInfo ? 1 : 0);
                    _data.writeInt(dumpDalvik ? 1 : 0);
                    _data.writeInt(dumpSummaryOnly ? 1 : 0);
                    if (dumpUnreachable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    try {
                        _data.writeStringArray(args);
                        try {
                            boolean _status = this.mRemote.transact(30, _data, null, 1);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().dumpMemInfo(fd, mem, checkin, dumpInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable, args);
                                _data.recycle();
                                return;
                            }
                            _data.recycle();
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpMemInfoProto(ParcelFileDescriptor fd, Debug.MemoryInfo mem, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (mem != null) {
                        _data.writeInt(1);
                        mem.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(dumpInfo ? 1 : 0);
                    _data.writeInt(dumpDalvik ? 1 : 0);
                    _data.writeInt(dumpSummaryOnly ? 1 : 0);
                    if (dumpUnreachable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    try {
                        _data.writeStringArray(args);
                        try {
                            boolean _status = this.mRemote.transact(31, _data, null, 1);
                            if (!_status && Stub.getDefaultImpl() != null) {
                                Stub.getDefaultImpl().dumpMemInfoProto(fd, mem, dumpInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable, args);
                                _data.recycle();
                                return;
                            }
                            _data.recycle();
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpGfxInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(args);
                    boolean _status = this.mRemote.transact(32, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpGfxInfo(fd, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpCacheInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(args);
                    boolean _status = this.mRemote.transact(33, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpCacheInfo(fd, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpProvider(ParcelFileDescriptor fd, IBinder servicetoken, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(servicetoken);
                    _data.writeStringArray(args);
                    boolean _status = this.mRemote.transact(34, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpProvider(fd, servicetoken, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void dumpDbInfo(ParcelFileDescriptor fd, String[] args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(args);
                    boolean _status = this.mRemote.transact(35, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dumpDbInfo(fd, args);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void unstableProviderDied(IBinder provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(provider);
                    boolean _status = this.mRemote.transact(36, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unstableProviderDied(provider);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType, int sessionId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(requestToken);
                    _data.writeInt(requestType);
                    _data.writeInt(sessionId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(37, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestAssistContextExtras(activityToken, requestToken, requestType, sessionId, flags);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleTranslucentConversionComplete(IBinder token, boolean timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(timeout ? 1 : 0);
                    boolean _status = this.mRemote.transact(38, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleTranslucentConversionComplete(token, timeout);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void setProcessState(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(39, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProcessState(state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleInstallProvider(ProviderInfo provider) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (provider != null) {
                        _data.writeInt(1);
                        provider.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(40, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleInstallProvider(provider);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void updateTimePrefs(int timeFormatPreference) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeFormatPreference);
                    boolean _status = this.mRemote.transact(41, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateTimePrefs(timeFormatPreference);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleEnterAnimationComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(42, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleEnterAnimationComplete(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void notifyCleartextNetwork(byte[] firstPacket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(firstPacket);
                    boolean _status = this.mRemote.transact(43, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCleartextNetwork(firstPacket);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void startBinderTracking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(44, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startBinderTracking();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(45, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopBinderTrackingAndDump(fd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleLocalVoiceInteractionStarted(IBinder token, IVoiceInteractor voiceInteractor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeStrongBinder(voiceInteractor != null ? voiceInteractor.asBinder() : null);
                    boolean _status = this.mRemote.transact(46, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleLocalVoiceInteractionStarted(token, voiceInteractor);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void handleTrustStorageUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(47, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().handleTrustStorageUpdate();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void attachAgent(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    boolean _status = this.mRemote.transact(48, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().attachAgent(path);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void attachStartupAgents(String dataDir) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dataDir);
                    boolean _status = this.mRemote.transact(49, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().attachStartupAgents(dataDir);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleApplicationInfoChanged(ApplicationInfo ai) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ai != null) {
                        _data.writeInt(1);
                        ai.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(50, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleApplicationInfoChanged(ai);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void setNetworkBlockSeq(long procStateSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(procStateSeq);
                    boolean _status = this.mRemote.transact(51, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setNetworkBlockSeq(procStateSeq);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void scheduleTransaction(ClientTransaction transaction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transaction != null) {
                        _data.writeInt(1);
                        transaction.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(52, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleTransaction(transaction);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void requestDirectActions(IBinder activityToken, IVoiceInteractor intractor, RemoteCallback cancellationCallback, RemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(intractor != null ? intractor.asBinder() : null);
                    if (cancellationCallback != null) {
                        _data.writeInt(1);
                        cancellationCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(53, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestDirectActions(activityToken, intractor, cancellationCallback, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void performDirectAction(IBinder activityToken, String actionId, Bundle arguments, RemoteCallback cancellationCallback, RemoteCallback resultCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeString(actionId);
                    if (arguments != null) {
                        _data.writeInt(1);
                        arguments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (cancellationCallback != null) {
                        _data.writeInt(1);
                        cancellationCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (resultCallback != null) {
                        _data.writeInt(1);
                        resultCallback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(54, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().performDirectAction(activityToken, actionId, arguments, cancellationCallback, resultCallback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void notifyContentProviderPublishStatus(ContentProviderHolder holder, String authorities, int userId, boolean published) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (holder != null) {
                        _data.writeInt(1);
                        holder.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authorities);
                    _data.writeInt(userId);
                    if (published) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(55, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyContentProviderPublishStatus(holder, authorities, userId, published);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void instrumentWithoutRestart(ComponentName instrumentationName, Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher, IUiAutomationConnection instrumentationUiConnection, ApplicationInfo targetInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (instrumentationName != null) {
                        _data.writeInt(1);
                        instrumentationName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (instrumentationArgs != null) {
                        _data.writeInt(1);
                        instrumentationArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(instrumentationWatcher != null ? instrumentationWatcher.asBinder() : null);
                    _data.writeStrongBinder(instrumentationUiConnection != null ? instrumentationUiConnection.asBinder() : null);
                    if (targetInfo != null) {
                        _data.writeInt(1);
                        targetInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(56, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().instrumentWithoutRestart(instrumentationName, instrumentationArgs, instrumentationWatcher, instrumentationUiConnection, targetInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.IApplicationThread
            public void updateUiTranslationState(IBinder activityToken, int state, TranslationSpec sourceSpec, TranslationSpec targetSpec, List<AutofillId> viewIds, UiTranslationSpec uiTranslationSpec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(activityToken);
                    try {
                        _data.writeInt(state);
                        if (sourceSpec != null) {
                            _data.writeInt(1);
                            sourceSpec.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (targetSpec != null) {
                            _data.writeInt(1);
                            targetSpec.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeTypedList(viewIds);
                            if (uiTranslationSpec != null) {
                                _data.writeInt(1);
                                uiTranslationSpec.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                boolean _status = this.mRemote.transact(57, _data, null, 1);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    Stub.getDefaultImpl().updateUiTranslationState(activityToken, state, sourceSpec, targetSpec, viewIds, uiTranslationSpec);
                                    _data.recycle();
                                    return;
                                }
                                _data.recycle();
                            } catch (Throwable th2) {
                                th = th2;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
            }
        }

        public static boolean setDefaultImpl(IApplicationThread impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IApplicationThread getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
