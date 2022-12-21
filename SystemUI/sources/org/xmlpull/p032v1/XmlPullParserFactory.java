package org.xmlpull.p032v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* renamed from: org.xmlpull.v1.XmlPullParserFactory */
public class XmlPullParserFactory {
    public static final String PROPERTY_NAME = "org.xmlpull.v1.XmlPullParserFactory";
    protected String classNamesLocation = null;
    protected HashMap<String, Boolean> features = new HashMap<>();
    protected ArrayList parserClasses = new ArrayList();
    protected ArrayList serializerClasses = new ArrayList();

    protected XmlPullParserFactory() {
        try {
            this.parserClasses.add(Class.forName("com.android.org.kxml2.io.KXmlParser"));
            this.serializerClasses.add(Class.forName("com.android.org.kxml2.io.KXmlSerializer"));
        } catch (ClassNotFoundException unused) {
            throw new AssertionError();
        }
    }

    public void setFeature(String str, boolean z) throws XmlPullParserException {
        this.features.put(str, Boolean.valueOf(z));
    }

    public boolean getFeature(String str) {
        Boolean bool = this.features.get(str);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public void setNamespaceAware(boolean z) {
        this.features.put(XmlPullParser.FEATURE_PROCESS_NAMESPACES, Boolean.valueOf(z));
    }

    public boolean isNamespaceAware() {
        return getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES);
    }

    public void setValidating(boolean z) {
        this.features.put(XmlPullParser.FEATURE_VALIDATION, Boolean.valueOf(z));
    }

    public boolean isValidating() {
        return getFeature(XmlPullParser.FEATURE_VALIDATION);
    }

    public XmlPullParser newPullParser() throws XmlPullParserException {
        XmlPullParser parserInstance = getParserInstance();
        for (Map.Entry next : this.features.entrySet()) {
            if (((Boolean) next.getValue()).booleanValue()) {
                parserInstance.setFeature((String) next.getKey(), ((Boolean) next.getValue()).booleanValue());
            }
        }
        return parserInstance;
    }

    private XmlPullParser getParserInstance() throws XmlPullParserException {
        ArrayList arrayList;
        ArrayList arrayList2 = this.parserClasses;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            arrayList = null;
        } else {
            arrayList = new ArrayList();
            Iterator it = this.parserClasses.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next != null) {
                    try {
                        return (XmlPullParser) ((Class) next).newInstance();
                    } catch (InstantiationException e) {
                        arrayList.add(e);
                    } catch (IllegalAccessException e2) {
                        arrayList.add(e2);
                    } catch (ClassCastException e3) {
                        arrayList.add(e3);
                    }
                }
            }
        }
        throw newInstantiationException("Invalid parser class list", arrayList);
    }

    private XmlSerializer getSerializerInstance() throws XmlPullParserException {
        ArrayList arrayList;
        ArrayList arrayList2 = this.serializerClasses;
        if (arrayList2 == null || arrayList2.isEmpty()) {
            arrayList = null;
        } else {
            arrayList = new ArrayList();
            Iterator it = this.serializerClasses.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (next != null) {
                    try {
                        return (XmlSerializer) ((Class) next).newInstance();
                    } catch (InstantiationException e) {
                        arrayList.add(e);
                    } catch (IllegalAccessException e2) {
                        arrayList.add(e2);
                    } catch (ClassCastException e3) {
                        arrayList.add(e3);
                    }
                }
            }
        }
        throw newInstantiationException("Invalid serializer class list", arrayList);
    }

    private static XmlPullParserException newInstantiationException(String str, ArrayList<Exception> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return new XmlPullParserException(str);
        }
        XmlPullParserException xmlPullParserException = new XmlPullParserException(str);
        Iterator<Exception> it = arrayList.iterator();
        while (it.hasNext()) {
            xmlPullParserException.addSuppressed(it.next());
        }
        return xmlPullParserException;
    }

    public XmlSerializer newSerializer() throws XmlPullParserException {
        return getSerializerInstance();
    }

    public static XmlPullParserFactory newInstance() throws XmlPullParserException {
        return new XmlPullParserFactory();
    }

    public static XmlPullParserFactory newInstance(String str, Class cls) throws XmlPullParserException {
        return newInstance();
    }
}
