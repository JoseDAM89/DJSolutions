package datos;

import modelos.MateriaPrima;

import java.sql.*;
import java.util.LinkedHashMap;
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


}
