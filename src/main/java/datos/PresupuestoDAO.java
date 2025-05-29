package datos;

import Modelos.Presupuesto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PresupuestoDAO {

    public static boolean insertar(Presupuesto p) {
        String sql = "INSERT INTO presupuesto (nombre, cantidad, precio) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getCantidad());
            ps.setDouble(3, p.getPrecio());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Presupuesto> listarTodos() {
        List<Presupuesto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, cantidad, precio FROM presupuesto";

        try (Connection conexion = ConexionBD.conectar();
             Statement stmt = conexion.createStatement();
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean actualizarPorID(Presupuesto p) {
        String sql = "UPDATE presupuesto SET nombre=?, cantidad=?, precio=? WHERE id=?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getCantidad());
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarPorID(int id) {
        String sql = "DELETE FROM presupuesto WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
