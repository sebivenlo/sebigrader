package sebigrader;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import static java.util.stream.Collectors.joining;

/**
 * Collects the grade records from the exam solution as a correction reference.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class TemplateCollector implements Consumer<GradeRecord> {

    private final Map<String, Integer> taskLookup = new LinkedHashMap<>();
    private static final AtomicInteger taskSerial = new AtomicInteger();

    @Override
    public void accept( GradeRecord t ) {
        taskLookup.put( taskMethodKey( t ), taskSerial.incrementAndGet() );
    }

    static String taskMethodKey( GradeRecord t ) {
        String project = t.getProject();
        String testmethod = t.getTestmethod();
        return project + ':' + testmethod;
    }

    Optional<Integer> lookupTaskId( GradeRecord t ) {
        String taskMethodKey = taskMethodKey( t );
        Integer result = taskLookup.get( taskMethodKey );
        return Optional.ofNullable( result );
    }

    int size() {
        return taskLookup.size();
    }

    @Override
    public String toString() {

        return "Template:{\n" + taskLookup.keySet().stream().collect( joining( "\n\t" ) ) + "}\n";
    }

    Map<String, Integer> lookupMap() {
        return Collections.unmodifiableMap( taskLookup );
    }
}
