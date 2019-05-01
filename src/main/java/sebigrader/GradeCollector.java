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
            double grade = t.getPassFail().equals( "P" ) ? 10.0D : 1.0D;
            t.setTask( task ).setGrade( grade );
            results.add( t );
        } );
    }

    public List<GradeRecord> getResults() {
        return results;
    }

}
