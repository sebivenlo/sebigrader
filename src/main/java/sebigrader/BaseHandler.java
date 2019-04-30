package sebigrader;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.helpers.DefaultHandler;
import static sebigrader.Settings.SETTINGS;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class BaseHandler extends DefaultHandler {

    protected String project;
    protected String exam;
    protected final Map<String, Map<String, GraderConsideration>> considerations;

    public BaseHandler( Map<String, Map<String, GraderConsideration>> considerations ) {
        this.considerations = considerations;
    }

    public BaseHandler forPath( Path p ) {
        setFromPath( p );
        return this;
    }

    private void setFromPath( Path path ) {
        String[] fnParts = path.toAbsolutePath().normalize().toString().split( "/" );
        for ( int i = 0; i < fnParts.length; i++ ) {
            if ( i > 0 ) {
                if ( fnParts[ i ].equals( SETTINGS.BUILD_DIR ) ) {
                    project = fnParts[ i - 1 ];
                    if ( i > 2 ) {
                        exam = fnParts[ i - 2 ];
                    }
                    System.out.println( "project = " + project);
                    System.out.println( "exam = " + exam);
                    break;
                }
            }
        }
    }
}
