package Datos;

import Modelos.Producto;

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

        try (Connection conn = ConexionBD.conectar();
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

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombreproduct());
            stmt.setDouble(2, producto.getPrecioproduct());
            stmt.setString(3, producto.getDescripcionproduct());
            stmt.setInt(4, producto.getStockproduct());
            stmt.setBoolean(5, producto.isMateriaprima());
            stmt.setInt(6, producto.getIdmateriaprima());
            stmt.setInt(7, producto.getCodproduct());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarPorID(int codproduct) {
        String sql = "DELETE FROM productos WHERE codproduct = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codproduct);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
