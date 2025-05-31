package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://ep-late-art-a2jopc25.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require";
    private static final String USUARIO = "neondb_owner";
    private static final String CONTRASENA = "npg_LkV2h8vgPlfy";

    public static Connection conectar() {
        try {
            System.out.println("Intentando conectar a: " + URL);
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

            // Establecer el esquema por seguridad
            try (Statement stmt = conexion.createStatement()) {
                stmt.execute("SET search_path TO public");
            }

            System.out.println("✅ Conexión exitosa a Neon.");
            return conexion;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a Neon: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
