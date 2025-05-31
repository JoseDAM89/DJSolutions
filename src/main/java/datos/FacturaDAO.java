package datos;

import modelos.Factura;
import modelos.LineaFactura;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturaDAO {

    // Método para insertar una factura con sus líneas
    public int insertarFactura(Factura factura) {
        String sqlFactura = "INSERT INTO facturas (idcliente, fecha, pagada) VALUES (?, ?, ?) RETURNING idfactura";
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
                psFactura.setBoolean(3, factura.isPagada()); // ✅ campo booleano
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

    // Método para recuperar una factura por su ID
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
        try (PreparedStatement psFactura = conn.prepareStatement(sqlFactura);
             PreparedStatement psLineas = conn.prepareStatement(sqlLineas)) {

            psFactura.setInt(1, idFactura);
            ResultSet rsFactura = psFactura.executeQuery();

            if (rsFactura.next()) {
                int idCliente = rsFactura.getInt("idcliente");
                Timestamp fecha = rsFactura.getTimestamp("fecha");
                boolean pagada = rsFactura.getBoolean("pagada");

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

                factura = new Factura(idFactura, idCliente, fecha.toLocalDateTime(), lineas, pagada);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al recuperar la factura por ID:");
            e.printStackTrace();
        }

        return factura;
    }

    // Método para listar todas las facturas con sus líneas
    public List<Factura> listarTodas() {
        String sqlFacturas = "SELECT * FROM facturas ORDER BY fecha DESC";
        String sqlLineas = """
            SELECT fp.idfactura, fp.idproducto, p.descripcionproduct, fp.cantidad, fp.precio_unitario
            FROM factura_productos fp
            JOIN productos p ON fp.idproducto = p.codproduct
        """;

        List<Factura> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement psFacturas = conn.prepareStatement(sqlFacturas);
             PreparedStatement psLineas = conn.prepareStatement(sqlLineas)) {

            ResultSet rsFacturas = psFacturas.executeQuery();
            Map<Integer, Factura> mapaFacturas = new HashMap<>();

            while (rsFacturas.next()) {
                int id = rsFacturas.getInt("idfactura");
                int idCliente = rsFacturas.getInt("idcliente");
                Timestamp fecha = rsFacturas.getTimestamp("fecha");
                boolean pagada = rsFacturas.getBoolean("pagada");

                Factura f = new Factura(id, idCliente, fecha.toLocalDateTime(), new ArrayList<>(), pagada);
                mapaFacturas.put(id, f);
            }

            ResultSet rsLineas = psLineas.executeQuery();
            while (rsLineas.next()) {
                int idFactura = rsLineas.getInt("idfactura");
                int idProducto = rsLineas.getInt("idproducto");
                String desc = rsLineas.getString("descripcionproduct");
                int cantidad = rsLineas.getInt("cantidad");
                double precio = rsLineas.getDouble("precio_unitario");

                LineaFactura linea = new LineaFactura(idProducto, desc, cantidad, precio);
                Factura factura = mapaFacturas.get(idFactura);
                if (factura != null) {
                    factura.getLineas().add(linea);
                }
            }

            lista.addAll(mapaFacturas.values());

        } catch (SQLException e) {
            System.err.println("❌ Error al listar todas las facturas con líneas:");
            e.printStackTrace();
        }

        return lista;
    }

    // Método para actualizar el campo "pagada"
    public void actualizarEstadoPagada(int idFactura, boolean pagada) {
        String sql = "UPDATE facturas SET pagada = ? WHERE idfactura = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, pagada);
            ps.setInt(2, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar el estado de la factura: " + e.getMessage());
        }
    }
}
