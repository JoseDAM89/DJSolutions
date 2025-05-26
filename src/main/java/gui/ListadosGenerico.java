package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        setBackground(Color.decode("#f2f4f7")); // Fondo general claro

        // Modelo y tabla
        modelo = new DefaultTableModel(datos, columnas);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        // Estilizar tabla
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(230, 230, 230));
        header.setForeground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campo de búsqueda
        campoBuscar = new JTextField();
        campoBuscar.setToolTipText("Escribe para buscar...");
        campoBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoBuscar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        campoBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String texto = campoBuscar.getText().trim();
                sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto));
            }
        });

        JPanel panelBuscar = new JPanel(new BorderLayout());
        panelBuscar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Buscar"));
        panelBuscar.setBackground(getBackground());
        panelBuscar.setBorder(new EmptyBorder(10, 15, 10, 15));
        panelBuscar.add(campoBuscar, BorderLayout.CENTER);

        // Botones
        btnEditar = crearBoton("✏ Editar", new Color(70, 130, 180));
        btnEliminar = crearBoton("🗑 Eliminar", new Color(220, 53, 69));

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
        panelBotones.setBackground(getBackground());
        panelBotones.setBorder(new EmptyBorder(10, 15, 10, 15));
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Armado del panel principal
        add(panelBuscar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
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
