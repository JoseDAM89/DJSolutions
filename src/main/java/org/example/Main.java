package org.example;


import Datos.ClienteDAO;
import Datos.ConexionBD;
import GUI.GestionUsuarios;
import GUI.Vprin;
import Modelos.Cliente;

public class Main {
    public static void main(String[] args) {

        
        Vprin ventana = new Vprin();
            ventana.ponPanel(new GestionUsuarios(ventana));
            ventana.setVisible(true);

    }
}
