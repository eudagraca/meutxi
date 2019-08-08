package models;

public class Balanco {

    private String balanco;
    private double receita;
    private double despesa;
    private String mes;

    public Balanco(double receita, double despesa, String mes) {
        this.balanco = String.valueOf(receita - despesa);
        this.receita = receita;
        this.despesa = despesa;
        this.mes = mes;
    }

    public String getBalanco() {
        return balanco;
    }

    public double getReceita() {
        return receita;
    }

    public double getDespesa() {
        return despesa;
    }

    public String getMes() {
        return mes;
    }
}
