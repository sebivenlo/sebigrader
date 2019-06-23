package sebigrader;

import static java.util.Comparator.comparing;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class GradeRecord {

    private final String event;
    private final String stick;
    private final Integer task;
    private final String passFail;
    private final Double testGrade;
    private final Double businessGrade;

    public GradeRecord( String event, String stick, Integer task, String passFail, Double testGrade, Double businessGrade ) {
        this.event = event;
        this.stick = stick;
        this.task = task;
        this.passFail = passFail;
        this.testGrade = testGrade;
        this.businessGrade = businessGrade;
    }

    static GradeRecord of( GradeKey key, Map<String, String> testModes ) {
        double testScore = 0.0D;
        double bussScore = 0.0D;
        boolean studentCodePasses = studentCodePasses( testModes );

        boolean refCodePasses = refCodePassesStudent( testModes );

        // 2. Fail at least of Bn where n = 0..
        boolean anyTestFail = anyTestFail( testModes );
        boolean trivialGreen = trivialGreen( testModes );
        boolean trivialRed = trivialRed( testModes );
        boolean passBB = passBB( testModes );
        boolean notTrivial = !( trivialGreen || trivialRed );

        bussScore = studentCodePasses ? 10.0D : 0.0D;

        testScore += ( !trivialGreen && refCodePasses ) ? 5.0D : 0.0D;
        testScore += ( !trivialRed && anyTestFail ) ? 5.0D : 0.0D;

        String passFail
                = testModes.entrySet()
                        .stream()
                        .sorted( comparing( Entry<String, String>::getKey ) )
                        .map( e -> e.getKey() + '=' + e.getValue() )
                        .collect( joining( ":" ) );

        return new GradeRecord( key.getEvent(), key.getStick(), key.getTask(), passFail, testScore, bussScore );
    }

    static boolean passBB( Map<String, String> testModes ) {
        return testModes.getOrDefault( "BB", "F" ).equals( "P" );
    }

    static boolean studentCodePasses( Map<String, String> testModes ) {
        return testModes.getOrDefault( "AB", "F" ).equals( "P" );
    }

    static boolean refCodePassesStudent( Map<String, String> testModes ) {
        // test grade is determined by
        // 1. pass BA and
        return testModes.getOrDefault( "BA", "F" ).equals( "P" );
    }

    static Predicate<String> bTest = Pattern.compile( "^B[B0-9]$" ).asPredicate();

    static boolean trivialRed( Map<String, String> testModes ) {
        return testModes.size() > 0
                && testModes.entrySet()
                        .stream()
                        .filter( e -> bTest.test( e.getKey() ) )
                        .allMatch( e -> e.getValue().matches( "^[EF]$" ) );
    }

    static boolean trivialGreen( Map<String, String> testModes ) {
        return testModes.size() > 0
                && testModes.entrySet()
                        .stream()
                        .filter( e -> bTest.test( e.getKey() ) )
                        .map( s -> s.getValue() )
                        .allMatch( "P"::equals );
    }

    static boolean anyTestFail( Map<String, String> testModes ) {
        return testModes
                .entrySet()
                .stream()
                .filter( e -> bTest.test( e.getKey() ) && e.getValue().equals( "F" ) )
                .count() > 0;
    }

    public String getEvent() {
        return event;
    }

    public String getStick() {
        return stick;
    }

    public Integer getTask() {
        return task;
    }

    public Double getBusinessGrade() {
        return businessGrade;
    }

    public Double getTestGrade() {
        return testGrade;
    }

    static String q = "\"";
    static String qc = "\",";
    static String qcq = "\",\"";
    static String cq = ",\"";

    public static final String CSVHEADER = "event,stick,task,pass_fail,test,buss";

    @Override
    public String toString() {
        return q + event + qc + stick + "," + task + cq + passFail + qc + testGrade + "," + businessGrade;
    }

}
