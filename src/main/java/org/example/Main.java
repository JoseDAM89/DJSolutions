package org.example;


import Datos.ConexionBD;
import GUI.GestionUsuarios;
import GUI.Vprin;

public class Main {
    public static void main(String[] args) {

            Vprin ventana = new Vprin();
            ventana.ponPanel(new GestionUsuarios(ventana));
            ventana.setVisible(true);

    }
}
