package FuncionesFacturas;

import datos.ClienteDAO;
import datos.FacturaDAO;
import modelos.Cliente;
import modelos.Factura;
import utilidades.GeneradorFacturaPDF;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialFacturasPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    public HistorialFacturasPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de Facturas"));

        modelo = new DefaultTableModel(new String[]{
                "ID Factura", "ID Cliente", "Fecha", "Total"
        }, 0) {
            // Para evitar que las celdas sean editables
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        cargarFacturas();

        JButton btnVerPDF = new JButton("Ver Factura (PDF)");
        btnVerPDF.addActionListener(e -> verFacturaPDF());
        add(btnVerPDF, BorderLayout.SOUTH);

    }

    private void cargarFacturas() {
        modelo.setRowCount(0); // Limpiar tabla

        FacturaDAO dao = new FacturaDAO();
        List<Factura> facturas = dao.listarTodas();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Factura f : facturas) {
            double total = f.getLineas().stream()
                    .mapToDouble(l -> l.getCantidad() * l.getPrecioUnitario())
                    .sum();

            modelo.addRow(new Object[]{
                    f.getIdFactura(),
                    f.getIdCliente(),
                    f.getFecha().format(formato),
                    String.format("%.2f €", total)
            });
        }
    }

    private void verFacturaPDF() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idFactura = (int) modelo.getValueAt(filaSeleccionada, 0);

        FacturaDAO dao = new FacturaDAO();
        Factura factura = dao.obtenerFacturaPorID(idFactura);

        if (factura != null) {
            Cliente cliente = ClienteDAO.obtenerPorID(factura.getIdCliente());
            GeneradorFacturaPDF.generarYMostrar(factura, cliente);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo obtener la factura.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void abrirPDF(String ruta) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(ruta));
            } else {
                JOptionPane.showMessageDialog(this, "Abrir archivos no está soportado en este sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo abrir el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
