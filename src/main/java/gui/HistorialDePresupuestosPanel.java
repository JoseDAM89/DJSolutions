package gui;

import FuncionesPresupuesto.HistorialDePresupuestos;
import datos.ClienteDAO;
import datos.PresupuestoDAO;
import modelos.Cliente;
import modelos.Presupuesto;
import utilidades.GeneradorDocumentoPDF;
import Correo.EnviarCorreo;

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

public class HistorialDePresupuestosPanel extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;

    public HistorialDePresupuestosPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Historial de Presupuestos"));

        // 🔍 Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        JLabel lblBuscar = new JLabel("Buscar cliente: ");
        JTextField txtBuscar = new JTextField();
        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        add(panelBusqueda, BorderLayout.NORTH);

        // 📋 Modelo de tabla con columna Aceptado editable
        modelo = new DefaultTableModel(new Object[]{"ID", "Cliente", "Fecha", "Total", "Aceptado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo editable columna Aceptado
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 4) ? Boolean.class : String.class;
            }
        };

        tabla = new JTable(modelo);
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        // ✅ Renderer que respeta selección y colorea si está aceptado
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int modelRow = table.convertRowIndexToModel(row);
                boolean aceptado = (boolean) table.getModel().getValueAt(modelRow, 4);

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(aceptado ? new Color(200, 255, 200) : Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // 🔄 Cargar datos desde la BD
        cargarPresupuestos();

        // 🔎 Filtro en tiempo real
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1)); // columna 1 = Cliente
                }
            }
        });

        // 📎 Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVerPDF = new JButton("Ver Presupuesto (PDF)");
        JButton btnEnviarCorreo = new JButton("Enviar por Correo");

        btnVerPDF.addActionListener(e -> verPresupuestoPDF());
        btnEnviarCorreo.addActionListener(e -> enviarPresupuestoPorCorreo());

        panelBotones.add(btnVerPDF);
        panelBotones.add(btnEnviarCorreo);

        add(panelBotones, BorderLayout.SOUTH);

        // 🖱️ Listener para actualizar estado aceptado
        modelo.addTableModelListener(e -> {
            int fila = e.getFirstRow();
            int columna = e.getColumn();
            if (columna == 4 && fila >= 0) {
                int idPresupuesto = (int) modelo.getValueAt(fila, 0);
                boolean aceptado = (boolean) modelo.getValueAt(fila, 4);
                PresupuestoDAO dao = new PresupuestoDAO();
                dao.actualizarAceptado(idPresupuesto, aceptado);
                JOptionPane.showMessageDialog(this, "✅ Estado actualizado.");
                tabla.repaint();
            }
        });
    }

    private void cargarPresupuestos() {
        modelo.setRowCount(0);

        PresupuestoDAO dao = new PresupuestoDAO();
        List<Presupuesto> presupuestos = dao.listarTodos();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Presupuesto p : presupuestos) {
            Cliente cliente = ClienteDAO.obtenerPorID(p.getIdCliente());
            String nombreCliente = (cliente != null) ? cliente.getCampoNombre() : "Desconocido";

            modelo.addRow(new Object[]{
                    p.getId(),
                    nombreCliente,
                    p.getFecha().format(formato),
                    String.format("%.2f €", p.getTotal()),
                    p.isAceptado()
            });
        }
    }

    private void verPresupuestoPDF() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un presupuesto primero.");
            return;
        }

        int idPresupuesto = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        HistorialDePresupuestos logica = new HistorialDePresupuestos();
        Presupuesto presupuesto = logica.obtenerPorId(idPresupuesto);
        Cliente cliente = logica.obtenerCliente(presupuesto.getIdCliente());

        if (presupuesto != null && cliente != null) {
            GeneradorDocumentoPDF.generarYMostrarPresupuesto(presupuesto, cliente);
        } else {
            JOptionPane.showMessageDialog(this, "Error al obtener datos del presupuesto.");
        }
    }

    private void enviarPresupuestoPorCorreo() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un presupuesto primero.");
            return;
        }

        int idPresupuesto = (int) modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0);
        Presupuesto presupuesto = new PresupuestoDAO().obtenerPorID(idPresupuesto);
        Cliente cliente = ClienteDAO.obtenerPorID(presupuesto.getIdCliente());

        if (presupuesto == null || cliente == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos.");
            return;
        }

        String correo = JOptionPane.showInputDialog(this, "Introduce el correo del cliente:");
        if (correo == null || correo.trim().isEmpty()) return;

        try {
            File archivo = GeneradorDocumentoPDF.generarPresupuesto(presupuesto, cliente);
            EnviarCorreo.enviarArchivoPorCorreo(
                    correo,
                    archivo,
                    "Presupuesto DJ Solutions",
                    "Adjunto le enviamos su presupuesto generado. Gracias por su confianza."
            );
            JOptionPane.showMessageDialog(this, "📧 Presupuesto enviado correctamente.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error al enviar: " + ex.getMessage());
        }
    }
}
