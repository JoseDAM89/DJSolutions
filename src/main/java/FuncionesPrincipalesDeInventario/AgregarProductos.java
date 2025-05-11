package FuncionesPrincipalesDeInventario;

import Datos.ConexionBD;
import GUI.FormularioGenericoAlta;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Clase que se encarga de construir el formulario para agregar nuevos productos.
 */
public class AgregarProductos {

    /**
     * Método que construye el formulario genérico para dar de alta un producto.
     * @return JPanel listo para insertar en una ventana
     */
    public JPanel construirFormulario() {
        // Campos que queremos que aparezcan en el formulario
        String[] campos = {"Nombre", "Precio", "Stock"};

        // Usamos un array para poder usar la variable dentro del ActionListener
        final FormularioGenericoAlta[] formulario = new FormularioGenericoAlta[1];

        // Definimos la acción del botón guardar
        ActionListener accionGuardar = e -> {
            HashMap<String, String> valores = formulario[0].getValores();

            // Validamos que no haya campos vacíos
            if (valores.values().stream().anyMatch(String::isEmpty)) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.");
                return;
            }

            // Insertamos el producto en la base de datos
            boolean guardado = insertarProducto(valores);

            if (guardado) {
                JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
                formulario[0].limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar el producto.");
            }
        };

        // Creamos el formulario genérico pasándole los campos y la acción del botón
        formulario[0] = new FormularioGenericoAlta(campos, accionGuardar);

        return formulario[0];
    }

    /**
     * Método que inserta un producto en la base de datos con los valores proporcionados.
     * @param valores Mapa con los valores de los campos del formulario.
     * @return true si se insertó correctamente, false en caso contrario.
     */
    private boolean insertarProducto(HashMap<String, String> valores) {
        String tabla = "productos";
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tabla).append(" (");
        StringBuilder placeholders = new StringBuilder();

        for (String campo : valores.keySet()) {
            sql.append("campo").append(campo).append(","); // prefijamos con "campo" como en clientes
            placeholders.append("?,");
        }

        // Eliminar la última coma
        sql.setLength(sql.length() - 1);
        placeholders.setLength(placeholders.length() - 1);

        sql.append(") VALUES (").append(placeholders).append(")");

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (String valor : valores.values()) {
                stmt.setObject(index++, valor);
            }

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
