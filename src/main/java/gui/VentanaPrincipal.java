package gui;

import Controladores.ControladorMenu;
import gui.componentes.Header;
import gui.componentes.Menu;
import gui.eventos.EventMenuSelected;
import gui.eventos.EventShowPopupMenu;
import gui.formularios.Form_Home;
import gui.formularios.MainForm;
import gui.swing.MenuItem;
import gui.swing.PanelTransparent;
import gui.swing.PopupMenu;
import gui.swing.iconos.GoogleMaterialDesignIcons;
import gui.swing.iconos.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static modelos.Sesion.cerrarSesion;
import static modelos.Sesion.esAdmin;

public class VentanaPrincipal extends JFrame {

    private MigLayout layout;
    private Menu menu;
    private Header header;
    private MainForm mainForm;
    private PanelTransparent bg;

    public VentanaPrincipal(String correoUsuario, boolean esAdmin) {
        setTitle("DJ Solution");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);

        initUI();
        configurarEventos();
        mostrarPanelInicial();

        header.setUserInfo(correoUsuario, esAdmin ? "Admin" : "Usuario");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mostrarDialogoCerrar();
            }
        });
    }

    private void initUI() {
        layout = new MigLayout("fill", "10[]10[100%, fill]10", "10[fill, top]10");
        bg = new PanelTransparent();
        bg.setLayout(layout);

        menu = new Menu();
        header = new Header();
        mainForm = new MainForm();

        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());


        bg.add(menu, "w 230!, spany 2");
        bg.add(header, "h 50!, wrap");
        bg.add(mainForm, "w 100%, h 100%");
        setContentPane(bg);
    }

    private void configurarEventos() {
        menu.addEvent(new EventMenuSelected() {
            public void menuSelected(int menuIndex, int subMenuIndex) {
                JPanel panel = ControladorMenu.obtenerFormulario(menuIndex, subMenuIndex);
                if (panel != null) {
                    ponPanel(panel);
                }
            }
        });


        menu.addEventShowPopup(new EventShowPopupMenu() {
            public void showPopup(Component com) {
                MenuItem item = (MenuItem) com;

                // Medir ancho necesario para el submenu más largo
                FontMetrics fm = com.getFontMetrics(com.getFont());
                int maxWidth = 0;
                for (String sub : item.getMenu().getSubMenu()) {
                    maxWidth = Math.max(maxWidth, fm.stringWidth(sub));
                }
                maxWidth += 40; // un margen extra para no cortar texto

                // Definir altura por opción (ajusta según tu diseño)
                int itemHeight = 30;
                int totalItems = item.getMenu().getSubMenu().length;
                int totalHeight = itemHeight * totalItems;

                PopupMenu popup = new PopupMenu(VentanaPrincipal.this, item.getIndex(), item.getEventSelected(), item.getMenu().getSubMenu());

                // Ajustar tamaño del popup con ancho y alto calculados
                popup.setPreferredSize(new Dimension(maxWidth, totalHeight));
                popup.pack(); // actualizar tamaño

                int x = getX() + 62;
                int y = getY() + com.getY() + 128;

                popup.setLocation(x, y);
                popup.setVisible(true);
            }
        });

        // Animación de apertura/cierre del menú
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double width;
                if (menu.isShowMenu()) {
                    width = 60 + (170 * (1f - fraction));
                } else {
                    width = 60 + (170 * fraction);
                }
                layout.setComponentConstraints(menu, "w " + width + "!, spany2");
                menu.revalidate();
            }

            @Override
            public void end() {
                menu.setShowMenu(!menu.isShowMenu());
                menu.setEnableMenu(true);
            }
        };

        Animator animator = new Animator(500, target);
        animator.setResolution(0);
        animator.setDeceleration(0.5f);
        animator.setAcceleration(0.5f);

        header.addMenuEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    animator.start();
                }
                menu.setEnableMenu(false);
                if (menu.isShowMenu()) {
                    menu.hideallMenu();
                }
            }
        });
        
        menu.initMenuItem();

    }

    public void ponPanel(JPanel panel) {
        mainForm.showForm(panel);
    }

    private void mostrarPanelInicial() {
        ponPanel(new Form_Home());
    }

    private void mostrarDialogoCerrar() {
        String[] opciones = {"Cerrar Sesión", "Salir de la App", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this, "¿Qué quieres hacer?", "Cerrar",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (opcion == 0) {
            cerrarSesion(); // <- Limpia estado de sesión actual si lo haces
            dispose(); // Cierra la ventana actual

            LoginDialog login = new LoginDialog(null);
            login.setVisible(true);

            if (login.isSucceeded()) {
                // Aquí relanzas la app con los datos del nuevo usuario
                new VentanaPrincipal(login.getCorreo(),esAdmin()).setVisible(true);
            } else {
                System.exit(0); // Si cancela el login, cerramos todo
            }

        } else if (opcion == 1) {
            System.exit(0);
        }
    }


    public void volverAHome() {
        menu.limpiarSeleccion(); // <- limpiar selección visual
        ponPanel(new Form_Home());
    }


}