package sebigrader;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static sebigrader.Settings.SETTINGS;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class TestResult {

    static final Pattern stickDirPattern = Pattern.compile( "^.*?/examproject-EXAM(\\d{3})/(.+?)/([AB][AB]|B[0-9]).*$" );
    static final Pattern solutionDirPattern = Pattern.compile( ".*examsolution/(.+?)/.*" );

    static TestResult forMethod( Path reportPath, String testMethod, String passFail ) {
        String reportDir = reportPath.toAbsolutePath().toString();
        Matcher stickM = stickDirPattern.matcher( reportDir );
        String aStick = "000";
        String project = "unknown";
        String event = SETTINGS.get( "event" );
        String testMode = "AA";
        if ( stickM.matches() && stickM.groupCount() >= 2 ) { // wierd number
            System.out.println( "considering " + reportPath.toString() );
            aStick = stickM.group( 1 );// Integer.parseInt( stickM.group( 1 ) );
            project = stickM.group( 2 );
            testMode = stickM.group( 3 );
            return new TestResult( event, aStick, Aspect.of( event, project, testMethod ), passFail, testMode );
        }

        Matcher solM = solutionDirPattern.matcher( reportDir );
        if ( solM.matches() ) {
            project = solM.group( 1 );
        }
        TestResult tr = new TestResult( event, aStick, Aspect.of( event, project, testMethod ), passFail, testMode );
        return tr;
    }

    private final String event;
    private final String stick;
    private final Aspect aspect;
    private final String passFail;
    private final String testMode;

    private TestResult( String event, String stick, Aspect aspect, String passFail, String testMode ) {
        this.event = event;
        this.stick = stick;//.replace( "examproject-EXAM", "");
        this.aspect = aspect;
        this.passFail = passFail;
        this.testMode = testMode;
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
                + aspect.testMethod + "," + passFail + "," + normal;
    }

    public String getEvent() {
        return event;
    }

    public String getStick() {
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

    public String getPassFail() {
        return passFail;
    }

    public String getCSVRecord() {

        return String.format( "%s,%s,%2d,%s,%.1f", event, stick, aspect.task, passFail );
    }

    public Aspect getAspect() {
        return aspect;
    }

    public String getTestMode() {
        return testMode;
    }

}
