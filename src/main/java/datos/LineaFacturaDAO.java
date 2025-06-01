package datos;

import modelos.LineaFactura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LineaFacturaDAO {

    // ✅ Insertar líneas asociadas a una factura existente
    public void insertarLineas(int idFactura, List<LineaFactura> lineas, Connection conn) throws SQLException {
        String sql = "INSERT INTO factura_productos (idfactura, idproducto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (LineaFactura l : lineas) {
                ps.setInt(1, idFactura);
                ps.setInt(2, l.getIdProducto());
                ps.setInt(3, l.getCantidad());
                ps.setDouble(4, l.getPrecioUnitario());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    // ✅ Obtener todas las líneas de una factura específica
    public List<LineaFactura> obtenerLineasPorFactura(int idFactura) {
        List<LineaFactura> lineas = new ArrayList<>();

        String sql = """
            SELECT fp.idproducto, p.nombreproduct, fp.cantidad, fp.precio_unitario
            FROM factura_productos fp
            JOIN productos p ON fp.idproducto = p.codproduct
            WHERE fp.idfactura = ?
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idProducto = rs.getInt("idproducto");
                String nombre = rs.getString("nombreproduct");
                int cantidad = rs.getInt("cantidad");
                double precio = rs.getDouble("precio_unitario");

                lineas.add(new LineaFactura(idProducto, nombre, cantidad, precio));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al recuperar líneas de factura: " + e.getMessage());
        }

        return lineas;
    }
}
