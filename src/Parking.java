import java.util.concurrent.LinkedBlockingQueue;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Martin
 */

public class Parking {

    private final LinkedBlockingQueue<Coche> coches;
    private final LinkedBlockingQueue<Camion> camiones;

    public Parking() {
        this.coches = new LinkedBlockingQueue<>();
        this.camiones = new LinkedBlockingQueue<>();
    }

    public void addCoche(Coche coche) {
        try {
            coches.put(coche);
            System.out.println("Coche " + coche.getMatricula() + " ha sido aniadido al parking.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al anadir coche al parking: " + e.getMessage());
        }
    }

    public void addCamion(Camion camion) {
        try {
            camiones.put(camion);
            System.out.println("Camion " + camion.getMatricula() + " ha sido aniadido al parking.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error al aniadir camión al parking: " + e.getMessage());
        }
    }

    public Coche getCoche() throws InterruptedException {
        return coches.take();
    }

    public Camion getCamion() throws InterruptedException {
        return camiones.take();
    }
}