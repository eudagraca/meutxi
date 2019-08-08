package models;


public class Receita extends MonthData {

    private String recebido;

    private String tipo;

    @Override
    public String getSituacao() {
        return recebido;
    }

    @Override
    public void setSituacao(String recebido) {
        this.recebido = recebido;
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
