package sebigrader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        gcol.getResults().forEach( System.out::println );

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
