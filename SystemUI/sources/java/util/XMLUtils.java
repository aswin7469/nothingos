package java.util;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.Reader;
import java.p026io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class XMLUtils {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String EXTERNAL_XML_VERSION = "1.0";
    private static final String PROPS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for properties --><!ELEMENT properties ( comment?, entry* ) ><!ATTLIST properties version CDATA #FIXED \"1.0\"><!ELEMENT comment (#PCDATA) ><!ELEMENT entry (#PCDATA) ><!ATTLIST entry  key CDATA #REQUIRED>";
    private static final String PROPS_DTD_URI = "http://java.sun.com/dtd/properties.dtd";

    XMLUtils() {
    }

    static void load(Properties properties, InputStream inputStream) throws IOException, InvalidPropertiesFormatException {
        try {
            Element documentElement = getLoadingDoc(inputStream).getDocumentElement();
            String attribute = documentElement.getAttribute("version");
            if (attribute.compareTo(EXTERNAL_XML_VERSION) <= 0) {
                importProperties(properties, documentElement);
                return;
            }
            throw new InvalidPropertiesFormatException("Exported Properties file format version " + attribute + " is not supported. This java installation can read versions 1.0 or older. You may need to install a newer version of JDK.");
        } catch (SAXException e) {
            throw new InvalidPropertiesFormatException((Throwable) e);
        }
    }

    static Document getLoadingDoc(InputStream inputStream) throws SAXException, IOException {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        newInstance.setIgnoringElementContentWhitespace(true);
        newInstance.setCoalescing(true);
        newInstance.setIgnoringComments(true);
        try {
            DocumentBuilder newDocumentBuilder = newInstance.newDocumentBuilder();
            newDocumentBuilder.setEntityResolver(new Resolver());
            newDocumentBuilder.setErrorHandler(new C4400EH());
            return newDocumentBuilder.parse(new InputSource(inputStream));
        } catch (ParserConfigurationException e) {
            throw new Error((Throwable) e);
        }
    }

    static void importProperties(Properties properties, Element element) {
        String str;
        NodeList childNodes = element.getChildNodes();
        int length = childNodes.getLength();
        int i = 0;
        if (length > 0 && childNodes.item(0).getNodeName().equals("comment")) {
            i = 1;
        }
        while (i < length) {
            if (childNodes.item(i) instanceof Element) {
                Element element2 = (Element) childNodes.item(i);
                if (element2.hasAttribute("key")) {
                    Node firstChild = element2.getFirstChild();
                    if (firstChild == null) {
                        str = "";
                    } else {
                        str = firstChild.getNodeValue();
                    }
                    properties.setProperty(element2.getAttribute("key"), str);
                }
            }
            i++;
        }
    }

    static void save(Properties properties, OutputStream outputStream, String str, String str2) throws IOException {
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException unused) {
            documentBuilder = null;
        }
        Document newDocument = documentBuilder.newDocument();
        Element element = (Element) newDocument.appendChild(newDocument.createElement("properties"));
        if (str != null) {
            ((Element) element.appendChild(newDocument.createElement("comment"))).appendChild(newDocument.createTextNode(str));
        }
        synchronized (properties) {
            for (String next : properties.stringPropertyNames()) {
                Element element2 = (Element) element.appendChild(newDocument.createElement("entry"));
                element2.setAttribute("key", next);
                element2.appendChild(newDocument.createTextNode(properties.getProperty(next)));
            }
        }
        emitDocument(newDocument, outputStream, str2);
    }

    static void emitDocument(Document document, OutputStream outputStream, String str) throws IOException {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            try {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, PROPS_DTD_URI);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, XMLConstants.XML_NS_PREFIX);
                transformer.setOutputProperty(OutputKeys.ENCODING, str);
            } catch (TransformerConfigurationException unused) {
            }
        } catch (TransformerConfigurationException unused2) {
            transformer = null;
        }
        try {
            transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        } catch (TransformerException e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        }
    }

    private static class Resolver implements EntityResolver {
        private Resolver() {
        }

        public InputSource resolveEntity(String str, String str2) throws SAXException {
            if (str2.equals(XMLUtils.PROPS_DTD_URI)) {
                InputSource inputSource = new InputSource((Reader) new StringReader(XMLUtils.PROPS_DTD));
                inputSource.setSystemId(XMLUtils.PROPS_DTD_URI);
                return inputSource;
            }
            throw new SAXException("Invalid system identifier: " + str2);
        }
    }

    /* renamed from: java.util.XMLUtils$EH */
    private static class C4400EH implements ErrorHandler {
        private C4400EH() {
        }

        public void error(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }

        public void fatalError(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }

        public void warning(SAXParseException sAXParseException) throws SAXException {
            throw sAXParseException;
        }
    }
}
