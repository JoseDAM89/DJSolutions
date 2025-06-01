package gui;

import Controladores.GenerarFacturaControlador;
import modelos.Cliente;
import modelos.Factura;
import modelos.LineaFactura;
import modelos.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class GenerarFacturaPanel extends JPanel {

    private final GenerarFacturaControlador controlador = new GenerarFacturaControlador();

    private JComboBox<Cliente> comboClientes;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private List<LineaFactura> lineasFactura = new ArrayList<>();

    public GenerarFacturaPanel() {
        setLayout(new BorderLayout());

        // 🔝 Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Cliente:"));

        comboClientes = new JComboBox<>();
        cargarClientes();
        panelSuperior.add(comboClientes);

        JButton btnAgregarProducto = new JButton("Agregar producto");
        btnAgregarProducto.addActionListener(this::agregarProducto);
        panelSuperior.add(btnAgregarProducto);

        JButton btnGenerarFactura = new JButton("Generar factura");
        btnGenerarFactura.addActionListener(this::generarFactura);
        panelSuperior.add(btnGenerarFactura);

        add(panelSuperior, BorderLayout.NORTH);

        // 📋 Tabla productos añadidos
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Cantidad", "Precio", "Subtotal"}, 0);
        tablaProductos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
    }

    private void cargarClientes() {
        List<Cliente> clientes = controlador.obtenerClientes();
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }
    }

    private void agregarProducto(ActionEvent e) {
        List<Producto> productos = controlador.obtenerProductos();

        Producto seleccionado = (Producto) JOptionPane.showInputDialog(
                this,
                "Selecciona un producto:",
                "Productos",
                JOptionPane.PLAIN_MESSAGE,
                null,
                productos.toArray(),
                productos.get(0)
        );

        if (seleccionado != null) {
            String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad:");
            try {
                int cantidad = Integer.parseInt(cantidadStr);

                LineaFactura linea = new LineaFactura(
                        seleccionado.getCodproduct(),
                        seleccionado.getNombreproduct(), // usamos el nombre como "descripción"
                        cantidad,
                        seleccionado.getPrecioproduct()
                );

                lineasFactura.add(linea);

                modeloTabla.addRow(new Object[]{
                        linea.getIdProducto(),
                        linea.getNombreProducto(),
                        cantidad,
                        String.format("%.2f €", linea.getPrecioUnitario()),
                        String.format("%.2f €", linea.getSubtotal())
                });

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Cantidad inválida.");
            }
        }
    }

    private void generarFactura(ActionEvent e) {
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();

        if (cliente == null || lineasFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un cliente y al menos un producto.");
            return;
        }

        Factura factura = new Factura(cliente.getIdcliente(), lineasFactura);
        int id = controlador.guardarFactura(factura);

        if (id > 0) {
            JOptionPane.showMessageDialog(this, "✅ Factura generada con ID: " + id);
            lineasFactura.clear();
            modeloTabla.setRowCount(0);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error al generar la factura.");
        }
    }
}
