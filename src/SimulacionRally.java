import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulacionRally {

    public static void main(String[] args) {
        // Crear instancias de gasolineras, puentes y tramos cronometrados
        Gasolinera gasolineraIzq = new Gasolinera(500);
        Gasolinera gasolineraDch = new Gasolinera(500);
        Puente puenteIzq = new Puente();
        Puente puenteDch = new Puente();
        TramoCronometrado tramo1 = new TramoCronometrado(1);
        TramoCronometrado tramo2 = new TramoCronometrado(2);
        TramoCronometrado tramo3 = new TramoCronometrado(3);
        TramoCronometrado tramo4 = new TramoCronometrado(4);
        Parking parking = new Parking();

        // Crear un ExecutorService para gestionar la creación de hilos concurrentes
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Tarea para crear y arrancar coches de rally
        Runnable crearCoches = () -> {
            Random random = new Random();
            for (int i = 0; i < 200; i++) {
                try {
                    Coche coche = new Coche(75 + random.nextInt(56), 25 + random.nextInt(26), 130, 25, gasolineraIzq, gasolineraDch, puenteIzq, puenteDch, tramo1, tramo2, tramo3, tramo4);
                    parking.addCoche(coche);
                    coche.start();
                    TimeUnit.SECONDS.sleep(1 + random.nextInt(3)); // Entre 1 y 3 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Tarea para crear y arrancar camiones de combustible
        Runnable crearCamiones = () -> {
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                try {
                    int gasolina = 500 + random.nextInt(501); // Entre 500 y 1000 litros
                    Camion camion = new Camion(gasolina, 1000, 500, gasolineraIzq, gasolineraDch, puenteIzq, puenteDch);
                    parking.addCamion(camion);
                    camion.start();
                    TimeUnit.SECONDS.sleep(2 + random.nextInt(3)); // Entre 2 y 4 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Ejecutar las tareas concurrentemente
        executor.execute(crearCoches);
        executor.execute(crearCamiones);

        // Shutdown the executor service gracefully
        executor.shutdown();

        // Aquí se puede agregar la lógica para coordinar la interacción entre los diferentes componentes y la interfaz
    }
}
