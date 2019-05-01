package sebigrader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author hom
 */
enum Settings {
    SETTINGS;

    private final Properties settings;
    final Pattern SUBTREES_TO_SKIP ;
    final Pattern TEST_RESULT_FILE_PATTERN ;
    final String BUILD_DIR;
    final Pattern EXAM_DIR_PATTERN;

    final String RESULTS_DIR_ENDING;
    private static final String PROP_FILENAME = "sebigrader.properties";
    
    Settings() {
        settings = loadProperties();
        String subtreesSkipped = settings.getProperty("subtrees_to_skip", "(classes|empty|generated-sources)$" );
        SUBTREES_TO_SKIP = Pattern.compile( subtreesSkipped );
        String testResultFilePattern = settings.getProperty( "results_file_pattern", "^.*TestSuites?\\.xml$" );
        TEST_RESULT_FILE_PATTERN = Pattern.compile( testResultFilePattern );
        RESULTS_DIR_ENDING = settings.getProperty( "results_dir_ending", "results" );
        EXAM_DIR_PATTERN = Pattern.compile( settings.getProperty("exam_dir_pattern","examproject-EXAM(\\d{3})" ));
        BUILD_DIR=settings.getProperty( "build_dir", "build" );
    }

    private Properties loadProperties() {
        Properties result = new Properties();
        // first attempt to get a normal file from the well known location
        try ( InputStream in = new FileInputStream( new File( PROP_FILENAME ) ) ) {
            result.load( in );
            result.forEach( ( a, b ) -> System.out.println( a + " = " + b ) );
        } catch ( FileNotFoundException ignored ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log( Level.INFO, "properties file " + PROP_FILENAME + " not found" );
        } catch ( IOException ex ) {
            Logger.getLogger(Settings.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return result;

    }

    String get( String key ) {

        return (String) settings.getProperty( key );
    }
}
