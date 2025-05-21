package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

public class ListadosGenerico extends JFrame {

    private JTable tabla;                         // Tabla para mostrar los datos
    private DefaultTableModel modelo;             // Modelo de la tabla
    public JButton botonAccion;                  // Botón para editar/ver
    private JTextField campoBuscar;               // Campo para escribir el texto a buscar
    private TableRowSorter<DefaultTableModel> sorter; // Permite ordenar y filtrar

    /**
     * Constructor de la tabla reutilizable con filtro.
     */
    public ListadosGenerico(String titulo, String[] columnas, Object[][] datos, String textoBoton, ActionListener accionBoton) {
        setTitle(titulo);
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Modelo de la tabla
        modelo = new DefaultTableModel(datos, columnas);
        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter); // Asignamos el sorter a la tabla

        // Campo de búsqueda (filtro)
        campoBuscar = new JTextField();
        campoBuscar.setToolTipText("Escribe para buscar...");
        campoBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = campoBuscar.getText();
                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null); // Mostrar todo si no hay texto
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto)); // Filtrar ignorando mayúsculas
                }
            }
        });

        // Panel superior para el campo de búsqueda
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Buscar"));
        panelSuperior.add(campoBuscar, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH); // Añadir arriba

        // Scroll con la tabla
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER); // Añadir en el centro

        // Botón de acción si se requiere
        if (accionBoton != null && textoBoton != null) {
            botonAccion = new JButton(textoBoton);
            botonAccion.addActionListener(e -> {
                int filaSeleccionada = tabla.getSelectedRow();
                if (filaSeleccionada != -1) {
                    accionBoton.actionPerformed(e);
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor selecciona una fila.");
                }
            });

            JPanel panelBoton = new JPanel();
            panelBoton.add(botonAccion);
            add(panelBoton, BorderLayout.SOUTH); // Añadir abajo
        }
    }

    /**
     * Devuelve la fila seleccionada (con los datos reales, no del índice visual).
     */
    public Object[] getFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return null;

        // Convertimos el índice visual a índice real (por si hay filtro activo)
        int filaModelo = tabla.convertRowIndexToModel(fila);

        Object[] datos = new Object[tabla.getColumnCount()];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = modelo.getValueAt(filaModelo, i);
        }

        return datos;
    }
    public JTable getTabla() {
        return tabla;
    }
    public JButton getBotonAccion() {
        return botonAccion;
    }


}
