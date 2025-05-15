package GUI;

import Modelos.Sesion;

import javax.swing.*;
import java.awt.event.*;

public class Vprin extends JFrame {

    public Vprin() {
        setTitle("DJSolutions");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Para capturar la X

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mostrarDialogoCerrar();
            }
        });

        ponPanel(new InicioSesion(this));
    }

    public void ponPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }

    private void mostrarDialogoCerrar() {
        String[] opciones = {"Cerrar Sesión", "Salir de la App", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Qué quieres hacer?",
                "Cerrar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion == 0) {
            // Cerrar sesión y volver a inicio de sesión
            Sesion.cerrarSesion();
            ponPanel(new InicioSesion(this));
        } else if (opcion == 1) {
            System.exit(0);
        }
        // Cancelar no hace nada
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Vprin().setVisible(true);
        });
    }
}
