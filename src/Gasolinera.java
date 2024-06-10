import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Martin
 */

public class Gasolinera {

    private static final int NUM_SURTIDORES = 5;
    private final Semaphore surtidores = new Semaphore(NUM_SURTIDORES, true);
    private final BlockingQueue<Vehiculo> colaVehiculos = new LinkedBlockingQueue<>();
    private final Lock lock = new ReentrantLock();
    private int capacidadCombustible;

    public Gasolinera(int capacidadInicial) {
        this.capacidadCombustible = capacidadInicial;
    }

    public void entrarGasolinera(Vehiculo vehiculo) throws InterruptedException {
        colaVehiculos.put(vehiculo);
        surtidores.acquire();
        Vehiculo vehiculoActual = colaVehiculos.take();

        try {
            if (vehiculoActual instanceof Coche) {
                repostarCoche((Coche) vehiculoActual);
            } else if (vehiculoActual instanceof Camion) {
                recargarGasolinera((Camion) vehiculoActual);
            }
        } finally {
            surtidores.release();
        }
    }

    private void repostarCoche(Coche coche) throws InterruptedException {
        lock.lock();
        try {
            int capacidadDeposito = coche.getTANQUE_GASOLINA() - coche.getGasolina();
            int cantidadRepostar = Math.min(capacidadDeposito, capacidadCombustible);
            cantidadRepostar = Math.min(cantidadRepostar, capacidadDeposito);
            TimeUnit.SECONDS.sleep(3 + new Random().nextInt(3)); // Entre 3 y 5 segundos
            coche.setGasolina(coche.getGasolina() + cantidadRepostar);
            capacidadCombustible -= cantidadRepostar;
            System.out.println("Coche " + coche.getMatricula() + " ha repostado " + cantidadRepostar + " litros. Capacidad restante en gasolinera: " + capacidadCombustible);
        } finally {
            lock.unlock();
        }
    }

    private void recargarGasolinera(Camion camion) throws InterruptedException {
        lock.lock();
        try {
            int cantidadRecarga = camion.getGasolina();
            TimeUnit.SECONDS.sleep(5 + new Random().nextInt(6)); // Entre 5 y 10 segundos
            capacidadCombustible += cantidadRecarga;
            camion.setGasolina(0);
            System.out.println("Camion " + camion.getMatricula() + " ha recargado " + cantidadRecarga + " litros. Capacidad en gasolinera: " + capacidadCombustible);
        } finally {
            lock.unlock();
        }
    }
}

