package sebigrader;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class GradeRecordTest {

    @Test
    public void parsePathSolution() {

        Path reportPath = Paths.get( "", "TEST-pack-PersonTest.xml" );
        String passFail = "P";
        String testMethod = "aTest";

        GradeRecord gr = GradeRecord.forMethod( reportPath, testMethod, passFail );
        assertEquals( "PRC2-2018-07-02", gr.getEvent() );
        assertEquals( passFail, gr.getPassFail() );
        assertEquals( testMethod, gr.getTestmethod() );

    }

//    @Ignore( "Thick TDD" )
    @Test
    public void parseCandidate() {

        String project = "admin";
        Path reportPath = Paths.get( "sw/examproject-EXAM123/" + project + "/surefire-reports/",
                 "TEST-pack-PersonTest.xml" );
        String passFail = "F";
        String testMethod = "aTest";

        GradeRecord gr = GradeRecord.forMethod( reportPath, testMethod, passFail );
        assertEquals( "PRC2-2018-07-02", gr.getEvent() );
        assertEquals( 123, gr.getStick() );
        assertEquals( passFail, gr.getPassFail() );
        assertEquals( testMethod, gr.getTestmethod() );
        assertEquals( project, gr.getProject() );

    }
}
