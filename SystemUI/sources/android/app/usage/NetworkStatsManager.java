package android.app.usage;

import android.annotation.SystemApi;
import android.app.usage.NetworkStats;
import android.content.Context;
import android.net.DataUsageRequest;
import android.net.INetworkStatsService;
import android.net.Network;
import android.net.NetworkStateSnapshot;
import android.net.NetworkTemplate;
import android.net.UnderlyingNetworkInfo;
import android.net.connectivity.com.android.net.module.util.NetworkIdentityUtils;
import android.net.netstats.IUsageCallback;
import android.net.netstats.provider.NetworkStatsProvider;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class NetworkStatsManager {
    public static final int CALLBACK_LIMIT_REACHED = 0;
    public static final int CALLBACK_RELEASED = 1;
    private static final boolean DBG = false;
    public static final int FLAG_AUGMENT_WITH_SUBSCRIPTION_PLAN = 4;
    public static final int FLAG_POLL_FORCE = 2;
    public static final int FLAG_POLL_ON_OPEN = 1;
    public static final long MIN_THRESHOLD_BYTES = 2097152;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final int NETWORK_TYPE_5G_NSA = -2;
    @Deprecated
    public static final String PREFIX_DEV = "dev";
    private static final String TAG = "NetworkStatsManager";
    private final Context mContext;
    private int mFlags;
    private final INetworkStatsService mService;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int getCollapsedRatType(int i) {
        int i2 = -2;
        if (i != -2) {
            i2 = 20;
            if (i != 20) {
                switch (i) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                    case 16:
                        return 16;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                    case 17:
                        return 3;
                    case 13:
                    case 18:
                        return 13;
                    default:
                        return 0;
                }
            }
        }
        return i2;
    }

    public NetworkStatsManager(Context context, INetworkStatsService iNetworkStatsService) {
        this.mContext = context;
        this.mService = iNetworkStatsService;
        setPollOnOpen(true);
        setAugmentWithSubscriptionPlan(true);
    }

    public INetworkStatsService getBinder() {
        return this.mService;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setPollOnOpen(boolean z) {
        if (z) {
            this.mFlags |= 1;
        } else {
            this.mFlags &= -2;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void setPollForce(boolean z) {
        if (z) {
            this.mFlags |= 2;
        } else {
            this.mFlags &= -3;
        }
    }

    public void setAugmentWithSubscriptionPlan(boolean z) {
        if (z) {
            this.mFlags |= 4;
        } else {
            this.mFlags &= -5;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NetworkStats.Bucket querySummaryForDevice(NetworkTemplate networkTemplate, long j, long j2) {
        Objects.requireNonNull(networkTemplate);
        try {
            NetworkStats networkStats = new NetworkStats(this.mContext, networkTemplate, this.mFlags, j, j2, this.mService);
            NetworkStats.Bucket deviceSummaryForNetwork = networkStats.getDeviceSummaryForNetwork();
            networkStats.close();
            return deviceSummaryForNetwork;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public NetworkStats.Bucket querySummaryForDevice(int i, String str, long j, long j2) throws SecurityException, RemoteException {
        try {
            return querySummaryForDevice(createTemplate(i, str), j, j2);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public NetworkStats.Bucket querySummaryForUser(int i, String str, long j, long j2) throws SecurityException, RemoteException {
        try {
            NetworkStats networkStats = new NetworkStats(this.mContext, createTemplate(i, str), this.mFlags, j, j2, this.mService);
            networkStats.startSummaryEnumeration();
            networkStats.close();
            return networkStats.getSummaryAggregate();
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public NetworkStats querySummary(int i, String str, long j, long j2) throws SecurityException, RemoteException {
        try {
            return querySummary(createTemplate(i, str), j, j2);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NetworkStats querySummary(NetworkTemplate networkTemplate, long j, long j2) throws SecurityException {
        Objects.requireNonNull(networkTemplate);
        try {
            NetworkStats networkStats = new NetworkStats(this.mContext, networkTemplate, this.mFlags, j, j2, this.mService);
            networkStats.startSummaryEnumeration();
            return networkStats;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NetworkStats queryTaggedSummary(NetworkTemplate networkTemplate, long j, long j2) throws SecurityException {
        Objects.requireNonNull(networkTemplate);
        try {
            NetworkStats networkStats = new NetworkStats(this.mContext, networkTemplate, this.mFlags, j, j2, this.mService);
            networkStats.startTaggedSummaryEnumeration();
            return networkStats;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NetworkStats queryDetailsForDevice(NetworkTemplate networkTemplate, long j, long j2) {
        Objects.requireNonNull(networkTemplate);
        try {
            NetworkStats networkStats = new NetworkStats(this.mContext, networkTemplate, this.mFlags, j, j2, this.mService);
            networkStats.startHistoryDeviceEnumeration();
            return networkStats;
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public NetworkStats queryDetailsForUid(int i, String str, long j, long j2, int i2) throws SecurityException {
        return queryDetailsForUidTagState(i, str, j, j2, i2, 0, -1);
    }

    public NetworkStats queryDetailsForUid(NetworkTemplate networkTemplate, long j, long j2, int i) throws SecurityException {
        return queryDetailsForUidTagState(networkTemplate, j, j2, i, 0, -1);
    }

    public NetworkStats queryDetailsForUidTag(int i, String str, long j, long j2, int i2, int i3) throws SecurityException {
        return queryDetailsForUidTagState(i, str, j, j2, i2, i3, -1);
    }

    public NetworkStats queryDetailsForUidTagState(int i, String str, long j, long j2, int i2, int i3, int i4) throws SecurityException {
        return queryDetailsForUidTagState(createTemplate(i, str), j, j2, i2, i3, i4);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public NetworkStats queryDetailsForUidTagState(NetworkTemplate networkTemplate, long j, long j2, int i, int i2, int i3) throws SecurityException {
        int i4 = i;
        int i5 = i2;
        int i6 = i3;
        Objects.requireNonNull(networkTemplate);
        try {
            NetworkStats networkStats = new NetworkStats(this.mContext, networkTemplate, this.mFlags, j, j2, this.mService);
            networkStats.startHistoryUidEnumeration(i4, i5, i6);
            return networkStats;
        } catch (RemoteException e) {
            Log.e(TAG, "Error while querying stats for uid=" + i4 + " tag=" + i5 + " state=" + i6, e);
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public NetworkStats queryDetails(int i, String str, long j, long j2) throws SecurityException, RemoteException {
        try {
            NetworkStats networkStats = new NetworkStats(this.mContext, createTemplate(i, str), this.mFlags, j, j2, this.mService);
            networkStats.startUserUidEnumeration();
            return networkStats;
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public android.net.NetworkStats getMobileUidStats() {
        try {
            return this.mService.getUidStatsForTransport(0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public android.net.NetworkStats getWifiUidStats() {
        try {
            return this.mService.getUidStatsForTransport(1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void registerUsageCallback(NetworkTemplate networkTemplate, long j, Executor executor, UsageCallback usageCallback) {
        Objects.requireNonNull(networkTemplate, "NetworkTemplate cannot be null");
        Objects.requireNonNull(usageCallback, "UsageCallback cannot be null");
        Objects.requireNonNull(executor, "Executor cannot be null");
        DataUsageRequest dataUsageRequest = new DataUsageRequest(0, networkTemplate, j);
        try {
            usageCallback.request = this.mService.registerUsageCallback(this.mContext.getOpPackageName(), dataUsageRequest, new UsageCallbackWrapper(executor, usageCallback));
            if (usageCallback.request == null) {
                Log.e(TAG, "Request from callback is null; should not happen");
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerUsageCallback(int i, String str, long j, UsageCallback usageCallback) {
        registerUsageCallback(i, str, j, usageCallback, (Handler) null);
    }

    public void registerUsageCallback(int i, String str, long j, UsageCallback usageCallback, Handler handler) {
        NetworkStatsManager$$ExternalSyntheticLambda1 networkStatsManager$$ExternalSyntheticLambda1;
        NetworkTemplate createTemplate = createTemplate(i, str);
        if (handler == null) {
            new NetworkStatsManager$$ExternalSyntheticLambda0
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0008: CONSTRUCTOR  (r7v3 ? I:android.app.usage.NetworkStatsManager$$ExternalSyntheticLambda0) =  call: android.app.usage.NetworkStatsManager$$ExternalSyntheticLambda0.<init>():void type: CONSTRUCTOR in method: android.app.usage.NetworkStatsManager.registerUsageCallback(int, java.lang.String, long, android.app.usage.NetworkStatsManager$UsageCallback, android.os.Handler):void, dex: classes.dex
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:256)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:221)
                	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
                	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:142)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
                	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
                	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
                	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:211)
                	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:204)
                	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:318)
                	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:271)
                	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:240)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
                	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
                	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
                	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
                	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:485)
                	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
                	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
                	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
                	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
                	at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
                	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:236)
                	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:227)
                	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:112)
                	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:78)
                	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
                	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:33)
                	at jadx.core.codegen.CodeGen.generate(CodeGen.java:21)
                	at jadx.core.ProcessClass.generateCode(ProcessClass.java:61)
                	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r7v3 ?
                	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:189)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:620)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:364)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:250)
                	... 34 more
                */
            /*
                this = this;
                android.net.NetworkTemplate r1 = createTemplate(r7, r8)
                if (r12 != 0) goto L_0x000c
                android.app.usage.NetworkStatsManager$$ExternalSyntheticLambda0 r7 = new android.app.usage.NetworkStatsManager$$ExternalSyntheticLambda0
                r7.<init>()
                goto L_0x0011
            L_0x000c:
                android.app.usage.NetworkStatsManager$$ExternalSyntheticLambda1 r7 = new android.app.usage.NetworkStatsManager$$ExternalSyntheticLambda1
                r7.<init>(r12)
            L_0x0011:
                r4 = r7
                r0 = r6
                r2 = r9
                r5 = r11
                r0.registerUsageCallback((android.net.NetworkTemplate) r1, (long) r2, (java.util.concurrent.Executor) r4, (android.app.usage.NetworkStatsManager.UsageCallback) r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.usage.NetworkStatsManager.registerUsageCallback(int, java.lang.String, long, android.app.usage.NetworkStatsManager$UsageCallback, android.os.Handler):void");
        }

        public void unregisterUsageCallback(UsageCallback usageCallback) {
            if (usageCallback == null || usageCallback.request == null || usageCallback.request.requestId == 0) {
                throw new IllegalArgumentException("Invalid UsageCallback");
            }
            try {
                this.mService.unregisterUsageRequest(usageCallback.request);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public static abstract class UsageCallback {
            /* access modifiers changed from: private */
            public DataUsageRequest request;

            public abstract void onThresholdReached(int i, String str);

            @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
            public void onThresholdReached(NetworkTemplate networkTemplate) {
                String str;
                int networkTypeForTemplate = networkTypeForTemplate(networkTemplate);
                if (networkTypeForTemplate != -1) {
                    if (networkTemplate.getSubscriberIds().isEmpty()) {
                        str = null;
                    } else {
                        str = networkTemplate.getSubscriberIds().iterator().next();
                    }
                    onThresholdReached(networkTypeForTemplate, str);
                }
            }

            private static int networkTypeForTemplate(NetworkTemplate networkTemplate) {
                int matchRule = networkTemplate.getMatchRule();
                if (matchRule != 1) {
                    return matchRule != 4 ? -1 : 1;
                }
                return 0;
            }
        }

        @SystemApi
        public void registerNetworkStatsProvider(String str, NetworkStatsProvider networkStatsProvider) {
            try {
                if (networkStatsProvider.getProviderCallbackBinder() == null) {
                    networkStatsProvider.setProviderCallbackBinder(this.mService.registerNetworkStatsProvider(str, networkStatsProvider.getProviderBinder()));
                    return;
                }
                throw new IllegalArgumentException("provider is already registered");
            } catch (RemoteException e) {
                e.rethrowAsRuntimeException();
            }
        }

        @SystemApi
        public void unregisterNetworkStatsProvider(NetworkStatsProvider networkStatsProvider) {
            try {
                networkStatsProvider.getProviderCallbackBinderOrThrow().unregister();
            } catch (RemoteException e) {
                e.rethrowAsRuntimeException();
            }
        }

        private static NetworkTemplate createTemplate(int i, String str) {
            if (i != 0) {
                if (i != 1) {
                    throw new IllegalArgumentException("Cannot create template for network type " + i + ", subscriberId '" + NetworkIdentityUtils.scrubSubscriberId(str) + "'.");
                } else if (TextUtils.isEmpty(str)) {
                    return NetworkTemplate.buildTemplateWifiWildcard();
                } else {
                    return NetworkTemplate.buildTemplateWifi(NetworkTemplate.WIFI_NETWORKID_ALL, str);
                }
            } else if (str == null) {
                return NetworkTemplate.buildTemplateMobileWildcard();
            } else {
                return NetworkTemplate.buildTemplateMobileAll(str);
            }
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public void notifyNetworkStatus(List<Network> list, List<NetworkStateSnapshot> list2, String str, List<UnderlyingNetworkInfo> list3) {
            try {
                Objects.requireNonNull(list);
                Objects.requireNonNull(list2);
                Objects.requireNonNull(list3);
                this.mService.notifyNetworkStatus((Network[]) list.toArray(new Network[0]), (NetworkStateSnapshot[]) list2.toArray(new NetworkStateSnapshot[0]), str, (UnderlyingNetworkInfo[]) list3.toArray(new UnderlyingNetworkInfo[0]));
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        private static class UsageCallbackWrapper extends IUsageCallback.Stub {
            private volatile UsageCallback mCallback;
            private final Executor mExecutor;

            UsageCallbackWrapper(Executor executor, UsageCallback usageCallback) {
                this.mCallback = usageCallback;
                this.mExecutor = executor;
            }

            public void onThresholdReached(DataUsageRequest dataUsageRequest) {
                UsageCallback usageCallback = this.mCallback;
                if (usageCallback != null) {
                    this.mExecutor.execute(new C0003xf2514dc4(usageCallback, dataUsageRequest));
                    return;
                }
                Log.e(NetworkStatsManager.TAG, "onThresholdReached with released callback for " + dataUsageRequest);
            }

            public void onCallbackReleased(DataUsageRequest dataUsageRequest) {
                this.mCallback = null;
            }
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public void noteUidForeground(int i, boolean z) {
            try {
                this.mService.noteUidForeground(i, z);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public void setDefaultGlobalAlert(long j) {
            try {
                this.mService.advisePersistThreshold(j);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public void forceUpdate() {
            try {
                this.mService.forceUpdate();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public void setStatsProviderWarningAndLimitAsync(String str, long j, long j2) {
            try {
                this.mService.setStatsProviderWarningAndLimitAsync(str, j, j2);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }
