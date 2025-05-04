package Datos;

import javax.swing.*;

public class Clientes {

    int IdCLiente;
    String campoNombre;
    String campoCIF;
    String campoEmail;
    String campoPersonaDeContacto;
    String campoDireccion;
    String campoDescripcion;

    public Clientes(int idCLiente, String campoNombre, String campoCIF, String campoEmail, String campoPersonaDeContacto, String campoDireccion, String campoDescripcion) {
        IdCLiente = idCLiente;
        this.campoNombre = campoNombre;
        this.campoCIF = campoCIF;
        this.campoEmail = campoEmail;
        this.campoPersonaDeContacto = campoPersonaDeContacto;
        this.campoDireccion = campoDireccion;
        this.campoDescripcion = campoDescripcion;
    }
}
