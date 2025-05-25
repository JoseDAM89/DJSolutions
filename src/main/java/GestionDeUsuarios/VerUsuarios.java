package GestionDeUsuarios;

import Datos.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class VerUsuarios extends JPanel {

    private JTable tablaUsuarios;
    private DefaultTableModel modelo;

    public VerUsuarios() {
        setLayout(new BorderLayout());

        // Columnas visibles para el usuario
        String[] columnas = {"ID", "Nombre", "Apellido", "Correo Electrónico", "Administrador", "Editar", "Eliminar"};

        modelo = new DefaultTableModel(columnas, 0) {
            // Evitar edición directa de celdas excepto botones (que no son editables)
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo las columnas Editar (5) y Eliminar (6) serán "editables" para poner botones
                return column == 5 || column == 6;
            }
        };

        tablaUsuarios = new JTable(modelo);
        tablaUsuarios.setRowHeight(30);

        // Renderizar botones en las columnas 5 y 6
        tablaUsuarios.getColumn("Editar").setCellRenderer(new ButtonRenderer());
        tablaUsuarios.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());

        tablaUsuarios.getColumn("Editar").setCellEditor(new ButtonEditor(new JCheckBox(), "Editar", this));
        tablaUsuarios.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox(), "Eliminar", this));

        add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);

        cargarUsuarios();
    }

    // Carga todos los usuarios desde la base de datos a la tabla
    public void cargarUsuarios() {
        modelo.setRowCount(0); // Limpiar filas previas
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT id, nombre, apellido, correo_electronico, admin FROM usuarios ORDER BY id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> fila = new Vector<>();
                fila.add(rs.getInt("id"));
                fila.add(rs.getString("nombre"));
                fila.add(rs.getString("apellido"));
                fila.add(rs.getString("correo_electronico"));
                fila.add(rs.getBoolean("admin") ? "Sí" : "No");
                fila.add("Editar");
                fila.add("Eliminar");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }

    public void editarUsuario(int fila) {
        // Obtener datos actuales
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = (String) modelo.getValueAt(fila, 1);
        String apellido = (String) modelo.getValueAt(fila, 2);
        String correo = (String) modelo.getValueAt(fila, 3);
        String adminStr = (String) modelo.getValueAt(fila, 4); // "Sí" o "No"

        // Crear formulario personalizado
        JTextField tfNombre = new JTextField(nombre);
        JTextField tfApellido = new JTextField(apellido);
        JTextField tfCorreo = new JTextField(correo);

        JComboBox<String> cbAdmin = new JComboBox<>(new String[]{"true", "false"});
        cbAdmin.setSelectedItem(adminStr.equalsIgnoreCase("sí") ? "true" : "false");

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Nombre:"));
        panel.add(tfNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(tfApellido);
        panel.add(new JLabel("Correo Electrónico:"));
        panel.add(tfCorreo);
        panel.add(new JLabel("Administrador:"));
        panel.add(cbAdmin);

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Guardar cambios en BD
            try (Connection conn = ConexionBD.conectar()) {
                String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, correo_electronico = ?, admin = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tfNombre.getText());
                stmt.setString(2, tfApellido.getText());
                stmt.setString(3, tfCorreo.getText());
                stmt.setBoolean(4, cbAdmin.getSelectedItem().equals("true"));
                stmt.setInt(5, id);

                int updated = stmt.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
                    cargarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar usuario: " + e.getMessage());
            }
        }
    }


    public void eliminarUsuario(int fila) {
        int id = (int) modelo.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = ConexionBD.conectar()) {
                String sql = "DELETE FROM usuarios WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                int eliminado = stmt.executeUpdate();

                if (eliminado > 0) {
                    JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.");
                    cargarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + e.getMessage());
            }
        }
    }
}


// Clases para renderizar y manejar botones en JTable
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private VerUsuarios panelPadre;
    private int filaActual;

    public ButtonEditor(JCheckBox checkBox, String label, VerUsuarios panelPadre) {
        super(checkBox);
        this.label = label;
        this.panelPadre = panelPadre;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        filaActual = row;
        button.setText(label);
        isPushed = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            if (label.equals("Editar")) {
                panelPadre.editarUsuario(filaActual);
            } else if (label.equals("Eliminar")) {
                panelPadre.eliminarUsuario(filaActual);
            }
        }
        isPushed = false;
        return label;
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
