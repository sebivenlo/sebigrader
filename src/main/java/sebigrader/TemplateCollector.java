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
public class TemplateCollector implements Consumer<TestResult> {

    private final Map<Aspect, Integer> taskLookup = new LinkedHashMap<>();

    @Override
    public void accept( TestResult t ) {
        taskLookup.put( t.getAspect(), t.getTask() );
    }

    Optional<Integer> lookupTaskId( Aspect aspect ) {
        return Optional.of( aspect.task );
    }

    int size() {
        return taskLookup.size();
    }

    @Override
    public String toString() {

        return "Template:{\n" + taskLookup.keySet().stream().map( Aspect::toString ).collect( joining( "\n\t" ) ) + "}\n";
    }

    Map<Aspect, Integer> lookupMap() {
        return Collections.unmodifiableMap( taskLookup );
    }
}
