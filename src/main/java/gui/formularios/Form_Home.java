package gui.formularios;

import modelos.Cliente;
import datos.ConexionBD;
import gui.dialogos.Message;
import gui.modelosvista.ModelCard;
import gui.modelosvista.ModelStudent;
import gui.swing.iconos.GoogleMaterialDesignIcons;
import gui.swing.iconos.IconFontSwing;
import gui.swing.aviso.ModelNoticeBoard;
import gui.swing.tablero.EventAction;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class Form_Home extends javax.swing.JPanel {

    public Form_Home() {
        initComponents();
        table1.fixTable(jScrollPane1);
        setOpaque(false);
        initData();
    }

    private void initData() {
        initCardData();
        initNoticeBoard();
        initTableData();
    }

    private void initTableData() {
        EventAction eventAction = new EventAction() {
            @Override
            public void delete(ModelStudent student) {
                showMessage("Eliminar cliente: " + student.getName());
            }

            @Override
            public void update(ModelStudent student) {
                showMessage("Actualizar cliente: " + student.getName());
            }
        };

        String sql = "SELECT idcliente, campoNombre, campoCIF, campoEmail, campoPersonaDeContacto, campoDireccion, campoDescripcion FROM clientes";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idcliente"),
                        rs.getString("campoNombre"),
                        rs.getString("campoCIF"),
                        rs.getString("campoEmail"),
                        rs.getString("campoPersonaDeContacto"),
                        rs.getString("campoDireccion"),
                        rs.getString("campoDescripcion")
                );

                String nombre = cliente.getCampoNombre();
                String email = cliente.getCampoEmail();
                String descripcion = cliente.getCampoDescripcion();
                ImageIcon icono = new ImageIcon(getClass().getResource("/JSWINGICONS/icon/profile.jpg"));
                ModelStudent student = new ModelStudent(icono, nombre, "Cliente", email, descripcion);
                table1.addRow(student.toRowTable(eventAction));
            }

        } catch (SQLException e) {
            showMessage("❌ Error al cargar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int contarAprobados(String tabla) {
        int cantidad = 0;
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE aceptado = TRUE";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                cantidad = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    private int contarPagadas(String tabla) {
        int cantidad = 0;
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE pagada = TRUE";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                cantidad = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    private int contarTotales(String tabla) {
        int cantidad = 0;
        String sql = "SELECT COUNT(*) FROM " + tabla;

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                cantidad = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cantidad;
    }

    private int calcularPorcentaje(int parte, int total) {
        if (total == 0) return 0;
        return Math.min((parte * 100) / total, 100);
    }

    private void initCardData() {
        int totalProductos = contarTotales("productos");
        int totalClientes = contarTotales("clientes");

        int facturasTotales = contarTotales("facturas");
        int facturasAprobadas = contarPagadas("facturas");

        int presupuestosTotales = contarTotales("presupuestos");
        int presupuestosAprobados = contarAprobados("presupuestos");

        Icon icon1 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.SHOPPING_BASKET, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card1.setData(new ModelCard("Productos", totalProductos, calcularPorcentaje(totalProductos, 1000), icon1));

        Icon icon2 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PEOPLE, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card2.setData(new ModelCard("Clientes", totalClientes, calcularPorcentaje(totalClientes, 1000), icon2));

        Icon icon3 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.ACCOUNT_BALANCE, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card3.setData(new ModelCard("Facturas Pagadas", facturasAprobadas, calcularPorcentaje(facturasAprobadas, facturasTotales), icon3));

        Icon icon4 = IconFontSwing.buildIcon(GoogleMaterialDesignIcons.PRINT, 60, new Color(255, 255, 255, 100), new Color(255, 255, 255, 15));
        card4.setData(new ModelCard("Presupuestos Aprobados", presupuestosAprobados, calcularPorcentaje(presupuestosAprobados, presupuestosTotales), icon4));
    }

    private void initNoticeBoard() {
        noticeBoard.addDate("27/05/2025");

        noticeBoard.addNoticeBoard(new ModelNoticeBoard(
                new Color(174, 54, 12), // Naranja fuerte
                "Inventario",
                "Ahora",
                "Mejoramos el control del stock. Ahora puedes ver fácilmente los productos con poco inventario."
        ));

        noticeBoard.addNoticeBoard(new ModelNoticeBoard(
                new Color(156, 39, 176), // Púrpura vibrante
                "Clientes",
                "Hace 1 hora",
                "Puedes Visualizar todos los clientes registrados"
        ));

        noticeBoard.addDate("26/05/2025");

        noticeBoard.addNoticeBoard(new ModelNoticeBoard(
                new Color(76, 175, 80), // Verde brillante
                "Reportes",
                "14:30",
                "Agregamos nuevos reportes de ventas. Puedes descargarlos en PDF."
        ));

        noticeBoard.addNoticeBoard(new ModelNoticeBoard(
                new Color(218, 12, 12), // Azul cielo (resalta bien sobre azul oscuro)
                "Productos",
                "11:00",
                "El formulario para agregar productos es más fácil."
        ));

        noticeBoard.addNoticeBoard(new ModelNoticeBoard(
                new Color(0, 188, 212), // Cian fuerte
                "Permisos",
                "9:15",
                "Puedes dar permisos a cada usuario. Controla quién puede ver o editar datos."
        ));

        noticeBoard.addNoticeBoard(new ModelNoticeBoard(
                new Color(205, 36, 23), // Rojo brillante
                "Actualización",
                "7:00",
                "La App muy sencilla así cualquier persona puede entenderla y utilizarla fácilmente"
        ));

        noticeBoard.scrollToTop();
    }


    private void showMessage(String message) {
        Message obj = new Message(JFrame.getFrames()[0], true);
        obj.showMessage(message);
    }

    // Componentes Swing generados automáticamente...
    // Aquí incluirías tu método initComponents() tal como ya lo tienes.



@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        card1 = new gui.componentes.Card();
        jLabel1 = new javax.swing.JLabel();
        card2 = new gui.componentes.Card();
        card3 = new gui.componentes.Card();
        card4 = new gui.componentes.Card();
        panelTransparent1 = new gui.swing.PanelTransparent();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new gui.swing.tablero.Table();
        jLabel5 = new javax.swing.JLabel();
        panelTransparent2 = new gui.swing.PanelTransparent();
        noticeBoard = new gui.swing.aviso.NoticeBoard();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        card1.setColorGradient(new Color(211, 28, 215));

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel1.setForeground(new Color(0, 0, 0));
        jLabel1.setText("Pagina Principal");

        card2.setBackground(new Color(10, 30, 214));
        card2.setColorGradient(new Color(72, 111, 252));

        card3.setBackground(new Color(194, 85, 1));
        card3.setColorGradient(new Color(255, 212, 99));

        card4.setBackground(new Color(60, 195, 0));
        card4.setColorGradient(new Color(208, 255, 90));

        panelTransparent1.setTransparent(0.0F);


        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Tipo", "Correo", "Descripcion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setPreferredWidth(150);
        }


        jLabel5.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        jLabel5.setForeground(new Color(0, 0, 0));
        jLabel5.setText("Clientes Registrados");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        javax.swing.GroupLayout panelTransparent1Layout = new javax.swing.GroupLayout(panelTransparent1);
        panelTransparent1.setLayout(panelTransparent1Layout);
        panelTransparent1Layout.setHorizontalGroup(
            panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTransparent1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        panelTransparent1Layout.setVerticalGroup(
            panelTransparent1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelTransparent2.setTransparent(0.0F);

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel3.setForeground(new Color(0, 0, 0));
        jLabel3.setText("Consultas DJSOLUTIONS");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 15)); // NOI18N
        jLabel2.setForeground(new Color(0, 0, 0));
        jLabel2.setText("Ultimas Noticias");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));

        jLabel4.setOpaque(true);

        javax.swing.GroupLayout panelTransparent2Layout = new javax.swing.GroupLayout(panelTransparent2);
        panelTransparent2.setLayout(panelTransparent2Layout);
        panelTransparent2Layout.setHorizontalGroup(
            panelTransparent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransparent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(noticeBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelTransparent2Layout.createSequentialGroup()
                        .addGroup(panelTransparent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 257, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelTransparent2Layout.setVerticalGroup(
            panelTransparent2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparent2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addGap(9, 9, 9)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(noticeBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(card1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(card2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(card3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(card4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(panelTransparent1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelTransparent2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTransparent2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTransparent1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

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
    private gui.componentes.Card card1;
    private gui.componentes.Card card2;
    private gui.componentes.Card card3;
    private gui.componentes.Card card4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private gui.swing.aviso.NoticeBoard noticeBoard;
    private gui.swing.PanelTransparent panelTransparent1;
    private gui.swing.PanelTransparent panelTransparent2;
    private gui.swing.tablero.Table table1;
    // End of variables declaration//GEN-END:variables
}
