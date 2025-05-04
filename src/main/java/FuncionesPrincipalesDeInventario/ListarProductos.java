package FuncionesPrincipalesDeInventario;

import Datos.ConexionBD;
import GUI.GestionInventario;
import GUI.Vprin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ListarProductos extends JPanel {

    private JButton btnMostrar;
    private JButton btnEliminar;
    private JButton btnEditar;
    private JButton btnVolver;
    private JTable tabla;
    private JScrollPane scrollPane;
    private final String nombreTabla = "empresas";
    private Vprin ventana;

    public ListarProductos(Vprin ventana) {
        this.ventana = ventana;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(220, 235, 250));
        panelSuperior.setLayout(new FlowLayout());

        btnMostrar = new JButton("Mostrar");
        btnEliminar = new JButton("Eliminar");
        btnEditar = new JButton("Editar");
        btnVolver = new JButton("Volver al Menú");

        panelSuperior.add(btnMostrar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnVolver);

        tabla = new JTable();
        scrollPane = new JScrollPane(tabla);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        agregarEventos();
        mostrarDatosDeTabla(); // Mostrar al inicio
    }

    private void agregarEventos() {
        btnMostrar.addActionListener(e -> mostrarDatosDeTabla());

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                String columnaID = tabla.getColumnName(0);
                Object idValor = tabla.getValueAt(fila, 0);
                eliminarRegistro(nombreTabla, columnaID, idValor);
                mostrarDatosDeTabla(); // Refrescar tabla
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.");
            }
        });

        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                String columnaID = tabla.getColumnName(0);
                Object idValor = tabla.getValueAt(fila, 0);

                int columnas = tabla.getColumnCount();
                JTextField[] campos = new JTextField[columnas];
                JPanel panel = new JPanel(new GridLayout(columnas, 2));

                for (int i = 0; i < columnas; i++) {
                    panel.add(new JLabel(tabla.getColumnName(i)));
                    campos[i] = new JTextField(tabla.getValueAt(fila, i).toString());
                    panel.add(campos[i]);
                }

                int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Registro",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (resultado == JOptionPane.OK_OPTION) {
                    for (int i = 1; i < columnas; i++) {
                        String columna = tabla.getColumnName(i);
                        Object nuevoValor = campos[i].getText();
                        editarRegistro(nombreTabla, columna, nuevoValor, columnaID, idValor);
                    }
                    mostrarDatosDeTabla();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una fila para editar.");
            }
        });

        btnVolver.addActionListener(e -> {
            ventana.ponPanel(new GestionInventario(ventana));
        });
    }

    private void mostrarDatosDeTabla() {
        String consultaSQL = "SELECT * FROM " + nombreTabla;

        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(consultaSQL)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnas = metaData.getColumnCount();
            DefaultTableModel modelo = new DefaultTableModel();

            for (int i = 1; i <= columnas; i++) {
                modelo.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                modelo.addRow(fila);
            }

            tabla.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarRegistro(String tabla, String columna, Object nuevoValor, String columnaID, Object idValor) {
        String sql = "UPDATE " + tabla + " SET " + columna + " = ? WHERE " + columnaID + " = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, nuevoValor);
            stmt.setObject(2, idValor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarRegistro(String tabla, String columnaID, Object idValor) {
        String sql = "DELETE FROM " + tabla + " WHERE " + columnaID + " = ?";

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar este registro?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try (Connection conn = ConexionBD.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, idValor);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }
}
