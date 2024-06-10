/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Martin
 */
public abstract class Vehiculo extends Thread {
    
    private String matricula;
    private int TANQUE_GASOLINA;
    private int gasolina;
    private int maximo_tanque;
    private int minimo_tanque;

    public Vehiculo(String matricula, int TANQUE_GASOLINA, int gasolina, int maximo_tanque, int minimo_tanque) {
        this.matricula = matricula;
        this.TANQUE_GASOLINA = TANQUE_GASOLINA;
        this.gasolina = gasolina;
        this.maximo_tanque = maximo_tanque;
        this.minimo_tanque = minimo_tanque;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getTANQUE_GASOLINA() {
        return TANQUE_GASOLINA;
    }

    public void setTANQUE_GASOLINA(int TANQUE_GASOLINA) {
        this.TANQUE_GASOLINA = TANQUE_GASOLINA;
    }

    public int getGasolina() {
        return gasolina;
    }

    public void setGasolina(int gasolina) {
        this.gasolina = gasolina;
    }

    public int getMaximo_tanque() {
        return maximo_tanque;
    }

    public void setMaximo_tanque(int maximo_tanque) {
        this.maximo_tanque = maximo_tanque;
    }

    public int getMinimo_tanque() {
        return minimo_tanque;
    }

    public void setMinimo_tanque(int minimo_tanque) {
        this.minimo_tanque = minimo_tanque;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "matricula=" + matricula + ", TANQUE_GASOLINA=" + TANQUE_GASOLINA + ", gasolina=" + gasolina + ", maximo_tanque=" + maximo_tanque + ", minimo_tanque=" + minimo_tanque + '}';
    }
    
    
    
    
}
