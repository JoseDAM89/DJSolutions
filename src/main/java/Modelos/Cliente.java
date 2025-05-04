package Modelos;

public class Cliente {


    String campoNombre;
    String campoCIF;
    String campoEmail;
    String campoPersonaDeContacto;
    String campoDireccion;
    String campoDescripcion;

    public Cliente(String campoNombre, String campoCIF, String campoEmail, String campoPersonaDeContacto, String campoDireccion, String campoDescripcion) {

        this.campoNombre = campoNombre;
        this.campoCIF = campoCIF;
        this.campoEmail = campoEmail;
        this.campoPersonaDeContacto = campoPersonaDeContacto;
        this.campoDireccion = campoDireccion;
        this.campoDescripcion = campoDescripcion;
    }


    public String getCampoNombre() {
        return campoNombre;
    }

    public String getCampoCIF() {
        return campoCIF;
    }

    public String getCampoEmail() {
        return campoEmail;
    }

    public String getCampoPersonaDeContacto() {
        return campoPersonaDeContacto;
    }

    public String getCampoDireccion() {
        return campoDireccion;
    }

    public String getCampoDescripcion() {
        return campoDescripcion;
    }


    public void setCampoNombre(String campoNombre) {
        this.campoNombre = campoNombre;
    }

    public void setCampoCIF(String campoCIF) {
        this.campoCIF = campoCIF;
    }

    public void setCampoEmail(String campoEmail) {
        this.campoEmail = campoEmail;
    }

    public void setCampoPersonaDeContacto(String campoPersonaDeContacto) {
        this.campoPersonaDeContacto = campoPersonaDeContacto;
    }

    public void setCampoDireccion(String campoDireccion) {
        this.campoDireccion = campoDireccion;
    }

    public void setCampoDescripcion(String campoDescripcion) {
        this.campoDescripcion = campoDescripcion;
    }
}
