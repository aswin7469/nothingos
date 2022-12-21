package androidx.slice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceItemHolder;
import androidx.slice.core.SliceActionImpl;
import androidx.slice.core.SliceHints;
import androidx.slice.core.SliceQuery;
import androidx.versionedparcelable.ParcelUtils;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.nio.charset.Charset;
import java.p026io.BufferedInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.List;

public class SliceUtils {

    public interface SliceActionListener {
        void onSliceAction(Uri uri, Context context, Intent intent);
    }

    private SliceUtils() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0016, code lost:
        r4 = r5.getShortcut((android.content.Context) null);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static androidx.slice.Slice stripSlice(androidx.slice.Slice r3, int r4, boolean r5) {
        /*
            androidx.slice.widget.ListContent r5 = new androidx.slice.widget.ListContent
            r5.<init>(r3)
            boolean r0 = r5.isValid()
            if (r0 == 0) goto L_0x007c
            androidx.slice.Slice$Builder r0 = copyMetadata(r3)
            r1 = 1
            if (r4 == r1) goto L_0x0024
            r1 = 3
            if (r4 == r1) goto L_0x0016
            return r3
        L_0x0016:
            r4 = 0
            androidx.slice.core.SliceAction r4 = r5.getShortcut(r4)
            if (r4 == 0) goto L_0x0023
            androidx.slice.core.SliceActionImpl r4 = (androidx.slice.core.SliceActionImpl) r4
            androidx.slice.Slice r3 = r4.buildSlice(r0)
        L_0x0023:
            return r3
        L_0x0024:
            androidx.slice.widget.RowContent r3 = r5.getHeader()
            androidx.slice.SliceItem r3 = r3.getSliceItem()
            r0.addItem(r3)
            java.util.List r3 = r5.getSliceActions()
            if (r3 == 0) goto L_0x0078
            int r4 = r3.size()
            if (r4 <= 0) goto L_0x0078
            androidx.slice.Slice$Builder r4 = new androidx.slice.Slice$Builder
            r4.<init>((androidx.slice.Slice.Builder) r0)
            java.util.Iterator r3 = r3.iterator()
        L_0x0044:
            boolean r5 = r3.hasNext()
            java.lang.String r1 = "actions"
            if (r5 == 0) goto L_0x0069
            java.lang.Object r5 = r3.next()
            androidx.slice.core.SliceAction r5 = (androidx.slice.core.SliceAction) r5
            androidx.slice.Slice$Builder r2 = new androidx.slice.Slice$Builder
            r2.<init>((androidx.slice.Slice.Builder) r4)
            java.lang.String[] r1 = new java.lang.String[]{r1}
            androidx.slice.Slice$Builder r1 = r2.addHints((java.lang.String[]) r1)
            androidx.slice.core.SliceActionImpl r5 = (androidx.slice.core.SliceActionImpl) r5
            androidx.slice.Slice r5 = r5.buildSlice(r1)
            r4.addSubSlice(r5)
            goto L_0x0044
        L_0x0069:
            java.lang.String[] r3 = new java.lang.String[]{r1}
            androidx.slice.Slice$Builder r3 = r4.addHints((java.lang.String[]) r3)
            androidx.slice.Slice r3 = r3.build()
            r0.addSubSlice(r3)
        L_0x0078:
            androidx.slice.Slice r3 = r0.build()
        L_0x007c:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.SliceUtils.stripSlice(androidx.slice.Slice, int, boolean):androidx.slice.Slice");
    }

    private static Slice.Builder copyMetadata(Slice slice) {
        Slice.Builder builder = new Slice.Builder(slice.getUri());
        SliceItem findTopLevelItem = SliceQuery.findTopLevelItem(slice, "long", "ttl", (String[]) null, (String[]) null);
        if (findTopLevelItem != null) {
            builder.addLong(findTopLevelItem.getLong(), SliceHints.SUBTYPE_MILLIS, "ttl");
        }
        SliceItem findTopLevelItem2 = SliceQuery.findTopLevelItem(slice, "long", "last_updated", (String[]) null, (String[]) null);
        if (findTopLevelItem2 != null) {
            builder.addLong(findTopLevelItem2.getLong(), SliceHints.SUBTYPE_MILLIS, "last_updated");
        }
        SliceItem findTopLevelItem3 = SliceQuery.findTopLevelItem(slice, "int", "color", (String[]) null, (String[]) null);
        if (findTopLevelItem3 != null) {
            builder.addInt(findTopLevelItem3.getInt(), "color", new String[0]);
        }
        SliceItem findTopLevelItem4 = SliceQuery.findTopLevelItem(slice, "int", "layout_direction", (String[]) null, (String[]) null);
        if (findTopLevelItem4 != null) {
            builder.addInt(findTopLevelItem4.getInt(), "layout_direction", new String[0]);
        }
        List<String> sliceKeywords = SliceMetadata.from((Context) null, slice).getSliceKeywords();
        if (sliceKeywords != null && sliceKeywords.size() > 0) {
            Slice.Builder builder2 = new Slice.Builder(builder);
            for (String addText : sliceKeywords) {
                builder2.addText((CharSequence) addText, (String) null, new String[0]);
            }
            builder.addSubSlice(builder2.addHints(BaseIconCache.IconDB.COLUMN_KEYWORDS).build());
        }
        List<String> hints = slice.getHints();
        if (hints.size() > 0) {
            builder.addHints(hints);
        }
        return builder;
    }

    public static void serializeSlice(Slice slice, final Context context, OutputStream outputStream, final SerializeOptions serializeOptions) throws IllegalArgumentException {
        synchronized (SliceItemHolder.sSerializeLock) {
            SliceItemHolder.sHandler = new SliceItemHolder.HolderHandler() {
                public void handle(SliceItemHolder sliceItemHolder, String str) {
                    SliceUtils.handleOptions(context, sliceItemHolder, str, serializeOptions);
                }
            };
            ParcelUtils.toOutputStream(slice, outputStream);
            SliceItemHolder.sHandler = null;
        }
    }

    static void handleOptions(Context context, SliceItemHolder sliceItemHolder, String str, SerializeOptions serializeOptions) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1422950858:
                if (str.equals("action")) {
                    c = 0;
                    break;
                }
                break;
            case 100313435:
                if (str.equals("image")) {
                    c = 1;
                    break;
                }
                break;
            case 100358090:
                if (str.equals("input")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                int actionMode = serializeOptions.getActionMode();
                if (actionMode == 0) {
                    throw new IllegalArgumentException("Cannot serialize action");
                } else if (actionMode == 1) {
                    sliceItemHolder.mVersionedParcelable = null;
                    return;
                } else if (actionMode == 2) {
                    sliceItemHolder.mParcelable = null;
                    return;
                } else {
                    return;
                }
            case 1:
                int imageMode = serializeOptions.getImageMode();
                if (imageMode == 0) {
                    throw new IllegalArgumentException("Cannot serialize icon");
                } else if (imageMode == 1) {
                    sliceItemHolder.mVersionedParcelable = null;
                    return;
                } else if (imageMode == 2) {
                    sliceItemHolder.mVersionedParcelable = convert(context, (IconCompat) sliceItemHolder.mVersionedParcelable, serializeOptions);
                    return;
                } else {
                    return;
                }
            case 2:
                if (serializeOptions.getActionMode() != 0) {
                    sliceItemHolder.mParcelable = null;
                    return;
                }
                throw new IllegalArgumentException("Cannot serialize action");
            default:
                return;
        }
    }

    public static IconCompat convert(Context context, IconCompat iconCompat, SerializeOptions serializeOptions) {
        if (iconCompat.getType() == 2) {
            return iconCompat;
        }
        byte[] convertToBytes = SliceXml.convertToBytes(iconCompat, context, serializeOptions);
        return IconCompat.createWithData(convertToBytes, 0, convertToBytes.length);
    }

    public static Slice parseSlice(final Context context, InputStream inputStream, String str, final SliceActionListener sliceActionListener) throws IOException, SliceParseException {
        Slice slice;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        String name = Slice.class.getName();
        bufferedInputStream.mark(name.length() + 4);
        boolean doesStreamStartWith = doesStreamStartWith(name, bufferedInputStream);
        bufferedInputStream.reset();
        if (doesStreamStartWith) {
            final C13212 r3 = new SliceItem.ActionHandler() {
                public void onAction(SliceItem sliceItem, Context context, Intent intent) {
                    SliceActionListener.this.onSliceAction(sliceItem.getSlice().getUri(), context, intent);
                }
            };
            synchronized (SliceItemHolder.sSerializeLock) {
                SliceItemHolder.sHandler = new SliceItemHolder.HolderHandler() {
                    public void handle(SliceItemHolder sliceItemHolder, String str) {
                        SliceUtils.setActionsAndUpdateIcons(sliceItemHolder, SliceItem.ActionHandler.this, context, str);
                    }
                };
                slice = (Slice) ParcelUtils.fromInputStream(bufferedInputStream);
                slice.mHints = (String[]) ArrayUtils.appendElement(String.class, slice.mHints, SliceHints.HINT_CACHED);
                SliceItemHolder.sHandler = null;
            }
            return slice;
        }
        Slice parseSlice = SliceXml.parseSlice(context, bufferedInputStream, str, sliceActionListener);
        parseSlice.mHints = (String[]) ArrayUtils.appendElement(String.class, parseSlice.mHints, SliceHints.HINT_CACHED);
        return parseSlice;
    }

    static void setActionsAndUpdateIcons(SliceItemHolder sliceItemHolder, SliceItem.ActionHandler actionHandler, Context context, String str) {
        str.hashCode();
        if (str.equals("action")) {
            sliceItemHolder.mCallback = actionHandler;
        } else if (str.equals("image") && (sliceItemHolder.mVersionedParcelable instanceof IconCompat)) {
            ((IconCompat) sliceItemHolder.mVersionedParcelable).checkResource(context);
        }
    }

    public static int parseImageMode(SliceItem sliceItem) {
        return SliceActionImpl.parseImageMode(sliceItem);
    }

    private static boolean doesStreamStartWith(String str, BufferedInputStream bufferedInputStream) {
        int length = str.getBytes(Charset.forName("UTF-16")).length;
        byte[] bArr = new byte[length];
        try {
            if (bufferedInputStream.read(bArr, 0, 4) >= 0 && bufferedInputStream.read(bArr, 0, length) >= 0) {
                return str.equals(new String(bArr, "UTF-16"));
            }
            return false;
        } catch (IOException unused) {
            return false;
        }
    }

    public static class SerializeOptions {
        public static final int MODE_CONVERT = 2;
        public static final int MODE_REMOVE = 1;
        public static final int MODE_THROW = 0;
        private int mActionMode = 0;
        private Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.PNG;
        private int mImageMode = 0;
        private int mMaxHeight = 1000;
        private int mMaxWidth = 1000;
        private int mQuality = 100;

        public void checkThrow(String str) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1422950858:
                    if (str.equals("action")) {
                        c = 0;
                        break;
                    }
                    break;
                case 100313435:
                    if (str.equals("image")) {
                        c = 1;
                        break;
                    }
                    break;
                case 100358090:
                    if (str.equals("input")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 2:
                    if (this.mActionMode != 0) {
                        return;
                    }
                    break;
                case 1:
                    if (this.mImageMode != 0) {
                        return;
                    }
                    break;
                default:
                    return;
            }
            throw new IllegalArgumentException(str + " cannot be serialized");
        }

        public int getActionMode() {
            return this.mActionMode;
        }

        public int getImageMode() {
            return this.mImageMode;
        }

        public int getMaxWidth() {
            return this.mMaxWidth;
        }

        public int getMaxHeight() {
            return this.mMaxHeight;
        }

        public Bitmap.CompressFormat getFormat() {
            return this.mFormat;
        }

        public int getQuality() {
            return this.mQuality;
        }

        public SerializeOptions setActionMode(int i) {
            this.mActionMode = i;
            return this;
        }

        public SerializeOptions setImageMode(int i) {
            this.mImageMode = i;
            return this;
        }

        public SerializeOptions setMaxImageWidth(int i) {
            this.mMaxWidth = i;
            return this;
        }

        public SerializeOptions setMaxImageHeight(int i) {
            this.mMaxHeight = i;
            return this;
        }

        public SerializeOptions setImageConversionFormat(Bitmap.CompressFormat compressFormat, int i) {
            this.mFormat = compressFormat;
            this.mQuality = i;
            return this;
        }
    }

    public static class SliceParseException extends Exception {
        public SliceParseException(String str, Throwable th) {
            super(str, th);
        }

        public SliceParseException(String str) {
            super(str);
        }
    }
}
