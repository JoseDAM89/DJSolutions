package FuncionesPrincipalesDeInventario;

import Datos.ConexionBD;
import GUI.FormularioGenericoAlta;
import GUI.Vprin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class AgregarProductos extends JPanel {

    private final Vprin ventana;

    public AgregarProductos(Vprin ventana) {
        this.ventana = ventana;
        mostrarFormulario();
    }



    private void mostrarFormulario() {
        String[] columnas = obtenerColumnasSinID(); // Opcionalmente puedes excluir la columna ID

        // Creamos el panel del formulario
        FormularioGenericoAlta panelFormulario = new FormularioGenericoAlta(columnas, null);

        // Creamos la ventana y le incrustamos el formulario
        JFrame frame = new JFrame("Agregar Producto");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(panelFormulario);
        frame.pack();
        frame.setLocationRelativeTo(null);

        // Añadimos la acción del botón guardar
        panelFormulario.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, String> valores = panelFormulario.getValores();
                if (insertarProducto(valores)) {
                    JOptionPane.showMessageDialog(frame, "Producto agregado correctamente");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error al agregar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private String[] obtenerColumnasSinID() {
        // Hardcodeado por simplicidad. Si quieres hacerlo dinámico, necesitarías consultar metadata y excluir ID si es autoincremental.
        return new String[]{"nombre", "precio", "stock"}; // Cambia esto según tus columnas reales sin incluir ID
    }

    private boolean insertarProducto(HashMap<String, String> valores) {
        String tabla = "productos";
        StringBuilder sql = new StringBuilder("INSERT INTO " + tabla + " (");
        StringBuilder placeholders = new StringBuilder();

        for (String campo : valores.keySet()) {
            sql.append(campo).append(",");
            placeholders.append("?,");
        }

        // Eliminar última coma
        sql.setLength(sql.length() - 1);
        placeholders.setLength(placeholders.length() - 1);

        sql.append(") VALUES (").append(placeholders).append(")");

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (String valor : valores.values()) {
                stmt.setObject(index++, valor); // Aquí podrías hacer conversiones según el tipo
            }

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
