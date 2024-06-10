import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
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
public class TramoCronometrado {

    private static final int MAX_COCHES_EN_TRAMO = 4;
    private final Queue<Coche> colaCoches = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final int numeroTramo;
    private static String clima = "SOLEADO";

    public TramoCronometrado(int numeroTramo) {
        this.numeroTramo = numeroTramo;
    }

    public static String getClima() {
        return clima;
    }

    public static void setClima(String nuevoClima) {
        clima = nuevoClima;
    }

    public void entrarTramo(Coche coche) throws InterruptedException {
        colaCoches.add(coche);
        while (true) {
            lock.lock();
            try {
                if (colaCoches.peek() == coche && colaCoches.size() <= MAX_COCHES_EN_TRAMO) {
                    colaCoches.poll();
                    recorrerTramo(coche);
                    break;
                }
            } finally {
                lock.unlock();
            }
            Thread.sleep(1000); // Esperar antes de volver a intentar
        }
    }

    private void recorrerTramo(Coche coche) throws InterruptedException {
        // Verificación del oficial
        System.out.println("Coche " + coche.getMatricula() + " siendo verificado por el oficial en tramo " + numeroTramo);
        Thread.sleep(1000 + new Random().nextInt(2000)); // Entre 1 y 3 segundos

        long tiempoTotal = 0;
        for (int i = 1; i <= 4; i++) {
            long tiempoSector = 4000 + new Random().nextInt(7000); // Entre 4 y 10 segundos
            if (!coche.getNeumatico().equals(clima)) {
                tiempoSector *= 3; // Si las ruedas no son adecuadas, tarda el triple
            }
            System.out.println("Coche " + coche.getMatricula() + " recorriendo sector " + i + " del tramo " + numeroTramo);
            Thread.sleep(tiempoSector);
            tiempoTotal += tiempoSector;

            if (i == 3) {
                // Permitir que otro coche entre al tramo
                lock.lock();
                try {
                    if (colaCoches.size() < MAX_COCHES_EN_TRAMO) {
                        lock.unlock();
                        Thread.sleep(1000); // Simular el ingreso de otro coche
                        lock.lock();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        // Calcular y registrar el tiempo total del tramo
        long tiempoEnSegundos = tiempoTotal / 1000;
        LocalTime horaFinalizacion = LocalTime.now();
        String registro = String.format("Coche: %s, Hora: %s, Tramo: %d, Tiempo: %d segundos",
                coche.getMatricula(), horaFinalizacion, numeroTramo, tiempoEnSegundos);
        System.out.println(registro);
        // Aquí se puede agregar el código para guardar el registro en una estructura interna
    }
}
