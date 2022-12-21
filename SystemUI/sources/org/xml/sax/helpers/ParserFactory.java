package org.xml.sax.helpers;

import org.xml.sax.Parser;

@Deprecated
public class ParserFactory {
    private ParserFactory() {
    }

    public static Parser makeParser() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NullPointerException, ClassCastException {
        String property = System.getProperty("org.xml.sax.parser");
        if (property != null) {
            return makeParser(property);
        }
        throw new NullPointerException("No value for sax.parser property");
    }

    public static Parser makeParser(String str) throws ClassNotFoundException, IllegalAccessException, InstantiationException, ClassCastException {
        return (Parser) NewInstance.newInstance(NewInstance.getClassLoader(), str);
    }
}
