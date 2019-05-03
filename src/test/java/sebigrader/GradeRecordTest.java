package sebigrader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@RunWith( Parameterized.class )
public class GradeRecordTest {

    @Parameters( name = "{index}: {0}" )
    public static Object[][] data() {

        // label, test, buss, modes
        return new Object[][] {
            { "FFFFEP", td( 5.0D, 0.0D, "AB", "F", "BA", "F", "BO", "F", "B1", "F", "B2", "E", "B3", "P" ) },
            { "PPFFEP", td( 10.0D, 10.0D, "AB", "P", "BA", "P", "BO", "F", "B1", "F", "B2", "E", "B3", "P" ) },
            { "FPFFEP", td( 10.0D, 0.0D, "AB", "F", "BA", "P", "BO", "F", "B1", "F", "B2", "E", "B3", "P" ) },
            { "PIIIII", td( 0.0D, 10.0D, "AB", "P", "BA", "I", "BO", "I", "B1", "I", "B2", "I", "B3", "I" ) },
            { "FPEPPP", td( 5.0D, 0.0D, "AB", "F", "BA", "P", "BO", "E", "B1", "P", "B2", "P", "B3", "P" ) },
            { "PF", td( 10.0D, 10.0D, "AA", "P", "BO", "F" ) },
        };

    }

    Map<String, String> modes = new HashMap<>();
    TD td;
    String flag;
    String passFails="";
    public GradeRecordTest( String flag, TD td ) {
        this.flag = flag;
        this.td = td;
        for ( int i = 0; i < td.modes.length; i += 2 ) {
            modes.put( td.modes[ i + 0 ], td.modes[ i + 1 ] );
            passFails += td.modes[ i + 1 ];
        }

    }

    //@Ignore( "Think TDD" )
    @Test
    public void method() {
        GradeKey key = createGradeKey();

        GradeRecord gr = GradeRecord.of( key, modes );
        Assert.assertEquals( "test grade " + passFails, td.testGrade, gr.getTestGrade(), 0.01D );
        Assert.assertEquals( "business grade" + passFails, td.businessGrade, gr.getBusinessGrade(), 0.01D );

        //Assert.fail( "method method reached end. You know what to do." );
    }

    static GradeKey createGradeKey() {
        String project = "admin";
        Path reportPath = Paths.get( "sw/examproject-EXAM123/" + project + "/B0-surefire-reports/",
                "TEST-pack.PersonTest.xml" );
        String passFail = "F";
        String testMethod = "aTest";
        TestResult tr = TestResult.forMethod( reportPath, testMethod, passFail );
        GradeKey key = GradeKey.of( tr );
        return key;
    }

    static class TD {

        final Double testGrade;
        final Double businessGrade;
        final String[] modes;

        public TD( Double testGrade, Double businessGrade, String... modes ) {
            this.testGrade = testGrade;
            this.businessGrade = businessGrade;
            this.modes = modes;
            System.out.println( "modes = " + Arrays.toString( modes ) );
        }

    }

    static TD td( Double testGrade, Double businessGrade, String... modes ) {
        return new TD( testGrade, businessGrade, modes );
    }

}
