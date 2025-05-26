package org.example;

import gui.LoginDialog;
import gui.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDlg = new LoginDialog(null);
            loginDlg.setVisible(true);

            if (loginDlg.isSucceeded()) {
                VentanaPrincipal vp = new VentanaPrincipal(
                        loginDlg.getCorreo(),
                        loginDlg.isAdmin()
                );
                vp.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
