package sebigrader;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Predicate;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import static sebigrader.Settings.SETTINGS;

/**
 *
 * @author Pieter van den Hombergh (homberghp)
 * {@code pieter.van.den.hombergh@gmail.com}
 */
class TestFileVisitor implements FileVisitor<Path> {

    private final Predicate<Path> acceptDir;
    private final TestReportHandler handler;

    public TestFileVisitor( Predicate<Path> acceptDir,  TestReportHandler handler ) {
        this.acceptDir = acceptDir;
        this.handler = handler;
    }

    @Override
    public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException {
        if ( acceptDir.test( dir ) ) {
            return CONTINUE;
        } else {
            return FileVisitResult.SKIP_SUBTREE;
        }
    }

    private void doFile( Path p ) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse( p.toFile(), handler );

        } catch ( ParserConfigurationException | SAXException | IOException e ) {
            Logger.getGlobal().fine( e.getMessage() );
        }
    }

    @Override
    public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
        String fileName = file.getFileName().toString();
        boolean acceptableFile = SETTINGS.TEST_RESULT_FILE_PATTERN.matcher( fileName ).matches();
        if ( acceptableFile ) {
            Path aPath = file.toAbsolutePath();
            handler.setReportPath( aPath );
            doFile( aPath );
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed( Path file, IOException exc ) throws IOException {
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory( Path dir, IOException exc ) throws IOException {
        return CONTINUE;
    }

}
