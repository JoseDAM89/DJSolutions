package gui.swing.tablero;

import gui.modelosvista.ModelStudent;

public interface EventAction {

    public void delete(ModelStudent student);

    public void update(ModelStudent student);
}
