package FuncionesInventario;

import Datos.ProductoDAO;
import GUI.EditarGenerico;
import GUI.EliminarGenerico;
import Modelos.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarProductos {

    public JPanel mostrarVentana() {
        // 1. Obtener productos
        List<Producto> productos = ProductoDAO.listarTodos();

        // 2. Columnas
        String[] columnas = {
                "ID", "Nombre", "Precio", "Descripción", "Stock", "Materia Prima", "ID Materia"
        };

        // 3. Convertir datos
        Object[][] datos = new Object[productos.size()][columnas.length];
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            datos[i][0] = p.getCodproduct();
            datos[i][1] = p.getNombreproduct();
            datos[i][2] = p.getPrecioproduct();
            datos[i][3] = p.getDescripcionproduct();
            datos[i][4] = p.getStockproduct();
            datos[i][5] = p.isMateriaprima() ? "Sí" : "No";
            datos[i][6] = p.getIdmateriaprima();
        }

        // 4. Modelo tabla
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // evitar editar directamente
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 5. Panel principal con BorderLayout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // 6. Panel botones abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // 7. Acción botón editar
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(panel, "Selecciona un producto para editar.");
                return;
            }

            Object[] filaSeleccionada = new Object[modelo.getColumnCount()];
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                filaSeleccionada[i] = modelo.getValueAt(fila, i);
            }

            JPanel panelEdicion = EditarGenerico.crearFormularioEdicion(
                    "productos",
                    columnas,
                    filaSeleccionada,
                    "codproduct",
                    filaSeleccionada[0],
                    "INTEGER",
                    tabla,
                    fila,
                    () -> JOptionPane.showMessageDialog(panel, "Producto editado con éxito.")
            );

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel), "Editar Producto", true);
            dialog.getContentPane().add(panelEdicion);
            dialog.pack();
            dialog.setLocationRelativeTo(panel);
            dialog.setVisible(true);
        });

        // 8. Acción botón eliminar
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(panel, "Selecciona un producto para eliminar.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(panel,
                    "¿Estás seguro de que deseas eliminar este producto?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            Object idValor = modelo.getValueAt(fila, 0);

            try {
                boolean eliminado = EliminarGenerico.eliminarRegistro("productos", idValor);
                if (eliminado) {
                    modelo.removeRow(fila);
                    JOptionPane.showMessageDialog(panel, "Producto eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(panel, "No se pudo eliminar el producto.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error al eliminar: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        return panel;
    }
}
