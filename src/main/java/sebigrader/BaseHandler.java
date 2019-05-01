package sebigrader;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    protected final Predicate<Path> examDirAcceptor;
    
    public BaseHandler( Map<String, Map<String, GraderConsideration>> considerations ) {
        this.considerations = considerations;
        examDirAcceptor = p -> true;
    }

    protected BaseHandler forPath( Path p) {
        setFromPath( p );
        return this;
    }

    private void setFromPath( Path path) {
        String[] fnParts = path.toAbsolutePath().normalize().toString().split( "/" );
        System.out.println( "fnParts=" + Arrays.toString( fnParts ) );
        Pattern examdir = SETTINGS.EXAM_DIR_PATTERN;
        System.out.println( "examdir = " + examdir );
        for ( int i = 0; i < fnParts.length; i++ ) {
            if ( i > 0 ) {
                Matcher matcher = examdir.matcher( fnParts[ i ] );
                
                //could solution directory 
                if ( matcher.matches() ) {//.equals( SETTINGS.BUILD_DIR ) ) {
                    if ( i < fnParts.length - 1 ) {
                        project = fnParts[ i + 1 ];
                    }
                    exam = matcher.group( 1 );
                    System.out.println( "exam = " + exam + " correcting project = " + project );
                    break;
                } 
            }
        }
    }
}
