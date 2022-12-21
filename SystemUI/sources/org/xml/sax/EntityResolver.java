package org.xml.sax;

import java.p026io.IOException;

public interface EntityResolver {
    InputSource resolveEntity(String str, String str2) throws SAXException, IOException;
}
