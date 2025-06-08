package gui;

import Correo.EnviarCorreo;
import datos.ClienteDAO;
import datos.FacturaDAO;
import datos.PresupuestoDAO;
import gui.dialogos.SelectorClienteDialog;
import modelos.*;
import utilidades.GeneradorDocumentoPDF;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialDocumentosPanel extends JPanel {

    public enum TipoDocumento { FACTURA, PRESUPUESTO }

    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private TipoDocumento tipo;

    public HistorialDocumentosPanel(TipoDocumento tipo) {
        this.tipo = tipo;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de " + tipo));

        // Buscar cliente
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.add(new JLabel("Buscar cliente: "), BorderLayout.WEST);
        JTextField txtBuscar = new JTextField();
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);

        // Modelo de tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Cliente", "Fecha", "Total", (tipo == TipoDocumento.FACTURA ? "Pagada" : "Aceptado")}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 4) ? Boolean.class : String.class;
            }
        };

        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        aplicarColores();

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Cargar datos
        cargarDatos();

        // Filtro en tiempo real
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();
                sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto, 1));
            }
        });

        // Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVerPDF = new JButton("Ver " + tipo);
        JButton btnEnviar = new JButton("Enviar por correo");

        btnVerPDF.addActionListener(e -> verPDF());
        btnEnviar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un documento.");
                return;
            }

            // Selección del cliente desde el diálogo
            SelectorClienteDialog selector = new SelectorClienteDialog(SwingUtilities.getWindowAncestor(this));
            Cliente clienteSeleccionado = selector.mostrarYObtenerSeleccion();
            if (clienteSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "No se seleccionó ningún cliente.");
                return;
            }

            String correoCliente = clienteSeleccionado.getCampoEmail().trim();

            // Preguntar si quiere usar otro correo
            String nuevoCorreo = JOptionPane.showInputDialog(this,
                    "Correo actual del cliente: " + (correoCliente.isEmpty() ? "(no tiene)" : correoCliente) +
                            "\nIntroduce otro correo si quieres cambiarlo, o deja vacío para usar el existente:");

            // Determinar el correo final a usar
            String correoDestino = (nuevoCorreo != null && !nuevoCorreo.trim().isEmpty())
                    ? nuevoCorreo.trim()
                    : correoCliente;

            if (correoDestino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se ha proporcionado ningún correo.");
                return;
            }

            // Obtener el documento según el tipo
            int id = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
            File archivo;
            if (tipo == TipoDocumento.FACTURA) {
                Factura f = new FacturaDAO().obtenerFacturaPorID(id);
                archivo = GeneradorDocumentoPDF.generarFactura(f, clienteSeleccionado);
            } else {
                Presupuesto p = new PresupuestoDAO().obtenerPorID(id);
                archivo = GeneradorDocumentoPDF.generarPresupuesto(p, clienteSeleccionado);
            }

            // Enviar el correo
            try {
                EnviarCorreo.enviarArchivoPorCorreo(
                        correoDestino,
                        archivo,
                        (tipo == TipoDocumento.FACTURA ? "Factura DJ Solutions" : "Presupuesto DJ Solutions"),
                        "Adjunto le enviamos su " + tipo.toString().toLowerCase() + ". Gracias por su confianza."
                );
                JOptionPane.showMessageDialog(this, "📧 Documento enviado correctamente a " + correoDestino);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error al enviar: " + ex.getMessage());
            }
        });


        panelBotones.add(btnVerPDF);
        panelBotones.add(btnEnviar);
        add(panelBotones, BorderLayout.SOUTH);

        // Cambiar estado
        modelo.addTableModelListener(e -> {
            int fila = e.getFirstRow();
            int col = e.getColumn();
            if (col == 4 && fila >= 0) {
                int id = (int) modelo.getValueAt(fila, 0);
                boolean valor = (boolean) modelo.getValueAt(fila, 4);
                if (tipo == TipoDocumento.FACTURA) {
                    new FacturaDAO().actualizarEstadoPagada(id, valor);
                } else {
                    new PresupuestoDAO().actualizarAceptado(id, valor);
                }
                JOptionPane.showMessageDialog(this, "✅ Estado actualizado.");
                tabla.repaint();
            }
        });
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        if (tipo == TipoDocumento.FACTURA) {
            List<Factura> facturas = new FacturaDAO().listarTodas();
            for (Factura f : facturas) {
                double total = f.getLineas().stream().mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario()).sum();
                Cliente c = ClienteDAO.obtenerPorID(f.getIdCliente());
                modelo.addRow(new Object[]{f.getIdFactura(), c != null ? c.getCampoNombre() : "Desconocido", f.getFecha().format(formato), String.format("%.2f €", total), f.isPagada()});
            }
        } else {
            List<Presupuesto> lista = new PresupuestoDAO().listarTodos();
            for (Presupuesto p : lista) {
                Cliente c = ClienteDAO.obtenerPorID(p.getIdCliente());
                modelo.addRow(new Object[]{p.getId(), c != null ? c.getCampoNombre() : "Desconocido", p.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), String.format("%.2f €", p.getTotal()), p.isAceptado()});
            }
        }
    }

    private void verPDF() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un documento.");
            return;
        }
        int id = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        Cliente cliente;
        if (tipo == TipoDocumento.FACTURA) {
            Factura f = new FacturaDAO().obtenerFacturaPorID(id);
            cliente = ClienteDAO.obtenerPorID(f.getIdCliente());
            GeneradorDocumentoPDF.generarYMostrarFactura(f, cliente);
        } else {
            Presupuesto p = new PresupuestoDAO().obtenerPorID(id);
            cliente = ClienteDAO.obtenerPorID(p.getIdCliente());
            GeneradorDocumentoPDF.generarYMostrarPresupuesto(p, cliente);
        }
    }

    private void enviarCorreo() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un documento.");
            return;
        }
        int id = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        Cliente cliente;
        File archivo;
        if (tipo == TipoDocumento.FACTURA) {
            Factura f = new FacturaDAO().obtenerFacturaPorID(id);
            cliente = ClienteDAO.obtenerPorID(f.getIdCliente());
            archivo = GeneradorDocumentoPDF.generarFactura(f, cliente);
        } else {
            Presupuesto p = new PresupuestoDAO().obtenerPorID(id);
            cliente = ClienteDAO.obtenerPorID(p.getIdCliente());
            archivo = GeneradorDocumentoPDF.generarPresupuesto(p, cliente);
        }
        String correo = JOptionPane.showInputDialog(this, "Correo del cliente:");
        if (correo == null || correo.trim().isEmpty()) return;

        try {
            EnviarCorreo.enviarArchivoPorCorreo(
                    correo,
                    archivo,
                    (tipo == TipoDocumento.FACTURA ? "Factura DJ Solutions" : "Presupuesto DJ Solutions"),
                    "Adjunto le enviamos su " + tipo.toString().toLowerCase() + ". Gracias por su confianza."
            );
            JOptionPane.showMessageDialog(this, "📧 Documento enviado correctamente.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error al enviar: " + ex.getMessage());
        }
    }

    private void aplicarColores() {
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                boolean check = Boolean.TRUE.equals(table.getValueAt(row, 4));
                if (check && !isSelected) c.setBackground(new Color(200, 255, 200));
                else c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                return c;
            }
        });
    }
}
