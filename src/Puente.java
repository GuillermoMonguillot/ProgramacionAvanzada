import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Puente {

    private static final int CAPACIDAD_GRUPO = 10;
    private final CyclicBarrier barrierIzquierda;
    private final CyclicBarrier barrierDerecha;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean ultimoSentidoIzquierda = true;
    private int vehiculosEsperandoIzquierda = 0;
    private int vehiculosEsperandoDerecha = 0;

    public Puente() {
        barrierIzquierda = new CyclicBarrier(CAPACIDAD_GRUPO, () -> {
            // Acción a realizar cuando el grupo completo ha alcanzado la barrera
            Logger.getInstance().log("Puente", "Grupo de 10 vehículos cruzando hacia la izquierda.");
            try {
                Thread.sleep(5000); // 5 segundos para cruzar el puente
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        barrierDerecha = new CyclicBarrier(CAPACIDAD_GRUPO, () -> {
            // Acción a realizar cuando el grupo completo ha alcanzado la barrera
            Logger.getInstance().log("Puente", "Grupo de 10 vehículos cruzando hacia la derecha.");
            try {
                Thread.sleep(5000); // 5 segundos para cruzar el puente
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void cruzarIzquierda(Vehiculo vehiculo) throws InterruptedException {
        lock.lock();
        try {
            vehiculosEsperandoIzquierda++;
            while (!ultimoSentidoIzquierda || vehiculosEsperandoIzquierda < CAPACIDAD_GRUPO) {
                condition.await();
            }
            vehiculosEsperandoIzquierda--;
            Logger.getInstance().log("Puente", vehiculo.getMatricula() + " está esperando para cruzar hacia la izquierda.");
        } finally {
            lock.unlock();
        }

        try {
            barrierIzquierda.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        cruzar(vehiculo, true);
    }

    public void cruzarDerecha(Vehiculo vehiculo) throws InterruptedException {
        lock.lock();
        try {
            vehiculosEsperandoDerecha++;
            while (ultimoSentidoIzquierda || vehiculosEsperandoDerecha < CAPACIDAD_GRUPO) {
                condition.await();
            }
            vehiculosEsperandoDerecha--;
            Logger.getInstance().log("Puente", vehiculo.getMatricula() + " está esperando para cruzar hacia la derecha.");
        } finally {
            lock.unlock();
        }

        try {
            barrierDerecha.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        cruzar(vehiculo, false);
    }

    private void cruzar(Vehiculo vehiculo, boolean esIzquierda) {
        Logger.getInstance().log("Puente", vehiculo.getMatricula() + " ha cruzado el puente hacia " + (esIzquierda ? "la izquierda" : "la derecha") + ".");
        lock.lock();
        try {
            if (esIzquierda && vehiculosEsperandoIzquierda == 0) {
                ultimoSentidoIzquierda = false;
                condition.signalAll();
            } else if (!esIzquierda && vehiculosEsperandoDerecha == 0) {
                ultimoSentidoIzquierda = true;
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
