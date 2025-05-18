package FuncionesInventario;

import Datos.ConexionBD;
import GUI.ListadosGenerico;
import GUI.EditarGenerico;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class ListarProductos {

    public void mostrarVentana() {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;

        try {
            conexion = ConexionBD.conectar();

            String sql = """
                SELECT codproduct, nombreproduct, precioproduct, descripcionproduct,
                       stockproduct, materiaprima, idmateriaprima
                FROM productos
            """;

            sentencia = conexion.prepareStatement(sql);
            resultado = sentencia.executeQuery();

            ArrayList<Object[]> listaDatos = new ArrayList<>();
            while (resultado.next()) {
                Object[] fila = new Object[] {
                        resultado.getInt("codproduct"),
                        resultado.getString("nombreproduct"),
                        resultado.getDouble("precioproduct"),
                        resultado.getString("descripcionproduct"),
                        resultado.getInt("stockproduct"),
                        resultado.getBoolean("materiaprima") ? "Sí" : "No",
                        resultado.getInt("idmateriaprima")
                };
                listaDatos.add(fila);
            }

            Object[][] datos = listaDatos.toArray(new Object[0][]);

            // Aquí deben coincidir exactamente con el orden de los campos
            String[] columnas = {
                    "ID", "Nombre", "Precio", "Descripción", "Stock", "Materia Prima", "ID Materia"
            };

            final ListadosGenerico[] tablaProductos = new ListadosGenerico[1];

            ListadosGenerico tabla = new ListadosGenerico(
                    "Listado de Productos",
                    columnas,
                    datos,
                    "Editar",
                    e -> {
                        Object[] fila = tablaProductos[0].getFilaSeleccionada();
                        if (fila != null) {
                            int filaSeleccionada = tablaProductos[0].getTabla().getSelectedRow();

                            EditarGenerico.mostrarFormularioDeEdicion(
                                    "productos",
                                    columnas,
                                    fila,
                                    "codproduct",
                                    fila[0],
                                    "INTEGER",
                                    tablaProductos[0].getTabla(),
                                    filaSeleccionada
                            );
                        }

                    }
            );

            tablaProductos[0] = tabla;
            tabla.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener productos: " + ex.getMessage());
        } finally {
            try {
                if (resultado != null) resultado.close();
                if (sentencia != null) sentencia.close();
                if (conexion != null) conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
