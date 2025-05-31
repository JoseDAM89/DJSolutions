package Correo;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

public class EnviarCorreo {

    public static void enviarArchivoPorCorreo(String destino, File archivo, String asunto, String mensajeTexto) throws Exception {
        final String remitente = "djsolutionssa@gmail.com";
        final String contrasena = "dazi psjp mpvc ouhv"; // Contraseña de aplicación

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session sesion = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, contrasena);
            }
        });

        Message mensaje = new MimeMessage(sesion);
        mensaje.setFrom(new InternetAddress(remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
        mensaje.setSubject(asunto);

        MimeBodyPart cuerpoTexto = new MimeBodyPart();
        cuerpoTexto.setText(mensajeTexto);

        MimeBodyPart cuerpoAdjunto = new MimeBodyPart();
        cuerpoAdjunto.attachFile(archivo);

        Multipart contenido = new MimeMultipart();
        contenido.addBodyPart(cuerpoTexto);
        contenido.addBodyPart(cuerpoAdjunto);

        mensaje.setContent(contenido);
        Transport.send(mensaje);
    }
}
