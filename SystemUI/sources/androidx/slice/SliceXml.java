package androidx.slice;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceUtils;
import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.charset.StandardCharsets;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.List;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlPullParserException;
import org.xmlpull.p032v1.XmlPullParserFactory;
import org.xmlpull.p032v1.XmlSerializer;

class SliceXml {
    private static final String ATTR_FORMAT = "format";
    private static final String ATTR_HINTS = "hints";
    private static final String ATTR_ICON_PACKAGE = "pkg";
    private static final String ATTR_ICON_RES_TYPE = "resType";
    private static final String ATTR_ICON_TYPE = "iconType";
    private static final String ATTR_SUBTYPE = "subtype";
    private static final String ATTR_URI = "uri";
    private static final String ICON_TYPE_DEFAULT = "def";
    private static final String ICON_TYPE_RES = "res";
    private static final String ICON_TYPE_URI = "uri";
    private static final String NAMESPACE = null;
    private static final String TAG_ACTION = "action";
    private static final String TAG_ITEM = "item";
    private static final String TAG_SLICE = "slice";

    public static Slice parseSlice(Context context, InputStream inputStream, String str, SliceUtils.SliceActionListener sliceActionListener) throws IOException, SliceUtils.SliceParseException {
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(inputStream, str);
            int depth = newPullParser.getDepth();
            Slice slice = null;
            while (true) {
                int next = newPullParser.next();
                if (next == 1 || (next == 3 && newPullParser.getDepth() <= depth)) {
                    return slice;
                }
                if (next == 2) {
                    slice = parseSlice(context, newPullParser, sliceActionListener);
                }
            }
            return slice;
        } catch (XmlPullParserException e) {
            throw new IOException("Unable to init XML Serialization", e);
        }
    }

    private static Slice parseSlice(Context context, XmlPullParser xmlPullParser, SliceUtils.SliceActionListener sliceActionListener) throws IOException, XmlPullParserException, SliceUtils.SliceParseException {
        if ("slice".equals(xmlPullParser.getName()) || TAG_ACTION.equals(xmlPullParser.getName())) {
            int depth = xmlPullParser.getDepth();
            String str = NAMESPACE;
            Slice.Builder builder = new Slice.Builder(Uri.parse(xmlPullParser.getAttributeValue(str, SliceBroadcastRelay.EXTRA_URI)));
            builder.addHints(hints(xmlPullParser.getAttributeValue(str, ATTR_HINTS)));
            while (true) {
                int next = xmlPullParser.next();
                if (next != 1 && (next != 3 || xmlPullParser.getDepth() > depth)) {
                    if (next == 2 && TAG_ITEM.equals(xmlPullParser.getName())) {
                        parseItem(context, builder, xmlPullParser, sliceActionListener);
                    }
                }
            }
            return builder.build();
        }
        throw new IOException("Unexpected tag " + xmlPullParser.getName());
    }

    private static void parseItem(Context context, Slice.Builder builder, XmlPullParser xmlPullParser, SliceUtils.SliceActionListener sliceActionListener) throws IOException, XmlPullParserException, SliceUtils.SliceParseException {
        Context context2 = context;
        Slice.Builder builder2 = builder;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        final SliceUtils.SliceActionListener sliceActionListener2 = sliceActionListener;
        int depth = xmlPullParser.getDepth();
        String str = NAMESPACE;
        String attributeValue = xmlPullParser2.getAttributeValue(str, ATTR_FORMAT);
        String attributeValue2 = xmlPullParser2.getAttributeValue(str, ATTR_SUBTYPE);
        String attributeValue3 = xmlPullParser2.getAttributeValue(str, ATTR_HINTS);
        String attributeValue4 = xmlPullParser2.getAttributeValue(str, ATTR_ICON_TYPE);
        String attributeValue5 = xmlPullParser2.getAttributeValue(str, "pkg");
        String attributeValue6 = xmlPullParser2.getAttributeValue(str, ATTR_ICON_RES_TYPE);
        String[] hints = hints(attributeValue3);
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && xmlPullParser.getDepth() <= depth) {
                return;
            }
            if (next == 4) {
                attributeValue.hashCode();
                char c = 65535;
                switch (attributeValue.hashCode()) {
                    case -1377881982:
                        if (attributeValue.equals("bundle")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 104431:
                        if (attributeValue.equals("int")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 3327612:
                        if (attributeValue.equals("long")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 3556653:
                        if (attributeValue.equals("text")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 100313435:
                        if (attributeValue.equals("image")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 100358090:
                        if (attributeValue.equals("input")) {
                            c = 5;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 5:
                        break;
                    case 1:
                        builder2.addInt(Integer.parseInt(xmlPullParser.getText()), attributeValue2, hints);
                        break;
                    case 2:
                        builder2.addLong(Long.parseLong(xmlPullParser.getText()), attributeValue2, hints);
                        break;
                    case 3:
                        builder2.addText((CharSequence) Html.fromHtml(xmlPullParser.getText()), attributeValue2, hints);
                        break;
                    case 4:
                        attributeValue4.hashCode();
                        if (!attributeValue4.equals(ICON_TYPE_RES)) {
                            if (attributeValue4.equals(SliceBroadcastRelay.EXTRA_URI)) {
                                builder2.addIcon(IconCompat.createWithContentUri(xmlPullParser.getText()), attributeValue2, hints);
                                break;
                            } else {
                                byte[] decode = Base64.decode(xmlPullParser.getText(), 2);
                                builder2.addIcon(IconCompat.createWithBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length)), attributeValue2, hints);
                                break;
                            }
                        } else {
                            String text = xmlPullParser.getText();
                            try {
                                int identifier = context.getPackageManager().getResourcesForApplication(attributeValue5).getIdentifier(text, attributeValue6, attributeValue5);
                                if (identifier != 0) {
                                    builder2.addIcon(IconCompat.createWithResource(context2.createPackageContext(attributeValue5, 0), identifier), attributeValue2, hints);
                                    break;
                                } else {
                                    throw new SliceUtils.SliceParseException("Cannot find resource " + attributeValue5 + ":" + attributeValue6 + "/" + text);
                                }
                            } catch (PackageManager.NameNotFoundException e) {
                                throw new SliceUtils.SliceParseException("Invalid icon package " + attributeValue5, e);
                            }
                        }
                    default:
                        throw new IllegalArgumentException("Unrecognized format " + attributeValue);
                }
            } else if (next == 2 && "slice".equals(xmlPullParser.getName())) {
                builder2.addSubSlice(parseSlice(context2, xmlPullParser2, sliceActionListener2), attributeValue2);
            } else if (next == 2 && TAG_ACTION.equals(xmlPullParser.getName())) {
                builder2.addAction((SliceItem.ActionHandler) new SliceItem.ActionHandler() {
                    public void onAction(SliceItem sliceItem, Context context, Intent intent) {
                        SliceUtils.SliceActionListener.this.onSliceAction(sliceItem.getSlice().getUri(), context, intent);
                    }
                }, parseSlice(context2, xmlPullParser2, sliceActionListener2), attributeValue2);
            }
        }
    }

    private static String[] hints(String str) {
        return TextUtils.isEmpty(str) ? new String[0] : str.split(NavigationBarInflaterView.BUTTON_SEPARATOR);
    }

    public static void serializeSlice(Slice slice, Context context, OutputStream outputStream, String str, SliceUtils.SerializeOptions serializeOptions) throws IOException {
        try {
            XmlSerializer newSerializer = XmlPullParserFactory.newInstance().newSerializer();
            newSerializer.setOutput(outputStream, str);
            newSerializer.startDocument(str, (Boolean) null);
            serialize(slice, context, serializeOptions, newSerializer, false, (String) null);
            newSerializer.endDocument();
            newSerializer.flush();
        } catch (XmlPullParserException e) {
            throw new IOException("Unable to init XML Serialization", e);
        }
    }

    private static void serialize(Slice slice, Context context, SliceUtils.SerializeOptions serializeOptions, XmlSerializer xmlSerializer, boolean z, String str) throws IOException {
        String str2 = NAMESPACE;
        String str3 = TAG_ACTION;
        xmlSerializer.startTag(str2, z ? str3 : "slice");
        xmlSerializer.attribute(str2, SliceBroadcastRelay.EXTRA_URI, slice.getUri().toString());
        if (str != null) {
            xmlSerializer.attribute(str2, ATTR_SUBTYPE, str);
        }
        if (!slice.getHints().isEmpty()) {
            xmlSerializer.attribute(str2, ATTR_HINTS, hintStr(slice.getHints()));
        }
        for (SliceItem serialize : slice.getItems()) {
            serialize(serialize, context, serializeOptions, xmlSerializer);
        }
        String str4 = NAMESPACE;
        if (!z) {
            str3 = "slice";
        }
        xmlSerializer.endTag(str4, str3);
    }

    private static void serialize(SliceItem sliceItem, Context context, SliceUtils.SerializeOptions serializeOptions, XmlSerializer xmlSerializer) throws IOException {
        String format = sliceItem.getFormat();
        serializeOptions.checkThrow(format);
        String str = NAMESPACE;
        xmlSerializer.startTag(str, TAG_ITEM);
        xmlSerializer.attribute(str, ATTR_FORMAT, format);
        if (sliceItem.getSubType() != null) {
            xmlSerializer.attribute(str, ATTR_SUBTYPE, sliceItem.getSubType());
        }
        if (!sliceItem.getHints().isEmpty()) {
            xmlSerializer.attribute(str, ATTR_HINTS, hintStr(sliceItem.getHints()));
        }
        format.hashCode();
        char c = 65535;
        switch (format.hashCode()) {
            case -1422950858:
                if (format.equals(TAG_ACTION)) {
                    c = 0;
                    break;
                }
                break;
            case -1377881982:
                if (format.equals("bundle")) {
                    c = 1;
                    break;
                }
                break;
            case 104431:
                if (format.equals("int")) {
                    c = 2;
                    break;
                }
                break;
            case 3327612:
                if (format.equals("long")) {
                    c = 3;
                    break;
                }
                break;
            case 3556653:
                if (format.equals("text")) {
                    c = 4;
                    break;
                }
                break;
            case 100313435:
                if (format.equals("image")) {
                    c = 5;
                    break;
                }
                break;
            case 100358090:
                if (format.equals("input")) {
                    c = 6;
                    break;
                }
                break;
            case 109526418:
                if (format.equals("slice")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (serializeOptions.getActionMode() == 2) {
                    serialize(sliceItem.getSlice(), context, serializeOptions, xmlSerializer, true, sliceItem.getSubType());
                    break;
                } else if (serializeOptions.getActionMode() == 0) {
                    throw new IllegalArgumentException("Slice contains an action " + sliceItem);
                }
                break;
            case 1:
            case 6:
                break;
            case 2:
                xmlSerializer.text(String.valueOf(sliceItem.getInt()));
                break;
            case 3:
                xmlSerializer.text(String.valueOf(sliceItem.getLong()));
                break;
            case 4:
                if (!(sliceItem.getText() instanceof Spanned)) {
                    xmlSerializer.text(String.valueOf((Object) sliceItem.getText()));
                    break;
                } else {
                    xmlSerializer.text(Html.toHtml((Spanned) sliceItem.getText()));
                    break;
                }
            case 5:
                if (serializeOptions.getImageMode() == 2) {
                    IconCompat icon = sliceItem.getIcon();
                    int type = icon.getType();
                    if (type != 2) {
                        if (type == 4) {
                            if (!"file".equals(icon.getUri().getScheme())) {
                                serializeIcon(xmlSerializer, icon, context, serializeOptions);
                                break;
                            } else {
                                serializeFileIcon(xmlSerializer, icon);
                                break;
                            }
                        } else {
                            serializeIcon(xmlSerializer, icon, context, serializeOptions);
                            break;
                        }
                    } else {
                        serializeResIcon(xmlSerializer, icon, context);
                        break;
                    }
                } else if (serializeOptions.getImageMode() == 0) {
                    throw new IllegalArgumentException("Slice contains an image " + sliceItem);
                }
                break;
            case 7:
                serialize(sliceItem.getSlice(), context, serializeOptions, xmlSerializer, false, sliceItem.getSubType());
                break;
            default:
                throw new IllegalArgumentException("Unrecognized format " + format);
        }
        xmlSerializer.endTag(str, TAG_ITEM);
    }

    private static void serializeResIcon(XmlSerializer xmlSerializer, IconCompat iconCompat, Context context) throws IOException {
        try {
            Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(iconCompat.getResPackage());
            int resId = iconCompat.getResId();
            String str = NAMESPACE;
            xmlSerializer.attribute(str, ATTR_ICON_TYPE, ICON_TYPE_RES);
            xmlSerializer.attribute(str, "pkg", resourcesForApplication.getResourcePackageName(resId));
            xmlSerializer.attribute(str, ATTR_ICON_RES_TYPE, resourcesForApplication.getResourceTypeName(resId));
            xmlSerializer.text(resourcesForApplication.getResourceEntryName(resId));
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException("Slice contains invalid icon", e);
        }
    }

    private static void serializeFileIcon(XmlSerializer xmlSerializer, IconCompat iconCompat) throws IOException {
        xmlSerializer.attribute(NAMESPACE, ATTR_ICON_TYPE, SliceBroadcastRelay.EXTRA_URI);
        xmlSerializer.text(iconCompat.getUri().toString());
    }

    private static void serializeIcon(XmlSerializer xmlSerializer, IconCompat iconCompat, Context context, SliceUtils.SerializeOptions serializeOptions) throws IOException {
        byte[] convertToBytes = convertToBytes(iconCompat, context, serializeOptions);
        xmlSerializer.attribute(NAMESPACE, ATTR_ICON_TYPE, ICON_TYPE_DEFAULT);
        xmlSerializer.text(new String(Base64.encode(convertToBytes, 2), StandardCharsets.UTF_8));
    }

    public static byte[] convertToBytes(IconCompat iconCompat, Context context, SliceUtils.SerializeOptions serializeOptions) {
        Drawable loadDrawable = iconCompat.loadDrawable(context);
        int intrinsicWidth = loadDrawable.getIntrinsicWidth();
        int intrinsicHeight = loadDrawable.getIntrinsicHeight();
        if (intrinsicWidth > serializeOptions.getMaxWidth()) {
            intrinsicHeight = (int) (((double) (serializeOptions.getMaxWidth() * intrinsicHeight)) / ((double) intrinsicWidth));
            intrinsicWidth = serializeOptions.getMaxWidth();
        }
        if (intrinsicHeight > serializeOptions.getMaxHeight()) {
            intrinsicWidth = (int) (((double) (serializeOptions.getMaxHeight() * intrinsicWidth)) / ((double) intrinsicHeight));
            intrinsicHeight = serializeOptions.getMaxHeight();
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        loadDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        loadDrawable.draw(canvas);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        createBitmap.compress(serializeOptions.getFormat(), serializeOptions.getQuality(), byteArrayOutputStream);
        createBitmap.recycle();
        return byteArrayOutputStream.toByteArray();
    }

    private static String hintStr(List<String> list) {
        return TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, list);
    }

    private SliceXml() {
    }
}
