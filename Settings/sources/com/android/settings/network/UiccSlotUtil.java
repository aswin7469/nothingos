package com.android.settings.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.UiccSlotInfo;
import android.util.Log;
import com.android.settingslib.utils.ThreadUtils;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class UiccSlotUtil {
    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getSlotInfos$0(UiccSlotInfo uiccSlotInfo) {
        return uiccSlotInfo != null;
    }

    public static ImmutableList<UiccSlotInfo> getSlotInfos(TelephonyManager telephonyManager) {
        UiccSlotInfo[] uiccSlotsInfo = telephonyManager.getUiccSlotsInfo();
        if (uiccSlotsInfo == null) {
            return ImmutableList.of();
        }
        return ImmutableList.copyOf((UiccSlotInfo[]) Arrays.stream(uiccSlotsInfo).filter(UiccSlotUtil$$ExternalSyntheticLambda1.INSTANCE).toArray(UiccSlotUtil$$ExternalSyntheticLambda0.INSTANCE));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ UiccSlotInfo[] lambda$getSlotInfos$1(int i) {
        return new UiccSlotInfo[i];
    }

    public static synchronized void switchToRemovableSlot(int i, Context context) throws UiccSlotsException {
        synchronized (UiccSlotUtil.class) {
            if (ThreadUtils.isMainThread()) {
                throw new IllegalThreadStateException("Do not call switchToRemovableSlot on the main thread.");
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            if (telephonyManager.isMultiSimEnabled()) {
                Log.i("UiccSlotUtil", "Multiple active slots supported. Not calling switchSlots.");
                return;
            }
            ImmutableList<UiccSlotInfo> slotInfos = getSlotInfos(telephonyManager);
            int size = slotInfos.size();
            if (i == -1) {
                for (int i2 = 0; i2 < size; i2++) {
                    UiccSlotInfo uiccSlotInfo = slotInfos.get(i2);
                    if (uiccSlotInfo.isRemovable() && !uiccSlotInfo.getIsActive() && uiccSlotInfo.getCardStateInfo() != 3 && uiccSlotInfo.getCardStateInfo() != 4) {
                        performSwitchToRemovableSlot(i2, context);
                        return;
                    }
                }
            } else if (i >= size || !slotInfos.get(i).isRemovable()) {
                throw new UiccSlotsException("The given slotId is not a removable slot: " + i);
            } else if (!slotInfos.get(i).getIsActive()) {
                performSwitchToRemovableSlot(i, context);
            }
        }
    }

    private static void performSwitchToRemovableSlot(int i, Context context) throws UiccSlotsException {
        CountDownLatch countDownLatch;
        CarrierConfigChangedReceiver carrierConfigChangedReceiver;
        long j = Settings.Global.getLong(context.getContentResolver(), "euicc_switch_slot_timeout_millis", 25000L);
        BroadcastReceiver broadcastReceiver = null;
        try {
            try {
                countDownLatch = new CountDownLatch(1);
                carrierConfigChangedReceiver = new CarrierConfigChangedReceiver(countDownLatch);
            } catch (InterruptedException e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            carrierConfigChangedReceiver.registerOn(context);
            switchSlots(context, i);
            countDownLatch.await(j, TimeUnit.MILLISECONDS);
            context.unregisterReceiver(carrierConfigChangedReceiver);
        } catch (InterruptedException e2) {
            e = e2;
            broadcastReceiver = carrierConfigChangedReceiver;
            Thread.currentThread().interrupt();
            Log.e("UiccSlotUtil", "Failed switching to physical slot.", e);
            if (broadcastReceiver == null) {
                return;
            }
            context.unregisterReceiver(broadcastReceiver);
        } catch (Throwable th2) {
            th = th2;
            broadcastReceiver = carrierConfigChangedReceiver;
            if (broadcastReceiver != null) {
                context.unregisterReceiver(broadcastReceiver);
            }
            throw th;
        }
    }

    private static void switchSlots(Context context, int... iArr) throws UiccSlotsException {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        if (telephonyManager.isMultiSimEnabled()) {
            Log.i("UiccSlotUtil", "Multiple active slots supported. Not calling switchSlots.");
        } else if (!telephonyManager.switchSlots(iArr)) {
            throw new UiccSlotsException("Failed to switch slots");
        }
    }
}
