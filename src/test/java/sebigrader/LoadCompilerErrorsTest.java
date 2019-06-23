/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sebigrader;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class LoadCompilerErrorsTest {

//    @Ignore( "Think TDD" )
    @Test
    public void testMain() throws IOException {
        LoadCompilerErrors.main( new String[] { "compiler-out-all.txt" } );
//        Assert.fail( "method method reached end. You know what to do." );
        
    }
    
}
