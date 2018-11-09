/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorhttp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alumno
 */
public class ServidorHTTP {

    private static List<Properties> propiedades = new ArrayList<>();
    private static MIMEType mimeTypes = new MIMEType();
    private final String WEB_PAGES_DIR = "web/";
    private final String IMAGES_DIR = "web/images/";
    private final String SCRIPTS_DIR = "web/scripts/";
    private final String STYLES_DIR = "web/styles/";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try (ServerSocket socket = new ServerSocket(9000);) {

            while (true) {
                Socket cliente = socket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                String input = reader.readLine();
                List<String> request = new ArrayList<>();
                while (reader.ready()) {
                    request.add(reader.readLine());
                }
                System.out.println(input);
                request.stream().forEach((l) -> System.out.println(l));
                System.out.println();
                request.clear();

                if (input == null) {
                    continue;
                }
                String[] tokens = input.split(" ");
                String recurso = tokens[1];
                responder(cliente, recurso.substring(1));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void responder(Socket socket, String recurso) {

        if (isRecurso(recurso)) {
            File file = new File(recurso);
            if (!file.exists()) {
                sendResponse(socket, "web/notfound.html", "404");
                return;
            } else {
                sendResponse(socket, recurso, "200");
            }
        } else { //si es GET de formulario
            //guardar properties en formato json 
            Properties p = extractFormValues(recurso);
            propiedades.add(p);
            saveAsJSON(propiedades);
            sendData(socket, p);
        }

    }

    public static boolean isRecurso(String recurso) {
        return !recurso.contains("?");
    }

    private static void sendResponse(Socket socket, String recurso, String code) {
        try (ByteArrayOutputStream byteWriter = new ByteArrayOutputStream();
                PrintWriter stringWriter = new PrintWriter(socket.getOutputStream())) {
            File file = new File(recurso);
            String charContents = null;
            byte[] byteContents = null;
            String header = makeHeader(file, recurso, code);
            if (mimeTypes.isCharEncoded('.' + recurso.split("\\.")[1])) {
                charContents = readAsString(recurso);
                stringWriter.write(header + charContents);
                stringWriter.flush();
            } else {
                byteContents = readAsByte(recurso);
                stringWriter.write(header);
                stringWriter.flush();
                byteWriter.write(byteContents);
                byteWriter.writeTo(socket.getOutputStream());
                byteWriter.flush();
            }
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
    }

    private static String makeHeader(File file, String resource, String code) {
        String result = "HTTP/1.1 ";
        result += (code.equals("200") ? "200 OK" : "404 Not Found");
        result += "\nContent-length: " + file.length();
        result += "\nContent-type: " + mimeTypes.getMIMEType('.' + resource.split("\\.")[1]);
        result += "\n\n";
        return result;
    }

    private static byte[] readAsByte(String resource) {
        File file = new File(resource);
        byte[] bytes = null;
        try (FileInputStream fileReader = new FileInputStream(file);) {
            bytes = new byte[(int) file.length()];
            fileReader.read(bytes, 0, bytes.length);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    private static String readAsString(String resource) {
        File file = new File(resource);
        char[] contents = null;
        String result = null;
        try (FileReader fileReader = new FileReader(file);) {
            contents = new char[(int) file.length()];
            fileReader.read(contents, 0, contents.length);
            result = String.valueOf(contents);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String toJSON(Properties properties) {
        String result = "{";
        for (Entry set : properties.entrySet()) {
            result += "\"" + set.getKey() + "\":\"" + set.getValue() + "\", ";
        }
        result = result.substring(0, result.length() - 2) + "}";
        return result;
    }

    public static String toXML(Properties properties) {
        String indent = "    ";
        String result = "<PACIENTE>\n";
        for (Entry entry : properties.entrySet()) {
            result += indent + indent + "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">\n";
        }
        result += indent + "</PACIENTE>\n";

        return result;
    }

    public static void saveAsJSON(List<Properties> properties) {
        File json = new File("web/pacientes/pacientes.json");
        try (FileWriter writer = new FileWriter(json)) {
            writer.write("{\n\"pacientes\":[\n");
            for (Iterator it = properties.iterator(); it.hasNext();) {
                Properties prop = (Properties) it.next();
                writer.write(toJSON(prop));
                if (it.hasNext()) {
                    writer.write(",\n");
                }
            }
            writer.write("\n]\n}");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveAsXML(List<Properties> properties) {
        String indent = "    ";
        File json = new File("web/pacientes/pacientes.xml");
        try (FileWriter writer = new FileWriter(json)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<PACIENTES>\n");
            for (Properties prop : properties) {
                writer.write(indent + toXML(prop));
            }
            writer.write("</PACIENTES>");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void sendData(Socket socket, Properties data) {
        String beginning = "<!DOCTYPE html>\n"
                + "<html lang=\"es\">\n"
                + "\n"
                + "<head>\n"
                + "<title>Mi Cheff</title>\n"
                + "<meta charset=\"utf-8\">\n"
                + "</head>\n"
                + "\n"
                + "<body><h1>Datos</h1>\n";
        String body = propertiesToHtml(data);
        String end = "</body>\n"
                + "</html>";

        String fullPage = beginning + body + end;
        String header = "HTTP/1.1 200 OK\nContent-length: " + fullPage.length() + "\nContent-type: text/html\n\n";
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
            writer.write(header);
            writer.write(fullPage);
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Properties extractFormValues(String data) {
        Properties props = new Properties();
        String keyValues = data.split("\\?")[1];
        String[] keyValuePairs = keyValues.split("&");
        for (String kvp : keyValuePairs) {
            String[] kv = kvp.split("=");
            props.put(kv[0], kv[1]);
        }
        props.remove("submit");
        props.replace("correo", ((String) props.get("correo")).replace("%40", "@"));
        return props;
    }

    public static String propertiesToHtml(Properties props) {
        String propsString = props.toString();
        String result = propsString.replace("{", "").replace("}", "<br>\n").replace(", ", "<br>\n");
        return result;
    }

}

