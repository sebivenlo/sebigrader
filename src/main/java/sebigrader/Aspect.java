package sebigrader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
class Aspect {
    
    private final static Set<Aspect> idMap = new HashSet<>();
    private final static AtomicInteger nextId = new AtomicInteger();
   
    final Integer task;
    final String project;
    final String testMethod;
    
    private Aspect( Integer task, String project, String testMethod ) {
        this.task=task;
        this.project = project;
        this.testMethod = testMethod;
    }
    
    static Aspect of( String project, String testmethod ) {
        Optional<Aspect> findAny = idMap.stream().filter( a -> ( a.project.equals( project ) && a.testMethod.equals( testmethod ) ) ).findAny();
        
        if ( findAny.isPresent() ) {
            return findAny.get();
        }
        
        Aspect na = new Aspect(nextId.incrementAndGet(),project,testmethod);
            idMap.add( na );
        return na;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode( this.project );
        hash = 37 * hash + Objects.hashCode( this.testMethod );
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
        final Aspect other = (Aspect) obj;
        if ( !Objects.equals( this.project, other.project ) ) {
            return false;
        }
        return Objects.equals( this.testMethod, other.testMethod );
    }

    public Integer getTask() {
        return task;
    }

    public String getProject() {
        return project;
    }

    public String getTestMethod() {
        return testMethod;
    }

     static String q = "\"";
    static String qc = "\",";
    static String qcq = "\",\"";
    static String cq = ",\"";

    public static final String CSVHEADER = "task,project,test_method";

    @Override
    public String toString() {
        return task + cq+  project + qcq+ testMethod + '"';
    }
    
    
}
