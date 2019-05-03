package sebigrader;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class GradeRecord {

    private final String event;
    private final String stick;
    private final Integer task;
    private final Double testGrade;
    private final Double businessGrade;

    public GradeRecord( String event, String stick, Integer task, Double testGrade, Double businessGrade ) {
        this.event = event;
        this.stick = stick;
        this.task = task;
        this.testGrade = testGrade;
        this.businessGrade = businessGrade;
    }

    static Predicate<String> bTest = Pattern.compile( "^B\\d$" ).asPredicate();
    static Predicate<String> aTest = Pattern.compile( "^[AB]A$" ).asPredicate();

    static GradeRecord of( GradeKey key, Map<String, String> testModes ) {
        double testScore = 0.0D;
        double bussScore = 0.0D;

        bussScore = testModes.getOrDefault( "AB", "F" ).equals( "P" ) ? 10.0D : 0.0D;


        // test grade is determined by 
        // 1. pass BA and 
        boolean allBusinessPass = testModes.entrySet().stream().filter( e -> aTest.test( e.getKey() ) ).allMatch( e -> e.getValue().equals( "P" ) );

        // 2. Fail at least of Bn where n = 0..
        boolean anyTestFail = testModes.entrySet()
                .stream()
                .filter( e -> bTest.test( e.getKey() ) )
                .anyMatch( e -> e.getValue().equals( "F" ) );
        testScore += allBusinessPass ? 5.0D : 0.0D;
        testScore += anyTestFail ? 5.0D : 0.0D;

        return new GradeRecord( key.getEvent(), key.getStick(), key.getTask(), testScore, bussScore );
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

    public String csvHeader() {
        return "event,stick,task,test,buss";

    }

    @Override
    public String toString() {
        return q + event + qc + stick + "," + task + "," + testGrade + "," + businessGrade;
    }

}
