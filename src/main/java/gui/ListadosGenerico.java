package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
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

    private final Color COLOR_FONDO = new Color(53, 107, 140,100);
    private final Color COLOR_PANEL = Color.WHITE;
    private final Color COLOR_BOTON_ACCION = new Color(80, 140, 255);
    private final Color COLOR_BOTON_PELIGRO = new Color(220, 85, 90);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);
    private final Color COLOR_ENCABEZADO = new Color(120, 140, 180);

    public ListadosGenerico(String titulo, String[] columnas, Object[][] datos,
                            BiFunction<Object[], JTable, JPanel> crearFormularioEdicion,
                            Consumer<Object[]> accionEliminar) {

        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        modelo = new DefaultTableModel(datos, columnas);
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(28);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setSelectionBackground(new Color(120, 150, 255));
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(COLOR_ENCABEZADO);
        header.setForeground(Color.WHITE);
        header.setBorder(new MatteBorder(0, 0, 2, 0, COLOR_ENCABEZADO.darker()));

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(210, 210, 210), 1, true),
                new EmptyBorder(5, 5, 5, 5)
        ));

        campoBuscar = new JTextField();
        campoBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoBuscar.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        campoBuscar.setBackground(Color.WHITE);
        campoBuscar.setForeground(COLOR_TEXTO);
        campoBuscar.setToolTipText("Buscar...");

        campoBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String texto = campoBuscar.getText().trim();
                sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto));
            }
        });

        JPanel panelBuscar = new JPanel(new BorderLayout());
        panelBuscar.setBackground(COLOR_PANEL);
        panelBuscar.setBorder(new CompoundBorder(
                new EmptyBorder(10, 15, 10, 15),
                new LineBorder(new Color(200, 200, 200), 1, true)
        ));
        panelBuscar.add(new JLabel("🔍 Buscar:", SwingConstants.LEFT), BorderLayout.WEST);
        panelBuscar.add(campoBuscar, BorderLayout.CENTER);

        btnEditar = crearBoton("✏ Editar", COLOR_BOTON_ACCION);
        btnEliminar = crearBoton("🗑 Eliminar", COLOR_BOTON_PELIGRO);

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

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBuscar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorFondo);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBorder(new RoundedBorder(12));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

    // Clase para bordes redondeados
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getBackground().darker());
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 16, 8, 16);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(8, 16, 8, 16);
            return insets;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_FONDO);
    }

    public JTable getTabla() {
        return tabla;
    }
}
