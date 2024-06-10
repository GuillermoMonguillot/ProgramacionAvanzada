import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Martin
 */
public class Coche extends Vehiculo {

    /*
    private static final String[] TIPOS_NEUMATICOS = {"SOLEADO", "LLUVIA", "NIEVE"};
    private String neumatico;

    public Coche(String matricula, int TANQUE_GASOLINA, int gasolina, int maximo_tanque, int minimo_tanque, String[] neumaticos) {
        super(matricula, TANQUE_GASOLINA, gasolina, maximo_tanque, minimo_tanque);
        this.neumatico = seleccionarNeumaticoAlAzar();
    }

    private String seleccionarNeumaticoAlAzar() {
        Random random = new Random();
        return TIPOS_NEUMATICOS[random.nextInt(TIPOS_NEUMATICOS.length)];
    }

    public String getNeumaticos() {
        return neumatico;
    }

    public void setNeumaticos(String neumatico) {
        this.neumatico = neumatico;
    }

    @Override
    public String toString() {
        return super.toString() + "Coche{" + "neumatico=" + neumatico + '}';
    }
     */
    private static final String LETRAS = "BCDFGHJKLMNPQRSTVWXYZ";
    private static final Map<Integer, String> matriculasGeneradas = new HashMap<>();
    private static final String[] TIPOS_NEUMATICOS = {"SOLEADO", "LLUVIA", "NIEVE"};
    private String neumatico;
    private Random random = new Random();

    public Coche(int TANQUE_GASOLINA, int gasolina, int maximo_tanque, int minimo_tanque) {
        super(generarMatricula(), TANQUE_GASOLINA, gasolina, maximo_tanque, minimo_tanque);
        this.neumatico = seleccionarNeumaticoAlAzar();
    }

    public static synchronized String generarMatricula() {
        Random random = new Random();
        int id;
        String letras;
        String matricula;
        do {
            id = random.nextInt(10000);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                sb.append(LETRAS.charAt(random.nextInt(LETRAS.length())));
            }
            letras = sb.toString();
            matricula = String.format("%04d-%s", id, letras); //Especifica que se debe formatear un número entero (d de "decimal") con al menos 4 dígitos, rellenando con ceros a la izquierda si es necesario.
        } while (matriculasGeneradas.containsKey(id));
        matriculasGeneradas.put(id, letras);
        return matricula;
    }

    public String seleccionarNeumaticoAlAzar() {
        Random random = new Random();
        return TIPOS_NEUMATICOS[random.nextInt(TIPOS_NEUMATICOS.length)];
    }

    public String getNeumatico() {
        return neumatico;
    }

    public void setNeumatico(String neumatico) {
        this.neumatico = neumatico;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 1. Verificaciones técnicas
                realizarVerificacionesTecnicas();

                // 2. Dirigirse al tramo cronometrado
                int tramo = seleccionarTramoInicial();
                dirigirseATramo(tramo);

                // 3. Completar el tramo
                completarTramo(tramo);

                // 4. Regreso al parking y descanso
                regresarAlParking();
                descansarEnParking();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void realizarVerificacionesTecnicas() throws InterruptedException {
        System.out.println("Coche " + getMatricula() + " realizando verificaciones técnicas.");
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
    }

    private int seleccionarTramoInicial() {
        int idNumerico = Integer.parseInt(getMatricula().split("-")[0]);
        if (idNumerico % 2 == 0) {
            // Par: tramo 01 o 02
            return random.nextBoolean() ? 1 : 2;
        } else {
            // Impar: tramo 03 o 04
            return random.nextBoolean() ? 3 : 4;
        }
    }

    private void dirigirseATramo(int tramo) throws InterruptedException {
        System.out.println("Coche " + getMatricula() + " dirigiéndose al tramo " + tramo);
        // Del Parking al Puente
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos

        // Del Puente a la Gasolinera
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos

        // Repostar en la gasolinera si es necesario
        if (getGasolina() < getTANQUE_GASOLINA() / 2) {
            repostarEnGasolinera(tramo);
        }
    }

    private void repostarEnGasolinera(int tramo) {
        System.out.println("Coche " + getMatricula() + " repostando en gasolinera del tramo " + tramo);
        setGasolina(getTANQUE_GASOLINA());
    }

    private void completarTramo(int tramo) throws InterruptedException {
        System.out.println("Coche " + getMatricula() + " compitiendo en el tramo " + tramo);
        TimeUnit.SECONDS.sleep(10); // Simulación de tiempo en el tramo
    }

    private void regresarAlParking() throws InterruptedException {
        System.out.println("Coche " + getMatricula() + " regresando al parking.");
        // Del Tramo al Puente
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
        // Del Puente al Parking
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos
    }

    private void descansarEnParking() throws InterruptedException {
        System.out.println("Coche " + getMatricula() + " descansando en el parking.");
        TimeUnit.SECONDS.sleep(5 + random.nextInt(6)); // Entre 5 y 10 segundos
    }

    @Override
    public String toString() {
        return super.toString() + " Coche{" + "neumatico=" + neumatico + '}';
    }
}
