package Correo;
// EnviarCorreo.java
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

public class EnviarCorreo {

    public static void enviarPresupuestoPorCorreo(String destino, File archivo) throws Exception {
        final String remitente = "djsolutionssa@gmail.com";
        final String contrasena = "dazi psjp mpvc ouhv";  // Usa una contraseña de aplicación

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
        mensaje.setSubject("Presupuesto generado");

        MimeBodyPart texto = new MimeBodyPart();
        texto.setText("Adjunto le enviamos su presupuesto generado.");

        MimeBodyPart adjunto = new MimeBodyPart();
        adjunto.attachFile(archivo);

        Multipart contenido = new MimeMultipart();
        contenido.addBodyPart(texto);
        contenido.addBodyPart(adjunto);

        mensaje.setContent(contenido);

        Transport.send(mensaje);
    }
}
