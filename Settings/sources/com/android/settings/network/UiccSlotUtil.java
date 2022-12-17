package com.android.settings.network;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.UiccSlotMapping;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.utils.ThreadUtils;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UiccSlotUtil {
    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getSlotInfos$0(UiccSlotInfo uiccSlotInfo) {
        return uiccSlotInfo != null;
    }

    public static ImmutableList<UiccSlotInfo> getSlotInfos(TelephonyManager telephonyManager) {
        UiccSlotInfo[] uiccSlotsInfo = telephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            return ImmutableList.m25of();
        }
        return ImmutableList.copyOf((E[]) (UiccSlotInfo[]) Arrays.stream(uiccSlotsInfo).filter(new UiccSlotUtil$$ExternalSyntheticLambda4()).toArray(new UiccSlotUtil$$ExternalSyntheticLambda5()));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ UiccSlotInfo[] lambda$getSlotInfos$1(int i) {
        return new UiccSlotInfo[i];
    }

    public static synchronized void switchToRemovableSlot(int i, Context context) throws UiccSlotsException {
        synchronized (UiccSlotUtil.class) {
            switchToRemovableSlot(context, i, (SubscriptionInfo) null);
        }
    }

    public static synchronized void switchToRemovableSlot(Context context, int i, SubscriptionInfo subscriptionInfo) throws UiccSlotsException {
        synchronized (UiccSlotUtil.class) {
            if (!ThreadUtils.isMainThread()) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
                int inactiveRemovableSlot = getInactiveRemovableSlot(telephonyManager.getUiccSlotsInfo(), i);
                Log.d("UiccSlotUtil", "The InactiveRemovableSlot: " + inactiveRemovableSlot);
                if (inactiveRemovableSlot != -1) {
                    Collection simSlotMapping = telephonyManager.getSimSlotMapping();
                    Log.d("UiccSlotUtil", "The SimSlotMapping: " + simSlotMapping);
                    performSwitchToSlot(telephonyManager, prepareUiccSlotMappings(simSlotMapping, true, inactiveRemovableSlot, 0, getExcludedLogicalSlotIndex(simSlotMapping, SubscriptionUtil.getActiveSubscriptions((SubscriptionManager) context.getSystemService(SubscriptionManager.class)), subscriptionInfo, telephonyManager.isMultiSimEnabled())), context);
                    return;
                }
                return;
            }
            throw new IllegalThreadStateException("Do not call switchToRemovableSlot on the main thread.");
        }
    }

    public static synchronized void switchToEuiccSlot(Context context, int i, int i2, SubscriptionInfo subscriptionInfo) throws UiccSlotsException {
        synchronized (UiccSlotUtil.class) {
            if (!ThreadUtils.isMainThread()) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
                Collection simSlotMapping = telephonyManager.getSimSlotMapping();
                Log.d("UiccSlotUtil", "The SimSlotMapping: " + simSlotMapping);
                if (isTargetSlotActive(simSlotMapping, i, i2)) {
                    Log.d("UiccSlotUtil", "The slot is active, then the sim can enable directly.");
                } else {
                    performSwitchToSlot(telephonyManager, prepareUiccSlotMappings(simSlotMapping, false, i, i2, getExcludedLogicalSlotIndex(simSlotMapping, SubscriptionUtil.getActiveSubscriptions((SubscriptionManager) context.getSystemService(SubscriptionManager.class)), subscriptionInfo, telephonyManager.isMultiSimEnabled())), context);
                }
            } else {
                throw new IllegalThreadStateException("Do not call switchToRemovableSlot on the main thread.");
            }
        }
    }

    public static int getEsimSlotId(Context context, int i) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        List<UiccCardInfo> uiccCardsInfo = telephonyManager.getUiccCardsInfo();
        ImmutableList<UiccSlotInfo> slotInfos = getSlotInfos(telephonyManager);
        SubscriptionInfo subById = SubscriptionUtil.getSubById((SubscriptionManager) context.getSystemService(SubscriptionManager.class), i);
        if (subById != null && subById.isEmbedded()) {
            for (UiccCardInfo next : uiccCardsInfo) {
                if (next.getCardId() == subById.getCardId() && next.getCardId() > -1 && next.isEuicc() && next.isRemovable()) {
                    Log.d("UiccSlotUtil", "getEsimSlotId: This subInfo is removable esim.");
                    return next.getPhysicalSlotIndex();
                }
            }
        }
        int orElse = IntStream.range(0, slotInfos.size()).filter(new UiccSlotUtil$$ExternalSyntheticLambda3(slotInfos)).findFirst().orElse(-1);
        Log.i("UiccSlotUtil", "firstEsimSlot: " + orElse);
        return orElse;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getEsimSlotId$2(ImmutableList immutableList, int i) {
        UiccSlotInfo uiccSlotInfo = (UiccSlotInfo) immutableList.get(i);
        if (uiccSlotInfo == null) {
            return false;
        }
        return uiccSlotInfo.getIsEuicc();
    }

    private static boolean isTargetSlotActive(Collection<UiccSlotMapping> collection, int i, int i2) {
        return collection.stream().anyMatch(new UiccSlotUtil$$ExternalSyntheticLambda6(i, i2));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$isTargetSlotActive$3(int i, int i2, UiccSlotMapping uiccSlotMapping) {
        return uiccSlotMapping.getPhysicalSlotIndex() == i && uiccSlotMapping.getPortIndex() == i2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0040  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void performSwitchToSlot(android.telephony.TelephonyManager r5, java.util.Collection<android.telephony.UiccSlotMapping> r6, android.content.Context r7) throws com.android.settings.network.UiccSlotsException {
        /*
            android.content.ContentResolver r0 = r7.getContentResolver()
            java.lang.String r1 = "euicc_switch_slot_timeout_millis"
            r2 = 25000(0x61a8, double:1.23516E-319)
            long r0 = android.provider.Settings.Global.getLong(r0, r1, r2)
            r2 = 0
            java.util.concurrent.CountDownLatch r3 = new java.util.concurrent.CountDownLatch     // Catch:{ InterruptedException -> 0x002f }
            r4 = 1
            r3.<init>(r4)     // Catch:{ InterruptedException -> 0x002f }
            com.android.settings.network.CarrierConfigChangedReceiver r4 = new com.android.settings.network.CarrierConfigChangedReceiver     // Catch:{ InterruptedException -> 0x002f }
            r4.<init>(r3)     // Catch:{ InterruptedException -> 0x002f }
            r4.registerOn(r7)     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            r5.setSimSlotMapping(r6)     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            java.util.concurrent.TimeUnit r5 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            r3.await(r0, r5)     // Catch:{ InterruptedException -> 0x002a, all -> 0x0027 }
            r7.unregisterReceiver(r4)
            goto L_0x0043
        L_0x0027:
            r5 = move-exception
            r2 = r4
            goto L_0x0044
        L_0x002a:
            r5 = move-exception
            r2 = r4
            goto L_0x0030
        L_0x002d:
            r5 = move-exception
            goto L_0x0044
        L_0x002f:
            r5 = move-exception
        L_0x0030:
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x002d }
            r6.interrupt()     // Catch:{ all -> 0x002d }
            java.lang.String r6 = "UiccSlotUtil"
            java.lang.String r0 = "Failed switching to physical slot."
            android.util.Log.e(r6, r0, r5)     // Catch:{ all -> 0x002d }
            if (r2 == 0) goto L_0x0043
            r7.unregisterReceiver(r2)
        L_0x0043:
            return
        L_0x0044:
            if (r2 == 0) goto L_0x0049
            r7.unregisterReceiver(r2)
        L_0x0049:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.UiccSlotUtil.performSwitchToSlot(android.telephony.TelephonyManager, java.util.Collection, android.content.Context):void");
    }

    private static int getInactiveRemovableSlot(UiccSlotInfo[] uiccSlotInfoArr, int i) throws UiccSlotsException {
        if (uiccSlotInfoArr != null) {
            if (i == -1) {
                for (int i2 = 0; i2 < uiccSlotInfoArr.length; i2++) {
                    if (uiccSlotInfoArr[i2].isRemovable() && !((UiccPortInfo) uiccSlotInfoArr[i2].getPorts().stream().findFirst().get()).isActive() && uiccSlotInfoArr[i2].getCardStateInfo() != 3 && uiccSlotInfoArr[i2].getCardStateInfo() != 4) {
                        return i2;
                    }
                }
            } else if (i >= uiccSlotInfoArr.length || !uiccSlotInfoArr[i].isRemovable()) {
                throw new UiccSlotsException("The given slotId is not a removable slot: " + i);
            } else if (!((UiccPortInfo) uiccSlotInfoArr[i].getPorts().stream().findFirst().get()).isActive()) {
                return i;
            }
            return -1;
        }
        throw new UiccSlotsException("UiccSlotInfo is null");
    }

    @VisibleForTesting
    static Collection<UiccSlotMapping> prepareUiccSlotMappings(Collection<UiccSlotMapping> collection, boolean z, int i, int i2, int i3) {
        if (i3 == -1) {
            Log.d("UiccSlotUtil", "There is no removedLogicalSlotId. Do nothing.");
            return collection;
        }
        int i4 = 0;
        Log.d("UiccSlotUtil", String.format("Create new SimSlotMapping. Remove the UiccSlotMapping of logicalSlot%d, and insert PhysicalSlotId%d-Port%d", new Object[]{Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i2)}));
        ArrayList arrayList = new ArrayList();
        if (z) {
            arrayList.add(new UiccSlotMapping(i2, i, 0));
            i4 = 1;
        }
        for (UiccSlotMapping uiccSlotMapping : (Collection) collection.stream().sorted(Comparator.comparingInt(new UiccSlotUtil$$ExternalSyntheticLambda1())).collect(Collectors.toList())) {
            if (uiccSlotMapping.getLogicalSlotIndex() != i3) {
                if (z) {
                    uiccSlotMapping = new UiccSlotMapping(uiccSlotMapping.getPortIndex(), uiccSlotMapping.getPhysicalSlotIndex(), i4);
                    i4++;
                }
                arrayList.add(uiccSlotMapping);
            } else if (!z) {
                arrayList.add(new UiccSlotMapping(i2, i, uiccSlotMapping.getLogicalSlotIndex()));
            }
        }
        Log.d("UiccSlotUtil", "The new SimSlotMapping: " + arrayList);
        return arrayList;
    }

    @VisibleForTesting
    static int getExcludedLogicalSlotIndex(Collection<UiccSlotMapping> collection, Collection<SubscriptionInfo> collection2, SubscriptionInfo subscriptionInfo, boolean z) {
        if (!z) {
            Log.i("UiccSlotUtil", "In the ss mode.");
            return 0;
        } else if (subscriptionInfo != null) {
            Log.i("UiccSlotUtil", "The removedSubInfo is not null");
            return subscriptionInfo.getSimSlotIndex();
        } else {
            Log.i("UiccSlotUtil", "The removedSubInfo is null");
            return collection.stream().filter(new UiccSlotUtil$$ExternalSyntheticLambda0(collection2)).sorted(Comparator.comparingInt(new UiccSlotUtil$$ExternalSyntheticLambda1())).mapToInt(new UiccSlotUtil$$ExternalSyntheticLambda2()).findFirst().orElse(-1);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getExcludedLogicalSlotIndex$4(Collection collection, UiccSlotMapping uiccSlotMapping) {
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            if (((SubscriptionInfo) it.next()).getSimSlotIndex() == uiccSlotMapping.getLogicalSlotIndex()) {
                return false;
            }
        }
        return true;
    }
}
