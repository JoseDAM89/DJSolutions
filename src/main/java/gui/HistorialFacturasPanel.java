package gui;

import FuncionesFacturas.HistorialDeFacturas;
import modelos.Cliente;
import modelos.Factura;
import utilidades.GeneradorDocumentoPDF;
import Correo.EnviarCorreo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialFacturasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private HistorialDeFacturas logica = new HistorialDeFacturas();

    public HistorialFacturasPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de Facturas"));

        // 🔍 Panel búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        JLabel lblBuscar = new JLabel("Buscar cliente: ");
        JTextField txtBuscar = new JTextField();
        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);

        // 📋 Modelo de tabla
        modelo = new DefaultTableModel(new Object[]{"ID Factura", "Cliente", "Fecha", "Total", "Pagada"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 4) ? Boolean.class : String.class;
            }
        };

        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        aplicarColores();

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        cargarFacturas();

        // 🔄 Cambio de estado pagada
        modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int fila = e.getFirstRow();
                int columna = e.getColumn();
                if (columna == 4 && fila >= 0) {
                    int idFactura = (int) modelo.getValueAt(fila, 0);
                    boolean pagada = (boolean) modelo.getValueAt(fila, 4);
                    logica.actualizarEstadoPagada(idFactura, pagada);
                    JOptionPane.showMessageDialog(HistorialFacturasPanel.this, "Estado actualizado.");
                }
            }
        });

        // 🔎 Filtro dinámico
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();
                sorter.setRowFilter(texto.isEmpty() ? null : RowFilter.regexFilter("(?i)" + texto, 1));
            }
        });

        // 📎 Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVerPDF = new JButton("Ver Factura (PDF)");
        JButton btnEnviarCorreo = new JButton("Enviar por Correo");

        btnVerPDF.addActionListener(e -> verFacturaPDF());
        btnEnviarCorreo.addActionListener(e -> enviarFacturaPorCorreo());

        panelBotones.add(btnVerPDF);
        panelBotones.add(btnEnviarCorreo);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarFacturas() {
        modelo.setRowCount(0);
        List<Factura> facturas = logica.obtenerTodas();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Factura f : facturas) {
            double total = f.getLineas().stream().mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario()).sum();
            Cliente cliente = logica.obtenerCliente(f.getIdCliente());
            String nombreCliente = (cliente != null) ? cliente.getCampoNombre() : "Desconocido";

            modelo.addRow(new Object[]{
                    f.getIdFactura(),
                    nombreCliente,
                    f.getFecha().format(formato),
                    String.format("%.2f €", total),
                    f.isPagada()
            });
        }
    }

    private void verFacturaPDF() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura.");
            return;
        }

        int id = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        Factura f = logica.obtenerPorId(id);
        Cliente c = logica.obtenerCliente(f.getIdCliente());

        if (f != null && c != null) {
            GeneradorDocumentoPDF.generarYMostrarFactura(f, c);
        }
    }

    private void enviarFacturaPorCorreo() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura.");
            return;
        }

        int id = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        Factura f = logica.obtenerPorId(id);
        Cliente c = logica.obtenerCliente(f.getIdCliente());

        if (f == null || c == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos.");
            return;
        }

        String correo = JOptionPane.showInputDialog(this, "Correo del cliente:");
        if (correo == null || correo.trim().isEmpty()) return;

        try {
            File archivo = GeneradorDocumentoPDF.generarFactura(f, c);
            EnviarCorreo.enviarArchivoPorCorreo(
                    correo,
                    archivo,
                    "Factura DJ Solutions",
                    "Adjunto le enviamos su factura. Gracias por su confianza."
            );
            JOptionPane.showMessageDialog(this, "📧 Factura enviada correctamente.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error al enviar: " + ex.getMessage());
        }
    }

    private void aplicarColores() {
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                boolean pagada = Boolean.TRUE.equals(table.getValueAt(row, 4));
                if (pagada && !isSelected) c.setBackground(new Color(200, 255, 200));
                else c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                return c;
            }
        });
    }
}
