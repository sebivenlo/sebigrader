package sebigrader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Walks the tree of examsolution and collects data from test reports. Collects
 * data in {@code Map project-> Map<methodName,Consideration}
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
class TestReportToTemplateHandler extends BaseHandler {

    private String testMethod = null;
    private String text = "";
    private final String event;

    public TestReportToTemplateHandler( String event, Map<String, Map<String, GraderConsideration>> considerations ) {
        super( considerations );
        this.event = event;
    }

    //private String projectKey = "";
    public TestReportToTemplateHandler( Map<String, Map<String, GraderConsideration>> considerations ) {
        this( "unknown", considerations );

    }

    @Override
    public TestReportToTemplateHandler forPath( Path p ) {
        super.forPath( p );
        if ( !considerations.containsKey( project ) ) {
            System.out.println( "adding project " + project );
            considerations.put( project, new TreeMap<>() );
        }
        return this;
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
    public void characters( char[] ch, int start, int length ) throws SAXException {
        text += new String( Arrays.copyOfRange( ch, start, start + length ) ).trim();
    }

    /**
     * Invoke on end tag, completing collection and resetting states.
     *
     * @param uri of the element source
     * @param localName of error
     * @param qName tag name
     * @throws SAXException on error
     */
    @Override
    public void endElement( String uri, String localName, String qName ) throws SAXException {
        super.endElement( uri, localName, qName );
        if ( qName.equalsIgnoreCase( "testcase" ) ) {
            GraderConsideration gc = new GraderConsideration( event, project, testMethod );
            putConsideration( gc );
            testMethod = null;
            text = "";
        }
    }

    private void putConsideration( GraderConsideration gc ) {

        Map<String, GraderConsideration> projectMap = considerations.get( project );
        if ( projectMap == null ) {
            projectMap = new HashMap<>();
            considerations.put( project, projectMap );
        }
        projectMap.put( testMethod, gc );
    }

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
        }
    }
}
