package java.sql;

import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.Reader;
import java.p026io.Writer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

public interface SQLXML {
    void free() throws SQLException;

    InputStream getBinaryStream() throws SQLException;

    Reader getCharacterStream() throws SQLException;

    <T extends Source> T getSource(Class<T> cls) throws SQLException;

    String getString() throws SQLException;

    OutputStream setBinaryStream() throws SQLException;

    Writer setCharacterStream() throws SQLException;

    <T extends Result> T setResult(Class<T> cls) throws SQLException;

    void setString(String str) throws SQLException;
}
