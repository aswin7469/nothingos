package android.content.pm.parsing.component;

import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import com.android.internal.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ParsedAttributionUtils {
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ad, code lost:
        if (r0 != null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00af, code lost:
        r0 = java.util.Collections.emptyList();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00c3, code lost:
        return r13.success(new android.content.pm.parsing.component.ParsedAttribution(r4, r5, r0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00b4, code lost:
        ((java.util.ArrayList) r0).trimToSize();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ParseResult<ParsedAttribution> parseAttribution(Resources res, XmlResourceParser parser, ParseInput input) throws IOException, XmlPullParserException {
        List<String> inheritFrom = null;
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestAttribution);
        if (sa == null) {
            return input.error("<attribution> could not be parsed");
        }
        try {
            String attributionTag = sa.getNonConfigurationString(1, 0);
            if (attributionTag == null) {
                return input.error("<attribution> does not specify android:tag");
            }
            if (attributionTag.length() > 50) {
                return input.error("android:tag is too long. Max length is 50");
            }
            int label = sa.getResourceId(0, 0);
            if (label == 0) {
                return input.error("<attribution> does not specify android:label");
            }
            sa.recycle();
            int innerDepth = parser.getDepth();
            while (true) {
                int type = parser.next();
                if (type == 1 || (type == 3 && parser.getDepth() <= innerDepth)) {
                    break;
                } else if (type != 3 && type != 4) {
                    String tagName = parser.getName();
                    if (!tagName.equals("inherit-from")) {
                        return input.error("Bad element under <attribution>: " + tagName);
                    }
                    sa = res.obtainAttributes(parser, R.styleable.AndroidManifestAttributionInheritFrom);
                    if (sa == null) {
                        return input.error("<inherit-from> could not be parsed");
                    }
                    try {
                        String inheritFromId = sa.getNonConfigurationString(0, 0);
                        if (inheritFrom == null) {
                            inheritFrom = new ArrayList<>();
                        }
                        inheritFrom.add(inheritFromId);
                    } finally {
                    }
                }
            }
        } finally {
        }
    }
}
