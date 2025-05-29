package gui;

import Controladores.GenerarPresupuestoControlador;
import Modelos.Cliente;
import Modelos.Producto;
import Modelos.Presupuesto;
import datos.ProductoDAO;
import gui.dialogos.SelectorClienteDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GenerarPresupuestoPanel extends JPanel {

    private JTable tablaProductos;
    private JTable tablaPresupuesto;
    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloPresupuesto;
    private JTextField campoBuscar;
    private JButton btnAgregar, btnEliminar, btnGenerarPDF, btnDatosCliente;
    private JLabel labelClienteSeleccionado;

    private GenerarPresupuestoControlador controlador;
    private Cliente cliente;

    public GenerarPresupuestoPanel() {
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
        String[] columnasPresupuesto = {"ID", "Nombre", "Cantidad", "Precio/u", "Total"};
        modeloPresupuesto = new DefaultTableModel(columnasPresupuesto, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPresupuesto = new JTable(modeloPresupuesto);
        JScrollPane scrollPresupuesto = new JScrollPane(tablaPresupuesto);
        scrollPresupuesto.setPreferredSize(new Dimension(0, 250));

        // ---------- Label del cliente (ANTES de la tabla inferior) ----------
        labelClienteSeleccionado = new JLabel("Cliente seleccionado: (ninguno)");
        labelClienteSeleccionado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BorderLayout());
        panelInferior.add(labelClienteSeleccionado, BorderLayout.NORTH);
        panelInferior.add(scrollPresupuesto, BorderLayout.CENTER);

        add(panelInferior, BorderLayout.SOUTH);

        // ---------------- PANEL DE BOTONES ----------------
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnDatosCliente = new JButton("Datos Cliente");
        btnAgregar = new JButton("Agregar Producto");
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnGenerarPDF = new JButton("Generar Presupuesto");

        panelBotones.add(btnDatosCliente);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGenerarPDF);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(panelBotones);
        panelCentro.add(Box.createVerticalStrut(10));

        add(panelCentro, BorderLayout.CENTER);

        // ---------------- EVENTOS Y FUNCIONALIDAD ----------------
        campoBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });

        cargarProductos("");

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

                modeloPresupuesto.addRow(new Object[]{
                        id,
                        nombre,
                        cantidad,
                        String.format("%.3f", precio),
                        String.format("%.3f", total)
                });

                if (controlador != null) {
                    Presupuesto p = new Presupuesto(id, nombre, cantidad, precio);
                    controlador.agregarProducto(p);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tablaPresupuesto.getSelectedRow();
            if (fila != -1) {
                modeloPresupuesto.removeRow(fila);
                if (controlador != null) {
                    controlador.getProductos().remove(fila);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto del presupuesto para eliminar.");
            }
        });

        btnDatosCliente.addActionListener(e -> {
            SelectorClienteDialog selector = new SelectorClienteDialog(SwingUtilities.getWindowAncestor(this));
            Cliente seleccionado = selector.mostrarYObtenerSeleccion();
            if (seleccionado != null) {
                this.cliente = seleccionado;
                labelClienteSeleccionado.setText("Cliente seleccionado: " + cliente.getCampoNombre());
            }
        });

        btnGenerarPDF.addActionListener(e -> {
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Primero introduce datos del cliente.");
                return;
            }
            if (controlador != null) {
                try {
                    controlador.generarPDF(controlador.getProductos(), cliente);
                    JOptionPane.showMessageDialog(this, "Presupuesto generado correctamente.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al generar el presupuesto:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Controlador no inicializado.");
            }
        });

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

    public void setControlador(GenerarPresupuestoControlador c) {
        this.controlador = c;
    }
}
