package gui;

import FuncionesPrecios.CalculadoraPrecioProducto;
import modelos.PrecioProducto;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class PrecioProductoPanel extends JPanel {

    private JSpinner spnTiempoOperario, spnTiempoMaquina, spnPreparacion, spnCosteBarra, spnPiezasPorBarra;
    private JSpinner spnCosteZincado, spnEstructura, spnMargen, spnCantidad;
    private JCheckBox chkZincado;
    private JLabel lblPrecioFinal;

    public PrecioProductoPanel() {
        setLayout(new BorderLayout());

        // Panel principal de inputs
        JPanel panelCampos = new JPanel(new GridLayout(0, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        spnTiempoOperario = crearSpinner(0.0);
        spnTiempoMaquina = crearSpinner(0.0);
        spnPreparacion = crearSpinner(0.0);
        spnCosteBarra = crearSpinner(0.0);
        spnPiezasPorBarra = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        spnCosteZincado = crearSpinner(0.0);
        spnCosteZincado.setEnabled(false); // desactivado por defecto
        spnEstructura = crearSpinner(0.0);
        spnMargen = crearSpinner(0.0);
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));

        chkZincado = new JCheckBox("¿Zincado? (€)");
        chkZincado.addActionListener(e -> spnCosteZincado.setEnabled(chkZincado.isSelected()));

        // Añadimos campos al panel
        panelCampos.add(new JLabel("Tiempo Operario (min):"));
        panelCampos.add(spnTiempoOperario);

        panelCampos.add(new JLabel("Tiempo Máquina (min):"));
        panelCampos.add(spnTiempoMaquina);

        panelCampos.add(new JLabel("Tiempo Preparación (min):"));
        panelCampos.add(spnPreparacion);

        panelCampos.add(new JLabel("Coste Barra (€):"));
        panelCampos.add(spnCosteBarra);

        panelCampos.add(new JLabel("Piezas por Barra:"));
        panelCampos.add(spnPiezasPorBarra);

        panelCampos.add(chkZincado);
        panelCampos.add(spnCosteZincado);

        panelCampos.add(new JLabel("Estructura (%):"));
        panelCampos.add(spnEstructura);

        panelCampos.add(new JLabel("Margen Beneficio (%):"));
        panelCampos.add(spnMargen);

        panelCampos.add(new JLabel("Cantidad a producir:"));
        panelCampos.add(spnCantidad);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnCalcular = new JButton("Calcular Precio");
        JButton btnLimpiar = new JButton("Limpiar");
        lblPrecioFinal = new JLabel("Precio por unidad: -");

        btnCalcular.addActionListener(e -> calcularPrecio());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panelBotones.add(btnCalcular);
        panelBotones.add(btnLimpiar);
        panelBotones.add(lblPrecioFinal);

        // Añadir todo al panel principal
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JSpinner crearSpinner(double valorInicial) {
        return new JSpinner(new SpinnerNumberModel(valorInicial, 0.0, 100000.0, 0.1));
    }

    private void calcularPrecio() {
        PrecioProducto p = new PrecioProducto(
                (int) spnCantidad.getValue(),
                (double) spnTiempoOperario.getValue(),
                (double) spnTiempoMaquina.getValue(),
                (double) spnPreparacion.getValue(),
                (double) spnCosteBarra.getValue(),
                (int) spnPiezasPorBarra.getValue(),
                chkZincado.isSelected(),
                (double) spnCosteZincado.getValue(),
                ((double) spnEstructura.getValue()) / 100.0,  // % → decimal
                ((double) spnMargen.getValue()) / 100.0       // % → decimal
        );

        double precioUnidad = CalculadoraPrecioProducto.calcularPrecioVentaUnidad(p);
        DecimalFormat df = new DecimalFormat("#,##0.00");
        lblPrecioFinal.setText("Precio por unidad: " + df.format(precioUnidad) + " €");
    }

    private void limpiarCampos() {
        spnTiempoOperario.setValue(0.0);
        spnTiempoMaquina.setValue(0.0);
        spnPreparacion.setValue(0.0);
        spnCosteBarra.setValue(0.0);
        spnPiezasPorBarra.setValue(1);
        spnCosteZincado.setValue(0.0);
        chkZincado.setSelected(false);
        spnCosteZincado.setEnabled(false);
        spnEstructura.setValue(0.0);
        spnMargen.setValue(0.0);
        spnCantidad.setValue(1);
        lblPrecioFinal.setText("Precio por unidad: -");
    }

}