package FuncionesPrincipalesDeInventario;

import Datos.ConexionBD;
import GUI.ListadosGenerico;
import GUI.EditarGenerico;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Clase que se encarga de listar todos los productos de la base de datos
 * y mostrarlos en una tabla reutilizable (ListadosGenerico).
 */
public class ListarProductos {

    /**
     * Este método se ejecuta cuando el usuario pulsa el botón "Listar Productos".
     */
    public void mostrarVentana() {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;

        try {
            // Establecer conexión con la base de datos
            conexion = ConexionBD.conectar();

            // Consulta SQL para obtener datos de productos
            String sql = """
                         SELECT codproduct, nombreproduct, precioproduct, descripcionproduct, 
                         stockproduct, materiaprima, idmateriaprima
                         FROM productos
                         """;

            sentencia = conexion.prepareStatement(sql);
            resultado = sentencia.executeQuery();

            // Guardar los resultados en una lista
            ArrayList<Object[]> listaDatos = new ArrayList<>();
            while (resultado.next()) {
                Object[] fila = new Object[] {
                        resultado.getInt("codproduct"),
                        resultado.getString("nombreproduct"),
                        resultado.getDouble("precioproduct"),
                        resultado.getString("descripcionproduct"),
                        resultado.getInt("stockproduct"),
                        resultado.getString("materiaprima"),
                        resultado.getInt("idmateriaprima")
                };
                listaDatos.add(fila);
            }

            // Convertimos la lista en un array bidimensional
            Object[][] datos = listaDatos.toArray(new Object[0][]);

            // Definimos los nombres de columnas
            String[] columnas = {
                    "ID", "Nombre", "Referencia", "Precio", "Cantidad", "Descripción"
            };

            // Creamos un array final para el acceso desde el ActionListener
            final ListadosGenerico[] tablaProductos = new ListadosGenerico[1];

            // Creamos la tabla con botón Editar
            ListadosGenerico tabla = new ListadosGenerico(
                    "Listado de Productos",
                    columnas,
                    datos,
                    "Editar",
                    e -> {
                        Object[] fila = tablaProductos[0].getFilaSeleccionada();
                        if (fila != null) {
                            int id = (int) fila[0];
                            String nombre = (String) fila[1];
                            System.out.println("Editar Producto con ID: " + id + ", Nombre: " + nombre);

                            // Llamamos a EditarGenerico para editar el producto
                            EditarGenerico.mostrarFormularioDeEdicion(
                                    "productos",  // Nombre de la tabla
                                    columnas,     // Nombres de las columnas
                                    fila          // Fila seleccionada
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
