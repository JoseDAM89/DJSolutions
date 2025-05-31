package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://ep-late-art-a2jopc25.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require";
    private static final String USUARIO = "neondb_owner";
    private static final String CONTRASENA = "npg_LkV2h8vgPlfy";

    private static Connection conexion = null;

    private ConexionBD() {} // Evitar instanciación

    public static Connection getConexion() {
        try {
            if (conexion == null) {
                System.out.println("🔁 Conexión es null");
            } else if (conexion.isClosed()) {
                System.out.println("🔁 Conexión estaba cerrada");
            } else {
                System.out.println("✅ Reutilizando conexión existente");
            }

            if (conexion == null || conexion.isClosed()) {
                System.out.println("⏳ Estableciendo nueva conexión a Neon...");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

                try (Statement stmt = conexion.createStatement()) {
                    stmt.execute("SET search_path TO public");
                }

                System.out.println("✅ Conexión persistente establecida.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a Neon: " + e.getMessage());
            e.printStackTrace();
        }

        return conexion;
    }

}
