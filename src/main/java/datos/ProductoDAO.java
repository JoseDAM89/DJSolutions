package datos;

import modelos.Producto;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public static List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = """
            SELECT codproduct, nombreproduct, precioproduct, descripcionproduct,
                   stockproduct, materiaprima, idmateriaprima
            FROM productos
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto(
                        rs.getInt("codproduct"),
                        rs.getString("nombreproduct"),
                        rs.getDouble("precioproduct"),
                        rs.getString("descripcionproduct"),
                        rs.getInt("stockproduct"),
                        rs.getBoolean("materiaprima"),
                        rs.getInt("idmateriaprima")
                );
                lista.add(producto);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    public static boolean insertar(Producto producto) {
        String sql = """
            INSERT INTO productos
            (codproduct, nombreproduct, precioproduct, descripcionproduct,
             stockproduct, materiaprima, idmateriaprima)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, producto.getCodproduct());
            stmt.setString(2, producto.getNombreproduct());
            stmt.setDouble(3, producto.getPrecioproduct());
            stmt.setString(4, producto.getDescripcionproduct());
            stmt.setInt(5, producto.getStockproduct());
            stmt.setBoolean(6, producto.isMateriaprima());

            if (!producto.isMateriaprima() || producto.getIdmateriaprima() <= 0) {
                stmt.setNull(7, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(7, producto.getIdmateriaprima());
            }

            stmt.executeUpdate();
            return true;

        } catch (org.postgresql.util.PSQLException e) {
            if (e.getSQLState().equals("23505")) {
                JOptionPane.showMessageDialog(null, "❌ Ya existe un producto con ese COD. Por favor elige otro.");
                return false;
            } else {
                JOptionPane.showMessageDialog(null, "❌ Error al insertar producto: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error inesperado al insertar: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarPorID(Producto producto) {
        String sql = """
            UPDATE productos SET
                nombreproduct = ?,
                precioproduct = ?,
                descripcionproduct = ?,
                stockproduct = ?,
                materiaprima = ?,
                idmateriaprima = ?
            WHERE codproduct = ?
        """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombreproduct());
            stmt.setDouble(2, producto.getPrecioproduct());
            stmt.setString(3, producto.getDescripcionproduct());
            stmt.setInt(4, producto.getStockproduct());
            stmt.setBoolean(5, producto.isMateriaprima());

            if (!producto.isMateriaprima() || producto.getIdmateriaprima() <= 0) {
                stmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(6, producto.getIdmateriaprima());
            }

            stmt.setInt(7, producto.getCodproduct());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarPorID(int codproduct) {
        String sql = "DELETE FROM productos WHERE codproduct = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codproduct);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Producto obtenerPorID(int codproduct) {
        String sql = """
        SELECT codproduct, nombreproduct, precioproduct, descripcionproduct,
               stockproduct, materiaprima, idmateriaprima
        FROM productos
        WHERE codproduct = ?
    """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codproduct);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("codproduct"),
                            rs.getString("nombreproduct"),
                            rs.getDouble("precioproduct"),
                            rs.getString("descripcionproduct"),
                            rs.getInt("stockproduct"),
                            rs.getBoolean("materiaprima"),
                            rs.getInt("idmateriaprima")
                    );
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al obtener producto: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


}
