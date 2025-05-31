package datos;

import modelos.Factura;
import modelos.LineaFactura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    public int insertarFactura(Factura factura) {
        String sqlFactura = "INSERT INTO facturas (idcliente, fecha) VALUES (?, ?) RETURNING idfactura";
        String sqlLinea = "INSERT INTO factura_productos (idfactura, idproducto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        int idGenerado = -1;

        Connection conn = null;

        try {
            conn = ConexionBD.getConexion();
            conn.setAutoCommit(false); // Iniciar transacción

            // Insertar la cabecera de la factura
            try (PreparedStatement psFactura = conn.prepareStatement(sqlFactura)) {
                psFactura.setInt(1, factura.getIdCliente());
                psFactura.setTimestamp(2, Timestamp.valueOf(factura.getFecha()));
                ResultSet rs = psFactura.executeQuery();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                } else {
                    throw new SQLException("No se generó el ID de la factura.");
                }
            }

            // Insertar las líneas de la factura
            try (PreparedStatement psLinea = conn.prepareStatement(sqlLinea)) {
                for (LineaFactura linea : factura.getLineas()) {
                    psLinea.setInt(1, idGenerado);
                    psLinea.setInt(2, linea.getIdProducto());
                    psLinea.setInt(3, linea.getCantidad());
                    psLinea.setDouble(4, linea.getPrecioUnitario());
                    psLinea.addBatch();
                }
                psLinea.executeBatch();
            }

            conn.commit(); // Confirmar cambios
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar la factura:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   SQLState: " + e.getSQLState());
            System.err.println("   Código: " + e.getErrorCode());

            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("ℹ️ Transacción revertida.");
                } catch (SQLException rollbackEx) {
                    System.err.println("❌ Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }

        return idGenerado;
    }

    public Factura obtenerFacturaPorID(int idFactura) {
        String sqlFactura = "SELECT * FROM facturas WHERE idfactura = ?";
        String sqlLineas = """
            SELECT fp.idproducto, p.descripcionproduct, fp.cantidad, fp.precio_unitario
            FROM factura_productos fp
            JOIN productos p ON fp.idproducto = p.codproduct
            WHERE fp.idfactura = ?
        """;

        Factura factura = null;
        Connection conn = ConexionBD.getConexion();
        try ( PreparedStatement psFactura = conn.prepareStatement(sqlFactura);
             PreparedStatement psLineas = conn.prepareStatement(sqlLineas)) {

            psFactura.setInt(1, idFactura);
            ResultSet rsFactura = psFactura.executeQuery();

            if (rsFactura.next()) {
                int idCliente = rsFactura.getInt("idcliente");
                Timestamp fecha = rsFactura.getTimestamp("fecha");

                List<LineaFactura> lineas = new ArrayList<>();
                psLineas.setInt(1, idFactura);
                ResultSet rsLineas = psLineas.executeQuery();

                while (rsLineas.next()) {
                    int idProducto = rsLineas.getInt("idproducto");
                    String desc = rsLineas.getString("descripcionproduct");
                    int cantidad = rsLineas.getInt("cantidad");
                    double precio = rsLineas.getDouble("precio_unitario");
                    lineas.add(new LineaFactura(idProducto, desc, cantidad, precio));
                }

                factura = new Factura(idFactura, idCliente, fecha.toLocalDateTime(), lineas);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al recuperar la factura por ID:");
            e.printStackTrace();
        }

        return factura;
    }

    public List<Factura> listarTodas() {
        String sqlFacturas = "SELECT * FROM facturas ORDER BY fecha DESC";
        List<Factura> lista = new ArrayList<>();

        Connection conn = ConexionBD.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sqlFacturas);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idFactura = rs.getInt("idfactura");
                int idCliente = rs.getInt("idcliente");
                Timestamp fecha = rs.getTimestamp("fecha");

                // Cargar líneas asociadas usando el método ya existente
                Factura factura = obtenerFacturaPorID(idFactura);

                // Evitamos nulls si hubo error al cargar
                if (factura != null) {
                    lista.add(factura);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar todas las facturas:");
            e.printStackTrace();
        }

        return lista;
    }

}
