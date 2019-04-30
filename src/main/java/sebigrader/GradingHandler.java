package sebigrader;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.vandenhombergh@google.com}
 */
class GradingHandler extends BaseHandler {

    Map<String, Map<Integer, GradeRecord>> grades;
   private String testMethod = null;
    private String text = "";

    public GradingHandler( Map<String, Map<String, GraderConsideration>> cons ) {
        super( cons );
        grades = new TreeMap<>();
    }

    public Map<String, Map<Integer, GradeRecord>> getGrades() {
        return grades;
    }

    @Override
    public GradingHandler forPath( Path p ) {
        super.forPath( p );
        prepareGradingTable();
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
    public void characters( char[] ch, int start, int length ) throws
            SAXException {
        text += new String( Arrays.copyOfRange( ch, start, start + length ) ).
                trim();
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

            if ( considerations.get( project ).containsKey( testMethod ) ) {
                int id = considerations.get( project ).get( testMethod ).getId();
                GradeRecord gr = grades.get( exam ).get( id );
                if ( text.isEmpty() ) {
                    passFail = "P";
                    gr.setGrade( 10.0D );
                } else {
                    System.out.println( "text = " + text );
                    if ( !failure.toString().isEmpty() ) {
                        passFail = "F";
                    } else if ( !error.toString().isEmpty() ) {
                        passFail = "E";
                    }
                    gr.setGrade( 1.5D );
                }
                gr.setPassFail(passFail);
                System.err.println( gr );
            }
            testMethod = null;
        } else if ( qName.equals( "failure" ) ) {
            failure.append( text );
        } else if ( qName.equals( "error" ) ) {
            error.append( text );
        }

    }
    String passFail = "P";

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
            text = "";
            failure = new StringBuilder();
            error = new StringBuilder();
        }
    }

    private void prepareGradingTable() {
        Map<String, GraderConsideration> examTaskMap = considerations.get(
                project );
        Map<Integer, GradeRecord> gr = grades.get( exam );
        if ( gr == null ) {
            gr = new TreeMap<>();
            grades.put( exam, gr );
        }


        for ( GraderConsideration value : examTaskMap.values() ) {
            int id = value.getId();
            final GradeRecord gradeRecord = new GradeRecord( value.getEvent(),
                    value.getId(), exam, project, value.getTestMethod(),
                    "P", 0.0D );
            gr.put( value.getId(), gradeRecord );
        }
    }

}
