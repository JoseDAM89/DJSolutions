package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:postgresql://localhost:5433/EmpresaPrueba1";
    private static final String USUARIO = "postgres";
    private static final String CONTRASENA = "123456"; // Contraseña de PostgreSQL

    // Método para obtener conexión
    public static Connection conectar() {
        Connection conexion = null;
        try {
            System.out.println("Intentando conectar a: " + URL);
            conexion = DriverManager.getConnection(URL.trim(), USUARIO.trim(), CONTRASENA.trim());
            System.out.println("Conexión exitosa a PostgreSQL.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a PostgreSQL: " + e.getMessage());
            e.printStackTrace();  // 👈 Esto te da más detalle
        }
        return conexion;
    }



}
