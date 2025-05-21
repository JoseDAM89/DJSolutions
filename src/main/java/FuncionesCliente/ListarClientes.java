package FuncionesCliente;

import Datos.ClienteDAO;
import GUI.ListadosGenerico;
import GUI.EditarGenerico;
import GUI.EliminarGenerico;
import Modelos.Cliente;

import javax.swing.*;
import java.util.List;

public class ListarClientes {

    public void mostrarVentana() {
        // 1. Obtener todos los clientes usando el DAO (ya conectado a la base de datos)
        List<Cliente> clientes = ClienteDAO.listarTodos();

        // 2. Definir nombres visibles de columnas (como se mostrarán en la tabla)
        String[] columnas = {
                "ID", "Nombre", "CIF", "Email", "Persona de Contacto", "Dirección", "Descripción"
        };

        // 3. Convertir lista de objetos Cliente a una matriz de datos para JTable
        Object[][] datos = new Object[clientes.size()][columnas.length];

        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            datos[i][0] = c.getIdcliente();
            datos[i][1] = c.getCampoNombre();
            datos[i][2] = c.getCampoCIF();
            datos[i][3] = c.getCampoEmail();
            datos[i][4] = c.getCampoPersonaDeContacto();
            datos[i][5] = c.getCampoDireccion();
            datos[i][6] = c.getCampoDescripcion();
        }

        // 4. Crear tabla reutilizable con botón "Editar"
        final ListadosGenerico[] tablaClientes = new ListadosGenerico[1];

        ListadosGenerico tabla = new ListadosGenerico(
                "Listado de Clientes",
                columnas,
                datos,
                "Editar",
                e -> {
                    Object[] fila = tablaClientes[0].getFilaSeleccionada();
                    int filaSeleccionada = tablaClientes[0].getTabla().getSelectedRow();

                    if (fila != null && filaSeleccionada >= 0) {
                        int id = (int) fila[0];
                        String nombre = (String) fila[1];
                        System.out.println("Editar Cliente con ID: " + id + ", Nombre: " + nombre);

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

        // 5. Botón adicional "Eliminar"
        JButton botonEliminar = new JButton("Eliminar");
        botonEliminar.addActionListener(e -> {
            Object[] fila = tabla.getFilaSeleccionada();
            int filaSeleccionada = tabla.getTabla().getSelectedRow();

            if (fila != null && filaSeleccionada >= 0) {
                EliminarGenerico.eliminarRegistro(
                        "clientes",
                        "idcliente",
                        fila[0],
                        "INTEGER",
                        tabla.getTabla(),
                        filaSeleccionada
                );
            } else {
                JOptionPane.showMessageDialog(tabla, "Selecciona un cliente para eliminar.");
            }
        });

        // 6. Panel inferior con los botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(tabla.getBotonAccion());  // Botón Editar
        panelBotones.add(botonEliminar);           // Botón Eliminar
        tabla.add(panelBotones, java.awt.BorderLayout.SOUTH);

        // 7. Mostrar la tabla
        tablaClientes[0] = tabla;
        tabla.setVisible(true);
    }
}
