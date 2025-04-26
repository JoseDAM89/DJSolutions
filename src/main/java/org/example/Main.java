package org.example;


import GUI.GestionUsuarios;
import GUI.Vprin;

public class Main {
    public static void main(String[] args) {


            Vprin ventana = new Vprin();
            ventana.ponPanel(new GestionUsuarios(ventana,true));
            ventana.setVisible(true);

    }
}
