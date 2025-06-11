package modelos;

public class Cliente {

    private int idcliente; // Clave primaria

    private String campoNombre;
    private String cif;
    private String email;
    private String campoPersonaDeContacto;
    private String campoDireccion;
    private String campoDescripcion;

    // 🔹 Constructor completo con ID (para consultas y edición)
    public Cliente(int idcliente, String campoNombre, String cif, String email,
                   String campoPersonaDeContacto, String campoDireccion, String campoDescripcion) {
        this.idcliente = idcliente;
        this.campoNombre = campoNombre;
        this.cif = cif;
        this.email = email;
        this.campoPersonaDeContacto = campoPersonaDeContacto;
        this.campoDireccion = campoDireccion;
        this.campoDescripcion = campoDescripcion;
    }

    // 🔹 Constructor sin ID (para inserciones nuevas)
    public Cliente(String campoNombre, String cif, String email,
                   String campoPersonaDeContacto, String campoDireccion, String campoDescripcion) {
        this.campoNombre = campoNombre;
        this.cif = cif;
        this.email = email;
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

    public String getcif() {
        return cif;
    }

    public String getemail() {
        return email;
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

    public void setcif(String cif) {
        this.cif = cif;
    }

    public void setemail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return campoNombre; // Cliente
    }

}
