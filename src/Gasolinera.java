import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Gasolinera {

    private int capacidad;
    private final Lock lock = new ReentrantLock();
    private final Condition surtidorLibre = lock.newCondition();
    private final int numSurtidores = 5;
    private int surtidoresOcupados = 0;
    private final Logger logger;

    public Gasolinera(int capacidadInicial) {
        this.capacidad = capacidadInicial;
        this.logger = Logger.getInstance();
    }

    public void entrarGasolinera(Vehiculo vehiculo) throws InterruptedException {
        lock.lock();
        try {
            while (surtidoresOcupados >= numSurtidores) {
                surtidorLibre.await();
            }
            surtidoresOcupados++;
            int cantidadRepostar = Math.min(vehiculo.getTANQUE_GASOLINA() - vehiculo.getGasolina(), capacidad);
            logger.log("Gasolinera", vehiculo.getMatricula() + " empieza a repostar " + cantidadRepostar + " litros.");
            Thread.sleep(3000 + new Random().nextInt(3000)); // Entre 3 y 5 segundos
            capacidad -= cantidadRepostar;
            vehiculo.setGasolina(vehiculo.getGasolina() + cantidadRepostar);
            logger.log("Gasolinera", vehiculo.getMatricula() + " ha repostado " + cantidadRepostar + " litros. Gasolina restante: " + capacidad + " litros.");
        } finally {
            surtidoresOcupados--;
            surtidorLibre.signalAll();
            lock.unlock();
        }
    }

    public void reponerCombustible(Camion camion) throws InterruptedException {
        lock.lock();
        try {
            logger.log("Gasolinera", camion.getMatricula() + " empieza a reponer combustible.");
            Thread.sleep(5000 + new Random().nextInt(5000)); // Entre 5 y 10 segundos
            capacidad += camion.getGasolina();
            logger.log("Gasolinera", camion.getMatricula() + " ha repuesto " + camion.getGasolina() + " litros. Gasolina restante: " + capacidad + " litros.");
            camion.setGasolina(0);
        } finally {
            surtidorLibre.signalAll();
            lock.unlock();
        }
    }

    public int getCapacidad() {
        return capacidad;
    }
}
