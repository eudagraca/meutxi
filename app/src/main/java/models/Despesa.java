package models;


public class Despesa extends MonthData {

    private String pago;
    private String tipo;

    @Override
    public String getSituacao() {
        return pago;
    }

    @Override
    public void setSituacao(String pago) {
        this.pago = pago;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
