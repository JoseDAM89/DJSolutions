package datos;

import modelos.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static boolean registrar(Usuario usuario) {
        String hashed = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        String sql = "INSERT INTO usuarios (nombre, apellido, correo_electronico, contraseña, admin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreoElectronico());
            stmt.setString(4, hashed);
            stmt.setBoolean(5, usuario.isAdmin());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean correoYaExiste(String correo) {
        String sql = "SELECT 1 FROM usuarios WHERE correo_electronico = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static void sincronizarSecuenciaId() {
        String sql = "SELECT setval('usuarios_id_seq', (SELECT COALESCE(MAX(id), 1) FROM usuarios))";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, correo_electronico, admin FROM usuarios";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo_electronico"),
                        "", // no se muestra la contraseña
                        rs.getBoolean("admin")
                );
                u.setId(rs.getInt("id"));
                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean actualizarPorID(Usuario usuario) {
        String sql = """
        UPDATE usuarios SET
            nombre = ?,
            apellido = ?,
            correo_electronico = ?,
            admin = ?
        WHERE id = ?
    """;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreoElectronico());
            stmt.setBoolean(4, usuario.isAdmin());
            stmt.setInt(5, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public static boolean eliminarPorID(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
