package FuncionesCliente;

import Datos.ClienteDAO;
import gui.EditarGenerico;
import gui.EliminarGenerico;
import Modelos.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarClientes {

    public JPanel mostrarVentana() {
        // 1. Obtener todos los clientes desde la base de datos
        List<Cliente> clientes = ClienteDAO.listarTodos();

        // 2. Definir columnas visibles
        String[] columnas = {
                "ID", "Nombre", "CIF", "Email", "Persona de Contacto", "Dirección", "Descripción"
        };

        // 3. Convertir la lista a una matriz de datos
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

        // 4. Crear modelo para JTable
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            // Para evitar edición directa de la tabla
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 5. Crear panel principal y agregar componentes
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Añadimos la tabla con scroll
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panel.add(panelBotones, BorderLayout.SOUTH);

        // 6. Agregar acciones a los botones

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(panel, "Por favor, selecciona un cliente para editar.");
                return;
            }

            Object[] filaSeleccionada = new Object[modelo.getColumnCount()];
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                filaSeleccionada[i] = modelo.getValueAt(fila, i);
            }

            // Aquí asumimos que EditarGenerico.crearFormularioEdicion devuelve un JPanel con el formulario
            JPanel formularioEdicion = EditarGenerico.crearFormularioEdicion(
                    "clientes",
                    columnas,
                    filaSeleccionada,
                    "idcliente",
                    filaSeleccionada[0],
                    "INTEGER",
                    tabla,
                    fila,
                    () -> {
                        // Opcional: algo a ejecutar tras actualizar (recargar tabla, etc)
                    }
            );

            // Mostrar formulario en un diálogo modal
            int opcion = JOptionPane.showConfirmDialog(panel, formularioEdicion,
                    "Editar Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (opcion == JOptionPane.OK_OPTION) {
                // Los cambios se manejan dentro del formulario
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(panel, "Por favor, selecciona un cliente para eliminar.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(panel,
                    "¿Estás seguro que quieres eliminar este cliente?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Object idValor = modelo.getValueAt(fila, 0);

            boolean eliminado = EliminarGenerico.eliminarRegistro(
                    "clientes",
                    idValor
            );

            if (eliminado) {
                modelo.removeRow(fila);
                JOptionPane.showMessageDialog(panel, "Cliente eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(panel, "Error eliminando el cliente.");
            }
        });

        return panel;
    }
}
