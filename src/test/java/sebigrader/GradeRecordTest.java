package sebigrader;

import java.nio.file.Path;
import java.nio.file.Paths;
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
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class GradeRecordTest {

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
//    @Ignore
    @Test
    public void parsePathSolution() {

        Path reportPath = Paths.get( "", "TEST-pack-PersonTest.xml" );
        String passFail = "P";
        String testMethod = "aTest";

        GradeRecord gr = GradeRecord.forMethod( reportPath, testMethod, passFail );
        assertEquals( "PRC2-2018-07-02", gr.getEvent() );
        assertEquals( passFail, gr.getPassFail() );
        assertEquals( testMethod, gr.getTestmethod() );

//        Assert.fail( "method parsePathSolution reached end. You know what to do." );
    }

//    @Ignore( "Thick TDD" )
    @Test
    public void parseCandidate() {

        Path reportPath = Paths.get( "sw/examproject-EXAM123/admin/surefire-reports/", "TEST-pack-PersonTest.xml" );
        String passFail = "F";
        String testMethod = "aTest";

        GradeRecord gr = GradeRecord.forMethod( reportPath, testMethod, passFail );
        assertEquals( "PRC2-2018-07-02", gr.getEvent() );
        assertEquals( 123, gr.getStick() );
        assertEquals( passFail, gr.getPassFail() );
        assertEquals( testMethod, gr.getTestmethod() );

//        Assert.fail( "method parseCandidate reached end. You know what to do." );
    }

}
