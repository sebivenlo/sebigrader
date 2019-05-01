package sebigrader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh (homberghp)
 * {@code pieter.van.den.hombergh@gmail.com}
 */
public class SimpleFileVisitorTest {

    @Test
    public void firstVisit() {
        String d ="testreports";
        Path p = Paths.get( d);//, "TEST-administration.PersonTest.xml" );
        Consumer<GradeRecord> cons = System.out::println;
        TestReportHandler han = new TestReportHandler( p, cons );
        
        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, cons, han );
        try {
            Path walkFileTree = Files.walkFileTree( p.toAbsolutePath(), vst);

        } catch ( IOException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
            ex.printStackTrace();
        }

//        Assert.fail( "method method reached end. You know what to do." );
    }
}
