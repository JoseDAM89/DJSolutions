package FuncionesFacturas;

import datos.ClienteDAO;
import datos.FacturaDAO;
import datos.ProductoDAO;
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
    private JComboBox<Cliente> comboClientes;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private List<LineaFactura> lineasFactura = new ArrayList<>();

    public GenerarFacturaPanel() {
        setLayout(new BorderLayout());

        // Panel superior: Cliente y botón Agregar producto
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

        // Tabla inferior: productos añadidos
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Descripción", "Cantidad", "Precio", "Subtotal"}, 0);
        tablaProductos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
    }

    private void cargarClientes() {
        List<Cliente> clientes = new ClienteDAO().listarTodos();
        for (Cliente c : clientes) {
            comboClientes.addItem(c);
        }
    }

    private void agregarProducto(ActionEvent e) {
        List<Producto> productos = new ProductoDAO().listarTodos();

        Producto seleccionado = (Producto) JOptionPane.showInputDialog(
                this,
                "Selecciona un producto",
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
                        seleccionado.getNombreproduct(),
                        cantidad,
                        seleccionado.getPrecioproduct()
                );
                lineasFactura.add(linea);
                modeloTabla.addRow(new Object[]{
                        linea.getIdProducto(),
                        linea.getDescripcion(),
                        linea.getCantidad(),
                        String.format("%.2f €", linea.getPrecioUnitario()),
                        String.format("%.2f €", linea.getSubtotal())
                });
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        }
    }

    private void generarFactura(ActionEvent e) {
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        if (cliente == null || lineasFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos.");
            return;
        }

        Factura factura = new Factura(cliente.getIdcliente(), lineasFactura);
        int id = new FacturaDAO().insertarFactura(factura);

        if (id > 0) {
            JOptionPane.showMessageDialog(this, "Factura generada correctamente con ID: " + id);
            lineasFactura.clear();
            modeloTabla.setRowCount(0);
        } else {
            JOptionPane.showMessageDialog(this, "Error al generar la factura.");
        }
    }
}
