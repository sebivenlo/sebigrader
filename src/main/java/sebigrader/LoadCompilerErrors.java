package sebigrader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LoadCompilerErrors {

    // pattern has groups 1=exam nr, 2=project 3=task, 4= mode 5 = main or test 6=filename 7=line 8=com 9 = msg
    static final Pattern COMPILER_ERROR_PATTERN = Pattern.compile( "^\\[ERROR\\].*?EXAM(\\d{3})"
            + "-(\\w+)?-(\\w{2})/src/((main|test))/java/?(.+)?:\\[(\\d+),(\\d+)\\]\\s(.*)$" );

    public static void main( String[] args ) throws IOException {
        if ( args.length < 1 ) {
            System.out.println( "usage: ... filename" );
            System.exit( 1 );
        }
        new LoadCompilerErrors( "PUK" ).loadErrors( args[ 0 ], ( e ) -> {
            System.out.println( "e = " + e );
        } );

    }

    void loadErrors( String fn, Consumer<? super TestResult> collector ) throws IOException {

        Files.lines( Paths.get( fn ) )
                .map( l -> COMPILER_ERROR_PATTERN.matcher( l ) )
                .filter( Matcher::matches )
                .map( m -> lineToTestResult( m ) )
                .forEach( collector::accept );
    }

    final String event;

    public LoadCompilerErrors( String event ) {
        this.event = event;
    }

    TestResult lineToTestResult( Matcher m ) {
        TestResult result = null;
        String stick, project, task, mode, mainOrTest, jfile, line, col, msg;
//        Matcher m = COMPILER_ERROR_PATTERN.matcher( l );
        if ( m.matches() ) {
            stick = m.group( 1 );
            project = m.group( 2 );
            task = m.group( 3 );
            mode = m.group( 4 );
            mainOrTest = m.group( 5 );
            jfile = m.group( 6 );
            line = m.group( 7 );
            col = m.group( 8 );
            msg = m.group( 9 );
            System.out.print( "stick=" + stick );
//            System.out.print( ",project=" + project );
//            System.out.print( ",task=" + task );
//            System.out.print( ",mode=" + mode );
//            System.out.print( "mainOrTest=" + mainOrTest );
//            System.out.print( ",jfile=" + jfile );
//            System.out.print( ",line=" + line );
//            System.out.print( ",col=" + col );
//            System.out.println( ",msg=" + msg );
            String passFail = ( mainOrTest.equals( "test" ) ) ? "t" : "c";
            result = TestResult.forCompilerError( event, stick, project, 
                    jfile.replace( "/", ".").replace( ".java", ""), mode, passFail );
        }
        return result;
    }

}
