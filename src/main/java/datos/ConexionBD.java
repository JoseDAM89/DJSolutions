package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://ep-late-art-a2jopc25-pooler.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require";
    private static final String USUARIO = "neondb_owner";
    private static final String CONTRASENA = "npg_LkV2h8vgPlfy"; // cópiala desde Neon (con el ojito)

    public static Connection conectar() {
        try {
            System.out.println("Intentando conectar a: " + URL);
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("✅ Conexión exitosa a Neon.");
            return conexion;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a Neon: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
