package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListadosGenerico extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField campoBuscar;
    private TableRowSorter<DefaultTableModel> sorter;

    private JButton btnEditar;
    private JButton btnEliminar;

    public ListadosGenerico(String titulo, String[] columnas, Object[][] datos,
                            BiFunction<Object[], JTable, JPanel> crearFormularioEdicion,
                            Consumer<Object[]> accionEliminar) {

        setLayout(new BorderLayout());

        // Modelo y tabla
        modelo = new DefaultTableModel(datos, columnas);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        // Campo de búsqueda
        campoBuscar = new JTextField();
        campoBuscar.setToolTipText("Escribe para buscar...");
        campoBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String texto = campoBuscar.getText().trim();
                sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto));
            }
        });

        JPanel panelBuscar = new JPanel(new BorderLayout());
        panelBuscar.setBorder(BorderFactory.createTitledBorder("Buscar"));
        panelBuscar.add(campoBuscar, BorderLayout.CENTER);

        // Botones
        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> {
            Object[] fila = getFilaSeleccionada();
            if (fila == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una fila para editar.");
                return;
            }

            JPanel panelEdicion = crearFormularioEdicion.apply(fila, tabla);
            JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Editar", true);
            dialog.setContentPane(panelEdicion);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            Object[] fila = getFilaSeleccionada();
            if (fila == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de eliminar el registro seleccionado?",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                accionEliminar.accept(fila);
                eliminarFilaSeleccionada();
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Armado del panel principal
        add(panelBuscar, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public Object[] getFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return null;

        int filaModelo = tabla.convertRowIndexToModel(fila);
        Object[] datos = new Object[modelo.getColumnCount()];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = modelo.getValueAt(filaModelo, i);
        }
        return datos;
    }

    public void actualizarFila(Object[] nuevaFila) {
        int filaVisual = tabla.getSelectedRow();
        if (filaVisual == -1) return;

        int filaModelo = tabla.convertRowIndexToModel(filaVisual);
        for (int i = 0; i < nuevaFila.length; i++) {
            modelo.setValueAt(nuevaFila[i], filaModelo, i);
        }
    }


    public void eliminarFilaSeleccionada() {
        int filaVisual = tabla.getSelectedRow();
        if (filaVisual == -1) return;

        int filaModelo = tabla.convertRowIndexToModel(filaVisual);
        modelo.removeRow(filaModelo);
    }

    public JTable getTabla() {
        return tabla;
    }
}
