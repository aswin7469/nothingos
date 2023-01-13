package com.nothing.systemui.p024qs.tiles;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import com.nothing.p023os.device.DeviceConstant;
import com.nothing.p023os.device.DeviceServiceConnector;
import com.nothing.p023os.device.IDeviceBitmap;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.util.NTLogUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\u0018\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\u001a\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016¨\u0006\r"}, mo65043d2 = {"com/nothing/systemui/qs/tiles/BluetoothTileEx$deviceConnectorCallback$1", "Lcom/nothing/os/device/DeviceServiceConnector$Callback;", "onDeviceServiceConnected", "", "onDeviceServiceDisConnected", "onFail", "i", "", "i1", "onSuccess", "command", "bundle", "Landroid/os/Bundle;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.nothing.systemui.qs.tiles.BluetoothTileEx$deviceConnectorCallback$1 */
/* compiled from: BluetoothTileEx.kt */
public final class BluetoothTileEx$deviceConnectorCallback$1 implements DeviceServiceConnector.Callback {
    final /* synthetic */ BluetoothTileEx this$0;

    BluetoothTileEx$deviceConnectorCallback$1(BluetoothTileEx bluetoothTileEx) {
        this.this$0 = bluetoothTileEx;
    }

    public void onDeviceServiceConnected() {
        BluetoothTileEx.AncCallback ancCallback = this.this$0.getAncCallback();
        if (ancCallback != null) {
            ancCallback.onDeviceServiceConnected();
        }
    }

    public void onDeviceServiceDisConnected() {
        BluetoothTileEx.AncCallback ancCallback = this.this$0.getAncCallback();
        if (ancCallback != null) {
            ancCallback.onDeviceServiceDisConnected();
        }
    }

    public void onFail(int i, int i2) {
        BluetoothTileEx.AncCallback ancCallback = this.this$0.getAncCallback();
        if (ancCallback != null) {
            ancCallback.onFail(i, i2);
        }
    }

    public void onSuccess(int i, Bundle bundle) {
        BluetoothTileEx.AncCallback ancCallback = this.this$0.getAncCallback();
        if (ancCallback != null) {
            ancCallback.onSuccess(i, bundle);
        }
        if (bundle != null) {
            String string = bundle.getString(DeviceConstant.KEY_MAC_ADDRESS);
            Intrinsics.checkNotNullExpressionValue(string, "bundle.getString(DeviceConstant.KEY_MAC_ADDRESS)");
            if (!TextUtils.isEmpty(string)) {
                if (i == 0) {
                    this.this$0.getCommand(4, string);
                } else if (i == 3) {
                    String modeID = this.this$0.getModeID(string);
                    if (modeID != null && IDeviceBitmap.Stub.asInterface(bundle.getBinder(DeviceConstant.KEY_BITMAP)) != null) {
                        Bitmap defaultBitmap = IDeviceBitmap.Stub.asInterface(bundle.getBinder(DeviceConstant.KEY_BITMAP)).getDefaultBitmap();
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(defaultBitmap);
                        if (this.this$0.isSupportAirpods(string)) {
                            String string2 = bundle.getString(DeviceConstant.KEY_VALUE_STRING);
                            String airpodsVersion = this.this$0.getAirpodsVersion(string);
                            NTLogUtil.m1686d("BluetoothTileEx", "onSuccess Airpods GET_EAR_BITMAP version: " + string2 + ", savedVersion: " + airpodsVersion);
                            CharSequence charSequence = string2;
                            if (!TextUtils.equals(airpodsVersion, charSequence) && !TextUtils.isEmpty(charSequence)) {
                                BluetoothTileEx bluetoothTileEx = this.this$0;
                                Intrinsics.checkNotNullExpressionValue(string2, "version");
                                bluetoothTileEx.saveAirpodsVersion(string, string2);
                                BluetoothTileEx bluetoothTileEx2 = this.this$0;
                                Intrinsics.checkNotNullExpressionValue(defaultBitmap, "defaultBitmap");
                                bluetoothTileEx2.saveModuleIDEarBitmap(defaultBitmap, string2);
                            }
                        }
                        if (this.this$0.getIconCache().size() <= 0 || this.this$0.getIconCache().get(modeID) == null) {
                            BluetoothTileEx bluetoothTileEx3 = this.this$0;
                            Intrinsics.checkNotNullExpressionValue(defaultBitmap, "defaultBitmap");
                            bluetoothTileEx3.saveModuleIDEarBitmap(defaultBitmap, modeID);
                        }
                        this.this$0.getIconCache().put(modeID, bitmapDrawable);
                        BluetoothTileEx.AncCallback ancCallback2 = this.this$0.getAncCallback();
                        if (ancCallback2 != null) {
                            ancCallback2.onDeviceBitmapLoaded(string, bitmapDrawable);
                        }
                    } else {
                        return;
                    }
                } else if (i == 4) {
                    int i2 = bundle.getInt(DeviceConstant.KEY_BATTERY_LEFT);
                    int i3 = bundle.getInt(DeviceConstant.KEY_BATTERY_RIGHT);
                    BluetoothTileEx.BluetoothBatteryDate bluetoothBatteryDate = new BluetoothTileEx.BluetoothBatteryDate();
                    bluetoothBatteryDate.setAddress(string);
                    bluetoothBatteryDate.setBatteryLeft(i2);
                    bluetoothBatteryDate.setBatteryRight(i3);
                    this.this$0.getBluetoothBatteryDates().put(string, bluetoothBatteryDate);
                    NTLogUtil.m1686d("BluetoothTileEx", "onSuccess batteryLeft = " + i2 + ", batteryRight = " + i3);
                }
                this.this$0.refreshState((Object) null);
            }
        }
    }
}
