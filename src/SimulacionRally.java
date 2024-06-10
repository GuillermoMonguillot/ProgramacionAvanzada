import java.util.Random;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Martin
 */
public class SimulacionRally {

    public static void main(String[] args) {
        // Crear instancias de parking, gasolineras, puentes y tramos cronometrados
        Gasolinera gasolineraIzq = new Gasolinera(500);
        Gasolinera gasolineraDch = new Gasolinera(500);
        Puente puenteIzq = new Puente();
        Puente puenteDch = new Puente();
        TramoCronometrado tramo1 = new TramoCronometrado(1);
        TramoCronometrado tramo2 = new TramoCronometrado(2);
        TramoCronometrado tramo3 = new TramoCronometrado(3);
        TramoCronometrado tramo4 = new TramoCronometrado(4);
        Parking parking = new Parking();

        // Crear y arrancar coches
        for (int i = 0; i < 8000; i++) {
            Coche coche = new Coche(75 + new Random().nextInt(56), 25 + new Random().nextInt(26), 130, 25, gasolineraIzq, gasolineraDch, puenteIzq, puenteDch, tramo1, tramo2, tramo3, tramo4);
            parking.addCoche(coche);
            coche.start();
        }

        // Crear y arrancar camiones
        for (int i = 0; i < 1000; i++) {
            int gasolina = 500 + new Random().nextInt(501); // Entre 500 y 1000 litros
            Camion camion = new Camion(gasolina, 1000, 500, gasolineraIzq, gasolineraDch, puenteIzq, puenteDch);
            parking.addCamion(camion);
            camion.start();
        }

        // Aquí se puede agregar la lógica para coordinar la interacción entre los diferentes componentes
    }
}



