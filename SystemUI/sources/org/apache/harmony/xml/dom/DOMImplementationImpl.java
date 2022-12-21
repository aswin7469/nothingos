package org.apache.harmony.xml.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public final class DOMImplementationImpl implements DOMImplementation {
    private static DOMImplementationImpl instance;

    DOMImplementationImpl() {
    }

    public Document createDocument(String str, String str2, DocumentType documentType) throws DOMException {
        return new DocumentImpl(this, str, str2, documentType, (String) null);
    }

    public DocumentType createDocumentType(String str, String str2, String str3) throws DOMException {
        return new DocumentTypeImpl((DocumentImpl) null, str, str2, str3);
    }

    public boolean hasFeature(String str, String str2) {
        boolean z = str2 == null || str2.length() == 0;
        if (str.startsWith("+")) {
            str = str.substring(1);
        }
        if (str.equalsIgnoreCase("Core")) {
            if (z || str2.equals("1.0") || str2.equals("2.0") || str2.equals("3.0")) {
                return true;
            }
            return false;
        } else if (str.equalsIgnoreCase("XML")) {
            if (z || str2.equals("1.0") || str2.equals("2.0") || str2.equals("3.0")) {
                return true;
            }
            return false;
        } else if (!str.equalsIgnoreCase("XMLVersion")) {
            return false;
        } else {
            if (z || str2.equals("1.0") || str2.equals("1.1")) {
                return true;
            }
            return false;
        }
    }

    public static DOMImplementationImpl getInstance() {
        if (instance == null) {
            instance = new DOMImplementationImpl();
        }
        return instance;
    }

    public Object getFeature(String str, String str2) {
        if (hasFeature(str, str2)) {
            return this;
        }
        return null;
    }
}
