package FuncionesPrincipalesDeClientes;

import javax.swing.*;
import java.awt.*;

public class AltaCliente extends JPanel {

    private JTextField campoNombre;
    private JTextField campoCIF;
    private JTextField campoEmail;
    private JTextField campoPersonaDeContacto;
    private JTextField campoDireccion;
    private JTextArea campoDescripcion;
    private JButton btnGuardar;

    public AltaCliente() {
        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        campoNombre = new JTextField(20);
        campoDescripcion = new JTextArea(5, 20);
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);
        campoCIF = new JTextField(10);
        campoEmail = new JTextField(30);
        campoPersonaDeContacto = new JTextField(20);
        campoDireccion = new JTextField(50);


        btnGuardar = new JButton("Alta Cliente");
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245)); // Fondo claro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("CIF:"), gbc);
        gbc.gridx++;
        add(campoCIF, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Nombre"), gbc);
        gbc.gridx++;
        add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Email"), gbc);
        gbc.gridx++;
        add(campoEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Persona de Contacto"), gbc);
        gbc.gridx++;
        add(campoPersonaDeContacto, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Direccion"), gbc);
        gbc.gridx++;
        add(campoDireccion, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Descripcion"), gbc);
        gbc.gridx++;
        add(campoDescripcion, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnGuardar, gbc);
    }

    private void agregarEventos() {
        btnGuardar.addActionListener(e -> guardarProducto());
    }

    private void guardarProducto() {
        String cif = campoCIF.getText().trim();
        String nombre = campoNombre.getText().trim();
        String email = campoEmail.getText().trim();
        String personaCOntacto = campoPersonaDeContacto.getText().trim();
        String direccion = campoDireccion.getText().trim();
        String descripcion = campoDescripcion.getText().trim();


        if (cif.isEmpty() ||nombre.isEmpty() || email.isEmpty() || personaCOntacto.isEmpty() || direccion.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
/*
        // Aquí podrías validar que precio y cantidad sean números
        try {
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);

            // Aquí podrías guardar el producto en una base de datos o en memoria
            JOptionPane.showMessageDialog(this, "Producto guardado exitosamente:\n" +
                    "Nombre: " + nombre +
                    "\nDescripción: " + descripcion +
                    "\nPrecio: " + precio +
                    "\nCantidad: " + cantidad +
                    "\nCategoría: " + categoria, "Producto Guardado", JOptionPane.INFORMATION_MESSAGE);

            limpiarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y Cantidad deben ser valores numéricos válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }

*/
    }

    private void limpiarCampos() {
        campoCIF.setText("");
        campoNombre.setText("");
        campoEmail.setText("");
        campoPersonaDeContacto.setText("");
        campoDireccion.setText("");
        campoDescripcion.setText("");
    }
}
