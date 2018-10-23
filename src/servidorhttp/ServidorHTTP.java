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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try (ServerSocket socket = new ServerSocket(9000);) {

            while (true) {
                Socket cliente = socket.accept();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(cliente.getInputStream()));

                String input = reader.readLine();
                System.out.println(input);
                System.out.println(reader.readLine());
                System.out.println(reader.readLine());
                System.out.println(reader.readLine());
                System.out.println(reader.readLine());
                System.out.println(reader.readLine());
                System.out.println(reader.readLine());
                System.out.println(reader.readLine());
//                System.out.println(reader.readLine());
                System.out.println();

                if (input == null) {
                    continue;
                }

                String[] tokens = input.split(" ");
                String recurso = tokens[1];
                responder(cliente, recurso.substring(1));
            }
        } catch (IOException ex) {
            Logger.getLogger(
                    ServidorHTTP.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    private static void responder(Socket socket, String recurso) {

        if (isResource(recurso)) {
            File file = new File(recurso);
            if (!file.exists()) {
                pageNotFound(socket);
                return;
            }
            String[] archivo = recurso.split("\\.");
            String extension = archivo[archivo.length - 1];

            if (isTextFile(extension)) {
                sendWebPage(socket, recurso);
                return;

            } else if (isImage(extension)) {
                sendImage(socket, recurso);
                return;
            }
        } else { //si es GET de formulario
            //guardar properties en formato json 
            Properties p = extractFormValues(recurso);
            propiedades.add(p);
            saveAsJSON(propiedades);
            sendData(socket, p);
        }

    }

    public static String toJSON(Properties properties) {
        String result = "{";
        for (Entry set : properties.entrySet()) {
            result += "\"" + set.getKey() + "\":\"" + set.getValue() + "\", ";
        }
        result = result.substring(0, result.length() - 2) + "}";
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

    private static void pageNotFound(Socket socket) {
        File file = new File("notfound.html");
        try (FileReader fileReader = new FileReader(file);
                PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
            char[] html = new char[(int) file.length()];
            fileReader.read(html, 0, html.length);
            String header = "HTTP/1.1 404 Not Found\nContent-length: " + file.length() + "\nContent-type: text/html\n\n";
            String respuesta = header + String.valueOf(html);
            writer.write(respuesta);
            writer.flush();
        } catch (FileNotFoundException ex1) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (IOException ex1) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex1);
        }

    }

    public static boolean isResource(String recurso) {
        return !recurso.contains("?");
    }

    private static boolean isTextFile(String extension) {
        return (extension.equals("html") || extension.equals("css") || extension.equals("json") || extension.equals("js"));
    }

    private static boolean isImage(String extension) {
        return (extension.equals("png") || extension.equals("jpg"));
    }

    private static void sendWebPage(Socket socket, String recurso) {
        File file = new File(recurso);
        try (FileReader fileReader = new FileReader(file);
                PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
            char[] html = new char[(int) file.length()];
            fileReader.read(html, 0, html.length);
            String header = "HTTP/1.1 200 OK\nContent-length: " + file.length() + "\nContent-type: text/" + recurso.split("\\.")[1] + "\n\n";
            String respuesta = header + String.valueOf(html);
            writer.write(respuesta);
            writer.flush();
        } catch (FileNotFoundException ex1) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (IOException ex1) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }

    private static void sendImage(Socket socket, String recurso) {
        File file = new File(recurso);

        try (FileInputStream fileReader = new FileInputStream(file);
                ByteArrayOutputStream byteWriter = new ByteArrayOutputStream();
                PrintWriter stringWriter = new PrintWriter(socket.getOutputStream())) {

            byte[] imagen = new byte[(int) file.length()];
            fileReader.read(imagen, 0, imagen.length);
            String header = "HTTP/1.1 200 OK\nContent-length: " + file.length() + "\nContent-type: image/" + recurso.split("\\.")[1] + "\n\n";
            stringWriter.write(header);
            stringWriter.flush();
            byteWriter.write(imagen);
            byteWriter.writeTo(socket.getOutputStream());

        } catch (FileNotFoundException ex1) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex1);
        } catch (IOException ex1) {
            Logger.getLogger(ServidorHTTP.class.getName()).log(Level.SEVERE, null, ex1);
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
        props.replace("correo", ((String)props.get("correo")).replace("%40", "@"));
        return props;
    }

    public static String propertiesToHtml(Properties props) {
        String propsString = props.toString();
        String result = propsString.replace("{", "").replace("}", "<br>\n").replace(", ", "<br>\n");
        return result;
    }

}
