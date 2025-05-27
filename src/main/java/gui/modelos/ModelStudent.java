package gui.modelos;

import gui.swing.tablero.EventAction;
import gui.swing.tablero.ModelAction;
import gui.swing.tablero.ModelProfile;

import javax.swing.Icon;

public class ModelStudent {

    private Icon icon;
    private String name;
    private String tipo;
    private String correo;
    private String descripcion;

    public ModelStudent() {
    }

    public ModelStudent(Icon icon, String name, String tipo, String correo, String descripcion) {
        this.icon = icon;
        this.name = name;
        this.tipo = tipo;
        this.correo = correo;
        this.descripcion = descripcion;
    }

    // Getters
    public Icon getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Setters
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Conversión a fila de tabla
    public Object[] toRowTable(EventAction event) {
        return new Object[]{
                new ModelProfile(icon, name),
                tipo,
                correo,
                descripcion,
                new ModelAction(this, event)
        };
    }
}
