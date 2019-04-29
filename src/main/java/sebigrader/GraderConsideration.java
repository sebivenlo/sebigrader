package sebigrader;

import java.util.Objects;

/**
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class GraderConsideration {

    private final int task;
    private final String event;
    private final double weight;
    private final String testMethod;
    private final String project;
    private final String objective;
    private final String description;
    private static int taskIdSeq = 0;

    private GraderConsideration( String event, int task, double points, String project, String testMethod, String objective, String description ) {
        this.event = event;
        this.task = task;
        this.weight = points;
        this.testMethod = testMethod;
        this.project = project;
        this.objective = objective;
        this.description = description;
    }

    public GraderConsideration( String event, String project, String testMethod ) {
        this( event, ++taskIdSeq, 5.0, project, testMethod, "", "" );
    }

    public int getId() {
        return task;
    }

    public double getWeight() {
        return weight;
    }

    public String getTestMethod() {
        return testMethod;
    }

    public String getObjective() {
        return objective;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "event=" + event + ", task=" + task + ", weigth=" + weight + ", testMethod=" + testMethod + ", project=" + project;
    }

    public String toXML( String indent ) {
        return indent + "<consideration id=\"T" + task + "\">\n"
                + indent + "\t<points>" + weight + "</points>\n"
                + indent + "\t<testmethod>" + testMethod + "</testmethod>\n"
                + indent + "\t<objective>" + objective + "</objective>\n"
                + indent + "\t<description>" + description + "</description>\n"
                + indent + "</consideration>";
    }

    public String getEvent() {
        return event;
    }

    public String getProject() {
        return project;
    }

    public static int getIdSeq() {
        return taskIdSeq;
    }

}
