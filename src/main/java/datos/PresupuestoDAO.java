package datos;

import Modelos.Presupuesto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoDAO {

    private Connection conexion;

    public PresupuestoDAO() {
        this.conexion = ConexionBD.conectar();
        if (this.conexion == null) {
            throw new RuntimeException("No se pudo establecer la conexión a la base de datos");
        }
    }

    public void guardarPresupuesto(Presupuesto p) throws SQLException {
        String sql = "INSERT INTO presupuesto (nombre, cantidad, precio) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Ojo que no incluyo ID, asumo auto-increment en la base de datos
            ps.setString(1, p.nombre);
            ps.setInt(2, p.cantidad);
            ps.setDouble(3, p.precio);
            ps.executeUpdate();
        }
    }

    public List<Presupuesto> obtenerTodos() throws SQLException {
        List<Presupuesto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, cantidad, precio FROM presupuesto";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Presupuesto p = new Presupuesto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    // Aquí podrías agregar métodos para actualizar o eliminar si los necesitas
}
