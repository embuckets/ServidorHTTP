/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorhttp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author emilio
 */
public class ServidorHTTPTest {
    
    public ServidorHTTPTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class ServidorHTTP.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ServidorHTTP.main(args);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
    @Test
    public void testExtension(){
        String archivo =  "/image.png";
        String[] tokens = archivo.split("\\.");
        String extension = tokens[tokens.length-1];
        System.out.println(extension);
        System.out.println(archivo.substring(1, archivo.length()));
        System.out.println(archivo.substring(1));
        System.out.println(archivo.split("\\.")[1]);
        
    }
}
