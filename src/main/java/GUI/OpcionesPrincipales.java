package GUI;

import javax.swing.*;
import java.awt.*;

public class OpcionesPrincipales extends JPanel {

    private JButton btnGestionInventario;
    private JButton btnGestionPresupuestos;

    private Vprin ventana; // Referencia a la ventana principal para poder usar ponPanel()

    public OpcionesPrincipales(Vprin ventana) {
        this.ventana = ventana;

        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        btnGestionInventario = crearBoton("Gestión de Inventario");
        btnGestionPresupuestos = crearBoton("Gestión de Presupuestos");
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255)); // Color clarito tipo profesional

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        add(btnGestionInventario, gbc);

        gbc.gridy = 1;
        add(btnGestionPresupuestos, gbc);
    }

    private void agregarEventos() {
        btnGestionInventario.addActionListener(e -> {
            GestionInventario gestionInventario = new GestionInventario(ventana);
            ventana.ponPanel(gestionInventario);
        });

        btnGestionPresupuestos.addActionListener(e -> {
            GestionPresupuestos gestionPresupuestos = new GestionPresupuestos();
            ventana.ponPanel(gestionPresupuestos);
        });
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(300, 60));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(60, 120, 180));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }
}
