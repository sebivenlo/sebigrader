package sebigrader;

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class GradeKey {
    private final String event;
    private final String stick;
    private final Integer task;

    private GradeKey( String event, String stick, Integer task ) {
        this.event = event;
        this.stick = stick;
        this.task = task;
    }

    
    static GradeKey of( TestResult tr){
        String e= tr.getEvent();
        String st =tr.getStick();
        Integer t = tr.getTask();
        return new GradeKey(e,st,t);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode( this.event );
        hash = 89 * hash + Objects.hashCode( this.stick );
        hash = 89 * hash + Objects.hashCode( this.task );
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
        final GradeKey other = (GradeKey) obj;
        if ( !Objects.equals( this.event, other.event ) ) {
            return false;
        }
        if ( !Objects.equals( this.stick, other.stick ) ) {
            return false;
        }
        if ( !Objects.equals( this.task, other.task ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GradeKey{" + "event=" + event + ", stick=" + stick + ", task=" + task + '}';
    }

    public String getEvent() {
        return event;
    }

    public String getStick() {
        return stick;
    }

    public Integer getTask() {
        return task;
    }
    
    
}
