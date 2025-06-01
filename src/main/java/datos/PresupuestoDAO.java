package datos;

import modelos.Presupuesto;
import modelos.LineaPresupuesto;

import java.sql.*;
import java.util.*;

public class PresupuestoDAO {

    private final LineaPresupuestoDAO lineaPresupuestoDAO = new LineaPresupuestoDAO();

    // ✅ Insertar un presupuesto con líneas
    public int insertar(Presupuesto presupuesto) {
        String sql = "INSERT INTO presupuestos (idcliente, fecha, total, aceptado) VALUES (?, ?, ?, ?) RETURNING idpresupuesto";
        int idGenerado = -1;
        Connection conn = null;

        try {
            conn = ConexionBD.getConexion();
            conn.setAutoCommit(false); // Transacción

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, presupuesto.getIdCliente());
                ps.setTimestamp(2, Timestamp.valueOf(presupuesto.getFecha().atStartOfDay()));
                ps.setDouble(3, presupuesto.getTotal());
                ps.setBoolean(4, presupuesto.isAceptado());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                } else {
                    throw new SQLException("No se generó el ID del presupuesto.");
                }
            }

            // Llamar al método con la misma conexión activa:
            lineaPresupuestoDAO.insertarLineas(idGenerado, presupuesto.getLineas(), conn);


            conn.commit(); // Confirmar

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar presupuesto: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("ℹ️ Transacción revertida.");
                } catch (SQLException rollbackEx) {
                    System.err.println("❌ Error en rollback: " + rollbackEx.getMessage());
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

    // ✅ Obtener presupuesto por ID
    public Presupuesto obtenerPorID(int idPresupuesto) {
        String sql = "SELECT * FROM presupuestos WHERE idpresupuesto = ?";
        Presupuesto presupuesto = null;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPresupuesto);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idCliente = rs.getInt("idcliente");
                Timestamp fecha = rs.getTimestamp("fecha");
                double total = rs.getDouble("total");
                boolean aceptado = rs.getBoolean("aceptado");

                List<LineaPresupuesto> lineas = lineaPresupuestoDAO.obtenerLineasPorPresupuesto(idPresupuesto);
                presupuesto = new Presupuesto(idPresupuesto, idCliente, fecha.toLocalDateTime().toLocalDate(), lineas, total, aceptado);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener presupuesto: " + e.getMessage());
        }

        return presupuesto;
    }


    // ✅ Listar todos los presupuestos
    public List<Presupuesto> listarTodos() {
        String sql = "SELECT * FROM presupuestos ORDER BY fecha DESC";
        List<Presupuesto> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idpresupuesto");
                int idCliente = rs.getInt("idcliente");
                Timestamp fecha = rs.getTimestamp("fecha");
                double total = rs.getDouble("total");
                boolean aceptado = rs.getBoolean("aceptado");

                List<LineaPresupuesto> lineas = lineaPresupuestoDAO.obtenerLineasPorPresupuesto(id);
                Presupuesto presupuesto = new Presupuesto(id, idCliente, fecha.toLocalDateTime().toLocalDate(), lineas, total, aceptado);
                lista.add(presupuesto);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar presupuestos: " + e.getMessage());
        }

        return lista;
    }

    public void actualizarAceptado(int idPresupuesto, boolean aceptado) {
        String sql = "UPDATE presupuestos SET aceptado = ? WHERE idpresupuesto = ?";
        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, aceptado);
            stmt.setInt(2, idPresupuesto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar aceptado: " + e.getMessage());
        }
    }



}
