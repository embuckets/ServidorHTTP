/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorhttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
    private List<Properties> props;
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
        props = new ArrayList<>();
        Properties property = new Properties();
        property.put("nombre", "emilio");
        property.put("paterno", "hernandez");
        property.put("materno", "segovia");
        props.add(property);
        property = new Properties();
        property.put("nombre", "daniel");
        property.put("paterno", "gomez");
        property.put("materno", "chagoya");
        props.add(property);
        
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
    public void testToJSON() {
        Properties props = new Properties();
        props.put("nombre", "emilio");
        props.put("paterno", "hernandez");
        props.put("materno", "segovia");
        System.out.println(ServidorHTTP.toJSON(props));
    }
    
    @Test
    public void testSaveAsJSON() {
        ServidorHTTP.saveAsJSON(props);
    }

    @Test
    public void testExtension() {
        String archivo = "/image.png";
        String[] tokens = archivo.split("\\.");
        String extension = tokens[tokens.length - 1];
        System.out.println(extension);
        System.out.println(archivo.substring(1, archivo.length()));
        System.out.println(archivo.substring(1));
        System.out.println(archivo.split("\\.")[1]);

    }

    @Test
    public void testExtractFormValues() {
        Properties expectedProperties = new Properties();
        expectedProperties.put("nombre", "Emilio");
        expectedProperties.put("paterno", "Hernandez");
        expectedProperties.put("materno", "Segovia");
        expectedProperties.put("peso", "64");
        expectedProperties.put("estatura", "1.72");
        expectedProperties.put("cintura", "65");
        expectedProperties.put("cadera", "60");
        expectedProperties.put("correo", "mok.boss%40hotmail.com");
        expectedProperties.put("telefono", "55-21-19-55-14");
        expectedProperties.put("sexo", "h");
        expectedProperties.put("submit", "Enviar");

        String data = "/web/paciente.html?nombre=Emilio&paterno=Hernandez&materno=Segovia&peso=64&estatura=1.72&cintura=65&cadera=60&correo=mok.boss%40hotmail.com&telefono=55-21-19-55-14&sexo=h&submit=Enviar";
        Properties result = servidorhttp.ServidorHTTP.extractFormValues(data);
        assertEquals(expectedProperties, result);
        System.out.println(result);

    }

    @Test
    public void testpropertiesToHtml() {
        Properties expectedProperties = new Properties();
        expectedProperties.put("nombre", "Emilio");
        expectedProperties.put("paterno", "Hernandez");
        expectedProperties.put("materno", "Segovia");
        expectedProperties.put("peso", "64");
        expectedProperties.put("estatura", "1.72");
        expectedProperties.put("cintura", "65");
        expectedProperties.put("cadera", "60");
        expectedProperties.put("correo", "mok.boss%40hotmail.com");
        expectedProperties.put("telefono", "55-21-19-55-14");
        expectedProperties.put("sexo", "h");
        expectedProperties.put("submit", "Enviar");

        System.out.println(servidorhttp.ServidorHTTP.propertiesToHtml(expectedProperties));
    }

    @Test
    public void testIsResource() {
        String getForm = "/web/paciente.html?nombre=Emilio&paterno=Hernandez&materno=Segovia&peso=64&estatura=1.72&cintura=65&cadera=60&correo=mok.boss%40hotmail.com&telefono=55-21-19-55-14&sexo=h&submit=Enviar";
        String getResource = "GET /web/paciente.html HTTP/1.1";
        System.out.println(getForm + " = " + servidorhttp.ServidorHTTP.isResource(getForm));
        System.out.println(getResource + " = " + servidorhttp.ServidorHTTP.isResource(getResource));

    }
}
