package JSWING.component;

import JSWING.swing.PanelTransparent;
import java.awt.event.ActionListener;

public class Header extends PanelTransparent {

    public Header() {
        initComponents();
        setTransparent(0.5f);
    }

    public void addMenuEvent(ActionListener event) {
        cmdMenu.addActionListener(event);
    }

    // Método para actualizar el usuario y el rol que se muestran en el Header
    public void setUserInfo(String userName, String role) {
        lbUserName.setText(userName);
        lbRole.setText(role);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdMenu = new JSWING.swing.Button();
        pic = new JSWING.swing.ImageAvatar();
        lbUserName = new javax.swing.JLabel();
        lbRole = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        buttonBadges1 = new JSWING.swing.ButtonBadges();
        buttonBadges2 = new JSWING.swing.ButtonBadges();

        cmdMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/JSWINGICONS/icon/menu.png"))); // NOI18N

        pic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/JSWINGICONS/icon/profile.jpg"))); // NOI18N

        lbUserName.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lbUserName.setForeground(new java.awt.Color(127, 127, 127));
        lbUserName.setText("User Name");

        lbRole.setForeground(new java.awt.Color(127, 127, 127));
        lbRole.setText("Admin");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        buttonBadges1.setForeground(new java.awt.Color(250, 49, 49));
        buttonBadges1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/JSWINGICONS/icon/notification.png"))); // NOI18N
        buttonBadges1.setBadges(12);

        buttonBadges2.setForeground(new java.awt.Color(63, 178, 232));
        buttonBadges2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/JSWINGICONS/icon/message.png"))); // NOI18N
        buttonBadges2.setBadges(5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(cmdMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 362, Short.MAX_VALUE)
                                .addComponent(buttonBadges2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(buttonBadges1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbUserName, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbRole, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lbUserName)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(lbRole))
                                        .addComponent(cmdMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSeparator1)
                                        .addComponent(buttonBadges1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(buttonBadges2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JSWING.swing.ButtonBadges buttonBadges1;
    private JSWING.swing.ButtonBadges buttonBadges2;
    private JSWING.swing.Button cmdMenu;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbRole;
    private javax.swing.JLabel lbUserName;
    private JSWING.swing.ImageAvatar pic;
    // End of variables declaration//GEN-END:variables
}
