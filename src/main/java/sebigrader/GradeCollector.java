package sebigrader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class GradeCollector implements Consumer<GradeRecord> {

    private final TemplateCollector template;

    private final List<GradeRecord> results = new ArrayList<>();

    public GradeCollector( TemplateCollector template ) {
        this.template = template;
    }

    @Override
    public void accept( GradeRecord t ) {
        template.lookupTaskId( t ).ifPresent( task -> {
            gradeTask( t, task );
            results.add( t );
        } );
    }

    void gradeTask( GradeRecord t, Integer task ) {
        double grade = 0.0d;
        switch ( t.getPassFail() ) {
            case "P":
                grade = 10.0D;
                break;
            case "F":
                grade = 1.5D;
                break;
            default:
            case "I":
                grade = 0.0D;
                break;
            case "E":
                grade = 1.0D;
                break;

        }
        t.setGrade( grade );
    }

    public List<GradeRecord> getResults() {
        return results;
    }

}
