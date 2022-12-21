package org.xml.sax;

public class SAXParseException extends SAXException {
    private int columnNumber;
    private int lineNumber;
    private String publicId;
    private String systemId;

    public SAXParseException(String str, Locator locator) {
        super(str);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init((String) null, (String) null, -1, -1);
        }
    }

    public SAXParseException(String str, Locator locator, Exception exc) {
        super(str, exc);
        if (locator != null) {
            init(locator.getPublicId(), locator.getSystemId(), locator.getLineNumber(), locator.getColumnNumber());
        } else {
            init((String) null, (String) null, -1, -1);
        }
    }

    public SAXParseException(String str, String str2, String str3, int i, int i2) {
        super(str);
        init(str2, str3, i, i2);
    }

    public SAXParseException(String str, String str2, String str3, int i, int i2, Exception exc) {
        super(str, exc);
        init(str2, str3, i, i2);
    }

    private void init(String str, String str2, int i, int i2) {
        this.publicId = str;
        this.systemId = str2;
        this.lineNumber = i;
        this.columnNumber = i2;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }
}
