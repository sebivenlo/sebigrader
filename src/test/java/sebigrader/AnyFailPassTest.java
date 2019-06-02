/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sebigrader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static sebigrader.AnyFailPassTest.M.m;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
@RunWith( Parameterized.class )
public class AnyFailPassTest {

    static Map<String, Predicate<Map<String, String>>> predMap = new HashMap<>();

    static {
        predMap.put( "anyTestFail", GradeRecord::anyTestFail );
        predMap.put( "trivialRed", GradeRecord::trivialRed );
        predMap.put( "trivialGreen", GradeRecord::trivialGreen );
        predMap.put("studentCodePasses", GradeRecord::studentCodePasses);
        predMap.put("refCodePassesStudent", GradeRecord::refCodePassesStudent);
        predMap.put("passBB", GradeRecord::passBB);
    }

    @Parameters( name = "{index}: {0}" )

    public static Object[] data() {
        return new Object[] {
            m( true, "anyTestFail", "BA", "F", "B0", "F", "B1", "F" ),
            m( true, "anyTestFail", "BA", "P", "BB", "P", "B0", "F", "B1", "P" ),
            m( true, "trivialRed", "BA", "F", "B0", "F", "B1", "F" ),
            m( true, "trivialRed", "BA", "P", "B0", "F", "B1", "F" ),
            m( true, "trivialGreen", "BA", "P", "B0", "P", "B1", "P" ),
            m( false, "trivialGreen", "BB", "P", "B0", "E" ),
            m( true, "trivialGreen", "BB", "P", "B1", "P" ),
            m( false, "trivialGreen", "BB", "P", "B0", "F", "B1", "P" ),
            m( true, "studentCodePasses", "AB", "P" ),
            m( false, "studentCodePasses", "AB", "F" ),
            m( false, "refCodePassesStudent", "AB", "P","BA", "F", "B0", "P", "B1", "P"),
            m( false, "refCodePassesStudent", "AB", "F","BA", "F", "B0", "P", "B1", "P"),
            m( true, "refCodePassesStudent", "AB", "P","BA", "P", "B0", "P", "B1", "P"),
            m( true, "refCodePassesStudent", "AB", "F","BA", "P", "B0", "P", "B1", "P"),
            m( true, "passBB", "BB","P","AB", "F","BA", "P", "B0", "P", "B1", "P"),
            m( false, "passBB", "BB","F","AB", "F","BA", "P", "B0", "P", "B1", "P"),
            
        };
    }
    final M m;

    public AnyFailPassTest( M m ) {
        this.m = m;
    }

//    @Ignore( "Think TDD" )
    @Test
    public void method() {
        System.out.println( "m = " + m );
        assertEquals( m.expected, predMap.get( m.pred ).test( m.testModes ) );
        // Assert.fail( "method method reached end. You know what to do." );
    }

    static Map<String, String> makeMap( String... pairs ) {
        Map<String, String> result = new LinkedHashMap<>();
        for ( int i = 0; i < pairs.length; i += 2 ) {
            result.put( pairs[ i + 0 ], pairs[ i + 1 ] );
        }
        return result;
    }

    static class M {

        boolean expected;
        final String pred;
        final Map<String, String> testModes;

        M( boolean expected, String pred, String... pairs ) {
            this.expected = expected;
            this.pred = pred;
            testModes = makeMap( pairs );
        }

        static M m( boolean expected, String pred, String... pairs ) {
            return new M( expected, pred, pairs );
        }

        @Override
        public String toString() {
            return "M{" + expected + " " + pred + ", testModes=" + testModes + '}';
        }

    }
}
