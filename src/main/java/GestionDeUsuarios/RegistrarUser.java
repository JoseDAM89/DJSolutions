package GestionDeUsuarios;

import Datos.ConexionBD;
import GUI.OpcionesPrincipales;
import GUI.Vprin;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrarUser extends JPanel {

    public RegistrarUser(Vprin ventana) {
        setLayout(new GridLayout(6, 2, 10, 10));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();

        JLabel lblApellido = new JLabel("Apellido:");
        JTextField txtApellido = new JTextField();

        JLabel lblCorreo = new JLabel("Correo electrónico:");
        JTextField txtCorreo = new JTextField();

        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();

        JCheckBox chkAdmin = new JCheckBox("¿Es administrador?");

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnVolver = new JButton("Volver");

        add(lblNombre);
        add(txtNombre);

        add(lblApellido);
        add(txtApellido);

        add(lblCorreo);
        add(txtCorreo);

        add(lblPassword);
        add(txtPassword);

        add(new JLabel());
        add(chkAdmin);

        add(btnVolver);
        add(btnRegistrar);

        btnRegistrar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String correo = txtCorreo.getText().trim();
            String password = new String(txtPassword.getPassword());
            boolean esAdmin = chkAdmin.isSelected();

            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Rellena todos los campos.");
                return;
            }

            if (correoYaExiste(correo)) {
                JOptionPane.showMessageDialog(this, "Ese correo ya está registrado.");
                return;
            }

            // Sincronizar la secuencia antes de insertar, por si está desincronizada
            sincronizarSecuenciaId();

            if (registrar(nombre, apellido, correo, password, esAdmin)) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                ventana.ponPanel(new OpcionesPrincipales(ventana));
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario.");
            }
        });

        btnVolver.addActionListener(e -> ventana.ponPanel(new OpcionesPrincipales(ventana)));
    }

    public static boolean registrar(String nombre, String apellido, String correo, String password, boolean esAdmin) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = ConexionBD.conectar()) {
            String sql = "INSERT INTO usuarios (nombre, apellido, correo_electronico, contraseña, admin) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, correo);
            stmt.setString(4, hashed);
            stmt.setBoolean(5, esAdmin);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean correoYaExiste(String correo) {
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT 1 FROM usuarios WHERE correo_electronico = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Por seguridad, asumimos que sí existe si hay error
        }
    }


    // Nueva función para sincronizar la secuencia de id con el máximo id actual en la tabla
    public static void sincronizarSecuenciaId() {
        try (Connection conn = ConexionBD.conectar()) {
            String sql = "SELECT setval('usuarios_id_seq', (SELECT COALESCE(MAX(id), 1) FROM usuarios))";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            // No abortar el flujo, solo informamos el error
        }
    }
}
