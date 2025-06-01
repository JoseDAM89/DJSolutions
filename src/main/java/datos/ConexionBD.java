package datos;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBD {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();

            config.setJdbcUrl("jdbc:postgresql://ep-late-art-a2jopc25.eu-central-1.aws.neon.tech:5432/neondb?sslmode=require");
            config.setUsername("neondb_owner");
            config.setPassword("npg_LkV2h8vgPlfy");

            config.setMaximumPoolSize(20);
            config.setMinimumIdle(6);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);

            config.setConnectionInitSql("SET search_path TO public");

            dataSource = new HikariDataSource(config);
            System.out.println("✅ Pool de conexiones HikariCP inicializado");

        } catch (Exception e) {
            System.err.println("❌ Error inicializando pool HikariCP: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ConexionBD() {
        // Evitar instanciación
    }

    /**
     * Devuelve una conexión del pool.
     * Importante: el caller debe cerrar la conexión para devolverla al pool.
     * Aquí no se captura ni lanza SQLException para que sea coherente con tu DAO actual.
     */
    public static Connection getConexion() {
        if (dataSource == null) {
            throw new RuntimeException("El pool de conexiones no está inicializado.");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            // Aquí sí conviene lanzar RuntimeException para no forzar manejo checked en DAO
            throw new RuntimeException("No se pudo obtener conexión del pool", e);
        }
    }

    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("🛑 Pool de conexiones cerrado");
        }
    }
}
