package GUI;

import FuncionesPrincipalesDeInventario.*;

import javax.swing.*;
import java.awt.*;

public class GestionInventario extends JPanel {

    private JButton btnAgregarProductos;
    private JButton btnListarProductos;
    private JButton btnVerAlertaStock;
    private JButton btnConsultarStock;
    private JButton btnAtras;
    private Vprin ventana;

    public GestionInventario(Vprin ventana) {
        this.ventana = ventana;
        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        btnAgregarProductos = crearBoton("Agregar Productos");
        btnVerAlertaStock = crearBoton("Ver Alerta de Stock Bajo");
        btnConsultarStock = crearBoton("Consultar Stock");
        btnListarProductos = crearBoton("Listar Productos");
        btnAtras = crearBoton("Atras");
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(255, 250, 250)); // Color claro para un estilo limpio

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        gbc.gridy = fila++;
        add(btnAgregarProductos, gbc);

        gbc.gridy = fila++;
        add(btnVerAlertaStock, gbc);

        gbc.gridy = fila++;
        add(btnConsultarStock, gbc);

        gbc.gridy = fila++;
        add(btnListarProductos, gbc);

        gbc.gridy = fila++;
        add(btnAtras, gbc);
    }

    private void agregarEventos() {
        btnAgregarProductos.addActionListener(e -> {
            ventana.ponPanel(new AgregarProductos(ventana));
        });

        btnVerAlertaStock.addActionListener(e -> {
            VerAlertaStock verAlertaStock = new VerAlertaStock();
            ventana.ponPanel(new VerAlertaStock());
        });
        btnConsultarStock.addActionListener(e -> {
            ConsultarStock consultarStock = new ConsultarStock();
            ventana.ponPanel(new ConsultarStock());
        });
        btnListarProductos.addActionListener(e -> new ListarProductos(ventana));

        btnAtras.addActionListener(e -> {
            GestionInventario gestionInventario = new GestionInventario(ventana);
            ventana.ponPanel(new OpcionesPrincipales(ventana, true));
        });
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(250, 50));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(60, 120, 180));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
