package java.util.prefs;

import com.android.settingslib.datetime.ZoneGetter;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.Reader;
import java.p026io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import sun.security.x509.PolicyMappingsExtension;

class XmlSupport {
    private static final String EXTERNAL_XML_VERSION = "1.0";
    private static final String MAP_XML_VERSION = "1.0";
    private static final String PREFS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for preferences --><!ELEMENT preferences (root) ><!ATTLIST preferences EXTERNAL_XML_VERSION CDATA \"0.0\"  ><!ELEMENT root (map, node*) ><!ATTLIST root          type (system|user) #REQUIRED ><!ELEMENT node (map, node*) ><!ATTLIST node          name CDATA #REQUIRED ><!ELEMENT map (entry*) ><!ATTLIST map  MAP_XML_VERSION CDATA \"0.0\"  ><!ELEMENT entry EMPTY ><!ATTLIST entry          key CDATA #REQUIRED          value CDATA #REQUIRED >";
    private static final String PREFS_DTD_URI = "http://java.sun.com/dtd/preferences.dtd";

    XmlSupport() {
    }

    static void export(OutputStream outputStream, Preferences preferences, boolean z) throws IOException, BackingStoreException {
        if (!((AbstractPreferences) preferences).isRemoved()) {
            Document createPrefsDoc = createPrefsDoc("preferences");
            Element documentElement = createPrefsDoc.getDocumentElement();
            documentElement.setAttribute("EXTERNAL_XML_VERSION", "1.0");
            Element element = (Element) documentElement.appendChild(createPrefsDoc.createElement("root"));
            element.setAttribute("type", preferences.isUserNode() ? "user" : "system");
            ArrayList arrayList = new ArrayList();
            Preferences preferences2 = preferences;
            for (Preferences parent = preferences.parent(); parent != null; parent = parent.parent()) {
                arrayList.add(preferences2);
                preferences2 = parent;
            }
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                element.appendChild(createPrefsDoc.createElement(PolicyMappingsExtension.MAP));
                element = (Element) element.appendChild(createPrefsDoc.createElement("node"));
                element.setAttribute(ZoneGetter.KEY_DISPLAYNAME, ((Preferences) arrayList.get(size)).name());
            }
            putPreferencesInXml(element, createPrefsDoc, preferences, z);
            writeDoc(createPrefsDoc, outputStream);
            return;
        }
        throw new IllegalStateException("Node has been removed");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006c, code lost:
        if (r12 == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006f, code lost:
        if (r3 >= r11.length) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0071, code lost:
        r0 = (org.w3c.dom.Element) r9.appendChild(r10.createElement("node"));
        r0.setAttribute(com.android.settingslib.datetime.ZoneGetter.KEY_DISPLAYNAME, r11[r3]);
        putPreferencesInXml(r0, r10, r6[r3], r12);
        r3 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void putPreferencesInXml(org.w3c.dom.Element r9, org.w3c.dom.Document r10, java.util.prefs.Preferences r11, boolean r12) throws java.util.prefs.BackingStoreException {
        /*
            r0 = r11
            java.util.prefs.AbstractPreferences r0 = (java.util.prefs.AbstractPreferences) r0
            java.lang.Object r0 = r0.lock
            monitor-enter(r0)
            r1 = r11
            java.util.prefs.AbstractPreferences r1 = (java.util.prefs.AbstractPreferences) r1     // Catch:{ all -> 0x008d }
            boolean r1 = r1.isRemoved()     // Catch:{ all -> 0x008d }
            if (r1 == 0) goto L_0x0018
            org.w3c.dom.Node r10 = r9.getParentNode()     // Catch:{ all -> 0x008d }
            r10.removeChild(r9)     // Catch:{ all -> 0x008d }
            monitor-exit(r0)     // Catch:{ all -> 0x008d }
            return
        L_0x0018:
            java.lang.String[] r1 = r11.keys()     // Catch:{ all -> 0x008d }
            java.lang.String r2 = "map"
            org.w3c.dom.Element r2 = r10.createElement(r2)     // Catch:{ all -> 0x008d }
            org.w3c.dom.Node r2 = r9.appendChild(r2)     // Catch:{ all -> 0x008d }
            org.w3c.dom.Element r2 = (org.w3c.dom.Element) r2     // Catch:{ all -> 0x008d }
            r3 = 0
            r4 = r3
        L_0x002a:
            int r5 = r1.length     // Catch:{ all -> 0x008d }
            r6 = 0
            if (r4 >= r5) goto L_0x004f
            java.lang.String r5 = "entry"
            org.w3c.dom.Element r5 = r10.createElement(r5)     // Catch:{ all -> 0x008d }
            org.w3c.dom.Node r5 = r2.appendChild(r5)     // Catch:{ all -> 0x008d }
            org.w3c.dom.Element r5 = (org.w3c.dom.Element) r5     // Catch:{ all -> 0x008d }
            java.lang.String r7 = "key"
            r8 = r1[r4]     // Catch:{ all -> 0x008d }
            r5.setAttribute(r7, r8)     // Catch:{ all -> 0x008d }
            java.lang.String r7 = "value"
            r8 = r1[r4]     // Catch:{ all -> 0x008d }
            java.lang.String r6 = r11.get(r8, r6)     // Catch:{ all -> 0x008d }
            r5.setAttribute(r7, r6)     // Catch:{ all -> 0x008d }
            int r4 = r4 + 1
            goto L_0x002a
        L_0x004f:
            if (r12 == 0) goto L_0x006a
            java.lang.String[] r6 = r11.childrenNames()     // Catch:{ all -> 0x008d }
            int r1 = r6.length     // Catch:{ all -> 0x008d }
            java.util.prefs.Preferences[] r1 = new java.util.prefs.Preferences[r1]     // Catch:{ all -> 0x008d }
            r2 = r3
        L_0x0059:
            int r4 = r6.length     // Catch:{ all -> 0x008d }
            if (r2 >= r4) goto L_0x0067
            r4 = r6[r2]     // Catch:{ all -> 0x008d }
            java.util.prefs.Preferences r4 = r11.node(r4)     // Catch:{ all -> 0x008d }
            r1[r2] = r4     // Catch:{ all -> 0x008d }
            int r2 = r2 + 1
            goto L_0x0059
        L_0x0067:
            r11 = r6
            r6 = r1
            goto L_0x006b
        L_0x006a:
            r11 = r6
        L_0x006b:
            monitor-exit(r0)     // Catch:{ all -> 0x008d }
            if (r12 == 0) goto L_0x008c
        L_0x006e:
            int r0 = r11.length
            if (r3 >= r0) goto L_0x008c
            java.lang.String r0 = "node"
            org.w3c.dom.Element r0 = r10.createElement(r0)
            org.w3c.dom.Node r0 = r9.appendChild(r0)
            org.w3c.dom.Element r0 = (org.w3c.dom.Element) r0
            java.lang.String r1 = "name"
            r2 = r11[r3]
            r0.setAttribute(r1, r2)
            r1 = r6[r3]
            putPreferencesInXml(r0, r10, r1, r12)
            int r3 = r3 + 1
            goto L_0x006e
        L_0x008c:
            return
        L_0x008d:
            r9 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x008d }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.XmlSupport.putPreferencesInXml(org.w3c.dom.Element, org.w3c.dom.Document, java.util.prefs.Preferences, boolean):void");
    }

    static void importPreferences(InputStream inputStream) throws IOException, InvalidPreferencesFormatException {
        try {
            Document loadPrefsDoc = loadPrefsDoc(inputStream);
            String attribute = loadPrefsDoc.getDocumentElement().getAttribute("EXTERNAL_XML_VERSION");
            if (attribute.compareTo("1.0") <= 0) {
                NodeList elementsByTagName = loadPrefsDoc.getDocumentElement().getElementsByTagName("root");
                if (elementsByTagName == null || elementsByTagName.getLength() != 1) {
                    throw new InvalidPreferencesFormatException("invalid root node");
                }
                Element element = (Element) elementsByTagName.item(0);
                ImportSubtree(element.getAttribute("type").equals("user") ? Preferences.userRoot() : Preferences.systemRoot(), element);
                return;
            }
            throw new InvalidPreferencesFormatException("Exported preferences file format version " + attribute + " is not supported. This java installation can read versions 1.0 or older. You may need to install a newer version of JDK.");
        } catch (SAXException e) {
            throw new InvalidPreferencesFormatException((Throwable) e);
        }
    }

    private static Document createPrefsDoc(String str) {
        try {
            DOMImplementation dOMImplementation = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
            return dOMImplementation.createDocument((String) null, str, dOMImplementation.createDocumentType(str, (String) null, PREFS_DTD_URI));
        } catch (ParserConfigurationException e) {
            throw new AssertionError((Object) e);
        }
    }

    private static Document loadPrefsDoc(InputStream inputStream) throws SAXException, IOException {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        newInstance.setIgnoringElementContentWhitespace(true);
        newInstance.setCoalescing(true);
        newInstance.setIgnoringComments(true);
        try {
            DocumentBuilder newDocumentBuilder = newInstance.newDocumentBuilder();
            newDocumentBuilder.setEntityResolver(new Resolver());
            newDocumentBuilder.setErrorHandler(new C4450EH());
            return newDocumentBuilder.parse(new InputSource(inputStream));
        } catch (ParserConfigurationException e) {
            throw new AssertionError((Object) e);
        }
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x000f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final void writeDoc(org.w3c.dom.Document r5, java.p026io.OutputStream r6) throws java.p026io.IOException {
        /*
            javax.xml.transform.TransformerFactory r0 = javax.xml.transform.TransformerFactory.newInstance()     // Catch:{ TransformerException -> 0x0041 }
            java.lang.String r1 = "indent-number"
            java.lang.Integer r2 = new java.lang.Integer     // Catch:{ IllegalArgumentException -> 0x000f }
            r3 = 2
            r2.<init>((int) r3)     // Catch:{ IllegalArgumentException -> 0x000f }
            r0.setAttribute(r1, r2)     // Catch:{ IllegalArgumentException -> 0x000f }
        L_0x000f:
            javax.xml.transform.Transformer r0 = r0.newTransformer()     // Catch:{ TransformerException -> 0x0041 }
            java.lang.String r1 = "doctype-system"
            org.w3c.dom.DocumentType r2 = r5.getDoctype()     // Catch:{ TransformerException -> 0x0041 }
            java.lang.String r2 = r2.getSystemId()     // Catch:{ TransformerException -> 0x0041 }
            r0.setOutputProperty(r1, r2)     // Catch:{ TransformerException -> 0x0041 }
            java.lang.String r1 = "indent"
            java.lang.String r2 = "yes"
            r0.setOutputProperty(r1, r2)     // Catch:{ TransformerException -> 0x0041 }
            javax.xml.transform.dom.DOMSource r1 = new javax.xml.transform.dom.DOMSource     // Catch:{ TransformerException -> 0x0041 }
            r1.<init>(r5)     // Catch:{ TransformerException -> 0x0041 }
            javax.xml.transform.stream.StreamResult r5 = new javax.xml.transform.stream.StreamResult     // Catch:{ TransformerException -> 0x0041 }
            java.io.BufferedWriter r2 = new java.io.BufferedWriter     // Catch:{ TransformerException -> 0x0041 }
            java.io.OutputStreamWriter r3 = new java.io.OutputStreamWriter     // Catch:{ TransformerException -> 0x0041 }
            java.lang.String r4 = "UTF-8"
            r3.<init>((java.p026io.OutputStream) r6, (java.lang.String) r4)     // Catch:{ TransformerException -> 0x0041 }
            r2.<init>(r3)     // Catch:{ TransformerException -> 0x0041 }
            r5.<init>((java.p026io.Writer) r2)     // Catch:{ TransformerException -> 0x0041 }
            r0.transform(r1, r5)     // Catch:{ TransformerException -> 0x0041 }
            return
        L_0x0041:
            r5 = move-exception
            java.lang.AssertionError r6 = new java.lang.AssertionError
            r6.<init>((java.lang.Object) r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.XmlSupport.writeDoc(org.w3c.dom.Document, java.io.OutputStream):void");
    }

    static class NodeListAdapter implements NodeList {
        private final List<? extends Node> delegate;

        public NodeListAdapter(List<? extends Node> list) {
            this.delegate = (List) Objects.requireNonNull(list);
        }

        public Node item(int i) {
            if (i < 0 || i >= this.delegate.size()) {
                return null;
            }
            return (Node) this.delegate.get(i);
        }

        public int getLength() {
            return this.delegate.size();
        }
    }

    private static NodeList elementNodesOf(NodeList nodeList) {
        ArrayList arrayList = new ArrayList(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if (item instanceof Element) {
                arrayList.add((Element) item);
            }
        }
        return new NodeListAdapter(arrayList);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0047, code lost:
        if (r3 >= r0) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0049, code lost:
        ImportSubtree(r2[r3 - 1], (org.w3c.dom.Element) r9.item(r3));
        r3 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0059, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void ImportSubtree(java.util.prefs.Preferences r8, org.w3c.dom.Element r9) {
        /*
            org.w3c.dom.NodeList r9 = r9.getChildNodes()
            org.w3c.dom.NodeList r9 = elementNodesOf(r9)
            int r0 = r9.getLength()
            r1 = r8
            java.util.prefs.AbstractPreferences r1 = (java.util.prefs.AbstractPreferences) r1
            java.lang.Object r1 = r1.lock
            monitor-enter(r1)
            r2 = r8
            java.util.prefs.AbstractPreferences r2 = (java.util.prefs.AbstractPreferences) r2     // Catch:{ all -> 0x005a }
            boolean r2 = r2.isRemoved()     // Catch:{ all -> 0x005a }
            if (r2 == 0) goto L_0x001d
            monitor-exit(r1)     // Catch:{ all -> 0x005a }
            return
        L_0x001d:
            r2 = 0
            org.w3c.dom.Node r2 = r9.item(r2)     // Catch:{ all -> 0x005a }
            org.w3c.dom.Element r2 = (org.w3c.dom.Element) r2     // Catch:{ all -> 0x005a }
            ImportPrefs(r8, r2)     // Catch:{ all -> 0x005a }
            int r2 = r0 + -1
            java.util.prefs.Preferences[] r2 = new java.util.prefs.Preferences[r2]     // Catch:{ all -> 0x005a }
            r3 = 1
            r4 = r3
        L_0x002d:
            if (r4 >= r0) goto L_0x0046
            org.w3c.dom.Node r5 = r9.item(r4)     // Catch:{ all -> 0x005a }
            org.w3c.dom.Element r5 = (org.w3c.dom.Element) r5     // Catch:{ all -> 0x005a }
            int r6 = r4 + -1
            java.lang.String r7 = "name"
            java.lang.String r5 = r5.getAttribute(r7)     // Catch:{ all -> 0x005a }
            java.util.prefs.Preferences r5 = r8.node(r5)     // Catch:{ all -> 0x005a }
            r2[r6] = r5     // Catch:{ all -> 0x005a }
            int r4 = r4 + 1
            goto L_0x002d
        L_0x0046:
            monitor-exit(r1)     // Catch:{ all -> 0x005a }
        L_0x0047:
            if (r3 >= r0) goto L_0x0059
            int r8 = r3 + -1
            r8 = r2[r8]
            org.w3c.dom.Node r1 = r9.item(r3)
            org.w3c.dom.Element r1 = (org.w3c.dom.Element) r1
            ImportSubtree(r8, r1)
            int r3 = r3 + 1
            goto L_0x0047
        L_0x0059:
            return
        L_0x005a:
            r8 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x005a }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.XmlSupport.ImportSubtree(java.util.prefs.Preferences, org.w3c.dom.Element):void");
    }

    private static void ImportPrefs(Preferences preferences, Element element) {
        NodeList elementNodesOf = elementNodesOf(element.getChildNodes());
        int length = elementNodesOf.getLength();
        for (int i = 0; i < length; i++) {
            Element element2 = (Element) elementNodesOf.item(i);
            preferences.put(element2.getAttribute("key"), element2.getAttribute("value"));
        }
    }

    static void exportMap(OutputStream outputStream, Map<String, String> map) throws IOException {
        Document createPrefsDoc = createPrefsDoc(PolicyMappingsExtension.MAP);
        Element documentElement = createPrefsDoc.getDocumentElement();
        documentElement.setAttribute("MAP_XML_VERSION", "1.0");
        for (Map.Entry next : map.entrySet()) {
            Element element = (Element) documentElement.appendChild(createPrefsDoc.createElement("entry"));
            element.setAttribute("key", (String) next.getKey());
            element.setAttribute("value", (String) next.getValue());
        }
        writeDoc(createPrefsDoc, outputStream);
    }

    static void importMap(InputStream inputStream, Map<String, String> map) throws IOException, InvalidPreferencesFormatException {
        try {
            Element documentElement = loadPrefsDoc(inputStream).getDocumentElement();
            String attribute = documentElement.getAttribute("MAP_XML_VERSION");
            if (attribute.compareTo("1.0") <= 0) {
                NodeList childNodes = documentElement.getChildNodes();
                int length = childNodes.getLength();
                for (int i = 0; i < length; i++) {
                    if (childNodes.item(i) instanceof Element) {
                        Element element = (Element) childNodes.item(i);
                        map.put(element.getAttribute("key"), element.getAttribute("value"));
                    }
                }
                return;
            }
            throw new InvalidPreferencesFormatException("Preferences map file format version " + attribute + " is not supported. This java installation can read versions 1.0 or older. You may need to install a newer version of JDK.");
        } catch (SAXException e) {
            throw new InvalidPreferencesFormatException((Throwable) e);
        }
    }

    private static class Resolver implements EntityResolver {
        private Resolver() {
        }

        public InputSource resolveEntity(String str, String str2) throws SAXException {
            if (str2.equals(XmlSupport.PREFS_DTD_URI)) {
                InputSource inputSource = new InputSource((Reader) new StringReader(XmlSupport.PREFS_DTD));
                inputSource.setSystemId(XmlSupport.PREFS_DTD_URI);
                return inputSource;
            }
            throw new SAXException("Invalid system identifier: " + str2);
        }
    }

    /* renamed from: java.util.prefs.XmlSupport$EH */
    private static class C4450EH implements ErrorHandler {
        private C4450EH() {
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
