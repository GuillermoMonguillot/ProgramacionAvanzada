import java.time.LocalTime;
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

    private final Gasolinera gasolineraIzq;
    private final Gasolinera gasolineraDch;
    private final Puente puenteIzq;
    private final Puente puenteDch;
    private final TramoCronometrado tramo1;
    private final TramoCronometrado tramo2;
    private final TramoCronometrado tramo3;
    private final TramoCronometrado tramo4;

    public Coche(int TANQUE_GASOLINA, int gasolina, int maximo_tanque, int minimo_tanque, Gasolinera gasolineraIzq, Gasolinera gasolineraDch, Puente puenteIzq, Puente puenteDch, TramoCronometrado tramo1, TramoCronometrado tramo2, TramoCronometrado tramo3, TramoCronometrado tramo4) {
        super(generarMatricula(), TANQUE_GASOLINA, gasolina, maximo_tanque, minimo_tanque);
        this.neumatico = seleccionarNeumaticoAlAzar();
        this.gasolineraIzq = gasolineraIzq;
        this.gasolineraDch = gasolineraDch;
        this.puenteIzq = puenteIzq;
        this.puenteDch = puenteDch;
        this.tramo1 = tramo1;
        this.tramo2 = tramo2;
        this.tramo3 = tramo3;
        this.tramo4 = tramo4;
    }

    private static synchronized String generarMatricula() {
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
            matricula = String.format("%04d-%s", id, letras);
        } while (matriculasGeneradas.containsKey(id));
        matriculasGeneradas.put(id, letras);
        return matricula;
    }

    private String seleccionarNeumaticoAlAzar() {
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

        // Cruzar el puente
        if (tramo == 1 || tramo == 2) {
            puenteIzq.cruzarIzquierda(this);
        } else {
            puenteDch.cruzarDerecha(this);
        }

        // Del Puente a la Gasolinera
        TimeUnit.SECONDS.sleep(3 + random.nextInt(3)); // Entre 3 y 5 segundos

        // Repostar en la gasolinera si es necesario
        if (getGasolina() < getTANQUE_GASOLINA() / 2) {
            if (tramo == 1 || tramo == 2) {
                gasolineraIzq.entrarGasolinera(this);
            } else {
                gasolineraDch.entrarGasolinera(this);
            }
        }

        // Llegar al tramo cronometrado
        if (tramo == 1) {
            tramo1.entrarTramo(this);
        } else if (tramo == 2) {
            tramo2.entrarTramo(this);
        } else if (tramo == 3) {
            tramo3.entrarTramo(this);
        } else {
            tramo4.entrarTramo(this);
        }
    }

    private void completarTramo(int tramo) throws InterruptedException {
        System.out.println("Coche " + getMatricula() + " compitiendo en el tramo " + tramo);
        long tiempoTotal = 0;
        for (int i = 1; i <= 4; i++) {
            long tiempoSector = 4000 + new Random().nextInt(7000); // Entre 4 y 10 segundos
            if (!getNeumatico().equals(TramoCronometrado.getClima())) {
                tiempoSector *= 3; // Si las ruedas no son adecuadas, tarda el triple
            }
            System.out.println("Coche " + getMatricula() + " recorriendo sector " + i + " del tramo " + tramo);
            TimeUnit.MILLISECONDS.sleep(tiempoSector);
            tiempoTotal += tiempoSector;

            if (i == 3) {
                // Permitir que otro coche entre al tramo
                // Esta lógica depende de cómo implementes los tramos y puentes
            }
        }

        // Calcular y registrar el tiempo total del tramo
        long tiempoEnSegundos = tiempoTotal / 1000;
        LocalTime horaFinalizacion = LocalTime.now();
        String registro = String.format("Coche: %s, Hora: %s, Tramo: %d, Tiempo: %d segundos",
                getMatricula(), horaFinalizacion, tramo, tiempoEnSegundos);
        System.out.println(registro);
        // Aquí se puede agregar el código para guardar el registro en una estructura interna
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
}
