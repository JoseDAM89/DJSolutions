package gui.dialogos;

import modelos.Cliente;
import datos.ClienteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SelectorClienteDialog extends JDialog {

    private Cliente clienteSeleccionado = null;
    private JTable tablaClientes;
    private DefaultTableModel modelo;
    private JTextField campoBuscar;

    public SelectorClienteDialog(Window owner) {
        super(owner, "Seleccionar Cliente", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(owner);

        campoBuscar = new JTextField();
        add(new JLabel("Buscar:"), BorderLayout.NORTH);
        add(campoBuscar, BorderLayout.NORTH);

        String[] columnas = {"ID", "Nombre", "CIF", "Email"};
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaClientes = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaClientes);
        add(scroll, BorderLayout.CENTER);

        JButton btnSeleccionar = new JButton("Seleccionar");
        add(btnSeleccionar, BorderLayout.SOUTH);

        cargarClientes("");

        campoBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });

        btnSeleccionar.addActionListener(e -> seleccionar());

        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    seleccionar();
                }
            }
        });
    }

    private void filtrar() {
        String texto = campoBuscar.getText().trim().toLowerCase();
        cargarClientes(texto);
    }

    private void cargarClientes(String filtro) {
        modelo.setRowCount(0);
        List<Cliente> lista = ClienteDAO.listarTodos();

        List<Cliente> filtrados = lista.stream()
                .filter(c -> c.getCampoNombre().toLowerCase().contains(filtro) ||
                        c.getCampoCIF().toLowerCase().contains(filtro) ||
                        c.getCampoEmail().toLowerCase().contains(filtro))
                .collect(Collectors.toList());

        for (Cliente c : filtrados) {
            modelo.addRow(new Object[]{
                    c.getIdcliente(), c.getCampoNombre(), c.getCampoCIF(), c.getCampoEmail()
            });
        }
    }

    private void seleccionar() {
        int fila = tablaClientes.getSelectedRow();
        if (fila != -1) {
            int id = (int) modelo.getValueAt(fila, 0);
            String nombre = (String) modelo.getValueAt(fila, 1);
            String cif = (String) modelo.getValueAt(fila, 2);
            String email = (String) modelo.getValueAt(fila, 3);

            clienteSeleccionado = new Cliente(id, nombre, cif, email, "", "", "");
            dispose();
        }
    }
    public Cliente mostrarYObtenerSeleccion() {
        setVisible(true); // Muestra el diálogo y espera
        return clienteSeleccionado; // Retorna el cliente si se seleccionó, o null si se canceló
    }


    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }
}
