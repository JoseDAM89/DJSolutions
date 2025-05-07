package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class FormularioGenericoAlta extends JPanel {

    private final HashMap<String, JTextField> campos = new HashMap<>();
    private final JButton btnGuardar;

    public FormularioGenericoAlta(String[] etiquetas, ActionListener accionGuardar) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;
        for (String etiqueta : etiquetas) {
            gbc.gridx = 0;
            gbc.gridy = fila;
            add(new JLabel(etiqueta + ":"), gbc);

            gbc.gridx = 1;
            JTextField campo = new JTextField(20);
            campos.put(etiqueta, campo);
            add(campo, gbc);

            fila++;
        }

        // Botón guardar
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(accionGuardar);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnGuardar, gbc);
    }

    public HashMap<String, String> getValores() {
        HashMap<String, String> valores = new HashMap<>();
        for (String etiqueta : campos.keySet()) {
            valores.put(etiqueta, campos.get(etiqueta).getText().trim());
        }
        return valores;
    }

    public void limpiarCampos() {
        campos.values().forEach(campo -> campo.setText(""));
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }
}
