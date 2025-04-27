package FuncionesPrincipalesDeInventario;

import javax.swing.*;
import java.awt.*;

public class AgregarProductos extends JPanel {

    private JTextField campoNombre;
    private JTextArea campoDescripcion;
    private JTextField campoPrecio;
    private JTextField campoCantidad;
    private JTextField campoCategoria;
    private JButton btnGuardar;

    public AgregarProductos() {
        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        campoNombre = new JTextField(20);
        campoDescripcion = new JTextArea(5, 20);
        campoDescripcion.setLineWrap(true);
        campoDescripcion.setWrapStyleWord(true);

        campoPrecio = new JTextField(10);
        campoCantidad = new JTextField(10);
        campoCategoria = new JTextField(20);

        btnGuardar = new JButton("Guardar Producto");
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

        add(new JLabel("Nombre del Producto:"), gbc);
        gbc.gridx++;
        add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Descripción:"), gbc);
        gbc.gridx++;
        add(new JScrollPane(campoDescripcion), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Precio:"), gbc);
        gbc.gridx++;
        add(campoPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Cantidad:"), gbc);
        gbc.gridx++;
        add(campoCantidad, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Categoría:"), gbc);
        gbc.gridx++;
        add(campoCategoria, gbc);

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
        String nombre = campoNombre.getText().trim();
        String descripcion = campoDescripcion.getText().trim();
        String precioStr = campoPrecio.getText().trim();
        String cantidadStr = campoCantidad.getText().trim();
        String categoria = campoCategoria.getText().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
    }

    private void limpiarCampos() {
        campoNombre.setText("");
        campoDescripcion.setText("");
        campoPrecio.setText("");
        campoCantidad.setText("");
        campoCategoria.setText("");
    }
}
