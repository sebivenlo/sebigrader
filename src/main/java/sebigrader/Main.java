package sebigrader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.util.Comparator.comparing;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        List<GradeRecord> results = gcol.getResults();

//        PrintStream sout = System.out;
        printTemplate( tcol, System.out );

        try (
                PrintStream out = new PrintStream( new File( "scores.csv" ) ); ) {

            out.println( "event,stick_nr,task,passFail,grade" );
            results.stream()
                    .map( GradeRecord::getCSVRecord ).forEach( out::println );

        } catch ( FileNotFoundException ex ) {
            Logger.getLogger( Main.class.getName() ).log( Level.SEVERE, null, ex );
        }

        results.forEach( System.out::println );

    }

    final String q="\"";
    void printTemplate( TemplateCollector tcol, PrintStream sout ) {
        tcol.lookupMap().entrySet().stream()
                .sorted( comparing( Map.Entry::getValue ) )
                .forEach( e -> {
                    sout.println( q+" "+e.getValue() +q+ ","+q + e.getKey().replace( ":", q+","+q)+q );
                } );
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
