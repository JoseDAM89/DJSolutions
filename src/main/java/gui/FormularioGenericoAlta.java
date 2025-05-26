package gui;

import datos.MateriaPrimaDAO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FormularioGenericoAlta extends JPanel {

    private final HashMap<String, JComponent> campos = new HashMap<>();
    private final JButton btnGuardar;
    private final Map<Integer, String> materiasDisponibles;

    public FormularioGenericoAlta(Map<String, String> camposDefinicion, ActionListener accionGuardar) {
        setLayout(new GridBagLayout());
        setBackground(new Color(250, 250, 255));  // Fondo suave

        materiasDisponibles = MateriaPrimaDAO.obtenerTodas();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int fila = 0;
        for (String etiqueta : camposDefinicion.keySet()) {
            String tipo = camposDefinicion.get(etiqueta);

            // Etiqueta estilizada
            JLabel label = new JLabel(etiqueta + ":");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setForeground(new Color(50, 50, 50));

            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            add(label, gbc);

            gbc.gridx = 1;
            JComponent campo;

            if ("boolean".equalsIgnoreCase(tipo)) {
                JComboBox<String> combo = new JComboBox<>(new String[]{"Sí", "No"});
                estilizarCombo(combo);
                combo.setName("comboBoolean");
                campo = combo;

            } else if (etiqueta.equalsIgnoreCase("ID Materia")) {
                JComboBox<String> comboMateria = new JComboBox<>();
                comboMateria.setName("comboMateria");
                comboMateria.addItem("0 - Ninguna");

                for (Map.Entry<Integer, String> entry : materiasDisponibles.entrySet()) {
                    comboMateria.addItem(entry.getKey() + " - " + entry.getValue());
                }

                estilizarCombo(comboMateria);
                campo = comboMateria;

            } else {
                JTextField textField = new JTextField(20);
                estilizarTextField(textField);
                campo = textField;
            }

            campos.put(etiqueta, campo);
            add(campo, gbc);
            fila++;
        }

        // Botón guardar estilizado
        btnGuardar = new JButton("💾 Guardar");
        estilizarBoton(btnGuardar);
        btnGuardar.addActionListener(accionGuardar);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnGuardar, gbc);

        // Vinculación Materia Prima ↔ ID Materia
        JComponent comboMateriaPrima = campos.get("Materia Prima");
        JComponent campoIDMateria = campos.get("ID Materia");

        if (comboMateriaPrima instanceof JComboBox<?> combo && campoIDMateria instanceof JComboBox<?> comboMateria) {
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboMateria.getModel();

            combo.addActionListener(e -> {
                boolean esSi = combo.getSelectedItem().toString().equalsIgnoreCase("Sí");

                if (esSi) {
                    int idx = model.getIndexOf("0 - Ninguna");
                    if (idx != -1) model.removeElementAt(idx);
                    comboMateria.setEnabled(true);
                    if (comboMateria.getSelectedIndex() == -1 && model.getSize() > 0)
                        comboMateria.setSelectedIndex(0);
                } else {
                    if (model.getIndexOf("0 - Ninguna") == -1)
                        model.insertElementAt("0 - Ninguna", 0);
                    comboMateria.setSelectedItem("0 - Ninguna");
                    comboMateria.setEnabled(false);
                }
            });

            boolean esSi = combo.getSelectedItem().toString().equalsIgnoreCase("Sí");
            comboMateria.setEnabled(esSi);

            if (!esSi) {
                if (model.getIndexOf("0 - Ninguna") == -1)
                    model.insertElementAt("0 - Ninguna", 0);
                comboMateria.setSelectedItem("0 - Ninguna");
            } else {
                int idx = model.getIndexOf("0 - Ninguna");
                if (idx != -1) model.removeElementAt(idx);
            }
        }
    }

    public HashMap<String, String> getValores() {
        HashMap<String, String> valores = new HashMap<>();

        for (String etiqueta : campos.keySet()) {
            JComponent campo = campos.get(etiqueta);

            if (campo instanceof JTextField textField) {
                valores.put(etiqueta, textField.getText().trim());

            } else if (campo instanceof JComboBox<?> combo) {
                String seleccion = combo.getSelectedItem().toString();

                if ("comboMateria".equals(combo.getName())) {
                    String id = seleccion.split(" - ")[0].trim();
                    valores.put(etiqueta, id);
                } else {
                    valores.put(etiqueta, seleccion);
                }
            }
        }

        return valores;
    }

    public void limpiarCampos() {
        for (Map.Entry<String, JComponent> entry : campos.entrySet()) {
            JComponent campo = entry.getValue();

            if (campo instanceof JTextField textField) {
                textField.setText("");
            } else if (campo instanceof JComboBox<?> combo) {
                combo.setSelectedIndex(0);
            }

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

    // ==== Métodos de estilizado ====

    private void estilizarTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 200), 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.DARK_GRAY);
    }

    private void estilizarCombo(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new LineBorder(new Color(180, 180, 200), 1, true));
        comboBox.setPreferredSize(new Dimension(200, 30));
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBackground(new Color(88, 130, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(88, 130, 255), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));
    }
}
