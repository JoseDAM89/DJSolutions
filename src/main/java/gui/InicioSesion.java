package gui;

import modelos.Sesion;
import datos.ConexionBD;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InicioSesion extends JLayeredPane {

    private final LoginDialog loginDialog;
    private MyTextField txtEmailLogin;
    private MyPasswordField txtPassLogin;

    public InicioSesion(LoginDialog loginDialog) {
        this.loginDialog = loginDialog;
        setLayout(new BorderLayout());
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setPreferredSize(new Dimension(800, 400));
        mainPanel.add(createLoginPanel());
        mainPanel.add(createLogoPanel());
        return mainPanel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        panel.setBackground(new Color(0xF4F6F9)); // Fondo claro

        JLabel title = new JLabel("Iniciar Sesión");
        title.setFont(new Font("sansserif", Font.BOLD, 30));
        title.setForeground(new Color(0x4A90E2)); // Azul pastel
        panel.add(title);

        txtEmailLogin = new MyTextField();
        txtEmailLogin.setHint("Correo Electrónico");
        txtEmailLogin.setPrefixIcon(new ImageIcon(getClass().getResource("/JSWINGICONS/icon/iconosInicioSesion/mail.png")));
        panel.add(txtEmailLogin, "w 60%");

        txtPassLogin = new MyPasswordField();
        txtPassLogin.setHint("Contraseña");
        txtPassLogin.setPrefixIcon(new ImageIcon(getClass().getResource("/JSWINGICONS/icon/iconosInicioSesion/pass.png")));
        panel.add(txtPassLogin, "w 60%");

        BotonAnimado loginBtn = new BotonAnimado("INICIAR SESIÓN", new Color(0x4A90E2), Color.WHITE);
        loginBtn.addActionListener(this::verificarCredenciales);
        panel.add(loginBtn, "w 40%, h 40");

        return panel;
    }

    private JPanel createLogoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0x2C3E50)); // Fondo oscuro elegante
        panel.setPreferredSize(new Dimension(350, 400));

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/JSWINGICONS/icon/Logo-removebg-preview.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logo = new JLabel(scaledIcon);
        panel.add(logo);

        return panel;
    }

    private void verificarCredenciales(ActionEvent e) {
        String correo = txtEmailLogin.getText().trim();
        String pass = String.valueOf(txtPassLogin.getPassword());

        if (correo.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes llenar todos los campos.");
            return;
        }

        try (Connection conn = ConexionBD.getConexion()) {
            String sql = "SELECT contraseña, admin FROM usuarios WHERE correo_electronico = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("contraseña");
                boolean esAdmin = rs.getBoolean("admin");

                // Validar formato del hash antes de usarlo
                if (hashedPassword != null && hashedPassword.startsWith("$2")) {
                    try {
                        if (org.mindrot.jbcrypt.BCrypt.checkpw(pass, hashedPassword)) {
                            Sesion.iniciarSesion(correo, esAdmin);
                            if (loginDialog != null) {
                                loginDialog.onLoginSuccess(correo, esAdmin);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
                        }
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Formato de contraseña inválido en la base de datos.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña almacenada no válida.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.");
        }
    }



    // -------------------------
    // BOTÓN CON EFECTO ONCLICK
    // -------------------------
    private class BotonAnimado extends JButton {
        private Animator animator;
        private int targetSize;
        private float animatSize;
        private Point pressedPoint;
        private float alpha;
        private Color effectColor = new Color(0x90CAF9); // Azul claro suave

        public BotonAnimado(String text, Color bg, Color fg) {
            setText(text);
            setBackground(bg);
            setForeground(fg);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFont(getFont().deriveFont(Font.BOLD, 14f));
            setBorder(new EmptyBorder(10, 20, 10, 20));

            TimingTarget target = new TimingTargetAdapter() {
                public void timingEvent(float fraction) {
                    if (fraction > 0.5f) alpha = 1 - fraction;
                    animatSize = fraction * targetSize;
                    repaint();
                }
            };

            animator = new Animator(400, target);
            animator.setAcceleration(0.5f);
            animator.setDeceleration(0.5f);

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    pressedPoint = e.getPoint();
                    animatSize = 0;
                    targetSize = Math.max(getWidth(), getHeight()) * 2;
                    alpha = 0.5f;
                    if (animator.isRunning()) animator.stop();
                    animator.start();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, w, h, h, h);
            if (pressedPoint != null) {
                g2.setColor(effectColor);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
                g2.fillOval((int) (pressedPoint.x - animatSize / 2), (int) (pressedPoint.y - animatSize / 2), (int) animatSize, (int) animatSize);
            }
            g2.dispose();
            g.drawImage(img, 0, 0, null);
            super.paintComponent(g);
        }
    }

    public String getLoginEmail() {
        return txtEmailLogin.getText().trim();
    }

    public String getLoginPassword() {
        return String.valueOf(txtPassLogin.getPassword());
    }
}
