package sebigrader;

import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static sebigrader.Settings.SETTINGS;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class GradeRecord {

    static final Pattern stickDirPattern = Pattern.compile( "^.*?/examproject-EXAM(\\d{3})/(.+?)/([AB][AB]|B[0-9]).*$" );
    static final Pattern solutionDirPattern = Pattern.compile( ".*examsolution/(.+?)/.*" );

    static GradeRecord forMethod( Path reportPath, String testMethod, String passFail ) {
        String reportDir = reportPath.toAbsolutePath().toString();
        Matcher stickM = stickDirPattern.matcher( reportDir );
        int aStick = 0;
        String project = "unknown";
        String event = SETTINGS.get( "event" );
        String testMode= "AA";
        if ( stickM.matches() && stickM.groupCount() >= 2 ) { // wierd number
            aStick = Integer.parseInt( stickM.group( 1 ) );
            project = stickM.group( 2 );
            testMode= stickM.group( 3 );
            return new GradeRecord( event, aStick, Aspect.of( project, testMethod ), passFail );
        }

        Matcher solM = solutionDirPattern.matcher( reportDir );
        if ( solM.matches() ) {
            project = solM.group( 1 );
        }
        return new GradeRecord( event, aStick, Aspect.of( project, testMethod ), passFail );
    }

    
    private final String event;
    private final Integer stick;
    private final Aspect aspect;
//    private String passFail;
//    private double grade;

    private GradeRecord( String event, Integer stick, Aspect aspect, String passFail, double grade ) {
        this.event = event;
        this.stick = stick;//.replace( "examproject-EXAM", "");
        this.aspect = aspect;
//        this.passFail = passFail;
//        this.grade = grade;
    }

//    public GradeRecord( String event, Integer stick, Aspect aspect, String passFail, double grade ) {
//        this.event = event;
//        this.stick = stick;//.replace( "examproject-EXAM", "");
//        this.aspect = aspect;
//        this.passFail = passFail;
//        this.grade = grade;
//    }
    private GradeRecord( String event, Integer aStick, Aspect aspect, String passFail ) {
        this( event, aStick, aspect, passFail, 0.0D );
    }

//    @Override
//    public String toString() {
//        String style;
//        String normal = "\033[m";
//        String pass = "\033[1;37;42m";
//        String error = "\033[1;37;41m";
//        String ignored = "\033[1;30;47m";
//        String fail = "\033[1;37;43m";
//        switch ( passFail ) {
//            default:
//            case "P":
//                style = pass;
//                break;
//            case "F":
//                style = fail;
//                break;
//            case "E":
//                style = error;
//                break;
//            case "I":
//                style = ignored;
//                break;
//
//        }
//        return style + event + "," + stick + "," + aspect.task + "," + aspect.project + ","
//                + aspect.testMethod + "," + passFail + "," + grade + normal;
//    }

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

//    public double getGrade() {
//        return grade;
//    }
//
//    public GradeRecord setGrade( double grade ) {
//        this.grade = grade;
//        return this;
//    }
//
//    public String getPassFail() {
//        return passFail;
//    }
//
//    public String getCSVRecord() {
//
//        return String.format( "%s,%s,%2d,%s,%.1f", event, stick, aspect.task, passFail,
//                grade );
//    }
//
//    void setPassFail( String passFail ) {
//        this.passFail = passFail;
//    }
//
    public Aspect getAspect() {
        return aspect;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode( this.stick );
        hash = 83 * hash + Objects.hashCode( this.aspect );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final GradeRecord other = (GradeRecord) obj;
        if ( !Objects.equals( this.stick, other.stick ) ) {
            return false;
        }
        if ( !Objects.equals( this.aspect, other.aspect ) ) {
            return false;
        }
        return true;
    }
    
}
