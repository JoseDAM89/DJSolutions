package Modelos;

public class Cliente {

    private int idcliente; // Clave primaria

    private String campoNombre;
    private String campoCIF;
    private String campoEmail;
    private String campoPersonaDeContacto;
    private String campoDireccion;
    private String campoDescripcion;

    // 🔹 Constructor completo con ID (para consultas y edición)
    public Cliente(int idcliente, String campoNombre, String campoCIF, String campoEmail,
                   String campoPersonaDeContacto, String campoDireccion, String campoDescripcion) {
        this.idcliente = idcliente;
        this.campoNombre = campoNombre;
        this.campoCIF = campoCIF;
        this.campoEmail = campoEmail;
        this.campoPersonaDeContacto = campoPersonaDeContacto;
        this.campoDireccion = campoDireccion;
        this.campoDescripcion = campoDescripcion;
    }

    // 🔹 Constructor sin ID (para inserciones nuevas)
    public Cliente(String campoNombre, String campoCIF, String campoEmail,
                   String campoPersonaDeContacto, String campoDireccion, String campoDescripcion) {
        this.campoNombre = campoNombre;
        this.campoCIF = campoCIF;
        this.campoEmail = campoEmail;
        this.campoPersonaDeContacto = campoPersonaDeContacto;
        this.campoDireccion = campoDireccion;
        this.campoDescripcion = campoDescripcion;
    }

    // 🔹 Getters
    public int getIdcliente() {
        return idcliente;
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

    // 🔹 Setters
    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
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
