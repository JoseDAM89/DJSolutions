package datos;

import modelos.LineaPresupuesto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineaPresupuestoDAO {

    // ✅ Insertar varias líneas asociadas a un presupuesto (usando conexión existente)
    public void insertarLineas(int idPresupuesto, List<LineaPresupuesto> lineas, Connection conn) throws SQLException {
        String sql = "INSERT INTO presupuesto_productos (idpresupuesto, idproducto, nombre, cantidad, precio) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (LineaPresupuesto linea : lineas) {
                ps.setInt(1, idPresupuesto);
                ps.setInt(2, linea.getIdProducto());
                ps.setString(3, linea.getNombreProducto());
                ps.setInt(4, linea.getCantidad());
                ps.setDouble(5, linea.getPrecioUnitario());
                ps.addBatch();
            }

            ps.executeBatch(); // Ejecutar todas juntas
        }
    }


    // ✅ Obtener las líneas de un presupuesto por ID
    public List<LineaPresupuesto> obtenerLineasPorPresupuesto(int idPresupuesto) {
        String sql = "SELECT idproducto, nombre, cantidad, precio FROM presupuesto_productos WHERE idpresupuesto = ?";
        List<LineaPresupuesto> lineas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPresupuesto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idProd = rs.getInt("idproducto");
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio");

                lineas.add(new LineaPresupuesto(idProd, nombre, cantidad, precio));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener líneas de presupuesto: " + e.getMessage());
        }

        return lineas;
    }
}
