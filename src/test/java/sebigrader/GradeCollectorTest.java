package sebigrader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class GradeCollectorTest extends CollectorsTestBase {

    //@Ignore( "Think TDD" )
    @Test
    public void correctTwo() {

        TemplateCollector col = createTemplate();
        assertNotNull( col );
        String d = "testreports/sw";
        Path p = Paths.get( d );//, "TEST-administration.PersonTest.xml" );
        GradeCollector gcol = new GradeCollector( col );

        TestReportHandler han = new TestReportHandler( p, gcol );
        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );
        assertEquals( "25 results", 25, gcol.getResults().size() );
    }
}
