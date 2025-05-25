package GUI;

import Datos.MateriaPrimaDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormularioGenericoAlta extends JPanel {

    private final HashMap<String, JComponent> campos = new HashMap<>();
    private final JButton btnGuardar;
    private final Map<Integer, String> materiasDisponibles; // Para mapear nombres con IDs

    public FormularioGenericoAlta(Map<String, String> camposDefinicion, ActionListener accionGuardar) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        materiasDisponibles = MateriaPrimaDAO.obtenerTodas();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;
        for (String etiqueta : camposDefinicion.keySet()) {
            String tipo = camposDefinicion.get(etiqueta);

            gbc.gridx = 0;
            gbc.gridy = fila;
            add(new JLabel(etiqueta + ":"), gbc);

            gbc.gridx = 1;

            JComponent campo;

            // Booleano → Combo "Sí/No"
            if ("boolean".equalsIgnoreCase(tipo)) {
                JComboBox<String> combo = new JComboBox<>(new String[]{"Sí", "No"});
                combo.setName("comboBoolean");
                campo = combo;

            } else if (etiqueta.equalsIgnoreCase("ID Materia")) {
                // Combo con materias primas disponibles
                JComboBox<String> comboMateria = new JComboBox<>();
                comboMateria.setName("comboMateria"); // Marcador para el getValores

                comboMateria.addItem("0 - Ninguna"); // opción por defecto

                for (Map.Entry<Integer, String> entry : materiasDisponibles.entrySet()) {
                    comboMateria.addItem(entry.getKey() + " - " + entry.getValue());
                }

                campo = comboMateria;

            } else {
                campo = new JTextField(20);
            }

            campos.put(etiqueta, campo);
            add(campo, gbc);
            fila++;
        }

        // Botón Guardar
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(accionGuardar);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnGuardar, gbc);

        // 🔄 Activar/desactivar selector de materia según combo de "Materia Prima"
        JComponent comboMateriaPrima = campos.get("Materia Prima");
        JComponent campoIDMateria = campos.get("ID Materia");

        if (comboMateriaPrima instanceof JComboBox<?> combo && campoIDMateria instanceof JComboBox<?> comboMateria) {
            combo.addActionListener(e -> {
                boolean esSi = combo.getSelectedItem().toString().equalsIgnoreCase("Sí");
                comboMateria.setEnabled(esSi);
                if (!esSi) {
                    comboMateria.setSelectedIndex(0); // "0 - Ninguna"
                }
            });

            // Estado inicial
            if (!combo.getSelectedItem().toString().equalsIgnoreCase("Sí")) {
                comboMateria.setEnabled(false);
                ((JComboBox<?>) campoIDMateria).setSelectedIndex(0);
            }
        }
    }

    /**
     * Obtiene los valores del formulario en forma de HashMap.
     */
    public HashMap<String, String> getValores() {
        HashMap<String, String> valores = new HashMap<>();

        for (String etiqueta : campos.keySet()) {
            JComponent campo = campos.get(etiqueta);

            if (campo instanceof JTextField textField) {
                valores.put(etiqueta, textField.getText().trim());

            } else if (campo instanceof JComboBox<?> combo) {
                String seleccion = combo.getSelectedItem().toString();

                // Si es combo de materia prima → devolver solo el ID
                if (combo.getName() != null && combo.getName().equals("comboMateria")) {
                    // Formato esperado: "ID - Nombre"
                    String id = seleccion.split(" - ")[0].trim();
                    valores.put(etiqueta, id);
                } else {
                    // Si es combo booleano ("Sí"/"No")
                    valores.put(etiqueta, seleccion);
                }
            }
        }

        return valores;
    }

    /**
     * Limpia todos los campos del formulario.
     */
    public void limpiarCampos() {
        for (Map.Entry<String, JComponent> entry : campos.entrySet()) {
            JComponent campo = entry.getValue();

            if (campo instanceof JTextField textField) {
                textField.setText("");

            } else if (campo instanceof JComboBox<?> combo) {
                combo.setSelectedIndex(0);
            }

            // Desactiva "ID Materia" si corresponde
            if (entry.getKey().equalsIgnoreCase("ID Materia")) {
                JComponent comboMateria = campos.get("Materia Prima");
                if (comboMateria instanceof JComboBox<?> cm) {
                    boolean esSi = cm.getSelectedItem().toString().equalsIgnoreCase("Sí");
                    campo.setEnabled(esSi);
                }
            }
        }
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }
}
