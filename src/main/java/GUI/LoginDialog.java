package GUI;


import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private boolean succeeded = false;
    private String correo;
    private boolean esAdmin;
    private InicioSesion inicioSesionPanel;

    public LoginDialog(Frame parent) {
        super(parent, "Iniciar Sesión", true);
        inicioSesionPanel = new InicioSesion(this);
        setContentPane(inicioSesionPanel);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void onLoginSuccess(String correo, boolean esAdmin) {
        this.correo = correo;
        this.esAdmin = esAdmin;
        this.succeeded = true;
        setVisible(false);
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isAdmin() {
        return esAdmin;
    }
}