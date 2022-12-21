package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.DeviceConfig;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.core.graphics.drawable.IconCompat;
import com.android.settingslib.C1757R;
import com.android.settingslib.widget.AdaptiveIcon;
import com.android.settingslib.widget.AdaptiveOutlineDrawable;
import java.p026io.IOException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BluetoothUtils {
    public static final String BT_ADVANCED_HEADER_ENABLED = "bt_advanced_header_enabled";

    /* renamed from: D */
    public static final boolean f239D = true;
    private static final String KEY_HEARABLE_CONTROL_SLICE = "HEARABLE_CONTROL_SLICE_WITH_WIDTH";
    private static final int METADATA_FAST_PAIR_CUSTOMIZED_FIELDS = 25;
    public static final int META_INT_ERROR = -1;
    private static final String TAG = "BluetoothUtils";

    /* renamed from: V */
    public static final boolean f240V = false;
    private static ErrorListener sErrorListener;

    public interface ErrorListener {
        void onShowError(Context context, String str, int i);
    }

    public static int getConnectionStateSummary(int i) {
        if (i == 0) {
            return C1757R.string.bluetooth_disconnected;
        }
        if (i == 1) {
            return C1757R.string.bluetooth_connecting;
        }
        if (i == 2) {
            return C1757R.string.bluetooth_connected;
        }
        if (i != 3) {
            return 0;
        }
        return C1757R.string.bluetooth_disconnecting;
    }

    static void showError(Context context, String str, int i) {
        ErrorListener errorListener = sErrorListener;
        if (errorListener != null) {
            errorListener.onShowError(context, str, i);
        }
    }

    public static void setErrorListener(ErrorListener errorListener) {
        sErrorListener = errorListener;
    }

    public static Pair<Drawable, String> getBtClassDrawableWithDescription(Context context, CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothClass btClass = cachedBluetoothDevice.getBtClass();
        if (btClass != null) {
            int majorDeviceClass = btClass.getMajorDeviceClass();
            if (majorDeviceClass == 256) {
                return new Pair<>(getBluetoothDrawable(context, 17302339), context.getString(C1757R.string.bluetooth_talkback_computer));
            }
            if (majorDeviceClass == 512) {
                return new Pair<>(getBluetoothDrawable(context, 17302817), context.getString(C1757R.string.bluetooth_talkback_phone));
            }
            if (majorDeviceClass == 1280) {
                return new Pair<>(getBluetoothDrawable(context, HidProfile.getHidClassDrawable(btClass)), context.getString(C1757R.string.bluetooth_talkback_input_peripheral));
            }
            if (majorDeviceClass == 1536) {
                return new Pair<>(getBluetoothDrawable(context, 17302850), context.getString(C1757R.string.bluetooth_talkback_imaging));
            }
            if (!cachedBluetoothDevice.isLeAudioEnabled() && (btClass.getClassOfDevice() & 16384) == 16384) {
                return new Pair<>(getBluetoothDrawable(context, C1757R.C1759drawable.ic_adv_audio), context.getString(C1757R.string.bluetooth_talkback_group));
            }
        }
        for (LocalBluetoothProfile drawableResource : cachedBluetoothDevice.getProfiles()) {
            int drawableResource2 = drawableResource.getDrawableResource(btClass);
            if (drawableResource2 != 0) {
                return new Pair<>(getBluetoothDrawable(context, drawableResource2), (Object) null);
            }
        }
        if (btClass != null) {
            if (doesClassMatch(btClass, 0)) {
                return new Pair<>(getBluetoothDrawable(context, 17302337), context.getString(C1757R.string.bluetooth_talkback_headset));
            }
            if (doesClassMatch(btClass, 1)) {
                return new Pair<>(getBluetoothDrawable(context, 17302336), context.getString(C1757R.string.bluetooth_talkback_headphone));
            }
        }
        return new Pair<>(getBluetoothDrawable(context, 17302848).mutate(), context.getString(C1757R.string.bluetooth_talkback_bluetooth));
    }

    public static Drawable getBluetoothDrawable(Context context, int i) {
        return context.getDrawable(i);
    }

    public static Pair<Drawable, String> getBtRainbowDrawableWithDescription(Context context, CachedBluetoothDevice cachedBluetoothDevice) {
        Resources resources = context.getResources();
        Pair<Drawable, String> btDrawableWithDescription = getBtDrawableWithDescription(context, cachedBluetoothDevice);
        if (btDrawableWithDescription.first instanceof BitmapDrawable) {
            return new Pair<>(new AdaptiveOutlineDrawable(resources, ((BitmapDrawable) btDrawableWithDescription.first).getBitmap()), (String) btDrawableWithDescription.second);
        }
        return new Pair<>(buildBtRainbowDrawable(context, (Drawable) btDrawableWithDescription.first, cachedBluetoothDevice.getAddress().hashCode()), (String) btDrawableWithDescription.second);
    }

    public static Drawable buildBtRainbowDrawable(Context context, Drawable drawable, int i) {
        Resources resources = context.getResources();
        int[] intArray = resources.getIntArray(C1757R.array.bt_icon_fg_colors);
        int[] intArray2 = resources.getIntArray(C1757R.array.bt_icon_bg_colors);
        int abs = Math.abs(i % intArray2.length);
        drawable.setTint(intArray[abs]);
        AdaptiveIcon adaptiveIcon = new AdaptiveIcon(context, drawable);
        AdaptiveIcon adaptiveIcon2 = adaptiveIcon;
        adaptiveIcon.setBackgroundColor(intArray2[abs]);
        return adaptiveIcon;
    }

    public static Pair<Drawable, String> getBtDrawableWithDescription(Context context, CachedBluetoothDevice cachedBluetoothDevice) {
        Uri uriMetaData;
        Pair<Drawable, String> btClassDrawableWithDescription = getBtClassDrawableWithDescription(context, cachedBluetoothDevice);
        BluetoothDevice device = cachedBluetoothDevice.getDevice();
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1757R.dimen.bt_nearby_icon_size);
        Resources resources = context.getResources();
        if (isAdvancedDetailsHeader(device) && (uriMetaData = getUriMetaData(device, 5)) != null) {
            try {
                context.getContentResolver().takePersistableUriPermission(uriMetaData, 1);
            } catch (SecurityException e) {
                Log.e(TAG, "Failed to take persistable permission for: " + uriMetaData, e);
            }
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriMetaData);
                if (bitmap != null) {
                    Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, false);
                    bitmap.recycle();
                    return new Pair<>(new BitmapDrawable(resources, createScaledBitmap), (String) btClassDrawableWithDescription.second);
                }
            } catch (IOException e2) {
                Log.e(TAG, "Failed to get drawable for: " + uriMetaData, e2);
            } catch (SecurityException e3) {
                Log.e(TAG, "Failed to get permission for: " + uriMetaData, e3);
            }
        }
        return new Pair<>((Drawable) btClassDrawableWithDescription.first, (String) btClassDrawableWithDescription.second);
    }

    public static boolean isAdvancedDetailsHeader(BluetoothDevice bluetoothDevice) {
        if (!DeviceConfig.getBoolean("settings_ui", BT_ADVANCED_HEADER_ENABLED, true)) {
            Log.d(TAG, "isAdvancedDetailsHeader: advancedEnabled is false");
            return false;
        } else if (getBooleanMetaData(bluetoothDevice, 6)) {
            Log.d(TAG, "isAdvancedDetailsHeader: untetheredHeadset is true");
            return true;
        } else {
            String stringMetaData = getStringMetaData(bluetoothDevice, 17);
            if (!TextUtils.equals(stringMetaData, "Untethered Headset") && !TextUtils.equals(stringMetaData, "Watch") && !TextUtils.equals(stringMetaData, "Default")) {
                return false;
            }
            Log.d(TAG, "isAdvancedDetailsHeader: deviceType is " + stringMetaData);
            return true;
        }
    }

    public static IconCompat createIconWithDrawable(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (intrinsicWidth <= 0) {
                intrinsicWidth = 1;
            }
            if (intrinsicHeight <= 0) {
                intrinsicHeight = 1;
            }
            bitmap = createBitmap(drawable, intrinsicWidth, intrinsicHeight);
        }
        return IconCompat.createWithBitmap(bitmap);
    }

    public static Drawable buildAdvancedDrawable(Context context, Drawable drawable) {
        Bitmap bitmap;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1757R.dimen.advanced_icon_size);
        Resources resources = context.getResources();
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (intrinsicWidth <= 0) {
                intrinsicWidth = 1;
            }
            if (intrinsicHeight <= 0) {
                intrinsicHeight = 1;
            }
            bitmap = createBitmap(drawable, intrinsicWidth, intrinsicHeight);
        }
        if (bitmap == null) {
            return drawable;
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, false);
        bitmap.recycle();
        return new AdaptiveOutlineDrawable(resources, createScaledBitmap, 1);
    }

    public static Bitmap createBitmap(Drawable drawable, int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public static boolean getBooleanMetaData(BluetoothDevice bluetoothDevice, int i) {
        byte[] metadata;
        if (bluetoothDevice == null || (metadata = bluetoothDevice.getMetadata(i)) == null) {
            return false;
        }
        return Boolean.parseBoolean(new String(metadata));
    }

    public static String getStringMetaData(BluetoothDevice bluetoothDevice, int i) {
        byte[] metadata;
        if (bluetoothDevice == null || (metadata = bluetoothDevice.getMetadata(i)) == null) {
            return null;
        }
        return new String(metadata);
    }

    public static int getIntMetaData(BluetoothDevice bluetoothDevice, int i) {
        byte[] metadata;
        if (bluetoothDevice == null || (metadata = bluetoothDevice.getMetadata(i)) == null) {
            return -1;
        }
        try {
            return Integer.parseInt(new String(metadata));
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public static Uri getUriMetaData(BluetoothDevice bluetoothDevice, int i) {
        String stringMetaData = getStringMetaData(bluetoothDevice, i);
        if (stringMetaData == null) {
            return null;
        }
        return Uri.parse(stringMetaData);
    }

    public static String getControlUriMetaData(BluetoothDevice bluetoothDevice) {
        return extraTagValue(KEY_HEARABLE_CONTROL_SLICE, getStringMetaData(bluetoothDevice, 25));
    }

    private static boolean doesClassMatch(BluetoothClass bluetoothClass, int i) {
        return bluetoothClass.doesClassMatch(i);
    }

    private static String extraTagValue(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        Matcher matcher = Pattern.compile(generateExpressionWithTag(str, "(.*?)")).matcher(str2);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String getTagStart(String str) {
        return String.format(Locale.ENGLISH, "<%s>", str);
    }

    private static String getTagEnd(String str) {
        return String.format(Locale.ENGLISH, "</%s>", str);
    }

    private static String generateExpressionWithTag(String str, String str2) {
        return getTagStart(str) + str2 + getTagEnd(str);
    }
}
