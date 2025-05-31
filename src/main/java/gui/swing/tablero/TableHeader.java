package gui.swing.tablero;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class TableHeader extends JLabel {

    public TableHeader(String text) {
        super(text);
        setFont(new Font("sansserif", 1, 12));
        setForeground(new Color(0, 0, 0));
        setBorder(new EmptyBorder(10, 5, 10, 5));

    }
}
