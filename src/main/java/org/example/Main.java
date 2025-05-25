package org.example;

import FuncionesCliente.AltaCliente;
import FuncionesCliente.ListarClientes;
import FuncionesInventario.AltaProducto;
import FuncionesInventario.ConsultarStock;
import FuncionesInventario.ListarProductos;
import FuncionesPresupuesto.GenerarPresupuesto;
import FuncionesPresupuesto.HistorialDePresupuestos;
import FuncionesPresupuesto.SeleccionarMateriales;
import GestionDeUsuarios.RegistrarUser;
import GestionDeUsuarios.VerUsuarios;
import gui.componentes.Header;
import gui.componentes.Menu;
import gui.eventos.EventMenuSelected;
import gui.eventos.EventShowPopupMenu;
import gui.formularios.Form1;
import gui.formularios.Form_Home;
import gui.formularios.MainForm;
import gui.swing.MenuItem;
import gui.swing.PopupMenu;
import gui.swing.iconos.GoogleMaterialDesignIcons;
import gui.swing.iconos.IconFontSwing;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import javax.swing.*;

import gui.LoginDialog;

public class Main extends javax.swing.JFrame {

    private MigLayout layout;
    private Menu menu;
    private Header header;
    private MainForm main;
    private Animator animator;

    public Main() {
        initComponents();
        init();
    }

    private void init() {
        layout = new MigLayout("fill", "10[]10[100%, fill]10", "10[fill, top]10");
        bg.setLayout(layout);
        menu = new Menu();
        header = new Header();
        main = new MainForm();

        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        menu.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(int menuIndex, int subMenuIndex) {
                if (menuIndex == 0) {
                    if (subMenuIndex == 0) {
                        main.showForm(new AltaCliente().construirFormulario());
                    } else if (subMenuIndex == 1) {
                        main.showForm(new Form1());
                    } else if (subMenuIndex == 2) {
                        main.showForm(new ListarClientes().mostrarVentana());
                    }
                }
                if (menuIndex == 1) {
                    if (subMenuIndex == 0) {
                        main.showForm(new AltaProducto().construirFormulario());
                    } else if (subMenuIndex == 1) {
                        main.showForm(new ConsultarStock());
                    } else if (subMenuIndex == 2) {
                        main.showForm(new ListarProductos().mostrarVentana());
                    } else if (subMenuIndex == 3) {
                        new ListarProductos().mostrarVentana();
                    }
                }
                if (menuIndex == 2) {
                    if (subMenuIndex == 0) {
                        main.showForm(new GenerarPresupuesto());
                    } else if (subMenuIndex == 1) {
                        main.showForm(new HistorialDePresupuestos());
                    } else if (subMenuIndex == 2) {
                        main.showForm(new SeleccionarMateriales());
                    }
                }
                if (menuIndex == 3) {
                    if (subMenuIndex == 0) {
                        main.showForm(new RegistrarUser());
                    } else if (subMenuIndex == 1) {
                        main.showForm(new VerUsuarios());
                    } else if (subMenuIndex == 2) {
                        main.showForm(new SeleccionarMateriales());
                    }
                }
            }
        });
        menu.addEventShowPopup(new EventShowPopupMenu() {
            @Override
            public void showPopup(Component com) {
                MenuItem item = (MenuItem) com;
                PopupMenu popup = new PopupMenu(Main.this, item.getIndex(), item.getEventSelected(), item.getMenu().getSubMenu());
                int x = Main.this.getX() + 62;
                int y = Main.this.getY() + com.getY() + 95;
                popup.setLocation(x, y);
                popup.setVisible(true);
            }
        });
        menu.initMenuItem();
        bg.add(menu, "w 230!, spany 2");    // Span Y 2cell
        bg.add(header, "h 50!, wrap");
        bg.add(main, "w 100%, h 100%");
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
        animator = new Animator(500, target);
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

        main.showForm(new Form_Home());
    }

    public void setUserInfo(String correo, String rol) {
        header.setUserInfo(correo, rol);
    }

    public Header getHeader() {
        return header;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        bg = new gui.swing.PanelTransparent();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(false);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
                bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1366, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
                bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 783, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            LoginDialog loginDlg = new LoginDialog(null);
            loginDlg.setVisible(true);

            if (loginDlg.isSucceeded()) {
                Main main = new Main();
                main.setUserInfo(loginDlg.getCorreo(), loginDlg.isAdmin() ? "Admin" : "Usuario");
                main.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }

    private gui.swing.PanelTransparent bg;
}