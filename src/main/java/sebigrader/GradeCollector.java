package sebigrader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class GradeCollector implements Consumer<TestResult> {

    private final TemplateCollector template;

    private final Map<GradeKey, Map<String, String>> results = new HashMap<>();

    public GradeCollector( TemplateCollector template ) {
        this.template = template;
    }

    @Override
    public void accept( TestResult t ) {
        template.lookupTaskId( t.getAspect() ).ifPresent( task -> {

            GradeKey gk = GradeKey.of(t);
            Map<String, String> aspects = results.computeIfAbsent( gk, k -> new HashMap<>() );
            aspects.put( t.getTestMode(), t.getPassFail() );
        } );
    }

    public Map<GradeKey, Map<String, String>> getResults() {
        return results;
    }
    
    
}
