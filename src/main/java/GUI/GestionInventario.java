package GUI;

import FuncionesPrincipalesDeInventario.*;

import javax.swing.*;
import java.awt.*;

public class GestionInventario extends JPanel {

    private JButton btnAgregarProductos;
    private JButton btnListarProductos;
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
        add(btnConsultarStock, gbc);

        gbc.gridy = fila++;
        add(btnListarProductos, gbc);

        gbc.gridy = fila++;
        add(btnAtras, gbc);
    }

    private void agregarEventos() {
        btnAgregarProductos.addActionListener(e -> {
                JFrame ventanaAlta = new JFrame("Alta Producto");
                ventanaAlta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ventanaAlta.setSize(1000, 500);
                ventanaAlta.setLocationRelativeTo(null);

                // Creamos la instancia lógica de AgregarProductos
                AgregarProductos altaProducto = new AgregarProductos();

                // Obtenemos el formulario (que es un JPanel)
                JPanel formulario = altaProducto.construirFormulario();

                // Lo insertamos en la ventana
                ventanaAlta.setContentPane(formulario);
                ventanaAlta.setVisible(true);
            });

        btnConsultarStock.addActionListener(e -> {
            JFrame ventanaStock = new JFrame("Consultar Stock");
            ventanaStock.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ventanaStock.setSize(1000, 500);
            ventanaStock.setLocationRelativeTo(null);
            ConsultarStock consultarStock = new ConsultarStock();

            ventanaStock.setContentPane(consultarStock);
            ventanaStock.setVisible(true);

        });
        btnListarProductos.addActionListener(e -> {
            ListarProductos listar = new ListarProductos();
            listar.mostrarVentana();
        });


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
