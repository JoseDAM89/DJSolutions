package datos;

import modelos.MateriaPrima;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelos.MateriaPrima;

public class MateriaPrimaDAO {

    public static Map<Integer, String> obtenerTodas() {
        Map<Integer, String> materias = new LinkedHashMap<>();
        String sql = "SELECT idmateriaprima, descripcionmaterial FROM \"MateriasPrimas\"";


        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idmateriaprima");
                String nombre = rs.getString("descripcionmaterial");
                materias.put(id, nombre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materias;
    }

    public static boolean insertar(MateriaPrima materia) {
        String sql = "INSERT INTO \"MateriasPrimas\" (descripcionmaterial, stockmetros) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, materia.getDescripcionMaterial());
            stmt.setDouble(2, materia.getStockMaterial());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<MateriaPrima> obtenerTodasComoLista() {
        List<MateriaPrima> lista = new ArrayList<>();
        String sql = "SELECT idmateriaprima, descripcionmaterial, stockmetros FROM \"MateriasPrimas\"";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idmateriaprima");
                String descripcion = rs.getString("descripcionmaterial");
                double stock = rs.getDouble("stockmetros");
                lista.add(new MateriaPrima(id, descripcion, stock));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean actualizarPorID(MateriaPrima materia) {
        String sql = "UPDATE \"MateriasPrimas\" SET descripcionmaterial = ?, stockmetros = ? WHERE idmateriaprima = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, materia.getDescripcionMaterial());
            stmt.setDouble(2, materia.getStockMaterial());
            stmt.setInt(3, materia.getIdMaterial());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método que te faltaba para eliminar por ID:
    public static boolean eliminarPorID(int id) {
        String sql = "DELETE FROM \"MateriasPrimas\" WHERE idmateriaprima = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
