package GUI;

import FuncionesPrincipalesDePresupuestos.GenerarPresupuesto;
import FuncionesPrincipalesDePresupuestos.HistorialDePresupuestos;
import FuncionesPrincipalesDePresupuestos.SeleccionarMateriales;

import javax.swing.*;
import java.awt.*;

public class GestionPresupuestos extends JPanel {

    private JButton btnGenerarPresupuesto;
    private JButton btnSeleccionarMateriales;
    private JButton btnHistorialPresupuestos;
    private JButton btnAtras;
    private Vprin ventana;

    public GestionPresupuestos(Vprin ventana) {
        this.ventana = ventana;
        inicializarComponentes();
        configurarPanel();
        agregarEventos();
    }

    private void inicializarComponentes() {
        btnGenerarPresupuesto = crearBoton("Generar Presupuesto");
        btnHistorialPresupuestos = crearBoton("Historial de Presupuestos");
        btnAtras = crearBoton("Atras");
    }

    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245)); // Color de fondo clarito

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        gbc.gridy = fila++;
        add(btnGenerarPresupuesto, gbc);


        gbc.gridy = fila++;
        add(btnHistorialPresupuestos, gbc);


        gbc.gridy = fila++;
        add(btnAtras, gbc);
    }

    private void agregarEventos() {
        btnGenerarPresupuesto.addActionListener(e -> {
            GenerarPresupuesto generarPresupuesto = new GenerarPresupuesto();
            ventana.ponPanel(new GenerarPresupuesto());
        });
        btnHistorialPresupuestos.addActionListener(e -> {
            HistorialDePresupuestos historialDePresupuestos = new HistorialDePresupuestos();
            ventana.ponPanel(new HistorialDePresupuestos());
        });
        btnAtras.addActionListener(e -> {
            GestionPresupuestos gestionPresupuestos = new GestionPresupuestos(ventana);
            ventana.ponPanel(new OpcionesPrincipales(ventana, true));
        });
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(250, 50));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(70, 130, 180)); // Azul bonito
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return boton;
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
