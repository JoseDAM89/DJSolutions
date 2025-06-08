package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConsultarDAO {

    public static boolean clienteTieneDocumentos(int idCliente) {
        return tieneRelacion("SELECT 1 FROM facturas WHERE idcliente = ? LIMIT 1", idCliente)
                || tieneRelacion("SELECT 1 FROM presupuestos WHERE idcliente = ? LIMIT 1", idCliente);
    }

    public static boolean productoTieneDocumentos(int idProducto) {
        return tieneRelacion("SELECT 1 FROM factura_productos WHERE idproducto = ? LIMIT 1", idProducto)
                || tieneRelacion("SELECT 1 FROM presupuesto_productos WHERE idproducto = ? LIMIT 1", idProducto);
    }

    private static boolean tieneRelacion(String sql, int id) {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
