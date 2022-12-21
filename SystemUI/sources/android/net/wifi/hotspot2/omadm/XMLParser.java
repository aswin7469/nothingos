package android.net.wifi.hotspot2.omadm;

import android.text.TextUtils;
import java.p026io.IOException;
import java.p026io.Reader;
import java.p026io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {
    private XMLNode mCurrent = null;
    private XMLNode mRoot = null;

    public XMLNode parse(String str) throws IOException, SAXException {
        if (!TextUtils.isEmpty(str)) {
            this.mRoot = null;
            this.mCurrent = null;
            try {
                SAXParserFactory.newInstance().newSAXParser().parse(new InputSource((Reader) new StringReader(str)), (DefaultHandler) this);
                return this.mRoot;
            } catch (ParserConfigurationException e) {
                throw new SAXException((Exception) e);
            }
        } else {
            throw new IOException("XML string not provided");
        }
    }

    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        XMLNode xMLNode = this.mCurrent;
        XMLNode xMLNode2 = new XMLNode(xMLNode, str3);
        this.mCurrent = xMLNode2;
        if (this.mRoot == null) {
            this.mRoot = xMLNode2;
        } else if (xMLNode != null) {
            xMLNode.addChild(xMLNode2);
        } else {
            throw new SAXException("More than one root nodes");
        }
    }

    public void endElement(String str, String str2, String str3) throws SAXException {
        if (str3.equals(this.mCurrent.getTag())) {
            this.mCurrent.close();
            this.mCurrent = this.mCurrent.getParent();
            return;
        }
        throw new SAXException("End tag '" + str3 + "' doesn't match current node: " + this.mCurrent);
    }

    public void characters(char[] cArr, int i, int i2) throws SAXException {
        this.mCurrent.addText(new String(cArr, i, i2));
    }
}
