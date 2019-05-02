package sebigrader;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static sebigrader.Settings.SETTINGS;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class GradeRecord {

    public static Pattern stickDirPattern = Pattern.compile( "^.*?/examproject-EXAM(\\d{3})/(.+?)/.*$" );
    public static Pattern solutionDirPattern = Pattern.compile( ".*examsolution/(.+?)/.*" );

    static GradeRecord forMethod( Path reportPath, String testMethod, String passFail ) {
        String reportDir = reportPath.toAbsolutePath().toString();
        Matcher stickM = stickDirPattern.matcher( reportDir );
        int aStick = 0;
        String project = "unknown";
        if ( stickM.matches() && stickM.groupCount() >= 2 ) { // wierd number
            aStick = Integer.parseInt( stickM.group( 1 ) );
            project = stickM.group( 2 );
        }
        Matcher solM = solutionDirPattern.matcher( reportDir );
        if ( solM.matches() ) {
            project = solM.group( 1 );
        }
        String event = SETTINGS.get( "event" );
        int task = 0;
        return new GradeRecord( event, task, aStick, Aspect.of(project, testMethod), passFail );
    }

    private final String event;
    private final Integer stick;
    private final Aspect aspect;
    private String passFail;
    private double grade;

    private GradeRecord( String event, int task, Integer stick, String project,
            String testmethod, String passFail, double grade ) {
        this.event = event;
        this.stick = stick;//.replace( "examproject-EXAM", "");
        aspect = Aspect.of( project, testmethod );
        this.passFail = passFail;
        this.grade = grade;
    }

    public GradeRecord( String event, int task, Integer stick, Aspect aspect, String passFail, double grade ) {
        this.event = event;
        this.stick = stick;//.replace( "examproject-EXAM", "");
        this.aspect = aspect;
        this.passFail = passFail;
        this.grade = grade;
    }

    private GradeRecord( String event, int task, Integer aStick, Aspect aspect, String passFail ) {
        this( event, task, aStick, aspect, passFail, 0.0D );
    }

    @Override
    public String toString() {
        String style;
        String normal = "\033[m";
        String pass = "\033[1;37;42m";
        String error = "\033[1;37;41m";
        String ignored = "\033[1;30;47m";
        String fail = "\033[1;37;43m";
        switch ( passFail ) {
            default:
            case "P":
                style = pass;
                break;
            case "F":
                style = fail;
                break;
            case "E":
                style = error;
                break;
            case "I":
                style = ignored;
                break;

        }
        return style + event + "," + stick + "," + aspect.task + "," + aspect.project + ","
                + aspect.testMethod + "," + passFail + "," + grade + normal;
    }

    public String getEvent() {
        return event;
    }

    public Integer getStick() {
        return stick;
    }

    public int getTask() {
        return aspect.task;
    }

    public String getProject() {
        return aspect.project;
    }

    public String getTestmethod() {
        return aspect.testMethod;
    }

    public double getGrade() {
        return grade;
    }

    public GradeRecord setGrade( double grade ) {
        this.grade = grade;
        return this;
    }

    public String getPassFail() {
        return passFail;
    }

    public String getCSVRecord() {

        return String.format( "%s,%s,%2d,%s,%.1f", event, stick, aspect.task, passFail,
                grade );
    }

    void setPassFail( String passFail ) {
        this.passFail = passFail;
    }

//    public GradeRecord setTask( Integer task ) {
//        this.task = task;
//        return this;
//    }
}
