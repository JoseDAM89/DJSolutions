package Datos;

import Modelos.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ClienteDAO {

    public static boolean insertar(Cliente cliente) {
        String sql = """
            INSERT INTO clientes 
            (campoNombre, campoCIF, campoEmail, campoPersonaDeContacto, campoDireccion, campoDescripcion)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConexionBD.conectar();
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
}
