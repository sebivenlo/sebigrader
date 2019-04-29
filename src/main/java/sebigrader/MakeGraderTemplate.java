package sebigrader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static java.util.Comparator.comparing;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
class MakeGraderTemplate implements FileVisitor<Path>, Runnable {

    private final PrintStream out;
    private final Path startingDir;
    private final Map<String, Map<String, GraderConsideration>> consMap;
    private String moduleName;
    private BaseHandler handler = null;
    private final List<String> projectOrder = new ArrayList<>();
    static String resultsDir = "sandboxes";
    private String event;

    @Override
    public void run() {
        getBaseConfig();
        TestReportToTemplateHandler thandler = new TestReportToTemplateHandler(
                event, consMap );
        handler = thandler;
        try {
            for ( String project : projectOrder ) {
                System.out.println( "work for project '" + project + '\'' );
                Path projectDir = startingDir.resolveSibling( "examsolution/"
                        + project );
                Files.walkFileTree( projectDir, this );
            }
        } catch ( IOException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        }
        printQuestionInserts( "questions.csv" );
        GradingHandler gradingHandler = new GradingHandler( consMap );
        handler = gradingHandler;
        Path sw = Paths.get( resultsDir );
        try {
            Files.walkFileTree( sw, this );
        } catch ( IOException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        }
        printScores( "scores.csv", gradingHandler );
    }

    private void printScores( String fileName, GradingHandler gradingHandler ) {
        try ( PrintStream out = new PrintStream( fileName ) ) {
            out.println( "event,stick_nr,task,passFail,score" );
            for ( Map.Entry<String, Map<Integer, GradeRecord>> e
                    : gradingHandler.getGrades().entrySet() ) {
                Map<Integer, GradeRecord> examGrades = e.getValue();
                for ( GradeRecord gradeRecord : examGrades.values() ) {
                    out.println( gradeRecord.getCSVRecord() );
                }
            }

        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        }
    }

    public MakeGraderTemplate( Path startingDir, PrintStream out ) {
        this.out = out;
        this.startingDir = startingDir;
        consMap = new LinkedHashMap<>();

    }

    private void printTemplateXML() {
        out.println( "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                + "<?xml-stylesheet type=\"text/xsl\" href=\"sebigrader.xsl\" ?>\n"
                + "<!-- fontys venlo sebi grading template -->\n"
                + "  <sebigradingtemplate project=\"" + moduleName + "\">" );
        for ( Map.Entry<String, Map<String, GraderConsideration>> m : consMap.
                entrySet() ) {
            out.append( "<project " ).append( " name=\"" ).append( m.getKey() ).
                    append( "\">\n" );
            for ( Map.Entry<String, GraderConsideration> e : m.getValue().
                    entrySet() ) {
                out.append( e.getValue().toXML( "  " ) ).append( "\n" );
            }
            out.append( "</project>\n" );
        }
        out.println( "</sebigradingtemplate>" );
    }

    private void getBaseConfig() {
        Properties props = new Properties();
        moduleName = "unknown";
        try {
            props.load( new FileInputStream( "../default.properties" ) );
            moduleName = props.getProperty( "module_name" );
        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        } catch ( IOException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        }
        String pwd = System.getProperty( "user.dir" );
        String[] parts = pwd.split( "/" );

        event = moduleName + parts[ parts.length - 1 ];
        Map<String, String> orderMap = new TreeMap<>();
        try {
            // read doexports for order of tasks
            orderMap = Files.lines( Paths.get( "doexports" ) )
                    .filter( l -> l.startsWith( "fix_task_prefix" ) )
                    .map( s -> s.split( "\\s+" ) )
                    .filter( p -> p.length >= 3 )
                    .peek( p -> System.out.println( "p" + Arrays.toString( p ) ) ).
                    collect( toMap( p -> p[ 2 ], q -> q[ 1 ] ) );
        } catch ( IOException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        }
        for ( String value : orderMap.values() ) {
            System.out.println( "order value = " + value );
            projectOrder.add( value );
        }
    }

    static final String PROP_FILENAME = "sebigrader.properties";
    static Properties properties;

