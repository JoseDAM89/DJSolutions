package modelos;

public class Sesion {
    private static String correoUsuario;
    private static boolean esAdmin;

    public static void iniciarSesion(String correo, boolean admin) {
        correoUsuario = correo;
        esAdmin = admin;
    }

    public static String getCorreoUsuario() {
        return correoUsuario;
    }

    public static boolean esAdmin() {
        return esAdmin;
    }

    public static void cerrarSesion() {
        correoUsuario = null;
        esAdmin = false;
    }
}
