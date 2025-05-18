package FuncionesCliente;

import Datos.ConexionBD;
import GUI.ListadosGenerico;
import GUI.EditarGenerico;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Clase que se encarga de listar todos los clientes de la base de datos
 * y mostrarlos en una tabla reutilizable (ListadosTablas).
 */
public class ListarClientes {

    /**
     * Este método se ejecuta cuando el usuario pulsa el botón "Listar Clientes".
     */
    public void mostrarVentana() {
        Connection conexion = null;
        PreparedStatement sentencia = null;
        ResultSet resultado = null;

        try {
            // Establecer la conexión con la base de datos
            conexion = ConexionBD.conectar();

            // Consulta SQL para obtener los datos de los clientes
            String sql = """
                         SELECT IdCliente, campoNombre, campoCIF, campoEmail,
                                campoPersonaDeContacto, campoDireccion, campoDescripcion
                         FROM clientes
                         """;

            sentencia = conexion.prepareStatement(sql);
            resultado = sentencia.executeQuery();

            // Guardar los resultados en una lista
            ArrayList<Object[]> listaDatos = new ArrayList<>();
            while (resultado.next()) {
                Object[] fila = new Object[]{
                        resultado.getInt("IdCliente"),
                        resultado.getString("campoNombre"),
                        resultado.getString("campoCIF"),
                        resultado.getString("campoEmail"),
                        resultado.getString("campoPersonaDeContacto"),
                        resultado.getString("campoDireccion"),
                        resultado.getString("campoDescripcion")
                };
                listaDatos.add(fila);
            }

            // Convertimos la lista en un array bidimensional
            Object[][] datos = listaDatos.toArray(new Object[0][]);

            // Nombres de las columnas que se mostrarán
            String[] columnas = {
                    "ID", "Nombre", "CIF", "Email",
                    "Persona de Contacto", "Dirección", "Descripción"
            };

            // Creamos un array final para poder usar dentro del ActionListener
            final ListadosGenerico[] tablaClientes = new ListadosGenerico[1];

            // Creamos la tabla con botón Editar
            ListadosGenerico tabla = new ListadosGenerico(
                    "Listado de Clientes",
                    columnas,
                    datos,
                    "Editar",
                    e -> {
                        Object[] fila = tablaClientes[0].getFilaSeleccionada();

                        if (fila != null) {
                            int id = (int) fila[0];
                            String nombre = (String) fila[1];
                            System.out.println("Editar Cliente con ID: " + id + ", Nombre: " + nombre);

                            int filaSeleccionada = tablaClientes[0].getTabla().getSelectedRow(); // ✅ Añade esta línea

                            // Llamamos a EditarGenerico para editar el cliente
                            EditarGenerico.mostrarFormularioDeEdicion(
                                    "clientes",
                                    columnas,
                                    fila,
                                    "idcliente",
                                    fila[0],
                                    "INTEGER",
                                    tablaClientes[0].getTabla(),
                                    filaSeleccionada
                            );
                        }

                    }
            );

            tablaClientes[0] = tabla;
            tabla.setVisible(true);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener clientes: " + ex.getMessage());
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
