package gui;

import Controladores.GenerarPresupuestoControlador;
import Modelos.Cliente;
import Modelos.Presupuesto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GenerarPresupuestoPanel extends JPanel {

    private JButton btnAgregarProducto;
    private JButton btnGenerarPDF;
    private JButton btnDatosCliente;
    private JPanel panelListaProductos;

    private GenerarPresupuestoControlador controller;
    private List<Presupuesto> listaProductos;
    private Cliente cliente;

    public GenerarPresupuestoPanel() {
        this.listaProductos = new ArrayList<>();

        setLayout(new BorderLayout());

        // Panel botones arriba
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAgregarProducto = new JButton("Agregar Producto");
        btnGenerarPDF = new JButton("Generar PDF");
        btnDatosCliente = new JButton("Datos Cliente");

        panelBotones.add(btnAgregarProducto);
        panelBotones.add(btnGenerarPDF);
        panelBotones.add(btnDatosCliente);

        add(panelBotones, BorderLayout.NORTH);

        // Panel lista de productos
        panelListaProductos = new JPanel();
        panelListaProductos.setLayout(new BoxLayout(panelListaProductos, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelListaProductos);
        add(scrollPane, BorderLayout.CENTER);

        // Listeners botones
        btnAgregarProducto.addActionListener((ActionEvent e) -> {
            Presupuesto nuevoProducto = pedirProducto();
            if (nuevoProducto != null) {
                if (controller == null) {
                    mostrarErrorControlador();
                    return;
                }
                controller.agregarProducto(nuevoProducto);
                agregarProductoVisual(nuevoProducto);
            }
        });

        btnGenerarPDF.addActionListener((ActionEvent e) -> {
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce los datos del cliente antes de generar el PDF.");
                return;
            }
            if (controller == null) {
                mostrarErrorControlador();
                return;
            }
            List<Presupuesto> productos = controller.getProductos();
            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay productos para generar el PDF.");
                return;
            }
            controller.generarPDF(productos, cliente);
            JOptionPane.showMessageDialog(this, "PDF creado con éxito.");
        });



        btnDatosCliente.addActionListener((ActionEvent e) -> {
            Cliente c = pedirDatosCliente();
            if (c != null) {
                cliente = c;
                JOptionPane.showMessageDialog(this, "Datos del cliente guardados:\n" + cliente.getCampoNombre());
            }
        });
    }

    public void setControlador(GenerarPresupuestoControlador controlador) {
        this.controller = controlador;
    }

    public void agregarProductoVisual(Presupuesto p) {
        listaProductos.add(p);

        JLabel label = new JLabel(p.getNombre() + " x" + p.getCantidad() + " (" + p.getPrecio() + " €/u)");
        label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        panelListaProductos.add(label);
        panelListaProductos.revalidate();
        panelListaProductos.repaint();
    }

    public List<Presupuesto> getListaProductos() {
        return new ArrayList<>(listaProductos);
    }

    public Cliente getCliente() {
        return cliente;
    }

    private Cliente pedirDatosCliente() {
        JTextField nombreField = new JTextField();
        JTextField cifField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField personaContactoField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField descripcionField = new JTextField();

        Object[] message = {
                "Nombre:", nombreField,
                "CIF:", cifField,
                "Email:", emailField,
                "Persona de Contacto:", personaContactoField,
                "Dirección:", direccionField,
                "Descripción:", descripcionField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Datos del Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (nombreField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y Email son obligatorios.");
                return null;
            }
            return new Cliente(
                    nombreField.getText().trim(),
                    cifField.getText().trim(),
                    emailField.getText().trim(),
                    personaContactoField.getText().trim(),
                    direccionField.getText().trim(),
                    descripcionField.getText().trim()
            );
        }
        return null;
    }

    private Presupuesto pedirProducto() {
        JTextField nombreField = new JTextField();
        JTextField cantidadField = new JTextField();
        JTextField precioField = new JTextField();

        Object[] message = {
                "Nombre del producto:", nombreField,
                "Cantidad:", cantidadField,
                "Precio unitario (€):", precioField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nombre = nombreField.getText().trim();
                int cantidad = Integer.parseInt(cantidadField.getText().trim());
                double precio = Double.parseDouble(precioField.getText().trim());

                if (nombre.isEmpty() || cantidad <= 0 || precio <= 0) {
                    JOptionPane.showMessageDialog(this, "Datos inválidos. Revisa nombre, cantidad y precio.");
                    return null;
                }
                return new Presupuesto(0, nombre, cantidad, precio);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad y Precio deben ser números válidos.");
                return null;
            }
        }
        return null;
    }

    private void mostrarErrorControlador() {
        JOptionPane.showMessageDialog(this, "Controlador no inicializado. Usa setControlador() antes de usar este panel.");
    }
}