    public static void main( String[] args ) throws IOException {
        loadProperties();

        try ( PrintStream out = new PrintStream( "correction.xml" ); ) {
            Path startingDir = Paths.get( resultsDir );
            //TestReportVisitor visitor= new TestReportVisitor(new TestReportHandler());
            MakeGraderTemplate grader
                    = new MakeGraderTemplate( startingDir, out );
            grader.run();
        }
    }

    private static Pattern SUBTREES_TO_SKIP = Pattern.compile( "(classes|empty|generated-sources)$" );
    private static Pattern TEST_RESULT_FILE_PATTERN = Pattern.compile(
            "^.*TestSuites?\\.xml$" );

    private static String resultsDirEnding= "results";
    
    private static void loadProperties() throws IOException {
        properties = new Properties();
        // first attempt to get a normal file from the well known location
        InputStream inputStream = null;
        String usedFile = PROP_FILENAME;
        boolean foundFile = false;
        try {
            File f = new File( usedFile );
            inputStream = new FileInputStream( f );
            foundFile = true;
        } catch ( FileNotFoundException ignored ) {
        }
        System.out.println( "inputStream filename = " + usedFile );
        properties.load( inputStream );
        properties.forEach( ( a, b ) -> System.out.println( a + " = " + b ) );

        String subtreesSkipped = properties.getProperty( "subtrees_to_skip", "(classes|empty|generated-sources)$" );
        SUBTREES_TO_SKIP = Pattern.compile( subtreesSkipped );
        String testResultFilePattern = properties.getProperty("results_file_pattern", "^.*TestSuites?\\.xml$" );
        TEST_RESULT_FILE_PATTERN = Pattern.compile( testResultFilePattern );
        resultsDirEnding = properties.getProperty("result_dir_ending", "results");
    }

    private void doFile( Path p ) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse( p.toFile(), handler );

        } catch ( ParserConfigurationException | SAXException | IOException e ) {
            e.printStackTrace();
        }
    }

    private Path currentDirectory;

    @Override
    public FileVisitResult preVisitDirectory( Path dir,
            BasicFileAttributes attrs ) throws IOException {
        //System.out.println( "pre visiting dir = " + dir);
        String dirToString = dir.toAbsolutePath().normalize().toString();
        if ( dir.toString().endsWith( "results" ) ) {
            //System.err.println( "found build at  = " + dir );
            handler.forPath( dir );
        }
        if ( SUBTREES_TO_SKIP.matcher( dirToString ).matches() ) {
            System.err.println( "skipping path " + dirToString );
            return FileVisitResult.SKIP_SUBTREE;
        } else {
            currentDirectory = dir;
            return CONTINUE;
        }
    }

    @Override
    public FileVisitResult visitFile( Path file, BasicFileAttributes attrs )
            throws IOException {
        String fileName = file.getFileName().toString();
        if ( TEST_RESULT_FILE_PATTERN.matcher( fileName ).matches() ) {
            System.err.println( "visit  " + file.toAbsolutePath() );
            doFile( file );
        } else {
            //System.err.println("filename does not match "+fileName +" pattern /"+TEST_RESULT_FILE_PATTERN.pattern()+ '/');
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed( Path file, IOException exc ) throws
            IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory( Path dir, IOException exc )
            throws IOException {
        return CONTINUE;
    }

    private void printQuestionInserts( String fileName ) {
        try ( PrintStream out = new PrintStream( fileName ); ) {
            out.println(
                    "event,project,task,qid,testmethod,weight" );
            for ( String project : consMap.keySet() ) {
                Map<String, GraderConsideration> cm = consMap.get( project );
                printConsiderationForProject( cm, out, project );
            }
        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        }
    }

    private void printConsiderationForProject(
            final Map<String, GraderConsideration> cm, final PrintStream out1,
            final String project ) {
        cm.entrySet().stream()
                .sorted( ( a, b ) -> Integer.compare( a.getValue().getId(), b.
                getValue().getId() ) ).forEach( e -> {
            GraderConsideration gc = e.getValue();
            int id = gc.getId();
            String ids = String.format( "Q%02d", id );
            out1.println( "" + event + "," + project + "," + id + "," + ids + "," + gc.getTestMethod() + ",5" );

        } );
    }

}
