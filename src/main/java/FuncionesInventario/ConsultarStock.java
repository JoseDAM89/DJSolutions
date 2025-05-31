package FuncionesInventario;

import datos.ConexionBD;
import gui.ListadosGenerico;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ConsultarStock extends JPanel {

    public ConsultarStock() {
        setLayout(new GridLayout(2, 1, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Productos
        add(crearListadoStock(
                "Productos",
                "productos",
                new String[]{"ID", "Nombre", "Stock"},
                "codproduct", "nombreproduct", "stockproduct"
        ));

        // Panel Materias Primas
        add(crearListadoStock(
                "Materia Prima",
                "MateriasPrimas",
                new String[]{"ID", "Descripción", "Stock"},
                "idmateriaprima", "descripcionmaterial", "stockmetros"
        ));
    }

    private JPanel crearListadoStock(String titulo, String tablaBD, String[] columnas,
                                     String idCol, String nombreCol, String stockCol) {
        Object[][] datos = obtenerDatos(tablaBD, idCol, nombreCol, stockCol);

        return new ListadosGenerico(
                "Stock de " + titulo,
                columnas,
                datos,

                (fila, tabla) -> {
                    JOptionPane.showMessageDialog(this, "Edición no disponible en este listado.");
                    return new JPanel();
                },
                fila -> JOptionPane.showMessageDialog(this, "Eliminación no permitida en este listado."),
                false // No permitir botones de editar/eliminar
        );
    }

    private Object[][] obtenerDatos(String tabla, String idCol, String nombreCol, String stockCol) {
        ArrayList<Object[]> listaDatos = new ArrayList<>();

        String sql = String.format("SELECT %s AS id, %s AS nombre, %s AS stock FROM \"%s\"",
                idCol, nombreCol, stockCol, tabla);

        try (Connection conn = ConexionBD.getConexion();
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
