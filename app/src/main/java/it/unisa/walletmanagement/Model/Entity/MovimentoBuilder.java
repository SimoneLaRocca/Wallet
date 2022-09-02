package it.unisa.walletmanagement.Model.Entity;

import java.util.GregorianCalendar;

public final class MovimentoBuilder {

    private int id; // id < 0: movimento non memorizzato nel db
    private String nome;
    private float importo;
    private int tipo; // 0 = uscita , 1 = entrata
    private GregorianCalendar data;
    private String categoria;

    private MovimentoBuilder(int id) {
        this.id = id;
    }

    public static MovimentoBuilder newBuilder(int id) {
        return new MovimentoBuilder(id);
    }

    public MovimentoBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public MovimentoBuilder importo(float importo) {
        this.importo = importo;
        return this;
    }

    public MovimentoBuilder tipo(int tipo) {
        this.tipo = tipo;
        return this;
    }

    public MovimentoBuilder data(GregorianCalendar data) {
        this.data = data;
        return this;
    }

    public MovimentoBuilder categoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public Movimento build() {
        return new Movimento(id, nome, importo, tipo, data, categoria);
    }
}