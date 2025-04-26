package GUI;

import javax.swing.*;
import java.awt.*;

public class Vprin extends JFrame {

    private JPanel panelActual;

    public Vprin() {
        setTitle("Datos Partidos");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    /**
     * Método modular para cambiar entre paneles.
     * @param nuevoPanel El nuevo JPanel que quieres mostrar.
     */
    public void ponPanel(JPanel nuevoPanel) {
        if (panelActual != null) {
            remove(panelActual);
        }
        panelActual = nuevoPanel;
        add(panelActual, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
