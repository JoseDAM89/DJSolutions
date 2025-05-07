package GUI;

import Datos.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditarGenerico {

    /**
     * Método que muestra el formulario para editar el registro de la tabla.
     *
     * @param tabla    El nombre de la tabla en la base de datos.
     * @param columnas Las columnas que se van a editar.
     * @param fila     La fila seleccionada de la tabla.
     */
    public static void mostrarFormularioDeEdicion(String tabla, String[] columnas, Object[] fila) {
        JTextField[] campos = new JTextField[fila.length];
        JPanel panel = new JPanel(new GridLayout(fila.length, 2));

        // Crear campos de texto para cada columna de la fila seleccionada
        for (int i = 0; i < fila.length; i++) {
            panel.add(new JLabel(columnas[i]));
            campos[i] = new JTextField(fila[i].toString());
            panel.add(campos[i]);
        }

        // Mostrar un cuadro de diálogo con el formulario de edición
        int resultado = JOptionPane.showConfirmDialog(null, panel, "Editar Registro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String columnaID = columnas[0];
            Object idValor = fila[0];

            // Actualizamos cada columna con los valores nuevos
            for (int i = 1; i < columnas.length; i++) {
                actualizarBD(tabla, columnas[i], campos[i].getText(), columnaID, idValor);
            }
        }
    }

    /**
     * Actualiza el registro en la base de datos.
     *
     * @param tabla     Nombre de la tabla.
     * @param columna   La columna que se actualizará.
     * @param nuevoValor El nuevo valor para esa columna.
     * @param columnaID El nombre de la columna ID.
     * @param idValor   El valor de la columna ID.
     */
    private static void actualizarBD(String tabla, String columna, String nuevoValor, String columnaID, Object idValor) {
        String sql = "UPDATE " + tabla + " SET " + columna + " = ? WHERE " + columnaID + " = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, nuevoValor);
            stmt.setObject(2, idValor);
            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        }
    }
}
