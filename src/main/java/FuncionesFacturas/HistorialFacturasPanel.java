package FuncionesFacturas;

import Correo.EnviarCorreo;
import datos.ClienteDAO;
import datos.FacturaDAO;
import modelos.Cliente;
import modelos.Factura;
import utilidades.GeneradorFacturaPDF;

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

    public HistorialFacturasPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de Facturas"));

        // 🔍 Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        JLabel lblBuscar = new JLabel("Buscar cliente: ");
        JTextField txtBuscar = new JTextField();
        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{
                "ID Factura", "Cliente", "Fecha", "Total", "Pagada"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class;
                return String.class;
            }
        };

        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        aplicarColoresATabla();

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int fila = e.getFirstRow();
                int columna = e.getColumn();

                if (columna == 4 && fila >= 0) {
                    int idFactura = (int) modelo.getValueAt(fila, 0);
                    boolean pagada = (boolean) modelo.getValueAt(fila, 4);

                    FacturaDAO dao = new FacturaDAO();
                    dao.actualizarEstadoPagada(idFactura, pagada);

                    JOptionPane.showMessageDialog(
                            HistorialFacturasPanel.this,
                            "Estado actualizado correctamente.",
                            "Confirmación",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1));
                }
            }
        });

        cargarFacturas();

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

        FacturaDAO dao = new FacturaDAO();
        List<Factura> facturas = dao.listarTodas();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Factura f : facturas) {
            double total = f.getLineas().stream()
                    .mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario())
                    .sum();

            Cliente cliente = ClienteDAO.obtenerPorID(f.getIdCliente());
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
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idFactura = (int) modelo.getValueAt(tabla.convertRowIndexToModel(filaSeleccionada), 0);

        FacturaDAO dao = new FacturaDAO();
        Factura factura = dao.obtenerFacturaPorID(idFactura);

        if (factura != null) {
            Cliente cliente = ClienteDAO.obtenerPorID(factura.getIdCliente());
            GeneradorFacturaPDF.generarYMostrar(factura, cliente);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo obtener la factura.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarFacturaPorCorreo() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idFactura = (int) modelo.getValueAt(tabla.convertRowIndexToModel(filaSeleccionada), 0);
        FacturaDAO dao = new FacturaDAO();
        Factura factura = dao.obtenerFacturaPorID(idFactura);

        if (factura == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = ClienteDAO.obtenerPorID(factura.getIdCliente());
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el cliente asociado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String correo = JOptionPane.showInputDialog(this, "Introduce el correo del cliente:");
        if (correo == null || correo.trim().isEmpty()) {
            return; // Cancelado
        }

        try {
            File archivo = GeneradorFacturaPDF.generar(factura, cliente); // genera el PDF pero no lo abre
            EnviarCorreo.enviarArchivoPorCorreo(
                    correo,
                    archivo,
                    "Factura DJ Solutions",
                    "Adjunto le enviamos su factura generada. Gracias por su confianza."
            );

            JOptionPane.showMessageDialog(this, "📧 Factura enviada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error al enviar la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarColoresATabla() {
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                boolean pagada = Boolean.TRUE.equals(table.getValueAt(row, 4));
                if (pagada && !isSelected) {
                    c.setBackground(new Color(200, 255, 200));
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                }

                return c;
            }
        });
    }
}
