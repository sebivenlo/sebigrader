package sebigrader;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TestResultTest {

    @Test
    public void parsePathSolution() {

        Path reportPath = Paths.get( "examsolution/admin/surefire-reports", "TEST-pack.PersonTest.xml" );
        String passFail = "P";
        String testMethod = "aTest";

        TestResult tr = TestResult.forMethod( reportPath, testMethod, passFail );
        assertEquals( "PRC2-2018-07-02", tr.getEvent() );
        assertEquals( passFail, tr.getPassFail() );
        assertEquals( testMethod, tr.getTestmethod() );
        assertEquals( "admin", tr.getProject() );

    }

//    @Ignore( "Thick TDD" )
    @Test
    public void parseCandidate() {

        String project = "admin";
        Path reportPath = Paths.get( "sw/examproject-EXAM123/" + project + "/B0-surefire-reports/",
                "TEST-pack.PersonTest.xml" );
        String passFail = "F";
        String testMethod = "aTest";

        TestResult gr = TestResult.forMethod( reportPath, testMethod, passFail );
        assertEquals( "PRC2-2018-07-02", gr.getEvent() );
        assertEquals( "123", gr.getStick() );
        assertEquals( passFail, gr.getPassFail() );
        assertEquals( testMethod, gr.getTestmethod() );
        assertEquals( project, gr.getProject() );

    }
}
