package org.apache.harmony.xml.dom;

import org.w3c.dom.DOMError;
import org.w3c.dom.DOMLocator;
import org.w3c.dom.Node;

public final class DOMErrorImpl implements DOMError {
    private static final DOMLocator NULL_DOM_LOCATOR = new DOMLocator() {
        public int getByteOffset() {
            return -1;
        }

        public int getColumnNumber() {
            return -1;
        }

        public int getLineNumber() {
            return -1;
        }

        public Node getRelatedNode() {
            return null;
        }

        public String getUri() {
            return null;
        }

        public int getUtf16Offset() {
            return -1;
        }
    };
    private final short severity;
    private final String type;

    public Object getRelatedData() {
        return null;
    }

    public Object getRelatedException() {
        return null;
    }

    public DOMErrorImpl(short s, String str) {
        this.severity = s;
        this.type = str;
    }

    public short getSeverity() {
        return this.severity;
    }

    public String getMessage() {
        return this.type;
    }

    public String getType() {
        return this.type;
    }

    public DOMLocator getLocation() {
        return NULL_DOM_LOCATOR;
    }
}
