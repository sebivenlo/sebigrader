package sebigrader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh (homberghp)
 * {@code pieter.van.den.hombergh@gmail.com}
 */
public class TestFileVisitorTest {

    @Test
    public void firstVisit() {
        String d = "testreports";
        Path p = Paths.get( d );//, "TEST-administration.PersonTest.xml" );
        Consumer<GradeRecord> cons = System.out::println;
        TestReportHandler han = new TestReportHandler( p, cons );

        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );

        walk( p, vst );
    }

    private void walk( Path p, TestFileVisitor vst ) {
        try {
            Files.walkFileTree( p.toAbsolutePath(), vst );

        } catch ( IOException ex ) {
            Logger.getLogger( MakeGraderTemplate.class.getName() ).log(
                    Level.SEVERE, null, ex );
        }

//        Assert.fail( "method method reached end. You know what to do." );
    }

    //@Ignore( "Think TDD" )
    @Test
    public void findTests() {

        final Set<String> testMethods = new HashSet<>();

        String d = "testreports";
        Path p = Paths.get( d );//, "TEST-administration.PersonTest.xml" );
        Consumer<GradeRecord> cons = gr -> testMethods.add( gr.getTestmethod() );
        TestReportHandler han = new TestReportHandler( p, cons );

        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );

//        testMethods.forEach( System.out::println );

        assertEquals( "expect 19", 19, testMethods.size() );
//        Assert.fail( "method findTests reached end. You know what to do." );
    }

    //@Ignore( "Think TDD" )
    @Test
    public void findGrades() {

        final List<GradeRecord> gradeRecords = new ArrayList<>();
        String d = "testreports";
        Path p = Paths.get( d );//, "TEST-administration.PersonTest.xml" );
        Consumer<GradeRecord> cons = g -> gradeRecords.add( g );
        TestReportHandler han = new TestReportHandler( p, cons );

        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );

        gradeRecords.forEach( System.out::println );        
        assertEquals( 19, gradeRecords.size() );
//        Assert.fail( "method findGrades reached end. You know what to do." );
    }
}
