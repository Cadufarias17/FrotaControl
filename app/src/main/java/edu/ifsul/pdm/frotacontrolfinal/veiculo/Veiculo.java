package edu.ifsul.pdm.frotacontrolfinal.veiculo;

public class Veiculo {

    // atributos da classe Veiculo
    private int id;
    private String placa;
    private int numLugares;
    private int ocupado;
    private int idSec;

    // métodos da classe Veiculo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getNumLugares() {
        return numLugares;
    }

    public void setNumLugares(int numLugares) {
        this.numLugares = numLugares;
    }

    public int getOcupado() {
        return ocupado;
    }

    public void setOcupado(int ocupado) {
        this.ocupado = ocupado;
    }

    public int getIdSec() {
        return idSec;
    }

    public void setIdSec(int idSec) {
        this.idSec = idSec;
    }

    @Override
    public String toString() {
        return getPlaca();
    }
}
