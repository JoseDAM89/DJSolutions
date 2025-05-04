package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://localhost:5432/BaseDeDatosTFG";
    private static final String USUARIO = "postgres";
    private static final String CONTRASENA = "12345"; // Contraseña de PostgreSQL

    // Método para obtener conexión
    public static Connection conectar() {
        Connection conexion = null;
        try {

            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("Conexión exitosa a PostgreSQL.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a PostgreSQL: " + e.getMessage());
        }
        return conexion;
    }


}
