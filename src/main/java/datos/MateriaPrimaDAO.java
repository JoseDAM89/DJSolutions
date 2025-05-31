package datos;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

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
}
