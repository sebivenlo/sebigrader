package sebigrader;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import static sebigrader.MakeGraderTemplate.resultsDir;
import static sebigrader.Settings.SETTINGS;

/**
 *
 * @author hom
 */
public class Main {
        public static void main( String[] args ) throws IOException {

        try ( PrintStream out = new PrintStream( "correction.xml" ); ) {
            Path startingDir = Paths.get( SETTINGS.get( resultsDir ) );
            //TestReportVisitor visitor= new TestReportVisitor(new TestReportHandler());
            MakeGraderTemplate grader
                    = new MakeGraderTemplate( startingDir, out );
            grader.run();
        }
    }

}
