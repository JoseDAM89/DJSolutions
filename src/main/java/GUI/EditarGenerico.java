package GUI;

import Datos.ConexionBD;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditarGenerico {

    /**
     * Muestra un formulario para editar los datos de una fila seleccionada de una tabla.
     *
     * @param tabla    Nombre de la tabla en la base de datos.
     * @param columnas Nombres visibles de las columnas (los que ve el usuario).
     * @param fila     Datos actuales de la fila seleccionada.
     */
    public static void mostrarFormularioDeEdicion(String tabla, String[] columnas, Object[] fila) {
        JTextField[] campos = new JTextField[fila.length];
        JPanel panel = new JPanel(new GridLayout(fila.length, 2));

        // Creamos los campos de texto para cada dato de la fila
        for (int i = 0; i < fila.length; i++) {
            panel.add(new JLabel(columnas[i]));  // Etiqueta visible
            campos[i] = new JTextField(fila[i].toString()); // Valor actual editable
            panel.add(campos[i]);
        }

        // Mostramos el cuadro de diálogo con el formulario
        int resultado = JOptionPane.showConfirmDialog(null, panel, "Editar Registro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            // Obtenemos un mapeo entre nombre visible → nombre real en BD
            Map<String, String> mapeo = generarMapeoColumnas(tabla, columnas);

            // Primera columna: ID (clave primaria)
            String columnaID = mapeo.getOrDefault(columnas[0], columnas[0]);
            Object idValor = fila[0]; // Valor actual del ID

            // Recorremos cada campo (excepto el ID) y actualizamos en la BD
            for (int i = 1; i < columnas.length; i++) {
                String columnaBD = mapeo.getOrDefault(columnas[i], columnas[i]); // nombre real en BD
                actualizarBD(tabla, columnaBD, campos[i].getText(), columnaID, idValor);
            }
        }
    }

    /**
     * Ejecuta un UPDATE en la base de datos con el nuevo valor para una columna.
     *
     * @param tabla      Nombre de la tabla.
     * @param columna    Nombre real de la columna en la base de datos.
     * @param nuevoValor Nuevo valor que se va a guardar.
     * @param columnaID  Nombre de la columna clave primaria (ej: idcliente).
     * @param idValor    Valor del ID del registro a actualizar.
     */
    private static void actualizarBD(String tabla, String columna, String nuevoValor, String columnaID, Object idValor) {
        String sql = "UPDATE " + tabla + " SET " + columna + " = ? WHERE " + columnaID + " = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, nuevoValor); // Valor nuevo para la columna
            stmt.setObject(2, idValor);    // Valor del ID para localizar la fila
            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        }
    }

    /**
     * Genera un mapa entre los nombres visibles de las columnas y sus nombres reales en la base de datos.
     * Este método asume que el orden de las columnas visibles y reales coincide.
     *
     * @param tabla    Nombre de la tabla.
     * @param visibles Array de nombres visibles (los que se muestran en pantalla).
     * @return Mapa con clave = nombre visible, valor = nombre real de columna.
     */
    private static Map<String, String> generarMapeoColumnas(String tabla, String[] visibles) {
        String[] reales = obtenerNombresReales(tabla); // Extrae desde la BD
        Map<String, String> mapeo = new HashMap<>();

        // Asocia cada nombre visible con el nombre real correspondiente
        for (int i = 0; i < visibles.length && i < reales.length; i++) {
            mapeo.put(visibles[i], reales[i]);
        }

        return mapeo;
    }

    /**
     * Obtiene los nombres reales de las columnas de una tabla en orden.
     * Usa `information_schema.columns`, que es una tabla interna de PostgreSQL.
     *
     * @param tabla Nombre de la tabla en minúsculas.
     * @return Array con los nombres reales de columna.
     */
    private static String[] obtenerNombresReales(String tabla) {
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT column_name FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position")) {

            stmt.setString(1, tabla.toLowerCase()); // PostgreSQL guarda los nombres en minúsculas
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> columnas = new ArrayList<>();
            while (rs.next()) {
                columnas.add(rs.getString("column_name"));
            }

            return columnas.toArray(new String[0]);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener columnas reales: " + e.getMessage());
            return new String[0];
        }
    }
}
