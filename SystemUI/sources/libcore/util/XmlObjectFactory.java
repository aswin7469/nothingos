package libcore.util;

import android.annotation.SystemApi;
import com.android.org.kxml2.p007io.KXmlParser;
import com.android.org.kxml2.p007io.KXmlSerializer;
import org.apache.harmony.xml.ExpatReader;
import org.xml.sax.XMLReader;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlSerializer;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class XmlObjectFactory {
    private XmlObjectFactory() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static XmlSerializer newXmlSerializer() {
        return new KXmlSerializer();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static XmlPullParser newXmlPullParser() {
        return new KXmlParser();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static XMLReader newXMLReader() {
        return new ExpatReader();
    }
}
