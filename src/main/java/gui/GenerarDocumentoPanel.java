package gui;

import Controladores.GenerarFacturaControlador;
import Controladores.GenerarPresupuestoControlador;
import modelos.Cliente;
import modelos.LineaFactura;
import modelos.LineaPresupuesto;
import modelos.Producto;
import modelos.Factura;
import modelos.Presupuesto;
import datos.ProductoDAO;
import gui.dialogos.SelectorClienteDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GenerarDocumentoPanel extends JPanel {

    public enum TipoDocumento { PRESUPUESTO, FACTURA }

    private TipoDocumento tipo;
    private Cliente cliente;

    private JTable tablaProductos;
    private JTable tablaDocumento;
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloDocumento;
    private JTextField campoBuscar;
    private JButton btnAgregar, btnEliminar, btnGenerar, btnDatosCliente;
    private JLabel labelClienteSeleccionado;

    // Controladores
    private final GenerarPresupuestoControlador controladorPresupuesto = new GenerarPresupuestoControlador();
    private final GenerarFacturaControlador controladorFactura = new GenerarFacturaControlador();
    private final List<Object> lineas = new ArrayList<>();

    public GenerarDocumentoPanel(TipoDocumento tipo) {
        this.tipo = tipo;
        setLayout(new BorderLayout());

        // ---------------- PANEL SUPERIOR ----------------
        JPanel panelSuperior = new JPanel(new BorderLayout());

        campoBuscar = new JTextField();
        panelSuperior.add(new JLabel("Buscar producto:"), BorderLayout.WEST);
        panelSuperior.add(campoBuscar, BorderLayout.CENTER);

        String[] columnasProductos = {"ID", "Nombre", "Precio"};
        modeloProductos = new DefaultTableModel(columnasProductos, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloProductos);
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        scrollProductos.setPreferredSize(new Dimension(0, 250));

        JPanel contenedorSuperior = new JPanel(new BorderLayout());
        contenedorSuperior.add(panelSuperior, BorderLayout.NORTH);
        contenedorSuperior.add(scrollProductos, BorderLayout.CENTER);

        add(contenedorSuperior, BorderLayout.NORTH);

        // ---------------- PANEL INFERIOR ----------------
        String[] columnasDocumento = {"ID", "Nombre", "Cantidad", "Precio/u", "Total"};
        modeloDocumento = new DefaultTableModel(columnasDocumento, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDocumento = new JTable(modeloDocumento);
        JScrollPane scrollDocumento = new JScrollPane(tablaDocumento);
        scrollDocumento.setPreferredSize(new Dimension(0, 250));

        labelClienteSeleccionado = new JLabel("Cliente seleccionado: (ninguno)");
        labelClienteSeleccionado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(labelClienteSeleccionado, BorderLayout.NORTH);
        panelInferior.add(scrollDocumento, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        // ---------------- BOTONES ----------------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnDatosCliente = new JButton("Datos Cliente");
        btnAgregar = new JButton("Agregar Producto");
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnGenerar = new JButton(tipo == TipoDocumento.PRESUPUESTO ? "Generar Presupuesto" : "Generar Factura");

        panelBotones.add(btnDatosCliente);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGenerar);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelBotones);
        panelCentro.add(Box.createVerticalStrut(10));
        add(panelCentro, BorderLayout.CENTER);

        // ---------------- EVENTOS ----------------
        campoBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        btnDatosCliente.addActionListener(e -> {
            SelectorClienteDialog selector = new SelectorClienteDialog(SwingUtilities.getWindowAncestor(this));
            Cliente seleccionado = selector.mostrarYObtenerSeleccion();
            if (seleccionado != null) {
                this.cliente = seleccionado;
                labelClienteSeleccionado.setText("Cliente seleccionado: " + cliente.getCampoNombre());
            }
        });

        btnAgregar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para añadir.");
                return;
            }

            int id = (int) modeloProductos.getValueAt(fila, 0);
            String nombre = (String) modeloProductos.getValueAt(fila, 1);
            double precio = (double) modeloProductos.getValueAt(fila, 2);

            String input = JOptionPane.showInputDialog(this, "Cantidad:");
            try {
                int cantidad = Integer.parseInt(input);
                if (cantidad <= 0) throw new NumberFormatException();

                double total = cantidad * precio;
                modeloDocumento.addRow(new Object[]{id, nombre, cantidad, String.format("%.3f", precio), String.format("%.3f", total)});

                if (tipo == TipoDocumento.PRESUPUESTO) {
                    LineaPresupuesto linea = new LineaPresupuesto(id, nombre, cantidad, precio);
                    controladorPresupuesto.agregarProducto(linea);
                    lineas.add(linea);
                } else {
                    LineaFactura linea = new LineaFactura(id, nombre, cantidad, precio);
                    lineas.add(linea);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaDocumento.getSelectedRow();
            if (fila != -1) {
                modeloDocumento.removeRow(fila);
                lineas.remove(fila);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            }
        });

        btnGenerar.addActionListener(e -> {
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Primero selecciona un cliente.");
                return;
            }

            if (lineas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Añade al menos un producto.");
                return;
            }

            if (tipo == TipoDocumento.PRESUPUESTO) {
                Presupuesto p = controladorPresupuesto.guardarPresupuestoEnBD(cliente);
                if (p != null) {
                    JOptionPane.showMessageDialog(this, "✅ Presupuesto guardado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al guardar presupuesto.");
                }
            } else {
                Factura f = new Factura(cliente.getIdcliente(), (List<LineaFactura>) (List<?>) lineas);
                int id = controladorFactura.guardarFactura(f);
                if (id > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Factura generada con ID: " + id);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al generar factura.");
                }
            }

            // Limpiar después de guardar
            modeloDocumento.setRowCount(0);
            lineas.clear();
        });

        cargarProductos("");
    }

    private void cargarProductos(String filtro) {
        modeloProductos.setRowCount(0);
        List<Producto> productos = ProductoDAO.listarTodos();

        productos.stream()
                .filter(p -> p.getNombreproduct().toLowerCase().contains(filtro.toLowerCase()))
                .forEach(p -> modeloProductos.addRow(new Object[]{
                        p.getCodproduct(),
                        p.getNombreproduct(),
                        p.getPrecioproduct()
                }));
    }

    private void filtrar() {
        String texto = campoBuscar.getText();
        cargarProductos(texto);
    }
}
