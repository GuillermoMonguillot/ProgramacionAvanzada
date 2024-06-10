import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
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
public class Puente {

    private static final int CAPACIDAD_GRUPO = 10;
    private final Semaphore semaforo = new Semaphore(CAPACIDAD_GRUPO, true);
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int sentidoActual = 0; // 0: ninguno, 1: izquierda, 2: derecha
    private int esperandoIzquierda = 0;
    private int esperandoDerecha = 0;

    public void cruzarIzquierda(Vehiculo vehiculo) throws InterruptedException {
        lock.lock();
        try {
            esperandoIzquierda++;
            while (sentidoActual == 2 || esperandoIzquierda < CAPACIDAD_GRUPO) {
                condition.await();
            }
            esperandoIzquierda--;
            if (esperandoIzquierda == 0) {
                sentidoActual = 0;
            }
            if (esperandoIzquierda == 0 || semaforo.availablePermits() == 0) {
                sentidoActual = 2;
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }

        semaforo.acquire();
        try {
            System.out.println(vehiculo.getMatricula() + " está cruzando el puente hacia la izquierda.");
            Thread.sleep(5000);
        } finally {
            semaforo.release();
            if (semaforo.availablePermits() == CAPACIDAD_GRUPO) {
                lock.lock();
                try {
                    sentidoActual = 2;
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public void cruzarDerecha(Vehiculo vehiculo) throws InterruptedException {
        lock.lock();
        try {
            esperandoDerecha++;
            while (sentidoActual == 1 || esperandoDerecha < CAPACIDAD_GRUPO) {
                condition.await();
            }
            esperandoDerecha--;
            if (esperandoDerecha == 0) {
                sentidoActual = 0;
            }
            if (esperandoDerecha == 0 || semaforo.availablePermits() == 0) {
                sentidoActual = 1;
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }

        semaforo.acquire();
        try {
            System.out.println(vehiculo.getMatricula() + " está cruzando el puente hacia la derecha.");
            Thread.sleep(5000);
        } finally {
            semaforo.release();
            if (semaforo.availablePermits() == CAPACIDAD_GRUPO) {
                lock.lock();
                try {
                    sentidoActual = 1;
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}

