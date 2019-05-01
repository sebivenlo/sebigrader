package sebigrader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TemplateCollectorTest extends CollectorsTestBase {

//    @Ignore( "Think TDD" )
    @Test
    public void getSolutionTemplate() {

        TemplateCollector cons = createTemplate();
        assertEquals( 6, cons.size() );

    }


//    @Ignore( "Think TDD" )
    @Test
    public void correctAdmin() {
        String d = "testreports/examsolution";
        Path p = Paths.get( d );//, "TEST-administration.PersonTest.xml" );
        TemplateCollector cons = new TemplateCollector();
        TestReportHandler han = new TestReportHandler( p, cons );

        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );
        assertEquals( 6, cons.size() );
    }
    

}
