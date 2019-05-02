package sebigrader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
class CollectorsTestBase {

    TemplateCollector createTemplate() {
        String d = "testreports/examsolution";
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
            Logger.getLogger( getClass().getName() ).log( Level.SEVERE, null, ex );
        }
        //        Assert.fail( "method method reached end. You know what to do." );
    }
    
}
