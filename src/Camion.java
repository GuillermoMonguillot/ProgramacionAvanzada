
import static java.lang.Math.random;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Martin
 */
public class Camion extends Vehiculo {

    private static final Set<String> matriculasGeneradas = new HashSet<>();
    private Random random = new Random();
    private static final int CAPACIDAD_MAXIMA = 1000;
    private int cargaCombustible;
    public Camion(int gasolina, int maximo_tanque, int minimo_tanque) {
        super(generarMatricula(),CAPACIDAD_MAXIMA,gasolina, maximo_tanque, minimo_tanque);

    }

    private static synchronized String generarMatricula() {
        Random random = new Random();
        String matricula;
        do {
            int id = random.nextInt(10000);
            matricula = String.format("CC-%04d", id);
        } while (matriculasGeneradas.contains(matricula));
        matriculasGeneradas.add(matricula);
        return matricula;
    }

    public void run() {
        try {
            while (true) {
                // 1. Cargar combustible en la ciudad
                cargarCombustibleEnCiudad();

                // 2. Viaje al parking
                TimeUnit.SECONDS.sleep(5 + random.nextInt(6)); // Entre 5 y 10 segundos

                // 3. Descanso en el parking
                descansarEnParking();

                // 4. Dirigirse a la gasolinera
                int tramo = seleccionarTramo();
                dirigirseAGasolinera(tramo);

                // 5. Rellenar el depósito de la gasolinera
                rellenarDepositoGasolinera(tramo);

                // 6. Regreso a la ciudad
                regresarACiudad();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void cargarCombustibleEnCiudad() throws InterruptedException {
        System.out.println("Camion " + getMatricula() + " cargando combustible en la ciudad.");
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
        this.cargaCombustible = CAPACIDAD_MAXIMA / 2 + random.nextInt(CAPACIDAD_MAXIMA / 2); // Entre el 50% y el 100%
    }

    private void descansarEnParking() throws InterruptedException {
        System.out.println("Camion " + getMatricula() + " descansando en el parking.");
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
    }

    private int seleccionarTramo() {
        int idNumerico = Integer.parseInt(getMatricula().split("-")[1]);
        return idNumerico % 2 == 0 ? 1 : 2; // Par: Izq, Impar: Dch
    }

    private void dirigirseAGasolinera(int tramo) throws InterruptedException {
        System.out.println("Camion " + getMatricula() + " dirigiéndose a la gasolinera " + (tramo == 1 ? "Izq" : "Dch"));
        // Del Parking al Puente
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos

        // Del Puente a la Gasolinera
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
    }

    private void rellenarDepositoGasolinera(int tramo) {
        System.out.println("Camion " + getMatricula() + " rellenando el depósito de la gasolinera " + (tramo == 1 ? "Izq" : "Dch"));
        this.cargaCombustible = 0; // Descarga todo el combustible
    }

    private void regresarACiudad() throws InterruptedException {
        System.out.println("Camion " + getMatricula() + " regresando a la ciudad.");
        // Del Gasolinera al Puente
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
        // Del Puente al Parking
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
        // Descanso en el Parking
        descansarEnParking();
        // Del Parking a la Ciudad
        TimeUnit.SECONDS.sleep(5 + random.nextInt(6)); // Entre 5 y 10 segundos
    }

    @Override
    public String toString() {
        return super.toString() + " Camion ";
    }
}
