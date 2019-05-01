package sebigrader;

import java.nio.file.Path;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class GradeRecord {

    static GradeRecord forMethod( Path reportPath, String testMethod, String passFail ) {
        String aStick = "999";
        String event = "now";
        int task = 0;
        String project = "test";
        return new GradeRecord( event, task, aStick, project, testMethod, passFail );
    }

    private final String event;
    private final String stick;
    private final int task;
    private final String project;
    private final String testmethod;
    private String passFail;
    private double grade;

    public GradeRecord( String event, int task, String stick, String project,
            String testmethod, String passFail, double grade ) {
        this.event = event;
        this.task = task;
        this.stick = stick;//.replace( "examproject-EXAM", "");
        this.project = project;
        this.testmethod = testmethod;
        this.passFail = passFail;
        this.grade = grade;
    }

    private GradeRecord( String event, int task, String aStick, String project, String testMethod, String passFail ) {
        this( event, task, aStick, project, testMethod, passFail, 0.0D );
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
        return style + event + "," + stick + "," + task + "," + project + ","
                + testmethod + "," + passFail + "," + grade + normal;
    }

    public String getEvent() {
        return event;
    }

    public String getStick() {
        return stick;
    }

    public int getTask() {
        return task;
    }

    public String getProject() {
        return project;
    }

    public String getTestmethod() {
        return testmethod;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade( double grade ) {
        this.grade = grade;
    }

    public String getPassFail() {
        return passFail;
    }

    public String getCSVRecord() {

        return String.format( "%s,%s,%2d,%s,%.1f", event, stick, task, passFail,
                grade );
        //return '"' + event + "\",\"" + stick + "\",\"" + task + "\",\"" + grade + '"';
    }

    void setPassFail( String passFail ) {
        this.passFail = passFail;
    }

}