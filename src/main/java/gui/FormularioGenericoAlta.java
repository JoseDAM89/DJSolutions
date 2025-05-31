package gui;

import datos.MateriaPrimaDAO;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class FormularioGenericoAlta extends JPanel {

    private final HashMap<String, JComponent> campos = new HashMap<>();
    private final JButton btnGuardar;
    private final Map<Integer, String> materiasDisponibles;

    private final JLabel etiquetaTipoFormulario;

    public FormularioGenericoAlta(Map<String, String> camposDefinicion, ActionListener accionGuardar) {
        this(camposDefinicion, accionGuardar, null);
    }

    public FormularioGenericoAlta(Map<String, String> camposDefinicion, ActionListener accionGuardar, String tipoFormulario) {
        setLayout(new GridBagLayout());
        setBackground(new Color(53, 107, 140,100));

        materiasDisponibles = MateriaPrimaDAO.obtenerTodas();

        // Panel contenedor (card)
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 200), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int fila = 0;

        if (tipoFormulario != null && !tipoFormulario.isBlank()) {
            etiquetaTipoFormulario = new JLabel(tipoFormulario);
            etiquetaTipoFormulario.setFont(new Font("Segoe UI", Font.BOLD, 18));
            etiquetaTipoFormulario.setForeground(new Color(30, 80, 160));
            etiquetaTipoFormulario.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(30, 80, 160)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            gbc.gridx = 0;
            gbc.gridy = fila++;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            card.add(etiquetaTipoFormulario, gbc);
        } else {
            etiquetaTipoFormulario = null;
        }

        // Agregar campos
        for (String etiqueta : camposDefinicion.keySet()) {
            String tipo = camposDefinicion.get(etiqueta);

            JLabel label = new JLabel(etiqueta + ":");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            label.setForeground(new Color(50, 50, 50));

            gbc.gridx = 0;
            gbc.gridy = fila;
            gbc.gridwidth = 1;
            card.add(label, gbc);

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
            card.add(campo, gbc);
            fila++;
        }

        btnGuardar = new JButton("Guardar");
        estilizarBoton(btnGuardar);
        btnGuardar.addActionListener(accionGuardar);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(btnGuardar, gbc);

        // Añadir el panel "card" al principal
        GridBagConstraints wrap = new GridBagConstraints();
        wrap.gridx = 0;
        wrap.gridy = 0;
        wrap.weightx = 1.0;
        wrap.weighty = 1.0;
        wrap.fill = GridBagConstraints.BOTH;
        wrap.insets = new Insets(30, 60, 30, 60);
        add(card, wrap);

        // Vinculación de campos
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

    // === Estilos ===
    private void estilizarTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 220), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(new Color(50, 50, 50));
    }

    private void estilizarCombo(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(new Color(50, 50, 50));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 220), 1, true),
                new EmptyBorder(2, 5, 2, 5)
        ));
        comboBox.setPreferredSize(new Dimension(200, 30));
    }

    private void estilizarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBackground(new Color(63, 114, 175));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(63, 114, 175), 1, true),
                new EmptyBorder(10, 25, 10, 25)
        ));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(52, 96, 148));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(63, 114, 175));
            }
        });
    }
}
