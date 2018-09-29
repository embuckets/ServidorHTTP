/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorhttp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alumno
 */
public class ServidorHTTP {

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
        File file = new File(recurso);
        if (!file.exists()) {
            pageNotFound(socket);
            return;
        }
        String[] archivo = recurso.split("\\.");
        String extension = archivo[archivo.length - 1];

        if (isWebPage(extension)) {
            sendWebPage(socket, recurso);
            return;

        } else if (isImage(extension)) {
            sendImage(socket, recurso);
            return;
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

    private static boolean isWebPage(String extension) {
        return extension.equals("html");
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
            String header = "HTTP/1.1 200 OK\nContent-length: " + file.length() + "\nContent-type: text/html\n\n";
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

}
