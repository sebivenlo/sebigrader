package sebigrader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static sebigrader.Settings.SETTINGS;

/**
 *
 * @author hom
 */
public class Main {

    public static void main( String[] args ) throws IOException {
        new Main().run();
    }

    public void run() {
        TemplateCollector tcol = createTemplate();
        String d = SETTINGS.get( "sandboxes" );
        Path p = Paths.get( d );
        GradeCollector gcol = new GradeCollector( tcol );

        TestReportHandler han = new TestReportHandler( p, gcol );
        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );
        Map<GradeKey, Map<String, String>> results = gcol.getResults();

//        PrintStream sout = System.out;
        printTemplate( tcol, System.out );
        try ( PrintStream out = new PrintStream( new File( "questions.csv" ) ); ) {

            printTemplate( tcol, out );

        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
        }
        try (
                PrintStream out = new PrintStream( new File( "scores.csv" ) ); ) {

            printScores( out, results );

        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
        }

//        results.forEach( System.out::println );
        printScores( System.out, results );
    }

    void printScores( final PrintStream out, Map<GradeKey, Map<String, String>> results ) {
        out.println( GradeRecord.CSVHEADER );
        Comparator<Entry<GradeKey, Map<String, String>>> comp1 = ( a, b ) -> a.getKey().getStick().compareTo( b.getKey().getStick() );
        Comparator<Entry<GradeKey, Map<String, String>>> comp2 = ( a, b ) -> a.getKey().getTask() - b.getKey().getTask();
        results.entrySet()
                .stream()
                .sorted( comp1.thenComparing( comp2 ) )
                .map( e -> GradeRecord.of( e.getKey(), e.getValue() ) )
                .forEach( out::println );
    }

    final String q = "\"";

    void printTemplate( TemplateCollector tcol, PrintStream sout ) {
        sout.println( Aspect.CSVHEADER );
        tcol.lookupMap().keySet().forEach( sout::println );
    }

    TemplateCollector createTemplate() {
        String d = SETTINGS.get( "solutiondir" );
        Path p = Paths.get( d ); //, "TEST-administration.PersonTest.xml" );
        TemplateCollector cons = new TemplateCollector();
        TestReportHandler han = new TestReportHandler( p, cons );
        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );
        return cons;
    }

    void walk( Path p, TestFileVisitor vst ) {
        try {
            Files.walkFileTree( p.toAbsolutePath(), vst );
        } catch ( IOException ex ) {
            Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

}
