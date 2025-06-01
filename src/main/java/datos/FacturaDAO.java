package datos;

import modelos.Factura;
import modelos.LineaFactura;

import java.sql.*;
import java.util.*;

public class FacturaDAO {

    private final LineaFacturaDAO lineaFacturaDAO = new LineaFacturaDAO();

    // ✅ Insertar una factura con sus líneas
    public int insertarFactura(Factura factura) {
        String sqlFactura = "INSERT INTO facturas (idcliente, fecha, pagada) VALUES (?, ?, ?) RETURNING idfactura";
        int idGenerado = -1;
        Connection conn = null;

        try {
            conn = ConexionBD.getConexion();
            conn.setAutoCommit(false); // Transacción

            // Insertar la cabecera
            try (PreparedStatement ps = conn.prepareStatement(sqlFactura)) {
                ps.setInt(1, factura.getIdCliente());
                ps.setTimestamp(2, Timestamp.valueOf(factura.getFecha()));
                ps.setBoolean(3, factura.isPagada());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                } else {
                    throw new SQLException("No se generó el ID de la factura.");
                }
            }

            // Insertar líneas
            lineaFacturaDAO.insertarLineas(idGenerado, factura.getLineas(), conn);


            conn.commit(); // Confirmar
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar la factura: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("ℹ️ Transacción revertida.");
                } catch (SQLException ex) {
                    System.err.println("❌ Error en rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
                }
            }
        }

        return idGenerado;
    }

    // ✅ Obtener factura completa por ID
    public Factura obtenerFacturaPorID(int idFactura) {
        String sql = "SELECT * FROM facturas WHERE idfactura = ?";
        Factura factura = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFactura);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idCliente = rs.getInt("idcliente");
                Timestamp fecha = rs.getTimestamp("fecha");
                boolean pagada = rs.getBoolean("pagada");

                List<LineaFactura> lineas = lineaFacturaDAO.obtenerLineasPorFactura(idFactura);
                factura = new Factura(idFactura, idCliente, fecha.toLocalDateTime(), lineas, pagada);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al recuperar la factura: " + e.getMessage());
        }

        return factura;
    }

    // ✅ Listar todas las facturas con sus líneas
    public List<Factura> listarTodas() {
        String sql = "SELECT * FROM facturas ORDER BY fecha DESC";
        List<Factura> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idfactura");
                int idCliente = rs.getInt("idcliente");
                Timestamp fecha = rs.getTimestamp("fecha");
                boolean pagada = rs.getBoolean("pagada");

                List<LineaFactura> lineas = lineaFacturaDAO.obtenerLineasPorFactura(id);
                Factura f = new Factura(id, idCliente, fecha.toLocalDateTime(), lineas, pagada);
                lista.add(f);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar facturas: " + e.getMessage());
        }

        return lista;
    }

    // ✅ Actualizar estado pagada
    public void actualizarEstadoPagada(int idFactura, boolean pagada) {
        String sql = "UPDATE facturas SET pagada = ? WHERE idfactura = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, pagada);
            ps.setInt(2, idFactura);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar estado: " + e.getMessage());
        }
    }
}
