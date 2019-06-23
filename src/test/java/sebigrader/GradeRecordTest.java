package sebigrader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
        //{ "AB", "BA", "BB", "B0", "B1", "B2", "B3" };
        // label, test, buss, modes
        return new Object[][] {
            // tes, buss, fail modes
            { "FFFFEP", td( 05.0D, 00.0D, "FFFFEP" ) }, //
            { "PPFFEP", td( 10.0D, 10.0D, "PPFFEP" ) },
            { "FPFFFP", td( 10.0D, 00.0D, "FPFFFP" ) },
            { "PIIIII", td( 00.0D, 10.0D, "PIIIII" ) },
            { "FPEPPP", td( 05.0D, 00.0D, "FPEPPP" ) },
            { "FPPPPP", td( 00.0D, 00.0D, "FPPPPP" ) },// trivial green
            { "PPPPPP", td( 00.0D, 10.0D, "PPPPPP" ) },
            { "PF", td( 00.0D, 10.0D, "PF" ) },
            { "F", td( 00.0D, 00.0D, "F" ) }, };

    }

    Map<String, String> modes = new LinkedHashMap<>();
    TD td;
    String flag;
    String passFails = "";

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
        Assert.assertEquals( "test grade " + modes + " " + passFails, td.testGrade, gr.getTestGrade(), 0.01D );
        Assert.assertEquals( "business grade " + modes + " " + passFails, td.businessGrade, gr.getBusinessGrade(), 0.01D );

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
        static final String[] modeNames = { "AB", "BA", "BB", "B0", "B1", "B2", "B3" };
        final String[] modes;

        public TD( Double testGrade, Double businessGrade, String fp, String... modes ) {
            this.testGrade = testGrade;
            this.businessGrade = businessGrade;
            List<String> modeList = new ArrayList<>();
            for ( int i = 0; i < fp.length(); i++ ) {
                modeList.add( modeNames[ i ] );
                modeList.add( "" + fp.charAt( i ) );
            }
            this.modes = modeList.toArray( new String[ modeList.size() ] );
        }

        @Override
        public String toString() {
            return "TD{" + "testGrade=" + testGrade + ", businessGrade=" + businessGrade + ", modes=" + Arrays.toString( modes ) + '}';
        }

    }

    static TD td( Double testGrade, Double businessGrade, String fp, String... modes ) {
        return new TD( testGrade, businessGrade, fp, modes );
    }

}
