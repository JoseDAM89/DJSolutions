package gui.componentes;

import gui.eventos.EventMenu;
import gui.eventos.EventMenuSelected;
import gui.eventos.EventShowPopupMenu;
import gui.modelosvista.ModelMenu;
import gui.swing.MenuAnimation;
import gui.swing.MenuItem;
import gui.swing.PanelTransparent;
import gui.swing.scrollbar.ScrollBarCustom;

import java.awt.*;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;

public class Menu extends PanelTransparent {

    public boolean isShowMenu() {
        return showMenu;
    }

    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public void addEventShowPopup(EventShowPopupMenu eventShowPopup) {
        this.eventShowPopup = eventShowPopup;
    }

    private final MigLayout layout;
    private EventMenuSelected event;
    private EventShowPopupMenu eventShowPopup;
    private boolean enableMenu = true;
    private boolean showMenu = true;

    public Menu() {
        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
        panel.setLayout(layout);
        setTransparent(0.5f);
    }

    public void initMenuItem() {
        boolean esAdmin = modelos.Sesion.esAdmin();

        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/JSWINGICONS/icon/1.png")), "Funciones Cliente", "Alta Cliente", "Listar Clientes"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/JSWINGICONS/icon/2.png")), "Funciones Inventario", "Alta Productos", "Consultar Stock", "Listar Productos"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/JSWINGICONS/icon/3.png")), "Funciones Presupuesto", "Generar Presupuesto", "Historial de Presupuesto", "Seleccionar Materiales"));
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/JSWINGICONS/icon/5.png")), "Funciones Factura", "Generar Factura", "Historial Facturas"));

        if (esAdmin) {
            addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/JSWINGICONS/icon/4.png")), "Gestion de Usuarios", "Registrar Usuario", "Ver Usuario"));
        }
    }

    private void addMenu(ModelMenu menu) {
        panel.add(new MenuItem(menu, getEventMenu(), event, panel.getComponentCount()), "h 40!");
    }

    private EventMenu getEventMenu() {
        return new EventMenu() {
            @Override
            public boolean menuPressed(Component com, boolean open) {
                if (enableMenu) {
                    if (isShowMenu()) {
                        if (open) {
                            new MenuAnimation(layout, com).openMenu();
                        } else {
                            new MenuAnimation(layout, com).closeMenu();
                        }
                        return true;
                    } else {
                        eventShowPopup.showPopup(com);
                    }
                }
                return false;
            }
        };
    }

    public void hideallMenu() {
        for (Component com : panel.getComponents()) {
            MenuItem item = (MenuItem) com;
            if (item.isOpen()) {
                new MenuAnimation(layout, com, 500).closeMenu();
                item.setOpen(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        profile1 = new gui.componentes.Profile();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setViewportBorder(null);

        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 312, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 523, Short.MAX_VALUE)
        );

        sp.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g);

        // Nuevo degradado azul sobrio
        GradientPaint gp = new GradientPaint(
                0, 0, new Color(45, 80, 115),
                0, getHeight(), new Color(100, 145, 185)
        );

        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private gui.componentes.Profile profile1;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
