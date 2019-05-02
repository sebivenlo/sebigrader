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
public class TestFileVisitorTest extends CollectorsTestBase {

    //@Ignore( "Think TDD" )
    @Test
    public void findTests() {

        final Set<String> testMethods = new HashSet<>();

        String d = "testreports/sw";
        Path p = Paths.get( d );//, "TEST-administration.PersonTest.xml" );
        Consumer<TestResult> cons = gr -> testMethods.add( gr.getTestmethod() );
        TestReportHandler han = new TestReportHandler( p, cons );

        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );
        assertEquals( "expect 19", 19, testMethods.size() );
    }

    //@Ignore( "Think TDD" )
    @Test
    public void findGrades() {

        final List<TestResult> testResults = new ArrayList<>();
        String d = "testreports/examsolution";
        Path p = Paths.get( d );//, "TEST-administration.PersonTest.xml" );
        Consumer<TestResult> cons = g -> testResults.add( g );
        TestReportHandler han = new TestReportHandler( p, cons );

        TestFileVisitor vst = new TestFileVisitor( ( Path path ) -> true, han );
        walk( p, vst );
        assertEquals( 6, testResults.size() );
//        Assert.fail( "method findGrades reached end. You know what to do." );
    }
}
