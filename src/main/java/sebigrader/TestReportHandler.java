package sebigrader;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Consumer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TestReportHandler extends DefaultHandler {

    private Path reportPath;
    private final Consumer<GradeRecord> resultCollector;
    private String testMethod = "?";
    private StringBuilder text = new StringBuilder();

    public TestReportHandler( Path reportPath, Consumer<GradeRecord> resultCollector ) {
        this.reportPath = reportPath;
        this.resultCollector = resultCollector;
    }

    public Path getReportPath() {
        return reportPath;
    }

    public void setReportPath( Path reportPath ) {
        this.reportPath = reportPath;
    }

    /**
     * Characters between tags.
     *
     * @param ch characters
     * @param start position to consider
     * @param length to consider
     * @throws SAXException on error
     */
    @Override
    public void characters( char[] ch, int start, int length ) throws
            SAXException {
        text.append( new String( Arrays.copyOfRange( ch, start, start + length ) ).
                trim() );
    }

    StringBuilder error = new StringBuilder();
    StringBuilder failure = new StringBuilder();

    /**
     * Invoke on end tag, completing collection and resetting states.
     *
     * @param uri of the element source
     * @param localName of error
     * @param qName tag name
     * @throws SAXException on error
     */
    @Override
    public void endElement( String uri, String localName, String qName ) throws
            SAXException {
        super.endElement( uri, localName, qName );
       if ( qName.equalsIgnoreCase( "testcase" ) ) {
            if ( skipped ) {
                passFail = "I";
            } else if ( 0 == text.length() ) {
                passFail = "P";
            } else {
                if ( !failure.toString().isEmpty() ) {
                    passFail = "F";
                } else if ( !error.toString().isEmpty() ) {
                    passFail = "E";
                }
            }
            GradeRecord gr = GradeRecord.forMethod( this.reportPath, testMethod, passFail );
            this.resultCollector.accept( gr );
            testMethod = null;
        } else if ( qName.equals( "failure" ) ) {
            failure.append( text );
        } else if ( qName.equals( "error" ) ) {
            error.append( text );
        } else if ( qName.equals( "skipped" ) ) {
            skipped = true;
        }

    }
    String passFail = "?";
    boolean skipped = false;

    /**
     * See sax default handler.
     *
     * @param uri of the source
     * @param localName of the element
     * @param qName tag name
     * @param attributes of the tag
     * @throws SAXException on error
     */
    @Override
    public void startElement( String uri, String localName,
            String qName, Attributes attributes ) throws SAXException {
        if ( qName.equalsIgnoreCase( "testcase" ) ) {
            String className = attributes.getValue( "classname" );
            String name = attributes.getValue( "name" );

            testMethod = className + '#' + name;
            text = new StringBuilder();
            failure = new StringBuilder();
            error = new StringBuilder();
            passFail = "?";
            skipped = false;
        } else if ( qName.equals( "skipped" ) ) {
            skipped = true;
        }
    }

}
