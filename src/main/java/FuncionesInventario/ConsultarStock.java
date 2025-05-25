package FuncionesInventario;

import Datos.ConexionBD;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ConsultarStock extends JPanel {

    public ConsultarStock() {
        setLayout(new GridLayout(2, 1, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de Productos
        add(crearPanelStock("Productos", "productos",
                "codproduct", "nombreproduct", "stockproduct"));

        // Panel de Materias Primas
        add(crearPanelStock("Materia Prima", "MateriasPrimas",
                "idmateriaprima", "descripcionmaterial", "stockmetros"));
    }

    private JPanel crearPanelStock(String titulo, String tablaBD, String idCol, String nombreCol, String stockCol) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Stock de " + titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                Color.DARK_GRAY
        ));

        String[] columnas = {"ID", "Nombre", "Stock"};
        Object[][] datos = obtenerDatos(tablaBD, idCol, nombreCol, stockCol);

        JTable tabla = new JTable(new DefaultTableModel(datos, columnas)) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla.setRowHeight(24);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.setSelectionBackground(new Color(204, 229, 255));
        tabla.setSelectionForeground(Color.BLACK);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private Object[][] obtenerDatos(String tabla, String idCol, String nombreCol, String stockCol) {
        ArrayList<Object[]> listaDatos = new ArrayList<>();

        // Poner la tabla entre comillas dobles para respetar el case sensitive en PostgreSQL
        String sql = String.format("SELECT %s AS id, %s AS nombre, %s AS stock FROM \"%s\"", idCol, nombreCol, stockCol, tabla);

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listaDatos.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("stock")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener datos de " + tabla + ": " + e.getMessage());
        }

        return listaDatos.toArray(new Object[0][]);
    }

}
