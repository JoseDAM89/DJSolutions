package datos;

import modelos.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static boolean insertar(Cliente cliente) {
        String sql = """
            INSERT INTO clientes 
            (campoNombre, campoCIF, campoEmail, campoPersonaDeContacto, campoDireccion, campoDescripcion)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexionBD.getConexion();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCampoNombre());
            stmt.setString(2, cliente.getCampoCIF());
            stmt.setString(3, cliente.getCampoEmail());
            stmt.setString(4, cliente.getCampoPersonaDeContacto());
            stmt.setString(5, cliente.getCampoDireccion());
            stmt.setString(6, cliente.getCampoDescripcion());

            stmt.executeUpdate();
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = """
        SELECT idcliente, camponombre, campocif, campoemail,
               campopersonadecontacto, campodireccion, campodescripcion
        FROM clientes
    """;

        try (  Connection conn = ConexionBD.getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idcliente"),
                        rs.getString("camponombre"),
                        rs.getString("campocif"),
                        rs.getString("campoemail"),
                        rs.getString("campopersonadecontacto"),
                        rs.getString("campodireccion"),
                        rs.getString("campodescripcion")
                );
                lista.add(cliente);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    public static boolean actualizarPorID(Cliente cliente) {
        String sql = """
        UPDATE clientes SET
        camponombre = ?,
        campocif = ?,
        campoemail = ?,
        campopersonadecontacto = ?,
        campodireccion = ?,
        campodescripcion = ?
        WHERE idcliente = ?
    """;


        try ( Connection conn = ConexionBD.getConexion();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCampoNombre());
            stmt.setString(2, cliente.getCampoCIF());
            stmt.setString(3, cliente.getCampoEmail());
            stmt.setString(4, cliente.getCampoPersonaDeContacto());
            stmt.setString(5, cliente.getCampoDireccion());
            stmt.setString(6, cliente.getCampoDescripcion());
            stmt.setInt(7, cliente.getIdcliente());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Cliente obtenerPorID(int idcliente) {
        String sql = """
        SELECT idcliente, camponombre, campocif, campoemail,
               campopersonadecontacto, campodireccion, campodescripcion
        FROM clientes
        WHERE idcliente = ?
    """;

        try ( Connection conn = ConexionBD.getConexion();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idcliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("idcliente"),
                        rs.getString("camponombre"),
                        rs.getString("campocif"),
                        rs.getString("campoemail"),
                        rs.getString("campopersonadecontacto"),
                        rs.getString("campodireccion"),
                        rs.getString("campodescripcion")
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static boolean eliminarPorID(int idcliente) {
        String sql = "DELETE FROM clientes WHERE idcliente = ?";
        try (Connection conn = ConexionBD.getConexion();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idcliente);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
