package GUI;

import Modelos.Sesion;
import javax.swing.*;
import java.awt.event.*;

public class Vprin extends JFrame {

    public Vprin() {
        setTitle("DJSolutions");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mostrarDialogoCerrar();
            }
        });

        mostrarLogin();
    }

    private void mostrarLogin() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);
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
            Sesion.cerrarSesion();
            mostrarLogin();
        } else if (opcion == 1) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Vprin().setVisible(true);
        });
    }
}